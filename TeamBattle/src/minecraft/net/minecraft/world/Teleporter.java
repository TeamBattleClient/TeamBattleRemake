package net.minecraft.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;

public class Teleporter {
	public class PortalPosition extends ChunkCoordinates {
		public long lastUpdateTime;

		public PortalPosition(int p_i1962_2_, int p_i1962_3_, int p_i1962_4_,
				long p_i1962_5_) {
			super(p_i1962_2_, p_i1962_3_, p_i1962_4_);
			lastUpdateTime = p_i1962_5_;
		}
	}

	/** Stores successful portal placement locations for rapid lookup. */
	private final LongHashMap destinationCoordinateCache = new LongHashMap();

	/**
	 * A list of valid keys for the destinationCoordainteCache. These are based
	 * on the X & Z of the players initial location.
	 */
	private final List destinationCoordinateKeys = new ArrayList();

	/** A private Random() function in Teleporter */
	private final Random random;

	private final WorldServer worldServerInstance;

	public Teleporter(WorldServer p_i1963_1_) {
		worldServerInstance = p_i1963_1_;
		random = new Random(p_i1963_1_.getSeed());
	}

	public boolean makePortal(Entity p_85188_1_) {
		final byte var2 = 16;
		double var3 = -1.0D;
		final int var5 = MathHelper.floor_double(p_85188_1_.posX);
		final int var6 = MathHelper.floor_double(p_85188_1_.posY);
		final int var7 = MathHelper.floor_double(p_85188_1_.posZ);
		int var8 = var5;
		int var9 = var6;
		int var10 = var7;
		int var11 = 0;
		final int var12 = random.nextInt(4);
		int var13;
		double var14;
		int var16;
		double var17;
		int var19;
		int var20;
		int var21;
		int var22;
		int var23;
		int var24;
		int var25;
		int var26;
		int var27;
		double var31;
		double var32;

		for (var13 = var5 - var2; var13 <= var5 + var2; ++var13) {
			var14 = var13 + 0.5D - p_85188_1_.posX;

			for (var16 = var7 - var2; var16 <= var7 + var2; ++var16) {
				var17 = var16 + 0.5D - p_85188_1_.posZ;
				label274:

				for (var19 = worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19) {
					if (worldServerInstance.isAirBlock(var13, var19, var16)) {
						while (var19 > 0
								&& worldServerInstance.isAirBlock(var13,
										var19 - 1, var16)) {
							--var19;
						}

						for (var20 = var12; var20 < var12 + 4; ++var20) {
							var21 = var20 % 2;
							var22 = 1 - var21;

							if (var20 % 4 >= 2) {
								var21 = -var21;
								var22 = -var22;
							}

							for (var23 = 0; var23 < 3; ++var23) {
								for (var24 = 0; var24 < 4; ++var24) {
									for (var25 = -1; var25 < 4; ++var25) {
										var26 = var13 + (var24 - 1) * var21
												+ var23 * var22;
										var27 = var19 + var25;
										final int var28 = var16 + (var24 - 1)
												* var22 - var23 * var21;

										if (var25 < 0
												&& !worldServerInstance
														.getBlock(var26, var27,
																var28)
														.getMaterial()
														.isSolid()
												|| var25 >= 0
												&& !worldServerInstance
														.isAirBlock(var26,
																var27, var28)) {
											continue label274;
										}
									}
								}
							}

							var31 = var19 + 0.5D - p_85188_1_.posY;
							var32 = var14 * var14 + var31 * var31 + var17
									* var17;

							if (var3 < 0.0D || var32 < var3) {
								var3 = var32;
								var8 = var13;
								var9 = var19;
								var10 = var16;
								var11 = var20 % 4;
							}
						}
					}
				}
			}
		}

		if (var3 < 0.0D) {
			for (var13 = var5 - var2; var13 <= var5 + var2; ++var13) {
				var14 = var13 + 0.5D - p_85188_1_.posX;

				for (var16 = var7 - var2; var16 <= var7 + var2; ++var16) {
					var17 = var16 + 0.5D - p_85188_1_.posZ;
					label222:

					for (var19 = worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19) {
						if (worldServerInstance.isAirBlock(var13, var19, var16)) {
							while (var19 > 0
									&& worldServerInstance.isAirBlock(var13,
											var19 - 1, var16)) {
								--var19;
							}

							for (var20 = var12; var20 < var12 + 2; ++var20) {
								var21 = var20 % 2;
								var22 = 1 - var21;

								for (var23 = 0; var23 < 4; ++var23) {
									for (var24 = -1; var24 < 4; ++var24) {
										var25 = var13 + (var23 - 1) * var21;
										var26 = var19 + var24;
										var27 = var16 + (var23 - 1) * var22;

										if (var24 < 0
												&& !worldServerInstance
														.getBlock(var25, var26,
																var27)
														.getMaterial()
														.isSolid()
												|| var24 >= 0
												&& !worldServerInstance
														.isAirBlock(var25,
																var26, var27)) {
											continue label222;
										}
									}
								}

								var31 = var19 + 0.5D - p_85188_1_.posY;
								var32 = var14 * var14 + var31 * var31 + var17
										* var17;

								if (var3 < 0.0D || var32 < var3) {
									var3 = var32;
									var8 = var13;
									var9 = var19;
									var10 = var16;
									var11 = var20 % 2;
								}
							}
						}
					}
				}
			}
		}

		final int var29 = var8;
		int var15 = var9;
		var16 = var10;
		int var30 = var11 % 2;
		int var18 = 1 - var30;

		if (var11 % 4 >= 2) {
			var30 = -var30;
			var18 = -var18;
		}

		boolean var33;

		if (var3 < 0.0D) {
			if (var9 < 70) {
				var9 = 70;
			}

			if (var9 > worldServerInstance.getActualHeight() - 10) {
				var9 = worldServerInstance.getActualHeight() - 10;
			}

			var15 = var9;

			for (var19 = -1; var19 <= 1; ++var19) {
				for (var20 = 1; var20 < 3; ++var20) {
					for (var21 = -1; var21 < 3; ++var21) {
						var22 = var29 + (var20 - 1) * var30 + var19 * var18;
						var23 = var15 + var21;
						var24 = var16 + (var20 - 1) * var18 - var19 * var30;
						var33 = var21 < 0;
						worldServerInstance.setBlock(var22, var23, var24,
								var33 ? Blocks.obsidian : Blocks.air);
					}
				}
			}
		}

		for (var19 = 0; var19 < 4; ++var19) {
			for (var20 = 0; var20 < 4; ++var20) {
				for (var21 = -1; var21 < 4; ++var21) {
					var22 = var29 + (var20 - 1) * var30;
					var23 = var15 + var21;
					var24 = var16 + (var20 - 1) * var18;
					var33 = var20 == 0 || var20 == 3 || var21 == -1
							|| var21 == 3;
					worldServerInstance.setBlock(var22, var23, var24,
							var33 ? Blocks.obsidian : Blocks.portal, 0, 2);
				}
			}

			for (var20 = 0; var20 < 4; ++var20) {
				for (var21 = -1; var21 < 4; ++var21) {
					var22 = var29 + (var20 - 1) * var30;
					var23 = var15 + var21;
					var24 = var16 + (var20 - 1) * var18;
					worldServerInstance.notifyBlocksOfNeighborChange(var22,
							var23, var24,
							worldServerInstance.getBlock(var22, var23, var24));
				}
			}
		}

		return true;
	}

