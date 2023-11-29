package net.cyber.mod.helper;

import net.minecraft.item.ItemStack;

public interface ICyberPart {
    int getEssenceCost();
    void setEssenceCost(int essence);
    CyberPartEnum getCategory();
    void runOnce();
    void runOnTick();
}
