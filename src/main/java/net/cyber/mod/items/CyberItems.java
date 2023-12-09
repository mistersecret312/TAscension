package net.cyber.mod.items;

import net.cyber.mod.CyberMod;
import net.cyber.mod.helper.CyberPartEnum;
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

    public static final RegistryObject<Item> CYBERARML = ITEMS.register("cyber_arm_left", () -> createItem(new ItemCyberware(new Item.Properties().group(CyberItemGroups.CYBERNETIC), CyberPartEnum.ARM, 10)));
    public static final RegistryObject<Item> CYBERARMR = ITEMS.register("cyber_arm_right", () -> createItem(new ItemCyberware(new Item.Properties().group(CyberItemGroups.CYBERNETIC), CyberPartEnum.ARM, 10)));
    public static final RegistryObject<Item> CYBERLEL = ITEMS.register("cyber_leg_left", () -> createItem(new ItemCyberware(new Item.Properties().group(CyberItemGroups.CYBERNETIC), CyberPartEnum.LEG, 10)));
    public static final RegistryObject<Item> CYBERLER = ITEMS.register("cyber_leg_right", () -> createItem(new ItemCyberware(new Item.Properties().group(CyberItemGroups.CYBERNETIC), CyberPartEnum.LEG, 10)));

    public static final RegistryObject<Item> NORMALEYE = ITEMS.register("normal_eye", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.EYES)));
    public static final RegistryObject<Item> NORMALBRAIN = ITEMS.register("normal_brain", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.CRANIUM)));
    public static final RegistryObject<Item> NORMALHEARH = ITEMS.register("normal_heart", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.HEART)));
    public static final RegistryObject<Item> NORMALLUN = ITEMS.register("normal_lungs", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.LUNGS)));
    public static final RegistryObject<Item> NORMALSOMACH = ITEMS.register("normal_stomach", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.LOWER_ORGANS)));
    public static final RegistryObject<Item> NORMALLIVER = ITEMS.register("normal_liver", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.LOWER_ORGANS)));
    public static final RegistryObject<Item> NORMALARML = ITEMS.register("normal_arm_left", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.ARM)));
    public static final RegistryObject<Item> NORMALARMR = ITEMS.register("normal_arm_right", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.ARM)));
    public static final RegistryObject<Item> NORMALLEL = ITEMS.register("normal_leg_left", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.LEG)));
    public static final RegistryObject<Item> NORMALLER = ITEMS.register("normal_leg_right", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.LEG)));
    public static final RegistryObject<Item> NORMALSKIN = ITEMS.register("normal_skin", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.SKIN)));
    public static final RegistryObject<Item> NORMALMUSCLE = ITEMS.register("normal_muscle", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.MUSCLE)));
    public static final RegistryObject<Item> NORMALBONE = ITEMS.register("normal_bone", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.BONE)));
    public static final RegistryObject<Item> NORMALHANDL = ITEMS.register("normal_hand", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.HAND)));
    public static final RegistryObject<Item> NORMALFOOL = ITEMS.register("normal_foot", () -> createItem(new ItemBodyPart(new Item.Properties().group(CyberItemGroups.ORGANIC), CyberPartEnum.FOOT)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static <T extends Item> T createItem(T item) {
        return item;
    }

}
