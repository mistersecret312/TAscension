package net.cyber.mod.items;

import net.cyber.mod.CyberMod;
import net.cyber.mod.helper.ICyberI;
import net.cyber.mod.helper.ICyberInfo;
import net.cyber.mod.helper.IDeconstructable;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemCyberware extends Item implements ICyberInfo, IDeconstructable, ICyberI {
    public ItemCyberware(Properties properties) {
        super(properties);
    }

    public EnumSlot slots;
    public int[] essence;
    public String[] subnames;
    private NonNullList<NonNullList<ItemStack>> components;
    private ItemStack[] itemStackCache;

    public ItemCyberware(Properties properties,String name, EnumSlot slots)
    {
        super(properties);

        this.slots = slots;

        this.essence = new int[subnames.length + 1];
        this.components = NonNullList.create();

        itemStackCache = new ItemStack[Math.max(subnames.length, 1)];


    }

    public ItemCyberware setWeights(int... weight)
    {
        assert weight.length == Math.max(1, components.size());
        for (int meta = 0; meta < weight.length; meta++)
        {
            ItemStack stack = new ItemStack(this, 1);
            int installedStackSize = installedStackSize(stack);
            stack.setCount(installedStackSize);
        }
        return this;
    }

    public ItemCyberware setEssenceCost(int... essence)
    {
        assert essence.length == Math.max(1, components.size());
        this.essence = essence;
        return this;
    }

    public ItemCyberware setComponents(NonNullList<ItemStack>... components)
    {
        assert components.length == Math.max(1, components.length);
        NonNullList<NonNullList<ItemStack>> list = NonNullList.create();
        Collections.addAll(list, components);
        this.components = list;
        return this;
    }

    @Override
    public int getEssenceCost(ItemStack stack)
    {
        int cost = getUnmodifiedEssenceCost(stack);
        return cost;
    }

    protected int getUnmodifiedEssenceCost(ItemStack stack)
    {
        return essence[Math.min(this.components.size(), stack.getDamage())];
    }


    @Override
    public EnumSlot getSlot(ItemStack stack)
    {
        return slots;
    }

    @Override
    public int installedStackSize(ItemStack stack)
    {
        return 1;
    }

    @Override
    public boolean isIncompatible(ItemStack stack, ItemStack other)
    {
        return false;
    }

    @Override
    public boolean isEssential(ItemStack stack)
    {
        return false;
    }

    @Override
    public List<String> getInfo(ItemStack stack)
    {
        List<String> ret = new ArrayList<>();
        List<String> desc = this.getDesciption(stack);
        if (desc != null && desc.size() > 0)
        {

            ret.addAll(desc);

        }
        return ret;
    }

    public List<String> getStackDesc(ItemStack stack)
    {
        String[] toReturnArray = I18n.format("cyberware.tooltip." + this.getRegistryName().toString().substring(10)
                + (this.components.size() > 0 ? "." + stack.getDamage() : "")).split("\\\\n");
        List<String> toReturn = new ArrayList<>(Arrays.asList(toReturnArray));

        if (toReturn.size() > 0 && toReturn.get(0).length() == 0)
        {
            toReturn.remove(0);
        }

        return toReturn;
    }

    public List<String> getDesciption(ItemStack stack)
    {
        List<String> toReturn = getStackDesc(stack);

        if (installedStackSize(stack) > 1)
        {
            toReturn.add(TextFormatting.BLUE + I18n.format("cyberware.tooltip.max_install", installedStackSize(stack)));
        }

        boolean hasPowerConsumption = false;
        String toAddPowerConsumption = "";
        for (int i = 0; i < installedStackSize(stack); i++)
        {
            ItemStack temp = stack.copy();
            temp.setCount(i+1);
            int cost = this.getPowerConsumption(temp);
            if (cost > 0)
            {
                hasPowerConsumption = true;
            }

            if (i != 0)
            {
                toAddPowerConsumption += I18n.format("cyberware.tooltip.joiner");
            }

            toAddPowerConsumption += " " + cost;
        }

        if (hasPowerConsumption)
        {
            String toTranslate = hasCustomPowerMessage(stack) ?
                    "cyberware.tooltip." + this.getRegistryName().toString().substring(10)
                            + (this.components.size() > 0 ? "." + stack.getDamage() : "") + ".power_consumption"
                    :
                    "cyberware.tooltip.power_consumption";
            toReturn.add(TextFormatting.GREEN + I18n.format(toTranslate, toAddPowerConsumption));
        }

        boolean hasPowerProduction = false;
        String toAddPowerProduction = "";
        for (int i = 0; i < installedStackSize(stack); i++)
        {
            ItemStack temp = stack.copy();
            temp.setCount(i+1);
            int cost = this.getPowerProduction(temp);
            if (cost > 0)
            {
                hasPowerProduction = true;
            }

            if (i != 0)
            {
                toAddPowerProduction += I18n.format("cyberware.tooltip.joiner");
            }

            toAddPowerProduction += " " + cost;
        }

        if (hasPowerProduction)
        {
            String toTranslate = hasCustomPowerMessage(stack) ?
                    "cyberware.tooltip." + this.getRegistryName().toString().substring(10)
                            + (this.components.size() > 0 ? "." + stack.getDamage() : "") + ".power_production"
                    :
                    "cyberware.tooltip.power_production";
            toReturn.add(TextFormatting.GREEN + I18n.format(toTranslate, toAddPowerProduction));
        }

        if (getCapacity(stack) > 0)
        {
            String toTranslate = hasCustomCapacityMessage(stack) ?
                    "cyberware.tooltip." + this.getRegistryName().toString().substring(10)
                            + (this.components.size() > 0 ? "." + stack.getDamage() : "") + ".capacity"
                    :
                    "cyberware.tooltip.capacity";
            toReturn.add(TextFormatting.GREEN + I18n.format(toTranslate, getCapacity(stack)));
        }


        boolean hasEssenceCost = false;
        boolean essenceCostNegative = true;
        String toAddEssence = "";
        for (int i = 0; i < installedStackSize(stack); i++)
        {
            ItemStack temp = stack.copy();
            temp.setCount(i+1);
            int cost = this.getEssenceCost(temp);
            if (cost != 0)
            {
                hasEssenceCost = true;
            }
            if (cost < 0)
            {
                essenceCostNegative = false;
            }

            if (i != 0)
            {
                toAddEssence += I18n.format("cyberware.tooltip.joiner");
            }

            toAddEssence += " " + Math.abs(cost);
        }

        if (hasEssenceCost)
        {
            toReturn.add(TextFormatting.DARK_PURPLE + I18n.format(essenceCostNegative ? "cyberware.tooltip.essence" : "cyberware.tooltip.essence_add", toAddEssence));
        }




        return toReturn;
    }

    public int getPowerConsumption(ItemStack stack)
    {
        return 0;
    }

    public int getPowerProduction(ItemStack stack)
    {
        return 0;
    }

    public boolean hasCustomPowerMessage(ItemStack stack)
    {
        return false;
    }

    public boolean hasCustomCapacityMessage(ItemStack stack)
    {
        return false;
    }

    @Override
    public NonNullList<NonNullList<ItemStack>> required(ItemStack stack)
    {
        return NonNullList.create();
    }

    @Override
    public EnumCategory getCategory(ItemStack stack)
    {
        return EnumCategory.values()[this.getSlot(stack).ordinal()];
    }

    @Override
    public int getCapacity(ItemStack wareStack)
    {
        return 0;
    }



    @Override
    public void onAdded(Entity entityLivingBase, ItemStack stack)
    {
        // no operation
    }

    @Override
    public void onRemoved(Entity entityLivingBase, ItemStack stack)
    {
        // no operation
    }

    @Override
    public boolean canDestroy(ItemStack stack)
    {
        return stack.getDamage() < this.components.size();
    }

    @Override
    public NonNullList<ItemStack> getComponents(ItemStack stack)
    {
        return components.get(Math.min(this.components.size() - 1, stack.getDamage()));
    }

    public ItemStack getCachedStack(int damage)
    {
        ItemStack itemStack = itemStackCache[damage];
        if ( itemStack != null
                && ( itemStack.getItem() != this
                || itemStack.getCount() != 1
                || getDamage(itemStack) != damage ) )
        {
            CyberMod.LOGGER.error(String.format("Corrupted item stack cache: found %s as %s:%d, expected %s:%d",
                    itemStack, itemStack.getItem(), itemStack.getDamage(),
                    this, damage ));
            itemStack = null;
        }
        if (itemStack == null)
        {
            itemStack = new ItemStack(this, 1);
            itemStackCache[damage] = itemStack;
        }
        return itemStack;
    }

}
