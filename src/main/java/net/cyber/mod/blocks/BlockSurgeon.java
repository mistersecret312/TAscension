package net.cyber.mod.blocks;

import io.netty.buffer.Unpooled;
import net.cyber.mod.container.ContainerSurgery;
import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.ICyberData;
import net.cyber.mod.tileentity.CyberTileEntitys;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSurgeon extends TileBlock{

    public BlockSurgeon(Properties prop) {
        super(prop);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = world.getTileEntity(pos);

        if ( tileentity instanceof TileEntitySurgery
                && !world.isRemote )
        {
            TileEntitySurgery surgery = (TileEntitySurgery) tileentity;

            for (int indexSlot = 0; indexSlot < surgery.slots.getSlots(); indexSlot++)
            {
                ItemStack stack = surgery.slots.getStackInSlot(indexSlot);
                if (!stack.isEmpty())
                {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    {
        if(!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntitySurgery) {
                TileEntitySurgery tileEntitySurgery = (TileEntitySurgery) tileEntity;

                ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(player);
                tileEntitySurgery.updatePlayerSlots(player, cyberwareUserData);
                NetworkHooks.openGui((ServerPlayerEntity) player, createContainerProvider(tileEntitySurgery), pos);
            }
        }

            return ActionResultType.SUCCESS;
        }
    }

    private INamedContainerProvider createContainerProvider(TileEntitySurgery tile) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("container.techascension.surgeon");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new ContainerSurgery(i, playerInventory, tile);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return CyberTileEntitys.SR.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
