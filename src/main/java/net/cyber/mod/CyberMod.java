package net.cyber.mod;

import net.cyber.mod.helper.ICyberData;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.cyber.mod.blocks.CyberBlocks;
import net.cyber.mod.cap.*;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.container.CyberContainers;
import net.cyber.mod.events.CommonEvents;
import net.cyber.mod.items.CyberItems;
import net.cyber.mod.network.CyberNetwork;
import net.cyber.mod.recipe.CyberRecipeSerialisers;
import net.cyber.mod.sound.CyberSounds;
import net.cyber.mod.tileentity.CyberTileEntitys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CyberMod.MOD_ID)
public class CyberMod
{
    public static final String MOD_ID = "tascension";


    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public CyberMod() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CyberItems.ITEMS.register(eventBus);
        CyberBlocks.BLOCKS.register(eventBus);
        CyberTileEntitys.TILES.register(eventBus);
        CyberContainers.CONTAINERS.register(eventBus);
        CyberRecipeSerialisers.RECIPE_SERIALISERS.register(eventBus);
        CyberSounds.SOUND_EVENT.register(eventBus);




        eventBus.addListener(this::setup);

        eventBus.addListener(this::enqueueIMC);

        eventBus.addListener(this::processIMC);

        eventBus.addListener(this::doClientStuff);


        CyberNetwork.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CyberConfigs.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CyberConfigs.SERVER_SPEC);
        MinecraftForge.EVENT_BUS.register(this);


    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
                {
                });
        CapabilityManager.INSTANCE.register(ICyberUser.class, new ICyberUser.Storage(), () -> new CyberwareCap(null));
    }



    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {

    }

    private void processIMC(final InterModProcessEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
    }

}
