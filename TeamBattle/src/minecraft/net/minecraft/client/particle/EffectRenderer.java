package net.minecraft.client.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class EffectRenderer {
	private static final ResourceLocation particleTextures = new ResourceLocation(
			"textures/particle/particles.png");

	private final List[] fxLayers = new List[4];
	/** RNG. */
	private final Random rand = new Random();
	private final TextureManager renderer;

	/** Reference to the World object. */
	protected World worldObj;

	public EffectRenderer(World p_i1220_1_, TextureManager p_i1220_2_) {
		if (p_i1220_1_ != null) {
			worldObj = p_i1220_1_;
		}

		renderer = p_i1220_2_;

		for (int var3 = 0; var3 < 4; ++var3) {
			fxLayers[var3] = new ArrayList();
		}
	}

	/**
	 * Adds block hit particles for the specified block. Args: x, y, z, sideHit
	 */
	public void addBlockHitEffects(int p_78867_1_, int p_78867_2_,
			int p_78867_3_, int p_78867_4_) {
		final Block var5 = worldObj
				.getBlock(p_78867_1_, p_78867_2_, p_78867_3_);

		if (var5.getMaterial() != Material.air) {
			final float var6 = 0.1F;
			double var7 = p_78867_1_
					+ rand.nextDouble()
					* (var5.getBlockBoundsMaxX() - var5.getBlockBoundsMinX() - var6 * 2.0F)
					+ var6 + var5.getBlockBoundsMinX();
			double var9 = p_78867_2_
					+ rand.nextDouble()
					* (var5.getBlockBoundsMaxY() - var5.getBlockBoundsMinY() - var6 * 2.0F)
					+ var6 + var5.getBlockBoundsMinY();
			double var11 = p_78867_3_
					+ rand.nextDouble()
					* (var5.getBlockBoundsMaxZ() - var5.getBlockBoundsMinZ() - var6 * 2.0F)
					+ var6 + var5.getBlockBoundsMinZ();

			if (p_78867_4_ == 0) {
				var9 = p_78867_2_ + var5.getBlockBoundsMinY() - var6;
			}

			if (p_78867_4_ == 1) {
				var9 = p_78867_2_ + var5.getBlockBoundsMaxY() + var6;
			}

			if (p_78867_4_ == 2) {
				var11 = p_78867_3_ + var5.getBlockBoundsMinZ() - var6;
			}

			if (p_78867_4_ == 3) {
				var11 = p_78867_3_ + var5.getBlockBoundsMaxZ() + var6;
			}

			if (p_78867_4_ == 4) {
				var7 = p_78867_1_ + var5.getBlockBoundsMinX() - var6;
			}

			if (p_78867_4_ == 5) {
				var7 = p_78867_1_ + var5.getBlockBoundsMaxX() + var6;
			}

			addEffect(new EntityDiggingFX(worldObj, var7, var9, var11, 0.0D,
					0.0D, 0.0D, var5, worldObj.getBlockMetadata(p_78867_1_,
							p_78867_2_, p_78867_3_))
					.applyColourMultiplier(p_78867_1_, p_78867_2_, p_78867_3_)
					.multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public void addEffect(EntityFX p_78873_1_) {
		final int var2 = p_78873_1_.getFXLayer();

		if (fxLayers[var2].size() >= 4000) {
			fxLayers[var2].remove(0);
		}

		fxLayers[var2].add(p_78873_1_);
	}

	public void clearEffects(World p_78870_1_) {
		worldObj = p_78870_1_;

		for (int var2 = 0; var2 < 4; ++var2) {
			fxLayers[var2].clear();
		}
	}

	public void func_147215_a(int p_147215_1_, int p_147215_2_,
			int p_147215_3_, Block p_147215_4_, int p_147215_5_) {
		if (p_147215_4_.getMaterial() != Material.air) {
			final byte var6 = 4;

			for (int var7 = 0; var7 < var6; ++var7) {
				for (int var8 = 0; var8 < var6; ++var8) {
					for (int var9 = 0; var9 < var6; ++var9) {
						final double var10 = p_147215_1_ + (var7 + 0.5D) / var6;
						final double var12 = p_147215_2_ + (var8 + 0.5D) / var6;
						final double var14 = p_147215_3_ + (var9 + 0.5D) / var6;
						addEffect(new EntityDiggingFX(worldObj, var10, var12,
								var14, var10 - p_147215_1_ - 0.5D, var12
										- p_147215_2_ - 0.5D, var14
										- p_147215_3_ - 0.5D, p_147215_4_,
								p_147215_5_).applyColourMultiplier(p_147215_1_,
								p_147215_2_, p_147215_3_));
					}
				}
			}
		}
	}

	public String getStatistics() {
		return ""
				+ (fxLayers[0].size() + fxLayers[1].size() + fxLayers[2].size());
	}

	public void renderLitParticles(Entity p_78872_1_, float p_78872_2_) {
		final float var4 = MathHelper
				.cos(p_78872_1_.rotationYaw * 0.017453292F);
		final float var5 = MathHelper
				.sin(p_78872_1_.rotationYaw * 0.017453292F);
		final float var6 = -var5
				* MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
		final float var7 = var4
				* MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
		final float var8 = MathHelper
				.cos(p_78872_1_.rotationPitch * 0.017453292F);
		final byte var9 = 3;
		final List var10 = fxLayers[var9];

		if (!var10.isEmpty()) {
			final Tessellator var11 = Tessellator.instance;

			for (int var12 = 0; var12 < var10.size(); ++var12) {
				final EntityFX var13 = (EntityFX) var10.get(var12);
				var11.setBrightness(var13.getBrightnessForRender(p_78872_2_));
				var13.renderParticle(var11, p_78872_2_, var4, var8, var5, var6,
						var7);
			}
		}
	}

	/**
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles(Entity p_78874_1_, float p_78874_2_) {
		final float var3 = ActiveRenderInfo.rotationX;
		final float var4 = ActiveRenderInfo.rotationZ;
		final float var5 = ActiveRenderInfo.rotationYZ;
		final float var6 = ActiveRenderInfo.rotationXY;
		final float var7 = ActiveRenderInfo.rotationXZ;
		EntityFX.interpPosX = p_78874_1_.lastTickPosX
				+ (p_78874_1_.posX - p_78874_1_.lastTickPosX) * p_78874_2_;
		EntityFX.interpPosY = p_78874_1_.lastTickPosY
				+ (p_78874_1_.posY - p_78874_1_.lastTickPosY) * p_78874_2_;
		EntityFX.interpPosZ = p_78874_1_.lastTickPosZ
				+ (p_78874_1_.posZ - p_78874_1_.lastTickPosZ) * p_78874_2_;

		for (int var88 = 0; var88 < 3; ++var88) {
			final int var8 = var88;

			if (!fxLayers[var8].isEmpty()) {
				switch (var8) {
				case 0:
				default:
					renderer.bindTexture(particleTextures);
					break;

				case 1:
					renderer.bindTexture(TextureMap.locationBlocksTexture);
					break;

				case 2:
					renderer.bindTexture(TextureMap.locationItemsTexture);
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDepthMask(false);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
				final Tessellator var9 = Tessellator.instance;
				var9.startDrawingQuads();

				for (int var10 = 0; var10 < fxLayers[var8].size(); ++var10) {
					final EntityFX var11 = (EntityFX) fxLayers[var8].get(var10);
					var9.setBrightness(var11.getBrightnessForRender(p_78874_2_));

					try {
						var11.renderParticle(var9, p_78874_2_, var3, var7,
								var4, var5, var6);
					} catch (final Throwable var16) {
						final CrashReport var13 = CrashReport.makeCrashReport(
								var16, "Rendering Particle");
						final CrashReportCategory var14 = var13
								.makeCategory("Particle being rendered");
						var14.addCrashSectionCallable("Particle",
								new Callable() {

									@Override
									public String call() {
										return var11.toString();
									}
								});
						var14.addCrashSectionCallable("Particle Type",
								new Callable() {

									@Override
									public String call() {
										return var8 == 0 ? "MISC_TEXTURE"
												: var8 == 1 ? "TERRAIN_TEXTURE"
														: var8 == 2 ? "ITEM_TEXTURE"
																: var8 == 3 ? "ENTITY_PARTICLE_TEXTURE"
																		: "Unknown - "
																				+ var8;
									}
								});
						throw new ReportedException(var13);
					}
				}

				var9.draw();
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			}
		}
	}

	public void updateEffects() {
		for (int var11 = 0; var11 < 4; ++var11) {
			final int var1 = var11;

			for (int var2 = 0; var2 < fxLayers[var1].size(); ++var2) {
				final EntityFX var3 = (EntityFX) fxLayers[var1].get(var2);

				try {
					var3.onUpdate();
				} catch (final Throwable var8) {
					final CrashReport var5 = CrashReport.makeCrashReport(var8,
							"Ticking Particle");
					final CrashReportCategory var6 = var5
							.makeCategory("Particle being ticked");
					var6.addCrashSectionCallable("Particle", new Callable() {

						@Override
						public String call() {
							return var3.toString();
						}
					});
					var6.addCrashSectionCallable("Particle Type",
							new Callable() {

								@Override
								public String call() {
									return var1 == 0 ? "MISC_TEXTURE"
											: var1 == 1 ? "TERRAIN_TEXTURE"
													: var1 == 2 ? "ITEM_TEXTURE"
															: var1 == 3 ? "ENTITY_PARTICLE_TEXTURE"
																	: "Unknown - "
																			+ var1;
								}
							});
					throw new ReportedException(var5);
				}

				if (var3.isDead) {
					fxLayers[var1].remove(var2--);
				}
			}
		}
	}
}
