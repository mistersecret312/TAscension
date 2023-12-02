package net.cyber.mod.container.slots;

import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class UpgradeSlot extends SlotItemHandler {
    Predicate<CyberPartEnum> filter;

    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(stack -> true, itemHandler, index, xPosition, yPosition);
    }

    public UpgradeSlot(Predicate<CyberPartEnum> filter, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);;
        this.filter = filter;
    }

    public void setFilter(Predicate<CyberPartEnum> filter) {
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.getItem() instanceof ICyberPart) {
            return this.filter.test(((ICyberPart) stack.getItem()).getCategory());
        } else return false;
    }

}
