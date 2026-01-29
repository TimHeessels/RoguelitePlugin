package com.coinboundplugin.overlays;

import com.coinboundplugin.CoinboundPlugin;
import com.coinboundplugin.data.PackChoiceState;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
public class CardPickOverlay extends Overlay {
    private static final int BUTTON_SIZE = 100;
    private static final int BUTTON_SPACING = 15;
    private static final int PANEL_PADDING = 20;
    private static final int IMAGE_SIZE = 30;

    private static final Color TYPE_COLOR = new Color(180, 160, 120);
    private static final Color PANEL_FILL = new Color(40, 32, 24, 240);
    private static final Color PANEL_BORDER = new Color(120, 100, 70, 220);
    private static final Color BUTTON_FILL = new Color(64, 53, 37, 235);
    private static final Color BUTTON_BORDER = new Color(168, 138, 92, 210);
    private static final Color BUTTON_HOVER = new Color(214, 176, 118, 245);
    private static final Color TEXT_COLOR = new Color(238, 224, 186);

    private final CoinboundPlugin plugin;
    private final Client client;
    private final MouseManager mouseManager;

    private Rectangle buyPackButtonBounds;
    private final Rectangle[] buttonBounds = new Rectangle[4];
    private final String[] buttonLabels = new String[4];
    private final String[] buttonTypeNames = new String[4];
    private final BufferedImage[] buttonImages = new BufferedImage[4];
    private final Runnable[] buttonCallbacks = new Runnable[4];
    private final String[] buttonDescriptions = new String[4];
    private static final int HOVER_AREA_HEIGHT = 30;

    //Animation
    private long animationStartTime = 0;
    private static final long CARD_REVEAL_DELAY_MS = 1000; // Time between each card starting
    private static final long CARD_ANIMATION_DURATION_MS = 400; // Duration of flip animation


