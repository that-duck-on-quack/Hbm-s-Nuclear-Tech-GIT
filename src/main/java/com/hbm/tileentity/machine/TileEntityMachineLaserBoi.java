package com.hbm.tileentity.machine;

import com.hbm.config.VersatileConfig;
import com.hbm.inventory.OreDictManager;

import com.hbm.inventory.container.ContainerMachineLaserBoi;
import com.hbm.inventory.gui.GUIMachineLaserBoi;
import com.hbm.inventory.recipes.MachineRecipes;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemCircuit;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.items.machine.ItemCircuit.EnumCircuitType;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static com.hbm.inventory.OreDictManager.SI;

public class TileEntityMachineLaserBoi extends TileEntityMachineBase implements IEnergyReceiverMK2, IGUIProvider {

	public long power = 0;
	public int process = 0;
	public static final long maxPower = 100000;
	public static final int baseprocess = 100;
	public static final int processSpeed = 60;

	private AudioWrapper audio;

	private static final int[] slots_io = new int[] { 0, 1, 2, 3 };

	public TileEntityMachineLaserBoi() {
		super(4);
	}

	@Override
	public String getName() {
		return "Laser-Engraver";
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		switch (i) {
			case 0:
				if (MachineRecipes.mODE(stack, OreDictManager.SI.billet()))
					return true;
				break;
			case 2:
				if (stack.getItem() == ModItems.laser_crystal_co2 || stack.getItem() == ModItems.laser_crystal_co2 || stack.getItem() == ModItems.laser_crystal_iron || stack.getItem() == ModItems.laser_crystal_digamma || stack.getItem() == ModItems.laser_crystal_dnt || stack.getItem() == ModItems.laser_crystal_cmb)
					return true;
				break;
			case 3:
				if (stack.getItem() instanceof IBatteryItem)
					return true;
				break;
		}
		return false;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		power = nbt.getLong("power");
		process = nbt.getInteger("process");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setInteger("process", process);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return slots_io;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack stack, int j) {

		if(stack.getItem() == ModItems.laser_crystal_co2 || stack.getItem() == ModItems.laser_crystal_bismuth || stack.getItem() == ModItems.laser_crystal_iron || stack.getItem() == ModItems.laser_crystal_digamma || stack.getItem() == ModItems.laser_crystal_dnt || stack.getItem() == ModItems.laser_crystal_cmb) return false;

		if(i == 1) {
			return true;
		}

		if(i == 3) {
			if(stack.getItem() instanceof IBatteryItem && ((IBatteryItem) stack.getItem()).getCharge(stack) == 0)
				return true;
		}

		return false;
	}


	public long getPowerScaled(long i) {
		return (power * i) / maxPower;
	}

	public int getProgressScaled(int i) {
		return (process * i) / processSpeed;
	}

	public boolean canProcess() {
		if ( slots[0] != null && slots[2] != null&& (MachineRecipes.mODE(slots[0], OreDictManager.SI.billet()) || MachineRecipes.mODE(slots[0], OreDictManager.GAAS.billet()) ) && slots[2] != null
			&& (slots[2].getItem() == ModItems.laser_crystal_bismuth || slots[2].getItem() == ModItems.laser_crystal_co2 || slots[2].getItem() == ModItems.laser_crystal_cmb|| slots[2].getItem() == ModItems.laser_crystal_digamma|| slots[2].getItem() == ModItems.laser_crystal_dnt|| slots[2].getItem() == ModItems.laser_crystal_iron)
			&& (slots[1] == null) && (power > 0) ) {
			return true;
		}
		return false;
	}




	public boolean isProcessing() {
		return process > 0;
	}

	public void process() {
		process++;
		power=power-1000;

		if (process >= processSpeed) {

			process = 0;

			if (slots[1] == null && slots[0].getItem() == ModItems.billet_silicon) {
				slots[1] = OreDictManager.DictFrame.fromOne(ModItems.circuit, EnumCircuitType.SILICON);
			} else if (slots[1] != null && slots[0].getItem() == ModItems.billet_silicon) {
				slots[1].stackSize++;
			}
			if (slots[1] == null && slots[0].getItem() == ModItems.billet_gaas) {
				slots[1] = OreDictManager.DictFrame.fromOne(ModItems.circuit, EnumCircuitType.GAAS);
			}  else if (slots[1] != null && slots[0].getItem() == ModItems.billet_gaas) {
				slots[1].stackSize++;
			}

			slots[0].stackSize--;
			if (slots[0].stackSize <= 0) {
				slots[0] = null;
			}

		}
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			this.updateConnections();

			power = Library.chargeTEFromItems(slots, 3, power, maxPower);

			if(canProcess()) {
				process();
			} else {
				process = 0;
			}

			this.networkPackNT(50);

		} else {

			if(process > 0) {

				if(audio == null) {
					audio = createAudioLoop();
					audio.startSound();
				} else if(!audio.isPlaying()) {
					audio = rebootAudio(audio);
				}
				audio.updateVolume(getVolume(1F));
			} else {

				if(audio != null) {
					audio.stopSound();
					audio = null;
				}
			}
		}
	}

	@Override
	public void serialize(ByteBuf buf) {
		super.serialize(buf);
		buf.writeLong(this.power);
		buf.writeInt(this.process);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		super.deserialize(buf);
		this.power = buf.readLong();
		this.process = buf.readInt();
	}

	@Override
	public AudioWrapper createAudioLoop() {
		return MainRegistry.proxy.getLoopedSound("hbm:weapon.tauChargeLoop", xCoord, yCoord, zCoord, 1.0F, 10F, 1.0F);
	}

	private void updateConnections() {

		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			this.trySubscribe(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir);
	}

	@Override
	public void onChunkUnload() {

		if(audio != null) {
			audio.stopSound();
			audio = null;
		}
	}

	@Override
	public void invalidate() {

		super.invalidate();

		if(audio != null) {
			audio.stopSound();
			audio = null;
		}
	}

	@Override
	public void setPower(long i) {
		power = i;
	}

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}

	@Override
	public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerMachineLaserBoi(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GUIMachineLaserBoi(player.inventory, this);
	}
}
