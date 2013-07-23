package us.havanki.tamal.entity;

import java.util.List;
import us.havanki.tamal.item.Item;
import us.havanki.tamal.item.ResourceItem;
import us.havanki.tamal.res.Resource;

/**
 * An entity's inventory.
 */
public class Inventory {
    private List<Item> items = new java.util.ArrayList<Item>();

    /**
     * Gets the size of (number of slots in) the inventory.
     *
     * @return size
     */
    public int size() { return items.size(); }
    /**
     * Gets the item in the given slot.
     *
     * @param slot inventory slot to look in
     * @return item in slot, or null if slot is empty or outside inventory size
     */
    public Item get(int slot) {
        if (slot < 0 || slot > items.size()) { return null; }
        return items.get(slot);
    }
    /**
     * Finds a matching item in the inventory.
     *
     * @param item item to find
     * @return slot of first matching item found, or -1 if no matches
     */
    public int find(Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).matches(item)) { return i; }
        }
        return -1;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item item to add
     * @return this inventory
     */
    public Inventory add (Item item) {
        return add (items.size(), item);
    }
    /**
     * Adds an item to a particular slot in the inventory.
     *
     * @param slot inventory slot to add into
     * @param item item to add
     * @return this inventory
     */
    public Inventory add (int slot, Item item) {
        items.add (slot, item);
        return this;
    }
    /**
     * Adds a resource item to the inventory.
     *
     * @param item resource item to add
     * @return this inventory
     */
    public Inventory add (ResourceItem item) {
        return add (items.size(), item);
    }
    /**
     * Adds a resource item to a particular slot in the inventory. If any other
     * item in the inventory already holds the same resource, the items are
     * merged.
     *
     * @param slot inventory slot to add into
     * @param item item to add
     * @return this inventory
     */
    public Inventory add (int slot, ResourceItem item) {
        ResourceItem ritem = (ResourceItem) item;
        ResourceItem ritemInInv = findRI (ritem.getResource());
        if (ritemInInv != null) {
            ritemInInv.changeAmount (ritem.getAmount());
        } else {
            items.add (slot, item);
        }
        return this;
    }
    private ResourceItem findRI (Resource r) {
        for (Item i : items) {
            if (!(i instanceof ResourceItem)) { continue; }
            if (((ResourceItem) i).getResource().equals(r)) {
                return (ResourceItem) i;
            }
        }
        return null;
    }

    /**
     * Removes an item from a particular slot in the inventory.
     *
     * @param slot inventory slot to remove from
     * @return the item that was in the slot
     * @throws ArrayIndexOutOfBoundsException if the slot is invalid
     */
    public Item remove(int slot) {
        if (slot < 0 || slot >= items.size()) {
            throw new ArrayIndexOutOfBoundsException(slot);
        }
        return items.remove(slot);
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
        if (ritem == null) { return (amount == 0); }
        return (ritem.getAmount() >= amount);
    }
    /**
     * Removes some amount of a resource from the inventory.
     *
     * @param r resource to deduct
     * @param amount amount to remove
     * @return true if amount was removed, false if not enough available
     */
    public boolean removeResources (Resource r, int amount) {
        ResourceItem ritem = findRI (r);
        if (ritem == null) { return false; }
        if (ritem.getAmount() < amount) { return false; }
        ritem.changeAmount (-amount);
        if (ritem.getAmount() <= 0) {  // could just be == 0
            items.remove (ritem);
        }
        return true;
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
     * @param r resource to count
     * @return count
     */
    public int countResource (Resource r) {
        ResourceItem ri = findRI(r);
        return (ri != null ? ri.getAmount() : 0);
    }
}
