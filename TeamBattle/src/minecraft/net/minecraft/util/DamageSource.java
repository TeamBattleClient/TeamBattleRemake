package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

public class DamageSource {
	public static DamageSource anvil = new DamageSource("anvil");

	public static DamageSource cactus = new DamageSource("cactus");

	public static DamageSource drown = new DamageSource("drown")
			.setDamageBypassesArmor();

	public static DamageSource fall = new DamageSource("fall")
			.setDamageBypassesArmor();

	public static DamageSource fallingBlock = new DamageSource("fallingBlock");

	public static DamageSource generic = new DamageSource("generic")
			.setDamageBypassesArmor();

	public static DamageSource inFire = new DamageSource("inFire")
			.setFireDamage();

	public static DamageSource inWall = new DamageSource("inWall")
			.setDamageBypassesArmor();

	public static DamageSource lava = new DamageSource("lava").setFireDamage();
	public static DamageSource magic = new DamageSource("magic")
			.setDamageBypassesArmor().setMagicDamage();
	public static DamageSource onFire = new DamageSource("onFire")
			.setDamageBypassesArmor().setFireDamage();
	public static DamageSource outOfWorld = new DamageSource("outOfWorld")
			.setDamageBypassesArmor().setDamageAllowedInCreativeMode();
	public static DamageSource starve = new DamageSource("starve")
			.setDamageBypassesArmor().setDamageIsAbsolute();
	public static DamageSource wither = new DamageSource("wither")
			.setDamageBypassesArmor();

	/**
	 * returns EntityDamageSourceIndirect of an arrow
	 */
	public static DamageSource causeArrowDamage(EntityArrow p_76353_0_,
			Entity p_76353_1_) {
		return new EntityDamageSourceIndirect("arrow", p_76353_0_, p_76353_1_)
				.setProjectile();
	}

	/**
	 * returns EntityDamageSourceIndirect of a fireball
	 */
	public static DamageSource causeFireballDamage(EntityFireball p_76362_0_,
			Entity p_76362_1_) {
		return p_76362_1_ == null ? new EntityDamageSourceIndirect("onFire",
				p_76362_0_, p_76362_0_).setFireDamage().setProjectile()
				: new EntityDamageSourceIndirect("fireball", p_76362_0_,
						p_76362_1_).setFireDamage().setProjectile();
	}

	public static DamageSource causeIndirectMagicDamage(Entity p_76354_0_,
			Entity p_76354_1_) {
		return new EntityDamageSourceIndirect("indirectMagic", p_76354_0_,
				p_76354_1_).setDamageBypassesArmor().setMagicDamage();
	}

	public static DamageSource causeMobDamage(EntityLivingBase p_76358_0_) {
		return new EntityDamageSource("mob", p_76358_0_);
	}

	/**
	 * returns an EntityDamageSource of type player
	 */
	public static DamageSource causePlayerDamage(EntityPlayer p_76365_0_) {
		return new EntityDamageSource("player", p_76365_0_);
	}

	/**
	 * Returns the EntityDamageSource of the Thorns enchantment
	 */
	public static DamageSource causeThornsDamage(Entity p_92087_0_) {
		return new EntityDamageSource("thorns", p_92087_0_).setMagicDamage();
	}

	public static DamageSource causeThrownDamage(Entity p_76356_0_,
			Entity p_76356_1_) {
		return new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_)
				.setProjectile();
	}

	public static DamageSource setExplosionSource(Explosion p_94539_0_) {
		return p_94539_0_ != null && p_94539_0_.getExplosivePlacedBy() != null ? new EntityDamageSource(
				"explosion.player", p_94539_0_.getExplosivePlacedBy())
				.setDifficultyScaled().setExplosion() : new DamageSource(
				"explosion").setDifficultyScaled().setExplosion();
	}

	/**
	 * Whether or not the damage ignores modification by potion effects or
	 * enchantments.
	 */
	private boolean damageIsAbsolute;
	public String damageType;

	/**
	 * Whether this damage source will have its damage amount scaled based on
	 * the current difficulty.
	 */
	private boolean difficultyScaled;

	private boolean explosion;

	/** This kind of damage is based on fire or not. */
	private boolean fireDamage;

	private float hungerDamage = 0.3F;

	private boolean isDamageAllowedInCreativeMode;

	/** This kind of damage can be blocked or not. */
	private boolean isUnblockable;

	private boolean magicDamage;

	/** This kind of damage is based on a projectile or not. */
	private boolean projectile;

	protected DamageSource(String p_i1566_1_) {
		damageType = p_i1566_1_;
	}

	public boolean canHarmInCreative() {
		return isDamageAllowedInCreativeMode;
	}

	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		final EntityLivingBase var2 = p_151519_1_.func_94060_bK();
		final String var3 = "death.attack." + damageType;
		final String var4 = var3 + ".player";
		return var2 != null && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(
				var4, new Object[] { p_151519_1_.func_145748_c_(),
						var2.func_145748_c_() })
				: new ChatComponentTranslation(var3,
						new Object[] { p_151519_1_.func_145748_c_() });
	}

	/**
	 * Return the name of damage type.
	 */
	public String getDamageType() {
		return damageType;
	}

	public Entity getEntity() {
		return null;
	}

	/**
	 * How much satiate(food) is consumed by this DamageSource
	 */
	public float getHungerDamage() {
		return hungerDamage;
	}

	public Entity getSourceOfDamage() {
		return getEntity();
	}

	/**
	 * Whether or not the damage ignores modification by potion effects or
	 * enchantments.
	 */
	public boolean isDamageAbsolute() {
		return damageIsAbsolute;
	}

	/**
	 * Return whether this damage source will have its damage amount scaled
	 * based on the current difficulty.
	 */
	public boolean isDifficultyScaled() {
		return difficultyScaled;
	}

	public boolean isExplosion() {
		return explosion;
	}

	/**
	 * Returns true if the damage is fire based.
	 */
	public boolean isFireDamage() {
		return fireDamage;
	}

	/**
	 * Returns true if the damage is magic based.
	 */
	public boolean isMagicDamage() {
		return magicDamage;
	}

	/**
	 * Returns true if the damage is projectile based.
	 */
	public boolean isProjectile() {
		return projectile;
	}

	public boolean isUnblockable() {
		return isUnblockable;
	}

	protected DamageSource setDamageAllowedInCreativeMode() {
		isDamageAllowedInCreativeMode = true;
		return this;
	}

	protected DamageSource setDamageBypassesArmor() {
		isUnblockable = true;
		hungerDamage = 0.0F;
		return this;
	}

	/**
	 * Sets a value indicating whether the damage is absolute (ignores
	 * modification by potion effects or enchantments), and also clears out
	 * hunger damage.
	 */
	protected DamageSource setDamageIsAbsolute() {
		damageIsAbsolute = true;
		hungerDamage = 0.0F;
		return this;
	}

	/**
	 * Set whether this damage source will have its damage amount scaled based
	 * on the current difficulty.
	 */
	public DamageSource setDifficultyScaled() {
		difficultyScaled = true;
		return this;
	}

	public DamageSource setExplosion() {
		explosion = true;
		return this;
	}

	/**
	 * Define the damage type as fire based.
	 */
	protected DamageSource setFireDamage() {
		fireDamage = true;
		return this;
	}

	/**
	 * Define the damage type as magic based.
	 */
	public DamageSource setMagicDamage() {
		magicDamage = true;
		return this;
	}

	/**
	 * Define the damage type as projectile based.
	 */
	public DamageSource setProjectile() {
		projectile = true;
		return this;
	}
}
