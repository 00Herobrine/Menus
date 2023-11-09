package org.x00Hero.Menus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;

public class MenuItemClickEvent extends Event implements Cancellable {
    private final Menu menu;
    private final MenuItem menuItem;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MenuItemClickEvent(Player whoClicked, MenuItem menuItem, Menu menu, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.menuItem = menuItem;
        this.menu = menu;
        this.event = event;
        if(menuItem.isEnabled()) event.setCancelled(true);
    }

    public String getID() { return menuItem.getID(); }

    public Player getWhoClicked() { return whoClicked; }

    public Menu getMenu() { return menu; }
    public MenuItem getMenuItem() { return menuItem; }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
