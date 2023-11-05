package net.cyber.mod.container;

import net.cyber.mod.helper.CyberAPI;
import net.cyber.mod.helper.CyberCon;
import net.cyber.mod.helper.ICyberInfo;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSurgery extends BEContainer<TileEntitySurgery> {

    public ContainerSurgery(int windowId, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(CyberContainers.ADVQUANTISCOPE_WELD.get(), windowId);
        this.blockEntity = (TileEntitySurgery) playerInventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos());
        if(blockEntity != null) {
            ini(playerInventory, blockEntity);
        }
    }

    public ContainerSurgery(int windowId, PlayerInventory playerInventory, TileEntitySurgery blockEntity) {
        super(CyberContainers.ADVQUANTISCOPE_WELD.get(), windowId);
        this.blockEntity = blockEntity;

        ini(playerInventory, blockEntity);
    }


    public void ini(PlayerInventory playerInventory, TileEntitySurgery surgery){
        this.blockEntity = surgery;

        int indexContainerSlot = 0;
        for (ICyberInfo.EnumSlot slot : ICyberInfo.EnumSlot.values()) {
            for (int indexCyberwareSlot = 0; indexCyberwareSlot < 8; indexCyberwareSlot++) {
                addSlot(new SlotSurgery(surgery.slots, surgery.slotsPlayer, indexContainerSlot, 9 + 20 * indexCyberwareSlot, 109, slot));
                indexContainerSlot++;
            }
            for (int indexCyberwareSlot = 0; indexCyberwareSlot < CyberCon.WARE_PER_SLOT - 8; indexCyberwareSlot++) {
                addSlot(new SlotSurgery(surgery.slots, surgery.slotsPlayer, indexContainerSlot, Integer.MIN_VALUE, Integer.MIN_VALUE, slot));
                indexContainerSlot++;
            }
        }

        for (int indexRow = 0; indexRow < 3; indexRow++) {
            for (int indexColumn = 0; indexColumn < 9; indexColumn++) {
                addSlot(new Slot(playerInventory, indexColumn + indexRow * 9 + 9, 8 + indexColumn * 18, 103 + indexRow * 18 + 37));
            }
        }

        for (int indexColumn = 0; indexColumn < 9; indexColumn++) {
            addSlot(new Slot(playerInventory, indexColumn, 8 + indexColumn * 18, 161 + 37));
        }
    }

    public class SlotSurgery extends SlotItemHandler {
        public final int savedXPosition;
        public final int savedYPosition;
        public final ICyberInfo.EnumSlot slot;
        private final int index;
        private IItemHandler playerItems;

        public SlotSurgery(IItemHandler itemHandler, IItemHandler playerItems, int index, int xPosition, int yPosition, ICyberInfo.EnumSlot slot) {
            super(itemHandler, index, xPosition, yPosition);

            savedXPosition = xPosition;
            savedYPosition = yPosition;
            this.slot = slot;
            this.index = index;
            this.playerItems = playerItems;
        }

        public ItemStack getPlayerStack() {
            return playerItems.getStackInSlot(slotNumber);
        }

        public boolean slotDiscarded() {
            return blockEntity.discardSlots[slotNumber];
        }

        public void setDiscarded(boolean dis) {
            blockEntity.discardSlots[slotNumber] = dis;
            blockEntity.updateEssential(slot);
            blockEntity.updateEssence();
        }

        @Override
        public boolean canTakeStack(PlayerEntity player) {
            return blockEntity.canDisableItem(getStack(), slot, index % CyberCon.WARE_PER_SLOT);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();

            blockEntity.updateEssence();
            blockEntity.markDirty();
        }

        @Override
        public ItemStack onTake(PlayerEntity player, ItemStack stack) {
            super.onTake(player, stack);
            blockEntity.markDirty();
            blockEntity.updateEssential(slot);
            blockEntity.updateEssence();
            return stack;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            ItemStack playerStack = getPlayerStack();
            if (!getPlayerStack().isEmpty()
                    && !blockEntity.canDisableItem(playerStack, slot, index % CyberCon.WARE_PER_SLOT)) {
                return false;
            }
            if (!(stack.isEmpty()
                    && CyberAPI.isCyberware(stack)
                    && CyberAPI.getCyberware(stack).getSlot(stack) == slot)) {
                return false;
            }

            if (CyberAPI.areCyberwareStacksEqual(stack, playerStack)) {
                int stackSize = CyberAPI.getCyberware(stack).installedStackSize(stack);
                if (playerStack.getCount() == stackSize) return false;
            }

            return !doesItemConflict(stack)
                    && areRequirementsFulfilled(stack);
        }

        public boolean doesItemConflict(ItemStack stack) {
            return blockEntity.doesItemConflict(stack, slot, index % CyberCon.WARE_PER_SLOT);
        }

        public boolean areRequirementsFulfilled(ItemStack stack) {
            return blockEntity.areRequirementsFulfilled(stack, slot, index % CyberCon.WARE_PER_SLOT);
        }

        @Override
        public int getItemStackLimit(ItemStack stack) {
            if (stack.isEmpty()
                    || !CyberAPI.isCyberware(stack)) {
                return 1;
            }
            ItemStack playerStack = getPlayerStack();
            int stackSize = CyberAPI.getCyberware(stack).installedStackSize(stack);
            if (CyberAPI.areCyberwareStacksEqual(playerStack, stack)) {
                return stackSize - playerStack.getCount();
            }
            return stackSize;
        }
    }




    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return blockEntity.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (!(slot instanceof SlotSurgery)) {
                if (index >= 3 && index < 30) {
                    if (!mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
