package org.kasbench.blotter;

import java.math.BigDecimal;

public class OrderRequestDTO {
    private Integer securityId;
    private Integer blotterId;
    private BigDecimal quantity;
    private Integer orderTypeId;
    private Integer orderStatusId;
    private Integer version;

    public Integer getSecurityId() { return securityId; }
    public void setSecurityId(Integer securityId) { this.securityId = securityId; }

    public Integer getBlotterId() { return blotterId; }
    public void setBlotterId(Integer blotterId) { this.blotterId = blotterId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public Integer getOrderTypeId() { return orderTypeId; }
    public void setOrderTypeId(Integer orderTypeId) { this.orderTypeId = orderTypeId; }

    public Integer getOrderStatusId() { return orderStatusId; }
    public void setOrderStatusId(Integer orderStatusId) { this.orderStatusId = orderStatusId; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
} 