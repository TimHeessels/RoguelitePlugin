package com.rogueliteplugin.enforcement;

import com.google.inject.Inject;
import com.rogueliteplugin.RoguelitePlugin;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.ScriptID;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

import static com.rogueliteplugin.data.QuestYearList.QUEST_BY_NAME;
import static com.rogueliteplugin.data.QuestYearList.QUEST_YEAR;

public class QuestBlocker {

    private static final int QUEST_LIST_REBUILD_SCRIPT = 2646;
    private static final String GRAY_TAG = "<col=9f9f9f>";
    private static final String END_TAG = "</col>";

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private RoguelitePlugin plugin;

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        int id = event.getScriptId();
        if (id != ScriptID.QUESTLIST_INIT && id != QUEST_LIST_REBUILD_SCRIPT) {
            return;
        }

        clientThread.invokeLater(() ->
                clientThread.invokeLater(this::applyQuestPrefixes)
        );
    }

    private void applyQuestPrefixes() {
        Widget root = client.getWidget(WidgetInfo.QUESTLIST_CONTAINER);
        if (root == null) {
            return;
        }

        Widget[] s1 = root.getStaticChildren();
        if (s1 == null || s1.length < 2) {
            return;
        }

        Widget[] s2 = s1[1].getStaticChildren();
        if (s2 == null || s2.length < 2) {
            return;
        }

        Widget[] s3 = s2[1].getStaticChildren();
        if (s3 == null || s3.length < 1) {
            return;
        }

        Widget scrollContent = s3[0];
        Widget[] entries = scrollContent.getDynamicChildren();
        if (entries == null) {
            return;
        }

        for (Widget entry : entries) {
            if (entry == null || entry.getType() != WidgetType.TEXT) {
                continue;
            }

            String text = entry.getText();
            if (text == null || text.isEmpty()) {
                continue;
            }

            String clean = Text.removeTags(text).trim();

            // Skip headers like "--- Free Quests ---"
            if (clean.startsWith("-")) {
                continue;
            }

            String questName = clean.split("\\(")[0].trim();
            String questYear = getQuestYear(questName);

            plugin.Debug("questName: "+questName + ", questYear: "+questYear);

            if (questYear == null)
                continue;

            boolean unlocked = plugin.isUnlocked("Quests" + questYear);

            String displayText;
            if (unlocked)
                displayText = clean; // no color override
            else
                displayText = GRAY_TAG + "[X] " + clean + END_TAG;

            entry.setText(displayText);
            entry.revalidate();
        }
    }

    private String getQuestYear(String questName)
    {
        Quest quest = QUEST_BY_NAME.get(questName);
        return quest == null ? null : QUEST_YEAR.get(quest);
    }
}
