package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.NavigationItem;
import org.x00Hero.Menus.Components.Page;
import org.x00Hero.Menus.Events.Menu.MenuClickEvent;

public class MenuNavigationClickEvent extends MenuClickEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final NavigationItem navigationItem;
    public MenuNavigationClickEvent(Player player, Page page, NavigationItem navigationItem, InventoryClickEvent event) {
        super(player, page, event, navigationItem, event.getCursor());
        this.navigationItem = navigationItem;
        event.setCancelled(true);
    }

    public boolean isForward() { return navigationItem.isForward(); }
    public int getNavAmount() { return navigationItem.getNavAmount(); }
    public NavigationItem getNavigationItem() { return navigationItem; }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
}
