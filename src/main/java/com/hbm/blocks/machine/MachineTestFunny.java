package com.hbm.blocks.machine;

import java.util.ArrayList;
import java.util.List;

import com.hbm.blocks.ILookOverlay;
import com.hbm.tileentity.machine.TileEntityTestFunny;
import com.hbm.util.I18nUtil;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;

// This is an example block/machine.
public class MachineTestFunny extends BlockContainer implements ILookOverlay {

	public MachineTestFunny(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTestFunny();
	}

	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {

		TileEntity te = world.getTileEntity(x, y, z);

		if(!(te instanceof TileEntityTestFunny))
			return;

		TileEntityTestFunny testfunny = (TileEntityTestFunny) te;

		List<String> text = new ArrayList<>();
		text.add("haiiiiiiiiiiiiiiiiiiiiii :DDDDD");

		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}
}
