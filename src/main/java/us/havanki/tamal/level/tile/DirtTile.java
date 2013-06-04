package us.havanki.tamal.level.tile;

import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.Level;

public class DirtTile extends Tile {

    DirtTile (int id) {
        super (id);
    }

    /*
     * Sprite numbers for dirt. These must be kept in sync with the
     * spritesheet image.
     */
    private static final int N1 = 0;
    private static final int N2 = 1;
    private static final int N3 = 2;
    private static final int N4 = 3;

    @Override public void render(Screen screen, TilePos p) {
        Level level = p.level();
        SimpleColor dColor = level.env().getDirtColor();
        SimpleColor ddColor = dColor.darker();
        SimpleColorSet colors =
            new SimpleColorSet(dColor, dColor, ddColor, ddColor);

        int x = p.xt();
        int y = p.yt();

        screen.render(x * TILE_SIZE, y * TILE_SIZE, N1, colors, 0);
        screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE, N2, colors, 0);
        screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N3, colors, 0);
        screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N4, colors, 0);
    }
}
