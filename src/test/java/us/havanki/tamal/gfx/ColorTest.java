package us.havanki.tamal.gfx;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColorTest {
    private static int red, green, blue, white;
    private static int colors;

    @BeforeClass public static void setUpClass() {
        int red = Color.get(500);
        int green = Color.get(50);
        int blue = Color.get(5);
        int white = Color.get(555);
        int colors = Color.merge(500,50,5,555);
    }

    @Test public void testGet() {
        assertEquals(36*1 + 6*2 + 3, Color.get(123));
        assertEquals(0, Color.get(0));
        assertEquals(36*5 + 6*5 + 5, Color.get(555));
        assertEquals(255, Color.get(-1));
    }
    @Test public void testIsTransparent() {
        assertTrue(Color.isTransparent(255));
        assertFalse(Color.isTransparent(180));  // red
    }

    @Test public void testMerge() {
        assertEquals((white << 24) + (blue << 16) + (green << 8) + red, colors);
    }

    @Test public void testSeparate() {
        assertEquals(white, Color.separate(colors, 3));
        assertEquals(blue, Color.separate(colors, 2));
        assertEquals(green, Color.separate(colors, 1));
        assertEquals(red, Color.separate(colors, 0));
    }
}
