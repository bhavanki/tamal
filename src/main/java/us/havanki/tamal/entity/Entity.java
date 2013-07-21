package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.item.Item;
import us.havanki.tamal.level.Level;
// import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.TilePos;

/**
 * An entity present in the game world.
 */
public abstract class Entity {

    /*
     * The entity's position.
     */
    private int x, y;
    /**
     * The entity's radius.
     */
    private int xr = 6, yr = 6;
    /**
     * The level where the entity is located.
     */
    private Level level;
    /**
     * Whether the entity has been removed from a level.
     */
    private boolean removed;

    /**
     * Creates a new entity with default radius at the top left of a level.
     */
    protected Entity() {
        x = 0; y = 0;
    }
    /**
     * Creates a new entity.
     *
     * @param x x coordinate of entity
     * @param y y coordinate of entity
     * @param xr x radius of entity
     * @param yr y radius of entity
     */
    protected Entity (int x, int y, int xr, int yr) {
        this.x = x; this.y = y;
        this.xr = xr; this.yr = yr;
    }

    /**
     * Gets this entity's x coordinate.
     *
     * @return x coordinate
     */
    public int x() { return x; }
    /**
     * Gets this entity's y coordinate.
     *
     * @return y coordinate
     */
    public int y() { return y; }
    /**
     * Places this entity at the given coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void place(int x, int y) {
        this.x = x; this.y = y;
    }
    /**
     * Gets this entity's x radius.
     *
     * @return x radius
     */
    int xr() { return xr; }
    /**
     * Gets this entity's y radius.
     *
     * @return y radius
     */
    int yr() { return yr; }
    /**
     * Gets this entity's level.
     *
     * @return level
     */
    public Level level() { return level; }

    /**
     * Renders this entity.
     *
     * @param screen screen
     */
    public abstract void render (Screen screen);

    /**
     * Signals to the entity that one tick of game time has passed.
     */
    public void tick() {}
    /**
     * Initializes this entity when it is added to a level.
     *
     * @param level level to which entity is added
     */
    public final void addedToLevel (Level level) {  // was init
        this.level = level;
    }

    /**
     * Checks whether this entity is removed.
     *
     * @return true if entity is removed
     */
    public boolean isRemoved() { return removed; }
    /**
     * Sets whether this entity is removed.
     *
     * @param r true if entity is removed
     */
    public Entity setRemoved (boolean r) { removed = r; return this; }
    /**
     * Removes this entity.
     */
    public Entity remove() { return setRemoved (true); }

    /**
     * Checks if this entity intersects something.
     *
     * @param x0 leftmost x coordinate of other thing
     * @param y0 topmost y coordinate of other thing
     * @param x1 rightmost x coordinate of other thing
     * @param y1 bottommost y coordinate of other thing
     * @return true if this entity intersects
     */
    public boolean intersects (int x0, int y0, int x1, int y1) {
        return !(x + xr < x0 || y + yr < y0 ||  // to the left, above ...
                 x - xr > x1 || y - yr > y1);   // to the right, or below
    }

    /**
     * Checks if this entity blocks another.
     *
     * @param e other entity
     * @return true if this entity blocks
     */
    public boolean blocks (Entity e) { return false; }

    /**
     * Moves this entity.
     *
     * @param xa change in entity x coordinate
     * @param ya change in entity y coordinate
     * @return true if move was successful, false if blocked in both directions
     */
    public boolean move (int xa, int ya) {
        if (xa == 0 && ya == 0) { return true; }  // stationary
        boolean stopped = true;
        if (xa != 0 && move2 (xa, 0)) {  // move along the x axis
            stopped = false;
        }
        if (ya != 0 && move2 (0, ya)) {  // move along the y axis
            stopped = false;
        }
        if (!stopped) {  // step on a new tile
            int xt = x >> 4;
            int yt = y >> 4;
            // level.getTile (xt, yt).steppedOn (level, xt, yt, this);
        }
        return !stopped;
    }

    /**
     * Moves this entity along a single axis.
     *
     * @param xa change in entity x coordinate; must be 0 if ya is nonzero
     * @param ya change in entity y coordinate; must be 0 if xa is nonzero
     * @return true if move was successful, false if blocked in both directions
     */
    protected boolean move2 (int xa, int ya) {
        if (xa != 0 && ya != 0) {
            throw new IllegalArgumentException
            ("Move2 can only move along one axis at a time!");
        }

        // Check for blocking.

        // First, find the coordinates for where the entity is now, but for
        // tiles.
        // tbd assumes tile size of 16
        int xt0b = (x - xr) >> 4;  // b for "begin"
        int yt0b = (y - yr) >> 4;
        int xt1b = (x + xr) >> 4;
        int yt1b = (y + yr) >> 4;
        // Now get the tile coordinates for where the entity will end up, if
        // it can move.
        int xt0e = ((x + xa) - xr) >> 4;  // e for "end"
        int yt0e = ((y + ya) - yr) >> 4;
        int xt1e = ((x + xa) + xr) >> 4;
        int yt1e = ((y + ya) + yr) >> 4;
        // Check each tile where the entity will end up. See if any tile there
        // will block the movement.
        for (int yt = yt0e; yt <= yt1e; yt++) {
            for (int xt = xt0e; xt <= xt1e; xt++) {
                // If the entity is already sitting on this tile, don't worry
                // about it.
                if (xt >= xt0b && xt <= xt1b &&
                    yt >= yt0b && yt <= yt1b) { continue; }

                // Tell the tile that the entity bumped into it.
                // level.getTile (xt, yt).bumpedInto (level, xt, yt, this);
                // See if the tile will let the entity pass into it.
                TilePos tp = new TilePos(level, xt, yt);
                if (!level.getTile (xt, yt).mayPass(tp, this)) {
                    return false;
                }
            }
        }

        // tbd inside entities check

        // The move is OK! Update the entity position.
        x += xa; y += ya;
        return true;
    }

    /**
     * Tell this entity something touched it.
     *
     * @param e entity that touched this entity
     */
    protected void touchedBy (Entity e) {}

    /**
     * Checks whether this entity can be blocked by the given mobile entity.
     *
     * @return true if the given mobile entity blocks this one
     */
    public boolean isBlockableBy (Mob mob) { return true; }

    /**
     * Signals that this entity touches an item entity.
     *
     * @param itemEntity entity this entity touches
     */
    public void touchItem (ItemEntity itemEntity) {}

    // ?
    public boolean interact (Player player, Item item, int attackDir) {
        // return item.interact (player, this, attackDir);
        return false;
    }

    // ?
    public boolean use (Player player, int attackDir) { return false; }
}
