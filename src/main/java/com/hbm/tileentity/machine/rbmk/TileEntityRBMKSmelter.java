package com.hbm.tileentity.machine.rbmk;

import com.github.bsideup.jabel.Desugar;
import com.hbm.entity.projectile.EntityRBMKDebris.DebrisType;
import com.hbm.handler.CompatHandler;
import com.hbm.handler.neutron.NeutronStream;
import com.hbm.handler.neutron.RBMKNeutronHandler;
import com.hbm.inventory.container.ContainerRBMKSmelter;
import com.hbm.inventory.gui.GUIRBMKSmelter;
import com.hbm.inventory.recipes.RBBQRecipes;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKConsole.ColumnType;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Implementation of the {@link com.hbm.tileentity.machine.TileEntityFurnaceSteel} using RBMK heat. It also can smelt entire stacks at once.
 * Block {@link com.hbm.blocks.machine.rbmk.RBMKSmelter}
 * GUI {@link GUIRBMKSmelter}
 * @author Jack Andersen
 */
@Optional.InterfaceList({@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")})
public class TileEntityRBMKSmelter extends TileEntityRBMKSlottedBase implements SimpleComponent, CompatHandler.OCComponent {
	public double progressRequired;
	public double progress;
	/**
	 * Duration is based off of {@link com.hbm.tileentity.machine.TileEntityFurnaceSteel#processTime} divided by 200.
	 * The RBBQ is actually 200 times energy efficient compared to the steel furnace.
	 */
	public static final int progressPerItem = 200;

	public TileEntityRBMKSmelter() {
		super(2);
	}

	@Override
	public String getName() {
		return "container.rbmkSmelter";
	}

	@Override
	public void updateEntity() {

		if(!worldObj.isRemote) {
			process();
		}

		super.updateEntity();
	}

	private void process() {
		if(heat > 20) {
			RecipeInfo recipe = getOutputAndConsumption(slots[0],slots[1]);
			if (recipe != null && recipe.recipesToExecute > 0) {
				progressRequired = progressPerItem*recipe.recipesToExecute;
				if (progress < progressRequired) {
					// Heat is distributed between the input and the rod itself.
					double progressChange = Math.min((heat-20) / (recipe.recipesToExecute+1), progressRequired - progress);
					progress += progressChange;
					double heatChange = Math.min(progressChange * recipe.recipesToExecute, progressRequired - progress);
					// Change heat
					heat -= heatChange;
				} else {
					this.progress = 0;
					this.decrStackSize(0, recipe.recipesToExecute);
					if (slots[1] == null) {
						slots[1] = recipe.output();
					} else {
						slots[1].stackSize += recipe.output().stackSize;
					}
				}
			}
			if(recipe == null){
				progress = 0;
			}
		}
	}

	private @Nullable RecipeInfo getOutputAndConsumption(@Nullable ItemStack input, @Nullable ItemStack outputSlot){
		if(input == null){
			return null;
		}
		ItemStack output = null;
		Map<ItemStack,ItemStack> options = FurnaceRecipes.smelting().getSmeltingList();
		for (ItemStack stack : options.keySet()) {
			if(matches(slots[0],stack)){
				output = options.get(stack);
			}
		}
		if(output == null){
			output = RBBQRecipes.getOutput(input);
			if(output == null) {
				return null;
			}
		}
		// Cannot do recipe if the slot has an incompatible item.
		if(outputSlot != null && (!output.isItemEqual(outputSlot) || outputSlot.getMaxStackSize() == outputSlot.stackSize)){
			return null;
		}
		// If output slot is empty, use maximum size, if not find remaining space.
		// n = number of items able to fit in output.
		int n = outputSlot == null ? output.getMaxStackSize() : output.getMaxStackSize()-output.stackSize;
		// clamp n to the maximum recipes possible. Could be 0.
		n = Math.min(input.stackSize, n/output.stackSize);
		return new RecipeInfo(output,n);
	}

	@Desugar
	private record RecipeInfo(ItemStack output, int recipesToExecute) {
		public ItemStack output(){
			ItemStack out = output.copy();
			out.stackSize*=recipesToExecute;
			return out;
		}
	}

	private boolean matches(ItemStack s1, ItemStack s2)
	{
		return s2.getItem() == s1.getItem() && (s2.getItemDamage() == 32767 || s2.getItemDamage() == s1.getItemDamage());
	}

	@Override
	public void onMelt(int reduce) {

		int count = 4 + worldObj.rand.nextInt(2);

		for(int i = 0; i < count; i++) {
			spawnDebris(DebrisType.BLANK);
		}

		super.onMelt(reduce);
	}

	@Override
	public ColumnType getConsoleType() {
		return ColumnType.SMELTER;
	}

	@Override
	public NBTTagCompound getNBTForConsole() {
		NBTTagCompound data = new NBTTagCompound();
		data.setDouble("progress", this.progress);
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		this.progress = nbt.getDouble("progress");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setDouble("progress", this.progress);
	}

	@Override
	public void serialize(ByteBuf buf) {
		super.serialize(buf);
		buf.writeDouble(this.progress);
		buf.writeDouble(this.progressRequired);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		super.deserialize(buf);
		this.progress = buf.readDouble();
		this.progressRequired = buf.readDouble();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null && i == 0;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return i == 1;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[] {0, 1};
	}

	//do some opencomputers stuff
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName() {
		return "rbmk_smelter";
	}

	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getProgress(Context context, Arguments args) {
		return new Object[] {progress};
	}

	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getCoordinates(Context context, Arguments args) {
		return new Object[] {xCoord, yCoord, zCoord};
	}

	@Callback(direct = true)
	@Optional.Method(modid = "OpenComputers")
	public Object[] getInfo(Context context, Arguments args) {
		return new Object[] {progress, xCoord, yCoord, zCoord};
	}

	@Override
	public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerRBMKSmelter(player.inventory, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GUIRBMKSmelter(player.inventory, this);
	}
}
