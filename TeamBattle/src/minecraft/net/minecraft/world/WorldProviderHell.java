package net.minecraft.world;

import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;

public class WorldProviderHell extends WorldProvider {

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified
	 * time (usually worldTime)
	 */
	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
		return 0.5F;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map
	 * spawn point
	 */
	@Override
	public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
		return false;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false
	 * = nether).
	 */
	@Override
	public boolean canRespawnHere() {
		return false;
	}

	/**
	 * Returns a new chunk provider which generates chunks for this world
	 */
	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderHell(worldObj, worldObj.getSeed());
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	@Override
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		return true;
	}

	/**
	 * Creates the light to brightness table
	 */
	@Override
	protected void generateLightBrightnessTable() {
		final float var1 = 0.1F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			final float var3 = 1.0F - var2 / 15.0F;
			lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F)
					* (1.0F - var1) + var1;
		}
	}

	/**
	 * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
	 */
	@Override
	public String getDimensionName() {
		return "Nether";
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	@Override
	public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
		return Vec3.createVectorHelper(0.20000000298023224D,
				0.029999999329447746D, 0.029999999329447746D);
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the
	 * Nether or End dimensions.
	 */
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	@Override
	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
		isHellWorld = true;
		hasNoSky = true;
		dimensionId = -1;
	}
}
