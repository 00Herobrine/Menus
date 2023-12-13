package org.x00Hero.Tests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Main;
import org.x00Hero.Menus.Components.*;

public class MenuTest {
    private static Menu scalingMenu = new Menu("Scaling");
    private static Menu nineMenu = new Menu("Nine");
    private static Page ninePage = new Page("Nine Page", InventoryType.BEACON);

    public static void InitializeMenus() {
        for(Material material : Material.values()) {
            if(!material.isItem() || !material.isBlock() || material.isAir()) continue;
            scalingMenu.addItem(new ItemStack(material));
        }
        //TestMenu.addPage(TestPage);
        if(nineMenu.addPage(0, ninePage)) nineMenu.register();
        scalingMenu.register();
    }

    public static void AnyMenu(Player player, InventoryType type) {
        Page page = new Page(type.getDefaultTitle(), type);
        Menu menu = new Menu("Any-Inventory");
        menu.addPage(page);
        Main.Log("Opening " + type + " Menu");
        menu.open(player);
    }
    public static void ScalingMenu(Player player) { // Infinitely Scaling Menu
/*        Menu menu = new Menu("Scaling Menu");
        for(Material material : Material.values()) {
            if(!material.isItem() || !material.isBlock() || material.isAir()) continue;
            menu.addItem(new ItemStack(material));
        }*/
        scalingMenu.open(player);
    }

    public static void LimitedMenu(Player player) { // Scaling Inventory factoring the itemCount
        int maxSlots = 100;
        Menu menu = new Menu( maxSlots + " slotted Inventory", maxSlots); // need to find a way to define slots and pages
    }

    public static void DefinedMenu(Player player) { // Should show all 5 pages regardless if they contain an item
        Menu menu = new Menu("Defined Menu", 5);
        menu.build(); // build all pages within the limit
        menu.open(player);
    }

    public static void DefinedAddedMenu(Player player) { // Same as above but with a defined Page set
        Menu menu = new Menu("Defined Menu with Added Page", 5);
        Page page = new Page(2, 5, "Defined Page");
        MenuItem freeItem = new MenuItem(new ItemStack(Material.ACACIA_BOAT));
        MenuItem setItem = new MenuItem(new ItemStack(Material.REDSTONE), 3);
        page.addItem(freeItem);
        page.addItem(setItem); // item placed in slot #3
        menu.setPage(page);
    }
}
