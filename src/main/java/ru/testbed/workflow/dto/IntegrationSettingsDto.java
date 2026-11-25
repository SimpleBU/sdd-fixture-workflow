package ru.testbed.workflow.dto;

/** Сгенерировано из manifest.yaml — не править руками. */
public class IntegrationSettingsDto {

    private String dataSource;
    private String database;
    private String contentType;
    private String metadataVersion;
    private String endpointUrl;
    private Integer timeoutMs;

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getMetadataVersion() { return metadataVersion; }
    public void setMetadataVersion(String metadataVersion) { this.metadataVersion = metadataVersion; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public Integer getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }
}
