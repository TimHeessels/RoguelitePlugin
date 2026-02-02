package com.coinlockedplugin.data;

import com.coinlockedplugin.requirements.AppearRequirement;
import com.coinlockedplugin.requirements.MaxSkillLevelUnlocked;
import com.coinlockedplugin.requirements.UnlockIDRequirement;
import com.coinlockedplugin.unlocks.UnlockRegistry;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CoinboundQuestRequirement {

    private static final Map<Quest, Function<UnlockRegistry, List<AppearRequirement>>> QUEST_REQUIREMENTS = new HashMap<>();

    static {
        register(Quest.ANOTHER_SLICE_OF_HAM, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, r),
                new UnlockIDRequirement("Quests" + Quest.THE_DIG_SITE, r),
                new UnlockIDRequirement("Quests" + Quest.THE_GIANT_DWARF, r),
                new UnlockIDRequirement("Quests" + Quest.DEATH_TO_THE_DORGESHUUN, r),
                new MaxSkillLevelUnlocked(Skill.ATTACK, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.THE_ASCENT_OF_ARCEUUS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.AT_FIRST_LIGHT, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.EAGLES_PEAK, r),
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.BELOW_ICE_MOUNTAIN, r -> List.of(
        ));

        register(Quest.BENEATH_CURSED_SANDS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CONTACT, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.BETWEEN_A_ROCK, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.FISHING_CONTEST, r),
                new UnlockIDRequirement("Quests" + Quest.DWARF_CANNON, r),
                new MaxSkillLevelUnlocked(Skill.DEFENCE, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.BIG_CHOMPY_BIRD_HUNTING, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.BIOHAZARD, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PLAGUE_CITY, r)
        ));

        register(Quest.BLACK_KNIGHTS_FORTRESS, r -> List.of(
        ));

        register(Quest.BONE_VOYAGE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_DIG_SITE, r)
        ));

        register(Quest.CABIN_FEVER, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PIRATES_TREASURE, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new UnlockIDRequirement("Quests" + Quest.RUM_DEAL, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.CHILDREN_OF_THE_SUN, r -> List.of(
        ));

        register(Quest.CLIENT_OF_KOUREND, r -> List.of(
        ));

        register(Quest.CLOCK_TOWER, r -> List.of(
        ));

        register(Quest.COLD_WAR, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.CONTACT, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PRINCE_ALI_RESCUE, r),
                new UnlockIDRequirement("Quests" + Quest.ICTHLARINS_LITTLE_HELPER, r)
        ));

        register(Quest.COOKS_ASSISTANT, r -> List.of(
        ));

        register(Quest.THE_CORSAIR_CURSE, r -> List.of(
        ));

        register(Quest.CREATURE_OF_FENKENSTRAIN, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_RESTLESS_GHOST, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)

        ));

        register(Quest.CURRENT_AFFAIRS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PANDEMONIUM, r),
                new MaxSkillLevelUnlocked(Skill.SAILING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._10, r)

        ));

        register(Quest.THE_CURSE_OF_ARRAV, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TROLL_ROMANCE, r),
                new UnlockIDRequirement("Quests" + Quest.DEFENDER_OF_VARROCK, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.STRENGTH, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.DARKNESS_OF_HALLOWVALE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.IN_AID_OF_THE_MYREQUE, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.STRENGTH, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.DEATH_PLATEAU, r -> List.of(
        ));

        register(Quest.DEATH_ON_THE_ISLE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.DEATH_TO_THE_DORGESHUUN, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_LOST_TRIBE, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.DEFENDER_OF_VARROCK, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DEMON_SLAYER, r),
                new UnlockIDRequirement("Quests" + Quest.ROMEO__JULIET, r),
                new UnlockIDRequirement("Quests" + Quest.SHIELD_OF_ARRAV, r),
                new UnlockIDRequirement("Quests" + Quest.FAMILY_CREST, r),
                new UnlockIDRequirement("Quests" + Quest.TEMPLE_OF_IKOV, r),
                new UnlockIDRequirement("Quests" + Quest.GARDEN_OF_TRANQUILLITY, r),
                new UnlockIDRequirement("Quests" + Quest.WHAT_LIES_BELOW, r),
                new UnlockIDRequirement("Quests" + Quest.BELOW_ICE_MOUNTAIN, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.DEMON_SLAYER, r -> List.of(
        ));

        register(Quest.THE_DEPTHS_OF_DESPAIR, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.DESERT_TREASURE_I, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TEMPLE_OF_IKOV, r),
                new UnlockIDRequirement("Quests" + Quest.WATERFALL_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.THE_TOURIST_TRAP, r),
                new UnlockIDRequirement("Quests" + Quest.THE_DIG_SITE, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new UnlockIDRequirement("Quests" + Quest.TROLL_STRONGHOLD, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DESERT_TREASURE_I, r),
                new UnlockIDRequirement("Quests" + Quest.DEVIOUS_MINDS, r),
                new UnlockIDRequirement("Quests" + Quest.ENAKHRAS_LAMENT, r),
                new UnlockIDRequirement("Quests" + Quest.BELOW_ICE_MOUNTAIN, r),
                new UnlockIDRequirement("Quests" + Quest.TEMPLE_OF_THE_EYE, r),
                new UnlockIDRequirement("Quests" + Quest.THE_GARDEN_OF_DEATH, r),
                new UnlockIDRequirement("Quests" + Quest.SECRETS_OF_THE_NORTH, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._80, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._80, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.DEVIOUS_MINDS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DORICS_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.TROLL_STRONGHOLD, r),
                new UnlockIDRequirement("Quests" + Quest.WANTED, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.THE_DIG_SITE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.DORICS_QUEST, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.DRAGON_SLAYER_I, r -> List.of(
        ));

        register(Quest.DRAGON_SLAYER_II, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.LEGENDS_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.GHOSTS_AHOY, r),
                new UnlockIDRequirement("Quests" + Quest.A_TAIL_OF_TWO_CATS, r),
                new UnlockIDRequirement("Quests" + Quest.ANIMAL_MAGNETISM, r),
                new UnlockIDRequirement("Quests" + Quest.DREAM_MENTOR, r),
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r),
                new UnlockIDRequirement("Quests" + Quest.BONE_VOYAGE, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._80, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.DREAM_MENTOR, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.EADGARS_RUSE, r),
                new UnlockIDRequirement("Quests" + Quest.LUNAR_DIPLOMACY, r)
        ));

        register(Quest.DRUIDIC_RITUAL, r -> List.of(
        ));

        register(Quest.DWARF_CANNON, r -> List.of(
        ));

        register(Quest.EADGARS_RUSE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, r),
                new UnlockIDRequirement("Quests" + Quest.TROLL_STRONGHOLD, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.EAGLES_PEAK, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.ELEMENTAL_WORKSHOP_I, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.ELEMENTAL_WORKSHOP_II, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.ELEMENTAL_WORKSHOP_I, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.ENAKHRAS_LAMENT, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.ENLIGHTENED_JOURNEY, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.ERNEST_THE_CHICKEN, r -> List.of(
        ));

        register(Quest.ETHICALLY_ACQUIRED_ANTIQUITIES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.SHIELD_OF_ARRAV, r),
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.THE_EYES_OF_GLOUPHRIE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_GRAND_TREE, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.FAIRYTALE_I__GROWING_PAINS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.LOST_CITY, r),
                new UnlockIDRequirement("Quests" + Quest.NATURE_SPIRIT, r)
        ));

        register(Quest.FAIRYTALE_II__CURE_A_QUEEN, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.FAIRYTALE_I__GROWING_PAINS, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.FAMILY_CREST, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.THE_FEUD, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.FIGHT_ARENA, r -> List.of(
        ));

        register(Quest.THE_FINAL_DAWN, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PERILOUS_MOONS, r),
                new UnlockIDRequirement("Quests" + Quest.THE_HEART_OF_DARKNESS, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.FISHING_CONTEST, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.FORGETTABLE_TALE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.FISHING_CONTEST, r),
                new UnlockIDRequirement("Quests" + Quest.THE_GIANT_DWARF, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.THE_FORSAKEN_TOWER, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r)
        ));

        register(Quest.THE_FREMENNIK_EXILES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.HEROES_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.MOUNTAIN_DAUGHTER, r),
                new UnlockIDRequirement("Quests" + Quest.LUNAR_DIPLOMACY, r),
                new UnlockIDRequirement("Quests" + Quest.THE_FREMENNIK_ISLES, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.THE_FREMENNIK_ISLES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_FREMENNIK_TRIALS, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.THE_FREMENNIK_TRIALS, r -> List.of(
        ));

        register(Quest.THE_GARDEN_OF_DEATH, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.GARDEN_OF_TRANQUILLITY, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CREATURE_OF_FENKENSTRAIN, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.GERTRUDES_CAT, r -> List.of(
        ));

        register(Quest.GETTING_AHEAD, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.GHOSTS_AHOY, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_RESTLESS_GHOST, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.THE_GIANT_DWARF, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.GOBLIN_DIPLOMACY, r -> List.of(
        ));

        register(Quest.THE_GOLEM, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.THE_GRAND_TREE, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.THE_GREAT_BRAIN_ROBBERY, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CREATURE_OF_FENKENSTRAIN, r),
                new UnlockIDRequirement("Quests" + Quest.CABIN_FEVER, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.GRIM_TALES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.WITCHS_HOUSE, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._80, r)
        ));

        register(Quest.THE_HAND_IN_THE_SAND, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.HAUNTED_MINE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.HAZEEL_CULT, r -> List.of(
        ));

        register(Quest.THE_HEART_OF_DARKNESS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TWILIGHTS_PROMISE, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.HEROES_QUEST, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.SHIELD_OF_ARRAV, r),
                new UnlockIDRequirement("Quests" + Quest.DRAGON_SLAYER_I, r),
                new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, r),
                new UnlockIDRequirement("Quests" + Quest.LOST_CITY, r),
                new UnlockIDRequirement("Quests" + Quest.MERLINS_CRYSTAL, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.HOLY_GRAIL, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.MERLINS_CRYSTAL, r),
                new MaxSkillLevelUnlocked(Skill.ATTACK, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.HORROR_FROM_THE_DEEP, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.ICTHLARINS_LITTLE_HELPER, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.GERTRUDES_CAT, r)
        ));

        register(Quest.IMP_CATCHER, r -> List.of(
        ));

        register(Quest.IN_AID_OF_THE_MYREQUE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.IN_SEARCH_OF_THE_MYREQUE, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.IN_SEARCH_OF_THE_MYREQUE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.NATURE_SPIRIT, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.JUNGLE_POTION, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.KINGS_RANSOM, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.BLACK_KNIGHTS_FORTRESS, r),
                new UnlockIDRequirement("Quests" + Quest.HOLY_GRAIL, r),
                new UnlockIDRequirement("Quests" + Quest.MURDER_MYSTERY, r),
                new UnlockIDRequirement("Quests" + Quest.ONE_SMALL_FAVOUR, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.DEFENCE, MaxSkillLevelUnlocked.LevelRanges._70, r)
        ));

        register(Quest.A_KINGDOM_DIVIDED, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_QUEEN_OF_THIEVES, r),
                new UnlockIDRequirement("Quests" + Quest.THE_DEPTHS_OF_DESPAIR, r),
                new UnlockIDRequirement("Quests" + Quest.TALE_OF_THE_RIGHTEOUS, r),
                new UnlockIDRequirement("Quests" + Quest.THE_FORSAKEN_TOWER, r),
                new UnlockIDRequirement("Quests" + Quest.THE_ASCENT_OF_ARCEUUS, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.THE_KNIGHTS_SWORD, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.LAND_OF_THE_GOBLINS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.FISHING_CONTEST, r),
                new UnlockIDRequirement("Quests" + Quest.ANOTHER_SLICE_OF_HAM, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.LEGENDS_QUEST, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.HEROES_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.FAMILY_CREST, r),
                new UnlockIDRequirement("Quests" + Quest.WATERFALL_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.SHILO_VILLAGE, r),
                new UnlockIDRequirement("Quests" + Quest.UNDERGROUND_PASS, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.STRENGTH, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.LOST_CITY, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.THE_LOST_TRIBE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.GOBLIN_DIPLOMACY, r),
                new UnlockIDRequirement("Quests" + Quest.RUNE_MYSTERIES, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.LUNAR_DIPLOMACY, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.LOST_CITY, r),
                new UnlockIDRequirement("Quests" + Quest.SHILO_VILLAGE, r),
                new UnlockIDRequirement("Quests" + Quest.RUNE_MYSTERIES, r),
                new UnlockIDRequirement("Quests" + Quest.THE_FREMENNIK_TRIALS, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.DEFENCE, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.MAKING_FRIENDS_WITH_MY_ARM, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.ROMEO__JULIET, r),
                new UnlockIDRequirement("Quests" + Quest.SWAN_SONG, r),
                new UnlockIDRequirement("Quests" + Quest.MY_ARMS_BIG_ADVENTURE, r),
                new UnlockIDRequirement("Quests" + Quest.COLD_WAR, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._80, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r)
        ));

        register(Quest.MAKING_HISTORY, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_RESTLESS_GHOST, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r)
        ));

        register(Quest.MEAT_AND_GREET, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r)
        ));

        register(Quest.MERLINS_CRYSTAL, r -> List.of(
        ));

        register(Quest.MISTHALIN_MYSTERY, r -> List.of(
        ));

        register(Quest.MONKS_FRIEND, r -> List.of(
        ));

        register(Quest.MONKEY_MADNESS_I, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TREE_GNOME_VILLAGE, r),
                new UnlockIDRequirement("Quests" + Quest.THE_GRAND_TREE, r)
        ));

        register(Quest.MONKEY_MADNESS_II, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.WATCHTOWER, r),
                new UnlockIDRequirement("Quests" + Quest.TROLL_STRONGHOLD, r),
                new UnlockIDRequirement("Quests" + Quest.THE_EYES_OF_GLOUPHRIE, r),
                new UnlockIDRequirement("Quests" + Quest.ENLIGHTENED_JOURNEY, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.MOUNTAIN_DAUGHTER, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.MOURNINGS_END_PART_I, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.SHEEP_HERDER, r),
                new UnlockIDRequirement("Quests" + Quest.BIG_CHOMPY_BIRD_HUNTING, r),
                new UnlockIDRequirement("Quests" + Quest.ROVING_ELVES, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.MOURNINGS_END_PART_II, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.MOURNINGS_END_PART_I, r)
        ));

        register(Quest.MURDER_MYSTERY, r -> List.of(
        ));

        register(Quest.MY_ARMS_BIG_ADVENTURE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.JUNGLE_POTION, r),
                new UnlockIDRequirement("Quests" + Quest.EADGARS_RUSE, r),
                new UnlockIDRequirement("Quests" + Quest.THE_FEUD, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.NATURE_SPIRIT, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_RESTLESS_GHOST, r),
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r)
        ));

        register(Quest.A_NIGHT_AT_THE_THEATRE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.A_TASTE_OF_HOPE, r)
        ));

        register(Quest.OBSERVATORY_QUEST, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.OLAFS_QUEST, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_FREMENNIK_TRIALS, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.ONE_SMALL_FAVOUR, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.SHILO_VILLAGE, r),
                new UnlockIDRequirement("Quests" + Quest.RUNE_MYSTERIES, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.PANDEMONIUM, r -> List.of(
        ));

        register(Quest.THE_PATH_OF_GLOUPHRIE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TREE_GNOME_VILLAGE, r),
                new UnlockIDRequirement("Quests" + Quest.WATERFALL_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.THE_EYES_OF_GLOUPHRIE, r),
                new MaxSkillLevelUnlocked(Skill.STRENGTH, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.PERILOUS_MOONS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TWILIGHTS_PROMISE, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.PIRATES_TREASURE, r -> List.of(
        ));

        register(Quest.PLAGUE_CITY, r -> List.of(
        ));

        register(Quest.A_PORCINE_OF_INTEREST, r -> List.of(
        ));

        register(Quest.PRIEST_IN_PERIL, r -> List.of(
        ));

        register(Quest.PRINCE_ALI_RESCUE, r -> List.of(
        ));

        register(Quest.PRYING_TIMES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THE_KNIGHTS_SWORD, r),
                new UnlockIDRequirement("Quests" + Quest.PANDEMONIUM, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.SAILING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.THE_QUEEN_OF_THIEVES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.RAG_AND_BONE_MAN_I, r -> List.of(
        ));

        register(Quest.RAG_AND_BONE_MAN_II, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.RAG_AND_BONE_MAN_I, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.RATCATCHERS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.ICTHLARINS_LITTLE_HELPER, r)
        ));

        register(Quest.RECIPE_FOR_DISASTER, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.COOKS_ASSISTANT, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.RECRUITMENT_DRIVE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.BLACK_KNIGHTS_FORTRESS, r),
                new UnlockIDRequirement("Quests" + Quest.DRUIDIC_RITUAL, r)
        ));

        register(Quest.REGICIDE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.UNDERGROUND_PASS, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.THE_RESTLESS_GHOST, r -> List.of(
        ));

        register(Quest.THE_RIBBITING_TALE_OF_A_LILY_PAD_LABOUR_DISPUTE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.ROMEO__JULIET, r -> List.of(
        ));

        register(Quest.ROVING_ELVES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.WATERFALL_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.REGICIDE, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.ROYAL_TROUBLE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.THRONE_OF_MISCELLANIA, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.RUM_DEAL, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new UnlockIDRequirement("Quests" + Quest.ZOGRE_FLESH_EATERS, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.RUNE_MYSTERIES, r -> List.of(
        ));

        register(Quest.SCORPION_CATCHER, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.PRAYER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.SCRAMBLED, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.SEA_SLUG, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.SECRETS_OF_THE_NORTH, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.HAZEEL_CULT, r),
                new UnlockIDRequirement("Quests" + Quest.DEVIOUS_MINDS, r),
                new UnlockIDRequirement("Quests" + Quest.MAKING_FRIENDS_WITH_MY_ARM, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._60, r)
        ));

        register(Quest.SHADES_OF_MORTTON, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.SHADOW_OF_THE_STORM, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DEMON_SLAYER, r),
                new UnlockIDRequirement("Quests" + Quest.THE_GOLEM, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.SHADOWS_OF_CUSTODIA, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.SHEEP_HERDER, r -> List.of(
        ));

        register(Quest.SHEEP_SHEARER, r -> List.of(
        ));

        register(Quest.SHIELD_OF_ARRAV, r -> List.of(
        ));

        register(Quest.SHILO_VILLAGE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.JUNGLE_POTION, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.SINS_OF_THE_FATHER, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.VAMPYRE_SLAYER, r),
                new UnlockIDRequirement("Quests" + Quest.A_TASTE_OF_HOPE, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.ATTACK, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._50, r)
        ));

        register(Quest.SLEEPING_GIANTS, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.THE_SLUG_MENACE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.SEA_SLUG, r),
                new UnlockIDRequirement("Quests" + Quest.WANTED, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.SONG_OF_THE_ELVES, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.MOURNINGS_END_PART_II, r),
                new UnlockIDRequirement("Quests" + Quest.MAKING_HISTORY, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._70, r)
        ));

        register(Quest.A_SOULS_BANE, r -> List.of(
        ));

        register(Quest.SPIRITS_OF_THE_ELID, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.SWAN_SONG, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.ONE_SMALL_FAVOUR, r),
                new UnlockIDRequirement("Quests" + Quest.GARDEN_OF_TRANQUILLITY, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.TAI_BWO_WANNAI_TRIO, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.JUNGLE_POTION, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.COOKING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.FISHING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.A_TAIL_OF_TWO_CATS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.ICTHLARINS_LITTLE_HELPER, r)
        ));

        register(Quest.TALE_OF_THE_RIGHTEOUS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CLIENT_OF_KOUREND, r),
                new MaxSkillLevelUnlocked(Skill.STRENGTH, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.A_TASTE_OF_HOPE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DARKNESS_OF_HALLOWVALE, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.ATTACK, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._40, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.TEARS_OF_GUTHIX, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FIREMAKING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.TEMPLE_OF_IKOV, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.TEMPLE_OF_THE_EYE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.RUNE_MYSTERIES, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.THRONE_OF_MISCELLANIA, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.HEROES_QUEST, r),
                new UnlockIDRequirement("Quests" + Quest.THE_FREMENNIK_TRIALS, r)
        ));

        register(Quest.THE_TOURIST_TRAP, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.FLETCHING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.TOWER_OF_LIFE, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._10, r)
        ));

        register(Quest.TREE_GNOME_VILLAGE, r -> List.of(
        ));

        register(Quest.TRIBAL_TOTEM, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.TROLL_ROMANCE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.TROLL_STRONGHOLD, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.TROLL_STRONGHOLD, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.DEATH_PLATEAU, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._20, r)
        ));

        register(Quest.TROUBLED_TORTUGANS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PANDEMONIUM, r),
                new MaxSkillLevelUnlocked(Skill.SLAYER, MaxSkillLevelUnlocked.LevelRanges._60, r),
                new MaxSkillLevelUnlocked(Skill.CONSTRUCTION, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.SAILING, MaxSkillLevelUnlocked.LevelRanges._50, r),
                new MaxSkillLevelUnlocked(Skill.WOODCUTTING, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.CRAFTING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.TWILIGHTS_PROMISE, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.CHILDREN_OF_THE_SUN, r)
        ));

        register(Quest.UNDERGROUND_PASS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.BIOHAZARD, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._30, r)
        ));

        register(Quest.VAMPYRE_SLAYER, r -> List.of(
        ));

        register(Quest.WANTED, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.PRIEST_IN_PERIL, r),
                new UnlockIDRequirement("Quests" + Quest.THE_LOST_TRIBE, r),
                new UnlockIDRequirement("Quests" + Quest.RECRUITMENT_DRIVE, r)
        ));

        register(Quest.WATCHTOWER, r -> List.of(
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._30, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._20, r),
                new MaxSkillLevelUnlocked(Skill.MINING, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.WATERFALL_QUEST, r -> List.of(
        ));

        register(Quest.WHAT_LIES_BELOW, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.RUNE_MYSTERIES, r),
                new MaxSkillLevelUnlocked(Skill.RUNECRAFT, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));

        register(Quest.WHILE_GUTHIX_SLEEPS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.FIGHT_ARENA, r),
                new UnlockIDRequirement("Quests" + Quest.NATURE_SPIRIT, r),
                new UnlockIDRequirement("Quests" + Quest.TEARS_OF_GUTHIX, r),
                new UnlockIDRequirement("Quests" + Quest.A_TAIL_OF_TWO_CATS, r),
                new UnlockIDRequirement("Quests" + Quest.WANTED, r),
                new UnlockIDRequirement("Quests" + Quest.THE_HAND_IN_THE_SAND, r),
                new UnlockIDRequirement("Quests" + Quest.DREAM_MENTOR, r),
                new UnlockIDRequirement("Quests" + Quest.TEMPLE_OF_THE_EYE, r),
                new UnlockIDRequirement("Quests" + Quest.THE_PATH_OF_GLOUPHRIE, r),
                new UnlockIDRequirement("Quests" + Quest.DEFENDER_OF_VARROCK, r),
                new MaxSkillLevelUnlocked(Skill.THIEVING, MaxSkillLevelUnlocked.LevelRanges._80, r),
                new MaxSkillLevelUnlocked(Skill.MAGIC, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.AGILITY, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.FARMING, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._70, r),
                new MaxSkillLevelUnlocked(Skill.HUNTER, MaxSkillLevelUnlocked.LevelRanges._70, r)
        ));

        register(Quest.WITCHS_HOUSE, r -> List.of(
        ));

        register(Quest.WITCHS_POTION, r -> List.of(
        ));

        register(Quest.X_MARKS_THE_SPOT, r -> List.of(
        ));

        register(Quest.ZOGRE_FLESH_EATERS, r -> List.of(
                new UnlockIDRequirement("Quests" + Quest.JUNGLE_POTION, r),
                new UnlockIDRequirement("Quests" + Quest.BIG_CHOMPY_BIRD_HUNTING, r),
                new MaxSkillLevelUnlocked(Skill.SMITHING, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.HERBLORE, MaxSkillLevelUnlocked.LevelRanges._10, r),
                new MaxSkillLevelUnlocked(Skill.RANGED, MaxSkillLevelUnlocked.LevelRanges._40, r)
        ));
    }

    private static void register(Quest quest, Function<UnlockRegistry, List<AppearRequirement>> requirements) {
        QUEST_REQUIREMENTS.put(quest, requirements);
    }

    public static List<AppearRequirement> getRequirementsForQuest(Quest quest, UnlockRegistry registry) {
        Function<UnlockRegistry, List<AppearRequirement>> factory = QUEST_REQUIREMENTS.get(quest);
        if (factory != null) {
            return factory.apply(registry);
        } else return null;
    }
}
