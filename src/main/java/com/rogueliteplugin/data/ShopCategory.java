package com.rogueliteplugin.data;

import java.util.Arrays;

public enum ShopCategory {
    SHOP_AMULET_SHOP(
            "Amulet Shop",
            new String[]{"Davon's Amulet Store."}
    ),

    SHOP_ARCHERY_SHOP(
            "Archery Shop",
            new String[]{"Brian's Archery Supplies", "Lowe's Archery Emporium", "Aaron's Archery Appendages", "Arcuani's Archery Supplies", "Dargaud's Bow and Arrows", "Daryl's Ranging Surplus",
                    "Hickton's Archery Emporium.", "Lletya Archery Shop", "Oobapohk's Javelin Store", "Port Roberts Cannonball Stall", "Sian's Ranged Weaponry", "Void Knight Archery Store"}
    ),

    SHOP_AXE_SHOP(
            "Axe Shop",
            new String[]{"Bob's Brilliant Axes", "Brian's Battleaxe Bazaar", "Mount Karuulm Weapon Shop", "Lunami's Axe Shop", "Perry's Chop-chop Shop", "Warrior Guild Armoury"}
    ),

    SHOP_CANDLE_SHOP(
            "Candle Shop",
            new String[]{"Candle seller", "Candle Shop", "Darkmeyer Lantern Shop", "Miltog's Lamps"}
    ),

    SHOP_CHAINBODY_SHOP(
            "Chainbody Shop",
            new String[]{"Wayne's Chains", "Blair's Armour"}
    ),

    SHOP_CLOTHES_SHOP(
            "Clothes Shop",
            new String[]{"Thessalia's Fine Clothes.", "Agmundi Quality Clothes", "Barkers' Haberdashery",
                    "Darkmeyer Seamstress", "Dodgy Mike's Second Hand Clothing.", "Fancy Clothes Store",
                    "Fine Fashions", "Floria's Fashion", "Grace's Graceful Clothing", "Lletya Seamstress",
                    "Lliann's Wares", "Miscellanian Clothes Shop", "Moon Clan Fine Clothes",
                    "Mythical Cape Store", "Shayzien Styles", "Vermundi's Clothes Stall", "Yrsa's Accoutrements"}
    ),

    SHOP_COOKING_SHOP(
            "Cooking Shop",
            new String[]{"Pie Shop", "Grand Tree Groceries", "Funch's Fine Groceries",
                    "Frenita's Cookery Shop", "Cobado's Groceries"}
    ),

    SHOP_CRAFTING_SHOP(
            "Crafting Shop",
            new String[]{"Dommik's Crafting Store", "Rommik's Crafty Supplies", "Artima's Crafting Supplies",
                    "Carefree Crafting Stall", "Elder Coco's Crafting Stall", "Hamab's Crafting Emporium",
                    "Jamila's Craft Stall", "Neitiznot supplies", "Lletya Seamstress",
                    "Prifddinas' Seamstress", "Raetul and Co's Cloth Store"}
    ),

    SHOP_CROSSBOW_SHOP(
            "Crossbow Shop",
            new String[]{"Crossbow Shop"}
    ),

    SHOP_DYE_SHOP(
            "Dye Shop",
            new String[]{"Aggie", "Ali the dyer", "Betty", "Guinevere's Dyes", "Lletya Seamstress",
                    "Dyes to Die For"}
    ),

    SHOP_FARMING_SHOP(
            "Farming Shop",
            new String[]{"Agelus' Farm Shop", "Alice's Farming shop", "Allanna's Farming Shop",
                    "Amelia's Seed Shop", "Branwen's Farming Shop", "Draynor Seed Market",
                    "Elder Reggle's Farming Shop", "Kastori Farming Supplies", "Leprechaun Larry's Farming Supplies",
                    "Richard's Farming shop", "Sarah's Farming shop", "Vanessa's Farming shop",
                    "Vannah's Farming Stall"}
    ),

