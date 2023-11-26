package net.cyber.mod.cap;

import net.cyber.mod.CyberMod;
import net.cyber.mod.helper.*;
import net.cyber.mod.items.CyberItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.*;

public class CyberwareCap implements ICyberUser {

    private PlayerEntity player;
    public int essence = 100;

    public CyberwareCap(PlayerEntity ent){
        this.player = ent;
    }

    @Override
    public NonNullList<ItemStack> getInstalledCyberware(CyberPartEnum type) {
        return null;
    }

    @Override
    public boolean isCyberwareInstalled(ItemStack cyberware) {
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("essence", this.essence);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.essence = nbt.getInt("essence");
    }
}
