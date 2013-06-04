package us.havanki.tamal;

/**
 * A logical input key.
 */
public enum InputKey {
    UP, DOWN, LEFT, RIGHT, ATTACK, MENU;

    private int presses, absorbs;
    private boolean down, clicked;

    /**
     * Gets the down state of the key.
     *
     * @return down state
     */
    public boolean isDown() { return down; }
    /**
     * Sets the down state of the key.
     *
     * @param down down state
     */
    public void setDown (boolean down) { this.down = down; }
    /**
     * Gets whether the key has been clicked.
     *
     * @return true if key is clicked
     */
    public boolean isClicked() { return clicked; }

    /**
     * Changes the key state.
     *
     * @param pressed whether the key is pressed (or not)
     */
    public void toggle (boolean pressed) {
	down = pressed;
	if (pressed) {
	    presses++;
	}
    }

    /**
     * Signals to this key that a tick of game time has passed. This method
     * is used to determine when a button has been "clicked". A button click
     * happens when the key has been pressed down, and then is let up.
     */
    public void tick() {
	if (absorbs < presses) {
	    absorbs++;
	    clicked = true;
	} else {
	    clicked = false;
	}
    }
}

