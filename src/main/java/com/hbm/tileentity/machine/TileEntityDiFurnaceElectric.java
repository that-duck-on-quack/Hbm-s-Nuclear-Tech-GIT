package com.hbm.tileentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.tile.IInfoProviderEC;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.MachineDiFurnaceElectric;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.container.ContainerDiFurnaceElectric;
import com.hbm.inventory.gui.GUIDiFurnaceElectric;
import com.hbm.inventory.recipes.BlastFurnaceRecipes;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.lib.Library;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.IUpgradeInfoProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.CompatEnergyControl;
import com.hbm.util.i18n.I18nUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.List;

/**
 * Electric Blast Furnace acting as an intermediary between the normal one and the RTGBF.
 * {@link GUIDiFurnaceElectric}
 * {@link ContainerDiFurnaceElectric}
 * {@link MachineDiFurnaceElectric}
 * @author Jack Andersen
 */
public class TileEntityDiFurnaceElectric extends TileEntityMachineBase implements IEnergyReceiverMK2, IUpgradeInfoProvider, IGUIProvider, IInfoProviderEC {

	public static final long maxPower = 1000000;
	public static final int consumption = 1000;

	public static int progressRequiredBase = 100;
	public long power;
	public int progress;
	public boolean wasRunning = false;
	public int progressRequired;

	private static final int BATTERY_SLOT = 0;
	private static final int INPUT_1_SLOT = 1;
	private static final int INPUT_2_SLOT = 2;
	private static final int OUTPUT_SLOT = 3;
	private static final int UPGRADE_SLOT = 4;

	public UpgradeManagerNT upgradeManager = new UpgradeManagerNT();

	private static final int[] slots_io = new int[] {0, 1, 2, 3, 4};

	public TileEntityDiFurnaceElectric() {
		super(5);
	}

	@Override
	public String getName() {
		return "container.diFurnaceElectric";
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		if(i == OUTPUT_SLOT || i == UPGRADE_SLOT){
			return false;
		}
		if(i == BATTERY_SLOT){
			return stack.getItem() instanceof IBatteryItem;
		}
		//Makes sure that duplicate items cannot be inserted into both slots.
		if(i == INPUT_1_SLOT){
			if(slots[INPUT_2_SLOT] != null && slots[INPUT_2_SLOT].isItemEqual(stack)) return false;
		}
		else if(i == INPUT_2_SLOT){
			if(slots[INPUT_1_SLOT] != null && slots[INPUT_1_SLOT].isItemEqual(stack)) return false;
		}
		return true;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		progress = nbt.getInteger("progress");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setInteger("progress", progress);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return slots_io;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == OUTPUT_SLOT) return false;
		return this.isItemValidForSlot(i, itemStack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return i == OUTPUT_SLOT || i == BATTERY_SLOT;
	}

	public boolean canProcess() {
		if(slots[INPUT_1_SLOT] == null || slots[INPUT_2_SLOT] == null) return false;
		if(this.power < consumption) return false;

		ItemStack output = BlastFurnaceRecipes.getOutput(slots[INPUT_1_SLOT], slots[INPUT_2_SLOT]);
		if(output == null) return false;
		if(slots[OUTPUT_SLOT] == null) return true;
		if(!slots[OUTPUT_SLOT].isItemEqual(output)) return false;

		if(slots[OUTPUT_SLOT].stackSize + output.stackSize <= slots[OUTPUT_SLOT].getMaxStackSize()) {
			return true;
		}

		return false;
	}

	private void processItem() {
		ItemStack itemStack = BlastFurnaceRecipes.getOutput(slots[INPUT_1_SLOT], slots[INPUT_2_SLOT]);

		if(slots[OUTPUT_SLOT] == null) {
			slots[OUTPUT_SLOT] = itemStack.copy();
		} else if(slots[OUTPUT_SLOT].isItemEqual(itemStack)) {
			slots[OUTPUT_SLOT].stackSize += itemStack.stackSize;
		}
		this.decrStackSize(INPUT_1_SLOT,1);
		this.decrStackSize(INPUT_2_SLOT,1);
	}

