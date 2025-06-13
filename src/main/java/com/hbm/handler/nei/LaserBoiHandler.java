package com.hbm.handler.nei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.LaserBoiRecipes;

import net.minecraft.item.ItemStack;
public class LaserBoiHandler extends NEIUniversalHandler {

	public LaserBoiHandler() {
		super("Laser Engraver", new ItemStack[] { new ItemStack(ModBlocks.machine_laserboi), new ItemStack(ModBlocks.machine_laserboi) }, generateRecipes());
	}

	@Override
	public String getKey() {
		return "ntmLaserboi";
	}


	public static HashMap<Object, Object> generateRecipes() {
		List recipeList = LaserBoiRecipes.getRecipes();
		HashMap<Object, Object> recipes = new HashMap<>();
		for(Object recipe : recipeList){
			Map.Entry<RecipesCommon.ComparableStack, LaserBoiRecipes.engraverRecipe> entry = (Map.Entry<RecipesCommon.ComparableStack, LaserBoiRecipes.engraverRecipe>) recipe;
			ItemStack input = entry.getValue().input.toStack();
			ItemStack output = entry.getKey().toStack();
			recipes.put(input, output);
		}
		return recipes;
	}
}
