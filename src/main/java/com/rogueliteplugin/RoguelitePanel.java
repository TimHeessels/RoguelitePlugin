package com.rogueliteplugin;

import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.ui.PackOptionButton;
import com.rogueliteplugin.unlocks.*;
import net.runelite.client.ui.PluginPanel;
import net.runelite.api.Client;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RoguelitePanel extends PluginPanel {
    private final RoguelitePlugin plugin;

    private static final int UNLOCK_ICON_WIDTH = 25;
    private final JButton buyButton = new JButton("Buy new pack");
    private final JPanel content = new JPanel();

    List<PackOptionButton> optionButtons = new ArrayList<>();

    private JPanel rulesPanel;
    private boolean rulesVisible = false;

    @Inject
    private Client client;

    public RoguelitePanel(RoguelitePlugin plugin) {
        this.plugin = plugin;
        setLayout(new BorderLayout());

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(content, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);

        buyButton.addActionListener(e -> plugin.onBuyPackClicked());

        refresh();
    }

    private void animateReveal() {
        final int[] index = {0};

        Timer timer = new Timer(450, e ->
        {
            if (index[0] >= optionButtons.size()) {
                ((Timer) e.getSource()).stop();
                return;
            }

            fadeFlip(optionButtons.get(index[0]));
            index[0]++;
        });

        timer.setInitialDelay(300);
        timer.start();
    }

    private void fadeFlip(PackOptionButton button) {
        final int steps = 10;
        final int delay = 20;

        Timer fadeOut = new Timer(delay, null);
        Timer fadeIn = new Timer(delay, null);

        final int[] step = {steps};

        fadeOut.addActionListener(e ->
        {
            button.setAlpha(step[0] / (float) steps);
            step[0]--;

            if (step[0] <= 0) {
                fadeOut.stop();
                button.reveal();
                step[0] = 0;
                fadeIn.start();
            }
        });

        fadeIn.addActionListener(e ->
        {
            button.setAlpha(step[0] / (float) steps);
            step[0]++;

            if (step[0] >= steps) {
                button.setAlpha(1f);
                fadeIn.stop();
            }
        });

        fadeOut.start();
    }


    public void refresh() {
        optionButtons.clear();
        content.removeAll();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        int points = plugin.getCurrentPoints();

        content.add(new JLabel("Current Points: " + points));
        content.add(Box.createVerticalStrut(6));
        content.add(new JLabel("XP to Next Point: " + plugin.getXpToNextPoint()));
        content.add(Box.createVerticalStrut(12));

        if (plugin.getPackChoiceState() == PackChoiceState.NONE) {
            JButton buyButton = new JButton("Buy new pack");
            buyButton.setEnabled(points >= 1);
            buyButton.addActionListener(e -> plugin.onBuyPackClicked());
            content.add(buyButton);
        } else if (plugin.getPackChoiceState() == PackChoiceState.CHOOSING) {
            content.add(new JLabel("Choose a card:"));
            content.add(Box.createVerticalStrut(8));

            for (PackOption option : plugin.getCurrentPackOptions()) {
                Unlock unlock = ((UnlockPackOption) option).getUnlock();

                Icon icon = resolveIcon(unlock);

                PackOptionButton button = new PackOptionButton(
                        option.getDisplayName(),
                        option.getDisplayType(),
                        icon
                );
                button.setAlignmentX(Component.LEFT_ALIGNMENT);
                button.setMaximumSize(
                        new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height)
                );
                button.addActionListener(e -> plugin.onPackOptionSelected(option));

                optionButtons.add(button);
                content.add(button);
                content.add(Box.createVerticalStrut(6));
            }

            animateReveal();
        }

        //Rules
        JButton toggleRules = new JButton("Show rules ▸");
        toggleRules.setAlignmentX(Component.LEFT_ALIGNMENT);

        rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setVisible(false);
        rulesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

// Example rules text
        JLabel rulesText = new JLabel(
                "<html>"
                        + "<b>Rules</b><br>"
                        + "• For each certain amount of XP (set in config) you may open a pack.<br>"
                        + "• Packs contain cards that unlock a range of content, see the list on this page to see what you have access to.<br>"
                        + "• Blocked content is not physically blocked as that is against runescape rules, but it is indicated as best as possible.<br>"
                        + "</html>"
        );
        rulesText.setAlignmentX(Component.LEFT_ALIGNMENT);

        rulesPanel.add(rulesText);

        toggleRules.addActionListener(e ->
        {
            rulesVisible = !rulesVisible;
            rulesPanel.setVisible(rulesVisible);
            toggleRules.setText(rulesVisible ? "Hide rules ▾" : "Show rules ▸");

            revalidate();
            repaint();
        });

        content.add(Box.createVerticalStrut(6));
        content.add(toggleRules);
        content.add(Box.createVerticalStrut(6));
        content.add(rulesPanel);
        content.add(Box.createVerticalStrut(12));

        addUnlockedSection(content);

        revalidate();
        repaint();
    }

    private static void leftAlign(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void addUnlockedSection(JPanel content) {
        if (plugin.getUnlockRegistry() == null) {
            return;
        }

        Map<UnlockType, List<Unlock>> byType = new EnumMap<>(UnlockType.class);

        for (Unlock unlock : plugin.getUnlockRegistry().getAll()) {
            byType.computeIfAbsent(unlock.getType(), t -> new ArrayList<>())
                    .add(unlock);
        }

        content.add(Box.createVerticalStrut(12));
        int totalUnlocks = plugin.getUnlockRegistry().getAll().size();

        long unlockedCount = plugin.getUnlockRegistry()
                .getAll()
                .stream()
                .filter(plugin::isUnlocked)
                .count();

        JLabel unlocksHeader = new JLabel(
                "Unlocks (" + unlockedCount + " / " + totalUnlocks + ")"
        );
        unlocksHeader.setFont(unlocksHeader.getFont().deriveFont(Font.BOLD));

        content.add(unlocksHeader);
        content.add(Box.createVerticalStrut(6));

        for (UnlockType type : UnlockType.values()) {
            List<Unlock> list = byType.get(type);
            if (list == null || list.isEmpty()) {
                continue;
            }

            JLabel header = new JLabel(type.toString());
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            content.add(header);
            content.add(Box.createVerticalStrut(4));

            for (Unlock unlock : list) {
                boolean unlocked = plugin.isUnlocked(unlock);

                Icon icon = resolveIcon(unlock);

                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

// Icon column (fixed width)
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setPreferredSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));
                iconLabel.setMinimumSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));
                iconLabel.setMaximumSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));

