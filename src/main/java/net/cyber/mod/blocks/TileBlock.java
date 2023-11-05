package net.cyber.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TileBlock extends Block {

    protected TileEntityType<?> type;

    public TileBlock(Block.Properties prop) {
        super(prop);
    }

    public void setTileEntity(TileEntityType<?> type) {
        this.type = type;
    }

    /**
     * 1.17: This will be removed and handled via another interface
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return type != null;
    }

    /**
     * 1.17: This will be removed and handled via BaseEntityBlock
     */
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return type.create();
    }

    //My tiles need these more often then not so..
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        return false;
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        return false;
    }
}

