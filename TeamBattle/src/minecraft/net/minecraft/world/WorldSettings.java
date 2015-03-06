package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
	public static enum GameType {
		ADVENTURE("ADVENTURE", 3, 2, "adventure"), CREATIVE("CREATIVE", 2, 1,
				"creative"), NOT_SET("NOT_SET", 0, -1, ""), SURVIVAL(
				"SURVIVAL", 1, 0, "survival");
		public static WorldSettings.GameType getByID(int p_77146_0_) {
			final WorldSettings.GameType[] var1 = values();
			final int var2 = var1.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				final WorldSettings.GameType var4 = var1[var3];

				if (var4.id == p_77146_0_)
					return var4;
			}

			return SURVIVAL;
		}

		public static WorldSettings.GameType getByName(String p_77142_0_) {
			final WorldSettings.GameType[] var1 = values();
			final int var2 = var1.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				final WorldSettings.GameType var4 = var1[var3];

				if (var4.name.equals(p_77142_0_))
					return var4;
			}

			return SURVIVAL;
		}

		int id;

		String name;

		private GameType(String p_i1956_1_, int p_i1956_2_, int p_i1956_3_,
				String p_i1956_4_) {
			id = p_i1956_3_;
			name = p_i1956_4_;
		}

		public void configurePlayerCapabilities(PlayerCapabilities p_77147_1_) {
			if (this == CREATIVE) {
				p_77147_1_.allowFlying = true;
				p_77147_1_.isCreativeMode = true;
				p_77147_1_.disableDamage = true;
			} else {
				p_77147_1_.allowFlying = false;
				p_77147_1_.isCreativeMode = false;
				p_77147_1_.disableDamage = false;
				p_77147_1_.isFlying = false;
			}

			p_77147_1_.allowEdit = !isAdventure();
		}

		public int getID() {
			return id;
		}

		public String getName() {
			return name;
		}

		public boolean isAdventure() {
			return this == ADVENTURE;
		}

		public boolean isCreative() {
			return this == CREATIVE;
		}

		public boolean isSurvivalOrAdventure() {
			return this == SURVIVAL || this == ADVENTURE;
		}
	}

	/**
	 * Gets the GameType by ID
	 */
	public static WorldSettings.GameType getGameTypeById(int p_77161_0_) {
		return WorldSettings.GameType.getByID(p_77161_0_);
	}

	/** True if the Bonus Chest is enabled. */
	private boolean bonusChestEnabled;

	/** True if Commands (cheats) are allowed. */
	private boolean commandsAllowed;
	private String field_82751_h;

	/** True if hardcore mode is enabled */
	private final boolean hardcoreEnabled;

	/**
	 * Switch for the map features. 'true' for enabled, 'false' for disabled.
	 */
	private final boolean mapFeaturesEnabled;
	/** The seed for the map. */
	private final long seed;

	private final WorldType terrainType;

	/** The EnumGameType. */
	private final WorldSettings.GameType theGameType;

	public WorldSettings(long p_i1957_1_, WorldSettings.GameType p_i1957_3_,
			boolean p_i1957_4_, boolean p_i1957_5_, WorldType p_i1957_6_) {
		field_82751_h = "";
		seed = p_i1957_1_;
		theGameType = p_i1957_3_;
		mapFeaturesEnabled = p_i1957_4_;
		hardcoreEnabled = p_i1957_5_;
		terrainType = p_i1957_6_;
	}

	public WorldSettings(WorldInfo p_i1958_1_) {
		this(p_i1958_1_.getSeed(), p_i1958_1_.getGameType(), p_i1958_1_
				.isMapFeaturesEnabled(), p_i1958_1_.isHardcoreModeEnabled(),
				p_i1958_1_.getTerrainType());
	}

	/**
	 * Returns true if Commands (cheats) are allowed.
	 */
	public boolean areCommandsAllowed() {
		return commandsAllowed;
	}

	/**
	 * Enables the bonus chest.
	 */
	public WorldSettings enableBonusChest() {
		bonusChestEnabled = true;
		return this;
	}

	/**
	 * Enables Commands (cheats).
	 */
	public WorldSettings enableCommands() {
		commandsAllowed = true;
		return this;
	}

	public String func_82749_j() {
		return field_82751_h;
	}

	public WorldSettings func_82750_a(String p_82750_1_) {
		field_82751_h = p_82750_1_;
		return this;
	}

	/**
	 * Gets the game type.
	 */
	public WorldSettings.GameType getGameType() {
		return theGameType;
	}

	/**
	 * Returns true if hardcore mode is enabled, otherwise false
	 */
	public boolean getHardcoreEnabled() {
		return hardcoreEnabled;
	}

	/**
	 * Returns the seed for the world.
	 */
	public long getSeed() {
		return seed;
	}

	public WorldType getTerrainType() {
		return terrainType;
	}

	/**
	 * Returns true if the Bonus Chest is enabled.
	 */
	public boolean isBonusChestEnabled() {
		return bonusChestEnabled;
	}

	/**
	 * Get whether the map features (e.g. strongholds) generation is enabled or
	 * disabled.
	 */
	public boolean isMapFeaturesEnabled() {
		return mapFeaturesEnabled;
	}
}
