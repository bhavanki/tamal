package us.havanki.tamal.gfx;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class ScreenTest {
    private SpriteSheet ss;
    private Screen s;

    @Before public void setUp() throws Exception {
        ss = createMock(SpriteSheet.class);
        s = new Screen(1024, 768, ss);
    }
    @Test public void testGetters() {
        assertEquals(1024, s.w());
        assertEquals(768, s.h());
        assertEquals(ss, s.getSpriteSheet());
    }
    @Test public void testPixels() {
        SimpleColor[] pixels = s.pixels();
        assertEquals(1024 * 768, pixels.length);
    }
    @Test public void testPixel() {
        SimpleColor[] pixels = s.pixels();
        pixels[0] = SimpleColors.RED;
        pixels[1] = SimpleColors.GREEN;
        pixels[1024] = SimpleColors.BLUE;

        assertEquals(SimpleColors.RED, s.pixel(0, 0));
        assertEquals(SimpleColors.GREEN, s.pixel(1, 0));
        assertEquals(SimpleColors.BLUE, s.pixel(0, 1));
    }
    @Test public void testOffsets() {
        assertEquals(0, s.xOffset());
        assertEquals(0, s.yOffset());
        assertSame(s, s.setRenderOffset(512, 256));
        assertEquals(512, s.xOffset());
        assertEquals(256, s.yOffset());
    }
    @Test public void testClear() {
        assertSame(s, s.clear(SimpleColors.BLACK));
        SimpleColor[] pixels = s.pixels();
        for (SimpleColor p : pixels) { assertEquals (SimpleColors.BLACK, p); }
    }

    private static final SimpleColor SPRITE_COLOR_ARRAY[] =
        {SimpleColors.RED, SimpleColors.GREEN, SimpleColors.BLUE,
         SimpleColors.WHITE};
    private static final SimpleColorSet SPRITE_COLORS =
        new SimpleColorSet(SimpleColors.RED, SimpleColors.GREEN,
                           SimpleColors.BLUE, SimpleColors.WHITE);

    private void mockSprite() {
        expect(ss.getWidthInSprites()).andReturn(4);
        expectLastCall().anyTimes();
        expect(ss.getSpriteOffset(2, 1)).andReturn(272);  // whatever

        for (int x = 0; x < SpriteSheet.SPRITE_SIZE; x++) {
            for (int y = 0; y < SpriteSheet.SPRITE_SIZE; y++) {
                byte value = (byte) ((x * SpriteSheet.SPRITE_SIZE + y) % 4);
                expect(ss.valueAt(272, x, y)).andReturn(value);
                // TBD: transparency
            }
        }
    }
    @Test public void testRender_Normal() {
        mockSprite();
        replay(ss);

        assertSame(s, s.render(100, 120, 6, SPRITE_COLORS, 0));
        SimpleColor[] pixels = s.pixels();
        for (int x = 100; x < 100 + SpriteSheet.SPRITE_SIZE; x++) {
            int vx = x - 100;
            for (int y = 120; y < 120 + SpriteSheet.SPRITE_SIZE; y++) {
                int vy = y - 120;
                SimpleColor p = pixels[y * 1024 + x];
                int value = (vx * SpriteSheet.SPRITE_SIZE + vy) % 4;
                assertEquals(SPRITE_COLOR_ARRAY[value], p);
            }
        }
    }
    @Test public void testRender_MirrorX() {
        mockSprite();
        replay(ss);

        assertSame(s, s.render(100, 120, 6, SPRITE_COLORS, Screen.MIRROR_X));
        SimpleColor[] pixels = s.pixels();
        for (int x = 100; x < 100 + SpriteSheet.SPRITE_SIZE; x++) {
            int vx = SpriteSheet.SPRITE_SIZE - 1 - (x - 100);
            for (int y = 120; y < 120 + SpriteSheet.SPRITE_SIZE; y++) {
                int vy = y - 120;
                SimpleColor p = pixels[y * 1024 + x];
                int value = (vx * SpriteSheet.SPRITE_SIZE + vy) % 4;
                assertEquals(SPRITE_COLOR_ARRAY[value], p);
            }
        }
    }
    @Test public void testRender_MirrorY() {
        mockSprite();
        replay(ss);

        assertSame(s, s.render(100, 120, 6, SPRITE_COLORS, Screen.MIRROR_Y));
        SimpleColor[] pixels = s.pixels();
        for (int x = 100; x < 100 + SpriteSheet.SPRITE_SIZE; x++) {
            int vx = x - 100;
            for (int y = 120; y < 120 + SpriteSheet.SPRITE_SIZE; y++) {
                int vy = SpriteSheet.SPRITE_SIZE - 1 - (y - 120);
                SimpleColor p = pixels[y * 1024 + x];
                int value = (vx * SpriteSheet.SPRITE_SIZE + vy) % 4;
                assertEquals(SPRITE_COLOR_ARRAY[value], p);
            }
        }
    }
    @Test public void testRender_OffTopLeft() {
        mockSprite();
        replay(ss);

        s.setRenderOffset(4, 4);
        assertSame(s, s.render(0, 0, 6, SPRITE_COLORS, 0));
        SimpleColor[] pixels = s.pixels();
        for (int x = 0; x < -4 + SpriteSheet.SPRITE_SIZE; x++) {
            int vx = x - (-4);
            for (int y = 0; y < -4 + SpriteSheet.SPRITE_SIZE; y++) {
                int vy = y - (-4);
                SimpleColor p = pixels[y * 1024 + x];
                int value = (vx * SpriteSheet.SPRITE_SIZE + vy) % 4;
                assertEquals(SPRITE_COLOR_ARRAY[value], p);
            }
        }
    }
    @Test public void testRender_OffBottomRight() {
        mockSprite();
        replay(ss);

        s.setRenderOffset(-1020, -764);
        assertSame(s, s.render(0, 0, 6, SPRITE_COLORS, 0));
        SimpleColor[] pixels = s.pixels();
        for (int x = 1020; x < 1024; x++) {
            int vx = x - 1020;
            for (int y = 764; y < 768; y++) {
                int vy = y - 764;
                SimpleColor p = pixels[y * 1024 + x];
                int value = (vx * SpriteSheet.SPRITE_SIZE + vy) % 4;
                assertEquals(SPRITE_COLOR_ARRAY[value], p);
            }
        }
    }
}
