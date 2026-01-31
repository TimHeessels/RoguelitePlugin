package com.coinboundplugin.overlays;

import com.coinboundplugin.CoinboundPlugin;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

@Singleton
public class ShopLockOverlay extends Overlay
{
    private static final int LOCK_SPRITE_ID = 1342;

    @Inject private Client client;
    @Inject private CoinboundPlugin plugin;
    @Inject
    private SpriteManager spriteManager;

    // Replace with the groupId you logged for normal shops
    private static final int SHOP_GROUP_ID = 300; // <-- CHANGE THIS

    private BufferedImage lockSprite;

    @Inject
    public ShopLockOverlay()
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        if (plugin.isUnlocked("Shops"))
            return null;

        Widget shopRoot = client.getWidget(SHOP_GROUP_ID, 0);
        if (shopRoot == null || shopRoot.isHidden())
            return null;

        Widget panel = findLargestVisibleWidget(shopRoot);
        if (panel == null)
            return null;

        Rectangle r = panel.getBounds();
        if (r == null || r.width <= 0 || r.height <= 0)
            return null;

        // --- Grey overlay ---
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(r.x, r.y, r.width, r.height);

        // --- Lock icon ---
        BufferedImage lock = getLockSprite();
        if (lock != null)
        {
            int size = Math.min(32, Math.min(r.width, r.height) / 4);
            int x = r.x + (r.width - size) / 2;
            int y = r.y + (r.height - size) / 2;

            g.drawImage(lock, x, y, size, size, null);
        }

        return null;
    }

    private BufferedImage getLockSprite()
    {
        if (lockSprite == null)
        {
            lockSprite = spriteManager.getSprite(LOCK_SPRITE_ID, 0);
        }
        return lockSprite;
    }

    /**
     * Finds the biggest visible widget under the shop root.
     * This is usually the main shop panel and survives layout changes.
     */
    private Widget findLargestVisibleWidget(Widget root)
    {
        Widget best = null;
        int bestArea = 0;

        Deque<Widget> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty())
        {
            Widget w = stack.pop();
            if (w == null || w.isHidden())
                continue;

            Rectangle r = w.getBounds();
            if (r != null)
            {
                int area = r.width * r.height;
                if (area > bestArea)
                {
                    bestArea = area;
                    best = w;
                }
            }

            Widget[] children = w.getChildren();
            if (children != null)
            {
                for (Widget c : children)
                {
                    if (c != null)
                        stack.push(c);
                }
            }
        }

        return best;
    }
}
