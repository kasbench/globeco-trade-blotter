package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TradeTest {
    @Test
    void testNoArgsConstructor() {
        Trade trade = new Trade();
        assertNotNull(trade);
    }

    @Test
    void testAllArgsConstructor() {
        Block block = new Block();
        TradeType tradeType = new TradeType();
        Trade trade = new Trade(1, block, new BigDecimal("100.00"), tradeType, null, new BigDecimal("50.00"), 1);
        assertEquals(1, trade.getId());
        assertEquals(block, trade.getBlock());
        assertEquals(new BigDecimal("100.00"), trade.getQuantity());
        assertEquals(tradeType, trade.getTradeType());
        assertEquals(new BigDecimal("50.00"), trade.getFilledQuantity());
        assertEquals(1, trade.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Trade trade = new Trade();
        Block block = new Block();
        TradeType tradeType = new TradeType();
        trade.setId(2);
        trade.setBlock(block);
        trade.setQuantity(new BigDecimal("200.00"));
        trade.setTradeType(tradeType);
        trade.setFilledQuantity(new BigDecimal("100.00"));
        trade.setVersion(3);
        assertEquals(2, trade.getId());
        assertEquals(block, trade.getBlock());
        assertEquals(new BigDecimal("200.00"), trade.getQuantity());
        assertEquals(tradeType, trade.getTradeType());
        assertEquals(new BigDecimal("100.00"), trade.getFilledQuantity());
        assertEquals(3, trade.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        Block block = new Block();
        TradeType tradeType = new TradeType();
        Trade t1 = new Trade(1, block, new BigDecimal("100.00"), tradeType, null, new BigDecimal("50.00"), 1);
        Trade t2 = new Trade(1, block, new BigDecimal("100.00"), tradeType, null, new BigDecimal("50.00"), 1);
        Trade t3 = new Trade(2, block, new BigDecimal("200.00"), tradeType, null, new BigDecimal("100.00"), 2);
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        Block block = new Block();
        block.setId(10);
        TradeType tradeType = new TradeType();
        tradeType.setId(20);
        Trade trade = new Trade(1, block, new BigDecimal("100.00"), tradeType, null, new BigDecimal("50.00"), 1);
        String str = trade.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("block=10"));
        assertTrue(str.contains("quantity=100.00"));
        assertTrue(str.contains("tradeType=20"));
        assertTrue(str.contains("filledQuantity=50.00"));
        assertTrue(str.contains("version=1"));
    }
} 