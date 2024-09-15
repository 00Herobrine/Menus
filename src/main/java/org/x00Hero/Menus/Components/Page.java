package org.x00Hero.Menus.Components;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.MenuController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import static org.x00Hero.Main.Log;

public class Page extends HashMap<Integer, MenuItem> {
    public String title;
    public boolean itemAddition = true, updated = false;
    private int slots;
    public int pageNumber, biggestSlot;
    private Menu parent;
    private HashMap<Integer, NavigationItem> customNavigation = new HashMap<>(); // Custom Nav Items supplied
    private HashMap<Integer, NavigationItem> navigationItems = new HashMap<>(); // what is actually displayed
    private InventoryType inventoryType = null;
    private PageType pageType = PageType.DYNAMIC;
    private Inventory inventory = null;
    public Page(int pageNumber) {
        this.pageNumber = pageNumber;
        setSlots(pageType.maxSlots);
        setNavigationItems();
    }
    public Page(int pageNumber, int slots) {
        this.pageNumber = pageNumber;
        setSlots(slots);
        setNavigationItems();
    }
    public Page(int pageNumber, int slots, String title) {
        this.pageNumber = pageNumber;
        setSlots(slots);
        setTitle(title);
        setNavigationItems();
    }
    public Page(String title, InventoryType type) {
        setTitle(title);
        setSlots(pageType.maxSlots);
        this.inventoryType = type;
        setNavigationItems();
    }
    public Page(String title) {
        setTitle(title);
        setSlots(pageType.maxSlots);
        setNavigationItems();
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isSlotAvailable() { return getAvailableSlot() != -1; }
    public int getAvailableSlot() {
        for (int curSlot = 0; curSlot < pageType.maxSlots; curSlot++)
            if(!containsKey(curSlot) && !navigationItems.containsKey(curSlot)) return curSlot;
        return -1;
    }
    public static int getAdjustedAmount(Integer slots) { return slots <= 5 ? 5 : (int) (Math.ceil((double) slots / 9)) * 9; }
    public boolean isFull() {
        int mod = 2;
        if(isOnlyPage() && !isInitial()) mod = 0;
        else if(isLastPage() || isFirstPage()) mod = 1;
        return getItemCount() >= slots - mod || getItemCount() >= pageType.maxSlots;
        //return size() >= slots;
    }
    public int getItemCount() { return keySet().size(); }
    public int getItemSlots() { return slots - navigationItems.size(); }
    public int getSize() { return getAdjustedAmount(biggestSlot + 1); }
    public int getSlots() { return slots; }
    public void setSlots(int slots) { this.slots = slots; }
    public void setNavigationItem(NavigationItem navItem) { setNavigationItem(navItem, navItem.getSlot()); }
    public void setNavigationItem(NavigationItem navItem, int slot) {
        navItem.setSlot(slot);
        navigationItems.put(slot, navItem);
    }
    public void setNavigationItems() {
        customNavigation.clear();
        Log("Setting Nav items " + pageNumber);
        if(!isFirstPage() && !isInitial()) {
            NavigationItem navigationItem = new NavigationItem(new MenuItem(Menu.backItemBuilder, 45, this), -1);
            customNavigation.put(45, navigationItem);
            Log("Setting Backwards @ " + navigationItem.getSlot() + " page: " + navigationItem.getPage());
        }
        if(!isLastPage() && (!isOnlyPage() || isInitial())) {
            NavigationItem navigationItem = new NavigationItem(new MenuItem(Menu.forwardItemBuilder, 53, this), 1);
            customNavigation.put(53, navigationItem);
            Log("Setting Forward @ " + navigationItem.getSlot() + " page: " + navigationItem.getPage());
        }
        updateNavigationItems(getSize());
    }
    public void updateNavigationItems() { updateNavigationItems(getSize()); }
    private void updateNavigationItems(int adjustedCount) {
        navigationItems.clear();
        Log("Updating Nav Items on page " + pageNumber + " with count " + adjustedCount);
        for(NavigationItem navItem : customNavigation.values()) {
            int itemSlot = navItem.getSlot();
            int originalSlots = getAdjustedAmount(itemSlot + 1);
            Log("Original Slot: " + itemSlot + " slots: " + originalSlots);
            if(itemSlot > adjustedCount) itemSlot = itemSlot - (originalSlots - adjustedCount);
            navItem.setSlot(itemSlot);
            navigationItems.put(itemSlot, navItem);
            Log("Updating '" + navItem.getName() + "' to " + itemSlot + " @ " + pageNumber);
        }
    }
    public Collection<NavigationItem> getNavigationItems() { return navigationItems.values(); }
    //public void setNavigationItems(List<MenuItem> navigationItems) { this.navigationItems = navigationItems; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public boolean removeItem(int slot) { return remove(slot) != null; }
    public NavigationItem getNavigationItem(int slot) { return navigationItems.get(slot); }
    public MenuItem getItem(int slot) {
        MenuItem item;
        return (item = getNavigationItem(slot)) == null ? get(slot) : item;
    }
    public MenuItem addItem(ItemStack itemStack) { return addItem(new MenuItem(itemStack)); }
    public MenuItem addItem(ItemStack itemStack, int slot) { return addItem(new MenuItem(itemStack, slot)); }
    public MenuItem addItem(MenuItem menuItem) {
        if(isFull() || !itemAddition) return menuItem;
        int itemSlot = menuItem.getSlot();
        if(itemSlot == -1 || itemSlot >= pageType.maxSlots) {
            int available = getAvailableSlot();
            if(available == -1) return menuItem;
            itemSlot = available;
            menuItem.setSlot(available);
        }
        int rowsNow = getSize() / 9;
        if(itemSlot > biggestSlot) biggestSlot = itemSlot;
        int rowsRequired = getAdjustedAmount(itemSlot + 1) / 9;
        menuItem.setParent(this);
        if(rowsRequired > rowsNow) updateNavigationItems(itemSlot + 1);
        //Log("Rows: " + rowsNow + " Required: " + rowsRequired + " Slot: " + itemSlot);
        //Log("Adding Item @ " + pageNumber);
        return setItem(menuItem);
    }
    public MenuItem setItem(MenuItem menuItem) { return setItem(menuItem, menuItem.getSlot()); }
    public MenuItem setItem(MenuItem menuItem, int slot) {
        if(inventory != null && slot < inventory.getSize())
            inventory.setItem(slot, menuItem);
        return put(slot, menuItem);
    }
    public InventoryType getInventoryType() {
        if(inventoryType != null) return inventoryType;
        int slots = getSize();
        if(slots <= 1) return InventoryType.LECTERN; // 1 slotter
        if(slots <= 5) return InventoryType.HOPPER; // 5 slotter
        if(slots == 9) return InventoryType.DISPENSER; // 3x3
        else return null;
    }
    public Set<Integer> getNavItemSlots() { return navigationItems.keySet(); }
    public boolean isInitial() { return parent == null || parent.isInitial(); }
    public boolean isFirstPage() { return parent == null || parent.isFirstPage(this); }
    public boolean isLastPage() { return parent == null || parent.isLastPage(this); }
    public boolean isOnlyPage() { return isFirstPage() && isLastPage(); }
    public void CreateInventory() {
        Inventory inventory;
        InventoryType type = getInventoryType();
        if(type == null) inventory = Bukkit.createInventory(null, getSize(), title);
        else inventory = Bukkit.createInventory(null, type, title);
        this.inventory = inventory;
        Log("Creating Inventory " + pageNumber);
        PageItems();
    }
    public void PageItems() {
        if(inventory == null) return;
        Log("Paging Items " + pageNumber);
        setNavigationItems();
        for(MenuItem menuItem : values()) {
            //Log("Paging " + menuItem + " @ " + menuItem.getSlot());
            inventory.setItem(menuItem.getSlot(), menuItem);
        }
        for(MenuItem navItem : navigationItems.values()) {
            //Log("Paging NavItem " + navItem + " @ " + navItem.getSlot());
            inventory.setItem(navItem.getSlot(), navItem);
        }
    }
    public Inventory getInventory() { if(inventory == null || updated) CreateInventory(); return inventory; }
    public void open(Player player) {
        player.openInventory(getInventory());
        MenuController.setInMenus(player, this);
    }
    public Menu getParent() { return parent; }
    public Page setParent(Menu menu) { this.parent = menu; return this; }
}
