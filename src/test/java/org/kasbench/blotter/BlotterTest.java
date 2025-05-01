package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlotterTest {

    @Test
    void testNoArgsConstructor() {
        Blotter blotter = new Blotter();
        assertNotNull(blotter);
    }

    @Test
    void testAllArgsConstructor() {
        Blotter blotter = new Blotter(1, "Default", true, 2, 1);
        assertEquals(1, blotter.getId());
        assertEquals("Default", blotter.getName());
        assertTrue(blotter.getAutoPopulate());
        assertEquals(2, blotter.getSecurityTypeId());
        assertEquals(1, blotter.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Blotter blotter = new Blotter();
        blotter.setId(1);
        blotter.setName("Priority");
        blotter.setAutoPopulate(false);
        blotter.setSecurityTypeId(3);
        blotter.setVersion(2);
        assertEquals(1, blotter.getId());
        assertEquals("Priority", blotter.getName());
        assertFalse(blotter.getAutoPopulate());
        assertEquals(3, blotter.getSecurityTypeId());
        assertEquals(2, blotter.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        Blotter b1 = new Blotter(1, "Default", false, null, 1);
        Blotter b2 = new Blotter(1, "Default", false, null, 1);
        Blotter b3 = new Blotter(2, "Other", true, 2, 2);
        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testToString() {
        Blotter blotter = new Blotter(1, "Default", false, null, 1);
        String str = blotter.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("name='Default'"));
        assertTrue(str.contains("autoPopulate=false"));
        assertTrue(str.contains("version=1"));
    }
} 