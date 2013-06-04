package us.havanki.tamal.level;

import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.level.Level.SpriteSorter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class SpriteSorterTest {
    private Entity e1, e2;
    private SpriteSorter sorter;
    @Before public void setUp() {
        e1 = createMock(Entity.class);
        e2 = createMock(Entity.class);
        sorter = new SpriteSorter();
    }
    private void mockY(Entity e, int y) {
        expect(e.y()).andReturn(y);
        expectLastCall().anyTimes();
        replay(e);
    }
    @Test public void testE1LessThanE2() {
        mockY(e1, 12);
        mockY(e2, 13);
        assertTrue(sorter.compare(e1, e2) < 0);
    }
    @Test public void testE1GreaterThanE2() {
        mockY(e1, 13);
        mockY(e2, 12);
        assertTrue(sorter.compare(e1, e2) > 0);
    }
    @Test public void testE1EqualsE2() {
        mockY(e1, 12);
        mockY(e2, 12);
        assertTrue(sorter.compare(e1, e2) == 0);
    }
}
