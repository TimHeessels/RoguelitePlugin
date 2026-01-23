package com.rogueliteplugin.challenge;

import com.rogueliteplugin.data.ChallengeType;
import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;

import java.util.List;

public class DropValueChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final int lowAmount;
    private final int highAmount;
    private final List<AppearRequirement> requirements;

    public DropValueChallenge(String id, String name, int lowAmount, int highAmount, String description, List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
        this.requirements = requirements;
    }

    @Override
    public ChallengeType getType() {
        return ChallengeType.Drops;
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
    public List<AppearRequirement> getRequirements()
    {
        return requirements;
    }

    @Override
    public boolean handleNpcLoot(
            ServerNpcLoot event,
            ChallengeState state,
            ItemManager itemManager) {
        for (ItemStack item : event.getItems()) {
            long value = (long) itemManager.getItemPrice(item.getId())
                    * item.getQuantity();

            if (value >= state.getGoal()) {
                return true;
            }
        }
        return false;
    }
}
