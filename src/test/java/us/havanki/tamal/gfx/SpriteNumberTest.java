package us.havanki.tamal.gfx;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.easymock.Capture;
import static org.easymock.EasyMock.*;

public class SpriteNumberTest {

    private static final int N = 10;

    private SpriteSheet sheet;
    private SpriteNumber n;

    @Before public void setUp() throws Exception {
        sheet = createMock(SpriteSheet.class);
        n = new SpriteNumber(N, sheet);
    }
    @Test public void testGetNumber() {
        assertEquals(N, n.getNumber());
    }
    @Test public void testCoordinates() {
        expect(sheet.getWidthInSprites()).andReturn(8);
        expectLastCall().anyTimes();
        replay(sheet);
        assertEquals(2, n.xs());
        assertEquals(1, n.ys());
    }
    @Test public void testOffset() {
        expect(sheet.getWidthInSprites()).andReturn(8);
        expectLastCall().anyTimes();
        expect(sheet.getSpriteOffset(2, 1)).andReturn(12345);
        replay(sheet);
        assertEquals(12345, n.getOffset());
    }
}
