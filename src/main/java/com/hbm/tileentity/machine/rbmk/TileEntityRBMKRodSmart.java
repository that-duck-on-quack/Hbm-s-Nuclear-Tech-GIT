package com.hbm.tileentity.machine.rbmk;

import com.hbm.config.ServerConfig;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.container.ContainerRBMKRod;
import com.hbm.inventory.container.ContainerRBMKRodSmart;
import com.hbm.inventory.gui.GUIRBMKRodSmart;
import com.hbm.items.machine.ItemRBMKRod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Variant of the RBMK Moderated Fuel Rod that also supports automated replacement of fuel rods based on their stats.
 * It also has moderation toggle ability.
 * @author Jack Andersen
 */
public class TileEntityRBMKRodSmart extends TileEntityRBMKRod implements IControlReceiver {
	public boolean enableModerator = true;

	// If any of these limits are met or exceeded the fuel rod will become extractable.
	public double skinHeatLimit = 2000D;
	public double columnHeatLimit = 1400D;
	public double depletionLimit = 0.8D;
	// Do not use this on the serverside, this is solely here for client gui sync purposes. use ServerConfig.Sk_smartestSmartRod.get() instead.
	public static boolean enablePoorMansScram = ServerConfig.Sk_smartestSmartRod.get();

	@Override
	public String getName() {
		return "container.rbmkRodSmart";
	}

	/**
	 * This fuel rod is optionally moderated.
	 */
	@Override
	public boolean isModerated() {
		return enableModerator;
	}

	/**
	 * Allows items to be inserted into the fuel slot. The index doesn't matter as the only slot available is 0.
	 */
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return i == 0 && itemStack.getItem() instanceof ItemRBMKRod;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return i == 0 && hasFuelExceededLimits() && itemStack.isItemEqual(slots[0]);
	}

	public boolean hasFuelExceededLimits(){
		ItemStack stack = slots[0];
		if(ServerConfig.Sk_smartestSmartRod.get()){
			return ItemRBMKRod.getDepletion(stack) >= depletionLimit || ItemRBMKRod.getHullHeat(stack) >= skinHeatLimit || heat >= columnHeatLimit;
		}else{
			return ItemRBMKRod.getDepletion(stack) >= depletionLimit;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GUIRBMKRodSmart(player.inventory, this);
	}

	@Override
	public boolean hasPermission(EntityPlayer player) {
		return Vec3.createVectorHelper(xCoord - player.posX, yCoord - player.posY, zCoord - player.posZ).lengthVector() < 20;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setDouble("depletionLimit", depletionLimit);
		nbt.setDouble("skinHeatLimit", skinHeatLimit);
		nbt.setDouble("columnHeatLimit", columnHeatLimit);
		nbt.setBoolean("enableModerator", enableModerator);
	}

	@Override
	public void serialize(ByteBuf buf) {
		super.serialize(buf);
		buf.writeDouble(this.depletionLimit);
		buf.writeDouble(this.skinHeatLimit);
		buf.writeDouble(this.columnHeatLimit);
		buf.writeBoolean(this.enableModerator);
		buf.writeBoolean(ServerConfig.Sk_smartestSmartRod.get());
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if(data.hasKey("depletionLimit", 6)){
			this.depletionLimit = MathHelper.clamp_double(data.getDouble("depletionLimit"),0D,1D);
		}
		if(data.hasKey("skinHeatLimit", 6)){
			this.skinHeatLimit = MathHelper.clamp_double(data.getDouble("skinHeatLimit"),0D,9999D);
		}
		if(data.hasKey("columnHeatLimit", 6)){
			this.columnHeatLimit = MathHelper.clamp_double(data.getDouble("columnHeatLimit"),0D,9999D);
		}
		if(data.hasKey("enableModerator",1)){
			this.enableModerator = data.getBoolean("enableModerator");
		}
	}

	@Override
	public void deserialize(ByteBuf buf) {
		super.deserialize(buf);
		this.depletionLimit = buf.readDouble();
		this.skinHeatLimit = buf.readDouble();
		this.columnHeatLimit = buf.readDouble();
		this.enableModerator = buf.readBoolean();
		enablePoorMansScram = buf.readBoolean();
	}


	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		if(EnumFacing.UP.ordinal() == p_94128_1_ || EnumFacing.DOWN.ordinal() == p_94128_1_){
			return new int[]{0};
		}
		else {
			return new int[0];
		}
	}


	@Override
	public void receiveControl(NBTTagCompound data) {
		if(data.hasKey("depletion", 6)){
			this.depletionLimit = MathHelper.clamp_double(data.getDouble("depletion"),0D,1D);
		}
		if(data.hasKey("skin", 6)){
			this.skinHeatLimit = MathHelper.clamp_double(data.getDouble("skin"),0D,9999D);
		}
		if(data.hasKey("column", 6)){
			this.columnHeatLimit = MathHelper.clamp_double(data.getDouble("column"),0D,9999D);
		}
		if(data.hasKey("moderated",1)){
			this.enableModerator = data.getBoolean("moderated");
		}
		this.markDirty();
	}

	@Override
	public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerRBMKRodSmart(player.inventory, this);
	}
}
