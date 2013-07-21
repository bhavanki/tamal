package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;

/**
 * An alchemy table, for creating potions.
 */
public class AlchemyTable extends Furniture {

    /**
     * Sprite number for top-left of entity. These must be kept in sync with the
     * spritesheet image.
     */
    static final int S = 264;

    public static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColor.fromHexal(100),
                           SimpleColor.fromHexal(321),
                           SimpleColor.fromHexal(431));

    public AlchemyTable() {
        super("AlchemyTable", S, COLORS);
        // xr = 3;
        // yr = 2;
    }

    // public boolean use(Player player, int attackDir) {
    //     player.game.setMenu(new CraftingMenu(Crafting.workbenchRecipes, player));
    //     return true;
    // }
}
