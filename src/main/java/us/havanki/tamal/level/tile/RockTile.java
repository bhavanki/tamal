package us.havanki.tamal.level.tile;

import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.Level;
import static us.havanki.tamal.gfx.Screen.MIRROR_X;
import static us.havanki.tamal.gfx.Screen.MIRROR_Y;

public class RockTile extends Tile {

    RockTile (int id) {
        super(id);
    }

    /*
     * Sprite numbers for rock. These must be kept in sync with the
     * spritesheet image.
     */
    private static final int N1 = 0;
    private static final int N2 = 1;
    private static final int N3 = 2;
    private static final int N4 = 3;
    private static final int TL = 70;  // all below are rotated when rendered
    private static final int T = 69;
    private static final int TR = 68;
    private static final int L = 38;
    private static final int R = 36;
    private static final int BL = 6;
    private static final int B = 5;
    private static final int BR = 4;
    private static final int KTL = 7;
    private static final int KTR = 8;
    private static final int KBL = 39;
    private static final int KBR = 40;

    /**
     * Standard rock colors.
     */
    private static final SimpleColor rColor = SimpleColor.fromHexal(444);
    private static final SimpleColor rColor2 = SimpleColor.fromHexal(333);
    private static final SimpleColor rColor3 = SimpleColor.fromHexal(111);
    private static final SimpleColorSet COLORS =
        new SimpleColorSet(rColor, rColor, rColor2, rColor2);

    private static final int ROT_180 = MIRROR_X | MIRROR_Y;

    @Override public void render(Screen screen, TilePos p) {
        Level level = p.level();
        SimpleColorSet transitionColors =
            new SimpleColorSet(rColor3, rColor, SimpleColors.WHITE,
                               level.env().getDirtColor());

        int x = p.xt();
        int y = p.yt();

        boolean u = level.getTile(x, y - 1) == Tiles.ROCK;
        boolean d = level.getTile(x, y + 1) == Tiles.ROCK;
        boolean l = level.getTile(x - 1, y) == Tiles.ROCK;
        boolean r = level.getTile(x + 1, y) == Tiles.ROCK;

        // pattern, for each diagonal =>
        // if rock on both sides of diagonal:
        //   if rock on diagonal:
        //     totally enclosed, draw normal tile
        //   else:
        //     only open on diagonal, draw nook
        // else (rock not on both sides of diagonal):
        //   draw tile (rock on side ? vert border :
        //                             (rock on vert ? side border : both))
        if (u && l) {
            if (level.getTile(x - 1, y - 1) == Tiles.ROCK) {
                screen.render(x * TILE_SIZE, y * TILE_SIZE, N1, COLORS, 0);
            } else {
                screen.render(x * TILE_SIZE, y * TILE_SIZE, KTL,
                              transitionColors, ROT_180);
            }
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE,
                          (l ? T : (u ? L : TL)), transitionColors, ROT_180);
        }

        if (u && r) {
            if (level.getTile(x + 1, y - 1) == Tiles.ROCK) {
                screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE, N2, COLORS, 0);
            } else {
                screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE, KTR,
                              transitionColors, ROT_180);
            }
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE,
                          (r ? T : (u ? R : TR)), transitionColors, ROT_180);
        }

        if (d && l) {
            if (level.getTile(x - 1, y + 1) == Tiles.ROCK) {
                screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N3, COLORS, 0);
            } else {
                screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, KBL, transitionColors, ROT_180);
            }
        } else {
            screen.render(x * TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (l ? B : (d ? L : BL)), transitionColors, ROT_180);
        }
        if (d && r) {
            if (level.getTile(x + 1, y + 1) == Tiles.ROCK) {
                screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, N4, COLORS, 0);
            } else {
                screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE, KBR, transitionColors, ROT_180);
            }
        } else {
            screen.render(x * TILE_SIZE + HALF_TILE_SIZE, y * TILE_SIZE + HALF_TILE_SIZE,
                          (r ? B : (d ? R : BR)), transitionColors, ROT_180);
        }
    }

    @Override public boolean mayPass (TilePos p, Entity e) { return false; }

}
