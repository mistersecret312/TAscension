package net.cyber.mod.network.packets;

import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.ICyberData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class UpdateHudColorPacket {

    private int color;

    public UpdateHudColorPacket(int hudColorHex) {
        this.color = hudColorHex;
    }

    public static void encode(UpdateHudColorPacket mes, PacketBuffer buf) {
        buf.writeInt(mes.color);
    }

    public static UpdateHudColorPacket decode(PacketBuffer buf) {
        return new UpdateHudColorPacket(buf.readInt());
    }

    public static void handle(UpdateHudColorPacket mes, Supplier<NetworkEvent.Context> con) {
        con.get().enqueueWork(() -> {
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(con.get().getSender());
            if (cyberwareUserData != null)
            {
                cyberwareUserData.setHudColor(mes.color);
            }
        });
        con.get().setPacketHandled(true);
    }
}
