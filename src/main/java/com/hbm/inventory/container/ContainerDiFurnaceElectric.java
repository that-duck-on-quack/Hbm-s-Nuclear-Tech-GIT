package com.hbm.inventory.container;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotSmelting;
import com.hbm.inventory.SlotUpgrade;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.tileentity.machine.TileEntityDiFurnaceElectric;
import com.hbm.tileentity.machine.TileEntityMachineElectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * {@link com.hbm.tileentity.machine.TileEntityDiFurnaceElectric}
 * {@link com.hbm.inventory.gui.GUIDiFurnaceElectric}
 * @author Jack Andersen
 */
public class ContainerDiFurnaceElectric extends Container {

	private TileEntityDiFurnaceElectric diFurnace;

	public ContainerDiFurnaceElectric(InventoryPlayer invPlayer, TileEntityDiFurnaceElectric tedf) {

		diFurnace = tedf;

		this.addSlotToContainer(new Slot(tedf, 0, 20, 64));
		this.addSlotToContainer(new Slot(tedf, 1, 56, 53));
		this.addSlotToContainer(new Slot(tedf, 2, 56, 17));
		this.addSlotToContainer(new SlotCraftingOutput(invPlayer.player, tedf, 3, 116, 35));
		//Upgrades
		this.addSlotToContainer(new SlotUpgrade(tedf, 4, 147, 34));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack rStack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			rStack = stack.copy();

			if(index <= 3) {
				if(!this.mergeItemStack(stack, 4, this.inventorySlots.size(), true)) {
					return null;
				}

				slot.onSlotChange(stack, rStack);
			} else {

				if(rStack.getItem() instanceof IBatteryItem || rStack.getItem() == ModItems.battery_creative) {
					if(!this.mergeItemStack(stack, 0, 1, false))
						return null;

				} else if(rStack.getItem() instanceof ItemMachineUpgrade) {
					if(!this.mergeItemStack(stack, 4, 5, false))
						return null;

				} else if(!this.mergeItemStack(stack, 1, 3, false))
					return null;
			}

			if(stack.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}

		return rStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return diFurnace.isUseableByPlayer(player);
	}
}
