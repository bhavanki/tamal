package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;

/**
 * A furnace, used for cooking stuff.
 */
public class Furnace extends Furniture {

    /**
     * Sprite number for top-left of entity. These must be kept in sync with the
     * spritesheet image.
     */
    static final int S = 262;

    public static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColors.BLACK,
                           SimpleColors.DOVE_GRAY,
                           SimpleColors.GRAY);

    /**
     * Creates a new furnace.
     */
    public Furnace() {
        super("Furnace", S, COLORS);
        // xr = 3;
        // yr = 2;
    }

    // public boolean use(Player player, int attackDir) {
    //     player.game.setMenu(new CraftingMenu(Crafting.furnaceRecipes, player));
    //     return true;
    // }
}
