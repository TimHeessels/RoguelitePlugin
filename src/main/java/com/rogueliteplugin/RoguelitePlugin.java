package com.rogueliteplugin;

import java.util.*;
import javax.inject.Inject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.rogueliteplugin.data.ChallengeType;
import com.rogueliteplugin.data.PackChoiceState;
import com.google.inject.Provides;
import com.rogueliteplugin.challenge.*;
import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.enforcement.*;
import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.unlocks.*;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ServerNpcLoot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SpriteManager;

import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;

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
    private SpriteManager spriteManager;

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    @Inject
    private ClientThread clientThread;

    @Inject
    private ItemManager itemManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ChallengeManager challengeManager;

    @Inject
    private SkillBlocker skillBlocker;

    @Inject
    private QuestBlocker questBlocker;

    @Inject
    private EquipmentSlotBlockerOverlay equipmentSlotBlocker;

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

    private static final int XP_PER_POINT = 50;
    private Map<Skill, Integer> previousXp = new EnumMap<>(Skill.class);

    private final RogueliteInfoboxOverlay overlay = new RogueliteInfoboxOverlay(this);

    @Inject
    private InventorySlotPanelOverlay inventoryBlockOverlay; //Old system

    @Inject
    private InventoryBlocker inventoryBlocker;

    @Inject
    private TransportBlocker teleportBlocker;

    @Inject
    private InventoryFillerTooltip inventoryFillerTooltip;

    @Inject
    private TooltipManager tooltipManager;

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

    public UnlockRegistry getUnlockRegistry() {
        return unlockRegistry;
    }

    private ChallengeRegistry challengeRegistry;

    public ChallengeRegistry getChallengeRegistry() {
        return challengeRegistry;
    }

    private final Set<String> unlockedIds = new HashSet<>();

    public Set<String> getUnlockedIds() {
        return unlockedIds;
    }

    @Provides
    RogueliteConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RogueliteConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        //Setup all unlockable stuff
        unlockRegistry = new UnlockRegistry();
        UnlockDefinitions.registerAll(unlockRegistry, skillIconManager, this);

        challengeRegistry = new ChallengeRegistry();
        ChallengeDefinitions.registerAll(challengeRegistry, unlockRegistry, skillIconManager, this);

        //Load current challenge state
        challengeManager.loadFromConfig(config, client, challengeRegistry);
        Debug("Challenge progress: "+challengeManager.getCurrent().getProgress());
        Debug("Challenge goal: "+challengeManager.getCurrent().getGoal());

        overlayManager.add(overlay);
        //overlayManager.add(inventoryBlockOverlay);
        eventBus.register(skillBlocker);
        eventBus.register(questBlocker);
        eventBus.register(equipmentSlotBlocker);
        eventBus.register(shopBlocker);
        eventBus.register(inventoryBlocker);
        eventBus.register(teleportBlocker);
        overlayManager.add(inventoryFillerTooltip);

        loadUnlocked();

        //Check if HP is unlocked (it should always be unlocked)
        if (!unlockedIds.contains("SKILL_HITPOINTS"))
            unlock("SKILL_HITPOINTS");

        //Build the panel
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
        //overlayManager.remove(inventoryBlockOverlay);
        eventBus.unregister(skillBlocker);
        eventBus.unregister(questBlocker);
        eventBus.unregister(equipmentSlotBlocker);
        eventBus.unregister(shopBlocker);
        eventBus.unregister(inventoryBlocker);
        eventBus.unregister(teleportBlocker);
        overlayManager.remove(inventoryFillerTooltip);
        //clientThread.invoke(inventoryBlocker::redrawInventory);

        skillBlocker.clearAll();
        equipmentSlotBlocker.clearAll();
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!RogueliteConfig.GROUP.equals(event.getGroup())) {
            return;
        }
        log.debug("Runelite config changes!");

        skillBlocker.refreshAll();
        equipmentSlotBlocker.refreshAll();
        clientThread.invoke(inventoryBlocker::redrawInventory);
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        ChallengeState state = challengeManager.getCurrent();
        if (state == null)
            return;

        Challenge challenge = state.getChallenge();

        if (!(challenge instanceof WalkChallenge))
            return;

        if (client.getLocalPlayer() == null)
            return;

        WalkChallenge walk = (WalkChallenge) challenge;

        WorldPoint current = client.getLocalPlayer().getWorldLocation();
        int walked = walk.countMovement(current);

        if (walked > 0) {
            challengeManager.increment(walked);
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        ChallengeState state = challengeManager.getCurrent();
        if (state == null)
            return;

        Challenge challenge = state.getChallenge();
        if (!(challenge instanceof OneHpAfterDamageChallenge))
            return;

        if (client.getLocalPlayer() == null)
            return;

        // Only count hitsplats applied to the local player
        if (event.getActor() != client.getLocalPlayer())
            return;

        int damage = event.getHitsplat().getAmount();
        if (damage <= 0)
            return;

        int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
        if (currentHp == 1)
            challengeManager.increment(1);
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
            if (panel != null) {
                Debug("Refreshing panel due to login");
                panel.refresh();
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

        ChallengeState state = challengeManager.getCurrent();
        if (state == null)
            return;

        Challenge challenge = state.getChallenge();

        if (challenge.handleXp(event, state))
            challengeManager.increment(delta);
    }

    @Subscribe
    public void onServerNpcLoot(final ServerNpcLoot event) {
        ChallengeState state = challengeManager.getCurrent();
        if (state == null)
            return;

        Challenge challenge = state.getChallenge();

        //Check drop related challenges (handleNpcLoot is overridden on specific challenges)
        if (challenge.handleNpcLoot(event, state, itemManager))
            challengeManager.increment(1);
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        /*
        if (event.getGroupId() == InterfaceID.INVENTORY) {
            overlayManager.resetOverlay(inventoryBlockOverlay);
        }
         */
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getType() != ChatMessageType.SPAM)
            return;

        Debug("name: " + chatMessage.getMessage());

        ChallengeState state = challengeManager.getCurrent();
        if (state == null)
            return;

        Challenge challenge = state.getChallenge();

        if (!(challenge instanceof CheckForSpamchatChallenge))
            return;

        String pattern = ((CheckForSpamchatChallenge) challenge).getSpamMessage();
        if (chatMessage.getMessage().matches(pattern))
            challengeManager.increment(1);
    }

    private void showChatMessage(String message) {
        client.addChatMessage(net.runelite.api.ChatMessageType.GAMEMESSAGE, "", message, null);
    }

    public void onBuyPackClicked() {
        if (clientThread == null || packChoiceState == PackChoiceState.CHOOSING)
            return;

        clientThread.invoke(() ->
        {
            if (anyChallengeActive())
                return;

            if (!statsInitialized) {
                //TODO: Notify user to relog
                if (panel != null) {
                    panel.refresh();
                }
                return;
            }
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

    public void onPackOptionSelected(PackOption option, int balancedAmount) {
        clientThread.invoke(() ->
        {
            Unlock unlock = ((UnlockPackOption) option).getUnlock();

            if (unlock instanceof CurrencyUnlock) {
                ((CurrencyUnlock) unlock).grant(this);
            } else {
                option.onChosen(this,balancedAmount);
            }

            packChoiceState = PackChoiceState.NONE;
            currentPackOptions = List.of();

            if (panel != null) {
                panel.refresh();
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

    public Map<UnlockType, List<Unlock>> getUnlockedByType() {
        Map<UnlockType, List<Unlock>> map = new EnumMap<>(UnlockType.class);

        for (Unlock unlock : unlockRegistry.getAll()) {
            if (unlockedIds.contains(unlock.getId())) {
                map.computeIfAbsent(unlock.getType(), t -> new ArrayList<>())
                        .add(unlock);
            }
        }

        return map;
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

            skillBlocker.refreshAll();
            equipmentSlotBlocker.refreshAll();

            if (panel != null) {
                panel.refresh();
            }
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
        skillBlocker.refreshAll();
        equipmentSlotBlocker.refreshAll();

        if (panel != null) {
            panel.refresh();
        }

        // Get the unlock's display name
        Unlock unlock = unlockRegistry.get(randomUnlockId);
        return unlock != null ? unlock.getDisplayName() : randomUnlockId;
    }

    public void setActiveChallenge(Challenge activeChallenge,int balancedAmount) {
        challengeManager.startChallenge(activeChallenge, balancedAmount);
    }

    public String getCurrentChallengeFormatted() {
        return challengeManager.getChallengeFormatted();
    }

    public long getCurrentChallengeProgress() {
        return challengeManager.getCurrent().getProgress();
    }

    public long getCurrentChallengeGoal() {
        return challengeManager.getCurrent().getGoal();
    }

    public boolean anyChallengeActive() {
        return challengeManager.hasActiveChallenge();
    }

    private void generatePackOptions() {
        List<Unlock> locked = unlockRegistry.getAll().stream()
                .filter(u -> !unlockedIds.contains(u.getId()))
                .filter(this::canAppearAsPackOption)
                .collect(Collectors.toList());
        Collections.shuffle(locked);

        int optionCount = Math.min(4, locked.size());
        Set<UnlockType> usedUnlockTypes = new HashSet<>();
        Set<ChallengeType> usedChallengeTypes = new HashSet<>();

        currentPackOptions = IntStream.range(0, optionCount)
                .mapToObj(i -> {
                    Unlock unlock = pickUnlockWithDiversityBias(locked, usedUnlockTypes);
                    usedUnlockTypes.add(unlock.getType());

                    Set<String> hypotheticalUnlocks = new HashSet<>(unlockedIds);
                    hypotheticalUnlocks.add(unlock.getId());

                    List<Challenge> validChallenges = challengeRegistry.getAll().stream()
                            .filter(c -> c.isValidWithUnlocks(this, hypotheticalUnlocks))
                            .collect(Collectors.toList());

                    for (int j = 0; j < validChallenges.size(); j++) {
                        Debug(optionCount + "] Valid challenge " + j + ": " + validChallenges.get(j).getDisplayName());
                    }

                    Challenge challenge =
                            pickChallengeWithDiversityBias(validChallenges, usedChallengeTypes);

                    if (challenge != null)
                        usedChallengeTypes.add(challenge.getType());

                    return new UnlockPackOption(unlock, challenge);
                })
                .collect(Collectors.toList());
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

    private Challenge pickChallengeWithDiversityBias(
            List<Challenge> candidates,
            Set<ChallengeType> usedTypes) {
        if (candidates.isEmpty()) return null;

        List<Challenge> preferred = candidates.stream()
                .filter(c -> !usedTypes.contains(c.getType()))
                .collect(Collectors.toList());

        List<Challenge> pool = preferred.isEmpty() ? candidates : preferred;
        return pool.get(random.nextInt(pool.size()));
    }

    public int getBalancedChallengeAmount(int min, int max) {
        Player player = client.getLocalPlayer();
        if (player == null) {
            return min;
        }

        int combatLevel = player.getCombatLevel();
        float normalized = combatLevel / 126f;

        // Steep power curve (fits your anchors)
        float curve = (float) Math.pow(normalized, 6.4);

        int base = min + Math.round(curve * (max - min));

        // ±5% variation
        float jitter = 1f + (random.nextFloat() - 0.5f) * 0.10f * normalized;
        int result = Math.round(base * jitter);

        // Safety clamp
        return Math.max(min, Math.min(max, result));
    }

    public void Debug(String textToDebug) {
        log.debug(textToDebug);
    }

    public int getRerollTokens() {
        return config.rerollTokens();
    }

    public void addRerollTokens(int amount) {
        config.rerollTokens(config.rerollTokens() + amount);
        if (panel != null) {
            panel.refresh();
        }
    }

    public void useRerollToken() {
        clientThread.invokeLater(() -> {
            Debug("Bought reroll");
            config.rerollTokens(config.rerollTokens() - 1);
            Debug("Bought reroll, new token amount: " + config.rerollTokens());
            packChoiceState = PackChoiceState.NONE;
            onBuyPackClicked();
        });
    }

    public int getSkipTokens() {
        return config.skipTokens();
    }

    public void addChallengeSkipTokens(int amount) {
        config.skipTokens(config.skipTokens() + amount);
        if (panel != null) {
            panel.refresh();
        }
    }

    public void UseChallengeSkipToken() {
        clientThread.invokeLater(() -> {
            Debug("Bought challenge skip");
            config.skipTokens(config.skipTokens() - 1);
            Debug("Bought reroll, new token amount: " + config.skipTokens());
            challengeManager.CompleteGoal();
            ShowPluginChat("<col=ff0000><b>Challenge skipped! </b></col> You used a skip-token to skip the current challenge.", false);
            if (panel != null) {
                panel.refresh();
            }
        });
    }

    public void ForfeitChallenge() {
        clientThread.invokeLater(() -> {
            Debug("Forfeit challenge");

            String removedUnlock = removeRandomUnlock();
            if (removedUnlock != null) {
                ShowPluginChat("<col=ff0000><b>Challenge skipped! </b></col> You have forfeited your current challenge and lost the unlock: " + removedUnlock, false);
            } else {
                showChatMessage("You have forfeited your current challenge but had no unlocks left to lose.");
                ShowPluginChat("<col=ff0000><b>Challenge skipped! </b></col> You have forfeited your current challenge but had no unlocks left to lose.", false);
            }
            challengeManager.CompleteGoal();
            if (panel != null) {
                panel.refresh();
            }
        });
    }

    List<String> EQUIP_MENU_OPTIONS = Arrays.asList("Wield", "Wear", "Equip", "Hold", "Ride", "Chill");
    List<String> EAT_MENU_OPTIONS = Arrays.asList("Eat", "Consume");  //TODO: Check if more needed
    List<String> POTIONS_MENU_OPTIONS = Arrays.asList("Drink"); //TODO: Check if more needed

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String raw = event.getMenuOption();
        if (raw == null) return;
        String option = Text.removeTags(raw).trim();

        if (EQUIP_MENU_OPTIONS.contains(option)) {
            CheckIfCanEquipItem(event);
            return;
        }

        Debug("Mouseclick option: " + option);

        if (EAT_MENU_OPTIONS.contains(option) && !isUnlocked("Food")) {
            ShowPluginChat("<col=ff0000><b>Eating food locked!</b></col> You haven't unlocked the ability to eat food yet!", true);
            event.consume();
            return;
        }

        if (POTIONS_MENU_OPTIONS.contains(option) && !isUnlocked("Potions")) {
            ShowPluginChat("<col=ff0000><b>Drinking potions locked!</b></col> You haven't unlocked the ability to drink potions yet!", true);
            event.consume();
        }
    }

    void CheckIfCanEquipItem(MenuOptionClicked event) {
        int itemId = event.getItemId();
        if (itemId <= 0) {
            return;
        }

        ItemStats itemStats = itemManager.getItemStats(itemId, true);
        if (itemStats == null || !itemStats.isEquipable()) {
            return;
        }

        ItemEquipmentStats equipStats = itemStats.getEquipment();
        if (equipStats == null) {
            return;
        }

        // Determine required equipment slot
        UnlockEquipslot.EquipSlot slot = equipmentSlotBlocker.mapSlotFromEquipStats(equipStats.getSlot());
        if (slot == null) {
            return;
        }

        if (!isUnlocked("EQUIP_" + slot)) {
            ShowPluginChat("<col=ff0000><b>Possibly locked!</b></col>" + slot.getDisplayName() + " is not unlocked yet.", true);
            event.consume();
        }
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
