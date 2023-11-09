package org.x00Hero.Components;

import org.bukkit.inventory.ItemStack;
import org.x00Hero.Logger;

public class MenuItem {
    private ItemBuilder itemBuilder;
    private Page parent;
    private String ID;
    private boolean enabled = true, cancelClick = true;
    private int page = -1, slot = -1;

    public MenuItem(ItemStack itemStack) {
        itemBuilder = new ItemBuilder(itemStack);
    }
    public MenuItem(ItemStack itemStack, int slot) {
        this.slot = slot;
        itemBuilder = new ItemBuilder(itemStack);
    }
    public MenuItem(ItemStack itemStack, int slot, int page) {
        this.page = page;
        this.slot = slot;
        this.itemBuilder = new ItemBuilder(itemStack);
    }
    public MenuItem(ItemBuilder itemBuilder, int slot) {
        this.slot = slot;
        this.itemBuilder = itemBuilder;
    }
    public MenuItem(ItemBuilder itemBuilder, int slot, int page) {
        this.page = page;
        this.slot = slot;
        this.itemBuilder = itemBuilder;
    }

    public ItemStack getItemStack() { return getItemBuilder().getItemStack(); }
    public ItemBuilder getItemBuilder() {return itemBuilder; }
    public void setItemBuilder(ItemBuilder itemBuilder) { this.itemBuilder = itemBuilder; }

    public int getPage() { return page; }
    public void setPage(int page) {this.page = page; }
    public int getSlot() { return slot; }
    public void setSlot(int slot) { this.slot = slot; } // If slot is changed while assigned to a Page maybe set it to rebuild?
    public void isEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }
    public boolean isCancelClick() { return cancelClick; }
    public void setCancelClick(boolean cancelClick) { this.cancelClick = cancelClick; }
    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public Page getParent() { return parent; }
    public void setParent(Page parent) {
        page = parent.pageNumber;
        this.parent = parent;
    }

}