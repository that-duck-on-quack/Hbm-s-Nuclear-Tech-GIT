package com.hbm.inventory.gui;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.container.ContainerDiFurnaceElectric;
import com.hbm.inventory.container.ContainerElectricFurnace;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.TileEntityDiFurnaceElectric;
import com.hbm.tileentity.machine.TileEntityMachineElectricFurnace;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * {@link TileEntityDiFurnaceElectric}
 * {@link ContainerDiFurnaceElectric}
 * @author Jack Andersn
 */
public class GUIDiFurnaceElectric extends GuiInfoContainer {

	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/gui_difurnace_electric.png");
	private TileEntityDiFurnaceElectric diFurnace;

	public GUIDiFurnaceElectric(InventoryPlayer invPlayer, TileEntityDiFurnaceElectric tedf) {
		super(new ContainerDiFurnaceElectric(invPlayer, tedf));
		diFurnace = tedf;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);

		this.drawElectricityInfo(this, mouseX, mouseY, guiLeft + 20, guiTop + 5, 16, 52, diFurnace.power, diFurnace.maxPower);

		String[] upgradeText = new String[3];
		upgradeText[0] = I18nUtil.resolveKey("desc.gui.upgrade");
		upgradeText[1] = I18nUtil.resolveKey("desc.gui.upgrade.speed");
		upgradeText[2] = I18nUtil.resolveKey("desc.gui.upgrade.power");
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 151, guiTop + 19, 8, 8, mouseX, mouseY, upgradeText);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String name = this.diFurnace.hasCustomInventoryName() ? this.diFurnace.getInventoryName() : I18n.format(this.diFurnace.getInventoryName());
//this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2
		this.fontRendererObj.drawString(name, 39, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 39, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		//failsafe TE clone
		//if initial TE invalidates, new TE is fetched
		//if initial ZE is still present, it'll be used instead
		//works so that container packets can still be used
		//efficiency!
		if(diFurnace.isInvalid() && diFurnace.getWorldObj().getTileEntity(diFurnace.xCoord, diFurnace.yCoord, diFurnace.zCoord) instanceof TileEntityDiFurnaceElectric)
			diFurnace = (TileEntityDiFurnaceElectric) diFurnace.getWorldObj().getTileEntity(diFurnace.xCoord, diFurnace.yCoord, diFurnace.zCoord);

		if(diFurnace.power > 0) {
			int i = (int)diFurnace.getPowerScaled(52);
			drawTexturedModalRect(guiLeft + 20, guiTop + 57 - i, 200, 52 - i, 16, i);
		}

		if(diFurnace.getWorldObj().getBlock(diFurnace.xCoord, diFurnace.yCoord, diFurnace.zCoord) == ModBlocks.machine_difurnace_electric_on) {
			drawTexturedModalRect(guiLeft + 56, guiTop + 35, 176, 0, 16, 16);
		}

		int j1 = diFurnace.getProgressScaled(24);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 17, j1, 17);

		this.drawInfoPanel(guiLeft + 151, guiTop + 19, 8, 8, 8);
	}

}
