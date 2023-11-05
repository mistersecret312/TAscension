package net.cyber.mod.helper;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.cap.ICyberUser;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.network.CyberNetwork;
import net.cyber.mod.network.packets.CyberwareSyncPacket;
import net.cyber.mod.network.packets.UpdateHudColorPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyberAPI {
    public static final String DATA_TAG = "cyberwareFunctionData";
    public static Map<ItemStack, ICyberInfo> linkedWare = new HashMap<>();
    public static PacketDispatcher PACKET_HANDLER;


    public static final Attribute TOLERANCE_ATTR = new RangedAttribute( "cyberware.tolerance", CyberConfigs.COMMON.FoodCubeCost.get(), 0.0F, Double.MAX_VALUE);

    @OnlyIn(Dist.CLIENT)
    public static void setHUDColor(float[] color) {
        PlayerEntity PlayerEntity = Minecraft.getInstance().player;
        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(PlayerEntity);
        if (cyberwareUserData != null) {
            cyberwareUserData.setHudColor(color);
        }
    }

    public static void syncHUDColor() {
        CyberNetwork.sendToServer(new UpdateHudColorPacket(getHUDColorHex()));
    }

    @OnlyIn(Dist.CLIENT)
    public static void setHUDColor(int hexVal) {
        PlayerEntity PlayerEntity = Minecraft.getInstance().player;
        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(PlayerEntity);
        if (cyberwareUserData != null) {
            cyberwareUserData.setHudColor(hexVal);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void setHUDColor(float r, float g, float b) {
        setHUDColor(new float[] { r, g, b });
    }

    @OnlyIn(Dist.CLIENT)
    public static int getHUDColorHex() {
        PlayerEntity PlayerEntity = Minecraft.getInstance().player;
        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(PlayerEntity);
        if (cyberwareUserData != null) {
            return cyberwareUserData.getHudColorHex();
        }
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static float[] getHUDColor() {
        PlayerEntity PlayerEntity = Minecraft.getInstance().player;
        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(PlayerEntity);
        if (cyberwareUserData != null) {
            return cyberwareUserData.getHudColor();
        }
        return new float[] { 0F, 0F, 0F };
    }

    public static ItemStack sanitize(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            CompoundNBT tagCompound = stack.serializeNBT();
            if (tagCompound != null && tagCompound.contains(DATA_TAG)) {
                tagCompound.remove(DATA_TAG);
            }
            if (tagCompound != null && tagCompound.isEmpty()) {
                stack.setTag(null);
            }
        }
        return stack;
    }

    @Nonnull
    public static CompoundNBT getCyberwareNBT(@Nonnull ItemStack stack) {
        CompoundNBT tagCompound = stack.serializeNBT();
        if (tagCompound == null) {
            tagCompound = new CompoundNBT();
            stack.setTag(tagCompound);
        }
        if (!tagCompound.contains(DATA_TAG)) {
            tagCompound.put(DATA_TAG, new CompoundNBT());
        }
        return tagCompound.getCompound(DATA_TAG);
    }

    public static boolean areCyberwareStacksEqual(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) return false;
        ItemStack sanitized1 = sanitize(stack1.copy());
        ItemStack sanitized2 = sanitize(stack2.copy());
        return sanitized1.isItemEqual(sanitized2) && ItemStack.areItemStackTagsEqual(sanitized1, sanitized2);
    }

    public static void linkCyberware(ItemStack stack, ICyberInfo cyberware) {
        linkedWare.put(stack, cyberware);
    }

    public static void linkCyberware(Item item, ICyberInfo cyberware) {
        linkedWare.put(new ItemStack(item), cyberware);
    }

    public static ICyberInfo getCyberware(ItemStack stack) {
        return linkedWare.get(stack);
    }

    public static boolean isCyberware(ItemStack stack) {
        return getCyberware(stack) != null;
    }

    public static boolean isCyberware(Item item) {
        return getCyberware(new ItemStack(item)) != null;
    }

    public static List<ItemStack> getComponents(ItemStack stack) {
        if (stack.getItem() instanceof IDeconstructable) {
            return ((IDeconstructable) stack.getItem()).getComponents(stack);
        }
        return Collections.emptyList();
    }

    public static ICyberData getCyberwareUserData(Entity entity) {
        return (ICyberData) entity.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY);
    }

    @Deprecated
    public static boolean hasCapability(Entity entity) {
        return entity.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).isPresent();
    }

    @Nullable
    public static ICyberData getCapabilityOrNull(@Nullable Entity targetEntity) {
        if (targetEntity == null) return null;
        LazyOptional<ICyberData> capability = targetEntity.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY);
        return capability.resolve().orElse(null);
    }


    public static void updateData(Entity targetEntity)
    {
        if (!targetEntity.world.isRemote)
        {
            ServerWorld world = targetEntity.world.getServer().func_241755_D_();

            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(targetEntity);
            if (cyberwareUserData == null) return;
            CompoundNBT tagCompound = cyberwareUserData.serializeNBT();

            if (targetEntity instanceof PlayerEntity)
            {
                CyberNetwork.sendToServer(new CyberwareSyncPacket(tagCompound, targetEntity.getEntityId()));
                // Cyberware.logger.info("Sent data for player " + ((EntityPlayer) targetEntity).getName() + " to that player's client");
            }

            for (PlayerEntity trackingPlayer : world.getPlayers())
            {
                CyberNetwork.sendToServer(new CyberwareSyncPacket(tagCompound, targetEntity.getEntityId()));
				/*
				if (targetEntity instanceof EntityPlayer)
				{
					Cyberware.logger.info("Sent data for player " + ((EntityPlayer) targetEntity).getName() + " to player " + trackingPlayer.getName());
				}
				*/
            }
        }
    }

    public static void useActiveItem(Entity entity, ItemStack stack)
    {
        ((IMenuItem) stack.getItem()).use(entity, stack);
    }
}
