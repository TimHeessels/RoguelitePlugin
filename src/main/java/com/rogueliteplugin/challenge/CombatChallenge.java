package com.rogueliteplugin.challenge;

import com.rogueliteplugin.requirements.AppearRequirement;
import net.runelite.api.NPCComposition;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.game.ItemManager;

import java.util.List;

public class CombatChallenge implements Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final int lowAmount;
    private final int highAmount;
    private final EnemyGroup enemyGroup;
    private final List<AppearRequirement> requirements;

    public CombatChallenge(String id, String name, int lowAmount, int highAmount,
                           EnemyGroup enemyGroup, String description, List<AppearRequirement> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowAmount = lowAmount;
        this.highAmount = highAmount;
        this.enemyGroup = enemyGroup;
        this.requirements = requirements;
    }

    @Override
    public ChallengeType getType() {
        return ChallengeType.Combat;
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
    public boolean handleNpcLoot(
            ServerNpcLoot event,
            ChallengeState state,
            ItemManager itemManager) {

        NPCComposition npc = event.getComposition();
        return (enemyGroup.matches(npc));
    }

    @Override
    public List<AppearRequirement> getRequirements()
    {
        return requirements;
    }

    public enum EnemyGroup {
        GOBLINS("Goblin"),
        COWS("Cow"),
        CHICKENS("Chicken"),
        SPIDERS("Spider");

        private final String token;

        EnemyGroup(String token)
        {
            this.token = token;
        }

        public boolean matches(NPCComposition npc)
        {
            String name = npc.getName();
            System.out.println("Killed "+name + ", needed to kill: " + token);
            return name != null && name.contains(token);
        }
    }
}
