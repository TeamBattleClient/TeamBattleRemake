package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEnd implements IChunkProvider {
	/** The biomes that are used to generate the chunk */
	private BiomeGenBase[] biomesForGeneration;
	private double[] densities;
	private final Random endRNG;
	private final World endWorld;
	int[][] field_73203_h = new int[32][32];
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;

	double[] noiseData4;
	double[] noiseData5;
	private final NoiseGeneratorOctaves noiseGen1;
	private final NoiseGeneratorOctaves noiseGen2;
	private final NoiseGeneratorOctaves noiseGen3;
	public NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;

	public ChunkProviderEnd(World p_i2007_1_, long p_i2007_2_) {
		endWorld = p_i2007_1_;
		endRNG = new Random(p_i2007_2_);
		noiseGen1 = new NoiseGeneratorOctaves(endRNG, 16);
		noiseGen2 = new NoiseGeneratorOctaves(endRNG, 16);
		noiseGen3 = new NoiseGeneratorOctaves(endRNG, 8);
		noiseGen4 = new NoiseGeneratorOctaves(endRNG, 10);
		noiseGen5 = new NoiseGeneratorOctaves(endRNG, 16);
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

	public void func_147420_a(int p_147420_1_, int p_147420_2_,
			Block[] p_147420_3_, BiomeGenBase[] p_147420_4_) {
		final byte var5 = 2;
		final int var6 = var5 + 1;
		final byte var7 = 33;
		final int var8 = var5 + 1;
		densities = initializeNoiseField(densities, p_147420_1_ * var5, 0,
				p_147420_2_ * var5, var6, var7, var8);

		for (int var9 = 0; var9 < var5; ++var9) {
			for (int var10 = 0; var10 < var5; ++var10) {
				for (int var11 = 0; var11 < 32; ++var11) {
					final double var12 = 0.25D;
					double var14 = densities[((var9 + 0) * var8 + var10 + 0)
							* var7 + var11 + 0];
					double var16 = densities[((var9 + 0) * var8 + var10 + 1)
							* var7 + var11 + 0];
					double var18 = densities[((var9 + 1) * var8 + var10 + 0)
							* var7 + var11 + 0];
					double var20 = densities[((var9 + 1) * var8 + var10 + 1)
							* var7 + var11 + 0];
					final double var22 = (densities[((var9 + 0) * var8 + var10 + 0)
							* var7 + var11 + 1] - var14)
							* var12;
					final double var24 = (densities[((var9 + 0) * var8 + var10 + 1)
							* var7 + var11 + 1] - var16)
							* var12;
					final double var26 = (densities[((var9 + 1) * var8 + var10 + 0)
							* var7 + var11 + 1] - var18)
							* var12;
					final double var28 = (densities[((var9 + 1) * var8 + var10 + 1)
							* var7 + var11 + 1] - var20)
							* var12;

					for (int var30 = 0; var30 < 4; ++var30) {
						final double var31 = 0.125D;
						double var33 = var14;
						double var35 = var16;
						final double var37 = (var18 - var14) * var31;
						final double var39 = (var20 - var16) * var31;

						for (int var41 = 0; var41 < 8; ++var41) {
							int var42 = var41 + var9 * 8 << 11
									| 0 + var10 * 8 << 7 | var11 * 4 + var30;
							final short var43 = 128;
							final double var44 = 0.125D;
							double var46 = var33;
							final double var48 = (var35 - var33) * var44;

							for (int var50 = 0; var50 < 8; ++var50) {
								Block var51 = null;

								if (var46 > 0.0D) {
									var51 = Blocks.end_stone;
								}

								p_147420_3_[var42] = var51;
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

	public void func_147421_b(int p_147421_1_, int p_147421_2_,
			Block[] p_147421_3_, BiomeGenBase[] p_147421_4_) {
		for (int var5 = 0; var5 < 16; ++var5) {
			for (int var6 = 0; var6 < 16; ++var6) {
				final byte var7 = 1;
				int var8 = -1;
				final Block var9 = Blocks.end_stone;
				final Block var10 = Blocks.end_stone;

				for (int var11 = 127; var11 >= 0; --var11) {
					final int var12 = (var6 * 16 + var5) * 128 + var11;
					final Block var13 = p_147421_3_[var12];

					if (var13 != null && var13.getMaterial() != Material.air) {
						if (var13 == Blocks.stone) {
							if (var8 == -1) {
								var8 = var7;

								if (var11 >= 0) {
									p_147421_3_[var12] = var9;
								} else {
									p_147421_3_[var12] = var10;
								}
							} else if (var8 > 0) {
								--var8;
								p_147421_3_[var12] = var10;
							}
						}
					} else {
						var8 = -1;
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
		final BiomeGenBase var5 = endWorld.getBiomeGenForCoords(p_73155_2_,
				p_73155_4_);
		return var5.getSpawnableList(p_73155_1_);
	}

	/**
	 * generates a subset of the level's terrain data. Takes 7 arguments: the
	 * [empty] noise array, the position, and the size.
	 */
	private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_,
			int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_,
			int p_73187_7_) {
		if (p_73187_1_ == null) {
			p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
		}

		double var8 = 684.412D;
		final double var10 = 684.412D;
		noiseData4 = noiseGen4.generateNoiseOctaves(noiseData4, p_73187_2_,
				p_73187_4_, p_73187_5_, p_73187_7_, 1.121D, 1.121D, 0.5D);
		noiseData5 = noiseGen5.generateNoiseOctaves(noiseData5, p_73187_2_,
				p_73187_4_, p_73187_5_, p_73187_7_, 200.0D, 200.0D, 0.5D);
		var8 *= 2.0D;
		noiseData1 = noiseGen3.generateNoiseOctaves(noiseData1, p_73187_2_,
				p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_,
				var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
		noiseData2 = noiseGen1.generateNoiseOctaves(noiseData2, p_73187_2_,
				p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_,
				var8, var10, var8);
		noiseData3 = noiseGen2.generateNoiseOctaves(noiseData3, p_73187_2_,
				p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_,
				var8, var10, var8);
		int var12 = 0;
		int var13 = 0;

		for (int var14 = 0; var14 < p_73187_5_; ++var14) {
			for (int var15 = 0; var15 < p_73187_7_; ++var15) {
				double var16 = (noiseData4[var13] + 256.0D) / 512.0D;

				if (var16 > 1.0D) {
					var16 = 1.0D;
				}

				double var18 = noiseData5[var13] / 8000.0D;

				if (var18 < 0.0D) {
					var18 = -var18 * 0.3D;
				}

				var18 = var18 * 3.0D - 2.0D;
				final float var20 = (var14 + p_73187_2_ - 0) / 1.0F;
				final float var21 = (var15 + p_73187_4_ - 0) / 1.0F;
				float var22 = 100.0F - MathHelper.sqrt_float(var20 * var20
						+ var21 * var21) * 8.0F;

				if (var22 > 80.0F) {
					var22 = 80.0F;
				}

				if (var22 < -100.0F) {
					var22 = -100.0F;
				}

				if (var18 > 1.0D) {
					var18 = 1.0D;
				}

				var18 /= 8.0D;
				var18 = 0.0D;

				if (var16 < 0.0D) {
					var16 = 0.0D;
				}

				var16 += 0.5D;
				var18 = var18 * p_73187_6_ / 16.0D;
				++var13;
				final double var23 = p_73187_6_ / 2.0D;

				for (int var25 = 0; var25 < p_73187_6_; ++var25) {
					double var26 = 0.0D;
					double var28 = (var25 - var23) * 8.0D / var16;

					if (var28 < 0.0D) {
						var28 *= -1.0D;
					}

					final double var30 = noiseData2[var12] / 512.0D;
					final double var32 = noiseData3[var12] / 512.0D;
					final double var34 = (noiseData1[var12] / 10.0D + 1.0D) / 2.0D;

					if (var34 < 0.0D) {
						var26 = var30;
					} else if (var34 > 1.0D) {
						var26 = var32;
					} else {
						var26 = var30 + (var32 - var30) * var34;
					}

					var26 -= 8.0D;
					var26 += var22;
					byte var36 = 2;
					double var37;

					if (var25 > p_73187_6_ / 2 - var36) {
						var37 = (var25 - (p_73187_6_ / 2 - var36)) / 64.0F;

						if (var37 < 0.0D) {
							var37 = 0.0D;
						}

						if (var37 > 1.0D) {
							var37 = 1.0D;
						}

						var26 = var26 * (1.0D - var37) + -3000.0D * var37;
					}

					var36 = 8;

					if (var25 < var36) {
						var37 = (var36 - var25) / (var36 - 1.0F);
						var26 = var26 * (1.0D - var37) + -30.0D * var37;
					}

					p_73187_1_[var12] = var26;
					++var12;
				}
			}
		}

		return p_73187_1_;
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
		return "RandomLevelSource";
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
		final BiomeGenBase var6 = endWorld.getBiomeGenForCoords(var4 + 16,
				var5 + 16);
		var6.decorate(endWorld, endWorld.rand, var4, var5);
		BlockFalling.field_149832_M = false;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		endRNG.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
		final Block[] var3 = new Block[32768];
		biomesForGeneration = endWorld.getWorldChunkManager()
				.loadBlockGeneratorData(biomesForGeneration, p_73154_1_ * 16,
						p_73154_2_ * 16, 16, 16);
		func_147420_a(p_73154_1_, p_73154_2_, var3, biomesForGeneration);
		func_147421_b(p_73154_1_, p_73154_2_, var3, biomesForGeneration);
		final Chunk var4 = new Chunk(endWorld, var3, p_73154_1_, p_73154_2_);
		final byte[] var5 = var4.getBiomeArray();

		for (int var6 = 0; var6 < var5.length; ++var6) {
			var5[var6] = (byte) biomesForGeneration[var6].biomeID;
		}

		var4.generateSkylightMap();
		return var4;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
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
