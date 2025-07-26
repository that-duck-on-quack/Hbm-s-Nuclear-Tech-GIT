package com.hbm.inventory.gui;

import com.hbm.inventory.container.ContainerRBMKOutgasser;
import com.hbm.inventory.container.ContainerRBMKSmelter;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKOutgasser;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKSmelter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * GUI for {@link TileEntityRBMKSmelter}, Container {@link ContainerRBMKSmelter}
 * @author Jack Andersen
 */
public class GUIRBMKSmelter extends GuiInfoContainer {

	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/reactors/gui_rbmk_smelter.png");
	private TileEntityRBMKSmelter rod;

	public GUIRBMKSmelter(InventoryPlayer invPlayer, TileEntityRBMKSmelter tedf) {
		super(new ContainerRBMKSmelter(invPlayer, tedf));
		rod = tedf;

		this.xSize = 176;
		this.ySize = 186;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String name = this.rod.hasCustomInventoryName() ? this.rod.getInventoryName() : I18n.format(this.rod.getInventoryName());

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int progress = (int) (13*rod.progress / rod.progressRequired);
		drawTexturedModalRect(guiLeft + 82, guiTop + 50, 176, 0, progress, 6);
	}
}
