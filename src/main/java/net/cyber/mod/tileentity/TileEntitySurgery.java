package net.cyber.mod.tileentity;

import net.cyber.mod.CyberMod;
import net.cyber.mod.blocks.BlockSurgeryChamber;
import net.cyber.mod.config.CyberConfigs;
import net.cyber.mod.events.CyberwareSurgeryEvent;
import net.cyber.mod.helper.*;
import net.cyber.mod.items.CyberItems;
import net.cyber.mod.items.ItemCyberware;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class TileEntitySurgery extends TileEntity implements ITickableTileEntity
	{
		public ItemStackHandler slotsPlayer = new ItemStackHandler(120);
		public ItemStackHandler slots = new ItemStackHandler(120);
		public boolean[] discardSlots = new boolean[120];
		public boolean[] isEssentialMissing = new boolean[ICyberInfo.EnumSlot.values().length * 2];
		public int essence = 0;
		public int maxEssence = 0;
		public int wrongSlot = -1;
		public int ticksWrong = 0;
		public int lastEntity = -1;
		public int cooldownTicks = 0;
		public boolean missingPower = false;

		public TileEntitySurgery(TileEntityType<?> tileEntityTypeIn) {
			super(tileEntityTypeIn);
		}

		public TileEntitySurgery() {
			this(CyberTileEntitys.SR.get());
		}

		public boolean isUsableByPlayer (PlayerEntity entityPlayer)
		{
			return this.world.getTileEntity(pos) == this
					&& entityPlayer.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
		}

		public void updatePlayerSlots (Entity entityLivingBase, ICyberData cyberwareUserData)
		{
			markDirty();

			if (cyberwareUserData != null) {
				if (entityLivingBase.getEntityId() != lastEntity) {
					for (int indexEssential = 0; indexEssential < discardSlots.length; indexEssential++) {
						discardSlots[indexEssential] = false;
					}
					lastEntity = entityLivingBase.getEntityId();
				}
				maxEssence = cyberwareUserData.getMaxTolerance(entityLivingBase);

				// Update slotsPlayer with the items in the player's body
				for (ICyberInfo.EnumSlot slot : ICyberInfo.EnumSlot.values()) {
					NonNullList<ItemStack> cyberwares = cyberwareUserData.getInstalledCyberware(slot);
					for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
						ItemStack toPut = cyberwares.get(indexSlot).copy();

						// If there's a new item, don't set it to discard by default unless it conflicts
						if (!ItemStack.areItemStacksEqual(toPut, slotsPlayer.getStackInSlot(slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot))) {
							discardSlots[slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot] = doesItemConflict(toPut, slot, indexSlot);
						}
						slotsPlayer.setStackInSlot(slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot, toPut);
					}
					updateEssential(slot);
				}

				// Check for items with requirements that are no longer fulfilled
				boolean needToCheck = true;
				while (needToCheck) {
					needToCheck = false;
					for (ICyberInfo.EnumSlot slot : ICyberInfo.EnumSlot.values()) {
						for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
							int index = slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot;

							ItemStack stack = slots.getStackInSlot(index);
							if (!stack.isEmpty()
									&& !areRequirementsFulfilled(stack, slot, indexSlot)) {
								addItemStack(entityLivingBase, stack);
								slots.setStackInSlot(index, ItemStack.EMPTY);
								needToCheck = true;
							}
						}
					}
				}

				this.updateEssence();
			} else {
				slotsPlayer = new ItemStackHandler(120);
				this.maxEssence = CyberConfigs.COMMON.FoodCubeCost.get();
				for (ICyberInfo.EnumSlot slot : ICyberInfo.EnumSlot.values()) {
					updateEssential(slot);
				}
			}
			wrongSlot = -1;
		}

		public boolean doesItemConflict (@Nonnull ItemStack stack, ICyberInfo.EnumSlot slot, int indexSlotToCheck)
		{
			int row = slot.ordinal();
			if (!stack.isEmpty()) {
				for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
					if (indexSlot != indexSlotToCheck) {
						int index = row * CyberCon.WARE_PER_SLOT + indexSlot;
						ItemStack slotStack = slots.getStackInSlot(index);
						ItemStack playerStack = slotsPlayer.getStackInSlot(index);

						ItemStack otherStack = !slotStack.isEmpty() ? slotStack : (discardSlots[index] ? ItemStack.EMPTY : playerStack);

						// Automatically incompatible with the same item/damage. Doesn't use areCyberwareStacksEqual because items conflict even if different grades.
						if (!otherStack.isEmpty() && (otherStack.getItem() == stack.getItem() && otherStack.getDamage() == stack.getDamage())) {
							setWrongSlot(index);
							return true;
						}

						// Incompatible if either stack doesn't like the other one
						if (!otherStack.isEmpty() && CyberAPI.getCyberware(otherStack).isIncompatible(otherStack, stack)) {
							setWrongSlot(index);
							return true;
						}
						if (!otherStack.isEmpty() && CyberAPI.getCyberware(stack).isIncompatible(stack, otherStack)) {
							setWrongSlot(index);
							return true;
						}
					}
				}
			}

			return false;
		}

		public void setWrongSlot (int index)
		{
			this.wrongSlot = index;
		}

		public void disableDependants (ItemStack stack, ICyberInfo.EnumSlot slot, int indexSlotToCheck)
		{
			int row = slot.ordinal();
			if (!stack.isEmpty()) {
				for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
					if (indexSlot != indexSlotToCheck) {
						int index = row * CyberCon.WARE_PER_SLOT + indexSlot;
						ItemStack playerStack = slotsPlayer.getStackInSlot(index);

						if (!areRequirementsFulfilled(playerStack, slot, indexSlotToCheck)) {
							discardSlots[index] = true;
						}
					}
				}
			}
		}

		public void enableDependsOn (ItemStack stack, ICyberInfo.EnumSlot slot, int indexSlotToCheck)
		{
			if (!stack.isEmpty()) {
				ICyberInfo ware = CyberAPI.getCyberware(stack);
				for (NonNullList<ItemStack> neededItem : ware.required(stack)) {
					boolean found = false;

					outerLoop:
					for (ItemStack needed : neededItem) {
						for (int row = 0; row < ICyberInfo.EnumSlot.values().length; row++) {
							for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
								if (indexSlot != indexSlotToCheck) {
									int index = row * CyberCon.WARE_PER_SLOT + indexSlot;
									ItemStack playerStack = slotsPlayer.getStackInSlot(index);

									if (!playerStack.isEmpty()
											&& playerStack.getItem() == needed.getItem()
											&& playerStack.getDamage() == needed.getDamage()) {
										found = true;
										discardSlots[index] = false;
										break outerLoop;
									}
								}
							}
						}

					}
					if (!found) {
						CyberMod.LOGGER.error("Can't find required" + neededItem + "for" + stack + "in" + slot + ":" + indexSlotToCheck);
					}
				}
			}
		}

		public boolean canDisableItem (ItemStack stack, ICyberInfo.EnumSlot slot, int indexSlotToCheck)
		{
			if (!stack.isEmpty()) {
				for (int row = 0; row < ICyberInfo.EnumSlot.values().length; row++) {
					for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
						if (indexSlot != indexSlotToCheck) {
							int index = row * CyberCon.WARE_PER_SLOT + indexSlot;
							ItemStack slotStack = slots.getStackInSlot(index);
							ItemStack playerStack = ItemStack.EMPTY;

							ItemStack otherStack = !slotStack.isEmpty() ? slotStack : (discardSlots[index] ? ItemStack.EMPTY : playerStack);

							if (!areRequirementsFulfilled(otherStack, slot, indexSlotToCheck)) {
								setWrongSlot(index);
								return false;
							}
						}
					}
				}
			}
			return true;
		}

		public boolean areRequirementsFulfilled (ItemStack stack, ICyberInfo.EnumSlot slot, int indexSlotToCheck)
		{
			if (!stack.isEmpty()) {
				ICyberInfo ware = CyberAPI.getCyberware(stack);
				for (NonNullList<ItemStack> neededItem : ware.required(stack)) {
					boolean found = false;

					outerLoop:
					for (ItemStack needed : neededItem) {
						for (int row = 0; row < ICyberInfo.EnumSlot.values().length; row++) {
							for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
								if (indexSlot != indexSlotToCheck) {
									int index = row * CyberCon.WARE_PER_SLOT + indexSlot;
									ItemStack slotStack = slots.getStackInSlot(index);
									ItemStack playerStack = slotsPlayer.getStackInSlot(index);

									ItemStack otherStack = !slotStack.isEmpty() ? slotStack : (discardSlots[index] ? ItemStack.EMPTY : playerStack);

									if (!otherStack.isEmpty() && otherStack.getItem() == needed.getItem() && otherStack.getDamage() == needed.getDamage()) {
										found = true;
										break outerLoop;
									}
								}
							}
						}
					}
					if (!found) return false;
				}
			}

			return true;
		}

		@Override
		public void read(BlockState state,CompoundNBT tagCompound)
		{
			super.read(state,tagCompound);

			slots.deserializeNBT(tagCompound.getCompound("inv"));

			this.essence = tagCompound.getInt("essence");
			this.maxEssence = tagCompound.getInt("maxEssence");
			this.lastEntity = tagCompound.getInt("lastEntity");
			this.missingPower = tagCompound.getBoolean("missingPower");
		}

		@Nonnull
		@Override
		public CompoundNBT getUpdateTag ()
		{
			return write(new CompoundNBT());
		}

		@Nonnull
		@Override
		public CompoundNBT write(CompoundNBT tagCompound)
		{
			tagCompound = super.write(tagCompound);

			tagCompound.putInt("essence", essence);
			tagCompound.putInt("maxEssence", maxEssence);
			tagCompound.putInt("lastEntity", lastEntity);
			tagCompound.putBoolean("missingPower", missingPower);

			tagCompound.put("inv", this.slots.serializeNBT());
			tagCompound.put("inv2", this.slotsPlayer.serializeNBT());

			return tagCompound;
		}

		public void updateEssential (ICyberInfo.EnumSlot slot)
		{
			if (slot.hasEssential()) {
				byte answer = isEssential(slot);
				boolean foundFirst = (answer & 1) > 0;
				boolean foundSecond = (answer & 2) > 0;
				this.isEssentialMissing[slot.ordinal() * 2] = !foundFirst;
				this.isEssentialMissing[slot.ordinal() * 2 + 1] = !foundSecond;
			} else {
				this.isEssentialMissing[slot.ordinal() * 2] = false;
				this.isEssentialMissing[slot.ordinal() * 2 + 1] = false;
			}
		}

		private byte isEssential (ICyberInfo.EnumSlot slot)
		{
			byte r = 0;

			for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
				int index = slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot;
				ItemStack slotStack = slots.getStackInSlot(index);
				ItemStack playerStack = slotsPlayer.getStackInSlot(index);

				ItemStack stack = !slotStack.isEmpty() ? slotStack : (discardSlots[index] ? ItemStack.EMPTY : playerStack);

				if (!stack.isEmpty()) {
					ICyberInfo ware = CyberAPI.getCyberware(stack);
					if (ware.isEssential(stack)) {
						if (slot.isSided() && ware instanceof ICyberInfo.ISidedLimb) {
							if (((ICyberInfo.ISidedLimb) ware).getSide(stack) == ICyberInfo.ISidedLimb.EnumSide.LEFT && (r & 1) == 0) {
								r += 1;
							} else if ((r & 2) == 0) {
								r += 2;
							}
						} else {
							return 3;
						}
					}

				}
			}
			return r;
		}

		@Override
		public void tick()
		{
			if (inProgress && progressTicks < 80) {
				ICyberData cyberwareUserData = CyberAPI.getCapabilityOrNull(targetEntity);
				if (targetEntity != null
						&& targetEntity.isAlive()
						&& cyberwareUserData != null) {
					BlockPos pos = getPos();

					if (progressTicks > 20 && progressTicks < 60) {
						targetEntity.prevPosX = pos.getX() + .5F;
						targetEntity.prevPosZ = pos.getZ() + .5F;
					}

					if (progressTicks >= 20 && progressTicks <= 60 && progressTicks % 5 == 0) {
						targetEntity.attackEntityFrom(EssentialsMissingHandler.surgery, 2F);
					}

					if (progressTicks == 60) {
						processUpdate(cyberwareUserData);
					}

					progressTicks++;

				} else {
					inProgress = false;
					progressTicks = 0;
					targetEntity = null;

					BlockState state = world.getBlockState(getPos().down());
					if (state.getBlock() instanceof BlockSurgeryChamber) {
						((BlockSurgeryChamber) state.getBlock()).toggleDoor(state, getPos().down(), world);
					}
				}
			} else if (inProgress) {
				inProgress = false;
				progressTicks = 0;
				targetEntity = null;
				cooldownTicks = 60;

				BlockState state = world.getBlockState(getPos().down());
				if (state.getBlock() instanceof BlockSurgeryChamber) {
					((BlockSurgeryChamber) state.getBlock()).toggleDoor(state, getPos().down(), world);
				}
			}

			if (cooldownTicks > 0) {
				cooldownTicks--;
			}
		}

		public void processUpdate (ICyberData cyberwareUserData)
		{
			updatePlayerSlots(targetEntity, cyberwareUserData);

			for (int indexCyberSlot = 0; indexCyberSlot < ICyberInfo.EnumSlot.values().length; indexCyberSlot++) {
				ICyberInfo.EnumSlot slot = ICyberInfo.EnumSlot.values()[indexCyberSlot];
				NonNullList<ItemStack> nnlToInstall = NonNullList.create();
				for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
					nnlToInstall.add(ItemStack.EMPTY);
				}

				int indexToInstall = 0;
				for (int indexCyberware = indexCyberSlot * CyberCon.WARE_PER_SLOT; indexCyberware < (indexCyberSlot + 1) * CyberCon.WARE_PER_SLOT; indexCyberware++) {
					ItemStack itemStackSurgery = slots.getStackInSlot(indexCyberware);

					ItemStack itemStackPlayer = slotsPlayer.getStackInSlot(indexCyberware).copy();
					if (!itemStackSurgery.isEmpty()) {
						ItemStack itemStackToSet = itemStackSurgery.copy();
						if (CyberAPI.areCyberwareStacksEqual(itemStackToSet, itemStackPlayer)) {
							int maxSize = CyberAPI.getCyberware(itemStackToSet).installedStackSize(itemStackToSet);

							if (itemStackToSet.getCount() < maxSize) {
								int numToShift = Math.min(maxSize - itemStackToSet.getCount(), itemStackPlayer.getCount());
								itemStackPlayer.shrink(numToShift);
								itemStackToSet.grow(numToShift);
							}
						}

						if (!itemStackPlayer.isEmpty()) {
							itemStackPlayer = CyberAPI.sanitize(itemStackPlayer);

							addItemStack(targetEntity, itemStackPlayer);
						}

						nnlToInstall.set(indexToInstall, itemStackToSet);
						indexToInstall++;
					} else if (!itemStackPlayer.isEmpty()) {
						if (discardSlots[indexCyberware]) {
							itemStackPlayer = CyberAPI.sanitize(itemStackPlayer);

							addItemStack(targetEntity, itemStackPlayer);
						} else {
							nnlToInstall.set(indexToInstall, slotsPlayer.getStackInSlot(indexCyberware).copy());
							indexToInstall++;
						}
					}
				}
				if (!world.isRemote) {
					cyberwareUserData.setInstalledCyberware(targetEntity, slot, nnlToInstall);
				}
				cyberwareUserData.setHasEssential(slot, !isEssentialMissing[indexCyberSlot * 2], !isEssentialMissing[indexCyberSlot * 2 + 1]);
			}
			cyberwareUserData.setTolerance(targetEntity, essence);
			cyberwareUserData.updateCapacity();
			cyberwareUserData.setImmune();
			if (!world.isRemote) {
				CyberAPI.updateData(targetEntity);
			}
			slots = new ItemStackHandler(120);

			CyberwareSurgeryEvent.Post postSurgeryEvent = new CyberwareSurgeryEvent.Post(targetEntity);
			MinecraftForge.EVENT_BUS.post(postSurgeryEvent);
		}

		private void addItemStack (Entity entityLivingBase, ItemStack stack)
		{
			boolean flag = true;

			if (entityLivingBase instanceof PlayerEntity) {
				PlayerEntity entityPlayer = ((PlayerEntity) entityLivingBase);
				flag = !entityPlayer.inventory.addItemStackToInventory(stack);
			}

			if (flag && !world.isRemote) {
				ItemEntity item = new ItemEntity(world, getPos().getX() + .5F, getPos().getY() - 2F, getPos().getZ() + .5F, stack);
				world.addEntity(item);
			}
		}

		public void notifyChange() {
			if(!world.isRemote()) {
				boolean opened = world.getBlockState(getPos().down()).get(BlockSurgeryChamber.OPEN);

				if (!opened) {
					BlockPos p = getPos();
					List<Entity> entityLivingBases = world.getEntitiesWithinAABB(Entity.class,
							new AxisAlignedBB(p.getX(), p.getY() - 2F, p.getZ(),
									p.getX() + 1F, p.getY(), p.getZ() + 1F));
					if (entityLivingBases.size() == 1) {
						Entity entityLivingBase = entityLivingBases.get(0);
						CyberwareSurgeryEvent.Pre preSurgeryEvent = new CyberwareSurgeryEvent.Pre(entityLivingBase, slotsPlayer, slots);

						if (!MinecraftForge.EVENT_BUS.post(preSurgeryEvent)) {
							this.inProgress = true;
							this.progressTicks = 0;
							this.targetEntity = entityLivingBase;
						} else {
							BlockState state = world.getBlockState(getPos().down());
							if (state.getBlock() instanceof BlockSurgeryChamber) {
								((BlockSurgeryChamber) state.getBlock()).toggleDoor(state, getPos().down(), world);
							}
						}
					}
				}
			}
		}

		public boolean inProgress = false;
		public Entity targetEntity = null;
		public int progressTicks = 0;
		public static boolean workingOnPlayer = false;
		public static int playerProgressTicks = 0;

		public void updateEssence () {
			this.essence = this.maxEssence;
			boolean hasConsume = false;
			boolean hasProduce = false;

			for (ICyberInfo.EnumSlot slot : ICyberInfo.EnumSlot.values()) {
				for (int indexSlot = 0; indexSlot < CyberCon.WARE_PER_SLOT; indexSlot++) {
					int index = slot.ordinal() * CyberCon.WARE_PER_SLOT + indexSlot;
					ItemStack slotStack = slots.getStackInSlot(index);
					ItemStack playerStack = slotsPlayer.getStackInSlot(index);

					ItemStack stack = !slotStack.isEmpty() ? slotStack : (discardSlots[index] ? ItemStack.EMPTY : playerStack);

					if (!stack.isEmpty()) {
						ItemStack ret = stack.copy();
						if (!slotStack.isEmpty()
								&& !ret.isEmpty()
								&& !playerStack.isEmpty()
								&& CyberAPI.areCyberwareStacksEqual(playerStack, ret)) {
							int maxSize = CyberAPI.getCyberware(ret).installedStackSize(ret);

							if (ret.getCount() < maxSize) {
								int numToShift = Math.min(maxSize - ret.getCount(), playerStack.getCount());
								ret.grow(numToShift);
							}
						}
						ICyberInfo ware = CyberAPI.getCyberware(ret);

						this.essence -= ware.getEssenceCost(ret);

						if (ware instanceof ItemCyberware && ((ItemCyberware) ware).getPowerConsumption(ret) > 0) {
							hasConsume = true;
						}
						if (ware instanceof ItemCyberware && (((ItemCyberware) ware).getPowerProduction(ret) > 0 || ware == CyberItems.FOODCUBE.get())) {
							hasProduce = true;
						}
					}
				}
			}

			this.missingPower = hasConsume && !hasProduce;
		}


}
