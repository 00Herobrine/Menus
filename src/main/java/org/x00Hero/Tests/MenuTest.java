package org.x00Hero.Tests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Components.Menu;
import org.x00Hero.Components.MenuItem;
import org.x00Hero.Components.Page;

public class MenuTest {

    public void ScalingMenu(Player player) { // Infinitely Scaling Menu
        Menu menu = new Menu("Scaling Menu");
        for(Material material : Material.values()) {
            if(!material.isItem() || !material.isBlock()) continue;
            menu.addItem(new ItemStack(material));
        }
        menu.open(player);
    }

    public void LimitedMenu(Player player) { // Scaling Inventory factoring the itemCount
        int maxSlots = 100;
        Menu menu = new Menu( maxSlots + " slotted Inventory", maxSlots); // need to find a way to define slots and pages
    }

    public void DefinedMenu(Player player) { // Should show all 5 pages regardless if they're full or not.
        Menu menu = new Menu("Defined Menu", 5);
        menu.build(); // build all pages within the limit
        menu.open(player);
    }

    public void DefinedAddedMenu(Player player) { // Same as above but with a defined Page set
        Menu menu = new Menu("Defined Menu with Added Page", 5);
        Page page = new Page(2, "Defined Page", 5);
        MenuItem freeItem = new MenuItem(new ItemStack(Material.ACACIA_BOAT));
        MenuItem setItem = new MenuItem(new ItemStack(Material.REDSTONE), 3);
        page.addItem(freeItem);
        page.addItem(setItem); // item placed in slot #3
        menu.setPage(page);
    }
}