package us.havanki.tamal.gfx;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleColorSetTest {
    private static SimpleColor red, green, blue, white;

    @BeforeClass public static void setUpClass() {
        red = SimpleColor.fromHexal(500);
        green = SimpleColor.fromHexal(50);
        blue = SimpleColor.fromHexal(5);
        white = SimpleColor.fromHexal(555);
    }

    @Test public void testIt() {
        SimpleColorSet colors = new SimpleColorSet(red, green, blue, white);
        assertEquals(red, colors.get(0));
        assertEquals(green, colors.get(1));
        assertEquals(blue, colors.get(2));
        assertEquals(white, colors.get(3));
    }

}
