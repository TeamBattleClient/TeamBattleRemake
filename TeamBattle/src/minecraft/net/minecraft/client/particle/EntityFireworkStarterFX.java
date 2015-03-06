package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkStarterFX extends EntityFX {
	private int fireworkAge;
	private NBTTagList fireworkExplosions;
	private final EffectRenderer theEffectRenderer;
	boolean twinkle;

	public EntityFireworkStarterFX(World p_i1208_1_, double p_i1208_2_,
			double p_i1208_4_, double p_i1208_6_, double p_i1208_8_,
			double p_i1208_10_, double p_i1208_12_, EffectRenderer p_i1208_14_,
			NBTTagCompound p_i1208_15_) {
		super(p_i1208_1_, p_i1208_2_, p_i1208_4_, p_i1208_6_, 0.0D, 0.0D, 0.0D);
		motionX = p_i1208_8_;
		motionY = p_i1208_10_;
		motionZ = p_i1208_12_;
		theEffectRenderer = p_i1208_14_;
		particleMaxAge = 8;

		if (p_i1208_15_ != null) {
			fireworkExplosions = p_i1208_15_.getTagList("Explosions", 10);

			if (fireworkExplosions.tagCount() == 0) {
				fireworkExplosions = null;
			} else {
				particleMaxAge = fireworkExplosions.tagCount() * 2 - 1;

				for (int var16 = 0; var16 < fireworkExplosions.tagCount(); ++var16) {
					final NBTTagCompound var17 = fireworkExplosions
							.getCompoundTagAt(var16);

					if (var17.getBoolean("Flicker")) {
						twinkle = true;
						particleMaxAge += 15;
						break;
					}
				}
			}
		}
	}

	/**
	 * Creates a small ball or large ball type explosion. Args: particle speed,
	 * size, colours, fade colours, whether to trail, whether to flicker
	 */
	private void createBall(double p_92035_1_, int p_92035_3_,
			int[] p_92035_4_, int[] p_92035_5_, boolean p_92035_6_,
			boolean p_92035_7_) {
		final double var8 = posX;
		final double var10 = posY;
		final double var12 = posZ;

		for (int var14 = -p_92035_3_; var14 <= p_92035_3_; ++var14) {
			for (int var15 = -p_92035_3_; var15 <= p_92035_3_; ++var15) {
				for (int var16 = -p_92035_3_; var16 <= p_92035_3_; ++var16) {
					final double var17 = var15
							+ (rand.nextDouble() - rand.nextDouble()) * 0.5D;
					final double var19 = var14
							+ (rand.nextDouble() - rand.nextDouble()) * 0.5D;
					final double var21 = var16
							+ (rand.nextDouble() - rand.nextDouble()) * 0.5D;
					final double var23 = MathHelper.sqrt_double(var17 * var17
							+ var19 * var19 + var21 * var21)
							/ p_92035_1_ + rand.nextGaussian() * 0.05D;
					createParticle(var8, var10, var12, var17 / var23, var19
							/ var23, var21 / var23, p_92035_4_, p_92035_5_,
							p_92035_6_, p_92035_7_);

					if (var14 != -p_92035_3_ && var14 != p_92035_3_
							&& var15 != -p_92035_3_ && var15 != p_92035_3_) {
						var16 += p_92035_3_ * 2 - 1;
					}
				}
			}
		}
	}

	/**
	 * Creates a burst type explosion. Args: colours, fade colours, whether to
	 * trail, whether to flicker
	 */
	private void createBurst(int[] p_92036_1_, int[] p_92036_2_,
			boolean p_92036_3_, boolean p_92036_4_) {
		final double var5 = rand.nextGaussian() * 0.05D;
		final double var7 = rand.nextGaussian() * 0.05D;

		for (int var9 = 0; var9 < 70; ++var9) {
			final double var10 = motionX * 0.5D + rand.nextGaussian() * 0.15D
					+ var5;
			final double var12 = motionZ * 0.5D + rand.nextGaussian() * 0.15D
					+ var7;
			final double var14 = motionY * 0.5D + rand.nextDouble() * 0.5D;
			createParticle(posX, posY, posZ, var10, var14, var12, p_92036_1_,
					p_92036_2_, p_92036_3_, p_92036_4_);
		}
	}

	/**
	 * Creates a single particle. Args: x, y, z, x velocity, y velocity, z
	 * velocity, colours, fade colours, whether to trail, whether to twinkle
	 */
	private void createParticle(double p_92034_1_, double p_92034_3_,
			double p_92034_5_, double p_92034_7_, double p_92034_9_,
			double p_92034_11_, int[] p_92034_13_, int[] p_92034_14_,
			boolean p_92034_15_, boolean p_92034_16_) {
		final EntityFireworkSparkFX var17 = new EntityFireworkSparkFX(worldObj,
				p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_,
				p_92034_11_, theEffectRenderer);
		var17.setTrail(p_92034_15_);
		var17.setTwinkle(p_92034_16_);
		final int var18 = rand.nextInt(p_92034_13_.length);
		var17.setColour(p_92034_13_[var18]);

		if (p_92034_14_ != null && p_92034_14_.length > 0) {
			var17.setFadeColour(p_92034_14_[rand.nextInt(p_92034_14_.length)]);
		}

		theEffectRenderer.addEffect(var17);
	}

	/**
	 * Creates a creeper-shaped or star-shaped explosion. Args: particle speed,
	 * shape, colours, fade colours, whether to trail, whether to flicker,
	 * unknown
	 */
	private void createShaped(double p_92038_1_, double[][] p_92038_3_,
			int[] p_92038_4_, int[] p_92038_5_, boolean p_92038_6_,
			boolean p_92038_7_, boolean p_92038_8_) {
		final double var9 = p_92038_3_[0][0];
		final double var11 = p_92038_3_[0][1];
		createParticle(posX, posY, posZ, var9 * p_92038_1_, var11 * p_92038_1_,
				0.0D, p_92038_4_, p_92038_5_, p_92038_6_, p_92038_7_);
		final float var13 = rand.nextFloat() * (float) Math.PI;
		final double var14 = p_92038_8_ ? 0.034D : 0.34D;

		for (int var16 = 0; var16 < 3; ++var16) {
			final double var17 = var13 + var16 * (float) Math.PI * var14;
			double var19 = var9;
			double var21 = var11;

			for (int var23 = 1; var23 < p_92038_3_.length; ++var23) {
				final double var24 = p_92038_3_[var23][0];
				final double var26 = p_92038_3_[var23][1];

				for (double var28 = 0.25D; var28 <= 1.0D; var28 += 0.25D) {
					double var30 = (var19 + (var24 - var19) * var28)
							* p_92038_1_;
					final double var32 = (var21 + (var26 - var21) * var28)
							* p_92038_1_;
					final double var34 = var30 * Math.sin(var17);
					var30 *= Math.cos(var17);

					for (double var36 = -1.0D; var36 <= 1.0D; var36 += 2.0D) {
						createParticle(posX, posY, posZ, var30 * var36, var32,
								var34 * var36, p_92038_4_, p_92038_5_,
								p_92038_6_, p_92038_7_);
					}
				}

				var19 = var24;
				var21 = var26;
			}
		}
	}

	private boolean func_92037_i() {
		final Minecraft var1 = Minecraft.getMinecraft();
		return var1 == null
				|| var1.renderViewEntity == null
				|| var1.renderViewEntity.getDistanceSq(posX, posY, posZ) >= 256.0D;
	}

	@Override
	public int getFXLayer() {
		return 0;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		boolean var1;

		if (fireworkAge == 0 && fireworkExplosions != null) {
			var1 = func_92037_i();
			boolean var2 = false;

			if (fireworkExplosions.tagCount() >= 3) {
				var2 = true;
			} else {
				for (int var3 = 0; var3 < fireworkExplosions.tagCount(); ++var3) {
					final NBTTagCompound var4 = fireworkExplosions
							.getCompoundTagAt(var3);

					if (var4.getByte("Type") == 1) {
						var2 = true;
						break;
					}
				}
			}

			final String var16 = "fireworks." + (var2 ? "largeBlast" : "blast")
					+ (var1 ? "_far" : "");
			worldObj.playSound(posX, posY, posZ, var16, 20.0F,
					0.95F + rand.nextFloat() * 0.1F, true);
		}

		if (fireworkAge % 2 == 0 && fireworkExplosions != null
				&& fireworkAge / 2 < fireworkExplosions.tagCount()) {
			final int var13 = fireworkAge / 2;
			final NBTTagCompound var14 = fireworkExplosions
					.getCompoundTagAt(var13);
			final byte var17 = var14.getByte("Type");
			final boolean var18 = var14.getBoolean("Trail");
			final boolean var5 = var14.getBoolean("Flicker");
			final int[] var6 = var14.getIntArray("Colors");
			final int[] var7 = var14.getIntArray("FadeColors");

			if (var17 == 1) {
				createBall(0.5D, 4, var6, var7, var18, var5);
			} else if (var17 == 2) {
				createShaped(0.5D, new double[][] { { 0.0D, 1.0D },
						{ 0.3455D, 0.309D }, { 0.9511D, 0.309D },
						{ 0.3795918367346939D, -0.12653061224489795D },
						{ 0.6122448979591837D, -0.8040816326530612D },
						{ 0.0D, -0.35918367346938773D } }, var6, var7, var18,
						var5, false);
			} else if (var17 == 3) {
				createShaped(0.5D, new double[][] { { 0.0D, 0.2D },
						{ 0.2D, 0.2D }, { 0.2D, 0.6D }, { 0.6D, 0.6D },
						{ 0.6D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.0D },
						{ 0.4D, 0.0D }, { 0.4D, -0.6D }, { 0.2D, -0.6D },
						{ 0.2D, -0.4D }, { 0.0D, -0.4D } }, var6, var7, var18,
						var5, true);
			} else if (var17 == 4) {
				createBurst(var6, var7, var18, var5);
			} else {
				createBall(0.25D, 2, var6, var7, var18, var5);
			}

			final int var8 = var6[0];
			final float var9 = ((var8 & 16711680) >> 16) / 255.0F;
			final float var10 = ((var8 & 65280) >> 8) / 255.0F;
			final float var11 = ((var8 & 255) >> 0) / 255.0F;
			final EntityFireworkOverlayFX var12 = new EntityFireworkOverlayFX(
					worldObj, posX, posY, posZ);
			var12.setRBGColorF(var9, var10, var11);
			theEffectRenderer.addEffect(var12);
		}

		++fireworkAge;

		if (fireworkAge > particleMaxAge) {
			if (twinkle) {
				var1 = func_92037_i();
				final String var15 = "fireworks."
						+ (var1 ? "twinkle_far" : "twinkle");
				worldObj.playSound(posX, posY, posZ, var15, 20.0F,
						0.9F + rand.nextFloat() * 0.15F, true);
			}

			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
	}
}
