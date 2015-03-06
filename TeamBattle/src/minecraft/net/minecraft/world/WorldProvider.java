package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;

public abstract class WorldProvider {
	public static final float[] moonPhaseFactors = new float[] { 1.0F, 0.75F,
			0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };

	public static WorldProvider getProviderForDimension(int p_76570_0_) {
		return p_76570_0_ == -1 ? new WorldProviderHell()
				: p_76570_0_ == 0 ? new WorldProviderSurface()
						: p_76570_0_ == 1 ? new WorldProviderEnd() : null;
	}

	/** Array for sunrise/sunset colors (RGBA) */
	private final float[] colorsSunriseSunset = new float[4];
	/** The id for the dimension (ex. -1: Nether, 0: Overworld, 1: The End) */
	public int dimensionId;

	public String field_82913_c;

	/**
	 * A boolean that tells if a world does not have a sky. Used in calculating
	 * weather and skylight
	 */
	public boolean hasNoSky;

	/**
	 * States whether the Hell world provider is used(true) or if the normal
	 * world provider is used(false)
	 */
	public boolean isHellWorld;

	/** Light to brightness conversion table */
	public float[] lightBrightnessTable = new float[16];

	public WorldType terrainType;

	/** World chunk manager being used to generate chunks */
	public WorldChunkManager worldChunkMgr;

	/** world object being used */
	public World worldObj;

	/**
	 * Returns array with sunrise/sunset colors
	 */
	public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
		final float var3 = 0.4F;
		final float var4 = MathHelper.cos(p_76560_1_ * (float) Math.PI * 2.0F) - 0.0F;
		final float var5 = -0.0F;

		if (var4 >= var5 - var3 && var4 <= var5 + var3) {
			final float var6 = (var4 - var5) / var3 * 0.5F + 0.5F;
			float var7 = 1.0F - (1.0F - MathHelper.sin(var6 * (float) Math.PI)) * 0.99F;
			var7 *= var7;
			colorsSunriseSunset[0] = var6 * 0.3F + 0.7F;
			colorsSunriseSunset[1] = var6 * var6 * 0.7F + 0.2F;
			colorsSunriseSunset[2] = var6 * var6 * 0.0F + 0.2F;
			colorsSunriseSunset[3] = var7;
			return colorsSunriseSunset;
		} else
			return null;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified
	 * time (usually worldTime)
	 */
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
		final int var4 = (int) (p_76563_1_ % 24000L);
		float var5 = (var4 + p_76563_3_) / 24000.0F - 0.25F;

		if (var5 < 0.0F) {
			++var5;
		}

		if (var5 > 1.0F) {
			--var5;
		}

		final float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map
	 * spawn point
	 */
	public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
		return worldObj.getTopBlock(p_76566_1_, p_76566_2_) == Blocks.grass;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false
	 * = nether).
	 */
	public boolean canRespawnHere() {
		return true;
	}

	/**
	 * Returns a new chunk provider which generates chunks for this world
	 */
	public IChunkProvider createChunkGenerator() {
		return terrainType == WorldType.FLAT ? new ChunkProviderFlat(worldObj,
				worldObj.getSeed(), worldObj.getWorldInfo()
						.isMapFeaturesEnabled(), field_82913_c)
				: new ChunkProviderGenerate(worldObj, worldObj.getSeed(),
						worldObj.getWorldInfo().isMapFeaturesEnabled());
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		return false;
	}

	/**
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable() {
		final float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			final float var3 = 1.0F - var2 / 15.0F;
			lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F)
					* (1.0F - var1) + var1;
		}
	}

	public int getAverageGroundLevel() {
		return terrainType == WorldType.FLAT ? 4 : 64;
	}

	/**
	 * the y level at which clouds are rendered.
	 */
	public float getCloudHeight() {
		return 128.0F;
	}

	/**
	 * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
	 */
	public abstract String getDimensionName();

	/**
	 * Gets the hard-coded portal location to use when entering this dimension.
	 */
	public ChunkCoordinates getEntrancePortalLocation() {
		return null;
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
		float var3 = MathHelper.cos(p_76562_1_ * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = 0.7529412F;
		float var5 = 0.84705883F;
		float var6 = 1.0F;
		var4 *= var3 * 0.94F + 0.06F;
		var5 *= var3 * 0.94F + 0.06F;
		var6 *= var3 * 0.91F + 0.09F;
		return Vec3.createVectorHelper(var4, var5, var6);
	}

	public int getMoonPhase(long p_76559_1_) {
		return (int) (p_76559_1_ / 24000L % 8L + 8L) % 8;
	}

	/**
	 * Returns a double value representing the Y value relative to the top of
	 * the map at which void fog is at its maximum. The default factor of
	 * 0.03125 relative to 256, for example, means the void fog will be at its
	 * maximum at (256*0.03125), or 8.
	 */
	public double getVoidFogYFactor() {
		return terrainType == WorldType.FLAT ? 1.0D : 0.03125D;
	}

	/**
	 * returns true if this dimension is supposed to display void particles and
	 * pull in the far plane based on the user's Y offset.
	 */
	public boolean getWorldHasVoidParticles() {
		return terrainType != WorldType.FLAT && !hasNoSky;
	}

	public boolean isSkyColored() {
		return true;
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the
	 * Nether or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return true;
	}

	/**
	 * associate an existing world with a World provider, and setup its
	 * lightbrightness table
	 */
	public final void registerWorld(World p_76558_1_) {
		worldObj = p_76558_1_;
		terrainType = p_76558_1_.getWorldInfo().getTerrainType();
		field_82913_c = p_76558_1_.getWorldInfo().getGeneratorOptions();
		registerWorldChunkManager();
		generateLightBrightnessTable();
	}

	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	protected void registerWorldChunkManager() {
		if (worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT) {
			final FlatGeneratorInfo var1 = FlatGeneratorInfo
					.createFlatGeneratorFromString(worldObj.getWorldInfo()
							.getGeneratorOptions());
			worldChunkMgr = new WorldChunkManagerHell(
					BiomeGenBase.func_150568_d(var1.getBiome()), 0.5F);
		} else {
			worldChunkMgr = new WorldChunkManager(worldObj);
		}
	}
}
