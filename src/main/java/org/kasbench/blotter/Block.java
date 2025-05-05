package org.kasbench.blotter;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "block", schema = "public")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Security security;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_type_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderType orderType;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Block() {}

    public Block(Integer id, Security security, OrderType orderType, Integer version) {
        this.id = id;
        this.security = security;
        this.orderType = orderType;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(id, block.id) &&
               Objects.equals(security, block.security) &&
               Objects.equals(orderType, block.orderType) &&
               Objects.equals(version, block.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, security, orderType, version);
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", security=" + (security != null ? security.getId() : null) +
                ", orderType=" + (orderType != null ? orderType.getId() : null) +
                ", version=" + version +
                '}';
    }
} 