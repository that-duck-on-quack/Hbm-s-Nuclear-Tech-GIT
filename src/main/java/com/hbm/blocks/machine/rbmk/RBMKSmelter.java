package com.hbm.blocks.machine.rbmk;

import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKOutgasser;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKSmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * {@link com.hbm.tileentity.machine.rbmk.TileEntityRBMKSmelter}
 * @author Jack Andersen
 */
public class RBMKSmelter extends RBMKBase {

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {

		if(meta >= this.offset)
			return new TileEntityRBMKSmelter();

		if(hasExtra(meta))
			return new TileEntityProxyCombo(true, false, false);

		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return openInv(world, x, y, z, player);
	}

	@Override
	public int getRenderType(){
		return this.renderIDPassive;
	}
}
