package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkProviderHell implements IChunkProvider {
	public MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();

	private double[] gravelNoise = new double[256];
	private final Random hellRNG;
	private final MapGenBase netherCaveGenerator = new MapGenCavesHell();

	/** A NoiseGeneratorOctaves used in generating nether terrain */
	private final NoiseGeneratorOctaves netherNoiseGen1;

	private final NoiseGeneratorOctaves netherNoiseGen2;
	private final NoiseGeneratorOctaves netherNoiseGen3;
	public NoiseGeneratorOctaves netherNoiseGen6;

	public NoiseGeneratorOctaves netherNoiseGen7;
	/**
	 * Holds the noise used to determine whether something other than netherrack
	 * can be generated at a location
	 */
	private double[] netherrackExclusivityNoise = new double[256];
	/**
	 * Determines whether something other than nettherack can be generated at a
	 * location
	 */
	private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;

	double[] noiseData1;
	double[] noiseData2;

	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;
	private double[] noiseField;
	/** Determines whether slowsand or gravel can be generated at a location */
	private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
	/**
	 * Holds the noise used to determine whether slowsand can be generated at a
	 * location
	 */
	private double[] slowsandNoise = new double[256];
	/** Is the world that the nether is getting generated. */
	private final World worldObj;

	public ChunkProviderHell(World p_i2005_1_, long p_i2005_2_) {
		worldObj = p_i2005_1_;
		hellRNG = new Random(p_i2005_2_);
		netherNoiseGen1 = new NoiseGeneratorOctaves(hellRNG, 16);
		netherNoiseGen2 = new NoiseGeneratorOctaves(hellRNG, 16);
		netherNoiseGen3 = new NoiseGeneratorOctaves(hellRNG, 8);
		slowsandGravelNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
		netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
		netherNoiseGen6 = new NoiseGeneratorOctaves(hellRNG, 10);
		netherNoiseGen7 = new NoiseGeneratorOctaves(hellRNG, 16);
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	@Override
	public boolean canSave() {
		return true;
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
		return true;
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_,
			int p_147416_3_, int p_147416_4_, int p_147416_5_) {
		return null;
	}

	public void func_147418_b(int p_147418_1_, int p_147418_2_,
			Block[] p_147418_3_) {
		final byte var4 = 64;
		final double var5 = 0.03125D;
		slowsandNoise = slowsandGravelNoiseGen.generateNoiseOctaves(
				slowsandNoise, p_147418_1_ * 16, p_147418_2_ * 16, 0, 16, 16,
				1, var5, var5, 1.0D);
		gravelNoise = slowsandGravelNoiseGen.generateNoiseOctaves(gravelNoise,
				p_147418_1_ * 16, 109, p_147418_2_ * 16, 16, 1, 16, var5, 1.0D,
				var5);
		netherrackExclusivityNoise = netherrackExculsivityNoiseGen
				.generateNoiseOctaves(netherrackExclusivityNoise,
						p_147418_1_ * 16, p_147418_2_ * 16, 0, 16, 16, 1,
						var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

		for (int var7 = 0; var7 < 16; ++var7) {
			for (int var8 = 0; var8 < 16; ++var8) {
				final boolean var9 = slowsandNoise[var7 + var8 * 16]
						+ hellRNG.nextDouble() * 0.2D > 0.0D;
				final boolean var10 = gravelNoise[var7 + var8 * 16]
						+ hellRNG.nextDouble() * 0.2D > 0.0D;
				final int var11 = (int) (netherrackExclusivityNoise[var7 + var8
						* 16] / 3.0D + 3.0D + hellRNG.nextDouble() * 0.25D);
				int var12 = -1;
				Block var13 = Blocks.netherrack;
				Block var14 = Blocks.netherrack;

				for (int var15 = 127; var15 >= 0; --var15) {
					final int var16 = (var8 * 16 + var7) * 128 + var15;

					if (var15 < 127 - hellRNG.nextInt(5)
							&& var15 > 0 + hellRNG.nextInt(5)) {
						final Block var17 = p_147418_3_[var16];

						if (var17 != null
								&& var17.getMaterial() != Material.air) {
							if (var17 == Blocks.netherrack) {
								if (var12 == -1) {
									if (var11 <= 0) {
										var13 = null;
										var14 = Blocks.netherrack;
									} else if (var15 >= var4 - 4
											&& var15 <= var4 + 1) {
										var13 = Blocks.netherrack;
										var14 = Blocks.netherrack;

										if (var10) {
											var13 = Blocks.gravel;
											var14 = Blocks.netherrack;
										}

										if (var9) {
											var13 = Blocks.soul_sand;
											var14 = Blocks.soul_sand;
										}
									}

									if (var15 < var4
											&& (var13 == null || var13
													.getMaterial() == Material.air)) {
										var13 = Blocks.lava;
									}

									var12 = var11;

									if (var15 >= var4 - 1) {
										p_147418_3_[var16] = var13;
									} else {
										p_147418_3_[var16] = var14;
									}
								} else if (var12 > 0) {
									--var12;
									p_147418_3_[var16] = var14;
								}
							}
						} else {
							var12 = -1;
						}
					} else {
						p_147418_3_[var16] = Blocks.bedrock;
					}
				}
			}
		}
	}

	public void func_147419_a(int p_147419_1_, int p_147419_2_,
			Block[] p_147419_3_) {
		final byte var4 = 4;
		final byte var5 = 32;
		final int var6 = var4 + 1;
		final byte var7 = 17;
		final int var8 = var4 + 1;
		noiseField = initializeNoiseField(noiseField, p_147419_1_ * var4, 0,
				p_147419_2_ * var4, var6, var7, var8);

		for (int var9 = 0; var9 < var4; ++var9) {
			for (int var10 = 0; var10 < var4; ++var10) {
				for (int var11 = 0; var11 < 16; ++var11) {
					final double var12 = 0.125D;
					double var14 = noiseField[((var9 + 0) * var8 + var10 + 0)
							* var7 + var11 + 0];
					double var16 = noiseField[((var9 + 0) * var8 + var10 + 1)
							* var7 + var11 + 0];
					double var18 = noiseField[((var9 + 1) * var8 + var10 + 0)
							* var7 + var11 + 0];
					double var20 = noiseField[((var9 + 1) * var8 + var10 + 1)
							* var7 + var11 + 0];
					final double var22 = (noiseField[((var9 + 0) * var8 + var10 + 0)
							* var7 + var11 + 1] - var14)
							* var12;
					final double var24 = (noiseField[((var9 + 0) * var8 + var10 + 1)
							* var7 + var11 + 1] - var16)
							* var12;
					final double var26 = (noiseField[((var9 + 1) * var8 + var10 + 0)
							* var7 + var11 + 1] - var18)
							* var12;
					final double var28 = (noiseField[((var9 + 1) * var8 + var10 + 1)
							* var7 + var11 + 1] - var20)
							* var12;

					for (int var30 = 0; var30 < 8; ++var30) {
						final double var31 = 0.25D;
						double var33 = var14;
						double var35 = var16;
						final double var37 = (var18 - var14) * var31;
						final double var39 = (var20 - var16) * var31;

						for (int var41 = 0; var41 < 4; ++var41) {
							int var42 = var41 + var9 * 4 << 11
									| 0 + var10 * 4 << 7 | var11 * 8 + var30;
							final short var43 = 128;
							final double var44 = 0.25D;
							double var46 = var33;
							final double var48 = (var35 - var33) * var44;

							for (int var50 = 0; var50 < 4; ++var50) {
								Block var51 = null;

								if (var11 * 8 + var30 < var5) {
									var51 = Blocks.lava;
								}

								if (var46 > 0.0D) {
									var51 = Blocks.netherrack;
								}

								p_147419_3_[var42] = var51;
								var42 += var43;
								var46 += var48;
							}

							var33 += var37;
							var35 += var39;
						}

						var14 += var22;
						var16 += var24;
						var18 += var26;
						var20 += var28;
					}
				}
			}
		}
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 */
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_,
			int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		if (p_73155_1_ == EnumCreatureType.monster) {
			if (genNetherBridge.hasStructureAt(p_73155_2_, p_73155_3_,
					p_73155_4_))
				return genNetherBridge.getSpawnList();

			if (genNetherBridge.func_142038_b(p_73155_2_, p_73155_3_,
					p_73155_4_)
					&& worldObj
							.getBlock(p_73155_2_, p_73155_3_ - 1, p_73155_4_) == Blocks.nether_brick)
				return genNetherBridge.getSpawnList();
		}

		final BiomeGenBase var5 = worldObj.getBiomeGenForCoords(p_73155_2_,
				p_73155_4_);
		return var5.getSpawnableList(p_73155_1_);
	}

	/**
	 * generates a subset of the level's terrain data. Takes 7 arguments: the
	 * [empty] noise array, the position, and the size.
	 */
	private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_,
			int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_,
			int p_73164_7_) {
		if (p_73164_1_ == null) {
			p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
		}

		final double var8 = 684.412D;
		final double var10 = 2053.236D;
		noiseData4 = netherNoiseGen6.generateNoiseOctaves(noiseData4,
				p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_,
				1.0D, 0.0D, 1.0D);
		noiseData5 = netherNoiseGen7.generateNoiseOctaves(noiseData5,
				p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_,
				100.0D, 0.0D, 100.0D);
		noiseData1 = netherNoiseGen3.generateNoiseOctaves(noiseData1,
				p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_,
				p_73164_7_, var8 / 80.0D, var10 / 60.0D, var8 / 80.0D);
		noiseData2 = netherNoiseGen1.generateNoiseOctaves(noiseData2,
				p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_,
				p_73164_7_, var8, var10, var8);
		noiseData3 = netherNoiseGen2.generateNoiseOctaves(noiseData3,
				p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_,
				p_73164_7_, var8, var10, var8);
		int var12 = 0;
		int var13 = 0;
		final double[] var14 = new double[p_73164_6_];
		int var15;

		for (var15 = 0; var15 < p_73164_6_; ++var15) {
			var14[var15] = Math.cos(var15 * Math.PI * 6.0D / p_73164_6_) * 2.0D;
			double var16 = var15;

			if (var15 > p_73164_6_ / 2) {
				var16 = p_73164_6_ - 1 - var15;
			}

			if (var16 < 4.0D) {
				var16 = 4.0D - var16;
				var14[var15] -= var16 * var16 * var16 * 10.0D;
			}
		}

		for (var15 = 0; var15 < p_73164_5_; ++var15) {
			for (int var36 = 0; var36 < p_73164_7_; ++var36) {
				double var17 = (noiseData4[var13] + 256.0D) / 512.0D;

				if (var17 > 1.0D) {
					var17 = 1.0D;
				}

				final double var19 = 0.0D;
				double var21 = noiseData5[var13] / 8000.0D;

				if (var21 < 0.0D) {
					var21 = -var21;
				}

				var21 = var21 * 3.0D - 3.0D;

				if (var21 < 0.0D) {
					var21 /= 2.0D;

					if (var21 < -1.0D) {
						var21 = -1.0D;
					}

					var21 /= 1.4D;
					var21 /= 2.0D;
					var17 = 0.0D;
				} else {
					if (var21 > 1.0D) {
						var21 = 1.0D;
					}

					var21 /= 6.0D;
				}

				var17 += 0.5D;
				var21 = var21 * p_73164_6_ / 16.0D;
				++var13;

				for (int var23 = 0; var23 < p_73164_6_; ++var23) {
					double var24 = 0.0D;
					final double var26 = var14[var23];
					final double var28 = noiseData2[var12] / 512.0D;
					final double var30 = noiseData3[var12] / 512.0D;
					final double var32 = (noiseData1[var12] / 10.0D + 1.0D) / 2.0D;

					if (var32 < 0.0D) {
						var24 = var28;
					} else if (var32 > 1.0D) {
						var24 = var30;
					} else {
						var24 = var28 + (var30 - var28) * var32;
					}

					var24 -= var26;
					double var34;

					if (var23 > p_73164_6_ - 4) {
						var34 = (var23 - (p_73164_6_ - 4)) / 3.0F;
						var24 = var24 * (1.0D - var34) + -10.0D * var34;
					}

					if (var23 < var19) {
						var34 = (var19 - var23) / 4.0D;

						if (var34 < 0.0D) {
							var34 = 0.0D;
						}

						if (var34 > 1.0D) {
							var34 = 1.0D;
						}

						var24 = var24 * (1.0D - var34) + -10.0D * var34;
					}

					p_73164_1_[var12] = var24;
					++var12;
				}
			}
		}

		return p_73164_1_;
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		return provideChunk(p_73158_1_, p_73158_2_);
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	@Override
	public String makeString() {
		return "HellRandomLevelSource";
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_,
			int p_73153_3_) {
		BlockFalling.field_149832_M = true;
		final int var4 = p_73153_2_ * 16;
		final int var5 = p_73153_3_ * 16;
		genNetherBridge.generateStructuresInChunk(worldObj, hellRNG,
				p_73153_2_, p_73153_3_);
		int var6;
		int var7;
		int var8;
		int var9;

		for (var6 = 0; var6 < 8; ++var6) {
			var7 = var4 + hellRNG.nextInt(16) + 8;
			var8 = hellRNG.nextInt(120) + 4;
			var9 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenHellLava(Blocks.flowing_lava, false).generate(worldObj,
					hellRNG, var7, var8, var9);
		}

		var6 = hellRNG.nextInt(hellRNG.nextInt(10) + 1) + 1;
		int var10;

		for (var7 = 0; var7 < var6; ++var7) {
			var8 = var4 + hellRNG.nextInt(16) + 8;
			var9 = hellRNG.nextInt(120) + 4;
			var10 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenFire().generate(worldObj, hellRNG, var8, var9, var10);
		}

		var6 = hellRNG.nextInt(hellRNG.nextInt(10) + 1);

		for (var7 = 0; var7 < var6; ++var7) {
			var8 = var4 + hellRNG.nextInt(16) + 8;
			var9 = hellRNG.nextInt(120) + 4;
			var10 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenGlowStone1().generate(worldObj, hellRNG, var8, var9,
					var10);
		}

		for (var7 = 0; var7 < 10; ++var7) {
			var8 = var4 + hellRNG.nextInt(16) + 8;
			var9 = hellRNG.nextInt(128);
			var10 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenGlowStone2().generate(worldObj, hellRNG, var8, var9,
					var10);
		}

		if (hellRNG.nextInt(1) == 0) {
			var7 = var4 + hellRNG.nextInt(16) + 8;
			var8 = hellRNG.nextInt(128);
			var9 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenFlowers(Blocks.brown_mushroom).generate(worldObj,
					hellRNG, var7, var8, var9);
		}

		if (hellRNG.nextInt(1) == 0) {
			var7 = var4 + hellRNG.nextInt(16) + 8;
			var8 = hellRNG.nextInt(128);
			var9 = var5 + hellRNG.nextInt(16) + 8;
			new WorldGenFlowers(Blocks.red_mushroom).generate(worldObj,
					hellRNG, var7, var8, var9);
		}

		final WorldGenMinable var12 = new WorldGenMinable(Blocks.quartz_ore,
				13, Blocks.netherrack);
		int var11;

		for (var8 = 0; var8 < 16; ++var8) {
			var9 = var4 + hellRNG.nextInt(16);
			var10 = hellRNG.nextInt(108) + 10;
			var11 = var5 + hellRNG.nextInt(16);
			var12.generate(worldObj, hellRNG, var9, var10, var11);
		}

		for (var8 = 0; var8 < 16; ++var8) {
			var9 = var4 + hellRNG.nextInt(16);
			var10 = hellRNG.nextInt(108) + 10;
			var11 = var5 + hellRNG.nextInt(16);
			new WorldGenHellLava(Blocks.flowing_lava, true).generate(worldObj,
					hellRNG, var9, var10, var11);
		}

		BlockFalling.field_149832_M = false;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		hellRNG.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
		final Block[] var3 = new Block[32768];
		func_147419_a(p_73154_1_, p_73154_2_, var3);
		func_147418_b(p_73154_1_, p_73154_2_, var3);
		netherCaveGenerator.func_151539_a(this, worldObj, p_73154_1_,
				p_73154_2_, var3);
		genNetherBridge.func_151539_a(this, worldObj, p_73154_1_, p_73154_2_,
				var3);
		final Chunk var4 = new Chunk(worldObj, var3, p_73154_1_, p_73154_2_);
		final BiomeGenBase[] var5 = worldObj.getWorldChunkManager()
				.loadBlockGeneratorData((BiomeGenBase[]) null, p_73154_1_ * 16,
						p_73154_2_ * 16, 16, 16);
		final byte[] var6 = var4.getBiomeArray();

		for (int var7 = 0; var7 < var6.length; ++var7) {
			var6[var7] = (byte) var5[var7].biomeID;
		}

		var4.resetRelightChecks();
		return var4;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
		genNetherBridge.func_151539_a(this, worldObj, p_82695_1_, p_82695_2_,
				(Block[]) null);
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If
	 * passed false, save up to two chunks. Return true if all chunks have been
	 * saved.
	 */
	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
		return true;
	}

	/**
	 * Save extra data not associated with any Chunk. Not saved during autosave,
	 * only during world unload. Currently unimplemented.
	 */
	@Override
	public void saveExtraData() {
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
}
