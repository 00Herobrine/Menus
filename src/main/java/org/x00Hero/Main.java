package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00Hero.Menus.MenuController;
import org.x00Hero.Tests.MenuEvents;

public class Main extends JavaPlugin {

    public void onEnable() {
        registerCommands();
        registerEvents();
    }

    public void registerCommands() {
        getCommand("menu").setExecutor(new CommandController());
    }
    public void registerEvents() {
        registerEvent(new CommandController());
        registerEvent(new MenuController());
        registerEvent(new MenuEvents());
    }
    public void registerEvent(Listener listener) { Bukkit.getPluginManager().registerEvents(listener, this); }
    public static void Log(String message) { Bukkit.getServer().getLogger().info(message); }
}