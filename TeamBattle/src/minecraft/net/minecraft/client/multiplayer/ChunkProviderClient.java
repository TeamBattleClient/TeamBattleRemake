package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderClient implements IChunkProvider {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The completely empty chunk used by ChunkProviderClient when chunkMapping
	 * doesn't contain the requested coordinates.
	 */
	private final Chunk blankChunk;

	/**
	 * This may have been intended to be an iterable version of all currently
	 * loaded chunks (MultiplayerChunkCache), with identical contents to
	 * chunkMapping's values. However it is never actually added to.
	 */
	private final List chunkListing = new ArrayList();

	/**
	 * The mapping between ChunkCoordinates and Chunks that ChunkProviderClient
	 * maintains.
	 */
	private final LongHashMap chunkMapping = new LongHashMap();

	/** Reference to the World object. */
	private final World worldObj;

	public ChunkProviderClient(World p_i1184_1_) {
		blankChunk = new EmptyChunk(p_i1184_1_, 0, 0);
		worldObj = p_i1184_1_;
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	@Override
	public boolean canSave() {
		return false;
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

	@Override
	public int getLoadedChunkCount() {
		return chunkListing.size();
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 */
	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_,
			int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		return null;
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	@Override
	public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
		final Chunk var3 = new Chunk(worldObj, p_73158_1_, p_73158_2_);
		chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_),
				var3);
		chunkListing.add(var3);
		var3.isChunkLoaded = true;
		return var3;
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	@Override
	public String makeString() {
		return "MultiplayerChunkCache: " + chunkMapping.getNumHashElements()
				+ ", " + chunkListing.size();
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_,
			int p_73153_3_) {
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		final Chunk var3 = (Chunk) chunkMapping.getValueByKey(ChunkCoordIntPair
				.chunkXZ2Int(p_73154_1_, p_73154_2_));
		return var3 == null ? blankChunk : var3;
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
	 * Unload chunk from ChunkProviderClient's hashmap. Called in response to a
	 * Packet50PreChunk with its mode field set to false
	 */
	public void unloadChunk(int p_73234_1_, int p_73234_2_) {
		final Chunk var3 = provideChunk(p_73234_1_, p_73234_2_);

		if (!var3.isEmpty()) {
			var3.onChunkUnload();
		}

		chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_,
				p_73234_2_));
		chunkListing.remove(var3);
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	@Override
	public boolean unloadQueuedChunks() {
		final long var1 = System.currentTimeMillis();
		final Iterator var3 = chunkListing.iterator();

		while (var3.hasNext()) {
			final Chunk var4 = (Chunk) var3.next();
			var4.func_150804_b(System.currentTimeMillis() - var1 > 5L);
		}

		if (System.currentTimeMillis() - var1 > 100L) {
			logger.info(
					"Warning: Clientside chunk ticking took {} ms",
					new Object[] { Long.valueOf(System.currentTimeMillis()
							- var1) });
		}

		return false;
	}
}
