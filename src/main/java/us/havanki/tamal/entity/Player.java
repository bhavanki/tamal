package us.havanki.tamal.entity;

import java.util.List;
import us.havanki.tamal.Game;
import us.havanki.tamal.InputHandler;
import us.havanki.tamal.InputKey;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.level.tile.Tile;

public class Player extends Mob {

    private final Game game;
    private final InputHandler input;
    private final Inventory inventory;

    public Player (Game game, InputHandler input) {
        super (24, 24, 6, 6);  // ?
        this.game = game;
        this.input = input;
        inventory = new Inventory();
    }

    @Override
        public void tick() {
        super.tick();

        // What tile is the player on?
        Tile onTile = level().getTile (x() >> 4, y() >> 4);

        // Check if the player is trying to move.
        int xa = 0; int ya = 0;
        if (InputKey.UP.isDown()) { ya--; }
        if (InputKey.DOWN.isDown()) { ya++; }
        if (InputKey.LEFT.isDown()) { xa--; }
        if (InputKey.RIGHT.isDown()) { xa++; }

        // Check if the player is trying to use something.
        if (InputKey.MENU.isClicked()) {
            if (!(use())) {
                // If there is nothing to use, pop up inventory.
                // game.setMenu (new InventoryMenu (this));
            }
        }
    }

    private static final int RANGE = SpriteSheet.SPRITE_SIZE * 3 / 2;

    private boolean use() {
        int yo = -2;  // ?

        // Define a rectangle within which maybe there is something to be
        // used.
        int ux0 = 0, uy0 = 0, ux1 = 0, uy1 = 0;  // ???????? check if this is OK
        switch (getDirection()) {
        case DOWN:
            ux0 = x() - SpriteSheet.SPRITE_SIZE;
            uy0 = y() + (SpriteSheet.SPRITE_SIZE / 2) + yo;
            ux1 = x() + SpriteSheet.SPRITE_SIZE;
            uy1 = y() + RANGE + yo;
            break;
        case UP:
            ux0 = x() - SpriteSheet.SPRITE_SIZE;
            uy0 = y() - RANGE + yo;
            ux1 = x() + SpriteSheet.SPRITE_SIZE;
            uy1 = y() - (SpriteSheet.SPRITE_SIZE / 2) + yo;
            break;
        case LEFT:
            ux0 = x() - RANGE;
            uy0 = y() - SpriteSheet.SPRITE_SIZE + yo;
            ux1 = x() - (SpriteSheet.SPRITE_SIZE / 2);
            uy1 = y() + SpriteSheet.SPRITE_SIZE + yo;
            break;
        case RIGHT:
            ux0 = x() + (SpriteSheet.SPRITE_SIZE / 2);
            uy0 = y() - SpriteSheet.SPRITE_SIZE + yo;
            ux1 = x() + RANGE;
            uy1 = y() + SpriteSheet.SPRITE_SIZE + yo;
            break;
        }
        if (useEntity (ux0, uy0, ux1, uy1)) return true;

        // tbd tile

        return false;
    }

    /**
     * Use one of the entities in the level within the given rectangle.
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
            // if (e.use (this, getDirection())) { return true; }
        }
        return false;
    }

    private static final int PLAYER_SPRITE_COL = 0;
    private static final int PLAYER_SPRITE_ROW = 14;
    private static final SimpleColorSet PLAYER_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColor.fromHexal(100),
                           SimpleColor.fromHexal(220),
                           SimpleColor.fromHexal(532));

    @Override
        public void render (Screen screen) {
        int xs = PLAYER_SPRITE_COL;  // start with down-facing sprite
        int ys = PLAYER_SPRITE_ROW;

        // One step by the player depends on the direction:
        // - heading up and down, a step is 8 pixels.
        // - heading left and right, a step is 16 pixels.
        int halfstep = (int) ((getWalkDistance() >> 3) & 1);  // 0 or 1
        int fullstep = halfstep;  // to start with
        int bitMaskTop = 0;
        int bitMaskBottom = 0;

        // Pick a set of sprites.
        switch (getDirection()) {
        case UP:
            xs += 2;  // select up-facing sprite
            break;
        case LEFT:
        case RIGHT:
            xs += 4 + (2 * halfstep);  // standing or striding sprite
            break;
        }

        // Figure out the bitMasks, whether to flip things.
        switch (getDirection()) {
        case DOWN:
        case UP:
            if (halfstep == 1) {
                bitMaskTop = bitMaskBottom = Screen.MIRROR_X;
            } else {
                bitMaskTop = bitMaskBottom = 0;
            }
            break;
        case LEFT:
        case RIGHT:
            if (getDirection() == Direction.LEFT) {
                bitMaskTop = Screen.MIRROR_X;
            } else {
                bitMaskTop = 0;
            }
            fullstep = (int) ((getWalkDistance() >> 4) & 1);
            bitMaskBottom = (fullstep == 1 ? Screen.MIRROR_X : 0);
            break;
        }

        SpriteSheet sheet = screen.getSpriteSheet();
        int xo = x() - 8;  // ?
        int yo = y() - 11;  // ?

        // Render the top two sprites of the player.
        int tlSpriteNumber = xs + (ys * sheet.getWidthInSprites());
        int tlXs = (halfstep == 1) ? xo + SpriteSheet.SPRITE_SIZE: xo;
        screen.render (tlXs, yo, tlSpriteNumber, PLAYER_COLORS, bitMaskTop);
        int trXs = (halfstep == 1) ? xo : xo + SpriteSheet.SPRITE_SIZE;
        int trSpriteNumber = (xs + 1) + (ys * sheet.getWidthInSprites());
        screen.render (trXs, yo, trSpriteNumber, PLAYER_COLORS, bitMaskTop);

        // Render the bottom two sprites of the player.
        int blSpriteNumber = xs + ((ys + 1) * sheet.getWidthInSprites());
        int blXs = (fullstep == 1) ? xo + SpriteSheet.SPRITE_SIZE: xo;
        screen.render (blXs, yo + SpriteSheet.SPRITE_SIZE,
                       blSpriteNumber, PLAYER_COLORS, bitMaskBottom);
        int brSpriteNumber = (xs + 1) + ((ys + 1) * sheet.getWidthInSprites());
        int brXs = (fullstep == 1) ? xo : xo + SpriteSheet.SPRITE_SIZE;
        screen.render (brXs, yo + SpriteSheet.SPRITE_SIZE,
                       blSpriteNumber, PLAYER_COLORS, bitMaskBottom);
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
