package us.havanki.tamal.level.tile;

import java.util.Set;
import us.havanki.tamal.entity.Entity;

public class TestTile extends Tile {

    private Set<TilePos> ps = new java.util.HashSet<TilePos>();

    public TestTile(int id) { super(id); }

    @Override public void tick(TilePos p) { ps.add(p); }
    public Set<TilePos> getTickPositions() { return ps; }
    public int getTheTickCount() { return Tile.getTickCount(); }
}
