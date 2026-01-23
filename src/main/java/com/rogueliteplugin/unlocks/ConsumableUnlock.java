package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.requirements.AppearRequirement;

import javax.swing.*;
import java.util.List;

public class ConsumableUnlock implements Unlock {
    private final String id;
    private final Icon icon;
    private final String displayName;
    private final String description;

    public ConsumableUnlock(String id, String displayName, Icon icon, String description) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }

    @Override
    public UnlockType getType() {
        return UnlockType.Consumable;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
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
        return List.of();
    }
}
