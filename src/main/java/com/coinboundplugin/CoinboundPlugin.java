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

import com.coinboundplugin.overlays.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.coinboundplugin.data.PackChoiceState;
import com.google.inject.Provides;
import com.coinboundplugin.data.UnlockType;
import com.coinboundplugin.enforcement.*;
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
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;

import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.ImageUtil;

import static net.runelite.client.util.QuantityFormatter.formatNumber;

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

    private final Map<Skill, Integer> previousXp = new EnumMap<>(Skill.class);

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
    CardPickOverlay cardPickOverlay;

    @Inject
    ShopLockOverlay shopLockOverlay;

    @Inject
    SpellBookLockOverlay spellTeleportLockOverlay;

    @Inject
    ItemLockOverlay itemLockOverlay;

    @Inject
    ProtectionPrayerLockOverlay protectionPrayerLockOverlay;

    @Inject
    SpecialAttackOverlay specialAttackOverlay;

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

    public final WorldPoint JailStartingPosition =
            new WorldPoint(3297, 3124, 0);
    public SetupStage gamemodeSetupState = SetupStage.DropAllItems;

    public enum SetupStage {
        DropAllItems,
        GetFlyers,
        SetupComplete
    }

    public boolean isItemBlocked(int itemId) {
        return false; //TODO: Check if some items need to have a lock icon overlay
    }

    public List<Integer> ALCHEMY_WIDGETS = List.of(14286892, 14286869);
    public List<Integer> teleportWidgetIDs = List.of(
            // Normal spellbook
            14286874, //Lumbridge
            14286871, //Varrock
            14286877, //Falador
            14286879, //House
            14286882, //Camelot
            14286884, //Kourend
            14286889, //Ardougne
            14286891, //Civitas
            14286895, //Watchtower
            14286902, //Trollheim
            14286905, //Ape atoll
            14286928, //Boat
            // Ancient spellbook
            14286945, //Paddewwa
            14286946, //Senntisten
            14286947, //Kharyrll
            14286948, //Lassar
            14286949, //Dareeyak
            14286950, //Carrallangar
            14286921, //Target
            14286951, //Annakarl
            14286952, //Ghorrock
            //Lunar spellbook
            14286961, //Moonclan
            14286962, //Moonclan group
            14286997, //ourania
            14286965, //waterbirth
            14286966, //waterbrith group
            14286969, //barbarian
            14286970, //barbarian group
            14286973, //khazard
            14286974,  //khazard group
            14286981, //fishing guild
            14286921, //target
            14286982, //fishing guild group
            14286984, //catherby
            14286985,  //catherby group
            14286987, //ice plateau
            14286988,  //ice plateau group
            //Arceuus spellbook
            14287000, //library
            14287004, //draynor
            14287016, //battlefront
            14287006, //mind altar
            14287007, //respawn
            14287008, //salve graveyard
            14287009, //frankenstrain castle
            14287010, //west ardy
            14287011, //harmony
            14287012, //cemetary
            14287014, //barrows
            14286921, //target
            14287015 //ape atoll dungeon
    );

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
        overlayManager.add(cardPickOverlay);
        overlayManager.add(spellTeleportLockOverlay);
        overlayManager.add(itemLockOverlay);
        overlayManager.add(shopLockOverlay);
        overlayManager.add(protectionPrayerLockOverlay);
        overlayManager.add(specialAttackOverlay);
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

            SetupCardButton(index, option.getDisplayName(), option.getDisplayType(), option.getDescription(), image, option);
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

    void SetupCardButton(int buttonIndex, String unlockName, String typeName, String description, BufferedImage image, PackOption option) {
        cardPickOverlay.setButton(buttonIndex, unlockName, typeName, description, image, () -> {
            clientThread.invoke(() -> onPackOptionSelected(option));
        });
    }

    private void RefreshAllBlockers() {
        skillBlocker.refreshAll();
        equipmentSlotBlocker.refreshAll();
        questBlocker.refreshAll();
        clientThread.invoke(inventoryBlocker::redrawInventory);

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
        overlayManager.remove(cardPickOverlay);
        overlayManager.remove(spellTeleportLockOverlay);
        overlayManager.remove(itemLockOverlay);
        overlayManager.remove(shopLockOverlay);
        overlayManager.remove(protectionPrayerLockOverlay);
        overlayManager.remove(specialAttackOverlay);
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
        if (Objects.equals(event.getKey(), "packsBought") || Objects.equals(event.getKey(), "unlockedIds") || Objects.equals(event.getKey(), "peakWealth"))
            RefreshAllBlockers();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {

        Debug("Container changed");
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

        currentCoins = getCoinsInInventory();
        Debug("Current coins: " + currentCoins);

        long prevPeak = config.peakWealth();
        if (currentCoins <= prevPeak)
            return;

        int packIndex = getPackBought() + getAvailablePacksToBuy() + 1;

        int reached = 0;
        long lastReq = 0;

        // count how many thresholds are now reachable
        while (currentCoins >= (lastReq = peakCoinsRequiredForPack(packIndex))) {
            // only count ones you haven't already "passed" before
            if (lastReq > prevPeak)
                reached++;

            packIndex++;
        }

        if (reached > 1) {
            config.peakWealth(currentCoins);
            ShowPluginChat(
                    "<col=329114><b>" + reached + " new brackets reached!</b></col> Latest: "
                            + formatNumber(lastReq) + " gp. You can open new booster packs!",
                    3924
            );
        } else if (reached == 1) {
            config.peakWealth(currentCoins);
            ShowPluginChat(
                    "<col=329114><b>New bracket reached!</b></col> "
                            + formatNumber(lastReq) + " gp. You can open new booster pack!",
                    3924
            );
        }
    }

    public void setFillerItemsShortAmount(int amount) {
        fillerItemsShort = amount;
        if (fillerItemsShort <= 0 && gamemodeSetupState == SetupStage.GetFlyers) {
            ShowPluginChat("<col=329114><b>Inventory filled! </b></col> You're now 'free' to start your adventure!", 3924);
            gamemodeSetupState = SetupStage.SetupComplete;
            configManager.setConfiguration(CoinboundConfig.GROUP, "setupStage", gamemodeSetupState.name());
            RefreshAllBlockers();
        }
    }

    public long getCoinsInInventory() {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        Debug("inventory: " + inventory);
        if (inventory == null) {
            return 0;
        }

        for (Item item : inventory.getItems()) {
            if (item.getId() == ItemID.COINS) {
                Debug("coins found: ");
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
            ShowPluginChat("<col=ff0000><b>" + skill.getName() + " locked!</b></col> You've earned XP in " + skill.getName() + " but did not have the skill unlocked!", 2394);
            ShowPluginChat("You now have a total of " + newValue + " illegal XP.", -1);
            return;
        }
    }

    public long peakCoinsRequiredForPack(int packIndex) {
        double A = 0.45;
        double B = 3.1;
        double C = 6.0;

        return (long) Math.floor(A * Math.pow(packIndex + C, B));
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

    public void onBuyPackClicked() {
        if (clientThread == null || packChoiceState == PackChoiceState.PACKGENERATED)
            return;

        clientThread.invoke(() ->
        {
            if (getAvailablePacksToBuy() < 1)
                return;


            if (!statsInitialized) {
                if (swingPanel != null) {
                    swingPanel.refresh();
                }
                return;
            }

            if (!generatePackOptions()) {
                ShowPluginChat("<col=ff0000><b>Cant make a pack!</b></col> There are 3 or less available cards (with requirements met)!", 2394);
                return;
            }
            PackBoughtSuccess();
        });
    }

    private void PackBoughtSuccess() {
        config.packsBought(config.packsBought() + 1);

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
    }

    public void onPackOptionSelected(PackOption option) {
        clientThread.invoke(() ->
        {
            ShowPluginChat("<col=329114><b>" + option.getDisplayName() + " unlocked! </b></col> ", 2308);
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

    public void toggleUnlock(String unlockID) {
        if (isUnlocked(unlockID)) {
            removeUnlock(unlockID);
        } else {
            unlock(unlockID);
        }
    }

    public void unlock(String unlockID) {
        if (unlockedIds.add(unlockID)) {
            saveUnlocked();
            RefreshAllBlockers();
        }
    }

    public String removeUnlock(String unlockID) {

        if (unlockedIds.isEmpty())
            return null;

        if (!unlockedIds.contains(unlockID))
            return null;

        unlockedIds.remove(unlockID);
        saveUnlocked();

        // Refresh UI and blockers
        RefreshAllBlockers();

        // Get the unlock's display name
        Unlock unlock = unlockRegistry.get(unlockID);
        return unlock != null ? unlock.getDisplayName() : unlockID;
    }

    public String removeRandomUnlock() {
        if (unlockedIds.isEmpty()) {
            return null;
        }

        // Convert to list to enable random selection
        List<String> unlockList = new ArrayList<>(unlockedIds);

        // Pick random unlock
        String randomUnlockId = unlockList.get(random.nextInt(unlockList.size()));

        return removeUnlock(randomUnlockId);
    }

    private boolean generatePackOptions() {
        List<Unlock> locked = unlockRegistry.getAll().stream()
                .filter(u -> !unlockedIds.contains(u.getId()))
                .filter(this::canAppearAsPackOption)
                .collect(Collectors.toList());

        Collections.shuffle(locked);

        int optionCount = Math.min(4, locked.size());
        Set<UnlockType> usedUnlockTypes = new HashSet<>();

        currentPackOptions = new ArrayList<>(optionCount);
        for (int i = 0; i < optionCount; i++) {
            Unlock unlock = pickUnlockWithDiversityBias(locked, usedUnlockTypes);
            if (unlock == null)
                break;

            usedUnlockTypes.add(unlock.getType());
            currentPackOptions.add(new UnlockPackOption(unlock));
        }

        int count = currentPackOptions.size();

        if (count < 4)
            return false;

        packChoiceState = PackChoiceState.PACKGENERATED;
        SetupCardButtons();

        savePackOptionsToConfig();
        return true;
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

