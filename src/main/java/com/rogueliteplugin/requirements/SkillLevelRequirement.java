package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.challenge.ChallengeContext;
import net.runelite.api.Experience;
import net.runelite.api.Skill;

import java.util.Set;

public class SkillLevelRequirement implements AppearRequirement {
    private final Skill skill;
    private final int level;

    public SkillLevelRequirement(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    public boolean isMet(Set<String> unlockedIds) {
        // Skill levels are NOT unlock-based
        return false;
    }

    @Override
    public boolean isMet(RoguelitePlugin plugin) {
        int xp = plugin.getClient().getSkillExperience(skill);
        return Experience.getLevelForXp(xp) >= level;
    }

    @Override
    public String getRequiredUnlockTitle() {
        return skill.getName() + " level " + level;
    }
}