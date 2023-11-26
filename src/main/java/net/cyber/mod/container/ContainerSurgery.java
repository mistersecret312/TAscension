package net.cyber.mod.container;

import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

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

    public void init(PlayerInventory inv, TileEntitySurgery quantiscope) {

    }


        @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
