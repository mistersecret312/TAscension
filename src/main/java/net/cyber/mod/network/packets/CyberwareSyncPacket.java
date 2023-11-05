package net.cyber.mod.network.packets;

import net.cyber.mod.events.CyberwareHudDataEvent;
import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.HudNBTData;
import net.cyber.mod.helper.ICyberData;
import net.cyber.mod.helper.IHudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class CyberwareSyncPacket {

    private CompoundNBT data;
    private int entityId;

    public CyberwareSyncPacket(CompoundNBT tagCompound, int entityId) {
        this.data = tagCompound;
        this.entityId = entityId;
    }

    public static void encode(CyberwareSyncPacket mes, PacketBuffer buf) {
        buf.writeCompoundTag(mes.data);
        buf.writeInt(mes.entityId);
    }

    public static CyberwareSyncPacket decode(PacketBuffer buf) {
        return new CyberwareSyncPacket(buf.readCompoundTag(), buf.readInt());
    }

    public static void handle(CyberwareSyncPacket mes, Supplier<NetworkEvent.Context> con) {
        con.get().enqueueWork(() -> {
            Entity targetEntity = Minecraft.getInstance().world.getEntityByID(mes.entityId);
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(targetEntity);
            if (cyberwareUserData != null)
            {
                cyberwareUserData.deserializeNBT(mes.data);

                if (targetEntity == Minecraft.getInstance().player)
                {
                    CompoundNBT tagCompound = cyberwareUserData.getHudData();

                    CyberwareHudDataEvent hudEvent = new CyberwareHudDataEvent();
                    MinecraftForge.EVENT_BUS.post(hudEvent);
                    List<IHudElement> elements = hudEvent.getElements();

                    for (IHudElement element : elements)
                    {
                        if (tagCompound.contains(element.getUniqueName()))
                        {
                            element.load(new HudNBTData((CompoundNBT) tagCompound.get(element.getUniqueName())));
                        }
                    }
                }
            }
        });
        con.get().setPacketHandled(true);
    }
}
