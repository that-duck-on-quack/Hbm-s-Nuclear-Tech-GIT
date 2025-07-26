package com.hbm.dim;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeCollisionException extends RuntimeException {

	private static String EXCEPTION_MESSAGE
		= "Biome ID conflict, attempted to register NTM Space biome to ID %d which is already in use by:"
		+ "\nBiome name: %s"
		+ "\nBiome class: %s"
		+ "\nPlease modify hbm.cfg to fix this error. Note that the maximum biome ID is 255, if you run out you MUST install EndlessIDs!";

	public BiomeCollisionException(BiomeGenBase conflictsWith) {
		super(String.format(EXCEPTION_MESSAGE, conflictsWith.biomeID, conflictsWith.biomeName, conflictsWith.getBiomeClass().getName()));
	}

}
