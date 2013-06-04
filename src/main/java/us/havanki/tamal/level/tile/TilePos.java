package us.havanki.tamal.level.tile;

import us.havanki.tamal.level.Level;

/**
 * The position of a tile in the game map. An instance of this class is
 * externalized state for a Tile.
 */
public class TilePos {
    private final Level level;
    private final int xt;
    private final int yt;

    /**
     * Creates a new tile position.
     *
     * @param level the level of the tile
     * @param xt the tile's x coordinate
     * @param yt the tile's y coordinate
     */
    public TilePos (Level level, int xt, int yt) {
        this.level = level;
        this.xt = xt;
        this.yt = yt;
    }

    /**
     * Gets the tile's level.
     *
     * @return level
     */
    public Level level() { return level; }
    /**
     * Gets the tile's x coordinate.
     *
     * @return x coordinate
     */
    public int xt() { return xt; }
    /**
     * Gets the tile's y coordinate.
     *
     * @return y coordinate
     */
    public int yt() { return yt; }
}
