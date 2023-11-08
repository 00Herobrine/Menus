package org.x00Hero.Components;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.x00Hero.Components.Menu.nothing;

public class Page {
    private HashMap<Integer, MenuItem> items = new HashMap<>();
    private String title;
    private Inventory inventory;
    private boolean allowItemAddition = true, build = false;
    private int slotLimit = 54, curSlot = 0; // for autoAdding
    public int pageNumber, backSlot, forwardSlot, biggestSlot;
    private Menu parent;
    public MenuItem backItem, forwardItem;

    public Page(int pageNumber, String title, int slotLimit) {
        this.pageNumber = pageNumber;
        this.title = title;
        this.slotLimit = slotLimit;
    }
    public Page(int pageNumber, String title) {
        this.pageNumber = pageNumber;
        this.title = title;
        //this.inventory = Bukkit.createInventory(null, 54, title);
    }

    public List<MenuItem> addItems(List<MenuItem> menuItems) {
        for (MenuItem item : menuItems) {
            MenuItem result = addItem(item);
            menuItems.remove(item);
            if(result != null) break;
        }
        return menuItems;
    }
    public MenuItem addItem(MenuItem menuItem) {
        int itemSlot = menuItem.getSlot();
        if(itemSlot == -1 || itemSlot >= slotLimit) { // automatically slot it
            itemSlot = getAvailableSlot(); // returns -1 if none found
            if(itemSlot == -1) return menuItem;
            menuItem.setSlot(itemSlot);
            items.put(itemSlot, menuItem);
            parent.addToItemCount();
            return null;
        }
        return menuItem;
    }
    public int getAvailableSlot() {
        int slot = -1;
        int curSlot = 0;
        while(slot == -1 && curSlot < slotLimit) {
            if(!items.containsKey(curSlot)) slot = curSlot;
            curSlot++;
        }
        return slot;
    }
    public boolean isSlotAvailable() {
        return getAvailableSlot() != -1;
    }
    public boolean isSlotAvailable(int slot) {
        return slot >= 0 && slot < slotLimit && !items.containsKey(slot);
    }

