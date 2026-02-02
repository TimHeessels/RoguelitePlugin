package com.coinlockedplugin.requirements;

import com.coinlockedplugin.CoinlockedPlugin;
import net.runelite.api.Skill;

import java.util.Set;

public class CombatRequirement implements AppearRequirement {
    private final int requiredLevel;

    public CombatRequirement(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    @Override
    public boolean isMet(CoinlockedPlugin plugin, Set<String> unlockedIds) {
        if (plugin.getClient().getLocalPlayer().getCombatLevel() < requiredLevel)
            return false;
        return plugin.isSkillBracketUnlocked(Skill.ATTACK) || plugin.isSkillBracketUnlocked(Skill.STRENGTH) || plugin.isSkillBracketUnlocked(Skill.DEFENCE) || plugin.isSkillBracketUnlocked(Skill.RANGED) || plugin.isSkillBracketUnlocked(Skill.MAGIC);
    }

    @Override
    public String getRequiredUnlockTitle() {
        return "Combat level " + requiredLevel + " or higher.";
    }
}