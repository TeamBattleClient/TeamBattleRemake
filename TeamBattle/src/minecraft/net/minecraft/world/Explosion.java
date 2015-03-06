package net.minecraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Explosion {
	/** A list of ChunkPositions of blocks affected by this explosion */
	public List affectedBlockPositions = new ArrayList();

	public Entity exploder;
	private final Random explosionRNG = new Random();
	public float explosionSize;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	private final Map field_77288_k = new HashMap();
	private final int field_77289_h = 16;
	/** whether or not the explosion sets fire to blocks around it */
	public boolean isFlaming;

	/** whether or not this explosion spawns smoke particles */
	public boolean isSmoking = true;
	private final World worldObj;

	public Explosion(World p_i1948_1_, Entity p_i1948_2_, double p_i1948_3_,
			double p_i1948_5_, double p_i1948_7_, float p_i1948_9_) {
		worldObj = p_i1948_1_;
		exploder = p_i1948_2_;
		explosionSize = p_i1948_9_;
		explosionX = p_i1948_3_;
		explosionY = p_i1948_5_;
		explosionZ = p_i1948_7_;
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA() {
		final float var1 = explosionSize;
		final HashSet var2 = new HashSet();
		int var3;
		int var4;
		int var5;
		double var15;
		double var17;
		double var19;

		for (var3 = 0; var3 < field_77289_h; ++var3) {
			for (var4 = 0; var4 < field_77289_h; ++var4) {
				for (var5 = 0; var5 < field_77289_h; ++var5) {
					if (var3 == 0 || var3 == field_77289_h - 1 || var4 == 0
							|| var4 == field_77289_h - 1 || var5 == 0
							|| var5 == field_77289_h - 1) {
						double var6 = var3 / (field_77289_h - 1.0F) * 2.0F
								- 1.0F;
						double var8 = var4 / (field_77289_h - 1.0F) * 2.0F
								- 1.0F;
						double var10 = var5 / (field_77289_h - 1.0F) * 2.0F
								- 1.0F;
						final double var12 = Math.sqrt(var6 * var6 + var8
								* var8 + var10 * var10);
						var6 /= var12;
						var8 /= var12;
						var10 /= var12;
						float var14 = explosionSize
								* (0.7F + worldObj.rand.nextFloat() * 0.6F);
						var15 = explosionX;
						var17 = explosionY;
						var19 = explosionZ;

						for (final float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
							final int var22 = MathHelper.floor_double(var15);
							final int var23 = MathHelper.floor_double(var17);
							final int var24 = MathHelper.floor_double(var19);
							final Block var25 = worldObj.getBlock(var22, var23,
									var24);

							if (var25.getMaterial() != Material.air) {
								final float var26 = exploder != null ? exploder
										.func_145772_a(this, worldObj, var22,
												var23, var24, var25) : var25
										.getExplosionResistance(exploder);
								var14 -= (var26 + 0.3F) * var21;
							}

							if (var14 > 0.0F
									&& (exploder == null || exploder
											.func_145774_a(this, worldObj,
													var22, var23, var24, var25,
													var14))) {
								var2.add(new ChunkPosition(var22, var23, var24));
							}

							var15 += var6 * var21;
							var17 += var8 * var21;
							var19 += var10 * var21;
						}
					}
				}
			}
		}

		affectedBlockPositions.addAll(var2);
		explosionSize *= 2.0F;
		var3 = MathHelper.floor_double(explosionX - explosionSize - 1.0D);
		var4 = MathHelper.floor_double(explosionX + explosionSize + 1.0D);
		var5 = MathHelper.floor_double(explosionY - explosionSize - 1.0D);
		final int var29 = MathHelper.floor_double(explosionY + explosionSize
				+ 1.0D);
		final int var7 = MathHelper.floor_double(explosionZ - explosionSize
				- 1.0D);
		final int var30 = MathHelper.floor_double(explosionZ + explosionSize
				+ 1.0D);
		final List var9 = worldObj.getEntitiesWithinAABBExcludingEntity(
				exploder, AxisAlignedBB.getBoundingBox(var3, var5, var7, var4,
						var29, var30));
		final Vec3 var31 = Vec3.createVectorHelper(explosionX, explosionY,
				explosionZ);

		for (int var11 = 0; var11 < var9.size(); ++var11) {
			final Entity var32 = (Entity) var9.get(var11);
			final double var13 = var32.getDistance(explosionX, explosionY,
					explosionZ) / explosionSize;

			if (var13 <= 1.0D) {
				var15 = var32.posX - explosionX;
				var17 = var32.posY + var32.getEyeHeight() - explosionY;
				var19 = var32.posZ - explosionZ;
				final double var33 = MathHelper.sqrt_double(var15 * var15
						+ var17 * var17 + var19 * var19);

				if (var33 != 0.0D) {
					var15 /= var33;
					var17 /= var33;
					var19 /= var33;
					final double var34 = worldObj.getBlockDensity(var31,
							var32.boundingBox);
					final double var35 = (1.0D - var13) * var34;
					var32.attackEntityFrom(
							DamageSource.setExplosionSource(this),
							(int) ((var35 * var35 + var35) / 2.0D * 8.0D
									* explosionSize + 1.0D));
					final double var27 = EnchantmentProtection.func_92092_a(
							var32, var35);
					var32.motionX += var15 * var27;
					var32.motionY += var17 * var27;
					var32.motionZ += var19 * var27;

					if (var32 instanceof EntityPlayer) {
						field_77288_k.put(
								var32,
								Vec3.createVectorHelper(var15 * var35, var17
										* var35, var19 * var35));
					}
				}
			}
		}

		explosionSize = var1;
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	public void doExplosionB(boolean p_77279_1_) {
		worldObj.playSoundEffect(
				explosionX,
				explosionY,
				explosionZ,
				"random.explode",
				4.0F,
				(1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (explosionSize >= 2.0F && isSmoking) {
			worldObj.spawnParticle("hugeexplosion", explosionX, explosionY,
					explosionZ, 1.0D, 0.0D, 0.0D);
		} else {
			worldObj.spawnParticle("largeexplode", explosionX, explosionY,
					explosionZ, 1.0D, 0.0D, 0.0D);
		}

		Iterator var2;
		ChunkPosition var3;
		int var4;
		int var5;
		int var6;
		Block var7;

		if (isSmoking) {
			var2 = affectedBlockPositions.iterator();

			while (var2.hasNext()) {
				var3 = (ChunkPosition) var2.next();
				var4 = var3.field_151329_a;
				var5 = var3.field_151327_b;
				var6 = var3.field_151328_c;
				var7 = worldObj.getBlock(var4, var5, var6);

				if (p_77279_1_) {
					final double var8 = var4 + worldObj.rand.nextFloat();
					final double var10 = var5 + worldObj.rand.nextFloat();
					final double var12 = var6 + worldObj.rand.nextFloat();
					double var14 = var8 - explosionX;
					double var16 = var10 - explosionY;
					double var18 = var12 - explosionZ;
					final double var20 = MathHelper.sqrt_double(var14 * var14
							+ var16 * var16 + var18 * var18);
					var14 /= var20;
					var16 /= var20;
					var18 /= var20;
					double var22 = 0.5D / (var20 / explosionSize + 0.1D);
					var22 *= worldObj.rand.nextFloat()
							* worldObj.rand.nextFloat() + 0.3F;
					var14 *= var22;
					var16 *= var22;
					var18 *= var22;
					worldObj.spawnParticle("explode",
							(var8 + explosionX * 1.0D) / 2.0D,
							(var10 + explosionY * 1.0D) / 2.0D,
							(var12 + explosionZ * 1.0D) / 2.0D, var14, var16,
							var18);
					worldObj.spawnParticle("smoke", var8, var10, var12, var14,
							var16, var18);
				}

				if (var7.getMaterial() != Material.air) {
					if (var7.canDropFromExplosion(this)) {
						var7.dropBlockAsItemWithChance(worldObj, var4, var5,
								var6,
								worldObj.getBlockMetadata(var4, var5, var6),
								1.0F / explosionSize, 0);
					}

					worldObj.setBlock(var4, var5, var6, Blocks.air, 0, 3);
					var7.onBlockDestroyedByExplosion(worldObj, var4, var5,
							var6, this);
				}
			}
		}

		if (isFlaming) {
			var2 = affectedBlockPositions.iterator();

			while (var2.hasNext()) {
				var3 = (ChunkPosition) var2.next();
				var4 = var3.field_151329_a;
				var5 = var3.field_151327_b;
				var6 = var3.field_151328_c;
				var7 = worldObj.getBlock(var4, var5, var6);
				final Block var24 = worldObj.getBlock(var4, var5 - 1, var6);

				if (var7.getMaterial() == Material.air && var24.func_149730_j()
						&& explosionRNG.nextInt(3) == 0) {
					worldObj.setBlock(var4, var5, var6, Blocks.fire);
				}
			}
		}
	}

	public Map func_77277_b() {
		return field_77288_k;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity
	 * that caused the explosion or null.
	 */
	public EntityLivingBase getExplosivePlacedBy() {
		return exploder == null ? null
				: exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) exploder)
						.getTntPlacedBy()
						: exploder instanceof EntityLivingBase ? (EntityLivingBase) exploder
								: null;
	}
}
