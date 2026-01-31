package com.coinboundplugin.unlocks;

import com.coinboundplugin.data.UnlockType;
import com.coinboundplugin.requirements.AppearRequirement;

import javax.swing.*;
import java.util.List;

public class ResorationUnlock implements Unlock {
    private final String id;
    private final Icon icon;
    private final String displayName;
    private final String description;
    private final List<AppearRequirement> requirements;

    public ResorationUnlock(String id, String displayName, Icon icon, String description,List<AppearRequirement> requirements) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.requirements = requirements;
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
    public List<AppearRequirement> getRequirements()
    {
        return requirements;
    }
}
