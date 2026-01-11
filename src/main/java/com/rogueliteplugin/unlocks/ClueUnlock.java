package com.rogueliteplugin.unlocks;

import javax.swing.*;

public class ClueUnlock implements Unlock
{
    private final String id;
    private final String name;
    private final Icon icon;
    private final String description;

    public ClueUnlock(String id, String name, Icon icon, String description)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    @Override
    public UnlockType getType()
    {
        return UnlockType.CLUE;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getDisplayName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public UnlockIcon getIcon()
    {
        return new ImageUnlockIcon(icon);
    }
}
