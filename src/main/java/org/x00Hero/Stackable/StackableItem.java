package org.x00Hero.Stackable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.x00Hero.Menus.Components.ItemBuilder;

import static org.x00Hero.Main.storeString;

public abstract class StackableItem extends ItemBuilder {
    private String name = null, requiredTag = null;
    private int maxStack = 500;
    private int stackAmount = 1;
    private boolean stackInName = false;

    public StackableItem(ItemStack itemStack) {
        super(itemStack);
        storeString(itemStack, "tag", "stackable");
        setAmount(itemStack.getAmount());
        storeName();
    }
    public StackableItem(ItemStack itemStack, boolean stackInName) {
        super(itemStack);
        storeString(itemStack, "tag", "stackable");
        setAmount(itemStack.getAmount());
        this.stackInName = stackInName;
        storeName();
    }

    public StackableItem(ItemBuilder itemBuilder) {
        super(itemBuilder);
        storeString(itemBuilder, "tag", "stackable");
        setAmount(itemBuilder.getAmount()); }
    public StackableItem(ItemBuilder itemBuilder, boolean stackInName) {
        super(itemBuilder);
        storeString(itemBuilder, "tag", "stackable");
        setAmount(itemBuilder.getAmount());
        this.stackInName = stackInName;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; super.setName(name); }
    public void storeName() { name = hasItemMeta() && getItemMeta().hasDisplayName() ? getItemMeta().getDisplayName() : getType().name(); }
    public int getMaxStack() { return maxStack; }

    @Override
    public void setAmount(int amount) {
        if(stackInName) { setName(amount + name); updateDisplayName(); }
        else super.setAmount(Math.min(getMaxStackSize(), amount));
    }
    @Override
    public int getAmount() {
        if(stackInName()) return Integer.parseInt(getName().split(" ")[0]);
        return super.getAmount();
    }

    public void updateDisplayName() { setName(stackAmount + name); }

    public String getRequiredTag() { return requiredTag; }
    public void setRequiredTag(String requiredTag) { this.requiredTag = requiredTag; }
    public boolean stackInName() { return stackInName; }

/*    public int maxStackCheck(ItemStack itemStack) {
        int amount = getAmount();
        int otherAmount = itemStack.getAmount();
        int total = amount + otherAmount;
        if(total > maxStackSize) return total - maxStackSize;
        return -1;
    }*/

}
