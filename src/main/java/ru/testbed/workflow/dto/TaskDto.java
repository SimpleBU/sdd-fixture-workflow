package ru.testbed.workflow.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/** Сгенерировано из manifest.yaml — не править руками. */
public class TaskDto {

    private String id;
    private String name;
    private String assignee;
    private List<String> candidateUsers;
    private List<String> candidateGroups;
    private String owner;
    private String createdBy;
    private Instant dueDate;
    private Integer priority;
    private String description;
    private Map<String, Object> variables;
    private String formKey;
    private String processInstanceId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
    public List<String> getCandidateUsers() { return candidateUsers; }
    public void setCandidateUsers(List<String> candidateUsers) { this.candidateUsers = candidateUsers; }
    public List<String> getCandidateGroups() { return candidateGroups; }
    public void setCandidateGroups(List<String> candidateGroups) { this.candidateGroups = candidateGroups; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getVariables() { return variables; }
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    public String getFormKey() { return formKey; }
    public void setFormKey(String formKey) { this.formKey = formKey; }
    public String getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }
}
