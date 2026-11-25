package ru.testbed.workflow.dto;

/** Сгенерировано из manifest.yaml — не править руками. */
public class NotificationDto {

    private String recipient;
    private String subject;
    private String body;
    private String messageId;
    private String reasonCode;
    private String template;

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
}
