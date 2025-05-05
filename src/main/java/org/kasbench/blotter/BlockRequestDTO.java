package org.kasbench.blotter;

public class BlockRequestDTO {
    private Integer securityId;
    private Integer orderTypeId;
    private Integer version;

    public Integer getSecurityId() { return securityId; }
    public void setSecurityId(Integer securityId) { this.securityId = securityId; }

    public Integer getOrderTypeId() { return orderTypeId; }
    public void setOrderTypeId(Integer orderTypeId) { this.orderTypeId = orderTypeId; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
} 