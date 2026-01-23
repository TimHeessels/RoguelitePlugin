package com.rogueliteplugin.challenge;

import com.rogueliteplugin.data.ChallengeType;

public class OneHpAfterDamageChallenge implements Challenge
{
    private final String id;
    private final String name;
    private final String description;

    public OneHpAfterDamageChallenge(
            String id,
            String name,
            String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public ChallengeType getType() {
        return ChallengeType.Unique;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getDisplayName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public Integer getLowAmount()
    {
        return 1;
    }

    @Override
    public Integer getHighAmount()
    {
        return 1;
    }
}