package com.hbm.items.tool;

import com.hbm.inventory.container.ContainerLeadBox;
import com.hbm.inventory.gui.GUILeadBox;
import com.hbm.items.ItemInventory;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.util.ItemStackUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemLeadBox extends Item implements IGUIProvider {

	public ItemLeadBox() {
		this.setMaxStackSize(1);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if(!world.isRemote) player.openGui(MainRegistry.instance, 0, world, 0, 0, 0);
		return stack;
	}

	@Override
	public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerLeadBox(player.inventory, new InventoryLeadBox(player, player.getHeldItem()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GUILeadBox(player.inventory, new InventoryLeadBox(player, player.getHeldItem()));
	}

	public static class InventoryLeadBox extends ItemInventory {

		public InventoryLeadBox(EntityPlayer player, ItemStack box) {
			super(player,box);
		}

		@Override
		public int getSizeInventory() {
			return 20;
		}

		@Override
		public String getInventoryName() {
			return "container.leadBox";
		}

		@Override
		public boolean hasCustomInventoryName() {
			return original.hasDisplayName();
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}
	}
}
