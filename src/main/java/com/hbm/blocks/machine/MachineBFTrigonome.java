package com.hbm.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.tileentity.machine.TileEntityBFTrigonome;
import com.hbm.util.I18nUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;

import java.util.ArrayList;
import java.util.List;

// This is an example block/machine.
public class MachineBFTrigonome extends BlockContainer implements ILookOverlay {

	public MachineBFTrigonome(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBFTrigonome();
	}

	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {

		TileEntity te = world.getTileEntity(x, y, z);

		if(!(te instanceof TileEntityBFTrigonome))
			return;

		TileEntityBFTrigonome bf = (TileEntityBFTrigonome) te;

		List<String> text = new ArrayList<>();
		text.add("haiiiiiiiiiiiiiiiiiiiiii :DDDDD");

		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}
}
