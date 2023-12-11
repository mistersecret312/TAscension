package net.cyber.mod.cap;

import com.google.common.collect.Maps;
import net.cyber.mod.CyberMod;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.helper.*;
import net.cyber.mod.items.CyberItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    public int essence = CyberConfigs.COMMON.BaseEssence.get();
    public int maxEssence = CyberConfigs.COMMON.BaseEssence.get();
    public ItemStackHandler handler = new ItemStackHandler(24);
    private Map<ItemStack, Boolean> mapActivated = Maps.newHashMap();

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

    public void tick() {
        if (!player.getEntityWorld().isRemote()) {
            this.getAllCyberware().forEach(items -> {
                if (items.getItem() instanceof ICyberPart) {
                    ICyberPart item = (ICyberPart) items.getItem();
                    item.runOnTick(player);
                }
            });

            initializeCyberware();

            mapActivated.keySet().forEach(ite -> {
                if (!mapActivated.get(ite)) {
                    if (ite.getItem() instanceof ICyberPart) {
                        ((ICyberPart) ite.getItem()).runOnce(player);
                        mapActivated.replace(ite, true);
                    }
                }
            });

            updateEssence();
        }
    }


    public void initializeCyberware(){
        this.getAllCyberware().forEach(items -> {
            if (items.getItem() instanceof ICyberPart) {
                if(mapActivated != null){
                    if(!mapActivated.containsKey(items)){
                        mapActivated.put(items, false);
                    }
                }
            }
        });
    }

    public void updateEssence(){
        int newValue = 100;

        for(int i = 0; i < this.getAllCyberware().size(); ++i) {
            ItemStack stack = this.getAllCyberware().get(i);
            if(stack.getItem() instanceof ICyberPart) {
                ICyberPart item = (ICyberPart)stack.getItem();
                newValue -= item.getEssenceCost();
            }
        }
        setEssence(newValue);

        if(essence > this.maxEssence)
            this.essence = this.maxEssence;
    }

    @Override
    public NonNullList<ItemStack> getAllCyberware() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for(int i = 0; i< this.handler.getSlots(); i++){
           stacks.add(this.handler.getStackInSlot(i));
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
        NonNullList<ItemStack> stacks = NonNullList.create();
        for(int i = 0; i<this.handler.getSlots(); i++){
            stacks.add(i, this.handler.getStackInSlot(i));
        }
        return stacks.contains(cyberware);
    }

    @Override
    public int getSlotItemIn(ItemStack cyberware) {
        for(int i = 0; i<this.handler.getSlots(); i++){
            if(this.handler.getStackInSlot(i) == cyberware){
                return i;
            }
        }
        return -1;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("essence", this.essence);
        tag.put("Handler", this.handler.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.essence = nbt.getInt("essence");
        this.handler.deserializeNBT(nbt.getCompound("Handler"));
    }
}
