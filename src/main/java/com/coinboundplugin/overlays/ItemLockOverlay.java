package com.coinboundplugin.overlays;

import com.coinboundplugin.CoinboundPlugin;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

import net.runelite.client.game.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;

@Singleton
public class ItemLockOverlay extends Overlay
{
    private static final int LOCK_SPRITE_ID = 1342;

    @Inject private Client client;
    @Inject private CoinboundPlugin plugin;
    @Inject private SpriteManager spriteManager;
    @Inject private ItemManager itemManager;

    private BufferedImage lockSprite;

    @Inject
    public ItemLockOverlay()
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        BufferedImage lock = getLockSprite();
        if (lock == null)
            return null;
        drawLocksInInventory(g, lock);
        return null;
    }

    private void drawLocksInInventory(Graphics2D g, BufferedImage lock)
    {
        Widget invWidget = client.getWidget(ComponentID.INVENTORY_CONTAINER);
        if (invWidget == null || invWidget.isHidden())
            return;

        Widget[] slots = invWidget.getDynamicChildren();
        if (slots == null || slots.length == 0)
            return;

        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);
        if (inv == null)
            return;

        Item[] items = inv.getItems();
        int n = Math.min(items.length, slots.length);

        for (int idx = 0; idx < n; idx++)
        {
            Item it = items[idx];
            if (it == null)
                continue;

            int itemId = it.getId();
            if (itemId <= 0)
                continue;

            Widget slotWidget = slots[idx];
            if (slotWidget == null || slotWidget.isHidden())
                continue;

            //TODO: Skip blocker item

            if (!plugin.isItemBlocked(itemId))
                continue;

            Rectangle r = slotWidget.getBounds();
            if (r == null || r.width <= 0 || r.height <= 0)
                continue;

            // Background dimming, checking if this is cool
            //g.setColor(new Color(0, 0, 0, 90));
            //g.fillRect(r.x, r.y, r.width, r.height);

            int size = Math.min(12, Math.min(r.width, r.height));
            g.drawImage(lock, r.x + r.width - size - 1, r.y + 1, size, size, null);
        }
    }

    private BufferedImage getLockSprite()
    {
        if (lockSprite == null)
            lockSprite = spriteManager.getSprite(LOCK_SPRITE_ID, 0);
        return lockSprite;
    }
}