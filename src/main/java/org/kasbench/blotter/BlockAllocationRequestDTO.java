package org.kasbench.blotter;

import java.math.BigDecimal;

public class BlockAllocationRequestDTO {
    private Integer orderId;
    private Integer blockId;
    private BigDecimal quantity;
    private BigDecimal filledQuantity;
    private Integer version;

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getBlockId() { return blockId; }
    public void setBlockId(Integer blockId) { this.blockId = blockId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getFilledQuantity() { return filledQuantity; }
    public void setFilledQuantity(BigDecimal filledQuantity) { this.filledQuantity = filledQuantity; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
} 