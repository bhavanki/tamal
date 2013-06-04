package us.havanki.tamal.gfx;

import java.util.Map;

/**
 * A simple color, represented as a byte value. The color is expressed as an
 * RGB value, where each channel can be a number from 0 (none) to 5 (full).
 * So, red is 500, green is 50, blue is 5, and white is 555. 216 colors may
 * be represented. The byte value is calculated as (unsigned) 36 * red +
 * 6 * green + blue, or in "hexal". The value -1 (0xff) is reserved as
 * transparency.
 *
 * Instances of this class are immutable.
 */
public final class SimpleColor {
    private static Map<Byte, SimpleColor> scs =
        new java.util.HashMap<Byte, SimpleColor>();

    private final byte value;

    private SimpleColor(byte b) {
        value = b;
    }

    /**
     * Gets a simple color for the given byte value.
     *
     * @param b byte value
     * @return simple color
     */
    public static synchronized SimpleColor getInstance(byte b) {
        SimpleColor c = scs.get(b);
        if (c != null) { return c; }
        c = new SimpleColor(b);
        scs.put(b, c);
        return c;
    }
    /**
     * Gets a simple color for the given integer value. This is not for hexal
     * values, but direct color values.
     *
     * @param i integer value from 0 to 255 (unsigned byte)
     * @return simple color
     * @throws IllegalArgumentException if i is out of range
     */
    public static SimpleColor getInstance(int i) {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException("argument must be 8-bit");
        }
        return getInstance((byte) (i & 0xff));
    }

    /**
     * Returns a simple color for the given hexal value. Any negative number
     * input represents the transparent color.
     *
     * @param d hexal integer
     * @return simple color
     */
    public static SimpleColor fromHexal (int d) {
        if (d < 0) { return getInstance(0xff); }
        int red = (d / 100) % 10;
        int green = (d / 10) % 10;
        int blue = d % 10;
        return getInstance((36 * red) + (6 * green) + blue);
    }

    /**
     * Gets the byte value of this simple color.
     *
     * @return value
     */
    public byte value() { return value; }
    /**
     * Checks whether this simple color is transparent.
     *
     * @return true if simple color is transparent
     */
    public boolean isTransparent() {
        return value == (byte) 0xff;
    }
    /**
     * Converts this simple color back to a hexal value.
     *
     * @return hexal value
     */
    public int toHexal() {
        if (value == (byte) 0xff) { return -1; }
        int ivalue = (int) value & 0xff;
        int blue = ivalue % 6;
        int green = (ivalue / 6) % 6;
        int red = (ivalue / 36) % 6;
        return 100*red + 10*green + blue;
    }
    /**
     * Converts this simple color to an RGB integer value. The resulting 32-bit
     * integer specifies 8-bit red, green, and blue channels in its second,
     * third, and fourth bytes. The transparent color is converted to black.
     *
     * @return RGB value
     */
    public int toRgb() {
        if (value == (byte) 0xff) { return 0; }
        int ivalue = (int) value & 0xff;
        int blue = ivalue % 6;
        int green = (ivalue / 6) % 6;
        int red = (ivalue / 36) % 6;
        return ((red*51) << 16) + ((green*51) << 8) + (blue* 51);
    }

    /**
     * Gets a brighter color than this one.
     *
     * @return color brighter in all channels than this one
     */
    public SimpleColor brighter() {
        return adjust(1, 1, 1);
    }
    /**
     * Gets a darker color than this one.
     *
     * @return color darker in all channels than this one
     */
    public SimpleColor darker() {
        return adjust(-1, -1, -1);
    }
    /**
     * Gets a color adjusted from this one.
     *
     * @param rd change in red channel (hexal)
     * @param gd change in green channel (hexal)
     * @param bd change in blue channel (hexal)
     * @return adjusted color
     */
    public SimpleColor adjust(int rd, int gd, int bd) {
        if (value == (byte) 0xff) { return this; }
        int ivalue = (int) value & 0xff;
        int blue = ivalue % 6;
        int green = (ivalue / 6) % 6;
        int red = (ivalue / 36) % 6;
        red = adjust(red, rd);
        green = adjust(green, gd);
        blue = adjust(blue, bd);
        return getInstance((36 * red) + (6 * green) + blue);
    }
    private int adjust(int channel, int delta) {
        channel += delta;
        if (channel < 0) { channel = 0; }
        else if (channel > 5) { channel = 5; }
        return channel;
    }
}
