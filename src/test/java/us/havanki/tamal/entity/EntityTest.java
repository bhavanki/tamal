package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.level.Level;
import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.TilePos;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class EntityTest {
    private static class TestEntity extends Entity {
        TestEntity(int x, int y, int xr, int yr) { super (x, y, xr, yr); }
        @Override public void render(Screen s) {}
        @Override public boolean move2(int xa, int ya) {
            return super.move2(xa, ya);
        }
    }
    private Entity e;
    @Before public void setUp() throws Exception {
        e = new TestEntity(10, 20, 6, 8);
    }
    @Test public void testGetters() {
        assertEquals(10, e.x());
        assertEquals(20, e.y());
        assertEquals(6, e.xr());
        assertEquals(8, e.yr());
    }
    @Test public void testPlace() {
        e.place(20, 40);
        assertEquals(20, e.x());
        assertEquals(40, e.y());
    }
    @Test public void testAddedToLevel() {
        Level l = createMock(Level.class);
        e.addedToLevel(l);
        assertEquals(l, e.level());
    }
    @Test public void testRemoval() {
        assertFalse(e.isRemoved());
        assertSame(e, e.remove());
        assertTrue(e.isRemoved());
        assertSame(e, e.setRemoved(false));
        assertFalse(e.isRemoved());
    }

    @Test public void testIntersection() {
        /*   4     16
         *   +-----+ 12
         *   |     |
         *   |     |
         *   |     |
         *   +-----+ 28
         */
        // x0, y0, x1, y1
        assertTrue(e.intersects(8, 18, 12, 22));  // inside
        assertTrue(e.intersects(0, 0, 4, 12));  // touch top left
        assertTrue(e.intersects(0, 0, 4, 30));  // touch left side
        assertTrue(e.intersects(0, 28, 4, 30));  // touch bottom left
        assertTrue(e.intersects(0, 28, 20, 30));  // touch bottom side
        assertTrue(e.intersects(16, 28, 20, 30));  // touch bottom right
        assertTrue(e.intersects(16, 0, 20, 30));  // touch right side
        assertTrue(e.intersects(16, 0, 20, 12));  // touch top right
        assertTrue(e.intersects(0, 0, 20, 12));  // touch top side

        assertFalse("tl", e.intersects(0, 0, 3, 11));  // top left
        assertFalse("l", e.intersects(0, 14, 3, 26));  // left side
        assertFalse("bl", e.intersects(0, 29, 3, 30));  // bottom left
        assertFalse("b", e.intersects(5, 29, 15, 30));  // bottom side
        assertFalse("br", e.intersects(17, 29, 20, 30));  // bottom right
        assertFalse("r", e.intersects(17, 14, 20, 26));  // right side
        assertFalse("tr", e.intersects(17, 0, 20, 11));  // top right

        assertFalse("tw", e.intersects(0, 0, 30, 11));  // top, wide
        assertFalse("rw", e.intersects(17, 0, 20, 30));  // right, wide
    }

    @Test public void testBlocks() {
        assertFalse(e.blocks(new TestEntity(10, 20, 4, 6)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMove2_BothNonZero() {
        e.move2(2, 4);
    }
    @Test public void testMove2_X_NoBlocking() {
        testMove2_X(false);
    }
    @Test public void testMove2_X_Blocking() {
        testMove2_X(true);
    }
    private void testMove2_X(boolean blocking) {
        Level l = createMock(Level.class);
        e.addedToLevel(l);
        Tile t = createMock(Tile.class);
        expect(l.getTile(anyInt(), anyInt())).andReturn(t);
        expectLastCall().anyTimes();
        replay(l);
        expect(t.mayPass(anyObject(TilePos.class), eq(e))).andReturn(!blocking);
        expectLastCall().anyTimes();
        replay(t);
        int oldx = e.x();
        int oldy = e.y();
        assertEquals(!blocking, e.move2(48, 0));  // 3 tiles right
        if (!blocking) {
            assertEquals(oldx + 48, e.x());
            assertEquals(oldy, e.y());
        }
    }
    @Test public void testMove2_Y_NoBlocking() {
        testMove2_Y(false);
    }
    @Test public void testMove2_Y_Blocking() {
        testMove2_Y(true);
    }
    private void testMove2_Y(boolean blocking) {
        Level l = createMock(Level.class);
        e.addedToLevel(l);
        Tile t = createMock(Tile.class);
        expect(l.getTile(anyInt(), anyInt())).andReturn(t);
        expectLastCall().anyTimes();
        replay(l);
        expect(t.mayPass(anyObject(TilePos.class), eq(e))).andReturn(!blocking);
        expectLastCall().anyTimes();
        replay(t);
        int oldx = e.x();
        int oldy = e.y();
        assertEquals(!blocking, e.move2(0, 48));  // 3 tiles down
        if (!blocking) {
            assertEquals(oldx, e.x());
            assertEquals(oldy + 48, e.y());
        }
    }

    @Test public void testMove_Nowhere() {
        assertTrue(e.move(0, 0));
    }
}
