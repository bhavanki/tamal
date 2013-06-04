package us.havanki.tamal.gfx;

import java.awt.image.BufferedImage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class SpriteSheetTest {

    private BufferedImage image;
    private int[] imagePixels;
    private SpriteSheet ss;

    @Before public void setUp() throws Exception {
        image = createMock(BufferedImage.class);
        expect(image.getWidth()).andReturn(64);
        expect(image.getHeight()).andReturn(128);
        imagePixels = new int[64 * 128];
        imagePixels[0] = 0;
        imagePixels[1] = 64;
        imagePixels[2] = 128;
        imagePixels[3] = 192;
        imagePixels[64] = 192;
        expect(image.getRGB(0, 0, 64, 128, null, 0, 64)).andReturn(imagePixels);
        replay(image);

        ss = new SpriteSheet(image);
    }
    @Test public void testPixelMath() {
        byte[] ssPixels = ss.pixels();
        assertEquals(imagePixels.length, ssPixels.length);
        assertEquals(0, ssPixels[0]);
        assertEquals(1, ssPixels[1]);
        assertEquals(2, ssPixels[2]);
        assertEquals(3, ssPixels[3]);
    }
    @Test public void testDimensions() {
        assertEquals(64, ss.w());
        assertEquals(128, ss.h());

        assertEquals(64 / SpriteSheet.SPRITE_SIZE, ss.getSpriteWidth());
        assertEquals(128 / SpriteSheet.SPRITE_SIZE, ss.getSpriteHeight());
    }
    @Test public void testSpriteOffset() {
        assertEquals(0, ss.getSpriteOffset(0, 0));
        assertEquals(SpriteSheet.SPRITE_SIZE, ss.getSpriteOffset(1, 0));
        assertEquals(64 * SpriteSheet.SPRITE_SIZE, ss.getSpriteOffset(0, 1));
    }
    @Test public void testValueAt() {
        assertEquals(0, ss.valueAt(0, 0, 0));
        assertEquals(1, ss.valueAt(0, 1, 0));
        assertEquals(2, ss.valueAt(0, 2, 0));
        assertEquals(3, ss.valueAt(0, 3, 0));
        assertEquals(3, ss.valueAt(0, 0, 1));
    }
}
