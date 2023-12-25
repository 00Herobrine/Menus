package org.x00Hero.Menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.x00Hero.Menus.Components.Menu;
import org.x00Hero.Menus.Components.MenuItem;
import org.x00Hero.Menus.Components.NavigationItem;
import org.x00Hero.Menus.Components.Page;
import org.x00Hero.Menus.Events.Item.*;
import org.x00Hero.Menus.Events.Menu.*;


import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.x00Hero.Main.Log;

public class MenuController implements Listener {
    private static HashMap<String, Menu> registeredMenus = new HashMap<>();
    private static HashMap<UUID, Page> inMenus = new HashMap<>(); // <Player ID, Page> being viewed

    //region Menu Handling
    @EventHandler
    public void InventoryHandler(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory clickedInventory = event.getClickedInventory();
        Page page = getPage(player);
        InventoryAction action = event.getAction();
        if (clickedInventory == null || page == null) return;
        ItemStack currentItem = event.getCurrentItem();
        ItemStack heldItem = event.getCursor();
        int slot = event.getSlot();
        String currentName = currentItem != null ? currentItem.hasItemMeta() ? currentItem.getItemMeta().getDisplayName() : currentItem.getType().name() : "NULL";
        String heldName = heldItem != null ? heldItem.hasItemMeta() ? heldItem.getItemMeta().getDisplayName() : heldItem.getType().name() : "NULL";
        //Main.Log("Current: " + currentName + " held: " + heldName);
        MenuItem clickedItem = page.getItem(slot);
        if(clickedInventory == page.getInventory()) CallEvent(new MenuClickEvent(player, page, event, clickedItem, event.getCursor()));
        int interactedSlot = event.getRawSlot();
        Inventory interactedInventory = isTopInventory(interactedSlot, event.getView()) ? event.getView().getTopInventory() : event.getView().getBottomInventory();
        //boolean destinedToPage = destinationInventory == getCurrentInventory(player);
        if(currentItem != null && currentItem.isSimilar(Menu.nothing)) { event.setCancelled(true); /*Clicked Filled Slot*/ }
        if(clickedItem != null) {
            for(MenuItem navItem : page.getNavigationItems())
                if(clickedItem.isSimilar(navItem)) {
                    NavigationItem nav = new NavigationItem(navItem);
                    Menu menu = page.getParent();
                    Page nextPage = menu.getPage(page.pageNumber + nav.getNavAmount());
                    if(nextPage != null) nextPage.open(player);
                    CallEvent(new MenuNavigationClickEvent(player, page, nav, event));
                    Log("Navigating");
                }
            for(MenuItem menuItem : page.values())
                if(clickedItem.isSimilar(menuItem)) CallEvent(new MenuItemClickEvent(player, menuItem, page, event));
        }
        if(action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            //Main.Log("Attempting to Shift-Add Item")
            Inventory destinationInventory = interactedInventory == player.getInventory() ? event.getView().getTopInventory() : event.getView().getBottomInventory();
            boolean movedToPage = destinationInventory == page.getInventory();
            int destinationSlot = findDestinationSlot(interactedInventory, destinationInventory, movedToPage ? currentItem : heldItem);
            if(destinationSlot == -1) { Log("Inventory Full"); return; }
            if(interactedInventory == page.getInventory()) { // Removing Item From Menu
                ItemStack interactedItem = interactedInventory.getItem(slot);
                Log("Updated Total: " + (interactedItem == null ? 0 : interactedItem.getAmount()) + " @ " + slot);
                //page.unstoreItem(slot, player, event);
            } else if(movedToPage) { // Adding Item to Menu
                //findDestinationSlot(event.getClickedInventory(), destinationInventory, event.getCurrentItem());
                //page.storeItem(currentItem, destinationSlot, player, event);
            }
        }
        if(interactedInventory != page.getInventory()) return;
        Log("Doing ACTION: " + action + " Interacted Slot: " + event.getSlot() + "(" + event.getRawSlot() + ") HK: " + event.getHotbarButton() + " Clicked: " + event.getClickedInventory().getType());
        ItemStack hotBarItem = event.getHotbarButton() != -1 ? player.getInventory().getItem(event.getHotbarButton()) : null;
        switch(action) {
            case HOTBAR_MOVE_AND_READD -> {} //Main.Log("MOVED " + page.storeItem(hotBarItem, slot, player, event).getName());
            case HOTBAR_SWAP -> {
                // is in currentItem if taking from menu and is in heldItem if depositing to menu
                // partially wrong, returns air if depositing, so I need a way to determine the deposited item
                String HBName = hotBarItem == null ? "NULL" : hotBarItem.getType().name();
                Log("HB: " + HBName + " Held: " + heldName + " Current: " + currentName);
                //if(hotBarItem != null && heldItem != null && heldItem.getType() == Material.AIR) page.storeItem(hotBarItem, slot, player, event);
                //else page.unstoreItem(slot, player, event);

            }
            case PLACE_ALL, PLACE_ONE, PLACE_SOME -> {} //page.storeItem(heldItem, slot, player, event);
            case PICKUP_ALL, PICKUP_ONE, PICKUP_HALF, PICKUP_SOME -> {} // this won't always remove the item need to check
                    //page.unstoreItem(slot, player, event);
            case SWAP_WITH_CURSOR -> {
                //MenuItem updatedItem = new MenuItem(heldItem, slot);
                //page.storeItem(heldItem, slot, player, event);
                //CallEvent(new MenuItemSwapEvent(player, page.getItemInSlot(slot), updatedItem, page, event));
            }
        }
    }
    @EventHandler
    public void inventoryDrag(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        UUID playerID = player.getUniqueId();
        Page page = getPage(playerID);
        if(!inMenus(playerID)) return;
        for(int slot : e.getRawSlots()) {
            if(!isTopInventory(slot, e.getView())) continue;
            //page.storeItem(e.getNewItems().get(slot), slot, player, e);
        }
        //Main.Log("Dragged to " + e.getRawSlots().size() + " slots");
    }
    //endregion

