package net.minecraft.world;

public class WorldType {
	/** List of world types. */
	public static final WorldType[] worldTypes = new WorldType[16];
	/** Default world type. */
	public static final WorldType DEFAULT = new WorldType(0, "default", 1)
			.setVersioned();

	/** Default (1.1) world type. */
	public static final WorldType DEFAULT_1_1 = new WorldType(8, "default_1_1",
			0).setCanBeCreated(false);

	public static final WorldType field_151360_e = new WorldType(3, "amplified")
			.func_151358_j();

	/** Flat world type. */
	public static final WorldType FLAT = new WorldType(1, "flat");
	/** Large Biome world Type. */
	public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");

	public static WorldType parseWorldType(String p_77130_0_) {
		for (final WorldType worldType2 : worldTypes) {
			if (worldType2 != null
					&& worldType2.worldType.equalsIgnoreCase(p_77130_0_))
				return worldType2;
		}

		return null;
	}

	/**
	 * Whether this world type can be generated. Normally true; set to false for
	 * out-of-date generator versions.
	 */
	private boolean canBeCreated;

	private boolean field_151361_l;

	/** The int version of the ChunkProvider that generated this world. */
	private final int generatorVersion;

	/** Whether this WorldType has a version or not. */
	private boolean isWorldTypeVersioned;
	/** 'default' or 'flat' */
	private final String worldType;

	/** ID for this world type. */
	private final int worldTypeId;

	private WorldType(int p_i1959_1_, String p_i1959_2_) {
		this(p_i1959_1_, p_i1959_2_, 0);
	}

	private WorldType(int p_i1960_1_, String p_i1960_2_, int p_i1960_3_) {
		worldType = p_i1960_2_;
		generatorVersion = p_i1960_3_;
		canBeCreated = true;
		worldTypeId = p_i1960_1_;
		worldTypes[p_i1960_1_] = this;
	}

	public boolean func_151357_h() {
		return field_151361_l;
	}

	private WorldType func_151358_j() {
		field_151361_l = true;
		return this;
	}

	public String func_151359_c() {
		return getTranslateName() + ".info";
	}

	/**
	 * Gets whether this WorldType can be used to generate a new world.
	 */
	public boolean getCanBeCreated() {
		return canBeCreated;
	}

	/**
	 * Returns generatorVersion.
	 */
	public int getGeneratorVersion() {
		return generatorVersion;
	}

	/**
	 * Gets the translation key for the name of this world type.
	 */
	public String getTranslateName() {
		return "generator." + worldType;
	}

	public WorldType getWorldTypeForGeneratorVersion(int p_77132_1_) {
		return this == DEFAULT && p_77132_1_ == 0 ? DEFAULT_1_1 : this;
	}

	public int getWorldTypeID() {
		return worldTypeId;
	}

	public String getWorldTypeName() {
		return worldType;
	}

	/**
	 * Returns true if this world Type has a version associated with it.
	 */
	public boolean isVersioned() {
		return isWorldTypeVersioned;
	}

	/**
	 * Sets canBeCreated to the provided value, and returns this.
	 */
	private WorldType setCanBeCreated(boolean p_77124_1_) {
		canBeCreated = p_77124_1_;
		return this;
	}

	/**
	 * Flags this world type as having an associated version.
	 */
	private WorldType setVersioned() {
		isWorldTypeVersioned = true;
		return this;
	}
}
