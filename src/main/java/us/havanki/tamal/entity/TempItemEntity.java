package us.havanki.tamal.entity;

import java.util.Random;
import us.havanki.tamal.TheRandom;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.item.Item;

/**
 * An entity that represents a dropped item in the world. The item has a
 * lifetime and will disappear. In the beginning of its lifetime, it bounces
 * away from its initial position randomly.
 */
public class TempItemEntity extends ItemEntity {

    private static final int MINIMUM_LIFETIME_TICKS = 600;

    private final int lifetime;
    private int time;

    private double xa, ya, za;
    private double xx, yy, zz;

    public TempItemEntity(Item item) {
        this(item, 0, 0);
    }
    public TempItemEntity(Item item, int x, int y) {
        super(item, x, y);
        Random r = TheRandom.r();
        lifetime = MINIMUM_LIFETIME_TICKS + r.nextInt (60);
        time = 0;

        // Initialize parameters for bouncing.
        xx = x(); yy = y(); zz = 2;  // initial position
        xa = r.nextGaussian() * 0.3;  // initial pixels/tick in x
        ya = r.nextGaussian() * 0.2;  // initial pixels/tick in y
        za = r.nextFloat() * 0.7 + 1;  // initial height above ground
    }

    @Override
        public void tick() {
        time++;
        if (time >= lifetime) {
            remove();
            return;
        }

        // Bounce. This stuff is kind of mysterious to me.
        // - Start by figuring where the entity should move to.
        xx += xa;
        yy += ya;
        zz += za;
        // - If the entity hits the ground, have it bounce up with half speed,
        //   and traverse the ground 6/10 as quickly.
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        // - Have gravity pull the entity down.
        za -= 0.15;
        // - Save where the entity is now.
        int ox = x(); int oy = y();
        // - Get where it should end up after moving, according to the
        //   calculations above.
        int nx = (int) xx; int ny = (int) yy;
        int expectedxchange = nx - x(); int expectedychange = ny - y();
        // - Try to move the entity where it should go.
        move (expectedxchange, expectedychange);
        // - Where did it end up for reals? Maybe it banged into something.
        int gotxchange = x() - ox;
        int gotychange = y() - oy;
        // - If where the entity ended up differs from expected, compensate.
        //   - xx = xx + x() - ox - nx + ox = xx + x() - nx
        xx += (gotxchange - expectedxchange);
        //   - yy = yy + y() - oy - ny + oy = yy + y() - ny
        yy += (gotychange - expectedychange);
    }

    private static final SimpleColorSet SHADOW_COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT, SimpleColors.BLACK,
                           SimpleColors.BLACK, SimpleColors.BLACK);

    @Override
        public void render (Screen screen) {
        // When the lifetime of the entity draws near its end, make it
        // blink by not rendering it sometimes.
        if (time >= lifetime - 120 && time / 6 % 2 == 0) { return; }

        // First render the entity's shadow.
        // - Since the entity should be centered on x/y and not use it as
        //   the upper-left corner, adjust the x and y for rendering.
        int rx = x() - (SpriteSheet.SPRITE_SIZE / 2);
        int ry = y() - (SpriteSheet.SPRITE_SIZE / 2);
        screen.render (rx, ry, getItem().getSprite(), SHADOW_COLORS, 0);

        // Now render the entity itself. Account for its z position.
        screen.render (rx, ry - (int) zz, getItem().getSprite(),
                       getItem().getColors(), 0);
    }

    @Override
        protected void touchedBy (Entity entity) {
        // The entity cannot be touched right away.
        if (time > 30) {
            entity.touchItem (this);
        }
    }
}
