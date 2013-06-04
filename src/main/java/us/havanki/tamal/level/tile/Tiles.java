package us.havanki.tamal.level.tile;

/**
 * Tile instances.
 */
public final class Tiles {
    private Tiles() {}

    public static final int GRASS_ID = 0;
    public static final int ROCK_ID = 1;
    public static final int WATER_ID = 2;
    public static final int DIRT_ID = 5;
    public static final int SAND_ID = 6;
    public static final int TEST_ID = 255;

    public static final GrassTile GRASS = new GrassTile(GRASS_ID);
    public static final RockTile ROCK = new RockTile(ROCK_ID);
    public static final WaterTile WATER = new WaterTile(WATER_ID);
    public static final DirtTile DIRT = new DirtTile(DIRT_ID);
    public static final SandTile SAND = new SandTile(SAND_ID);
    public static final TestTile TEST = new TestTile(TEST_ID);

}
