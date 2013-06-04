package us.havanki.tamal.level;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import us.havanki.tamal.entity.Entity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.level.Level;
import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.TileFactory;
import us.havanki.tamal.level.tile.TilePos;
import us.havanki.tamal.level.tile.Tiles;

/**
 * A single level in the game. A level has a map of tiles and associated
 * data, and entities reside in it. The width and height of a level is expressed
 * in tiles, not pixels.
 */
public class Level {

    private Random r = us.havanki.tamal.TheRandom.getInstance().r();
    private final int w, h;
    private final int depth;
    private final byte[] tiles;
    private final byte[] data;
    private final LevelEnvironment env;
    private final LevelEntityLookup entityLookup;

    /**
     * Creates a new level.
     *
     * @param w width (in tiles)
     * @param h height (in tiles)
     * @param depth depth
     * @param tiles array of tile IDs (row by row)
     * @param data array of tile data
     * @throws IllegalArgumentException if the tiles and data arrays are not
     * each of length w*h
     */
    public Level (int w, int h, int depth, byte[] tiles, byte[] data) {
        this.w = w;
        this.h = h;
        this.depth = depth;

        if (tiles.length != w * h) {
            throw new IllegalArgumentException("tiles array is length " +
                                               tiles.length + ", expected " +
                                               w * h);
        }
        this.tiles = Arrays.copyOf(tiles, tiles.length);
        if (data.length != w * h) {
            throw new IllegalArgumentException("data array is length " +
                                               data.length + ", expected " +
                                               w * h);
        }
        this.data = Arrays.copyOf(data, data.length);

        env = new LevelEnvironment();
        entityLookup = new LevelEntityLookup (w, h);
    }

    /**
     * Gets the width of this level.
     *
     * @return width (in tiles)
     */
    public int w() { return w; }
    /**
     * Gets the height of this level.
     *
     * @return height (in tiles)
     */
    public int h() { return h; }
    /**
     * Gets the depth of this level.
     *
     * @return depth
     */
    public int depth() { return depth; }
    /**
     * Gets the environment for this level.
     *
     * @return environment
     */
    public LevelEnvironment env() { return env; }
    /**
     * Gets the entity lookup for this level.
     *
     * @return entity lookup
     */
    public LevelEntityLookup entityLookup() { return entityLookup; }

    /**
     * Renders the tiles in this level.
     *
     * @param screen screen
     * @param xScroll left x coordinate of visible portion of screen
     * @param yScroll top y coordinate of visible portion of screen
     */
    public void renderBackground (Screen screen, int xScroll, int yScroll) {
        // Work out the range of tiles to render. Start with the left and
        // top coordinates.
        int xt0 = xScroll >> 4;
        int yt0 = yScroll >> 4;

        // Now determine the width and height to render (in tiles).
        int wt = (screen.w() + 15) >> 4;  // tbd how about (w >> 4) + 1 ?
        int ht = (screen.h() + 15) >> 4;

        // Set the screen offset for the scroll values.
        screen.setRenderOffset (xScroll, yScroll);
        // Iterate through the tiles to be rendered, and render them.
        for (int yt = yt0; yt <= yt0 + ht; yt++) {
            for (int xt = xt0; xt <= xt0 + wt; xt++) {
                getTile (xt, yt).render (screen, new TilePos (this, xt, yt));
            }
        }
        // Reset the screen offset.
        screen.setRenderOffset (0, 0);
    }

    private boolean rangeCheck (int xt, int yt) {
        return (xt >= 0 && yt >= 0 && xt < w && yt < h);
    }

    private List<Entity> rowSprites = new java.util.ArrayList<Entity>();

    /**
     * Renders the sprites for the entities in this level.
     *
     * @param screen screen
     * @param xScroll left x coordinate of visible portion of screen
     * @param yScroll top y coordinate of visible portion of screen
     */
    public void renderSprites (Screen screen, int xScroll, int yScroll) {
        // Work out the range of tiles whose entities are to be rendered. Start
        // with the left and top coordinates.
        int xt0 = xScroll >> 4;
        int yt0 = yScroll >> 4;

        // Now determine the width and height to render (in tiles).
        int wt = (screen.w() + 15) >> 4;  // tbd how about (w >> 4) + 1 ?
        int ht = (screen.h() + 15) >> 4;

        // Set the screen offset for the scroll values.
        screen.setRenderOffset (xScroll, yScroll);
        // Iterate through the tiles to be render, and render them (by row).
        for (int yt = yt0; yt <= yt0 + ht; yt++) {
            for (int xt = xt0; xt <= xt0 + wt; xt++) {
                // make sure we are inside the level here
                if (!rangeCheck (xt, yt)) { continue; }
                rowSprites.addAll (entityLookup.getEntitiesInTile (xt, yt));
            }
            if (rowSprites.size() > 0) {
                sortAndRender (screen, rowSprites);
                rowSprites.clear();
            }
        }
        // Reset the screen offset.
        screen.setRenderOffset (0, 0);
    }

    /**
     * An entity comparator that sorts entities by y (column) position.
     */
    public static class SpriteSorter implements Comparator<Entity> {
        public int compare (Entity e0, Entity e1) {
            // if (e1.y() < e0.y()) return 1;
            // if (e1.y() > e0.y()) return -1;
            // return 0;
            return e0.y() - e1.y();
        }
    }
    private static final SpriteSorter SPRITE_SORTER = new SpriteSorter();
    /**
     * Sorts a list of entities by y position and renders them.
     *
     * @param screen screen
     * @param l list of entities to render
     */
    private void sortAndRender (Screen screen, List<Entity> l) {
        Collections.sort (l, SPRITE_SORTER);
        for (Entity e : l) { e.render (screen); }
    }

