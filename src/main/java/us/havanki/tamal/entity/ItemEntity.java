package us.havanki.tamal.entity;

import java.util.Random;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.item.Item;

/**
 * An entity that represents an item in the world.
 */
public class ItemEntity extends Entity {

    private static final int DEFAULT_XR = 3;  // ???
    private static final int DEFAULT_YR = 3;  // ???

    private final Item item;

    /**
     * Creates a new entity with default radius at the top left of a level.
     *
     * @param item item for entity
     */
    public ItemEntity(Item item) {
        this(item, 0, 0);
    }
    /**
     * Creates a new entity with default radius.
     *
     * @param item item for entity
     * @param x x coordinate of entity
     * @param y y coordinate of entity
     */
    public ItemEntity(Item item, int x, int y) {
        super (x, y, DEFAULT_XR, DEFAULT_YR);
        this.item = item;
    }

    /**
     * Gets this entity's item.
     *
     * @return item
     */
    Item getItem() { return item; }

    @Override
        public boolean isBlockableBy (Mob mob) { return false; }

    @Override
        public void render (Screen screen) {
        // Since the entity should be centered on x/y and not use it as
        // the upper-left corner, adjust the x and y for rendering.
        int rx = x() - (SpriteSheet.SPRITE_SIZE / 2);
        int ry = y() - (SpriteSheet.SPRITE_SIZE / 2);
        screen.render (rx, ry, item.getSprite(), item.getColors(), 0);
    }

    @Override
        protected void touchedBy (Entity entity) {
        entity.touchItem (this);
    }

    /**
     * Take the item for this entity. This destroys the entity.
     *
     * @param player player taking the item
     */
    public void take (Player player) {
        //Sound.pickup.play();
        //player.score++;
        item.onTake (this);
        remove();
    }
}