    SHOP_FISHING_SHOP(
            "Fishing Shop",
            new String[]{"Gerrant's Fishy Business", "Elder Krill's Fishing Stall", "Etceteria Fish",
                    "Fernahei's Fishing Hut", "Fishing Guild Shop", "Flosi's Fishmongers",
                    "Fremennik Fish Monger", "Harry's Fishing Shop", "Ishmael's Fish He Sells",
                    "Island Fishmonger", "Lovecraft's Tackle", "Mairin's Market",
                    "Picaria's Fishing Shop", "Port Roberts Fish Stall", "Sulisal's Superb Fishing Store",
                    "Two Feet Charley's Fish Shop", "Tynan's Fishing Supplies", "Warrens Fish Monger"}
    ),

    SHOP_FOOD_SHOP(
            "Food Shop",
            new String[]{"Ardougne Baker's Stall", "Darkmeyer Meat Shop", "Frankie's Fishing Emporium",
                    "Gianne's Restaurant", "Keepa Kettilon's Store", "Keldagrim's Best Bread",
                    "Kenelme's Wares", "Kourend Castle Baker's Stall", "Miscellanian Food Shop",
                    "Nathifa's Bake Stall", "Prifddinas Foodstuffs", "Pie Shop",
                    "Rufus' Meat Emporium", "Solihib's Food Stall", "The Shrimp and Parrot",
                    "Tony's Pizza Bases", "Warrior Guild Food Shop", "Wydin's Food Store",
                    "Yarnio's Baked Goods"}
    ),

    SHOP_FUR_TRADER(
            "Fur Trader",
            new String[]{"Baraek", "Fur trader", "Fur Merchant", "Pellem", "Perdie",
                    "Prifddinas' Seamstress"}
    ),

    SHOP_GEM_SHOP(
            "Gem Shop",
            new String[]{"Gem Trader", "Herquin's Gems", "Ardougne Gem Stall", "Fortis Gem Stall",
                    "Green Gemstone Gems", "Irksol", "Kourend Castle Gem Stall", "Port Roberts Gem Stall",
                    "Prifddinas Gem Stall", "Toci's Gem Store", "TzHaar-Hur-Lek's Ore and Gem Store",
                    "TzHaar-Hur-Rin's Ore and Gem Store"}
    ),

    SHOP_GENERAL_STORE(
            "General Store",
            new String[]{"Aemad's Adventuring Supplies", "Al Kharid General Store", "Aldarin General Store",
                    "Arhein's General Goods", "Arnold's Eclectic Supplies", "Aurel's Supplies",
                    "Bandit Duty Free", "Bolkoy's Village Shop", "Burthorpe Supplies",
                    "Cam Torum General Store", "Dal's General Ogre Supplies", "Darkmeyer General Store",
                    "Dorgesh-Kaan General Supplies", "Dwarven shopping store", "Edgeville General Store",
                    "Falador General Store", "Fortis General Store", "Fossil Island General Store",
                    "General Store", "Gunslik's Assorted Items", "Ifaba's General Store",
                    "Jennifer's General Supplies", "Jiminua's Jungle Store", "Karamja General Store",
                    "Khazard General Store", "Leenz's General Supplies", "Legends Guild General Store",
                    "Little Munty's Little Shop", "Little Shop of Horace", "Lletya General Store",
                    "Lumbridge General Store", "Martin Thwait's Lost and Found", "Miscellanian General Store",
                    "Moon Clan General Store", "Nardah General Store", "Obli's General Store",
                    "Outer Fortis General Store", "Pollnivneach general store", "Port Phasmatys General Store",
                    "Port Roberts General Store", "Prifddinas General Store", "Quartermaster's Stores",
                    "Quetzacalli Gorge General Store", "Rasolo the Wandering Merchant", "Razmire General Store",
                    "Regath's Wares", "Rimmington General Store", "Salvager Overlook General Store",
                    "Sigmund the Merchant", "Sunset Coast General Store", "The Lighthouse Store",
                    "Trader Stan's Trading Post", "Varrock General Store", "Void Knight General Store",
                    "Warrens General Store", "West Ardougne General Store", "Zanaris General Store"}
    ),


    SHOP_HERBLORE_SHOP(
            "Herblore Shop",
            new String[]{"Elder Raley's Herblore Stall", "Frincos' Fabulous Herb Store", "Grud's Herblore Stall",
                    "Huita's Herbal Supplies", "Jatix's Herblore Shop", "Myths' Guild Herbalist",
                    "Prifddinas Herbal Supplies", "Zahur"}
    ),

