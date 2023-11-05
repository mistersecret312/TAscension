package net.cyber.mod.helper;

import net.minecraft.item.ItemStack;

public interface ICyberI {
    public static enum EnumCategory
    {
        BLOCKS,
        BODYPARTS,
        EYES,
        CRANIUM,
        HEART,
        LUNGS,
        LOWER_ORGANS,
        SKIN,
        MUSCLE,
        BONE,
        ARM,
        HAND,
        LEG,
        FOOT;
    }

    public EnumCategory getCategory(ItemStack stack);
}
