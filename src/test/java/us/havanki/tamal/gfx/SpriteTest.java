package us.havanki.tamal.gfx;

import org.junit.Test;
import static org.junit.Assert.*;

public class SpriteTest {
    @Test public void testIt() {
        SimpleColorSet colors =
            new SimpleColorSet(SimpleColors.RED, SimpleColors.GREEN,
                               SimpleColors.BLUE, SimpleColors.WHITE);
        Sprite s = new Sprite(10, 20, 123, colors, 32);

        assertEquals(10, s.x());
        assertEquals(20, s.y());
        assertEquals(123, s.image());
        assertEquals(colors, s.colors());
        assertEquals(32, s.bits());
    }
}
