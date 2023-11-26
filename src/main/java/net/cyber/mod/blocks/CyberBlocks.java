package net.cyber.mod.blocks;

import net.cyber.mod.CyberMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.cyber.mod.items.CyberItemGroups;
import net.cyber.mod.items.CyberItems;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class CyberBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CyberMod.MOD_ID);

    public static final RegistryObject<Block> SURGEON = register("surgeon", () -> setUpBlock(new BlockSurgeon(AbstractBlock.Properties.create(Material.ANVIL))), CyberItemGroups.CYBERMOD);


    private static <T extends Block> T setUpBlock(T block) {
        return block;
    }

    /**
     * Registers a Block and BlockItem to the ItemGroup of your choice
     * @param <T>
     * @param id
     * @param blockSupplier
     * @param itemGroup
     * @return
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, ItemGroup itemGroup){
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        CyberItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(itemGroup)));
        return registryObject;
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, ItemGroup itemGroup, Item con){
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        CyberItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(itemGroup).containerItem(con)));
        return registryObject;
    }

    /**
     * Registers a Block without a BlockItem
     * <br> Use when you need a special BlockItem. The BlockItem should be registered in TItems with the same registry name as the block
     * @param <T>
     * @param id
     * @param blockSupplier
     * @return
     */
    private static <T extends Block> RegistryObject<T> registerBlockOnly(String id, Supplier<T> blockSupplier){
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        return registryObject;
    }

    /**
     * Registers a Block and BlockItem into the FutureItemGroup
     * @param <T>
     * @param id
     * @param blockSupplier
     * @return
     */
    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier){
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        CyberItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(CyberItemGroups.CYBERMOD)));
        return registryObject;
    }

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.get(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }
}
