package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.x00Hero.Components.Menu;
import org.x00Hero.Tests.MenuTest;

public class CommandController implements CommandExecutor, Listener {

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.equals(Bukkit.getConsoleSender())) return false;
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("menu")) {
            SelectMenu(player, args[0]);
        }
        return false;
    }

    public void SelectMenu(Player player, String args0) {
        switch(args0) {
            case "scaling" -> MenuTest.ScalingMenu(player);
            case "defined" -> MenuTest.DefinedMenu(player);
            case "added" -> MenuTest.DefinedAddedMenu(player);
            case "limited" -> MenuTest.LimitedMenu(player);
            default -> new Menu("DEFAULT_MENU").open(player);
        }
    }
}
