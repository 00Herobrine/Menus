package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        registerCommands();
        registerEvents();
    }

    public void registerCommands() {
        getCommand("menu").setExecutor(new CommandController());
    }
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new CommandController(), this);
        Bukkit.getPluginManager().registerEvents(new MenuController(), this);
    }
}