package us.havanki.tamal.entity;

import java.util.Random;
import us.havanki.tamal.TheRandom;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;
import us.havanki.tamal.item.Item;

public class ItemEntity extends Entity {

    private final Item item;
    private final int lifetime;
    private int time;

    private double xa, ya, za;
    private double xx, yy, zz;

    public ItemEntity (Item item, int x, int y) {
        super (x, y, 3, 3);
        this.item = item;
        Random r = TheRandom.r();
        lifetime = r.nextInt (60) + 600;  // lifetime in ticks
        time = 0;

        // Initialize parameters for bouncing.
        xx = x; yy = y; zz = 2;  // initial position
        xa = r.nextGaussian() * 0.3;  // initial pixels/tick in x
        ya = r.nextGaussian() * 0.2;  // initial pixels/tick in y
        za = r.nextFloat() * 0.7 + 1;  // initial height above ground
    }

    /**
     * Gets this entity's item.
     *
     * @return item
     */
    Item getItem() { return item; }

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
        // - Where did it end it for reals? Maybe it banged into something.
        int gotxchange = x() - ox;
        int gotychange = y() - oy;
        // - If where the entity ended up differs from expected, compensate.
        //   - xx = xx + x() - ox - nx + ox = xx + x() - nx
        xx += (gotxchange - expectedxchange);
        //   - yy = yy + y() - oy - ny + oy = yy + y() - ny
        yy += (gotychange - expectedychange);
    }

    @Override
        public boolean isBlockableBy (Mob mob) { return false; }

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
        screen.render (rx, ry, item.getSprite(), SHADOW_COLORS, 0);

        // Now render the entity itself. Account for its z position.
        screen.render (rx, ry - (int) zz, item.getSprite(), item.getColors(),
                       0);
    }

    @Override
        protected void touchedBy (Entity entity) {
        // The entity cannot be touched right away.
        if (time > 30) {
            entity.touchItem (this);
        }
    }

    /**
     * Take the item for this entity. This destroys the entity.
     *
     * @param player player taking the item
     */
    public void take (Player player) {
        //Sound.pickup.play();
        //player.score++;
        item.onTake (this);
        remove();
    }
}
