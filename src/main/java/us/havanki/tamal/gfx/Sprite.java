package us.havanki.tamal.gfx;

/**
 * A sprite.
 */
public class Sprite {

    private int x, y;
    private int img;
    private SimpleColorSet colors;
    private int bits;

    /**
     * Constructs a new sprite.
     *
     * @param x x coordinate of sprite
     * @param y y coordinate of sprite
     * @param img image number of sprite
     * @param colors sprite colors
     * @param bits sprite bit masks
     */
    public Sprite (int x, int y, int img, SimpleColorSet colors, int bits) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.colors = colors;
        this.bits = bits;
    }

    /**
     * Gets the sprite's x coordinate.
     *
     * @return x coordinate
     */
    public int x() { return x; }
    /**
     * Gets the sprite's y coordinate.
     *
     * @return y coordinate
     */
    public int y() { return y; }
    /**
     * Gets the sprite's image number.
     *
     * @return image number
     */
    public int image() { return img; }
    /**
     * Gets the sprite's colors.
     *
     * @return colors
     */
    public SimpleColorSet colors() { return colors; }
    /**
     * Gets the sprite's bit masks.
     *
     * @return bit masks
     */
    public int bits() { return bits; }
}
