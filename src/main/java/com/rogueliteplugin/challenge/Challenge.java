package com.rogueliteplugin.challenge;
import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.api.events.StatChanged;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.game.ItemManager;

import java.util.List;
import java.util.Set;

public interface Challenge
{
    ChallengeType getType();
    String getId();
    String getDisplayName();
    String getDescription();
    Integer getLowAmount();
    Integer getHighAmount();

    default boolean isValidWithUnlocks(Set<String> unlockedIds)
    {
        for (AppearRequirement req : getRequirements())
        {
            if (!req.isMet(unlockedIds))
            {
                return false;
            }
        }
        return true;
    }

    default List<AppearRequirement> getRequirements()
    {
        return List.of(); // no requirements by default
    }

    // Optional event hooks
    default boolean handleNpcLoot(
            ServerNpcLoot event,
            ChallengeState state,
            ItemManager itemManager)
    {
        return false;
    }

    default boolean handleXp(
            StatChanged event,
            ChallengeState state)
    {
        return false;
    }
}
