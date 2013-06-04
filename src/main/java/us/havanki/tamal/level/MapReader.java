package us.havanki.tamal.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import us.havanki.tamal.level.tile.Tiles;

/**
 * A reader of map information.
 */
public class MapReader {

    private Reader r;

    /**
     * Creates a new map reader.
     *
     * @param r reader for map information
     */
    public MapReader (Reader r) {
	this.r = r;
    }

    /**
     * Reads map information and generates a level from it.
     *
     * @param depth level depth
     */
    public Level readLevel (int depth) throws IOException {
	int w = 0, h = 0;
	List<byte[]> rows = new java.util.ArrayList<byte[]>();
	BufferedReader br = new BufferedReader (r);
	try {
	    String line;
	    while ((line = br.readLine()) != null) {
		if (w == 0) { w = line.length(); }
		byte[] row = new byte [w];
		for (int i = 0; i < w; i++) {
		    char c = line.charAt (i);
		    switch (c) {
		    case 'x': row[i] = Tiles.ROCK.getId(); break;
		    case '_': row[i] = Tiles.WATER.getId(); break;
		    default: row[i] = Tiles.GRASS.getId();
		    }
		}
		rows.add (row);
		h++;
	    }
	} finally {
	    br.close();
	}

	byte[] ids = new byte [w * h];
	for (int i = 0; i < rows.size(); i++) {
	    byte[] row = rows.get (i);
	    System.arraycopy (row, 0, ids, h * i, w);
	}
	byte[] data = new byte [w * h];
	return new Level (w, h, depth, ids, data);
    }

}