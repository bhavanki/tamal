package us.havanki.tamal.entity;

import java.util.List;
import us.havanki.tamal.item.Item;
import us.havanki.tamal.item.ResourceItem;
import us.havanki.tamal.res.Resource;

/**
 * The player's inventory.
 */
public class Inventory {
    private List<Item> items = new java.util.ArrayList<Item>();

    /**
     * Adds an item to the inventory.
     *
     * @param item item to add
     */
    public void add (Item item) {
	add (items.size(), item);
    }
    /**
     * Adds an item to a particular slot in the inventory. If the item holds
     * resources, it is merged into any existing same item.
     *
     * @param slot inventory slot to add into
     * @param item item to add
     */
    public void add (int slot, Item item) {
	if (item instanceof ResourceItem) {
	    ResourceItem ritem = (ResourceItem) item;
	    ResourceItem ritemInInv = findRI (ritem.getResource());
	    if (ritemInInv != null) {
		ritemInInv.changeAmount (ritem.getAmount());
	    } else {
		items.add (slot, item);
	    }
	} else {
	    items.add (slot, item);
	}
    }
    private ResourceItem findRI (Resource r) {
	for (Item i : items) {
	    if (!(i instanceof ResourceItem)) { continue; }
	    if (((ResourceItem) i).getResource() == r) {
		return (ResourceItem) i;
	    }
	}
	return null;
    }

    /**
     * Checks whether the inventory has enough of a resource.
     *
     * @param r resource to check for
     * @param amount amount to check
     * @return true if inventory has at least the given amount of a resource
     */
    public boolean hasSufficientResources (Resource r, int amount) {
	ResourceItem ritem = findRI (r);
	if (ritem == null) { return false; }
	return (ritem.getAmount() >= amount);
    }
    /**
     * Removes some amount of a resource from the inventory.
     *
     * @param r resource to deduct
     * @param amount amount to remove
     * @return true if resource was removed
     */
    public boolean removeResources (Resource r, int amount) {
	ResourceItem ritem = findRI (r);
	if (ritem == null) { return false; }
	if (ritem.getAmount() < amount) { return false; }
	ritem.changeAmount (-amount);
	if (ritem.getAmount() <= 0) {
	    items.remove (ritem);
	}
    return true;  // ????????????? check this
    }

    /**
     * Counts how many of an item is in the inventory.
     *
     * @param item item to count
     * @return count
     */
    public int count (Item item) {
	int count = 0;
	for (Item i : items) {
	    if (i.matches (item)) { count++; }
	}
	return count;
    }
    /**
     * Counts how much of a resource is in the inventory.
     *
     * @param ritem resource item to count
     * @return count
     */
    public int count (ResourceItem ritem) {
	ResourceItem ri = findRI (ritem.getResource());
	return (ri != null ? ri.getAmount() : 0);
    }
}
