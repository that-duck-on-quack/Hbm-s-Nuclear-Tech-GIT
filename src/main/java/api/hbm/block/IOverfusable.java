package api.hbm.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * For machines that can be boosted using the singularity screwdriver.
 * @author Jack Andersen
 */
public interface IOverfusable {
	boolean onOverfuse(World world, EntityPlayer player, int x, int y, int z, int side, float fX, float fY, float fZ, ItemStack item);
}
