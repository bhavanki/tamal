package us.havanki.tamal.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import us.havanki.tamal.Game;
import us.havanki.tamal.InputHandler;
import us.havanki.tamal.InputKey;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.level.tile.Tile;

/**
 * You!
 */
public class Player extends Mob {

    private final Game game;
    private final InputHandler input;
    private final Inventory inventory;

    public Player (Game game, InputHandler input) {
        super (24, 24, 4, 3, Direction.DOWN);  // ?
        this.game = game;
        this.input = input;
        inventory = new Inventory();
    }

    @Override
        public void tick() {
        super.tick();

        // What tile is the player on?
        Tile onTile = level().getTile (x() >> 4, y() >> 4);  // assume tile=16

        // Check if the player is trying to move.
        int xa = 0; int ya = 0;
        if (InputKey.UP.isDown()) { ya--; }
        if (InputKey.DOWN.isDown()) { ya++; }
        if (InputKey.LEFT.isDown()) { xa--; }
        if (InputKey.RIGHT.isDown()) { xa++; }
        move(xa, ya);

        // Check if the player is trying to use something.
        if (InputKey.MENU.isClicked()) {
            if (!(use())) {
                // If there is nothing to use, pop up inventory.
                // game.setMenu (new InventoryMenu (this));
            }
        }
    }

    static final int USE_MIN = SpriteSheet.SPRITE_SIZE / 2;
    static final int USE_DEPTH = SpriteSheet.SPRITE_SIZE;
    static final int USE_MAX = USE_MIN + USE_DEPTH;
    static final int USE_HALF_SPAN = SpriteSheet.SPRITE_SIZE;

    private boolean use() {
        /*
         * Define a rectangle within which maybe there is something to be
         * used. Here's a picture. Not to scale.
         *
         *   +-----+    <-- this rectangle is the use box when facing up
         *   |     |
         * +-+-+ +-+-+  <-- these are the tops of the use boxes when facing
         * | | | | | |      left and right
         * | +-+-+-+ |
         * |   |*|   |  <-- player is at star in center
         * | +-+-+-+ |
         * | | | | | |      these are the bottoms of the use boxes when facing
         * +-+-+ +-+-+  <-- left and right
         *   |     |
         *   +-----+    <-- this rectangle is the use box when facing down
         *
         *
         */
        int ux0 = 0, uy0 = 0, ux1 = 0, uy1 = 0;
        switch (getDirection()) {
        case DOWN:
            ux0 = x() - USE_HALF_SPAN;
            uy0 = y() + USE_MIN;
            ux1 = x() + USE_HALF_SPAN;
            uy1 = y() + USE_MAX;
            break;
        case UP:
            ux0 = x() - USE_HALF_SPAN;
            uy0 = y() - USE_MAX;
            ux1 = x() + USE_HALF_SPAN;
            uy1 = y() - USE_MIN;
            break;
        case LEFT:
            ux0 = x() - USE_MAX;
            uy0 = y() - USE_HALF_SPAN;
            ux1 = x() - USE_MIN;
            uy1 = y() + USE_HALF_SPAN;
            break;
        case RIGHT:
            ux0 = x() + USE_MIN;
            uy0 = y() - USE_HALF_SPAN;
            ux1 = x() + USE_MAX;
            uy1 = y() + USE_HALF_SPAN;
            break;
        }

        int yo = -2;  // some sort of fudge offset to shift all use boxes
        uy0 += yo;
        uy1 += yo;

        if (useEntity (ux0, uy0, ux1, uy1)) return true;

        // tbd tile

        return false;
    }

    /**
     * Use one of the entities in the level within the given rectangle. The
     * first one available that isn't the player is used.
     *
     * @param x0 left x coordinate of rectangle
     * @param y0 top y coordinate of rectangle
     * @param x1 right x coordinate of rectangle
     * @param y1 bottom y coordinate of rectangle
     * @return true if an entity was used
     */
    private boolean useEntity (int x0, int y0, int x1, int y1) {
        List<Entity> entities = level().getEntitiesInRect(x0, y0, x1, y1);
        for (Entity e : entities) {
            if (e == this) { continue; }
            if (e.use (this, getDirection())) { return true; }
        }
        return false;
    }

    // ---

    /**
     * Sprite coordinates for player sprite. These must be kept in sync with
     * the spritesheet image.
     */
    static final Map<Direction, int[]> PLAYER_SPRITES =
        new java.util.HashMap<Direction, int[]>();
    static {
        PLAYER_SPRITES.put(Direction.DOWN, new int[] { 0, 14 });
        PLAYER_SPRITES.put(Direction.UP, new int[] { 2, 14 });
        PLAYER_SPRITES.put(Direction.LEFT, new int[] { 4, 14 });  // stand r
        PLAYER_SPRITES.put(Direction.RIGHT, new int[] { 4, 14 });  // stand r
    }

