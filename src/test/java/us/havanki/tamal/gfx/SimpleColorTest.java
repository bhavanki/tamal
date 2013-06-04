package us.havanki.tamal.gfx;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleColorTest {
    private static SimpleColor red, green, blue, white, transparent;

    @BeforeClass public static void setUpClass() {
        red = SimpleColor.fromHexal(500);
        green = SimpleColor.fromHexal(50);
        blue = SimpleColor.fromHexal(5);
        white = SimpleColor.fromHexal(555);
        transparent = SimpleColor.fromHexal(-1);
    }

    @Test public void testGetInstance_Byte() {
        assertSame(red, SimpleColor.getInstance(red.value()));
    }
    @Test public void testGetInstance_Int() {
        assertSame(red, SimpleColor.getInstance(36*5));
        assertSame(green, SimpleColor.getInstance(6 * 5));
        assertSame(blue, SimpleColor.getInstance(5));
        assertSame(white, SimpleColor.getInstance(36*5 + 6*5 + 5));
    }

    @Test public void testFromHexal() {
        assertEquals((byte) (36*1 + 6*2 + 3),
                     SimpleColor.fromHexal(123).value());
        assertEquals(0, SimpleColor.fromHexal(0).value());
        assertEquals((byte) (36*5 + 6*5 + 5),
                     SimpleColor.fromHexal(555).value());
        assertEquals((byte) 0xff, transparent.value());
    }
    @Test public void testIsTransparent() {
        assertTrue(transparent.isTransparent());
        assertFalse(red.isTransparent());
    }
    @Test public void testToHexal() {
        assertEquals(500, red.toHexal());
        assertEquals(50, green.toHexal());
        assertEquals(5, blue.toHexal());
        assertEquals(555, white.toHexal());
        assertEquals(-1, transparent.toHexal());
    }
    @Test public void testToRgb() {
        assertEquals(0xff0000, red.toRgb());
        assertEquals(0x00ff00, green.toRgb());
        assertEquals(0x0000ff, blue.toRgb());
        assertEquals(0xffffff, white.toRgb());
        assertEquals(0x000000, transparent.toRgb());
    }

    @Test public void testAdjust1() {
        SimpleColor color = SimpleColor.fromHexal(123);
        SimpleColor adjusted = color.adjust(1, 2, 3);
        assertEquals(245, adjusted.toHexal());
    }
    @Test public void testAdjust2() {
        SimpleColor color = SimpleColor.fromHexal(432);
        SimpleColor adjusted = color.adjust(-1, -2, -3);
        assertEquals(310, adjusted.toHexal());
    }
    @Test public void testAdjust_Transparent() {
        assertEquals(transparent, transparent.adjust(1, 2, 3));
    }
    @Test public void testBrighter() {
        SimpleColor bColor = SimpleColor.fromHexal(333).brighter();
        assertEquals(444, bColor.toHexal());
    }
    @Test public void testDarker() {
        SimpleColor dColor = SimpleColor.fromHexal(333).darker();
        assertEquals(222, dColor.toHexal());
    }
}
