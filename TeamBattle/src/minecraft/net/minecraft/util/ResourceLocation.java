package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class ResourceLocation {
	private final String resourceDomain;
	private final String resourcePath;

	public ResourceLocation(String p_i1293_1_) {
		String var2 = "minecraft";
		String var3 = p_i1293_1_;
		final int var4 = p_i1293_1_.indexOf(58);

		if (var4 >= 0) {
			var3 = p_i1293_1_.substring(var4 + 1, p_i1293_1_.length());

			if (var4 > 1) {
				var2 = p_i1293_1_.substring(0, var4);
			}
		}

		resourceDomain = var2.toLowerCase();
		resourcePath = var3;
	}

	public ResourceLocation(String p_i1292_1_, String p_i1292_2_) {
		Validate.notNull(p_i1292_2_);

		if (p_i1292_1_ != null && p_i1292_1_.length() != 0) {
			resourceDomain = p_i1292_1_;
		} else {
			resourceDomain = "minecraft";
		}

		resourcePath = p_i1292_2_;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ResourceLocation))
			return false;
		else {
			final ResourceLocation var2 = (ResourceLocation) p_equals_1_;
			return resourceDomain.equals(var2.resourceDomain)
					&& resourcePath.equals(var2.resourcePath);
		}
	}

	public String getResourceDomain() {
		return resourceDomain;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public int hashCode() {
		return 31 * resourceDomain.hashCode() + resourcePath.hashCode();
	}

	@Override
	public String toString() {
		return resourceDomain + ":" + resourcePath;
	}
}
