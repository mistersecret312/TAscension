package net.cyber.mod.cap;

import net.cyber.mod.helper.CyberPartEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ICyberUser extends INBTSerializable<CompoundNBT> {
    NonNullList<ItemStack> getInstalledCyberware(CyberPartEnum type);
    boolean isCyberwareInstalled(ItemStack cyberware);

    @Deprecated
    public class Storage implements Capability.IStorage<ICyberUser> {

        @Override
        public INBT writeNBT(Capability<ICyberUser> capability, ICyberUser instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICyberUser> capability, ICyberUser instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT)
                instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

    class Provider implements ICapabilitySerializable<CompoundNBT> {

        ICyberUser data;

        public Provider(ICyberUser data) {
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
