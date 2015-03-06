package net.minecraft.entity.ai.attributes;

public interface IAttribute {
	double clampValue(double p_111109_1_);

	String getAttributeUnlocalizedName();

	double getDefaultValue();

	boolean getShouldWatch();
}
