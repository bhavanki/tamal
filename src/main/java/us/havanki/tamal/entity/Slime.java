package us.havanki.tamal.entity;

import java.util.Map;
import java.util.Random;
import us.havanki.tamal.TheRandom;
import us.havanki.tamal.entity.Player;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SimpleColor;
import us.havanki.tamal.gfx.SimpleColors;
import us.havanki.tamal.gfx.SimpleColorSet;
import us.havanki.tamal.gfx.SpriteSheet;

/**
 * A moving ball of goo.
 */
public class Slime extends Mob {

    public static final int DEFAULT_JUMP_ODDS = 40;
    public static final int DEFAULT_JUMP_TICKS = 10;
    public static final int DEFAULT_JUMP_SPEED = 1;
    public static final int DEFAULT_JUMP_PAUSE = 10;
    public static final int DEFAULT_CHASE_RANGE = 50;

    private Random random = TheRandom.r();
    private int jumpTime = 0;
    private int xa = 0;
    private int ya = 0;
    private int jumpOdds = DEFAULT_JUMP_ODDS;
    private int jumpTicks = DEFAULT_JUMP_TICKS;
    private int jumpSpeed = DEFAULT_JUMP_SPEED;
    private int jumpPause = DEFAULT_JUMP_PAUSE;
    private int squareOfChaseRange = DEFAULT_CHASE_RANGE * DEFAULT_CHASE_RANGE;

    /**
     * Sprite coordinates for slime sprite. These must be kept in sync with
     * the spritesheet image.
     */
    static final Map<Boolean, int[]> SLIME_SPRITES =
        new java.util.HashMap<Boolean, int[]>();  // key = whether jumping
    static {
        SLIME_SPRITES.put(false, new int[] { 0, 18 });
        SLIME_SPRITES.put(true, new int[] { 2, 18 });
    }

    public static final SimpleColorSet COLORS =
        new SimpleColorSet(SimpleColors.TRANSPARENT,
                           SimpleColors.GREEN,
                           SimpleColor.fromHexal(252),
                           SimpleColors.WHITE);

    /**
     * Creates a slime with default behavior.
     */
    public Slime() {}
    /**
     * Creates a slime with specific behavior.
     *
     * @param jumpOdds how likely a still slime is to jump; probability = 1 / this
     * @param jumpTicks how many ticks long a jump lasts
     * @param jumpSpeed a multiplier for jump speed, with 1 = 1 pixel per tick
     * @param jumpPause how many ticks a landing slime waits before thinking of jumping again
     * @param chaseRange maximum straight-line distance, in pixels, between
     *        slime and player below which slime will chase after player; set to
     *        zero to effectively disable chasing
     * @throws IllegalArgumentException if any parameter doesn't make sense
     */
    public Slime(int jumpOdds, int jumpTicks, int jumpSpeed, int jumpPause,
                 int chaseRange) {
        if (jumpOdds < 1) {
            throw new IllegalArgumentException("jumpOdds must be >= 1");
        }
        if (jumpTicks < 1) {
            throw new IllegalArgumentException("jumpTicks must be >= 1");
        }
        if (jumpSpeed < 1) {
            throw new IllegalArgumentException("jumpSpeed must be >= 1");
        }
        if (jumpPause < 0) {
            throw new IllegalArgumentException("jumpTicks must be >= 0");
        }
        if (chaseRange < 0) {
            throw new IllegalArgumentException("chaseRange must be >= 0");
        }
        this.jumpOdds = jumpOdds;
        this.jumpTicks = jumpTicks;
        this.jumpSpeed = jumpSpeed;
        this.jumpPause = jumpPause;
        this.squareOfChaseRange = chaseRange * chaseRange;
    }

    @Override public void tick() {
        super.tick();

        // moving or not?
        if (xa != 0 || ya != 0) {
            // moving!
            move(xa * jumpSpeed, ya * jumpSpeed);
        } else {
            // still
            if (jumpTime <= -(jumpPause) && random.nextInt(jumpOdds) == 0) {
                // jump!
                xa = random.nextInt(3) - 1;  // -1, 0, or 1
                ya = random.nextInt(3) - 1;  // -1, 0, or 1

                // possibly chase!
                Player player = level().getPlayer();
                if (player != null) {
                    int xd = player.x() - x();
                    int yd = player.y() - y();
                    int sqDist = xd * xd + yd * yd;
                    if (sqDist < squareOfChaseRange) {
                        xa = (xd < 0 ? -1 : (xd > 0 ? 1 : 0));
                        ya = (yd < 0 ? -1 : (yd > 0 ? 1 : 0));
                    }
                }

                if (xa != 0 || ya != 0) {
                    jumpTime = jumpTicks;
                }
            }
        }

        jumpTime--;
        if (jumpTime == 0) {
            xa = ya = 0;  // JUMP_TICKS ticks have elapsed, stop
        }
    }

    @Override public void render(Screen screen) {
        SpriteSheet sheet = screen.getSpriteSheet();
        int xo = x() - 8;  // ?
        int yo = y() - 11;  // ?

        boolean moving = (jumpTime > 0);
        int[] scoords = SLIME_SPRITES.get(moving);
        int xs = scoords[0];
        int ys = scoords[1];
        if (moving) { yo -= 4; }  // draw up a bit

        int tlSpriteNumber = xs + (ys * sheet.getWidthInSprites());
        int trSpriteNumber = (xs + 1) + (ys * sheet.getWidthInSprites());
        int blSpriteNumber = xs + ((ys + 1) * sheet.getWidthInSprites());
        int brSpriteNumber = (xs + 1) + ((ys + 1) * sheet.getWidthInSprites());

        screen.render(xo, yo, tlSpriteNumber, COLORS, 0);
        screen.render(xo + SpriteSheet.SPRITE_SIZE, yo, trSpriteNumber, COLORS, 0);
        screen.render(xo, yo + SpriteSheet.SPRITE_SIZE, blSpriteNumber, COLORS, 0);
        screen.render(xo + SpriteSheet.SPRITE_SIZE, yo + SpriteSheet.SPRITE_SIZE,
                      brSpriteNumber, COLORS, 0);
    }
}
