package net.minecraft.util;

import java.util.Random;

public class EnchantmentNameParts {
	public static final EnchantmentNameParts instance = new EnchantmentNameParts();
	private final String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale "
			.split(" ");
	private final Random rand = new Random();

	/**
	 * Randomly generates a new name built up of 3 or 4 randomly selected words.
	 */
	public String generateNewRandomName() {
		final int var1 = rand.nextInt(2) + 3;
		String var2 = "";

		for (int var3 = 0; var3 < var1; ++var3) {
			if (var3 > 0) {
				var2 = var2 + " ";
			}

			var2 = var2 + namePartsArray[rand.nextInt(namePartsArray.length)];
		}

		return var2;
	}

	/**
	 * Resets the underlying random number generator using a given seed.
	 */
	public void reseedRandomGenerator(long p_148335_1_) {
		rand.setSeed(p_148335_1_);
	}
}
