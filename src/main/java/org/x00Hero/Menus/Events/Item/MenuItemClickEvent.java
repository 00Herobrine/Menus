package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;

public class MenuItemClickEvent extends Event implements Cancellable {
    private final Page page;
    private final MenuItem clickedItem;
    private final MenuItem heldItem;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MenuItemClickEvent(Player whoClicked, MenuItem clickedItem, MenuItem heldItem, Page page, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.clickedItem = clickedItem;
        this.heldItem = heldItem;
        this.page = page;
        this.event = event;
        if(this.clickedItem.isEnabled()) event.setCancelled(true);
    }

    public Player getWhoClicked() { return whoClicked; }
    public Page getPage() { return page; }
    public Menu getMenu() { return page.getParent(); }
    public String getID() { return clickedItem.getID(); }
    public MenuItem getClickedItem() { return clickedItem; }
    public MenuItem getHeldItem() { return heldItem; }
    public int getSlot() { return event.getSlot(); }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
