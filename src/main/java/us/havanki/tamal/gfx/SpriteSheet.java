package us.havanki.tamal.gfx;

import java.awt.image.BufferedImage;

/**
 * A sheet containing sprites.
 */
public class SpriteSheet {

    /**
     * The minimum width / height for a sprite. Sprites may be even multiples
     * of this size.
     */
    public static final int SPRITE_SIZE = 8;

    private final int w, h;
    private byte[] pixels;

    /**
     * Creates a new sprite sheet from the given image.
     *
     * @param image image of sprites
     */
    public SpriteSheet (BufferedImage image) {
        // Get the height and width of the sheet (in pixels).
        w = image.getWidth();
        h = image.getHeight();

        // Grab all of the pixels in the sheet. Each integer that comes
        // back represents the color in one pixel according to the default
        // RGB DirectColorModel: 8 bits each for alpha, red, green, and blue.
        int[] iPixels = image.getRGB (0, 0, w, h, null, 0, w);

        // The image is expected to be 4-color grayscale, so each RGB component
        // is the same. We'll just take the blue one. This loop then converts
        // the blue down to 0 (black), 1 (dark gray), 2 (light gray), or
        // white (3).
        int len = iPixels.length;
        pixels = new byte[len];
        for (int i = 0; i < len; i++) {
            pixels[i] = (byte) ((iPixels[i] & 0xff) / 64);
        }
    }

    /**
     * Gets the width of the sprite sheet in pixels.
     *
     * @return width
     */
    public int w() { return w; }
    /**
     * Gets the height of the sprite sheet in pixels.
     *
     * @return height
     */
    public int h() { return h; }
    /**
     * Gets the pixels in the sprite sheet.
     *
     * @return pixels
     */
    byte[] pixels() { return pixels; }
    /**
     * Gets the width of the sprite sheet in sprites.
     *
     * @return number of sprites across sheet
     */
    public int getWidthInSprites() { return w / SPRITE_SIZE; }
    /**
     * Gets the height of the sprite sheet in sprites.
     *
     * @return number of sprites down sheet
     */
    public int getHeightInSprites() { return h / SPRITE_SIZE; }

    /**
     * Gets the sprite offset for a sprite at the given position in the sheet.
     * The top left sprite is at (0,0).
     *
     * @param xs sprite x coordinate
     * @param ys sprite y coordinate
     * @return sprite offset
     */
    public int getSpriteOffset (int xs, int ys) {
        return (SPRITE_SIZE * ys) * w + (SPRITE_SIZE * xs);
    }

    /**
     * Gets the color value (0 - 3) for the given sprite at the given x and y
     * coordinates within the sprite.
     *
     * @param spriteOffset offset of sprite in sheet
     * @param x x coordinate for sprite pixel
     * @param y y coordinate for sprite pixel
     * @return pixel color value, 0 - 3
     * @see #getSpriteOffset(int,int)
     */
    public byte valueAt (int spriteOffset, int x, int y) {
        return pixels [spriteOffset + (y * w) + x];
    }
}
