package com.rogueliteplugin.challenge;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.data.ChallengeType;
import com.rogueliteplugin.requirements.CombatRequirement;
import com.rogueliteplugin.requirements.SkillLevelRequirement;
import com.rogueliteplugin.requirements.UnlockIDRequirement;
import com.rogueliteplugin.unlocks.UnlockRegistry;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

import java.util.List;

public final class ChallengeDefinitions {
    private ChallengeDefinitions() {
    }

    public static void registerAll(
            ChallengeRegistry registry,
            UnlockRegistry unlockRegistry,
            SkillIconManager skillIconManager,
            RoguelitePlugin plugin
    ) {
        registerCombat(registry);
        registerAttackSkill(registry, unlockRegistry);
        registerStrengthSkill(registry, unlockRegistry);
        registerDefenceSkill(registry, unlockRegistry);
        registerRangedSkill(registry, unlockRegistry);
        registerPrayerSkill(registry, unlockRegistry);
        registerMagicSkill(registry, unlockRegistry);
        registerRunecraftSkill(registry, unlockRegistry);
        registerHitpointsSkill(registry, unlockRegistry);
        registerCraftingSkill(registry, unlockRegistry);
        registerMiningSkill(registry, unlockRegistry);
        registerSmithingSkill(registry, unlockRegistry);
        registerFishingSkill(registry, unlockRegistry);
        registerCookingSkill(registry, unlockRegistry);
        registerFiremakingSkill(registry, unlockRegistry);
        registerWoodcuttingSkill(registry, unlockRegistry);
        registerAgilitySkill(registry, unlockRegistry);
        registerHerbloreSkill(registry, unlockRegistry);
        registerThievingSkill(registry, unlockRegistry);
        registerFletchingSkill(registry, unlockRegistry);
        registerSlayerSkill(registry, unlockRegistry);
        registerFarmingSkill(registry, unlockRegistry);
        registerConstructionSkill(registry, unlockRegistry);
        registerHunterSkill(registry, unlockRegistry);
        registerSailingSkill(registry, unlockRegistry);
        registerDrops(registry);
        registerStarterChallenges(registry);
    }

    private static void registerStarterChallenges(ChallengeRegistry registry) {
        registry.register(new WalkChallenge(
                "MoveTiles",
                "Walk or run $ tile(s)",
                100,
                3000,
                "Get your good boots on."
        ));
        registry.register(new OneHpAfterDamageChallenge(
                "HealthChallenge",
                "Survive a hit with 1hp remaining",
                "Ha-ha-ha staying alive."
        ));
        registry.register(new CheckForSpamchatChallenge(
                "GainCollectionSlot",
                "Gain any new collection slot item",
                1, 1,
                "New is always better.",
                "New item added to your collection log",
                ChallengeType.Unique,
                List.of()
        ));

        //Complete a random event
    }

    private static void registerCombat(ChallengeRegistry registry) {
        registry.register(
                new CombatChallenge(
                        "KillGoblins",
                        "Kill $ goblin(s)",
                        1,
                        20,
                        CombatChallenge.EnemyGroup.GOBLINS,
                        "Kill a few pests.",
                        List.of(
                                new CombatRequirement(10, 70)
                        ))
        );
        registry.register(
                new CombatChallenge(
                        "KillCows",
                        "Kill $ cow(s)",
                        1,
                        20,
                        CombatChallenge.EnemyGroup.COWS,
                        "That's not how you get milk.",
                        List.of(
                                new CombatRequirement(10, 70)
                        ))
        );
        registry.register(
                new CombatChallenge(
                        "KillSpiders",
                        "Kill $ spider(s)",
                        1,
                        20,
                        CombatChallenge.EnemyGroup.SPIDERS,
                        "No more creepy crawlies.",
                        List.of(
                                new CombatRequirement(10, 70)
                        ))
        );
        registry.register(
                new CombatChallenge(
                        "KillChickens",
                        "Kill $ chicken(s)",
                        1,
                        20,
                        CombatChallenge.EnemyGroup.CHICKENS,
                        "Eggcelent.",
                        List.of(
                                new CombatRequirement(5, 30)
                        ))
        );
    }

