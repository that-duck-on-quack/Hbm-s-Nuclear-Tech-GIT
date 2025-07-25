package com.hbm.handler;

import com.hbm.config.VersatileConfig;
import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import javax.annotation.Nullable;
import java.util.*;

public class DecayConversions {
	private static final Random rand = new Random();
	private static final Map<Item,DecayStats> map = new IdentityHashMap<>();

	public static void init(){
		map.put(ModItems.ingot_au198,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 20F),new DecayResult(ModItems.ingot_mercury,1)));
		map.put(ModItems.nugget_au198,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 100F),new DecayResult(ModItems.nugget_mercury,1)));
		map.put(ModItems.ingot_pb209,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 10F),new DecayResult(ModItems.ingot_bismuth,1)));
		map.put(ModItems.nugget_pb209,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 50F),new DecayResult(ModItems.nugget_bismuth,1)));
		map.put(ModItems.powder_sr90,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 10F),new DecayResult(ModItems.powder_zirconium,1)));
		map.put(ModItems.nugget_sr90,new DecayStats(1F/(VersatileConfig.getShortDecayChance() / 50F),new DecayResult(ModItems.nugget_zirconium,1)));
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
				return ((DecayResult)WeightedRandom.getRandomItem(rand,options)).result;
			}
			return null;
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
