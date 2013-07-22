package us.havanki.tamal;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumSet;
import java.util.List;

/**
 * A key listener for the game.
 */
public class InputHandler implements KeyListener {

    /**
     * Creates a new input handler.
     *
     * @param game game
     */
    public InputHandler (Game game) {
        game.addKeyListener (this);
    }
    /**
     * Creates a new input handler.
     *
     * @param canvas canvas
     */
    public InputHandler(Canvas canvas) {
        canvas.addKeyListener(this);
    }

    /**
     * Releases all input keys.
     */
    public void releaseAll() {
        for (InputKey key : EnumSet.allOf (InputKey.class)) {
            key.setDown (false);
        }
    }

    /**
     * Sends a tick to every key.
     */
    public void tick() {
        for (InputKey key : EnumSet.allOf (InputKey.class)) {
            key.tick();
        }
    }

    public void keyTyped(KeyEvent ke) {}
    public void keyPressed (KeyEvent ke) {
        toggle (ke, true);
    }
    public void keyReleased(KeyEvent ke) {
        toggle (ke, false);
    }

    private void toggle (KeyEvent ke, boolean pressed) {
        InputKey key = null;  // ??????? check this
        switch (ke.getKeyCode()) {
        case KeyEvent.VK_NUMPAD8:
        case KeyEvent.VK_W:
        case KeyEvent.VK_UP:
            key = InputKey.UP; break;
        case KeyEvent.VK_NUMPAD2:
        case KeyEvent.VK_S:
        case KeyEvent.VK_DOWN:
            key = InputKey.DOWN; break;
        case KeyEvent.VK_NUMPAD4:
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
            key = InputKey.LEFT; break;
        case KeyEvent.VK_NUMPAD6:
        case KeyEvent.VK_D:
        case KeyEvent.VK_RIGHT:
            key = InputKey.RIGHT; break;
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_ALT:
        case KeyEvent.VK_ALT_GRAPH:
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_X:
            key = InputKey.MENU; break;
        case KeyEvent.VK_NUMPAD0:
        case KeyEvent.VK_SPACE:
        case KeyEvent.VK_CONTROL:
        case KeyEvent.VK_INSERT:
        case KeyEvent.VK_C:
            key = InputKey.ATTACK; break;
        }

        if (key == null) {
            throw new IllegalArgumentException("Unsupported key code " +
                                               ke.getKeyCode());
        }
        key.toggle (pressed);
    }
}
