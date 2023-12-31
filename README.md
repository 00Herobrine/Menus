# Menus
A configurable Minecraft Menu resource for Spigot.
### Features
- Scalable Menus
- Fixed Menus
- Limited Slot Menu
- Menu Events
- Page Insertion
- Enhanced ItemStack

# Code Usage

### Menu Creation
Menu Creation is straight forward, once the resource files are imported to your Workspace you can instanitate a new Menu with your base Title.
```java
Menu menu = new Menu("Scaling Menu");
```
```java
Menu menu = new Menu("Limited Menu", int pageLimit);
```

### Page Creation
Page Creation can be more of a hassle as you are defining more variables and should be handling the paging of a menu yourself.
```java
Page page = new Page("Set Page Title")
```
Pages that are =< 5 slots are put into a hopper inventory.
```java
Page page = new Page("Limited Slot Page", 5)
```

### Menu Item
Pages are comprised by a HashMap of MenuItems, they contain an [ItemBuilder](ItemBuilder), ID, visiblity, slot# and page#.

If a slot or page # is not defined it defaults to -1 meaning automatically slot and/or page the MenuItem.
```java
MenuItem menuItem = new MenuItem(ItemStack itemStack);
```

```java
MenuItem slottedItem = new MenuItem(ItemStack itemStack, int slot);
```
```java
MenuItem pagedSlottedItem = new MenuItem(ItemStack itemStack, int slot, int page);
```
There is another way of defining the slot or page
```java
MenuItem menuItem = new MenuItem(ItemStack itemStack)
menuItem.setSlot(32);
menuItem.setPage(5);
```

### ItemBuilder
```java
ItemBuilder itemBuilder = new ItemBuilder(ItemStack itemStack);
```
```java
ItemBuilder itemBuilder = new ItemBuilder(Material material);
```

### Opening Menus
The method to opening should be familiar as it's just the same was an Inventory.
```java
Menu menu = new Menu("Scaling Menu");
menu.open(Player);
```
There is another way to open the Menu and that is with unclickable filler items in all empty slots.
```java
Menu menu = new Menu("Scaling Menu");
menu.open(Player, true);
```
[ItemBuilder]:<ItemBuilder>
