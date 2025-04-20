package com.hbm.tileentity.machine;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.IConfigurableMachine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityTowerSmall extends TileEntityCondenser {

	//Configurable values
	public static int inputTankSizeTS = 1_000;
	public static int outputTankSizeTS = 1_000;
	public static int evTankSizeTS = 500;

	public TileEntityTowerSmall() {
		tanks = new FluidTank[3];
		tanks[0] = new FluidTank(Fluids.SPENTSTEAM, inputTankSizeTS);
		tanks[1] = new FluidTank(Fluids.AERATEDWATER, outputTankSizeTS);
		tanks[2] = new FluidTank(Fluids.WATER, evTankSizeTS);
		heatExchanging = true;
	}

	@Override
	public String getConfigName() {
		return "condenserTowerSmall";
	}

	@Override
	public void readIfPresent(JsonObject obj) {
		inputTankSizeTS = IConfigurableMachine.grab(obj, "I:inputTankSize", inputTankSizeTS);
		outputTankSizeTS = IConfigurableMachine.grab(obj, "I:outputTankSize", outputTankSizeTS);
		evTankSizeTS = IConfigurableMachine.grab(obj, "I:evTankSize", evTankSizeTS);
	}

	@Override
	public void writeConfig(JsonWriter writer) throws IOException {
		writer.name("I:inputTankSize").value(inputTankSizeTS);
		writer.name("I:outputTankSize").value(outputTankSizeTS);
		writer.name("I:evTankSize").value(evTankSizeTS);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if(worldObj.isRemote) {
			int convert = Math.min(tanks[0].getFill(), tanks[1].getMaxFill() - tanks[1].getFill());
			if(GeneralConfig.enableSteamParticles && (this.waterTimer > 0 && this.worldObj.getTotalWorldTime() % 2 == 0 && this.tanks[2].getFill()>convert/2)) {
				NBTTagCompound data = new NBTTagCompound();
				data.setString("type", "tower");
				data.setFloat("lift", 1F);
				data.setFloat("base", 0.5F);
				data.setFloat("max", 4F);
				data.setInteger("life", 250 + worldObj.rand.nextInt(250));

				data.setDouble("posX", xCoord + 0.5);
				data.setDouble("posZ", zCoord + 0.5);
				data.setDouble("posY", yCoord + 18);

				MainRegistry.proxy.effectNT(data);
			}
		}
	}

	@Override
	public void subscribeToAllAround(FluidType type, TileEntity te) {
		this.trySubscribe(this.tanks[0].getTankType(), worldObj, xCoord + 3, yCoord, zCoord, Library.POS_X);
		this.trySubscribe(this.tanks[0].getTankType(), worldObj, xCoord - 3, yCoord, zCoord, Library.NEG_X);
		this.trySubscribe(this.tanks[0].getTankType(), worldObj, xCoord, yCoord, zCoord + 3, Library.POS_Z);
		this.trySubscribe(this.tanks[0].getTankType(), worldObj, xCoord, yCoord, zCoord - 3, Library.NEG_Z);
		this.trySubscribe(this.tanks[2].getTankType(), worldObj, xCoord + 3, yCoord, zCoord, Library.POS_X);
		this.trySubscribe(this.tanks[2].getTankType(), worldObj, xCoord - 3, yCoord, zCoord, Library.NEG_X);
		this.trySubscribe(this.tanks[2].getTankType(), worldObj, xCoord, yCoord, zCoord + 3, Library.POS_Z);
		this.trySubscribe(this.tanks[2].getTankType(), worldObj, xCoord, yCoord, zCoord - 3, Library.NEG_Z);
	}

	@Override
	public void sendFluidToAll(FluidTank tank, TileEntity te) {
		this.sendFluid(this.tanks[1], worldObj, xCoord + 3, yCoord, zCoord, Library.POS_X);
		this.sendFluid(this.tanks[1], worldObj, xCoord - 3, yCoord, zCoord, Library.NEG_X);
		this.sendFluid(this.tanks[1], worldObj, xCoord, yCoord, zCoord + 3, Library.POS_Z);
		this.sendFluid(this.tanks[1], worldObj, xCoord, yCoord, zCoord - 3, Library.NEG_Z);
	}

	AxisAlignedBB bb = null;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {

		if(bb == null) {
			bb = AxisAlignedBB.getBoundingBox(
					xCoord - 2,
					yCoord,
					zCoord - 2,
					xCoord + 3,
					yCoord + 20,
					zCoord + 3
					);
		}

		return bb;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}
}
