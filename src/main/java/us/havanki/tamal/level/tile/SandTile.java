package us.havanki.tamal.level.tile;

import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.Level;

/**
 * A tile of sand.
 */
public class SandTile extends Tile {

    SandTile (int id) {
        super(id);
        cxnFlags.connectTo(Tiles.SAND_ID);
    }

    /*
     * Sprite numbers for sand. These must be kept in sync with the
     * spritesheet image.
     */
    private static final int N1 = 0;
    private static final int N2 = 1;
    private static final int N3 = 2;
    private static final int N4 = 3;
    private static final int FP = 35;
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
        SimpleColor sColor = level.env().getSandColor();
        SimpleColor sBluerColor = sColor.adjust(0, 0, 2);
        SimpleColor sdColor = sColor.adjust(-1, -1, 0);
        SimpleColorSet colors =
            new SimpleColorSet(sBluerColor, sColor, sdColor, sdColor);
        SimpleColorSet transitionColors =
            new SimpleColorSet(sdColor, sColor, sdColor,
                               level.env().getDirtColor());

        int x = p.xt();
        int y = p.yt();
        boolean steppedOn = level.getData(x, y) > (byte) 0;

        // do the tiles in each of these directions connect?
        boolean u = level.getTile(x, y - 1).connectsTo(Tiles.SAND);
        boolean d = level.getTile(x, y + 1).connectsTo(Tiles.SAND);
        boolean l = level.getTile(x - 1, y).connectsTo(Tiles.SAND);
        boolean r = level.getTile(x + 1, y).connectsTo(Tiles.SAND);

        if (u && l) {
            screen.render(x * TILE_SIZE, y * TILE_SIZE,
                          steppedOn ? FP : N1, colors, 0);
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
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (steppedOn ? FP : N4), colors, 0);
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (r ? B : (d ? R : BR)), transitionColors, 0);
        }
    }

    @Override public void tick (TilePos p) {
        Level level = p.level();
        int x = p.xt();
        int y = p.yt();
        byte d = level.getData(x, y);
        if (d > (byte) 0) level.setData(x, y, (byte) (d - 1));
    }

    // tbd steppedOn
    // tbd interact
}
