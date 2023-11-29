package net.cyber.mod.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;

public abstract class BEContainer<T extends TileEntity> extends BaseContainer{

    protected T blockEntity;

    protected BEContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    public T getBlockEntity() {
        return this.blockEntity;
    }


}
