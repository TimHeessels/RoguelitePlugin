package com.coinboundplugin.unlocks;

import com.coinboundplugin.CoinboundPlugin;
import com.coinboundplugin.CoinboundQuestRequirement;
import com.coinboundplugin.data.ClueTier;
import com.coinboundplugin.data.ShopCategory;
import com.coinboundplugin.requirements.*;
import net.runelite.api.Quest;
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
            CoinboundPlugin plugin
    ) {

        registerClueTiers(registry);
        /*
        registerProtectionPrayers(registry);
        registerOther(registry);
        registerSkills(registry, skillIconManager);
        registerInventoryUnlocks(registry);
        registerShops(registry, plugin);
        registerMinigames(registry, plugin);
        registerBosses(registry);
        registerTransport(registry);
        registerQuests(registry);
        registerEquipmentSlots(registry);
        registerRestoration(registry);
         */
    }

    private static void registerOther(UnlockRegistry registry) {
        registry.register(
                new Other(
                        "Alchemy",
                        "Cast Low/High Alchemy",
                        IconLoader.load("other/Alchemy.png"),
                        "Allows casting the Low and High Alchemy spell.",
                        List.of(new UnlockIDRequirement("SKILL_" + Skill.MAGIC, registry))
                )
        );
        registry.register(
                new Other(
                        "SpecialAttack",
                        "Use special attacks",
                        IconLoader.load("other/SpecialAttack.png"),
                        "Allows using special attacks on weapons.",
                        List.of(new CombatRequirement(40, 126))
                )
        );
    }

    private static void registerProtectionPrayers(UnlockRegistry registry) {
        registry.register(
                new ProtectionPrayer(
                        "MagicProtection",
                        "Magic protection prayer",
                        IconLoader.load("protection/Magic.png"),
                        "Allows using the magic protection prayer.",
                        List.of(new UnlockIDRequirement("SKILL_" + Skill.PRAYER, registry))
                )
        );
        registry.register(
                new ProtectionPrayer(
                        "RangeProtection",
                        "Range protection prayer",
                        IconLoader.load("protection/Range.png"),
                        "Allows using the range protection prayer.",
                        List.of(new UnlockIDRequirement("SKILL_" + Skill.PRAYER, registry))
                )
        );
        registry.register(
                new ProtectionPrayer(
                        "MeleeProtection",
                        "Melee protection prayer",
                        IconLoader.load("protection/Melee.png"),
                        "Allows using the melee protection prayer.",
                        List.of(new UnlockIDRequirement("SKILL_" + Skill.PRAYER, registry))
                )
        );
    }

    private static void registerRestoration(UnlockRegistry registry) {
        registry.register(
                new ResorationUnlock(
                        "Food",
                        "Eat healing food",
                        IconLoader.load("consumables/food.png"),
                        "Allows you to eat food to restore HP.",
                        List.of()
                )
        );
        registry.register(
                new ResorationUnlock(
                        "Potions",
                        "Drink potions",
                        IconLoader.load("consumables/potions.png"),
                        "Allows drinking potions.",
                        List.of()
                )
        );
        registry.register(
                new ResorationUnlock(
                        "PrayerAltars",
                        "Restore at altars",
                        IconLoader.load("consumables/altar.png"),
                        "Allows restoring player points at altars.",
                        List.of(new UnlockIDRequirement("SKILL_" + Skill.MAGIC, registry))
                )
        );
        registry.register(
                new ResorationUnlock(
                        "RefreshmentPools",
                        "Drink from refreshment pools",
                        IconLoader.load("consumables/pool.png"),
                        "Allows restoring stats at pools.",
                        List.of()
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
                {"GnomeGliders", "Gnome Gliders"},
                {"TeleportJewelry", "Teleport Jewelry"},
                {"Canoes", "Canoes"}
        };

        for (String[] def : defs) {
            String id = def[0];
            String name = def[1];
            registry.register(new TransportUnlock(
                    id,
                    name,
                    IconLoader.load("transport/" + id + ".png"),
                    "Allows access to use " + name + " to move around.",
                    List.of(new MemberRequirement())
            ));
        }
    }

    static List<Skill> f2pSkills = List.of(
            Skill.ATTACK,
            Skill.STRENGTH,
            Skill.DEFENCE,
            Skill.RANGED,
            Skill.PRAYER,
            Skill.MAGIC,
            Skill.HITPOINTS,
            Skill.CRAFTING,
            Skill.MINING,
            Skill.SMITHING,
            Skill.FISHING,
            Skill.COOKING,
            Skill.WOODCUTTING,
            Skill.FIREMAKING
    );

    private static void registerSkills(
            UnlockRegistry registry,
            SkillIconManager skillIconManager
    ) {
        for (Skill skill : Skill.values()) {
            BufferedImage img = skillIconManager.getSkillImage(skill);

            List<AppearRequirement> reqs = new ArrayList<>();

            if (!f2pSkills.contains(skill))
                reqs.add(new MemberRequirement());

            switch (skill) {
                case SAILING: //Needs pandemonium quest
                    reqs.add(new UnlockIDRequirement("Quests" + Quest.PANDEMONIUM, registry));
                    break;
                case HERBLORE: //Needs druidic ritual quest
                    reqs.add(new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, registry));
                    break;
            }

            if (img != null) {
                registry.register(new SkillUnlock(skill, skillIconManager, reqs));
            }
        }
    }

    private static void registerInventoryUnlocks(UnlockRegistry registry) {
        Integer previousSlot = null;

        for (int i = 2; i < 28; i++) {
            List<AppearRequirement> reqs = new ArrayList<>();

            if (previousSlot != null)
                reqs.add(new UnlockIDRequirement("InventorySlot" + previousSlot, registry));

            registry.register(
                    new InventorySpaceUnlock(
                            "InventorySlot" + i,
                            "Unlock inventory slot " + String.format("%02d", i),
                            IconLoader.load("inventory/inventoryExpansion.png"),
                            "Access to another inventory slot.",
                            reqs
                    )
            );
            previousSlot = i;
        }
    }

    private static void registerQuests(UnlockRegistry registry) {
        for (Quest quest : Quest.values()) {
            List<AppearRequirement> reqs = CoinboundQuestRequirement.getRequirementsForQuest(quest, registry);
            if (reqs != null) //Only register quests that have requirements (others are unlocked at the start)
                registry.register(
                        new QuestUnlock(
                                "Quests" + quest,
                                quest.getName(),
                                IconLoader.load("quests/quest_icon.png"),
                                "Allows you to start and finish " + quest.getName(),
                                reqs
                        )
                );
        }
    }

    private static void registerClueTiers(UnlockRegistry registry) {
        for (ClueTier tier : ClueTier.values()) {
            List<AppearRequirement> reqs = new ArrayList<>();

            ClueTier prev = tier.previous();
            if (prev != null)
                reqs.add(new UnlockIDRequirement(prev.getId(), registry));

            reqs.add(new MemberRequirement());

            registry.register(
                    new ClueUnlock(
                            tier.getId(),
                            tier.getDisplayName(),
                            IconLoader.load(tier.getIconPath()),
                            "Allows opening of " + tier.getDisplayName().toLowerCase() + "s.",
                            reqs
                    )
            );
        }
    }

    private static void registerMinigames(UnlockRegistry registry, CoinboundPlugin plugin) {
        registry.register(new MinigameUnlock("Barbarian_Assault", "Barbarian Assault",
                IconLoader.load("minigames/Barbarian_Assault.png"),
                "Allows access to the Barbarian Assault minigame",
                List.of(new MemberRequirement(), new CombatRequirement(40, 126))));

        registry.register(new MinigameUnlock("Bounty_Hunter", "Bounty Hunter",
                IconLoader.load("minigames/Bounty_Hunter.png"),
                "Allows access to the Bounty Hunter minigame",
                List.of(new MemberRequirement(), new CombatRequirement(20, 126))));

        registry.register(new MinigameUnlock("Temple_Trekking", "Temple Trekking",
                IconLoader.load("minigames/Temple_Trekking.png"),
                "Allows access to the Temple Trekking minigame",
                List.of(new MemberRequirement())));

        registry.register(new MinigameUnlock("ChampionsChallenge", "Champions' Challenge",
                IconLoader.load("minigames/ChampionsChallenge.png"),
                "Allows access to the Champions' Challenge minigame",
                List.of(new MemberRequirement(), new CombatRequirement(50, 126))));

        registry.register(new MinigameUnlock("CastleWars", "Castle Wars",
                IconLoader.load("minigames/CastleWars.png"),
                "Allows access to the Castle Wars minigame",
                List.of(new CombatRequirement(5, 126))));

        registry.register(new MinigameUnlock("ClanWars", "Clan Wars",
                IconLoader.load("minigames/ClanWars.png"),
                "Allows access to the Clan Wars minigame",
                List.of(new CombatRequirement(5, 126))));

        registry.register(new MinigameUnlock("DuelArena", "Duel Arena",
                IconLoader.load("minigames/DuelArena.png"),
                "Allows access to the Duel Arena minigame",
                List.of(new MemberRequirement(), new CombatRequirement(15, 126))));

        registry.register(new MinigameUnlock("MageArena", "Mage Arena",
                IconLoader.load("minigames/MageArena.png"),
                "Allows access to the Mage Arena minigame",
                List.of(new MemberRequirement(), new CombatRequirement(40, 126))));

        registry.register(new MinigameUnlock("NightmareZone", "Nightmare Zone",
                IconLoader.load("minigames/NightmareZone.png"),
                "Allows access to the Nightmare Zone minigame",
                List.of(new MemberRequirement(), new CombatRequirement(20, 126))));

        registry.register(new MinigameUnlock("PestControl", "Pest Control",
                IconLoader.load("minigames/PestControl.png"),
                "Allows access to the Pest Control minigame",
                List.of(new MemberRequirement(), new CombatRequirement(10, 126))));

        registry.register(new MinigameUnlock("TzHaarFightCave", "TzHaar Fight Cave",
                IconLoader.load("minigames/TzHaarFightCave.png"),
                "Allows access to the TzHaar Fight Cave minigame",
                List.of(new MemberRequirement(), new CombatRequirement(10, 126))));

        registry.register(new MinigameUnlock("TzHaarFightPit", "TzHaar Fight Pit",
                IconLoader.load("minigames/TzHaarFightPit.png"),
                "Allows access to the TzHaar Fight Pit minigame",
                List.of(new MemberRequirement(), new CombatRequirement(5, 126))));

        registry.register(new MinigameUnlock("LastManStanding", "Last Man Standing",
                IconLoader.load("minigames/LastManStanding.png"),
                "Allows access to the Last Man Standing minigame",
                List.of(new CombatRequirement(5, 126))));

        registry.register(new MinigameUnlock("Inferno", "Inferno",
                IconLoader.load("minigames/Inferno.png"),
                "Allows access to the Inferno minigame",
                List.of(new MemberRequirement(), new CombatRequirement(75, 126))));

        //TODO: Add more minigames
        /*
        soul wars
        Skilling minigames from wiki
        https://oldschool.runescape.wiki/w/Minigames
        
         */
    }


    private static void registerShops(UnlockRegistry registry, CoinboundPlugin plugin) {
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
        registry.register(new BossUnlock("OBOR", "Obor", IconLoader.load("boss/OBOR.png"), "Hill giant boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("BRYOPHYTA", "Bryophyta", IconLoader.load("boss/BRYOPHYTA.png"), "Moss giant boss.", List.of(
                new CombatRequirement(40, 126)
        )));
        registry.register(new BossUnlock("GIANT_MOLE", "Giant Mole", IconLoader.load("boss/GIANT_MOLE.png"), "Boss beneath Falador.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GROTESQUE_GUARDIANS", "Grotesque Guardians", IconLoader.load("boss/GROTESQUE_GUARDIANS.png"), "Slayer tower rooftop boss.", List.of(
                new CombatRequirement(85, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("TEMPOROSS", "Tempoross", IconLoader.load("boss/TEMPOROSS.png"), "Fishing skilling boss.", List.of(new MemberRequirement())));
        registry.register(new BossUnlock("WINTERTODT", "Wintertodt", IconLoader.load("boss/WINTERTODT.png"), "Firemaking skilling boss.", List.of(new MemberRequirement())));
        registry.register(new BossUnlock("BARROWS", "Barrows Brothers", IconLoader.load("boss/BARROWS.png"), "Barrows chest encounter.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SARACHNIS", "Sarachnis", IconLoader.load("boss/SARACHNIS.png"), "Spider boss in Forthos Dungeon.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KALPHITE_QUEEN", "Kalphite Queen", IconLoader.load("boss/KALPHITE_QUEEN.png"), "Desert boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("HESPORI", "Hespori", IconLoader.load("boss/HESPORI.png"), "Farming guild boss.", List.of(
                new CombatRequirement(20, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SKOTIZO", "Skotizo", IconLoader.load("boss/SKOTIZO.png"), "Demonic boss in Catacombs.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZALCANO", "Zalcano", IconLoader.load("boss/ZALCANO.png"), "Mining boss in Prifddinas.", List.of(new MemberRequirement()
        )));
        registry.register(new BossUnlock("KING_BLACK_DRAGON", "King Black Dragon", IconLoader.load("boss/KING_BLACK_DRAGON.png"), "Wilderness dragon boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KRAKEN", "Kraken", IconLoader.load("boss/KRAKEN.png"), "Slayer boss.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ABYSSAL_SIRE", "Abyssal Sire", IconLoader.load("boss/ABYSSAL_SIRE.png"), "Abyssal demon boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("THERMY", "Thermonuclear Smoke Devil", IconLoader.load("boss/THERMY.png"), "Smoke devil boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GAUNTLET", "The Gauntlet", IconLoader.load("boss/GAUNTLET.png"), "Prifddinas challenge.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZULRAH", "Zulrah", IconLoader.load("boss/ZULRAH.png"), "Poisonous serpent boss.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CERBERUS", "Cerberus", IconLoader.load("boss/CERBERUS.png"), "Hellhound boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("VORKATH", "Vorkath", IconLoader.load("boss/VORKATH.png"), "Undead dragon boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GENERAL_GRAARDOR", "General Graardor", IconLoader.load("boss/GENERAL_GRAARDOR.png"), "Bandos GWD boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KRIL", "K'ril Tsutsaroth", IconLoader.load("boss/KRIL.png"), "Zamorak GWD boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZILYANA", "Commander Zilyana", IconLoader.load("boss/ZILYANA.png"), "Saradomin GWD boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KREEARRA", "Kree'arra", IconLoader.load("boss/KREEARRA.png"), "Armadyl GWD boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("NIGHTMARE", "The Nightmare", IconLoader.load("boss/NIGHTMARE.png"), "Slepe nightmare boss.", List.of(
                new CombatRequirement(70, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CORPOREAL_BEAST", "Corporeal Beast", IconLoader.load("boss/CORPOREAL_BEAST.png"), "High-level group boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ALCHEMICAL_HYDRA", "Alchemical Hydra", IconLoader.load("boss/ALCHEMICAL_HYDRA.png"), "Hydra slayer boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("NEX", "Nex", IconLoader.load("boss/NEX.png"), "Ancient prison boss.", List.of(
                new CombatRequirement(70, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_SUPREME", "Dagannoth Supreme", IconLoader.load("boss/DK_SUPREME.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_REX", "Dagannoth Rex", IconLoader.load("boss/DK_REX.png"), "Dagannoth King.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_PRIME", "Dagannoth Prime", IconLoader.load("boss/DK_PRIME.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CORRUPTED_GAUNTLET", "Corrupted Gauntlet", IconLoader.load("boss/CORRUPTED_GAUNTLET.png"), "Hard mode Gauntlet.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("PHANTOM_MUSPAH", "Phantom Muspah", IconLoader.load("boss/PHANTOM_MUSPAH.png"), "Ancient ice boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CHAOS_ELEMENTAL", "Chaos Elemental", IconLoader.load("boss/CHAOS_ELEMENTAL.png"), "Wilderness boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ARTIO", "Artio", IconLoader.load("boss/ARTIO.png"), "Callisto variant.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CALVARION", "Calvar'ion", IconLoader.load("boss/CALVARION.png"), "Vet'ion variant.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SPINDEL", "Spindel", IconLoader.load("boss/SPINDEL.png"), "Venenatis variant.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CHAOS_FANATIC", "Chaos Fanatic", IconLoader.load("boss/CHAOS_FANATIC.png"), "Wilderness mage boss.", List.of(
                new CombatRequirement(30, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CRAZY_ARCHAEOLOGIST", "Crazy Archaeologist", IconLoader.load("boss/CRAZY_ARCHAEOLOGIST.png"), "Wilderness boss.", List.of(
                new CombatRequirement(30, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DERANGED_ARCHAEOLOGIST", "Deranged Archaeologist", IconLoader.load("boss/DERANGED_ARCHAEOLOGIST.png"), "Fossil Island boss.", List.of(
                new CombatRequirement(20, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SCORPIA", "Scorpia", IconLoader.load("boss/SCORPIA.png"), "Wilderness scorpion.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("WHISPERER", "The Whisperer", IconLoader.load("boss/WHISPERER.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DUKE", "Duke Sucellus", IconLoader.load("boss/DUKE.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("LEVIATHAN", "The Leviathan", IconLoader.load("boss/LEVIATHAN.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("VARDORVIS", "Vardorvis", IconLoader.load("boss/VARDORVIS.png"), "DT2 boss.", List.of(
                new CombatRequirement(80, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SCURRIUS", "Scurrius", IconLoader.load("boss/SCURRIUS.png"), "Varrock sewer rat boss.", List.of(
                new CombatRequirement(10, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ARAXXOR", "Araxxor", IconLoader.load("boss/ARAXXOR.png"), "Spider boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("AMOXLIATL", "Amoxliatl", IconLoader.load("boss/AMOXLIATL.png"), "Varlamore boss.", List.of(
                new CombatRequirement(40, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("HUEYCOATL", "The Hueycoatl", IconLoader.load("boss/HUEYCOATL.png"), "Varlamore boss.", List.of(
                new CombatRequirement(20, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GEMSTONE_CRAB", "Gemstone crab", IconLoader.load("boss/GEMSTONE_CRAB.png"), "Varlamore boss.", List.of(
                new CombatRequirement(1, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DOOM", "Doom of Mokhaiotl", IconLoader.load("boss/DOOM.png"), "Varlamore boss.", List.of(
                new CombatRequirement(60, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("MOONS", "Moons of peril", IconLoader.load("boss/MOONS.png"), "Varlamore boss.", List.of(
                new CombatRequirement(30, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ROYAL_TITANS", "Royal titans", IconLoader.load("boss/ROYAL_TITANS.png"), "Two for the price of one.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("REVENANT_MALEDICTUS", "Revenant maledictus", IconLoader.load("boss/REVENANT_MALEDICTUS.png"), "Wilderness boss.", List.of(
                new CombatRequirement(50, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("YAMA", "Yama", IconLoader.load("boss/YAMA.png"), "666", List.of(
                new CombatRequirement(70, 126), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SHELLBANE_GRYPHON", "Shellbane gryphon", IconLoader.load("boss/SHELLBANE_GRYPHON.png"), "666", List.of(
                new CombatRequirement(30, 126), new MemberRequirement()
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

