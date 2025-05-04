package org.kasbench.blotter;

import java.math.BigDecimal;

public class TradeRequestDTO {
    private Integer blockId;
    private BigDecimal quantity;
    private Integer tradeTypeId;
    private BigDecimal filledQuantity;
    private Integer version;

    public Integer getBlockId() { return blockId; }
    public void setBlockId(Integer blockId) { this.blockId = blockId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public Integer getTradeTypeId() { return tradeTypeId; }
    public void setTradeTypeId(Integer tradeTypeId) { this.tradeTypeId = tradeTypeId; }

    public BigDecimal getFilledQuantity() { return filledQuantity; }
    public void setFilledQuantity(BigDecimal filledQuantity) { this.filledQuantity = filledQuantity; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
} 