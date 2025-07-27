package com.hbm.items.food;

import com.hbm.extprop.HbmLivingProps;
import com.hbm.items.ModItems;
import com.hbm.main.MainRegistry;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemNuclearHotDog extends ItemFood {
	private final boolean isRaw;
	private final boolean isBunned;

	public ItemNuclearHotDog(int hunger, boolean isRaw, boolean isBunned) {
		super(hunger,true);
		this.isRaw = isRaw;
		this.isBunned = isBunned;
		setAlwaysEdible();
	}

	@Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote) {
			if(isRaw){
				player.addPotionEffect(new PotionEffect(Potion.hunger.id,10*20,0));
			} else if(isBunned){
				player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id,10,0));
				HbmLivingProps.incrementRadiation(player, 50F);
			} else {
				player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id,20,0));
				HbmLivingProps.incrementRadiation(player, 100F);
			}
        }
    }

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {
		String unloc = this.getUnlocalizedName() + ".desc";
		String loc = I18nUtil.resolveKey(unloc);

		if (!unloc.equals(loc)) {
			String[] locs = loc.split("\\$");
			for (String s : locs) {
				list.add(s);
			}
		}
	}

}
