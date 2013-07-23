package us.havanki.tamal.res;

import us.havanki.tamal.entity.Direction;
import us.havanki.tamal.entity.Player;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.TilePos;

/**
 * A resource that can be collected, used, etc.
 */
public class Resource {

    private final String name;
    private final int sprite;
    private final SimpleColorSet colors;

    /**
     * Creates a new resource.
     *
     * @param name name
     * @param sprite sprite number
     * @param colors sprite colors
     * @throws IllegalArgumentException if name is more than six characters
     */
    Resource (String name, int sprite, SimpleColorSet colors) {
        if (name.length() > 6) {
            throw new IllegalArgumentException ("Name " + name +
                                                " is too long");
        }
        this.name = name;
        this.sprite = sprite;
        this.colors = colors;
    }

    /**
     * Gets the name of the resource.
     *
     * @return name
     */
    public String getName() { return name; }
    /**
     * Gets the sprite number of this resource.
     *
     * @return sprite number
     */
    public int getSprite() { return sprite; }
    /**
     * Gets the merged colors of this resource.
     *
     * @return colors
     */
    public SimpleColorSet getColors() { return colors; }

    // ???
    public boolean interactOn (Tile tile, TilePos tilePos, Player player,
                               Direction dir) {
        return false;
    }

    @Override public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof Resource)) { return false; }
        Resource o = (Resource) other;
        if (!(name.equals(o.name))) { return false; }
        return true;
    }
    @Override public int hashCode() {
        int c = 17;
        c = 37 * c + name.hashCode();
        return c;
    }
    @Override public String toString() { return name; }
}
