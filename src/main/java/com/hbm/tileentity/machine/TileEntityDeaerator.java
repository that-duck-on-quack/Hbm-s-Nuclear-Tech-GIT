package com.hbm.tileentity.machine;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.dim.CelestialBody;
import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.lib.Library;
import com.hbm.saveddata.TomSaveData;
import com.hbm.tileentity.IBufPacketReceiver;
import com.hbm.tileentity.IConfigurableMachine;
import com.hbm.tileentity.IFluidCopiable;
import com.hbm.tileentity.TileEntityLoadedBase;
import com.hbm.util.CompatEnergyControl;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;

import java.io.IOException;

public class TileEntityDeaerator extends TileEntityLoadedBase implements IFluidStandardTransceiver, IInfoProviderEC, IConfigurableMachine, IBufPacketReceiver, IFluidCopiable {

	public int age = 0;
	public FluidTank[] tanks;

	public int daTimer = 0;
	protected int throughput;

	//Configurable values
	public static int inputTankSize = 2500;
	public static int outputTankSize = 2500;
	public static int daTankSize = 500;

	public TileEntityDeaerator() {
		tanks = new FluidTank[3];
		tanks[0] = new FluidTank(Fluids.AERATEDWATER, inputTankSize);
		tanks[1] = new FluidTank(Fluids.WATER, outputTankSize);
		tanks[2] = new FluidTank(Fluids.STEAM, daTankSize);
	}

	@Override
	public String getConfigName() {
		return "deaerator";
	}

	@Override
	public void readIfPresent(JsonObject obj) {
		inputTankSize = IConfigurableMachine.grab(obj, "I:inputTankSize", inputTankSize);
		outputTankSize = IConfigurableMachine.grab(obj, "I:outputTankSize", outputTankSize);
		daTankSize = IConfigurableMachine.grab(obj, "I:daTankSize", daTankSize);
	}

	@Override
	public void writeConfig(JsonWriter writer) throws IOException {
		writer.name("I:inputTankSize").value(inputTankSize);
		writer.name("I:outputTankSize").value(outputTankSize);
		writer.name("I:daTankSize").value(daTankSize);
	}

	@Override
	public void updateEntity() {

		if(!worldObj.isRemote) {

			age++;
			if(age >= 2) {
				age = 0;
			}

			if(this.daTimer > 0)
				this.daTimer--;

			int convert = Math.min(tanks[0].getFill(), tanks[1].getMaxFill() - tanks[1].getFill());
			this.throughput = convert;

			if(extraCondition(convert)) {
				tanks[0].setFill(tanks[0].getFill() - convert);

				if(convert > 0)
					this.daTimer = 20;

				if(tanks[2].getFill() > convert/100){
					tanks[1].setFill(tanks[1].getFill() + convert);
					tanks[2].setFill(tanks[2].getFill() - convert/100);
				}
				postConvert(convert);
			}

			this.subscribeToAllAround(tanks[2].getTankType(), this);
			this.subscribeToAllAround(tanks[0].getTankType(), this);
			this.sendFluidToAll(tanks[1], this);

			networkPackNT(150);
		}
	}

	public void packExtra(NBTTagCompound data) { }
	public boolean extraCondition(int convert) { return true; }
	public void postConvert(int convert) { }

	@Override
	public void serialize(ByteBuf buf) {
		this.tanks[0].serialize(buf);
		this.tanks[1].serialize(buf);
		this.tanks[2].serialize(buf);
		buf.writeByte(this.daTimer);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		this.tanks[0].deserialize(buf);
		this.tanks[1].deserialize(buf);
		this.tanks[2].deserialize(buf);
		this.daTimer = buf.readByte();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tanks[0].readFromNBT(nbt, "0");
		tanks[1].readFromNBT(nbt, "1");
		tanks[2].readFromNBT(nbt, "2");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		tanks[0].writeToNBT(nbt, "0");
		tanks[1].writeToNBT(nbt, "1");
		tanks[2].writeToNBT(nbt, "2");
	}

	@Override
	public FluidTank[] getSendingTanks() {
		return new FluidTank[] {tanks [1]};
	}

	@Override
	public FluidTank[] getReceivingTanks()  {
		return new FluidTank[] {tanks[0], tanks[2]};
	}

	@Override
	public FluidTank[] getAllTanks() {
		return tanks;
	}

	@Override
	public void provideExtraInfo(NBTTagCompound data) {
		data.setDouble(CompatEnergyControl.D_CONSUMPTION_MB, throughput);
		data.setDouble(CompatEnergyControl.D_OUTPUT_MB, throughput);
	}

	@Override
	public FluidTank getTankToPaste() {
		return null;
	}

	AxisAlignedBB bb = null;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {

		if (bb == null) {
			bb = AxisAlignedBB.getBoundingBox(
				xCoord - 3,
				yCoord,
				zCoord - 3,
				xCoord + 4,
				yCoord + 3,
				zCoord + 4
			);
		}
		return bb;
	}
}
