package org.kasbench.blotter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "block_allocation", schema = "public")
public class BlockAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Block block;

    @Column(precision = 18, scale = 8, nullable = false)
    private BigDecimal quantity;

    @Column(name = "filled_quantity", precision = 18, scale = 8, nullable = false)
    private BigDecimal filledQuantity = BigDecimal.ZERO;

    @Version
    @Column(nullable = false)
    private Integer version;

    public BlockAllocation() {}

    public BlockAllocation(Integer id, Order order, Block block, BigDecimal quantity, BigDecimal filledQuantity, Integer version) {
        this.id = id;
        this.order = order;
        this.block = block;
        this.quantity = quantity;
        this.filledQuantity = filledQuantity;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Block getBlock() { return block; }
    public void setBlock(Block block) { this.block = block; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getFilledQuantity() { return filledQuantity; }
    public void setFilledQuantity(BigDecimal filledQuantity) { this.filledQuantity = filledQuantity; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockAllocation that = (BlockAllocation) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(order, that.order) &&
               Objects.equals(block, that.block) &&
               Objects.equals(quantity, that.quantity) &&
               Objects.equals(filledQuantity, that.filledQuantity) &&
               Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, block, quantity, filledQuantity, version);
    }

    @Override
    public String toString() {
        return "BlockAllocation{" +
                "id=" + id +
                ", order=" + (order != null ? order.getId() : null) +
                ", block=" + (block != null ? block.getId() : null) +
                ", quantity=" + quantity +
                ", filledQuantity=" + filledQuantity +
                ", version=" + version +
                '}';
    }
} 