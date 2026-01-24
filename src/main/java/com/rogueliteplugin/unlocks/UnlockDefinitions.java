package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.data.ClueTier;
import com.rogueliteplugin.data.ShopCategory;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.requirements.CombatRequirement;
import com.rogueliteplugin.requirements.UnlockIDRequirement;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class UnlockDefinitions {
    private UnlockDefinitions() {
    }

    public static void registerAll(
            UnlockRegistry registry,
            SkillIconManager skillIconManager,
            RoguelitePlugin plugin
    ) {
        registerSkills(registry, skillIconManager);
        registerInventoryUnlocks(registry);
        registerClueTiers(registry);
        registerShops(registry, plugin);
        registerMinigames(registry, plugin);
        registerBosses(registry);
        registerTransport(registry);
        registerQuestTiers(registry);
        registerCurrencies(registry);
        registerEquipmentSlots(registry);
        registerConsumables(registry);
    }

    private static void registerCurrencies(UnlockRegistry registry) {
        registry.register(new CurrencyUnlock(
                "CURRENCY_REROLL",
                "Pack reroll Token",
                IconLoader.load("currency/reroll.png"),
                "Allows you to reroll the current pack options once.",
                CurrencyUnlock.CurrencyType.REROLL,
                1
        ));

        registry.register(new CurrencyUnlock(
                "CURRENCY_SKIP",
                "Challenge skip Token",
                IconLoader.load("currency/skip.png"),
                "Allows you to skip the current challenge.",
                CurrencyUnlock.CurrencyType.SKIP,
                1
        ));
    }

    private static void registerConsumables(UnlockRegistry registry) {
        registry.register(
                new ConsumableUnlock(
                        "Food",
                        "Eat healing food",
                        IconLoader.load("consumables/food.png"), //TODO: Change image
                        "Allows you to eat food to restore HP."
                )
        );
        registry.register(
                new ConsumableUnlock(
                        "Potions",
                        "Drink potions",
                        IconLoader.load("consumables/potions.png"), //TODO: Change image
                        "Allows potions."
                )
        );
    }

    private static void registerTransport(UnlockRegistry registry) {
        String[][] defs = {
                {"FairyRings", "Fairy Rings"},
                {"SpiritTrees", "Spirit Trees"},
                {"SpelbookTeleports", "Teleport using spells"},
                {"MinigameTeleports", "Minigame Teleports"},
                {"CharterShips", "Charter Ships"},
                {"AgilityShortcuts", "Agility Shortcuts"},
                {"BalloonTransport", "Balloon Transport"},
                {"GnomeGliders", "Gnome Gliders"}
        };

        for (String[] def : defs) {
            String id = def[0];
            String name = def[1];
            registry.register(new TransportUnlock(
                    id,
                    name,
                    IconLoader.load("transport/" + id + ".png"),
                    "Allows access to use " + name + " to move around."
            ));
        }
    }

    private static void registerSkills(
            UnlockRegistry registry,
            SkillIconManager skillIconManager
    ) {
        for (Skill skill : Skill.values()) {
            BufferedImage img = skillIconManager.getSkillImage(skill);

            List<AppearRequirement> reqs = new ArrayList<>();

            switch (skill) {
                case SAILING: //Needs pandemonium quest, which released in 2025.
                    reqs.add(new UnlockIDRequirement("Quests2025", registry));
                    break;
                case HERBLORE: //Needs druidic ritual quest, which released in 2002.
                    reqs.add(new UnlockIDRequirement("Quests2002", registry));
                    break;
            }

            if (img != null) {
                registry.register(new SkillUnlock(skill, skillIconManager,reqs));
            }
        }
    }

    private static void registerInventoryUnlocks(UnlockRegistry registry) {
        Integer previousRow = null;

        for (int i = 0; i < 6; i++) {
            List<AppearRequirement> reqs = new ArrayList<>();

            if (previousRow != null)
                reqs.add(new UnlockIDRequirement("InventorySlots" + previousRow, registry));

            registry.register(
                    new InventorySpaceUnlock(
                            "InventoryRow" + i,
                            "Inventory row " + (i + 2),
                            IconLoader.load("inventory/inventoryExpansion.png"),
                            "Access to more inventory space.",
                            reqs
                    )
            );
            previousRow = i;
        }
    }

    private static void registerQuestTiers(UnlockRegistry registry) {
        int[] QUEST_YEARS = {
                2001, 2002, 2003, 2004, 2005, 2006, 2007,
                2016, 2017, 2018, 2019, 2020,
                2021, 2022, 2023, 2024, 2025 //Add 2026 when next quest is released
        };
        Integer previousYear = null;

        for (int year : QUEST_YEARS) {
            List<AppearRequirement> reqs = new ArrayList<>();

            if (previousYear != null)
                reqs.add(new UnlockIDRequirement("Quests" + previousYear, registry));

            registry.register(
                    new QuestUnlock(
                            "Quests" + year,
                            "Quests released in " + year,
                            IconLoader.load("quests/quest_icon.png"),
                            "Access to all quests released in " + year + ".",
                            reqs
                    )
            );
            previousYear = year;
        }
    }

    private static void registerClueTiers(UnlockRegistry registry) {
        for (ClueTier tier : ClueTier.values()) {
            List<AppearRequirement> reqs = new ArrayList<>();

            ClueTier prev = tier.previous();
            if (prev != null)
                reqs.add(new UnlockIDRequirement(prev.getId(), registry));

            registry.register(
                    new ClueUnlock(
                            tier.getId(),
                            tier.getDisplayName(),
                            IconLoader.load(tier.getIconPath()),
                            "Allows opening of " + tier.getDisplayName().toLowerCase() + " clues.",
                            reqs
                    )
            );
        }
    }

    // Java
// In 'src/main/java/com/rogueliteplugin/unlocks/UnlockDefinitions.java'
    private static void registerMinigames(UnlockRegistry registry, RoguelitePlugin plugin) {
        String[][] defs = {
                {"Barbarian_Assault", "Barbarian Assault"},
                {"Bounty_Hunter", "Bounty Hunter"},
                {"Temple_Trekking", "Temple Trekking"},
                {"ChampionsChallenge", "Champions' Challenge"},
                {"CastleWars", "Castle Wars"},
                {"ClanWars", "Clan Wars"},
                {"DuelArena", "Duel Arena"},
                {"MageArena", "Mage Arena"},
                {"NightmareZone", "Nightmare Zone"},
                {"PestControl", "Pest Control"},
                {"TzHaarFightCave", "TzHaar Fight Cave"},
                {"TzHaarFightPit", "TzHaar Fight Pit"},
                {"LastManStanding", "Last Man Standing"},
                {"Inferno", "Inferno"}
        };

        for (String[] def : defs) {
            String id = def[0];
            String name = def[1];
            registry.register(new MinigameUnlock(
                    id,
                    name,
                    IconLoader.load("minigames/" + id + ".png"),
                    "Allows access to the " + name + " minigame"
            ));
        }
    }

    private static void registerShops(UnlockRegistry registry, RoguelitePlugin plugin) {
        for (ShopCategory category : ShopCategory.values()) {
            registry.register(new ShopUnlock(
                    category.name(), // unlock ID
                    category.getDisplayName(),
                    IconLoader.load("shopIcons/" + category.name() + ".png"),
                    "Allows access to the " + category.getDisplayName().toLowerCase()
            ));
        }
    }

    private static void registerBosses(UnlockRegistry registry) {
        registry.register(new BossUnlock("MIMIC", "Mimic", IconLoader.load("icon.png"), "Fight the Mimic.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("OBOR", "Obor", IconLoader.load("icon.png"), "Hill giant boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("BRYOPHYTA", "Bryophyta", IconLoader.load("icon.png"), "Moss giant boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("GIANT_MOLE", "Giant Mole", IconLoader.load("icon.png"), "Boss beneath Falador.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("GROTESQUE_GUARDIANS", "Grotesque Guardians", IconLoader.load("icon.png"), "Slayer tower rooftop boss.", List.of(
                new CombatRequirement(85, 126)
        )));
        registry.register(new BossUnlock("TEMPOROSS", "Tempoross", IconLoader.load("icon.png"), "Fishing skilling boss.", List.of()));
        registry.register(new BossUnlock("WINTERTODT", "Wintertodt", IconLoader.load("icon.png"), "Firemaking skilling boss.", List.of()));
        registry.register(new BossUnlock("BARROWS", "Barrows Brothers", IconLoader.load("icon.png"), "Barrows chest encounter.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("SARACHNIS", "Sarachnis", IconLoader.load("icon.png"), "Spider boss in Forthos Dungeon.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("KALPHITE_QUEEN", "Kalphite Queen", IconLoader.load("icon.png"), "Desert boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("HESPORI", "Hespori", IconLoader.load("icon.png"), "Farming guild boss.", List.of(
                new CombatRequirement(20, 126)
        )));
        registry.register(new BossUnlock("SKOTIZO", "Skotizo", IconLoader.load("icon.png"), "Demonic boss in Catacombs.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("ZALCANO", "Zalcano", IconLoader.load("icon.png"), "Mining boss in Prifddinas.", List.of(

        )));
        registry.register(new BossUnlock("KING_BLACK_DRAGON", "King Black Dragon", IconLoader.load("icon.png"), "Wilderness dragon boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("KRAKEN", "Kraken", IconLoader.load("icon.png"), "Slayer boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("ABYSSAL_SIRE", "Abyssal Sire", IconLoader.load("icon.png"), "Abyssal demon boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("THERMY", "Thermonuclear Smoke Devil", IconLoader.load("icon.png"), "Smoke devil boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("GAUNTLET", "The Gauntlet", IconLoader.load("icon.png"), "Prifddinas challenge.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("ZULRAH", "Zulrah", IconLoader.load("icon.png"), "Poisonous serpent boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("CERBERUS", "Cerberus", IconLoader.load("icon.png"), "Hellhound boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("VORKATH", "Vorkath", IconLoader.load("icon.png"), "Undead dragon boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("GENERAL_GRAARDOR", "General Graardor", IconLoader.load("icon.png"), "Bandos GWD boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("KRIL", "K'ril Tsutsaroth", IconLoader.load("icon.png"), "Zamorak GWD boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("ZILYANA", "Commander Zilyana", IconLoader.load("icon.png"), "Saradomin GWD boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("KREEARRA", "Kree'arra", IconLoader.load("icon.png"), "Armadyl GWD boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("NIGHTMARE", "The Nightmare", IconLoader.load("icon.png"), "Slepe nightmare boss.", List.of(
                new CombatRequirement(70, 126)
        )));
        registry.register(new BossUnlock("CORPOREAL_BEAST", "Corporeal Beast", IconLoader.load("icon.png"), "High-level group boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("COX", "Chambers of Xeric", IconLoader.load("icon.png"), "Raid.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("ALCHEMICAL_HYDRA", "Alchemical Hydra", IconLoader.load("icon.png"), "Hydra slayer boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("NEX", "Nex", IconLoader.load("icon.png"), "Ancient prison boss.", List.of(
                new CombatRequirement(70, 126)
        )));
        registry.register(new BossUnlock("DK_SUPREME", "Dagannoth Supreme", IconLoader.load("icon.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("DK_REX", "Dagannoth Rex", IconLoader.load("icon.png"), "Dagannoth King.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("DK_PRIME", "Dagannoth Prime", IconLoader.load("icon.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("CORRUPTED_GAUNTLET", "Corrupted Gauntlet", IconLoader.load("icon.png"), "Hard mode Gauntlet.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("PHANTOM_MUSPAH", "Phantom Muspah", IconLoader.load("icon.png"), "Ancient ice boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("CHAOS_ELEMENTAL", "Chaos Elemental", IconLoader.load("icon.png"), "Wilderness boss.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("ARTIO", "Artio", IconLoader.load("icon.png"), "Callisto variant.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("CALVARION", "Calvar'ion", IconLoader.load("icon.png"), "Vet'ion variant.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("SPINDEL", "Spindel", IconLoader.load("icon.png"), "Venenatis variant.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("CHAOS_FANATIC", "Chaos Fanatic", IconLoader.load("icon.png"), "Wilderness mage boss.", List.of(
                new CombatRequirement(30, 126)
        )));
        registry.register(new BossUnlock("CRAZY_ARCHAEOLOGIST", "Crazy Archaeologist", IconLoader.load("icon.png"), "Wilderness boss.", List.of(
                new CombatRequirement(30, 126)
        )));
        registry.register(new BossUnlock("DERANGED_ARCHAEOLOGIST", "Deranged Archaeologist", IconLoader.load("icon.png"), "Fossil Island boss.", List.of(
                new CombatRequirement(20, 126)
        )));
        registry.register(new BossUnlock("SCORPIA", "Scorpia", IconLoader.load("icon.png"), "Wilderness scorpion.", List.of(
                new CombatRequirement(50, 126)
        )));
        registry.register(new BossUnlock("WHISPERER", "The Whisperer", IconLoader.load("icon.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126)
        )));
        registry.register(new BossUnlock("DUKE", "Duke Sucellus", IconLoader.load("icon.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126)
        )));
        registry.register(new BossUnlock("LEVIATHAN", "The Leviathan", IconLoader.load("icon.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126)
        )));
        registry.register(new BossUnlock("VARDORVIS", "Vardorvis", IconLoader.load("icon.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126)
        )));
        registry.register(new BossUnlock("SCURRIUS", "Scurrius", IconLoader.load("icon.png"), "Varrock sewer rat boss.", List.of(
                new CombatRequirement(10, 126)
        )));
        registry.register(new BossUnlock("ARAXXOR", "Araxxor", IconLoader.load("icon.png"), "Spider boss.", List.of(
                new CombatRequirement(60, 126)
        )));
        registry.register(new BossUnlock("AMOXLIATL", "Amoxliatl", IconLoader.load("icon.png"), "Varlamore boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("HUEYCOATL", "The Hueycoatl", IconLoader.load("icon.png"), "Varlamore boss.", List.of(
                new CombatRequirement(20, 126)
        )));

    }

    private static void registerEquipmentSlots(UnlockRegistry registry) {
        for (UnlockEquipslot.EquipSlot slot : UnlockEquipslot.EquipSlot.values()) {
            final String id = "EQUIP_" + slot.toIdSuffix();
            final String name = slot.getDisplayName() + " slot";
            final String description = "Allows equipping items in the " + slot.getDisplayName().toLowerCase() + " slot.";
            registry.register(new UnlockEquipslot(id, name, IconLoader.load("equipmentslots/" + slot.toIdSuffix() + ".png"), description, slot));
        }
    }
}

