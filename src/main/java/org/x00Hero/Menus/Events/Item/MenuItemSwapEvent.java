package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;

public class MenuItemSwapEvent extends Event implements Cancellable {
    private final Page page;
    private final MenuItem menuItem;
    private final MenuItem swappedItem;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MenuItemSwapEvent(Player whoClicked, MenuItem menuItem, MenuItem swappedItem, Page page, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.menuItem = menuItem;
        this.swappedItem = swappedItem;
        this.page = page;
        this.event = event;
        if(menuItem.isEnabled()) event.setCancelled(true);
    }

    public Player getWhoClicked() { return whoClicked; }
    public Page getPage() { return page; }
    public Menu getMenu() { return page.getParent(); }
    public String getID() { return menuItem.getID(); }
    public MenuItem getMenuItem() { return menuItem; }
    public MenuItem getSwappedItem() { return swappedItem; }
    public int getSlot() { return event.getSlot(); }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
