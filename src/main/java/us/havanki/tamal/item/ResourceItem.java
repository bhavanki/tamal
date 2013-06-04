package us.havanki.tamal.item;

import us.havanki.tamal.gfx.Font;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.res.Resource;

/**
 * An item that holds / represents one or more resources.
 */
public class ResourceItem extends Item {

    private Resource resource;
    private int amount;

    /**
     * Creates a new resource item.
     *
     * @param r resource
     */
    public ResourceItem (Resource r) {
        this (r, 1);
    }
    /**
     * Creates a new resource item.
     *
     * @param r resource
     * @param amt amount of resource
     * @throws IllegalArgumentException if amt is nonpositive
     */
    public ResourceItem (Resource r, int amt) {
        super (r.getName(), r.getSprite(), r.getColors());
        if (amt <= 0) {
            throw new IllegalArgumentException ("Amount is negative");
        }
        resource = r;
        amount = amt;
    }

    /**
     * Gets this item's resource.
     *
     * @return resource
     */
    public Resource getResource() { return resource; }
    /**
     * Gets this item's (resource) name.
     *
     * @return name
     */
    public String getName() { return resource.getName(); }
    /**
     * Gets the amount of this item's resource.
     *
     * @return amount
     */
    public int getAmount() { return amount; }
    /**
     * Changes the amount of this item's resource.
     *
     * @param delta change to amount
     * @return new amount
     */
    public int changeAmount (int delta) { amount += delta; return amount; }
    @Override public boolean isDepleted() { return amount <= 0; }

    @Override public int getSprite() { return resource.getSprite(); }
    @Override public SimpleColorSet getColors() { return resource.getColors(); }

    private static final SimpleColorSet LABEL_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT, SimpleColors.WHITE,
                           SimpleColors.WHITE, SimpleColors.WHITE);
    private static final SimpleColor LIGHT_GRAY = SimpleColor.fromHexal(444);
    private static final SimpleColorSet AMOUNT_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT, LIGHT_GRAY,
                           LIGHT_GRAY, LIGHT_GRAY);

    @Override public void renderInventory (Screen screen, int xp, int yp) {
        screen.render (xp, yp, getSprite(), getColors(), 0);
        Font.draw (resource.getName(), screen,
                   xp + (SpriteSheet.SPRITE_SIZE * 4), yp, LABEL_COLORS);
        int iAmt = (amount <= 999 ? amount : 999);
        Font.draw ("" + iAmt, screen, xp + SpriteSheet.SPRITE_SIZE, yp,
                   AMOUNT_COLORS);
    }
    @Override public void renderIcon (Screen screen, int xp, int yp) {
        screen.render (xp, yp, getSprite(), getColors(), 0);
    }

    //public void onTake (ItemEntity itemEntity) {}
    // tbd interactOn

}
