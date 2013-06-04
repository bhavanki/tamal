package us.havanki.tamal;

import java.util.Random;

/**
 * A class to hold the random number generator for the game.
 */
public class TheRandom {

    // this is a singleton
    private static final TheRandom INSTANCE = new TheRandom();
    public static final TheRandom getInstance() { return INSTANCE; }

    private Random r;
    private TheRandom() {
        r = new Random();
    }

    /**
     * Gets the random number generator from this instance.
     *
     * @return random object
     */
    public Random getRandom() { return r; }

    /**
     * Convenience method to get the random number generator from the singleton
     * instance of this class in one call.
     *
     * @return random object
     */
    public static Random r() { return INSTANCE.getRandom(); }
}
