package com.hbm.items.tool;

import api.hbm.block.IOverfusable;
import api.hbm.block.IToolable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemOverfuse extends ItemTooling {
	public ItemOverfuse() {
		super(IToolable.ToolType.SCREWDRIVER, 0);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float fX, float fY, float fZ) {
		Block b = world.getBlock(x, y, z);

		if(b instanceof IOverfusable) {
			if (((IOverfusable) b).onOverfuse(world, player, x, y, z, side, fX, fY, fZ, stack)) {
				return true;
			}
		}
		return super.onItemUse(stack,player,world,x,y,z,side,fX,fY,fZ);
	}
}
