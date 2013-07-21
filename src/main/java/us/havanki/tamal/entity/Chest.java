package us.havanki.tamal.entity;

import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;

/**
 * A chest, for holding stuff.
 */
public class Chest extends Furniture {

    // private final Inventory inventory = new Inventory();

    /**
     * Sprite number for top-left of entity. These must be kept in sync with the
     * spritesheet image.
     */
    static final int S = 258;

    public static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColor.fromHexal(110),
                           SimpleColor.fromHexal(331),
                           SimpleColor.fromHexal(552));

    /**
     * Creates a new chest.
     */
    public Chest() {
        super("Chest", S, COLORS);
        // xr = 3;
        // yr = 2;
    }

    // public boolean use(Player player, int attackDir) {
    //     player.game.setMenu(new ContainerMenu(player, "Chest", inventory));
    //     return true;
    // }
}
