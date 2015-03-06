package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess {
	/**
	 * set by !chunk.getAreLevelsEmpty
	 */
	boolean extendedLevelsInChunkCache();

	/**
	 * Gets the biome for a given set of x/z coordinates
	 */
	BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_);

	Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_);

	/**
	 * Returns the block metadata at coords x,y,z
	 */
	int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_);

	/**
	 * Returns current world height.
	 */
	int getHeight();

	/**
	 * Any Light rendered on a 1.8 Block goes through here
	 */
	int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_,
			int p_72802_3_, int p_72802_4_);

	TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_);

	/**
	 * Returns true if the block at the specified coordinates is empty
	 */
	boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_);

	/**
	 * Is this block powering in the specified direction Args: x, y, z,
	 * direction
	 */
	int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_,
			int p_72879_4_);
}
