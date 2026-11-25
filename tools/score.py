#!/usr/bin/env python3
"""Сверка отчёта движка SDD с эталоном (expected.json).

  python3 tools/score.py --format route_candidates \
      --pair apps/bank/expected.json results/bank.report.json [--pair ...] \
      [--out results/score.json] [--md results/scorecard.md] \
      [--baseline baseline/score.json] [--max-fp-rate 0.15] [--min-recall 0]

Что считается:
  поля (L1)      — каждое место «endpoint + расположение + путь к полю» из эталона;
  endpoint (L2)  — категории находок с field = null против expect/allow эталона;
  константы (L3) — категории value_findings по символу константы.

Исходы: TP — нашли нужную категорию (или из accept); WRONG — нашли, но не ту категорию;
FN — не нашли; FP — нашли там, где эталон NONE; TN — правильно промолчали.
GRAY — спорные места: не идут в метрики, только в список.
Места, невидимые для формата входа (ответы при route_candidates), идут в отдельный счётчик.
FP-rate = FP / все находки на размеченных местах (как в NFR-4: «доля ложных срабатываний»).
"""
import argparse
import json
import sys
from collections import Counter, defaultdict

ORDER = ["TP", "WRONG", "FN", "FP", "TN"]


def norm_path(p):
    return p.rstrip("/") or "/"


def load(p):
    with open(p, encoding="utf-8") as f:
        return json.load(f)


def match_field(exp_items, f):
    """Ищет место эталона для находки движка по полю."""
    fld = f["field"]
    loc = fld["location"]
    ep = f["endpoint"]
    cands = [e for e in exp_items if e["method"] == ep["method"] and norm_path(e["path"]) == norm_path(ep["path"])]
    if loc in ("param", "header"):
        return next((e for e in cands if e["location"] == "param" and e["name"].lower() == fld["name"].lower()), None)
    jp = fld.get("json_path") or ""
    same_loc = [e for e in cands if e["location"] == loc]
    if loc == "request_body":
        exact = next((e for e in same_loc if e.get("json_path_rc") == jp), None)
        if exact:
            return exact
    # OpenAPI-адаптер может строить путь без имени параметра тела: $.a.b
    best = None
    for e in same_loc:
        rel = e["rel_path"]
        if rel and (jp == f"$.{rel}" or jp.endswith(f".{rel}")):
            if best is None or len(rel) > len(best["rel_path"]):
                best = e
        elif not rel and fld["name"] == e["name"]:
            best = best or e
    return best


def outcome_for(expect, accept, found_cats):
    ok = {expect, *accept}
    if expect == "GRAY":
        return "GRAY"
    if expect == "NONE":
        return "FP" if found_cats else "TN"
    if found_cats & ok:
        return "TP"
    return "WRONG" if found_cats else "FN"


