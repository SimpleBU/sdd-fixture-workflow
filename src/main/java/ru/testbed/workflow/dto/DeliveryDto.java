package ru.testbed.workflow.dto;

/** Сгенерировано из manifest.yaml — не править руками. */
public class DeliveryDto {

    private String id;
    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
    private String coordinates;
    private String courierComment;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }
    public String getCourierComment() { return courierComment; }
    public void setCourierComment(String courierComment) { this.courierComment = courierComment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
