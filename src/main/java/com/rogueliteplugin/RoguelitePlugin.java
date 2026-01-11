package com.rogueliteplugin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.inject.Inject;
import javax.swing.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.inject.Provides;
import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.unlocks.*;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SpriteManager;

import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
        name = "Roguelite game mode"
)
public class RoguelitePlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private SpriteManager spriteManager;
    public SpriteManager getSpriteManager()
    {
        return spriteManager;
    }

    @Inject
    private ClientThread clientThread;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private SkillBlocker skillBlocker;

    @Inject
    private EventBus eventBus;

    @Inject
    private RogueliteConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private SkillIconManager skillIconManager;

    private static final int XP_PER_POINT = 50;
    private Map<Skill, Integer> previousXp = new EnumMap<>(Skill.class);

    private long totalXpGained;
    private int totalPoints;
    private int pointsSpent;

    private final RogueliteInfoboxOverlay overlay = new RogueliteInfoboxOverlay(this);

    @Inject
    private ClientToolbar clientToolbar;

    private RoguelitePanel panel;
    private NavigationButton navButton;

    private final Random random = new Random();

    private PackChoiceState packChoiceState = PackChoiceState.NONE;

    public PackChoiceState getPackChoiceState() {
        return packChoiceState;
    }

    private List<PackOption> currentPackOptions = List.of();

    public List<PackOption> getCurrentPackOptions() {
        return currentPackOptions;
    }

    private UnlockRegistry unlockRegistry;
    public UnlockRegistry getUnlockRegistry()
    {
        return unlockRegistry;
    }
    private final Set<String> unlockedIds = new HashSet<>();

    @Provides
    RogueliteConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RogueliteConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay);
        eventBus.register(skillBlocker);

        totalXpGained = config.totalXpGained();
        totalPoints = config.totalPoints();
        pointsSpent = config.pointsSpent();

        //Setup all unlockable stuff
        unlockRegistry = new UnlockRegistry();
        UnlockDefinitions.registerAll(unlockRegistry, skillIconManager, this);
        loadUnlocked();

        panel = new RoguelitePanel(this);
        navButton = NavigationButton.builder()
                .tooltip("Roguelite")
                .icon(ImageUtil.loadImageResource(getClass(), "/icon.png"))
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
        log.debug("Roguelite plugin started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.debug("Roguelite plugin stopped!");
        previousXp.clear();
        overlayManager.remove(overlay);
        eventBus.unregister(skillBlocker);
        skillBlocker.clearAll();
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!RogueliteConfig.GROUP.equals(event.getGroup())) {
            return;
        }
        log.debug("Runelite config changes!");

        skillBlocker.refreshAll();

        if (panel != null) {
            panel.refresh();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            log.debug("Welcome!");
            if (panel != null)
            {
                panel.refresh();
            }
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged event) {
        Skill skill = event.getSkill();

        int xp = event.getXp();

        Integer previous = previousXp.put(skill, xp);
        if (previous == null) {
            return;
        }

        int delta = xp - previous;
        if (delta <= 0) {
            return;
        }

        if (!isSkillUnlocked(skill)) {
            long newValue = delta + config.illegalXPGained();
            config.illegalXPGained(newValue);
            showChatMessage("You've earned XP in " + skill.getName() + " but did not have the skill unlocked!");
            showChatMessage("You now have a total of " + newValue + " illegal XP.");
            return;
        }
        addXp(delta);
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        Player player = client.getLocalPlayer();
        if (player == null)
        {
            return;
        }

        int regionId = player.getWorldLocation().getRegionID();

        if (!isRegionUnlocked(regionId))
        {
            warnLockedRegion(regionId);
        }
    }

    public boolean isInLockedRegion()
    {
        if (client.getLocalPlayer() == null)
        {
            return false;
        }

        int regionId = client.getLocalPlayer()
                .getWorldLocation()
                .getRegionID();

        return !isRegionUnlocked(regionId);
    }

    private int lastWarnedRegion = -1;

    private void warnLockedRegion(int regionId)
    {
        if (regionId == lastWarnedRegion)
        {
            return;
        }

        lastWarnedRegion = regionId;

        client.addChatMessage(
                ChatMessageType.GAMEMESSAGE,
                "",
                "⚠ You are in a locked region.",
                null
        );
    }

    private void addXp(int xp) {
        totalXpGained += xp;
        config.totalXpGained(totalXpGained);

        int xpToNextPoint = config.xpToNextPoint();
        if (xpToNextPoint <= 0)
            xpToNextPoint = 2500; // default fallback

        int newTotalPoints = (int) (totalXpGained / xpToNextPoint);

        if (newTotalPoints > totalPoints) {
            int gained = newTotalPoints - totalPoints;
            totalPoints = newTotalPoints;
            config.totalPoints(totalPoints);
            showChatMessage("You've earned " + gained + " point(s)! Total: " + getCurrentPoints());
        } else if (newTotalPoints < totalPoints) {
            // Something went wrong (config was edited, data corruption, or XP reset)
            int oldtotal = totalPoints;
            totalPoints = newTotalPoints;
            config.totalPoints(totalPoints);

            showChatMessage("You've earned one or more points but the Total points exceeded by config.");
            showChatMessage("Resetting total points to " + newTotalPoints + " (From " + oldtotal + ")");
        }

        if (panel != null) {
            panel.refresh();
        }

        log.debug("Added {} xp, total xp: {}, xp to next point: {}. new total points: {}, current total points: {}", xp, totalXpGained, xpToNextPoint, newTotalPoints, totalPoints);
    }

    private void showChatMessage(String message) {
        client.addChatMessage(net.runelite.api.ChatMessageType.GAMEMESSAGE, "", message, null);
    }

    public int getCurrentPoints() {
        return totalPoints - pointsSpent;
    }

    public void onBuyPackClicked() {
        if (clientThread == null || packChoiceState == PackChoiceState.CHOOSING)
            return;

        clientThread.invoke(() ->
        {
            if (getCurrentPoints() < 1)
                return;
            pointsSpent++;
            config.pointsSpent(pointsSpent);
            generatePackOptions();
            packChoiceState = PackChoiceState.CHOOSING;

            // Refresh panel UI
            if (panel != null) {
                panel.refresh();
            }

            client.addChatMessage(
                    ChatMessageType.GAMEMESSAGE,
                    "",
                    "Bought a pack",
                    null
            );
        });
    }

    public void onPackOptionSelected(PackOption option) {
        clientThread.invoke(() ->
        {
            option.onChosen(this);

            packChoiceState = PackChoiceState.NONE;
            currentPackOptions = List.of();

            if (panel != null) {
                panel.refresh();
            }
        });
    }

    public int getXpToNextPoint() {
        int xpToNextPoint = 1; //fallback for invalid values
        if (config.xpToNextPoint() > 0)
            xpToNextPoint = config.xpToNextPoint();

        return xpToNextPoint - ((int) totalXpGained % xpToNextPoint);
    }

    private void generatePackOptions() {
        List<Unlock> locked = unlockRegistry.getAll().stream()
                .filter(u -> !unlockedIds.contains(u.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(locked);

        currentPackOptions = locked.stream()
                .limit(4)
                .map(UnlockPackOption::new)
                .collect(Collectors.toList());
    }

    public Map<UnlockType, List<Unlock>> getUnlockedByType()
    {
        Map<UnlockType, List<Unlock>> map = new EnumMap<>(UnlockType.class);

        for (Unlock unlock : unlockRegistry.getAll())
        {
            if (unlockedIds.contains(unlock.getId()))
            {
                map.computeIfAbsent(unlock.getType(), t -> new ArrayList<>())
                        .add(unlock);
            }
        }

        return map;
    }

    private void loadUnlocked()
    {
        unlockedIds.clear();

        String raw = config.unlockedIds();
        if (!raw.isEmpty())
        {
            unlockedIds.addAll(Arrays.asList(raw.split(",")));
        }
    }

    private void saveUnlocked()
    {
        configManager.setConfiguration(
                RogueliteConfig.GROUP,
                "unlockedIds",
                String.join(",", unlockedIds)
        );
    }

    public boolean isSkillUnlocked(Skill skill)
    {
        return unlockedIds.contains("skill:" + skill.name());
    }

    public void unlock(Unlock unlock)
    {
        if (unlockedIds.add(unlock.getId()))
        {
            saveUnlocked();

            skillBlocker.refreshAll();

            if (panel != null)
            {
                panel.refresh();
            }
        }
    }

    public boolean isRegionUnlocked(int regionId)
    {
        for (Unlock unlock : unlockRegistry.getAll())
        {
            if (!(unlock instanceof RegionUnlock))
            {
                continue;
            }

            RegionUnlock regionUnlock = (RegionUnlock) unlock;

            if (isUnlocked(unlock) && regionUnlock.containsRegion(regionId))
            {
                return true;
            }
        }

        return false;
    }


    public boolean isUnlocked(Unlock unlock)
    {
        return unlockedIds.contains(unlock.getId());
    }
}