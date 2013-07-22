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
     * Gets whether the key has been clicked. A click occurs when a key is
     * initially pressed down.
     *
     * @return true if key is clicked
     */
    public boolean isClicked() { return clicked; }

    /**
     * Changes the key state.
     *
     * @param true if the key was just pressed, false if released
     */
    public void toggle (boolean pressed) {
        down = pressed;
        if (pressed) {
            presses++;
        }
    }
    /**
     * Resets the key's internal state. Useful for testing.
     */
    final void reset() {
        absorbs = presses = 0;
        down = clicked = false;
    }

    /**
     * Signals to this key that a tick of game time has passed. This method
     * is used to determine when a button has been "clicked". A button click
     * happens on the first tick after a key has been pressed down.
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

