package com.rogueliteplugin;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NewItemDetection {

    private RoguelitePlugin plugin;

    private Set<Integer> seenItems;
    private Set<Integer> lastInventory = new HashSet<>();

    @Inject
    private ItemManager itemManager;

    public void setPlugin(RoguelitePlugin plugin) {
        this.plugin = plugin;
        this.seenItems = loadSeenItems();
    }

    private Set<Integer> loadSeenItems() {
        Set<Integer> set = new HashSet<>();

        String raw = plugin.getSeenItemIds();
        if (!raw.isEmpty()) {
            for (String s : raw.split(",")) {
                try {
                    set.add(Integer.parseInt(s));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return set;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
            return;
        }

        ItemContainer container = event.getItemContainer();
        if (container == null) {
            return;
        }

        Set<Integer> currentInventory = new HashSet<>();

        for (Item item : container.getItems()) {
            if (item.getId() > 0) {
                currentInventory.add(item.getId());
            }
        }

        // Items that appeared since last tick
        Set<Integer> newlyAdded = new HashSet<>(currentInventory);
        newlyAdded.removeAll(lastInventory);

        for (int itemId : newlyAdded) {
            if (seenItems.add(itemId) && itemId != plugin.replaceItemID) {
                persistSeenItems();
                onFirstTimePickup(itemId);
            }
        }

        lastInventory = currentInventory;
    }

    private boolean tileHasUncollectedItems(Tile tile) {
        ItemLayer layer = tile.getItemLayer();
        if (layer == null) {
            return false;
        }

        for (TileItem item = (TileItem) layer.getTop(); item != null; item = (TileItem) item.getNext()) {
            if (!seenItems.contains(item.getId())) {
                return true;
            }
        }

        return false;
    }


    private void persistSeenItems() {
        String value = seenItems.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        plugin.setSeenItemsIds(value);
    }

    private void onFirstTimePickup(int itemId) {
        ItemComposition itemComp = itemManager.getItemComposition(itemId);
        String name = itemComp.getName();

        int gePrice = itemManager.getItemPrice(itemId);
        if (gePrice <= 0)
            gePrice = itemComp.getPrice();

        plugin.newItemAcquired(name, getPointsForItem(gePrice));
    }

    private int getPointsForItem(int gePrice) {
        if (gePrice >= 10_000_000) return 100;
        if (gePrice >= 1_000_000) return 50;
        if (gePrice >= 100_000) return 20;
        if (gePrice >= 10_000) return 10;
        if (gePrice >= 1_000) return 5;
        return 1;
    }
}
