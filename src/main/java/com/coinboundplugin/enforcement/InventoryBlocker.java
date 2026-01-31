/*
https://github.com/CarlOmega/slotless/blob/master/src/main/java/com/slotless/SlotlessPlugin.java
Thanks to CarlOmega for the awesome idea of using items as blocks.
 */

package com.coinboundplugin.enforcement;

import com.google.inject.Inject;
import com.coinboundplugin.CoinboundPlugin;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;

import java.util.Arrays;
import java.util.Objects;

public class InventoryBlocker {
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private CoinboundPlugin plugin;

    @Inject
    private ItemManager itemManager;

    private Widget invUpdateWidget;

    public void redrawInventory() {
        clientThread.invokeLater(() -> {
            client.runScript(914, -2147483644, 1130, 4);
            client.runScript(3281, 2776, 1);
            Widget inventoryWidget = client.getWidget(ComponentID.INVENTORY_CONTAINER);
            if (inventoryWidget != null) {
                Object[] listener = inventoryWidget.getOnInvTransmitListener();
                if (listener != null) {
                    client.runScript(listener);
                }
            }
        });
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired scriptPreFired) {
        // [proc,interface_inv_update_big]
        if (scriptPreFired.getScriptId() == 153) {
            // [proc,interface_inv_update_big](component $component0, inv $inv1, int $int2, int $int3, int $int4, component $component5, string $string0, string $string1, string $string2, string $string3, string $string4, string $string5, string $string6, string $string7, string $string8)
            int w = client.getIntStack()[client.getIntStackSize() - 6]; // first argument
            invUpdateWidget = client.getWidget(w);
        }
        // [proc,deathkeep_left_setsection]
        else if (scriptPreFired.getScriptId() == 975) {
            // [proc,deathkeep_left_setsection](string $text0, component $component0, int $comsubid1, int $int2, int $int3, int $int4)
            int w = client.getIntStack()[client.getIntStackSize() - 5]; // second argument
            invUpdateWidget = client.getWidget(w);
        }
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired scriptPostFired) {
        final int id = scriptPostFired.getScriptId();
        // [proc,inventory_build]
        if (id == ScriptID.INVENTORY_DRAWITEM - 1) {
            replaceInventory(client.getWidget(ComponentID.INVENTORY_CONTAINER));
        }
        // [proc,ge_pricechecker_redraw]
        else if (id == 787) {
            replaceInventory(client.getWidget(ComponentID.GUIDE_PRICES_INVENTORY_ITEM_CONTAINER));
        }
        // [proc,interface_inv_update_big]
        // [proc,deathkeep_left_redraw]
        else if (id == 153 || id == 975) {
            if (invUpdateWidget != null) {
                replaceInventory(invUpdateWidget);
                invUpdateWidget = null;
            }
        }
        // [proc,bankside_build]
        else if (id == 296) {
            replaceInventory(client.getWidget(ComponentID.BANK_INVENTORY_ITEM_CONTAINER));
        }
        // [proc,shop_interface]
        else if (id == 1074) {
            replaceInventory(client.getWidget(ComponentID.INVENTORY_CONTAINER));
        }
    }

    private void replaceInventory(Widget w) {

        int fillerId = plugin.replaceItemID;

        int maxFillerAmount = 28 - (getUnlockedSlots());

        boolean replaceItemWithBlocks = true;
        if (w == null || !replaceItemWithBlocks) {
            return;
        }
        Widget[] children = w.getDynamicChildren();
        int fillerCount = 0;
        for (Widget i : children) {
            if (i.getItemId() == fillerId) {
                fillerCount++;
            }
        }
        int diff = fillerCount - maxFillerAmount;

        for (Widget i : children) {
            if (i.getItemId() == fillerId) {
                if (diff > 0) {
                    diff--;
                } else  if (plugin.fillerItemsShort <= 0){
                    i.setName("Filler");
                    i.setTargetVerb(null);
                    i.setItemId(ItemID.BANK_FILLER);
                    i.setClickMask(0);
                    i.setOnDragCompleteListener((Object[]) null);
                    i.setOnDragListener((Object[]) null);
                    Arrays.fill(Objects.requireNonNull(i.getActions()), "");
                }
            }
        }

        // Set to the number of filler items needed (can be negative if there are too many)
        plugin.setFillerItemsShortAmount(maxFillerAmount - fillerCount);
    }

    int getUnlockedSlots() {
        int unlockedSlots = 1; // First slot is always unlocked
        for (int i = 1; i < 28; i++) {
            if (plugin.isUnlocked("InventorySlot" + (i + 1))) {
                unlockedSlots++;
            }
        }
        return unlockedSlots;
    }
}