package com.hbm.handler.nei;

import java.util.HashMap;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ItemEnums.EnumAshType;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemCircuit;
import com.hbm.items.machine.ItemFluidIcon;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
public class LaserBoiHandler extends NEIUniversalHandler {

	public LaserBoiHandler() {
		super(ModBlocks.machine_laserboi.getLocalizedName(), new ItemStack[] { new ItemStack(ModBlocks.machine_laserboi), new ItemStack(ModBlocks.machine_laserboi) }, generateRecipes());
	}

	@Override
	public String getKey() {
		return "ntmLaserboi";
	}

	public static HashMap<Object, Object> generateRecipes() {
		HashMap<Object, Object> recipes = new HashMap();

		ItemStack[] billetsA = new ItemStack[] {new ItemStack(ModItems.billet_silicon)};
		ItemStack[] billetsB = new ItemStack[] { new ItemStack(ModItems.billet_gaas)};

		recipes.put(new ItemStack[][] {billetsA}, new ItemStack(ModItems.circuit, 1, ItemCircuit.EnumCircuitType.SILICON.ordinal()) );
		recipes.put(new ItemStack[][] {billetsB}, new ItemStack(ModItems.circuit, 1, ItemCircuit.EnumCircuitType.GAAS.ordinal()) );

		return recipes;
	}
}