	/**
	 * Place an entity in a nearby portal which already exists.
	 */
	public boolean placeInExistingPortal(Entity p_77184_1_, double p_77184_2_,
			double p_77184_4_, double p_77184_6_, float p_77184_8_) {
		final short var9 = 128;
		double var10 = -1.0D;
		int var12 = 0;
		int var13 = 0;
		int var14 = 0;
		final int var15 = MathHelper.floor_double(p_77184_1_.posX);
		final int var16 = MathHelper.floor_double(p_77184_1_.posZ);
		final long var17 = ChunkCoordIntPair.chunkXZ2Int(var15, var16);
		boolean var19 = true;
		double var27;
		int var48;

		if (destinationCoordinateCache.containsItem(var17)) {
			final Teleporter.PortalPosition var20 = (Teleporter.PortalPosition) destinationCoordinateCache
					.getValueByKey(var17);
			var10 = 0.0D;
			var12 = var20.posX;
			var13 = var20.posY;
			var14 = var20.posZ;
			var20.lastUpdateTime = worldServerInstance.getTotalWorldTime();
			var19 = false;
		} else {
			for (var48 = var15 - var9; var48 <= var15 + var9; ++var48) {
				final double var21 = var48 + 0.5D - p_77184_1_.posX;

				for (int var23 = var16 - var9; var23 <= var16 + var9; ++var23) {
					final double var24 = var23 + 0.5D - p_77184_1_.posZ;

					for (int var26 = worldServerInstance.getActualHeight() - 1; var26 >= 0; --var26) {
						if (worldServerInstance.getBlock(var48, var26, var23) == Blocks.portal) {
							while (worldServerInstance.getBlock(var48,
									var26 - 1, var23) == Blocks.portal) {
								--var26;
							}

							var27 = var26 + 0.5D - p_77184_1_.posY;
							final double var29 = var21 * var21 + var27 * var27
									+ var24 * var24;

							if (var10 < 0.0D || var29 < var10) {
								var10 = var29;
								var12 = var48;
								var13 = var26;
								var14 = var23;
							}
						}
					}
				}
			}
		}

		if (var10 >= 0.0D) {
			if (var19) {
				destinationCoordinateCache.add(var17,
						new Teleporter.PortalPosition(var12, var13, var14,
								worldServerInstance.getTotalWorldTime()));
				destinationCoordinateKeys.add(Long.valueOf(var17));
			}

			double var49 = var12 + 0.5D;
			final double var25 = var13 + 0.5D;
			var27 = var14 + 0.5D;
			int var50 = -1;

			if (worldServerInstance.getBlock(var12 - 1, var13, var14) == Blocks.portal) {
				var50 = 2;
			}

			if (worldServerInstance.getBlock(var12 + 1, var13, var14) == Blocks.portal) {
				var50 = 0;
			}

			if (worldServerInstance.getBlock(var12, var13, var14 - 1) == Blocks.portal) {
				var50 = 3;
			}

			if (worldServerInstance.getBlock(var12, var13, var14 + 1) == Blocks.portal) {
				var50 = 1;
			}

			final int var30 = p_77184_1_.getTeleportDirection();

			if (var50 > -1) {
				int var31 = Direction.rotateLeft[var50];
				int var32 = Direction.offsetX[var50];
				int var33 = Direction.offsetZ[var50];
				int var34 = Direction.offsetX[var31];
				int var35 = Direction.offsetZ[var31];
				boolean var36 = !worldServerInstance.isAirBlock(var12 + var32
						+ var34, var13, var14 + var33 + var35)
						|| !worldServerInstance.isAirBlock(var12 + var32
								+ var34, var13 + 1, var14 + var33 + var35);
				boolean var37 = !worldServerInstance.isAirBlock(var12 + var32,
						var13, var14 + var33)
						|| !worldServerInstance.isAirBlock(var12 + var32,
								var13 + 1, var14 + var33);

				if (var36 && var37) {
					var50 = Direction.rotateOpposite[var50];
					var31 = Direction.rotateOpposite[var31];
					var32 = Direction.offsetX[var50];
					var33 = Direction.offsetZ[var50];
					var34 = Direction.offsetX[var31];
					var35 = Direction.offsetZ[var31];
					var48 = var12 - var34;
					var49 -= var34;
					final int var22 = var14 - var35;
					var27 -= var35;
					var36 = !worldServerInstance.isAirBlock(var48 + var32
							+ var34, var13, var22 + var33 + var35)
							|| !worldServerInstance.isAirBlock(var48 + var32
									+ var34, var13 + 1, var22 + var33 + var35);
					var37 = !worldServerInstance.isAirBlock(var48 + var32,
							var13, var22 + var33)
							|| !worldServerInstance.isAirBlock(var48 + var32,
									var13 + 1, var22 + var33);
				}

				float var38 = 0.5F;
				float var39 = 0.5F;

				if (!var36 && var37) {
					var38 = 1.0F;
				} else if (var36 && !var37) {
					var38 = 0.0F;
				} else if (var36 && var37) {
					var39 = 0.0F;
				}

				var49 += var34 * var38 + var39 * var32;
				var27 += var35 * var38 + var39 * var33;
				float var40 = 0.0F;
				float var41 = 0.0F;
				float var42 = 0.0F;
				float var43 = 0.0F;

				if (var50 == var30) {
					var40 = 1.0F;
					var41 = 1.0F;
				} else if (var50 == Direction.rotateOpposite[var30]) {
					var40 = -1.0F;
					var41 = -1.0F;
				} else if (var50 == Direction.rotateRight[var30]) {
					var42 = 1.0F;
					var43 = -1.0F;
				} else {
					var42 = -1.0F;
					var43 = 1.0F;
				}

				final double var44 = p_77184_1_.motionX;
				final double var46 = p_77184_1_.motionZ;
				p_77184_1_.motionX = var44 * var40 + var46 * var43;
				p_77184_1_.motionZ = var44 * var42 + var46 * var41;
				p_77184_1_.rotationYaw = p_77184_8_ - var30 * 90 + var50 * 90;
			} else {
				p_77184_1_.motionX = p_77184_1_.motionY = p_77184_1_.motionZ = 0.0D;
			}

			p_77184_1_.setLocationAndAngles(var49, var25, var27,
					p_77184_1_.rotationYaw, p_77184_1_.rotationPitch);
			return true;
		} else
			return false;
	}

