package org.x00Hero.Stackable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.Components.ItemBuilder;

public class Currency extends StackableItem {
    public Currency(Material material, int amount, String name) {
        super(new ItemBuilder(material), true);
        setAmount(amount);
        setName(name);
        updateDisplayName();
    }

    public Currency(ItemStack itemStack) {
        super(itemStack, true);
    }

    public Currency stackWith(ItemStack cursorItem) {
        Currency cursor = new Currency(cursorItem);
        int newTotal = cursor.getAmount() + getAmount();
        int remainder = newTotal - getMaxStackSize();
        setAmount(newTotal);
        if(remainder > 0) return new Currency(cursorItem.getType(), remainder, cursor.getName());
        return null;
    }
}
