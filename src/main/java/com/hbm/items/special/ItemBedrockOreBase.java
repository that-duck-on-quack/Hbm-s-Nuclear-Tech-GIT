package com.hbm.items.special;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hbm.items.special.ItemBedrockOreNew.BedrockOreType;
import com.hbm.items.tool.ItemOreDensityScanner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class ItemBedrockOreBase extends Item {

	public static double getOreAmount(ItemStack stack, BedrockOreType type) {
		if(!stack.hasTagCompound()) return 0;
		NBTTagCompound data = stack.getTagCompound();
		return data.getDouble(type.suffix);
	}
	
	public static void setOreAmount(World world, ItemStack stack, int x, int z) {
		if(!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
		NBTTagCompound data = stack.getTagCompound();

		for(BedrockOreType type : BedrockOreType.values()) {
			data.setDouble(type.suffix, getOreLevel(world, x, z, type));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		
		for(BedrockOreType type : BedrockOreType.values()) {
			double amount = this.getOreAmount(stack, type);
			String typeName = StatCollector.translateToLocalFormatted("item.bedrock_ore.type." + type.suffix + ".name");
			list.add(typeName + ": " + ((int) (amount * 100)) / 100D + " (" + StatCollector.translateToLocalFormatted(ItemOreDensityScanner.translateDensity(amount)) + EnumChatFormatting.RESET + ")");
		}
	}
	
	public static double getOreLevel(World world, int x, int z, BedrockOreType type) {
		long seed = world.getSeed() + world.provider.dimensionId;

		NoiseGeneratorPerlin level = getGenerator(seed);
		NoiseGeneratorPerlin ore = getGenerator(seed - 4096 + type.ordinal());
		
		double scale = 0.01D;
		
		return MathHelper.clamp_double(Math.abs(level.func_151601_a(x * scale, z * scale) * ore.func_151601_a(x * scale, z * scale)) * 0.05, 0, 2);
	}

	private static Map<Long, NoiseGeneratorPerlin> generators = new HashMap<>();

	private static NoiseGeneratorPerlin getGenerator(long seed) {
		return generators.computeIfAbsent(seed, key -> new NoiseGeneratorPerlin(new Random(seed), 4));
	}

}
