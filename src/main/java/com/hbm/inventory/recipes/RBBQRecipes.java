package com.hbm.inventory.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ItemEnums.EnumTarType;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.util.Tuple.Pair;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.hbm.inventory.OreDictManager.*;

public class RBBQRecipes extends SerializableRecipe {

	public static Map<AStack, ItemStack> recipes = new HashMap<>();

	@Override
	public void registerDefaults() {
		recipes.put(new ComparableStack(ModItems.gsa_hot_dog_raw), new ItemStack(ModItems.gsa_hot_dog_cooked));
		recipes.put(new ComparableStack(new ItemStack(ModItems.marshmallow, 1, 0)), new ItemStack(ModItems.marshmallow, 1, 1));
	}

	public static @Nullable ItemStack getOutput(ItemStack input) {

		ComparableStack comp = new ComparableStack(input).makeSingular();

		if(recipes.containsKey(comp)) {
			return recipes.get(comp);
		}

		String[] dictKeys = comp.getDictKeys();

		for(String key : dictKeys) {
			OreDictStack dict = new OreDictStack(key);
			if(recipes.containsKey(dict)) {
				return recipes.get(dict);
			}
		}

		return null;
	}

	public static HashMap getRecipes() {

		HashMap<Object, Object[]> recipes = new HashMap<Object, Object[]>();

		for(Entry<AStack, ItemStack> entry : RBBQRecipes.recipes.entrySet()) {

			AStack input = entry.getKey();
			recipes.put(input,new Object[]{entry.getValue()});
		}

		return recipes;
	}

	@Override
	public String getFileName() {
		return "hbmRBBQ.json";
	}

	@Override
	public Object getRecipeObject() {
		return recipes;
	}

	@Override
	public void readRecipe(JsonElement recipe) {
		JsonObject obj = (JsonObject) recipe;

		AStack input = this.readAStack(obj.get("input").getAsJsonArray());
		ItemStack output = null;

		if(obj.has("output")) {
			output = this.readItemStack(obj.get("solidOutput").getAsJsonArray());
		}

		if(output != null) {
			this.recipes.put(input, output);
		}
	}

	@Override
	public void writeRecipe(Object recipe, JsonWriter writer) throws IOException {
		Entry<AStack, ItemStack> rec = (Entry<AStack, ItemStack>) recipe;

		writer.name("input");
		this.writeAStack(rec.getKey(), writer);
		writer.name("output");
		this.writeItemStack(rec.getValue(), writer);
	}

	@Override
	public void deleteRecipes() {
		recipes.clear();
	}
}
