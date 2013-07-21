package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;

import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class FurnitureTest {

    private static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.RED,
                           SimpleColors.GREEN,
                           SimpleColors.BLUE,
                           SimpleColors.BLACK);
    private static class LaZBoy extends Furniture {
        LaZBoy(String name, int sprite, SimpleColorSet colors) {
            super(name, sprite, colors);
        }
    }

    private Furniture f;
    @Before public void setUp() {
        f = new LaZBoy("Rex", 123, COLORS);
    }

    @Test public void testConstruction() {
        assertEquals(0, f.x());
        assertEquals(0, f.y());
        assertEquals(3, f.xr());
        assertEquals(3, f.yr());
        assertEquals("Rex", f.getName());
        assertEquals(123, f.getSpriteNumber());
        assertEquals(COLORS, f.getColors());
    }
    @Test public void testRender() {
        Screen screen = createMock(Screen.class);
        SpriteSheet sheet = createMock(SpriteSheet.class);
        expect(screen.getSpriteSheet()).andReturn(sheet);
        expect(sheet.getWidthInSprites()).andReturn(8);
        replay(sheet);

        int x = 24; int y = 16;
        int ss = SpriteSheet.SPRITE_SIZE;
        int hss = ss / 2;
        expect(screen.render(16, 4, 123, COLORS, 0)).andReturn(screen);
        expect(screen.render(24, 4, 124, COLORS, 0)).andReturn(screen);
        expect(screen.render(16, 12, 131, COLORS, 0)).andReturn(screen);
        expect(screen.render(24, 12, 132, COLORS, 0)).andReturn(screen);
        replay(screen);

        f.place(x, y);
        f.render(screen);
        verify(screen);
        verify(sheet);
    }
    @Test public void testBlocks() {
        Furniture f2 = createMock(Furniture.class);
        assertTrue(f.blocks(f2));
    }
}
