package net.cyber.mod.container.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class FilteredSlot extends Slot {

    private Predicate<Item> filter;
    private ISlotAction action;

    public FilteredSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Predicate<Item> filter) {
        super(inventoryIn, index, xPosition, yPosition);
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return filter.test(stack.getItem());
    }

    public void setAction(ISlotAction action) {
        this.action = action;
    }

    private void doAction() {
        if(this.action != null)
            this.action.doAction(this);
    }

    public static interface ISlotAction{
        void doAction(Slot slot);
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        ItemStack temp = super.onTake(thePlayer, stack);
        this.doAction();
        return temp;
    }

    @Override
    public void putStack(ItemStack stack) {
        super.putStack(stack);
        this.doAction();
    }

}
