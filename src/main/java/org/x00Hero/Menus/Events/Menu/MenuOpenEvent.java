package org.x00Hero.Menus.Events.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.Page;

public class MenuOpenEvent extends MenuEvent implements Cancellable {
    private boolean isCancelled;
    public MenuOpenEvent(Player player, Page page) {
        super(player, page);
    }

    @Override
    public boolean isCancelled() { return isCancelled; }
    @Override
    public void setCancelled(boolean b) { isCancelled = b; }
}
