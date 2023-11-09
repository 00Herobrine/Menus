package org.x00Hero.Menus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;

public class MenuNavigationClickEvent extends Event implements Cancellable {
    private final Menu menu;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public final boolean forward;

    public MenuNavigationClickEvent(Player whoClicked, boolean forward, Menu menu, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.forward = forward;
        this.menu = menu;
        this.event = event;
        event.setCancelled(true);
    }

    public boolean isForward() { return forward; }
    public Player getWhoClicked() { return whoClicked; }
    public Menu getMenu() { return menu; }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
