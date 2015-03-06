package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PotionEffect {
	/**
	 * Read a custom potion effect from a potion item's NBT data.
	 */
	public static PotionEffect readCustomPotionEffectFromNBT(
			NBTTagCompound p_82722_0_) {
		final byte var1 = p_82722_0_.getByte("Id");

		if (var1 >= 0 && var1 < Potion.potionTypes.length
				&& Potion.potionTypes[var1] != null) {
			final byte var2 = p_82722_0_.getByte("Amplifier");
			final int var3 = p_82722_0_.getInteger("Duration");
			final boolean var4 = p_82722_0_.getBoolean("Ambient");
			return new PotionEffect(var1, var3, var2, var4);
		} else
			return null;
	}

	/** The amplifier of the potion effect */
	private int amplifier;

	/** The duration of the potion effect */
	private int duration;

	/** Whether the potion effect came from a beacon */
	private boolean isAmbient;

	/** True if potion effect duration is at maximum, false otherwise. */
	private boolean isPotionDurationMax;

	/** Whether the potion is a splash potion */
	private boolean isSplashPotion;

	/** ID value of the potion this effect matches. */
	private final int potionID;

	public PotionEffect(int p_i1574_1_, int p_i1574_2_) {
		this(p_i1574_1_, p_i1574_2_, 0);
	}

	public PotionEffect(int p_i1575_1_, int p_i1575_2_, int p_i1575_3_) {
		this(p_i1575_1_, p_i1575_2_, p_i1575_3_, false);
	}

	public PotionEffect(int p_i1576_1_, int p_i1576_2_, int p_i1576_3_,
			boolean p_i1576_4_) {
		potionID = p_i1576_1_;
		duration = p_i1576_2_;
		amplifier = p_i1576_3_;
		isAmbient = p_i1576_4_;
	}

	public PotionEffect(PotionEffect p_i1577_1_) {
		potionID = p_i1577_1_.potionID;
		duration = p_i1577_1_.duration;
		amplifier = p_i1577_1_.amplifier;
	}

	/**
	 * merges the input PotionEffect into this one if this.amplifier <=
	 * tomerge.amplifier. The duration in the supplied potion effect is assumed
	 * to be greater.
	 */
	public void combine(PotionEffect p_76452_1_) {
		if (potionID != p_76452_1_.potionID) {
			System.err
					.println("This method should only be called for matching effects!");
		}

		if (p_76452_1_.amplifier > amplifier) {
			amplifier = p_76452_1_.amplifier;
			duration = p_76452_1_.duration;
		} else if (p_76452_1_.amplifier == amplifier
				&& duration < p_76452_1_.duration) {
			duration = p_76452_1_.duration;
		} else if (!p_76452_1_.isAmbient && isAmbient) {
			isAmbient = p_76452_1_.isAmbient;
		}
	}

	private int deincrementDuration() {
		return --duration;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof PotionEffect))
			return false;
		else {
			final PotionEffect var2 = (PotionEffect) p_equals_1_;
			return potionID == var2.potionID && amplifier == var2.amplifier
					&& duration == var2.duration
					&& isSplashPotion == var2.isSplashPotion
					&& isAmbient == var2.isAmbient;
		}
	}

	public int getAmplifier() {
		return amplifier;
	}

	public int getDuration() {
		return duration;
	}

	public String getEffectName() {
		return Potion.potionTypes[potionID].getName();
	}

	/**
	 * Gets whether this potion effect originated from a beacon
	 */
	public boolean getIsAmbient() {
		return isAmbient;
	}

	public boolean getIsPotionDurationMax() {
		return isPotionDurationMax;
	}

	/**
	 * Retrieve the ID of the potion this effect matches.
	 */
	public int getPotionID() {
		return potionID;
	}

	@Override
	public int hashCode() {
		return potionID;
	}

	public int incrementDuration() {
		return ++duration;
	}

	public boolean onUpdate(EntityLivingBase p_76455_1_) {
		if (duration > 0) {
			if (Potion.potionTypes[potionID].isReady(duration, amplifier)) {
				performEffect(p_76455_1_);
			}

			deincrementDuration();
		}

		return duration > 0;
	}

	public void performEffect(EntityLivingBase p_76457_1_) {
		if (duration > 0) {
			Potion.potionTypes[potionID].performEffect(p_76457_1_, amplifier);
		}
	}

	/**
	 * Toggle the isPotionDurationMax field.
	 */
	public void setPotionDurationMax(boolean p_100012_1_) {
		isPotionDurationMax = p_100012_1_;
	}

	/**
	 * Set whether this potion is a splash potion.
	 */
	public void setSplashPotion(boolean p_82721_1_) {
		isSplashPotion = p_82721_1_;
	}

	@Override
	public String toString() {
		String var1 = "";

		if (getAmplifier() > 0) {
			var1 = getEffectName() + " x " + (getAmplifier() + 1)
					+ ", Duration: " + getDuration();
		} else {
			var1 = getEffectName() + ", Duration: " + getDuration();
		}

		if (isSplashPotion) {
			var1 = var1 + ", Splash: true";
		}

		return Potion.potionTypes[potionID].isUsable() ? "(" + var1 + ")"
				: var1;
	}

	/**
	 * Write a custom potion effect to a potion item's NBT data.
	 */
	public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound p_82719_1_) {
		p_82719_1_.setByte("Id", (byte) getPotionID());
		p_82719_1_.setByte("Amplifier", (byte) getAmplifier());
		p_82719_1_.setInteger("Duration", getDuration());
		p_82719_1_.setBoolean("Ambient", getIsAmbient());
		return p_82719_1_;
	}
}
