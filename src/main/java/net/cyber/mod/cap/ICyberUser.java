package net.cyber.mod.cap;

import net.cyber.mod.helper.ICyberInfo;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public interface ICyberUser {
    NonNullList<ItemStack> getInstalledCyberware(ICyberInfo.EnumSlot slot);
    void setInstalledCyberware(Entity entityLivingBase, ICyberInfo.EnumSlot slot, List<ItemStack> cyberware);
    void setInstalledCyberware(Entity entityLivingBase, ICyberInfo.EnumSlot slot, NonNullList<ItemStack> cyberware);
    boolean isCyberwareInstalled(ItemStack cyberware);
    int getCyberwareRank(ItemStack cyberware);

    CompoundNBT serializeNBT();
    void deserializeNBT(CompoundNBT tagCompound);


    boolean hasEssential(ICyberInfo.EnumSlot slot);
    void setHasEssential(ICyberInfo.EnumSlot slot, boolean hasLeft, boolean hasRight);
    ItemStack getCyberware(ItemStack cyberware);
    void updateCapacity();
    void resetBuffer();
    void addPower(int amount, ItemStack inputter);
    boolean isAtCapacity(ItemStack stack);
    boolean isAtCapacity(ItemStack stack, int buffer);
    float getPercentFull();
    int getCapacity();
    int getStoredPower();
    int getProduction();
    int getConsumption();
    boolean usePower(ItemStack stack, int amount);
    List<ItemStack> getPowerOutages();
    List<Integer> getPowerOutageTimes();
    void setImmune();
    boolean usePower(ItemStack stack, int amount, boolean isPassive);
    boolean hasEssential(ICyberInfo.EnumSlot slot, ICyberInfo.ISidedLimb.EnumSide side);
    void resetWare(Entity entityLivingBase);
    int getNumActiveItems();
    List<ItemStack> getActiveItems();
    void removeHotkey(int i);
    void addHotkey(int i, ItemStack stack);
    ItemStack getHotkey(int i);
    Iterable<Integer> getHotkeys();
    List<ItemStack> getHudjackItems();
    void setHudData(CompoundNBT tagCompound);
    CompoundNBT getHudData();
    boolean hasOpenedRadialMenu();
    void setOpenedRadialMenu(boolean hasOpenedRadialMenu);
    void setHudColor(int color);
    void setHudColor(float[] color);
    int getHudColorHex();
    float[] getHudColor();
    int getMaxTolerance(@Nonnull Entity entityLivingBase);
    void setTolerance(@Nonnull Entity entityLivingBase, int amount);
    int getTolerance(@Nonnull Entity entityLivingBase);

    @Deprecated
    int getEssence();
    @Deprecated
    void setEssence(int essence);
    @Deprecated
    int getMaxEssence();
}
