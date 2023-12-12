package net.cyber.mod.container.slots;

import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.container.ContainerSurgery;
import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.ICyberPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class UpgradeSlot extends SlotItemHandler {
    Predicate<CyberPartEnum> filter;
    ContainerSurgery surgery;

    public UpgradeSlot(ContainerSurgery surgery, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(surgery, stack -> true, itemHandler, index, xPosition, yPosition);
    }

    public UpgradeSlot(ContainerSurgery container, Predicate<CyberPartEnum> filter, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);;
        this.filter = filter;
        this.surgery = container;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        thePlayer.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(cap -> {
            cap.handleRemoved(stack);
        });
        return super.onTake(thePlayer, stack);
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
