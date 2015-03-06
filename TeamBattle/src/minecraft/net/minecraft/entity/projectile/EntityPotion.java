package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityPotion extends EntityThrowable {
	/**
	 * The damage value of the thrown potion that this EntityPotion represents.
	 */
	private ItemStack potionDamage;

	public EntityPotion(World p_i1788_1_) {
		super(p_i1788_1_);
	}

	public EntityPotion(World p_i1791_1_, double p_i1791_2_, double p_i1791_4_,
			double p_i1791_6_, int p_i1791_8_) {
		this(p_i1791_1_, p_i1791_2_, p_i1791_4_, p_i1791_6_, new ItemStack(
				Items.potionitem, 1, p_i1791_8_));
	}

	public EntityPotion(World p_i1792_1_, double p_i1792_2_, double p_i1792_4_,
			double p_i1792_6_, ItemStack p_i1792_8_) {
		super(p_i1792_1_, p_i1792_2_, p_i1792_4_, p_i1792_6_);
		potionDamage = p_i1792_8_;
	}

	public EntityPotion(World p_i1789_1_, EntityLivingBase p_i1789_2_,
			int p_i1789_3_) {
		this(p_i1789_1_, p_i1789_2_, new ItemStack(Items.potionitem, 1,
				p_i1789_3_));
	}

	public EntityPotion(World p_i1790_1_, EntityLivingBase p_i1790_2_,
			ItemStack p_i1790_3_) {
		super(p_i1790_1_, p_i1790_2_);
		potionDamage = p_i1790_3_;
	}

	@Override
	protected float func_70182_d() {
		return 0.5F;
	}

	@Override
	protected float func_70183_g() {
		return -20.0F;
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	/**
	 * Returns the damage value of the thrown potion that this EntityPotion
	 * represents.
	 */
	public int getPotionDamage() {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		return potionDamage.getItemDamage();
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		if (!worldObj.isClient) {
			final List var2 = Items.potionitem.getEffects(potionDamage);

			if (var2 != null && !var2.isEmpty()) {
				final AxisAlignedBB var3 = boundingBox.expand(4.0D, 2.0D, 4.0D);
				final List var4 = worldObj.getEntitiesWithinAABB(
						EntityLivingBase.class, var3);

				if (var4 != null && !var4.isEmpty()) {
					final Iterator var5 = var4.iterator();

					while (var5.hasNext()) {
						final EntityLivingBase var6 = (EntityLivingBase) var5
								.next();
						final double var7 = getDistanceSqToEntity(var6);

						if (var7 < 16.0D) {
							double var9 = 1.0D - Math.sqrt(var7) / 4.0D;

							if (var6 == p_70184_1_.entityHit) {
								var9 = 1.0D;
							}

							final Iterator var11 = var2.iterator();

							while (var11.hasNext()) {
								final PotionEffect var12 = (PotionEffect) var11
										.next();
								final int var13 = var12.getPotionID();

								if (Potion.potionTypes[var13].isInstant()) {
									Potion.potionTypes[var13].affectEntity(
											getThrower(), var6,
											var12.getAmplifier(), var9);
								} else {
									final int var14 = (int) (var9
											* var12.getDuration() + 0.5D);

									if (var14 > 20) {
										var6.addPotionEffect(new PotionEffect(
												var13, var14, var12
														.getAmplifier()));
									}
								}
							}
						}
					}
				}
			}

			worldObj.playAuxSFX(2002, (int) Math.round(posX),
					(int) Math.round(posY), (int) Math.round(posZ),
					getPotionDamage());
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.func_150297_b("Potion", 10)) {
			potionDamage = ItemStack.loadItemStackFromNBT(p_70037_1_
					.getCompoundTag("Potion"));
		} else {
			setPotionDamage(p_70037_1_.getInteger("potionValue"));
		}

		if (potionDamage == null) {
			setDead();
		}
	}

	public void setPotionDamage(int p_82340_1_) {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		potionDamage.setItemDamage(p_82340_1_);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (potionDamage != null) {
			p_70014_1_.setTag("Potion",
					potionDamage.writeToNBT(new NBTTagCompound()));
		}
	}
}
