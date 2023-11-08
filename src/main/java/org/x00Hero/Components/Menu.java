package org.x00Hero.Components;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private final String title;
    private final Map<Integer, Page> pages = new HashMap<>();
    private int currentPage = 0;
    private int pageLimit = 10000; // to prevent issues keep this at a reasonable number I'd say
    private int slotLimit = 54;
    private int itemCount = 0;
    private boolean initial = true;
    public static ItemBuilder nothing = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ", " ");
    public static ItemBuilder backItemBuilder = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "Back");
    public static ItemBuilder forwardItemBuilder = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, "Forward");

    public Menu(String title, int pageLimit) { // expanding menu
        this.title = title;
        this.pageLimit = pageLimit;
    }
    public Menu(String title) {
        this.title = title;
    }

    //region Item Handling
    public MenuItem AddOverfillItem(MenuItem menuItem, Page page) {
        int pageNumber = page.pageNumber + 1;
        return getCreatePage(pageNumber).addItem(menuItem);
    }
    public void addItem(MenuItem menuItem) {
        int itemPage = menuItem.getPage();
        int itemSlot = menuItem.getSlot();
        if(itemPage == -1) {
            Page availablePage = getAvailablePage(true);
            if(availablePage == null) return; // menu is full idk
            else if(itemSlot == -1) menuItem.setSlot(availablePage.getAvailableSlot());
            availablePage.addItem(menuItem);
        } else getCreatePage(itemPage).addItem(menuItem);
    }
    public int getItemCount() {
        return itemCount;
    }
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
            for(Page page : pages.values()) {
                if(page.isSlotAvailable()) return page;
            }
        }
        return null;
    }
    public void addItem(ItemStack item) {
        addItem(-1, item);
    }
    public void addItem(int slot, ItemStack item) {
        if(!pages.containsKey(currentPage)) {
            pages.put(currentPage, new Page(currentPage, title + " - Page " + (currentPage + 1)));
        }

        Page page = pages.get(currentPage);
        if(page.isFull()) {
            currentPage++;
            page = new Page(currentPage, title + " - Page " + (currentPage + 1));
            pages.put(currentPage, page);
        }

        page.addItem(slot, item);
    }

    public void addItemToPage(int page, ItemStack item) {
        if (page < 0) {
            page = 0;
        }

        if (page >= pageLimit) {
            // Check if the page exceeds the limit
            throw new IllegalArgumentException("Page number exceeds the page limit.");
        }

        // Ensure the specified page exists
        while (page >= pages.size()) {
            // Create empty pages if needed
            pages.put(pages.size(), new Page(page, title + " - Page " + (pages.size() + 1)));
        }

        Page menuPage = pages.get(page);
        if (menuPage.isFull()) {
            // If the specified page is full, create a new one
            page++;
            if (page >= pageLimit) {
                // Check if the page exceeds the limit after incrementing
                throw new IllegalArgumentException("Page number exceeds the page limit.");
            }
            pages.put(page, new Page(page, title + " - Page " + (page + 1)));
            menuPage = pages.get(page);
        }

        menuPage.addItem(-1, item);
    }
    //endRegion
    public boolean isInitial() { return initial; }
    public boolean isValidPageNumber(int pageNumber) { return pageNumber <= pageLimit; }
    public boolean containsPage(int pageNumber) {
        return pages.containsKey(pageNumber);
    }
    public int getPageCount() { return pages.size(); }
    public Page getPage(int pageNumber) {
        return pages.get(pageNumber);
    }
    public Page createPage(int pageNumber) {
        if(isValidPageNumber(pageNumber) && (pages.containsKey(pageNumber) || pageNumber > pageLimit)) return null;
        Page page = new Page(pageNumber, title, slotLimit);
        pages.put(pageNumber, page);
        initial = false;
        return page;
    }
    public boolean addPage(Page page) { return addPage(page.pageNumber, page); }
    public boolean addPage(int pageNumber, Page page) {
        if(isValidPageNumber(pageNumber) && pages.containsKey(page.pageNumber)) return false;
        page.pageNumber = pageNumber;
        pages.put(pageNumber, page);
        return true;
    }
    public boolean setPage(Page page) { return setPage(page.pageNumber, page, false); }
    public boolean setPage(int pageNumber, Page page) { return setPage(pageNumber, page, true); }
    public boolean setPage(int pageNumber, Page page, boolean replace) {
        if(isValidPageNumber(pageNumber) && pages.containsKey(pageNumber) && !replace) return false;
        page.pageNumber = pageNumber;
        pages.put(pageNumber, page);
        return true;
    }
    public void setPageLimit(int pageLimit) {
        if (pageLimit < 1) throw new IllegalArgumentException("Page limit must be greater than or equal to 1.");
        this.pageLimit = pageLimit;
    }

    public void open(Player player) { open(player, 1); }
    public void open(Player player, int pageNumber) {
        if(pages.containsKey(pageNumber)) getPage(pageNumber).open(player);
        else player.sendMessage("Page not found.");
    }
    public void open(Player player, int pageNumber, boolean fillEmpty) {
        if(pages.containsKey(pageNumber)) getPage(pageNumber).open(player);
        else player.sendMessage("Page not found.");
    }
    public void build() {
        for(int i = 0; i < pageLimit; i++) getCreatePage(i).Build();
    }
}