package com.hbm.render.tileentity;

import org.lwjgl.opengl.GL11;

import com.hbm.blocks.ModBlocks;
import com.hbm.dim.SolarSystem;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.OrreryPronter;
import com.hbm.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class RenderOrrery extends TileEntitySpecialRenderer implements IItemRendererProvider {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float interp) {
		GL11.glPushMatrix();
		{

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_CULL_FACE);

			GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
			OrreryPronter.render(Minecraft.getMinecraft(), tile.getWorldObj(), interp);

		}
		GL11.glPopMatrix();
	}

	@Override
	public Item getItemForRenderer() {
		return Item.getItemFromBlock(ModBlocks.orrery);
	}

	@Override
	public IItemRenderer getRenderer() {
		return new ItemRenderBase() {
			public void renderInventory() {
				GL11.glTranslated(0, 3, 0);
				GL11.glScaled(6, 6, 6);
			}
			public void renderCommon() {
				Minecraft.getMinecraft().renderEngine.bindTexture(SolarSystem.kerbol.texture);
				Tessellator.instance.disableColor();
				RenderUtil.renderBlock(Tessellator.instance, 0.375, 0.625);
			}
		};
	}

}
