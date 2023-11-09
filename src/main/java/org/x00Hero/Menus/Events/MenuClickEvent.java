package org.x00Hero.Menus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class MenuClickEvent extends Event implements Cancellable {
    public final Player player;
    public final Menu menu;
    public final Page page;
    private final InventoryClickEvent inventoryClickEvent;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public MenuClickEvent(Player player, Menu menu, Page page, InventoryClickEvent inventoryClickEvent) {
        this.player = player;
        this.menu = menu;
        this.page = page;
        this.inventoryClickEvent = inventoryClickEvent;
    }

    @Override
    public boolean isCancelled() { return isCancelled; }
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
        getInventoryClickEvent().setCancelled(b);
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
    public InventoryClickEvent getInventoryClickEvent() { return inventoryClickEvent; }
}
