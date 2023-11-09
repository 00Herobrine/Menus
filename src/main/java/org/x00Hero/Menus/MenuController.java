package org.x00Hero.Menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.x00Hero.Logger;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;
import org.x00Hero.Menus.Events.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuController implements Listener {
    private static HashMap<UUID, Page> inMenus = new HashMap<>(); // <Player ID, Page> being viewed

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID playerID = player.getUniqueId();
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getClickedInventory() == null || !inMenus.containsKey(playerID)) return;
        if(e.getCurrentItem().equals(Menu.nothing.getItemStack())) { e.setCancelled(true); return; }
        Page page = inMenus.get(playerID);
        Menu menu = page.getParent();
        DoEvent(new MenuClickEvent(player, menu, page, e));
        if(e.getClickedInventory().equals(page.getInventory())) {
            List<MenuItem> menuItems = page.getMenuItems();
            if(e.getCurrentItem().isSimilar(page.backItem.getItemStack())) { page.getPreviousPage().open(player); DoEvent(new MenuNavigationClickEvent(player, false, menu, e)); return; }
            else if(e.getCurrentItem().isSimilar(page.forwardItem.getItemStack())) { page.getNextPage().open(player); DoEvent(new MenuNavigationClickEvent(player, true, menu, e)); return; }
            if(menuItems != null) {
                for(MenuItem menuItem : menuItems) {
                    if(e.getCurrentItem().equals(menuItem.getItemStack())) {
                        DoEvent(new MenuItemClickEvent(player, menuItem, menu, e));
                        return;
                    }
                }
            }
        }
    }
    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        UUID playerID = player.getUniqueId();
        if(!inMenus.containsKey(playerID)) return;
        Page page = inMenus.get(playerID);
        DoEvent(new MenuCloseEvent(player, page.getParent(), page, e));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID playerID = e.getPlayer().getUniqueId();
        if(inMenus.containsKey(playerID)) removeInMenus(playerID);
    }

    @EventHandler
    public void onMenuClick(MenuItemClickEvent e) {
        Logger.Log(e.getMenu().getTitle() + " clicked @ " + e.getMenuItem());
    }
    @EventHandler
    public void menuItemClick(MenuItemClickEvent e) {
        e.setCancelled(false);
    }

    public static boolean isInMenu(Player player) { return inMenus.containsKey(player.getUniqueId()); }
    public static void setInMenus(Player player, Page page) { setInMenus(player.getUniqueId(), page); }
    public static void setInMenus(UUID uuid, Page page) {
        Player player = Bukkit.getPlayer(uuid);
        if(inMenus.containsKey(uuid)) DoEvent(new MenuPageChangeEvent(player, page.getParent(), inMenus.get(uuid), page));
        else DoEvent(new MenuOpenEvent(player, page.getParent(), page));
        inMenus.put(uuid, page);
    }
    public static void removeInMenus(Player player) { removeInMenus(player.getUniqueId()); }
    public static void removeInMenus(UUID uuid) { inMenus.remove(uuid); }
    public static void DoEvent(Event event) { Bukkit.getServer().getPluginManager().callEvent(event); }
}
