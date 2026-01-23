package com.rogueliteplugin.data;

public enum ClueTier
{
    BEGINNER("Beginner clues"),
    EASY("Easy clues"),
    MEDIUM("Medium clues"),
    HARD("Hard clues"),
    ELITE("Elite clues"),
    MASTER("Master clues");

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

