package net.minecraft.world.storage;

import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class WorldInfo {
	private boolean allowCommands;
	private int dimension;
	private String generatorOptions;

	/** Hardcore mode flag */
	private boolean hardcore;

	private boolean initialized;

	/** The last time the player was in this world. */
	private long lastTimePlayed;

	/** The name of the save defined at world creation. */
	private String levelName;

	/**
	 * Whether the map features (e.g. strongholds) generation is enabled or
	 * disabled.
	 */
	private boolean mapFeaturesEnabled;

	private NBTTagCompound playerTag;

	/** True if it's raining, false otherwise. */
	private boolean raining;
	/** Number of ticks until next rain. */
	private int rainTime;
	/** Holds the seed of the currently world. */
	private long randomSeed;

	/** Introduced in beta 1.3, is the save version for future control. */
	private int saveVersion;

	/** The size of entire save of current world on the disk, isn't exactly. */
	private long sizeOnDisk;

	/** The spawn zone position X coordinate. */
	private int spawnX;

	/** The spawn zone position Y coordinate. */
	private int spawnY;

	/** The spawn zone position Z coordinate. */
	private int spawnZ;

	private WorldType terrainType;

	private GameRules theGameRules;

	/** The Game Type. */
	private WorldSettings.GameType theGameType;

	/** Is thunderbolts failing now? */
	private boolean thundering;
	/** Number of ticks untils next thunderbolt. */
	private int thunderTime;
	/** Total time for this world. */
	private long totalTime;
	/** The current world time in ticks, ranging from 0 to 23999. */
	private long worldTime;

	protected WorldInfo() {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
	}

	public WorldInfo(NBTTagCompound p_i2157_1_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2157_1_.getLong("RandomSeed");

		if (p_i2157_1_.func_150297_b("generatorName", 8)) {
			final String var2 = p_i2157_1_.getString("generatorName");
			terrainType = WorldType.parseWorldType(var2);

			if (terrainType == null) {
				terrainType = WorldType.DEFAULT;
			} else if (terrainType.isVersioned()) {
				int var3 = 0;

				if (p_i2157_1_.func_150297_b("generatorVersion", 99)) {
					var3 = p_i2157_1_.getInteger("generatorVersion");
				}

				terrainType = terrainType.getWorldTypeForGeneratorVersion(var3);
			}

			if (p_i2157_1_.func_150297_b("generatorOptions", 8)) {
				generatorOptions = p_i2157_1_.getString("generatorOptions");
			}
		}

		theGameType = WorldSettings.GameType.getByID(p_i2157_1_
				.getInteger("GameType"));

		if (p_i2157_1_.func_150297_b("MapFeatures", 99)) {
			mapFeaturesEnabled = p_i2157_1_.getBoolean("MapFeatures");
		} else {
			mapFeaturesEnabled = true;
		}

		spawnX = p_i2157_1_.getInteger("SpawnX");
		spawnY = p_i2157_1_.getInteger("SpawnY");
		spawnZ = p_i2157_1_.getInteger("SpawnZ");
		totalTime = p_i2157_1_.getLong("Time");

		if (p_i2157_1_.func_150297_b("DayTime", 99)) {
			worldTime = p_i2157_1_.getLong("DayTime");
		} else {
			worldTime = totalTime;
		}

		lastTimePlayed = p_i2157_1_.getLong("LastPlayed");
		sizeOnDisk = p_i2157_1_.getLong("SizeOnDisk");
		levelName = p_i2157_1_.getString("LevelName");
		saveVersion = p_i2157_1_.getInteger("version");
		rainTime = p_i2157_1_.getInteger("rainTime");
		raining = p_i2157_1_.getBoolean("raining");
		thunderTime = p_i2157_1_.getInteger("thunderTime");
		thundering = p_i2157_1_.getBoolean("thundering");
		hardcore = p_i2157_1_.getBoolean("hardcore");

		if (p_i2157_1_.func_150297_b("initialized", 99)) {
			initialized = p_i2157_1_.getBoolean("initialized");
		} else {
			initialized = true;
		}

		if (p_i2157_1_.func_150297_b("allowCommands", 99)) {
			allowCommands = p_i2157_1_.getBoolean("allowCommands");
		} else {
			allowCommands = theGameType == WorldSettings.GameType.CREATIVE;
		}

		if (p_i2157_1_.func_150297_b("Player", 10)) {
			playerTag = p_i2157_1_.getCompoundTag("Player");
			dimension = playerTag.getInteger("Dimension");
		}

		if (p_i2157_1_.func_150297_b("GameRules", 10)) {
			theGameRules.readGameRulesFromNBT(p_i2157_1_
					.getCompoundTag("GameRules"));
		}
	}

	public WorldInfo(WorldInfo p_i2159_1_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2159_1_.randomSeed;
		terrainType = p_i2159_1_.terrainType;
		generatorOptions = p_i2159_1_.generatorOptions;
		theGameType = p_i2159_1_.theGameType;
		mapFeaturesEnabled = p_i2159_1_.mapFeaturesEnabled;
		spawnX = p_i2159_1_.spawnX;
		spawnY = p_i2159_1_.spawnY;
		spawnZ = p_i2159_1_.spawnZ;
		totalTime = p_i2159_1_.totalTime;
		worldTime = p_i2159_1_.worldTime;
		lastTimePlayed = p_i2159_1_.lastTimePlayed;
		sizeOnDisk = p_i2159_1_.sizeOnDisk;
		playerTag = p_i2159_1_.playerTag;
		dimension = p_i2159_1_.dimension;
		levelName = p_i2159_1_.levelName;
		saveVersion = p_i2159_1_.saveVersion;
		rainTime = p_i2159_1_.rainTime;
		raining = p_i2159_1_.raining;
		thunderTime = p_i2159_1_.thunderTime;
		thundering = p_i2159_1_.thundering;
		hardcore = p_i2159_1_.hardcore;
		allowCommands = p_i2159_1_.allowCommands;
		initialized = p_i2159_1_.initialized;
		theGameRules = p_i2159_1_.theGameRules;
	}

	public WorldInfo(WorldSettings p_i2158_1_, String p_i2158_2_) {
		terrainType = WorldType.DEFAULT;
		generatorOptions = "";
		theGameRules = new GameRules();
		randomSeed = p_i2158_1_.getSeed();
		theGameType = p_i2158_1_.getGameType();
		mapFeaturesEnabled = p_i2158_1_.isMapFeaturesEnabled();
		levelName = p_i2158_2_;
		hardcore = p_i2158_1_.getHardcoreEnabled();
		terrainType = p_i2158_1_.getTerrainType();
		generatorOptions = p_i2158_1_.func_82749_j();
		allowCommands = p_i2158_1_.areCommandsAllowed();
		initialized = false;
	}

	/**
	 * Adds this WorldInfo instance to the crash report.
	 */
	public void addToCrashReport(CrashReportCategory p_85118_1_) {
		p_85118_1_.addCrashSectionCallable("Level seed", new Callable() {

			@Override
			public String call() {
				return String.valueOf(WorldInfo.this.getSeed());
			}
		});
		p_85118_1_.addCrashSectionCallable("Level generator", new Callable() {

			@Override
			public String call() {
				return String.format(
						"ID %02d - %s, ver %d. Features enabled: %b",
						new Object[] {
								Integer.valueOf(terrainType.getWorldTypeID()),
								terrainType.getWorldTypeName(),
								Integer.valueOf(terrainType
										.getGeneratorVersion()),
								Boolean.valueOf(mapFeaturesEnabled) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level generator options",
				new Callable() {

					@Override
					public String call() {
						return generatorOptions;
					}
				});
		p_85118_1_.addCrashSectionCallable("Level spawn location",
				new Callable() {

					@Override
					public String call() {
						return CrashReportCategory.getLocationInfo(spawnX,
								spawnY, spawnZ);
					}
				});
		p_85118_1_.addCrashSectionCallable("Level time", new Callable() {

			@Override
			public String call() {
				return String.format("%d game time, %d day time", new Object[] {
						Long.valueOf(totalTime), Long.valueOf(worldTime) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level dimension", new Callable() {

			@Override
			public String call() {
				return String.valueOf(dimension);
			}
		});
		p_85118_1_.addCrashSectionCallable("Level storage version",
				new Callable() {

					@Override
					public String call() {
						String var1 = "Unknown?";

						try {
							switch (saveVersion) {
							case 19132:
								var1 = "McRegion";
								break;

							case 19133:
								var1 = "Anvil";
							}
						} catch (final Throwable var3) {
							;
						}

						return String.format("0x%05X - %s", new Object[] {
								Integer.valueOf(saveVersion), var1 });
					}
				});
		p_85118_1_.addCrashSectionCallable("Level weather", new Callable() {

			@Override
			public String call() {
				return String.format(
						"Rain time: %d (now: %b), thunder time: %d (now: %b)",
						new Object[] { Integer.valueOf(rainTime),
								Boolean.valueOf(raining),
								Integer.valueOf(thunderTime),
								Boolean.valueOf(thundering) });
			}
		});
		p_85118_1_.addCrashSectionCallable("Level game mode", new Callable() {

			@Override
			public String call() {
				return String.format(
						"Game mode: %s (ID %d). Hardcore: %b. Cheats: %b",
						new Object[] { theGameType.getName(),
								Integer.valueOf(theGameType.getID()),
								Boolean.valueOf(hardcore),
								Boolean.valueOf(allowCommands) });
			}
		});
	}

	/**
	 * Returns true if commands are allowed on this World.
	 */
	public boolean areCommandsAllowed() {
		return allowCommands;
	}

	/**
	 * Creates a new NBTTagCompound for the world, with the given NBTTag as the
	 * "Player"
	 */
	public NBTTagCompound cloneNBTCompound(NBTTagCompound p_76082_1_) {
		final NBTTagCompound var2 = new NBTTagCompound();
		updateTagCompound(var2, p_76082_1_);
		return var2;
	}

	/**
	 * Gets the GameRules class Instance.
	 */
	public GameRules getGameRulesInstance() {
		return theGameRules;
	}

	/**
	 * Gets the GameType.
	 */
	public WorldSettings.GameType getGameType() {
		return theGameType;
	}

	public String getGeneratorOptions() {
		return generatorOptions;
	}

	/**
	 * Return the last time the player was in this world.
	 */
	public long getLastTimePlayed() {
		return lastTimePlayed;
	}

	/**
	 * Gets the NBTTagCompound for the worldInfo
	 */
	public NBTTagCompound getNBTTagCompound() {
		final NBTTagCompound var1 = new NBTTagCompound();
		updateTagCompound(var1, playerTag);
		return var1;
	}

	/**
	 * Returns the player's NBTTagCompound to be loaded
	 */
	public NBTTagCompound getPlayerNBTTagCompound() {
		return playerTag;
	}

	/**
	 * Return the number of ticks until rain.
	 */
	public int getRainTime() {
		return rainTime;
	}

	/**
	 * Returns the save version of this world
	 */
	public int getSaveVersion() {
		return saveVersion;
	}

	/**
	 * Returns the seed of current world.
	 */
	public long getSeed() {
		return randomSeed;
	}

	public long getSizeOnDisk() {
		return sizeOnDisk;
	}

	/**
	 * Returns the x spawn position
	 */
	public int getSpawnX() {
		return spawnX;
	}

	/**
	 * Return the Y axis spawning point of the player.
	 */
	public int getSpawnY() {
		return spawnY;
	}

	/**
	 * Returns the z spawn position
	 */
	public int getSpawnZ() {
		return spawnZ;
	}

	public WorldType getTerrainType() {
		return terrainType;
	}

	/**
	 * Returns the number of ticks until next thunderbolt.
	 */
	public int getThunderTime() {
		return thunderTime;
	}

	/**
	 * Returns vanilla MC dimension (-1,0,1). For custom dimension
	 * compatibility, always prefer WorldProvider.dimensionID accessed from
	 * World.provider.dimensionID
	 */
	public int getVanillaDimension() {
		return dimension;
	}

	/**
	 * Get current world name
	 */
	public String getWorldName() {
		return levelName;
	}

	/**
	 * Get current world time
	 */
	public long getWorldTime() {
		return worldTime;
	}

	public long getWorldTotalTime() {
		return totalTime;
	}

	public void incrementTotalWorldTime(long p_82572_1_) {
		totalTime = p_82572_1_;
	}

	/**
	 * Returns true if hardcore mode is enabled, otherwise false
	 */
	public boolean isHardcoreModeEnabled() {
		return hardcore;
	}

	/**
	 * Returns true if the World is initialized.
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Get whether the map features (e.g. strongholds) generation is enabled or
	 * disabled.
	 */
	public boolean isMapFeaturesEnabled() {
		return mapFeaturesEnabled;
	}

	/**
	 * Returns true if it is raining, false otherwise.
	 */
	public boolean isRaining() {
		return raining;
	}

	/**
	 * Returns true if it is thundering, false otherwise.
	 */
	public boolean isThundering() {
		return thundering;
	}

	/**
	 * Sets the GameType.
	 */
	public void setGameType(WorldSettings.GameType p_76060_1_) {
		theGameType = p_76060_1_;
	}

	/**
	 * Sets whether it is raining or not.
	 */
	public void setRaining(boolean p_76084_1_) {
		raining = p_76084_1_;
	}

	/**
	 * Sets the number of ticks until rain.
	 */
	public void setRainTime(int p_76080_1_) {
		rainTime = p_76080_1_;
	}

	/**
	 * Sets the save version of the world
	 */
	public void setSaveVersion(int p_76078_1_) {
		saveVersion = p_76078_1_;
	}

	/**
	 * Sets the initialization status of the World.
	 */
	public void setServerInitialized(boolean p_76091_1_) {
		initialized = p_76091_1_;
	}

	/**
	 * Sets the spawn zone position. Args: x, y, z
	 */
	public void setSpawnPosition(int p_76081_1_, int p_76081_2_, int p_76081_3_) {
		spawnX = p_76081_1_;
		spawnY = p_76081_2_;
		spawnZ = p_76081_3_;
	}

	/**
	 * Set the x spawn position to the passed in value
	 */
	public void setSpawnX(int p_76058_1_) {
		spawnX = p_76058_1_;
	}

	/**
	 * Sets the y spawn position
	 */
	public void setSpawnY(int p_76056_1_) {
		spawnY = p_76056_1_;
	}

	/**
	 * Set the z spawn position to the passed in value
	 */
	public void setSpawnZ(int p_76087_1_) {
		spawnZ = p_76087_1_;
	}

	public void setTerrainType(WorldType p_76085_1_) {
		terrainType = p_76085_1_;
	}

	/**
	 * Sets whether it is thundering or not.
	 */
	public void setThundering(boolean p_76069_1_) {
		thundering = p_76069_1_;
	}

	/**
	 * Defines the number of ticks until next thunderbolt.
	 */
	public void setThunderTime(int p_76090_1_) {
		thunderTime = p_76090_1_;
	}

	public void setWorldName(String p_76062_1_) {
		levelName = p_76062_1_;
	}

	/**
	 * Set current world time
	 */
	public void setWorldTime(long p_76068_1_) {
		worldTime = p_76068_1_;
	}

	private void updateTagCompound(NBTTagCompound p_76064_1_,
			NBTTagCompound p_76064_2_) {
		p_76064_1_.setLong("RandomSeed", randomSeed);
		p_76064_1_.setString("generatorName", terrainType.getWorldTypeName());
		p_76064_1_.setInteger("generatorVersion",
				terrainType.getGeneratorVersion());
		p_76064_1_.setString("generatorOptions", generatorOptions);
		p_76064_1_.setInteger("GameType", theGameType.getID());
		p_76064_1_.setBoolean("MapFeatures", mapFeaturesEnabled);
		p_76064_1_.setInteger("SpawnX", spawnX);
		p_76064_1_.setInteger("SpawnY", spawnY);
		p_76064_1_.setInteger("SpawnZ", spawnZ);
		p_76064_1_.setLong("Time", totalTime);
		p_76064_1_.setLong("DayTime", worldTime);
		p_76064_1_.setLong("SizeOnDisk", sizeOnDisk);
		p_76064_1_.setLong("LastPlayed", MinecraftServer.getSystemTimeMillis());
		p_76064_1_.setString("LevelName", levelName);
		p_76064_1_.setInteger("version", saveVersion);
		p_76064_1_.setInteger("rainTime", rainTime);
		p_76064_1_.setBoolean("raining", raining);
		p_76064_1_.setInteger("thunderTime", thunderTime);
		p_76064_1_.setBoolean("thundering", thundering);
		p_76064_1_.setBoolean("hardcore", hardcore);
		p_76064_1_.setBoolean("allowCommands", allowCommands);
		p_76064_1_.setBoolean("initialized", initialized);
		p_76064_1_.setTag("GameRules", theGameRules.writeGameRulesToNBT());

		if (p_76064_2_ != null) {
			p_76064_1_.setTag("Player", p_76064_2_);
		}
	}
}
