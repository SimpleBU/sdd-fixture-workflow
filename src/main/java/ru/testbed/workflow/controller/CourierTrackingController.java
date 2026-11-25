package ru.testbed.workflow.controller;

import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.CourierLocationDto;

/** Трекинг курьеров. Ни путь, ни класс не указывают на геоданные. */
@RestController
@RequestMapping("/api/couriers/{courierId}/locations")
public class CourierTrackingController {

    @PostMapping
    public ResponseEntity<Void> reportLocation(
            @PathVariable("courierId") String courierId,
            @RequestBody CourierLocationDto location) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CourierLocationDto>> trackHistory(
            @PathVariable("courierId") String courierId,
            @RequestParam(name = "from", required = false) Instant from,
            @RequestParam(name = "to", required = false) Instant to) {
        return ResponseEntity.ok(List.of());
    }

}
