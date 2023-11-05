package net.cyber.mod.helper;

import net.cyber.mod.cap.CyberCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.MixinEnvironment;
import net.cyber.mod.helper.ICyberInfo.EnumSlot;

import javax.annotation.Nonnull;
import java.util.List;

public interface ICyberData extends INBTSerializable<CompoundNBT> {
    NonNullList<ItemStack> getInstalledCyberware(EnumSlot slot);
    void setInstalledCyberware(Entity entityLivingBase, EnumSlot slot, List<ItemStack> cyberware);
    void setInstalledCyberware(Entity entityLivingBase, EnumSlot slot, NonNullList<ItemStack> cyberware);
    boolean isCyberwareInstalled(ItemStack cyberware);

    CompoundNBT serializeNBT();
    void deserializeNBT(CompoundNBT tagCompound);


    boolean hasEssential(EnumSlot slot);
    void setHasEssential(EnumSlot slot, boolean hasLeft, boolean hasRight);
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
    boolean hasEssential(EnumSlot slot, ICyberInfo.ISidedLimb.EnumSide side);
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

    @Deprecated
    class Storage implements Capability.IStorage<ICyberData> {

        @Override
        public INBT writeNBT(Capability<ICyberData> capability, ICyberData instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICyberData> capability, ICyberData instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT)
                instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

    class Provider implements ICapabilitySerializable<CompoundNBT> {

        ICyberData data;

            public Provider(ICyberData data) {
            this.data = data;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == CyberCapabilities.CYBERWARE_CAPABILITY ? (LazyOptional<T>) LazyOptional.of(() -> data) : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return data.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            data.deserializeNBT(nbt);
        }

    }

}
