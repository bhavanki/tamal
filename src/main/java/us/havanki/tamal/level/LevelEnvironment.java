package us.havanki.tamal.level;

import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;

/**
 * Information about a level's environment, such as its colors.
 */
public class LevelEnvironment {

    private SimpleColor grassColor = SimpleColor.fromHexal(141);
    private SimpleColor dirtColor = SimpleColor.fromHexal(322);
    private SimpleColor sandColor = SimpleColors.YELLOW;

    /**
     * Gets the color of grass in this level.
     *
     * @return grass color
     */
    public SimpleColor getGrassColor() { return grassColor; }
    /**
     * Sets the color of grass in this level.
     *
     * @param c grass color
     */
    public void setGrassColor(SimpleColor c) { grassColor = c; }
    /**
     * Gets the color of dirt in this level.
     *
     * @return dirt color
     */
    public SimpleColor getDirtColor() { return dirtColor; }
    /**
     * Sets the color of dirt in this level.
     *
     * @param c dirt color
     */
    public void setDirtColor(SimpleColor c) { dirtColor = c; }
    /**
     * Gets the color of sand in this level.
     *
     * @return sand color
     */
    public SimpleColor getSandColor() { return sandColor; }
    /**
     * Sets the color of sand in this level.
     *
     * @param c sand color
     */
    public void setSandColor(SimpleColor c) { sandColor = c; }
}
