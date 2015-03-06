package net.minecraft.entity.ai.attributes;

public class RangedAttribute extends BaseAttribute {
	private String description;
	private final double maximumValue;
	private final double minimumValue;

	public RangedAttribute(String p_i1609_1_, double p_i1609_2_,
			double p_i1609_4_, double p_i1609_6_) {
		super(p_i1609_1_, p_i1609_2_);
		minimumValue = p_i1609_4_;
		maximumValue = p_i1609_6_;

		if (p_i1609_4_ > p_i1609_6_)
			throw new IllegalArgumentException(
					"Minimum value cannot be bigger than maximum value!");
		else if (p_i1609_2_ < p_i1609_4_)
			throw new IllegalArgumentException(
					"Default value cannot be lower than minimum value!");
		else if (p_i1609_2_ > p_i1609_6_)
			throw new IllegalArgumentException(
					"Default value cannot be bigger than maximum value!");
	}

	@Override
	public double clampValue(double p_111109_1_) {
		if (p_111109_1_ < minimumValue) {
			p_111109_1_ = minimumValue;
		}

		if (p_111109_1_ > maximumValue) {
			p_111109_1_ = maximumValue;
		}

		return p_111109_1_;
	}

	public String getDescription() {
		return description;
	}

	public RangedAttribute setDescription(String p_111117_1_) {
		description = p_111117_1_;
		return this;
	}
}
