package net.minecraft.world.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;

public class ChunkProviderFlat implements IChunkProvider {
	private final Block[] cachedBlockIDs = new Block[256];
	private final byte[] cachedBlockMetadata = new byte[256];
	private final FlatGeneratorInfo flatWorldGenInfo;
	private final boolean hasDecoration;
	private final boolean hasDungeons;
	private WorldGenLakes lavaLakeGenerator;
	private final Random random;
	private final List structureGenerators = new ArrayList();
	private WorldGenLakes waterLakeGenerator;
	private final World worldObj;

	public ChunkProviderFlat(World p_i2004_1_, long p_i2004_2_,
			boolean p_i2004_4_, String p_i2004_5_) {
		worldObj = p_i2004_1_;
		random = new Random(p_i2004_2_);
		flatWorldGenInfo = FlatGeneratorInfo
				.createFlatGeneratorFromString(p_i2004_5_);

		if (p_i2004_4_) {
			final Map var6 = flatWorldGenInfo.getWorldFeatures();

			if (var6.containsKey("village")) {
				final Map var7 = (Map) var6.get("village");

				if (!var7.containsKey("size")) {
					var7.put("size", "1");
				}

				structureGenerators.add(new MapGenVillage(var7));
			}

			if (var6.containsKey("biome_1")) {
				structureGenerators.add(new MapGenScatteredFeature((Map) var6
						.get("biome_1")));
			}

			if (var6.containsKey("mineshaft")) {
				structureGenerators.add(new MapGenMineshaft((Map) var6
						.get("mineshaft")));
			}

			if (var6.containsKey("stronghold")) {
				structureGenerators.add(new MapGenStronghold((Map) var6
						.get("stronghold")));
			}
		}

		hasDecoration = flatWorldGenInfo.getWorldFeatures().containsKey(
				"decoration");

		if (flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
			waterLakeGenerator = new WorldGenLakes(Blocks.water);
		}

		if (flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
			lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
		}

		hasDungeons = flatWorldGenInfo.getWorldFeatures()
				.containsKey("dungeon");
		final Iterator var9 = flatWorldGenInfo.getFlatLayers().iterator();

		while (var9.hasNext()) {
			final FlatLayerInfo var10 = (FlatLayerInfo) var9.next();

			for (int var8 = var10.getMinY(); var8 < var10.getMinY()
					+ var10.getLayerCount(); ++var8) {
				cachedBlockIDs[var8] = var10.func_151536_b();
				cachedBlockMetadata[var8] = (byte) var10.getFillBlockMeta();
			}
		}
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
		if ("Stronghold".equals(p_147416_2_)) {
			final Iterator var6 = structureGenerators.iterator();

			while (var6.hasNext()) {
				final MapGenStructure var7 = (MapGenStructure) var6.next();

				if (var7 instanceof MapGenStronghold)
					return var7.func_151545_a(p_147416_1_, p_147416_3_,
							p_147416_4_, p_147416_5_);
			}
		}

		return null;
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
		final BiomeGenBase var5 = worldObj.getBiomeGenForCoords(p_73155_2_,
				p_73155_4_);
		return var5.getSpawnableList(p_73155_1_);
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
		return "FlatLevelSource";
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(IChunkProvider p_73153_1_, int p_73153_2_,
			int p_73153_3_) {
		final int var4 = p_73153_2_ * 16;
		final int var5 = p_73153_3_ * 16;
		final BiomeGenBase var6 = worldObj.getBiomeGenForCoords(var4 + 16,
				var5 + 16);
		boolean var7 = false;
		random.setSeed(worldObj.getSeed());
		final long var8 = random.nextLong() / 2L * 2L + 1L;
		final long var10 = random.nextLong() / 2L * 2L + 1L;
		random.setSeed(p_73153_2_ * var8 + p_73153_3_ * var10
				^ worldObj.getSeed());
		final Iterator var12 = structureGenerators.iterator();

		while (var12.hasNext()) {
			final MapGenStructure var13 = (MapGenStructure) var12.next();
			final boolean var14 = var13.generateStructuresInChunk(worldObj,
					random, p_73153_2_, p_73153_3_);

			if (var13 instanceof MapGenVillage) {
				var7 |= var14;
			}
		}

		int var16;
		int var17;
		int var18;

		if (waterLakeGenerator != null && !var7 && random.nextInt(4) == 0) {
			var16 = var4 + random.nextInt(16) + 8;
			var17 = random.nextInt(256);
			var18 = var5 + random.nextInt(16) + 8;
			waterLakeGenerator.generate(worldObj, random, var16, var17, var18);
		}

		if (lavaLakeGenerator != null && !var7 && random.nextInt(8) == 0) {
			var16 = var4 + random.nextInt(16) + 8;
			var17 = random.nextInt(random.nextInt(248) + 8);
			var18 = var5 + random.nextInt(16) + 8;

			if (var17 < 63 || random.nextInt(10) == 0) {
				lavaLakeGenerator.generate(worldObj, random, var16, var17,
						var18);
			}
		}

		if (hasDungeons) {
			for (var16 = 0; var16 < 8; ++var16) {
				var17 = var4 + random.nextInt(16) + 8;
				var18 = random.nextInt(256);
				final int var15 = var5 + random.nextInt(16) + 8;
				new WorldGenDungeons().generate(worldObj, random, var17, var18,
						var15);
			}
		}

		if (hasDecoration) {
			var6.decorate(worldObj, random, var4, var5);
		}
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	@Override
	public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
		final Chunk var3 = new Chunk(worldObj, p_73154_1_, p_73154_2_);
		int var6;

		for (int var4 = 0; var4 < cachedBlockIDs.length; ++var4) {
			final Block var5 = cachedBlockIDs[var4];

			if (var5 != null) {
				var6 = var4 >> 4;
				ExtendedBlockStorage var7 = var3.getBlockStorageArray()[var6];

				if (var7 == null) {
					var7 = new ExtendedBlockStorage(var4,
							!worldObj.provider.hasNoSky);
					var3.getBlockStorageArray()[var6] = var7;
				}

				for (int var8 = 0; var8 < 16; ++var8) {
					for (int var9 = 0; var9 < 16; ++var9) {
						var7.func_150818_a(var8, var4 & 15, var9, var5);
						var7.setExtBlockMetadata(var8, var4 & 15, var9,
								cachedBlockMetadata[var4]);
					}
				}
			}
		}

		var3.generateSkylightMap();
		final BiomeGenBase[] var10 = worldObj.getWorldChunkManager()
				.loadBlockGeneratorData((BiomeGenBase[]) null, p_73154_1_ * 16,
						p_73154_2_ * 16, 16, 16);
		final byte[] var11 = var3.getBiomeArray();

		for (var6 = 0; var6 < var11.length; ++var6) {
			var11[var6] = (byte) var10[var6].biomeID;
		}

		final Iterator var12 = structureGenerators.iterator();

		while (var12.hasNext()) {
			final MapGenBase var13 = (MapGenBase) var12.next();
			var13.func_151539_a(this, worldObj, p_73154_1_, p_73154_2_,
					(Block[]) null);
		}

		var3.generateSkylightMap();
		return var3;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
		final Iterator var3 = structureGenerators.iterator();

		while (var3.hasNext()) {
			final MapGenStructure var4 = (MapGenStructure) var3.next();
			var4.func_151539_a(this, worldObj, p_82695_1_, p_82695_2_,
					(Block[]) null);
		}
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
