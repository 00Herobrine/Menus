package org.x00Hero.Menus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class MenuCloseEvent extends Event implements Cancellable {
    public final Player player;
    public final Menu menu;
    public final Page page;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final InventoryCloseEvent inventoryCloseEvent;
    private boolean isCancelled;

    public MenuCloseEvent(Player player, Menu menu, Page page, InventoryCloseEvent e) {
        this.player = player;
        this.menu = menu;
        this.page = page;
        this.inventoryCloseEvent = e;
    }

    @Override
    public boolean isCancelled() { return isCancelled; }
    @Override
    public void setCancelled(boolean b) { isCancelled = b; player.openInventory(inventoryCloseEvent.getInventory()); } // might need to delay this by a tick

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
}
