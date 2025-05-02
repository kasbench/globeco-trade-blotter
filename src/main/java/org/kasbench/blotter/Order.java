package org.kasbench.blotter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"order\"", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    private Security security;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blotter_id")
    private Blotter blotter;

    @Column(precision = 18, scale = 8)
    private BigDecimal quantity;

    @Column(name = "order_timestamp", columnDefinition = "timestamptz")
    private OffsetDateTime orderTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_type_id", nullable = false)
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Order() {}

    public Order(Integer id, Security security, Blotter blotter, BigDecimal quantity,
                OffsetDateTime orderTimestamp, OrderType orderType, OrderStatus orderStatus, Integer version) {
        this.id = id;
        this.security = security;
        this.blotter = blotter;
        this.quantity = quantity;
        this.orderTimestamp = orderTimestamp;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public Blotter getBlotter() { return blotter; }
    public void setBlotter(Blotter blotter) { this.blotter = blotter; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public OffsetDateTime getOrderTimestamp() { return orderTimestamp; }
    public void setOrderTimestamp(OffsetDateTime orderTimestamp) { this.orderTimestamp = orderTimestamp; }

    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
               Objects.equals(quantity, order.quantity) &&
               Objects.equals(orderTimestamp, order.orderTimestamp) &&
               Objects.equals(version, order.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, orderTimestamp, version);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", security=" + (security != null ? security.getId() : null) +
                ", blotter=" + (blotter != null ? blotter.getId() : null) +
                ", quantity=" + quantity +
                ", orderTimestamp=" + orderTimestamp +
                ", orderType=" + (orderType != null ? orderType.getId() : null) +
                ", orderStatus=" + (orderStatus != null ? orderStatus.getId() : null) +
                ", version=" + version +
                '}';
    }
} 