package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    @Test
    void testNoArgsConstructor() {
        Block block = new Block();
        assertNotNull(block);
    }

    @Test
    void testAllArgsConstructor() {
        Security security = new Security();
        OrderType orderType = new OrderType();
        Block block = new Block(1, security, orderType, 1);
        assertEquals(1, block.getId());
        assertEquals(security, block.getSecurity());
        assertEquals(orderType, block.getOrderType());
        assertEquals(1, block.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Block block = new Block();
        Security security = new Security();
        OrderType orderType = new OrderType();
        block.setId(2);
        block.setSecurity(security);
        block.setOrderType(orderType);
        block.setVersion(3);
        assertEquals(2, block.getId());
        assertEquals(security, block.getSecurity());
        assertEquals(orderType, block.getOrderType());
        assertEquals(3, block.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        Security security = new Security();
        OrderType orderType = new OrderType();
        Block b1 = new Block(1, security, orderType, 1);
        Block b2 = new Block(1, security, orderType, 1);
        Block b3 = new Block(2, security, orderType, 2);
        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testToString() {
        Security security = new Security();
        security.setId(10);
        OrderType orderType = new OrderType();
        orderType.setId(20);
        Block block = new Block(1, security, orderType, 1);
        String str = block.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("security=10"));
        assertTrue(str.contains("orderType=20"));
        assertTrue(str.contains("version=1"));
    }
} 