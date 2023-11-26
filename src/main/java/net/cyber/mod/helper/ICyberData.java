package net.cyber.mod.helper;

import net.cyber.mod.cap.CyberCapabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ICyberData extends INBTSerializable<CompoundNBT> {

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
