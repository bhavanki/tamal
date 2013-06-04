package us.havanki.tamal.level.tile;

import java.util.Set;

/**
 * A utility class for tracking what sorts of tile a tile connects to.
 */
public class TileConnectionFlags {
    // integer values defined in Tiles
    private final Set<Integer> connectsTo = new java.util.HashSet<Integer>();

    /**
     * Creates a new objects. Initially, no flags are set.
     */
    public TileConnectionFlags() {}

    /**
     * Sets the flags for connection to the given tiles.
     *
     * @param tiles tiles to connect to
     * @return this
     */
    public TileConnectionFlags connectTo(int... tileIds) {
        for (int tileId : tileIds) {
            connectsTo.add(tileId);
        }
        return this;
    }

    /**
     * Determines if the flag for the given tile is set.
     *
     * @param tile tile to check
     * @return true if flag set
     */
    public boolean connectsTo(Tile tile) {
        return connectsTo.contains((int) tile.getId());
    }
}
