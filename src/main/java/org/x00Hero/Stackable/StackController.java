package org.x00Hero.Stackable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StackController implements Listener {
    public InventoryAction[] AcceptableActions = {InventoryAction.PLACE_ALL,InventoryAction.PLACE_ONE,InventoryAction.PLACE_SOME};
    @EventHandler
    public void onStack(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        ItemStack cursorItem = e.getCursor();
        ItemStack clickedItem = e.getClickedInventory().getItem(e.getSlot());
        if(cursorItem == null || cursorItem.getType() == Material.AIR || clickedItem == null || clickedItem.getType() == Material.AIR) return;
        Bukkit.getLogger().info("Action: " + e.getAction());
        if(!List.of(AcceptableActions).contains(e.getAction())) return;
        Currency currency = new Currency(clickedItem);
        currency.stackWith(cursorItem);
        //e.getClickedInventory().remove(clickedItem);
        if(cursorItem.isSimilar(clickedItem)) {
            Bukkit.getLogger().info("Stacking ");
        }
        Bukkit.getLogger().info("Stackable Item? " + currency.getAmount());
        //if stackInName do things
    }
}
