package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
	/** Whether this MapDataBase needs saving to disk. */
	private boolean dirty;

	/** The name of the map data nbt */
	public final String mapName;

	public WorldSavedData(String p_i2141_1_) {
		mapName = p_i2141_1_;
	}

	/**
	 * Whether this MapDataBase needs saving to disk.
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Marks this MapDataBase dirty, to be saved to disk when the level next
	 * saves.
	 */
	public void markDirty() {
		setDirty(true);
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public abstract void readFromNBT(NBTTagCompound p_76184_1_);

	/**
	 * Sets the dirty state of this MapDataBase, whether it needs saving to
	 * disk.
	 */
	public void setDirty(boolean p_76186_1_) {
		dirty = p_76186_1_;
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities
	 * and TileEntities
	 */
	public abstract void writeToNBT(NBTTagCompound p_76187_1_);
}
