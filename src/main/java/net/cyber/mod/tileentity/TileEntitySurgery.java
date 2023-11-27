package net.cyber.mod.tileentity;

import net.cyber.mod.helper.ICyberPart;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntitySurgery extends TileEntity implements ITickableTileEntity, IItemHandlerModifiable {

	private ItemStackHandler handler = new ItemStackHandler(24);

	public TileEntitySurgery(TileEntityType<?> tileEntityTypeIn) {
			super(tileEntityTypeIn);
		}

	public TileEntitySurgery() {
			this(CyberTileEntitys.SR.get());
		}

	@Override
	public void tick() {

	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.handler.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return this.handler.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.handler.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return this.handler.insertItem(slot, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return this.handler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.handler.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() instanceof ICyberPart;
	}
}
