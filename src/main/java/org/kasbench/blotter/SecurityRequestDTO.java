package org.kasbench.blotter;

public class SecurityRequestDTO {
    private String ticker;
    private String description;
    private Integer version;
    private Integer securityTypeId;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getSecurityTypeId() {
        return securityTypeId;
    }

    public void setSecurityTypeId(Integer securityTypeId) {
        this.securityTypeId = securityTypeId;
    }
} 