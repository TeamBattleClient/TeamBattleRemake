package net.minecraft.client.resources;

public class Language implements Comparable {
	private final boolean bidirectional;
	private final String languageCode;
	private final String name;
	private final String region;

	public Language(String p_i1303_1_, String p_i1303_2_, String p_i1303_3_,
			boolean p_i1303_4_) {
		languageCode = p_i1303_1_;
		region = p_i1303_2_;
		name = p_i1303_3_;
		bidirectional = p_i1303_4_;
	}

	public int compareTo(Language p_compareTo_1_) {
		return languageCode.compareTo(p_compareTo_1_.languageCode);
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((Language) p_compareTo_1_);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return this == p_equals_1_ ? true
				: !(p_equals_1_ instanceof Language) ? false : languageCode
						.equals(((Language) p_equals_1_).languageCode);
	}

	public String getLanguageCode() {
		return languageCode;
	}

	@Override
	public int hashCode() {
		return languageCode.hashCode();
	}

	public boolean isBidirectional() {
		return bidirectional;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", new Object[] { name, region });
	}
}
