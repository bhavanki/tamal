package us.havanki.tamal.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChestTest {
    private Chest f;
    @Before public void setUp() throws Exception {
        f = new Chest();
    }
    @Test public void testConstruction() {
        assertEquals("Chest", f.getName());
        assertEquals(Chest.S, f.getSpriteNumber());
        assertEquals(Chest.COLORS, f.getColors());
    }
}
