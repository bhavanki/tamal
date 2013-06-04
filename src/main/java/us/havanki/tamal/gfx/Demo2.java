package us.havanki.tamal.gfx;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import us.havanki.tamal.TheRandom;
import us.havanki.tamal.level.Level;
import us.havanki.tamal.level.tile.Tile;
import us.havanki.tamal.level.tile.Tiles;

public class Demo2 extends Canvas {
    private static final String SPRITE_SHEET_FILE = "/icons.png";
    private static final int SCREEN_WIDTH = 320;  // 20 tiles wide
    private static final int SCREEN_HEIGHT = 256;  // 16 tiles high
    private static final int SCALE = 3;

    BufferedImage image =
        new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                          BufferedImage.TYPE_INT_RGB);
    int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private static int tickCount = 0;

    public static void main(String[] args) throws IOException {
        SpriteSheet sheet = new SpriteSheet(ImageIO.read
            (Demo2.class.getResourceAsStream(SPRITE_SHEET_FILE)));
        Screen screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT, sheet);

        Demo2 demo = new Demo2();
        Dimension size = new Dimension(SCREEN_WIDTH * SCALE,
                                       SCREEN_HEIGHT * SCALE);
        demo.setMinimumSize(size);
        demo.setMaximumSize(size);
        demo.setPreferredSize(size);

        JFrame f = new JFrame("Demo2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());
        f.add(demo, BorderLayout.CENTER);
        f.pack();
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        BufferStrategy bs = demo.getBufferStrategy();
        if (bs == null) {
            demo.createBufferStrategy(3);
            demo.requestFocus();
            bs = demo.getBufferStrategy();
        }

        System.out.println("Constructing level");
        int lw = 20; int lh = 16; int ld = 0;
        byte[] tiles = new byte[lw * lh];
        TheRandom r = TheRandom.getInstance();
        for (int lx = 0; lx < lw; lx++) {
            for (int ly = 0; ly < lh; ly++) {
                byte id = Tiles.GRASS.getId();
                if (lx < 2 || lx > lw - 3) {
                    id = r.getRandom().nextBoolean() ? Tiles.ROCK.getId() :
                        Tiles.DIRT.getId();
                } else if (lx < 4 || lx > lw - 5) {
                    id = r.getRandom().nextBoolean() ? Tiles.SAND.getId() :
                        Tiles.WATER.getId();
                } else if (lx < 8 || lx > lw - 9) {
                    id = r.getRandom().nextBoolean() ? Tiles.WATER.getId() :
                        Tiles.DIRT.getId();
                }
                tiles[ly * lw + lx] = id;
            }
        }
        byte[] data = new byte[lw * lh];
        Arrays.fill(data, (byte) 0);
        Level level = new Level(lw, lh, ld, tiles, data);

        // ---

        long lastTime = System.nanoTime();
        double unprocessedTicks = 0.0;
        double nsPerTick = 1000000000.0 / 60;  // = 60 ticks per second
        int frames = 0;
        int ticks = 0;

        long perfTime = System.currentTimeMillis();

        while (true) {
            long now = System.nanoTime();
            unprocessedTicks += (now - lastTime) / nsPerTick;
            lastTime = now;
            // boolean shouldRender = true;
            while (unprocessedTicks >= 1.0) {
                ticks++;
                tick(level);
                unprocessedTicks -= 1.0;
                // shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // if (shouldRender) {
            if (true) {
                frames++;

                level.renderBackground(screen, 0, 0);

                for (int y = 0; y < screen.h(); y++) {
                    for (int x = 0; x < screen.w(); x++) {
                        SimpleColor color = screen.pixel(x, y);
                        if (!color.isTransparent()) {
                            int rgb = color.toRgb();
                            demo.pixels[x + y * SCREEN_WIDTH] = rgb;
                        }
                    }
                }

                Graphics g = bs.getDrawGraphics();
                g.fillRect(0, 0, demo.getWidth(), demo.getHeight());

                int ww = SCREEN_WIDTH * SCALE;
                int hh = SCREEN_HEIGHT * SCALE;
                int xo = (demo.getWidth() - ww) / 2;
                int yo = (demo.getHeight() - hh) / 2;
                g.drawImage(demo.image, xo, yo, ww, hh, null);
                g.dispose();
                bs.show();
            }

            if (System.currentTimeMillis() - perfTime > 1000) {
                perfTime += 1000;
                System.out.println(ticks + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public static void tick(Level level) {
        tickCount++;
        level.tick();
        Tile.incrementTickCount();
    }
}
