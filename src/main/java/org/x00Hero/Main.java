package org.x00Hero;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00Hero.Menus.MenuController;
import org.x00Hero.Stackable.StackController;
import org.x00Hero.Tests.MenuEvents;
import org.x00Hero.Tests.MenuTest;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onEnable() {
        plugin = this;
        registerCommands();
        registerEvents();
        MenuTest.InitializeMenus();
    }

    public void registerCommands() {
        getCommand("menu").setExecutor(new CommandController());
        //getCommand("stackable").setExecutor(new CommandController());
    }
    public void registerEvents() {
        registerEvent(new CommandController());
        registerEvent(new MenuController());
        registerEvent(new MenuEvents());
        registerEvent(new StackController());
    }
    public void registerEvent(Listener listener) { Bukkit.getPluginManager().registerEvents(listener, this); }
    public static void Log(String message) { Bukkit.getServer().getLogger().info(message); }

    public static String getStoredString(ItemStack itemStack, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if(container.has(namespacedKey, PersistentDataType.STRING)) return container.get(namespacedKey, PersistentDataType.STRING);
        return null;
    }

    public static void storeString(ItemStack itemStack, String key, String value) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
    }
}