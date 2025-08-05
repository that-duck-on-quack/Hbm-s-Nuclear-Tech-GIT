package com.hbm.tileentity.machine;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.MachineStardar;
import com.hbm.tileentity.TileEntityMachineBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDishControl extends TileEntityMachineBase {

	private TileEntityMachineStardar dish;
	public int[] linkPosition = new int[3];
	public boolean isLinked;
	private boolean foundLink = false;

	public TileEntityDishControl() {
		super(0);
	}

	@Override
	public String getName() {
		return "container.dishControl";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isLinked = nbt.getBoolean("isLinked");
		linkPosition[0] = nbt.getInteger("linkPosX");
		linkPosition[1] = nbt.getInteger("linkPosY");
		linkPosition[2] = nbt.getInteger("linkPosZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setBoolean("isLinked", isLinked);

		if(linkPosition[0] != 0 || linkPosition[1] != 0 || linkPosition[2] != 0) {
			nbt.setInteger("linkPosX", linkPosition[0]);
			nbt.setInteger("linkPosY", linkPosition[1]);
			nbt.setInteger("linkPosZ", linkPosition[2]);
		}
	}

	@Override
	public void updateEntity() {
		if(worldObj.isRemote) return;

		if(!foundLink && isLinked)
		{
			isLinked = establishLink(linkPosition[0], linkPosition[1], linkPosition[2]);
			foundLink = true;
		}

		this.networkPackNT(15);
	}

	@SideOnly(Side.CLIENT)
	public TileEntityMachineStardar getLinkedDishClientSafe() {
		if (dish == null && isLinked) {
			TileEntity te = worldObj.getTileEntity(linkPosition[0], linkPosition[1], linkPosition[2]);
			if (te instanceof TileEntityMachineStardar) {
				dish = (TileEntityMachineStardar) te;
			}
		}
		return dish;
	}

	@SideOnly(Side.CLIENT)
	public boolean starDarHasDisk() {
		TileEntityMachineStardar link = getLinkedDishClientSafe();
		return link != null && link.slots[0] != null;
	}

	@Override
	public void serialize(ByteBuf buf) {
		super.serialize(buf);
		buf.writeBoolean(isLinked);
		buf.writeInt(linkPosition[0]);
		buf.writeInt(linkPosition[1]);
		buf.writeInt(linkPosition[2]);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		super.deserialize(buf);
		isLinked = buf.readBoolean();
		linkPosition[0] = buf.readInt();
		linkPosition[1] = buf.readInt();
		linkPosition[2] = buf.readInt();
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	public void TryLink(ItemStack stack)
	{
		isLinked = linkWithSensor(stack);
	}

	private boolean linkWithSensor(ItemStack stack)
	{
		if(stack.getItem() != null && stack.stackTagCompound != null) {
			// Get the StarDar coordinates from the sensor
			int x = stack.stackTagCompound.getInteger("x");
			int y = stack.stackTagCompound.getInteger("y");
			int z = stack.stackTagCompound.getInteger("z");

			return establishLink(x, y, z);
		}

		return false;
	}

	private boolean establishLink(int x, int y, int z) {

		// Get the StarDar block
		Block b = worldObj.getBlock(x, y, z);

		if(b == ModBlocks.machine_stardar) {

			int[] pos = ((MachineStardar)ModBlocks.machine_stardar).findCore(worldObj, x, y, z);

			if(pos != null) {

				TileEntity tile = worldObj.getTileEntity(pos[0], pos[1], pos[2]);

				if(tile instanceof TileEntityMachineStardar) {
					// Dish linked successfully
					linkPosition = pos;
					return true;
				}
			}
		}

		return false;
	}

	AxisAlignedBB bb = null;
	@Override
	public AxisAlignedBB getRenderBoundingBox() {

		if(bb == null) {
			bb = AxisAlignedBB.getBoundingBox(
				xCoord - 1,
				yCoord,
				zCoord - 1,
				xCoord + 2,
				yCoord + 1,
				zCoord + 2
			);
		}

		return bb;
	}
}
