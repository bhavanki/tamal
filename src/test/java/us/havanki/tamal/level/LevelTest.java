package us.havanki.tamal.level;

import java.util.List;
import java.util.Set;
import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.entity.Player;
import us.havanki.tamal.level.tile.TestTile;
import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.TilePos;
import us.havanki.tamal.level.tile.Tiles;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class LevelTest {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 10;
    private static final int DEPTH = 0;

    private static byte[] tiles;
    private static byte[] data;
    @BeforeClass public static void setUpClass() {
        tiles = new byte[WIDTH*HEIGHT];
        tiles[0] = Tiles.GRASS.getId();
        tiles[1] = Tiles.ROCK.getId();
        tiles[WIDTH] = Tiles.WATER.getId();
        data = new byte[WIDTH*HEIGHT];
        data[0] = (byte) 0x1;
        data[1] = (byte) 0x40;
        data[WIDTH] = (byte) 0xff;
    }

    private Level level;
    @Before public void setUp() {
        level = new Level(WIDTH, HEIGHT, DEPTH, tiles, data);
    }
    @Test public void testGetters() {
        assertEquals(WIDTH, level.w());
        assertEquals(HEIGHT, level.h());
        assertEquals(DEPTH, level.depth());
        assertNotNull(level.env());
        assertNotNull(level.entityLookup());
        assertNull(level.getPlayer());
    }

    @Test public void testGetTile() {
        assertEquals(Tiles.GRASS, level.getTile(0, 0));
        assertEquals(Tiles.ROCK, level.getTile(1, 0));
        assertEquals(Tiles.WATER, level.getTile(0, 1));
    }
    @Test public void testGetTile_RangeCheck() {
        assertEquals(Tiles.ROCK, level.getTile(-1, 0));
        assertEquals(Tiles.ROCK, level.getTile(0, -1));
        assertEquals(Tiles.ROCK, level.getTile(WIDTH, 0));
        assertEquals(Tiles.ROCK, level.getTile(0, HEIGHT));
    }

    @Test public void testSetTile() {
        level.setTile(1, 0, Tiles.GRASS, (byte) 0x20);
        assertEquals(Tiles.GRASS, level.getTile(1, 0));
        assertEquals((byte) 0x20, level.getData(1, 0));
    }
    @Test public void testSetTile_RangeCheck() {
        level.setTile(-1, 0, Tiles.GRASS, (byte) 0x20);
        level.setTile(0, -1, Tiles.GRASS, (byte) 0x20);
        level.setTile(WIDTH, 0, Tiles.GRASS, (byte) 0x20);
        level.setTile(0, HEIGHT, Tiles.GRASS, (byte) 0x20);
    }

    @Test public void testGetData() {
        assertEquals((byte) 0x1, level.getData(0, 0));
        assertEquals((byte) 0x40, level.getData(1, 0));
        assertEquals((byte) 0xff, level.getData(0, 1));
    }
    @Test public void testGetData_RangeCheck() {
        assertEquals((byte) 0, level.getData(-1, 0));
        assertEquals((byte) 0, level.getData(0, -1));
        assertEquals((byte) 0, level.getData(WIDTH, 0));
        assertEquals((byte) 0, level.getData(0, HEIGHT));
    }

    @Test public void testSetData() {
        level.setData(1, 0, 0x20);
        assertEquals((byte) 0x20, level.getData(1, 0));
    }
    @Test public void testSetData_RangeCheck() {
        level.setData(-1, 0, (byte) 0x20);
        level.setData(0, -1, (byte) 0x20);
        level.setData(WIDTH, 0, (byte) 0x20);
        level.setData(0, HEIGHT, (byte) 0x20);
    }

    @Test public void testAdd() {
        Entity e = createMock(Entity.class);
        expect(e.setRemoved(false)).andReturn(e);
        e.addedToLevel(level);
        expect(e.x()).andReturn(33);  // => 2
        expect(e.y()).andReturn(17);  // => 1
        replay(e);

        level.add(e);
        verify(e);

        assertNull(level.getPlayer());
        List<Entity> l = level.entityLookup().getEntitiesInTile(2, 1);
        assertEquals(1, l.size());
        assertEquals(e, l.get(0));
    }
    @Test public void testAddPlayer() {
        Player p = createMock(Player.class);
        expect(p.setRemoved(false)).andReturn(p);
        p.addedToLevel(level);
        expect(p.x()).andReturn(33);  // => 2
        expect(p.y()).andReturn(17);  // => 1
        replay(p);

        level.add(p);
        verify(p);

        assertEquals(p, level.getPlayer());
    }
    @Test public void testRemove() {
        Entity e = createMock(Entity.class);
        expect(e.setRemoved(false)).andReturn(e);
        e.addedToLevel(level);
        expect(e.x()).andReturn(33);  // => 2
        expectLastCall().times(2);
        expect(e.y()).andReturn(17);  // => 1
        expectLastCall().times(2);
        expect(e.remove()).andReturn(e);
        replay(e);

        level.add(e);
        level.remove(e);
        verify(e);

        List<Entity> l = level.entityLookup().getEntitiesInTile(2, 1);
        assertEquals(0, l.size());
    }
    @Test public void testRemovePlayer() {
        Player p = createMock(Player.class);
        expect(p.setRemoved(false)).andReturn(p);
        p.addedToLevel(level);
        expect(p.x()).andReturn(33);  // => 2
        expectLastCall().times(2);
        expect(p.y()).andReturn(17);  // => 1
        expectLastCall().times(2);
        expect(p.remove()).andReturn(p);
        replay(p);

        level.add(p);
        level.remove(p);
        verify(p);

        assertNull(level.getPlayer());
    }

    // ---

    @Test public void testTickTiles() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                level.setTile(i, j, Tiles.TEST, (byte) 0);
            }
        }

        double pct = 0.02;
        level.tickTiles(pct);
        int expectedTickCount = (int) ((WIDTH * HEIGHT) * pct);
        Set<TilePos> tickedTiles = Tiles.TEST.getTickPositions();
        assertEquals(expectedTickCount, tickedTiles.size());
        Set<TilePos> checkedTiles = new java.util.HashSet<TilePos>();
        for (TilePos tp : tickedTiles) {
            for (TilePos cp : checkedTiles) {
                if (cp.xt() == tp.xt() && cp.yt() == tp.yt()) {
                    fail("Found repeated tick");
                }
            }
            checkedTiles.add(tp);
        }
    }

    @Test public void testTickEntities() {
        Entity e1 = createMock(Entity.class);
        expect(e1.setRemoved(false)).andReturn(e1);
        e1.addedToLevel(level);
        expect(e1.x()).andReturn(33);  // => 2
        expectLastCall().anyTimes();
        expect(e1.y()).andReturn(17);  // => 1
        expectLastCall().anyTimes();
        e1.tick();
        expect(e1.isRemoved()).andReturn(false);
        replay(e1);
        Entity e2 = createMock(Entity.class);
        expect(e2.setRemoved(false)).andReturn(e2);
        e2.addedToLevel(level);
        expect(e2.x()).andReturn(33);  // => 2
        expectLastCall().anyTimes();
        expect(e2.y()).andReturn(17);  // => 1
        expectLastCall().anyTimes();
        e2.tick();
        expect(e2.isRemoved()).andReturn(false);
        replay(e2);
        Entity e3 = createMock(Entity.class);
        expect(e3.setRemoved(false)).andReturn(e3);
        e3.addedToLevel(level);
        expect(e3.x()).andReturn(49);  // => 3
        expectLastCall().anyTimes();
        expect(e3.y()).andReturn(1);  // => 0
        expectLastCall().anyTimes();
        e3.tick();
        expect(e3.isRemoved()).andReturn(false);
        replay(e3);

        level.add(e1);
        level.add(e2);
        level.add(e3);
        level.tickEntities();
        verify(e1);
        verify(e2);
        verify(e3);

        List<Entity> entities = level.entityLookup().getAllEntities();
        assertEquals(3, entities.size());
    }
    @Test public void testTickEntities_Removed() {
        Entity e1 = createMock(Entity.class);
        expect(e1.setRemoved(false)).andReturn(e1);
        e1.addedToLevel(level);
        expect(e1.x()).andReturn(33);  // => 2
        expectLastCall().anyTimes();
        expect(e1.y()).andReturn(17);  // => 1
        expectLastCall().anyTimes();
        e1.tick();
        expect(e1.isRemoved()).andReturn(true);
        replay(e1);

        level.add(e1);
        level.tickEntities();
        verify(e1);

        List<Entity> entities = level.entityLookup().getAllEntities();
        assertEquals(0, entities.size());
    }
    @Test public void testTickEntities_Moved() {
        Entity e1 = createMock(Entity.class);
        expect(e1.setRemoved(false)).andReturn(e1);
        e1.addedToLevel(level);
        expect(e1.x()).andReturn(33);  // => 2
        expectLastCall().times(2);
        expect(e1.y()).andReturn(17);  // => 1
        expectLastCall().times(2);
        e1.tick();
        expect(e1.isRemoved()).andReturn(false);
        expect(e1.x()).andReturn(65);  // => 4
        expect(e1.y()).andReturn(49);  // => 3
        replay(e1);

        level.add(e1);
        level.tickEntities();
        verify(e1);

        List<Entity> entities = level.entityLookup().getAllEntities();
        assertEquals(1, entities.size());
        List<Entity> l = level.entityLookup().getEntitiesInTile(2, 1);
        assertEquals(0, l.size());
        l = level.entityLookup().getEntitiesInTile(4, 3);
        assertEquals(1, l.size());
        assertEquals(e1, l.get(0));
    }

    @Test public void testGetEntitiesInRect() {
        int x0 = 0;
        int x1 = 50;
        int y0 = 9;
        int y1 = 50;
        Entity e1 = createMock(Entity.class);
        expect(e1.setRemoved(false)).andReturn(e1);
        e1.addedToLevel(level);
        expect(e1.x()).andReturn(33);  // => 2
        expectLastCall().anyTimes();
        expect(e1.y()).andReturn(17);  // => 1
        expectLastCall().anyTimes();
        expect(e1.intersects(x0, y0, x1, y1)).andReturn(true);
        replay(e1);
        Entity e2 = createMock(Entity.class);
        expect(e2.setRemoved(false)).andReturn(e2);
        e2.addedToLevel(level);
        expect(e2.x()).andReturn(17);  // => 1
        expectLastCall().anyTimes();
        expect(e2.y()).andReturn(33);  // => 2
        expectLastCall().anyTimes();
        expect(e2.intersects(x0, y0, x1, y1)).andReturn(true);
        replay(e2);
        Entity e3 = createMock(Entity.class);
        expect(e3.setRemoved(false)).andReturn(e3);
        e3.addedToLevel(level);
        expect(e3.x()).andReturn(49);  // => 3
        expectLastCall().anyTimes();
        expect(e3.y()).andReturn(1);  // => 0
        expectLastCall().anyTimes();
        expect(e3.intersects(x0, y0, x1, y1)).andReturn(false);
        replay(e3);

        level.add(e1);
        level.add(e2);
        level.add(e3);
        List<Entity> entities = level.getEntitiesInRect(x0, y0, x1, y1);
        verify(e1);
        verify(e2);
        verify(e3);
        assertEquals(2, entities.size());
        assertTrue(entities.contains(e1));
        assertTrue(entities.contains(e2));
    }
}
