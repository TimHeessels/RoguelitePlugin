package com.coinlockedplugin.save;

import com.coinlockedplugin.data.PackChoiceState;
import com.coinlockedplugin.data.SetupStage;
import com.coinlockedplugin.pack.PackOption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveData {
    public int version = 1;

    // pack UI / state
    public List<String> currentPackOptionIds = new ArrayList<>();
    public PackChoiceState packChoiceState = PackChoiceState.NONE;
    public SetupStage setupStage = SetupStage.DropAllItems;

    // progression
    public long illegalXPGained = 0L;
    public int packsBought = 0;
    public long peakWealth = 0L;

    public String lastUnlockedName = "";
    public Set<String> unlockedIds = new HashSet<>();

    public long lastUpdatedEpochMs = 0L;
}
