package com.hbm.blocks.bomb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.entity.mob.EntityTaintCrab;
import com.hbm.entity.mob.EntityCreeperTainted;
import com.hbm.entity.mob.EntityTeslaCrab;
import com.hbm.potion.HbmPotion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTaint extends Block implements ITooltipProvider {

	public BlockTaint(Material mat) {
		super(mat);
		this.setTickRandomly(true);
	}

	@Override public MapColor getMapColor(int meta) { return MapColor.grayColor; }
	@Override public Item getItemDropped(int i, Random rand, int j) { return null; }

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {

		int meta = world.getBlockMetadata(x, y, z);
		if(meta >= 15) return;
		return;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.75, z + 1);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		return;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ext) {
		list.add("DO NOT TOUCH, BREATHE OR STARE AT.");
	}
}
