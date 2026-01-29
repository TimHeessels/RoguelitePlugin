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
public class CardPickOverlay extends Overlay
{
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

    @Inject
    public CardPickOverlay(Client client, CoinboundPlugin plugin, MouseManager mouseManager)
    {
        this.client = client;
        this.plugin = plugin;
        this.mouseManager = mouseManager;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void setButton(int index, String label, String typeName, BufferedImage image, Runnable callback)
    {
        if (index < 0 || index >= 4)
        {
            return;
        }
        buttonLabels[index] = label;
        buttonTypeNames[index] = typeName;
        buttonImages[index] = image;
        buttonCallbacks[index] = callback;
    }

    public void clearButtons()
    {
        for (int i = 0; i < 4; i++)
        {
            buttonLabels[i] = null;
            buttonTypeNames[i] = null;
            buttonImages[i] = null;
            buttonCallbacks[i] = null;
        }
    }

    private final MouseAdapter mouseListener = new MouseAdapter()
    {
        private int getHoveredButtonIndex(MouseEvent e)
        {
            if (plugin.getPackChoiceState() != PackChoiceState.PACKGENERATED)
            {
                return -1;
            }
            for (int i = 0; i < buttonBounds.length; i++)
            {
                if (buttonBounds[i] != null && buttonBounds[i].contains(e.getPoint()))
                {
                    return i;
                }
            }
            return -1;
        }

        private boolean isHoveringBuyPackButton(MouseEvent e)
        {
            return plugin.getPackChoiceState() == PackChoiceState.NONE
                    && plugin.getAvailablePacksToBuy() > 0
                    && buyPackButtonBounds != null
                    && buyPackButtonBounds.contains(e.getPoint());
        }

        @Override
        public MouseEvent mousePressed(MouseEvent e)
        {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e))
            {
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseReleased(MouseEvent e)
        {
            if (isHoveringBuyPackButton(e))
            {
                log.info("Buy Pack button clicked");
                plugin.onBuyPackClicked();
                e.consume();
                return e;
            }

            int index = getHoveredButtonIndex(e);
            if (index >= 0 && buttonCallbacks[index] != null)
            {
                log.info("Button {} clicked", buttonLabels[index]);
                buttonCallbacks[index].run();
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseClicked(MouseEvent e)
        {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e))
            {
                e.consume();
            }
            return e;
        }

        @Override
        public MouseEvent mouseDragged(MouseEvent e)
        {
            if (getHoveredButtonIndex(e) >= 0 || isHoveringBuyPackButton(e))
            {
                e.consume();
            }
            return e;
        }
    };


    public void start()
    {
        mouseManager.registerMouseListener(mouseListener);
    }

    public void stop()
    {
        mouseManager.unregisterMouseListener(mouseListener);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int vpX = client.getViewportXOffset();
        int vpY = client.getViewportYOffset();
        int vpWidth = client.getViewportWidth();

        if (plugin.getPackChoiceState() == PackChoiceState.NONE) {
            if (plugin.getAvailablePacksToBuy() > 0)
            {
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
                String text = "Buy Pack ("+plugin.getAvailablePacksToBuy()+")";
                int textX = buyButtonX + (buyButtonWidth - fm.stringWidth(text)) / 2;
                int textY = buyButtonY + (buyButtonHeight + fm.getAscent()) / 2 - 2;
                g.drawString(text, textX, textY);
            }
            return null;
        }

        int totalButtonsWidth = (BUTTON_SIZE * 4) + (BUTTON_SPACING * 3);
        int panelWidth = totalButtonsWidth + (PANEL_PADDING * 2);
        int panelHeight = BUTTON_SIZE + (PANEL_PADDING * 2);

        int panelX = vpX + (vpWidth / 2) - (panelWidth / 2);
        int panelY = vpY + 50;

        g.setColor(PANEL_FILL);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        g.setColor(PANEL_BORDER);
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        Point mouse = client.getMouseCanvasPosition();

        int buttonStartX = panelX + PANEL_PADDING;
        int buttonY = panelY + PANEL_PADDING;

        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < 4; i++)
        {
            int buttonX = buttonStartX + (i * (BUTTON_SIZE + BUTTON_SPACING));
            buttonBounds[i] = new Rectangle(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);

            boolean hovered = mouse != null && buttonBounds[i].contains(mouse.getX(), mouse.getY());

            g.setColor(BUTTON_FILL);
            g.fillRoundRect(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE, 8, 8);

            g.setColor(hovered ? BUTTON_HOVER : BUTTON_BORDER);
            g.setStroke(new BasicStroke(2f));
            g.drawRoundRect(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE, 8, 8);

            if (buttonImages[i] != null)
            {
                int imgX = buttonX + (BUTTON_SIZE - IMAGE_SIZE) / 2;
                int imgY = buttonY + 14;
                g.drawImage(buttonImages[i], imgX, imgY, IMAGE_SIZE, IMAGE_SIZE, null);
            }

            if (buttonLabels[i] != null)
            {
                g.setColor(TEXT_COLOR);
                drawWrappedText(g, buttonLabels[i], buttonX, buttonY + BUTTON_SIZE - 40, BUTTON_SIZE - 4, fm);
            }

            if (buttonTypeNames[i] != null)
            {
                g.setColor(TYPE_COLOR);
                g.setFont(new Font("SansSerif", Font.PLAIN, 9));
                FontMetrics typeFm = g.getFontMetrics();
                int typeX = buttonX + (BUTTON_SIZE - typeFm.stringWidth(buttonTypeNames[i])) / 2;
                int typeY = buttonY + BUTTON_SIZE - 6;
                g.drawString(buttonTypeNames[i], typeX, typeY);
                g.setFont(new Font("SansSerif", Font.BOLD, 10)); // Reset font
            }
        }

        return null;
    }

    private void drawWrappedText(Graphics2D g, String text, int x, int y, int maxWidth, FontMetrics fm)
    {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words)
        {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (fm.stringWidth(testLine) <= maxWidth)
            {
                currentLine = new StringBuilder(testLine);
            }
            else
            {
                if (currentLine.length() > 0)
                {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0)
        {
            lines.add(currentLine.toString());
        }

        int lineHeight = fm.getHeight();
        int totalHeight = lines.size() * lineHeight;
        int startY = y + (20 - totalHeight) / 2 + fm.getAscent();

        for (String line : lines)
        {
            int textX = x + (maxWidth + 4 - fm.stringWidth(line)) / 2;
            g.drawString(line, textX, startY);
            startY += lineHeight;
        }
    }
}