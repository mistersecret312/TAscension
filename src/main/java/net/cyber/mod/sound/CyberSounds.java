package net.cyber.mod.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.cyber.mod.CyberMod;

public class CyberSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CyberMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DISC1 =
            soundEventRegister("disc1");

    public static final RegistryObject<SoundEvent> DISC2 =
            soundEventRegister("disc2");

    public static final RegistryObject<SoundEvent> DISC3 =
            soundEventRegister("disc3");

    public static final RegistryObject<SoundEvent> DISC4 =
            soundEventRegister("disc4");


    public static final RegistryObject<SoundEvent> VERAMB1 =
            soundEventRegister("verge_ambient");

    public static final RegistryObject<SoundEvent> FULLREMA =
            soundEventRegister("remasound");

    public static final RegistryObject<SoundEvent> MURASAMA =
            soundEventRegister("murasama");

    public static final RegistryObject<SoundEvent> POWERAXE =
            soundEventRegister("poweraxe");



    public static RegistryObject<SoundEvent> soundEventRegister(String name) {
        return SOUND_EVENT.register(name, () -> new SoundEvent(new ResourceLocation(CyberMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENT.register(eventBus);
    }
}
