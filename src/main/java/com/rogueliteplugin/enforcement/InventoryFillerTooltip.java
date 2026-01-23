package com.rogueliteplugin.enforcement;

import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Point;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import javax.inject.Inject;
import java.awt.*;

public class InventoryFillerTooltip extends Overlay {
    private final RoguelitePlugin plugin;
    private final TooltipManager tooltipManager;

    @Inject
    public InventoryFillerTooltip(RoguelitePlugin plugin, TooltipManager tooltipManager) {
        this.plugin = plugin;
        this.tooltipManager = tooltipManager;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget inventoryWidget = plugin.getClient().getWidget(ComponentID.INVENTORY_CONTAINER);
        if (inventoryWidget == null || inventoryWidget.isHidden()) {
            return null;
        }

        Widget[] children = inventoryWidget.getDynamicChildren();
        if (children == null) {
            return null;
        }

        Point mousePos = plugin.getClient().getMouseCanvasPosition();

        for (Widget child : children) {
            if (child.getItemId() == ItemID.BANK_FILLER) {
                Rectangle bounds = child.getBounds();
                if (bounds != null && bounds.contains(mousePos.getX(), mousePos.getY())) {
                    tooltipManager.add(new Tooltip("Unlock more rows to use this slot."));
                    break;
                }
            }
        }

        return null;
    }
}
