package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {

	public EntitySnowman(World p_i1692_1_) {
		super(p_i1692_1_);
		setSize(0.4F, 1.8F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
		tasks.addTask(2, new EntityAIWander(this, 1.0D));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(4, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this,
				EntityLiving.class, 0, true, false, IMob.mobSelector));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.20000000298023224D);
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_,
			float p_82196_2_) {
		final EntitySnowball var3 = new EntitySnowball(worldObj, this);
		final double var4 = p_82196_1_.posX - posX;
		final double var6 = p_82196_1_.posY + p_82196_1_.getEyeHeight()
				- 1.100000023841858D - var3.posY;
		final double var8 = p_82196_1_.posZ - posZ;
		final float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
		var3.setThrowableHeading(var4, var6 + var10, var8, 1.6F, 12.0F);
		playSound("random.bow", 1.0F,
				1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
		worldObj.spawnEntityInWorld(var3);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final int var3 = rand.nextInt(16);

		for (int var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.snowball, 1);
		}
	}

	@Override
	protected Item func_146068_u() {
		return Items.snowball;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		int var1 = MathHelper.floor_double(posX);
		int var2 = MathHelper.floor_double(posY);
		int var3 = MathHelper.floor_double(posZ);

		if (isWet()) {
			attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (worldObj.getBiomeGenForCoords(var1, var3).getFloatTemperature(var1,
				var2, var3) > 1.0F) {
			attackEntityFrom(DamageSource.onFire, 1.0F);
		}

		for (int var4 = 0; var4 < 4; ++var4) {
			var1 = MathHelper.floor_double(posX + (var4 % 2 * 2 - 1) * 0.25F);
			var2 = MathHelper.floor_double(posY);
			var3 = MathHelper.floor_double(posZ + (var4 / 2 % 2 * 2 - 1)
					* 0.25F);

			if (worldObj.getBlock(var1, var2, var3).getMaterial() == Material.air
					&& worldObj.getBiomeGenForCoords(var1, var3)
							.getFloatTemperature(var1, var2, var3) < 0.8F
					&& Blocks.snow_layer.canPlaceBlockAt(worldObj, var1, var2,
							var3)) {
				worldObj.setBlock(var1, var2, var3, Blocks.snow_layer);
			}
		}
	}
}
