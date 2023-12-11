package net.cyber.mod.container;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.helper.Helper;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSurgery extends BEContainer<TileEntitySurgery> {

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
    }





    public void init(PlayerInventory inv, TileEntitySurgery tile) {

        //Upgrades
        //Part 1
        this.addSlot(new SlotItemHandler(tile, 0, -5, 28));
        this.addSlot(new SlotItemHandler(tile, 1, -5, 46));

        this.addSlot(new SlotItemHandler(tile, 2, 29, 28));
        this.addSlot(new SlotItemHandler(tile, 3, 29, 46));

        this.addSlot(new SlotItemHandler(tile, 4, 63, 28));
        this.addSlot(new SlotItemHandler(tile, 5, 63, 46));

        this.addSlot(new SlotItemHandler(tile, 6, 97, 28));
        this.addSlot(new SlotItemHandler(tile, 7, 97, 46));

        this.addSlot(new SlotItemHandler(tile, 8, 131, 28));
        this.addSlot(new SlotItemHandler(tile, 9, 131, 46));

        this.addSlot(new SlotItemHandler(tile, 10, 165, 28));
        this.addSlot(new SlotItemHandler(tile, 11, 165, 46));

        //Part 2
        this.addSlot(new SlotItemHandler(tile, 12, -5, 67));
        this.addSlot(new SlotItemHandler(tile, 13, -5, 85));

        this.addSlot(new SlotItemHandler(tile, 14, 29, 67));
        this.addSlot(new SlotItemHandler(tile, 15, 29, 85));

        this.addSlot(new SlotItemHandler(tile, 16, 63, 67));
        this.addSlot(new SlotItemHandler(tile, 17, 63, 85));

        this.addSlot(new SlotItemHandler(tile, 18, 97, 67));
        this.addSlot(new SlotItemHandler(tile, 19, 97, 85));

        this.addSlot(new SlotItemHandler(tile, 20, 131, 67));
        this.addSlot(new SlotItemHandler(tile, 21, 131, 85));

        this.addSlot(new SlotItemHandler(tile, 22, 165, 67));
        this.addSlot(new SlotItemHandler(tile, 23, 165, 85));

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
        });
        for(int i = 0; i<24; i++){
            this.getInventory().set(i, ItemStack.EMPTY);
        }

        super.onContainerClosed(playerIn);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
