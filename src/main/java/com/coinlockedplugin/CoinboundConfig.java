package com.coinlockedplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("coinlockedplugin")
public interface CoinboundConfig extends Config {
    String GROUP = "coinlockedplugin";

    //Option to toggle overlay visibility
    @ConfigItem(
            keyName = "showOverlay",
            name = "Show Coinlocked Overlay",
            description = "Toggles the visibility of the Coinlocked Overlay"
    )
    default boolean showOverlay() {
        return true;
    }

    //Option to hide card panel and buttons
    @ConfigItem(
            keyName = "showCardMenus",
            name = "Show Card menus",
            description = "Show/hide the open pack button and card picker"
    )
    default boolean showCardMenus() {
        return true;
    }
}
