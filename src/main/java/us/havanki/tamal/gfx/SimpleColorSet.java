package us.havanki.tamal.gfx;

/**
 * A set of four {@link SimpleColor}s, usually associated with a sprite.
 */
public final class SimpleColorSet {
    private final SimpleColor[] cs;

    /**
     * Creates a new set.
     *
     * @param a first color
     * @param b second color
     * @param c third color
     * @param d fourth color
     */
    public SimpleColorSet(SimpleColor a, SimpleColor b, SimpleColor c,
                          SimpleColor d){
        cs = new SimpleColor[4];
        cs[0] = a;
        cs[1] = b;
        cs[2] = c;
        cs[3] = d;
    }

    /**
     * Gets a single color from this set.
     *
     * @param i simple color to extract (0 - 3)
     * @return simple color
     * @throws IndexOutOfBoundsException if i is out of range
     */
    public SimpleColor get (int i) {
        if (i < 0 || i >= 4) {
            throw new IndexOutOfBoundsException();
        }
        return cs[i];
    }
}
