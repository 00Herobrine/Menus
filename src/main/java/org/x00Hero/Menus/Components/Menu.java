package org.x00Hero.Menus.Components;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.MenuController;

import java.util.HashMap;
import java.util.List;

import static org.x00Hero.Main.Log;

public class Menu extends HashMap<Integer, Page> {
    private final String title;
    private int currentPage, firstPage, lastPage;
    private int pageLimit = 10000; // to prevent issues keep this at a reasonable number I'd say
    private int slotLimit = 54; // itemsPerPage (will be adjusted for navigation if needed)
    private int itemCount = 0;
    private boolean initial = true;
    public final String permission, ID;
    public static ItemBuilder nothing = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    public static ItemBuilder backItemBuilder = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "&eBack");
    public static ItemBuilder forwardItemBuilder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, "&aForward");

    public Menu(String title) { this.title = title; this.ID = title; this.permission = "Menus.menu." + title; }
    public Menu(String title, int pageLimit) { // expanding menu
        this.title = title;
        this.ID = title;
        this.pageLimit = pageLimit;
        this.permission = "Menus.menu." + title;
    }
    public Menu(String title, String ID) {
        this.title = title;
        this.ID = ID;
        this.permission = "Menus.menu." + ID;
    }
    public String getTitle() { return title; }

    @Override
    public String toString() {
        return title;
    }

    //region Item Handling
    public MenuItem AddOverfillItem(MenuItem menuItem, Page page) {
        int pageNumber = page.getPageNumber() + 1;
        return getCreatePage(pageNumber).addItem(menuItem);
    }
    public int getItemCount() {
        int count = 0;
        for(Page page : values()) count += page.size();
        return count;
    }
    public int getTotalItems() { return itemCount; }
    public int addToItemCount() { return addToItemCount(1); }
    public int addToItemCount(int amount) {
        itemCount += amount;
        return itemCount;
    }

    public Page getCreatePage(int pageNum) {
        Page page = getPage(pageNum);
        if(page != null) return page;
        return createPage(pageNum);
    }
    public Page getAvailablePage() { return getAvailablePage(false); }
    public Page getAvailablePage(boolean create) {
        if(create) {
            for(int curPage = 0; curPage < pageLimit; curPage++) {
                Page page = getCreatePage(curPage);
                if(page.isSlotAvailable()) return page;
            }
        } else {
            for(Page page : values())
                if(page.isSlotAvailable()) return page;
        }
        return null;
    }
    public Menu addItems(List<MenuItem> menuitems) { menuitems.forEach(this::addItem); return this; }
    public void addItem(ItemStack item) { addItem(item, -1); } // Add Item to any slot in any Page
    public void addItem(ItemStack item, int slot) { addItem(new MenuItem(item, slot)); } // Add Item to any Page
    public void addItem(MenuItem item) {
        Page page = getCreatePage(currentPage);
        if(page.addItem(item) != null) {
            currentPage++;
            Log("Page is full with " + page.size());
            page = getCreatePage(currentPage);
            page.addItem(item);
        }
        //Log("Adding " + item.getName() + " @ " + item.getSlot() + " to page: " + item.getPage());
    }

    public void addItemToPage(int page, ItemStack item) {
        if (page < 0) throw new IllegalArgumentException("Page number must be >0.");
        else if (page >= pageLimit) throw new IllegalArgumentException("Page number exceeds the page limit.");
        Page menuPage = getCreatePage(page); // Ensures the specified page exists
        if (menuPage.isFull()) { // If the specified page is full, add to Next Page
            page++;
            if (page >= pageLimit) // Check if the page exceeds the limit after incrementing
                throw new IllegalArgumentException("Page number exceeds the page limit.");
            menuPage = getCreatePage(page);
        }
        menuPage.addItem(item, -1);
    }
    //endRegion
    public boolean isInitial() { return initial; }
    public boolean isFirstPage(Page page) { return isFirstPage(page.getPageNumber()); }
    public boolean isFirstPage(int pageNumber) { return pageNumber == firstPage; }
    public boolean isLastPage(Page page) { return isLastPage(page.getPageNumber()); }
    public boolean isLastPage(int pageNumber) { return pageNumber == lastPage; }
    public boolean isValidPageNumber(int pageNumber) { return pageNumber <= pageLimit && pageNumber >= 0; }
    public boolean containsPage(int pageNumber) { return containsKey(pageNumber); }
    public int getFirstPageNumber() { return firstPage; }
    public int getLastPageNumber() { return lastPage; }
    public int getSlotLimit() { return slotLimit; }
    public int getPageCount() { return size(); }
    public Page getFirstPage() { return get(firstPage); }
    public Page getLastPage() { return get(lastPage); }
    public Page getPage(int pageNumber) { return get(pageNumber); }
    public Page createPage(int pageNumber) {
        if(isValidPageNumber(pageNumber) && (containsKey(pageNumber) || pageNumber > pageLimit)) return null;
        Page page = new Page(pageNumber, slotLimit, title).setParent(this);
        Log("Creating page " + pageNumber);
        setPage(pageNumber, page);
        if(keySet().size() > 1) initial = false;
        return page;
    }
    public boolean addPage(Page page) { return addPage(page.pageNumber, page); }
    public boolean addPage(int pageNumber, Page page) { return setPage(pageNumber, page,false) != null; }
    public Page setPage(Page page) { return setPage(page.pageNumber, page, false); }
    public Page setPage(int pageNumber, Page page) { return setPage(pageNumber, page, true); }
    public Page setPage(int pageNumber, Page page, boolean replace) {
        if(isValidPageNumber(pageNumber) && containsKey(pageNumber) && !replace) return null;
        if(pageNumber > lastPage) lastPage = pageNumber;
        if(pageNumber < firstPage) firstPage = pageNumber;
        page.pageNumber = pageNumber;
        page.setParent(this);
        Log("Setting Page " + pageNumber + " lP: " + lastPage + " fP: " + firstPage);
        return put(pageNumber, page);
    }
    public void setPageLimit(int pageLimit) {
        if (pageLimit < 1) throw new IllegalArgumentException("Page limit must be greater than or equal to 1.");
        this.pageLimit = pageLimit;
    }

    public void open(Player player) { open(player, 0); }
    public void open(Player player, int pageNumber) {
        if(containsKey(pageNumber)) getPage(pageNumber).open(player);
        else player.sendMessage("Page not found.");
    }
    public void open(Player player, int pageNumber, boolean fillEmpty) {
        if(containsKey(pageNumber)) getPage(pageNumber).open(player);
        else player.sendMessage("Page not found.");
    }
    public void build() { for(int i = firstPage; i <= lastPage; i++) getCreatePage(i).CreateInventory(); }
    public Menu register() { MenuController.registerMenu(this); return this; }
}
