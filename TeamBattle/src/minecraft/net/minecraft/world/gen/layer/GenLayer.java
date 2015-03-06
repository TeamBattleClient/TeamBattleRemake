package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class GenLayer {
	protected static boolean func_151616_a(final int p_151616_0_,
			final int p_151616_1_) {
		if (p_151616_0_ == p_151616_1_)
			return true;
		else if (p_151616_0_ != BiomeGenBase.field_150607_aa.biomeID
				&& p_151616_0_ != BiomeGenBase.field_150608_ab.biomeID) {
			try {
				return BiomeGenBase.func_150568_d(p_151616_0_) != null
						&& BiomeGenBase.func_150568_d(p_151616_1_) != null ? BiomeGenBase
						.func_150568_d(p_151616_0_).func_150569_a(
								BiomeGenBase.func_150568_d(p_151616_1_))
						: false;
			} catch (final Throwable var5) {
				final CrashReport var3 = CrashReport.makeCrashReport(var5,
						"Comparing biomes");
				final CrashReportCategory var4 = var3
						.makeCategory("Biomes being compared");
				var4.addCrashSection("Biome A ID", Integer.valueOf(p_151616_0_));
				var4.addCrashSection("Biome B ID", Integer.valueOf(p_151616_1_));
				var4.addCrashSectionCallable("Biome A", new Callable() {

					@Override
					public String call() {
						return String.valueOf(BiomeGenBase
								.func_150568_d(p_151616_0_));
					}
				});
				var4.addCrashSectionCallable("Biome B", new Callable() {

					@Override
					public String call() {
						return String.valueOf(BiomeGenBase
								.func_150568_d(p_151616_1_));
					}
				});
				throw new ReportedException(var3);
			}
		} else
			return p_151616_1_ == BiomeGenBase.field_150607_aa.biomeID
					|| p_151616_1_ == BiomeGenBase.field_150608_ab.biomeID;
	}

	protected static boolean func_151618_b(int p_151618_0_) {
		return p_151618_0_ == BiomeGenBase.ocean.biomeID
				|| p_151618_0_ == BiomeGenBase.field_150575_M.biomeID
				|| p_151618_0_ == BiomeGenBase.frozenOcean.biomeID;
	}

	/**
	 * the first array item is a linked list of the bioms, the second is the
	 * zoom function, the third is the same as the first.
	 */
	public static GenLayer[] initializeAllBiomeGenerators(long p_75901_0_,
			WorldType p_75901_2_) {
		final boolean var3 = false;
		final GenLayerIsland var4 = new GenLayerIsland(1L);
		final GenLayerFuzzyZoom var11 = new GenLayerFuzzyZoom(2000L, var4);
		GenLayerAddIsland var12 = new GenLayerAddIsland(1L, var11);
		GenLayerZoom var13 = new GenLayerZoom(2001L, var12);
		var12 = new GenLayerAddIsland(2L, var13);
		var12 = new GenLayerAddIsland(50L, var12);
		var12 = new GenLayerAddIsland(70L, var12);
		final GenLayerRemoveTooMuchOcean var14 = new GenLayerRemoveTooMuchOcean(
				2L, var12);
		final GenLayerAddSnow var15 = new GenLayerAddSnow(2L, var14);
		var12 = new GenLayerAddIsland(3L, var15);
		GenLayerEdge var16 = new GenLayerEdge(2L, var12,
				GenLayerEdge.Mode.COOL_WARM);
		var16 = new GenLayerEdge(2L, var16, GenLayerEdge.Mode.HEAT_ICE);
		var16 = new GenLayerEdge(3L, var16, GenLayerEdge.Mode.SPECIAL);
		var13 = new GenLayerZoom(2002L, var16);
		var13 = new GenLayerZoom(2003L, var13);
		var12 = new GenLayerAddIsland(4L, var13);
		final GenLayerAddMushroomIsland var17 = new GenLayerAddMushroomIsland(
				5L, var12);
		final GenLayerDeepOcean var18 = new GenLayerDeepOcean(4L, var17);
		final GenLayer var19 = GenLayerZoom.magnify(1000L, var18, 0);
		byte var5 = 4;

		if (p_75901_2_ == WorldType.LARGE_BIOMES) {
			var5 = 6;
		}

		if (var3) {
			var5 = 4;
		}

		GenLayer var6 = GenLayerZoom.magnify(1000L, var19, 0);
		final GenLayerRiverInit var20 = new GenLayerRiverInit(100L, var6);
		Object var7 = new GenLayerBiome(200L, var19, p_75901_2_);

		if (!var3) {
			final GenLayer var23 = GenLayerZoom.magnify(1000L, (GenLayer) var7,
					2);
			var7 = new GenLayerBiomeEdge(1000L, var23);
		}

		final GenLayer var8 = GenLayerZoom.magnify(1000L, var20, 2);
		final GenLayerHills var24 = new GenLayerHills(1000L, (GenLayer) var7,
				var8);
		var6 = GenLayerZoom.magnify(1000L, var20, 2);
		var6 = GenLayerZoom.magnify(1000L, var6, var5);
		final GenLayerRiver var21 = new GenLayerRiver(1L, var6);
		final GenLayerSmooth var22 = new GenLayerSmooth(1000L, var21);
		var7 = new GenLayerRareBiome(1001L, var24);

		for (int var9 = 0; var9 < var5; ++var9) {
			var7 = new GenLayerZoom((long) (1000 + var9), (GenLayer) var7);

			if (var9 == 0) {
				var7 = new GenLayerAddIsland(3L, (GenLayer) var7);
			}

			if (var9 == 1) {
				var7 = new GenLayerShore(1000L, (GenLayer) var7);
			}
		}

		final GenLayerSmooth var25 = new GenLayerSmooth(1000L, (GenLayer) var7);
		final GenLayerRiverMix var26 = new GenLayerRiverMix(100L, var25, var22);
		final GenLayerVoronoiZoom var10 = new GenLayerVoronoiZoom(10L, var26);
		var26.initWorldGenSeed(p_75901_0_);
		var10.initWorldGenSeed(p_75901_0_);
		return new GenLayer[] { var26, var10, var26 };
	}

	/** base seed to the LCG prng provided via the constructor */
	protected long baseSeed;

	/**
	 * final part of the LCG prng that uses the chunk X, Z coords along with the
	 * other two seeds to generate pseudorandom numbers
	 */
	private long chunkSeed;

	/** parent GenLayer that was provided via the constructor */
	protected GenLayer parent;

	/** seed from World#getWorldSeed that is used in the LCG prng */
	private long worldGenSeed;

	public GenLayer(long p_i2125_1_) {
		baseSeed = p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
		baseSeed *= baseSeed * 6364136223846793005L + 1442695040888963407L;
		baseSeed += p_i2125_1_;
	}

	protected int func_151617_b(int p_151617_1_, int p_151617_2_,
			int p_151617_3_, int p_151617_4_) {
		return p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_ ? p_151617_2_
				: p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_ ? p_151617_1_
						: p_151617_1_ == p_151617_2_
								&& p_151617_1_ == p_151617_4_ ? p_151617_1_
								: p_151617_1_ == p_151617_3_
										&& p_151617_1_ == p_151617_4_ ? p_151617_1_
										: p_151617_1_ == p_151617_2_
												&& p_151617_3_ != p_151617_4_ ? p_151617_1_
												: p_151617_1_ == p_151617_3_
														&& p_151617_2_ != p_151617_4_ ? p_151617_1_
														: p_151617_1_ == p_151617_4_
																&& p_151617_2_ != p_151617_3_ ? p_151617_1_
																: p_151617_2_ == p_151617_3_
																		&& p_151617_1_ != p_151617_4_ ? p_151617_2_
																		: p_151617_2_ == p_151617_4_
																				&& p_151617_1_ != p_151617_3_ ? p_151617_2_
																				: p_151617_3_ == p_151617_4_
																						&& p_151617_1_ != p_151617_2_ ? p_151617_3_
																						: func_151619_a(new int[] {
																								p_151617_1_,
																								p_151617_2_,
																								p_151617_3_,
																								p_151617_4_ });
	}

	protected int func_151619_a(int... p_151619_1_) {
		return p_151619_1_[nextInt(p_151619_1_.length)];
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be
	 * interpreted as temperatures, rainfall amounts, or biomeList[] indices
	 * based on the particular GenLayer subclass.
	 */
	public abstract int[] getInts(int p_75904_1_, int p_75904_2_,
			int p_75904_3_, int p_75904_4_);

	/**
	 * Initialize layer's current chunkSeed based on the local worldGenSeed and
	 * the (x,z) chunk coordinates.
	 */
	public void initChunkSeed(long p_75903_1_, long p_75903_3_) {
		chunkSeed = worldGenSeed;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_1_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_3_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_1_;
		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += p_75903_3_;
	}

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the
	 * world's global seed (passed in as an argument).
	 */
	public void initWorldGenSeed(long p_75905_1_) {
		worldGenSeed = p_75905_1_;

		if (parent != null) {
			parent.initWorldGenSeed(p_75905_1_);
		}

		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
		worldGenSeed *= worldGenSeed * 6364136223846793005L + 1442695040888963407L;
		worldGenSeed += baseSeed;
	}

	/**
	 * returns a LCG pseudo random number from [0, x). Args: int x
	 */
	protected int nextInt(int p_75902_1_) {
		int var2 = (int) ((chunkSeed >> 24) % p_75902_1_);

		if (var2 < 0) {
			var2 += p_75902_1_;
		}

		chunkSeed *= chunkSeed * 6364136223846793005L + 1442695040888963407L;
		chunkSeed += worldGenSeed;
		return var2;
	}
}
