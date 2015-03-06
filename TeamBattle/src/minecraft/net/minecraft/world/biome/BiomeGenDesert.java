package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

public class BiomeGenDesert extends BiomeGenBase {

	public BiomeGenDesert(int p_i1977_1_) {
		super(p_i1977_1_);
		spawnableCreatureList.clear();
		topBlock = Blocks.sand;
		fillerBlock = Blocks.sand;
		theBiomeDecorator.treesPerChunk = -999;
		theBiomeDecorator.deadBushPerChunk = 2;
		theBiomeDecorator.reedsPerChunk = 50;
		theBiomeDecorator.cactiPerChunk = 10;
		spawnableCreatureList.clear();
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);

		if (p_76728_2_.nextInt(1000) == 0) {
			final int var5 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
			final int var6 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
			final WorldGenDesertWells var7 = new WorldGenDesertWells();
			var7.generate(p_76728_1_, p_76728_2_, var5,
					p_76728_1_.getHeightValue(var5, var6) + 1, var6);
		}
	}
}
