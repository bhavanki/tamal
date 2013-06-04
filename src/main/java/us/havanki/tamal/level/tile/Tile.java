package us.havanki.tamal.level.tile;

import java.util.Random;
import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;

/**
 * A tile in the game map. Tile instances are handled as flyweights.
 */
public class Tile {
    private static int tickCount = 0;
    /**
     * Increment the tick count, which is remembered for all tiles.
     */
    public static void incrementTickCount() { tickCount++; }
    /**
     * Get the tick count.
     */
    protected static int getTickCount() { return tickCount; }

    protected Random r = us.havanki.tamal.TheRandom.getInstance().r();

    /**
     * The size of a tile, in pixels.
     */
    public static final int TILE_SIZE = 16;
    /**
     * The size of half a tile, in pixels.
     */
    public static final int HALF_TILE_SIZE = 8;  // = sprite size

    private final byte id;
    protected final TileConnectionFlags cxnFlags;

    /**
     * Creates a new tile.
     *
     * @param id tile ID
     */
    Tile (int id) {
        this.id = (byte) id;
        cxnFlags = new TileConnectionFlags();
    }

    /**
     * Gets this tile's ID.
     *
     * @return ID
     */
    public byte getId() { return id; }
    /**
     * Determines if this tile connects to the given tile.
     *
     * @param tile tile to check
     * @return true if this tile connects
     */
    public boolean connectsTo(Tile tile) {
        return cxnFlags.connectsTo(tile);
    }

    /**
     * Renders this tile.
     *
     * @param screen screen
     * @param p tile position
     */
    public void render (Screen screen, TilePos p) {}
    /**
     * Checks whether the given entity can pass through a tile.
     *
     * @param p tile position
     * @param e entity attempting to pass
     * @return true if entity may pass
     */
    public boolean mayPass (TilePos p, Entity e) {  // tbd should not be xt/yt?
        return true;
    }
    /**
     * Tells a tile that an entity bumped into it.
     *
     * @param p tile position
     * @param e entity bumping into the tile
     */
    public void bumpedInto (TilePos p, Entity e) {}
    /**
     * Signal to the tile that one tick of game time has passed.
     *
     * @param p tile position
     */
    public void tick (TilePos p) {}
    /**
     * Tells a tile that an entity stepped on it.
     *
     * @param p tile position
     * @param e entity stepping on the tile
     */
    public void steppedOn (TilePos p, Entity e) {}
    // more tbd

    @Override public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        return other.getClass().equals(this.getClass());
    }
}
