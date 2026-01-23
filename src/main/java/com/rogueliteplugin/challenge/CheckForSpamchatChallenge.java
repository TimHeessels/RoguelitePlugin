package com.rogueliteplugin.challenge;

import com.rogueliteplugin.data.ChallengeType;
import com.rogueliteplugin.requirements.AppearRequirement;

import java.util.List;

public class CheckForSpamchatChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final int lowAmount;
    private final int highAmount;
    private final List<AppearRequirement> requirements;
    private final String spamMessage;
    private final ChallengeType challengeType;

    public CheckForSpamchatChallenge(String id, String name, int lowAmount, int highAmount, String description, String spamMessage, ChallengeType challengeType, List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
        this.requirements = requirements;
        this.spamMessage = spamMessage;
        this.challengeType = challengeType;
    }

    @Override
    public ChallengeType getType() {
        return challengeType;
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

    public String getSpamMessage() {
        return spamMessage;
    }
}
