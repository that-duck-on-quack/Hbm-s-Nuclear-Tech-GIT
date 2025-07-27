package com.hbm.items.machine;

import java.util.List;

import com.hbm.items.ItemEnumMulti;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemComps extends  ItemEnumMulti {

	public ItemComps() {
		super(EnumComptype.class, true, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,CreativeTabs tabs,List list){
		list.add(new ItemStack(item,1,EnumComptype.BARREL.ordinal()));

	}

	public static  enum EnumComptype {
		BARREL
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {

	}
}
