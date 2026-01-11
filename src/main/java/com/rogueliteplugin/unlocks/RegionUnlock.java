package com.rogueliteplugin.unlocks;

import javax.swing.Icon;
import java.util.Set;

public class RegionUnlock implements Unlock
{
    private final String id;
    private final String name;
    private final Set<Integer> regionIds;
    private final UnlockIcon icon;
    private final String description;

    public RegionUnlock(
            String id,
            String name,
            Set<Integer> regionIds,
            UnlockIcon icon,
            String description
    )
    {
        this.id = id;
        this.name = name;
        this.regionIds = regionIds;
        this.icon = icon;
        this.description = description;
    }

    public boolean containsRegion(int regionId)
    {
        return regionIds.contains(regionId);
    }

    @Override public UnlockType getType() { return UnlockType.REGION; }
    @Override public String getId() { return id; }
    @Override public String getDisplayName() { return name; }
    @Override public UnlockIcon getIcon() { return icon; }
    @Override public String getDescription() { return description; }
}
