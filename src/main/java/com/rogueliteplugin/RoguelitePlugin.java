package com.rogueliteplugin;

import java.util.*;
import javax.inject.Inject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.inject.Provides;
import com.rogueliteplugin.challenge.*;
import com.rogueliteplugin.pack.PackOption;
import com.rogueliteplugin.pack.UnlockPackOption;
import com.rogueliteplugin.requirements.AppearRequirement;
import com.rogueliteplugin.unlocks.*;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.callback.ClientThread;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
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

        //Setup all possible challenges
        challengeRegistry = new ChallengeRegistry();
        ChallengeDefinitions.registerAll(challengeRegistry, unlockRegistry, skillIconManager, this);

        //Load current challenge state
        challengeManager.loadFromConfig(config, client, challengeRegistry);

        overlayManager.add(overlay);
        eventBus.register(skillBlocker);

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
            if (panel != null) {
                panel.refresh();
            }
        }
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

            if (!playerIsLoggedIn()) {
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
                if (!req.isMet(this)) {
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

            if (panel != null) {
                panel.refresh();
            }
        }
    }

    public void setActiveChallenge(Challenge activeChallenge) {
        challengeManager.startChallenge(activeChallenge, getBalancedChallengeAmount(activeChallenge.getLowAmount(), activeChallenge.getHighAmount()));
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

    public boolean playerIsLoggedIn() {
        return client.getLocalPlayer() != null;
    }

    private void generatePackOptions() {
        List<Unlock> locked = unlockRegistry.getAll().stream()
                .filter(u -> !unlockedIds.contains(u.getId()))
                .filter(this::canAppearAsPackOption)
                .collect(Collectors.toList());
        Collections.shuffle(locked);

        int optionCount = Math.min(4, locked.size());

        currentPackOptions = IntStream.range(0, optionCount)
                .mapToObj(i -> {
                    Unlock unlock = locked.get(i);

                    // base unlocks + this card's unlock
                    Set<String> hypotheticalUnlocks = new HashSet<>(unlockedIds);
                    hypotheticalUnlocks.add(unlock.getId());

                    List<Challenge> validChallenges =
                            challengeRegistry.getAll().stream()
                                    .filter(c -> c.isValidWithUnlocks(hypotheticalUnlocks))
                                    .collect(Collectors.toList());

                    Challenge challenge = validChallenges.isEmpty()
                            ? null
                            : validChallenges.get(random.nextInt(validChallenges.size()));

                    return new UnlockPackOption(unlock, challenge);
                })
                .collect(Collectors.toList());
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

    public void Debug(String textToDebug)
    {
        log.debug(textToDebug);
    }
}