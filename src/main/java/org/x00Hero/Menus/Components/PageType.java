package org.x00Hero.Menus.Components;

public enum PageType {
    FIXED(54, "Fixed Menu"), // Pre-render the entire page
    DYNAMIC(54, "Scaling Menu"); // Adjusts the Menu size to remove empty slot

    final int maxSlots;
    final String defaultTitle;
    PageType(int maxPossibleSize, String defaultTitle) {
        this.maxSlots = maxPossibleSize;
        this.defaultTitle = defaultTitle;
    }
}
