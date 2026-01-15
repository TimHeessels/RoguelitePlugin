package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.challenge.ChallengeRegistry;
import com.rogueliteplugin.unlocks.UnlockRegistry;

import java.util.Set;

public class RequiresUnlockRequirement implements AppearRequirement
{
    private final String requiredUnlockId;
    private final UnlockRegistry unlockRegistry;

    public RequiresUnlockRequirement(String requiredUnlockId, UnlockRegistry unlockRegistry)
    {
        this.requiredUnlockId = requiredUnlockId;
        this.unlockRegistry = unlockRegistry;
    }

    @Override
    public boolean isMet(Set<String> unlockedIds)
    {
        return unlockedIds.contains(requiredUnlockId);
    }

    @Override
    public String getRequiredUnlockTitle()
    {
        String displayName = requiredUnlockId;
        if (unlockRegistry != null) {
            var unlock = unlockRegistry.getAll().stream()
                    .filter(u -> u.getId().equals(requiredUnlockId))
                    .findFirst();
            if (unlock.isPresent()) {
                displayName = unlock.get().getDisplayName();
            }
        }
        return "Requires " + displayName;
    }
}
