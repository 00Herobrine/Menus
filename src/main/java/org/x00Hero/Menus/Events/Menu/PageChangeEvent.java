package org.x00Hero.Menus.Events.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class PageChangeEvent extends Event implements Cancellable {
    public final Player player;
    public final Menu menu;
    public final Page previousPage, page;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public PageChangeEvent(Player player, Menu menu, Page previousPage, Page page) {
        this.player = player;
        this.menu = menu;
        this.previousPage = previousPage;
        this.page = page;
    }

    public Player getPlayer() { return player; }
    public Menu getMenu() { return menu; }
    public Page getPage() { return page; }
    public Page getPreviousPage() { return previousPage; }

    @Override
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean b) { isCancelled = b; }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
}