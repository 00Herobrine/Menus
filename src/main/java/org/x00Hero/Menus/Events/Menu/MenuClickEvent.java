package org.x00Hero.Menus.Events.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;

public class MenuClickEvent extends Event implements Cancellable {
    public final Player player;
    public final Menu menu;
    public final Page page;
    public final MenuItem clickedItem;
    public final MenuItem heldItem;
    private final InventoryClickEvent inventoryClickEvent;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public MenuClickEvent(Player player, Page page, InventoryClickEvent inventoryClickEvent, ItemStack clickedItem, ItemStack heldItem) {
        this.player = player;
        this.page = page;
        this.menu = page.getParent();
        this.inventoryClickEvent = inventoryClickEvent;
        this.clickedItem = clickedItem != null && clickedItem.getType() != Material.AIR ? new MenuItem(clickedItem, inventoryClickEvent.getSlot()) : null;
        this.heldItem = heldItem != null && heldItem.getType() != Material.AIR ? new MenuItem(heldItem, inventoryClickEvent.getSlot()) : null;
    }

    public Player getPlayer() { return player; }
    public Menu getMenu() { return menu; }
    public Page getPage() { return page; }
    public MenuItem getClickedItem() { return clickedItem; }
    public MenuItem getHeldItem() { return heldItem; }

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
