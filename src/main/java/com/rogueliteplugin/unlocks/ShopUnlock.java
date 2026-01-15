package com.rogueliteplugin.unlocks;

public class ShopUnlock implements Unlock
{
    private final String id;
    private final String name;
    private final int spriteId;
    private final String description;

    public ShopUnlock(String id, String name, int spriteId, String description)
    {
        this.id = id;
        this.name = name;
        this.spriteId = spriteId;
        this.description = description;
    }

    @Override
    public UnlockType getType()
    {
        return UnlockType.Shops;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getDisplayName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public UnlockIcon getIcon()
    {
        return new SpriteUnlockIcon(spriteId);
    }
}
