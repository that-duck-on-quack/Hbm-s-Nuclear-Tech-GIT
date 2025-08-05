package com.hbm.items.machine;

import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAssemblyIdent extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) return stack;
		if(!stack.hasTagCompound()){
			stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setBoolean("mode", true);
			stack.stackTagCompound.setString("recipe", "ass.stardar");
			return stack;
		}

		boolean mode = stack.stackTagCompound.getBoolean("mode");

		stack.stackTagCompound.setBoolean("mode", !mode);
		player.swingItem();

		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ext) {

		if(!stack.hasTagCompound()){
			list.add(EnumChatFormatting.RED + "Right click to initiate identifier!");
			return;
		}

		boolean mode = stack.stackTagCompound.getBoolean("mode");

		if(mode) {
			list.add(EnumChatFormatting.BLUE + "Setting Recipe...");
		} else {
			list.add(EnumChatFormatting.GREEN + "Ready!");
		}
		String name = stack.stackTagCompound.getString("recipe");
		GenericRecipe recipe = AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(name);
		if(recipe != null) {
			list.add("Current Recipe: " + recipe.getLocalizedName());
		}else{
			list.add(EnumChatFormatting.RED + "Unknown recipe: " + name);
		}
	}
}
