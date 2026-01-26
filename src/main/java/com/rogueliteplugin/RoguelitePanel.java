package com.rogueliteplugin;

import com.rogueliteplugin.data.PackChoiceState;
import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.ui.PackOptionButton;
import com.rogueliteplugin.unlocks.*;
import com.rogueliteplugin.data.ChallengeType;
import net.runelite.client.ui.PluginPanel;
import net.runelite.api.Client;
import com.google.inject.Inject;

import javax.swing.JOptionPane;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class RoguelitePanel extends PluginPanel {
    private final RoguelitePlugin plugin;

    private static final int UNLOCK_ICON_WIDTH = 25;
    private final JButton buyButton = new JButton("Buy new pack");
    private final JPanel content = new JPanel();

    List<PackOptionButton> optionButtons = new ArrayList<>();

    private JPanel rulesPanel;
    private Map<UnlockType, List<Unlock>> cachedByType;
    private JPanel unlocksContentPanel;

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

        // Clear caches to force rebuild on refresh
        cachedByType = null;
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        if (!plugin.statsInitialized) {
            content.add(new JLabel("Please login to see"));
            content.add(new JLabel("your roguelite progress."));
            revalidate();
            repaint();
            return;
        }

        //Buttons section

        //Open booster pack button
        if (plugin.getPackChoiceState() == PackChoiceState.NONE) {
            boolean buyNewPackButtonActive = plugin.getAvailablePacks() > 0;
            content.add(createActionButton(buyNewPackButtonActive ? "Open booster pack" : "<html>Collect at least X points to open a new booster pack</html>",
                    buyNewPackButtonActive, "/icons/stack.png", e -> plugin.onBuyPackClicked()));
            content.add(Box.createVerticalStrut(15));
        }

        // Show pack choice cards if user is choosing
        if (plugin.getPackChoiceState() == PackChoiceState.PACKGENERATED) {
            content.add(new JLabel("Choose a card,"));
            content.add(new JLabel("you can only pick one:"));
            content.add(Box.createVerticalStrut(8));

            for (PackOption option : plugin.getCurrentPackOptions()) {
                Unlock unlock = ((UnlockPackOption) option).getUnlock();
                Icon icon = resolveIcon(unlock);

                PackOptionButton button = new PackOptionButton(
                        option.getDisplayName(),
                        option.getDisplayType(),
                        icon
                );
                button.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
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

        // Always show rules panel
        String rulesHtml = "<html>"
                + "<b>Booster pack rules</b><br>"
                + "Collect X points to open a booster pack.<br>"
                + "You gain points by picking up items you've never held before. More points for better items.<br>"
                + "Packs contain cards that allow you to unlock something<br>" +
                "  see the list on this page to see what you currently have access to.<br>"
                + "You can only pick one of the four cards, so pick wisely.<br>"
                + "<b>Core rules</b><br>"
                + "You can only perform actions that are not currently locked by the game mode, at the start of the game you cannot:<br>"
                + "• Gain XP in any skill. (Hitpoints is unlocked at the start)<br>"
                + "• Complete quests. You unlock quests per year, starting at 2002.<br>"
                + "• Equip items. You unlock each slot seperatly.<br>"
                + "• Buy or sell items from shopkeepers. You unlock them per shop type (E.G Platebody shops unlock all shops with platebody icon)<br>"
                + "• Minigames. Allow you to participate and gain rewards in specific minigames.<br>"
                + "• Opening clue boxes. (Note, you're allowed to complete the clue scroll, just not open the casket.)<br>"
                + "• Bosses. Allow you to kill a specific boss.<br>"
                + "• Transport options. Allow you to use transport methods like fairy rings or teleport spells.<br><br>"
                + "<b>Tips</b><br>"
                + "• Combat is off-limits until you unlock at least one combat skill. Hitpoints are unlocked at the start.<br>"
                + "</html>";

        rulesPanel = new

                CollapsiblePanel("Rules", new JLabel(rulesHtml));
        rulesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(6));
        content.add(rulesPanel);
        content.add(Box.createVerticalStrut(12));

        // Always show unlocks section
        updateUnlocksSection(content);

        content.add(Box.createVerticalStrut(12));
        content.add(new

                JLabel("Uses icons from:"));
        content.add(new

                JLabel("https://game-icons.net"));


        revalidate();

        repaint();
    }

    private void showConfirmationDialog(String message, String title, Runnable onConfirm) {
        SwingUtilities.invokeLater(() -> {
            final int result = JOptionPane.showConfirmDialog(
                    null,
                    message,
                    title,
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                onConfirm.run();
            }
        });
    }

    private JButton createActionButton(String htmlText,
                                       boolean enabled,
                                       String iconResourcePath,
                                       ActionListener listener) {
        JButton btn = new JButton(htmlText);
        btn.setMargin(new Insets(12, 16, 12, 16));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setEnabled(enabled);

        // Load icon safely and set disabled variant
        URL iconUrl = getClass().getResource(iconResourcePath);
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            btn.setIcon(icon);
        }

        // Custom disabled visuals (optional, consistent dim look)
        if (!enabled) {
            btn.setForeground(new Color(150, 150, 150));
            btn.setBackground(new Color(235, 5, 5));
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setFocusPainted(false);
        }

        if (enabled && listener != null) {
            btn.addActionListener(listener);
        }

        return btn;
    }

    private void updateUnlocksSection(JPanel content) {
        if (plugin.getUnlockRegistry() == null) {
            return;
        }

        if (unlocksContentPanel == null) {
            unlocksContentPanel = new JPanel();
            unlocksContentPanel.setLayout(new BoxLayout(unlocksContentPanel, BoxLayout.Y_AXIS));
            content.add(unlocksContentPanel);
        } else {
            if (unlocksContentPanel.getParent() != content) {
                content.add(unlocksContentPanel);
            }
        }

        Map<UnlockType, List<Unlock>> byType = new EnumMap<>(UnlockType.class);
        List<Unlock> all = new ArrayList<>(plugin.getUnlockRegistry().getAll());
        all.sort(Comparator.comparing(Unlock::getType).thenComparing(Unlock::getDisplayName));

        for (Unlock unlock : all) {
            byType.computeIfAbsent(unlock.getType(), t -> new ArrayList<>()).add(unlock);
        }

        if (cachedByType == null || !byType.equals(cachedByType)) {
            cachedByType = byType;
            rebuildUnlocksPanel(unlocksContentPanel, byType);
        }
    }

    private void rebuildUnlocksPanel(JPanel panel, Map<UnlockType, List<Unlock>> byType) {
        panel.removeAll();

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
        panel.add(unlocksHeader);
        panel.add(Box.createVerticalStrut(6));

        for (UnlockType type : UnlockType.values()) {
            List<Unlock> list = byType.get(type);
            if (list == null || list.isEmpty()) {
                continue;
            }

            long typeUnlockedCount = list.stream()
                    .filter(plugin::isUnlocked)
                    .count();
            String typeHeader = type + " (" + typeUnlockedCount + "/" + list.size() + ")";

            JPanel categoryContent = new JPanel();
            categoryContent.setLayout(new BoxLayout(categoryContent, BoxLayout.Y_AXIS));
            categoryContent.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (Unlock unlock : list) {
                boolean unlocked = plugin.isUnlocked(unlock);
                boolean meetsRequirements = plugin.canAppearAsPackOption(unlock);

                Icon icon = resolveIcon(unlock);

                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel iconLabel = new JLabel(icon);
                iconLabel.setPreferredSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));
                iconLabel.setMinimumSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));
                iconLabel.setMaximumSize(new Dimension(UNLOCK_ICON_WIDTH, UNLOCK_ICON_WIDTH));

                JLabel textLabel = new JLabel(unlock.getDisplayName());
                textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                if (!unlocked) {
                    if (meetsRequirements)
                        textLabel.setForeground(new Color(128, 128, 128));
                    else
                        textLabel.setForeground(new Color(170, 60, 60));
                    if (icon != null)
                        iconLabel.setEnabled(false);
                } else
                    textLabel.setForeground(new Color(70, 167, 32));

                applyTooltip(textLabel, unlock);

                row.add(iconLabel);
                row.add(Box.createHorizontalStrut(6));
                row.add(textLabel);

                categoryContent.add(row);
            }

            CollapsiblePanel categoryPanel = new CollapsiblePanel(typeHeader, categoryContent);
            categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(categoryPanel);
            panel.add(Box.createVerticalStrut(6));
        }
    }

    private void applyTooltip(JLabel label, Unlock unlock) {
        StringBuilder sb = new StringBuilder("<html>");

        sb.append("<b>")
                .append(unlock.getDisplayName())
                .append("</b><br>")
                .append(unlock.getDescription());

        List<AppearRequirement> reqs = unlock.getRequirements();
        if (reqs != null && !reqs.isEmpty()) {
            sb.append("<br><br><b>Requirements:</b><br>");

            for (AppearRequirement req : reqs) {
                boolean met = false;

                try {
                    if (plugin != null) {
                        met = req.isMet(plugin, plugin.getUnlockedIds());
                    }
                } catch (Exception | AssertionError e) {
                    met = false;
                }

                sb.append(met ? "• " : "• <font color='red'>")
                        .append(req.getRequiredUnlockTitle())
                        .append(met ? "" : "</font>")
                        .append("<br>");
            }
        }

        sb.append("</html>");

        label.setToolTipText(sb.toString());
    }

    private Icon resolveIcon(Unlock unlock) {
        UnlockIcon icon = unlock.getIcon();
        if (icon == null) {
            return null;
        }

        if (icon instanceof ImageUnlockIcon) {
            return ((ImageUnlockIcon) icon).getIcon();
        }

        return null;
    }

    /**
     * Reusable collapsible panel with styled header and content.
     */
    private class CollapsiblePanel extends JPanel {
        private final JButton headerButton;
        private final JPanel contentPanel;
        private boolean expanded = false;

        public CollapsiblePanel(String title, JComponent content) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            headerButton = new JButton(title + " ▸");
            headerButton.setMargin(new Insets(8, 12, 8, 12));
            headerButton.setFont(headerButton.getFont().deriveFont(13f));
            headerButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));
            headerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            headerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            headerButton.setHorizontalAlignment(SwingConstants.LEFT);
            headerButton.setFocusPainted(false);

            contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 0));
            contentPanel.setVisible(false);

            contentPanel.add(content);

            headerButton.addActionListener(e -> toggle());

            add(headerButton);
            add(contentPanel);
        }

        private void toggle() {
            expanded = !expanded;
            contentPanel.setVisible(expanded);
            updateHeaderText();
            revalidate();
            repaint();
        }

        private void updateHeaderText() {
            String text = headerButton.getText();
            if (text.endsWith(" ▸")) {
                headerButton.setText(text.substring(0, text.length() - 2) + " ▾");
            } else if (text.endsWith(" ▾")) {
                headerButton.setText(text.substring(0, text.length() - 2) + " ▸");
            }
        }
    }
}
