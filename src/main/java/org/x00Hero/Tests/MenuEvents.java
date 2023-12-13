package org.x00Hero.Tests;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.x00Hero.Main;
import org.x00Hero.Menus.Events.Item.*;
import org.x00Hero.Menus.Events.Menu.MenuCloseEvent;
import org.x00Hero.Menus.Events.Menu.MenuOpenEvent;
import org.x00Hero.Menus.Events.Menu.PageChangeEvent;

import static org.x00Hero.Menus.Components.MenuItem.MenuItemKey;
import static org.x00Hero.Menus.Components.MenuItem.MenuItemType;

public class MenuEvents implements Listener {
    // How to use The Custom Events this resource contains

    //region Main Events
    @EventHandler
    public void onMenuClick(MenuItemClickEvent e) {
        Main.Log(e.getMenu().getTitle() + " clicked @ " + e.getClickedItem());
        Main.Log(e.getClickedItem().getCustomData(MenuItemKey, MenuItemType) + "");
    }
    @EventHandler
    public void onMenuOpen(MenuOpenEvent e) {
        Main.Log("Opened Menu " + e.menu.getTitle() + " on page " + e.page.pageNumber);
    }
    @EventHandler
    public void onMenuClose(MenuCloseEvent e) {
        Main.Log("Closed Menu '" + e.menu.getTitle() + "' on page '" + e.page.pageNumber + "'");
    }
    @EventHandler
    public void onMenuNavigation(PageChangeEvent e) {
        Main.Log("Navigated to page '" + e.getPage().pageNumber + "' from '" + e.getPreviousPage().pageNumber + "'");
    }
    //endregion

    //region Item Events
    @EventHandler
    public void menuItemClick(MenuItemClickEvent e) {
        Main.Log("Clicked Item '" + e.getClickedItem().getName() + "'");
        //e.setCancelled(false); // override any default cancelling
    }
    @EventHandler
    public void menuItemAdd(MenuItemAddEvent e) {
        Main.Log("Added Item '" + e.getMenuItem().getName() + "' to Page " + e.getPage().pageNumber + " @ " + e.getSlot());
    }
    @EventHandler
    public void menuItemRemoved(MenuItemRemoveEvent e) {
        Main.Log("Removed Item '" + e.getMenuItem().getName() + "' to Page " + e.getPage().pageNumber + " @ " + e.getSlot());
    }
    @EventHandler
    public void menuItemSwap(MenuItemSwapEvent e) {
        Main.Log("Swapped Item '" + e.getMenuItem().getName() + "' with '" + e.getSwappedItem().getName() + "' @ " + e.getSlot());
    }
    @EventHandler
    public void navigationItemClick(MenuNavigationClickEvent e) {
        Main.Log("Clicked " + (e.isForward() ? "Forward" : "Back") + " Navigation Item.");
        //e.setCancelled(false); // you can cancel it if you want I don't control you.
        // it will still navigate to the next page
    }
    //endregion
}
