package net.minecraft.client.audio;

import java.util.Map;

import com.google.common.collect.Maps;

public enum SoundCategory {
	AMBIENT("ambient", 8), ANIMALS("neutral", 6), BLOCKS("block", 4), MASTER(
			"master", 0), MOBS("hostile", 5), MUSIC("music", 1), PLAYERS(
			"player", 7), RECORDS("record", 2), WEATHER("weather", 3);
	private static final Map field_147168_j = Maps.newHashMap();

	private static final Map field_147169_k = Maps.newHashMap();
	static {
		final SoundCategory[] var0 = values();
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final SoundCategory var3 = var0[var2];

			if (field_147168_j.containsKey(var3.getCategoryName())
					|| field_147169_k.containsKey(Integer.valueOf(var3
							.getCategoryId())))
				throw new Error(
						"Clash in Sound Category ID & Name pools! Cannot insert "
								+ var3);

			field_147168_j.put(var3.getCategoryName(), var3);
			field_147169_k.put(Integer.valueOf(var3.getCategoryId()), var3);
		}
	}

	public static SoundCategory func_147154_a(String p_147154_0_) {
		return (SoundCategory) field_147168_j.get(p_147154_0_);
	}

	private final int categoryId;

	private final String categoryName;

	private SoundCategory(String p_i45126_3_, int p_i45126_4_) {
		categoryName = p_i45126_3_;
		categoryId = p_i45126_4_;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
