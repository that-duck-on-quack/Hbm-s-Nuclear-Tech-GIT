package com.hbm.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderDeaerator extends TileEntitySpecialRenderer implements IItemRendererProvider {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		// Initial config:
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y, z + 0.5D); // Translate the model for alignment.
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glRotatef(0, 0, 1, 0);

		switch(tile.getBlockMetadata() - BlockDummyable.offset) { //Configure model for rotations.
			case 4: GL11.glRotatef(180, 0F, 1F, 0F); break;
			case 3: GL11.glRotatef(270, 0F, 1F, 0F); break;
			case 5: GL11.glRotatef(0, 0F, 1F, 0F); break;
			case 2: GL11.glRotatef(90, 0F, 1F, 0F); break;
		}

		GL11.glShadeModel(GL11.GL_SMOOTH);
		bindTexture(ResourceManager.dahaf_tex); //Set texture here.
		ResourceManager.dahaf.renderAll(); //Initiate render here.
		GL11.glShadeModel(GL11.GL_FLAT);

		GL11.glPopMatrix();
	}

	//Get stuff for item render.
	@Override
	public Item getItemForRenderer() {
		return Item.getItemFromBlock(ModBlocks.deaerator);
	}

	//Inventorium renderer.
	@Override
	public IItemRenderer getRenderer() {
		return new ItemRenderBase() {
			public void renderInventory() {
				GL11.glTranslated(-.5, -3.5, 0); // Translate item model.
				double scale = 3; // Scaling
				GL11.glScaled(scale, scale, scale);
			}
			public void renderCommon() {
				GL11.glRotated(90, 0, 1, 0); // Set rotation if you wanna
				GL11.glShadeModel(GL11.GL_SMOOTH);
				bindTexture(ResourceManager.dahaf_tex); // Set texture...
				ResourceManager.dahaf.renderAll(); //And render!
				GL11.glShadeModel(GL11.GL_FLAT);
			}};
	}
}
