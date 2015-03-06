package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The time in milliseconds when this field was initialized. Stored in the
	 * session lock file.
	 */
	private final long initializationTime = MinecraftServer
			.getSystemTimeMillis();

	private final File mapDataDir;
	/** The directory in which to save player data. */
	private final File playersDirectory;

	/** The directory name of the world */
	private final String saveDirectoryName;

	/** The directory in which to save world data. */
	private final File worldDirectory;

	public SaveHandler(File p_i2146_1_, String p_i2146_2_, boolean p_i2146_3_) {
		worldDirectory = new File(p_i2146_1_, p_i2146_2_);
		worldDirectory.mkdirs();
		playersDirectory = new File(worldDirectory, "playerdata");
		mapDataDir = new File(worldDirectory, "data");
		mapDataDir.mkdirs();
		saveDirectoryName = p_i2146_2_;

		if (p_i2146_3_) {
			playersDirectory.mkdirs();
		}

		setSessionLock();
	}

	/**
	 * Checks the session lock to prevent save collisions
	 */
	@Override
	public void checkSessionLock() throws MinecraftException {
		try {
			final File var1 = new File(worldDirectory, "session.lock");
			final DataInputStream var2 = new DataInputStream(
					new FileInputStream(var1));

			try {
				if (var2.readLong() != initializationTime)
					throw new MinecraftException(
							"The save is being accessed from another location, aborting");
			} finally {
				var2.close();
			}
		} catch (final IOException var7) {
			throw new MinecraftException(
					"Failed to check session lock, aborting");
		}
	}

	/**
	 * Called to flush all changes to disk, waiting for them to complete.
	 */
	@Override
	public void flush() {
	}

	/**
	 * Returns an array of usernames for which player.dat exists for.
	 */
	@Override
	public String[] getAvailablePlayerDat() {
		final String[] var1 = playersDirectory.list();

		for (int var2 = 0; var2 < var1.length; ++var2) {
			if (var1[var2].endsWith(".dat")) {
				var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
			}
		}

		return var1;
	}

	/**
	 * Returns the chunk loader with the provided world provider
	 */
	@Override
	public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
		throw new RuntimeException("Old Chunk Storage is no longer supported.");
	}

	/**
	 * Gets the file location of the given map
	 */
	@Override
	public File getMapFileFromName(String p_75758_1_) {
		return new File(mapDataDir, p_75758_1_ + ".dat");
	}

	/**
	 * returns null if no saveHandler is relevent (eg. SMP)
	 */
	@Override
	public IPlayerFileData getSaveHandler() {
		return this;
	}

	/**
	 * Gets the File object corresponding to the base directory of this world.
	 */
	@Override
	public File getWorldDirectory() {
		return worldDirectory;
	}

	/**
	 * Returns the name of the directory where world information is saved.
	 */
	@Override
	public String getWorldDirectoryName() {
		return saveDirectoryName;
	}

	/**
	 * Loads and returns the world info
	 */
	@Override
	public WorldInfo loadWorldInfo() {
		File var1 = new File(worldDirectory, "level.dat");
		NBTTagCompound var2;
		NBTTagCompound var3;

		if (var1.exists()) {
			try {
				var2 = CompressedStreamTools
						.readCompressed(new FileInputStream(var1));
				var3 = var2.getCompoundTag("Data");
				return new WorldInfo(var3);
			} catch (final Exception var5) {
				var5.printStackTrace();
			}
		}

		var1 = new File(worldDirectory, "level.dat_old");

		if (var1.exists()) {
			try {
				var2 = CompressedStreamTools
						.readCompressed(new FileInputStream(var1));
				var3 = var2.getCompoundTag("Data");
				return new WorldInfo(var3);
			} catch (final Exception var4) {
				var4.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Reads the player data from disk into the specified PlayerEntityMP.
	 */
	@Override
	public NBTTagCompound readPlayerData(EntityPlayer p_75752_1_) {
		NBTTagCompound var2 = null;

		try {
			final File var3 = new File(playersDirectory, p_75752_1_
					.getUniqueID().toString() + ".dat");

			if (var3.exists() && var3.isFile()) {
				var2 = CompressedStreamTools
						.readCompressed(new FileInputStream(var3));
			}
		} catch (final Exception var4) {
			logger.warn("Failed to load player data for "
					+ p_75752_1_.getCommandSenderName());
		}

		if (var2 != null) {
			p_75752_1_.readFromNBT(var2);
		}

		return var2;
	}

	/**
	 * Saves the passed in world info.
	 */
	@Override
	public void saveWorldInfo(WorldInfo p_75761_1_) {
		final NBTTagCompound var2 = p_75761_1_.getNBTTagCompound();
		final NBTTagCompound var3 = new NBTTagCompound();
		var3.setTag("Data", var2);

		try {
			final File var4 = new File(worldDirectory, "level.dat_new");
			final File var5 = new File(worldDirectory, "level.dat_old");
			final File var6 = new File(worldDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(var3, new FileOutputStream(
					var4));

			if (var5.exists()) {
				var5.delete();
			}

			var6.renameTo(var5);

			if (var6.exists()) {
				var6.delete();
			}

			var4.renameTo(var6);

			if (var4.exists()) {
				var4.delete();
			}
		} catch (final Exception var7) {
			var7.printStackTrace();
		}
	}

	/**
	 * Saves the given World Info with the given NBTTagCompound as the Player.
	 */
	@Override
	public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_,
			NBTTagCompound p_75755_2_) {
		final NBTTagCompound var3 = p_75755_1_.cloneNBTCompound(p_75755_2_);
		final NBTTagCompound var4 = new NBTTagCompound();
		var4.setTag("Data", var3);

		try {
			final File var5 = new File(worldDirectory, "level.dat_new");
			final File var6 = new File(worldDirectory, "level.dat_old");
			final File var7 = new File(worldDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(var4, new FileOutputStream(
					var5));

			if (var6.exists()) {
				var6.delete();
			}

			var7.renameTo(var6);

			if (var7.exists()) {
				var7.delete();
			}

			var5.renameTo(var7);

			if (var5.exists()) {
				var5.delete();
			}
		} catch (final Exception var8) {
			var8.printStackTrace();
		}
	}

	/**
	 * Creates a session lock file for this process
	 */
	private void setSessionLock() {
		try {
			final File var1 = new File(worldDirectory, "session.lock");
			final DataOutputStream var2 = new DataOutputStream(
					new FileOutputStream(var1));

			try {
				var2.writeLong(initializationTime);
			} finally {
				var2.close();
			}
		} catch (final IOException var7) {
			var7.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	/**
	 * Writes the player data to disk from the specified PlayerEntityMP.
	 */
	@Override
	public void writePlayerData(EntityPlayer p_75753_1_) {
		try {
			final NBTTagCompound var2 = new NBTTagCompound();
			p_75753_1_.writeToNBT(var2);
			final File var3 = new File(playersDirectory, p_75753_1_
					.getUniqueID().toString() + ".dat.tmp");
			final File var4 = new File(playersDirectory, p_75753_1_
					.getUniqueID().toString() + ".dat");
			CompressedStreamTools.writeCompressed(var2, new FileOutputStream(
					var3));

			if (var4.exists()) {
				var4.delete();
			}

			var3.renameTo(var4);
		} catch (final Exception var5) {
			logger.warn("Failed to save player data for "
					+ p_75753_1_.getCommandSenderName());
		}
	}
}
