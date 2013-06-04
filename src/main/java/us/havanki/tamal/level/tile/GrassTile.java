package us.havanki.tamal.level.tile;

import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.Level;

/**
 * A tile of grass.
 */
public class GrassTile extends Tile {

    GrassTile (int id) {
        super(id);
        cxnFlags.connectTo(Tiles.GRASS_ID);
    }

    /*
     * Sprite numbers for grass. These must be kept in sync with the
     * spritesheet image.
     */
    private static final int N1 = 0;
    private static final int N2 = 1;
    private static final int N3 = 2;
    private static final int N4 = 3;
    private static final int TL = 11;
    private static final int T = 12;
    private static final int TR = 13;
    private static final int L = 43;
    private static final int R = 45;
    private static final int BL = 75;
    private static final int B = 76;
    private static final int BR = 77;

    @Override public void render(Screen screen, TilePos p) {
        Level level = p.level();
        SimpleColor gColor = level.env().getGrassColor();
        SimpleColor gbColor = gColor.brighter();
        SimpleColor gdColor = gColor.darker();
        SimpleColorSet colors =
            new SimpleColorSet(gColor, gColor, gbColor, gbColor);
        SimpleColorSet transitionColors =
            new SimpleColorSet(gdColor, gColor, gbColor,
                               level.env().getDirtColor());

        int x = p.xt();
        int y = p.yt();

        // do the tiles in each of these directions connect?
        boolean u = level.getTile(x, y - 1).connectsTo(Tiles.GRASS);
        boolean d = level.getTile(x, y + 1).connectsTo(Tiles.GRASS);
        boolean l = level.getTile(x - 1, y).connectsTo(Tiles.GRASS);
        boolean r = level.getTile(x + 1, y).connectsTo(Tiles.GRASS);

        if (u && l) {
            screen.render(x * TILE_SIZE, y * TILE_SIZE, N1, colors, 0);
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE,
                          (l ? T : (u ? L : TL)), transitionColors, 0);
        }
        if (u && r) {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE, N2, colors, 0);
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE,
                          (r ? T : (u ? R : TR)), transitionColors, 0);
        }
        if (d && l) {
            screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N3, colors, 0);
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (l ? B : (d ? L : BL)), transitionColors, 0);
        }
        if (d && r) {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N4, colors, 0);
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (r ? B : (d ? R : BR)), transitionColors, 0);
        }
    }

    private static final int GROWTH_ODDS = 40;
    @Override public void tick (TilePos p) {
        if (r.nextInt(GROWTH_ODDS) != 0) return;

        // Look at some adjacent tile. Start here.
        int xn = p.xt();
        int yn = p.yt();
        if (r.nextBoolean()) {
            // Look left or right.
            xn += r.nextInt(2) * 2 - 1;
        } else {
            // Look up or down.
            yn += r.nextInt(2) * 2 - 1;
        }

        // If the adjacent tile is dirt, make it grass!
        if (p.level().getTile(xn, yn) == Tiles.DIRT) {
            p.level().setTile(xn, yn, Tiles.GRASS, (byte) 0);
        }
    }

    // tbd interact
}
