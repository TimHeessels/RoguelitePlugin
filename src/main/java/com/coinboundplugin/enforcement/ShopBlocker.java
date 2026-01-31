package com.coinboundplugin.enforcement;

import com.coinboundplugin.data.ShopCategory;
import com.google.inject.Inject;
import com.coinboundplugin.CoinboundPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
public class ShopBlocker {
    private static final int SHOP_MAIN_INIT = 1074; // verify if needed

    @Inject
    private Client client;

    @Inject
    private CoinboundPlugin plugin;

    @Subscribe
    public void onScriptPreFired(ScriptPreFired event) {
        if (event.getScriptId() != SHOP_MAIN_INIT) {
            return;
        }

        Object[] args = event.getScriptEvent().getArguments();
        if (args == null || args.length < 3) {
            return;
        }

        String rawName = (String) args[2];
        if (rawName == null) {
            return;
        }

        String shopName = normalizeShopName(rawName);
        ShopCategory category = classifyByShopName(shopName);

        if (category == null) {
            // Unknown shop → allow
            plugin.ShowPluginChat("<col=ff0000><b>Possibly locked!</b></col> Shop category not recognized by plugin. Check for yourself if this is unlocked.", -1);
            return;
        }

        if (!plugin.isUnlocked(category.name())) {
            blockShop(category);
        }
    }

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------

    private ShopCategory classifyByShopName(String shopName) {
        for (ShopCategory category : ShopCategory.values()) {
            if (category.matchesShopName(shopName)) {
                return category;
            }
        }
        return null;
    }

    private String normalizeShopName(String name) {
        return name.toLowerCase()
                .replaceAll("<.*?>", "") // strip tags just in case
                .trim();
    }

    private void blockShop(ShopCategory category) {
        plugin.ShowPluginChat("<col=ff0000><b>"+category.getDisplayName()+" category is locked!</b></col> You have not unlocked access to this shop yet.", 2394);

    }
}
