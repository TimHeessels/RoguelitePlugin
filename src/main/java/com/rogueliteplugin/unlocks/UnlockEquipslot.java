package com.rogueliteplugin.unlocks;
import com.rogueliteplugin.data.UnlockType;
import com.rogueliteplugin.requirements.AppearRequirement;
import javax.swing.*;
import java.util.List;

public class UnlockEquipslot implements Unlock {
    private final String id;
    private final Icon icon;
    private final String displayName;
    private final String description;
    private final EquipSlot slot;

    public UnlockEquipslot(String id, String displayName, Icon icon, String description, EquipSlot slot) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.slot = slot;
    }

    @Override
    public UnlockType getType() {
        return UnlockType.EquipmentSlots;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public UnlockIcon getIcon() {
        return new ImageUnlockIcon(icon);
    }

    @Override
    public List<AppearRequirement> getRequirements() {
        return List.of();
    }

    public EquipSlot getSlot() {
        return slot;
    }

    public enum EquipSlot {
        AMMO("Ammo"),
        AMULET("Amulet"),
        BODY("Body"),
        BOOTS("Boots"),
        CAPE("Cape"),
        GLOVES("Gloves"),
        HEAD("Head"),
        LEGS("Legs"),
        RING("Ring"),
        SHIELD("Shield"),
        WEAPON("Weapon");

        private final String displayName;

        EquipSlot(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String toIdSuffix() {
            return name();
        }
    }
}
