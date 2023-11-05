package net.cyber.mod.helper;

import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.platform.GlStateManager;
import net.cyber.mod.CyberMod;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.events.CyberwareUpdateEvent;
import net.cyber.mod.items.CyberItems;
import net.cyber.mod.items.ItemCyberlimb;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EssentialsMissingHandler {
    public static final DamageSource brainless = new DamageSource("cyberware.brainless").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource heartless = new DamageSource("cyberware.heartless").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource surgery = new DamageSource("cyberware.surgery").setDamageBypassesArmor();
    public static final DamageSource spineless = new DamageSource("cyberware.spineless").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource nomuscles = new DamageSource("cyberware.nomuscles").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource noessence = new DamageSource("cyberware.noessence").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource lowessence = new DamageSource("cyberware.lowessence").setDamageBypassesArmor().setDamageIsAbsolute();

    public static final EssentialsMissingHandler INSTANCE = new EssentialsMissingHandler();

    private static Map<Integer, Integer> timesLungs = new HashMap<>();

    private static final UUID idMissingLegSpeedAttribute = UUID.fromString("fe00fdea-5044-11e6-beb8-9e71128cae77");
    private static final HashMultimap<String, AttributeModifier> multimapMissingLegSpeedAttribute;

    static {
        multimapMissingLegSpeedAttribute = HashMultimap.create();
        multimapMissingLegSpeedAttribute.put(Attributes.MOVEMENT_SPEED.getAttributeName(), new AttributeModifier(idMissingLegSpeedAttribute, "Missing leg speed", -100F, AttributeModifier.Operation.byId(0)));
    }

    private Map<Integer, Boolean> last = new HashMap<>();
    private Map<Integer, Boolean> lastClient = new HashMap<>();

    @SubscribeEvent
    public void triggerCyberwareEvent(LivingEvent.LivingUpdateEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            CyberwareUpdateEvent cyberwareUpdateEvent = new CyberwareUpdateEvent(entityLivingBase, cyberwareUserData);
            MinecraftForge.EVENT_BUS.post(cyberwareUpdateEvent);
        }
    }

    @SubscribeEvent(priority= EventPriority.LOWEST)
    public void handleMissingEssentials(CyberwareUpdateEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();
        ICyberData cyberwareUserData = event.getCyberwareUserData();

        if (entityLivingBase.ticksExisted % 20 == 0)
        {
            cyberwareUserData.resetBuffer();
        }

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.CRANIUM))
        {
            entityLivingBase.attackEntityFrom(brainless, Integer.MAX_VALUE);
        }

        if ( entityLivingBase instanceof PlayerEntity
                && entityLivingBase.ticksExisted % 20 == 0 )
        {
            int tolerance = cyberwareUserData.getTolerance(entityLivingBase);

            if (tolerance <= 0)
            {
                entityLivingBase.attackEntityFrom(noessence, Integer.MAX_VALUE);
            }

            if ( tolerance < CyberConfigs.SERVER.StattenheimRecipe.get()
                    && entityLivingBase.ticksExisted % 100 == 0
                    && !((PlayerEntity) entityLivingBase).getActivePotionEffects().contains(Potions.REGENERATION.getEffects().stream()) )
            {
                ((PlayerEntity) entityLivingBase).addPotionEffect(new EffectInstance(Effects.POISON, 110, 0, true, false));
                entityLivingBase.attackEntityFrom(lowessence, 2F);
            }

            if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.EYES))
            {
                ((PlayerEntity) entityLivingBase).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 40));
            }
        }

        int numMissingLegs = 0;
        int numMissingLegsVisible = 0;

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LEG, ICyberInfo.ISidedLimb.EnumSide.LEFT))
        {
            numMissingLegs++;
            numMissingLegsVisible++;
        }
        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LEG, ICyberInfo.ISidedLimb.EnumSide.RIGHT))
        {
            numMissingLegs++;
            numMissingLegsVisible++;
        }

        ItemStack legLeft = cyberwareUserData.getCyberware(CyberItems.CYBERLEL.get().getDefaultInstance());
        if ( !legLeft.isEmpty()
                && !ItemCyberlimb.isPowered(legLeft) )
        {
            numMissingLegs++;
        }

        ItemStack legRight = cyberwareUserData.getCyberware(CyberItems.CYBERLER.get().getDefaultInstance());
        if ( !legRight.isEmpty()
                && !ItemCyberlimb.isPowered(legRight) )
        {
            numMissingLegs++;
        }

        if (entityLivingBase instanceof PlayerEntity)
        {
            if (numMissingLegsVisible == 2)
            {


                if (entityLivingBase.world.isRemote)
                {
                    lastClient.put(entityLivingBase.getEntityId(), true);
                }
                else
                {
                    last.put(entityLivingBase.getEntityId(), true);
                }
            }
            else if (last(entityLivingBase.world.isRemote, entityLivingBase))
            {
                if (entityLivingBase.world.isRemote)
                {
                    lastClient.put(entityLivingBase.getEntityId(), false);
                }
                else
                {
                    last.put(entityLivingBase.getEntityId(), false);
                }
            }
        }

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.HEART))
        {
            entityLivingBase.attackEntityFrom(heartless, Integer.MAX_VALUE);
        }

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.BONE))
        {
            entityLivingBase.attackEntityFrom(spineless, Integer.MAX_VALUE);
        }

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.MUSCLE))
        {
            entityLivingBase.attackEntityFrom(nomuscles, Integer.MAX_VALUE);
        }

        if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LUNGS))
        {
            if (getLungsTime(entityLivingBase) >= 20)
            {
                timesLungs.put(entityLivingBase.getEntityId(), entityLivingBase.ticksExisted);
                entityLivingBase.attackEntityFrom(DamageSource.DROWN, 2F);
            }
        }
        else if (entityLivingBase.ticksExisted % 20 == 0)
        {
            timesLungs.remove(entityLivingBase.getEntityId());
        }
    }

    private boolean last(boolean remote, Entity entityLivingBase)
    {
        if (remote)
        {
            if (!lastClient.containsKey(entityLivingBase.getEntityId()))
            {
                lastClient.put(entityLivingBase.getEntityId(), false);
            }
            return lastClient.get(entityLivingBase.getEntityId());
        }
        else
        {
            if (!last.containsKey(entityLivingBase.getEntityId()))
            {
                last.put(entityLivingBase.getEntityId(), false);
            }
            return last.get(entityLivingBase.getEntityId());
        }
    }

    @SubscribeEvent
    public void handleJump(LivingEvent.LivingJumpEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            int numMissingLegs = 0;

            if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LEG, ICyberInfo.ISidedLimb.EnumSide.LEFT))
            {
                numMissingLegs++;
            }
            if (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LEG, ICyberInfo.ISidedLimb.EnumSide.RIGHT))
            {
                numMissingLegs++;
            }

            ItemStack legLeft = cyberwareUserData.getCyberware(CyberItems.CYBERLEL.get().getDefaultInstance());
            if (!legLeft.isEmpty() && !ItemCyberlimb.isPowered(legLeft))
            {
                numMissingLegs++;
            }

            ItemStack legRight = cyberwareUserData.getCyberware(CyberItems.CYBERLER.get().getDefaultInstance());
            if (!legRight.isEmpty() && !ItemCyberlimb.isPowered(legRight))
            {
                numMissingLegs++;
            }

            if (numMissingLegs == 2)
            {
                entityLivingBase.distanceWalkedOnStepModified = 0.2F;
            }
        }
    }

    private int getLungsTime(@Nonnull Entity entityLivingBase)
    {
        Integer timeLungs = timesLungs.computeIfAbsent(entityLivingBase.getEntityId(), k -> entityLivingBase.ticksExisted);
        return entityLivingBase.ticksExisted - timeLungs;
    }

    private static Map<Integer, Integer> mapHunger = new HashMap<>();
    private static Map<Integer, Float> mapSaturation = new HashMap<>();

    @SubscribeEvent
    public void handleEatFoodTick(LivingEntityUseItemEvent.Tick event)
    {
        Entity entityLivingBase = event.getEntityLiving();
        ItemStack stack = event.getItem();

        if (entityLivingBase == null) return;

        if ( entityLivingBase instanceof PlayerEntity
                && !stack.isEmpty()
                && stack.getItem().getUseAction(stack) == UseAction.EAT )
        {
            PlayerEntity entityPlayer = (PlayerEntity) entityLivingBase;
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);

            if (cyberwareUserData != null && !cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LOWER_ORGANS))
            {
                mapHunger.put(entityPlayer.getEntityId(), entityPlayer.getFoodStats().getFoodLevel());
                mapSaturation.put(entityPlayer.getEntityId(), entityPlayer.getFoodStats().getSaturationLevel());
                return;
            }
        }

        mapHunger.remove(entityLivingBase.getEntityId());
        mapSaturation.remove(entityLivingBase.getEntityId());
    }

    @SubscribeEvent
    public void handleEatFoodEnd(LivingEntityUseItemEvent.Finish event)
    {
        Entity entityLivingBase = event.getEntityLiving();
        ItemStack stack = event.getItem();

        if ( entityLivingBase instanceof PlayerEntity
                && !stack.isEmpty()
                && stack.getItem().getUseAction(stack) == UseAction.EAT )
        {
            PlayerEntity entityPlayer = (PlayerEntity) entityLivingBase;
            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);

            if (cyberwareUserData != null && !cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.LOWER_ORGANS))
            {
                Integer hunger = mapHunger.get(entityPlayer.getEntityId());
                if (hunger != null)
                {
                    entityPlayer.getFoodStats().setFoodLevel(hunger);
                }

                Float saturation = mapSaturation.get(entityPlayer.getEntityId());
                if (saturation != null)
                {
                    // note: setFoodSaturationLevel() is client side only
                    FoodStats foodStats = entityPlayer.getFoodStats();
                    CompoundNBT tagCompound = new CompoundNBT();
                    foodStats.write(tagCompound);
                    tagCompound.putFloat("foodSaturationLevel", saturation);
                    foodStats.read(tagCompound);
                }
            }
        }
    }

    public static final ResourceLocation BLACK_PX = new ResourceLocation(CyberMod.MOD_ID + ":textures/gui/blackpx.png");



    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void overlayPre(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            PlayerEntity entityPlayer = Minecraft.getInstance().player;
            if (entityPlayer == null) return;

            ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityPlayer);
            if ( cyberwareUserData != null
                    && !cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.EYES)
                    && !entityPlayer.isCreative() )
            {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.blendColor(1.0F, 1.0F, 1.0F, 0.9F);
                Minecraft.getInstance().getTextureManager().bindTexture(BLACK_PX);
                ClientUtils.drawTexturedModalRect(0, 0, 0, 0, Minecraft.getInstance().getMainWindow().getWindowX(), Minecraft.getInstance().getMainWindow().getWindowY());
                GlStateManager.popMatrix();
            }

            if (TileEntitySurgery.workingOnPlayer)
            {
                float trans = 1.0F;
                float ticks = TileEntitySurgery.playerProgressTicks + event.getPartialTicks();
                if (ticks < 20F)
                {
                    trans = ticks / 20F;
                }
                else if (ticks > 60F)
                {
                    trans = (80F - ticks) / 20F;
                }
                GlStateManager.enableBlend();
                GlStateManager.blendColor(1.0F, 1.0F, 1.0F, trans);
                Minecraft.getInstance().getTextureManager().bindTexture(BLACK_PX);
                ClientUtils.drawTexturedModalRect(0, 0, 0, 0, Minecraft.getInstance().getMainWindow().getWindowX(), Minecraft.getInstance().getMainWindow().getWindowY());
                GlStateManager.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
            }
        }
    }

    @SubscribeEvent
    public void handleMissingSkin(LivingHurtEvent event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            if ( !cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.SKIN)
                    && ( !event.getSource().isUnblockable()
                    || event.getSource() == DamageSource.FALL ) )
            {
                event.setAmount(event.getAmount() * 3F);
            }
        }
    }

    @SubscribeEvent
    public void handleEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            processEvent(event, event.getHand(), event.getPlayer(), cyberwareUserData);
        }
    }

    @SubscribeEvent
    public void handleLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            processEvent(event, event.getHand(), event.getPlayer(), cyberwareUserData);
        }
    }

    @SubscribeEvent
    public void handleRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            processEvent(event, event.getHand(), event.getPlayer(), cyberwareUserData);
        }
    }

    @SubscribeEvent
    public void handleRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        Entity entityLivingBase = event.getEntityLiving();

        ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(entityLivingBase);
        if (cyberwareUserData != null)
        {
            processEvent(event, event.getHand(), event.getPlayer(), cyberwareUserData);
        }
    }

    private void processEvent(Event event, Hand hand, PlayerEntity entityPlayer, ICyberData cyberwareUserData)
    {
        HandSide mainHand = entityPlayer.getPrimaryHand();
        HandSide offHand = ((mainHand == HandSide.LEFT) ? HandSide.RIGHT : HandSide.LEFT);
        ICyberInfo.ISidedLimb.EnumSide correspondingMainHand = ((mainHand == HandSide.RIGHT) ? ICyberInfo.ISidedLimb.EnumSide.RIGHT : ICyberInfo.ISidedLimb.EnumSide.LEFT);
        ICyberInfo.ISidedLimb.EnumSide correspondingOffHand = ((offHand == HandSide.RIGHT) ? ICyberInfo.ISidedLimb.EnumSide.RIGHT : ICyberInfo.ISidedLimb.EnumSide.LEFT);

        boolean leftUnpowered = false;
        ItemStack armLeft = cyberwareUserData.getCyberware(CyberItems.CYBERARML.get().getDefaultInstance());
        if (!armLeft.isEmpty() && !ItemCyberlimb.isPowered(armLeft))
        {
            leftUnpowered = true;
        }

        boolean rightUnpowered = false;
        ItemStack armRight = cyberwareUserData.getCyberware(CyberItems.CYBERARMR.get().getDefaultInstance());
        if (!armRight.isEmpty() && !ItemCyberlimb.isPowered(armRight))
        {
            rightUnpowered = true;
        }

        if (hand == Hand.MAIN_HAND && (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.ARM, correspondingMainHand) || leftUnpowered))
        {
            event.setCanceled(true);
        }
        else if (hand == Hand.OFF_HAND && (!cyberwareUserData.hasEssential(ICyberInfo.EnumSlot.ARM, correspondingOffHand) || rightUnpowered))
        {
            event.setCanceled(true);
        }
    }
}
