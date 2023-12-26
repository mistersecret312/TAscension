package net.cyber.mod.items;

import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.CyberPartType;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCyberware extends Item implements ICyberPart {
    public ItemCyberware(Properties properties) {
        super(properties);
    }

    public CyberPartEnum cat;
    public CyberPartType type;
    public int essence;

    public ItemCyberware(Properties properties, CyberPartEnum cat, CyberPartType type, int essenceCost){
        super(properties);
        this.cat = cat;
        this.type = type;
        this.essence = essenceCost;
    }

    public ItemCyberware(Properties properties, CyberPartEnum cat, int essenceCost){
        super(properties);
        this.cat = cat;
        this.type = CyberPartType.UPGRADE;
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
        return this.cat;
    }

    @Override
    public CyberPartType getType() {
        return type;
    }

    @Override
    public void runOnce(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getAttributeValue(Attributes.MAX_HEALTH)+2);
    }

    @Override
    public void runOnceUndo(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getAttributeValue(Attributes.MAX_HEALTH)-2);
    }

    @Override
    public void runOnTick(PlayerEntity player) {

    }
}
