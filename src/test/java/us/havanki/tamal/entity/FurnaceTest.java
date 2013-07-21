package us.havanki.tamal.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FurnaceTest {
    private Furnace f;
    @Before public void setUp() throws Exception {
        f = new Furnace();
    }
    @Test public void testConstruction() {
        assertEquals("Furnace", f.getName());
        assertEquals(Furnace.S, f.getSpriteNumber());
        assertEquals(Furnace.COLORS, f.getColors());
    }
}
