package org.x00Hero;

import org.bukkit.Bukkit;

public class Logger {

    public static void Log(String message) {
        Bukkit.getServer().getLogger().info(message);
    }
}
