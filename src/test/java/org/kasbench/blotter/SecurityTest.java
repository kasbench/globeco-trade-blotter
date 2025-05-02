package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void testNoArgsConstructor() {
        Security security = new Security();
        assertNotNull(security);
    }

    @Test
    void testAllArgsConstructor() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        assertEquals(1, security.getId());
        assertEquals("IBM", security.getTicker());
        assertEquals("IBM Corporation", security.getDescription());
        assertEquals(securityType, security.getSecurityType());
        assertEquals(1, security.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Security security = new Security();
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        security.setId(2);
        security.setTicker("AAPL");
        security.setDescription("Apple Inc.");
        security.setSecurityType(securityType);
        security.setVersion(1);
        assertEquals(2, security.getId());
        assertEquals("AAPL", security.getTicker());
        assertEquals("Apple Inc.", security.getDescription());
        assertEquals(securityType, security.getSecurityType());
        assertEquals(1, security.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security s1 = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Security s2 = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        Security s3 = new Security(2, "AAPL", "Apple Inc.", securityType, 1);
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToString() {
        SecurityType securityType = new SecurityType(1, "STK", "Stock", 1);
        Security security = new Security(1, "IBM", "IBM Corporation", securityType, 1);
        String str = security.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("ticker='IBM'"));
        assertTrue(str.contains("description='IBM Corporation'"));
        assertTrue(str.contains("securityType=1"));
        assertTrue(str.contains("version=1"));
    }
} 