package us.havanki.tamal.level;

import java.util.List;
import us.havanki.tamal.entity.Entity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class LevelEntityLookupTest {
    private static int WIDTH = 120;
    private static int HEIGHT = 100;

    private LevelEntityLookup lel;
    private Entity e1, e2, e3;

    @Before public void setUp() throws Exception {
        lel = new LevelEntityLookup(WIDTH, HEIGHT);
        e1 = createMock(Entity.class);
        e2 = createMock(Entity.class);
        e3 = createMock(Entity.class);
    }
    @Test public void testInsert() {
        lel.insert(1, 2, e1);

        List<Entity> l = lel.getEntitiesInTile(1, 2);
        assertEquals(1, l.size());
        assertEquals(e1, l.get(0));

        l = lel.getEntitiesInTile(2, 1);
        assertEquals(0, l.size());
    }
    @Test public void testGetAllEntities() {
        List<Entity> all = lel.getAllEntities();
        assertEquals(0, all.size());

        lel.insert(1, 2, e1);
        lel.insert(1, 2, e2);
        lel.insert(2, 1, e3);
        all = lel.getAllEntities();
        assertEquals(3, all.size());
        assertTrue(all.contains(e1));
        assertTrue(all.contains(e2));
        assertTrue(all.contains(e3));
    }
    @Test public void testInsert_RangeCheck() {
        lel.insert(-1, 2, e1);
        assertEquals(0, lel.getAllEntities().size());

        lel.insert(1, -1, e1);
        assertEquals(0, lel.getAllEntities().size());

        lel.insert(WIDTH, 2, e1);
        assertEquals(0, lel.getAllEntities().size());

        lel.insert(1, HEIGHT, e1);
        assertEquals(0, lel.getAllEntities().size());
    }

    @Test public void testGetEntitiesInTile_RangeCheck() {
        lel.insert(1, 2, e1);

        assertNull(lel.getEntitiesInTile(-1, 2));
        assertNull(lel.getEntitiesInTile(1, -1));
        assertNull(lel.getEntitiesInTile(WIDTH, 2));
        assertNull(lel.getEntitiesInTile(1, HEIGHT));
    }

    @Test public void testRemove() {
        lel.insert(1, 2, e1);
        lel.insert(1, 2, e2);
        lel.insert(2, 1, e3);

        lel.remove(1, 2, e1);
        List<Entity> l = lel.getEntitiesInTile(1, 2);
        assertEquals(1, l.size());
        assertEquals(e2, l.get(0));

        lel.remove(2, 1, e3);
        l = lel.getEntitiesInTile(2, 1);
        assertEquals(0, l.size());

        lel.remove(1, 2, e1);
        l = lel.getEntitiesInTile(1, 2);
        assertEquals(1, l.size());
        assertEquals(e2, l.get(0));
    }
    @Test public void testRemove_RangeCheck() {
        lel.insert(1, 2, e1);

        lel.remove(-1, 2, e1);
        assertEquals(1, lel.getAllEntities().size());

        lel.remove(1, -1, e1);
        assertEquals(1, lel.getAllEntities().size());

        lel.remove(WIDTH, 2, e1);
        assertEquals(1, lel.getAllEntities().size());

        lel.remove(1, HEIGHT, e1);
        assertEquals(1, lel.getAllEntities().size());
    }
}
