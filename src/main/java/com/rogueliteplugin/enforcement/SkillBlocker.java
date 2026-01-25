package com.rogueliteplugin.enforcement;

import com.google.inject.Inject;

import java.awt.Color;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.unlocks.UnlockEquipslot;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetSizeMode;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;

public class SkillBlocker {
    private static final int SCRIPTID_STATS_INIT = 394;
    private static final int SCRIPTID_STATS_REFRESH = 393;

    private static final int LOCK_SPRITE_ID = 1342; // padlock icon
    private static final int LOCK_ICON_CHILD_ID = 1000;
    private static final int GRAY_OVERLAY_CHILD_ID = 999;

    private static final int GRAY_OPACITY = 160;

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;

    @Inject
    private RoguelitePlugin plugin;

    private static final Skill[] SKILLS_TAB_ORDER = {
            Skill.ATTACK,
            Skill.STRENGTH,
            Skill.DEFENCE,
            Skill.RANGED,
            Skill.PRAYER,
            Skill.MAGIC,
            Skill.RUNECRAFT,
            Skill.CONSTRUCTION,
            Skill.HITPOINTS,
            Skill.AGILITY,
            Skill.HERBLORE,
            Skill.THIEVING,
            Skill.CRAFTING,
            Skill.FLETCHING,
            Skill.SLAYER,
            Skill.HUNTER,
            Skill.MINING,
            Skill.SMITHING,
            Skill.FISHING,
            Skill.COOKING,
            Skill.FIREMAKING,
            Skill.WOODCUTTING,
            Skill.FARMING,
            Skill.SAILING
    };

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        if (event.getScriptId() != SCRIPTID_STATS_INIT
                && event.getScriptId() != SCRIPTID_STATS_REFRESH) {
            return;
        }

        refreshAll();
    }

    private void applyGrayOverlay(Widget parent, Skill skill) {
        if (parent == null || parent.getType() != WidgetType.LAYER) {
            return;
        }

        boolean allowed = plugin.isSkillUnlocked(skill);

        Widget gray = parent.getChild(GRAY_OVERLAY_CHILD_ID);
        if (gray == null) {
            gray = parent.createChild(GRAY_OVERLAY_CHILD_ID, WidgetType.RECTANGLE);
            gray.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            gray.setYPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            gray.setWidthMode(WidgetSizeMode.MINUS);
            gray.setHeightMode(WidgetSizeMode.MINUS);
            gray.setOriginalWidth(0);
            gray.setOriginalHeight(0);
            gray.setFilled(true);
            gray.setHasListener(false);
            gray.setTextColor(Color.BLACK.getRGB());
        }

        Widget lockIcon = parent.getChild(LOCK_ICON_CHILD_ID);
        if (lockIcon == null) {
            lockIcon = parent.createChild(LOCK_ICON_CHILD_ID, WidgetType.GRAPHIC);
            lockIcon.setSpriteId(LOCK_SPRITE_ID);
            lockIcon.setXPositionMode(WidgetPositionMode.ABSOLUTE_LEFT);
            lockIcon.setYPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
            lockIcon.setOriginalX(10);
            lockIcon.setOriginalY(0);
            lockIcon.setOriginalWidth(14);
            lockIcon.setOriginalHeight(14);
            lockIcon.setHasListener(false);
        }

        if (allowed) {
            gray.setOpacity(255);
            lockIcon.setHidden(true);
        } else {
            gray.setHidden(false);
            gray.setOpacity(GRAY_OPACITY);
            lockIcon.setHidden(false);
        }

        gray.revalidate();
        lockIcon.revalidate();
    }

    public void clearAll() {
        plugin.Debug("Clearing skilling slot overlays");
        Widget container = client.getWidget(InterfaceID.Stats.UNIVERSE);
        if (container == null) {
            return;
        }

        clientThread.invokeLater(() -> {
            Widget[] tiles = container.getStaticChildren();
            for (int i = 0; i < tiles.length && i < SKILLS_TAB_ORDER.length; i++) {
                removeGrayOverlay(tiles[i]);
            }
        });
    }

    public void refreshAll() {
        plugin.Debug("Refreshing skilling slot overlays");
        Widget container = client.getWidget(InterfaceID.Stats.UNIVERSE);
        if (container == null) {
            return;
        }

        clientThread.invokeLater(() -> {
            Widget[] tiles = container.getStaticChildren();
            for (int i = 0; i < tiles.length && i < SKILLS_TAB_ORDER.length; i++) {
                applyGrayOverlay(tiles[i], SKILLS_TAB_ORDER[i]);
            }
        });
    }

    private void removeGrayOverlay(Widget parent) {
        if (parent == null) {
            return;
        }

        Widget gray = parent.getChild(GRAY_OVERLAY_CHILD_ID);
        if (gray != null)
            gray.setHidden(true);

        Widget lockIcon = parent.getChild(LOCK_ICON_CHILD_ID);
        if (lockIcon != null)
            lockIcon.setHidden(true);
    }
}