def score_app(exp, rep, fmt):
    rows = []                       # одна строка на размеченное место
    unexpected = []                 # находки, которым нет места в эталоне
    by_place = defaultdict(list)    # id места -> находки
    ep_found = defaultdict(set)     # (method, path) -> категории endpoint-level
    ep_rules = defaultdict(list)
    ep_attrs = {}

    for f in rep.get("findings") or []:
        key = (f["endpoint"]["method"], norm_path(f["endpoint"]["path"]))
        ra = f.get("risk_attributes") or {}
        ep_attrs[key] = (bool(ra.get("is_admin_path")), bool(ra.get("is_bulk_operation")))
        if f.get("field") is None:
            ep_found[key].add(f["category"])
            ep_rules[key] += [(f["category"], m["rule_id"]) for m in f["matched_rules"]]
            continue
        e = match_field(exp["fields"], f)
        if e is None:
            unexpected.append(f)
        else:
            by_place[e["id"]].append(f)

    for e in exp["fields"]:
        fs = by_place.get(e["id"], [])
        cats = {f["category"] for f in fs}
        visible = e["observable"].get(fmt, True)
        oc = outcome_for(e["expect"], e["accept"], cats)
        if not visible and not fs and oc != "GRAY":
            oc = "INVISIBLE"
        rows.append({"level": "field", "id": e["id"], "where": f"{e['method']} {e['path']} [{e['location']}] {e['rel_path'] or e['name']}",
                     "expect": e["expect"], "accept": e["accept"], "kind": e["kind"], "why": e["why"],
                     "found": sorted(cats), "rules": sorted({m["rule_id"] for f in fs for m in f["matched_rules"]}),
                     "outcome": oc, "visible": visible})

    attr_rows = []
    for e in exp["endpoints"]:
        key = (e["method"], norm_path(e["path"]))
        found = ep_found.get(key, set())
        exp_set, allow = set(e["expect"]), set(e["allow"])
        rules_by_cat = defaultdict(set)
        for c, r in ep_rules.get(key, []):
            rules_by_cat[c].add(r)
        for c in sorted(exp_set):
            rows.append({"level": "endpoint", "id": f"{e['id']}#{c}", "where": f"{e['method']} {e['path']}",
                         "expect": c, "accept": [], "kind": "endpoint", "why": e["why"],
                         "found": sorted(found), "rules": sorted(rules_by_cat.get(c, [])),
                         "outcome": "TP" if c in found else "FN", "visible": True})
        for c in sorted(found - exp_set - allow):
            rows.append({"level": "endpoint", "id": f"{e['id']}#!{c}", "where": f"{e['method']} {e['path']}",
                         "expect": "NONE", "accept": [], "kind": "endpoint", "why": e["why"],
                         "found": [c], "rules": sorted(rules_by_cat.get(c, [])), "outcome": "FP", "visible": True})
        if not exp_set and not (found - allow):
            rows.append({"level": "endpoint", "id": f"{e['id']}#none", "where": f"{e['method']} {e['path']}",
                         "expect": "NONE", "accept": [], "kind": "endpoint", "why": e["why"],
                         "found": [], "rules": [], "outcome": "TN", "visible": True})
        want = e["attrs"]
        got = ep_attrs.get(key)
        if got is None and any(want.values()):
            attr_rows.append({"where": f"{e['method']} {e['path']}", "status": "нет находок — признаки не видны",
                              "want": want})
        elif got is not None:
            for name, g in zip(("is_admin_path", "is_bulk_operation"), got):
                if g != want[name]:
                    attr_rows.append({"where": f"{e['method']} {e['path']}", "status": f"{name}: ждали {want[name]}, получили {g}",
                                      "want": want})

    vf_by_sym = defaultdict(list)
    for v in rep.get("value_findings") or []:
        src = v["source"]                      # const:<file>:<symbol>
        _, rest = src.split(":", 1)
        file, sym = rest.rsplit(":", 1)
        vf_by_sym[(file, sym)].append(v)
    known = {(c["file_path"], c["symbol"]) for c in exp["constants"]}
    for k, vs in vf_by_sym.items():
        if k not in known:
            unexpected += vs
    for c in exp["constants"]:
        vs = vf_by_sym.get((c["file_path"], c["symbol"]), [])
        cats = {v["category"] for v in vs}
        oc = outcome_for(c["expect"], c["accept"], cats)
        rows.append({"level": "constant", "id": c["id"], "where": f"{c['symbol']} = {c['value_preview']}",
                     "expect": c["expect"], "accept": c["accept"], "kind": c["kind"], "why": c["why"],
                     "found": sorted(cats), "rules": sorted({m["rule_id"] for v in vs for m in v["matched_rules"]}),
                     "outcome": oc, "visible": True})
        extra = cats - {c["expect"], *c["accept"]} if c["expect"] not in ("NONE", "GRAY") and oc == "TP" else set()
        for x in sorted(extra):
            rows.append({"level": "constant", "id": f"{c['id']}#!{x}", "where": f"{c['symbol']} (лишняя категория)",
                         "expect": "NONE", "accept": [], "kind": "plain", "why": "лишняя категория у верной находки",
                         "found": [x], "rules": sorted({m["rule_id"] for v in vs if v["category"] == x for m in v["matched_rules"]}),
                         "outcome": "FP", "visible": True})
    return rows, attr_rows, unexpected


def metrics(rows):
    c = Counter(r["outcome"] for r in rows)
    tp, fp, fn, wr = c["TP"], c["FP"], c["FN"], c["WRONG"]
    found = tp + fp + wr
    return {
        **{k: c[k] for k in ORDER + ["GRAY", "INVISIBLE"]},
        "precision": round(tp / found, 3) if found else None,
        "recall": round(tp / (tp + fn + wr), 3) if (tp + fn + wr) else None,
        "fp_rate": round((fp + wr) / found, 3) if found else None,
    }


