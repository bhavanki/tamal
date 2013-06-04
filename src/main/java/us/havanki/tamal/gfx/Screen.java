package us.havanki.tamal.gfx;

/**
 * The model of the screen, i.e., the viewable world.
 */
public class Screen {

    private final SpriteSheet sheet;
    private final int w, h;
    private final SimpleColor[] pixels;

    private int xOffset, yOffset;

    /**
     * Creates a new screen.
     *
     * @param w screen width
     * @param h screen height
     * @param sheet sprite sheet for rendering onto screen
     */
    public Screen (int w, int h, SpriteSheet sheet) {
        this.w = w;
        this.h = h;
        pixels = new SimpleColor [w * h];
        java.util.Arrays.fill(pixels, SimpleColors.TRANSPARENT);
        this.sheet = sheet;

        xOffset = yOffset = 0;
    }

    /**
     * Gets the width of the screen.
     *
     * @return width
     */
    public int w() { return w; }
    /**
     * Gets the height of the screen.
     *
     * @return height
     */
    public int h() { return h; }
    /**
     * Gets the screen pixels.
     *
     * @return pixels
     */
    SimpleColor[] pixels() { return pixels; }
    /**
     * Gets the color value at the chosen pixel.
     *
     * @return color
     */
    SimpleColor pixel(int x, int y) {
        return pixels[y * w + x];
    }
    /**
     * Gets the sprite sheet for the screen.
     *
     * @return sprite sheet
     */
    public SpriteSheet getSpriteSheet() { return sheet; }

    /**
     * Sets the offset for rendering to the screen. Each offset is subtracted
     * from the requested rendering coordinates for a sprite. These are used
     * to adjust the viewable area based on scrolling.
     *
     * @param xOffset x offset
     * @param yOffset y offset
     * @return this
     */
    public Screen setRenderOffset (int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }
    /**
     * Gets the x offset.
     *
     * @return x offset
     */
    int xOffset() { return xOffset; }
    /**
     * Gets the y offset.
     *
     * @return y offset
     */
    int yOffset() { return yOffset; }

    /**
     * Clears the screen to the given color.
     *
     * @param color color to set
     * @return this
     */
    public Screen clear (SimpleColor color) {
        java.util.Arrays.fill (pixels, color);
        return this;
    }

    public static final int MIRROR_X = 0x01;
    public static final int MIRROR_Y = 0x02;

    /**
     * Renders a sprite on the screen.
     *
     * @param xp x coordinate for left edge of sprite
     * @param yp y coordinate for top edge of sprite
     * @param sprite sprite number
     * @param colors sprite colors
     * @param bits control bits
     */
    public Screen render (int xp, int yp, int sprite, SimpleColorSet colors,
                          int bits) {
        // If there is a rendering offset, use it now.
        xp -= xOffset;
        yp -= yOffset;

        // Check the control bits for mirroring.
        boolean mirrorX = (bits & MIRROR_X) > 0;
        boolean mirrorY = (bits & MIRROR_Y) > 0;

        // Get the sprite offset based on the sprite number. Each minimum
        // sprite in the sheet (a block of size SPRITE_SIZE) has a number. The
        // first row is numbered 0 through the sheet's sprite width - 1, the
        // second row is numbered sw to 2 * sw - 1, and so on.
        int xsp = sprite % sheet.getSpriteWidth();
        int ysp = sprite / sheet.getSpriteWidth();
        int so = sheet.getSpriteOffset (xsp, ysp);

        // Write each sprite pixel's color into the screen.
        for (int y = 0; y < SpriteSheet.SPRITE_SIZE; y++) {
            if (y + yp < 0 || y + yp >= h) { continue; }  // out of screen
            int ys = y;
            if (mirrorY) {
                ys = SpriteSheet.SPRITE_SIZE - 1 - y;  // reverse
            }
            for (int x = 0; x < SpriteSheet.SPRITE_SIZE; x++) {
                if (x + xp < 0 || x + xp >= w) { continue; } // out of screen
                int xs = x;
                if (mirrorX) {
                    xs = SpriteSheet.SPRITE_SIZE - 1 - x;  // reverse
                }
                int value = sheet.valueAt (so, xs, ys);  // sprite value 0 - 3
                SimpleColor color = colors.get(value);  // pick color
                if (!color.isTransparent()) {
                    int pixelNum = (x + xp) + ((y + yp) * w);
                    pixels [pixelNum] = color;  // write color to screen
                }
            }
        }

        return this;
    }

    // TBD: dither, overlay, renderLight
}
