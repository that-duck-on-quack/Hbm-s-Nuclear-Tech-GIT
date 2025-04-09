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

		if(meta >= 8)
			return new TileEntityProxyCombo(false, false, true);

		return null;
	}

	@Override
	public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		x = x + dir.offsetX * o;
		z = z + dir.offsetZ * o;

		for(int i = 2; i <= 6; i++) {
			ForgeDirection dr2 = ForgeDirection.getOrientation(i);
			this.makeExtra(world, x + dr2.offsetX * 2, y, z + dr2.offsetZ * 2);
		}
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
