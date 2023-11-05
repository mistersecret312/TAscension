package net.cyber.mod.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;

public abstract class BEContainer<T extends TileEntity> extends Container {

    protected T blockEntity;

    protected BEContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    public T getBlockEntity() {
        return this.blockEntity;
    }

}
