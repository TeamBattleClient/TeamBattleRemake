package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {
	/**
	 * Returns an array of usernames for which player.dat exists for.
	 */
	String[] getAvailablePlayerDat();

	/**
	 * Reads the player data from disk into the specified PlayerEntityMP.
	 */
	NBTTagCompound readPlayerData(EntityPlayer p_75752_1_);

	/**
	 * Writes the player data to disk from the specified PlayerEntityMP.
	 */
	void writePlayerData(EntityPlayer p_75753_1_);
}
