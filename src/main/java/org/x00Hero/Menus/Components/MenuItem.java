package org.x00Hero.Menus.Components;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static org.x00Hero.Main.plugin;

public class MenuItem extends ItemBuilder {
    //private ItemBuilder itemBuilder;
    private Page parent;
    private String ID;
    private boolean enabled = true, cancelClick = true;
    private int slot = -1;
    private final String stored = "MenuItem420";
    public static final NamespacedKey MenuItemKey = new NamespacedKey(plugin, "HMS-MenuItem");
    public static final PersistentDataType<String, String> MenuItemType = PersistentDataType.STRING;

    public MenuItem(Material material) { super(material); setCustomData(MenuItemKey, MenuItemType, stored); }
    public MenuItem(ItemStack itemStack) { super(itemStack); setCustomData(MenuItemKey, MenuItemType, stored); }
    public MenuItem(ItemStack itemStack, boolean cancelClick) {
        super(itemStack);
        this.cancelClick = cancelClick;
        setCustomData(MenuItemKey, MenuItemType, stored);
    }
    public MenuItem(ItemStack itemStack, int slot) {
        super(itemStack);
        this.slot = slot;
        setCustomData(MenuItemKey, MenuItemType, stored);
    }
    public MenuItem(ItemStack itemStack, int slot, int page) {
        super(itemStack);
        //this.page = page;
        this.slot = slot;
        setCustomData(MenuItemKey, MenuItemType, stored);
    }
    public MenuItem(ItemBuilder itemBuilder, int slot) {
        super(itemBuilder);
        this.slot = slot;
        setCustomData(MenuItemKey, MenuItemType, stored);
    }
    public MenuItem(ItemBuilder itemBuilder, int slot, int page) {
        super(itemBuilder);
        //this.page = page;
        this.slot = slot;
        setCustomData(MenuItemKey, MenuItemType, stored);
    }

    public int getPage() { return parent == null ? -1 : parent.pageNumber; }
    //public void setPage(int page) { this.page = page; }
    public int getSlot() { return slot; }
    public void setSlot(int slot) { this.slot = slot; } // If slot is changed while assigned to a Page maybe set it to rebuild?
    public void isEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }
    public boolean isCancelClick() { return cancelClick; }
    public MenuItem setCancelClick(boolean cancelClick) { this.cancelClick = cancelClick; return this; }
    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public Page getParent() { return parent; }
    public void setParent(Page parent) {
        //page = parent.pageNumber;
        this.parent = parent;
    }

}