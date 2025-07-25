package api.hbm.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IOverfusable {
	boolean onOverfuse(World world, EntityPlayer player, int x, int y, int z, int side, float fX, float fY, float fZ, ItemStack item);
}
