package com.hbm.dim.moho.biome;

import com.hbm.blocks.ModBlocks;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenMohoCrag extends BiomeGenBaseMoho {

	public BiomeGenMohoCrag(int id) {
		super(id);
		this.setBiomeName("Moho Crag");

		this.setHeight(new BiomeGenBase.Height(0.275F, 0.666F));

		this.topBlock = ModBlocks.moho_regolith;
		this.fillerBlock = ModBlocks.moho_regolith; // thiccer regolith due to uhhhhhh...................
	}

}