    public static final SimpleColorSet PLAYER_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColor.fromHexal(100),
                           SimpleColor.fromHexal(220),
                           SimpleColor.fromHexal(532));

    @Override
    public void render (Screen screen) {
        // One step by the player depends on the direction:
        // - heading up and down, a step is 8 pixels.
        // - heading left and right, a step is 16 pixels.
        int halfstep = (int) ((getWalkDistance() >> 3) & 1);  // 0 or 1
        int fullstep = halfstep;  // to start with

        // Pick a set of sprites.
        int[] scoords = PLAYER_SPRITES.get(getDirection());
        scoords = Arrays.copyOf(scoords, scoords.length);
        if (getDirection() == Direction.LEFT ||
            getDirection() == Direction.RIGHT) {
            // select striding sprite on half steps
            // assumes player is 2 sprites wide, stride sprite is next door
            scoords[0] += (2 * halfstep);
        }
        int xs = scoords[0];
        int ys = scoords[1];

        // Figure out the bitMasks, whether to flip things.
        int bitMaskTop = 0;  // for the top half of the player sprite
        int bitMaskBottom = 0;  // for the bottom half of the player sprite
        switch (getDirection()) {
        case DOWN:
        case UP:
            // On a half step, mirror the sprite side-to-side.
            if (halfstep == 1) {
                bitMaskTop = bitMaskBottom = Screen.MIRROR_X;
            } else {
                bitMaskTop = bitMaskBottom = 0;
            }
            break;
        case LEFT:
        case RIGHT:
            // This is more complex. First, the top half, which is
            // straightforward.
            if (getDirection() == Direction.LEFT) {
                bitMaskTop = Screen.MIRROR_X;  // face the other way
            } else {
                bitMaskTop = 0;
            }
            // The bottom half flipping doesn't matter if it's left or right.
            // It flips for each full step. The side-facing player sprites are
            // specifically drawn so that the head is the top half and the
            // body the bottom half. So, this logic makes the striding pose
            // switch legs every full step. (The standing pose is symmetrical
            // so the flip isn't apparent.) Frankly, this is insane.
            fullstep = (int) ((getWalkDistance() >> 4) & 1);
            bitMaskBottom = (fullstep == 1 ? Screen.MIRROR_X : 0);
            break;
        }

        SpriteSheet sheet = screen.getSpriteSheet();
        int xo = x() - 8;  // ?
        int yo = y() - 11;  // ?

        // Render the top two sprites of the player.
        int tlSpriteNumber = xs + (ys * sheet.getWidthInSprites());
        int trSpriteNumber = (xs + 1) + (ys * sheet.getWidthInSprites());
        int tlXs = 0, trXs = 0;
        switch (getDirection()) {
            case DOWN:
            case UP:
                tlXs = (halfstep == 1) ? xo + SpriteSheet.SPRITE_SIZE: xo;
                trXs = (halfstep == 1) ? xo : xo + SpriteSheet.SPRITE_SIZE;
                break;
            case LEFT:
                tlXs = xo + SpriteSheet.SPRITE_SIZE;
                trXs = xo;
                break;
            case RIGHT:
                tlXs = xo;
                trXs = xo + SpriteSheet.SPRITE_SIZE;
                break;
        }
        screen.render (tlXs, yo, tlSpriteNumber, PLAYER_COLORS, bitMaskTop);
        screen.render (trXs, yo, trSpriteNumber, PLAYER_COLORS, bitMaskTop);

        // Render the bottom two sprites of the player.
        int blSpriteNumber = xs + ((ys + 1) * sheet.getWidthInSprites());
        int blXs = (fullstep == 1) ? xo + SpriteSheet.SPRITE_SIZE: xo;
        screen.render (blXs, yo + SpriteSheet.SPRITE_SIZE,
                       blSpriteNumber, PLAYER_COLORS, bitMaskBottom);
        int brSpriteNumber = (xs + 1) + ((ys + 1) * sheet.getWidthInSprites());
        int brXs = (fullstep == 1) ? xo : xo + SpriteSheet.SPRITE_SIZE;
        screen.render (brXs, yo + SpriteSheet.SPRITE_SIZE,
                       brSpriteNumber, PLAYER_COLORS, bitMaskBottom);
    }

    @Override
        public void touchItem (ItemEntity itemEntity) {
        itemEntity.take (this);
        inventory.add (itemEntity.getItem());
    }

    public void changeLevel (int dir) {
        // game.scheduleLevelChange (dir);
    }

    protected void touchEntity (Entity e) {
        if (!(e instanceof Player)) { e.touchedBy (this); }
    }
}
