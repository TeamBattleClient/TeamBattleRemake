package net.minecraft.world;

public enum EnumSkyBlock {
	Block(0), Sky(15);
	public final int defaultLightValue;

	private EnumSkyBlock(int p_i1961_3_) {
		defaultLightValue = p_i1961_3_;
	}
}
