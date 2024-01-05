package net.cyber.mod.container;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.container.slots.UpgradeSlot;
import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.Helper;
import net.cyber.mod.helper.ICyberPart;
import net.cyber.mod.items.ItemCyberware;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.SlotItemHandler;

import static net.minecraft.item.Items.AIR;

public class ContainerSurgery extends BEContainer<TileEntitySurgery> {
    public PlayerEntity entity;
    public NonNullList<ItemStack> oldstacks = NonNullList.create();
    public NonNullList<ItemStack> newstacks = NonNullList.create();
    public NonNullList<ItemStack> oldstackslist = NonNullList.create();
    public NonNullList<ItemStack> newstackslist = NonNullList.create();



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

        for (int i = 0; i < 24; i++) {
            oldstackslist.add(ItemStack.EMPTY);
        }
        System.out.println("oldstackslist size: " + oldstackslist.size());
        for (int i = 0; i < 24; i++) {
            oldstackslist.set(i, this.getSlot(i).getStack().copy());
            //System.out.println("Initialization stack: " + oldstackslist.get(i));
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        System.out.println("onContainerClosed called");
        playerIn.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(cap -> {
            /*NonNullList<ItemStack> stacks = NonNullList.create();
            if (cap.getAllCyberware() != stacks) {
                cap.setAllCyberware(stacks);
            }*/
            if(!playerIn.world.isRemote) {
                for (int i = 0; i < 24; i++) {
                    newstackslist.add(ItemStack.EMPTY);
                }
                for (int i = 0; i < 24; i++) {
                    newstackslist.set(i, this.getSlot(i).getStack().copy());
                }
                for (int i = 0; i < 24; i++) {
                    Slot slot = this.getSlot(i);
                    newstackslist.set(i, slot.getStack());
                    if(!ItemStack.areItemStacksEqual(oldstackslist.get(i), newstackslist.get(i))){
                        //System.out.println("oldstack: " + oldstacks.get(i));
                        //System.out.println("oldstack: " + newstackslist.get(i));
                        if(!newstackslist.get(i).isEmpty()){
                            cap.handleAdded(newstackslist.get(i));
                            oldstackslist.set(i, newstackslist.get(i));
                        }
                        else if (!oldstackslist.get(i).isEmpty()) {
                            cap.handleRemoved(oldstackslist.get(i));
                            oldstackslist.set(i, newstackslist.get(i));
                        }
                    }
                }
                oldstackslist.clear();
                newstackslist.clear();
            }
        });
        super.onContainerClosed(playerIn);
    }

    public void addToRemoved(ItemStack stack) {
        oldstacks.add(stack);
    }

    public void addToAdded(ItemStack stack){
        newstacks.add(stack);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
