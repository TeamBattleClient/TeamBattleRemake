package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCow extends EntityAnimal {

	public EntityCow(World p_i1683_1_) {
		super(p_i1683_1_);
		setSize(0.9F, 1.3F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 2.0D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.wheat, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.20000000298023224D);
	}

	@Override
	public EntityCow createChild(EntityAgeable p_90011_1_) {
		return new EntityCow(worldObj);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int var3 = rand.nextInt(3) + rand.nextInt(1 + p_70628_2_);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.leather, 1);
		}

		var3 = rand.nextInt(3) + 1 + rand.nextInt(1 + p_70628_2_);

		for (var4 = 0; var4 < var3; ++var4) {
			if (isBurning()) {
				func_145779_a(Items.cooked_beef, 1);
			} else {
				func_145779_a(Items.beef, 1);
			}
		}
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.cow.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Items.leather;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.cow.hurt";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.cow.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.cow.say";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && var2.getItem() == Items.bucket
				&& !p_70085_1_.capabilities.isCreativeMode) {
			if (var2.stackSize-- == 1) {
				p_70085_1_.inventory.setInventorySlotContents(
						p_70085_1_.inventory.currentItem, new ItemStack(
								Items.milk_bucket));
			} else if (!p_70085_1_.inventory
					.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
				p_70085_1_.dropPlayerItemWithRandomChoice(new ItemStack(
						Items.milk_bucket, 1, 0), false);
			}

			return true;
		} else
			return super.interact(p_70085_1_);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}
}
