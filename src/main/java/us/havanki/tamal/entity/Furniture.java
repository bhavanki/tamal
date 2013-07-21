package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;
// import com.mojang.ld22.item.FurnitureItem;
// import com.mojang.ld22.item.PowerGloveItem;

/**
 * An entity representing a piece of furniture. Furniture is made of a 2x2
 * set of smaller sprites (i.e., a furniture sprite is double the width and
 * height of the minimum). The position of a piece of furniture is located
 * one-half a sprite height below its center.
 */
public abstract class Furniture extends Entity {

    // private int pushTime = 0;
    // private int pushDir = -1;
    private final SimpleColorSet colors;
    private final int sprite;
    private final String name;
    // private Player shouldTake;

    /**
     * Creates a new (abstract) piece of furniture.
     *
     * @param name name of entity
     * @param sprite sprite number for top-left sprite
     * @param colors sprite colors
     */
    public Furniture(String name, int sprite, SimpleColorSet colors) {
        super(0, 0, 3, 3);  // tbd why 3?
        this.name = name;
        this.sprite = sprite;
        this.colors = colors;
    }

    /**
     * Gets the name of this entity.
     *
     * @return name
     */
    public String getName() { return name; }
    /**
     * Gets the sprite number of this entity.
     *
     * @return sprite number
     */
    public int getSpriteNumber() { return sprite; }
    /**
     * Gets the sprite colors of this entity.
     *
     * @return colors
     */
    public SimpleColorSet getColors() { return colors; }

    @Override
    public void render(Screen screen) {
        /*
         * There are four minimum sprites to render. Here is a picture of where
         * x and y are when the sprite is rendered.
         *
         *  +--+--+
         *  | 1|2 |
         *  +--+--+
         *  | 3*4 |  <-- (x, y) is where the star is
         *  +--+--+
         */
        int ss = SpriteSheet.SPRITE_SIZE;
        int hss = ss / 2;
        int ssw = screen.getSpriteSheet().getWidthInSprites();
        screen.render(x() - ss, y() - ss - hss, sprite, colors, 0);  // top left
        screen.render(x(), y() - ss - hss, sprite + 1, colors, 0);  // top right
        screen.render(x() - ss, y() - hss, sprite + ssw, colors, 0);  // bottom left
        screen.render(x(), y() - hss, sprite + ssw + 1, colors, 0);  // bottom right
    }

    @Override
    public void tick() {
        // if (shouldTake != null) {
        //     if (shouldTake.activeItem instanceof PowerGloveItem) {
        //         remove();
        //         shouldTake.inventory.add(0, shouldTake.activeItem);
        //         shouldTake.activeItem = new FurnitureItem(this);
        //     }
        //     shouldTake = null;
        // }
        // if (pushDir == 0) move(0, +1);
        // if (pushDir == 1) move(0, -1);
        // if (pushDir == 2) move(-1, 0);
        // if (pushDir == 3) move(+1, 0);
        // pushDir = -1;
        // if (pushTime > 0) pushTime--;
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    @Override
    protected void touchedBy(Entity entity) {
        // if (entity instanceof Player && pushTime == 0) {
        //     pushDir = ((Player) entity).dir;
        //     pushTime = 10;
        // }
    }

    public void take(Player player) {
        // shouldTake = player;
    }
}
