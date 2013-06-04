package us.havanki.tamal.gfx;

import java.util.Locale;
import static us.havanki.tamal.gfx.SpriteSheet.SPRITE_SIZE;

/**
 * A class for rendering messages, bare and boxed, on screen.
 */
public final class Font {
    // TBD configure based on SpriteSheet
    private Font() {}

    private static final int FONT_ROW = 30;
    private static final int FRAME_ROW = 13;
    private static final int SHEET_WIDTH = 32;
    private static final int FRAME_SPRITE_START = FRAME_ROW * SHEET_WIDTH;

    private static final String CHARS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
        "0123456789.,!?'\"-+=/\\%()<>:;     ";

    /**
     * Writes the given message on the screen.
     *
     * @param msg message to write
     * @param screen screen where message should be written
     * @param x x coordinate for left edge of message
     * @param y y coordinate for top edge of message
     * @param colors font colors
     */
    public static final void draw (String msg, Screen screen, int x, int y,
                                   SimpleColorSet colors) {
        msg = msg.toUpperCase (Locale.US);
        int len = msg.length();
        for (int i = 0; i < len; i++) {
            int idx = CHARS.indexOf (msg.charAt (i));
            if (idx >= 0) {
                screen.render (x + (i * SPRITE_SIZE), y,
                               (FONT_ROW * SHEET_WIDTH) + idx, colors, 0);
            }
        }
    }

    private static final SimpleColorSet BORDER_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT, SimpleColor.fromHexal(1),
                           SimpleColors.BLUE, SimpleColor.fromHexal(445));
    private static final SimpleColorSet INTERIOR_COLORS =
        new SimpleColorSet(SimpleColors.BLUE, SimpleColors.BLUE,
                           SimpleColors.BLUE, SimpleColors.BLUE);
    private static final SimpleColorSet FONT_COLORS =
        new SimpleColorSet(SimpleColors.BLUE, SimpleColors.BLUE,
                           SimpleColors.BLUE, SimpleColors.YELLOW);

    /**
     * Renders a frame for holding text, with a title.
     *
     * @param screen screen
     * @param title frame title
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public static final void renderFrame (Screen screen, String title,
                                          int x0, int y0, int x1, int y1) {
        // tbd why *8 for coordinates?

        // Draw the frame.
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                // default to top left
                SimpleColorSet colors = BORDER_COLORS;
                int spriteOffset = 0;
                int bitMask = 0;
                if (x == x1 && y == y0) {
                    bitMask = Screen.MIRROR_X;  // top right
                } else if (x == x0 && y == y1) {
                    bitMask = Screen.MIRROR_Y;  // bottom left
                } else if (x == x1 && y == y1) {
                    bitMask = Screen.MIRROR_X | Screen.MIRROR_Y;  // bot. right
                } else if (y == y0) {
                    spriteOffset = 1;  // left side
                } else if (y == y1) {
                    spriteOffset = 1;  // right side
                    bitMask = Screen.MIRROR_Y;
                } else if (x == x0) {
                    spriteOffset = 2;  // top side
                } else if (x == x1) {
                    spriteOffset = 2;  // bottom side
                    bitMask = Screen.MIRROR_X;
                } else {
                    colors = INTERIOR_COLORS;
                }
                screen.render (SPRITE_SIZE * x, SPRITE_SIZE * y,
                               FRAME_SPRITE_START + spriteOffset, colors,
                               bitMask);
            }
        }

        // Draw the title.
        draw (title, screen, SPRITE_SIZE * (x0 + 1), y0, FONT_COLORS);
    }
}
