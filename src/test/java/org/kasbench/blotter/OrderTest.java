package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testNoArgsConstructor() {
        Order order = new Order();
        assertNotNull(order);
    }

    @Test
    void testAllArgsConstructor() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Blotter blotter = new Blotter(1, "Default", false, null, 1);
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        OrderStatus orderStatus = new OrderStatus(1, "new", "New Order", 1);
        OffsetDateTime timestamp = OffsetDateTime.now();

        Order order = new Order(1, security, blotter, new BigDecimal("100.00"),
                timestamp, orderType, orderStatus, 1);

        assertEquals(1, order.getId());
        assertEquals(security, order.getSecurity());
        assertEquals(blotter, order.getBlotter());
        assertEquals(new BigDecimal("100.00"), order.getQuantity());
        assertEquals(timestamp, order.getOrderTimestamp());
        assertEquals(orderType, order.getOrderType());
        assertEquals(orderStatus, order.getOrderStatus());
        assertEquals(1, order.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Order order = new Order();
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Blotter blotter = new Blotter(1, "Default", false, null, 1);
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        OrderStatus orderStatus = new OrderStatus(1, "new", "New Order", 1);
        OffsetDateTime timestamp = OffsetDateTime.now();

        order.setId(1);
        order.setSecurity(security);
        order.setBlotter(blotter);
        order.setQuantity(new BigDecimal("100.00"));
        order.setOrderTimestamp(timestamp);
        order.setOrderType(orderType);
        order.setOrderStatus(orderStatus);
        order.setVersion(1);

        assertEquals(1, order.getId());
        assertEquals(security, order.getSecurity());
        assertEquals(blotter, order.getBlotter());
        assertEquals(new BigDecimal("100.00"), order.getQuantity());
        assertEquals(timestamp, order.getOrderTimestamp());
        assertEquals(orderType, order.getOrderType());
        assertEquals(orderStatus, order.getOrderStatus());
        assertEquals(1, order.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Blotter blotter = new Blotter(1, "Default", false, null, 1);
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        OrderStatus orderStatus = new OrderStatus(1, "new", "New Order", 1);
        OffsetDateTime timestamp = OffsetDateTime.now();

        Order o1 = new Order(1, security, blotter, new BigDecimal("100.00"),
                timestamp, orderType, orderStatus, 1);
        Order o2 = new Order(1, security, blotter, new BigDecimal("100.00"),
                timestamp, orderType, orderStatus, 1);
        Order o3 = new Order(2, security, blotter, new BigDecimal("200.00"),
                timestamp, orderType, orderStatus, 1);

        assertEquals(o1, o2);
        assertNotEquals(o1, o3);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testToString() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Blotter blotter = new Blotter(1, "Default", false, null, 1);
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        OrderStatus orderStatus = new OrderStatus(1, "new", "New Order", 1);
        OffsetDateTime timestamp = OffsetDateTime.now();

        Order order = new Order(1, security, blotter, new BigDecimal("100.00"),
                timestamp, orderType, orderStatus, 1);

        String str = order.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("security=1"));
        assertTrue(str.contains("blotter=1"));
        assertTrue(str.contains("quantity=100.00"));
        assertTrue(str.contains("orderType=1"));
        assertTrue(str.contains("orderStatus=1"));
        assertTrue(str.contains("version=1"));
    }
} 