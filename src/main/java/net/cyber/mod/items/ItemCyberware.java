package net.cyber.mod.items;

import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
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
    public CyberPartEnum getCategory() {
        return this.type;
    }

    @Override
    public void runOnce(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getAttribute(Attributes.MAX_HEALTH).getValue()+2);
    }

    @Override
    public void runOnceUndo(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getAttributeValue(Attributes.MAX_HEALTH)-2);
    }

    @Override
    public void runOnTick(PlayerEntity player) {

    }
}
