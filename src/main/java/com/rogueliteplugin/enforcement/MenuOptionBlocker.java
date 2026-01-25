package com.rogueliteplugin.enforcement;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.unlocks.UnlockEquipslot;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class MenuOptionBlocker {
    @Inject
    private Client client;

    @Inject
    private RoguelitePlugin plugin;

    @Inject
    private ItemManager itemManager;

    List<String> EQUIP_MENU_OPTIONS = Arrays.asList("Wield", "Wear", "Equip", "Hold", "Ride", "Chill");
    List<String> EAT_MENU_OPTIONS = Arrays.asList("Eat", "Consume");  //TODO: Check if more needed
    List<String> POTIONS_MENU_OPTIONS = Arrays.asList("Drink"); //TODO: Check if more needed

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String target = event.getMenuTarget();
        String option = event.getMenuOption();

        if (EQUIP_MENU_OPTIONS.contains(option)) {
            CheckIfCanEquipItem(event);
            return;
        }

        if (EAT_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Food")) {
            plugin.ShowPluginChat("<col=ff0000><b>Eating food locked!</b></col> You haven't unlocked the ability to eat food yet!", true);
            event.consume();
            return;
        }

        if (POTIONS_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Potions")) {
            plugin.ShowPluginChat("<col=ff0000><b>Drinking potions locked!</b></col> You haven't unlocked the ability to drink potions yet!", true);
            event.consume();
        }

        // Check spellbook teleports
        if (isTeleportSpellOption(target) && !plugin.isUnlocked("SpelbookTeleports")) {
            if (event.getMenuAction() == MenuAction.CC_OP ||
                    event.getMenuAction() == MenuAction.CC_OP_LOW_PRIORITY) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Teleports locked</b></col> Using the spellbook to teleport is not unlocked yet.", true);
            }
            return;
        }

        // Check minigame teleports
        if (isMinigameTeleportOption(option, target) && !plugin.isUnlocked("MinigameTeleports")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Minigame teleports locked</b></col> Teleporting to minigames is not unlocked yet.", true);
            return;
        }

        // Check agility shortcuts
        if (isAgilityShortcut(event.getId()) && !plugin.isUnlocked("AgilityShortcuts")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Agility shortcuts locked</b></col> Using agility shortcuts is not unlocked yet.", true);
            return;
        }

        // Check agility shortcuts
        // TODO: Check if working on all fairy ring types
        if (isFairyRing(event.getId()) && !plugin.isUnlocked("FairyRings")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Fairy ring usage locked</b></col> Using fairy rings is not unlocked yet.", true);
            return;
        }

        // Check spirit tree shortcuts
        // TODO: Check if working on all spririt tree types
        if (isSpiritTree(event.getId()) && !plugin.isUnlocked("SpiritTrees")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Spirit tree usage locked</b></col> Using spirit tree is not unlocked yet.", true);
            return;
        }

        // Check Charter ships shortcuts
        if (isChartership(option) && !plugin.isUnlocked("CharterShips")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Charter ships usage locked</b></col> Using charter ships is not unlocked yet.", true);
            return;
        }

        // Check balloon transport
        // TODO: Check if working
        if (isBaloonTransport(option) && !plugin.isUnlocked("BalloonTransport")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Balloon transport usage locked</b></col> Using balloon transport is not unlocked yet.", true);
            return;
        }

        // Check gnome glider
        // TODO: Check if working
        if (isGnomeGlider(option) && !plugin.isUnlocked("GnomeGliders")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Gnome glider usage locked</b></col> Using gnome glider is not unlocked yet.", true);
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

    private boolean isTeleportSpellOption(String target) {
        String tgt = target.toLowerCase()
                .replaceAll("<.*?>", "")
                .replaceAll("[^a-z ]", "")
                .trim();

        return tgt.contains("teleport") || tgt.contains("tele group");
    }

    private boolean isMinigameTeleportOption(String option, String target) {
        if (option == null) {
            return false;
        }

        String opt = option.toLowerCase().trim();
        return (opt.contains("teleport to <col=ff8040>"));
    }

    private boolean isFairyRing(int objectId) {
        return objectId >= 29495 && objectId <= 29624;
    }

    private boolean isSpiritTree(int objectId) {
        int[] objectToCheck = {ObjectID.SPIRITTREE_SMALL, ObjectID.SPIRITTREE_BIG_1OP, ObjectID.SPIRITTREE_BIG_2OPS,
                ObjectID.SPIRITTREE_BIG_2OPS_ORBS, ObjectID.SPIRITTREE_SMALL_1OP, ObjectID.SPIRITTREE_SMALL_2OPS};

        for (int id : objectToCheck) {
            if (id == objectId) {
                return true;
            }
        }
        return false;
    }

    private boolean isChartership(String option) {
        String opt = option.toLowerCase().trim();
        plugin.Debug("Click options: " + opt);
        return (opt.contains("charter"));
    }

    //TODO: Check all NPCs that offer balloon transport
    private boolean isBaloonTransport(String option) {
        String opt = option.toLowerCase().trim();
        plugin.Debug("Click options: " + opt);
        return false;
    }

    private boolean isGnomeGlider(String option) {
        String opt = option.toLowerCase().trim();
        plugin.Debug("Click options: " + opt);
        return (opt.contains("glider"));
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
            plugin.ShowPluginChat("<col=ff0000><b>"+slot.getDisplayName() +" slot locked!</b></col> Unlock this slot to be able to equip.", true);
            event.consume();
        }
    }
}
