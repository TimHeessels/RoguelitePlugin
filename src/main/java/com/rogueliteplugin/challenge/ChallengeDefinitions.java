package com.rogueliteplugin.challenge;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.requirements.CombatLevelRequirement;
import com.rogueliteplugin.requirements.RequiresUnlockRequirement;
import com.rogueliteplugin.requirements.SkillLevelRequirement;
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
        registerSkill(registry, unlockRegistry);
        registerDrops(registry);
        registerStarterChallenges(registry);
    }

    private static void registerStarterChallenges(ChallengeRegistry registry) {
        registry.register(
                new WalkChallenge(
                        "MoveTiles",
                        "Walk or run $ tile(s)",
                        100,
                        5000,
                        "Get your good boots on."
                ));
        registry.register(
                new OneHpAfterDamageChallenge(
                        "HealthChallenge",
                        "Survive a hit with 1hp remaining",
                        "Ha-ha-ha staying alive."
                ));
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
                                new CombatLevelRequirement(10)
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
                                new CombatLevelRequirement(10)
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
                                new CombatLevelRequirement(10)
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
                                new CombatLevelRequirement(5)
                        ))
        );
    }

    private static void registerSkill(ChallengeRegistry registry, UnlockRegistry unlockRegistry) {
        registry.register(new SkillXPChallenge(
                "GainAttackXP",
                "Gain $ XP in Attack",
                1, 200_000,
                "Train your accuracy.",
                Skill.ATTACK,
                List.of(new RequiresUnlockRequirement("SKILL_ATTACK", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainStrengthXP",
                "Gain $ XP in Strength",
                1, 200_000,
                "Power through your enemies.",
                Skill.STRENGTH,
                List.of(new RequiresUnlockRequirement("SKILL_STRENGTH", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainDefenceXP",
                "Gain $ XP in Defence",
                1, 200_000,
                "Harden your resolve.",
                Skill.DEFENCE,
                List.of(new RequiresUnlockRequirement("SKILL_DEFENCE", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainRangedXP",
                "Gain $ XP in Ranged",
                1, 200_000,
                "Strike from afar.",
                Skill.RANGED,
                List.of(new RequiresUnlockRequirement("SKILL_RANGED", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainPrayerXP",
                "Gain $ XP in Prayer",
                1, 200_000,
                "Seek divine favor.",
                Skill.PRAYER,
                List.of(new RequiresUnlockRequirement("SKILL_PRAYER", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainMagicXP",
                "Gain $ XP in Magic",
                1, 200_000,
                "Master the arcane.",
                Skill.MAGIC,
                List.of(new RequiresUnlockRequirement("SKILL_MAGIC", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainCookingXP",
                "Gain $ XP in Cooking",
                1, 200_000,
                "Cook something delicious.",
                Skill.COOKING,
                List.of(new RequiresUnlockRequirement("SKILL_COOKING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainWoodcuttingXP",
                "Gain $ XP in Woodcutting",
                1, 200_000,
                "Chop until it falls.",
                Skill.WOODCUTTING,
                List.of(new RequiresUnlockRequirement("SKILL_WOODCUTTING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainFletchingXP",
                "Gain $ XP in Fletching",
                1, 200_000,
                "Craft something sharp.",
                Skill.FLETCHING,
                List.of(new RequiresUnlockRequirement("SKILL_FLETCHING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainFishingXP",
                "Gain $ XP in Fishing",
                1, 200_000,
                "Cast out your net and relax.",
                Skill.FISHING,
                List.of(new RequiresUnlockRequirement("SKILL_FISHING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainFiremakingXP",
                "Gain $ XP in Firemaking",
                1, 200_000,
                "Let it burn.",
                Skill.FIREMAKING,
                List.of(new RequiresUnlockRequirement("SKILL_FIREMAKING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainCraftingXP",
                "Gain $ XP in Crafting",
                1, 200_000,
                "Create something useful.",
                Skill.CRAFTING,
                List.of(new RequiresUnlockRequirement("SKILL_CRAFTING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainSmithingXP",
                "Gain $ XP in Smithing",
                1, 200_000,
                "Hammer it into shape.",
                Skill.SMITHING,
                List.of(new RequiresUnlockRequirement("SKILL_SMITHING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainMiningXP",
                "Gain $ XP in Mining",
                1, 200_000,
                "Dig deep.",
                Skill.MINING,
                List.of(new RequiresUnlockRequirement("SKILL_MINING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainHerbloreXP",
                "Gain $ XP in Herblore",
                1, 200_000,
                "Mix something potent.",
                Skill.HERBLORE,
                List.of(new RequiresUnlockRequirement("SKILL_HERBLORE", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainAgilityXP",
                "Gain $ XP in Agility",
                1, 200_000,
                "Keep moving.",
                Skill.AGILITY,
                List.of(new RequiresUnlockRequirement("SKILL_AGILITY", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainThievingXP",
                "Gain $ XP in Thieving",
                1, 200_000,
                "Steal without being seen.",
                Skill.THIEVING,
                List.of(new RequiresUnlockRequirement("SKILL_THIEVING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainSlayerXP",
                "Gain $ XP in Slayer",
                1, 200_000,
                "Earn it the hard way.",
                Skill.SLAYER,
                List.of(new RequiresUnlockRequirement("SKILL_SLAYER", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainFarmingXP",
                "Gain $ XP in Farming",
                1, 200_000,
                "Let it grow.",
                Skill.FARMING,
                List.of(new RequiresUnlockRequirement("SKILL_FARMING", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainRunecraftXP",
                "Gain $ XP in Runecraft",
                1, 200_000,
                "Channel raw essence.",
                Skill.RUNECRAFT,
                List.of(new RequiresUnlockRequirement("SKILL_RUNECRAFT", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainHunterXP",
                "Gain $ XP in Hunter",
                1, 200_000,
                "Set the perfect trap.",
                Skill.HUNTER,
                List.of(new RequiresUnlockRequirement("SKILL_HUNTER", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainConstructionXP",
                "Gain $ XP in Construction",
                1, 200_000,
                "Build something solid.",
                Skill.CONSTRUCTION,
                List.of(new RequiresUnlockRequirement("SKILL_CONSTRUCTION", unlockRegistry))
        ));

        registry.register(new SkillXPChallenge(
                "GainSailingXP",
                "Gain $ XP in Sailing",
                1, 200_000,
                "Ride the open seas.",
                Skill.SAILING,
                List.of(new RequiresUnlockRequirement("SKILL_SAILING", unlockRegistry))
        ));
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
                                new CombatLevelRequirement(3)
                        ))
        );
        registry.register(
                new DropSpecificChallenge(
                        "GetBonesDrop",
                        526,
                        "Get a bones drop from any NPC.",
                        "Murderer!",
                        List.of(
                                new CombatLevelRequirement(3)
                        ))
        );
    }
}
