package com.rogueliteplugin.challenge;

import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;

import java.util.List;

public class SkillXPChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final int lowAmount;
    private final int highAmount;
    private final Skill skill;
    private final List<AppearRequirement> requirements;

    public SkillXPChallenge(String id, String name, int lowAmount, int highAmount, String description,
                            Skill skill, List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
        this.requirements = requirements;
        this.skill = skill;
    }

    @Override
    public ChallengeType getType() {
        return ChallengeType.Skill;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getLowAmount() {
        return lowAmount;
    }

    @Override
    public Integer getHighAmount() {
        return highAmount;
    }

    @Override
    public List<AppearRequirement> getRequirements() {
        return requirements;
    }

    @Override
    public boolean handleXp(StatChanged event, ChallengeState state)
    {
        return event.getSkill() == skill;
    }
}
