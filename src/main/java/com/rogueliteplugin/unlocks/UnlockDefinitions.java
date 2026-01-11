package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SkillIconManager;

import java.awt.image.BufferedImage;
import java.util.Set;

public final class UnlockDefinitions {
    private UnlockDefinitions() {
    }

    public static void registerAll(
            UnlockRegistry registry,
            SkillIconManager skillIconManager,
            RoguelitePlugin plugin
    ) {
        registerSkills(registry, skillIconManager);
        registerRegions(registry, plugin);
        registerClueTiers(registry);
        registerShops(registry, plugin);
        registerMinigames(registry, plugin);
        registerBosses(registry);
    }

    private static void registerSkills(
            UnlockRegistry registry,
            SkillIconManager skillIconManager
    ) {
        for (Skill skill : Skill.values()) {
            BufferedImage img = skillIconManager.getSkillImage(skill);
            if (img != null) {
                registry.register(new SkillUnlock(skill, skillIconManager));
            }
        }
    }

    private static void registerClueTiers(UnlockRegistry registry) {
        registry.register(
                new ClueUnlock(
                        "BEGINNERCLUES",
                        "Beginner clues",
                        IconLoader.load("clues/beginnerclue.png"),
                        "Allows opening of beginner clue caskets."
                )
        );
        registry.register(
                new ClueUnlock(
                        "EASYCLUES",
                        "Easy clues",
                        IconLoader.load("clues/easyclue.png"),
                        "Allows opening of easy clue caskets."
                )
        );
        registry.register(
                new ClueUnlock(
                        "MEDIUMCLUES",
                        "Medium clues",
                        IconLoader.load("clues/mediumclue.png"),
                        "Allows opening of hard clue caskets."
                )
        );
        registry.register(
                new ClueUnlock(
                        "HARDCLUES",
                        "Hard clues",
                        IconLoader.load("clues/hardclue.png"),
                        "Allows opening of hard clue caskets."
                )
        );
        registry.register(
                new ClueUnlock(
                        "ELITECLUES",
                        "Elite clues",
                        IconLoader.load("clues/eliteclue.png"),
                        "Allows opening of elite clue caskets."
                )
        );
        registry.register(
                new ClueUnlock(
                        "MASTERCLUES",
                        "Master clues",
                        IconLoader.load("clues/masterclue.png"),
                        "Allows opening of master clue caskets."
                )
        );
    }

    private static void registerRegions(UnlockRegistry registry, RoguelitePlugin plugin)
    {
        registry.register(new RegionUnlock(
                "MISthalin",
                "Misthalin",
                Set.of(
                        12850, 12851, // Lumbridge
                        13106, 13107, // Varrock
                        12849, 13105  // Draynor
                ),
                new ImageUnlockIcon(
                        IconLoader.load("areas/misthalin.png")
                ),
                "Allows access to Misthalin."
        ));

        registry.register(new RegionUnlock(
                "KARAMJA",
                "Karamja",
                Set.of(
                        11057, 11058,
                        11313, 11314
                ),
                new ImageUnlockIcon(
                        IconLoader.load("areas/karamja.png")
                ),
                "Allows access to Karamja."
        ));
    }

