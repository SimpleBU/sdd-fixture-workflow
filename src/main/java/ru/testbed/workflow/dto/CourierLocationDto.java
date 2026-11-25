package ru.testbed.workflow.dto;

import java.math.BigDecimal;
import java.time.Instant;

/** Сгенерировано из manifest.yaml — не править руками. */
public class CourierLocationDto {

    private String courierId;
    private BigDecimal lat;
    private BigDecimal lng;
    private BigDecimal accuracy;
    private String deviceId;
    private String imei;
    private Integer batteryLevel;
    private Instant recordedAt;

    public String getCourierId() { return courierId; }
    public void setCourierId(String courierId) { this.courierId = courierId; }
    public BigDecimal getLat() { return lat; }
    public void setLat(BigDecimal lat) { this.lat = lat; }
    public BigDecimal getLng() { return lng; }
    public void setLng(BigDecimal lng) { this.lng = lng; }
    public BigDecimal getAccuracy() { return accuracy; }
    public void setAccuracy(BigDecimal accuracy) { this.accuracy = accuracy; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
