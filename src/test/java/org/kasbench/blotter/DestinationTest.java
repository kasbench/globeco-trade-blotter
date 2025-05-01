package org.kasbench.blotter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DestinationTest {

    @Test
    void testNoArgsConstructor() {
        Destination destination = new Destination();
        assertNotNull(destination);
    }

    @Test
    void testAllArgsConstructor() {
        Destination destination = new Destination(1, "NYSE", "New York Stock Exchange", 1);
        assertEquals(1, destination.getId());
        assertEquals("NYSE", destination.getAbbreviation());
        assertEquals("New York Stock Exchange", destination.getDescription());
        assertEquals(1, destination.getVersion());
    }

    @Test
    void testGettersAndSetters() {
        Destination destination = new Destination();
        destination.setId(2);
        destination.setAbbreviation("NASDAQ");
        destination.setDescription("NASDAQ");
        destination.setVersion(1);
        assertEquals(2, destination.getId());
        assertEquals("NASDAQ", destination.getAbbreviation());
        assertEquals("NASDAQ", destination.getDescription());
        assertEquals(1, destination.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        Destination d1 = new Destination(1, "NYSE", "New York Stock Exchange", 1);
        Destination d2 = new Destination(1, "NYSE", "New York Stock Exchange", 1);
        Destination d3 = new Destination(2, "NASDAQ", "NASDAQ", 1);
        assertEquals(d1, d2);
        assertNotEquals(d1, d3);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testToString() {
        Destination destination = new Destination(1, "NYSE", "New York Stock Exchange", 1);
        String str = destination.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("abbreviation='NYSE'"));
        assertTrue(str.contains("description='New York Stock Exchange'"));
        assertTrue(str.contains("version=1"));
    }
} 