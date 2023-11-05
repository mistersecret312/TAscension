package net.cyber.mod.client;

import com.google.common.collect.Maps;
import net.cyber.mod.blocks.CyberBlocks;
import net.cyber.mod.screens.GuiSurgery;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.cyber.mod.CyberMod;
import net.cyber.mod.container.CyberContainers;

import java.util.EnumMap;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CyberMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyberClientRegistry {

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        registerTileRenderers();
        registerScreens();
        event.enqueueWork(() -> {
            //Block Render Layers
            RenderTypeLookup.setRenderLayer(CyberBlocks.SurgeryChamber.get(), RenderType.getCutout());
        });
    }


    private static void registerTileRenderers() {
    }


    public static void registerScreens() {
        ScreenManager.registerFactory(CyberContainers.ADVQUANTISCOPE_WELD.get(), GuiSurgery::new);
    }
}




