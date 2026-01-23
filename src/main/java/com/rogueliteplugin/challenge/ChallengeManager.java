package com.rogueliteplugin.challenge;

import com.rogueliteplugin.RogueliteConfig;
import net.runelite.api.Client;

import java.text.NumberFormat;
import java.util.Locale;

public class ChallengeManager {
    private ChallengeState current;
    private RogueliteConfig clientConfig;
    private Client runeliteClient;

    public void startChallenge(Challenge challenge, int goal) {
        this.current = new ChallengeState(challenge, goal);
        saveToConfig();
    }

    public String getChallengeFormatted() {
        return current.getChallenge().getDisplayName().replace("$", NumberFormat
                .getInstance(new Locale("nl", "NL"))
                .format(current.getGoal()));
    }

    public ChallengeState getCurrent() {
        return current;
    }

    public boolean hasActiveChallenge() {
        return current != null && !current.isComplete();
    }

    public void increment(int amount) {
        if (current.isComplete())
            return;
        if (current != null) {
            current.increment(amount);
        }
        if (current.getProgress() >= current.getGoal())
            CompleteGoal();
        else
            saveToConfig();
    }

    public void CompleteGoal() {
        current.CompleteGoal();
        runeliteClient.playSoundEffect(3283);
        saveToConfig();
    }

    //TODO: Check if need to implement
    public void clear() {
        current = null;
        saveToConfig();
    }

    public void saveToConfig() {
        if (current == null) {
            clientConfig.currentChallengeID("");
            return;
        }

        clientConfig.currentChallengeID(current.getChallenge().getId());
        clientConfig.currentChallengeProgress(current.getProgress());
        clientConfig.currentChallengeGoal(current.getGoal());
    }

    public void loadFromConfig(RogueliteConfig config, Client client, ChallengeRegistry registry) {
        clientConfig = config;
        runeliteClient = client;
        String id = config.currentChallengeID();

        if (id == null || id.isEmpty()) {
            return;
        }

        Challenge challenge = registry.get(id);
        if (challenge == null) {
            return;
        }

        ChallengeState state = new ChallengeState(challenge, config.currentChallengeGoal());
        state.increment(config.currentChallengeProgress());

        this.current = state;
    }
}
