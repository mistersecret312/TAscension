package net.cyber.mod.blocks;

import net.cyber.mod.tileentity.CyberTileEntitys;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSurgeryChamber extends ContainerBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final EnumProperty<EnumChamberHalf> HALF = EnumProperty.create("half", EnumChamberHalf.class);


    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
            toggleDoor(state, pos, world);
            notifySurgeon(pos, world);

        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(OPEN, false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(OPEN);
    }

    public BlockSurgeryChamber(Block.Properties properties) {
        super(properties.hardnessAndResistance(1,2));
    }

    public void toggleDoor(BlockState blockState, BlockPos pos, World worldIn)
    {
        BlockState blockStateNew = blockState.cycleValue(OPEN);
        worldIn.setBlockState(pos, blockStateNew, 2);
    }


    private void notifySurgeon(BlockPos pos, World worldIn)
    {
        TileEntity above = worldIn.getTileEntity(pos.up());

        if (above instanceof TileEntitySurgery)
        {
            ((TileEntitySurgery) above).notifyChange();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return CyberTileEntitys.SRC.get().create();
    }

    public enum EnumChamberHalf implements IStringSerializable
    {
        UPPER,
        LOWER;

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this == UPPER ? "upper" : "lower";
        }

        @Override
        public String getString() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
