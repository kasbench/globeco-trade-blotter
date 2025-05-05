package org.kasbench.blotter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "trade", schema = "public")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Block block;

    @Column(precision = 18, scale = 8, nullable = false)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_type_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destination destination;

    @Column(name = "filled_quantity", precision = 18, scale = 8, nullable = false)
    private BigDecimal filledQuantity = BigDecimal.ZERO;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Trade() {}

    public Trade(Integer id, Block block, BigDecimal quantity, TradeType tradeType, Destination destination, BigDecimal filledQuantity, Integer version) {
        this.id = id;
        this.block = block;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.destination = destination;
        this.filledQuantity = filledQuantity;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

    public Destination getDestination() { return destination; }
    public void setDestination(Destination destination) { this.destination = destination; }

    public BigDecimal getFilledQuantity() { return filledQuantity; }
    public void setFilledQuantity(BigDecimal filledQuantity) { this.filledQuantity = filledQuantity; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id) &&
               Objects.equals(block, trade.block) &&
               Objects.equals(quantity, trade.quantity) &&
               Objects.equals(tradeType, trade.tradeType) &&
               Objects.equals(destination, trade.destination) &&
               Objects.equals(filledQuantity, trade.filledQuantity) &&
               Objects.equals(version, trade.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, block, quantity, tradeType, destination, filledQuantity, version);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", block=" + (block != null ? block.getId() : null) +
                ", quantity=" + quantity +
                ", tradeType=" + (tradeType != null ? tradeType.getId() : null) +
                ", destination=" + (destination != null ? destination.getId() : null) +
                ", filledQuantity=" + filledQuantity +
                ", version=" + version +
                '}';
    }
} 