package com.rogueliteplugin.unlocks;

import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class SkillUnlock implements Unlock
{
    private final Skill skill;
    private final ImageUnlockIcon icon;

    public SkillUnlock(Skill skill, SkillIconManager skillIconManager)
    {
        this.skill = skill;

        BufferedImage img = skillIconManager.getSkillImage(skill);

        if (img != null)
        {
            this.icon = new ImageUnlockIcon(new ImageIcon(img));
        }
        else
        {
            this.icon = null;
        }
    }

    @Override
    public UnlockType getType()
    {
        return UnlockType.SKILL;
    }

    @Override
    public String getId()
    {
        return "SKILL_" + skill.name();
    }

    @Override
    public String getDisplayName()
    {
        return skill.getName();
    }

    @Override
    public UnlockIcon getIcon()
    {
        return icon;
    }

    @Override
    public String getDescription()
    {
        return "Unlocks the " + skill.getName() + " skill.";
    }
}
