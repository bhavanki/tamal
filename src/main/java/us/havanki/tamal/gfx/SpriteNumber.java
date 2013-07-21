package us.havanki.tamal.gfx;

/**
 * The "number" for a sprite in a sprite sheet. Each minimum sprite in the sheet
 * (a block of size SPRITE_SIZE) has a number. The first row is numbered 0
 * through the sheet's width in sprites (sw) - 1, the second row is numbered sw
 * to 2 * sw - 1, and so on.
 */
public class SpriteNumber {
    private final int n;
    private final SpriteSheet sheet;

    /**
     * Creates a new sprite number.
     *
     * @param n the number
     * @param sheet the sprite sheet holding the sprite
     */
    public SpriteNumber(int n, SpriteSheet sheet) {
        this.n = n;
        this.sheet = sheet;
    }

    /**
     * Gets the sprite number.
     *
     * @return sprite number
     */
    public int getNumber() { return n; }
    /**
     * Gets the x coordinate, in sprites, of the sprite within the sheet.
     *
     * @return x coordinate of sprite in sprite units
     */
    public int xs() { return n % sheet.getWidthInSprites(); }
    /**
     * Gets the y coordinate, in sprites, of the sprite within the sheet.
     *
     * @return y coordinate of sprite in sprite units
     */
    public int ys() { return n / sheet.getWidthInSprites(); }
    /**
     * Gets the offset of the sprite within the sheet.
     *
     * @return sprite offset
     * @see SpriteSheet#getSpriteOffset(int,int)
     */
    public int getOffset() { return sheet.getSpriteOffset(xs(), ys()); }
}