    public Menu getParent() {
        return parent;
    }
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void addItem(int slot, ItemStack item) {
        if (slot < 0 || slot >= inventory.getSize()) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, item);
                    break;
                }
            }
        } else {
            inventory.setItem(slot, item);
        }
        if(slot > biggestSlot) biggestSlot = slot;
        items.put(slot, new MenuItem(item, slot));
    }
    public boolean canAddItem() {
        return allowItemAddition && !isFull();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAddingItemsEnabled() {
        return allowItemAddition;
    }
    public void setAddingItemsEnabled(boolean allowItemAddition) {
        this.allowItemAddition = allowItemAddition;
    }

    public boolean isFull() { // the issue stems from here when the initial page is created it doesn't know that there should be future pages. So isOnlyPage is run on the initialPage
        int mod = 2;
        if(isOnlyPage() && !parent.isInitial()) mod = 0;
        else if(isLastPage() || isFirstPage()) mod = 1;
        return (items.size() >= slotLimit - mod);
    }

    public void Updated() {
        build = true;
    }

    /**
     * Calculates the adjusted slot count based on the number of menu items.
     * Ensures that the slot count is a multiple of 9 for proper inventory layout.
     * @param slots The current number of slots needed based on menu items.
     * @return The adjusted slot count that is a multiple of 9 or 5 if slots is fewer than 5.
     */
    public int getAdjustedAmount(Integer slots) {
        if(slots <= 5) return 5;
        else return (int) (Math.ceil((double) slots / 9)) * 9;
    }

    /**
     * @return The amount of items in the current page.
     */
    public int getItemCount() {
        return items.size();
    }
    /**
     * Checks if this page is the last page in the parent menu.
     * @return True if it's the last page, false otherwise.
     */
    public boolean isLastPage() {
        return pageNumber == parent.getPageCount();
    }
    /**
     * Checks if this page is the last page in the parent menu.
     * @return True if it's the first page, false otherwise.
     */
    public boolean isFirstPage() {
        return pageNumber == 1;
    }
    /**
     * Checks if this page is the only page in the parent menu.
     * @return True if it's the only page, false otherwise.
     */
    public boolean isOnlyPage() {
        return isFirstPage() && isLastPage();
    }
    public Page getNextPage() {
        return parent.getPage(pageNumber + 1);
    }
    public Page getPreviousPage() {
        return parent.getPage(pageNumber - 1);
    }
    public MenuItem getItemInSlot(int slot) {
        return items.get(slot);
    }
    public Inventory getInventory() { return inventory; }
    public List<MenuItem> getMenuItems() { return items.values().stream().toList(); }

    public void createItems() {
        int adjCount = getAdjustedAmount(biggestSlot);
        backSlot = biggestSlot < 5 ? adjCount - 5 : adjCount - 9;
        forwardSlot = adjCount - 1;
        backItem = new MenuItem(Menu.backItemBuilder, -1, backSlot);
        forwardItem = new MenuItem(Menu.forwardItemBuilder, -1, forwardSlot);
    }
    public void Build(boolean thingy) {
        if(getItemCount() <= 5) inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', title));
        else inventory = Bukkit.createInventory(null, getAdjustedAmount(getItemCount()), ChatColor.translateAlternateColorCodes('&', title));
        createItems();
        for(MenuItem menuItem : items.values()) {
            int itemSlot = menuItem.getSlot();
            inventory.setItem(itemSlot, menuItem.getItemStack());
        }
    }
    public void open(Player player) {
        if(inventory == null || build) Build();
        player.openInventory(inventory);
        //MenuController.setInMenus(player, this); for event handling
    }
    public void open(Player player, boolean fillEmpty) {
        if(inventory == null || build) Build();
        player.openInventory((fillEmpty) ? fillInventory(inventory) : fillPastLimit(inventory));
        //MenuController.setInMenus(player, this); for event handling
    }
    public int getBackSlot() {
        return 0;
    }
    public int getForwardSlot() {
        return 0;
    }

    public static Inventory fillInventory(Inventory i) {
        for(int f = 0; f < i.getSize(); f++) { // turn this into a function
            if(i.firstEmpty() != -1) {
                i.setItem(i.firstEmpty(), nothing.getItemStack());
            } else {
                f = i.getSize();
            }
        }
        return i;
    }
    public Inventory fillPastLimit(Inventory inventory) {
        Inventory newInv = inventory;
        for(int startSlot = slotLimit; startSlot < inventory.getSize(); startSlot++) if(startSlot != backSlot || startSlot != forwardSlot) newInv.setItem(startSlot, nothing.getItemStack());
        return newInv;
    }
    public void Build() {
        if(getItemCount() <= 5) inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', title));
        else inventory = Bukkit.createInventory(null, getAdjustedAmount(getItemCount()), ChatColor.translateAlternateColorCodes('&', title));
        curSlot = inventory.firstEmpty();
        List<MenuItem> overfill = new ArrayList<>();
        createItems();
        //Main.main.getLogger().info("curSlot: " + curSlot + " backSlot: " + backSlot + " forwardSlot: " + forwardSlot);
        for(MenuItem menuItem : items.values()) {
            int itemSlot = menuItem.getSlot();
            if(curSlot == backSlot && (!isFirstPage() && !isOnlyPage())) { inventory.setItem(backSlot, backItem.getItemStack()); curSlot++; } // shiftItem forward; Main.main.getLogger().info("On backSlot, shifting forward");
            else if(curSlot == forwardSlot && (!isLastPage() && !isOnlyPage())) { overfill.add(menuItem); inventory.setItem(forwardSlot, forwardItem.getItemStack()); continue; }
            if(itemSlot == -1) itemSlot = curSlot;
            menuItem.setSlot(itemSlot);
            if(!menuItem.isEnabled()) continue;
            else curSlot++;
            if(curSlot >= slotLimit) curSlot = -1;
            else inventory.setItem(menuItem.getSlot(), menuItem.getItemStack());
        }
        for(MenuItem toPage : overfill) parent.AddOverfillItem(toPage, this);
        build = false;
    }
}