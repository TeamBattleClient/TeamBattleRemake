package net.minecraft.world.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
	/**
	 * Determines if the chunk is lit or not at a light value greater than 0.
	 */
	public static boolean isLit;

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each
	 * colum belongs.
	 */
	private byte[] blockBiomeArray;

	/** A Map of ChunkPositions to TileEntities in this chunk */
	public Map chunkTileEntityMap;

	/**
	 * Array of Lists containing the entities in this Chunk. Each List
	 * represents a 16 block subchunk.
	 */
	public List[] entityLists;

	public boolean field_150815_m;

	/**
	 * Whether this Chunk has any Entities and thus requires saving on every
	 * tick
	 */
	public boolean hasEntities;

	public int[] heightMap;
	/** Lowest value in the heightmap. */
	public int heightMapMinimum;

	/** the cumulative number of ticks players have been in this chunk */
	public long inhabitedTime;

	/** Whether or not this Chunk is currently loaded into the World */
	public boolean isChunkLoaded;
	private boolean isGapLightingUpdated;

	public boolean isLightPopulated;

	/**
	 * Set to true if the chunk has been modified and needs to be updated
	 * internally.
	 */
	public boolean isModified;

	/** Boolean value indicating if the terrain is populated. */
	public boolean isTerrainPopulated;
	/** The time according to World.worldTime when this chunk was last saved */
	public long lastSaveTime;
	/**
	 * A map, similar to heightMap, that tracks how far down precipitation can
	 * fall.
	 */
	public int[] precipitationHeightMap;

	/**
	 * Contains the current round-robin relight check index, and is implied as
	 * the relight check location as well.
	 */
	private int queuedLightChecks;

	/**
	 * Updates to this chunk will not be sent to clients if this is false. This
	 * field is set to true the first time the chunk is sent to a client, and
	 * never set to false.
	 */
	public boolean sendUpdates;

	/**
	 * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps,
	 * and metadata. Each entry corresponds to a logical segment of 16x16x16
	 * blocks, stacked vertically.
	 */
	private ExtendedBlockStorage[] storageArrays;

	/** Which columns need their skylightMaps updated. */
	public boolean[] updateSkylightColumns;

	/** Reference to the World object. */
	public World worldObj;

	/** The x coordinate of the chunk. */
	public final int xPosition;

	/** The z coordinate of the chunk. */
	public final int zPosition;

	public Chunk(World p_i45447_1_, Block[] p_i45447_2_, byte[] p_i45447_3_,
			int p_i45447_4_, int p_i45447_5_) {
		this(p_i45447_1_, p_i45447_4_, p_i45447_5_);
		final int var6 = p_i45447_2_.length / 256;
		final boolean var7 = !p_i45447_1_.provider.hasNoSky;

		for (int var8 = 0; var8 < 16; ++var8) {
			for (int var9 = 0; var9 < 16; ++var9) {
				for (int var10 = 0; var10 < var6; ++var10) {
					final int var11 = var8 * var6 * 16 | var9 * var6 | var10;
					final Block var12 = p_i45447_2_[var11];

					if (var12 != null && var12 != Blocks.air) {
						final int var13 = var10 >> 4;

						if (storageArrays[var13] == null) {
							storageArrays[var13] = new ExtendedBlockStorage(
									var13 << 4, var7);
						}

						storageArrays[var13].func_150818_a(var8, var10 & 15,
								var9, var12);
						storageArrays[var13].setExtBlockMetadata(var8,
								var10 & 15, var9, p_i45447_3_[var11]);
					}
				}
			}
		}
	}

	public Chunk(World p_i45446_1_, Block[] p_i45446_2_, int p_i45446_3_,
			int p_i45446_4_) {
		this(p_i45446_1_, p_i45446_3_, p_i45446_4_);
		final int var5 = p_i45446_2_.length / 256;
		final boolean var6 = !p_i45446_1_.provider.hasNoSky;

		for (int var7 = 0; var7 < 16; ++var7) {
			for (int var8 = 0; var8 < 16; ++var8) {
				for (int var9 = 0; var9 < var5; ++var9) {
					final Block var10 = p_i45446_2_[var7 << 11 | var8 << 7
							| var9];

					if (var10 != null && var10.getMaterial() != Material.air) {
						final int var11 = var9 >> 4;

						if (storageArrays[var11] == null) {
							storageArrays[var11] = new ExtendedBlockStorage(
									var11 << 4, var6);
						}

						storageArrays[var11].func_150818_a(var7, var9 & 15,
								var8, var10);
					}
				}
			}
		}
	}

	public Chunk(World p_i1995_1_, int p_i1995_2_, int p_i1995_3_) {
		storageArrays = new ExtendedBlockStorage[16];
		blockBiomeArray = new byte[256];
		precipitationHeightMap = new int[256];
		updateSkylightColumns = new boolean[256];
		chunkTileEntityMap = new HashMap();
		queuedLightChecks = 4096;
		entityLists = new List[16];
		worldObj = p_i1995_1_;
		xPosition = p_i1995_2_;
		zPosition = p_i1995_3_;
		heightMap = new int[256];

		for (int var4 = 0; var4 < entityLists.length; ++var4) {
			entityLists[var4] = new ArrayList();
		}

		Arrays.fill(precipitationHeightMap, -999);
		Arrays.fill(blockBiomeArray, (byte) -1);
	}

	/**
	 * Adds an entity to the chunk. Args: entity
	 */
	public void addEntity(Entity p_76612_1_) {
		hasEntities = true;
		final int var2 = MathHelper.floor_double(p_76612_1_.posX / 16.0D);
		final int var3 = MathHelper.floor_double(p_76612_1_.posZ / 16.0D);

		if (var2 != xPosition || var3 != zPosition) {
			logger.warn("Wrong location! " + p_76612_1_ + " (at " + var2 + ", "
					+ var3 + " instead of " + xPosition + ", " + zPosition
					+ ")");
			Thread.dumpStack();
		}

		int var4 = MathHelper.floor_double(p_76612_1_.posY / 16.0D);

		if (var4 < 0) {
			var4 = 0;
		}

		if (var4 >= entityLists.length) {
			var4 = entityLists.length - 1;
		}

		p_76612_1_.addedToChunk = true;
		p_76612_1_.chunkCoordX = xPosition;
		p_76612_1_.chunkCoordY = var4;
		p_76612_1_.chunkCoordZ = zPosition;
		entityLists[var4].add(p_76612_1_);
	}

	public void addTileEntity(TileEntity p_150813_1_) {
		final int var2 = p_150813_1_.field_145851_c - xPosition * 16;
		final int var3 = p_150813_1_.field_145848_d;
		final int var4 = p_150813_1_.field_145849_e - zPosition * 16;
		func_150812_a(var2, var3, var4, p_150813_1_);

		if (isChunkLoaded) {
			worldObj.field_147482_g.add(p_150813_1_);
		}
	}

	/**
	 * Returns whether is not a block above this one blocking sight to the sky
	 * (done via checking against the heightmap)
	 */
	public boolean canBlockSeeTheSky(int p_76619_1_, int p_76619_2_,
			int p_76619_3_) {
		return p_76619_2_ >= heightMap[p_76619_3_ << 4 | p_76619_1_];
	}

	/**
	 * Checks the height of a block next to a sky-visible block and schedules a
	 * lighting update as necessary.
	 */
	private void checkSkylightNeighborHeight(int p_76599_1_, int p_76599_2_,
			int p_76599_3_) {
		final int var4 = worldObj.getHeightValue(p_76599_1_, p_76599_2_);

		if (var4 > p_76599_3_) {
			updateSkylightNeighborHeight(p_76599_1_, p_76599_2_, p_76599_3_,
					var4 + 1);
		} else if (var4 < p_76599_3_) {
			updateSkylightNeighborHeight(p_76599_1_, p_76599_2_, var4,
					p_76599_3_ + 1);
		}
	}

	/**
	 * Called once-per-chunk-per-tick, and advances the round-robin relight
	 * check index per-storage-block by up to 8 blocks at a time. In a
	 * worst-case scenario, can potentially take up to 1.6 seconds, calculated
	 * via (4096/(8*16))/20, to re-check all blocks in a chunk, which could
	 * explain both lagging light updates in certain cases as well as Nether
	 * relight
	 */
	public void enqueueRelightChecks() {
		for (int var1 = 0; var1 < 8; ++var1) {
			if (queuedLightChecks >= 4096)
				return;

			final int var2 = queuedLightChecks % 16;
			final int var3 = queuedLightChecks / 16 % 16;
			final int var4 = queuedLightChecks / 256;
			++queuedLightChecks;
			final int var5 = (xPosition << 4) + var3;
			final int var6 = (zPosition << 4) + var4;

			for (int var7 = 0; var7 < 16; ++var7) {
				final int var8 = (var2 << 4) + var7;

				if (storageArrays[var2] == null
						&& (var7 == 0 || var7 == 15 || var3 == 0 || var3 == 15
								|| var4 == 0 || var4 == 15)
						|| storageArrays[var2] != null
						&& storageArrays[var2].func_150819_a(var3, var7, var4)
								.getMaterial() == Material.air) {
					if (worldObj.getBlock(var5, var8 - 1, var6).getLightValue() > 0) {
						worldObj.func_147451_t(var5, var8 - 1, var6);
					}

					if (worldObj.getBlock(var5, var8 + 1, var6).getLightValue() > 0) {
						worldObj.func_147451_t(var5, var8 + 1, var6);
					}

					if (worldObj.getBlock(var5 - 1, var8, var6).getLightValue() > 0) {
						worldObj.func_147451_t(var5 - 1, var8, var6);
					}

					if (worldObj.getBlock(var5 + 1, var8, var6).getLightValue() > 0) {
						worldObj.func_147451_t(var5 + 1, var8, var6);
					}

					if (worldObj.getBlock(var5, var8, var6 - 1).getLightValue() > 0) {
						worldObj.func_147451_t(var5, var8, var6 - 1);
					}

					if (worldObj.getBlock(var5, var8, var6 + 1).getLightValue() > 0) {
						worldObj.func_147451_t(var5, var8, var6 + 1);
					}

					worldObj.func_147451_t(var5, var8, var6);
				}
			}
		}
	}

	/**
	 * Initialise this chunk with new binary data
	 */
	public void fillChunk(byte[] p_76607_1_, int p_76607_2_, int p_76607_3_,
			boolean p_76607_4_) {
		int var5 = 0;
		final boolean var6 = !worldObj.provider.hasNoSky;
		int var7;

		for (var7 = 0; var7 < storageArrays.length; ++var7) {
			if ((p_76607_2_ & 1 << var7) != 0) {
				if (storageArrays[var7] == null) {
					storageArrays[var7] = new ExtendedBlockStorage(var7 << 4,
							var6);
				}

				final byte[] var8 = storageArrays[var7].getBlockLSBArray();
				System.arraycopy(p_76607_1_, var5, var8, 0, var8.length);
				var5 += var8.length;
			} else if (p_76607_4_ && storageArrays[var7] != null) {
				storageArrays[var7] = null;
			}
		}

		NibbleArray var10;

		for (var7 = 0; var7 < storageArrays.length; ++var7) {
			if ((p_76607_2_ & 1 << var7) != 0 && storageArrays[var7] != null) {
				var10 = storageArrays[var7].getMetadataArray();
				System.arraycopy(p_76607_1_, var5, var10.data, 0,
						var10.data.length);
				var5 += var10.data.length;
			}
		}

		for (var7 = 0; var7 < storageArrays.length; ++var7) {
			if ((p_76607_2_ & 1 << var7) != 0 && storageArrays[var7] != null) {
				var10 = storageArrays[var7].getBlocklightArray();
				System.arraycopy(p_76607_1_, var5, var10.data, 0,
						var10.data.length);
				var5 += var10.data.length;
			}
		}

		if (var6) {
			for (var7 = 0; var7 < storageArrays.length; ++var7) {
				if ((p_76607_2_ & 1 << var7) != 0
						&& storageArrays[var7] != null) {
					var10 = storageArrays[var7].getSkylightArray();
					System.arraycopy(p_76607_1_, var5, var10.data, 0,
							var10.data.length);
					var5 += var10.data.length;
				}
			}
		}

		for (var7 = 0; var7 < storageArrays.length; ++var7) {
			if ((p_76607_3_ & 1 << var7) != 0) {
				if (storageArrays[var7] == null) {
					var5 += 2048;
				} else {
					var10 = storageArrays[var7].getBlockMSBArray();

					if (var10 == null) {
						var10 = storageArrays[var7].createBlockMSBArray();
					}

					System.arraycopy(p_76607_1_, var5, var10.data, 0,
							var10.data.length);
					var5 += var10.data.length;
				}
			} else if (p_76607_4_ && storageArrays[var7] != null
					&& storageArrays[var7].getBlockMSBArray() != null) {
				storageArrays[var7].clearMSBArray();
			}
		}

		if (p_76607_4_) {
			System.arraycopy(p_76607_1_, var5, blockBiomeArray, 0,
					blockBiomeArray.length);
		}

		for (var7 = 0; var7 < storageArrays.length; ++var7) {
			if (storageArrays[var7] != null && (p_76607_2_ & 1 << var7) != 0) {
				storageArrays[var7].removeInvalidBlocks();
			}
		}

		isLightPopulated = true;
		isTerrainPopulated = true;
		generateHeightMap();
		final Iterator var9 = chunkTileEntityMap.values().iterator();

		while (var9.hasNext()) {
			final TileEntity var11 = (TileEntity) var9.next();
			var11.updateContainingBlockInfo();
		}
	}

	private void func_150801_a(int p_150801_1_) {
		if (isTerrainPopulated) {
			int var2;

			if (p_150801_1_ == 3) {
				for (var2 = 0; var2 < 16; ++var2) {
					func_150811_f(15, var2);
				}
			} else if (p_150801_1_ == 1) {
				for (var2 = 0; var2 < 16; ++var2) {
					func_150811_f(0, var2);
				}
			} else if (p_150801_1_ == 0) {
				for (var2 = 0; var2 < 16; ++var2) {
					func_150811_f(var2, 15);
				}
			} else if (p_150801_1_ == 2) {
				for (var2 = 0; var2 < 16; ++var2) {
					func_150811_f(var2, 0);
				}
			}
		}
	}

	public boolean func_150802_k() {
		return field_150815_m && isTerrainPopulated && isLightPopulated;
	}

	public void func_150804_b(boolean p_150804_1_) {
		if (isGapLightingUpdated && !worldObj.provider.hasNoSky && !p_150804_1_) {
			recheckGaps(worldObj.isClient);
		}

		field_150815_m = true;

		if (!isLightPopulated && isTerrainPopulated) {
			func_150809_p();
		}
	}

	public TileEntity func_150806_e(int p_150806_1_, int p_150806_2_,
			int p_150806_3_) {
		final ChunkPosition var4 = new ChunkPosition(p_150806_1_, p_150806_2_,
				p_150806_3_);
		TileEntity var5 = (TileEntity) chunkTileEntityMap.get(var4);

		if (var5 == null) {
			final Block var6 = func_150810_a(p_150806_1_, p_150806_2_,
					p_150806_3_);

			if (!var6.hasTileEntity())
				return null;

			var5 = ((ITileEntityProvider) var6).createNewTileEntity(worldObj,
					getBlockMetadata(p_150806_1_, p_150806_2_, p_150806_3_));
			worldObj.setTileEntity(xPosition * 16 + p_150806_1_, p_150806_2_,
					zPosition * 16 + p_150806_3_, var5);
		}

		if (var5 != null && var5.isInvalid()) {
			chunkTileEntityMap.remove(var4);
			return null;
		} else
			return var5;
	}

	public boolean func_150807_a(int p_150807_1_, int p_150807_2_,
			int p_150807_3_, Block p_150807_4_, int p_150807_5_) {
		final int var6 = p_150807_3_ << 4 | p_150807_1_;

		if (p_150807_2_ >= precipitationHeightMap[var6] - 1) {
			precipitationHeightMap[var6] = -999;
		}

		final int var7 = heightMap[var6];
		final Block var8 = func_150810_a(p_150807_1_, p_150807_2_, p_150807_3_);
		final int var9 = getBlockMetadata(p_150807_1_, p_150807_2_, p_150807_3_);

		if (var8 == p_150807_4_ && var9 == p_150807_5_)
			return false;
		else {
			ExtendedBlockStorage var10 = storageArrays[p_150807_2_ >> 4];
			boolean var11 = false;

			if (var10 == null) {
				if (p_150807_4_ == Blocks.air)
					return false;

				var10 = storageArrays[p_150807_2_ >> 4] = new ExtendedBlockStorage(
						p_150807_2_ >> 4 << 4, !worldObj.provider.hasNoSky);
				var11 = p_150807_2_ >= var7;
			}

			final int var12 = xPosition * 16 + p_150807_1_;
			final int var13 = zPosition * 16 + p_150807_3_;

			if (!worldObj.isClient) {
				var8.onBlockPreDestroy(worldObj, var12, p_150807_2_, var13,
						var9);
			}

			var10.func_150818_a(p_150807_1_, p_150807_2_ & 15, p_150807_3_,
					p_150807_4_);

			if (!worldObj.isClient) {
				var8.breakBlock(worldObj, var12, p_150807_2_, var13, var8, var9);
			} else if (var8 instanceof ITileEntityProvider
					&& var8 != p_150807_4_) {
				worldObj.removeTileEntity(var12, p_150807_2_, var13);
			}

			if (var10.func_150819_a(p_150807_1_, p_150807_2_ & 15, p_150807_3_) != p_150807_4_)
				return false;
			else {
				var10.setExtBlockMetadata(p_150807_1_, p_150807_2_ & 15,
						p_150807_3_, p_150807_5_);

				if (var11) {
					generateSkylightMap();
				} else {
					final int var14 = p_150807_4_.getLightOpacity();
					final int var15 = var8.getLightOpacity();

					if (var14 > 0) {
						if (p_150807_2_ >= var7) {
							relightBlock(p_150807_1_, p_150807_2_ + 1,
									p_150807_3_);
						}
					} else if (p_150807_2_ == var7 - 1) {
						relightBlock(p_150807_1_, p_150807_2_, p_150807_3_);
					}

					if (var14 != var15
							&& (var14 < var15
									|| getSavedLightValue(EnumSkyBlock.Sky,
											p_150807_1_, p_150807_2_,
											p_150807_3_) > 0 || getSavedLightValue(
									EnumSkyBlock.Block, p_150807_1_,
									p_150807_2_, p_150807_3_) > 0)) {
						propagateSkylightOcclusion(p_150807_1_, p_150807_3_);
					}
				}

				TileEntity var16;

				if (var8 instanceof ITileEntityProvider) {
					var16 = func_150806_e(p_150807_1_, p_150807_2_, p_150807_3_);

					if (var16 != null) {
						var16.updateContainingBlockInfo();
					}
				}

				if (!worldObj.isClient) {
					p_150807_4_.onBlockAdded(worldObj, var12, p_150807_2_,
							var13);
				}

				if (p_150807_4_ instanceof ITileEntityProvider) {
					var16 = func_150806_e(p_150807_1_, p_150807_2_, p_150807_3_);

					if (var16 == null) {
						var16 = ((ITileEntityProvider) p_150807_4_)
								.createNewTileEntity(worldObj, p_150807_5_);
						worldObj.setTileEntity(var12, p_150807_2_, var13, var16);
					}

					if (var16 != null) {
						var16.updateContainingBlockInfo();
					}
				}

				isModified = true;
				return true;
			}
		}
	}

	public int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_) {
		return func_150810_a(p_150808_1_, p_150808_2_, p_150808_3_)
				.getLightOpacity();
	}

	public void func_150809_p() {
		isTerrainPopulated = true;
		isLightPopulated = true;

		if (!worldObj.provider.hasNoSky) {
			if (worldObj.checkChunksExist(xPosition * 16 - 1, 0,
					zPosition * 16 - 1, xPosition * 16 + 1, 63,
					zPosition * 16 + 1)) {
				for (int var1 = 0; var1 < 16; ++var1) {
					for (int var2 = 0; var2 < 16; ++var2) {
						if (!func_150811_f(var1, var2)) {
							isLightPopulated = false;
							break;
						}
					}
				}

				if (isLightPopulated) {
					Chunk var3 = worldObj.getChunkFromBlockCoords(
							xPosition * 16 - 1, zPosition * 16);
					var3.func_150801_a(3);
					var3 = worldObj.getChunkFromBlockCoords(
							xPosition * 16 + 16, zPosition * 16);
					var3.func_150801_a(1);
					var3 = worldObj.getChunkFromBlockCoords(xPosition * 16,
							zPosition * 16 - 1);
					var3.func_150801_a(0);
					var3 = worldObj.getChunkFromBlockCoords(xPosition * 16,
							zPosition * 16 + 16);
					var3.func_150801_a(2);
				}
			} else {
				isLightPopulated = false;
			}
		}
	}

	public Block func_150810_a(final int p_150810_1_, final int p_150810_2_,
			final int p_150810_3_) {
		Block var4 = Blocks.air;

		if (p_150810_2_ >> 4 < storageArrays.length) {
			final ExtendedBlockStorage var5 = storageArrays[p_150810_2_ >> 4];

			if (var5 != null) {
				try {
					var4 = var5.func_150819_a(p_150810_1_, p_150810_2_ & 15,
							p_150810_3_);
				} catch (final Throwable var9) {
					final CrashReport var7 = CrashReport.makeCrashReport(var9,
							"Getting block");
					final CrashReportCategory var8 = var7
							.makeCategory("Block being got");
					var8.addCrashSectionCallable("Location", new Callable() {

						@Override
						public String call() {
							return CrashReportCategory.getLocationInfo(
									p_150810_1_, p_150810_2_, p_150810_3_);
						}
					});
					throw new ReportedException(var7);
				}
			}
		}

		return var4;
	}

	private boolean func_150811_f(int p_150811_1_, int p_150811_2_) {
		final int var3 = getTopFilledSegment();
		boolean var4 = false;
		boolean var5 = false;
		int var6;

		for (var6 = var3 + 16 - 1; var6 > 63 || var6 > 0 && !var5; --var6) {
			final int var7 = func_150808_b(p_150811_1_, var6, p_150811_2_);

			if (var7 == 255 && var6 < 63) {
				var5 = true;
			}

			if (!var4 && var7 > 0) {
				var4 = true;
			} else if (var4
					&& var7 == 0
					&& !worldObj.func_147451_t(xPosition * 16 + p_150811_1_,
							var6, zPosition * 16 + p_150811_2_))
				return false;
		}

		for (; var6 > 0; --var6) {
			if (func_150810_a(p_150811_1_, var6, p_150811_2_).getLightValue() > 0) {
				worldObj.func_147451_t(xPosition * 16 + p_150811_1_, var6,
						zPosition * 16 + p_150811_2_);
			}
		}

		return true;
	}

	public void func_150812_a(int p_150812_1_, int p_150812_2_,
			int p_150812_3_, TileEntity p_150812_4_) {
		final ChunkPosition var5 = new ChunkPosition(p_150812_1_, p_150812_2_,
				p_150812_3_);
		p_150812_4_.setWorldObj(worldObj);
		p_150812_4_.field_145851_c = xPosition * 16 + p_150812_1_;
		p_150812_4_.field_145848_d = p_150812_2_;
		p_150812_4_.field_145849_e = zPosition * 16 + p_150812_3_;

		if (func_150810_a(p_150812_1_, p_150812_2_, p_150812_3_) instanceof ITileEntityProvider) {
			if (chunkTileEntityMap.containsKey(var5)) {
				((TileEntity) chunkTileEntityMap.get(var5)).invalidate();
			}

			p_150812_4_.validate();
			chunkTileEntityMap.put(var5, p_150812_4_);
		}
	}

	/**
	 * Generates the height map for a chunk from scratch
	 */
	public void generateHeightMap() {
		final int var1 = getTopFilledSegment();
		heightMapMinimum = Integer.MAX_VALUE;

		for (int var2 = 0; var2 < 16; ++var2) {
			int var3 = 0;

			while (var3 < 16) {
				precipitationHeightMap[var2 + (var3 << 4)] = -999;
				int var4 = var1 + 16 - 1;

				while (true) {
					if (var4 > 0) {
						final Block var5 = func_150810_a(var2, var4 - 1, var3);

						if (var5.getLightOpacity() == 0) {
							--var4;
							continue;
						}

						heightMap[var3 << 4 | var2] = var4;

						if (var4 < heightMapMinimum) {
							heightMapMinimum = var4;
						}
					}

					++var3;
					break;
				}
			}
		}

		isModified = true;
	}

	/**
	 * Generates the initial skylight map for the chunk upon generation or load.
	 */
	public void generateSkylightMap() {
		final int var1 = getTopFilledSegment();
		heightMapMinimum = Integer.MAX_VALUE;

		for (int var2 = 0; var2 < 16; ++var2) {
			int var3 = 0;

			while (var3 < 16) {
				precipitationHeightMap[var2 + (var3 << 4)] = -999;
				int var4 = var1 + 16 - 1;

				while (true) {
					if (var4 > 0) {
						if (func_150808_b(var2, var4 - 1, var3) == 0) {
							--var4;
							continue;
						}

						heightMap[var3 << 4 | var2] = var4;

						if (var4 < heightMapMinimum) {
							heightMapMinimum = var4;
						}
					}

					if (!worldObj.provider.hasNoSky) {
						var4 = 15;
						int var5 = var1 + 16 - 1;

						do {
							int var6 = func_150808_b(var2, var5, var3);

							if (var6 == 0 && var4 != 15) {
								var6 = 1;
							}

							var4 -= var6;

							if (var4 > 0) {
								final ExtendedBlockStorage var7 = storageArrays[var5 >> 4];

								if (var7 != null) {
									var7.setExtSkylightValue(var2, var5 & 15,
											var3, var4);
									worldObj.func_147479_m((xPosition << 4)
											+ var2, var5, (zPosition << 4)
											+ var3);
								}
							}

							--var5;
						} while (var5 > 0 && var4 > 0);
					}

					++var3;
					break;
				}
			}
		}

		isModified = true;
	}

	/**
	 * Returns whether the ExtendedBlockStorages containing levels (in blocks)
	 * from arg 1 to arg 2 are fully empty (true) or not (false).
	 */
	public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_) {
		if (p_76606_1_ < 0) {
			p_76606_1_ = 0;
		}

		if (p_76606_2_ >= 256) {
			p_76606_2_ = 255;
		}

		for (int var3 = p_76606_1_; var3 <= p_76606_2_; var3 += 16) {
			final ExtendedBlockStorage var4 = storageArrays[var3 >> 4];

			if (var4 != null && !var4.isEmpty())
				return false;
		}

		return true;
	}

	/**
	 * Returns an array containing a 16x16 mapping on the X/Z of block positions
	 * in this Chunk to biome IDs.
	 */
	public byte[] getBiomeArray() {
		return blockBiomeArray;
	}

	/**
	 * This method retrieves the biome at a set of coordinates
	 */
	public BiomeGenBase getBiomeGenForWorldCoords(int p_76591_1_,
			int p_76591_2_, WorldChunkManager p_76591_3_) {
		int var4 = blockBiomeArray[p_76591_2_ << 4 | p_76591_1_] & 255;

		if (var4 == 255) {
			final BiomeGenBase var5 = p_76591_3_.getBiomeGenAt((xPosition << 4)
					+ p_76591_1_, (zPosition << 4) + p_76591_2_);
			var4 = var5.biomeID;
			blockBiomeArray[p_76591_2_ << 4 | p_76591_1_] = (byte) (var4 & 255);
		}

		return BiomeGenBase.func_150568_d(var4) == null ? BiomeGenBase.plains
				: BiomeGenBase.func_150568_d(var4);
	}

	/**
	 * Gets the amount of light on a block taking into account sunlight
	 */
	public int getBlockLightValue(int p_76629_1_, int p_76629_2_,
			int p_76629_3_, int p_76629_4_) {
		final ExtendedBlockStorage var5 = storageArrays[p_76629_2_ >> 4];

		if (var5 == null)
			return !worldObj.provider.hasNoSky
					&& p_76629_4_ < EnumSkyBlock.Sky.defaultLightValue ? EnumSkyBlock.Sky.defaultLightValue
					- p_76629_4_
					: 0;
		else {
			int var6 = worldObj.provider.hasNoSky ? 0 : var5
					.getExtSkylightValue(p_76629_1_, p_76629_2_ & 15,
							p_76629_3_);

			if (var6 > 0) {
				isLit = true;
			}

			var6 -= p_76629_4_;
			final int var7 = var5.getExtBlocklightValue(p_76629_1_,
					p_76629_2_ & 15, p_76629_3_);

			if (var7 > var6) {
				var6 = var7;
			}

			return var6;
		}
	}

	/**
	 * Return the metadata corresponding to the given coordinates inside a
	 * chunk.
	 */
	public int getBlockMetadata(int p_76628_1_, int p_76628_2_, int p_76628_3_) {
		if (p_76628_2_ >> 4 >= storageArrays.length)
			return 0;
		else {
			final ExtendedBlockStorage var4 = storageArrays[p_76628_2_ >> 4];
			return var4 != null ? var4.getExtBlockMetadata(p_76628_1_,
					p_76628_2_ & 15, p_76628_3_) : 0;
		}
	}

	/**
	 * Returns the ExtendedBlockStorage array for this Chunk.
	 */
	public ExtendedBlockStorage[] getBlockStorageArray() {
		return storageArrays;
	}

	/**
	 * Gets a ChunkCoordIntPair representing the Chunk's position.
	 */
	public ChunkCoordIntPair getChunkCoordIntPair() {
		return new ChunkCoordIntPair(xPosition, zPosition);
	}

	/**
	 * Gets all entities that can be assigned to the specified class. Args:
	 * entityClass, aabb, listToFill
	 */
	public void getEntitiesOfTypeWithinAAAB(Class p_76618_1_,
			AxisAlignedBB p_76618_2_, List p_76618_3_,
			IEntitySelector p_76618_4_) {
		int var5 = MathHelper.floor_double((p_76618_2_.minY - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((p_76618_2_.maxY + 2.0D) / 16.0D);
		var5 = MathHelper.clamp_int(var5, 0, entityLists.length - 1);
		var6 = MathHelper.clamp_int(var6, 0, entityLists.length - 1);

		for (int var7 = var5; var7 <= var6; ++var7) {
			final List var8 = entityLists[var7];

			for (int var9 = 0; var9 < var8.size(); ++var9) {
				final Entity var10 = (Entity) var8.get(var9);

				if (p_76618_1_.isAssignableFrom(var10.getClass())
						&& var10.boundingBox.intersectsWith(p_76618_2_)
						&& (p_76618_4_ == null || p_76618_4_
								.isEntityApplicable(var10))) {
					p_76618_3_.add(var10);
				}
			}
		}
	}

	/**
	 * Fills the given list of all entities that intersect within the given
	 * bounding box that aren't the passed entity Args: entity, aabb, listToFill
	 */
	public void getEntitiesWithinAABBForEntity(Entity p_76588_1_,
			AxisAlignedBB p_76588_2_, List p_76588_3_,
			IEntitySelector p_76588_4_) {
		int var5 = MathHelper.floor_double((p_76588_2_.minY - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((p_76588_2_.maxY + 2.0D) / 16.0D);
		var5 = MathHelper.clamp_int(var5, 0, entityLists.length - 1);
		var6 = MathHelper.clamp_int(var6, 0, entityLists.length - 1);

		for (int var7 = var5; var7 <= var6; ++var7) {
			final List var8 = entityLists[var7];

			for (int var9 = 0; var9 < var8.size(); ++var9) {
				Entity var10 = (Entity) var8.get(var9);

				if (var10 != p_76588_1_
						&& var10.boundingBox.intersectsWith(p_76588_2_)
						&& (p_76588_4_ == null || p_76588_4_
								.isEntityApplicable(var10))) {
					p_76588_3_.add(var10);
					final Entity[] var11 = var10.getParts();

					if (var11 != null) {
						for (final Entity element : var11) {
							var10 = element;

							if (var10 != p_76588_1_
									&& var10.boundingBox
											.intersectsWith(p_76588_2_)
									&& (p_76588_4_ == null || p_76588_4_
											.isEntityApplicable(var10))) {
								p_76588_3_.add(var10);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the value in the height map at this x, z coordinate in the chunk
	 */
	public int getHeightValue(int p_76611_1_, int p_76611_2_) {
		return heightMap[p_76611_2_ << 4 | p_76611_1_];
	}

	/**
	 * Gets the height to which rain/snow will fall. Calculates it if not
	 * already stored.
	 */
	public int getPrecipitationHeight(int p_76626_1_, int p_76626_2_) {
		final int var3 = p_76626_1_ | p_76626_2_ << 4;
		int var4 = precipitationHeightMap[var3];

		if (var4 == -999) {
			int var5 = getTopFilledSegment() + 15;
			var4 = -1;

			while (var5 > 0 && var4 == -1) {
				final Block var6 = func_150810_a(p_76626_1_, var5, p_76626_2_);
				final Material var7 = var6.getMaterial();

				if (!var7.blocksMovement() && !var7.isLiquid()) {
					--var5;
				} else {
					var4 = var5 + 1;
				}
			}

			precipitationHeightMap[var3] = var4;
		}

		return var4;
	}

	public Random getRandomWithSeed(long p_76617_1_) {
		return new Random(worldObj.getSeed() + xPosition * xPosition * 4987142
				+ xPosition * 5947611 + zPosition * zPosition * 4392871L
				+ zPosition * 389711 ^ p_76617_1_);
	}

	/**
	 * Gets the amount of light saved in this block (doesn't adjust for
	 * daylight)
	 */
	public int getSavedLightValue(EnumSkyBlock p_76614_1_, int p_76614_2_,
			int p_76614_3_, int p_76614_4_) {
		final ExtendedBlockStorage var5 = storageArrays[p_76614_3_ >> 4];
		return var5 == null ? canBlockSeeTheSky(p_76614_2_, p_76614_3_,
				p_76614_4_) ? p_76614_1_.defaultLightValue : 0
				: p_76614_1_ == EnumSkyBlock.Sky ? worldObj.provider.hasNoSky ? 0
						: var5.getExtSkylightValue(p_76614_2_, p_76614_3_ & 15,
								p_76614_4_)
						: p_76614_1_ == EnumSkyBlock.Block ? var5
								.getExtBlocklightValue(p_76614_2_,
										p_76614_3_ & 15, p_76614_4_)
								: p_76614_1_.defaultLightValue;
	}

	/**
	 * Returns the topmost ExtendedBlockStorage instance for this Chunk that
	 * actually contains a block.
	 */
	public int getTopFilledSegment() {
		for (int var1 = storageArrays.length - 1; var1 >= 0; --var1) {
			if (storageArrays[var1] != null)
				return storageArrays[var1].getYLocation();
		}

		return 0;
	}

	/**
	 * Checks whether the chunk is at the X/Z location specified
	 */
	public boolean isAtLocation(int p_76600_1_, int p_76600_2_) {
		return p_76600_1_ == xPosition && p_76600_2_ == zPosition;
	}

	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns true if this Chunk needs to be saved
	 */
	public boolean needsSaving(boolean p_76601_1_) {
		if (p_76601_1_) {
			if (hasEntities && worldObj.getTotalWorldTime() != lastSaveTime
					|| isModified)
				return true;
		} else if (hasEntities
				&& worldObj.getTotalWorldTime() >= lastSaveTime + 600L)
			return true;

		return isModified;
	}

	/**
	 * Called when this Chunk is loaded by the ChunkProvider
	 */
	public void onChunkLoad() {
		isChunkLoaded = true;
		worldObj.func_147448_a(chunkTileEntityMap.values());

		for (final List entityList : entityLists) {
			final Iterator var2 = entityList.iterator();

			while (var2.hasNext()) {
				final Entity var3 = (Entity) var2.next();
				var3.onChunkLoad();
			}

			worldObj.addLoadedEntities(entityList);
		}
	}

	/**
	 * Called when this Chunk is unloaded by the ChunkProvider
	 */
	public void onChunkUnload() {
		isChunkLoaded = false;
		final Iterator var1 = chunkTileEntityMap.values().iterator();

		while (var1.hasNext()) {
			final TileEntity var2 = (TileEntity) var1.next();
			worldObj.func_147457_a(var2);
		}

		for (final List entityList : entityLists) {
			worldObj.unloadEntities(entityList);
		}
	}

	public void populateChunk(IChunkProvider p_76624_1_,
			IChunkProvider p_76624_2_, int p_76624_3_, int p_76624_4_) {
		if (!isTerrainPopulated
				&& p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1)
				&& p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1)
				&& p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_)) {
			p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
		}

		if (p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_)
				&& !p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_).isTerrainPopulated
				&& p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1)
				&& p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1)
				&& p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1)) {
			p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
		}

		if (p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1)
				&& !p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1).isTerrainPopulated
				&& p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1)
				&& p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1)
				&& p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_)) {
			p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
		}

		if (p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1)
				&& !p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1).isTerrainPopulated
				&& p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1)
				&& p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_)) {
			p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
		}
	}

	/**
	 * Propagates a given sky-visible block's light value downward and upward to
	 * neighboring blocks as necessary.
	 */
	private void propagateSkylightOcclusion(int p_76595_1_, int p_76595_2_) {
		updateSkylightColumns[p_76595_1_ + p_76595_2_ * 16] = true;
		isGapLightingUpdated = true;
	}

	private void recheckGaps(boolean p_150803_1_) {
		worldObj.theProfiler.startSection("recheckGaps");

		if (worldObj.doChunksNearChunkExist(xPosition * 16 + 8, 0,
				zPosition * 16 + 8, 16)) {
			for (int var2 = 0; var2 < 16; ++var2) {
				for (int var3 = 0; var3 < 16; ++var3) {
					if (updateSkylightColumns[var2 + var3 * 16]) {
						updateSkylightColumns[var2 + var3 * 16] = false;
						final int var4 = getHeightValue(var2, var3);
						final int var5 = xPosition * 16 + var2;
						final int var6 = zPosition * 16 + var3;
						int var7 = worldObj.getChunkHeightMapMinimum(var5 - 1,
								var6);
						final int var8 = worldObj.getChunkHeightMapMinimum(
								var5 + 1, var6);
						final int var9 = worldObj.getChunkHeightMapMinimum(
								var5, var6 - 1);
						final int var10 = worldObj.getChunkHeightMapMinimum(
								var5, var6 + 1);

						if (var8 < var7) {
							var7 = var8;
						}

						if (var9 < var7) {
							var7 = var9;
						}

						if (var10 < var7) {
							var7 = var10;
						}

						checkSkylightNeighborHeight(var5, var6, var7);
						checkSkylightNeighborHeight(var5 - 1, var6, var4);
						checkSkylightNeighborHeight(var5 + 1, var6, var4);
						checkSkylightNeighborHeight(var5, var6 - 1, var4);
						checkSkylightNeighborHeight(var5, var6 + 1, var4);

						if (p_150803_1_) {
							worldObj.theProfiler.endSection();
							return;
						}
					}
				}
			}

			isGapLightingUpdated = false;
		}

		worldObj.theProfiler.endSection();
	}

	/**
	 * Initiates the recalculation of both the block-light and sky-light for a
	 * given block inside a chunk.
	 */
	private void relightBlock(int p_76615_1_, int p_76615_2_, int p_76615_3_) {
		final int var4 = heightMap[p_76615_3_ << 4 | p_76615_1_] & 255;
		int var5 = var4;

		if (p_76615_2_ > var4) {
			var5 = p_76615_2_;
		}

		while (var5 > 0 && func_150808_b(p_76615_1_, var5 - 1, p_76615_3_) == 0) {
			--var5;
		}

		if (var5 != var4) {
			worldObj.markBlocksDirtyVertical(p_76615_1_ + xPosition * 16,
					p_76615_3_ + zPosition * 16, var5, var4);
			heightMap[p_76615_3_ << 4 | p_76615_1_] = var5;
			final int var6 = xPosition * 16 + p_76615_1_;
			final int var7 = zPosition * 16 + p_76615_3_;
			int var8;
			int var12;

			if (!worldObj.provider.hasNoSky) {
				ExtendedBlockStorage var9;

				if (var5 < var4) {
					for (var8 = var5; var8 < var4; ++var8) {
						var9 = storageArrays[var8 >> 4];

						if (var9 != null) {
							var9.setExtSkylightValue(p_76615_1_, var8 & 15,
									p_76615_3_, 15);
							worldObj.func_147479_m((xPosition << 4)
									+ p_76615_1_, var8, (zPosition << 4)
									+ p_76615_3_);
						}
					}
				} else {
					for (var8 = var4; var8 < var5; ++var8) {
						var9 = storageArrays[var8 >> 4];

						if (var9 != null) {
							var9.setExtSkylightValue(p_76615_1_, var8 & 15,
									p_76615_3_, 0);
							worldObj.func_147479_m((xPosition << 4)
									+ p_76615_1_, var8, (zPosition << 4)
									+ p_76615_3_);
						}
					}
				}

				var8 = 15;

				while (var5 > 0 && var8 > 0) {
					--var5;
					var12 = func_150808_b(p_76615_1_, var5, p_76615_3_);

					if (var12 == 0) {
						var12 = 1;
					}

					var8 -= var12;

					if (var8 < 0) {
						var8 = 0;
					}

					final ExtendedBlockStorage var10 = storageArrays[var5 >> 4];

					if (var10 != null) {
						var10.setExtSkylightValue(p_76615_1_, var5 & 15,
								p_76615_3_, var8);
					}
				}
			}

			var8 = heightMap[p_76615_3_ << 4 | p_76615_1_];
			var12 = var4;
			int var13 = var8;

			if (var8 < var4) {
				var12 = var8;
				var13 = var4;
			}

			if (var8 < heightMapMinimum) {
				heightMapMinimum = var8;
			}

			if (!worldObj.provider.hasNoSky) {
				updateSkylightNeighborHeight(var6 - 1, var7, var12, var13);
				updateSkylightNeighborHeight(var6 + 1, var7, var12, var13);
				updateSkylightNeighborHeight(var6, var7 - 1, var12, var13);
				updateSkylightNeighborHeight(var6, var7 + 1, var12, var13);
				updateSkylightNeighborHeight(var6, var7, var12, var13);
			}

			isModified = true;
		}
	}

	/**
	 * removes entity using its y chunk coordinate as its index
	 */
	public void removeEntity(Entity p_76622_1_) {
		removeEntityAtIndex(p_76622_1_, p_76622_1_.chunkCoordY);
	}

	/**
	 * Removes entity at the specified index from the entity array.
	 */
	public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_) {
		if (p_76608_2_ < 0) {
			p_76608_2_ = 0;
		}

		if (p_76608_2_ >= entityLists.length) {
			p_76608_2_ = entityLists.length - 1;
		}

		entityLists[p_76608_2_].remove(p_76608_1_);
	}

	public void removeTileEntity(int p_150805_1_, int p_150805_2_,
			int p_150805_3_) {
		final ChunkPosition var4 = new ChunkPosition(p_150805_1_, p_150805_2_,
				p_150805_3_);

		if (isChunkLoaded) {
			final TileEntity var5 = (TileEntity) chunkTileEntityMap
					.remove(var4);

			if (var5 != null) {
				var5.invalidate();
			}
		}
	}

	/**
	 * Resets the relight check index to 0 for this Chunk.
	 */
	public void resetRelightChecks() {
		queuedLightChecks = 0;
	}

	/**
	 * Accepts a 256-entry array that contains a 16x16 mapping on the X/Z plane
	 * of block positions in this Chunk to biome IDs.
	 */
	public void setBiomeArray(byte[] p_76616_1_) {
		blockBiomeArray = p_76616_1_;
	}

	/**
	 * Set the metadata of a block in the chunk
	 */
	public boolean setBlockMetadata(int p_76589_1_, int p_76589_2_,
			int p_76589_3_, int p_76589_4_) {
		final ExtendedBlockStorage var5 = storageArrays[p_76589_2_ >> 4];

		if (var5 == null)
			return false;
		else {
			final int var6 = var5.getExtBlockMetadata(p_76589_1_,
					p_76589_2_ & 15, p_76589_3_);

			if (var6 == p_76589_4_)
				return false;
			else {
				isModified = true;
				var5.setExtBlockMetadata(p_76589_1_, p_76589_2_ & 15,
						p_76589_3_, p_76589_4_);

				if (var5.func_150819_a(p_76589_1_, p_76589_2_ & 15, p_76589_3_) instanceof ITileEntityProvider) {
					final TileEntity var7 = func_150806_e(p_76589_1_,
							p_76589_2_, p_76589_3_);

					if (var7 != null) {
						var7.updateContainingBlockInfo();
						var7.blockMetadata = p_76589_4_;
					}
				}

				return true;
			}
		}
	}

	/**
	 * Sets the isModified flag for this Chunk
	 */
	public void setChunkModified() {
		isModified = true;
	}

	/**
	 * Sets the light value at the coordinate. If enumskyblock is set to sky it
	 * sets it in the skylightmap and if its a block then into the
	 * blocklightmap. Args enumSkyBlock, x, y, z, lightValue
	 */
	public void setLightValue(EnumSkyBlock p_76633_1_, int p_76633_2_,
			int p_76633_3_, int p_76633_4_, int p_76633_5_) {
		ExtendedBlockStorage var6 = storageArrays[p_76633_3_ >> 4];

		if (var6 == null) {
			var6 = storageArrays[p_76633_3_ >> 4] = new ExtendedBlockStorage(
					p_76633_3_ >> 4 << 4, !worldObj.provider.hasNoSky);
			generateSkylightMap();
		}

		isModified = true;

		if (p_76633_1_ == EnumSkyBlock.Sky) {
			if (!worldObj.provider.hasNoSky) {
				var6.setExtSkylightValue(p_76633_2_, p_76633_3_ & 15,
						p_76633_4_, p_76633_5_);
			}
		} else if (p_76633_1_ == EnumSkyBlock.Block) {
			var6.setExtBlocklightValue(p_76633_2_, p_76633_3_ & 15, p_76633_4_,
					p_76633_5_);
		}
	}

	public void setStorageArrays(ExtendedBlockStorage[] p_76602_1_) {
		storageArrays = p_76602_1_;
	}

	private void updateSkylightNeighborHeight(int p_76609_1_, int p_76609_2_,
			int p_76609_3_, int p_76609_4_) {
		if (p_76609_4_ > p_76609_3_
				&& worldObj.doChunksNearChunkExist(p_76609_1_, 0, p_76609_2_,
						16)) {
			for (int var5 = p_76609_3_; var5 < p_76609_4_; ++var5) {
				worldObj.updateLightByType(EnumSkyBlock.Sky, p_76609_1_, var5,
						p_76609_2_);
			}

			isModified = true;
		}
	}
}
