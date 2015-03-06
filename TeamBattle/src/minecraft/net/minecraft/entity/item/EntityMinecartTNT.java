package net.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart {
	private int minecartTNTFuse = -1;

	public EntityMinecartTNT(World p_i1727_1_) {
		super(p_i1727_1_);
	}

	public EntityMinecartTNT(World p_i1728_1_, double p_i1728_2_,
			double p_i1728_4_, double p_i1728_6_) {
		super(p_i1728_1_, p_i1728_2_, p_i1728_4_, p_i1728_6_);
	}

	/**
	 * Makes the minecart explode.
	 */
	protected void explodeCart(double p_94103_1_) {
		if (!worldObj.isClient) {
			double var3 = Math.sqrt(p_94103_1_);

			if (var3 > 5.0D) {
				var3 = 5.0D;
			}

			worldObj.createExplosion(this, posX, posY, posZ,
					(float) (4.0D + rand.nextDouble() * 1.5D * var3), true);
			setDead();
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		if (p_70069_1_ >= 3.0F) {
			final float var2 = p_70069_1_ / 10.0F;
			explodeCart(var2 * var2);
		}

		super.fall(p_70069_1_);
	}

	@Override
	public float func_145772_a(Explosion p_145772_1_, World p_145772_2_,
			int p_145772_3_, int p_145772_4_, int p_145772_5_, Block p_145772_6_) {
		return isIgnited()
				&& (BlockRailBase.func_150051_a(p_145772_6_) || BlockRailBase
						.func_150049_b_(p_145772_2_, p_145772_3_,
								p_145772_4_ + 1, p_145772_5_)) ? 0.0F : super
				.func_145772_a(p_145772_1_, p_145772_2_, p_145772_3_,
						p_145772_4_, p_145772_5_, p_145772_6_);
	}

	@Override
	public boolean func_145774_a(Explosion p_145774_1_, World p_145774_2_,
			int p_145774_3_, int p_145774_4_, int p_145774_5_,
			Block p_145774_6_, float p_145774_7_) {
		return isIgnited()
				&& (BlockRailBase.func_150051_a(p_145774_6_) || BlockRailBase
						.func_150049_b_(p_145774_2_, p_145774_3_,
								p_145774_4_ + 1, p_145774_5_)) ? false : super
				.func_145774_a(p_145774_1_, p_145774_2_, p_145774_3_,
						p_145774_4_, p_145774_5_, p_145774_6_, p_145774_7_);
	}

	@Override
	public Block func_145817_o() {
		return Blocks.tnt;
	}

	public int func_94104_d() {
		return minecartTNTFuse;
	}

	@Override
	public int getMinecartType() {
		return 3;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 10) {
			ignite();
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Ignites this TNT cart.
	 */
	public void ignite() {
		minecartTNTFuse = 80;

		if (!worldObj.isClient) {
			worldObj.setEntityState(this, (byte) 10);
			worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
		}
	}

	/**
	 * Returns true if the TNT minecart is ignited.
	 */
	public boolean isIgnited() {
		return minecartTNTFuse > -1;
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);
		final double var2 = motionX * motionX + motionZ * motionZ;

		if (!p_94095_1_.isExplosion()) {
			entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0F);
		}

		if (p_94095_1_.isFireDamage() || p_94095_1_.isExplosion()
				|| var2 >= 0.009999999776482582D) {
			explodeCart(var2);
		}
	}

	/**
	 * Called every tick the minecart is on an activator rail. Args: x, y, z, is
	 * the rail receiving power
	 */
	@Override
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_,
			int p_96095_3_, boolean p_96095_4_) {
		if (p_96095_4_ && minecartTNTFuse < 0) {
			ignite();
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (minecartTNTFuse > 0) {
			--minecartTNTFuse;
			worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D,
					0.0D, 0.0D);
		} else if (minecartTNTFuse == 0) {
			explodeCart(motionX * motionX + motionZ * motionZ);
		}

		if (isCollidedHorizontally) {
			final double var1 = motionX * motionX + motionZ * motionZ;

			if (var1 >= 0.009999999776482582D) {
				explodeCart(var1);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.func_150297_b("TNTFuse", 99)) {
			minecartTNTFuse = p_70037_1_.getInteger("TNTFuse");
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("TNTFuse", minecartTNTFuse);
	}
}
