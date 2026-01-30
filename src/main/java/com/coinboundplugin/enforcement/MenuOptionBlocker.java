package com.coinboundplugin.enforcement;

import com.coinboundplugin.CoinboundPlugin;
import com.coinboundplugin.unlocks.UnlockEquipslot;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuOptionBlocker {
    @Inject
    private Client client;

    @Inject
    private CoinboundPlugin plugin;

    @Inject
    private ItemManager itemManager;

    List<String> EQUIP_MENU_OPTIONS = Arrays.asList("wield", "wear", "equip", "hold", "ride", "chill");
    List<String> EAT_MENU_OPTIONS = Arrays.asList("eat", "consume");  //TODO: Check if more needed
    List<String> POTIONS_MENU_OPTIONS = Arrays.asList("drink"); //TODO: Check if more needed
    List<String> JEWELERY_MENU_OPTIONS = Arrays.asList("necklace", "ring", "amulet", "bracelet");

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String target = event.getMenuTarget().toLowerCase();
        String option = event.getMenuOption().toLowerCase();

        if (EQUIP_MENU_OPTIONS.contains(option)) {
            CheckIfCanEquipItem(event);
            return;
        }

        if (EAT_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Food")) {
            plugin.ShowPluginChat("<col=ff0000><b>Eating food locked!</b></col> You haven't unlocked the ability to eat food yet!", 2394);
            event.consume();
            return;
        }

        if (POTIONS_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Potions")) {
            plugin.ShowPluginChat("<col=ff0000><b>Drinking potions locked!</b></col> You haven't unlocked the ability to drink potions yet!", 2394);
            event.consume();
        }

        // Check spellbook teleports
        if (isTeleportSpellOption(option, target) && !plugin.isUnlocked("SpelbookTeleports")) {
            if (event.getMenuAction() == MenuAction.CC_OP ||
                    event.getMenuAction() == MenuAction.CC_OP_LOW_PRIORITY) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Teleports locked</b></col> Using the spellbook to teleport is not unlocked yet.", 2394);
            }
            return;
        }

        // Check jewelery teleports
        if (isMinigameJeweleryOption(option, target) && !plugin.isUnlocked("TeleportJewelry")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Jewelery teleports locked</b></col> Teleporting using jewelery is not unlocked yet.", 2394);
            return;
        }

        // Check minigame teleports
        if (isMinigameTeleportOption(option, target) && !plugin.isUnlocked("MinigameTeleports")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Minigame teleports locked</b></col> Teleporting to minigames is not unlocked yet.", 2394);
            return;
        }

        // Check agility shortcuts
        if (isAgilityShortcut(event.getId()) && !plugin.isUnlocked("AgilityShortcuts")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Agility shortcuts locked</b></col> Using agility shortcuts is not unlocked yet.", 2394);
            return;
        }

        // Check agility shortcuts
        // TODO: Check if working on all fairy ring types
        if (isFairyRing(event.getId()) && !plugin.isUnlocked("FairyRings")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Fairy ring usage locked</b></col> Using fairy rings is not unlocked yet.", 2394);
            return;
        }

        // Check spirit tree shortcuts
        // TODO: Check if working on all spririt tree types
        if (isSpiritTree(option, target) && !plugin.isUnlocked("SpiritTrees")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Spirit tree usage locked</b></col> Using spirit tree is not unlocked yet.", 2394);
            return;
        }

        // Check Charter ships shortcuts
        if (isChartership(option, target) && !plugin.isUnlocked("CharterShips")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Charter ships usage locked</b></col> Using charter ships is not unlocked yet.", 2394);
            return;
        }

        // Check balloon transport
        // TODO: Check if working
        if (isBaloonTransport(option, target) && !plugin.isUnlocked("BalloonTransport")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Balloon transport usage locked</b></col> Using balloon transport is not unlocked yet.", 2394);
            return;
        }

        // Check gnome glider
        // TODO: Check if working
        if (isGnomeGlider(option, target) && !plugin.isUnlocked("GnomeGliders")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Gnome glider usage locked</b></col> Using gnome glider is not unlocked yet.", 2394);
        }
    }

    private boolean isAgilityShortcut(int objectId) {
        for (AgilityShortcut shortcut : AgilityShortcut.values()) {
            for (int id : shortcut.getObstacleIds()) {
                if (objectId == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTeleportSpellOption(String option, String target) {
        String tgt = target
                .replaceAll("<.*?>", "")
                .replaceAll("[^a-z ]", "")
                .trim();

        if (Objects.equals(target, "home"))
            return false;
        return tgt.contains("teleport") || tgt.contains("tele group");
    }

    private boolean isMinigameTeleportOption(String option, String target) {
        if (option == null) {
            return false;
        }

        String opt = option.trim();
        return (opt.contains("teleport to <col=ff8040>"));
    }

    private boolean isMinigameJeweleryOption(String option, String target) {
        if (option == null) {
            return false;
        }
        String opt = option.trim();
        boolean hasJewelery = false;
        for (String jewelery : JEWELERY_MENU_OPTIONS) {
            if (target.toLowerCase().contains(jewelery)) {
                hasJewelery = true;
                break;
            }
        }
        return (opt.contains("rub") && hasJewelery);
    }

    private boolean isFairyRing(int objectId) {
        return objectId >= 29495 && objectId <= 29624;
    }

    private boolean isSpiritTree(String option, String target) {
        if (!target.contains("spirit tree"))
            return false;
        return (option.contains("travel"));
    }

    private boolean isChartership(String option, String target) {
        if (!target.contains("trader crewmember"))
            return false;
        return (option.contains("charter"));
    }

    //TODO: Add the bolloon objects as check as well ('use basket' might be too generic)
    private boolean isBaloonTransport(String option, String target) {
        if (!target.contains("assistant") && !target.contains("auguste"))
            return false;
        return (option.contains("fly"));
    }

    private boolean isGnomeGlider(String option, String target) {
        if (!target.contains("errdo") && !target.contains("dalbur") && !target.contains("avlafrim") && !target.contains("shoracks"))
            return false;
        return (option.contains("glider"));
    }

    void CheckIfCanEquipItem(MenuOptionClicked event) {
        int itemId = event.getItemId();
        if (itemId <= 0) {
            return;
        }

        ItemStats itemStats = itemManager.getItemStats(itemId, true);
        if (itemStats == null || !itemStats.isEquipable()) {
            return;
        }

        ItemEquipmentStats equipStats = itemStats.getEquipment();
        if (equipStats == null) {
            return;
        }

        // Determine required equipment slot
        UnlockEquipslot.EquipSlot slot = plugin.equipmentSlotBlocker.mapSlotFromEquipStats(equipStats.getSlot());
        if (slot == null) {
            return;
        }

        if (!plugin.isUnlocked("EQUIP_" + slot)) {
            plugin.ShowPluginChat("<col=ff0000><b>" + slot.getDisplayName() + " slot locked!</b></col> Unlock this slot to be able to equip.", 2394);
            event.consume();
        }
    }
}