    /**
     * Gets the tile at the given tile coordinates.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @return tile, or {@link Tiles.Rock} if out of range
     */
    public Tile getTile (int xt, int yt) {
        if (!rangeCheck (xt, yt)) { return Tiles.ROCK; }
        return TileFactory.getInstance().getTile (tiles [xt + yt * w]);
    }
    /**
     * Sets the tile at the given tile coordinates.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @param t tile to set
     * @param dataVal corresponding data value
     * @return this
     */
    public Level setTile (int xt, int yt, Tile t, byte dataVal) {
        if (!rangeCheck (xt, yt)) { return this; }
        tiles [xt + yt * w] = t.getId();
        data [xt + yt * w] = dataVal;
        return this;
    }

    /**
     * Gets the tile data at the given tile coordinates.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @return data for tile, or 0 if out of range
     */
    public byte getData (int xt, int yt) {
        if (!rangeCheck (xt, yt)) { return 0; }
        return data [xt + yt * w];
    }
    /**
     * Sets the tile data at the given tile coordinates.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @param dataVal tile data value
     * @return this
     */
    public Level setData (int xt, int yt, int dataVal) {
        if (!rangeCheck (xt, yt)) { return this; }
        data [xt + yt * w] = (byte) dataVal;
        return this;
    }

    /**
     * Adds an entity to this level. The entity's location is used to determine
     * which tile it occupies.
     *
     * @param e entity to add
     * @return this
     */
    public Level add (Entity e) {
        // tbd notice player
        e.setRemoved (false);
        e.addedToLevel (this);  // was init
        entityLookup.insert (e.x() >> 4, e.y() >> 4, e);
        return this;
    }
    /**
     * Removes an entity from this level.
     *
     * @param e entity to remove
     * @return this
     */
    public Level remove (Entity e) {
        e.remove();  // added this
        entityLookup.remove(e.x() >> 4, e.y() >> 4, e);
        return this;
    }

    // tbd trySpawn


    private static final double TILE_TICK_PERCENTAGE = 0.02;  // 2%

    /**
     * Signal to the level that one tick of game time has passed.
     */
    public void tick() {
        // tbd trySpawn

        tickTiles(TILE_TICK_PERCENTAGE);
        tickEntities();
    }

    /**
     * Tick a percentage of tiles in the level.
     *
     * @param pct percentage of tiles to tick
     */
    void tickTiles(double pct) {
        int numTilesToTick = (int) ((w * h) * pct);
        Set<TilePos> tickedTiles = new java.util.HashSet<TilePos>();
        for (int i = 0; i < numTilesToTick; i++) {
            boolean newTileToTick = false;
            int xt = 0, yt = 0;
            while (!newTileToTick) {
                xt = r.nextInt(w);
                yt = r.nextInt(h);
                for (TilePos tp : tickedTiles) {
                    if (xt == tp.xt() && yt == tp.yt()) {
                        continue;  // no repeats
                    }
                }
                newTileToTick = true;
            }
            TilePos tp = new TilePos(this, xt, yt);
            getTile(xt, yt).tick(tp);
            tickedTiles.add(tp);
        }
    }

    /**
     * Tick all entities in the level.
     */
    void tickEntities() {
        // Tick every entity in the level.
        List<Entity> entities = entityLookup.getAllEntities();
        for (Entity e : entities) {
            // Remember its beginning tile position.
            int xtb = e.x() >> 4;
            int ytb = e.y() >> 4;

            e.tick();  // it might move or decide to be removed

            if (e.isRemoved()) {
                // remove the entity from the level, and don't put it back
                entityLookup.remove(xtb, ytb, e);
            } else {
                // remove the entity from the tile where it was and insert
                // it where it ended up
                int xte = e.x() >> 4;
                int yte = e.y() >> 4;
                if (xtb != xte || ytb != yte) {
                    entityLookup.remove(xtb, ytb, e);
                    entityLookup.insert(xte, yte, e);
                }
            }
        }
    }

    /**
     * Gets entities in this level that intersect the given rectangle.
     *
     * @param x0 left x coordinate of rectangle
     * @param y0 top y coordinate of rectangle
     * @param x1 right x coordinate of rectangle
     * @param y1 bottom y coordinate of rectangle
     * @return entities in given rectangle
     */
    public List<Entity> getEntitiesInRect(int x0, int y0, int x1, int y1) {
        List<Entity> l = new java.util.ArrayList<Entity>();

        // Look for entities in all tiles that lie within the given rectangle,
        // and also those just outside it - entities there may extend in.
        int xt0 = (x0 >> 4) - 1;
        int yt0 = (y0 >> 4) - 1;
        int xt1 = (x1 >> 4) + 1;
        int yt1 = (y1 >> 4) + 1;

        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                // Get all the entities in the tile, and keep those that
                // intersect the given rectangle.
                List<Entity> tileEntities =
                    entityLookup.getEntitiesInTile (xt, yt);
                if (tileEntities != null) {
                    for (Entity e : tileEntities) {
                        if (e.intersects(x0, y0, x1, y1)) { l.add (e); }
                    }
                }
            }
        }

        return l;
    }
}
