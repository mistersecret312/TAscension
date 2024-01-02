package net.cyber.mod.container;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.container.slots.UpgradeSlot;
import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.Helper;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSurgery extends BEContainer<TileEntitySurgery> {
    public PlayerEntity entity;
    public NonNullList<ItemStack> oldstacks = NonNullList.create();
    public NonNullList<ItemStack> newstacks = NonNullList.create();

    protected ContainerSurgery(ContainerType<?> type, int id) {
        super(type, id);
    }
    /** Client Only constructor */
    public ContainerSurgery(int id, PlayerInventory inv, PacketBuffer buf) {
        super(CyberContainers.SURGEON.get(), id);
        this.init(inv, (TileEntitySurgery) inv.player.world.getTileEntity(buf.readBlockPos()));
    }
    /** Server Only constructor */
    public ContainerSurgery(int id, PlayerInventory inv, TileEntitySurgery tile) {
        super(CyberContainers.SURGEON.get(), id);
        this.init(inv, tile);
        this.entity = inv.player;
    }





    public void init(PlayerInventory inv, TileEntitySurgery tile) {

        //Upgrades
        //Part 1
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.ARM), tile, 0, -5, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.ARM), tile, 1, -5, 46));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.BONE), tile,  2, 29, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.BONE), tile,  3, 29, 46));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.CRANIUM), tile,  4, 63, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.CRANIUM), tile,  5, 63, 46));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.EYES), tile,  6, 97, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.EYES), tile,  7, 97, 46));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.FOOT), tile,  8, 131, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.FOOT), tile,  9, 131, 46));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.HAND), tile,  10, 165, 28));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.HAND), tile,  11, 165, 46));

        //Part 2
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.HEART), tile,  12, -5, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.HEART), tile,  13, -5, 85));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LEG), tile,  14, 29, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LEG), tile,  15, 29, 85));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LOWER_ORGANS), tile,  16, 63, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LOWER_ORGANS), tile,  17, 63, 85));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LUNGS), tile,  18, 97, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.LUNGS), tile,  19, 97, 85));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.MUSCLE), tile,  20, 131, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.MUSCLE), tile,  21, 131, 85));

        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.SKIN), tile,  22, 165, 67));
        this.addSlot(new UpgradeSlot(this, type -> type.equals(CyberPartEnum.SKIN), tile,  23, 165, 85));

        Helper.addPlayerInvContainer(this, inv, 0, 54);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        playerIn.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(cap -> {
            NonNullList<ItemStack> stacks = NonNullList.create();
            for(int i = 0; i<24; i++){
                stacks.add(i, this.getInventory().get(i));
            }
            if(cap.getAllCyberware() != stacks){
                cap.setAllCyberware(stacks);
            }

            newstacks.forEach(cap::handleAdded);
        });
        super.onContainerClosed(playerIn);
        if (!oldstacks.equals(newstacks)) {
            int size = Math.min(oldstacks.size(), newstacks.size());
            for (int i = 0; i < size; i++) {
                if (!ItemStack.areItemStacksEqual(oldstacks.get(i), newstacks.get(i))) {
                    if(!newstacks.get(i).isEmpty()){
                        ((ICyberPart)newstacks.get(i).getItem()).runOnce(entity);
                        System.out.print("Added:" + newstacks.get(i));
                    }
                    if(!oldstacks.get(i).isEmpty()){
                        ((ICyberPart)oldstacks.get(i).getItem()).runOnceUndo(entity);
                        System.out.print("Removed:" + oldstacks.get(i));
                    }
                }
            }
        }
        oldstacks.clear();
        newstacks.clear();
    }

    public void addToRemoved(ItemStack stack) {
        //oldstacks.clear();
        oldstacks.add(stack);
    }

    public void addToAdded(ItemStack stack){
        //newstacks.clear();
        newstacks.add(stack);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
