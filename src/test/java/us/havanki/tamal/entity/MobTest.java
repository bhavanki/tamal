package us.havanki.tamal.entity;

import java.util.List;
import us.havanki.tamal.gfx.Screen;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class MobTest {

    private static class Cat extends Mob {
        Cat(int x, int y, int xr, int yr, Direction dir) {
            super(x, y, xr, yr, dir);
        }
        @Override
        public void render(Screen screen) {}

        List<int[]> move2s = new java.util.ArrayList<int[]>();
        @Override
        protected boolean move2(int xa, int ya) {
            move2s.add(new int[] { xa, ya });
            return true;
        }
    }

    Mob m;
    @Before public void setUp() throws Exception {
        m = new Cat(42, 43, 4, 5, Direction.LEFT);
    }
    @Test public void testConstruction() {
        assertEquals(42, m.x());
        assertEquals(43, m.y());
        assertEquals(4, m.xr());
        assertEquals(5, m.yr());
        assertEquals(Direction.LEFT, m.getDirection());
    }
    @Test public void testDefaults() {
        assertEquals(0, m.getXKnockback());
        assertEquals(0, m.getYKnockback());
        assertEquals(0, m.getWalkDistance());
    }

    @Test public void testMove_Knockback() {
        m.setXKnockback(1);
        m.setYKnockback(2);

        m.move(0, 0);
        m.move(0, 0);
        m.move(0, 0);
        m.move(0, 0);

        List<int[]> knocks = ((Cat) m).move2s;
        assertEquals(3, knocks.size());
        assertArrayEquals(new int[] { 1, 0 }, knocks.get(0));
        assertArrayEquals(new int[] { 0, 1 }, knocks.get(1));
        assertArrayEquals(new int[] { 0, 1 }, knocks.get(2));
    }
    @Test public void testMove_Knockback2() {
        m.setXKnockback(-1);
        m.setYKnockback(-2);

        m.move(0, 0);
        m.move(0, 0);
        m.move(0, 0);
        m.move(0, 0);

        List<int[]> knocks = ((Cat) m).move2s;
        assertEquals(3, knocks.size());
        assertArrayEquals(new int[] { -1, 0 }, knocks.get(0));
        assertArrayEquals(new int[] { 0, -1 }, knocks.get(1));
        assertArrayEquals(new int[] { 0, -1 }, knocks.get(2));
    }

    @Test public void testMove_WalkDistance() {
        m.move(2, 3);
        m.move(3, 2);
        m.move(0, 0);
        m.move(-1, -5);

        assertEquals(3, m.getWalkDistance());
    }
    @Test public void testMove_Direction() {
        m.move(-1, 0);
        assertEquals(Direction.LEFT, m.getDirection());
        m.move(2, 0);
        assertEquals(Direction.RIGHT, m.getDirection());
        m.move(0, -3);
        assertEquals(Direction.UP, m.getDirection());
        m.move(0, 4);
        assertEquals(Direction.DOWN, m.getDirection());
        // don't care about moving in x and y
    }

    @Test public void testBlocks() {
        Entity e1 = createMock(Entity.class);
        expect(e1.isBlockableBy(m)).andReturn(false);
        replay(e1);
        Entity e2 = createMock(Entity.class);
        expect(e2.isBlockableBy(m)).andReturn(true);
        replay(e2);

        assertFalse(m.blocks(e1));
        assertTrue(m.blocks(e2));
    }
}
