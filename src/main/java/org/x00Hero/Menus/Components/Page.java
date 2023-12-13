package org.x00Hero.Menus.Components;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.MenuController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.x00Hero.Main.Log;

public class Page extends HashMap<Integer, MenuItem> {
    public String title;
    public boolean itemAddition = true, updated = false;
    private int slots;
    public int pageNumber, biggestSlot;
    private Menu parent;
    private HashMap<Integer, MenuItem> customNavigation = new HashMap<>();
    private HashMap<Integer, MenuItem> navigationItems = new HashMap<>();
    private InventoryType inventoryType = null;
    private PageType pageType = PageType.DYNAMIC;
    private Inventory inventory = null;
    public Page(int pageNumber) {
        this.pageNumber = pageNumber;
        setSlots(pageType.maxSlots);
        nav();
    }
    public Page(int pageNumber, int slots) {
        this.pageNumber = pageNumber;
        setSlots(slots);
        nav();
    }
    public Page(int pageNumber, int slots, String title) {
        this.pageNumber = pageNumber;
        setSlots(slots);
        setTitle(title);
        nav();
    }
    public Page(String title, InventoryType type) {
        setTitle(title);
        setSlots(pageType.maxSlots);
        this.inventoryType = type;
        nav();
    }
    public Page(String title) {
        setTitle(title);
        setSlots(pageType.maxSlots);
        nav();
    }
    public void nav() { customNavigation.put(45, new MenuItem(Menu.backItemBuilder.clone(), 45)); customNavigation.put(53, new MenuItem(Menu.forwardItemBuilder.clone(), 53));}
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isSlotAvailable() { return getAvailableSlot() != -1; }
    public int getAvailableSlot() {
        for (int curSlot = 0; curSlot < pageType.maxSlots; curSlot++) {
            if (containsKey(curSlot)) Log("Item Already @ " + curSlot);
            else if(customNavigation.containsKey(curSlot)) Log("Nav Item @ " + curSlot);
            else return curSlot;
        }
        return -1;
    }
    public static int getAdjustedAmount(Integer slots) { return slots <= 5 ? 5 : (int) (Math.ceil((double) slots / 9)) * 9; }
    public boolean isFull() { return size() >= slots; }
    public int getItemSlots() { return slots - navigationItems.size(); }
    public int getSize() { return getAdjustedAmount(biggestSlot); }
    public int getSlots() { return slots; }
    public void setSlots(int slots) { this.slots = slots; }
    public void setNavigationItem(MenuItem menuItem) { setNavigationItem(menuItem, menuItem.getSlot()); }
    public void setNavigationItem(MenuItem menuItem, int slot) {
        menuItem.setSlot(slot);
        navigationItems.put(slot, menuItem);
    }
    public void UpdateNavigationItems() { UpdateNavigationItems(getSize()); }
    private void UpdateNavigationItems(int adjustedCount) {
        navigationItems.clear();
        for(MenuItem navItem : customNavigation.values()) {
            int itemSlot = navItem.getSlot();
            if(itemSlot > adjustedCount) itemSlot = itemSlot - adjustedCount;
            navigationItems.put(itemSlot, navItem);
            Log("Setting NavItem @ " + itemSlot);
        }
    }
    public Collection<MenuItem> getNavigationItems() { return navigationItems.values(); }
    //public void setNavigationItems(List<MenuItem> navigationItems) { this.navigationItems = navigationItems; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public boolean removeItem(int slot) { return remove(slot) != null; }
    public MenuItem getItem(int slot) {
        MenuItem item;
        return (item = get(slot)) == null ? navigationItems.get(slot) : item; }
    public MenuItem addItem(ItemStack itemStack) { return addItem(new MenuItem(itemStack)); }
    public MenuItem addItem(ItemStack itemStack, int slot) { return addItem(new MenuItem(itemStack, slot)); }
    public MenuItem addItem(MenuItem menuItem) {
        if(isFull() || !itemAddition) return menuItem;
        int itemSlot = menuItem.getSlot();
        if(itemSlot == -1 || itemSlot >= pageType.maxSlots) {
            int available = getAvailableSlot();
            if(available != -1) {
                itemSlot = available;
                menuItem.setSlot(available);
            }
            else return menuItem;
        }
        int rowsNow = getSize() / 9;
        int adjustedSlots = getAdjustedAmount(itemSlot);
        int rowsRequired = adjustedSlots / 9;
        if(rowsRequired > rowsNow) UpdateNavigationItems(adjustedSlots);
        Log("Rows: " + rowsNow + " Required: " + rowsRequired + " Slot: " + itemSlot);
        Log("Adding Item @ " + pageNumber);
        if(itemSlot > biggestSlot) biggestSlot = itemSlot;
        return setItem(menuItem);
    }
    public MenuItem setItem(MenuItem menuItem) { return setItem(menuItem, menuItem.getSlot()); }
    public MenuItem setItem(MenuItem menuItem, int slot) { if(inventory != null) inventory.setItem(slot, menuItem); Log("Setting " + menuItem + " @ " + slot); return put(slot, menuItem); }
    public InventoryType getInventoryType() {
        if(inventoryType != null) return inventoryType;
        int slots = getSize();
        if(slots <= 1) return InventoryType.LECTERN; // 1 slotter
        if(slots <= 5) return InventoryType.HOPPER; // 5 slotter
        if(slots == 9) return InventoryType.DISPENSER; // 3x3
        else return null;
    }
    public boolean isFirstPage() { return pageNumber == 0; }
    public boolean isLastPage() { return pageNumber == parent.getPageCount() - 1; }
    public boolean isOnlyPage() { return isFirstPage() && isLastPage(); }
    public void CreateInventory() {
        Inventory inventory;
        InventoryType type = getInventoryType();
        if(type == null) inventory = Bukkit.createInventory(null, getSize(), title);
        else inventory = Bukkit.createInventory(null, type, title);
        this.inventory = inventory;
        PageItems();
    }
    public void PageItems() {
        if(inventory == null) return;
        UpdateNavigationItems();
        for(MenuItem menuItem : values()) {
            Log("Setting " + menuItem + " @ " + menuItem.getSlot());
            inventory.setItem(menuItem.getSlot(), menuItem);
        }
        for(MenuItem navItem : navigationItems.values()) {
            Log("Setting NavItem " + navItem + " @ " + navItem.getSlot());
            inventory.setItem(navItem.getSlot(), navItem);
        }
    }
    public Inventory getInventory() { if(inventory == null || updated) CreateInventory(); return inventory; }
    public void open(Player player) {
        player.openInventory(getInventory());
        MenuController.setInMenus(player, this);
    }
    public Menu getParent() { return parent; }
    public Page setParent(Menu menu) {
        this.parent = menu;
        return this;
    }
}
