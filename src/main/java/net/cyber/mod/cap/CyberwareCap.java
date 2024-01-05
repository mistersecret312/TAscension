package net.cyber.mod.cap;

import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.helper.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class CyberwareCap implements ICyberUser {

    private PlayerEntity player;
    public float essence = CyberConfigs.COMMON.BaseEssence.get();
    public float maxEssence = CyberConfigs.COMMON.BaseEssence.get();
    public ItemStackHandler handler = new ItemStackHandler(24);

    public CyberwareCap(PlayerEntity ent){
        this.player = ent;
    }

    public float getEssence(){
        return this.essence;
    }

    public float getMaxEssence(){
        return this.maxEssence;
    }

    public void setMaxEssence(float maxEssence) {
        this.maxEssence = maxEssence;
    }

    public void setEssence(float essence) {
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

            updateEssence();
        }
    }


    public void updateEssence(){
        float newValue = CyberConfigs.COMMON.BaseEssence.get();

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
    public void handleRemoved(ItemStack stack){
        if(stack.getItem() instanceof ICyberPart){
            ((ICyberPart)stack.getItem()).runOnceUndo(player);
        }
    }

    @Override
    public void handleAdded(ItemStack stack){
        if(stack.getItem() instanceof ICyberPart){
            ((ICyberPart)stack.getItem()).runOnce(player);
        }
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
        tag.putFloat("essence", this.essence);
        tag.put("Handler", this.handler.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.essence = nbt.getFloat("essence");
        this.handler.deserializeNBT(nbt.getCompound("Handler"));
    }
}
