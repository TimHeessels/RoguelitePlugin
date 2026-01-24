package com.rogueliteplugin.requirements;

import com.rogueliteplugin.RoguelitePlugin;

import java.util.Set;

public class MemberRequirement implements AppearRequirement {

    public MemberRequirement() {

    }

    @Override
    public boolean isMet(RoguelitePlugin plugin, Set<String> unlockedIds) {
        return plugin.isInMemberWorld();
    }

    @Override
    public String getRequiredUnlockTitle() {
        return "Membership Required";
    }
}

