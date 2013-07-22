package us.havanki.tamal;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InputKeyTest {

    private InputKey key;

    @Before public void setUp() {
        key = InputKey.UP;
        key.reset();
    }
    @Test public void testInitialState() {
        assertFalse(key.isDown());
        assertFalse(key.isClicked());
    }
    @Test public void testSetDown() {
        key.setDown(true);
        assertTrue(key.isDown());
        key.setDown(false);
        assertFalse(key.isDown());
    }
    @Test public void testToggle() {
        key.toggle(true);
        assertTrue(key.isDown());
        key.toggle(false);
        assertFalse(key.isDown());
    }
    @Test public void testClicking() {
        key.toggle(true);
        assertFalse(key.isClicked());
        key.tick();
        assertTrue(key.isClicked());
        key.tick();
        assertFalse(key.isClicked());
        key.toggle(false);
        assertFalse(key.isClicked());
        key.toggle(true);
        assertFalse(key.isClicked());
        key.tick();
        assertTrue(key.isClicked());
        key.tick();
        assertFalse(key.isClicked());
    }
}
