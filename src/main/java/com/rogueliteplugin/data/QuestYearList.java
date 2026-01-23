package com.rogueliteplugin.data;

import net.runelite.api.Quest;

import java.util.HashMap;
import java.util.Map;

public class QuestYearList {
    public static final Map<Quest, String> QUEST_YEAR = new HashMap<>();
    public static final Map<String, Quest> QUEST_BY_NAME = new HashMap<>();

    static
    {
        for (Quest quest : Quest.values())
        {
            QUEST_BY_NAME.put(quest.getName(), quest);
        }
    }
    static {
        // 2001
        QUEST_YEAR.put(Quest.COOKS_ASSISTANT, "2001");
        QUEST_YEAR.put(Quest.DEMON_SLAYER, "2001");
        QUEST_YEAR.put(Quest.THE_RESTLESS_GHOST, "2001");
        QUEST_YEAR.put(Quest.ROMEO__JULIET, "2001");
        QUEST_YEAR.put(Quest.SHEEP_SHEARER, "2001");
        QUEST_YEAR.put(Quest.SHIELD_OF_ARRAV, "2001");
        QUEST_YEAR.put(Quest.ERNEST_THE_CHICKEN, "2001");
        QUEST_YEAR.put(Quest.VAMPYRE_SLAYER, "2001");
        QUEST_YEAR.put(Quest.IMP_CATCHER, "2001");
        QUEST_YEAR.put(Quest.PRINCE_ALI_RESCUE, "2001");
        QUEST_YEAR.put(Quest.DORICS_QUEST, "2001");
        QUEST_YEAR.put(Quest.BLACK_KNIGHTS_FORTRESS, "2001");
        QUEST_YEAR.put(Quest.WITCHS_POTION, "2001");
        QUEST_YEAR.put(Quest.THE_KNIGHTS_SWORD, "2001");
        QUEST_YEAR.put(Quest.GOBLIN_DIPLOMACY, "2001");
        QUEST_YEAR.put(Quest.PIRATES_TREASURE, "2001");
        QUEST_YEAR.put(Quest.DRAGON_SLAYER_I, "2001");

// 2002
        QUEST_YEAR.put(Quest.DRUIDIC_RITUAL, "2002");
        QUEST_YEAR.put(Quest.LOST_CITY, "2002");
        QUEST_YEAR.put(Quest.WITCHS_HOUSE, "2002");
        QUEST_YEAR.put(Quest.MERLINS_CRYSTAL, "2002");
        QUEST_YEAR.put(Quest.HEROES_QUEST, "2002");
        QUEST_YEAR.put(Quest.SCORPION_CATCHER, "2002");
        QUEST_YEAR.put(Quest.FAMILY_CREST, "2002");
        QUEST_YEAR.put(Quest.TRIBAL_TOTEM, "2002");
        QUEST_YEAR.put(Quest.FISHING_CONTEST, "2002");
        QUEST_YEAR.put(Quest.MONKS_FRIEND, "2002");
        QUEST_YEAR.put(Quest.TEMPLE_OF_IKOV, "2002");
        QUEST_YEAR.put(Quest.CLOCK_TOWER, "2002");
        QUEST_YEAR.put(Quest.HOLY_GRAIL, "2002");
        QUEST_YEAR.put(Quest.TREE_GNOME_VILLAGE, "2002");
        QUEST_YEAR.put(Quest.FIGHT_ARENA, "2002");
        QUEST_YEAR.put(Quest.HAZEEL_CULT, "2002");
        QUEST_YEAR.put(Quest.SHEEP_HERDER, "2002");
        QUEST_YEAR.put(Quest.PLAGUE_CITY, "2002");
        QUEST_YEAR.put(Quest.SEA_SLUG, "2002");
        QUEST_YEAR.put(Quest.WATERFALL_QUEST, "2002");
        QUEST_YEAR.put(Quest.BIOHAZARD, "2002");
        QUEST_YEAR.put(Quest.JUNGLE_POTION, "2002");
        QUEST_YEAR.put(Quest.THE_GRAND_TREE, "2002");

// 2003
        QUEST_YEAR.put(Quest.SHILO_VILLAGE, "2003");
        QUEST_YEAR.put(Quest.UNDERGROUND_PASS, "2003");
        QUEST_YEAR.put(Quest.OBSERVATORY_QUEST, "2003");
        QUEST_YEAR.put(Quest.THE_TOURIST_TRAP, "2003");
        QUEST_YEAR.put(Quest.WATCHTOWER, "2003");
        QUEST_YEAR.put(Quest.DWARF_CANNON, "2003");
        QUEST_YEAR.put(Quest.MURDER_MYSTERY, "2003");
        QUEST_YEAR.put(Quest.THE_DIG_SITE, "2003");
        QUEST_YEAR.put(Quest.GERTRUDES_CAT, "2003");
        QUEST_YEAR.put(Quest.LEGENDS_QUEST, "2003");

// 2004
        QUEST_YEAR.put(Quest.RUNE_MYSTERIES, "2004");
        QUEST_YEAR.put(Quest.BIG_CHOMPY_BIRD_HUNTING, "2004");
        QUEST_YEAR.put(Quest.ELEMENTAL_WORKSHOP_I, "2004");
        QUEST_YEAR.put(Quest.PRIEST_IN_PERIL, "2004");
        QUEST_YEAR.put(Quest.NATURE_SPIRIT, "2004");
        QUEST_YEAR.put(Quest.DEATH_PLATEAU, "2004");
        QUEST_YEAR.put(Quest.TROLL_STRONGHOLD, "2004");
        QUEST_YEAR.put(Quest.TAI_BWO_WANNAI_TRIO, "2004");
        QUEST_YEAR.put(Quest.REGICIDE, "2004");
        QUEST_YEAR.put(Quest.EADGARS_RUSE, "2004");
        QUEST_YEAR.put(Quest.SHADES_OF_MORTTON, "2004");
        QUEST_YEAR.put(Quest.THE_FREMENNIK_TRIALS, "2004");
        QUEST_YEAR.put(Quest.HORROR_FROM_THE_DEEP, "2004");
        QUEST_YEAR.put(Quest.THRONE_OF_MISCELLANIA, "2004");
        QUEST_YEAR.put(Quest.MONKEY_MADNESS_I, "2004");
        QUEST_YEAR.put(Quest.HAUNTED_MINE, "2004");

// 2005
        QUEST_YEAR.put(Quest.TROLL_ROMANCE, "2005");
        QUEST_YEAR.put(Quest.IN_SEARCH_OF_THE_MYREQUE, "2005");
        QUEST_YEAR.put(Quest.CREATURE_OF_FENKENSTRAIN, "2005");
        QUEST_YEAR.put(Quest.ROVING_ELVES, "2005");
        QUEST_YEAR.put(Quest.GHOSTS_AHOY, "2005");
        QUEST_YEAR.put(Quest.ONE_SMALL_FAVOUR, "2005");
        QUEST_YEAR.put(Quest.MOUNTAIN_DAUGHTER, "2005");
        QUEST_YEAR.put(Quest.BETWEEN_A_ROCK, "2005");
        QUEST_YEAR.put(Quest.THE_FEUD, "2005");
        QUEST_YEAR.put(Quest.THE_GOLEM, "2005");
        QUEST_YEAR.put(Quest.DESERT_TREASURE_I, "2005");
        QUEST_YEAR.put(Quest.ICTHLARINS_LITTLE_HELPER, "2005");
        QUEST_YEAR.put(Quest.TEARS_OF_GUTHIX, "2005");
        QUEST_YEAR.put(Quest.ZOGRE_FLESH_EATERS, "2005");
        QUEST_YEAR.put(Quest.THE_LOST_TRIBE, "2005");
        QUEST_YEAR.put(Quest.THE_GIANT_DWARF, "2005");
        QUEST_YEAR.put(Quest.RECRUITMENT_DRIVE, "2005");
        QUEST_YEAR.put(Quest.MOURNINGS_END_PART_I, "2005");
        QUEST_YEAR.put(Quest.FORGETTABLE_TALE, "2005");
        QUEST_YEAR.put(Quest.GARDEN_OF_TRANQUILLITY, "2005");
        QUEST_YEAR.put(Quest.A_TAIL_OF_TWO_CATS, "2005");
        QUEST_YEAR.put(Quest.WANTED, "2005");
        QUEST_YEAR.put(Quest.MOURNINGS_END_PART_II, "2005");
        QUEST_YEAR.put(Quest.RUM_DEAL, "2005");
        QUEST_YEAR.put(Quest.SHADOW_OF_THE_STORM, "2005");
        QUEST_YEAR.put(Quest.MAKING_HISTORY, "2005");
        QUEST_YEAR.put(Quest.RATCATCHERS, "2005");
        QUEST_YEAR.put(Quest.SPIRITS_OF_THE_ELID, "2005");
        QUEST_YEAR.put(Quest.DEVIOUS_MINDS, "2005");

// 2006
        QUEST_YEAR.put(Quest.THE_HAND_IN_THE_SAND, "2006");
        QUEST_YEAR.put(Quest.ENAKHRAS_LAMENT, "2006");
        QUEST_YEAR.put(Quest.CABIN_FEVER, "2006");
        QUEST_YEAR.put(Quest.FAIRYTALE_I__GROWING_PAINS, "2006");
        QUEST_YEAR.put(Quest.RECIPE_FOR_DISASTER, "2006");
        QUEST_YEAR.put(Quest.IN_AID_OF_THE_MYREQUE, "2006");
        QUEST_YEAR.put(Quest.A_SOULS_BANE, "2006");
        QUEST_YEAR.put(Quest.RAG_AND_BONE_MAN_I, "2006");
        QUEST_YEAR.put(Quest.SWAN_SONG, "2006");
        QUEST_YEAR.put(Quest.ROYAL_TROUBLE, "2006");
        QUEST_YEAR.put(Quest.DEATH_TO_THE_DORGESHUUN, "2006");
        QUEST_YEAR.put(Quest.FAIRYTALE_II__CURE_A_QUEEN, "2006");
        QUEST_YEAR.put(Quest.LUNAR_DIPLOMACY, "2006");
        QUEST_YEAR.put(Quest.THE_EYES_OF_GLOUPHRIE, "2006");
        QUEST_YEAR.put(Quest.DARKNESS_OF_HALLOWVALE, "2006");
        QUEST_YEAR.put(Quest.THE_SLUG_MENACE, "2006");
        QUEST_YEAR.put(Quest.ELEMENTAL_WORKSHOP_II, "2006");
        QUEST_YEAR.put(Quest.MY_ARMS_BIG_ADVENTURE, "2006");
        QUEST_YEAR.put(Quest.ENLIGHTENED_JOURNEY, "2006");
        QUEST_YEAR.put(Quest.EAGLES_PEAK, "2006");
        QUEST_YEAR.put(Quest.ANIMAL_MAGNETISM, "2006");
        QUEST_YEAR.put(Quest.RAG_AND_BONE_MAN_II, "2006");

// 2007
        QUEST_YEAR.put(Quest.CONTACT, "2007");
        QUEST_YEAR.put(Quest.COLD_WAR, "2007");
        QUEST_YEAR.put(Quest.THE_FREMENNIK_ISLES, "2007");
        QUEST_YEAR.put(Quest.TOWER_OF_LIFE, "2007");
        QUEST_YEAR.put(Quest.THE_GREAT_BRAIN_ROBBERY, "2007");
        QUEST_YEAR.put(Quest.WHAT_LIES_BELOW, "2007");
        QUEST_YEAR.put(Quest.OLAFS_QUEST, "2007");
        QUEST_YEAR.put(Quest.ANOTHER_SLICE_OF_HAM, "2007");
        QUEST_YEAR.put(Quest.DREAM_MENTOR, "2007");
        QUEST_YEAR.put(Quest.GRIM_TALES, "2007");
        QUEST_YEAR.put(Quest.KINGS_RANSOM, "2007");

// Modern OSRS era
        QUEST_YEAR.put(Quest.MONKEY_MADNESS_II, "2016");

        QUEST_YEAR.put(Quest.MISTHALIN_MYSTERY, "2017");
        QUEST_YEAR.put(Quest.CLIENT_OF_KOUREND, "2017");
        QUEST_YEAR.put(Quest.THE_CORSAIR_CURSE, "2017");
        QUEST_YEAR.put(Quest.BONE_VOYAGE, "2017");
        QUEST_YEAR.put(Quest.THE_QUEEN_OF_THIEVES, "2017");
        QUEST_YEAR.put(Quest.THE_DEPTHS_OF_DESPAIR, "2017");

        QUEST_YEAR.put(Quest.DRAGON_SLAYER_II, "2018");
        QUEST_YEAR.put(Quest.TALE_OF_THE_RIGHTEOUS, "2018");
        QUEST_YEAR.put(Quest.A_TASTE_OF_HOPE, "2018");
        QUEST_YEAR.put(Quest.MAKING_FRIENDS_WITH_MY_ARM, "2018");

        QUEST_YEAR.put(Quest.THE_ASCENT_OF_ARCEUUS, "2019");
        QUEST_YEAR.put(Quest.X_MARKS_THE_SPOT, "2019");
        QUEST_YEAR.put(Quest.SONG_OF_THE_ELVES, "2019");
        QUEST_YEAR.put(Quest.THE_FREMENNIK_EXILES, "2019");
        QUEST_YEAR.put(Quest.THE_FORSAKEN_TOWER, "2019");

        QUEST_YEAR.put(Quest.SINS_OF_THE_FATHER, "2020");
        QUEST_YEAR.put(Quest.A_PORCINE_OF_INTEREST, "2020");
        QUEST_YEAR.put(Quest.GETTING_AHEAD, "2020");

        QUEST_YEAR.put(Quest.BELOW_ICE_MOUNTAIN, "2021");
        QUEST_YEAR.put(Quest.A_KINGDOM_DIVIDED, "2021");
        QUEST_YEAR.put(Quest.A_NIGHT_AT_THE_THEATRE, "2021");

        QUEST_YEAR.put(Quest.LAND_OF_THE_GOBLINS, "2022");
        QUEST_YEAR.put(Quest.TEMPLE_OF_THE_EYE, "2022");
        QUEST_YEAR.put(Quest.BENEATH_CURSED_SANDS, "2022");
        QUEST_YEAR.put(Quest.SLEEPING_GIANTS, "2022");
        QUEST_YEAR.put(Quest.THE_GARDEN_OF_DEATH, "2022");

        QUEST_YEAR.put(Quest.SECRETS_OF_THE_NORTH, "2023");
        QUEST_YEAR.put(Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE, "2023");
        QUEST_YEAR.put(Quest.THE_PATH_OF_GLOUPHRIE, "2023");

        QUEST_YEAR.put(Quest.MEAT_AND_GREET, "2024");
        QUEST_YEAR.put(Quest.CHILDREN_OF_THE_SUN, "2024");
        QUEST_YEAR.put(Quest.DEFENDER_OF_VARROCK, "2024");
        QUEST_YEAR.put(Quest.TWILIGHTS_PROMISE, "2024");
        QUEST_YEAR.put(Quest.AT_FIRST_LIGHT, "2024");
        QUEST_YEAR.put(Quest.PERILOUS_MOONS, "2024");
        QUEST_YEAR.put(Quest.THE_RIBBITING_TALE_OF_A_LILY_PAD_LABOUR_DISPUTE, "2024");
        QUEST_YEAR.put(Quest.WHILE_GUTHIX_SLEEPS, "2024");
        QUEST_YEAR.put(Quest.THE_HEART_OF_DARKNESS, "2024");
        QUEST_YEAR.put(Quest.DEATH_ON_THE_ISLE, "2024");
        QUEST_YEAR.put(Quest.ETHICALLY_ACQUIRED_ANTIQUITIES, "2024");
        QUEST_YEAR.put(Quest.THE_CURSE_OF_ARRAV, "2024");

        QUEST_YEAR.put(Quest.THE_FINAL_DAWN, "2025");
        QUEST_YEAR.put(Quest.SHADOWS_OF_CUSTODIA, "2025");
        QUEST_YEAR.put(Quest.SCRAMBLED, "2025");
        QUEST_YEAR.put(Quest.PANDEMONIUM, "2025");
        QUEST_YEAR.put(Quest.PRYING_TIMES, "2025");
        QUEST_YEAR.put(Quest.CURRENT_AFFAIRS, "2025");
        QUEST_YEAR.put(Quest.TROUBLED_TORTUGANS, "2025");

        //QUEST_YEAR.put("Tutorial Island", "2025"); Tutorial Island is not counted as a quest
    }
}
