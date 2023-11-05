package net.cyber.mod.items;

import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.ICyberInfo;
import net.minecraft.item.ItemStack;

public class ItemBodyPart extends ItemCyberware implements ICyberInfo.ISidedLimb {

    public ItemBodyPart(Properties properties, EnumSlot slots) {
        super(properties);
        this.slots = slots;
    }

    @Override
    public boolean isEssential(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getEssenceCost(ItemStack stack)
    {
        return 0;
    }

    @Override
    public boolean isIncompatible(ItemStack stack, ItemStack other)
    {
        if (stack.getDamage() <= 7)
        {
            return CyberAPI.getCyberware(other).isEssential(other);
        }

        ICyberInfo ware = CyberAPI.getCyberware(other);

        if (ware instanceof ISidedLimb)
        {
            return ware.isEssential(other) && ((ISidedLimb) ware).getSide(other) == this.getSide(stack);
        }
        return false;
    }

    @Override
    public EnumCategory getCategory(ItemStack stack)
    {
        return EnumCategory.BODYPARTS;
    }

    @Override
    public EnumSide getSide(ItemStack stack)
    {
        return stack.getDamage() % 2 == 0 ? EnumSide.LEFT : EnumSide.RIGHT;
    }
}
