package net.minecraft.world.gen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

public class ChunkProviderServer implements IChunkProvider {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * used by unload100OldestChunks to iterate the loadedChunkHashMap for
	 * unload (underlying assumption, first in, first out)
	 */
	private final Set chunksToUnload = Collections
			.newSetFromMap(new ConcurrentHashMap());
	private final IChunkLoader currentChunkLoader;
	private final IChunkProvider currentChunkProvider;
	private final Chunk defaultEmptyChunk;

	/**
	 * if this is false, the defaultEmptyChunk will be returned by the provider
	 */
	public boolean loadChunkOnProvideRequest = true;
	private final LongHashMap loadedChunkHashMap = new LongHashMap();
	private final List loadedChunks = new ArrayList();
	private final WorldServer worldObj;

	public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_,
			IChunkProvider p_i1520_3_) {
		defaultEmptyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
		worldObj = p_i1520_1_;
		currentChunkLoader = p_i1520_2_;
		currentChunkProvider = p_i1520_3_;
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	@Override
	public boolean canSave() {
		return !worldObj.levelSaving;
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	@Override
	public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
		return loadedChunkHashMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(
				p_73149_1_, p_73149_2_));
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_,
			int p_147416_3_, int p_147416_4_, int p_147416_5_) {
		return currentChunkProvider.func_147416_a(p_147416_1_, p_147416_2_,
				p_147416_3_, p_147416_4_, p_147416_5_);
	}

	public List func_152380_a() {
		return loadedChunks;
	}

	@Override
	public int getLoadedChunkCount() {
		return loadedChunkHashMap.getNumHashElements();
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 */
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_,
			int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		return currentChunkProvider.getPossibleCreatures(p_73155_1_,
				p_73155_2_, p_73155_3_, p_73155_4_);
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		final long var3 = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
		chunksToUnload.remove(Long.valueOf(var3));
		Chunk var5 = (Chunk) loadedChunkHashMap.getValueByKey(var3);

		if (var5 == null) {
			var5 = safeLoadChunk(p_73158_1_, p_73158_2_);

			if (var5 == null) {
				if (currentChunkProvider == null) {
					var5 = defaultEmptyChunk;
				} else {
					try {
						var5 = currentChunkProvider.provideChunk(p_73158_1_,
								p_73158_2_);
					} catch (final Throwable var9) {
						final CrashReport var7 = CrashReport.makeCrashReport(
								var9, "Exception generating new chunk");
						final CrashReportCategory var8 = var7
								.makeCategory("Chunk to be generated");
						var8.addCrashSection(
								"Location",
								String.format(
										"%d,%d",
										new Object[] {
												Integer.valueOf(p_73158_1_),
												Integer.valueOf(p_73158_2_) }));
						var8.addCrashSection("Position hash",
								Long.valueOf(var3));
						var8.addCrashSection("Generator",
								currentChunkProvider.makeString());
						throw new ReportedException(var7);
					}
				}
			}

			loadedChunkHashMap.add(var3, var5);
			loadedChunks.add(var5);
			var5.onChunkLoad();
			var5.populateChunk(this, this, p_73158_1_, p_73158_2_);
		}

		return var5;
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	@Override
	public String makeString() {
		return "ServerChunkCache: " + loadedChunkHashMap.getNumHashElements()
				+ " Drop: " + chunksToUnload.size();
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_,
			int p_73153_3_) {
		final Chunk var4 = provideChunk(p_73153_2_, p_73153_3_);

		if (!var4.isTerrainPopulated) {
			var4.func_150809_p();

			if (currentChunkProvider != null) {
				currentChunkProvider.populate(p_73153_1_, p_73153_2_,
						p_73153_3_);
				var4.setChunkModified();
			}
		}
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		final Chunk var3 = (Chunk) loadedChunkHashMap
				.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_,
						p_73154_2_));
		return var3 == null ? !worldObj.findingSpawnPoint
				&& !loadChunkOnProvideRequest ? defaultEmptyChunk : loadChunk(
				p_73154_1_, p_73154_2_) : var3;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
	}

	/**
	 * used by loadChunk, but catches any exceptions if the load fails.
	 */
	private Chunk safeLoadChunk(int p_73239_1_, int p_73239_2_) {
		if (currentChunkLoader == null)
			return null;
		else {
			try {
				final Chunk var3 = currentChunkLoader.loadChunk(worldObj,
						p_73239_1_, p_73239_2_);

				if (var3 != null) {
					var3.lastSaveTime = worldObj.getTotalWorldTime();

					if (currentChunkProvider != null) {
						currentChunkProvider.recreateStructures(p_73239_1_,
								p_73239_2_);
					}
				}

				return var3;
			} catch (final Exception var4) {
				logger.error("Couldn\'t load chunk", var4);
				return null;
			}
		}
	}

	/**
	 * used by saveChunks, but catches any exceptions if the save fails.
	 */
	private void safeSaveChunk(Chunk p_73242_1_) {
		if (currentChunkLoader != null) {
			try {
				p_73242_1_.lastSaveTime = worldObj.getTotalWorldTime();
				currentChunkLoader.saveChunk(worldObj, p_73242_1_);
			} catch (final IOException var3) {
				logger.error("Couldn\'t save chunk", var3);
			} catch (final MinecraftException var4) {
				logger.error(
						"Couldn\'t save chunk; already in use by another instance of Minecraft?",
						var4);
			}
		}
	}

	/**
	 * used by saveChunks, but catches any exceptions if the save fails.
	 */
	private void safeSaveExtraChunkData(Chunk p_73243_1_) {
		if (currentChunkLoader != null) {
			try {
				currentChunkLoader.saveExtraChunkData(worldObj, p_73243_1_);
			} catch (final Exception var3) {
				logger.error("Couldn\'t save entities", var3);
			}
		}
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If
	 * passed false, save up to two chunks. Return true if all chunks have been
	 * saved.
	 */
	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
		int var3 = 0;
		final ArrayList var4 = Lists.newArrayList(loadedChunks);

		for (int var5 = 0; var5 < var4.size(); ++var5) {
			final Chunk var6 = (Chunk) var4.get(var5);

			if (p_73151_1_) {
				safeSaveExtraChunkData(var6);
			}

			if (var6.needsSaving(p_73151_1_)) {
				safeSaveChunk(var6);
				var6.isModified = false;
				++var3;

				if (var3 == 24 && !p_73151_1_)
					return false;
			}
		}

		return true;
	}

	/**
	 * Save extra data not associated with any Chunk. Not saved during autosave,
	 * only during world unload. Currently unimplemented.
	 */
	@Override
	public void saveExtraData() {
		if (currentChunkLoader != null) {
			currentChunkLoader.saveExtraData();
		}
	}

	/**
	 * marks all chunks for unload, ignoring those near the spawn
	 */
	public void unloadAllChunks() {
		final Iterator var1 = loadedChunks.iterator();

		while (var1.hasNext()) {
			final Chunk var2 = (Chunk) var1.next();
			unloadChunksIfNotNearSpawn(var2.xPosition, var2.zPosition);
		}
	}

	/**
	 * marks chunk for unload by "unload100OldestChunks" if there is no spawn
	 * point, or if the center of the chunk is outside 200 blocks (x or z) of
	 * the spawn
	 */
	public void unloadChunksIfNotNearSpawn(int p_73241_1_, int p_73241_2_) {
		if (worldObj.provider.canRespawnHere()) {
			final ChunkCoordinates var3 = worldObj.getSpawnPoint();
			final int var4 = p_73241_1_ * 16 + 8 - var3.posX;
			final int var5 = p_73241_2_ * 16 + 8 - var3.posZ;
			final short var6 = 128;

			if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
				chunksToUnload.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(
						p_73241_1_, p_73241_2_)));
			}
		} else {
			chunksToUnload.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(
					p_73241_1_, p_73241_2_)));
		}
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	@Override
	public boolean unloadQueuedChunks() {
		if (!worldObj.levelSaving) {
			for (int var1 = 0; var1 < 100; ++var1) {
				if (!chunksToUnload.isEmpty()) {
					final Long var2 = (Long) chunksToUnload.iterator().next();
					final Chunk var3 = (Chunk) loadedChunkHashMap
							.getValueByKey(var2.longValue());

					if (var3 != null) {
						var3.onChunkUnload();
						safeSaveChunk(var3);
						safeSaveExtraChunkData(var3);
						loadedChunks.remove(var3);
					}

					chunksToUnload.remove(var2);
					loadedChunkHashMap.remove(var2.longValue());
				}
			}

			if (currentChunkLoader != null) {
				currentChunkLoader.chunkTick();
			}
		}

		return currentChunkProvider.unloadQueuedChunks();
	}
}
