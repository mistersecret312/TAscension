package net.cyber.mod.container.slots;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.container.ContainerSurgery;
import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.CyberPartType;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class UpgradeSlot extends SlotItemHandler {
    Predicate<CyberPartEnum> filter;
    Predicate<CyberPartType> filtertype;
    ContainerSurgery surgery;

    public UpgradeSlot(ContainerSurgery surgery, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(surgery, stack -> true, itemHandler, index, xPosition, yPosition);
    }

    public UpgradeSlot(ContainerSurgery container, Predicate<CyberPartEnum> filter, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);;
        this.filter = filter;
        this.surgery = container;
        this.filtertype = type -> type.equals(CyberPartType.UPGRADE);
    }

    public UpgradeSlot(ContainerSurgery container, Predicate<CyberPartType> filtertype, Predicate<CyberPartEnum> filter, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);;
        this.filter = filter;
        this.surgery = container;
        this.filtertype = filtertype;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        surgery.addToRemoved(stack);
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        surgery.addToAdded(stack);
        super.putStack(stack);
    }

    public void setFilter(Predicate<CyberPartEnum> filter) {
        this.filter = filter;
    }
    public void setFilterType(Predicate<CyberPartType> filterType){this.filtertype = filterType;}

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.getItem() instanceof ICyberPart) {
            return this.filter.test(((ICyberPart) stack.getItem()).getCategory()) && this.filtertype.test(((ICyberPart) stack.getItem()).getType());
        } else return false;
    }

}