    @Inject
    public CardPickOverlay(Client client, CoinboundPlugin plugin, MouseManager mouseManager) {
        this.client = client;
        this.plugin = plugin;
        this.mouseManager = mouseManager;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void setButton(int index, String label, String typeName, String description, BufferedImage image, Runnable callback) {
        if (index < 0 || index >= 4) {
            return;
        }
        if (index == 0) {
            startAnimation();
        }
        buttonLabels[index] = label;
        buttonTypeNames[index] = typeName;
        buttonDescriptions[index] = description;
        buttonImages[index] = image;
        buttonCallbacks[index] = callback;
    }

    private boolean isCardVisible(int index) {
        return getCardAnimationProgress(index) > 0.0f;
    }

    public void clearButtons() {
        for (int i = 0; i < 4; i++) {
            buttonLabels[i] = null;
            buttonTypeNames[i] = null;
            buttonDescriptions[i] = null;
            buttonImages[i] = null;
            buttonCallbacks[i] = null;
        }
        animationStartTime = 0;
    }

    private final MouseAdapter mouseListener = new MouseAdapter() {
        private int getHoveredButtonIndex(MouseEvent e) {
            if (plugin.getPackChoiceState() != PackChoiceState.PACKGENERATED) {
                return -1;
            }
            for (int i = 0; i < buttonBounds.length; i++) {
                if (buttonBounds[i] != null && buttonBounds[i].contains(e.getPoint())) {
                    return i;
                }
            }
            return -1;
        }

        private boolean isHoveringBuyPackButton(MouseEvent e) {
            return plugin.getPackChoiceState() == PackChoiceState.NONE
                    && plugin.getAvailablePacksToBuy() > 0
                    && buyPackButtonBounds != null
                    && buyPackButtonBounds.contains(e.getPoint());
        }

        @Override
        public MouseEvent mousePressed(MouseEvent e) {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e)) {
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseReleased(MouseEvent e) {
            if (isHoveringBuyPackButton(e)) {
                log.info("Buy Pack button clicked");
                plugin.onBuyPackClicked();
                e.consume();
                return e;
            }

            int index = getHoveredButtonIndex(e);
            if (index >= 0 && buttonCallbacks[index] != null) {
                log.info("Button {} clicked", buttonLabels[index]);
                buttonCallbacks[index].run();
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseClicked(MouseEvent e) {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e)) {
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseDragged(MouseEvent e) {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e)) {
                e.consume();
            }
            return e;
        }
    };


    public void start() {
        mouseManager.registerMouseListener(mouseListener);
    }

    public void stop() {
        mouseManager.unregisterMouseListener(mouseListener);
    }


    public void startAnimation() {
        animationStartTime = System.currentTimeMillis();
    }

    private float getCardAnimationProgress(int index) {
        if (animationStartTime == 0) {
            return 1.0f;
        }
        long elapsed = System.currentTimeMillis() - animationStartTime;
        long cardStartTime = index * CARD_REVEAL_DELAY_MS;

        if (elapsed < cardStartTime) {
            return 0.0f;
        }
        float progress = (float) (elapsed - cardStartTime) / CARD_ANIMATION_DURATION_MS;
        return Math.min(1.0f, progress);
    }

    @Override
    public Dimension render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int vpX = client.getViewportXOffset();
        int vpY = client.getViewportYOffset();
        int vpWidth = client.getViewportWidth();

        if (plugin.getPackChoiceState() == PackChoiceState.NONE) {
            if (plugin.getAvailablePacksToBuy() > 0) {
                int buyButtonWidth = 120;
                int buyButtonHeight = 40;
                int buyButtonX = vpX + (vpWidth / 2) - (buyButtonWidth / 2);
                int buyButtonY = vpY + 50;

                buyPackButtonBounds = new Rectangle(buyButtonX, buyButtonY, buyButtonWidth, buyButtonHeight);

                Point mouse = client.getMouseCanvasPosition();
                boolean hovered = mouse != null && buyPackButtonBounds.contains(mouse.getX(), mouse.getY());

                g.setColor(BUTTON_FILL);
                g.fillRoundRect(buyButtonX, buyButtonY, buyButtonWidth, buyButtonHeight, 8, 8);

                g.setColor(hovered ? BUTTON_HOVER : BUTTON_BORDER);
                g.setStroke(new BasicStroke(2f));
                g.drawRoundRect(buyButtonX, buyButtonY, buyButtonWidth, buyButtonHeight, 8, 8);

                g.setColor(TEXT_COLOR);
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g.getFontMetrics();
                String text = "Buy Pack (" + plugin.getAvailablePacksToBuy() + ")";
                int textX = buyButtonX + (buyButtonWidth - fm.stringWidth(text)) / 2;
                int textY = buyButtonY + (buyButtonHeight + fm.getAscent()) / 2 - 2;
                g.drawString(text, textX, textY);
            }
            return null;
        }

        int totalButtonsWidth = (BUTTON_SIZE * 4) + (BUTTON_SPACING * 3);
        int panelWidth = totalButtonsWidth + (PANEL_PADDING * 2);

        int panelX = vpX + (vpWidth / 2) - (panelWidth / 2);
        int panelY = vpY + 50;
        Point mouse = client.getMouseCanvasPosition();

        int panelHeight = BUTTON_SIZE + (PANEL_PADDING * 2) + HOVER_AREA_HEIGHT;

        // Find hovered card index
        int hoveredIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (buttonBounds[i] != null && mouse != null && buttonBounds[i].contains(mouse.getX(), mouse.getY())) {
                float progress = getCardAnimationProgress(i);
                if (progress >= 1.0f) {
                    hoveredIndex = i;
                    break;
                }
            }
        }


