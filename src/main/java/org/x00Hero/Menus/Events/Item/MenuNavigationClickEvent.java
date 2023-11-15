package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class MenuNavigationClickEvent extends Event implements Cancellable {
    private final Page page;
    private final Player whoClicked;
    private final InventoryClickEvent event;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public final boolean forward;

    public MenuNavigationClickEvent(Player whoClicked, boolean forward, Page page, InventoryClickEvent event) {
        this.whoClicked = whoClicked;
        this.forward = forward;
        this.page = page;
        this.event = event;
        event.setCancelled(true);
    }

    public Player getWhoClicked() { return whoClicked; }
    public Page getPage() { return page; }
    public Menu getMenu() { return page.getParent(); }
    public boolean isForward() { return forward; }

    public boolean isCancelled() { return event.isCancelled(); }
    public void setCancelled(boolean b) { getEvent().setCancelled(b); }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getEvent() { return event; }
}
