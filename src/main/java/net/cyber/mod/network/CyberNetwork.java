package net.cyber.mod.network;

import net.cyber.mod.CyberMod;
import net.cyber.mod.network.packets.TriggerActiveAbilityPacket;
import net.cyber.mod.network.packets.UpdateHudColorPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CyberNetwork {

	private static int ID = 0;
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(CyberMod.MOD_ID, "main_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    
    public static void init() {
        NETWORK_CHANNEL.registerMessage(nextId(), UpdateHudColorPacket.class, UpdateHudColorPacket::encode, UpdateHudColorPacket::decode, UpdateHudColorPacket::handle);
        NETWORK_CHANNEL.registerMessage(nextId(), TriggerActiveAbilityPacket.class, TriggerActiveAbilityPacket::encode, TriggerActiveAbilityPacket::decode, TriggerActiveAbilityPacket::handle);


    }

    /**
     * Sends a packet to the server.<br>
     * Must be called Client side.
     */
    public static void sendToServer(Object msg) {
        NETWORK_CHANNEL.sendToServer(msg);
    }

    public static int nextId(){
        return ++ID;
    }

}
