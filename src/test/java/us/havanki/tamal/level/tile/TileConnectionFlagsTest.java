package us.havanki.tamal.level.tile;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TileConnectionFlagsTest {
    private TileConnectionFlags cxnFlags;
    @Before public void setUp() {
        cxnFlags = new TileConnectionFlags();
    }
    @Test public void testIt() {
        assertFalse(cxnFlags.connectsTo(Tiles.GRASS));
        assertFalse(cxnFlags.connectsTo(Tiles.ROCK));

        cxnFlags.connectTo(Tiles.GRASS_ID);
        assertTrue(cxnFlags.connectsTo(Tiles.GRASS));
        assertFalse(cxnFlags.connectsTo(Tiles.ROCK));
    }
}
