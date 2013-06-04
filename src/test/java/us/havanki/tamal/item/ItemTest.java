package us.havanki.tamal.item;

import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemTest {
    private static final SimpleColorSet ITEM_COLORS =
        new SimpleColorSet(SimpleColors.RED, SimpleColors.GREEN,
                           SimpleColors.BLUE, SimpleColors.WHITE);
    private static class TestItem extends Item {
        TestItem(String name) { super(name, 1, ITEM_COLORS);}
    }
    private static class TestItem2 extends Item {
        TestItem2(String name) { super(name, 2, ITEM_COLORS);}
    }

    private Item item;
    @Before public void setUp() throws Exception {
        item = new TestItem("Thinger");
    }
    @Test public void testGetters() {
        assertEquals("Thinger", item.getName());
        assertEquals(1, item.getSprite());
        assertEquals(ITEM_COLORS, item.getColors());
    }
    @Test public void testIsDepleted() {
        assertFalse(item.isDepleted());
    }
    @Test public void testMatches() {
        assertTrue(item.matches(item));
        Item item2 = new TestItem2("Thinger");
        assertFalse(item.matches(item2));
        assertFalse(item2.matches(item));
    }
}
