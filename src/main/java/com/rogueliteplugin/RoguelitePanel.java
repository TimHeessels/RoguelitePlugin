package com.rogueliteplugin;

import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.ui.PackOptionButton;
import com.rogueliteplugin.unlocks.*;
import com.rogueliteplugin.challenge.Challenge;
import com.rogueliteplugin.challenge.ChallengeType;
import net.runelite.client.ui.PluginPanel;
import net.runelite.api.Client;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private Map<ChallengeType, List<Challenge>> cachedChallengesByType;
    private JPanel unlocksContentPanel;
    private JPanel challengesContentPanel;

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
        cachedChallengesByType = null;

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        if (plugin.anyChallengeActive()) {
            content.add(new JLabel("Complete the challenge to"));
            content.add(new JLabel("unlock a new booster pack."));
        } else
            content.add(new JLabel("You can unlock a new booster pack!"));
        content.add(Box.createVerticalStrut(6));

        // Always show the buy button
        if (plugin.playerIsLoggedIn()) {
            JButton buyButton = new JButton("Buy new pack");
            buyButton.setMargin(new Insets(8, 12, 8, 12));
            buyButton.setFont(buyButton.getFont().deriveFont(14f));
            buyButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
            buyButton.setEnabled(!plugin.anyChallengeActive());
            buyButton.addActionListener(e -> plugin.onBuyPackClicked());
            content.add(buyButton);
        }

        // Show pack choice cards if user is choosing
        if (plugin.playerIsLoggedIn() && plugin.getPackChoiceState() == PackChoiceState.CHOOSING) {
            content.add(new JLabel("Choose a card:"));
            content.add(Box.createVerticalStrut(8));

            for (PackOption option : plugin.getCurrentPackOptions()) {
                Unlock unlock = ((UnlockPackOption) option).getUnlock();
                Icon icon = resolveIcon(unlock);
                int balancedAmount = plugin.getBalancedChallengeAmount(((UnlockPackOption) option).getChallengeLowAmount(), ((UnlockPackOption) option).getChallengeHighAmount());
                String challengeName = option.getChallengeName().replace("$", NumberFormat
                        .getInstance(new Locale("nl", "NL"))
                        .format(balancedAmount));

                PackOptionButton button = new PackOptionButton(
                        option.getDisplayName(),
                        option.getDisplayType(),
                        challengeName,
                        option.getChallengeType(),
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
                + "<b>Rules</b><br>"
                + "• Complete the active challenge to open a booster pack.<br>"
                + "• Packs contain cards that unlock a range of content, see the list on this page to see what you have access to.<br>"
                + "• Blocked content is not physically blocked as that is against runescape rules, but it is indicated as best as possible.<br>"
                + "• Each card also contains a new challenge, so pick wisely.<br>"
                + "</html>";

        rulesPanel = new CollapsiblePanel("Rules", new JLabel(rulesHtml));
        rulesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(6));
        content.add(rulesPanel);
        content.add(Box.createVerticalStrut(12));

        // Always show unlocks section
        updateUnlocksSection(content);

        // Always show challenges section
        updateChallengesSection(content);

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

    private void updateChallengesSection(JPanel content) {
        if (plugin.getChallengeRegistry() == null) {
            return;
        }

        if (challengesContentPanel == null) {
            challengesContentPanel = new JPanel();
            challengesContentPanel.setLayout(new BoxLayout(challengesContentPanel, BoxLayout.Y_AXIS));
            content.add(challengesContentPanel);
        } else {
            if (challengesContentPanel.getParent() != content) {
                content.add(challengesContentPanel);
            }
        }

        Map<ChallengeType, List<Challenge>> byType = new EnumMap<>(ChallengeType.class);
        List<Challenge> all = new ArrayList<>(plugin.getChallengeRegistry().getAll());
        all.sort(Comparator.comparing(Challenge::getType).thenComparing(Challenge::getDisplayName));

        for (Challenge challenge : all) {
            byType.computeIfAbsent(challenge.getType(), t -> new ArrayList<>()).add(challenge);
        }

        if (cachedChallengesByType == null || !byType.equals(cachedChallengesByType)) {
            cachedChallengesByType = byType;
            rebuildChallengesPanel(challengesContentPanel, byType);
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

    private void rebuildChallengesPanel(JPanel panel, Map<ChallengeType, List<Challenge>> byType) {
        panel.removeAll();

        int totalChallenges = plugin.getChallengeRegistry().getAll().size();
        long validCount = plugin.getChallengeRegistry()
                .getAll()
                .stream()
                .filter(c -> c.isValidWithUnlocks(plugin.getUnlockedIds()))
                .count();

        JLabel challengesHeader = new JLabel(
                "Challenges (" + validCount + " / " + totalChallenges + ")"
        );
        challengesHeader.setFont(challengesHeader.getFont().deriveFont(Font.BOLD));
        panel.add(challengesHeader);
        panel.add(Box.createVerticalStrut(6));

        for (ChallengeType type : ChallengeType.values()) {
            List<Challenge> list = byType.get(type);
            if (list == null || list.isEmpty()) {
                continue;
            }

            long typeValidCount = list.stream()
                    .filter(c -> c.isValidWithUnlocks(plugin.getUnlockedIds()))
                    .count();
            String typeHeader = type + " (" + typeValidCount + "/" + list.size() + ")";

            JPanel categoryContent = new JPanel();
            categoryContent.setLayout(new BoxLayout(categoryContent, BoxLayout.Y_AXIS));
            categoryContent.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (Challenge challenge : list) {
                boolean meetsRequirements = challenge.isValidWithUnlocks(plugin.getUnlockedIds());

                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel textLabel = new JLabel(challenge.getDisplayName());
                textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                if (!meetsRequirements) {
                    textLabel.setForeground(new Color(170, 60, 60));
                } else {
                    textLabel.setForeground(new Color(70, 167, 32));
                }

                applyTooltipChallenge(textLabel, challenge);

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
                        met = req.isMet(plugin);
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

    private void applyTooltipChallenge(JLabel label, Challenge challenge) {
        StringBuilder sb = new StringBuilder("<html>");

        sb.append("<b>")
                .append(challenge.getDisplayName())
                .append("</b><br>")
                .append(challenge.getDescription());

        List<AppearRequirement> reqs = challenge.getRequirements();
        if (reqs != null && !reqs.isEmpty()) {
            sb.append("<br><br><b>Requirements:</b><br>");

            for (AppearRequirement req : reqs) {
                boolean met = false;

                try {
                    if (plugin != null) {
                        met = req.isMet(plugin);
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
                return null;
            }
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
