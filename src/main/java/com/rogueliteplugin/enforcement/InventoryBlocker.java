/*
https://github.com/CarlOmega/slotless/blob/master/src/main/java/com/slotless/SlotlessPlugin.java
Thanks to CarlOmega for the awesome idea of using items as blocks.
 */

package com.rogueliteplugin.enforcement;
import com.google.inject.Inject;
import com.rogueliteplugin.RoguelitePlugin;
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
    private RoguelitePlugin plugin;

    @Inject
    private ItemManager itemManager;

    private Widget invUpdateWidget;

    //TEMP
    int replaceItemID = ItemID.LEAFLET_DROPPER_FLYER;
    private static final int TOTAL_ROWS = 7;

    public void redrawInventory() {
        client.runScript(914, -2147483644, 1130, 4);
        client.runScript(3281, 2776, 1);
        client.runScript(Objects.requireNonNull(client.getWidget(ComponentID.INVENTORY_CONTAINER)).getOnInvTransmitListener());
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

    /* Unused for now
    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(SlotlessConfig.GROUP)) {
            String key = event.getKey();
            if (getSpendableSlots() < 0) {
                configManager.setConfiguration(SlotlessConfig.GROUP, event.getKey(), event.getOldValue());
                clientThread.invoke(this::warningNotEnoughSlots);
                return;
            }
            if (key.equals("inventorySlotUnlockCount")) {
                if (event.getOldValue() != null && config.inventorySlotUnlockCount() > Integer.parseInt(event.getOldValue())) {
                    clientThread.invoke(() -> openPopUp("Slot Unlocked", String.format("New slot:<br><br><col=ffffff>%s Inventory Slots Unlocked</col>", config.inventorySlotUnlockCount())));
                }
            } else if (key.endsWith("Unlocked") && event.getNewValue() != null && event.getNewValue().equals(Boolean.TRUE.toString())) {
                clientThread.invoke(() -> openPopUp("Equipment Unlocked", String.format("New slot:<br><br><col=ffffff>%s</col>", StringUtils.capitalize(key.substring(0, key.length() - 8)))));
            }
            clientThread.invoke(this::redrawInventory);
        }
    }
     */

    private void replaceInventory(Widget w) {

        plugin.Debug("Replacing inventory fillers...");

        int fillerId = replaceItemID;

        int maxFillerAmount = 28 - (getUnlockedRows() * 4);
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
            plugin.Debug("Inventory item: " + i.getItemId());
            if (i.getItemId() == fillerId) {
                if (diff > 0) {
                    diff--;
                } else {
                    plugin.Debug("Filler for slot "+i.getIndex()+" applied.");
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
    }

    private int getUnlockedRows() {
        int rows = 1; // first row always unlocked
        for (int i = 1; i < TOTAL_ROWS; i++) {
            if (plugin.isUnlocked("InventorySlots" + i))
                rows++;
        }
        return rows;
    }
}
