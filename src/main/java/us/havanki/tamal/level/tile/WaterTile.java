package us.havanki.tamal.level.tile;

import java.util.Random;
import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.Level;
import static us.havanki.tamal.gfx.Screen.MIRROR_X;
import static us.havanki.tamal.gfx.Screen.MIRROR_Y;

public class WaterTile extends Tile {

    WaterTile (int id) {
        super(id);
        cxnFlags.connectTo(Tiles.WATER_ID);
        cxnFlags.connectTo(Tiles.SAND_ID);
    }

    /*
     * Sprite numbers for water. These must be kept in sync with the
     * spritesheet image.
     */
    private static final int N1 = 0;  // vvv these must be contiguous
    private static final int N2 = 1;
    private static final int N3 = 2;
    private static final int N4 = 3;  // ^^^ these must be contiguous
    private static final int TL = 14;
    private static final int T = 15;
    private static final int TR = 16;
    private static final int L = 46;
    private static final int R = 48;
    private static final int BL = 78;
    private static final int B = 79;
    private static final int BR = 80;

    private static final SimpleColor BRIGHTER_BLUE =
        SimpleColors.BLUE.brighter();
    private static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.BLUE, SimpleColors.BLUE,
                           BRIGHTER_BLUE, BRIGHTER_BLUE);

    private Random wr = new Random();
    private int randomNormalTile() {
        return wr.nextInt(N4 - N1 + 1) + N1;
    }
    private int randomMirroring() {
        return (wr.nextBoolean() ? MIRROR_X : 0) |
            (wr.nextBoolean() ? MIRROR_Y : 0);
    }

    @Override public void render(Screen screen, TilePos p) {
        Level level = p.level();
        SimpleColorSet transitionColors1 =
            new SimpleColorSet(SimpleColor.fromHexal(3), SimpleColors.BLUE,
                               level.env().getDirtColor().darker(),
                               level.env().getDirtColor());
        SimpleColorSet transitionColors2 =
            new SimpleColorSet(SimpleColor.fromHexal(3), SimpleColors.BLUE,
                               level.env().getSandColor().adjust(-1, -1, 0),
                               level.env().getSandColor());

        int x = p.xt();
        int y = p.yt();
        // wr.setSeed((getTickCount() + (x / 2 - y) * 4311) / 10 * 54687121l + x * 3271612l + y * 3412987161l);
        /*
         * With pure randomness, every render will be different => crazy.
         * Seed deterministically based on where tile is and tick count. Only
         * alter the seed every so many ticks to keep the speed down.
         */
        wr.setSeed(getTickCount() / 20 + x * 1000 + y);

        // do the tiles in each of the directions connect to water?
        boolean u = level.getTile(x, y - 1).connectsTo(Tiles.WATER);
        boolean d = level.getTile(x, y + 1).connectsTo(Tiles.WATER);
        boolean l = level.getTile(x - 1, y).connectsTo(Tiles.WATER);
        boolean r = level.getTile(x + 1, y).connectsTo(Tiles.WATER);

        // what about sand (but not water)?
        boolean su = !u && level.getTile(x, y - 1).connectsTo(Tiles.SAND);
        boolean sd = !d && level.getTile(x, y + 1).connectsTo(Tiles.SAND);
        boolean sl = !l && level.getTile(x - 1, y).connectsTo(Tiles.SAND);
        boolean sr = !r && level.getTile(x + 1, y).connectsTo(Tiles.SAND);

        if (u && l) {
            screen.render(x * TILE_SIZE, y * TILE_SIZE,
                          randomNormalTile(), COLORS, randomMirroring());
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE,
                          (l ? T : (u ? L : TL)),
                          (su || sl) ? transitionColors2 : transitionColors1, 0);
        }

        if (u && r) {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE,
                          randomNormalTile(), COLORS, randomMirroring());
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE,
                          (r ? T : (u ? R : TR)),
                          (su || sr) ? transitionColors2 : transitionColors1, 0);
        }

        if (d && l) {
            screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          randomNormalTile(), COLORS, randomMirroring());
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (l ? B : (d ? L : BL)),
                          (sd || sl) ? transitionColors2 : transitionColors1, 0);
        }
        if (d && r) {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          randomNormalTile(), COLORS, randomMirroring());
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (r ? B : (d ? R : BR)),
                          (sd || sr) ? transitionColors2 : transitionColors1, 0);
        }
    }

    @Override public boolean mayPass (TilePos p, Entity e) { return false; }

    // tbd tick to fill in hole
}
