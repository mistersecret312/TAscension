package net.cyber.mod.items;

import com.google.common.collect.Lists;
import net.cyber.mod.events.CyberwareUpdateEvent;
import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.CyberCon;
import net.cyber.mod.helper.ICyberData;
import net.cyber.mod.helper.ICyberInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class ItemCyberlimb extends ItemCyberware implements ICyberInfo.ISidedLimb {

    public ItemCyberlimb(Properties properties, EnumSlot slot)
    {
        super(properties);

        this.slots = slots;
    }

    @Override
    public boolean isEssential(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean isIncompatible(ItemStack stack, ItemStack other)
    {
        ICyberInfo ware = CyberAPI.getCyberware(other);

        if (ware instanceof ISidedLimb)
        {
            return ware.isEssential(other) && ((ISidedLimb) ware).getSide(other) == this.getSide(stack);
        }
        return false;
    }

    @Override
    public EnumSide getSide(ItemStack stack)
    {
        return stack.getDamage() % 2 == 0 ? EnumSide.LEFT : EnumSide.RIGHT;
    }

    public static boolean isPowered(ItemStack stack)
    {
        CompoundNBT data = CyberAPI.getCyberwareNBT(stack);
        if (!data.contains("active"))
        {
            data.putBoolean("active", true);
        }
        return data.getBoolean("active");
    }

    private Set<Integer> didFall = new HashSet<>();

    @SubscribeEvent
    public void handleFallDamage(LivingAttackEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();
        if ( entityLivingBase.world.isRemote
                && event.getSource() == DamageSource.FALL )
        {
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
            if (cyberwareUserData == null) return;
            if ( cyberwareUserData.isCyberwareInstalled(CyberItems.CYBERLEL.get().getDefaultInstance())
                    && cyberwareUserData.isCyberwareInstalled(CyberItems.CYBERLER.get().getDefaultInstance()))
            {
                didFall.add(entityLivingBase.getEntityId());
            }
        }
    }

    @SubscribeEvent
    public void handleSound(PlaySoundAtEntityEvent event)
    {
        Entity entity = event.getEntity();
        if ( entity instanceof PlayerEntity
                && event.getSound() == SoundEvents.ENTITY_PLAYER_HURT
                && entity.world.isRemote
                && didFall.contains(entity.getEntityId()) )
        {
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entity);
            if (cyberwareUserData == null) return;

            int numLegs = 0;
            if (cyberwareUserData.isCyberwareInstalled(CyberItems.CYBERLEL.get().getDefaultInstance()))
            {
                numLegs++;
            }
            if (cyberwareUserData.isCyberwareInstalled(CyberItems.CYBERLER.get().getDefaultInstance()))
            {
                numLegs++;
            }

            if (numLegs > 0)
            {
                event.setSound(SoundEvents.ENTITY_IRON_GOLEM_HURT);
                event.setPitch(event.getPitch() + 1F);
                didFall.remove(entity.getEntityId());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void handleLivingUpdate(CyberwareUpdateEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();
        if (entityLivingBase.ticksExisted % 20 != 0) return;

        ICyberData cyberwareUserData = event.getCyberwareUserData();
        for (int damage = 0; damage < 4; damage++)
        {
            List<ItemStack> lis = Lists.newArrayList(CyberItems.CYBERARML.get().getDefaultInstance(), CyberItems.CYBERARMR.get().getDefaultInstance(), CyberItems.CYBERLER.get().getDefaultInstance(), CyberItems.CYBERLEL.get().getDefaultInstance());
            lis.forEach(itemStack -> {
                ItemStack itemStackInstalled = cyberwareUserData.getCyberware(itemStack);
                if (!itemStackInstalled.isEmpty())
                {
                    boolean isPowered = cyberwareUserData.usePower(itemStackInstalled, getPowerConsumption(itemStackInstalled));

                    CyberAPI.getCyberwareNBT(itemStackInstalled).putBoolean("active", isPowered);
                }
            });

        }
    }

    @Override
    public int getPowerConsumption(ItemStack stack)
    {
        return CyberCon.LIMB_CONSUMPTION;
    }
}
