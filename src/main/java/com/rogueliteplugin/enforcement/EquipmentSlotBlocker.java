package com.rogueliteplugin.enforcement;

import com.google.inject.Inject;
import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.unlocks.UnlockEquipslot;

import java.awt.Color;
import java.util.*;

import net.runelite.api.*;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;

public class EquipmentSlotBlocker {
    private static final int LOCK_SPRITE_ID = 1342; // padlock icon
    private static final int LOCK_ICON_CHILD_ID = 1001;
    private static final int GRAY_OVERLAY_CHILD_ID = 998;

    private static final int GRAY_OPACITY = 160;

    @Inject
    private Client client;

    @Inject
    private RoguelitePlugin plugin;

    @Inject
    private ItemManager itemManager;

    @Inject
    private ClientThread clientThread;

    // Map EquipSlot enum to EquipmentInventorySlot and widget child IDs
    private static final UnlockEquipslot.EquipSlot[] EQUIP_SLOTS = {
            UnlockEquipslot.EquipSlot.AMMO,
            UnlockEquipslot.EquipSlot.AMULET,
            UnlockEquipslot.EquipSlot.BODY,
            UnlockEquipslot.EquipSlot.BOOTS,
            UnlockEquipslot.EquipSlot.CAPE,
            UnlockEquipslot.EquipSlot.GLOVES,
            UnlockEquipslot.EquipSlot.HEAD,
            UnlockEquipslot.EquipSlot.LEGS,
            UnlockEquipslot.EquipSlot.RING,
            UnlockEquipslot.EquipSlot.SHIELD,
            UnlockEquipslot.EquipSlot.WEAPON,
    };

    // Map item equipment slot value to UnlockEquipslot.EquipSlot
    public UnlockEquipslot.EquipSlot mapSlotFromEquipStats(int slotValue) {
        switch (slotValue) {
            case 0:
                return UnlockEquipslot.EquipSlot.HEAD;
            case 1:
                return UnlockEquipslot.EquipSlot.CAPE;
            case 2:
                return UnlockEquipslot.EquipSlot.AMULET;
            case 3:
                return UnlockEquipslot.EquipSlot.WEAPON;
            case 4:
                return UnlockEquipslot.EquipSlot.BODY;
            case 5:
                return UnlockEquipslot.EquipSlot.SHIELD;
            case 7:
                return UnlockEquipslot.EquipSlot.LEGS;
            case 9:
                return UnlockEquipslot.EquipSlot.GLOVES;
            case 10:
                return UnlockEquipslot.EquipSlot.BOOTS;
            case 12:
                return UnlockEquipslot.EquipSlot.RING;
            case 13:
                return UnlockEquipslot.EquipSlot.AMMO;
            default:
                return null;
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() != WidgetID.EQUIPMENT_GROUP_ID) {
            return;
        }
        refreshAll();
    }

    //Map of EquipSlot to widget child index in the equipment interface
    private static final Map<UnlockEquipslot.EquipSlot, Integer> SLOT_INDEX =
            new EnumMap<>(UnlockEquipslot.EquipSlot.class);

    static {
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.HEAD, 10);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.CAPE, 11);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.AMULET, 12);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.WEAPON, 13);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.BODY, 14);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.SHIELD, 15);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.LEGS, 16);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.GLOVES, 17);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.BOOTS, 18);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.RING, 19);
        SLOT_INDEX.put(UnlockEquipslot.EquipSlot.AMMO, 20);
    }

    public void refreshAll() {

        plugin.Debug("Refreshing equipment slot overlays");
        Widget container = client.getWidget(InterfaceID.Wornitems.UNIVERSE);
        if (container == null)
            return;

        Widget[] children = container.getStaticChildren();
        if (children == null)
            return;


        clientThread.invokeLater(() -> {
            for (UnlockEquipslot.EquipSlot slot : EQUIP_SLOTS) {
                Integer idx = SLOT_INDEX.get(slot);
                if (idx == null || idx >= children.length)
                    continue;

                Widget slotWidget = children[idx];
                if (slotWidget != null) {
                    applyGrayOverlay(slotWidget, slot);
                }
            }
        });
    }


    private void applyGrayOverlay(Widget parent, UnlockEquipslot.EquipSlot slot) {
        if (parent == null) {
            return;
        }

        String slotId = "EQUIP_" + slot.toIdSuffix();
        boolean allowed = plugin.isUnlocked(slotId);

        Widget gray = parent.getChild(GRAY_OVERLAY_CHILD_ID);
        if (gray == null) {
            gray = parent.createChild(GRAY_OVERLAY_CHILD_ID, WidgetType.RECTANGLE);
            gray.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            gray.setYPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            gray.setWidthMode(WidgetSizeMode.MINUS);
            gray.setHeightMode(WidgetSizeMode.MINUS);
            gray.setOriginalWidth(0);
            gray.setOriginalHeight(0);
            gray.setFilled(true);
            gray.setHasListener(false);
            gray.setTextColor(Color.BLACK.getRGB());
        }

        Widget lockIcon = parent.getChild(LOCK_ICON_CHILD_ID);
        if (lockIcon == null) {
            lockIcon = parent.createChild(LOCK_ICON_CHILD_ID, WidgetType.GRAPHIC);
            lockIcon.setSpriteId(LOCK_SPRITE_ID);
            lockIcon.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            lockIcon.setYPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            lockIcon.setOriginalWidth(14);
            lockIcon.setOriginalHeight(14);
            lockIcon.setHasListener(false);
        }

        if (allowed) {
            gray.setOpacity(255);
            lockIcon.setHidden(true);
        } else {
            gray.setHidden(false);
            gray.setOpacity(GRAY_OPACITY);
            lockIcon.setHidden(false);
        }

        gray.revalidate();
        lockIcon.revalidate();
    }

    private void removeGrayOverlay(Widget parent) {
        if (parent == null) {
            return;
        }

        Widget gray = parent.getChild(GRAY_OVERLAY_CHILD_ID);
        if (gray != null) {
            gray.setOpacity(0);
            gray.setHidden(true);
        }

        Widget lockIcon = parent.getChild(LOCK_ICON_CHILD_ID);
        if (lockIcon != null)
            lockIcon.setHidden(true);
    }

    public void clearAll() {
        plugin.Debug("Clearing all equipment slot overlays");
        Widget container = client.getWidget(InterfaceID.Wornitems.UNIVERSE);
        if (container == null) {
            return;
        }

        Widget[] children = container.getStaticChildren();
        if (children == null)
            return;

        clientThread.invokeLater(() -> {
            for (UnlockEquipslot.EquipSlot slot : EQUIP_SLOTS) {
                Integer idx = SLOT_INDEX.get(slot);
                if (idx == null || idx >= children.length)
                    continue;

                Widget slotWidget = children[idx];
                removeGrayOverlay(slotWidget);
            }
        });
    }
}