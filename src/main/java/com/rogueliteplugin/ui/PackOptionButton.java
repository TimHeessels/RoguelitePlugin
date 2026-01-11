package com.rogueliteplugin.ui;

import com.rogueliteplugin.pack.PackOption;

import javax.swing.*;
import java.awt.*;

public class PackOptionButton extends JButton
{
    private static final int CARD_WIDTH = 150;
    private static final int CARD_HEIGHT = 150;

    private float alpha = 1f;
    private final String label;
    private final String type;
    private final Icon icon;

    public PackOptionButton(String label, String type, Icon icon)
    {
        this.label = label;
        this.icon = icon;
        this.type = type;

        Dimension size = new Dimension(CARD_WIDTH, CARD_HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setFocusPainted(false);
        setEnabled(false);

        showBack();
    }

    public void showBack()
    {
        setText("?");
        setIcon(null);
    }

    public void reveal()
    {
        setText("<html><center>"
                + label + "<br>"
                + "<span style='font-size:9px; color:#888888;'>"
                + type.toString()
                + "</span>"
                + "</center></html>");

        setIcon(icon);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setEnabled(true);
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        );
        super.paintComponent(g2);
        g2.dispose();
    }
}

