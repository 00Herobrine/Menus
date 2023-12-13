package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.x00Hero.Menus.Components.ItemBuilder;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.MenuController;
import org.x00Hero.Stackable.Currency;
import org.x00Hero.Tests.MenuTest;

import java.util.ArrayList;
import java.util.List;


public class CommandController implements CommandExecutor, Listener, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.equals(Bukkit.getConsoleSender())) return false;
        Player player = (Player) sender;
        switch(cmd.getName().toLowerCase()) {
            case "menu" -> {
                if(args.length > 0) SelectMenu(player, args);
                return true;
            }
            case "stackable" -> {
                Currency currency = new Currency(Material.IRON_INGOT, 5, " &aRUB");
                player.getInventory().addItem(currency);
                player.sendMessage("Adding " + currency);
            }
        }
        return false;
    }

    public void SelectMenu(Player player, String[] args) {
        if(MenuController.RegisteredMenus().containsKey(args[0])) MenuController.RegisteredMenus().get(args[0]).open(player);
        switch(args[0].toLowerCase()) {
            case "scaling" -> MenuTest.ScalingMenu(player);
            case "defined" -> MenuTest.DefinedMenu(player);
            case "added" -> MenuTest.DefinedAddedMenu(player);
            case "limited" -> MenuTest.LimitedMenu(player);
            default -> MenuTest.AnyMenu(player, InventoryType.valueOf(args[0]));
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> completions = new ArrayList<>();
        for(Menu menu : MenuController.getRegisteredMenus()) completions.add(menu.ID);
        for(InventoryType type : InventoryType.values()) completions.add(type.name());
        return completions;
    }
}
