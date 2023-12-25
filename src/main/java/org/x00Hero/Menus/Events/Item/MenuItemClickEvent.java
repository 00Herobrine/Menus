package org.x00Hero.Menus.Events.Item;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;

public class MenuItemClickEvent extends InventoryClickEvent implements Cancellable {
    private final Page page;
    private final MenuItem clickedItem;
    private final Player whoClicked;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MenuItemClickEvent(Player whoClicked, MenuItem clickedItem, Page page, InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction(), event.getHotbarButton());
        this.whoClicked = whoClicked;
        this.clickedItem = clickedItem;
        this.page = page;
    }

    public Player getWhoClicked() { return whoClicked; }
    public Page getPage() { return page; }
    public Menu getMenu() { return page.getParent(); }
    public String getID() { return clickedItem.getID(); }
    public MenuItem getClickedItem() { return clickedItem; }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }
    public static HandlerList getHandlerList() { return HANDLERS_LIST; }
}
