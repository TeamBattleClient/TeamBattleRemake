package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityChicken extends EntityAnimal {
	public float destPos;
	public boolean field_152118_bv;
	public float field_70884_g;
	public float field_70886_e;
	public float field_70888_h;

	public float field_70889_i = 1.0F;
	/** The time until the next egg is spawned. */
	public int timeUntilNextEgg;

	public EntityChicken(World p_i1682_1_) {
		super(p_i1682_1_);
		setSize(0.3F, 0.7F);
		timeUntilNextEgg = rand.nextInt(6000) + 6000;
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.4D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3,
				new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.25D);
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return func_152116_bZ() && riddenByEntity == null;
	}

	@Override
	public EntityChicken createChild(EntityAgeable p_90011_1_) {
		return new EntityChicken(worldObj);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final int var3 = rand.nextInt(3) + rand.nextInt(1 + p_70628_2_);

		for (int var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.feather, 1);
		}

		if (isBurning()) {
			func_145779_a(Items.cooked_chicken, 1);
		} else {
			func_145779_a(Items.chicken, 1);
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.chicken.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Items.feather;
	}

	public boolean func_152116_bZ() {
		return field_152118_bv;
	}

	public void func_152117_i(boolean p_152117_1_) {
		field_152118_bv = p_152117_1_;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.chicken.hurt";
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return func_152116_bZ() ? 10 : super.getExperiencePoints(p_70693_1_);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.chicken.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.chicken.say";
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_ != null && p_70877_1_.getItem() instanceof ItemSeeds;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		field_70888_h = field_70886_e;
		field_70884_g = destPos;
		destPos = (float) (destPos + (onGround ? -1 : 4) * 0.3D);

		if (destPos < 0.0F) {
			destPos = 0.0F;
		}

		if (destPos > 1.0F) {
			destPos = 1.0F;
		}

		if (!onGround && field_70889_i < 1.0F) {
			field_70889_i = 1.0F;
		}

		field_70889_i = (float) (field_70889_i * 0.9D);

		if (!onGround && motionY < 0.0D) {
			motionY *= 0.6D;
		}

		field_70886_e += field_70889_i * 2.0F;

		if (!worldObj.isClient && !isChild() && !func_152116_bZ()
				&& --timeUntilNextEgg <= 0) {
			playSound("mob.chicken.plop", 1.0F,
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			func_145779_a(Items.egg, 1);
			timeUntilNextEgg = rand.nextInt(6000) + 6000;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		field_152118_bv = p_70037_1_.getBoolean("IsChickenJockey");
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();
		final float var1 = MathHelper.sin(renderYawOffset * (float) Math.PI
				/ 180.0F);
		final float var2 = MathHelper.cos(renderYawOffset * (float) Math.PI
				/ 180.0F);
		final float var3 = 0.1F;
		final float var4 = 0.0F;
		riddenByEntity.setPosition(posX + var3 * var1, posY + height * 0.5F
				+ riddenByEntity.getYOffset() + var4, posZ - var3 * var2);

		if (riddenByEntity instanceof EntityLivingBase) {
			((EntityLivingBase) riddenByEntity).renderYawOffset = renderYawOffset;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("IsChickenJockey", field_152118_bv);
	}
}
