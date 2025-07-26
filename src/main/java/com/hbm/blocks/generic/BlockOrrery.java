package com.hbm.blocks.generic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockOrrery extends BlockContainer {

	public BlockOrrery(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityOrrery();
	}

	@Override public int getRenderType() { return -1; }
	@Override public boolean isOpaqueCube() { return false; }
	@Override public boolean renderAsNormalBlock() { return false; }

	public class TileEntityOrrery extends TileEntity {

		AxisAlignedBB bb = null;

		@Override
		public AxisAlignedBB getRenderBoundingBox() {
			if(bb == null) {
				bb = AxisAlignedBB.getBoundingBox(
					xCoord - 49,
					yCoord - 19,
					zCoord - 49,
					xCoord + 50,
					yCoord + 20,
					zCoord + 50
				);
			}

			return bb;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public double getMaxRenderDistanceSquared() {
			return 65536.0D;
		}

	}

}
