package us.havanki.tamal.level;

import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;

public class LevelEnvironment {

    private SimpleColor grassColor = SimpleColor.fromHexal(141);
    private SimpleColor dirtColor = SimpleColor.fromHexal(322);
    private SimpleColor sandColor = SimpleColors.YELLOW;

    public SimpleColor getGrassColor() { return grassColor; }
    public void setGrassColor(SimpleColor c) { grassColor = c; }
    public SimpleColor getDirtColor() { return dirtColor; }
    public void setDirtColor(SimpleColor c) { dirtColor = c; }
    public SimpleColor getSandColor() { return sandColor; }
    public void setSandColor(SimpleColor c) { sandColor = c; }
}
