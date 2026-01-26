package com.rogueliteplugin;

import java.lang.reflect.Type;
import java.util.*;
import javax.inject.Inject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rogueliteplugin.data.PackChoiceState;
import com.google.inject.Provides;
import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.enforcement.*;
import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.SerializablePackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.unlocks.*;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.callback.ClientThread;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;

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

    public Client getClient() {
        return client;
    }

    @Inject
    private ClientThread clientThread;

    @Inject
    private ItemManager itemManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private SkillBlocker skillBlocker;

    @Inject
    private QuestBlocker questBlocker;

    @Inject
    public EquipmentSlotBlocker equipmentSlotBlocker;

    @Inject
    private ShopBlocker shopBlocker;

    @Inject
    private EventBus eventBus;

    @Inject
    private RogueliteConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private SkillIconManager skillIconManager;

    @Inject
    private NewItemDetection newItemDetection;

    private Map<Skill, Integer> previousXp = new EnumMap<>(Skill.class);

    private final RogueliteInfoboxOverlay overlay = new RogueliteInfoboxOverlay(this);

    @Inject
    private InventoryBlocker inventoryBlocker;

    @Inject
    private MenuOptionBlocker teleportBlocker;

    @Inject
    private InventoryFillerTooltip inventoryFillerTooltip;

    @Inject
    private ClientToolbar clientToolbar;

    private RoguelitePanel swingPanel;
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

    public UnlockRegistry getUnlockRegistry() {
        return unlockRegistry;
    }

    private final Set<String> unlockedIds = new HashSet<>();

    public Set<String> getUnlockedIds() {
        return unlockedIds;
    }

    public boolean isInMemberWorld() {
        return client.getWorldType().contains(WorldType.MEMBERS);
    }

    public String getSeenItemIds() {
        return config.seenItemIds();
    }

    public void setSeenItemsIds(String value) {
        config.seenItemIds(value);
    }

    int cardPackCost = 8;
    public int replaceItemID = ItemID.LEAFLET_DROPPER_FLYER;

    @Provides
    RogueliteConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RogueliteConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        //Setup all unlockable stuff
        unlockRegistry = new UnlockRegistry();
        UnlockDefinitions.registerAll(unlockRegistry, skillIconManager, this);

        overlayManager.add(overlay);

        newItemDetection.setPlugin(this);
        eventBus.register(newItemDetection);

        eventBus.register(skillBlocker);
        eventBus.register(questBlocker);
        eventBus.register(equipmentSlotBlocker);

        eventBus.register(shopBlocker);
        eventBus.register(inventoryBlocker);
        eventBus.register(teleportBlocker);
        overlayManager.add(inventoryFillerTooltip);
        loadUnlocked();
        loadPackOptionsFromConfig();
        RefreshAllBlockers();

        //Check if HP is unlocked (it should always be unlocked)
        if (!unlockedIds.contains("SKILL_HITPOINTS"))
            unlock("SKILL_HITPOINTS");

        //Build the panel
        swingPanel = new RoguelitePanel(this);
        navButton = NavigationButton.builder()
                .tooltip("Roguelite")
                .icon(ImageUtil.loadImageResource(getClass(), "/icon.png"))
                .panel(swingPanel)
                .build();

        clientToolbar.addNavigation(navButton);
        log.debug("Roguelite plugin started!");
    }

    private void RefreshAllBlockers() {
        skillBlocker.refreshAll();
        equipmentSlotBlocker.refreshAll();
        questBlocker.refreshAll();
        clientThread.invoke(inventoryBlocker::redrawInventory);
        if (swingPanel != null)
            swingPanel.refresh();
    }

    private void loadPackOptionsFromConfig() {
        String stateStr = config.packChoiceState();
        try {
            packChoiceState = PackChoiceState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            packChoiceState = PackChoiceState.NONE;
        }

        if (packChoiceState != PackChoiceState.PACKGENERATED) {
            return;
        }

        Debug("Player was choosing cards, loading from config");

        String json = config.currentPackOptions();
        Type listType = new TypeToken<List<SerializablePackOption>>() {
        }.getType();
        List<SerializablePackOption> serialized = new Gson().fromJson(json, listType);

        if (serialized != null && !serialized.isEmpty()) {
            currentPackOptions = serialized.stream()
                    .map(s -> {
                        Unlock unlock = unlockRegistry.get(s.getUnlockId());
                        return new UnlockPackOption(unlock);
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected void shutDown() throws Exception {
        log.debug("Roguelite plugin stopped!");
        previousXp.clear();
        overlayManager.remove(overlay);

        eventBus.unregister(newItemDetection);
        eventBus.unregister(skillBlocker);
        eventBus.unregister(questBlocker);
        eventBus.unregister(equipmentSlotBlocker);
        eventBus.unregister(shopBlocker);
        eventBus.unregister(inventoryBlocker);
        eventBus.unregister(teleportBlocker);
        overlayManager.remove(inventoryFillerTooltip);
        equipmentSlotBlocker.clearAll();
        skillBlocker.clearAll();
        questBlocker.clearAll();
        //TODO: Clear inventory blocker
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("rogueliteplugin"))
            return;
        log.debug("Runelite config changes!");

        //Only refresh all content on actual changes
        if (Objects.equals(event.getKey(), "currentPoints"))
            return;
        if (Objects.equals(event.getKey(), "illegalXPGained"))
            return;
        if (Objects.equals(event.getKey(), "seenItemIds"))
            return;

        RefreshAllBlockers();
    }

    private static final int EXPECTED_SKILL_COUNT = 23;
    public boolean statsInitialized = false;

    @Subscribe
    public void onStatChanged(StatChanged event) {
        Skill skill = event.getSkill();

        int xp = event.getXp();
        Integer previous = previousXp.put(skill, xp);

        Debug(xp + " xp gained in " + skill.getName() + " size: " + previousXp.size());
        if (!statsInitialized && previousXp.size() >= EXPECTED_SKILL_COUNT) {
            statsInitialized = true;
            if (swingPanel != null) {
                Debug("Refreshing panel due to login");
                swingPanel.refresh();
            }
        }

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
    }

    public int getAvailablePacks() {
        return getCurrentPoints() / cardPackCost; //Packs cost 8 points each
    }

    public int getCurrentPoints() {
        return config.currentPoints();
    }

    public void newItemAcquired(String name, int points) {
        ShowPluginChat("<col=00ff00><b>" + name + " acquired!</col></b> gained " + points + " points.",
                false
        );
        config.currentPoints(config.currentPoints() + points);
    }

    private void showChatMessage(String message) {
        client.addChatMessage(net.runelite.api.ChatMessageType.GAMEMESSAGE, "", message, null);
    }

    public void onBuyPackClicked() {
        if (clientThread == null || packChoiceState == PackChoiceState.PACKGENERATED)
            return;

        clientThread.invoke(() ->
        {
            if (getAvailablePacks() < 1)
                return;

            config.currentPoints(config.currentPoints() - cardPackCost);

            if (!statsInitialized) {
                //TODO: Notify user to relog
                if (swingPanel != null) {
                    swingPanel.refresh();
                }
                return;
            }
            generatePackOptions();

            // Refresh panel UI
            if (swingPanel != null) {
                swingPanel.refresh();
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
            //Unlock unlock = ((UnlockPackOption) option).getUnlock();
            option.onChosen(this);

            packChoiceState = PackChoiceState.NONE;
            currentPackOptions = List.of();

            //Clear active config
            configManager.setConfiguration(RogueliteConfig.GROUP, "currentPackOptions", "[]");
            configManager.setConfiguration(RogueliteConfig.GROUP, "packChoiceState", "NONE");

            if (swingPanel != null) {
                swingPanel.refresh();
            }
        });
    }

    public boolean canAppearAsPackOption(Unlock unlock) {
        if (unlock == null || unlockRegistry == null) {
            return false;
        }

        // Already unlocked → should NOT appear as pack option
        if (isUnlocked(unlock)) {
            return false;
        }

        List<AppearRequirement> reqs = unlock.getRequirements();
        if (reqs == null || reqs.isEmpty()) {
            return true;
        }

        for (AppearRequirement req : reqs) {
            try {
                if (!req.isMet(this, this.getUnlockedIds())) {
                    return false;
                }
            } catch (Exception e) {
                // Defensive: never let UI crash because of requirements
                return false;
            }
        }
        return true;
    }

    public boolean isSkillUnlocked(Skill skill) {
        return unlockedIds.contains("SKILL_" + skill.name());
    }

    private void loadUnlocked() {
        unlockedIds.clear();

        String raw = config.unlockedIds();
        if (!raw.isEmpty()) {
            unlockedIds.addAll(Arrays.asList(raw.split(",")));
        }
    }

    private void saveUnlocked() {
        configManager.setConfiguration(
                RogueliteConfig.GROUP,
                "unlockedIds",
                String.join(",", unlockedIds)
        );
    }

    public boolean isUnlocked(String unlockId) {
        Unlock unlock = unlockRegistry.get(unlockId);
        if (unlock == null) {
            return false;
        }

        return isUnlocked(unlock);
    }

    public boolean isUnlocked(Unlock unlock) {
        return unlockedIds.contains(unlock.getId());
    }

    public void unlock(String unlockID) {
        if (unlockedIds.add(unlockID)) {
            saveUnlocked();
            RefreshAllBlockers();
        }
    }

    public String removeRandomUnlock() {
        if (unlockedIds.isEmpty()) {
            return null;
        }

        // Convert to list to enable random selection
        List<String> unlockList = new ArrayList<>(unlockedIds);

        // Pick random unlock
        String randomUnlockId = unlockList.get(random.nextInt(unlockList.size()));

        // Remove it
        unlockedIds.remove(randomUnlockId);
        saveUnlocked();

        // Refresh UI and blockers
        RefreshAllBlockers();

        // Get the unlock's display name
        Unlock unlock = unlockRegistry.get(randomUnlockId);
        return unlock != null ? unlock.getDisplayName() : randomUnlockId;
    }

    private void generatePackOptions() {
        List<Unlock> locked = unlockRegistry.getAll().stream()
                .filter(u -> !unlockedIds.contains(u.getId()))
                .filter(this::canAppearAsPackOption)
                .collect(Collectors.toList());
        Collections.shuffle(locked);

        int optionCount = Math.min(4, locked.size());
        Set<UnlockType> usedUnlockTypes = new HashSet<>();

        currentPackOptions = IntStream.range(0, optionCount)
                .mapToObj(i -> {
                    Unlock unlock = pickUnlockWithDiversityBias(locked, usedUnlockTypes);
                    usedUnlockTypes.add(unlock.getType());
                    return new UnlockPackOption(unlock);

                })
                .collect(Collectors.toList());


        packChoiceState = PackChoiceState.PACKGENERATED;

        // Save to config for when users reload client while cards are generated
        savePackOptionsToConfig();
    }

    private void savePackOptionsToConfig() {
        List<SerializablePackOption> serializable = currentPackOptions.stream()
                .map(opt -> {
                    UnlockPackOption unlockOpt = (UnlockPackOption) opt;
                    return new SerializablePackOption(
                            unlockOpt.getUnlock().getId()
                    );
                })
                .collect(Collectors.toList());

        String json = new Gson().toJson(serializable);
        configManager.setConfiguration(RogueliteConfig.GROUP, "currentPackOptions", json);
        configManager.setConfiguration(RogueliteConfig.GROUP, "packChoiceState", packChoiceState.name());
    }

    private Unlock pickUnlockWithDiversityBias(
            List<Unlock> candidates,
            Set<UnlockType> usedTypes) {
        List<Unlock> preferred = candidates.stream()
                .filter(u -> !usedTypes.contains(u.getType()))
                .collect(Collectors.toList());

        List<Unlock> pool = preferred.isEmpty() ? candidates : preferred;
        return pool.get(random.nextInt(pool.size()));
    }

    public void Debug(String textToDebug) {
        log.debug(textToDebug);
    }

    public void ShowPluginChat(String message, boolean isError) {
        client.addChatMessage(
                ChatMessageType.ENGINE,
                "",
                "[<col=6069df>Roguelite Mode</col>] " + message,
                null
        );
        if (isError)
            client.playSoundEffect(2394);
    }
}