package us.havanki.tamal.level;

import java.util.List;
import us.havanki.tamal.entity.Entity;

/**
 * A lookup mechanism by tile for entities in a level.
 */
public class LevelEntityLookup {

    private final int w, h;
    private List<List<Entity>> els;

    /**
     * Creates a new entity lookup.
     *
     * @param w width of level (in tiles)
     * @param h height of level (in tiles)
     */
    public LevelEntityLookup (int w, int h) {
        this.w = w;
        this.h = h;
        int size = w * h;
        els = new java.util.ArrayList<List<Entity>>();
        for (int i = 0; i < size; i++) {
            els.add (new java.util.ArrayList<Entity>());;
        }
    }

    private boolean rangeCheck (int xt, int yt) {
        return (xt >= 0 && yt >= 0 && xt < w && yt < h);
    }

    /**
     * Gets the entities in the given tile.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @return entities in that tile, or null if invalid tile coordinates
     */
    public List<Entity> getEntitiesInTile (int xt, int yt) {
        if (!rangeCheck (xt, yt)) { return null; }
        return new java.util.ArrayList<Entity> (els.get (xt + yt * w));
    }
    /**
     * Gets all entities in the level.
     *
     * @return entities
     */
    public List<Entity> getAllEntities() {
        List<Entity> all = new java.util.ArrayList<Entity>();
        for (List<Entity> l : els) { all.addAll(l); }
        return all;
    }
    /**
     * Inserts an entity into a tile.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @param e entity to insert
     * @return this
     */
    public LevelEntityLookup insert (int xt, int yt, Entity e) {
        if (rangeCheck (xt, yt)) {
            els.get (xt + yt * w).add (e);
        }
        return this;
    }
    /**
     * Removes an entity from a tile.
     *
     * @param xt x coordinate of tile
     * @param yt y coordinate of tile
     * @param e entity to remove
     * @return this
     */
    public LevelEntityLookup remove (int xt, int yt, Entity e) {
        if (rangeCheck (xt, yt)) {
            els.get (xt + yt * w).remove (e);
        }
        return this;
    }

}
