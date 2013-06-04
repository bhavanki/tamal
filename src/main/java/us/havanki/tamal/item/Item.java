package us.havanki.tamal.item;

import us.havanki.tamal.entity.ItemEntity;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColorSet;

/**
 * An item in the game.
 */
public class Item {  // TBD: ListItem / renderInventory

    /**
     * The item's name.
     */
    private final String name;
    /**
     * The item's sprite number.
     */
    private final int sprite;
    /**
     * The item's colors.
     */
    private final SimpleColorSet colors;

    /**
     * Creates a new item.
     *
     * @param name item name
     * @param sprite item sprite number
     * @param colors merged item colors
     */
    protected Item (String name, int sprite, SimpleColorSet colors) {
        this.name = name;
        this.sprite = sprite;
        this.colors = colors;
    }

    /**
     * Gets the item's name.
     *
     * @return name
     */
    public String getName() { return name; }
    /**
     * Gets the item's sprite number.
     *
     * @return sprite number
     */
    public int getSprite() { return sprite; }
    /**
     * Gets the item's merged colors.
     *
     * @return colors
     */
    public SimpleColorSet getColors() { return colors; }
    /**
     * Checks if the item is depleted.
     *
     * @return true if item is depleted
     */
    public boolean isDepleted() { return false; }

    /**
     * Signal to this item that it is being taken. ?
     *
     * @param e item entity
     */
    public void onTake (ItemEntity e) {}
    /**
     * Renders this item in an inventory.
     *
     * @param screen screen
     * @param xp x coordinate for left edge of sprite
     * @param yp y coordinate for top edge of sprite
     */
    public void renderInventory (Screen screen, int xp, int yp) {}
    /**
     * Renders the icon for this item.
     *
     * @param screen screen
     * @param xp x coordinate for left edge of sprite
     * @param yp y coordinate for top edge of sprite
     */
    public void renderIcon (Screen screen, int xp, int yp) {}
    // more tbd

    /**
     * Checks if this item is the same kind as another.
     *
     * @param item other item
     * @return true if items are the same kind
     */
    public boolean matches (Item item) {
        return (item.getClass() == getClass());
    }

    // tbd interactOn

}
