package com.hbm.handler.nei;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.machine.ItemFluidIcon;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DeaeratorHandler extends NEIUniversalHandler {

	public DeaeratorHandler() {
		super(ModBlocks.deaerator.getLocalizedName(), new ItemStack[] { new ItemStack(ModBlocks.deaerator), new ItemStack(ModBlocks.deaerator) }, generateRecipes());
	}

	@Override
	public String getKey() {
		return "ntmDeaerator";
	}

	public static HashMap<Object, Object> generateRecipes() {
		HashMap<Object, Object> map = new HashMap();
		map.put(ItemFluidIcon.make(Fluids.AERATEDWATER, 1_000), ItemFluidIcon.make(Fluids.WATER, 1000));
		return map;
	}
}
