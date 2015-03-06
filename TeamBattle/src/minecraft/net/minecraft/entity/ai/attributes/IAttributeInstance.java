package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public interface IAttributeInstance {
	void applyModifier(AttributeModifier p_111121_1_);

	Collection func_111122_c();

	/**
	 * Get the Attribute this is an instance of
	 */
	IAttribute getAttribute();

	double getAttributeValue();

	double getBaseValue();

	/**
	 * Returns attribute modifier, if any, by the given UUID
	 */
	AttributeModifier getModifier(UUID p_111127_1_);

	void removeAllModifiers();

	void removeModifier(AttributeModifier p_111124_1_);

	void setBaseValue(double p_111128_1_);
}
