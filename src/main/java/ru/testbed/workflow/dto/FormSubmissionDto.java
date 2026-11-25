package ru.testbed.workflow.dto;

import java.time.Instant;
import java.util.Map;

/** Сгенерировано из manifest.yaml — не править руками. */
public class FormSubmissionDto {

    private String formId;
    private Map<String, Object> formData;
    private String submittedBy;
    private Instant submittedAt;

    public String getFormId() { return formId; }
    public void setFormId(String formId) { this.formId = formId; }
    public Map<String, Object> getFormData() { return formData; }
    public void setFormData(Map<String, Object> formData) { this.formData = formData; }
    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
}
