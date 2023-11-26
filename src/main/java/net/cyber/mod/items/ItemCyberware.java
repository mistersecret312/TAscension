package net.cyber.mod.items;

import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCyberware extends Item implements ICyberPart {
    public ItemCyberware(Properties properties) {
        super(properties);
    }

    public CyberPartEnum type;
    public int essence;

    public ItemCyberware(Properties properties, CyberPartEnum type, int essenceCost){
        super(properties);

        this.type = type;
        this.essence = essenceCost;


    }

    @Override
    public void setEssenceCost(int essence)
    {
        this.essence = essence;
    }

    @Override
    public int getEssenceCost()
    {
        return essence;
    }

    @Override
    public CyberPartEnum getCategory(ItemStack stack) {
        return this.type;
    }
}
