package us.havanki.tamal.gfx;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Demo extends Canvas {
    private static final String SPRITE_SHEET_FILE = "/icons.png";
    private static final int SCREEN_WIDTH = 160;
    private static final int SCREEN_HEIGHT = 120;
    private static final int SCALE = 3;

    BufferedImage image =
        new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                          BufferedImage.TYPE_INT_RGB);
    int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public static void main(String[] args) throws IOException {
        SpriteSheet sheet = new SpriteSheet(ImageIO.read
            (Demo.class.getResourceAsStream(SPRITE_SHEET_FILE)));
        Screen screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT, sheet);

        Demo demo = new Demo();
        Dimension size = new Dimension(SCREEN_WIDTH * SCALE,
                                       SCREEN_HEIGHT * SCALE);
        demo.setMinimumSize(size);
        demo.setMaximumSize(size);
        demo.setPreferredSize(size);

        JFrame f = new JFrame("Demo");
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

        System.out.println("Rendering sprite");
        screen.render(20, 20, 0,
                      new SimpleColorSet(SimpleColor.fromHexal(20),
                                         SimpleColor.fromHexal(20),
                                         SimpleColor.fromHexal(121),
                                         SimpleColor.fromHexal(121)), 0);

        for (int y = 0; y < screen.h(); y++) {
            for (int x = 0; x < screen.w(); x++) {
                SimpleColor color = screen.pixel(x, y);
                if (!color.isTransparent()) {
                    int rgb = color.toRgb();
                    demo.pixels[x + y * SCREEN_WIDTH] = rgb;
                }
            }
        }

        demo.pixels[0] = 1234;
        demo.pixels[1] = 2345;
        demo.pixels[2] = 3456;

        while (true) {
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
    }
}