	/**
	 * Place an entity in a nearby portal, creating one if necessary.
	 */
	public void placeInPortal(Entity p_77185_1_, double p_77185_2_,
			double p_77185_4_, double p_77185_6_, float p_77185_8_) {
		if (worldServerInstance.provider.dimensionId != 1) {
			if (!placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_,
					p_77185_6_, p_77185_8_)) {
				makePortal(p_77185_1_);
				placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_,
						p_77185_6_, p_77185_8_);
			}
		} else {
			final int var9 = MathHelper.floor_double(p_77185_1_.posX);
			final int var10 = MathHelper.floor_double(p_77185_1_.posY) - 1;
			final int var11 = MathHelper.floor_double(p_77185_1_.posZ);
			final byte var12 = 1;
			final byte var13 = 0;

			for (int var14 = -2; var14 <= 2; ++var14) {
				for (int var15 = -2; var15 <= 2; ++var15) {
					for (int var16 = -1; var16 < 3; ++var16) {
						final int var17 = var9 + var15 * var12 + var14 * var13;
						final int var18 = var10 + var16;
						final int var19 = var11 + var15 * var13 - var14 * var12;
						final boolean var20 = var16 < 0;
						worldServerInstance.setBlock(var17, var18, var19,
								var20 ? Blocks.obsidian : Blocks.air);
					}
				}
			}

			p_77185_1_.setLocationAndAngles(var9, var10, var11,
					p_77185_1_.rotationYaw, 0.0F);
			p_77185_1_.motionX = p_77185_1_.motionY = p_77185_1_.motionZ = 0.0D;
		}
	}

	/**
	 * called periodically to remove out-of-date portal locations from the cache
	 * list. Argument par1 is a WorldServer.getTotalWorldTime() value.
	 */
	public void removeStalePortalLocations(long p_85189_1_) {
		if (p_85189_1_ % 100L == 0L) {
			final Iterator var3 = destinationCoordinateKeys.iterator();
			final long var4 = p_85189_1_ - 600L;

			while (var3.hasNext()) {
				final Long var6 = (Long) var3.next();
				final Teleporter.PortalPosition var7 = (Teleporter.PortalPosition) destinationCoordinateCache
						.getValueByKey(var6.longValue());

				if (var7 == null || var7.lastUpdateTime < var4) {
					var3.remove();
					destinationCoordinateCache.remove(var6.longValue());
				}
			}
		}
	}
}
