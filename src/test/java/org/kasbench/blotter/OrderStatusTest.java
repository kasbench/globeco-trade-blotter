package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void testNoArgsConstructor() {
        OrderStatus orderStatus = new OrderStatus();
        assertNotNull(orderStatus);
    }

    @Test
    void testAllArgsConstructor() {
        OrderStatus orderStatus = new OrderStatus(1, "new", "Newly created order", 1);
        assertEquals(1, orderStatus.getId());
        assertEquals("new", orderStatus.getAbbreviation());
        assertEquals("Newly created order", orderStatus.getDescription());
        assertEquals(1, orderStatus.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(2);
        orderStatus.setAbbreviation("open");
        orderStatus.setDescription("Order is pending");
        orderStatus.setVersion(1);
        assertEquals(2, orderStatus.getId());
        assertEquals("open", orderStatus.getAbbreviation());
        assertEquals("Order is pending", orderStatus.getDescription());
        assertEquals(1, orderStatus.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderStatus o1 = new OrderStatus(1, "new", "Newly created order", 1);
        OrderStatus o2 = new OrderStatus(1, "new", "Newly created order", 1);
        OrderStatus o3 = new OrderStatus(2, "open", "Order is pending", 1);
        assertEquals(o1, o2);
        assertNotEquals(o1, o3);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testToString() {
        OrderStatus orderStatus = new OrderStatus(1, "new", "Newly created order", 1);
        String str = orderStatus.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("abbreviation='new'"));
        assertTrue(str.contains("description='Newly created order'"));
        assertTrue(str.contains("version=1"));
    }
} 