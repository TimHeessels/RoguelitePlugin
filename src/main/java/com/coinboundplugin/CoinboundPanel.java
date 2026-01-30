package com.coinboundplugin;

import com.coinboundplugin.data.PackChoiceState;
import com.coinboundplugin.data.UnlockType;
import com.coinboundplugin.pack.PackOption;
import com.coinboundplugin.pack.UnlockPackOption;
import com.coinboundplugin.requirements.AppearRequirement;
import com.coinboundplugin.ui.PackOptionButton;
import com.coinboundplugin.unlocks.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.api.Client;
import com.google.inject.Inject;

import javax.swing.JOptionPane;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;

public class CoinboundPanel extends PluginPanel {
    private final CoinboundPlugin plugin;

    private static final int UNLOCK_ICON_WIDTH = 25;
    private final JButton buyButton = new JButton("Buy new pack");
    private final JPanel content = new JPanel();

    List<PackOptionButton> optionButtons = new ArrayList<>();

    private JPanel rulesPanel;
    private Map<UnlockType, List<Unlock>> cachedByType;
    private JPanel unlocksContentPanel;

    @Inject
    private Client client;

    @Inject
    private ItemManager itemManager;

    public CoinboundPanel(CoinboundPlugin plugin) {
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

        // Always show rules panel
        String rulesHtml = "<html>"
                + "<b>Booster pack rules</b><br>"
                + "Collect as many coins as you can in your inventory.<br>"
                + "For each new wealth bracket reached, you gain a new boost pack!<br>"
                + "Packs contain cards that allow you to unlock something<br>" +
                "  see the list on this page to see what you currently have access to.<br>"
                + "You can only pick one of the four cards, so pick wisely.<br>"
                + "<b>Core rules</b><br>"
                + "You can only perform actions that are not currently locked by the game mode, at the start of the game you cannot:<br>"
                + "• Go outside of the indicated area. (You're wearing a ankle monitor)<br>"
                + "• Gain XP in any skill except hitpoints. (Hitpoints is unlocked at the start)<br>"
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

    private Icon resolveUnlockIcon(Unlock unlock) {
        UnlockIcon icon = unlock.getIcon();
        if (icon == null) {
            return null;
        }

        if (icon instanceof ImageUnlockIcon) {
            return ((ImageUnlockIcon) icon).getIcon();
        }

        return null;
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

            JPanel categoryContent = new JPanel();
            categoryContent.setLayout(new BoxLayout(categoryContent, BoxLayout.Y_AXIS));
            categoryContent.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (Unlock unlock : list) {
                boolean unlocked = plugin.isUnlocked(unlock);
                boolean meetsRequirements = plugin.canAppearAsPackOption(unlock);

                Icon icon = resolveUnlockIcon(unlock);

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

            String typeHeader = type + " (" + typeUnlockedCount + "/" + list.size() + ")";
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
