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
        new Resource ("BL.FLR", 128,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(010),
                                         SimpleColors.BLUE,
                                         SimpleColors.YELLOW));
    public static final Resource RED_FLOWER =
        new Resource ("RD.FLR", 128,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(010),
                                         SimpleColors.RED,
                                         SimpleColors.YELLOW));
    public static final Resource PURPLE_FLOWER =
        new Resource ("PU.FLR", 128,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(010),
                                         SimpleColors.PURPLE,
                                         SimpleColors.YELLOW));

    public static final Resource ROCK_WARBLER_EGG =
        new Resource ("RW EGG", 141,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(111),
                                         SimpleColor.fromHexal(444)));
    public static final Resource PINE_THRUSH_EGG =
        new Resource ("PT EGG", 141,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(020),
                                         SimpleColor.fromHexal(353)));

    public static final Resource CREEP_CLUSTER =
        new Resource ("CREEP", 132,
                      new SimpleColorSet(SimpleColors.TRANSPARENT,
                                         SimpleColor.fromHexal(100),
                                         SimpleColor.fromHexal(200),
                                         SimpleColor.fromHexal(411)));

}
