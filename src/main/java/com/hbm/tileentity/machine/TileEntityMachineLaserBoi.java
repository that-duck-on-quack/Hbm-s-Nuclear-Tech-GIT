package com.hbm.tileentity.machine;

import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.container.ContainerMachineLaserBoi;
import com.hbm.inventory.gui.GUIMachineLaserBoi;
import com.hbm.inventory.recipes.LaserBoiRecipes;
import com.hbm.items.ModItems;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityMachineLaserBoi extends TileEntityMachineBase implements IEnergyReceiverMK2, IGUIProvider {

	public long power = 0;
	public int process = 0;
	public static final long maxPower = 100000;
	public static final int baseprocess = 100;
	public static final int processSpeed = 60;
	public static List recipeList = new ArrayList();
	public static HashMap<Item, ItemStack> itemList = new HashMap<>();
	public static HashMap<Item, Integer> crystalList = new HashMap<>();

	private AudioWrapper audio;

	private static final int[] slots_io = new int[] { 0, 1, 2, 3 };

	public TileEntityMachineLaserBoi() {
		super(4);
		loadRecipes();
	}

	@Override
	public String getName() {
		return "Laser-Engraver";
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		switch (i) {
			case 0:
				if (itemList.containsKey(stack.getItem()))
					return true;
				break;
			case 2:
				if (crystalList.containsKey(stack.getItem()))
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

		if(stack.getItem() != null && crystalList.containsKey(stack.getItem())) return false;

		if(i == 1) {
			return true;
		}

		if(i == 3) {
			return stack.getItem() instanceof IBatteryItem && ((IBatteryItem) stack.getItem()).getCharge(stack) == 0;
		}

		return false;
	}

	public long getPowerScaled(long i) {
		return (power * i) / maxPower;
	}

	public int getProgressScaled(int i) {
		return (process * i) / (processSpeed/(slots[2] != null ? crystalList.get(slots[2].getItem()) : 1));
	}

	public boolean canProcess() {
		if(slots[0] != null && slots[2] != null && power > 0){ //Base checks. Nested ifs are nasty but i'm prioritizing readability.
			if(slots[1] == null){
				return itemList.containsKey(slots[0].getItem()) && crystalList.containsKey(slots[2].getItem());
			}else {
				return itemList.containsKey(slots[0].getItem()) && crystalList.containsKey(slots[2].getItem()) && slots[1] == itemList.get(slots[0].getItem()) && slots[1].stackSize < 64;
			}
		}
		return false;
	}

	public boolean isProcessing() {
		return process > 0;
	}

	public void loadRecipes(){
		recipeList = LaserBoiRecipes.getRecipes();
		for(Object recipe : recipeList){
			Map.Entry<RecipesCommon.ComparableStack, LaserBoiRecipes.engraverRecipe> entry = (Map.Entry<RecipesCommon.ComparableStack, LaserBoiRecipes.engraverRecipe>) recipe;
			itemList.put(entry.getValue().input.toStack().getItem(), entry.getKey().toStack());
		}
		crystalList.put(ModItems.laser_crystal_bismuth, 2);
		crystalList.put(ModItems.laser_crystal_iron, 6);
		crystalList.put(ModItems.laser_crystal_co2, 1);
		crystalList.put(ModItems.laser_crystal_cmb, 4);
		crystalList.put(ModItems.laser_crystal_digamma, 58);
		crystalList.put(ModItems.laser_crystal_dnt, 2);
	}

	public void process() {
		process++;
		power=power-750;

		if (process >= processSpeed/crystalList.get(slots[2].getItem())) {
			process = 0;

			if (slots[1] == null && itemList.containsKey(slots[0].getItem())) {
				slots[1] = itemList.get(slots[0].getItem()).copy();
			} else if (slots[1] != null && slots[1].getItem() == itemList.get(slots[0].getItem()).getItem()) {
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