    private static void registerAttackSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainAttackXP",
                "Gain $ XP in Attack",
                1, 200_000,
                "Train your accuracy.",
                Skill.ATTACK,
                List.of(new UnlockIDRequirement("SKILL_ATTACK", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerStrengthSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainStrengthXP",
                "Gain $ XP in Strength",
                1, 200_000,
                "Power through your enemies.",
                Skill.STRENGTH,
                List.of(new UnlockIDRequirement("SKILL_STRENGTH", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerDefenceSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainDefenceXP",
                "Gain $ XP in Defence",
                1, 200_000,
                "Harden your resolve.",
                Skill.DEFENCE,
                List.of(new UnlockIDRequirement("SKILL_DEFENCE", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerRangedSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainRangedXP",
                "Gain $ XP in Ranged",
                1, 200_000,
                "Strike from afar.",
                Skill.RANGED,
                List.of(new UnlockIDRequirement("SKILL_RANGED", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerPrayerSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainPrayerXP",
                "Gain $ XP in Prayer",
                1, 200_000,
                "Seek divine favor.",
                Skill.PRAYER,
                List.of(new UnlockIDRequirement("SKILL_PRAYER", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerMagicSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainMagicXP",
                "Gain $ XP in Magic",
                1, 200_000,
                "Master the arcane.",
                Skill.MAGIC,
                List.of(new UnlockIDRequirement("SKILL_MAGIC", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerRunecraftSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainRunecraftXP",
                "Gain $ XP in Runecraft",
                1, 200_000,
                "Channel raw essence.",
                Skill.RUNECRAFT,
                List.of(new UnlockIDRequirement("SKILL_RUNECRAFT", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerHitpointsSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        //TODO: Fill in.
    }

    private static void registerCraftingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainCraftingXP",
                "Gain $ XP in Crafting",
                1, 200_000,
                "Create something useful.",
                Skill.CRAFTING,
                List.of(new UnlockIDRequirement("SKILL_CRAFTING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerMiningSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainMiningXP",
                "Gain $ XP in Mining",
                1, 200_000,
                "Dig deep.",
                Skill.MINING,
                List.of(new UnlockIDRequirement("SKILL_MINING", unlockRegistry))
        ));

        //TODO: Fill in.

        //Get a gem drop while mining
    }

    private static void registerSmithingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainSmithingXP",
                "Gain $ XP in Smithing",
                1, 200_000,
                "Hammer it into shape.",
                Skill.SMITHING,
                List.of(new UnlockIDRequirement("SKILL_SMITHING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerFishingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainFishingXP",
                "Gain $ XP in Fishing",
                1, 200_000,
                "Cast out your net and relax.",
                Skill.FISHING,
                List.of(new UnlockIDRequirement("SKILL_FISHING", unlockRegistry))
        ));

        //TODO: Fill in.

        //Fish up a medium? clue scroll
    }

    private static void registerCookingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainCookingXP",
                "Gain $ XP in Cooking",
                1, 200_000,
                "Try not to burn it.",
                Skill.COOKING,
                List.of(new UnlockIDRequirement("SKILL_COOKING", unlockRegistry))
        ));
        String[][] cookingData = {
                {"Herring", "herring", "5"},
                {"Mackerel", "mackerel", "10"},
                {"Trout", "trout", "15"},
                {"Pike", "pike", "20"},
                {"Salmon", "salmon", "25"},
                {"Lobster", "lobster", "40"},
                {"Swordfish", "swordfish", "45"},
                {"Monkfish", "monkfish", "62"},
                {"Shark", "shark", "80"},
                {"Anglerfish", "anglerfish", "84"},
                {"MantaRay", "manta ray", "91"}
        };

        for (String[] current : cookingData) {
            registry.register(new CheckForSpamchatChallenge(
                    "Cook" + current[0],
                    "Cook $ " + current[1],
                    1, 200,
                    "Try not to burn it.",
                    "You successfully (?:cook|bake) (?:a|an|some) " + current[1],
                    ChallengeType.Cooking,
                    List.of(new SkillLevelRequirement(Skill.COOKING, Integer.parseInt(current[2])))
            ));
        }
    }

    private static void registerFiremakingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainFiremakingXP",
                "Gain $ XP in Firemaking",
                1, 200_000,
                "Let it burn.",
                Skill.FIREMAKING,
                List.of(new UnlockIDRequirement("SKILL_FIREMAKING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerWoodcuttingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainWoodcuttingXP",
                "Gain $ XP in Woodcutting",
                1, 200_000,
                "Chop until it falls.",
                Skill.WOODCUTTING,
                List.of(new UnlockIDRequirement("SKILL_WOODCUTTING", unlockRegistry))
        ));

        //TODO: Fill in.

        //Get a birdness while woodcutting
    }

    private static void registerAgilitySkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainAgilityXP",
                "Gain $ XP in Agility",
                1, 200_000,
                "Keep moving.",
                Skill.AGILITY,
                List.of(new UnlockIDRequirement("SKILL_AGILITY", unlockRegistry))
        ));

        //TODO: Fill in.

        //Complete three laps without failing
    }

    private static void registerHerbloreSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainHerbloreXP",
                "Gain $ XP in Herblore",
                1, 200_000,
                "Mix something potent.",
                Skill.HERBLORE,
                List.of(new UnlockIDRequirement("SKILL_HERBLORE", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerThievingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainThievingXP",
                "Gain $ XP in Thieving",
                1, 200_000,
                "Steal without being seen.",
                Skill.THIEVING,
                List.of(new UnlockIDRequirement("SKILL_THIEVING", unlockRegistry))
        ));

        //TODO: Fill in.

        //Pickpocket 15 times in a row without being stunned
    }

    private static void registerFletchingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainFletchingXP",
                "Gain $ XP in Fletching",
                1, 200_000,
                "Craft something sharp.",
                Skill.FLETCHING,
                List.of(new UnlockIDRequirement("SKILL_FLETCHING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerSlayerSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainSlayerXP",
                "Gain $ XP in Slayer",
                1, 200_000,
                "Earn it the hard way.",
                Skill.SLAYER,
                List.of(new UnlockIDRequirement("SKILL_SLAYER", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerFarmingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainFarmingXP",
                "Gain $ XP in Farming",
                1, 200_000,
                "Let it grow.",
                Skill.FARMING,
                List.of(new UnlockIDRequirement("SKILL_FARMING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerConstructionSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainConstructionXP",
                "Gain $ XP in Construction",
                1, 200_000,
                "Build something solid.",
                Skill.CONSTRUCTION,
                List.of(new UnlockIDRequirement("SKILL_CONSTRUCTION", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerHunterSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainHunterXP",
                "Gain $ XP in Hunter",
                1, 200_000,
                "Set the perfect trap.",
                Skill.HUNTER,
                List.of(new UnlockIDRequirement("SKILL_HUNTER", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerSailingSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainSailingXP",
                "Gain $ XP in Sailing",
                1, 200_000,
                "Ride the open seas.",
                Skill.SAILING,
                List.of(new UnlockIDRequirement("SKILL_SAILING", unlockRegistry))
        ));

        //TODO: Fill in.
    }

    private static void registerDrops(ChallengeRegistry registry) {
        registry.register(
                new DropValueChallenge(
                        "GetDrop",
                        "Get a drop from an NPC, worth at least $ GP.",
                        1,
                        10000000,
                        "Get rich!",
                        List.of(
                                new CombatRequirement(3, 126)
                        ))
        );
        registry.register(
                new DropSpecificChallenge(
                        "GetBonesDrop",
                        526,
                        "Get a bones drop from any NPC.",
                        "Murderer!",
                        List.of(
                                new CombatRequirement(3, 126)
                        ))
        );
    }
}
