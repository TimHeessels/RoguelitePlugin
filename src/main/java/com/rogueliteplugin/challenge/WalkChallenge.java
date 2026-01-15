package com.rogueliteplugin.challenge;

import net.runelite.api.coords.WorldPoint;

public class WalkChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final int lowAmount;
    private final int highAmount;

    //Runtime
    private WorldPoint lastLocation;

    public WalkChallenge(String id, String name, int lowAmount, int highAmount, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
    }

    @Override
    public ChallengeType getType() {
        return ChallengeType.Unique;
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

    public int countMovement(WorldPoint current)
    {
        if (lastLocation == null)
        {
            lastLocation = current;
            return 0;
        }

        int dx = Math.abs(current.getX() - lastLocation.getX());
        int dy = Math.abs(current.getY() - lastLocation.getY());
        int dz = Math.abs(current.getPlane() - lastLocation.getPlane());

        lastLocation = current;

        // Plane change or teleport → ignore
        if (dz > 0 || dx > 2 || dy > 2)
        {
            return 0;
        }

        return dx + dy;
    }
}
