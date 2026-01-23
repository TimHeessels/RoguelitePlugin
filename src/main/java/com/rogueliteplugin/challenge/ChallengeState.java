package com.rogueliteplugin.challenge;

public class ChallengeState {
    private final Challenge challenge;
    private int progress;
    private final int goal;

    public ChallengeState(Challenge challenge, int goal) {
        this.challenge = challenge;
        this.goal = goal;
        this.progress = 0;
    }

    public void increment(int amount) {
        progress = Math.min(goal, progress + amount);
    }

    public void CompleteGoal() {
        progress = goal;
    }

    public boolean isComplete() {
        return progress >= goal;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public int getProgress() {
        return progress;
    }

    public int getGoal() {
        return goal;
    }
}

