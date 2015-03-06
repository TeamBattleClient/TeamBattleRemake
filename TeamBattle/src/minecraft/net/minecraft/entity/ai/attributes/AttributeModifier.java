package net.minecraft.entity.ai.attributes;

import java.util.UUID;

import org.apache.commons.lang3.Validate;

public class AttributeModifier {
	private final double amount;
	private final UUID id;
	/**
	 * If false, this modifier is not saved in NBT. Used for "natural" modifiers
	 * like speed boost from sprinting
	 */
	private boolean isSaved;
	private final String name;

	private final int operation;

	public AttributeModifier(String p_i1605_1_, double p_i1605_2_,
			int p_i1605_4_) {
		this(UUID.randomUUID(), p_i1605_1_, p_i1605_2_, p_i1605_4_);
	}

	public AttributeModifier(UUID p_i1606_1_, String p_i1606_2_,
			double p_i1606_3_, int p_i1606_5_) {
		isSaved = true;
		id = p_i1606_1_;
		name = p_i1606_2_;
		amount = p_i1606_3_;
		operation = p_i1606_5_;
		Validate.notEmpty(p_i1606_2_, "Modifier name cannot be empty",
				new Object[0]);
		Validate.inclusiveBetween(Integer.valueOf(0), Integer.valueOf(2),
				Integer.valueOf(p_i1606_5_), "Invalid operation", new Object[0]);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ != null
				&& this.getClass() == p_equals_1_.getClass()) {
			final AttributeModifier var2 = (AttributeModifier) p_equals_1_;

			if (id != null) {
				if (!id.equals(var2.id))
					return false;
			} else if (var2.id != null)
				return false;

			return true;
		} else
			return false;
	}

	public double getAmount() {
		return amount;
	}

	public UUID getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getOperation() {
		return operation;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * @see #isSaved
	 */
	public boolean isSaved() {
		return isSaved;
	}

	/**
	 * @see #isSaved
	 */
	public AttributeModifier setSaved(boolean p_111168_1_) {
		isSaved = p_111168_1_;
		return this;
	}

	@Override
	public String toString() {
		return "AttributeModifier{amount=" + amount + ", operation="
				+ operation + ", name=\'" + name + '\'' + ", id=" + id
				+ ", serialize=" + isSaved + '}';
	}
}
