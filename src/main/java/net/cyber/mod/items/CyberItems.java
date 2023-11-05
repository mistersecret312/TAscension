package net.cyber.mod.items;

import net.cyber.mod.CyberMod;
import net.cyber.mod.helper.ICyberInfo;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CyberItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CyberMod.MOD_ID);

    public static final RegistryObject<Item> FOODCUBE = ITEMS.register("foodcube", () -> createItem(new Item((new Item.Properties()).group(ItemGroup.FOOD).food(new Food.Builder().hunger(3).saturation(3F).build()))));

    public static final RegistryObject<Item> CYBERARML = ITEMS.register("cyber_arm_left", () -> createItem(new ItemCyberlimb(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.ARM)));
    public static final RegistryObject<Item> CYBERARMR = ITEMS.register("cyber_arm_right", () -> createItem(new ItemCyberlimb(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.ARM)));
    public static final RegistryObject<Item> CYBERLEL = ITEMS.register("cyber_leg_left", () -> createItem(new ItemCyberlimb(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LEG)));
    public static final RegistryObject<Item> CYBERLER = ITEMS.register("cyber_leg_right", () -> createItem(new ItemCyberlimb(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LEG)));

    public static final RegistryObject<Item> NORMALEYE = ITEMS.register("normal_eye", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.EYES)));
    public static final RegistryObject<Item> NORMALBRAIN = ITEMS.register("normal_brain", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.CRANIUM)));
    public static final RegistryObject<Item> NORMALHEARH = ITEMS.register("normal_hearh", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.HEART)));
    public static final RegistryObject<Item> NORMALLUN = ITEMS.register("normal_lungs", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LUNGS)));
    public static final RegistryObject<Item> NORMALSOMACH = ITEMS.register("normal_stomach", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LOWER_ORGANS)));
    public static final RegistryObject<Item> NORMALLIVER = ITEMS.register("normal_liver", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LOWER_ORGANS)));
    public static final RegistryObject<Item> NORMALARML = ITEMS.register("normal_arml", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.ARM)));
    public static final RegistryObject<Item> NORMALARMR = ITEMS.register("normal_armr", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.ARM)));
    public static final RegistryObject<Item> NORMALLEL = ITEMS.register("normal_lel", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LEG)));
    public static final RegistryObject<Item> NORMALLER = ITEMS.register("normal_ler", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.LEG)));
    public static final RegistryObject<Item> NORMALSKIN = ITEMS.register("normal_skin", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.SKIN)));
    public static final RegistryObject<Item> NORMALMUSCLE = ITEMS.register("normal_muscle", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.MUSCLE)));
    public static final RegistryObject<Item> NORMALBONE = ITEMS.register("normal_bone", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.BONE)));
    public static final RegistryObject<Item> NORMALHANDL = ITEMS.register("normal_handl", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.HAND)));
    public static final RegistryObject<Item> NORMALHANDR = ITEMS.register("normal_handr", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.HAND)));
    public static final RegistryObject<Item> NORMALFOOL = ITEMS.register("normal_fool", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.FOOT)));
    public static final RegistryObject<Item> NORMALFOOR = ITEMS.register("normal_foor", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.CYBERMOD), ICyberInfo.EnumSlot.FOOT)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static <T extends Item> T createItem(T item) {
        return item;
    }

}
