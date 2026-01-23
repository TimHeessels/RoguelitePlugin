package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SkillUnlock implements Unlock {
    private final Skill skill;
    private final ImageUnlockIcon icon;
    private final List<AppearRequirement> requirements;

    public SkillUnlock(Skill skill, SkillIconManager skillIconManager, List<AppearRequirement> requirements) {
        this.skill = skill;
        this.requirements = requirements;

        BufferedImage img = skillIconManager.getSkillImage(skill);

        if (img != null) {
            this.icon = new ImageUnlockIcon(new ImageIcon(img));
        } else {
            this.icon = null;
        }
    }

    @Override
    public UnlockType getType() {
        return UnlockType.Skills;
    }

    @Override
    public String getId() {
        return "SKILL_" + skill.name();
    }

    @Override
    public String getDisplayName() {
        return skill.getName();
    }

    @Override
    public UnlockIcon getIcon() {
        return icon;
    }

    @Override
    public String getDescription() {
        return "Unlocks the " + skill.getName() + " skill.";
    }

    @Override
    public List<AppearRequirement> getRequirements() {
        return requirements;
    }
}
