package com.rogueliteplugin.enforcement;

import com.google.inject.Inject;
import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.*;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import net.runelite.api.widgets.Widget;

import java.awt.*;
import java.util.Arrays;

public class InventorySlotPanelOverlay extends Overlay {
    private static final int TOTAL_ROWS = 7;
    private static final int INVENTORY_SIZE = 28;

    private final RoguelitePlugin plugin;
    private final Client client;

    @Inject
    private InventorySlotPanelOverlay(RoguelitePlugin plugin, Client client) {
        this.client = client;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(Overlay.PRIORITY_HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        renderHitboxOverlay(graphics);
        return null;
    }

    private void renderHitboxOverlay(Graphics2D graphics) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

        // Skip rendering if widget isn't ready yet
        if (inventoryWidget == null || inventoryWidget.isHidden() ||
                inventoryWidget.getBounds().width == 0 || inventoryWidget.getBounds().height == 0) {
            return;
        }

        // Get inventory widget bounds for positioning
        Rectangle inventoryBounds = inventoryWidget.getBounds();

        int unlockedRows = getUnlockedRows();
        int maxSlots = getUnlockedRows() * 4; // 4 slots per row

        // Create panel dimensions
        int panelWidth = inventoryBounds.width - 10;
        int panelHeight = ((inventoryBounds.height - 15) * (TOTAL_ROWS - unlockedRows)) / TOTAL_ROWS;
        int padding = 5;

        // Position at top-right of inventory (or adjust as needed)
        int panelX = inventoryBounds.x + inventoryBounds.width - panelWidth - padding;
        int panelY = inventoryBounds.y + inventoryBounds.height - panelHeight - padding;

        int currentItems = getCurrentItemCount();
        boolean hasItemInWrongRows = hasItemsOnOrAboveRow(unlockedRows);
        boolean illigalState = currentItems > maxSlots || hasItemInWrongRows;

        // Draw semi-transparent panel background
        graphics.setColor(new Color(0, 0, 0, 40));
        graphics.fillRect(panelX, panelY, panelWidth, panelHeight);

        // Draw panel border
        graphics.setColor(illigalState ? Color.RED : new Color(255, 255, 255, 100));
        graphics.drawRect(panelX, panelY, panelWidth, panelHeight);

        graphics.setColor(illigalState ? Color.RED : Color.WHITE);
        graphics.setFont(graphics.getFont().deriveFont(20f));

        String text = "Carrying: " + currentItems + "/" + maxSlots;
        int textX = panelX + 5;
        int textY = panelY + 20;
        graphics.drawString(text, textX, textY);

        String text2 = "Move items from locked rows.";
        if (currentItems > maxSlots || hasItemInWrongRows) {
            if (!hasItemInWrongRows)
                 text2 = "Drop " + (currentItems - maxSlots) + " items.";
            int textX2 = panelX + 5;
            int textY2 = panelY + 30;
            graphics.drawString(text2, textX2, textY2);
        }
    }

    private int getCurrentItemCount() {
        ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
        Item[] items = container == null ? new Item[0] : container.getItems();
        return (int) Arrays.stream(items).filter(p -> p.getId() != -1).count();
    }

    private boolean hasItemsOnOrAboveRow(int row) {
        if (row < 0 || row >= TOTAL_ROWS) {
            return false;
        }
        ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
        if (container == null) {
            return false;
        }

        Item[] items = container.getItems();
        if (items == null) {
            return false;
        }

        int startSlot = row * 4;  // Start checking from the specified row

        for (int i = startSlot; i < items.length; i++) {
            if (items[i].getId() != -1) {
                return true;
            }
        }
        return false;
    }

    private int getUnlockedRows() {
        int rows = 1; // first row always unlocked
        for (int i = 1; i < TOTAL_ROWS; i++) {
            if (plugin.isUnlocked("InventorySlots" + i))
                rows++;
        }
        return rows;
    }
}