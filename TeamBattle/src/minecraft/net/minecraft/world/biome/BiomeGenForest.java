package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

public class BiomeGenForest extends BiomeGenBase {
	protected static final WorldGenForest field_150629_aC = new WorldGenForest(
			false, true);
	protected static final WorldGenForest field_150630_aD = new WorldGenForest(
			false, false);
	protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(
			false);
	private final int field_150632_aF;

	public BiomeGenForest(int p_i45377_1_, int p_i45377_2_) {
		super(p_i45377_1_);
		field_150632_aF = p_i45377_2_;
		theBiomeDecorator.treesPerChunk = 10;
		theBiomeDecorator.grassPerChunk = 2;

		if (field_150632_aF == 1) {
			theBiomeDecorator.treesPerChunk = 6;
			theBiomeDecorator.flowersPerChunk = 100;
			theBiomeDecorator.grassPerChunk = 1;
		}

		func_76733_a(5159473);
		setTemperatureRainfall(0.7F, 0.8F);

		if (field_150632_aF == 2) {
			field_150609_ah = 353825;
			color = 3175492;
			setTemperatureRainfall(0.6F, 0.6F);
		}

		if (field_150632_aF == 0) {
			spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
					EntityWolf.class, 5, 4, 4));
		}

		if (field_150632_aF == 3) {
			theBiomeDecorator.treesPerChunk = -999;
		}
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		int var5;
		int var6;
		int var7;
		int var8;
		int var9;

		if (field_150632_aF == 3) {
			for (var5 = 0; var5 < 4; ++var5) {
				for (var6 = 0; var6 < 4; ++var6) {
					var7 = p_76728_3_ + var5 * 4 + 1 + 8
							+ p_76728_2_.nextInt(3);
					var8 = p_76728_4_ + var6 * 4 + 1 + 8
							+ p_76728_2_.nextInt(3);
					var9 = p_76728_1_.getHeightValue(var7, var8);

					if (p_76728_2_.nextInt(20) == 0) {
						final WorldGenBigMushroom var10 = new WorldGenBigMushroom();
						var10.generate(p_76728_1_, p_76728_2_, var7, var9, var8);
					} else {
						final WorldGenAbstractTree var12 = func_150567_a(p_76728_2_);
						var12.setScale(1.0D, 1.0D, 1.0D);

						if (var12.generate(p_76728_1_, p_76728_2_, var7, var9,
								var8)) {
							var12.func_150524_b(p_76728_1_, p_76728_2_, var7,
									var9, var8);
						}
					}
				}
			}
		}

		var5 = p_76728_2_.nextInt(5) - 3;

		if (field_150632_aF == 1) {
			var5 += 2;
		}

		var6 = 0;

		while (var6 < var5) {
			var7 = p_76728_2_.nextInt(3);

			if (var7 == 0) {
				field_150610_ae.func_150548_a(1);
			} else if (var7 == 1) {
				field_150610_ae.func_150548_a(4);
			} else if (var7 == 2) {
				field_150610_ae.func_150548_a(5);
			}

			var8 = 0;

			while (true) {
				if (var8 < 5) {
					var9 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
					final int var13 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
					final int var11 = p_76728_2_.nextInt(p_76728_1_
							.getHeightValue(var9, var13) + 32);

					if (!field_150610_ae.generate(p_76728_1_, p_76728_2_, var9,
							var11, var13)) {
						++var8;
						continue;
					}
				}

				++var6;
				break;
			}
		}

		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
	}

	@Override
	protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
		if (field_150632_aF == 2) {
			field_150609_ah = 353825;
			color = p_150557_1_;

			if (p_150557_2_) {
				field_150609_ah = (field_150609_ah & 16711422) >> 1;
			}

			return this;
		} else
			return super.func_150557_a(p_150557_1_, p_150557_2_);
	}

	@Override
	protected BiomeGenBase func_150566_k() {
		if (biomeID == BiomeGenBase.forest.biomeID) {
			final BiomeGenForest var1 = new BiomeGenForest(biomeID + 128, 1);
			var1.func_150570_a(new BiomeGenBase.Height(minHeight,
					maxHeight + 0.2F));
			var1.setBiomeName("Flower Forest");
			var1.func_150557_a(6976549, true);
			var1.func_76733_a(8233509);
			return var1;
		} else
			return biomeID != BiomeGenBase.field_150583_P.biomeID
					&& biomeID != BiomeGenBase.field_150582_Q.biomeID ? new BiomeGenMutated(
					biomeID + 128, this) {

				@Override
				public void decorate(World p_76728_1_, Random p_76728_2_,
						int p_76728_3_, int p_76728_4_) {
					field_150611_aD.decorate(p_76728_1_, p_76728_2_,
							p_76728_3_, p_76728_4_);
				}
			} : new BiomeGenMutated(biomeID + 128, this) {

				@Override
				public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
					return p_150567_1_.nextBoolean() ? BiomeGenForest.field_150629_aC
							: BiomeGenForest.field_150630_aD;
				}
			};
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return field_150632_aF == 3 && p_150567_1_.nextInt(3) > 0 ? field_150631_aE
				: field_150632_aF != 2 && p_150567_1_.nextInt(5) != 0 ? worldGeneratorTrees
						: field_150630_aD;
	}

	@Override
	public String func_150572_a(Random p_150572_1_, int p_150572_2_,
			int p_150572_3_, int p_150572_4_) {
		if (field_150632_aF == 1) {
			final double var5 = MathHelper.clamp_double(
					(1.0D + field_150606_ad.func_151601_a(p_150572_2_ / 48.0D,
							p_150572_4_ / 48.0D)) / 2.0D, 0.0D, 0.9999D);
			int var7 = (int) (var5 * BlockFlower.field_149859_a.length);

			if (var7 == 1) {
				var7 = 0;
			}

			return BlockFlower.field_149859_a[var7];
		} else
			return super.func_150572_a(p_150572_1_, p_150572_2_, p_150572_3_,
					p_150572_4_);
	}

	/**
	 * Provides the basic grass color based on the biome temperature and
	 * rainfall
	 */
	@Override
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_,
			int p_150558_3_) {
		final int var4 = super.getBiomeGrassColor(p_150558_1_, p_150558_2_,
				p_150558_3_);
		return field_150632_aF == 3 ? (var4 & 16711422) + 2634762 >> 1 : var4;
	}
}
