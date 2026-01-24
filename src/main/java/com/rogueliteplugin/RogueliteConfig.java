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
            keyName = "currentChallengeProgress",
            name = "Current goal progress",
            description = "The progress to the current challenge goal"
    )
    default int currentChallengeProgress() {
        return 0;
    }

    @ConfigItem(
            keyName = "currentChallengeProgress",
            name = "Current goal progress",
            description = "The progress to the current challenge goal"
    )
    void currentChallengeProgress(int value);

    @ConfigItem(
            keyName = "currentChallengeGoal",
            name = "Current goal goal",
            description = "The goal of the current challenge"
    )
    default int currentChallengeGoal() {
        return 0;
    }

    @ConfigItem(
            keyName = "currentChallengeGoal",
            name = "Current goal goal",
            description = "The goal of the current challenge"
    )
    void currentChallengeGoal(int value);

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
            keyName = "currentChallengeID",
            name = "Current challenge",
            description = "Which challenge ID is currently active?"
    )
    default String currentChallengeID() {
        return "";
    }

    @ConfigItem(
            keyName = "currentChallengeID",
            name = "Current challenge",
            description = "Which challenge ID is currently active?"
    )
    void currentChallengeID(String value);

    @ConfigItem(
            keyName = "unlockedIds",
            name = "Unlocked Elements",
            description = "Internal unlock tracking"
    )
    default String unlockedIds() {
        return "";
    }

    @ConfigItem(
            keyName = "rerollTokens",
            name = "Reroll Tokens",
            description = "Number of pack reroll tokens available"
    )
    default int rerollTokens()
    {
        return 0;
    }

    @ConfigItem(
            keyName = "rerollTokens",
            name = "",
            description = ""
    )
    void rerollTokens(int value);

    @ConfigItem(
            keyName = "skipTokens",
            name = "Skip Tokens",
            description = "Number of challenge skip tokens available"
    )
    default int skipTokens()
    {
        return 0;
    }

    @ConfigItem(
            keyName = "skipTokens",
            name = "",
            description = ""
    )
    void skipTokens(int value);
}