// Text label
                JLabel textLabel = new JLabel(unlock.getDisplayName());
                textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                if (!unlocked) {
                    textLabel.setForeground(new Color(170, 60, 60));
                    if (icon != null)
                        iconLabel.setEnabled(false);
                }
                else
                    textLabel.setForeground(new Color(70, 167, 32));

                textLabel.setToolTipText(unlock.getDescription());

// Assemble row
                row.add(iconLabel);
                row.add(Box.createHorizontalStrut(6));
                row.add(textLabel);

                content.add(row);
            }
            content.add(Box.createVerticalStrut(8));
        }
    }

    private Icon resolveIcon(Unlock unlock) {
        UnlockIcon icon = unlock.getIcon();
        if (icon == null) {
            return null;
        }

        if (icon instanceof ImageUnlockIcon) {
            return ((ImageUnlockIcon) icon).getIcon();
        }

        if (icon instanceof SpriteUnlockIcon) {
            SpriteUnlockIcon sprite = (SpriteUnlockIcon) icon;

            try {
                BufferedImage img = plugin
                        .getSpriteManager()
                        .getSprite(sprite.getSpriteId(), 0);

                if (img == null) {
                    return null;
                }

                Image scaled = img.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } catch (AssertionError e) {
                // Sprite system not ready yet
                return null;
            }
        }

        return null;
    }
}