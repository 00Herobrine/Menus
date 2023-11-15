package org.x00Hero.Menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Main;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.Page;
import org.x00Hero.Menus.Events.Item.*;
import org.x00Hero.Menus.Events.Menu.*;

import java.util.HashMap;
import java.util.UUID;

public class MenuController implements Listener {
    private static HashMap<UUID, Page> inMenus = new HashMap<>(); // <Player ID, Page> being viewed

    @EventHandler
    public void InventoryHandler(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) { return; }
        Inventory clickedInventory = event.getClickedInventory();
        Page page = getPage(player);
        InventoryAction action = event.getAction();
        if (clickedInventory == null || page == null) return;

        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        int slot = event.getSlot();
        if(clickedItem != null) {
            if(clickedItem.equals(Menu.nothing.getItemStack())) { event.setCancelled(true); return; }
            if(clickedItem.isSimilar(page.backItem.getItemStack())) {
                CallEvent(new MenuNavigationClickEvent(player, false, page, event));
                page.getPreviousPage().open(player);
                return;
            }
            if(clickedItem.isSimilar(page.forwardItem.getItemStack())) {
                CallEvent(new MenuNavigationClickEvent(player, true, page, event));
                page.getNextPage().open(player);
                return;
            }
        }
        if(action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            Main.Log("Attempting to Shift-Add Item");
            Inventory destinationInventory = event.getClickedInventory() == player.getInventory() ? event.getView().getTopInventory() : player.getInventory();
            int destinationSlot = findDestinationSlot(event.getClickedInventory(), destinationInventory, clickedItem);
            Main.Log("FirstEmpty: " + destinationInventory.firstEmpty() + " Destination: " + destinationSlot);
            if(destinationInventory.firstEmpty() == -1 && destinationSlot == -1) { Main.Log("Inventory Full"); return; }
            if(destinationInventory == player.getInventory()) { // Removing Item From Menu
                page.unstoreItem(slot, player, event);
            } else if(destinationInventory == getCurrentInventory(player)) { // Adding Item to Menu
                page.storeItem(new MenuItem(clickedItem, destinationSlot), player, event);
            }
        }
        if(event.getClickedInventory() != page.getInventory()) return;
        switch(action) {
            case PLACE_ALL, PLACE_ONE, PLACE_SOME -> page.storeItem(new MenuItem(cursorItem, slot), player, event);
            case PICKUP_ALL, PICKUP_ONE, PICKUP_HALF, PICKUP_SOME -> // this won't always remove the item need to check
                    page.unstoreItem(slot, player, event);
            //case HOTBAR_SWAP:
            case SWAP_WITH_CURSOR -> {
                MenuItem updatedItem = new MenuItem(clickedItem, slot);
                page.setItem(updatedItem);
                CallEvent(new MenuItemSwapEvent(player, page.getItemInSlot(slot), updatedItem, page, event));
            }
        }
    }
    public ItemStack getNotFilledStack(ItemStack itemStack, Inventory inventory) {
        int slot = 0;
        ItemStack item = null;
        while(slot < inventory.getSize() && item == null) {
            ItemStack slottedItem = inventory.getItem(slot);
            if(slottedItem == null) continue;
            if(slottedItem.isSimilar(itemStack) && slottedItem.getAmount() <= slottedItem.getMaxStackSize()) item = slottedItem;
            slot++;
        }
        return item;
    }
    private int findDestinationSlot(Inventory clickedInventory, Inventory destinationInventory, ItemStack movedItem) {
        for (int i = 0; i < destinationInventory.getSize(); i++) {
            if (destinationInventory.getItem(i) == null || destinationInventory.getItem(i).isSimilar(movedItem)) {
                return i;
            }
        }
        return -1; // Handle the case where the destination slot is not found
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID playerID = player.getUniqueId();
        Page page = getPage(playerID);
        if(!inMenus(playerID)) return;
        for(int slot : e.getRawSlots()) {
            if(!isTopInventory(slot, e.getView())) continue;
            MenuItem item = new MenuItem(e.getNewItems().get(slot), slot).setCancelClick(false);
            page.storeItem(item);
        }
        Main.Log("Dragged to " + e.getRawSlots().size() + " slots");
    }

    public static boolean isTopInventory(int slot, InventoryView view) { return isTopInventory(slot, view.getTopInventory().getSize()); }
    public static boolean isTopInventory(int slot, int size1) { return slot <= size1; }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        UUID playerID = player.getUniqueId();
        if(!inMenus(playerID)) return;
        Page page = getPage(playerID);
        removeInMenus(playerID);
        CallEvent(new MenuCloseEvent(player, page, e));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID playerID = e.getPlayer().getUniqueId();
        if(inMenus(playerID)) removeInMenus(playerID);
    }

    @EventHandler
    public void MenuClickEvent(MenuClickEvent e) {
        Player player = e.getPlayer();
        Page page = e.getPage();
        MenuItem clickedItem = e.getClickedItem();
        MenuItem heldItem = e.getHeldItem();

    }

    public static Inventory getCurrentInventory(Player player) { return getCurrentInventory(player.getUniqueId()); }
    public static Inventory getCurrentInventory(UUID uuid) { return getPage(uuid).getInventory(); }
    public static Page getPage(Player player) { return getPage(player.getUniqueId()); }
    public static Page getPage(UUID uuid) { return inMenus.get(uuid); }
    public static boolean inMenus(Player player) { return inMenus(player.getUniqueId()); }
    public static boolean inMenus(UUID uuid) { return inMenus.containsKey(uuid); }
    public static void setInMenus(Player player, Page page) { setInMenus(player.getUniqueId(), page); }
    public static void setInMenus(UUID uuid, Page page) {
        Player player = Bukkit.getPlayer(uuid);
        if(inMenus.containsKey(uuid)) CallEvent(new PageChangeEvent(player, page.getParent(), inMenus.get(uuid), page));
        else CallEvent(new MenuOpenEvent(player, page));
        inMenus.put(uuid, page);
    }
    public static void removeInMenus(Player player) { removeInMenus(player.getUniqueId()); }
    public static void removeInMenus(UUID uuid) { inMenus.remove(uuid); }
    public static void CallEvent(Event event) { Bukkit.getServer().getPluginManager().callEvent(event); }
}
