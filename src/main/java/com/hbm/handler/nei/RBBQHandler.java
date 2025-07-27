package com.hbm.handler.nei;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.gui.GUIRBMKOutgasser;
import com.hbm.inventory.gui.GUIRBMKSmelter;
import com.hbm.inventory.recipes.OutgasserRecipes;
import com.hbm.inventory.recipes.RBBQRecipes;

import java.awt.*;

public class RBBQHandler extends NEIUniversalHandler {

	public RBBQHandler() {
		super(ModBlocks.rbmk_smelter.getLocalizedName(), ModBlocks.rbmk_smelter, RBBQRecipes.getRecipes());
	}

	@Override
	public String getKey() {
		return "ntmRBBQ";
	}

	@Override
	public void loadTransferRects() {
		super.loadTransferRects();
		transferRectsGui.add(new RecipeTransferRect(new Rectangle(75, 26, 16, 32), "ntmRBBQ"));
		guiGui.add(GUIRBMKSmelter.class);
		RecipeTransferRectHandler.registerRectsToGuis(guiGui, transferRectsGui);
	}
}
