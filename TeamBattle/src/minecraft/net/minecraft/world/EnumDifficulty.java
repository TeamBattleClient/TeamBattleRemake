package net.minecraft.world;

public enum EnumDifficulty {
	EASY(1, "options.difficulty.easy"), HARD(3, "options.difficulty.hard"), NORMAL(
			2, "options.difficulty.normal"), PEACEFUL(0,
			"options.difficulty.peaceful");
	private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[values().length];

	static {
		final EnumDifficulty[] var0 = values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final EnumDifficulty var3 = var0[var2];
			difficultyEnums[var3.difficultyId] = var3;
		}
	}

	public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
		return difficultyEnums[p_151523_0_ % difficultyEnums.length];
	}

	private final int difficultyId;

	private final String difficultyResourceKey;

	private EnumDifficulty(int p_i45312_3_, String p_i45312_4_) {
		difficultyId = p_i45312_3_;
		difficultyResourceKey = p_i45312_4_;
	}

	public int getDifficultyId() {
		return difficultyId;
	}

	public String getDifficultyResourceKey() {
		return difficultyResourceKey;
	}
}
