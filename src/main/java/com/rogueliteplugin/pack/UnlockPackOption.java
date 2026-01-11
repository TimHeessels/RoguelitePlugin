package com.rogueliteplugin.pack;

import com.rogueliteplugin.RoguelitePlugin;
import com.rogueliteplugin.unlocks.Unlock;

import javax.swing.Icon;

public class UnlockPackOption implements PackOption
{
    private final Unlock unlock;

    public UnlockPackOption(Unlock unlock)
    {
        this.unlock = unlock;
    }

    public Unlock getUnlock()
    {
        return unlock;
    }

    @Override
    public String getDisplayName()
    {
        return unlock.getDisplayName();
    }

    @Override
    public String getDisplayType()
    {
        return unlock.getType().toString();
    }

    @Override
    public void onChosen(RoguelitePlugin plugin)
    {
        plugin.unlock(unlock);
    }
}
