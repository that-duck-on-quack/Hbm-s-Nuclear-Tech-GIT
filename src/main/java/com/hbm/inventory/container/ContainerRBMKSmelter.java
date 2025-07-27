package com.hbm.inventory.container;

import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKOutgasser;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKSmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * {@link ContainerRBMKSmelter}
 * @author Jack Andersen
 */
public class ContainerRBMKSmelter extends Container {

	private TileEntityRBMKSmelter rbmk;

	public ContainerRBMKSmelter(InventoryPlayer invPlayer, TileEntityRBMKSmelter tedf) {
		rbmk = tedf;

		this.addSlotToContainer(new Slot(tedf, 0, 48, 45));
		this.addSlotToContainer(new SlotCraftingOutput(invPlayer.player, tedf, 1, 112, 45));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 20));
			}
		}

		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142 + 20));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int index) {
		ItemStack var3 = null;
		Slot var4 = (Slot) this.inventorySlots.get(index);

		if(var4 != null && var4.getHasStack()) {
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if(index == 0 || index == 1) {
				if(!this.mergeItemStack(var5, rbmk.getSizeInventory(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else if(!this.mergeItemStack(var5, 0, 1, false)) {
				return null;
			}

			if(var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			} else {
				var4.onSlotChanged();
			}
		}

		return var3;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
}
