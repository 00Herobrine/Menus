package org.x00Hero.Menus.Events.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class MenuEvent extends Event {
    public final Player player;
    public final Menu menu;
    public final Page page;
    public static HandlerList handlerList = new HandlerList();

    public MenuEvent(Player player, Page page) {
        this.player = player;
        this.page = page;
        this.menu = page.getParent();
    }

    public Player getPlayer() { return player; }
    public Menu getMenu() { return menu; }
    public Page getPage() { return page; }

    @Override
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
