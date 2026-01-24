package com.rogueliteplugin.pack;

import com.rogueliteplugin.RoguelitePlugin;

import javax.swing.Icon;

public interface PackOption {
    String getDisplayName();

    String getDisplayType();

    String getChallengeName();

    String getChallengeType();

    Integer getChallengeAmount();

    void onChosen(RoguelitePlugin plugin, int balancedAmount);
}