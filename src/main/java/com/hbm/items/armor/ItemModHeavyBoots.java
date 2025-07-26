package com.hbm.items.armor;

import java.util.List;

import com.hbm.dim.CelestialBody;
import com.hbm.handler.ArmorModHandler;
import com.hbm.util.AstronomyUtil;
import com.hbm.util.i18n.I18nUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemModHeavyBoots extends ItemArmorMod {

	public ItemModHeavyBoots() {
		super(ArmorModHandler.boots_only, false, false, false, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.BLUE + "Increases fall speed in low gravity");
		list.add(EnumChatFormatting.BLUE + "Activated by crouching");
		list.add("");
		super.addInformation(itemstack, player, list, bool);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addDesc(List list, ItemStack stack, ItemStack armor) {
		list.add(EnumChatFormatting.DARK_PURPLE + "  " + stack.getDisplayName() + " (" + I18nUtil.resolveKey("armor.fastFall") + ")");
	}

	@Override
	public void modUpdate(EntityLivingBase entity, ItemStack armor) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			// Skip if we have armor that already applies fast fall buff
			if(armor.getItem() instanceof ArmorDNT) {
				if(ArmorFSB.hasFSBArmor(player)) return;
			}

			// if crouching in air, apply extra gravity until we match the overworld
			if(entity.isSneaking() && !entity.onGround && !entity.isInWater()) {
				float gravity = CelestialBody.getGravity(entity);
				if(gravity > 1.5F) return;
				if(gravity == 0) return;
				if(gravity < 0.2F) gravity = 0.2F;

				entity.motionY /= 0.98F;
				entity.motionY += (gravity / 20F);
				entity.motionY -= (AstronomyUtil.STANDARD_GRAVITY / 20F);
				entity.motionY *= 0.98F;
			}
		}
	}

}
