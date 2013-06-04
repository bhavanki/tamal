package us.havanki.tamal.level.tile;

import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.level.Level;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class TileTest {
    private Tile tile;
    @Before public void setUp() {
        tile = new Tile(1);
    }
    @Test public void testId() {
        assertEquals((byte) 1, tile.getId());
    }
    @Test public void testMayPass() {
        Level l = createMock(Level.class);
        Entity e = createMock(Entity.class);
        assertTrue(tile.mayPass(new TilePos(l, 1, 2), e));
    }
    @Test public void testTickCount() {
        assertEquals(0, Tiles.TEST.getTheTickCount());
        Tile.incrementTickCount();
        assertEquals(1, Tiles.TEST.getTheTickCount());
    }
}
