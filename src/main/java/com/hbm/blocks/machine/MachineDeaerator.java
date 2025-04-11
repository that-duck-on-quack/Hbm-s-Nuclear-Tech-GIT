package com.hbm.blocks.machine;

import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityDeaerator;
import com.hbm.util.I18nUtil;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class MachineDeaerator extends BlockDummyable implements ILookOverlay {

	public MachineDeaerator(Material mat) {
		super(mat);
	}

	@Override
	public int[] getDimensions() {
		return new int[] {2, 0, 1, 0, 0, 3};
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12)
			return new TileEntityDeaerator();
		if(hasExtra(meta))
			return new TileEntityProxyCombo().fluid();

		return null;
	}

	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

		this.makeExtra(world, x - rot.offsetX * 3, y, z - rot.offsetZ * 3);
		this.makeExtra(world, x - rot.offsetX * 3, y + 1, z - rot.offsetZ * 3);
		this.makeExtra(world, x - dir.offsetX - rot.offsetX * 3, y, z - dir.offsetZ - rot.offsetZ * 3);
		this.makeExtra(world, x - dir.offsetX - rot.offsetX * 3, y + 1, z - dir.offsetZ - rot.offsetZ * 3);
		//DOESN'T FUCKING WORK
	}

	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {
		int[] pos = this.findCore(world, x, y, z);

		if(pos == null)
			return;

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
