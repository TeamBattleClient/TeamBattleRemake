package net.minecraft.potion;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StringUtils;

import com.google.common.collect.Maps;

public class Potion {
	/** The array of potion types. */
	public static final Potion[] potionTypes = new Potion[32];
	/** The blindness Potion object. */
	public static final Potion blindness = new Potion(15, true, 2039587)
			.setPotionName("potion.blindness").setIconIndex(5, 1)
			.setEffectiveness(0.25D);

	public static final Potion confusion = new Potion(9, true, 5578058)
			.setPotionName("potion.confusion").setIconIndex(3, 1)
			.setEffectiveness(0.25D);
	public static final Potion damageBoost = new PotionAttackDamage(5, false,
			9643043)
			.setPotionName("potion.damageBoost")
			.setIconIndex(4, 0)
			.func_111184_a(SharedMonsterAttributes.attackDamage,
					"648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 3.0D, 2);
	public static final Potion digSlowdown = new Potion(4, true, 4866583)
			.setPotionName("potion.digSlowDown").setIconIndex(3, 0);
	public static final Potion digSpeed = new Potion(3, false, 14270531)
			.setPotionName("potion.digSpeed").setIconIndex(2, 0)
			.setEffectiveness(1.5D);
	public static final Potion field_76405_D = null;
	public static final Potion field_76406_E = null;
	public static final Potion field_76407_F = null;
	public static final Potion field_76408_G = null;
	public static final Potion field_76409_A = null;
	public static final Potion field_76410_B = null;

	public static final Potion field_76411_C = null;
	public static final Potion field_76423_b = null;

	public static final Potion field_76434_w = new PotionHealthBoost(21, false,
			16284963)
			.setPotionName("potion.healthBoost")
			.setIconIndex(2, 2)
			.func_111184_a(SharedMonsterAttributes.maxHealth,
					"5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0);

	public static final Potion field_76442_z = null;

	public static final Potion field_76443_y = new PotionHealth(23, false,
			16262179).setPotionName("potion.saturation");

	public static final Potion field_76444_x = new PotionAbsoption(22, false,
			2445989).setPotionName("potion.absorption").setIconIndex(2, 2);

	/** The fire resistance Potion object. */
	public static final Potion fireResistance = new Potion(12, false, 14981690)
			.setPotionName("potion.fireResistance").setIconIndex(7, 1);

	public static final Potion harm = new PotionHealth(7, true, 4393481)
			.setPotionName("potion.harm");

	public static final Potion heal = new PotionHealth(6, false, 16262179)
			.setPotionName("potion.heal");

	/** The hunger Potion object. */
	public static final Potion hunger = new Potion(17, true, 5797459)
			.setPotionName("potion.hunger").setIconIndex(1, 1);

	/** The invisibility Potion object. */
	public static final Potion invisibility = new Potion(14, false, 8356754)
			.setPotionName("potion.invisibility").setIconIndex(0, 1);
	public static final Potion jump = new Potion(8, false, 7889559)
			.setPotionName("potion.jump").setIconIndex(2, 1);
	public static final Potion moveSlowdown = new Potion(2, true, 5926017)
			.setPotionName("potion.moveSlowdown")
			.setIconIndex(1, 0)
			.func_111184_a(SharedMonsterAttributes.movementSpeed,
					"7107DE5E-7CE8-4030-940E-514C1F160890",
					-0.15000000596046448D, 2);
	public static final Potion moveSpeed = new Potion(1, false, 8171462)
			.setPotionName("potion.moveSpeed")
			.setIconIndex(0, 0)
			.func_111184_a(SharedMonsterAttributes.movementSpeed,
					"91AEAA56-376B-4498-935B-2F7F68070635",
					0.20000000298023224D, 2);
	/** The night vision Potion object. */
	public static final Potion nightVision = new Potion(16, false, 2039713)
			.setPotionName("potion.nightVision").setIconIndex(4, 1);
	/** The poison Potion object. */
	public static final Potion poison = new Potion(19, true, 5149489)
			.setPotionName("potion.poison").setIconIndex(6, 0)
			.setEffectiveness(0.25D);
	/** The regeneration Potion object. */
	public static final Potion regeneration = new Potion(10, false, 13458603)
			.setPotionName("potion.regeneration").setIconIndex(7, 0)
			.setEffectiveness(0.25D);
	public static final Potion resistance = new Potion(11, false, 10044730)
			.setPotionName("potion.resistance").setIconIndex(6, 1);
	/** The water breathing Potion object. */
	public static final Potion waterBreathing = new Potion(13, false, 3035801)
			.setPotionName("potion.waterBreathing").setIconIndex(0, 2);
	/** The weakness Potion object. */
	public static final Potion weakness = new PotionAttackDamage(18, true,
			4738376)
			.setPotionName("potion.weakness")
			.setIconIndex(5, 0)
			.func_111184_a(SharedMonsterAttributes.attackDamage,
					"22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);
	/** The wither Potion object. */
	public static final Potion wither = new Potion(20, true, 3484199)
			.setPotionName("potion.wither").setIconIndex(1, 2)
			.setEffectiveness(0.25D);

	public static String getDurationString(PotionEffect p_76389_0_) {
		if (p_76389_0_.getIsPotionDurationMax())
			return "**:**";
		else {
			final int var1 = p_76389_0_.getDuration();
			return StringUtils.ticksToElapsedTime(var1);
		}
	}

	private double effectiveness;

	private final Map field_111188_I = Maps.newHashMap();

	/** The Id of a Potion object. */
	public final int id;

	/**
	 * This field indicated if the effect is 'bad' - negative - for the entity.
	 */
	private final boolean isBadEffect;

	/** Is the color of the liquid for this potion. */
	private final int liquidColor;
	/** The name of the Potion. */
	private String name = "";
	/** The index for the icon displayed when the potion effect is active. */
	private int statusIconIndex = -1;

	private boolean usable;

	protected Potion(int p_i1573_1_, boolean p_i1573_2_, int p_i1573_3_) {
		id = p_i1573_1_;
		isBadEffect = p_i1573_2_;
		potionTypes[p_i1573_1_] = this;

		if (p_i1573_2_) {
			effectiveness = 0.5D;
		} else {
			effectiveness = 1.0D;
		}

		liquidColor = p_i1573_3_;
	}

	/**
	 * Hits the provided entity with this potion's instant effect.
	 */
	public void affectEntity(EntityLivingBase p_76402_1_,
			EntityLivingBase p_76402_2_, int p_76402_3_, double p_76402_4_) {
		int var6;

		if ((id != heal.id || p_76402_2_.isEntityUndead())
				&& (id != harm.id || !p_76402_2_.isEntityUndead())) {
			if (id == harm.id && !p_76402_2_.isEntityUndead() || id == heal.id
					&& p_76402_2_.isEntityUndead()) {
				var6 = (int) (p_76402_4_ * (6 << p_76402_3_) + 0.5D);

				if (p_76402_1_ == null) {
					p_76402_2_.attackEntityFrom(DamageSource.magic, var6);
				} else {
					p_76402_2_.attackEntityFrom(DamageSource
							.causeIndirectMagicDamage(p_76402_2_, p_76402_1_),
							var6);
				}
			}
		} else {
			var6 = (int) (p_76402_4_ * (4 << p_76402_3_) + 0.5D);
			p_76402_2_.heal(var6);
		}
	}

	public void applyAttributesModifiersToEntity(EntityLivingBase p_111185_1_,
			BaseAttributeMap p_111185_2_, int p_111185_3_) {
		final Iterator var4 = field_111188_I.entrySet().iterator();

		while (var4.hasNext()) {
			final Entry var5 = (Entry) var4.next();
			final IAttributeInstance var6 = p_111185_2_
					.getAttributeInstance((IAttribute) var5.getKey());

			if (var6 != null) {
				final AttributeModifier var7 = (AttributeModifier) var5
						.getValue();
				var6.removeModifier(var7);
				var6.applyModifier(new AttributeModifier(var7.getID(),
						getName() + " " + p_111185_3_, func_111183_a(
								p_111185_3_, var7), var7.getOperation()));
			}
		}
	}

	public double func_111183_a(int p_111183_1_, AttributeModifier p_111183_2_) {
		return p_111183_2_.getAmount() * (p_111183_1_ + 1);
	}

	public Potion func_111184_a(IAttribute p_111184_1_, String p_111184_2_,
			double p_111184_3_, int p_111184_5_) {
		final AttributeModifier var6 = new AttributeModifier(
				UUID.fromString(p_111184_2_), getName(), p_111184_3_,
				p_111184_5_);
		field_111188_I.put(p_111184_1_, var6);
		return this;
	}

	public Map func_111186_k() {
		return field_111188_I;
	}

	public double getEffectiveness() {
		return effectiveness;
	}

	/**
	 * returns the ID of the potion
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the color of the potion liquid.
	 */
	public int getLiquidColor() {
		return liquidColor;
	}

	/**
	 * returns the name of the potion
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the index for the icon to display when the potion is active.
	 */
	public int getStatusIconIndex() {
		return statusIconIndex;
	}

	/**
	 * Returns true if the potion has a associated status icon to display in
	 * then inventory when active.
	 */
	public boolean hasStatusIcon() {
		return statusIconIndex >= 0;
	}

	/**
	 * This method returns true if the potion effect is bad - negative - for the
	 * entity.
	 */
	public boolean isBadEffect() {
		return isBadEffect;
	}

	/**
	 * Returns true if the potion has an instant effect instead of a continuous
	 * one (eg Harming)
	 */
	public boolean isInstant() {
		return false;
	}

	/**
	 * checks if Potion effect is ready to be applied this tick.
	 */
	public boolean isReady(int p_76397_1_, int p_76397_2_) {
		int var3;

		if (id == regeneration.id) {
			var3 = 50 >> p_76397_2_;
			return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
		} else if (id == poison.id) {
			var3 = 25 >> p_76397_2_;
			return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
		} else if (id == wither.id) {
			var3 = 40 >> p_76397_2_;
			return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
		} else
			return id == hunger.id;
	}

	public boolean isUsable() {
		return usable;
	}

	public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {
		if (id == regeneration.id) {
			if (p_76394_1_.getHealth() < p_76394_1_.getMaxHealth()) {
				p_76394_1_.heal(1.0F);
			}
		} else if (id == poison.id) {
			if (p_76394_1_.getHealth() > 1.0F) {
				p_76394_1_.attackEntityFrom(DamageSource.magic, 1.0F);
			}
		} else if (id == wither.id) {
			p_76394_1_.attackEntityFrom(DamageSource.wither, 1.0F);
		} else if (id == hunger.id && p_76394_1_ instanceof EntityPlayer) {
			((EntityPlayer) p_76394_1_)
					.addExhaustion(0.025F * (p_76394_2_ + 1));
		} else if (id == field_76443_y.id && p_76394_1_ instanceof EntityPlayer) {
			if (!p_76394_1_.worldObj.isClient) {
				((EntityPlayer) p_76394_1_).getFoodStats().addStats(
						p_76394_2_ + 1, 1.0F);
			}
		} else if ((id != heal.id || p_76394_1_.isEntityUndead())
				&& (id != harm.id || !p_76394_1_.isEntityUndead())) {
			if (id == harm.id && !p_76394_1_.isEntityUndead() || id == heal.id
					&& p_76394_1_.isEntityUndead()) {
				p_76394_1_
						.attackEntityFrom(DamageSource.magic, 6 << p_76394_2_);
			}
		} else {
			p_76394_1_.heal(Math.max(4 << p_76394_2_, 0));
		}
	}

	public void removeAttributesModifiersFromEntity(
			EntityLivingBase p_111187_1_, BaseAttributeMap p_111187_2_,
			int p_111187_3_) {
		final Iterator var4 = field_111188_I.entrySet().iterator();

		while (var4.hasNext()) {
			final Entry var5 = (Entry) var4.next();
			final IAttributeInstance var6 = p_111187_2_
					.getAttributeInstance((IAttribute) var5.getKey());

			if (var6 != null) {
				var6.removeModifier((AttributeModifier) var5.getValue());
			}
		}
	}

	protected Potion setEffectiveness(double p_76404_1_) {
		effectiveness = p_76404_1_;
		return this;
	}

	/**
	 * Sets the index for the icon displayed in the player's inventory when the
	 * status is active.
	 */
	protected Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
		statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
		return this;
	}

	/**
	 * Set the potion name.
	 */
	public Potion setPotionName(String p_76390_1_) {
		name = p_76390_1_;
		return this;
	}
}
