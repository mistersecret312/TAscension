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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

public class CyberwareCap implements ICyberUser {

    private PlayerEntity player;
    public int essence = 100;
    public int maxEssence = 100;
    public ItemStackHandler handler;

    public CyberwareCap(PlayerEntity ent){
        this.player = ent;
    }

    public int getEssence(){
        return this.essence;
    }

    public int getMaxEssence(){
        return this.maxEssence;
    }

    public void setMaxEssence(int maxEssence) {
        this.maxEssence = maxEssence;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public void tick(){
        if(!player.getEntityWorld().isRemote())
        this.getAllCyberware().forEach(item -> {
            this.setEssence(this.getEssence()-((ICyberPart)item.getItem()).getEssenceCost());
            ((ICyberPart)item.getItem()).runOnTick();
        });

    }

    @Override
    public NonNullList<ItemStack> getAllCyberware() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for(int i = 0; i< this.handler.getSlots(); i++){
           ItemStack stack = this.handler.getStackInSlot(i);
           if(stack.getItem() instanceof ICyberPart){
               stacks.add(stack);
           }
        }
        return stacks;
    }

    public void setAllCyberware(NonNullList<ItemStack> stacks){
        for(int i = 0; i<this.handler.getSlots(); i++){
            this.handler.setStackInSlot(i, stacks.get(i));
        }
    }



    @Override
    public boolean isCyberwareInstalled(ItemStack cyberware) {
        for(int i = 0; i<this.handler.getSlots(); i++){
            return this.handler.getStackInSlot(i) == cyberware;
        }
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("essence", this.essence);
        tag.put("Items", this.handler.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.essence = nbt.getInt("essence");
        this.handler.deserializeNBT(nbt.getCompound("Items"));
    }
}
