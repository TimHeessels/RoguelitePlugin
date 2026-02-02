package com.coinlockedplugin.unlocks;

import com.coinlockedplugin.CoinlockedPlugin;
import com.coinlockedplugin.data.CoinboundQuestRequirement;
import com.coinlockedplugin.data.ClueTier;
import com.coinlockedplugin.data.ShopCategory;
import com.coinlockedplugin.requirements.*;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.coinlockedplugin.CoinlockedPlugin.LEVEL_RANGES;

public final class UnlockDefinitions {
    private UnlockDefinitions() {
    }

    public static void registerAll(
            UnlockRegistry registry,
            SkillIconManager skillIconManager,
            CoinlockedPlugin plugin
    ) {

        registerClueTiers(registry);
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
    }

    private static void registerOther(UnlockRegistry registry) {
        registry.register(
                new Other(
                        "Alchemy",
                        "Cast Low/High Alchemy",
                        IconLoader.load("other/Alchemy.png"),
                        "Allows casting the Low and High Alchemy spell.",
                        List.of(new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._30, registry))
                )
        );
        registry.register(
                new Other(
                        "Enchant",
                        "Cast Enchanting items",
                        IconLoader.load("other/Enchant.png"),
                        "Allows enchanting items.",
                        List.of(new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._10, registry))
                )
        );
        registry.register(
                new Other(
                        "SpecialAttack",
                        "Use special attacks",
                        IconLoader.load("other/SpecialAttack.png"),
                        "Allows using special attacks on weapons.",
                        List.of(new CombatRequirement(40))
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
                        List.of(new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._40, registry))
                )
        );
        registry.register(
                new ProtectionPrayer(
                        "RangeProtection",
                        "Range protection prayer",
                        IconLoader.load("protection/Range.png"),
                        "Allows using the range protection prayer.",
                        List.of(new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._40, registry))
                )
        );
        registry.register(
                new ProtectionPrayer(
                        "MeleeProtection",
                        "Melee protection prayer",
                        IconLoader.load("protection/Melee.png"),
                        "Allows using the melee protection prayer.",
                        List.of(new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, registry))
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
                        List.of(new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._30, registry))
                )
        );
        registry.register(
                new ResorationUnlock(
                        "Potions",
                        "Drink potions",
                        IconLoader.load("consumables/potions.png"),
                        "Allows drinking potions.",
                        List.of(new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._30, registry))
                )
        );
        registry.register(
                new ResorationUnlock(
                        "PrayerAltars",
                        "Restore at altars",
                        IconLoader.load("consumables/altar.png"),
                        "Allows restoring player points at altars.",
                        List.of(new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, registry))
                )
        );
        registry.register(
                new ResorationUnlock(
                        "RefreshmentPools",
                        "Drink from refreshment pools",
                        IconLoader.load("consumables/pool.png"),
                        "Allows restoring stats at pools.",
                        List.of(new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, registry),
                                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._30, registry),
                                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._30, registry))
                )
        );
    }

    private static void registerTransport(UnlockRegistry registry) {
        registry.register(new TransportUnlock(
                "FairyRings",
                "Fairy Rings",
                IconLoader.load("transport/FairyRings.png"),
                "Allows access to use Fairy Rings to move around.",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.FAIRYTALE_II__CURE_A_QUEEN, registry))
        ));
        registry.register(new TransportUnlock(
                "SpiritTrees",
                "Spirit Trees",
                IconLoader.load("transport/SpiritTrees.png"),
                "Allows access to use Spirit Trees to move around.",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.TREE_GNOME_VILLAGE, registry))
        ));
        registry.register(new TransportUnlock(
                "SpelbookTeleports",
                "Teleport using spells",
                IconLoader.load("transport/SpelbookTeleports.png"),
                "Allows access to use Teleport using spells to move around.",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._30, registry))
        ));
        registry.register(new TransportUnlock(
                "MinigameTeleports",
                "Minigame Teleports",
                IconLoader.load("transport/MinigameTeleports.png"),
                "Allows access to use Minigame Teleports to move around.",
                List.of(new MemberRequirement())
        ));
        registry.register(new TransportUnlock(
                "CharterShips",
                "Charter Ships",
                IconLoader.load("transport/CharterShips.png"),
                "Allows access to use Charter Ships to move around.",
                List.of(new MemberRequirement())
        ));
        registry.register(new TransportUnlock(
                "AgilityShortcuts",
                "Agility Shortcuts",
                IconLoader.load("transport/AgilityShortcuts.png"),
                "Allows access to use Agility Shortcuts to move around.",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._10, registry))
        ));
        registry.register(new TransportUnlock(
                "BalloonTransport",
                "Balloon Transport",
                IconLoader.load("transport/BalloonTransport.png"),
                "Allows access to use Balloon Transport to move around.",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.ENLIGHTENED_JOURNEY, registry))
        ));
        registry.register(new TransportUnlock(
                "GnomeGliders",
                "Gnome Gliders",
                IconLoader.load("transport/GnomeGliders.png"),
                "Allows access to use Gnome Gliders to move around.",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.THE_GRAND_TREE, registry))
        ));
        registry.register(new TransportUnlock(
                "TeleportJewelry",
                "Teleport Jewelry",
                IconLoader.load("transport/TeleportJewelry.png"),
                "Allows access to use Teleport Jewelry to move around.",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Enchant", registry))
        ));
        registry.register(new TransportUnlock(
                "Canoes",
                "Canoes",
                IconLoader.load("transport/Canoes.png"),
                "Allows access to use Canoes to move around.",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._20, registry))
        ));
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

    private static void registerSkills(UnlockRegistry registry, SkillIconManager skillIconManager) {

        for (Skill skill : Skill.values()) {
            int previousLevel = 1;
            for (Integer levelRange : LEVEL_RANGES) {
                BufferedImage img = skillIconManager.getSkillImage(skill);

                List<AppearRequirement> reqs = new ArrayList<>();
                if (!f2pSkills.contains(skill))
                    reqs.add(new MemberRequirement());
                if (previousLevel != 1)
                    reqs.add(new UnlockIDRequirement("SKILL_" + skill.name() + "_" + previousLevel, registry));

                if (img != null) {
                    registry.register(new SkillRangeUnlock(skill, previousLevel, levelRange, skillIconManager, reqs));
                }
                previousLevel = levelRange;
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

            if (tier == ClueTier.MEDIUM) {
                reqs.add(new CombatRequirement(10));
            } else if (tier == ClueTier.HARD) {
                reqs.add(new CombatRequirement(30));
            } else if (tier == ClueTier.ELITE) {
                reqs.add(new CombatRequirement(50));
            } else if (tier == ClueTier.MASTER) {
                reqs.add(new CombatRequirement(70));
            }

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

    private static void registerMinigames(UnlockRegistry registry, CoinlockedPlugin plugin) {
        registry.register(new MinigameUnlock("Barbarian_Assault", "Barbarian Assault",
                IconLoader.load("minigames/Barbarian_Assault.png"),
                "Allows access to the Barbarian Assault minigame",
                List.of(new MemberRequirement(), new CombatRequirement(40))));

        registry.register(new MinigameUnlock("Bounty_Hunter", "Bounty Hunter",
                IconLoader.load("minigames/Bounty_Hunter.png"),
                "Allows access to the Bounty Hunter minigame",
                List.of(new MemberRequirement(), new CombatRequirement(20))));

        registry.register(new MinigameUnlock("Temple_Trekking", "Temple Trekking",
                IconLoader.load("minigames/Temple_Trekking.png"),
                "Allows access to the Temple Trekking minigame",
                List.of(new MemberRequirement())));

        registry.register(new MinigameUnlock("ChampionsChallenge", "Champions' Challenge",
                IconLoader.load("minigames/ChampionsChallenge.png"),
                "Allows access to the Champions' Challenge minigame",
                List.of(new MemberRequirement(), new CombatRequirement(50))));

        registry.register(new MinigameUnlock("CastleWars", "Castle Wars",
                IconLoader.load("minigames/CastleWars.png"),
                "Allows access to the Castle Wars minigame",
                List.of(new CombatRequirement(5))));

        registry.register(new MinigameUnlock("ClanWars", "Clan Wars",
                IconLoader.load("minigames/ClanWars.png"),
                "Allows access to the Clan Wars minigame",
                List.of(new CombatRequirement(5))));

        registry.register(new MinigameUnlock("DuelArena", "Duel Arena",
                IconLoader.load("minigames/DuelArena.png"),
                "Allows access to the Duel Arena minigame",
                List.of(new MemberRequirement(), new CombatRequirement(15))));

        registry.register(new MinigameUnlock("MageArena", "Mage Arena",
                IconLoader.load("minigames/MageArena.png"),
                "Allows access to the Mage Arena minigame",
                List.of(new MemberRequirement(), new CombatRequirement(40))));

        registry.register(new MinigameUnlock("NightmareZone", "Nightmare Zone",
                IconLoader.load("minigames/NightmareZone.png"),
                "Allows access to the Nightmare Zone minigame",
                List.of(new MemberRequirement(), new CombatRequirement(20))));

        registry.register(new MinigameUnlock("PestControl", "Pest Control",
                IconLoader.load("minigames/PestControl.png"),
                "Allows access to the Pest Control minigame",
                List.of(new MemberRequirement(), new CombatRequirement(10))));

        registry.register(new MinigameUnlock("TzHaarFightCave", "TzHaar Fight Cave",
                IconLoader.load("minigames/TzHaarFightCave.png"),
                "Allows access to the TzHaar Fight Cave minigame",
                List.of(new MemberRequirement(), new CombatRequirement(10))));

        registry.register(new MinigameUnlock("TzHaarFightPit", "TzHaar Fight Pit",
                IconLoader.load("minigames/TzHaarFightPit.png"),
                "Allows access to the TzHaar Fight Pit minigame",
                List.of(new MemberRequirement(), new CombatRequirement(5))));

        registry.register(new MinigameUnlock("LastManStanding", "Last Man Standing",
                IconLoader.load("minigames/LastManStanding.png"),
                "Allows access to the Last Man Standing minigame",
                List.of(new CombatRequirement(5))));

        registry.register(new MinigameUnlock("Inferno", "Inferno",
                IconLoader.load("minigames/Inferno.png"),
                "Allows access to the Inferno minigame",
                List.of(new MemberRequirement(), new CombatRequirement(75))));

        registry.register(new MinigameUnlock("Archerycompetition", "Archery competition",
                IconLoader.load("minigames/Archerycompetition.png"),
                "Allows access to the Archery competition minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("Blastfurnace", "Blast furnace",
                IconLoader.load("minigames/Blastfurnace.png"),
                "Allows access to the Blast furnace competition minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("BrimhavenAgilityArena", "Brimhaven Agility Arena",
                IconLoader.load("minigames/BrimhavenAgilityArena.png"),
                "Allows access to the Brimhaven Agility Arena minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("FishingTrawler", "Fishing Trawler",
                IconLoader.load("minigames/FishingTrawler.png"),
                "Allows access to the Fishing Trawler minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._20, registry))));

        registry.register(new MinigameUnlock("GiantsFoundry", "Giants' Foundry",
                IconLoader.load("minigames/GiantsFoundry.png"),
                "Allows access to the Giants' Foundry minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._30, registry))));

        registry.register(new MinigameUnlock("GnomeBall", "Gnome Ball",
                IconLoader.load("minigames/GnomeBall.png"),
                "Allows access to the Gnome Ball minigame",
                List.of(new MemberRequirement())));

        registry.register(new MinigameUnlock("GnomeRestaurant", "Gnome Restaurant",
                IconLoader.load("minigames/GnomeRestaurant.png"),
                "Allows access to the Gnome Restaurant minigame",
                List.of(new MemberRequirement())));

        registry.register(new MinigameUnlock("GuardiansOfTheRift", "Guardians of the Rift",
                IconLoader.load("minigames/GuardiansOfTheRift.png"),
                "Allows access to the Guardians of the Rift minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._10, registry),
                        new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("HallowedSepulchre", "Hallowed Sepulchre",
                IconLoader.load("minigames/HallowedSepulchre.png"),
                "Allows access to the Hallowed Sepulchre minigame",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quest" + Quest.DARKNESS_OF_HALLOWVALE, registry))));

        registry.register(new MinigameUnlock("ImpetuousImpulses", "Impetuous Impulses",
                IconLoader.load("minigames/ImpetuousImpulses.png"),
                "Allows access to the Impetuous Impulses minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("MageTrainingArena", "Mage Training Arena",
                IconLoader.load("minigames/MageTrainingArena.png"),
                "Allows access to the Mage Training Arena minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("MahoganyHomes", "Mahogany Homes",
                IconLoader.load("minigames/MahoganyHomes.png"),
                "Allows access to the Mahogany Homes minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("MasteringMixology", "Mastering Mixology",
                IconLoader.load("minigames/MasteringMixology.png"),
                "Allows access to the Mastering Mixology minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("Mess", "Mess",
                IconLoader.load("minigames/Mess.png"),
                "Allows access to the Mess minigame",
                List.of(new MemberRequirement())));

        registry.register(new MinigameUnlock("PyramidPlunder", "Pyramid Plunder",
                IconLoader.load("minigames/PyramidPlunder.png"),
                "Allows access to the Pyramid Plunder minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("RoguesDen", "Rogues' Den",
                IconLoader.load("minigames/RoguesDen.png"),
                "Allows access to the Rogues' Den minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("SorceressGarden", "Sorceress's Garden",
                IconLoader.load("minigames/SorceressGarden.png"),
                "Allows access to the Sorceress's Garden minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("StealingArtefacts", "Stealing artefacts",
                IconLoader.load("minigames/StealingArtefacts.png"),
                "Allows access to the Stealing artefacts minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("TitheFarm", "Tithe Farm",
                IconLoader.load("minigames/TitheFarm.png"),
                "Allows access to the Tithe Farm minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("TroubleBrewing", "Trouble Brewing",
                IconLoader.load("minigames/TroubleBrewing.png"),
                "Allows access to the Trouble Brewing minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("ValeTotems", "Vale Totems",
                IconLoader.load("minigames/ValeTotems.png"),
                "Allows access to the Vale Totems minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("VolcanicMine", "Volcanic Mine",
                IconLoader.load("minigames/VolcanicMine.png"),
                "Allows access to the Volcanic Mine minigame",
                List.of(new MemberRequirement(), new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._10, registry))));

        registry.register(new MinigameUnlock("ShadesOfMortton", "Shades of Mort'ton",
                IconLoader.load("minigames/ShadesOfMortton.png"),
                "Allows access to the Shades of Mort'ton minigame",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.SHADES_OF_MORTTON, registry))));

        registry.register(new MinigameUnlock("TaiBwoWannaiCleanup", "Tai Bwo Wannai Cleanup",
                IconLoader.load("minigames/TaiBwoWannaiCleanup.png"),
                "Allows access to the Tai Bwo Wannai Cleanup minigame",
                List.of(new MemberRequirement(), new UnlockIDRequirement("Quests" + Quest.TAI_BWO_WANNAI_TRIO, registry))));

        registry.register(new MinigameUnlock("WarriorsGuild", "Warriors' Guild",
                IconLoader.load("minigames/WarriorsGuild.png"),
                "Allows access to the Warriors' Guild minigames",
                List.of(new MemberRequirement(), new CombatRequirement(40))));
    }


    private static void registerShops(UnlockRegistry registry, CoinlockedPlugin plugin) {
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
                new CombatRequirement(40)
        )));
        registry.register(new BossUnlock("BRYOPHYTA", "Bryophyta", IconLoader.load("boss/BRYOPHYTA.png"), "Moss giant boss.", List.of(
                new CombatRequirement(40)
        )));
        registry.register(new BossUnlock("GIANT_MOLE", "Giant Mole", IconLoader.load("boss/GIANT_MOLE.png"), "Boss beneath Falador.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GROTESQUE_GUARDIANS", "Grotesque Guardians", IconLoader.load("boss/GROTESQUE_GUARDIANS.png"), "Slayer tower rooftop boss.", List.of(
                new CombatRequirement(85), new MemberRequirement()
        )));
        registry.register(new BossUnlock("TEMPOROSS", "Tempoross", IconLoader.load("boss/TEMPOROSS.png"), "Fishing skilling boss.", List.of(new MemberRequirement())));
        registry.register(new BossUnlock("WINTERTODT", "Wintertodt", IconLoader.load("boss/WINTERTODT.png"), "Firemaking skilling boss.", List.of(new MemberRequirement())));
        registry.register(new BossUnlock("BARROWS", "Barrows Brothers", IconLoader.load("boss/BARROWS.png"), "Barrows chest encounter.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SARACHNIS", "Sarachnis", IconLoader.load("boss/SARACHNIS.png"), "Spider boss in Forthos Dungeon.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KALPHITE_QUEEN", "Kalphite Queen", IconLoader.load("boss/KALPHITE_QUEEN.png"), "Desert boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("HESPORI", "Hespori", IconLoader.load("boss/HESPORI.png"), "Farming guild boss.", List.of(
                new CombatRequirement(20), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SKOTIZO", "Skotizo", IconLoader.load("boss/SKOTIZO.png"), "Demonic boss in Catacombs.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZALCANO", "Zalcano", IconLoader.load("boss/ZALCANO.png"), "Mining boss in Prifddinas.", List.of(new MemberRequirement()
        )));
        registry.register(new BossUnlock("KING_BLACK_DRAGON", "King Black Dragon", IconLoader.load("boss/KING_BLACK_DRAGON.png"), "Wilderness dragon boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KRAKEN", "Kraken", IconLoader.load("boss/KRAKEN.png"), "Slayer boss.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ABYSSAL_SIRE", "Abyssal Sire", IconLoader.load("boss/ABYSSAL_SIRE.png"), "Abyssal demon boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("THERMY", "Thermonuclear Smoke Devil", IconLoader.load("boss/THERMY.png"), "Smoke devil boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GAUNTLET", "The Gauntlet", IconLoader.load("boss/GAUNTLET.png"), "Prifddinas challenge.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZULRAH", "Zulrah", IconLoader.load("boss/ZULRAH.png"), "Poisonous serpent boss.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CERBERUS", "Cerberus", IconLoader.load("boss/CERBERUS.png"), "Hellhound boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("VORKATH", "Vorkath", IconLoader.load("boss/VORKATH.png"), "Undead dragon boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GENERAL_GRAARDOR", "General Graardor", IconLoader.load("boss/GENERAL_GRAARDOR.png"), "Bandos GWD boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KRIL", "K'ril Tsutsaroth", IconLoader.load("boss/KRIL.png"), "Zamorak GWD boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ZILYANA", "Commander Zilyana", IconLoader.load("boss/ZILYANA.png"), "Saradomin GWD boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("KREEARRA", "Kree'arra", IconLoader.load("boss/KREEARRA.png"), "Armadyl GWD boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("NIGHTMARE", "The Nightmare", IconLoader.load("boss/NIGHTMARE.png"), "Slepe nightmare boss.", List.of(
                new CombatRequirement(70), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CORPOREAL_BEAST", "Corporeal Beast", IconLoader.load("boss/CORPOREAL_BEAST.png"), "High-level group boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ALCHEMICAL_HYDRA", "Alchemical Hydra", IconLoader.load("boss/ALCHEMICAL_HYDRA.png"), "Hydra slayer boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("NEX", "Nex", IconLoader.load("boss/NEX.png"), "Ancient prison boss.", List.of(
                new CombatRequirement(70), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_SUPREME", "Dagannoth Supreme", IconLoader.load("boss/DK_SUPREME.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_REX", "Dagannoth Rex", IconLoader.load("boss/DK_REX.png"), "Dagannoth King.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DK_PRIME", "Dagannoth Prime", IconLoader.load("boss/DK_PRIME.png"), "Dagannoth King.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CORRUPTED_GAUNTLET", "Corrupted Gauntlet", IconLoader.load("boss/CORRUPTED_GAUNTLET.png"), "Hard mode Gauntlet.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("PHANTOM_MUSPAH", "Phantom Muspah", IconLoader.load("boss/PHANTOM_MUSPAH.png"), "Ancient ice boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CHAOS_ELEMENTAL", "Chaos Elemental", IconLoader.load("boss/CHAOS_ELEMENTAL.png"), "Wilderness boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ARTIO", "Artio", IconLoader.load("boss/ARTIO.png"), "Callisto variant.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CALVARION", "Calvar'ion", IconLoader.load("boss/CALVARION.png"), "Vet'ion variant.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SPINDEL", "Spindel", IconLoader.load("boss/SPINDEL.png"), "Venenatis variant.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CHAOS_FANATIC", "Chaos Fanatic", IconLoader.load("boss/CHAOS_FANATIC.png"), "Wilderness mage boss.", List.of(
                new CombatRequirement(30), new MemberRequirement()
        )));
        registry.register(new BossUnlock("CRAZY_ARCHAEOLOGIST", "Crazy Archaeologist", IconLoader.load("boss/CRAZY_ARCHAEOLOGIST.png"), "Wilderness boss.", List.of(
                new CombatRequirement(30), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DERANGED_ARCHAEOLOGIST", "Deranged Archaeologist", IconLoader.load("boss/DERANGED_ARCHAEOLOGIST.png"), "Fossil Island boss.", List.of(
                new CombatRequirement(20), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SCORPIA", "Scorpia", IconLoader.load("boss/SCORPIA.png"), "Wilderness scorpion.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("WHISPERER", "The Whisperer", IconLoader.load("boss/WHISPERER.png"), "DT2 boss.", List.of(
                new CombatRequirement(80), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DUKE", "Duke Sucellus", IconLoader.load("boss/DUKE.png"), "DT2 boss.", List.of(
                new CombatRequirement(80), new MemberRequirement()
        )));
        registry.register(new BossUnlock("LEVIATHAN", "The Leviathan", IconLoader.load("boss/LEVIATHAN.png"), "DT2 boss.", List.of(
                new CombatRequirement(80), new MemberRequirement()
        )));
        registry.register(new BossUnlock("VARDORVIS", "Vardorvis", IconLoader.load("boss/VARDORVIS.png"), "DT2 boss.", List.of(
                new CombatRequirement(80), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SCURRIUS", "Scurrius", IconLoader.load("boss/SCURRIUS.png"), "Varrock sewer rat boss.", List.of(
                new CombatRequirement(10), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ARAXXOR", "Araxxor", IconLoader.load("boss/ARAXXOR.png"), "Spider boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("AMOXLIATL", "Amoxliatl", IconLoader.load("boss/AMOXLIATL.png"), "Varlamore boss.", List.of(
                new CombatRequirement(40), new MemberRequirement()
        )));
        registry.register(new BossUnlock("HUEYCOATL", "The Hueycoatl", IconLoader.load("boss/HUEYCOATL.png"), "Varlamore boss.", List.of(
                new CombatRequirement(20), new MemberRequirement()
        )));
        registry.register(new BossUnlock("GEMSTONE_CRAB", "Gemstone crab", IconLoader.load("boss/GEMSTONE_CRAB.png"), "Varlamore boss.", List.of(
                new CombatRequirement(1), new MemberRequirement()
        )));
        registry.register(new BossUnlock("DOOM", "Doom of Mokhaiotl", IconLoader.load("boss/DOOM.png"), "Varlamore boss.", List.of(
                new CombatRequirement(60), new MemberRequirement()
        )));
        registry.register(new BossUnlock("MOONS", "Moons of peril", IconLoader.load("boss/MOONS.png"), "Varlamore boss.", List.of(
                new CombatRequirement(30), new MemberRequirement()
        )));
        registry.register(new BossUnlock("ROYAL_TITANS", "Royal titans", IconLoader.load("boss/ROYAL_TITANS.png"), "Two for the price of one.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("REVENANT_MALEDICTUS", "Revenant maledictus", IconLoader.load("boss/REVENANT_MALEDICTUS.png"), "Wilderness boss.", List.of(
                new CombatRequirement(50), new MemberRequirement()
        )));
        registry.register(new BossUnlock("YAMA", "Yama", IconLoader.load("boss/YAMA.png"), "666", List.of(
                new CombatRequirement(70), new MemberRequirement()
        )));
        registry.register(new BossUnlock("SHELLBANE_GRYPHON", "Shellbane gryphon", IconLoader.load("boss/SHELLBANE_GRYPHON.png"), "666", List.of(
                new CombatRequirement(30), new MemberRequirement()
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

