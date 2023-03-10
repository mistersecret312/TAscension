package net.tadditions.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.tadditions.mod.items.ModItems;
import net.tadditions.mod.screens.MClientHelper;
import net.tadditions.mod.screens.MConstants;
import net.tadditions.mod.tileentity.AdvQuantiscopeTile;
import net.tadditions.mod.tileentity.SolenoidConTileEntity;
import net.tardis.mod.blocks.TileBlock;
import net.tardis.mod.constants.TardisConstants;
import net.tardis.mod.helper.TInventoryHelper;
import net.tardis.mod.helper.WorldHelper;
import net.tardis.mod.misc.GuiContext;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class SolenoidConBlock extends TileBlock {

    public SolenoidConBlock(Properties properties) {
        super(properties);
    }
    private static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(1, 0, 1, 15, 14, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItemMainhand();
        if (worldIn.isRemote) {
            SolenoidConTileEntity tileEntity = (SolenoidConTileEntity) worldIn.getTileEntity(pos);
            if (held == ModItems.QUANTUM_EXOTIC_MATTER.get().getDefaultInstance() && tileEntity.Contents.equals(ItemStack.EMPTY)) {
                tileEntity.setItem(held);
                held.shrink(1);
                return ActionResultType.SUCCESS;
            } else if (held == ItemStack.EMPTY && !tileEntity.Contents.equals(ItemStack.EMPTY)) {
                player.addItemStackToInventory(tileEntity.getItem());
                tileEntity.setItem(ItemStack.EMPTY);
                return ActionResultType.SUCCESS;
            }
            else return ActionResultType.FAIL;


        }
        return ActionResultType.SUCCESS;
    }
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        SolenoidConTileEntity tileEntity = (SolenoidConTileEntity) worldIn.getTileEntity(pos);
        if (tileEntity != null && state.getBlock() != newState.getBlock()) {
            worldIn.removeTileEntity(pos);
        }
    }
}
