package net.minecraft.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public final class SpawnerAnimals {
	/**
	 * Returns whether or not the specified creature type can spawn at the
	 * specified location.
	 */
	public static boolean canCreatureTypeSpawnAtLocation(
			EnumCreatureType par0EnumCreatureType, World par1World, int par2,
			int par3, int par4) {
		if (par0EnumCreatureType.getCreatureMaterial() == Material.water)
			return par1World.getBlock(par2, par3, par4).getMaterial()
					.isLiquid()
					&& par1World.getBlock(par2, par3 - 1, par4).getMaterial()
							.isLiquid()
					&& !par1World.getBlock(par2, par3 + 1, par4).isNormalCube();
		else if (!World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1,
				par4))
			return false;
		else {
			final Block var5 = par1World.getBlock(par2, par3 - 1, par4);
			return var5 != Blocks.bedrock
					&& !par1World.getBlock(par2, par3, par4).isNormalCube()
					&& !par1World.getBlock(par2, par3, par4).getMaterial()
							.isLiquid()
					&& !par1World.getBlock(par2, par3 + 1, par4).isNormalCube();
		}
	}

	protected static ChunkPosition func_151350_a(World p_151350_0_,
			int p_151350_1_, int p_151350_2_) {
		final Chunk var3 = p_151350_0_.getChunkFromChunkCoords(p_151350_1_,
				p_151350_2_);
		final int var4 = p_151350_1_ * 16 + p_151350_0_.rand.nextInt(16);
		final int var5 = p_151350_2_ * 16 + p_151350_0_.rand.nextInt(16);
		final int var6 = p_151350_0_.rand.nextInt(var3 == null ? p_151350_0_
				.getActualHeight() : var3.getTopFilledSegment() + 16 - 1);
		return new ChunkPosition(var4, var6, var5);
	}

	/**
	 * Called during chunk generation to spawn initial creatures.
	 */
	public static void performWorldGenSpawning(World par0World,
			BiomeGenBase par1BiomeGenBase, int par2, int par3, int par4,
			int par5, Random par6Random) {
		final List var7 = par1BiomeGenBase
				.getSpawnableList(EnumCreatureType.creature);

		if (!var7.isEmpty()) {
			while (par6Random.nextFloat() < par1BiomeGenBase
					.getSpawningChance()) {
				final BiomeGenBase.SpawnListEntry var8 = (BiomeGenBase.SpawnListEntry) WeightedRandom
						.getRandomItem(par0World.rand, var7);
				IEntityLivingData var9 = null;
				final int var10 = var8.minGroupCount
						+ par6Random.nextInt(1 + var8.maxGroupCount
								- var8.minGroupCount);
				int var11 = par2 + par6Random.nextInt(par4);
				int var12 = par3 + par6Random.nextInt(par5);
				final int var13 = var11;
				final int var14 = var12;

				for (int var15 = 0; var15 < var10; ++var15) {
					boolean var16 = false;

					for (int var17 = 0; !var16 && var17 < 4; ++var17) {
						final int var18 = par0World.getTopSolidOrLiquidBlock(
								var11, var12);

						if (canCreatureTypeSpawnAtLocation(
								EnumCreatureType.creature, par0World, var11,
								var18, var12)) {
							final float var19 = var11 + 0.5F;
							final float var20 = var18;
							final float var21 = var12 + 0.5F;
							EntityLiving var22;

							try {
								var22 = (EntityLiving) var8.entityClass
										.getConstructor(
												new Class[] { World.class })
										.newInstance(new Object[] { par0World });
							} catch (final Exception var24) {
								var24.printStackTrace();
								continue;
							}

							var22.setLocationAndAngles(var19, var20, var21,
									par6Random.nextFloat() * 360.0F, 0.0F);
							par0World.spawnEntityInWorld(var22);
							var9 = var22.onSpawnWithEgg(var9);
							var16 = true;
						}

						var11 += par6Random.nextInt(5) - par6Random.nextInt(5);

						for (var12 += par6Random.nextInt(5)
								- par6Random.nextInt(5); var11 < par2
								|| var11 >= par2 + par4 || var12 < par3
								|| var12 >= par3 + par4; var12 = var14
								+ par6Random.nextInt(5) - par6Random.nextInt(5)) {
							var11 = var13 + par6Random.nextInt(5)
									- par6Random.nextInt(5);
						}
					}
				}
			}
		}
	}

	/** The 17x17 area around the player where mobs can spawn */
	private final HashMap eligibleChunksForSpawning = new HashMap();

	private int lastPlayerChunkX = Integer.MAX_VALUE;

	private int lastPlayerChunkZ = Integer.MAX_VALUE;

	private final Map mapSampleEntitiesByClass = new HashMap();

	/**
	 * adds all chunks within the spawn radius of the players to
	 * eligibleChunksForSpawning. pars: the world, hostileCreatures,
	 * passiveCreatures. returns number of eligible chunks.
	 */
	public int findChunksForSpawning(WorldServer par1WorldServer, boolean par2,
			boolean par3, boolean par4) {
		if (!par2 && !par3)
			return 0;
		else {
			EntityPlayer player = null;

			if (par1WorldServer.playerEntities.size() == 1) {
				player = (EntityPlayer) par1WorldServer.playerEntities.get(0);
			}

			int var5;
			int var8;

			if (player == null || player.chunkCoordX != lastPlayerChunkX
					|| player.chunkCoordZ != lastPlayerChunkZ
					|| eligibleChunksForSpawning.size() <= 0) {
				eligibleChunksForSpawning.clear();

				for (var5 = 0; var5 < par1WorldServer.playerEntities.size(); ++var5) {
					final EntityPlayer var34 = (EntityPlayer) par1WorldServer.playerEntities
							.get(var5);
					final int var35 = MathHelper
							.floor_double(var34.posX / 16.0D);
					var8 = MathHelper.floor_double(var34.posZ / 16.0D);
					final byte var36 = 8;

					for (int var37 = -var36; var37 <= var36; ++var37) {
						for (int var38 = -var36; var38 <= var36; ++var38) {
							final boolean var39 = var37 == -var36
									|| var37 == var36 || var38 == -var36
									|| var38 == var36;
							final ChunkCoordIntPair chunk = new ChunkCoordIntPair(
									var37 + var35, var38 + var8);

							if (!var39) {
								eligibleChunksForSpawning.put(chunk,
										Boolean.valueOf(false));
							} else if (!eligibleChunksForSpawning
									.containsKey(chunk)) {
								eligibleChunksForSpawning.put(chunk,
										Boolean.valueOf(true));
							}
						}
					}
				}

				if (player != null) {
					lastPlayerChunkX = player.chunkCoordX;
					lastPlayerChunkZ = player.chunkCoordZ;
				}
			}

			var5 = 0;
			final ChunkCoordinates var371 = par1WorldServer.getSpawnPoint();
			final EnumCreatureType[] var381 = EnumCreatureType.values();
			var8 = var381.length;

			for (int var391 = 0; var391 < var8; ++var391) {
				final EnumCreatureType var40 = var381[var391];

				if ((!var40.getPeacefulCreature() || par3)
						&& (var40.getPeacefulCreature() || par2)
						&& (!var40.getAnimal() || par4)
						&& par1WorldServer.countEntities(var40
								.getCreatureClass()) <= var40
								.getMaxNumberOfCreature()
								* eligibleChunksForSpawning.size() / 256) {
					final Iterator var411 = eligibleChunksForSpawning.keySet()
							.iterator();
					label120:

					while (var411.hasNext()) {
						final ChunkCoordIntPair var42 = (ChunkCoordIntPair) var411
								.next();

						if (!((Boolean) eligibleChunksForSpawning.get(var42))
								.booleanValue()) {
							final Chunk var43 = par1WorldServer
									.getChunkFromChunkCoords(var42.chunkXPos,
											var42.chunkZPos);
							final int var14 = var42.chunkXPos * 16
									+ par1WorldServer.rand.nextInt(16);
							final int var16 = var42.chunkZPos * 16
									+ par1WorldServer.rand.nextInt(16);
							final int var15 = par1WorldServer.rand
									.nextInt(var43 == null ? par1WorldServer
											.getActualHeight() : var43
											.getTopFilledSegment() + 16 - 1);

							if (!par1WorldServer.getBlock(var14, var15, var16)
									.isNormalCube()
									&& par1WorldServer.getBlock(var14, var15,
											var16).getMaterial() == var40
											.getCreatureMaterial()) {
								int var17 = 0;
								int var18 = 0;

								while (var18 < 3) {
									int var19 = var14;
									int var20 = var15;
									int var21 = var16;
									final byte var22 = 6;
									BiomeGenBase.SpawnListEntry var23 = null;
									IEntityLivingData var24 = null;
									int var25 = 0;

									while (true) {
										if (var25 < 4) {
											label113: {
												var19 += par1WorldServer.rand
														.nextInt(var22)
														- par1WorldServer.rand
																.nextInt(var22);
												var20 += par1WorldServer.rand
														.nextInt(1)
														- par1WorldServer.rand
																.nextInt(1);
												var21 += par1WorldServer.rand
														.nextInt(var22)
														- par1WorldServer.rand
																.nextInt(var22);

												if (canCreatureTypeSpawnAtLocation(
														var40, par1WorldServer,
														var19, var20, var21)) {
													final float var26 = var19 + 0.5F;
													final float var27 = var20;
													final float var28 = var21 + 0.5F;

													if (par1WorldServer
															.getClosestPlayer(
																	var26,
																	var27,
																	var28,
																	24.0D) == null) {
														final float var29 = var26
																- var371.posX;
														final float var30 = var27
																- var371.posY;
														final float var31 = var28
																- var371.posZ;
														final float var32 = var29
																* var29
																+ var30
																* var30
																+ var31
																* var31;

														if (var32 >= 576.0F) {
															if (var23 == null) {
																var23 = par1WorldServer
																		.spawnRandomCreature(
																				var40,
																				var19,
																				var20,
																				var21);

																if (var23 == null) {
																	break label113;
																}
															}

															EntityLiving var41;

															try {
																var41 = (EntityLiving) mapSampleEntitiesByClass
																		.get(var23.entityClass);

																if (var41 == null) {
																	var41 = (EntityLiving) var23.entityClass
																			.getConstructor(
																					new Class[] { World.class })
																			.newInstance(
																					new Object[] { par1WorldServer });
																	mapSampleEntitiesByClass
																			.put(var23.entityClass,
																					var41);
																}
															} catch (final Exception var361) {
																var361.printStackTrace();
																return var5;
															}

															var41.setLocationAndAngles(
																	var26,
																	var27,
																	var28,
																	par1WorldServer.rand
																			.nextFloat() * 360.0F,
																	0.0F);

															if (var41
																	.getCanSpawnHere()) {
																mapSampleEntitiesByClass
																		.put(var23.entityClass,
																				(Object) null);
																++var17;
																par1WorldServer
																		.spawnEntityInWorld(var41);
																var24 = var41
																		.onSpawnWithEgg(var24);

																if (var17 >= var41
																		.getMaxSpawnedInChunk()) {
																	continue label120;
																}
															}

															var5 += var17;
														}
													}
												}

												++var25;
												continue;
											}
										}

										++var18;
										break;
									}
								}
							}
						}
					}
				}
			}

			return var5;
		}
	}
}
