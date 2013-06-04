package us.havanki.tamal.res;

import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;

/**
 * Resource instances.
 */
public final class Resources {
    private Resources() {}

    public static final Resource BLUE_FLOWER =
        new Resource ("BL.FLR", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColors.GREEN,
                                         SimpleColor.fromHexal(335)));
    public static final Resource RED_FLOWER =
        new Resource ("RD.FLR", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColors.GREEN,
                                         SimpleColor.fromHexal(533)));
    public static final Resource PURPLE_FLOWER =
        new Resource ("PU.FLR", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColors.GREEN,
                                         SimpleColor.fromHexal(525)));

    public static final Resource ROCK_WARBLER_EGG =
        new Resource ("RW EGG", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(111),
                                         SimpleColor.fromHexal(444)));
    public static final Resource PINE_THRUSH_EGG =
        new Resource ("PT EGG", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(020),
                                         SimpleColor.fromHexal(353)));

    public static final Resource CREEP_CLUSTER =
        new Resource ("CREEP", 0,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(200),
                                         SimpleColor.fromHexal(411)));

}
