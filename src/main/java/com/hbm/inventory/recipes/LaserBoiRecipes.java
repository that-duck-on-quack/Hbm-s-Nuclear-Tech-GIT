package com.hbm.inventory.recipes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;

public class LaserBoiRecipes extends SerializableRecipe {
	public static HashMap<ComparableStack, ItemStack> recipes = new HashMap<ComparableStack, ItemStack>();

	@Override
	public void registerDefaults() {
		recipes.put(new ComparableStack(ModItems.billet_silicon,1 ), new ItemStack(ModItems.billet_silicon));
	}

	public String getFileName() { return "hbmLaserBoi.json"; }

	@Override
	public Object getRecipeObject() {
		return recipes;
	}

	@Override
	public void deleteRecipes() {
		recipes.clear();
	}

	@Override
	public void readRecipe(JsonElement recipe) {
		JsonElement input = ((JsonObject)recipe).get("input");
		JsonElement output = ((JsonObject)recipe).get("output");
		ItemStack in = this.readItemStack((JsonArray) input);
		ItemStack out = this.readItemStack((JsonArray) output);
		recipes.put(new ComparableStack(in), out);
	}

	@Override
	public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
		Entry<ComparableStack, ItemStack> entry = (Entry<ComparableStack, ItemStack>) recipe;
		ItemStack in = entry.getKey().toStack();
		ItemStack out = entry.getValue();

		writer.name("input");
		this.writeItemStack(in, writer);
		writer.name("output");
		this.writeItemStack(out, writer);
	}

}
