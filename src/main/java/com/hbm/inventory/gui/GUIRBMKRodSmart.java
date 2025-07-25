package com.hbm.inventory.gui;

import com.hbm.inventory.container.ContainerRBMKRod;
import com.hbm.inventory.container.ContainerRBMKRodSmart;
import com.hbm.items.machine.ItemRBMKRod;
import com.hbm.lib.RefStrings;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKRodSmart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GUIRBMKRodSmart extends GuiInfoContainer {

	private static ResourceLocation texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/reactors/gui_rbmk_element_smart.png");
	private TileEntityRBMKRodSmart rod;
	private GuiTextField[] fields;
	private boolean moderated;


	public GUIRBMKRodSmart(InventoryPlayer invPlayer, TileEntityRBMKRodSmart tedf) {
		super(new ContainerRBMKRodSmart(invPlayer, tedf));
		rod = tedf;

		fields = new GuiTextField[3];
		moderated= rod.enableModerator;

		this.xSize = 176;
		this.ySize = 186;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String name = this.rod.hasCustomInventoryName() ? this.rod.getInventoryName() : I18n.format(this.rod.getInventoryName());

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		for(int i = 0; i < this.fields.length; i++) {
			this.fields[i] = new GuiTextField(this.fontRendererObj, guiLeft + 13, guiTop + 27 + 11 * i, 26, 6);
			this.fields[i].setTextColor(-1);
			this.fields[i].setDisabledTextColour(-1);
			this.fields[i].setEnableBackgroundDrawing(false);
			this.fields[i].setMaxStringLength(4);
		}
		this.fields[0].setText(String.valueOf((int)(rod.depletionLimit*100)));
		this.fields[1].setText(String.valueOf((int)rod.skinHeatLimit));
		this.fields[2].setText(String.valueOf((int)rod.columnHeatLimit));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);

		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 12, guiTop + 26, 30, 10, mouseX, mouseY, new String[]{ "Depletion Percentage Limit", "If fuel depletion exceeds this value the rod will be extractable." } );
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 12, guiTop + 37, 30, 10, mouseX, mouseY, new String[]{ "Skin Heat Limit", "If skin heat exceeds this value the rod will be extractable." } );
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 12, guiTop + 48, 30, 10, mouseX, mouseY, new String[]{ "Column Heat Limit", "If the column heat exceeds this value the rod will be extractable." } );
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 12, guiTop + 59, 30, 10, mouseX, mouseY, new String[]{ "Moderation Setting", moderated ? "Click to disable moderation" : "Clink to enable moderation"} );
		this.drawCustomInfoStat(mouseX, mouseY, guiLeft + 12, guiTop + 70, 30, 10, mouseX, mouseY, new String[]{ "Save parameters" } );
	}

	@Override
	protected void mouseClicked(int x, int y, int i) {
		super.mouseClicked(x, y, i);

		for(int j = 0; j < this.fields.length; j++) {
			this.fields[j].mouseClicked(x, y, i);
		}

		if(guiLeft + 12 <= x && guiLeft + 12 + 30 > x && guiTop + 59 < y && guiTop + 59 + 10 > y ) {
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			moderated=!moderated;
		}

		if(guiLeft + 12 <= x && guiLeft + 12 + 30 > x && guiTop + 70 < y && guiTop + 70 +10 >= y) {

			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			NBTTagCompound data = new NBTTagCompound();

			double[] vals = new double[] {0D ,0D, 0D};

			for(int k = 0; k < fields.length; k++) {

				double clamp = k == 0 ? 100 : 9999;

				if(NumberUtils.isNumber(fields[k].getText())) {
					int j = (int) MathHelper.clamp_double(Double.parseDouble(fields[k].getText()), 0, clamp);
					fields[k].setText(j + "");
					vals[k] = j;
				} else {
					fields[k].setText("0");
				}
			}

			data.setDouble("depletion", vals[0]/100D);
			data.setDouble("skin", vals[1]);
			data.setDouble("column", vals[2]);
			data.setBoolean("moderated",moderated);

			PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(data, rod.xCoord, rod.yCoord, rod.zCoord));
		}
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if(rod.slots[0] != null && rod.slots[0].getItem() instanceof ItemRBMKRod) {
			drawTexturedModalRect(guiLeft + 58, guiTop + 21, 176, 0, 18, 67);

			double depletion = ItemRBMKRod.getDepletion(rod.slots[0]);
			int d = (int)(depletion * 67);
			drawTexturedModalRect(guiLeft + 58, guiTop + 21, 194, 0, 18, d);

			double xenon = ItemRBMKRod.getPoisonLevel(rod.slots[0]);
			int x = (int)(xenon * 58);
			drawTexturedModalRect(guiLeft + 151, guiTop + 82 - x, 212, 58 - x, 14, x);
		}
		if(!moderated){
			drawTexturedModalRect(guiLeft+13,guiTop+60, 176, 67, 25, 8);
		} else {
			drawTexturedModalRect(guiLeft+13,guiTop+60, 176, 67+8, 25, 8);
		}

		for (int i = 0; i < fields.length; i++) {
			fields[i].drawTextBox();
		}
	}

	@Override
	protected void keyTyped(char c, int i) {

		for(int j = 0; j < fields.length; j++) {
			if(this.fields[j].textboxKeyTyped(c, i))
				return;
		}

		super.keyTyped(c, i);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
}
