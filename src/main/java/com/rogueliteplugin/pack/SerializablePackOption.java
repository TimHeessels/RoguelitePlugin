package com.rogueliteplugin.pack;

public class SerializablePackOption {
    private String unlockId;
    private String challengeId;
    private int balancedAmount;

    public SerializablePackOption(String unlockId, String challengeId, int balancedAmount) {
        this.unlockId = unlockId;
        this.challengeId = challengeId;
        this.balancedAmount = balancedAmount;
    }

    public String getUnlockId() { return unlockId; }
    public String getChallengeId() { return challengeId; }
    public int getBalancedAmount() { return balancedAmount; }
}