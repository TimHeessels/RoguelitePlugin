package com.coinboundplugin.pack;

import com.coinboundplugin.CoinboundPlugin;
import com.coinboundplugin.unlocks.Unlock;


public class UnlockPackOption implements PackOption {
    private final Unlock unlock;

    public UnlockPackOption(Unlock unlock) {
        this.unlock = unlock;
    }

    public Unlock getUnlock() {
        return unlock;
    }

    @Override
    public String getDisplayName() {
        return unlock.getDisplayName();
    }
    @Override
    public String getDisplayType() {
        return unlock.getType().toString();
    }
    @Override
    public String getDescription() {
        return unlock.getDescription();
    }

    @Override
    public void onChosen(CoinboundPlugin plugin) {
        plugin.unlock(unlock.getId());
    }
}
