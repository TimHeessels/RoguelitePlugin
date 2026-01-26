package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Experience;
import net.runelite.api.Skill;

import java.util.Set;

public class SkillLevelRequirement implements AppearRequirement {
    private final Skill skill;
    private final int minLevel;
    private final int maxLevel;

    public SkillLevelRequirement(Skill skill, int targetLevel) {
        this.skill = skill;
        this.minLevel = Math.max(targetLevel - 10, 1); //Can be assigned 10 levels below target so you have to level a bit
        this.maxLevel = Math.min(targetLevel + 30,99); //Can be assigned 30 levels above target to give easy task or variety
    }

    @Override
    public boolean isMet(RoguelitePlugin plugin, Set<String> unlockedIds) {
        if (!plugin.statsInitialized)
            return false;
        if (!plugin.isUnlocked("SKILL_" + skill))
            return false;
        int xp = plugin.getClient().getSkillExperience(skill);
        int level = Experience.getLevelForXp(xp);
        plugin.Debug("Checking SkillLevelRequirement for " + skill + ": level " + level + " (required between " + minLevel + " and " + maxLevel + ")");
        return level >= minLevel && level <= maxLevel;
    }

    @Override
    public String getRequiredUnlockTitle() {
        return skill.getName() + " level between " + minLevel + " and " + maxLevel;
    }
}