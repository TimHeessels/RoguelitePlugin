package com.rogueliteplugin.enforcement;

import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.AgilityShortcut;

import javax.inject.Inject;
import java.util.Arrays;

public class TransportBlocker {
    @Inject
    private Client client;

    @Inject
    private RoguelitePlugin plugin;

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String target = event.getMenuTarget();
        String option = event.getMenuOption();

        // Check spellbook teleports
        if (isTeleportSpellOption(target)) {
            if (!plugin.isUnlocked("SpelbookTeleports")) {
                if (event.getMenuAction() == MenuAction.CC_OP ||
                        event.getMenuAction() == MenuAction.CC_OP_LOW_PRIORITY) {
                    event.consume();
                    plugin.ShowPluginChat("<col=ff0000><b>Teleports locked</b></col> Using the spellbook to teleport is not unlocked yet.", true);
                }
            }
            return;
        }

        // Check minigame teleports
        if (isMinigameTeleportOption(option, target)) {
            if (!plugin.isUnlocked("MinigameTeleports")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Minigame teleports locked</b></col> Teleporting to minigames is not unlocked yet.", true);
            }
            return;
        }

        // Check agility shortcuts
        if (isAgilityShortcut(event.getId())) {
            if (!plugin.isUnlocked("AgilityShortcuts")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Agility shortcuts locked</b></col> Using agility shortcuts is not unlocked yet.", true);
            }
            return;
        }

        // Check agility shortcuts
        // TODO: Check if working
        if (isFairyRing(event.getId())) {
            if (!plugin.isUnlocked("FairyRings")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Fairy ring usage locked</b></col> Using fairy rings is not unlocked yet.", true);
            }
            return;
        }

        // Check spirit tree shortcuts
        // TODO: Check if working
        if (isSpiritTree(event.getId())) {
            if (!plugin.isUnlocked("SpiritTrees")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Spirit tree usage locked</b></col> Using spirit tree is not unlocked yet.", true);
            }
            return;
        }

        // Check teleport tab shortcuts
        // TODO: Check if working
        if (isTeleportTablet(event.getId())) {
            if (!plugin.isUnlocked("TeleportTablets")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Teleport tablets usage locked</b></col> Using teleport tree is not unlocked yet.", true);
            }
            return;
        }

        // Check Charter ships shortcuts
        // TODO: Check if working
        if (isChartership(event.getId())) {
            if (!plugin.isUnlocked("CharterShips")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Charter ships usage locked</b></col> Using charter ships is not unlocked yet.", true);
            }
            return;
        }

        // Check balloon transport
        // TODO: Check if working
        if (isBaloonTransport(event.getId())) {
            if (!plugin.isUnlocked("BalloonTransport")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Balloon transport usage locked</b></col> Using balloon transport is not unlocked yet.", true);
            }
            return;
        }

        // Check gnome glider
        // TODO: Check if working
        if (isGnomeGlider(event.getId())) {
            if (!plugin.isUnlocked("GnomeGliders")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Gnome glider usage locked</b></col> Using gnome glider is not unlocked yet.", true);
            }
            return;
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
        plugin.Debug("Checking spirit tree for object ID: " + objectId);

        int[] objectToCheck = {ObjectID.SPIRITTREE_SMALL, ObjectID.SPIRITTREE_BIG_1OP, ObjectID.SPIRITTREE_BIG_2OPS,
                ObjectID.SPIRITTREE_BIG_2OPS_ORBS, ObjectID.SPIRITTREE_SMALL_1OP, ObjectID.SPIRITTREE_SMALL_2OPS};

        for (int id : objectToCheck) {
            if (id == objectId) {
                return true;
            }
        }
        return false;
    }

    //TODO: Get the object IDs for teleport tablets
    private boolean isTeleportTablet(int objectId) {
        int[] objectToCheck = {

        };

        for (int id : objectToCheck) {
            if (id == objectId) {
                return true;
            }
        }
        return false;
    }

    //TODO: Get the object IDs for charter ships
    private boolean isChartership(int objectId) {
        return false;
    }

    //TODO: Get the object IDs for balloon transport
    private boolean isBaloonTransport(int objectId) {
        return false;
    }

    //TODO: Get the object IDs for gnome glider
    private boolean isGnomeGlider(int objectId) {
        return false;
    }
}
