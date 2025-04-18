package com.hbm.items;

import com.hbm.util.ItemStackUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.Random;

/**
 * Base class for items containing an inventory. This can be seen in crates, containment boxes, and the toolbox.
 * @author BallOfEnergy/Gammawave
 */
public abstract class ItemInventory implements IInventory {

	public EntityPlayer player;
	public ItemStack[] slots;
	public ItemStack original;

	public boolean toMarkDirty = false;

	public ItemInventory(EntityPlayer player, ItemStack target) {
		this.player = player;
		this.original = target;
		slots = new ItemStack[this.getSizeInventory()];
		if(target.stackTagCompound == null)
			target.stackTagCompound = new NBTTagCompound();

		ItemStack[] fromNBT = ItemStackUtil.readStacksFromNBT(target, slots.length);

		if(fromNBT != null) {
			System.arraycopy(fromNBT, 0, slots, 0, slots.length);
		}
		toMarkDirty = true;
		this.markDirty();
		toMarkDirty = false;
	}

	@Override
	public void markDirty() {
		if(player.getEntityWorld().isRemote || !toMarkDirty || player.inventory.currentItem != player.getEntityData().getInteger("crateslot")) { // go the fuck away
			return;
		}

		NBTTagCompound nbt = (NBTTagCompound) original.stackTagCompound.copy();

		int invSize = this.getSizeInventory();

		// Remove slots that are now empty or overwrite them with the correct item data.
		for(int i = 0; i < invSize; i++) {
			ItemStack stack = this.getStackInSlot(i);
			if(stack == null) {
				nbt.removeTag("slot" + i);
			} else {
				NBTTagCompound slot = new NBTTagCompound();
				stack.writeToNBT(slot);
				nbt.setTag("slot" + i, slot);
			}
		}
		ItemStack target = original.copy();
		target.setTagCompound(checkNBT(nbt));
		int k = -1;
		// Find the original ItemStack in case it moved and only save to it if size equals 1.
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack s = player.inventory.getStackInSlot(i);
			if(s != null) {
				if(s.isItemEqual(original) && s.stackSize == 1){
					k=i;
					break;
				}
			}
		}
		if(k != -1) {
			player.inventory.setInventorySlotContents(k, target);
		}

	}

	public NBTTagCompound checkNBT(NBTTagCompound nbt) {
		if(!nbt.hasNoTags()) {
			Random random = new Random();

			try {
				byte[] abyte = CompressedStreamTools.compress(nbt);

				if (abyte.length > 6000) {
					player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Warning: Container NBT exceeds 6kB, contents will be ejected!"));
					for (int i1 = 0; i1 < this.getSizeInventory(); ++i1) {
						ItemStack itemstack = this.getStackInSlot(i1);

						if (itemstack != null) {
							float f = random.nextFloat() * 0.8F + 0.1F;
							float f1 = random.nextFloat() * 0.8F + 0.1F;
							float f2 = random.nextFloat() * 0.8F + 0.1F;

							while (itemstack.stackSize > 0) {
								int j1 = random.nextInt(21) + 10;

								if (j1 > itemstack.stackSize) {
									j1 = itemstack.stackSize;
								}

								itemstack.stackSize -= j1;
								EntityItem entityitem = new EntityItem(player.worldObj, player.posX + f, player.posY + f1, player.posZ + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

								if (itemstack.hasTagCompound()) {
									entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
								}

								float f3 = 0.05F;
								entityitem.motionX = (float) random.nextGaussian() * f3 + player.motionX;
								entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F + player.motionY;
								entityitem.motionZ = (float) random.nextGaussian() * f3 + player.motionZ;
								player.worldObj.spawnEntityInWorld(entityitem);
							}
						}
					}

					return new NBTTagCompound(); // Reset.
				}
			} catch (IOException ignored) {}
		}
		return nbt;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
			} else {
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(stack != null) {
			stack.stackSize = Math.min(stack.stackSize, this.getInventoryStackLimit());
		}

		slots[slot] = stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory() {
		player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "hbm:block.crateOpen", 1.0F, 0.8F);
	}

	@Override
	public void closeInventory() {
		toMarkDirty = true;
		markDirty();
		toMarkDirty = false;
		player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "hbm:block.crateClose", 1.0F, 0.8F);
	}
}
