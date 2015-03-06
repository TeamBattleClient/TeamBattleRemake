package net.minecraft.world.chunk.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilSaveConverter extends SaveFormatOld {
	private static final Logger logger = LogManager.getLogger();

	public AnvilSaveConverter(File p_i2144_1_) {
		super(p_i2144_1_);
	}

	/**
	 * filters the files in the par1 directory, and adds them to the par2
	 * collections
	 */
	private void addRegionFilesToCollection(File p_75810_1_,
			Collection p_75810_2_) {
		final File var3 = new File(p_75810_1_, "region");
		final File[] var4 = var3.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File p_accept_1_, String p_accept_2_) {
				return p_accept_2_.endsWith(".mcr");
			}
		});

		if (var4 != null) {
			Collections.addAll(p_75810_2_, var4);
		}
	}

	/**
	 * copies a 32x32 chunk set from par2File to par1File, via
	 * AnvilConverterData
	 */
	private void convertChunks(File p_75811_1_, File p_75811_2_,
			WorldChunkManager p_75811_3_, int p_75811_4_, int p_75811_5_,
			IProgressUpdate p_75811_6_) {
		try {
			final String var7 = p_75811_2_.getName();
			final RegionFile var8 = new RegionFile(p_75811_2_);
			final RegionFile var9 = new RegionFile(
					new File(p_75811_1_, var7.substring(0, var7.length()
							- ".mcr".length())
							+ ".mca"));

			for (int var10 = 0; var10 < 32; ++var10) {
				int var11;

				for (var11 = 0; var11 < 32; ++var11) {
					if (var8.isChunkSaved(var10, var11)
							&& !var9.isChunkSaved(var10, var11)) {
						final DataInputStream var12 = var8
								.getChunkDataInputStream(var10, var11);

						if (var12 == null) {
							logger.warn("Failed to fetch input stream");
						} else {
							final NBTTagCompound var13 = CompressedStreamTools
									.read(var12);
							var12.close();
							final NBTTagCompound var14 = var13
									.getCompoundTag("Level");
							final ChunkLoader.AnvilConverterData var15 = ChunkLoader
									.load(var14);
							final NBTTagCompound var16 = new NBTTagCompound();
							final NBTTagCompound var17 = new NBTTagCompound();
							var16.setTag("Level", var17);
							ChunkLoader.convertToAnvilFormat(var15, var17,
									p_75811_3_);
							final DataOutputStream var18 = var9
									.getChunkDataOutputStream(var10, var11);
							CompressedStreamTools.write(var16, var18);
							var18.close();
						}
					}
				}

				var11 = (int) Math.round(100.0D * (p_75811_4_ * 1024)
						/ (p_75811_5_ * 1024));
				final int var20 = (int) Math.round(100.0D
						* ((var10 + 1) * 32 + p_75811_4_ * 1024)
						/ (p_75811_5_ * 1024));

				if (var20 > var11) {
					p_75811_6_.setLoadingProgress(var20);
				}
			}

			var8.close();
			var9.close();
		} catch (final IOException var19) {
			var19.printStackTrace();
		}
	}

	private void convertFile(File p_75813_1_, Iterable p_75813_2_,
			WorldChunkManager p_75813_3_, int p_75813_4_, int p_75813_5_,
			IProgressUpdate p_75813_6_) {
		final Iterator var7 = p_75813_2_.iterator();

		while (var7.hasNext()) {
			final File var8 = (File) var7.next();
			convertChunks(p_75813_1_, var8, p_75813_3_, p_75813_4_, p_75813_5_,
					p_75813_6_);
			++p_75813_4_;
			final int var9 = (int) Math.round(100.0D * p_75813_4_ / p_75813_5_);
			p_75813_6_.setLoadingProgress(var9);
		}
	}

	/**
	 * Converts the specified map to the new map format. Args: worldName,
	 * loadingScreen
	 */
	@Override
	public boolean convertMapFormat(String p_75805_1_,
			IProgressUpdate p_75805_2_) {
		p_75805_2_.setLoadingProgress(0);
		final ArrayList var3 = new ArrayList();
		final ArrayList var4 = new ArrayList();
		final ArrayList var5 = new ArrayList();
		final File var6 = new File(savesDirectory, p_75805_1_);
		final File var7 = new File(var6, "DIM-1");
		final File var8 = new File(var6, "DIM1");
		logger.info("Scanning folders...");
		addRegionFilesToCollection(var6, var3);

		if (var7.exists()) {
			addRegionFilesToCollection(var7, var4);
		}

		if (var8.exists()) {
			addRegionFilesToCollection(var8, var5);
		}

		final int var9 = var3.size() + var4.size() + var5.size();
		logger.info("Total conversion count is " + var9);
		final WorldInfo var10 = getWorldInfo(p_75805_1_);
		Object var11 = null;

		if (var10.getTerrainType() == WorldType.FLAT) {
			var11 = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F);
		} else {
			var11 = new WorldChunkManager(var10.getSeed(),
					var10.getTerrainType());
		}

		convertFile(new File(var6, "region"), var3, (WorldChunkManager) var11,
				0, var9, p_75805_2_);
		convertFile(new File(var7, "region"), var4, new WorldChunkManagerHell(
				BiomeGenBase.hell, 0.0F), var3.size(), var9, p_75805_2_);
		convertFile(new File(var8, "region"), var5, new WorldChunkManagerHell(
				BiomeGenBase.sky, 0.0F), var3.size() + var4.size(), var9,
				p_75805_2_);
		var10.setSaveVersion(19133);

		if (var10.getTerrainType() == WorldType.DEFAULT_1_1) {
			var10.setTerrainType(WorldType.DEFAULT);
		}

		createFile(p_75805_1_);
		final ISaveHandler var12 = getSaveLoader(p_75805_1_, false);
		var12.saveWorldInfo(var10);
		return true;
	}

	/**
	 * par: filename for the level.dat_mcr backup
	 */
	private void createFile(String p_75809_1_) {
		final File var2 = new File(savesDirectory, p_75809_1_);

		if (!var2.exists()) {
			logger.warn("Unable to create level.dat_mcr backup");
		} else {
			final File var3 = new File(var2, "level.dat");

			if (!var3.exists()) {
				logger.warn("Unable to create level.dat_mcr backup");
			} else {
				final File var4 = new File(var2, "level.dat_mcr");

				if (!var3.renameTo(var4)) {
					logger.warn("Unable to create level.dat_mcr backup");
				}
			}
		}
	}

	@Override
	public void flushCache() {
		RegionFileCache.clearRegionFileReferences();
	}

	@Override
	public String func_154333_a() {
		return "Anvil";
	}

	@Override
	public boolean func_154334_a(String p_154334_1_) {
		final WorldInfo var2 = getWorldInfo(p_154334_1_);
		return var2 != null && var2.getSaveVersion() == 19132;
	}

	@Override
	public List getSaveList() throws AnvilConverterException {
		if (savesDirectory != null && savesDirectory.exists()
				&& savesDirectory.isDirectory()) {
			final ArrayList var1 = new ArrayList();
			final File[] var2 = savesDirectory.listFiles();
			final File[] var3 = var2;
			final int var4 = var2.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				final File var6 = var3[var5];

				if (var6.isDirectory()) {
					final String var7 = var6.getName();
					final WorldInfo var8 = getWorldInfo(var7);

					if (var8 != null
							&& (var8.getSaveVersion() == 19132 || var8
									.getSaveVersion() == 19133)) {
						final boolean var9 = var8.getSaveVersion() != getSaveVersion();
						String var10 = var8.getWorldName();

						if (var10 == null
								|| MathHelper.stringNullOrLengthZero(var10)) {
							var10 = var7;
						}

						final long var11 = 0L;
						var1.add(new SaveFormatComparator(var7, var10, var8
								.getLastTimePlayed(), var11,
								var8.getGameType(), var9, var8
										.isHardcoreModeEnabled(), var8
										.areCommandsAllowed()));
					}
				}
			}

			return var1;
		} else
			throw new AnvilConverterException(
					"Unable to read or access folder where game worlds are saved!");
	}

	/**
	 * Returns back a loader for the specified save directory
	 */
	@Override
	public ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_) {
		return new AnvilSaveHandler(savesDirectory, p_75804_1_, p_75804_2_);
	}

	protected int getSaveVersion() {
		return 19133;
	}

	/**
	 * Checks if the save directory uses the old map format
	 */
	@Override
	public boolean isOldMapFormat(String p_75801_1_) {
		final WorldInfo var2 = getWorldInfo(p_75801_1_);
		return var2 != null && var2.getSaveVersion() != getSaveVersion();
	}
}
