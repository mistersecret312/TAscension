package net.cyber.mod.network.packets;

import io.netty.buffer.ByteBuf;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.ICyberData;
import net.cyber.mod.tags.CyberItemTags;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TriggerActiveAbilityPacket  {

    private Item stack;

    public TriggerActiveAbilityPacket(Item stack) {
        this.stack = stack;
    }

    public static void encode(TriggerActiveAbilityPacket mes, PacketBuffer buf) {
        buf.writeResourceLocation(mes.stack.getRegistryName());
    }

    public static TriggerActiveAbilityPacket decode(PacketBuffer buf) {
        return new TriggerActiveAbilityPacket(ForgeRegistries.ITEMS.getValue(buf.readResourceLocation()));
    }

    public static void handle(TriggerActiveAbilityPacket mes, Supplier<NetworkEvent.Context> con) {
        con.get().enqueueWork(() -> {
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(con.get().getSender());
            if (cyberwareUserData != null) {
                CyberAPI.useActiveItem(con.get().getSender(), cyberwareUserData.getCyberware(mes.stack.getDefaultInstance()));
            }
        });
        con.get().setPacketHandled(true);
    }
}
