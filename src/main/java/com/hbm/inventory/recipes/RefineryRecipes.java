package com.hbm.inventory.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ItemEnums.EnumTarType;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.util.ItemStackUtil;
import com.hbm.util.Tuple.Quartet;
import com.hbm.util.Tuple.Quintet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class RefineryRecipes {

	/// fractions in percent ///
	public static final int oil_frac_heavy = 50;
	public static final int oil_frac_naph = 25;
	public static final int oil_frac_light = 15;
	public static final int oil_frac_petro = 10;
	public static final int crack_frac_naph = 40;
	public static final int crack_frac_light = 30;
	public static final int crack_frac_aroma = 15;
	public static final int crack_frac_unsat = 15;

	public static final int oilds_frac_heavy = 30;
	public static final int oilds_frac_naph = 35;
	public static final int oilds_frac_light = 20;
	public static final int oilds_frac_unsat = 15;
	public static final int crackds_frac_naph = 35;
	public static final int crackds_frac_light = 35;
	public static final int crackds_frac_aroma = 15;
	public static final int crackds_frac_unsat = 15;

	public static final int vac_frac_heavy = 40;
	public static final int vac_frac_reform = 25;
	public static final int vac_frac_light = 20;
	public static final int vac_frac_sour = 15;

	//hbm hard stuff
	public static final int Oil_frack = 80;
	public static final int coaloil_frack = 10;
	public static final int water_frack = 7;
	public static final int oreslop_frack =3;

	private static Map<FluidType, Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> refinery = new HashMap();
	private static Map<FluidType, Quartet<FluidStack, FluidStack, FluidStack, FluidStack>> vacuum = new HashMap();

	public static HashMap<Object, Object[]> getRefineryRecipe() {

		HashMap<Object, Object[]> recipes = new HashMap<Object, Object[]>();

		for(Entry<FluidType, Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack>> recipe : refinery.entrySet()) {

			Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> fluids = recipe.getValue();

			recipes.put(ItemFluidIcon.make(recipe.getKey(), 1000),
					new ItemStack[] {
							ItemFluidIcon.make(fluids.getV().type, fluids.getV().fill * 10),
							ItemFluidIcon.make(fluids.getW().type, fluids.getW().fill * 10),
							ItemFluidIcon.make(fluids.getX().type, fluids.getX().fill * 10),
							ItemFluidIcon.make(fluids.getY().type, fluids.getY().fill * 10),
							ItemStackUtil.carefulCopy(fluids.getZ()) });
		}

		return recipes;
	}

	public static HashMap getVacuumRecipe() {

		HashMap<Object, Object[]> recipes = new HashMap<Object, Object[]>();

		for(Entry<FluidType, Quartet<FluidStack, FluidStack, FluidStack, FluidStack>> recipe : vacuum.entrySet()) {

			Quartet<FluidStack, FluidStack, FluidStack, FluidStack> fluids = recipe.getValue();

			recipes.put(ItemFluidIcon.make(recipe.getKey(), 1000, 2),
					new ItemStack[] {
							ItemFluidIcon.make(fluids.getW().type, fluids.getW().fill * 10),
							ItemFluidIcon.make(fluids.getX().type, fluids.getX().fill * 10),
							ItemFluidIcon.make(fluids.getY().type, fluids.getY().fill * 10),
							ItemFluidIcon.make(fluids.getZ().type, fluids.getZ().fill * 10) });
		}

		return recipes;
	}

	public static void registerRefinery() {
		refinery.put(Fluids.HOTOIL, new Quintet(
				new FluidStack(Fluids.HEAVYOIL,		oil_frac_heavy),
				new FluidStack(Fluids.NAPHTHA,		oil_frac_naph),
				new FluidStack(Fluids.LIGHTOIL,		oil_frac_light),
				new FluidStack(Fluids.PETROLEUM,	oil_frac_petro),
				new ItemStack(ModItems.sulfur)
				));
		refinery.put(Fluids.HOTCRACKOIL, new Quintet(
				new FluidStack(Fluids.NAPHTHA_CRACK,	crack_frac_naph),
				new FluidStack(Fluids.LIGHTOIL_CRACK,	crack_frac_light),
				new FluidStack(Fluids.AROMATICS,		crack_frac_aroma),
				new FluidStack(Fluids.UNSATURATEDS,		crack_frac_unsat),
				DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRACK)
				));
		refinery.put(Fluids.HOTOIL_DS, new Quintet(
				new FluidStack(Fluids.HEAVYOIL,		oilds_frac_heavy),
				new FluidStack(Fluids.NAPHTHA_DS,	oilds_frac_naph),
				new FluidStack(Fluids.LIGHTOIL_DS,	oilds_frac_light),
				new FluidStack(Fluids.UNSATURATEDS,	oilds_frac_unsat),
				DictFrame.fromOne(ModItems.oil_tar, EnumTarType.PARAFFIN)
				));
		refinery.put(Fluids.HOTCRACKOIL_DS, new Quintet(
				new FluidStack(Fluids.NAPHTHA_DS,		crackds_frac_naph),
				new FluidStack(Fluids.LIGHTOIL_DS,		crackds_frac_light),
				new FluidStack(Fluids.AROMATICS,		crackds_frac_aroma),
				new FluidStack(Fluids.UNSATURATEDS,		crackds_frac_unsat),
				DictFrame.fromOne(ModItems.oil_tar, EnumTarType.PARAFFIN)
				));

		//hbm hard stuff
		refinery.put(Fluids.OSLURRY, new Quintet(
			new FluidStack(Fluids.OIL, Oil_frack),
			new FluidStack(Fluids.COALOIL, coaloil_frack),
			new FluidStack(Fluids.WATER, water_frack),
			new FluidStack(Fluids.SLOP, oreslop_frack),
			new ItemStack(Blocks.gravel)
			));
		refinery.put(Fluids.LIGHTOIL_CRACK, new Quintet(
			new FluidStack(Fluids.TOULENE,12),
			new FluidStack(Fluids.BENZENE,60),
			new FluidStack(Fluids.BUTENE,24),
			new FluidStack(Fluids.ETHANE,4),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRACK)
		));
		refinery.put(Fluids.NAPHTHA_CRACK, new Quintet(
			new FluidStack(Fluids.GASOLINE,30),
			new FluidStack(Fluids.PROPANE,5),
			new FluidStack(Fluids.ETHANE,10),
			new FluidStack(Fluids.ETHYLENE,55),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRACK)
		));
		refinery.put(Fluids.LIGHTOIL, new Quintet(
			new FluidStack(Fluids.NAPHTHA,50),
			new FluidStack(Fluids.KEROSENE,30),
			new FluidStack(Fluids.ETHANE,5),
			new FluidStack(Fluids.PETROLEUM,15),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRUDE)
		));
		refinery.put(Fluids.NAPHTHA, new Quintet(
			new FluidStack(Fluids.GASOLINE,50),
			new FluidStack(Fluids.BUTENE,15),
			new FluidStack(Fluids.ETHANE,10),
			new FluidStack(Fluids.PETROLEUM,25),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRUDE)
		));
		refinery.put(Fluids.HEAVYOIL, new Quintet(
			new FluidStack(Fluids.LIGHTOIL, 50),
			new FluidStack(Fluids.NAPHTHA, 30),
			new FluidStack(Fluids.BUTADIENE, 10),
			new FluidStack(Fluids.BITUMEN, 10),
			new ItemStack(ModBlocks.asphalt,1)
		));
		refinery.put(Fluids.WOODOIL, new Quintet(
			new FluidStack(Fluids.ETHANOL,5),
			new FluidStack(Fluids.BENZENE,50),
			new FluidStack(Fluids.TOULENE,25),
			new FluidStack(Fluids.PHENOL,20),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.WOOD)
		));
		refinery.put(Fluids.COALOIL, new Quintet(
			new FluidStack(Fluids.LIGHTOIL,10),
			new FluidStack(Fluids.COALCREOSOTE,15),
			new FluidStack(Fluids.HEAVYOIL,20),
			new FluidStack(Fluids.BITUMEN,45),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.COAL)
		));

		refinery.put(Fluids.UNSATURATEDS,new Quintet(
			new FluidStack(Fluids.PETROLEUM,10),
			new FluidStack(Fluids.ETHYLENE,20),
			new FluidStack(Fluids.BUTADIENE,20),
			new FluidStack(Fluids.ACETYLENE,20),
			DictFrame.fromOne(ModItems.oil_tar, EnumTarType.CRACK)
		));


		vacuum.put(Fluids.OIL, new Quartet(
				new FluidStack(Fluids.HEAVYOIL_VACUUM,	vac_frac_heavy),
				new FluidStack(Fluids.REFORMATE,		vac_frac_reform),
				new FluidStack(Fluids.LIGHTOIL_VACUUM,	vac_frac_light),
				new FluidStack(Fluids.SOURGAS,			vac_frac_sour)
				));
		vacuum.put(Fluids.OIL_DS, new Quartet(
				new FluidStack(Fluids.HEAVYOIL_VACUUM,	vac_frac_heavy),
				new FluidStack(Fluids.REFORMATE,		vac_frac_reform),
				new FluidStack(Fluids.LIGHTOIL_VACUUM,	vac_frac_light),
				new FluidStack(Fluids.REFORMGAS,		vac_frac_sour)
				));
	}

	public static Quintet<FluidStack, FluidStack, FluidStack, FluidStack, ItemStack> getRefinery(FluidType oil) {
		return refinery.get(oil);
	}

	public static Quartet<FluidStack, FluidStack, FluidStack, FluidStack> getVacuum(FluidType oil) {
		return vacuum.get(oil);
	}
}
