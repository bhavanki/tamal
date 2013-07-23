package us.havanki.tamal.entity;

import us.havanki.tamal.item.Item;
import us.havanki.tamal.item.ResourceItem;
import us.havanki.tamal.res.Resource;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory i;
    @Before public void setUp() {
        i = new Inventory();
    }

    private Item mockItem() {
        Item item = createMock(Item.class);
        expect(item.matches(item)).andReturn(true);
        expectLastCall().anyTimes();
        expect(item.matches(and(anyObject(Item.class), not(eq(item)))))
            .andReturn(false);
        expectLastCall().anyTimes();
        replay(item);
        return item;
    }
    private Resource mockResource(String name) {
        Resource r = createMock(Resource.class);
        expect(r.getName()).andReturn(name);
        expectLastCall().anyTimes();
        replay(r);
        return r;
    }
    private ResourceItem mockResourceItem(Resource r) {
        ResourceItem item = createMock(ResourceItem.class);
        expect(item.matches(item)).andReturn(true);
        expectLastCall().anyTimes();
        expect(item.matches(and(anyObject(Item.class), not(eq(item)))))
            .andReturn(false);
        expectLastCall().anyTimes();
        expect(item.getResource()).andReturn(r);
        expectLastCall().anyTimes();
        return item;
    }

    @Test public void testAdd() {
        Item item1 = mockItem();

        assertEquals(0, i.size());
        assertEquals(0, i.count(item1));
        i.add(item1);
        assertEquals(1, i.size());
        assertEquals(1, i.count(item1));
        i.add(item1);
        assertEquals(2, i.size());
        assertEquals(2, i.count(item1));
    }
    @Test public void testAddDifferent() {
        Item item1 = mockItem();
        Item item2 = mockItem();

        i.add(item1).add(item2);
        assertEquals(2, i.size());
        assertEquals(1, i.count(item1));
        assertEquals(1, i.count(item2));
        i.add(item1);
        assertEquals(3, i.size());
        assertEquals(2, i.count(item1));
        assertEquals(1, i.count(item2));
    }
    @Test public void testGet() {
        Item item1 = mockItem();
        Item item2 = mockItem();
        Item item3 = mockItem();

        i.add(item1).add(item2).add(item1).add(item3);
        assertEquals(item1, i.get(0));
        assertEquals(item2, i.get(1));
        assertEquals(item1, i.get(2));
        assertEquals(item3, i.get(3));
    }
    @Test public void testFind() {
        Item item1 = mockItem();
        Item item2 = mockItem();
        Item item3 = mockItem();

        i.add(item2).add(item1).add(item3).add(item1).add(item1);
        assertEquals(0, i.find(item2));
        assertEquals(1, i.find(item1));
        assertEquals(2, i.find(item3));
    }
    @Test public void testRemove() {
        Item item1 = mockItem();
        Item item2 = mockItem();
        Item item3 = mockItem();

        i.add(item1).add(item2).add(item3);
        i.remove(1);
        assertEquals(2, i.size());
        assertEquals(0, i.find(item1));
        assertEquals(1, i.find(item3));
        assertEquals(-1, i.find(item2));
    }

    // ---

    @Test public void testAddResourceItem_NoMerge() {
        Resource r1 = mockResource("gold");
        Resource r2 = mockResource("silver");
        ResourceItem item1 = mockResourceItem(r1);
        replay(item1);
        ResourceItem item2 = mockResourceItem(r2);
        replay(item2);

        i.add(item1);
        assertEquals(1, i.size());
        assertEquals(1, i.count(item1));
        i.add(item2);
        assertEquals(2, i.size());
        assertEquals(1, i.count(item1));
        assertEquals(1, i.count(item2));
    }
    @Test public void testAddResourceItem_Merge() {
        Resource r1 = mockResource("gold");
        Resource r2 = mockResource("silver");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.changeAmount(5)).andReturn(15);
        replay(item1);
        ResourceItem item2 = mockResourceItem(r2);
        replay(item2);
        ResourceItem item3 = mockResourceItem(r1);
        expect(item3.getAmount()).andReturn(5);
        replay(item3);

        i.add(item1);
        i.add(item2);
        i.add(item3);
        assertEquals(2, i.size());
    }
    @Test public void testHasSufficientResources() {
        Resource r1 = mockResource("gold");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.getAmount()).andReturn(10);
        expectLastCall().anyTimes();
        replay(item1);

        i.add(item1);
        assertTrue(i.hasSufficientResources(r1, 5));
        assertTrue(i.hasSufficientResources(r1, 10));
        assertFalse(i.hasSufficientResources(r1, 11));

        Resource r2 = mockResource("silver");
        assertTrue(i.hasSufficientResources(r2, 0));
        assertFalse(i.hasSufficientResources(r2, 1));
    }
    @Test public void testRemoveResources_MoreThanEnough() {
        Resource r1 = mockResource("gold");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.getAmount()).andReturn(15);
        expect(item1.changeAmount(-10)).andReturn(5);
        expect(item1.getAmount()).andReturn(5);
        replay(item1);

        i.add(item1);
        assertTrue(i.removeResources(r1, 10));
        verify(item1);
        assertEquals(0, i.find(item1));
    }
    @Test public void testRemoveResources_JustEnough() {
        Resource r1 = mockResource("gold");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.getAmount()).andReturn(10);
        expect(item1.changeAmount(-10)).andReturn(0);
        expect(item1.getAmount()).andReturn(0);
        replay(item1);

        i.add(item1);
        assertTrue(i.removeResources(r1, 10));
        verify(item1);
        assertEquals(-1, i.find(item1));
    }
    @Test public void testRemoveResources_NotEnough() {
        Resource r1 = mockResource("gold");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.getAmount()).andReturn(5);
        replay(item1);

        i.add(item1);
        assertFalse(i.removeResources(r1, 10));
        verify(item1);
        assertEquals(0, i.find(item1));
    }
    @Test public void testRemoveResources_NotThere() {
        Resource r1 = mockResource("gold");
        assertFalse(i.removeResources(r1, 10));
    }

    @Test public void testCountResource() {
        Resource r1 = mockResource("gold");
        ResourceItem item1 = mockResourceItem(r1);
        expect(item1.getAmount()).andReturn(10);
        replay(item1);

        i.add(item1);
        assertEquals(10, i.countResource(r1));
    }
    @Test public void testCountResource_NotThere() {
        Resource r1 = mockResource("gold");
        assertEquals(0, i.countResource(r1));
    }
}
