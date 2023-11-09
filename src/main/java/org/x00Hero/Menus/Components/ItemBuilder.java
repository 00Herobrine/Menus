package org.x00Hero.Menus.Components;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private ItemStack itemStack;
    private Material material;
    private int amount = 1;
    private String name;
    private List<String> lore = null;
    private boolean updated = false;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        material = itemStack.getType();
        amount = itemStack.getAmount();
        if(itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()) name = itemStack.getItemMeta().getDisplayName();
        else name = material.name();
        lore = (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList<>());
    }
    public ItemBuilder(Material material) { this.material = material; }
    public ItemBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        name = material.name();
    }
    public ItemBuilder(Material material, String name) {
        this.material = material;
        this.name = name;
    }
    public ItemBuilder(Material material, String name, int amount) {
        this.material = material;
        this.amount = amount;
        this.name = name;
    }
    public ItemBuilder(Material material, String name, String lore) {
        this.material = material;
        this.name = name;
        setLore(lore);
    }
    public ItemBuilder(Material material, String name, String lore, int amount) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        setLore(lore);
    }

    public void Updated() { updated = true; }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        Updated();
    }

    public int getAmount() { return amount; }
    public void setAmount(int amount) {
        this.amount = amount;
        Updated();
    }

    public List<String> getLore() {return lore; }
    public void setLore(List<String> lore) {
        this.lore = lore;
        Updated();
    }
    public void setLore(String rawLore) {
        setLore(Arrays.stream(ChatColor.translateAlternateColorCodes('&', rawLore).split("\n")).toList());
    }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) {
        this.material = material;
        Updated();
    }

    public void Build() {
        itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if(lore != null) meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
    public ItemStack getItemStack() {
        if(itemStack == null || updated) Build();
        return itemStack;
    }
    public ItemStack getItemStack(boolean rebuild) {
        if(rebuild || itemStack == null || updated) Build();
        return itemStack;
    }
}
