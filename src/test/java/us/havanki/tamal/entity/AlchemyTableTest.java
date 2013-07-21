package us.havanki.tamal.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AlchemyTableTest {
    private AlchemyTable f;
    @Before public void setUp() throws Exception {
        f = new AlchemyTable();
    }
    @Test public void testConstruction() {
        assertEquals("AlchemyTable", f.getName());
        assertEquals(AlchemyTable.S, f.getSpriteNumber());
        assertEquals(AlchemyTable.COLORS, f.getColors());
    }
}
