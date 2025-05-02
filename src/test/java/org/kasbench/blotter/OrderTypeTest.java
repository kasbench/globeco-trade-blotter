package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTypeTest {

    @Test
    void testNoArgsConstructor() {
        OrderType orderType = new OrderType();
        assertNotNull(orderType);
    }

    @Test
    void testAllArgsConstructor() {
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        assertEquals(1, orderType.getId());
        assertEquals("MKT", orderType.getAbbreviation());
        assertEquals("Market Order", orderType.getDescription());
        assertEquals(1, orderType.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        OrderType orderType = new OrderType();
        orderType.setId(2);
        orderType.setAbbreviation("LMT");
        orderType.setDescription("Limit Order");
        orderType.setVersion(1);
        assertEquals(2, orderType.getId());
        assertEquals("LMT", orderType.getAbbreviation());
        assertEquals("Limit Order", orderType.getDescription());
        assertEquals(1, orderType.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        OrderType o1 = new OrderType(1, "MKT", "Market Order", 1);
        OrderType o2 = new OrderType(1, "MKT", "Market Order", 1);
        OrderType o3 = new OrderType(2, "LMT", "Limit Order", 1);
        assertEquals(o1, o2);
        assertNotEquals(o1, o3);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testToString() {
        OrderType orderType = new OrderType(1, "MKT", "Market Order", 1);
        String str = orderType.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("abbreviation='MKT'"));
        assertTrue(str.contains("description='Market Order'"));
        assertTrue(str.contains("version=1"));
    }
} 