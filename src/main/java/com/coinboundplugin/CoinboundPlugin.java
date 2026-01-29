package com.coinboundplugin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.util.*;
import javax.inject.Inject;
import javax.swing.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.coinboundplugin.overlays.CardPickOverlay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coinboundplugin.data.PackChoiceState;
import com.google.inject.Provides;
import com.coinboundplugin.data.UnlockType;
import com.coinboundplugin.enforcement.*;
import com.coinboundplugin.overlays.FogMapOverlay;
import com.coinboundplugin.overlays.FogOverlay;
import com.coinboundplugin.overlays.CoinboundInfoboxOverlay;
import com.coinboundplugin.pack.PackOption;
import com.coinboundplugin.pack.SerializablePackOption;
import com.coinboundplugin.pack.UnlockPackOption;
import com.coinboundplugin.requirements.AppearRequirement;
import com.coinboundplugin.unlocks.*;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
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
        name = "Coinbound game mode"
)
public class CoinboundPlugin extends Plugin {
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
    private CoinboundConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private SkillIconManager skillIconManager;

    private Map<Skill, Integer> previousXp = new EnumMap<>(Skill.class);

    private final CoinboundInfoboxOverlay overlay = new CoinboundInfoboxOverlay(this);

    @Inject
    private InventoryBlocker inventoryBlocker;

    @Inject
    private MenuOptionBlocker teleportBlocker;

    @Inject
    private InventoryFillerTooltip inventoryFillerTooltip;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private FogOverlay fogOverlay;

    @Inject
    private FogMapOverlay fogMapOverlay;

    @Inject
    CardPickOverlay cardPickOverlay;

    private CoinboundPanel swingPanel;
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

    public int getPackBought() {
        return config.packsBought();
    }

    public int getPeakCoins() {
        return config.peakWealth();
    }

    public boolean isInMemberWorld() {
        return client.getWorldType().contains(WorldType.MEMBERS);
    }

    public int replaceItemID = ItemID.LEAFLET_DROPPER_FLYER;
    public long currentCoins = 0;
    public int fillerItemsShort;

    public final WorldPoint WorldOrgin =
            new WorldPoint(3310, 3180, 0);
    public final WorldPoint JailStartingPosition =
            new WorldPoint(3297, 3124, 0);
    public int wanderRadius = 30;
    private boolean wasInside = true;
    public SetupStage gamemodeSetupState = SetupStage.DropAllItems;

    public enum SetupStage {
        DropAllItems,
        GetFlyers,
        GoToJail,
        SetupComplete
    }

    @Provides
    CoinboundConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CoinboundConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        //Setup all unlockable stuff
        unlockRegistry = new UnlockRegistry();
        UnlockDefinitions.registerAll(unlockRegistry, skillIconManager, this);

        String setupStr = config.setupStage();
        try {
            gamemodeSetupState = SetupStage.valueOf(setupStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            gamemodeSetupState = SetupStage.DropAllItems;
        }

        overlayManager.add(overlay);
        overlayManager.add(fogOverlay);
        overlayManager.add(fogMapOverlay);
        overlayManager.add(cardPickOverlay);
        cardPickOverlay.start();

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
        swingPanel = new CoinboundPanel(this);
        navButton = NavigationButton.builder()
                .tooltip("Roguelite")
                .icon(ImageUtil.loadImageResource(getClass(), "/icon.png"))
                .panel(swingPanel)
                .build();

        clientToolbar.addNavigation(navButton);

        log.debug("Roguelite plugin started!");
    }

    void SetupCardButtons() {
        int index = 0;
        for (PackOption option : getCurrentPackOptions()) {
            Unlock unlock = ((UnlockPackOption) option).getUnlock();
            UnlockIcon icon = unlock.getIcon();
            BufferedImage image = getBufferedImageFromIcon(icon);

            SetupCardButton(index, option.getDisplayName(), option.getDisplayType(), image, option);
            index++;
        }
    }