	private void updateConnections() {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			this.trySubscribe(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir);
	}

	public int getProcessingPower(int speed){

		return (int)((consumption*(speed > 1 ? speed*1.2 : 1))*(1-upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.POWER)*0.3F));
	}

	public int getProgressRequired(){
		return (int)(
			(TileEntityDiFurnaceElectric.progressRequiredBase *(1F - (upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.SPEED)*0.25F)))
			* (1F + (upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.POWER) * 0.1))
		);
	}

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			this.updateConnections();
			power = Library.chargeTEFromItems(slots, 0, power, maxPower);
			upgradeManager.checkSlots(this, slots, UPGRADE_SLOT,UPGRADE_SLOT);
			progressRequired = getProgressRequired();
			if(canProcess()){
				if(progress == 0){
					MachineDiFurnaceElectric.updateBlockState(true, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
				if(progress >= progressRequired){
					processItem();
					progress = 0;
					if(!canProcess()) {
						MachineDiFurnaceElectric.updateBlockState(false, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
					}
				} else {
					power-=getProcessingPower((1 + upgradeManager.getLevel(ItemMachineUpgrade.UpgradeType.SPEED)));
					progress++;
				}
				wasRunning = true;
			}
			else{
				if(progress > 0){
					MachineDiFurnaceElectric.updateBlockState(false, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
				progress = 0;
			}
			markDirty();
			networkPackNT(15);
		}
	}

	@Override
	public void serialize(ByteBuf buf) {
		buf.writeLong(this.power);
		buf.writeShort(this.progress);
		buf.writeShort(this.progressRequired);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		this.power = buf.readLong();
		this.progress = buf.readShort();
		this.progressRequired = buf.readShort();
	}

	@Override
	public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerDiFurnaceElectric(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GUIDiFurnaceElectric(player.inventory, this);
	}

	public void provideExtraInfo(NBTTagCompound data) {
		data.setInteger(CompatEnergyControl.I_PROGRESS, this.progress);
	}

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public void setPower(long power) {
		this.power=power;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}

	public int getProgressScaled(int i) {
		return (int)((progress / (float)progressRequired)*i);
	}

	public long getPowerScaled(long i) {
		return (power * i) / maxPower;
	}

	@Override
	public boolean canProvideInfo(ItemMachineUpgrade.UpgradeType type, int level, boolean extendedInfo) {
		return type == ItemMachineUpgrade.UpgradeType.SPEED || type == ItemMachineUpgrade.UpgradeType.POWER;
	}

	@Override
	public void provideInfo(ItemMachineUpgrade.UpgradeType type, int level, List<String> info, boolean extendedInfo) {
		info.add(IUpgradeInfoProvider.getStandardLabel(ModBlocks.machine_electric_furnace_off));
		if(type == ItemMachineUpgrade.UpgradeType.SPEED) {
			info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_DELAY, "-" + (level * 25) + "%"));
			info.add(EnumChatFormatting.RED + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "+" + (level * 120) + "%"));
		}
		if(type == ItemMachineUpgrade.UpgradeType.POWER) {
			info.add(EnumChatFormatting.GREEN + I18nUtil.resolveKey(this.KEY_CONSUMPTION, "-" + (level * 30) + "%"));
			info.add(EnumChatFormatting.RED + I18nUtil.resolveKey(this.KEY_DELAY, "+" + (level * 10) + "%"));
		}
	}

	@Override
	public HashMap<ItemMachineUpgrade.UpgradeType, Integer> getValidUpgrades() {
		HashMap<ItemMachineUpgrade.UpgradeType, Integer> upgrades = new HashMap<>();
		upgrades.put(ItemMachineUpgrade.UpgradeType.SPEED, 3);
		upgrades.put(ItemMachineUpgrade.UpgradeType.POWER, 3);
		return upgrades;
	}
}
