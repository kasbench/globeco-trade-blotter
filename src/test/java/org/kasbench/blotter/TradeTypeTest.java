package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TradeTypeTest {

    @Test
    void testNoArgsConstructor() {
        TradeType tradeType = new TradeType();
        assertNotNull(tradeType);
    }

    @Test
    void testAllArgsConstructor() {
        TradeType tradeType = new TradeType(1, "buy", "buy", 1);
        assertEquals(1, tradeType.getId());
        assertEquals("buy", tradeType.getAbbreviation());
        assertEquals("buy", tradeType.getDescription());
        assertEquals(1, tradeType.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        TradeType tradeType = new TradeType();
        tradeType.setId(2);
        tradeType.setAbbreviation("sell");
        tradeType.setDescription("sell");
        tradeType.setVersion(1);
        assertEquals(2, tradeType.getId());
        assertEquals("sell", tradeType.getAbbreviation());
        assertEquals("sell", tradeType.getDescription());
        assertEquals(1, tradeType.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        TradeType t1 = new TradeType(1, "buy", "buy", 1);
        TradeType t2 = new TradeType(1, "buy", "buy", 1);
        TradeType t3 = new TradeType(2, "sell", "sell", 1);
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        TradeType tradeType = new TradeType(1, "buy", "buy", 1);
        String str = tradeType.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("abbreviation='buy'"));
        assertTrue(str.contains("description='buy'"));
        assertTrue(str.contains("version=1"));
    }
} 