    private BufferedImage getBufferedImageFromIcon(UnlockIcon icon) {
        if (icon == null) {
            return null;
        }
        if (icon instanceof ImageUnlockIcon) {
            Icon swingIcon = ((ImageUnlockIcon) icon).getIcon();
            if (swingIcon instanceof ImageIcon) {
                Image img = ((ImageIcon) swingIcon).getImage();
                if (img instanceof BufferedImage) {
                    return (BufferedImage) img;
                }
                // Convert Image to BufferedImage
                BufferedImage buffered = new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g = buffered.createGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
                return buffered;
            }
        }
        return null;
    }

    void SetupCardButton(int buttonIndex, String unlockName,String typeName, BufferedImage image, PackOption option) {
        cardPickOverlay.setButton(buttonIndex, unlockName , typeName , image, () -> {
            clientThread.invoke(() -> onPackOptionSelected(option));
        });
    }

    private void RefreshAllBlockers() {
        skillBlocker.refreshAll();
        equipmentSlotBlocker.refreshAll();
        questBlocker.refreshAll();
        clientThread.invoke(inventoryBlocker::redrawInventory);
        wanderRadius = GetWanderRange();

        if (swingPanel != null)
            swingPanel.refresh();
    }

    int GetWanderRange() {
        if (isUnlocked("HouseArrestGone"))
            return -1;

        int baseRange = 60;
        int extraRangePerUnlock = 200;

        int expansionsUnlocked = (int) unlockedIds.stream()
                .filter(id -> id.startsWith("HouseArrestRange"))
                .count();

        return baseRange + (expansionsUnlocked * extraRangePerUnlock);
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
            SetupCardButtons();
        }
    }

    @Override
    protected void shutDown() throws Exception {
        log.debug("Roguelite plugin stopped!");
        previousXp.clear();

        overlayManager.remove(overlay);
        overlayManager.remove(fogOverlay);
        overlayManager.remove(fogMapOverlay);
        overlayManager.remove(cardPickOverlay);
        cardPickOverlay.stop();

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

        //TODO: Clear inventory blocker (it clears on panel switch but still)
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

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
            return;
        }
        if (gamemodeSetupState == SetupStage.DropAllItems) {
            ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
            if (inventory != null) {
                int itemCount = (int) Arrays.stream(inventory.getItems())
                        .filter(item -> item.getId() != -1)
                        .count();
                Debug("Items in inventory: " + itemCount);
                if (itemCount < 1) {
                    gamemodeSetupState = SetupStage.GetFlyers;
                    configManager.setConfiguration(CoinboundConfig.GROUP, "setupStage", gamemodeSetupState.name());
                    ShowPluginChat("<col=329114><b>All items dropped! </b></col> Please go to the Al Kharid flyererer and use the drop-trick to get flyers to fill up your inventory.", 3924);
                }
            }
            return;
        }

        //TODO: Show all notifications instead of only the first if multiple brackets are passed
        currentCoins = getCoinsInInventory();
        Debug("currentCoins " + currentCoins);
        int coinsRequiredForNextPack = (int) peakCoinsRequiredForPack(getPackBought() + getAvailablePacksToBuy() + 1);
        if (currentCoins >= coinsRequiredForNextPack) {
            config.peakWealth(currentCoins);
            ShowPluginChat("<col=329114><b>Bracket " + coinsRequiredForNextPack + " gp reached! </b></col> You can open a new booster pack!", 3924);
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        if (gamemodeSetupState == SetupStage.GoToJail) {
            Player player = client.getLocalPlayer();
            if (player == null || WorldOrgin == null) {
                return;
            }
            WorldPoint pos = player.getWorldLocation();
            int dist = pos.distanceTo(JailStartingPosition);
            if (dist < 2) {
                gamemodeSetupState = SetupStage.SetupComplete;
                configManager.setConfiguration(CoinboundConfig.GROUP, "setupStage", gamemodeSetupState.name());
                ShowPluginChat("<col=329114><b>Welcome to Coinbound Roguelite Mode! </b></col> Your adventure begins now.", 3924);
                RefreshAllBlockers();
            }
            return;
        }
        if (gamemodeSetupState != CoinboundPlugin.SetupStage.SetupComplete)
            return;

        //-1 is all areas unlocked
        if (wanderRadius == -1)
            return;
        Player player = client.getLocalPlayer();
        if (player == null || WorldOrgin == null) {
            return;
        }

        WorldPoint pos = player.getWorldLocation();
        if (pos.getPlane() != 0) {
            wasInside = true; // treat as always allowed
            return;
        }

        int dist = pos.distanceTo(WorldOrgin);

        boolean isInside = dist <= wanderRadius;

        if (wasInside && !isInside) {
            ShowPluginChat("<col=ff0000><b>Your house arrest device starts beeping!</b></col> Return closer to Lumbridge or unlock more distance.", 2394);
        } else if (!wasInside && isInside) {
            ShowPluginChat("Your house arrest device stops beeping.", -1);
        }

        wasInside = isInside;
    }

    public void setFillerItemsShortAmount(int amount) {
        fillerItemsShort = amount;
        if (fillerItemsShort <= 0 && gamemodeSetupState == SetupStage.GetFlyers) {
            gamemodeSetupState = SetupStage.GoToJail;
            configManager.setConfiguration(CoinboundConfig.GROUP, "setupStage", gamemodeSetupState.name());
            ShowPluginChat("<col=329114><b>Inventory filled! </b></col> Please head to the Al Kharid jail (the one in Shantey pass) to start your adventure.", 3924);
        }
    }

    public long getCoinsInInventory() {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory == null) {
            return 0;
        }

        for (Item item : inventory.getItems()) {
            if (item.getId() == ItemID.COINS) {
                return item.getQuantity();
            }
        }

        return 0;
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

    public long peakCoinsRequiredForPack(int packIndex) {
        // packIndex starts at 1
        double A = 5.0;
        double B = 2.1;

        return (long) Math.floor(A * Math.pow(packIndex, B));
    }

    public int getTotalUnlockedPacks(long peakCoins) {
        int pack = 1;

        while (true) {
            if (peakCoins < peakCoinsRequiredForPack(pack)) {
                return pack - 1;
            }
            pack++;
        }
    }

    public int getAvailablePacksToBuy() {
        int unlocked = getTotalUnlockedPacks(config.peakWealth());
        return Math.max(0, unlocked - config.packsBought());
    }

    private void showChatMessage(String message) {
        client.addChatMessage(net.runelite.api.ChatMessageType.GAMEMESSAGE, "", message, null);
    }

    public void onBuyPackClicked() {
        if (clientThread == null || packChoiceState == PackChoiceState.PACKGENERATED)
            return;

        clientThread.invoke(() ->
        {
            if (getAvailablePacksToBuy() < 1)
                return;

            config.packsBought(config.packsBought() + 1);

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
            ShowPluginChat("<col=329114><b>"+option.getDisplayName()+" unlocked! </b></col> ", 2308);
            option.onChosen(this);

            packChoiceState = PackChoiceState.NONE;
            currentPackOptions = List.of();

            //Clear active config
            configManager.setConfiguration(CoinboundConfig.GROUP, "currentPackOptions", "[]");
            configManager.setConfiguration(CoinboundConfig.GROUP, "packChoiceState", "NONE");
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
                CoinboundConfig.GROUP,
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
        SetupCardButtons();

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
        configManager.setConfiguration(CoinboundConfig.GROUP, "currentPackOptions", json);
        configManager.setConfiguration(CoinboundConfig.GROUP, "packChoiceState", packChoiceState.name());
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


    public void ShowPluginChat(String message, int soundEffect) {
        client.addChatMessage(
                ChatMessageType.ENGINE,
                "",
                "[<col=6069df>Roguelite Mode</col>] " + message,
                null
        );
        if (soundEffect != -1)
            client.playSoundEffect(soundEffect);
    }
}

