package us.havanki.tamal.level.tile;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TileFactoryTest {
    private TileFactory tf;

    @Before public void setUp() {
        tf = TileFactory.getInstance();
    }
    @Test public void testSingleton() {
        assertSame(tf, TileFactory.getInstance());
    }
    @Test public void testGetTile() {
        assertEquals(Tiles.GRASS, tf.getTile((byte) 0));
        assertEquals(Tiles.ROCK, tf.getTile((byte) 1));
        assertEquals(Tiles.WATER, tf.getTile((byte) 2));
        assertEquals(Tiles.TEST, tf.getTile((byte) -1));
    }
    // @Test public void testGetTile_InvalidId() {
    //     assertNull(tf.getTile(-1));
    //     assertNull(tf.getTile(1000000));
    // }
}
