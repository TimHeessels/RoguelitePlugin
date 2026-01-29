package com.coinboundplugin.pack;

import com.coinboundplugin.CoinboundPlugin;

public interface PackOption {
    String getDisplayName();

    String getDisplayType();

    String getDescription();

    void onChosen(CoinboundPlugin plugin);
}