package org.x00Hero.Menus.Components;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import static org.x00Hero.Main.plugin;

public class NavigationItem extends MenuItem {
    public final boolean forward;
    public final int navAmount;
    private static final String navKey = "HMS-NavItem";
    public static final NamespacedKey NavItemAmountKey = new NamespacedKey(plugin, navKey + "-Amount");
    public static final PersistentDataType<String, String> NavItemType = PersistentDataType.STRING;
    public NavigationItem(MenuItem menuItem, int navAmount) {
        super(menuItem);
        this.navAmount = navAmount;
        this.forward = navAmount > 0;
        setCustomData(NavItemAmountKey, NavItemType, navAmount + "");
    }
    public NavigationItem(MenuItem menuItem) {
        super(menuItem);
        this.navAmount = Integer.parseInt(getCustomData(NavItemAmountKey, NavItemType).toString());
        this.forward = navAmount > 0;
    }
    public boolean isForward() { return forward; }
    public int getNavAmount() { return navAmount; }
}
