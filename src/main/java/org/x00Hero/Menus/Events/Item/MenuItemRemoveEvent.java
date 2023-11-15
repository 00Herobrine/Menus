package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;

public class MenuItemRemoveEvent extends Event implements Cancellable {
    private final Page page;
    private final MenuItem menuItem;
    private final Player whoClicked;
    private final InventoryInteractEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MenuItemRemoveEvent(Player whoClicked, MenuItem menuItem, Page page, InventoryInteractEvent event) {
        this.whoClicked = whoClicked;
        this.menuItem = menuItem;
        this.page = page;
        this.event = event;
    }

    public Player getWhoClicked() { return whoClicked; }
    public Page getPage() { return page; }
    public Menu getMenu() { return page.getParent(); }
    public String getID() { return menuItem.getID(); }
    public MenuItem getMenuItem() { return menuItem; }
    public int getSlot() { return menuItem.getSlot(); }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryInteractEvent getEvent() { return event; }
}
