package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.requirements.AppearRequirement;

import javax.swing.*;
import java.util.List;

public class QuestUnlock implements Unlock {
    private final String id;
    private final String name;
    private final Icon icon;
    private final String description;
    private final List<AppearRequirement> requirements;

    public QuestUnlock(String id, String name, Icon icon, String description,
                       List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.requirements = requirements;
    }

    @Override
    public UnlockType getType() {
        return UnlockType.Quests;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public UnlockIcon getIcon() {
        return new ImageUnlockIcon(icon);
    }

    @Override
    public List<AppearRequirement> getRequirements() {
        return requirements;
    }
}
