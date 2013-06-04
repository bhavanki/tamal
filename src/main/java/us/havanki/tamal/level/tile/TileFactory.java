package us.havanki.tamal.level.tile;

import java.util.List;

/**
 * A factory for Tile objects.
 */
public class TileFactory {

    // this is a singleton
    private static final TileFactory INSTANCE = new TileFactory();
    /**
     * Gets the singleton instance of the tile factory.
     *
     * @return tile factory
     */
    public static TileFactory getInstance() { return INSTANCE; }

    private List<Tile> tiles = new java.util.ArrayList<Tile>();
    private void set(int idx, Tile t) {
        for (int i = tiles.size(); i < (idx + 1); i++) {
            tiles.add(null);
        }
        tiles.set(idx, t);
    }
    private TileFactory() {
        // tbd read from config file or something
        set(Tiles.GRASS_ID, Tiles.GRASS);
        set(Tiles.ROCK_ID, Tiles.ROCK);
        set(Tiles.WATER_ID, Tiles.WATER);
        set(Tiles.DIRT_ID, Tiles.DIRT);
        set(Tiles.SAND_ID, Tiles.SAND);
        set(Tiles.TEST_ID, Tiles.TEST);
    }

    /**
     * Gets the tile with the given ID.
     *
     * @param id tile ID
     * @return tile, or null if invalid ID
     */
    public Tile getTile (byte id) {
        int idx = ((int) id) & 0xff;
        if (idx < 0 || idx >= tiles.size()) { return null; }
        return tiles.get(idx);
    }

}
