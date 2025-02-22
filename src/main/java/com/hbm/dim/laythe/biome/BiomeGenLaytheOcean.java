package com.hbm.dim.laythe.biome;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenLaytheOcean extends BiomeGenBaseLaythe {

	public BiomeGenLaytheOcean(int id) {
		super(id);
		this.setBiomeName("Sagan Sea");

		this.setHeight(new BiomeGenBase.Height(-1.8F, 0.24F));
		this.setTemperatureRainfall(0.2F, 0.2F);
	}
}
