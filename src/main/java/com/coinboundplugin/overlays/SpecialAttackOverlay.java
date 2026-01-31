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

@Singleton
public class SpecialAttackOverlay extends Overlay {
    private final Client client;
    private final CoinboundPlugin plugin;
    private static final int LOCK_SPRITE_ID = 1342;
    @Inject
    private SpriteManager spriteManager;

    private BufferedImage lockSprite;

    @Inject
    public SpecialAttackOverlay(Client client, CoinboundPlugin plugin) {
        this.client = client;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    private BufferedImage getLockSprite() {
        if (lockSprite == null) {
            lockSprite = spriteManager.getSprite(LOCK_SPRITE_ID, 0);
        }
        return lockSprite;
    }

    @Override
    public Dimension render(Graphics2D g) {
        BufferedImage lock = getLockSprite();
        if (lock == null)
            return null; // sprite not loaded yet

        if (!plugin.isUnlocked("SpecialAttack")) {
            Widget w = client.getWidget(10485796); //Special attack orb
            if (w != null && !w.isHidden())
                lockWidget(w, g, lock);

            Widget w2 = client.getWidget(38862887); //Special attack bar in menu
            if (w2 != null && !w2.isHidden())
                lockWidget(w2, g, lock);
        }
        return null;
    }

    void lockWidget(Widget w, Graphics g, BufferedImage lock) {
        Rectangle r = w.getBounds();
        if (r == null || r.width <= 0 || r.height <= 0)
            return;

        g.setColor(new Color(0, 0, 0, 110));
        g.fillRect(r.x, r.y, r.width, r.height);

        // Scale lock relative to icon size
        int size = Math.min(14, Math.min(r.width, r.height));
        int x = r.x + r.width - size - 2;
        int y = r.y + 2;

        g.drawImage(lock, x, y, size, size, null);
    }
}