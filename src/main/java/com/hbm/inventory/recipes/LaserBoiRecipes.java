package com.hbm.inventory.recipes;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemCircuit;
import com.hbm.main.MainRegistry;
import net.minecraft.item.ItemStack;


//Reminder to self: adapt this so it doesn't use a hashmap, it's unnecessary.
public class LaserBoiRecipes extends SerializableRecipe {
	public static HashMap<ComparableStack, engraverRecipe> recipes = new HashMap<>();

	@Override
	public void registerDefaults() {
		makeRecipe(new ComparableStack(ModItems.billet_silicon, 1), 1, new ComparableStack(OreDictManager.DictFrame.fromOne(ModItems.circuit, ItemCircuit.EnumCircuitType.SILICON)));
		makeRecipe(new ComparableStack(ModItems.billet_gaas, 1), 1, new ComparableStack(OreDictManager.DictFrame.fromOne(ModItems.circuit, ItemCircuit.EnumCircuitType.GAAS)));
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
		JsonObject obj = recipe.getAsJsonObject();

		ItemStack input = readItemStack(obj.get("input").getAsJsonArray());
		int tier = obj.get("tier").getAsInt();
		ItemStack output = readItemStack(obj.get("output").getAsJsonArray());
		makeRecipe(new ComparableStack(input), tier, new ComparableStack(output));
	}

	@Override
	public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
		Entry<ComparableStack, engraverRecipe> entry = (Entry<ComparableStack, engraverRecipe>) recipe;
		ItemStack in = entry.getValue().input.toStack();
		ItemStack out = entry.getKey().toStack();
		int tier = entry.getValue().crystalTier;

		writer.name("input");
		this.writeItemStack(in, writer);
		writer.name("tier").value(tier);
		writer.name("output");
		this.writeItemStack(out, writer);
	}

	public static void makeRecipe(ComparableStack in, int tier, ComparableStack out){
		if(out == null) {
			MainRegistry.logger.error("Laser Engraver recipe returned null!");
			return;
		}
		engraverRecipe recipe = new engraverRecipe(in, tier, out);
		recipes.put(out, recipe);
	}

	public static List getRecipes() {
		List recipeList = new ArrayList();
		recipeList.addAll(recipes.entrySet());
		return recipeList;
	}

	public static class engraverRecipe {
		public ComparableStack input;
		public int crystalTier;
		public ComparableStack output;

		public engraverRecipe(ComparableStack input, int crystalTier, ComparableStack output) {
			this.input = input;
			this.crystalTier = crystalTier;
			this.output = output;
		}
	}

}
