package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo {
	/** Instance of WorldInfo. */
	private final WorldInfo theWorldInfo;

	public DerivedWorldInfo(WorldInfo p_i2145_1_) {
		theWorldInfo = p_i2145_1_;
	}

	/**
	 * Returns true if commands are allowed on this World.
	 */
	@Override
	public boolean areCommandsAllowed() {
		return theWorldInfo.areCommandsAllowed();
	}

	/**
	 * Creates a new NBTTagCompound for the world, with the given NBTTag as the
	 * "Player"
	 */
	@Override
	public NBTTagCompound cloneNBTCompound(NBTTagCompound p_76082_1_) {
		return theWorldInfo.cloneNBTCompound(p_76082_1_);
	}

	/**
	 * Gets the GameRules class Instance.
	 */
	@Override
	public GameRules getGameRulesInstance() {
		return theWorldInfo.getGameRulesInstance();
	}

	/**
	 * Gets the GameType.
	 */
	@Override
	public WorldSettings.GameType getGameType() {
		return theWorldInfo.getGameType();
	}

	/**
	 * Return the last time the player was in this world.
	 */
	@Override
	public long getLastTimePlayed() {
		return theWorldInfo.getLastTimePlayed();
	}

	/**
	 * Gets the NBTTagCompound for the worldInfo
	 */
	@Override
	public NBTTagCompound getNBTTagCompound() {
		return theWorldInfo.getNBTTagCompound();
	}

	/**
	 * Returns the player's NBTTagCompound to be loaded
	 */
	@Override
	public NBTTagCompound getPlayerNBTTagCompound() {
		return theWorldInfo.getPlayerNBTTagCompound();
	}

	/**
	 * Return the number of ticks until rain.
	 */
	@Override
	public int getRainTime() {
		return theWorldInfo.getRainTime();
	}

	/**
	 * Returns the save version of this world
	 */
	@Override
	public int getSaveVersion() {
		return theWorldInfo.getSaveVersion();
	}

	/**
	 * Returns the seed of current world.
	 */
	@Override
	public long getSeed() {
		return theWorldInfo.getSeed();
	}

	@Override
	public long getSizeOnDisk() {
		return theWorldInfo.getSizeOnDisk();
	}

	/**
	 * Returns the x spawn position
	 */
	@Override
	public int getSpawnX() {
		return theWorldInfo.getSpawnX();
	}

	/**
	 * Return the Y axis spawning point of the player.
	 */
	@Override
	public int getSpawnY() {
		return theWorldInfo.getSpawnY();
	}

	/**
	 * Returns the z spawn position
	 */
	@Override
	public int getSpawnZ() {
		return theWorldInfo.getSpawnZ();
	}

	@Override
	public WorldType getTerrainType() {
		return theWorldInfo.getTerrainType();
	}

	/**
	 * Returns the number of ticks until next thunderbolt.
	 */
	@Override
	public int getThunderTime() {
		return theWorldInfo.getThunderTime();
	}

	/**
	 * Returns vanilla MC dimension (-1,0,1). For custom dimension
	 * compatibility, always prefer WorldProvider.dimensionID accessed from
	 * World.provider.dimensionID
	 */
	@Override
	public int getVanillaDimension() {
		return theWorldInfo.getVanillaDimension();
	}

	/**
	 * Get current world name
	 */
	@Override
	public String getWorldName() {
		return theWorldInfo.getWorldName();
	}

	/**
	 * Get current world time
	 */
	@Override
	public long getWorldTime() {
		return theWorldInfo.getWorldTime();
	}

	@Override
	public long getWorldTotalTime() {
		return theWorldInfo.getWorldTotalTime();
	}

	@Override
	public void incrementTotalWorldTime(long p_82572_1_) {
	}

	/**
	 * Returns true if hardcore mode is enabled, otherwise false
	 */
	@Override
	public boolean isHardcoreModeEnabled() {
		return theWorldInfo.isHardcoreModeEnabled();
	}

	/**
	 * Returns true if the World is initialized.
	 */
	@Override
	public boolean isInitialized() {
		return theWorldInfo.isInitialized();
	}

	/**
	 * Get whether the map features (e.g. strongholds) generation is enabled or
	 * disabled.
	 */
	@Override
	public boolean isMapFeaturesEnabled() {
		return theWorldInfo.isMapFeaturesEnabled();
	}

	/**
	 * Returns true if it is raining, false otherwise.
	 */
	@Override
	public boolean isRaining() {
		return theWorldInfo.isRaining();
	}

	/**
	 * Returns true if it is thundering, false otherwise.
	 */
	@Override
	public boolean isThundering() {
		return theWorldInfo.isThundering();
	}

	/**
	 * Sets whether it is raining or not.
	 */
	@Override
	public void setRaining(boolean p_76084_1_) {
	}

	/**
	 * Sets the number of ticks until rain.
	 */
	@Override
	public void setRainTime(int p_76080_1_) {
	}

	/**
	 * Sets the save version of the world
	 */
	@Override
	public void setSaveVersion(int p_76078_1_) {
	}

	/**
	 * Sets the initialization status of the World.
	 */
	@Override
	public void setServerInitialized(boolean p_76091_1_) {
	}

	/**
	 * Sets the spawn zone position. Args: x, y, z
	 */
	@Override
	public void setSpawnPosition(int p_76081_1_, int p_76081_2_, int p_76081_3_) {
	}

	/**
	 * Set the x spawn position to the passed in value
	 */
	@Override
	public void setSpawnX(int p_76058_1_) {
	}

	/**
	 * Sets the y spawn position
	 */
	@Override
	public void setSpawnY(int p_76056_1_) {
	}

	/**
	 * Set the z spawn position to the passed in value
	 */
	@Override
	public void setSpawnZ(int p_76087_1_) {
	}

	@Override
	public void setTerrainType(WorldType p_76085_1_) {
	}

	/**
	 * Sets whether it is thundering or not.
	 */
	@Override
	public void setThundering(boolean p_76069_1_) {
	}

	/**
	 * Defines the number of ticks until next thunderbolt.
	 */
	@Override
	public void setThunderTime(int p_76090_1_) {
	}

	@Override
	public void setWorldName(String p_76062_1_) {
	}

	/**
	 * Set current world time
	 */
	@Override
	public void setWorldTime(long p_76068_1_) {
	}
}
