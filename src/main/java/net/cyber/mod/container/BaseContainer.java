package net.cyber.mod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BaseContainer extends Container {

    protected BaseContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    @Override
    public Slot addSlot(Slot slotIn) {
        return super.addSlot(slotIn);
    }

    /** Shift click logic*/
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);
        if ((slot != null) && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            itemstack = slotStack.copy();

            final int containerSlots = inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(slotStack, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, slotStack);
        }
        return itemstack;
    }

    /** Needs to be true so screen doesn't instantly open then close*/
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }


}
