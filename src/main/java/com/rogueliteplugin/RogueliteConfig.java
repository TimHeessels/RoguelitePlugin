package com.rogueliteplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("rogueliteplugin")
public interface RogueliteConfig extends Config {
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
            keyName = "currentPoints",
            name = "current item points",
            description = "How many points you gained from getting unique items"
    )
    default int currentPoints() {
        return 0;
    }

    @ConfigItem(
            keyName = "currentPoints",
            name = "current item points",
            description = "How many points you gained from getting unique items"
    )
    void currentPoints(int value);

    @ConfigItem(
            keyName = "unlockedIds",
            name = "Unlocked Elements",
            description = "Internal unlock tracking"
    )
    default String unlockedIds() {
        return "";
    }

    @ConfigItem(
            keyName = "seenItemIds",
            name = "Seen item IDs",
            description = "Comma-separated list of item IDs that have been picked up"
    )
    default String seenItemIds()
    {
        return "";
    }

    @ConfigItem(
            keyName = "seenItemIds",
            name = "Seen item IDs",
            description = "Comma-separated list of item IDs that have been picked up"
    )
    void seenItemIds(String value);

}
