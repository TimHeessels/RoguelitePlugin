package com.rogueliteplugin.challenge;

import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;

import java.util.List;

public class DropSpecificChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final Integer itemIDToGet;
    private final List<AppearRequirement> requirements;

    public DropSpecificChallenge(String id, Integer itemIDToGet, String name, String description, List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemIDToGet = itemIDToGet;
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
        return 1;
    } //No value, return default of 1

    @Override
    public Integer getHighAmount() {
        return 1;
    }//No value, return default of 1

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
            if (item.getId() == itemIDToGet) {
                return true;
            }
        }
        return false;
    }
}
