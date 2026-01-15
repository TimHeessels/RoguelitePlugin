package com.rogueliteplugin.pack;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.challenge.Challenge;
import com.rogueliteplugin.unlocks.Unlock;


public class UnlockPackOption implements PackOption {
    private final Unlock unlock;
    private final Challenge challenge;

    public UnlockPackOption(Unlock unlock, Challenge challenge) {
        this.unlock = unlock;
        this.challenge = challenge;
    }

    public Unlock getUnlock() {
        return unlock;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Integer getChallengeLowAmount() {
        return challenge.getLowAmount();
    }

    public Integer getChallengeHighAmount() {
        return challenge.getHighAmount();
    }

    public String getChallengeType() {
        return challenge.getType().toString();
    }

    @Override
    public String getDisplayName() {
        return unlock.getDisplayName();
    }

    @Override
    public String getChallengeName() {
        return challenge.getDisplayName();
    }

    @Override
    public String getDisplayType() {
        return unlock.getType().toString();
    }

    @Override
    public void onChosen(RoguelitePlugin plugin) {
        plugin.setActiveChallenge(challenge);
        plugin.unlock(unlock.getId());
    }
}