        g.setColor(PANEL_FILL);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        g.setColor(PANEL_BORDER);
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);


        int buttonStartX = panelX + PANEL_PADDING;
        int buttonY = panelY + PANEL_PADDING;

        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < 4; i++) {
            int buttonX = buttonStartX + (i * (BUTTON_SIZE + BUTTON_SPACING));

            float progress = getCardAnimationProgress(i);

            // Skip if animation hasn't started for this card
            if (progress <= 0.0f) {
                buttonBounds[i] = null;
                continue;
            }

            buttonBounds[i] = new Rectangle(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);

            // Calculate flip scale (0->1 flip effect using sine for smooth easing)
            // First half: scale shrinks, second half: scale grows
            float flipProgress = progress < 0.5f
                    ? 1.0f - (progress * 2.0f)  // 1.0 -> 0.0
                    : (progress - 0.5f) * 2.0f; // 0.0 -> 1.0

            // Apply easing for smoother animation
            float scaleX = (float) Math.sin(flipProgress * Math.PI / 2);
            float alpha = Math.min(1.0f, progress * 2.0f); // Fade in during first half

            // Save original transform and composite
            java.awt.geom.AffineTransform originalTransform = g.getTransform();
            Composite originalComposite = g.getComposite();

            // Apply alpha
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Apply horizontal scale transform (flip effect)
            int centerX = buttonX + BUTTON_SIZE / 2;
            g.translate(centerX, 0);
            g.scale(scaleX, 1.0);
            g.translate(-centerX, 0);

            boolean hovered = progress >= 1.0f && mouse != null && buttonBounds[i].contains(mouse.getX(), mouse.getY());

            g.setColor(BUTTON_FILL);
            g.fillRoundRect(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE, 8, 8);

            g.setColor(hovered ? BUTTON_HOVER : BUTTON_BORDER);
            g.setStroke(new BasicStroke(2f));
            g.drawRoundRect(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE, 8, 8);

            // Only show content after flip midpoint
            if (progress > 0.5f) {
                if (buttonImages[i] != null) {
                    int imgX = buttonX + (BUTTON_SIZE - IMAGE_SIZE) / 2;
                    int imgY = buttonY + 14;
                    g.drawImage(buttonImages[i], imgX, imgY, IMAGE_SIZE, IMAGE_SIZE, null);
                }

                if (buttonLabels[i] != null) {
                    g.setColor(TEXT_COLOR);
                    drawWrappedText(g, buttonLabels[i], buttonX, buttonY + BUTTON_SIZE - 40, BUTTON_SIZE - 4, fm);
                }

                if (buttonTypeNames[i] != null) {
                    g.setColor(TYPE_COLOR);
                    g.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    FontMetrics typeFm = g.getFontMetrics();
                    int typeX = buttonX + (BUTTON_SIZE - typeFm.stringWidth(buttonTypeNames[i])) / 2;
                    int typeY = buttonY + BUTTON_SIZE - 6;
                    g.drawString(buttonTypeNames[i], typeX, typeY);
                    g.setFont(new Font("SansSerif", Font.BOLD, 10));
                }
            }

            // Restore original state
            g.setTransform(originalTransform);
            g.setComposite(originalComposite);
        }

        // Draw hover description text at bottom of panel
        String desc = "Select a card you wish to unlock.";
        if (hoveredIndex >= 0 && buttonDescriptions[hoveredIndex] != null) {
            desc = buttonDescriptions[hoveredIndex];
        }

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics descFm = g.getFontMetrics();
        int descX = panelX + (panelWidth - descFm.stringWidth(desc)) / 2;
        int descY = panelY + PANEL_PADDING + BUTTON_SIZE + HOVER_AREA_HEIGHT / 2 + descFm.getAscent() / 2;
        g.drawString(desc, descX, descY);

        return null;
    }

    private void drawWrappedText(Graphics2D g, String text, int x, int y, int maxWidth, FontMetrics fm) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        int lineHeight = fm.getHeight();
        int totalHeight = lines.size() * lineHeight;
        int startY = y + (20 - totalHeight) / 2 + fm.getAscent();

        for (String line : lines) {
            int textX = x + (maxWidth + 4 - fm.stringWidth(line)) / 2;
            g.drawString(line, textX, startY);
            startY += lineHeight;
        }
    }
}