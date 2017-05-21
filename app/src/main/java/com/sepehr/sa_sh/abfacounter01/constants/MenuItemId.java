package com.sepehr.sa_sh.abfacounter01.constants;

/**
 * Created by sa-sh on 7/25/2016.
 */
public enum MenuItemId {
    MENU_ITEM_REPORT(1),
    MENU_ITEM_MORE_INFO(2),
    MENU_ITEM_LOCATION(3),
    MENU_ITEM_CAMERA(4),
    MENU_ITEM_SEARCH(5),
    MENU_ITEM_VIEWPAGER_FILTER(6),
    MENU_ITEM_LOCK_INPUT(7),
    MENU_ITEM_QEIRE_MOJAZ(8),
    MENUE_ITEM_CONTACT_US(9),
    MENUE_ITEM_DISPLAY_LAST_UNREAD(10);

    private final int value;
    MenuItemId(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
