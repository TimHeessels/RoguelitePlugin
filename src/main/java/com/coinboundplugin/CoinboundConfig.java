package com.coinboundplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("coinboundplugin")
public interface CoinboundConfig extends Config {
    String GROUP = "rogueliteplugin";
    
    @ConfigItem(
            keyName = "currentPackOptions",
            name = "Current Pack Options",
            description = "JSON representation of current pack options"
    )
    default String currentPackOptions() {
        return "[]";
    }

    @ConfigItem(
            keyName = "currentPackOptions",
            name = "",
            description = ""
    )
    void currentPackOptions(String value);

    @ConfigItem(
            keyName = "packChoiceState",
            name = "Pack Choice State",
            description = "Current state of pack selection"
    )
    default String packChoiceState() {
        return "NONE";
    }

    @ConfigItem(
            keyName = "packChoiceState",
            name = "",
            description = ""
    )
    void packChoiceState(String value);

    @ConfigItem(
            keyName = "illegalXPGained",
            name = "XP gained in blocked skills",
            description = "Total XP you gained in skills you did not have unlocked",
            hidden = true
    )
    default long illegalXPGained() {
        return 0L;
    }

    @ConfigItem(
            keyName = "illegalXPGained",
            name = "XP gained in blocked skills",
            description = "Total XP you gained in skills you did not have unlocked",
            hidden = true
    )
    void illegalXPGained(long value);

    @ConfigItem(
            keyName = "packsBought",
            name = "how many packs you've bought",
            description = "How many packs you've bought so far"
    )
    default int packsBought() {
        return 0;
    }

    @ConfigItem(
            keyName = "packsBought",
            name = "how many packs you've bought",
            description = "How many packs you've bought so far"
    )
    void packsBought(int value);

    @ConfigItem(
            keyName = "unlockedIds",
            name = "Unlocked Elements",
            description = "Internal unlock tracking"
    )
    default String unlockedIds() {
        return "";
    }

    @ConfigItem(
            keyName = "peakWealth",
            name = "Peak wealth",
            description = "The max amount of coins you had in your inventory at once point"
    )
    default int peakWealth()
    {
        return 0;
    }

    @ConfigItem(
            keyName = "peakWealth",
            name = "Peak wealth",
            description = "The max amount of coins you had in your inventory at once point"
    )
    void peakWealth(long value);


    @ConfigItem(
            keyName = "setupStage",
            name = "Setup stage",
            description = "The current stage of setup, used on game mode start"
    )
    default String setupStage()
    {
        return "";
    }

    @ConfigItem(
            keyName = "setupStage",
            name = "Setup stage",
            description = "The current stage of setup, used on game mode start"
    )
    void setupStage(String value);
}
