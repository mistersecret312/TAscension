package net.cyber.mod.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySurgeryChamber extends TileEntity {
    public boolean lastOpen;
    public float openTicks;

    public TileEntitySurgeryChamber(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public TileEntitySurgeryChamber(){
        this(CyberTileEntitys.SRC.get());
    }


}
