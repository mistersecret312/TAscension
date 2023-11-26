package net.cyber.mod.tileentity;

import com.google.common.base.Supplier;
import net.cyber.mod.CyberMod;
import net.cyber.mod.blocks.CyberBlocks;
import net.cyber.mod.blocks.TileBlock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CyberTileEntitys {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CyberMod.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntitySurgery>> SR = TILES.register("surgeon", () -> registerTiles(TileEntitySurgery::new, CyberBlocks.Surgery.get()));

    private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
        TileEntityType<T> type = TileEntityType.Builder.create(tile, validBlock).build(null);
        for(Block block : validBlock) {
            if(block instanceof TileBlock) {
                ((TileBlock)block).setTileEntity(type);
            }
        }
        return type;
    }
}
