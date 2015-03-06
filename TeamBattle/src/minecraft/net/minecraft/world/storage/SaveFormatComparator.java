package net.minecraft.world.storage;

import net.minecraft.world.WorldSettings;

public class SaveFormatComparator implements Comparable {
	private final boolean cheatsEnabled;

	/** the displayed name of this save file */
	private final String displayName;
	/** the file name of this save */
	private final String fileName;
	private final boolean hardcore;
	private final long lastTimePlayed;

	private final boolean requiresConversion;
	private final long sizeOnDisk;
	/** Instance of EnumGameType. */
	private final WorldSettings.GameType theEnumGameType;

	public SaveFormatComparator(String p_i2161_1_, String p_i2161_2_,
			long p_i2161_3_, long p_i2161_5_,
			WorldSettings.GameType p_i2161_7_, boolean p_i2161_8_,
			boolean p_i2161_9_, boolean p_i2161_10_) {
		fileName = p_i2161_1_;
		displayName = p_i2161_2_;
		lastTimePlayed = p_i2161_3_;
		sizeOnDisk = p_i2161_5_;
		theEnumGameType = p_i2161_7_;
		requiresConversion = p_i2161_8_;
		hardcore = p_i2161_9_;
		cheatsEnabled = p_i2161_10_;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((SaveFormatComparator) p_compareTo_1_);
	}

	public int compareTo(SaveFormatComparator p_compareTo_1_) {
		return lastTimePlayed < p_compareTo_1_.lastTimePlayed ? 1
				: lastTimePlayed > p_compareTo_1_.lastTimePlayed ? -1
						: fileName.compareTo(p_compareTo_1_.fileName);
	}

	public long func_154336_c() {
		return sizeOnDisk;
	}

	/**
	 * @return {@code true} if cheats are enabled for this world
	 */
	public boolean getCheatsEnabled() {
		return cheatsEnabled;
	}

	/**
	 * return the display name of the save
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the EnumGameType.
	 */
	public WorldSettings.GameType getEnumGameType() {
		return theEnumGameType;
	}

	/**
	 * return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	public long getLastTimePlayed() {
		return lastTimePlayed;
	}

	public boolean isHardcoreModeEnabled() {
		return hardcore;
	}

	public boolean requiresConversion() {
		return requiresConversion;
	}
}
