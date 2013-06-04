package us.havanki.tamal.level.tile;

import us.havanki.tamal.level.Level;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class TilePosTest {
    private Level level;

    @Before public void setUp() {
        level = createMock(Level.class);
    }
    @Test public void testIt() {
        TilePos tilePos = new TilePos(level, 1, 2);
        assertEquals(level, tilePos.level());
        assertEquals(1, tilePos.xt());
        assertEquals(2, tilePos.yt());
    }
}
