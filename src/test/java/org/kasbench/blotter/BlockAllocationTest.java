package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class BlockAllocationTest {
    @Test
    void testNoArgsConstructor() {
        BlockAllocation alloc = new BlockAllocation();
        assertNotNull(alloc);
    }

    @Test
    void testAllArgsConstructor() {
        Order order = new Order();
        Block block = new Block();
        BigDecimal quantity = new BigDecimal("100.00");
        BigDecimal filled = new BigDecimal("50.00");
        BlockAllocation alloc = new BlockAllocation(1, order, block, quantity, filled, 1);
        assertEquals(1, alloc.getId());
        assertEquals(order, alloc.getOrder());
        assertEquals(block, alloc.getBlock());
        assertEquals(quantity, alloc.getQuantity());
        assertEquals(filled, alloc.getFilledQuantity());
        assertEquals(1, alloc.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        BlockAllocation alloc = new BlockAllocation();
        Order order = new Order();
        Block block = new Block();
        BigDecimal quantity = new BigDecimal("200.00");
        BigDecimal filled = new BigDecimal("100.00");
        alloc.setId(2);
        alloc.setOrder(order);
        alloc.setBlock(block);
        alloc.setQuantity(quantity);
        alloc.setFilledQuantity(filled);
        alloc.setVersion(3);
        assertEquals(2, alloc.getId());
        assertEquals(order, alloc.getOrder());
        assertEquals(block, alloc.getBlock());
        assertEquals(quantity, alloc.getQuantity());
        assertEquals(filled, alloc.getFilledQuantity());
        assertEquals(3, alloc.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        Order order = new Order();
        Block block = new Block();
        BigDecimal quantity = new BigDecimal("100.00");
        BigDecimal filled = new BigDecimal("50.00");
        BlockAllocation a1 = new BlockAllocation(1, order, block, quantity, filled, 1);
        BlockAllocation a2 = new BlockAllocation(1, order, block, quantity, filled, 1);
        BlockAllocation a3 = new BlockAllocation(2, order, block, quantity, filled, 2);
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        Order order = new Order();
        order.setId(10);
        Block block = new Block();
        block.setId(20);
        BigDecimal quantity = new BigDecimal("100.00");
        BigDecimal filled = new BigDecimal("50.00");
        BlockAllocation alloc = new BlockAllocation(1, order, block, quantity, filled, 1);
        String str = alloc.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("order=10"));
        assertTrue(str.contains("block=20"));
        assertTrue(str.contains("quantity=100.00"));
        assertTrue(str.contains("filledQuantity=50.00"));
        assertTrue(str.contains("version=1"));
    }
} 