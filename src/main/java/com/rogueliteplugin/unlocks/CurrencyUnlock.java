package com.rogueliteplugin.unlocks;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.data.UnlockType;

import javax.swing.*;

public class CurrencyUnlock implements Unlock {
    public enum CurrencyType {
        REROLL,
        SKIP
    }

    private final String id;
    private final String name;
    private final Icon icon;
    private final String description;
    private final CurrencyType currencyType;
    private final int amount;

    public CurrencyUnlock(String id, String name, Icon icon, String description, CurrencyType currencyType, int amount) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.currencyType = currencyType;
        this.amount = amount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public UnlockIcon getIcon() {
        return new ImageUnlockIcon(icon);
    }

    @Override
    public UnlockType getType() {
        return UnlockType.Currency;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void grant(RoguelitePlugin plugin) {
        switch (currencyType) {
            case REROLL:
                plugin.addRerollTokens(amount);
                break;
            case SKIP:
                plugin.addChallengeSkipTokens(amount);
                break;
        }
    }
}