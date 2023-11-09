package org.x00Hero.Menus.Components;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Main;
import org.x00Hero.Menus.MenuController;
import java.util.HashMap;
import java.util.List;

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
    public MenuItem addItem(ItemStack item, int slot) {
        if(slot < 0 || slot > slotLimit) slot = -1;
        return addItem(new MenuItem(item, slot));
    }
    public MenuItem addItem(MenuItem menuItem) {
        int itemSlot = menuItem.getSlot();
        if(itemSlot == -1 || itemSlot >= slotLimit) { // automatically slot it
            itemSlot = getAvailableSlot(); // returns -1 if none found
            if(itemSlot == -1) return menuItem; // no Slot found
            menuItem.setSlot(itemSlot);
            items.put(itemSlot, menuItem);
            if(itemSlot > biggestSlot) biggestSlot = itemSlot;
            parent.addToItemCount(menuItem.getItemBuilder().getAmount());
            return null;
        }
        return menuItem;
    }
    public MenuItem setItem(MenuItem menuItem) { return setItem(menuItem, menuItem.getSlot()); }
    public MenuItem setItem(MenuItem menuItem, int slot) {
        MenuItem itemAtSlot = items.getOrDefault(slot, null);
        String itemSlotName = itemAtSlot == null ? "null" : itemAtSlot.getItemBuilder().getName();
        Main.Log("Setting Item '" + menuItem.getItemBuilder().getName() + "' @ " + slot + " itemAtSlot? " + itemSlotName);
        //menuItem.setSlot(slot);
        items.put(slot, menuItem);
        if(inventory != null) inventory.setItem(slot, menuItem.getItemStack());
        return itemAtSlot;
    }
    private void RecursivelySetItem(MenuItem menuItem) { // for shifting the last row
        MenuItem toAdd = menuItem;
        while(toAdd != null) toAdd = setItem(toAdd, toAdd.getSlot() + 1);
    }
    public boolean canAddItem() {
        return allowItemAddition && !isFull();
    }

    public int getAvailableSlot() {
        int slot = -1;
        int curSlot = 0;
        while(slot == -1 && curSlot < slotLimit) {
            if(!items.containsKey(curSlot)) slot = curSlot;
            curSlot++;
        }
        Main.Log("Got Available Slot " + slot);
        return slot;
    }
    public boolean isSlotAvailable() { return getAvailableSlot() != -1; }
    public boolean isSlotAvailable(int slot) { return slot >= 0 && slot < slotLimit && !items.containsKey(slot); }

    public Menu getParent() { return parent; }
    public Page setParent(Menu parent) {
        this.parent = parent;
        return this;
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
    public int getItemCount() { return items.size(); }
    /**
     * Checks if this page is the last page in the parent menu.
     * @return True if it's the last page, false otherwise.
     */
    public boolean isLastPage() { return pageNumber == parent.getPageCount() - 1; }
    /**
     * Checks if this page is the last page in the parent menu.
     * @return True if it's the first page, false otherwise.
     */
    public boolean isFirstPage() { return pageNumber == 0; }
    /**
     * Checks if this page is the only page in the parent menu.
     * @return True if it's the only page, false otherwise.
     */
    public boolean isOnlyPage() { return isFirstPage() && isLastPage(); }
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

    public void createItems() { createItems(getAdjustedAmount(biggestSlot)); }
    public void createItems(int adjCount) {
        backSlot = !isFirstPage() ? biggestSlot < 5 ? adjCount - 5 : adjCount - 9 : -1;
        forwardSlot = !isLastPage() ? adjCount - 1 : -1;
        backItem = new MenuItem(Menu.backItemBuilder, backSlot);
        forwardItem = new MenuItem(Menu.forwardItemBuilder, forwardSlot);
        Main.Log("Creating Navigation Items @ " + backSlot + " " + forwardSlot);
    }

    public void open(Player player) { open(player, false); }
    public void open(Player player, boolean fillEmpty) {
        if(inventory == null || build) Build();
        player.openInventory((fillEmpty) ? fillInventory(inventory) : fillPastLimit(inventory));
        MenuController.setInMenus(player, this);
    }
    public void Build() {
        int itemCount = getItemCount();
        int adjItemCount = (itemCount % 9 == 0 && itemCount < 54) ? getAdjustedAmount(itemCount + 1) : getAdjustedAmount(itemCount);
        Main.Log("Building page " + pageNumber + " with " + getItemCount() + " items. (" + adjItemCount + ")");
        if(getItemCount() <= 5) inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', title));
        else inventory = Bukkit.createInventory(null, adjItemCount, ChatColor.translateAlternateColorCodes('&', title));
        createItems(adjItemCount);
        for(int curSlot = 0; curSlot < getAdjustedAmount(adjItemCount); curSlot++) {
            Main.Log("Checking slot " + curSlot);
            if(curSlot == backSlot) { RecursivelySetItem(setItem(backItem)); curSlot++; continue; } // shiftItems forward
            else if(curSlot == forwardSlot) { setItem(forwardItem); continue; }
            MenuItem menuItem = items.get(curSlot);
            if(menuItem == null || !menuItem.isEnabled()) continue;
            Main.Log("Adding Item '" + menuItem.getItemBuilder().getName() +"' @ " + curSlot + "(" + menuItem.getSlot() +") to page " + pageNumber);
            inventory.setItem(curSlot, menuItem.getItemStack());
        }
        build = false;
    }
    public static Inventory fillInventory(Inventory i) {
        for(int f = 0; f < i.getSize(); f++) {
            if(i.firstEmpty() != -1) {
                i.setItem(i.firstEmpty(), Menu.nothing.getItemStack());
            } else {
                f = i.getSize();
            }
        }
        return i;
    }
    public Inventory fillPastLimit(Inventory inventory) {
        for(int startSlot = slotLimit; startSlot < inventory.getSize(); startSlot++) if(startSlot != backSlot || startSlot != forwardSlot) inventory.setItem(startSlot, Menu.nothing.getItemStack());
        return inventory;
    }
}