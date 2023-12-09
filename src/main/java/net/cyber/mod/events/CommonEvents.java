package net.cyber.mod.events;

import net.cyber.mod.CyberMod;
import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.cap.CyberwareCap;
import net.cyber.mod.cap.ICyberUser;
import net.cyber.mod.helper.ICyberData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = CyberMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    public static final ResourceLocation PLAYER_DATA_CAP = new ResourceLocation(CyberMod.MOD_ID, "cyber_data");


    private static HashMap<ResourceLocation, ResourceLocation> remappedEntries = new HashMap<ResourceLocation, ResourceLocation>();
    @SubscribeEvent
    public static void attachWorldCaps(AttachCapabilitiesEvent<World> event) {

    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(ICyberUser::tick);
    }

    @SubscribeEvent
    public static void attachItemStackCap(AttachCapabilitiesEvent<ItemStack> event) {
        //if (event.getObject().getItem() == CyberItems.ONEUSEREMOTE.get())
        //    event.addCapability(ONEUSEREMOTE_CAP, new IOneRemote.Provider(new OneUseRemoteCapability(event.getObject())));
    }

    @SubscribeEvent
    public static void attachPlayerCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity)
            event.addCapability(PLAYER_DATA_CAP, new ICyberUser.Provider(new CyberwareCap((PlayerEntity) event.getObject())));
    }




}
