package net.cyber.mod.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntitySurgery extends TileEntity implements ITickableTileEntity {

	public TileEntitySurgery(TileEntityType<?> tileEntityTypeIn) {
			super(tileEntityTypeIn);
		}

	public TileEntitySurgery() {
			this(CyberTileEntitys.SR.get());
		}

	@Override
	public void tick() {

	}
}
