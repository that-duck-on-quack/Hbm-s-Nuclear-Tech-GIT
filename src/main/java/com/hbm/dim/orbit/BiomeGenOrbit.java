package com.hbm.dim.orbit;

import java.util.Random;

import com.hbm.config.SpaceConfig;
import com.hbm.dim.BiomeGenBaseCelestial;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenOrbit extends BiomeGenBaseCelestial {

	public static final BiomeGenBase biome = new BiomeGenOrbit(SpaceConfig.orbitBiome);

	public BiomeGenOrbit(int id) {
		super(id);
		this.setBiomeName("Space");
		this.setDisableRain();
	}

	@Override
	public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] meta, int x, int z, double noise) {
		// NOTHING
	}

	public void decorate(World world, Random rand, int x, int z) {
		// EVEN LESS
	}

}