    //region Player Menu Storing
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
    //endregion

    public static HashMap<String, Menu> RegisteredMenus() { return registeredMenus; }
    public static List<Menu> getRegisteredMenus() { return registeredMenus.values().stream().toList(); }
    public static Inventory getCurrentInventory(Player player) { return getCurrentInventory(player.getUniqueId()); }
    public static Inventory getCurrentInventory(UUID uuid) { return getPage(uuid).getInventory(); }
    public static Page getPage(Player player) { return getPage(player.getUniqueId()); }
    public static Page getPage(UUID uuid) { return inMenus.get(uuid); }
    public static boolean inMenus(Player player) { return inMenus(player.getUniqueId()); }
    public static boolean inMenus(UUID uuid) { return inMenus.containsKey(uuid); }
    public static boolean isTopInventory(int slot, InventoryView view) { return isTopInventory(slot, view.getTopInventory().getSize()); }
    public static boolean isTopInventory(int slot, int size1) { return slot <= size1; }
    public static void registerMenu(Menu menu) { if(!registeredMenus.containsKey(menu.ID)) registeredMenus.put(menu.ID, menu); }
    public static void unregisterMenu(Menu menu) { registeredMenus.remove(menu.ID); }
    public static void removeInMenus(Player player) { removeInMenus(player.getUniqueId()); }
    public static void removeInMenus(UUID uuid) { inMenus.remove(uuid); }
    public static void CallEvent(Event event) { Bukkit.getServer().getPluginManager().callEvent(event); }
    public static void setInMenus(Player player, Page page) { setInMenus(player.getUniqueId(), page); }
    public static void setInMenus(UUID uuid, Page page) {
        Player player = Bukkit.getPlayer(uuid);
        if(inMenus.containsKey(uuid)) CallEvent(new MenuNavigationEvent(player, inMenus.get(uuid), page));
        else CallEvent(new MenuOpenEvent(player, page));
        inMenus.put(uuid, page);
    }
    private int findDestinationSlot(Inventory clickedInventory, Inventory destinationInventory, ItemStack movedItem) {
        int freeSlot = destinationInventory.firstEmpty();
        for (int i = 0; i < destinationInventory.getSize(); i++) {
            ItemStack item = destinationInventory.getItem(i);
            if(item == null || !item.isSimilar(movedItem)) continue;
            if(item.getAmount() >= item.getMaxStackSize()) continue;
            return i;
        }
        return freeSlot;
    }
}
