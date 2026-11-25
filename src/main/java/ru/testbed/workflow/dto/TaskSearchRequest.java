package ru.testbed.workflow.dto;

import java.time.Instant;

/** Сгенерировано из manifest.yaml — не править руками. */
public class TaskSearchRequest {

    private String assignee;
    private String completedBy;
    private String candidateUserId;
    private String candidateGroupId;
    private String processDefinitionKey;
    private String ownership;
    private Instant createdAfter;

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
    public String getCompletedBy() { return completedBy; }
    public void setCompletedBy(String completedBy) { this.completedBy = completedBy; }
    public String getCandidateUserId() { return candidateUserId; }
    public void setCandidateUserId(String candidateUserId) { this.candidateUserId = candidateUserId; }
    public String getCandidateGroupId() { return candidateGroupId; }
    public void setCandidateGroupId(String candidateGroupId) { this.candidateGroupId = candidateGroupId; }
    public String getProcessDefinitionKey() { return processDefinitionKey; }
    public void setProcessDefinitionKey(String processDefinitionKey) { this.processDefinitionKey = processDefinitionKey; }
    public String getOwnership() { return ownership; }
    public void setOwnership(String ownership) { this.ownership = ownership; }
    public Instant getCreatedAfter() { return createdAfter; }
    public void setCreatedAfter(Instant createdAfter) { this.createdAfter = createdAfter; }
}
