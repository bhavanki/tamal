package us.havanki.tamal;

import java.awt.Canvas;
import java.io.IOException;
import javax.imageio.ImageIO;
import us.havanki.tamal.gfx.Screen;
import us.havanki.tamal.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {

    private static final double NS_PER_TICK = 1000000000.0 / 60;  // 60 ticks/s
    private static final long PRE_RENDER_DELAY = 2L;  // 2 ms = 2000 ns

    private static final String SPRITE_SHEET_FILE = "/icons.png";
    private static final int SCREEN_WIDTH = 160;
    private static final int SCREEN_HEIGHT = 120;

    private static int[] colors = new int [256];

    static {
	// Fill in the color map. Recall that each color is represented as
	// a byte value, in hexal, indicating one of 216 possible colors.
	// Because of this, the color values increment by blue place, then green
	// place, then red place, so if we loop that way, we can just increment.
	int p = 0;
	for (int r = 0; r < 6; r++) {
	    int rr = 51 * r;
	    for (int g = 0; g < 6; g++) {
		int gg = 51 * g;
		for (int b = 0; b < 6; b++) {
		    int bb = 51 * b;
		    // The RGB values are now in the range 0 - 255, e.g.,
		    // one byte each.
		    // Calculate the luminance or grayscale value of the pixel.
		    int lum = (30 * rr + 59 * gg + 11 * bb) / 100;
		    // Now for some reason adjust the RGB values by averaging
		    // them with the luminance. Then, for yet some other reason,
		    // adjust the scale of the RGB values so that instead of
		    // 0 - 255, they are 10 - 240.
		    int r1 = ((rr + lum) / 2) * 230 / 255 + 10;
		    int g1 = ((gg + lum) / 2) * 230 / 255 + 10;
		    int b1 = ((bb + lum) / 2) * 230 / 255 + 10;
		    // Finally, encode the RGB color as an integer according
		    // to the Java DirectColorModel.
		    colors[p++] = (r1 << 16) | (g1 << 8) | b1;
		}
	    }
	}
    }

    private boolean running = false;
    private Screen screen;

    private void init() throws IOException {
	// Create the screen.
	// SpriteSheet sheet = ImageIO.read
	//     (Game.class.getResourceAsStream (SPRITE_SHEET_FILE));
	// screen = new Screen (SCREEN_WIDTH, SCREEN_HEIGHT, sheet);

	// resetGame();
	// setMenu (new TitleMenu());
    }

    public void run() {
	long lastTime = System.nanoTime();
	double unprocessed = 0.0;  // number of unprocessed ticks

	int ticks = 0;  // number of ticks issued
	int frames = 0;  // number of frames rendered
	long fpsTime = System.currentTimeMillis();

	try {
	init();
	} catch (IOException exc) {
		// do something!
	}

	while (running) {
	    long now = System.nanoTime();
	    unprocessed += (now - lastTime) / NS_PER_TICK;
	    lastTime = now;

	    while (unprocessed >= 1.0) {
		ticks++;
		// tick();
		unprocessed -= 1.0;
	    }

	    try {
		Thread.sleep (PRE_RENDER_DELAY);
	    } catch (InterruptedException exc) {
		return;
	    }

	    frames++;
	    // render();

	    if (System.currentTimeMillis() - fpsTime > 1000L) {
		fpsTime += 1000L;
		System.out.println
		    (ticks + " ticks, " + frames + " fps");
		ticks = 0;
		frames = 0;
	    }
	}

    }

}
