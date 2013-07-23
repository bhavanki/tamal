package us.havanki.tamal.res;

import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResourceTest {
    private static final SimpleColorSet RESOURCE_COLORS =
        new SimpleColorSet(SimpleColors.RED, SimpleColors.GREEN,
                           SimpleColors.BLUE, SimpleColors.WHITE);

    private Resource r;
    @Before public void setUp() throws Exception {
        r = new Resource("gold", 1, RESOURCE_COLORS);
    }
    @Test public void testGetters() {
        assertEquals("gold", r.getName());
        assertEquals(1, r.getSprite());
        assertEquals(RESOURCE_COLORS, r.getColors());
    }
    @Test(expected=IllegalArgumentException.class) public void testNameLength() {
        new Resource("mercury", 1, RESOURCE_COLORS);
    }
    @Test public void testEquals() {
        Resource r2 = new Resource("gold", 2, RESOURCE_COLORS);
        assertEquals(r, r2);
        Resource r3 = new Resource("pyrite", 1, RESOURCE_COLORS);
        assertFalse(r.equals(r3));
    }
}
