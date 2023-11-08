package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.x00Hero.Components.Menu;
import org.x00Hero.Components.MenuItem;
import org.x00Hero.Components.Page;
import org.x00Hero.Events.MenuClickedEvent;

import java.util.HashMap;
import java.util.List;

public class MenuController implements Listener {
    private static HashMap<Player, Page> inMenus = new HashMap<>();

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getClickedInventory() == null || !inMenus.containsKey(p)) return;
        if(e.getCurrentItem().equals(Menu.nothing.getItemStack())) { e.setCancelled(true); return; }
        Page page = inMenus.get(p);
        Menu menu = page.getParent();
        if(e.getClickedInventory().equals(page.getInventory())) {
            List<MenuItem> menuItems = page.getMenuItems();
            if(menuItems != null) {
                for(MenuItem menuItem : menuItems) {
                    if(e.getCurrentItem().equals(menuItem.getItemStack())) {
                        Bukkit.getServer().getPluginManager().callEvent(new MenuClickedEvent(p, menuItem, menu, e));
                        return;
                    }
                }
            }
            if(e.getCurrentItem().isSimilar(page.backItem.getItemStack())) { page.getPreviousPage().open(p); e.setCancelled(true); }
            else if(e.getCurrentItem().isSimilar(page.forwardItem.getItemStack())) { page.getNextPage().open(p); e.setCancelled(true); }
        }
    }
}
