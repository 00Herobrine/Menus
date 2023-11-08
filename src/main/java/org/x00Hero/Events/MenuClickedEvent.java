package org.x00Hero.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Components.Menu;
import org.x00Hero.Components.MenuItem;

public class MenuClickedEvent extends Event implements Cancellable {
    private final Menu menu;
    private final MenuItem menuItem;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public MenuClickedEvent(Player whoClicked, MenuItem menuItem, Menu menu, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.menuItem = menuItem;
        this.menu = menu;
        this.event = event;
    }

    public String getID() { return menuItem.getID(); }

    public Player getWhoClicked() { return whoClicked; }

    public Menu getMenu() { return menu; }
    public MenuItem getMenuItem() { return menuItem; }

    @Override
    public boolean isCancelled() { return isCancelled; }
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
        getEvent().setCancelled(b);
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
