package com.coinboundplugin.enforcement;

import com.coinboundplugin.CoinboundPlugin;
import com.coinboundplugin.data.ClueTier;
import com.coinboundplugin.unlocks.UnlockEquipslot;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
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

    private static final String[] TELEPORT_OPTIONS = {
            "Barbarian Outpost",
            "Burthorpe",
            "Tears of Guthix",
            "Corporeal Beast",
            "Wintertodt Camp",
            "Emir's Arena",
            "Ferox Enclave",
            "Castle Wars",
            "Fortis Colosseum",
            "Warriors' Guild",
            "Champions' Guild",
            "Monastery",
            "Ranging Guild",
            "Fishing Guild",
            "Mining Guild",
            "Crafting Guild",
            "Cooking Guild",
            "Woodcutting Guild",
            "Farming Guild",
            "Edgeville",
            "Karamja",
            "Draynor Village",
            "Al Kharid",
            "Miscellania",
            "Grand Exchange",
            "Falador Park",
            "Dondakan",
            "Slayer Tower",
            "Fremennik Slayer Dungeon",
            "Tarn's Lair",
            "Stronghold Slayer Cave",
            "Dark Beasts",
            "Digsite",
            "Fossil Island",
            "Lithkren",
            "Wizards' Tower",
            "The Outpost",
            "Eagles' Eyrie",
            "Chaos Temple",
            "Bandit Camp",
            "Lava Maze"
    };

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {

        String target = event.getMenuTarget().toLowerCase();
        String option = event.getMenuOption().toLowerCase();
        String action = event.getMenuAction().toString().toLowerCase();
        String entryType = event.getMenuEntry().toString().toLowerCase();
        Widget widgetID = event.getWidget();
        int widgetIDInt = widgetID != null ? widgetID.getId() : -1;
        boolean isItem = event.getItemId() != -1;

        plugin.Debug("widgetString: '" + widgetIDInt + "'   target: '" + target + "'   option: '" + option + "'   action: '" + action + "'   entryType: '" + entryType + "'");

        if (EQUIP_MENU_OPTIONS.contains(option) && isItem) {
            UnlockEquipslot.EquipSlot slot = getItemEquipmentType(event.getItemId());
            if (!isEquipmentSlotUnlocked(slot)) {
                plugin.ShowPluginChat("<col=ff0000><b>" + slot.getDisplayName() + " slot locked!</b></col> Unlock this slot to be able to equip.", 2394);
                event.consume();
                return;
            }
        }

        if (EAT_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Food") && isItem) {
            plugin.ShowPluginChat("<col=ff0000><b>Eating food locked!</b></col> You haven't unlocked the ability to eat food yet!", 2394);
            event.consume();
            return;
        }

        if (POTIONS_MENU_OPTIONS.contains(option) && !plugin.isUnlocked("Potions") && isItem) {
            plugin.ShowPluginChat("<col=ff0000><b>Drinking potions locked!</b></col> You haven't unlocked the ability to drink potions yet!", 2394);
            event.consume();
        }

        for (ClueTier tier : ClueTier.values()) {
            if (target.contains(tier.getDisplayName().toLowerCase()) && option.contains("open") && !plugin.isUnlocked(tier.getId())) {
                plugin.ShowPluginChat("<col=ff0000><b>" + tier.getDisplayName() + " openings locked!</b></col> You haven't unlocked the ability to open this tier clue caskets yet!", 2394);
                event.consume();
                return;
            }
        }

        // Check jewelery teleports
        if (isJeweleryOption(option, target) && !plugin.isUnlocked("TeleportJewelry")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Jewelery teleports locked</b></col> Teleporting using jewelery is not unlocked yet.", 2394);
            return;
        }

        // Check prayer altar usage
        if (isPrayerAltar(option, target) && !plugin.isUnlocked("PrayerAltars")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Prayer altars locked<//b></col> Restoring pryer using prayer altars is not unlocked yet.", 2394);
            return;
        }

        // Check for refreshment pools
        if (isRefreshmentPool(option, target) && !plugin.isUnlocked("RefreshmentPools")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Refreshment pools locked</b></col> Restoring stats using refreshment pools is not unlocked yet.", 2394);
            return;
        }

        //Check for special attack
        if (widgetIDInt == 10485796 || widgetIDInt == 38862887) {
            if (!plugin.isUnlocked("SpecialAttack")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Special attacks locked</b></col> Using special attacks is not unlocked yet.", 2394);
                return;
            }
        }

        //Check magic protection prayer
        if (widgetIDInt == 35454997) {
            if (option.contains("activate") && !plugin.isUnlocked("MagicProtection")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Magic protection prayer locked</b></col> Using magic protection prayers is not unlocked yet.", 2394);
                return;
            }
        }

        //Check range protection prayer
        if (widgetIDInt == 35454998) {
            if (option.contains("activate") && !plugin.isUnlocked("RangeProtection")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Ranged protection prayer locked</b></col> Using ranged protection prayers is not unlocked yet.", 2394);
                return;
            }
        }

        //Check melee protection prayer
        if (widgetIDInt == 35454999) {
            if (option.contains("activate") && !plugin.isUnlocked("MeleeProtection")) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Melee protection prayer locked</b></col> Using melee protection prayers is not unlocked yet.", 2394);
                return;
            }
        }

        // Check spellbook teleports
        if (isTeleportSpellOption(widgetIDInt) && !plugin.isUnlocked("SpelbookTeleports")) {
            if (event.getMenuAction() == MenuAction.CC_OP ||
                    event.getMenuAction() == MenuAction.CC_OP_LOW_PRIORITY) {
                event.consume();
                plugin.ShowPluginChat("<col=ff0000><b>Teleports locked</b></col> Using the spellbook to teleport is not unlocked yet.", 2394);
            }
            return;
        }

        // Low/High alchemy
        if (plugin.ALCHEMY_WIDGETS.contains(widgetIDInt) && !plugin.isUnlocked("Alchemy")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Alchemy locked</b></col> Using alchemy spells is not unlocked yet.", 2394);
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

        // Check canoe transport
        // TODO: Check if working
        if (isCanoe(option, target) && !plugin.isUnlocked("Canoes")) {
            event.consume();
            plugin.ShowPluginChat("<col=ff0000><b>Canoes locked</b></col> Using canoes is not unlocked yet.", 2394);
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

    //Check for teleport but skip home teleport spells
    private boolean isTeleportSpellOption(int widgetID) {
        return plugin.teleportWidgetIDs.contains(widgetID);
    }

    //Check for teleport but skip home teleport spells
    /*Old version kept for reference
    private boolean isTeleportSpellOption(String option, String target) {
        String tgt = target
                .replaceAll("<.*?>", "")
                .replaceAll("[^a-z ]", "")
                .trim();
        boolean isTeleport = tgt.contains("teleport") || tgt.contains("tele group");
        if (tgt.contains("home") && isTeleport)
            return false;
        return isTeleport;
    }

     */

    //TODO: Make cleaner check here
    private boolean isMinigameTeleportOption(String option, String target) {
        if (option == null) {
            return false;
        }
        String opt = option.trim();
        return (opt.contains("teleport to <col=ff8040>"));
    }

    //Checks for both 'rub' and 'teleport' options on jewelry
    private boolean isJeweleryOption(String option, String target) {
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

        boolean isDirectTeleportOption = false;
        for (String teleportOptions : TELEPORT_OPTIONS) {
            if (opt.toLowerCase().contains(teleportOptions.toLowerCase())) {
                isDirectTeleportOption = true;
                break;
            }
        }
        return ((opt.contains("rub") && hasJewelery)) || (target.equals("") && isDirectTeleportOption);
    }

    private boolean isPrayerAltar(String option, String target) {
        if (!target.contains("altar"))
            return false;
        return (option.contains("pray"));
    }

    private boolean isRefreshmentPool(String option, String target) {
        if (!target.contains("pool"))
            return false;
        return (option.contains("drink"));
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

    private boolean isCanoe(String option, String target) {
        return (option.contains("shape-canoe"));
    }

    UnlockEquipslot.EquipSlot getItemEquipmentType(int itemId) {
        ItemStats itemStats = itemManager.getItemStats(itemId, true);
        if (itemStats == null || !itemStats.isEquipable())
            return null;

        ItemEquipmentStats equipStats = itemStats.getEquipment();
        if (equipStats == null)
            return null;

        // Determine required equipment slot
        return plugin.equipmentSlotBlocker.mapSlotFromEquipStats(equipStats.getSlot());
    }

    boolean isEquipmentSlotUnlocked(UnlockEquipslot.EquipSlot slot) {
        if (slot == null)
            return false;
        return plugin.isUnlocked("EQUIP_" + slot);
    }
}
