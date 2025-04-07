package com.hbm.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.tileentity.machine.TileEntityDeaerator;
import com.hbm.util.I18nUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;

import java.util.ArrayList;
import java.util.List;

public class MachineDeaerator extends BlockContainer implements ILookOverlay {

	public MachineDeaerator(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDeaerator();
	}

	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {

		TileEntity te = world.getTileEntity(x, y, z);

		if(!(te instanceof TileEntityDeaerator))
			return;
		TileEntityDeaerator deaerator = (TileEntityDeaerator) te;

		List<String> text = new ArrayList<>();

		for(int i = 0; i < deaerator.tanks.length; i++)
			text.add((i < 1 || i == 2 ? (EnumChatFormatting.GREEN + "-> ") : (EnumChatFormatting.RED + "<- ")) + EnumChatFormatting.RESET +deaerator.tanks[i].getTankType().getLocalizedName() + ": " + deaerator.tanks[i].getFill() + "/" + deaerator.tanks[i].getMaxFill() + "mB");

		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}
}
