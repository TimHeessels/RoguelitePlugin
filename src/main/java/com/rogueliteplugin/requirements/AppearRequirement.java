package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;
import java.util.Set;

public interface AppearRequirement
{
    // Core check (used everywhere)
    boolean isMet(Set<String> unlockedIds);

    // Convenience bridge for UI / tooltips
    default boolean isMet(RoguelitePlugin plugin)
    {
        return isMet(plugin.getUnlockedIds());
    }

    String getRequiredUnlockTitle();
}