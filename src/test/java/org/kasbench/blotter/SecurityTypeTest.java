package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityTypeTest {

    @Test
    void testNoArgsConstructor() {
        SecurityType securityType = new SecurityType();
        assertNotNull(securityType);
    }

    @Test
    void testAllArgsConstructor() {
        SecurityType securityType = new SecurityType(1, "CS", "Common Stock", 1);
        assertEquals(1, securityType.getId());
        assertEquals("CS", securityType.getAbbreviation());
        assertEquals("Common Stock", securityType.getDescription());
        assertEquals(1, securityType.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        SecurityType securityType = new SecurityType();
        
        securityType.setId(1);
        securityType.setAbbreviation("PS");
        securityType.setDescription("Preferred Stock");
        securityType.setVersion(1);

        assertEquals(1, securityType.getId());
        assertEquals("PS", securityType.getAbbreviation());
        assertEquals("Preferred Stock", securityType.getDescription());
        assertEquals(1, securityType.getVersion());
    }

    @Test
    void testEquals() {
        SecurityType type1 = new SecurityType(1, "CS", "Common Stock", 1);
        SecurityType type2 = new SecurityType(1, "CS", "Common Stock", 1);
        SecurityType type3 = new SecurityType(2, "PS", "Preferred Stock", 1);

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
        assertNotEquals(type1, null);
        assertNotEquals(type1, new Object());
    }

    @Test
    void testHashCode() {
        SecurityType type1 = new SecurityType(1, "CS", "Common Stock", 1);
        SecurityType type2 = new SecurityType(1, "CS", "Common Stock", 1);

        assertEquals(type1.hashCode(), type2.hashCode());
    }

    @Test
    void testToString() {
        SecurityType type = new SecurityType(1, "CS", "Common Stock", 1);
        String toString = type.toString();
        
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("abbreviation='CS'"));
        assertTrue(toString.contains("description='Common Stock'"));
        assertTrue(toString.contains("version=1"));
    }
} 