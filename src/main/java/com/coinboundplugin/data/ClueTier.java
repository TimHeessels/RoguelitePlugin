package com.coinboundplugin.data;

public enum ClueTier
{
    BEGINNER("Reward casket (Beginner)"),
    EASY("Reward casket (Easy)"),
    MEDIUM("Reward casket (Medium)"),
    HARD("Reward casket (Hard)"),
    ELITE("Reward casket (Elite)"),
    MASTER("Reward casket (Master)");

    private final String displayName;

    ClueTier(String displayName)
    {
        this.displayName = displayName;
    }

    public String getId()
    {
        return name() + "_CLUES";
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getIconPath()
    {
        return "clues/" + name().toLowerCase() + ".png";
    }

    public ClueTier previous()
    {
        int ord = ordinal();
        return ord == 0 ? null : values()[ord - 1];
    }
}

