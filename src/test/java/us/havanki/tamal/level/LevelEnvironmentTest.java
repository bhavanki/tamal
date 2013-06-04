package us.havanki.tamal.level;

import us.havanki.tamal.gfx.SimpleColors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevelEnvironmentTest {
    private LevelEnvironment env;
    @Before public void setUp() throws Exception {
        env = new LevelEnvironment();
    }
    @Test public void testIt() {
        env.setGrassColor(SimpleColors.GREEN);
        env.setDirtColor(SimpleColors.BLUE);
        env.setSandColor(SimpleColors.WHITE);
        assertEquals(SimpleColors.GREEN, env.getGrassColor());
        assertEquals(SimpleColors.BLUE, env.getDirtColor());
        assertEquals(SimpleColors.WHITE, env.getSandColor());
    }
}