def pct(x):
    return "—" if x is None else f"{x * 100:.0f}%"


def md_table(title, groups):
    out = [f"### {title}", "", "| | TP | WRONG | FN | FP | TN | вне формата | точность | полнота | доля ложных |",
           "|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|"]
    for name, m in groups:
        out.append(f"| {name} | {m['TP']} | {m['WRONG']} | {m['FN']} | {m['FP']} | {m['TN']} | {m['INVISIBLE']} | "
                   f"{pct(m['precision'])} | {pct(m['recall'])} | {pct(m['fp_rate'])} |")
    return out + [""]


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--pair", nargs=2, action="append", required=True, metavar=("EXPECTED", "REPORT"))
    ap.add_argument("--format", default="route_candidates", choices=["route_candidates", "openapi"])
    ap.add_argument("--out")
    ap.add_argument("--md")
    ap.add_argument("--baseline")
    ap.add_argument("--max-fp-rate", type=float)
    ap.add_argument("--min-recall", type=float)
    a = ap.parse_args()

    all_rows, per_app, attrs_all, unexpected_all = [], [], [], []
    rules_meta = {}
    for exp_p, rep_p in a.pair:
        exp, rep = load(exp_p), load(rep_p)
        rows, attrs, unexpected = score_app(exp, rep, a.format)
        for r in rows:
            r["app"] = exp["app"]
        all_rows += rows
        per_app.append((exp["app"], metrics([r for r in rows if r["outcome"] != "GRAY"])))
        attrs_all += [{**x, "app": exp["app"]} for x in attrs]
        unexpected_all += [(exp["app"], u) for u in unexpected]
        rules_meta = {"engine": rep.get("engine_version"), "rules": rep.get("rules_version")}

    total = metrics(all_rows)
    by_level = [(lvl, metrics([r for r in all_rows if r["level"] == lvl])) for lvl in ("field", "endpoint", "constant")]
    cats = sorted({r["expect"] for r in all_rows if r["expect"] not in ("NONE", "GRAY")})
    by_cat = [(c, metrics([r for r in all_rows if r["expect"] == c or (r["expect"] == "NONE" and c in r["found"])]))
              for c in cats]
    by_kind = [(k, metrics([r for r in all_rows if r["kind"] == k])) for k in ("positive", "hard", "trap", "plain", "endpoint")]
    rule_stat = defaultdict(Counter)
    for r in all_rows:
        for rid in r["rules"]:
            rule_stat[rid][r["outcome"]] += 1

    result = {"engine": rules_meta, "format": a.format, "total": total,
              "by_app": dict(per_app), "by_level": dict(by_level), "by_category": dict(by_cat),
              "by_kind": dict(by_kind), "by_rule": {k: dict(v) for k, v in sorted(rule_stat.items())},
              "items": {r["id"]: r["outcome"] for r in all_rows},
              "attr_mismatches": attrs_all,
              "unexpected": [{"app": app, "source": u.get("source") or f"{u['endpoint']['method']} {u['endpoint']['path']} {u['field']['json_path'] or u['field']['name']}",
                              "category": u["category"]} for app, u in unexpected_all]}

    diff = []
    if a.baseline:
        base = load(a.baseline)["items"]
        for k, v in result["items"].items():
            if k in base and base[k] != v:
                diff.append((k, base[k], v))
        for k in base:
            if k not in result["items"]:
                diff.append((k, base[k], "исчез"))
        for k in result["items"]:
            if k not in base:
                diff.append((k, "новый", result["items"][k]))
        result["baseline_diff"] = [{"id": k, "was": w, "now": n} for k, w, n in diff]

    if a.out:
        with open(a.out, "w", encoding="utf-8") as f:
            json.dump(result, f, ensure_ascii=False, indent=2)

    md = [f"# SDD testbed — результат прогона", "",
          f"Движок `{rules_meta.get('engine')}`, набор правил `{rules_meta.get('rules')}`, вход: `{a.format}`.", "",
          f"**Итого:** точность {pct(total['precision'])}, полнота {pct(total['recall'])}, "
          f"доля ложных {pct(total['fp_rate'])} (TP {total['TP']}, WRONG {total['WRONG']}, FN {total['FN']}, "
          f"FP {total['FP']}, TN {total['TN']}; спорных GRAY {total['GRAY']}; невидимых для формата {total['INVISIBLE']}).", ""]
    md += md_table("По приложениям", per_app)
    md += md_table("По уровням", by_level)
    md += md_table("По категориям", by_cat)
    md += md_table("По типу примера (positive — очевидные, hard — неочевидные имена, trap — ловушки, plain — обычные поля, endpoint — уровень L2)", by_kind)
    md += ["### Правила", "", "| правило | TP | WRONG | FP | GRAY |", "|---|---:|---:|---:|---:|"]
    for rid, cnt in sorted(rule_stat.items(), key=lambda x: -(x[1]["FP"] + x[1]["WRONG"])):
        md.append(f"| `{rid}` | {cnt['TP']} | {cnt['WRONG']} | {cnt['FP']} | {cnt['GRAY']} |")
    md.append("")
    for title, oc in (("Ложные срабатывания (FP)", "FP"), ("Не та категория (WRONG)", "WRONG"), ("Пропуски (FN)", "FN")):
        items = [r for r in all_rows if r["outcome"] == oc]
        md += [f"### {title}: {len(items)}", "", "| приложение | уровень | где | ждали | нашли | правила | почему |",
               "|---|---|---|---|---|---|---|"]
        for r in items:
            exp_s = r["expect"] + ("|" + "|".join(r["accept"]) if r["accept"] else "")
            md.append(f"| {r['app']} | {r['level']} | `{r['where']}` | {exp_s} | {', '.join(r['found']) or '—'} | "
                      f"{', '.join(r['rules']) or '—'} | {r['why']} |")
        md.append("")
    if attrs_all:
        md += [f"### Признаки риска endpoint (is_admin_path / is_bulk_operation): {len(attrs_all)} расхождений", ""]
        md += [f"- {x['app']}: `{x['where']}` — {x['status']}" for x in attrs_all] + [""]
    if unexpected_all:
        md += [f"### Находки вне эталона: {len(unexpected_all)}", "",
               "Движок нашёл поле или константу, которых нет в разметке. Обычно это значит, что адаптер "
               "входа строит пути иначе, чем ожидалось; проверьте и дополните manifest.yaml.", ""]
        md += [f"- {x['app']}: {x['source']} → {x['category']}" for x in result["unexpected"]] + [""]
    if a.baseline:
        md += [f"### Изменения относительно эталонного прогона: {len(diff)}", ""]
        md += [f"- `{k}`: {w} → {n}" for k, w, n in diff] or ["Изменений нет."]
        md.append("")
    if a.md:
        with open(a.md, "w", encoding="utf-8") as f:
            f.write("\n".join(md) + "\n")

    print(f"точность {pct(total['precision'])} | полнота {pct(total['recall'])} | доля ложных {pct(total['fp_rate'])} "
          f"| TP {total['TP']} WRONG {total['WRONG']} FN {total['FN']} FP {total['FP']} TN {total['TN']} "
          f"| вне формата {total['INVISIBLE']} | GRAY {total['GRAY']}")
    for app, m in per_app:
        print(f"  {app:10s} точность {pct(m['precision']):>4s}  полнота {pct(m['recall']):>4s}  доля ложных {pct(m['fp_rate']):>4s}")
    if a.baseline:
        print(f"изменений относительно эталонного прогона: {len(diff)}")

    rc = 0
    if a.max_fp_rate is not None and total["fp_rate"] is not None and total["fp_rate"] > a.max_fp_rate:
        print(f"ПОРОГ: доля ложных {pct(total['fp_rate'])} > {pct(a.max_fp_rate)}", file=sys.stderr)
        rc = 1
    if a.min_recall is not None and total["recall"] is not None and total["recall"] < a.min_recall:
        print(f"ПОРОГ: полнота {pct(total['recall'])} < {pct(a.min_recall)}", file=sys.stderr)
        rc = 1
    if a.baseline and diff:
        rc = rc or 2
    sys.exit(rc)


if __name__ == "__main__":
    main()
