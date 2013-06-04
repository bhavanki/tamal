package us.havanki.tamal.gfx;

/**
 * A utility class for representing colors with integer values.
 *
 * @deprecated
 */
@Deprecated
public final class Color {
    private Color() {}

    /**
     * Merges a set of four colors into a single integer. Each of the four
     * colors is {@linkplain #get(int) converted} into an 8-bit value, and the
     * four values are combined into a single integer.
     *
     * @param a color for bits 0-7
     * @param b color for bits 8-15
     * @param c color for bits 16-23
     * @param d color for bits 24-31
     * @return merged colors as a single int
     */
    public static int merge (int a, int b, int c, int d) {
        return (get(d) << 24) + (get(c) << 16) +
            (get(b) << 8) + get(a);
    }

    /**
     * Separates out a color from a merged color integer.
     *
     * @param i merged colors
     * @param c color to extract: 0 for lowest-order through 3 for highest
     * @return color
     * @see #merge(int,int,int,int)
     */
    public static int separate (int i, int c) {
        return (i >> (c * 8)) & 0xff;
    }

    /**
     * Returns an 8-bit value for the color represented by the given
     * integer. The given integer specifies the color with three digits
     * representing the red, green, and blue values. Each digit may range
     * from 0 to 5. 000 is black; 555 is white; 216 colors can be
     * represented. Any negative number input represents a transparent color,
     * returned as 255.
     *
     * @param d RGB color integer
     * @return 8-bit color representation
     */
    public static int get (int d) {
        if (d < 0) { return 255; }
        int red = (d / 100) % 10;
        int green = (d / 10) % 10;
        int blue = d % 10;
        return (36 * red) + (6 * green) + blue;  // hexal?
    }

    /**
     * Checks whether a color is transparent.
     *
     * @param c color
     * @return true if color is transparent
     */
    public static boolean isTransparent (int c) {
        return c == 255;
    }

}
