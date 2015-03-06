package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime {

	public EntityMagmaCube(World p_i1737_1_) {
		super(p_i1737_1_);
		isImmuneToFire = true;
	}

	@Override
	protected void alterSquishAmount() {
		squishAmount *= 0.9F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.20000000298023224D);
	}

	/**
	 * Indicates weather the slime is able to damage the player (based upon the
	 * slime's size)
	 */
	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

	@Override
	protected EntitySlime createInstance() {
		return new EntityMagmaCube(worldObj);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final Item var3 = func_146068_u();

		if (var3 != null && getSlimeSize() > 1) {
			int var4 = rand.nextInt(4) - 2;

			if (p_70628_2_ > 0) {
				var4 += rand.nextInt(p_70628_2_ + 1);
			}

			for (int var5 = 0; var5 < var4; ++var5) {
				func_145779_a(var3, 1);
			}
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected Item func_146068_u() {
		return Items.magma_cream;
	}

	/**
	 * Gets the amount of damage dealt to the player when "attacked" by the
	 * slime.
	 */
	@Override
	protected int getAttackStrength() {
		return super.getAttackStrength() + 2;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL
				&& worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox)
						.isEmpty() && !worldObj.isAnyLiquid(boundingBox);
	}

	/**
	 * Gets the amount of time the slime needs to wait between jumps.
	 */
	@Override
	protected int getJumpDelay() {
		return super.getJumpDelay() * 4;
	}

	/**
	 * Returns the name of the sound played when the slime jumps.
	 */
	@Override
	protected String getJumpSound() {
		return getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
	}

	/**
	 * Returns the name of a particle effect that may be randomly created by
	 * EntitySlime.onUpdate()
	 */
	@Override
	protected String getSlimeParticle() {
		return "flame";
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue() {
		return getSlimeSize() * 3;
	}

	/**
	 * Whether or not the current entity is in lava
	 */
	@Override
	public boolean handleLavaMovement() {
		return false;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire
	 * effect on rendering.
	 */
	@Override
	public boolean isBurning() {
		return false;
	}

	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	@Override
	protected void jump() {
		motionY = 0.42F + getSlimeSize() * 0.1F;
		isAirBorne = true;
	}

	/**
	 * Returns true if the slime makes a sound when it lands after a jump (based
	 * upon the slime's size)
	 */
	@Override
	protected boolean makesSoundOnLand() {
		return true;
	}
}
