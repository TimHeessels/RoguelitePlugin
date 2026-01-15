package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.challenge.ChallengeContext;

import java.util.Set;

public class CombatLevelRequirement implements AppearRequirement {
    private final int requiredLevel;

    public CombatLevelRequirement(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public boolean isMet(Set<String> unlockedIds) {
        // Skill levels are NOT unlock-based
        return false;
    }

    @Override
    public boolean isMet(RoguelitePlugin plugin) {
        boolean hasCombatLevel = plugin.getClient().getLocalPlayer().getCombatLevel() >= requiredLevel;
        boolean hasAnyFormOfCombatUnlocked = plugin.isUnlocked("SKILL_ATTACK") || plugin.isUnlocked("SKILL_STRENGTH") || plugin.isUnlocked("SKILL_DEFENCE") || plugin.isUnlocked("SKILL_RANGED") || plugin.isUnlocked("SKILL_MAGIC");
        return hasCombatLevel && hasAnyFormOfCombatUnlocked;
    }

    @Override
    public String getRequiredUnlockTitle() {
        return "Combat level " + requiredLevel + " or higher.";
    }
}

