package net.cyber.mod.container;

import net.cyber.mod.helper.Helper;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
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

        Helper.addPlayerInvContainer(this, inv, 9, 141);
    }


        @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
