package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeGenSnow extends BiomeGenBase {
	private final boolean field_150615_aC;
	private final WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
	private final WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);

	public BiomeGenSnow(int p_i45378_1_, boolean p_i45378_2_) {
		super(p_i45378_1_);
		field_150615_aC = p_i45378_2_;

		if (p_i45378_2_) {
			topBlock = Blocks.snow;
		}

		spawnableCreatureList.clear();
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		if (field_150615_aC) {
			int var5;
			int var6;
			int var7;

			for (var5 = 0; var5 < 3; ++var5) {
				var6 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				var7 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				field_150616_aD.generate(p_76728_1_, p_76728_2_, var6,
						p_76728_1_.getHeightValue(var6, var7), var7);
			}

			for (var5 = 0; var5 < 2; ++var5) {
				var6 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
				var7 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
				field_150617_aE.generate(p_76728_1_, p_76728_2_, var6,
						p_76728_1_.getHeightValue(var6, var7), var7);
			}
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	protected BiomeGenBase func_150566_k() {
		final BiomeGenBase var1 = new BiomeGenSnow(biomeID + 128, true)
				.func_150557_a(13828095, true)
				.setBiomeName(biomeName + " Spikes")
				.setEnableSnow()
				.setTemperatureRainfall(0.0F, 0.5F)
				.func_150570_a(
						new BiomeGenBase.Height(minHeight + 0.1F,
								maxHeight + 0.1F));
		var1.minHeight = minHeight + 0.3F;
		var1.maxHeight = maxHeight + 0.4F;
		return var1;
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return new WorldGenTaiga2(false);
	}
}