    SHOP_HUNTER_SHOP(
            "Hunter Shop",
            new String[]{"Aleck's Hunter Emporium", "Elder Strom's Hunting Stall", "Nardah Hunter Shop",
                    "Imia's Supplies"}
    ),

    SHOP_KEBAB_SELLER(
            "Kebab Seller",
            new String[]{"Karim", "Ali the Kebab seller", "Kjut's Kebabs", "Emelio's Kebab Shop"}
    ),

    SHOP_MAGIC_SHOP(
            "Magic Shop",
            new String[]{"Aubury's Rune Shop", "Betty's Magic Emporium", "Ali's Discount Wares",
                    "Amlodd's Magical Supplies", "Baba Yaga's Magic Shop", "Battle Runes",
                    "Lundail's Arena-side Rune Shop", "Magic Guild Store", "Regath's Wares",
                    "Thyria's Wares", "TzHaar-Mej-Roh's Rune Store", "Tutab's Magical Market",
                    "Void Knight Magic Store", "The Runic Emporium", "Tal Teklan Rune Shop"}
    ),

    SHOP_MINING_SHOP(
            "Mining Shop",
            new String[]{"Drogo's Mining Emporium", "Nurmof's Pickaxe Shop", "Yarsul's Prodigious Pickaxes",
                    "Deepfin Point Ore Exchange", "Gwyn's Mining Emporium", "Mistrock Mining Supplies",
                    "Pickaxe-Is-Mine", "Port Roberts Ore Stall", "Thirus Urkar's Fine Dynamite Store",
                    "Tizoro's Pickaxes", "Toothy's Pickaxes", "TzHaar-Hur-Rin's Ore and Gem Store"}
    ),

    SHOP_PLATEBODY_SHOP(
            "Platebody Shop",
            new String[]{"Horvik's Armour Shop", "Aneirin's Armour", "Armour Shop",
                    "Myths' Guild Armoury", "TzHaar-Hur-Zal's Equipment Store",
                    "Zenesha's Plate Mail Body Shop"}
    ),

    SHOP_SILK_SHOP(
            "Silk Shop",
            new String[]{"Silk trader", "Anwen", "Doria", "Silk merchant", "Vermundi's Clothes Stall"}
    ),

    SHOP_SILVER_SHOP(
            "Silver Shop",
            new String[]{"Ardougne Silver Stall", "Silver Cog Silver Stall", "Port Roberts Silver Stall",
                    "Prifddinas Silver Stall"}
    ),

    SHOP_SPICE_SHOP(
            "Spice Shop",
            new String[]{"Ardougne Spice Stall", "Port Roberts Spice Stall", "Prifddinas Spice Stall",
                    "The Spice is Right"}
    ),

    SHOP_STAFF_SHOP(
            "Staff Shop",
            new String[]{"Zaff's Superior Staffs", "Elgan's Exceptional Staffs", "Filamina's Wares",
                    "Sebamo's Sublime Staffs"}
    ),

    SHOP_SWORD_SHOP(
            "Sword Shop",
            new String[]{"Varrock Swordshop", "Armoury", "Blades by Urbi", "Briget's Weapons",
                    "Fortis Blacksmith", "Gaius' Two-Handed Shop", "Gulluck and Sons",
                    "Iorwerth's Arms", "Jukat", "Myths' Guild Weaponry", "Nardok's Bone Weapons",
                    "Quality Weapons Shop", "TzHaar-Hur-Tel's Equipment Store", "Vigr's Warhammers",
                    "Warrior Guild Armoury"}
    ),

    SHOP_VEGETABLE_SHOP(
            "Vegetable Shop",
            new String[]{"Greengrocer of Miscellania", "Island Greengrocer", "Logava Gricoller's Cooking Supplies",
                    "Port Roberts Veg Stall"}
    ),


    SHOP_WINE_TRADER(
            "Wine Trader",
            new String[]{"Wine Shop", "Moonrise Wines"}
    );

    private final String displayName;
    private final String[] qualifyingShops;

    ShopCategory(String displayName, String[] qualifyingShops) {
        this.displayName = displayName;
        this.qualifyingShops = Arrays.stream(qualifyingShops)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean matchesShopName(String shopName) {
        shopName = shopName.toLowerCase();
        for (String keyword : qualifyingShops) {
            if (shopName.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}