package com.hbm.handler;

import com.hbm.config.VersatileConfig;
import com.hbm.inventory.RecipesCommon;
import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Maintains a list of decay results for the {@link com.hbm.tileentity.machine.TileEntityStorageDrum} for item conversions.
 * Does not include support for the waste items.
 * @author Jack Andersen
 */
public class DecayConversions {
	private static final Random rand = new Random();
	private static final Map<Item,DecayStats> map = new IdentityHashMap<>();

	public static @Nullable ItemStack tryConvert(ItemStack stack) {
		DecayStats stats = map.get(stack.getItem());
		if(stats != null) {
			return stats.tryDecay();
		}
		return null;
	}

	public static void init(){
		// Base NTM
		map.put(ModItems.ingot_au198,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 20F),new DecayResult(ModItems.ingot_mercury,1)));
		map.put(ModItems.nugget_au198,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.nugget_mercury,1)));
		map.put(ModItems.ingot_pb209,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 10F),new DecayResult(ModItems.ingot_bismuth,1)));
		map.put(ModItems.nugget_pb209,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 50F),new DecayResult(ModItems.nugget_bismuth,1)));
		map.put(ModItems.powder_sr90,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 10F),new DecayResult(ModItems.powder_zirconium,1)));
		map.put(ModItems.nugget_sr90,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 50F),new DecayResult(ModItems.nugget_zirconium,1)));
		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_caesium, https://en.wikipedia.org/wiki/Isotopes_of_barium
		map.put(ModItems.powder_cs137,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.powder_xe135,1)));
		map.put(ModItems.powder_cs137_tiny,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 500F),new DecayResult(ModItems.powder_lanthanium_tiny,1)));
		// ac227->ra223->po215->pb211->bi211->tl207->lead
		map.put(ModItems.ingot_actinium,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.ingot_lead,1)));
		map.put(ModItems.nugget_actinium,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 500F),new DecayResult(ModItems.nugget_lead,1)));
		// at209->pb205 (wikipedia), technically not stable but HL is 1e7 years so it might as well be.
		map.put(ModItems.powder_at209,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.powder_lead,1)));
		// at210->po210 (wikipedia), reverse of the cyclotron recipe
		map.put(ModItems.powder_astatine,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 500F),new DecayResult(ModItems.powder_polonium,1)));
		// po210->pb206 (wikipedia)
		map.put(ModItems.ingot_polonium,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.ingot_lead,1)));
		map.put(ModItems.nugget_polonium,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 500F),new DecayResult(ModItems.nugget_lead,1)));
		map.put(ModItems.powder_polonium,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.powder_lead,1)));
		// co60->ni60 (wikipedia)
		map.put(ModItems.ingot_co60,new DecayStats(1F/(VersatileConfig.getLongDecayChance() / 50F),new DecayResult(ModItems.ingot_nickel,1)));
		map.put(ModItems.nugget_co60,new DecayStats(1F/(VersatileConfig.getLongDecayChance() / 400F),new DecayResult(ModItems.nugget_nickel,1)));
		map.put(ModItems.powder_co60,new DecayStats(1F/(VersatileConfig.getLongDecayChance() / 50F),new DecayResult(ModItems.powder_nickel,1)));

		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_neptunium/, and https://en.wikipedia.org/wiki/Isotopes_of_protactinium
		map.put(ModItems.ingot_neptunium,new DecayStats(1F/(VersatileConfig.getLongDecayChance()*20F),new DecayResult(ModItems.ingot_u233,1)));
		map.put(ModItems.nugget_neptunium,new DecayStats(1F/(VersatileConfig.getLongDecayChance()*20F/9F),new DecayResult(ModItems.nugget_u233,1)));

		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_plutonium/, https://en.wikipedia.org/wiki/Isotopes_of_uranium, and https://en.wikipedia.org/wiki/Isotopes_of_thorium
		map.put(ModItems.ingot_pu238,new DecayStats(1F/(VersatileConfig.getLongDecayChance()),new DecayResult(ModItems.ingot_ra226,1)));
		map.put(ModItems.nugget_pu238,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/9F),new DecayResult(ModItems.nugget_ra226,1)));
		map.put(ModItems.ingot_pu239,new DecayStats(1F/(VersatileConfig.getLongDecayChance()),new DecayResult(ModItems.ingot_u235,1)));
		map.put(ModItems.nugget_pu239,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/9F),new DecayResult(ModItems.nugget_u235,1)));
		map.put(ModItems.ingot_pu240,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/5F),new DecayResult(ModItems.ingot_th232,1)));
		map.put(ModItems.nugget_pu240,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/45F),new DecayResult(ModItems.nugget_th232,1)));
		map.put(ModItems.ingot_pu241,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/10F),new DecayResult(ModItems.ingot_am241,1)));
		map.put(ModItems.nugget_pu241,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/90F),new DecayResult(ModItems.ingot_am241,1)));
		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_americium and https://en.wikipedia.org/wiki/Isotopes_of_plutonium
		map.put(ModItems.ingot_am241,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/8F),new DecayResult(ModItems.ingot_neptunium,1)));
		map.put(ModItems.nugget_am241,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/72F),new DecayResult(ModItems.nugget_neptunium,1)));
		map.put(ModItems.ingot_am242,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/18F),new DecayResult(ModItems.ingot_cm242,827),new DecayResult(ModItems.ingot_u238,173)));
		map.put(ModItems.nugget_am242,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/100F),new DecayResult(ModItems.nugget_cm242,827),new DecayResult(ModItems.nugget_u238,173)));
		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_curium, https://en.wikipedia.org/wiki/Isotopes_of_plutonium, https://en.wikipedia.org/wiki/Isotopes_of_americium, and https://en.wikipedia.org/wiki/Isotopes_of_neptunium
		map.put(ModItems.ingot_cm242,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/20F),new DecayResult(ModItems.ingot_pu238,1)));
		map.put(ModItems.nugget_cm242,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/120F),new DecayResult(ModItems.nugget_pu238,1)));
		map.put(ModItems.ingot_cm243,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/2F),new DecayResult(ModItems.ingot_pu239,1)));
		map.put(ModItems.nugget_cm243,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/18F),new DecayResult(ModItems.nugget_pu239,1)));
		map.put(ModItems.ingot_cm244,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/3F),new DecayResult(ModItems.ingot_pu240,1)));
		map.put(ModItems.nugget_cm244,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/27F),new DecayResult(ModItems.nugget_pu240,1)));
		map.put(ModItems.ingot_cm245,new DecayStats(1F/(VersatileConfig.getLongDecayChance()),new DecayResult(ModItems.ingot_pu241,1)));
		map.put(ModItems.nugget_cm245,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/9F),new DecayResult(ModItems.nugget_pu241,1)));
		map.put(ModItems.ingot_cm246,new DecayStats(1F/(VersatileConfig.getLongDecayChance()),new DecayResult(ModItems.ingot_u238,1)));
		map.put(ModItems.nugget_cm246,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/9F),new DecayResult(ModItems.nugget_u238,1)));
		map.put(ModItems.ingot_cm247,new DecayStats(1F/(VersatileConfig.getLongDecayChance()*2),new DecayResult(ModItems.ingot_pu239,1)));
		map.put(ModItems.nugget_cm247,new DecayStats(1F/(VersatileConfig.getLongDecayChance()*2/9F),new DecayResult(ModItems.nugget_pu239,1)));
		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_berkelium, https://en.wikipedia.org/wiki/Isotopes_of_plutonium, https://en.wikipedia.org/wiki/Isotopes_of_americium, and https://en.wikipedia.org/wiki/Isotopes_of_neptunium
		map.put(ModItems.ingot_bk247,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/5F),new DecayResult(ModItems.ingot_pu239,1)));
		map.put(ModItems.nugget_bk247,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/45F),new DecayResult(ModItems.nugget_pu239,1)));
		// Based off of https://en.wikipedia.org/wiki/Isotopes_of_californium, https://en.wikipedia.org/wiki/Isotopes_of_curium, https://en.wikipedia.org/wiki/Isotopes_of_uranium, https://en.wikipedia.org/wiki/Isotopes_of_plutonium,
		map.put(ModItems.ingot_cf251,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/2F),new DecayResult(ModItems.ingot_cm247,1)));
		map.put(ModItems.nugget_cf251,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/18F),new DecayResult(ModItems.nugget_cm247,1)));
		map.put(ModItems.ingot_cf252,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/20F),new DecayResult(ModItems.ingot_pu240,1)));
		map.put(ModItems.nugget_cf252,new DecayStats(1F/(VersatileConfig.getLongDecayChance()/180F),new DecayResult(ModItems.nugget_pu240,1)));
		//Based off of https://en.wikipedia.org/wiki/Isotopes_of_einsteinium, https://en.wikipedia.org/wiki/Isotopes_of_berkelium, and https://en.wikipedia.org/wiki/Isotopes_of_californium
		map.put(ModItems.ingot_es253,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/4F),new DecayResult(ModItems.ingot_cm245,1)));
		map.put(ModItems.nugget_es253,new DecayStats(1F/(VersatileConfig.getShortDecayChance()/36F),new DecayResult(ModItems.nugget_cm245,1)));
	}

	public static void addRecipes(HashMap<RecipesCommon.ComparableStack, ItemStack> dest) {
		for (Item item : map.keySet()) {
			int i = 0;
			DecayStats stats = map.get(item);
			for (DecayResult option : stats.getOptions()) {
				dest.put(new DRStack(item,i),option.result.copy());
				i++;
			}
		}
	}

	public static class DRStack extends RecipesCommon.ComparableStack {
		private final int idx;

		public DRStack(Item item, int idx) {
			super(item);
			this.idx = idx;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) return true;
			if (object == null || getClass() != object.getClass()) return false;
			if (!super.equals(object)) return false;
			DRStack drStack = (DRStack) object;
			return idx == drStack.idx;
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), idx);
		}
	}

	public static class DecayStats {
		private List<DecayResult> options = new ArrayList<>();
		private float decayChance;

		public DecayStats(float decayChance, DecayResult... options) {
			this.decayChance = decayChance;
			this.options.addAll(Arrays.asList(options));
		}

		public @Nullable ItemStack tryDecay(){
			if(rand.nextFloat() < decayChance){
				return ((DecayResult)WeightedRandom.getRandomItem(rand,options)).result.copy();
			}
			return null;
		}

		public List<DecayResult> getOptions() {
			return options;
		}
	}

	public static class DecayResult extends WeightedRandom.Item {
		private final ItemStack result;

		public DecayResult(ItemStack result, int weight) {
			super(weight);
			this.result = result;
		}

		public DecayResult(Item result, int weight) {
			this(new ItemStack(result),weight);
		}

		public ItemStack getResult() {
			return result;
		}
	}
}