    private static void registerMinigames(UnlockRegistry registry, RoguelitePlugin plugin) {
        registry.register(
                new MinigameUnlock(
                        "Barbarian_Assault",
                        "Barbarian Assault",
                        IconLoader.load("minigames/Barbarian_Assault_logo.png"),
                        "Allows access to the Barbarian assault minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "Bounty_Hunter",
                        "Bounty Hunter",
                        IconLoader.load("minigames/Bounty_Hunter_logo.png"),
                        "Allows access to the Bounty Hunter minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "Temple_Trekking)",
                        "Temple Trekking",
                        IconLoader.load("minigames/Temple_Trekking_logo.png"),
                        "Allows access to the Temple Trekking minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "Castle Wars",
                        "Castle Wars",
                        IconLoader.load("minigames/Temple_Trekking_logo.png"),
                        "Allows access to the Castle Wars minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "ChampionsChallenge",
                        "Champions' Challenge",
                        IconLoader.load("minigames/Champions_Challenge_logo.png"),
                        "Allows access to the Champions' Challenge minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "CastleWars",
                        "Castle Wars",
                        IconLoader.load("minigames/Mg_castlewars.png"),
                        "Allows access to the Castle Wars minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "ClanWars",
                        "Clan Wars",
                        IconLoader.load("minigames/Clan_Wars_logo.png"),
                        "Allows access to the Clan Wars minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "DuelArena",
                        "Duel Arena",
                        IconLoader.load("minigames/Duel_Arena_logo.png"),
                        "Allows access to the Duel Arena minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "MageArena",
                        "Mage Arena",
                        IconLoader.load("minigames/Mage_Arena_logo.png"),
                        "Allows access to the Mage Arena minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "NightmareZone",
                        "Nightmare Zone",
                        IconLoader.load("minigames/Nightmare_Zone_logo.png"),
                        "Allows access to the Nightmare Zone minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "PestControl",
                        "Pest Control",
                        IconLoader.load("minigames/Pest_Control_logo.png"),
                        "Allows access to the Pest Control minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "TzHaarFightCave",
                        "TzHaar Fight Cave",
                        IconLoader.load("minigames/Tzhaar_Fight_Cave_logo.png"),
                        "Allows access to the TzHaar Fight Cave minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "TzHaarFightPit",
                        "TzHaar Fight Pit",
                        IconLoader.load("minigames/Tzhaar_Fight_Pit_logo.png"),
                        "Allows access to the TzHaar Fight Pit minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "LastManStanding",
                        "Last Man Standing",
                        IconLoader.load("minigames/Last_Man_Standing_logo.png"),
                        "Allows access to the Last Man Standing minigame"
                )
        );
        registry.register(
                new MinigameUnlock(
                        "Inferno",
                        "Inferno",
                        IconLoader.load("minigames/Inferno_logo.png"),
                        "Allows access to the Inferno minigame"
                )
        );
    }

    private static void registerShops(UnlockRegistry registry, RoguelitePlugin plugin) {
        // Basic shops/minimap icons registry
        registry.register(new ShopUnlock(
                "SHOP_GENERAL_STORE",
                "General Store",
                SpriteID.MAP_ICON_GENERAL_STORE,
                "Allows access to general stores."
        ));

        registry.register(new ShopUnlock(
                "SHOP_AXE_SHOP",
                "Axe Shop",
                SpriteID.MAP_ICON_AXE_SHOP,
                "Allows access to axe sales points."
        ));

        registry.register(new ShopUnlock(
                "SHOP_CANDLE_SHOP",
                "Candle Shop",
                SpriteID.MAP_ICON_CANDLE_SHOP,
                "Allows access to candle shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_CHAINMAIL_SHOP",
                "Chainmail Shop",
                SpriteID.MAP_ICON_CHAINMAIL_SHOP,
                "Allows access to chainmail shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_CLOTHES_SHOP",
                "Clothes Shop",
                SpriteID.MAP_ICON_CLOTHES_SHOP,
                "Allows access to clothes shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_CRAFTING_SHOP",
                "Crafting Shop",
                SpriteID.MAP_ICON_CRAFTING_SHOP,
                "Allows access to crafting shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FARMING_SHOP",
                "Farming Shop",
                SpriteID.MAP_ICON_FARMING_SHOP,
                "Allows access to farming shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FISHING_SHOP",
                "Fishing Shop",
                SpriteID.MAP_ICON_FISHING_SHOP,
                "Allows access to fishing shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FOOD_SHOP",
                "Food Shop",
                SpriteID.MAP_ICON_FOOD_SHOP,
                "Allows access to general food shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FOOD_SHOP_CUTLERY",
                "Cutlery Food Shop",
                SpriteID.MAP_ICON_FOOD_SHOP_CUTLERY,
                "Unlocks access to cutlery food shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FOOD_SHOP_FRUIT",
                "Fruit Food Shop",
                SpriteID.MAP_ICON_FOOD_SHOP_FRUIT,
                "Unlocks access to fruit food shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_FUR_TRADER",
                "Fur Trader",
                SpriteID.MAP_ICON_FUR_TRADER,
                "Allows access to fur trading shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_GEM_SHOP",
                "Gem Shop",
                SpriteID.MAP_ICON_GEM_SHOP,
                "Allows access to gem shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_HELMET_SHOP",
                "Helmet Shop",
                SpriteID.MAP_ICON_HELMET_SHOP,
                "Allows access to helmet shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_HUNTER_SHOP",
                "Hunter Shop",
                SpriteID.MAP_ICON_HUNTER_SHOP,
                "Allows access to hunter equipment shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_JEWELLERY_SHOP",
                "Jewellery Shop",
                SpriteID.MAP_ICON_JEWELLERY_SHOP,
                "Allows access to jewellery shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_MACE_SHOP",
                "Mace Shop",
                SpriteID.MAP_ICON_MACE_SHOP,
                "Allows access to mace shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_MAGIC_SHOP",
                "Magic Shop",
                SpriteID.MAP_ICON_MAGIC_SHOP,
                "Allows access to magic item shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_MINING_SHOP",
                "Mining Shop",
                SpriteID.MAP_ICON_MINING_SHOP,
                "Allows access to mining supply shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_PET_SHOP",
                "Pet Shop",
                SpriteID.MAP_ICON_PET_SHOP,
                "Allows access to pet shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_PLATEBODY_SHOP",
                "Platebody Shop",
                SpriteID.MAP_ICON_PLATEBODY_SHOP,
                "Allows access to platebody armour shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_PLATELEGS_SHOP",
                "Platelegs Shop",
                SpriteID.MAP_ICON_PLATELEGS_SHOP,
                "Allows access to platelegs shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_PLATESKIRT_SHOP",
                "Plateskirt Shop",
                SpriteID.MAP_ICON_PLATESKIRT_SHOP,
                "Allows access to plateskirt shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_SCIMITAR_SHOP",
                "Scimitar Shop",
                SpriteID.MAP_ICON_SCIMITAR_SHOP,
                "Allows access to scimitar shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_SHIELD_SHOP",
                "Shield Shop",
                SpriteID.MAP_ICON_SHIELD_SHOP,
                "Allows access to shield shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_SILK_TRADER",
                "Silk Trader",
                SpriteID.MAP_ICON_SILK_TRADER,
                "Allows access to silk trading shops."
        ));

        registry.register(new ShopUnlock(
                "SHOP_SILVER_SHOP",
                "Silver Shop",
                SpriteID.MAP_ICON_SILVER_SHOP,
                "Allows access to silver product shops."
        ));
    }

    private static void registerBosses(UnlockRegistry registry) {
        registry.register(new BossUnlock("MIMIC", "Mimic", IconLoader.load("icon.png"), "Fight the Mimic."));
        registry.register(new BossUnlock("OBOR", "Obor", IconLoader.load("icon.png"), "Hill giant boss."));
        registry.register(new BossUnlock("BRYOPHYTA", "Bryophyta", IconLoader.load("icon.png"), "Moss giant boss."));
        registry.register(new BossUnlock("GIANT_MOLE", "Giant Mole", IconLoader.load("icon.png"), "Boss beneath Falador."));
        registry.register(new BossUnlock("GROTESQUE_GUARDIANS", "Grotesque Guardians", IconLoader.load("icon.png"), "Slayer tower rooftop boss."));
        registry.register(new BossUnlock("TEMPOROSS", "Tempoross", IconLoader.load("icon.png"), "Fishing skilling boss."));
        registry.register(new BossUnlock("WINTERTODT", "Wintertodt", IconLoader.load("icon.png"), "Firemaking skilling boss."));
        registry.register(new BossUnlock("BARROWS", "Barrows Brothers", IconLoader.load("icon.png"), "Barrows chest encounter."));
        registry.register(new BossUnlock("SARACHNIS", "Sarachnis",IconLoader.load("icon.png"), "Spider boss in Forthos Dungeon."));
        registry.register(new BossUnlock("KALPHITE_QUEEN", "Kalphite Queen", IconLoader.load("icon.png"), "Desert boss."));
        registry.register(new BossUnlock("HESPORI", "Hespori", IconLoader.load("icon.png"), "Farming guild boss."));
        registry.register(new BossUnlock("SKOTIZO", "Skotizo", IconLoader.load("icon.png"), "Demonic boss in Catacombs."));
        registry.register(new BossUnlock("ZALCANO", "Zalcano", IconLoader.load("icon.png"), "Mining boss in Prifddinas."));
        registry.register(new BossUnlock("KING_BLACK_DRAGON", "King Black Dragon", IconLoader.load("icon.png"), "Wilderness dragon boss."));
        registry.register(new BossUnlock("KRAKEN", "Kraken", IconLoader.load("icon.png"), "Slayer boss."));
        registry.register(new BossUnlock("ABYSSAL_SIRE", "Abyssal Sire", IconLoader.load("icon.png"), "Abyssal demon boss."));
        registry.register(new BossUnlock("THERMY", "Thermonuclear Smoke Devil", IconLoader.load("icon.png"), "Smoke devil boss."));
        registry.register(new BossUnlock("GAUNTLET", "The Gauntlet", IconLoader.load("icon.png"), "Prifddinas challenge."));
        registry.register(new BossUnlock("ZULRAH", "Zulrah", IconLoader.load("icon.png"), "Poisonous serpent boss."));
        registry.register(new BossUnlock("CERBERUS", "Cerberus", IconLoader.load("icon.png"), "Hellhound boss."));
        registry.register(new BossUnlock("VORKATH", "Vorkath", IconLoader.load("icon.png"), "Undead dragon boss."));
        registry.register(new BossUnlock("GENERAL_GRAARDOR", "General Graardor", IconLoader.load("icon.png"), "Bandos GWD boss."));
        registry.register(new BossUnlock("KRIL", "K'ril Tsutsaroth", IconLoader.load("icon.png"), "Zamorak GWD boss."));
        registry.register(new BossUnlock("ZILYANA", "Commander Zilyana", IconLoader.load("icon.png"), "Saradomin GWD boss."));
        registry.register(new BossUnlock("KREEARRA", "Kree'arra",IconLoader.load("icon.png"), "Armadyl GWD boss."));
        registry.register(new BossUnlock("NIGHTMARE", "The Nightmare", IconLoader.load("icon.png"), "Slepe nightmare boss."));
        registry.register(new BossUnlock("CORPOREAL_BEAST", "Corporeal Beast", IconLoader.load("icon.png"), "High-level group boss."));
        registry.register(new BossUnlock("COX", "Chambers of Xeric", IconLoader.load("icon.png"), "Raid."));
        registry.register(new BossUnlock("ALCHEMICAL_HYDRA", "Alchemical Hydra", IconLoader.load("icon.png"), "Hydra slayer boss."));
        registry.register(new BossUnlock("NEX", "Nex", IconLoader.load("icon.png"), "Ancient prison boss."));
        registry.register(new BossUnlock("DK_SUPREME", "Dagannoth Supreme", IconLoader.load("icon.png"), "Dagannoth King."));
        registry.register(new BossUnlock("DK_REX", "Dagannoth Rex", IconLoader.load("icon.png"), "Dagannoth King."));
        registry.register(new BossUnlock("DK_PRIME", "Dagannoth Prime", IconLoader.load("icon.png"), "Dagannoth King."));
        registry.register(new BossUnlock("CORRUPTED_GAUNTLET", "Corrupted Gauntlet", IconLoader.load("icon.png"), "Hard mode Gauntlet."));
        registry.register(new BossUnlock("PHANTOM_MUSPAH", "Phantom Muspah", IconLoader.load("icon.png"), "Ancient ice boss."));

        registry.register(new BossUnlock("CHAOS_ELEMENTAL", "Chaos Elemental",IconLoader.load("icon.png"), "Wilderness boss."));
        registry.register(new BossUnlock("ARTIO", "Artio",IconLoader.load("icon.png"), "Callisto variant."));
        registry.register(new BossUnlock("CALVARION", "Calvar'ion", IconLoader.load("icon.png"), "Vet'ion variant."));
        registry.register(new BossUnlock("SPINDEL", "Spindel",IconLoader.load("icon.png"), "Venenatis variant."));
        registry.register(new BossUnlock("CHAOS_FANATIC", "Chaos Fanatic", IconLoader.load("icon.png"), "Wilderness mage boss."));
        registry.register(new BossUnlock("CRAZY_ARCHAEOLOGIST", "Crazy Archaeologist", IconLoader.load("icon.png"), "Wilderness boss."));
        registry.register(new BossUnlock("DERANGED_ARCHAEOLOGIST", "Deranged Archaeologist", IconLoader.load("icon.png"), "Fossil Island boss."));
        registry.register(new BossUnlock("SCORPIA", "Scorpia", IconLoader.load("icon.png"), "Wilderness scorpion."));
        registry.register(new BossUnlock("JAD", "TzTok-Jad",IconLoader.load("icon.png"), "Fight caves boss."));
        registry.register(new BossUnlock("ZUK", "TzKal-Zuk",IconLoader.load("icon.png"), "Inferno boss."));
        registry.register(new BossUnlock("WHISPERER", "The Whisperer", IconLoader.load("icon.png"), "DT2 boss."));
        registry.register(new BossUnlock("DUKE", "Duke Sucellus", IconLoader.load("icon.png"), "DT2 boss."));
        registry.register(new BossUnlock("LEVIATHAN", "The Leviathan",IconLoader.load("icon.png"), "DT2 boss."));
        registry.register(new BossUnlock("VARDORVIS", "Vardorvis", IconLoader.load("icon.png"), "DT2 boss."));
        registry.register(new BossUnlock("SCURRIUS", "Scurrius", IconLoader.load("icon.png"), "Varrock sewer rat boss."));
        registry.register(new BossUnlock("SOL_HEREDIT", "Sol Heredit",IconLoader.load("icon.png"), "Varlamore boss."));
        registry.register(new BossUnlock("LUNAR_CHESTS", "Lunar Chests", IconLoader.load("icon.png"), "Perilous Moons reward."));
        registry.register(new BossUnlock("ARAXXOR", "Araxxor", IconLoader.load("icon.png"), "Spider boss."));
        registry.register(new BossUnlock("AMOXLIATL", "Amoxliatl", IconLoader.load("icon.png"), "Varlamore boss."));
        registry.register(new BossUnlock("HUEYCOATL", "The Hueycoatl", IconLoader.load("icon.png"), "Varlamore boss."));
    }
}
