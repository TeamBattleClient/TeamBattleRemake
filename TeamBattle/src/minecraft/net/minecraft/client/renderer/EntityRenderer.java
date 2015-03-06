package net.minecraft.client.renderer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.ItemRendererOF;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.src.TextureUtils;
import net.minecraft.src.WrUpdates;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.google.gson.JsonSyntaxException;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.events.EventRender3D;

public class EntityRenderer implements IResourceManagerReloadListener {
	public static boolean anaglyphEnable;
	/** Anaglyph field (0=R, 1=GB) */
	public static int anaglyphField;
	private static final ResourceLocation locationRainPng = new ResourceLocation(
			"textures/environment/rain.png");
	private static final ResourceLocation locationSnowPng = new ResourceLocation(
			"textures/environment/snow.png");

	private static final Logger logger = LogManager.getLogger();

	private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[] {
			new ResourceLocation("shaders/post/notch.json"),
			new ResourceLocation("shaders/post/fxaa.json"),
			new ResourceLocation("shaders/post/art.json"),
			new ResourceLocation("shaders/post/bumpy.json"),
			new ResourceLocation("shaders/post/blobs2.json"),
			new ResourceLocation("shaders/post/pencil.json"),
			new ResourceLocation("shaders/post/color_convolve.json"),
			new ResourceLocation("shaders/post/deconverge.json"),
			new ResourceLocation("shaders/post/flip.json"),
			new ResourceLocation("shaders/post/invert.json"),
			new ResourceLocation("shaders/post/ntsc.json"),
			new ResourceLocation("shaders/post/outline.json"),
			new ResourceLocation("shaders/post/phosphor.json"),
			new ResourceLocation("shaders/post/scan_pincushion.json"),
			new ResourceLocation("shaders/post/sobel.json"),
			new ResourceLocation("shaders/post/bits.json"),
			new ResourceLocation("shaders/post/desaturate.json"),
			new ResourceLocation("shaders/post/green.json"),
			new ResourceLocation("shaders/post/blur.json"),
			new ResourceLocation("shaders/post/wobble.json"),
			new ResourceLocation("shaders/post/blobs.json"),
			new ResourceLocation("shaders/post/antialias.json") };
	public static final int shaderCount = shaderResourceLocations.length;
	private float bossColorModifier;
	private float bossColorModifierPrev;

	private double cameraPitch;

	private double cameraYaw;
	private final double cameraZoom;
	private float camRoll;

	public long[] chunkTimes = new long[512];

	/** Cloud fog mode */
	private boolean cloudFog;

	private float debugCamFOV;

	private float debugCamPitch;
	private float debugCamYaw;

	/**
	 * Debug view direction (0=OFF, 1=Front, 2=Right, 3=Back, 4=Left,
	 * 5=TiltLeft, 6=TiltRight)
	 */
	public int debugViewDirection;
	private float farPlaneDistance;
	/** Fog color 1 */
	private float fogColor1;
	/** Fog color 2 */
	private float fogColor2;
	/** blue component of the fog color */
	float fogColorBlue;

	/** Fog color buffer */
	FloatBuffer fogColorBuffer;

	/** green component of the fog color */
	float fogColorGreen;

	/** red component of the fog color */
	float fogColorRed;

	public boolean fogStandard = false;

	/** FOV modifier hand */
	private float fovModifierHand;
	/** FOV modifier hand prev */
	private float fovModifierHandPrev;
	/** FOV multiplier temp */
	private float fovMultiplierTemp;
	public long[] frameTimes = new long[512];
	private boolean initialized = false;

	public ItemRenderer itemRenderer;

	private int lastServerTicks = 0;
	private long lastServerTime = 0L;

	private boolean lastShowDebugInfo = false;

	/**
	 * Colors computed in updateLightmap() and loaded into the lightmap
	 * emptyTexture
	 */
	private final int[] lightmapColors;

	/**
	 * The texture id of the blocklight/skylight texture used for lighting
	 * effects
	 */
	private final DynamicTexture lightmapTexture;
	/**
	 * Is set, updateCameraAndRender() calls updateLightmap(); set by
	 * updateTorchFlicker()
	 */
	private boolean lightmapUpdateNeeded;
	private final ResourceLocation locationLightMap;

	/** A reference to the Minecraft object. */
	private final Minecraft mc;
	/** Mouse filter dummy 1 */
	private final MouseFilter mouseFilterDummy1 = new MouseFilter();
	/** Mouse filter dummy 2 */
	private final MouseFilter mouseFilterDummy2 = new MouseFilter();
	/** Mouse filter dummy 3 */
	private final MouseFilter mouseFilterDummy3 = new MouseFilter();
	/** Mouse filter dummy 4 */
	private final MouseFilter mouseFilterDummy4 = new MouseFilter();
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();
	public int numRecordedFrameTimes = 0;
	/** Pointed entity */
	private Entity pointedEntity;

	private float prevCamRoll;

	private float prevDebugCamFOV;

	private float prevDebugCamPitch;

	private float prevDebugCamYaw;

	/** Previous frame time in milliseconds */
	private long prevFrameTime;

	public long prevFrameTimeNano = -1L;

	/** Rain sound counter */
	private int rainSoundCounter;
	/** Rain X coords */
	float[] rainXCoords;

	/** Rain Y coords */
	float[] rainYCoords;

	private final Random random;

	/** End time of last render (ns) */
	private long renderEndNanoTime;

	/** Entity renderer update count */
	private int rendererUpdateCount;

	private final IResourceManager resourceManager;

	public long[] serverTimes = new long[512];

	private int serverWaitTime = 0;

	private int serverWaitTimeCurrent = 0;

	private int shaderIndex;

	private boolean showExtendedDebugInfo = false;

	/** Smooth cam filter X */
	private float smoothCamFilterX;
	/** Smooth cam filter Y */
	private float smoothCamFilterY;
	/** Smooth cam partial ticks */
	private float smoothCamPartialTicks;
	/** Smooth cam pitch */
	private float smoothCamPitch;
	/** Smooth cam yaw */
	private float smoothCamYaw;
	private final MapItemRenderer theMapItemRenderer;
	public ShaderGroup theShaderGroup;
	private final float thirdPersonDistance = 4.0F;
	/** Third person distance temp */
	private float thirdPersonDistanceTemp = 4.0F;
	public long[] tickTimes = new long[512];
	/** Torch flicker DX */
	float torchFlickerDX;
	/** Torch flicker DY */
	float torchFlickerDY;
	/** Torch flicker X */
	float torchFlickerX;
	/** Torch flicker Y */
	float torchFlickerY;
	private World updatedWorld = null;

	public EntityRenderer(Minecraft p_i45076_1_, IResourceManager p_i45076_2_) {
		shaderIndex = shaderCount;
		cameraZoom = 1.0D;
		prevFrameTime = Minecraft.getSystemTime();
		random = new Random();
		fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
		mc = p_i45076_1_;
		resourceManager = p_i45076_2_;
		theMapItemRenderer = new MapItemRenderer(
				p_i45076_1_.getTextureManager());
		itemRenderer = new ItemRenderer(p_i45076_1_);
		lightmapTexture = new DynamicTexture(16, 16);
		locationLightMap = p_i45076_1_.getTextureManager()
				.getDynamicTextureLocation("lightMap", lightmapTexture);
		lightmapColors = lightmapTexture.getTextureData();
		theShaderGroup = null;
	}

	public void activateNextShader() {
		if (OpenGlHelper.shadersSupported) {
			if (theShaderGroup != null) {
				theShaderGroup.deleteShaderGroup();
			}

			shaderIndex = (shaderIndex + 1)
					% (shaderResourceLocations.length + 1);

			if (shaderIndex != shaderCount) {
				try {
					logger.info("Selecting effect "
							+ shaderResourceLocations[shaderIndex]);
					theShaderGroup = new ShaderGroup(mc.getTextureManager(),
							resourceManager, mc.getFramebuffer(),
							shaderResourceLocations[shaderIndex]);
					theShaderGroup.createBindFramebuffers(mc.displayWidth,
							mc.displayHeight);
				} catch (final IOException var2) {
					logger.warn("Failed to load shader: "
							+ shaderResourceLocations[shaderIndex], var2);
					shaderIndex = shaderCount;
				} catch (final JsonSyntaxException var3) {
					logger.warn("Failed to load shader: "
							+ shaderResourceLocations[shaderIndex], var3);
					shaderIndex = shaderCount;
				}
			} else {
				theShaderGroup = null;
				logger.info("No effect selected");
			}
		}
	}

	private void addRainParticles() {
		float var1 = mc.theWorld.getRainStrength(1.0F);

		if (!Config.isRainFancy()) {
			var1 /= 2.0F;
		}

		if (var1 != 0.0F && Config.isRainSplash()) {
			random.setSeed(rendererUpdateCount * 312987231L);
			final EntityLivingBase var2 = mc.renderViewEntity;
			final WorldClient var3 = mc.theWorld;
			final int var4 = MathHelper.floor_double(var2.posX);
			final int var5 = MathHelper.floor_double(var2.posY);
			final int var6 = MathHelper.floor_double(var2.posZ);
			final byte var7 = 10;
			double var8 = 0.0D;
			double var10 = 0.0D;
			double var12 = 0.0D;
			int var14 = 0;
			int var15 = (int) (100.0F * var1 * var1);

			if (mc.gameSettings.particleSetting == 1) {
				var15 >>= 1;
			} else if (mc.gameSettings.particleSetting == 2) {
				var15 = 0;
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				final int var17 = var4 + random.nextInt(var7)
						- random.nextInt(var7);
				final int var18 = var6 + random.nextInt(var7)
						- random.nextInt(var7);
				final int var19 = var3.getPrecipitationHeight(var17, var18);
				final Block var20 = var3.getBlock(var17, var19 - 1, var18);
				final BiomeGenBase var21 = var3.getBiomeGenForCoords(var17,
						var18);

				if (var19 <= var5 + var7
						&& var19 >= var5 - var7
						&& var21.canSpawnLightningBolt()
						&& var21.getFloatTemperature(var17, var19, var18) >= 0.15F) {
					final float var22 = random.nextFloat();
					final float var23 = random.nextFloat();

					if (var20.getMaterial() == Material.lava) {
						mc.effectRenderer.addEffect(new EntitySmokeFX(var3,
								var17 + var22, var19 + 0.1F
										- var20.getBlockBoundsMinY(), var18
										+ var23, 0.0D, 0.0D, 0.0D));
					} else if (var20.getMaterial() != Material.air) {
						++var14;

						if (random.nextInt(var14) == 0) {
							var8 = var17 + var22;
							var10 = var19 + 0.1F - var20.getBlockBoundsMinY();
							var12 = var18 + var23;
						}

						final EntityRainFX fx = new EntityRainFX(var3, var17
								+ var22, var19 + 0.1F
								- var20.getBlockBoundsMinY(), var18 + var23);
						CustomColorizer.updateWaterFX(fx, var3);
						mc.effectRenderer.addEffect(fx);
					}
				}
			}

			if (var14 > 0 && random.nextInt(3) < rainSoundCounter++) {
				rainSoundCounter = 0;

				if (var10 > var2.posY + 1.0D
						&& var3.getPrecipitationHeight(
								MathHelper.floor_double(var2.posX),
								MathHelper.floor_double(var2.posZ)) > MathHelper
								.floor_double(var2.posY)) {
					mc.theWorld.playSound(var8, var10, var12,
							"ambient.weather.rain", 0.1F, 0.5F, false);
				} else {
					mc.theWorld.playSound(var8, var10, var12,
							"ambient.weather.rain", 0.2F, 1.0F, false);
				}
			}
		}
	}

	public void deactivateShader() {
		if (theShaderGroup != null) {
			theShaderGroup.deleteShaderGroup();
		}

		theShaderGroup = null;
		shaderIndex = shaderCount;
	}

	/**
	 * Disable secondary texture unit used by lightmap
	 */
	public void disableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	/**
	 * Enable lightmap in secondary texture unit
	 */
	public void enableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		final float var3 = 0.00390625F;
		GL11.glScalef(var3, var3, var3);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		mc.getTextureManager().bindTexture(locationLightMap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_CLAMP);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void func_152430_c(float p_152430_1_) {
		setupOverlayRendering();
		final ScaledResolution var2 = new ScaledResolution(mc, mc.displayWidth,
				mc.displayHeight);
		final int var3 = var2.getScaledWidth();
		final int var4 = var2.getScaledHeight();
		mc.ingameGUI.func_152126_a(var3, var4);
	}

	/**
	 * Changes the field of view of the player depending on if they are
	 * underwater or not
	 */
	private float getFOVModifier(float par1, boolean par2) {
		if (debugViewDirection > 0)
			return 90.0F;
		else {
			final EntityLivingBase var3 = mc.renderViewEntity;
			float var4 = 70.0F;

			if (par2) {
				var4 = mc.gameSettings.fovSetting;
				var4 *= fovModifierHandPrev
						+ (fovModifierHand - fovModifierHandPrev) * par1;
			}

			boolean zoomActive = false;

			if (mc.currentScreen == null) {
				if (mc.gameSettings.ofKeyBindZoom.getKeyCode() < 0) {
					zoomActive = Mouse
							.isButtonDown(mc.gameSettings.ofKeyBindZoom
									.getKeyCode() + 100);
				} else {
					zoomActive = Keyboard
							.isKeyDown(mc.gameSettings.ofKeyBindZoom
									.getKeyCode());
				}
			}

			if (zoomActive) {
				if (!Config.zoomMode) {
					Config.zoomMode = true;
					mc.gameSettings.smoothCamera = true;
				}

				if (Config.zoomMode) {
					var4 /= 4.0F;
				}
			} else if (Config.zoomMode) {
				Config.zoomMode = false;
				mc.gameSettings.smoothCamera = false;
				mouseFilterXAxis = new MouseFilter();
				mouseFilterYAxis = new MouseFilter();
			}

			if (var3.getHealth() <= 0.0F) {
				final float var6 = var3.deathTime + par1;
				var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
			}

			final Block var61 = ActiveRenderInfo.getBlockAtEntityViewpoint(
					mc.theWorld, var3, par1);

			if (var61.getMaterial() == Material.water) {
				var4 = var4 * 60.0F / 70.0F;
			}

			return var4 + prevDebugCamFOV + (debugCamFOV - prevDebugCamFOV)
					* par1;
		}
	}

	public MapItemRenderer getMapItemRenderer() {
		return theMapItemRenderer;
	}

	/**
	 * Finds what block or object the mouse is over at the specified partial
	 * tick time. Args: partialTickTime
	 */
	public void getMouseOver(float par1) {
		if (mc.renderViewEntity != null && mc.theWorld != null) {
			mc.pointedEntity = null;
			double var2 = mc.playerController.getBlockReachDistance();
			mc.objectMouseOver = mc.renderViewEntity.rayTrace(var2, par1);
			double var4 = var2;
			final Vec3 var6 = mc.renderViewEntity.getPosition(par1);

			if (mc.playerController.extendedReach()) {
				var2 = 6.0D;
				var4 = 6.0D;
			} else {
				if (var2 > 3.0D) {
					var4 = 3.0D;
				}

				var2 = var4;
			}

			if (mc.objectMouseOver != null) {
				var4 = mc.objectMouseOver.hitVec.distanceTo(var6);
			}

			final Vec3 var7 = mc.renderViewEntity.getLook(par1);
			final Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord
					* var2, var7.zCoord * var2);
			pointedEntity = null;
			Vec3 var9 = null;
			final float var10 = 1.0F;
			final List var11 = mc.theWorld
					.getEntitiesWithinAABBExcludingEntity(
							mc.renderViewEntity,
							mc.renderViewEntity.boundingBox.addCoord(
									var7.xCoord * var2, var7.yCoord * var2,
									var7.zCoord * var2).expand(var10, var10,
									var10));
			double var12 = var4;

			for (int var14 = 0; var14 < var11.size(); ++var14) {
				final Entity var15 = (Entity) var11.get(var14);

				if (var15.canBeCollidedWith()) {
					final float var16 = var15.getCollisionBorderSize();
					final AxisAlignedBB var17 = var15.boundingBox.expand(var16,
							var16, var16);
					final MovingObjectPosition var18 = var17
							.calculateIntercept(var6, var8);

					if (var17.isVecInside(var6)) {
						if (0.0D < var12 || var12 == 0.0D) {
							pointedEntity = var15;
							var9 = var18 == null ? var6 : var18.hitVec;
							var12 = 0.0D;
						}
					} else if (var18 != null) {
						final double var19 = var6.distanceTo(var18.hitVec);

						if (var19 < var12 || var12 == 0.0D) {
							boolean canRiderInteract = false;

							if (Reflector.ForgeEntity_canRiderInteract.exists()) {
								canRiderInteract = Reflector.callBoolean(var15,
										Reflector.ForgeEntity_canRiderInteract,
										new Object[0]);
							}

							if (var15 == mc.renderViewEntity.ridingEntity
									&& !canRiderInteract) {
								if (var12 == 0.0D) {
									pointedEntity = var15;
									var9 = var18.hitVec;
								}
							} else {
								pointedEntity = var15;
								var9 = var18.hitVec;
								var12 = var19;
							}
						}
					}
				}
			}

			if (pointedEntity != null
					&& (var12 < var4 || mc.objectMouseOver == null)) {
				mc.objectMouseOver = new MovingObjectPosition(pointedEntity,
						var9);

				if (pointedEntity instanceof EntityLivingBase
						|| pointedEntity instanceof EntityItemFrame) {
					mc.pointedEntity = pointedEntity;
				}
			}
		}
	}

	/**
	 * Gets the night vision brightness
	 */
	private float getNightVisionBrightness(EntityPlayer par1EntityPlayer,
			float par2) {
		final int var3 = par1EntityPlayer.getActivePotionEffect(
				Potion.nightVision).getDuration();
		return var3 > 200 ? 1.0F : 0.7F + MathHelper.sin((var3 - par2)
				* (float) Math.PI * 0.2F) * 0.3F;
	}

	public ShaderGroup getShaderGroup() {
		return theShaderGroup;
	}

	private void hurtCameraEffect(float par1) {
		final EntityLivingBase var2 = mc.renderViewEntity;
		float var3 = var2.hurtTime - par1;
		float var4;

		if (var2.getHealth() <= 0.0F) {
			var4 = var2.deathTime + par1;
			GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if (var3 >= 0.0F) {
			var3 /= var2.maxHurtTime;
			var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float) Math.PI);
			var4 = var2.attackedAtYaw;
			GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
		}
	}

	public boolean isShaderActive() {
		return OpenGlHelper.shadersSupported && theShaderGroup != null;
	}

	@Override
	public void onResourceManagerReload(IResourceManager par1ResourceManager) {
		if (theShaderGroup != null) {
			theShaderGroup.deleteShaderGroup();
		}

		if (shaderIndex != shaderCount) {
			try {
				theShaderGroup = new ShaderGroup(mc.getTextureManager(),
						par1ResourceManager, mc.getFramebuffer(),
						shaderResourceLocations[shaderIndex]);
				theShaderGroup.createBindFramebuffers(mc.displayWidth,
						mc.displayHeight);
			} catch (final IOException var3) {
				logger.warn("Failed to load shader: "
						+ shaderResourceLocations[shaderIndex], var3);
				shaderIndex = shaderCount;
			}
		}
	}

	/**
	 * sets up player's eye (or camera in third person mode)
	 */
	private void orientCamera(float par1) {
		final EntityLivingBase var2 = mc.renderViewEntity;
		float var3 = var2.yOffset - 1.62F;
		double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * par1;
		double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * par1 - var3;
		double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * par1;
		GL11.glRotatef(prevCamRoll + (camRoll - prevCamRoll) * par1, 0.0F,
				0.0F, 1.0F);

		if (var2.isPlayerSleeping()) {
			var3 = (float) (var3 + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);

			if (!mc.gameSettings.debugCamEnable) {
				final Block var27 = mc.theWorld.getBlock(
						MathHelper.floor_double(var2.posX),
						MathHelper.floor_double(var2.posY),
						MathHelper.floor_double(var2.posZ));

				if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
					Reflector.callVoid(
							Reflector.ForgeHooksClient_orientBedCamera,
							new Object[] { mc, var2 });
				} else if (var27 == Blocks.bed) {
					final int var11 = mc.theWorld.getBlockMetadata(
							MathHelper.floor_double(var2.posX),
							MathHelper.floor_double(var2.posY),
							MathHelper.floor_double(var2.posZ));
					final int var13 = var11 & 3;
					GL11.glRotatef(var13 * 90, 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(var2.prevRotationYaw
						+ (var2.rotationYaw - var2.prevRotationYaw) * par1
						+ 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(var2.prevRotationPitch
						+ (var2.rotationPitch - var2.prevRotationPitch) * par1,
						-1.0F, 0.0F, 0.0F);
			}
		} else if (mc.gameSettings.thirdPersonView > 0) {
			double var271 = thirdPersonDistanceTemp
					+ (thirdPersonDistance - thirdPersonDistanceTemp) * par1;
			float var28;
			float var281;

			if (mc.gameSettings.debugCamEnable) {
				var28 = prevDebugCamYaw + (debugCamYaw - prevDebugCamYaw)
						* par1;
				var281 = prevDebugCamPitch
						+ (debugCamPitch - prevDebugCamPitch) * par1;
				GL11.glTranslatef(0.0F, 0.0F, (float) -var271);
				GL11.glRotatef(var281, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
			} else {
				var28 = var2.rotationYaw;
				var281 = var2.rotationPitch;

				if (mc.gameSettings.thirdPersonView == 2) {
					var281 += 180.0F;
				}

				final double var14 = -MathHelper.sin(var28 / 180.0F
						* (float) Math.PI)
						* MathHelper.cos(var281 / 180.0F * (float) Math.PI)
						* var271;
				final double var16 = MathHelper.cos(var28 / 180.0F
						* (float) Math.PI)
						* MathHelper.cos(var281 / 180.0F * (float) Math.PI)
						* var271;
				final double var18 = -MathHelper.sin(var281 / 180.0F
						* (float) Math.PI)
						* var271;

				for (int var20 = 0; var20 < 8; ++var20) {
					float var21 = (var20 & 1) * 2 - 1;
					float var22 = (var20 >> 1 & 1) * 2 - 1;
					float var23 = (var20 >> 2 & 1) * 2 - 1;
					var21 *= 0.1F;
					var22 *= 0.1F;
					var23 *= 0.1F;
					final MovingObjectPosition var24 = mc.theWorld
							.rayTraceBlocks(
									Vec3.createVectorHelper(var4 + var21, var6
											+ var22, var8 + var23),
									Vec3.createVectorHelper(var4 - var14
											+ var21 + var23, var6 - var18
											+ var22, var8 - var16 + var23));

					if (var24 != null) {
						final double var25 = var24.hitVec.distanceTo(Vec3
								.createVectorHelper(var4, var6, var8));

						if (var25 < var271) {
							var271 = var25;
						}
					}
				}

				if (mc.gameSettings.thirdPersonView == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(var2.rotationPitch - var281, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(var2.rotationYaw - var28, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float) -var271);
				GL11.glRotatef(var28 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var281 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if (!mc.gameSettings.debugCamEnable) {
			GL11.glRotatef(var2.prevRotationPitch
					+ (var2.rotationPitch - var2.prevRotationPitch) * par1,
					1.0F, 0.0F, 0.0F);
			GL11.glRotatef(
					var2.prevRotationYaw
							+ (var2.rotationYaw - var2.prevRotationYaw) * par1
							+ 180.0F, 0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, var3, 0.0F);
		var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * par1;
		var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * par1 - var3;
		var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * par1;
		cloudFog = mc.renderGlobal.hasCloudFog(var4, var6, var8, par1);
	}

	/**
	 * Render clouds if enabled
	 */
	private void renderCloudsCheck(RenderGlobal par1RenderGlobal, float par2) {
		if (mc.gameSettings.shouldRenderClouds()) {
			mc.mcProfiler.endStartSection("clouds");
			GL11.glPushMatrix();
			setupFog(0, par2);
			GL11.glEnable(GL11.GL_FOG);
			par1RenderGlobal.renderClouds(par2);
			GL11.glDisable(GL11.GL_FOG);
			setupFog(1, par2);
			GL11.glPopMatrix();
		}
	}

	/**
	 * Render player hand
	 */
	private void renderHand(float par1, int par2) {
		if (debugViewDirection <= 0) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			final float var3 = 0.07F;

			if (mc.gameSettings.anaglyph) {
				GL11.glTranslatef(-(par2 * 2 - 1) * var3, 0.0F, 0.0F);
			}

			if (cameraZoom != 1.0D) {
				GL11.glTranslatef((float) cameraYaw, (float) -cameraPitch, 0.0F);
				GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
			}

			Project.gluPerspective(getFOVModifier(par1, false),
					(float) mc.displayWidth / (float) mc.displayHeight, 0.05F,
					farPlaneDistance * 2.0F);

			if (mc.playerController.enableEverythingIsScrewedUpMode()) {
				final float var4 = 0.6666667F;
				GL11.glScalef(1.0F, var4, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			if (mc.gameSettings.anaglyph) {
				GL11.glTranslatef((par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			hurtCameraEffect(par1);

			if (mc.gameSettings.viewBobbing) {
				setupViewBobbing(par1);
			}

			if (mc.gameSettings.thirdPersonView == 0
					&& !mc.renderViewEntity.isPlayerSleeping()
					&& !mc.gameSettings.hideGUI
					&& !mc.playerController.enableEverythingIsScrewedUpMode()) {
				enableLightmap(par1);
				itemRenderer.renderItemInFirstPerson(par1);
				disableLightmap(par1);
			}

			GL11.glPopMatrix();

			if (mc.gameSettings.thirdPersonView == 0
					&& !mc.renderViewEntity.isPlayerSleeping()) {
				itemRenderer.renderOverlays(par1);
				hurtCameraEffect(par1);
			}

			if (mc.gameSettings.viewBobbing) {
				setupViewBobbing(par1);
			}
		}
	}

	/**
	 * Render rain and snow
	 */
	protected void renderRainSnow(float par1) {
		if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
			final WorldProvider var2 = mc.theWorld.provider;
			final Object var41 = Reflector.call(var2,
					Reflector.ForgeWorldProvider_getWeatherRenderer,
					new Object[0]);

			if (var41 != null) {
				Reflector.callVoid(var41, Reflector.IRenderHandler_render,
						new Object[] { Float.valueOf(par1), mc.theWorld, mc });
				return;
			}
		}

		final float var411 = mc.theWorld.getRainStrength(par1);

		if (var411 > 0.0F) {
			enableLightmap(par1);

			if (rainXCoords == null) {
				rainXCoords = new float[1024];
				rainYCoords = new float[1024];

				for (int var421 = 0; var421 < 32; ++var421) {
					for (int var42 = 0; var42 < 32; ++var42) {
						final float var43 = var42 - 16;
						final float var44 = var421 - 16;
						final float var45 = MathHelper.sqrt_float(var43 * var43
								+ var44 * var44);
						rainXCoords[var421 << 5 | var42] = -var44 / var45;
						rainYCoords[var421 << 5 | var42] = var43 / var45;
					}
				}
			}

			if (Config.isRainOff())
				return;

			final EntityLivingBase var431 = mc.renderViewEntity;
			final WorldClient var441 = mc.theWorld;
			final int var451 = MathHelper.floor_double(var431.posX);
			final int var461 = MathHelper.floor_double(var431.posY);
			final int var471 = MathHelper.floor_double(var431.posZ);
			final Tessellator var8 = Tessellator.instance;
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			final double var9 = var431.lastTickPosX
					+ (var431.posX - var431.lastTickPosX) * par1;
			final double var11 = var431.lastTickPosY
					+ (var431.posY - var431.lastTickPosY) * par1;
			final double var13 = var431.lastTickPosZ
					+ (var431.posZ - var431.lastTickPosZ) * par1;
			final int var15 = MathHelper.floor_double(var11);
			byte var16 = 5;

			if (Config.isRainFancy()) {
				var16 = 10;
			}

			byte var18 = -1;
			final float var19 = rendererUpdateCount + par1;

			if (Config.isRainFancy()) {
				var16 = 10;
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			for (int var20 = var471 - var16; var20 <= var471 + var16; ++var20) {
				for (int var21 = var451 - var16; var21 <= var451 + var16; ++var21) {
					final int var22 = (var20 - var471 + 16) * 32 + var21
							- var451 + 16;
					final float var23 = rainXCoords[var22] * 0.5F;
					final float var24 = rainYCoords[var22] * 0.5F;
					final BiomeGenBase var25 = var441.getBiomeGenForCoords(
							var21, var20);

					if (var25.canSpawnLightningBolt() || var25.getEnableSnow()) {
						final int var26 = var441.getPrecipitationHeight(var21,
								var20);
						int var27 = var461 - var16;
						int var28 = var461 + var16;

						if (var27 < var26) {
							var27 = var26;
						}

						if (var28 < var26) {
							var28 = var26;
						}

						final float var29 = 1.0F;
						int var30 = var26;

						if (var26 < var15) {
							var30 = var15;
						}

						if (var27 != var28) {
							random.setSeed(var21 * var21 * 3121 + var21
									* 45238971 ^ var20 * var20 * 418711 + var20
									* 13761);
							final float var31 = var25.getFloatTemperature(
									var21, var27, var20);
							float var32;
							double var35;

							if (var441.getWorldChunkManager()
									.getTemperatureAtHeight(var31, var26) >= 0.15F) {
								if (var18 != 0) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 0;
									mc.getTextureManager().bindTexture(
											locationRainPng);
									var8.startDrawingQuads();
								}

								var32 = ((rendererUpdateCount + var21 * var21
										* 3121 + var21 * 45238971 + var20
										* var20 * 418711 + var20 * 13761 & 31) + par1)
										/ 32.0F * (3.0F + random.nextFloat());
								final double var46 = var21 + 0.5F - var431.posX;
								var35 = var20 + 0.5F - var431.posZ;
								final float var47 = MathHelper
										.sqrt_double(var46 * var46 + var35
												* var35)
										/ var16;
								final float var38 = 1.0F;
								var8.setBrightness(var441
										.getLightBrightnessForSkyBlocks(var21,
												var30, var20, 0));
								var8.setColorRGBA_F(var38, var38, var38,
										((1.0F - var47 * var47) * 0.5F + 0.5F)
												* var411);
								var8.setTranslation(-var9 * 1.0D,
										-var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV(var21 - var23 + 0.5D,
										var27, var20 - var24 + 0.5D,
										0.0F * var29, var27 * var29 / 4.0F
												+ var32 * var29);
								var8.addVertexWithUV(var21 + var23 + 0.5D,
										var27, var20 + var24 + 0.5D,
										1.0F * var29, var27 * var29 / 4.0F
												+ var32 * var29);
								var8.addVertexWithUV(var21 + var23 + 0.5D,
										var28, var20 + var24 + 0.5D,
										1.0F * var29, var28 * var29 / 4.0F
												+ var32 * var29);
								var8.addVertexWithUV(var21 - var23 + 0.5D,
										var28, var20 - var24 + 0.5D,
										0.0F * var29, var28 * var29 / 4.0F
												+ var32 * var29);
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							} else {
								if (var18 != 1) {
									if (var18 >= 0) {
										var8.draw();
									}

									var18 = 1;
									mc.getTextureManager().bindTexture(
											locationSnowPng);
									var8.startDrawingQuads();
								}

								var32 = ((rendererUpdateCount & 511) + par1) / 512.0F;
								final float var48 = random.nextFloat() + var19
										* 0.01F * (float) random.nextGaussian();
								final float var34 = random.nextFloat() + var19
										* (float) random.nextGaussian()
										* 0.001F;
								var35 = var21 + 0.5F - var431.posX;
								final double var49 = var20 + 0.5F - var431.posZ;
								final float var39 = MathHelper
										.sqrt_double(var35 * var35 + var49
												* var49)
										/ var16;
								final float var40 = 1.0F;
								var8.setBrightness((var441
										.getLightBrightnessForSkyBlocks(var21,
												var30, var20, 0) * 3 + 15728880) / 4);
								var8.setColorRGBA_F(var40, var40, var40,
										((1.0F - var39 * var39) * 0.3F + 0.5F)
												* var411);
								var8.setTranslation(-var9 * 1.0D,
										-var11 * 1.0D, -var13 * 1.0D);
								var8.addVertexWithUV(var21 - var23 + 0.5D,
										var27, var20 - var24 + 0.5D, 0.0F
												* var29 + var48, var27 * var29
												/ 4.0F + var32 * var29 + var34);
								var8.addVertexWithUV(var21 + var23 + 0.5D,
										var27, var20 + var24 + 0.5D, 1.0F
												* var29 + var48, var27 * var29
												/ 4.0F + var32 * var29 + var34);
								var8.addVertexWithUV(var21 + var23 + 0.5D,
										var28, var20 + var24 + 0.5D, 1.0F
												* var29 + var48, var28 * var29
												/ 4.0F + var32 * var29 + var34);
								var8.addVertexWithUV(var21 - var23 + 0.5D,
										var28, var20 - var24 + 0.5D, 0.0F
												* var29 + var48, var28 * var29
												/ 4.0F + var32 * var29 + var34);
								var8.setTranslation(0.0D, 0.0D, 0.0D);
							}
						}
					}
				}
			}

			if (var18 >= 0) {
				var8.draw();
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			disableLightmap(par1);
		}
	}

	public void renderWorld(float par1, long par2) {
		mc.mcProfiler.startSection("lightTex");

		if (lightmapUpdateNeeded) {
			updateLightmap(par1);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

		if (mc.renderViewEntity == null) {
			mc.renderViewEntity = mc.thePlayer;
		}

		mc.mcProfiler.endStartSection("pick");
		getMouseOver(par1);
		final EntityLivingBase var4 = mc.renderViewEntity;
		final RenderGlobal var5 = mc.renderGlobal;
		final EffectRenderer var6 = mc.effectRenderer;
		final double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX)
				* par1;
		final double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY)
				* par1;
		final double var11 = var4.lastTickPosZ
				+ (var4.posZ - var4.lastTickPosZ) * par1;
		mc.mcProfiler.endStartSection("center");

		for (int var13 = 0; var13 < 2; ++var13) {
			if (mc.gameSettings.anaglyph) {
				anaglyphField = var13;

				if (anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			mc.mcProfiler.endStartSection("clear");
			GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
			updateFogColor(par1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			mc.mcProfiler.endStartSection("camera");
			setupCameraTransform(par1, var13);
			ActiveRenderInfo.updateRenderInfo(mc.thePlayer,
					mc.gameSettings.thirdPersonView == 2);
			mc.mcProfiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();

			if (!Config.isSkyEnabled() && !Config.isSunMoonEnabled()
					&& !Config.isStarsEnabled()) {
				GL11.glDisable(GL11.GL_BLEND);
			} else {
				setupFog(-1, par1);
				mc.mcProfiler.endStartSection("sky");
				var5.renderSky(par1);
			}

			GL11.glEnable(GL11.GL_FOG);
			setupFog(1, par1);

			if (mc.gameSettings.ambientOcclusion != 0) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			mc.mcProfiler.endStartSection("culling");
			final Frustrum var14 = new Frustrum();
			var14.setPosition(var7, var9, var11);
			mc.renderGlobal.clipRenderersByFrustum(var14, par1);

			if (var13 == 0) {
				mc.mcProfiler.endStartSection("updatechunks");

				while (!mc.renderGlobal.updateRenderers(var4, false)
						&& par2 != 0L) {
					final long var17 = par2 - System.nanoTime();

					if (var17 < 0L || var17 > 1000000000L) {
						break;
					}
				}
			}

			if (var4.posY < 128.0D) {
				renderCloudsCheck(var5, par1);
			}

			mc.mcProfiler.endStartSection("prepareterrain");
			setupFog(0, par1);
			GL11.glEnable(GL11.GL_FOG);
			mc.getTextureManager()
					.bindTexture(TextureMap.locationBlocksTexture);
			RenderHelper.disableStandardItemLighting();
			mc.mcProfiler.endStartSection("terrain");
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			var5.sortAndRender(var4, 0, par1);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			final boolean hasForge = Reflector.ForgeHooksClient.exists();
			EntityPlayer var18;

			if (debugViewDirection == 0) {
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				RenderHelper.enableStandardItemLighting();
				mc.mcProfiler.endStartSection("entities");

				if (hasForge) {
					Reflector.callVoid(
							Reflector.ForgeHooksClient_setRenderPass,
							new Object[] { Integer.valueOf(0) });
				}

				var5.renderEntities(var4, var14, par1);

				if (hasForge) {
					Reflector.callVoid(
							Reflector.ForgeHooksClient_setRenderPass,
							new Object[] { Integer.valueOf(-1) });
				}

				RenderHelper.disableStandardItemLighting();
				disableLightmap(par1);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();

				if (mc.objectMouseOver != null
						&& var4.isInsideOfMaterial(Material.water)
						&& var4 instanceof EntityPlayer
						&& !mc.gameSettings.hideGUI) {
					var18 = (EntityPlayer) var4;
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					mc.mcProfiler.endStartSection("outline");

					if ((!hasForge || !Reflector.callBoolean(
							Reflector.ForgeHooksClient_onDrawBlockHighlight,
							new Object[] { var5, var18, mc.objectMouseOver,
									Integer.valueOf(0),
									var18.inventory.getCurrentItem(),
									Float.valueOf(par1) }))
							&& !mc.gameSettings.hideGUI) {
						var5.drawSelectionBox(var18, mc.objectMouseOver, 0,
								par1);
					}
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();

			if (cameraZoom == 1.0D && var4 instanceof EntityPlayer
					&& !mc.gameSettings.hideGUI && mc.objectMouseOver != null
					&& !var4.isInsideOfMaterial(Material.water)) {
				var18 = (EntityPlayer) var4;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				mc.mcProfiler.endStartSection("outline");

				if ((!hasForge || !Reflector.callBoolean(
						Reflector.ForgeHooksClient_onDrawBlockHighlight,
						new Object[] { var5, var18, mc.objectMouseOver,
								Integer.valueOf(0),
								var18.inventory.getCurrentItem(),
								Float.valueOf(par1) }))
						&& !mc.gameSettings.hideGUI) {
					var5.drawSelectionBox(var18, mc.objectMouseOver, 0, par1);
				}
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			mc.mcProfiler.endStartSection("destroyProgress");
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			var5.drawBlockDamageTexture(Tessellator.instance, var4, par1);
			GL11.glDisable(GL11.GL_BLEND);

			if (debugViewDirection == 0) {
				enableLightmap(par1);
				mc.mcProfiler.endStartSection("litParticles");
				var6.renderLitParticles(var4, par1);
				RenderHelper.disableStandardItemLighting();
				setupFog(0, par1);
				mc.mcProfiler.endStartSection("particles");
				var6.renderParticles(var4, par1);
				disableLightmap(par1);
			}

			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_CULL_FACE);
			mc.mcProfiler.endStartSection("weather");
			renderRainSnow(par1);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			setupFog(0, par1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			mc.getTextureManager()
					.bindTexture(TextureMap.locationBlocksTexture);
			WrUpdates.resumeBackgroundUpdates();

			if (Config.isWaterFancy()) {
				mc.mcProfiler.endStartSection("water");

				if (mc.gameSettings.ambientOcclusion != 0) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);

				if (mc.gameSettings.anaglyph) {
					if (anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}

					var5.renderAllSortedRenderers(1, par1);
				} else {
					var5.renderAllSortedRenderers(1, par1);
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				mc.mcProfiler.endStartSection("water");
				var5.renderAllSortedRenderers(1, par1);
			}

			WrUpdates.pauseBackgroundUpdates();

			if (hasForge && debugViewDirection == 0) {
				RenderHelper.enableStandardItemLighting();
				mc.mcProfiler.endStartSection("entities");
				Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass,
						new Object[] { Integer.valueOf(1) });
				mc.renderGlobal.renderEntities(var4, var14, par1);
				Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass,
						new Object[] { Integer.valueOf(-1) });
				RenderHelper.disableStandardItemLighting();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);

			if (var4.posY >= 128.0D + mc.gameSettings.ofCloudsHeight * 128.0F) {
				mc.mcProfiler.endStartSection("aboveClouds");
				renderCloudsCheck(var5, par1);
			}

			if (hasForge) {
				mc.mcProfiler.endStartSection("FRenderLast");
				Reflector.callVoid(
						Reflector.ForgeHooksClient_dispatchRenderLast,
						new Object[] { var5, Float.valueOf(par1) });
			}

			mc.mcProfiler.endStartSection("hand");
			final boolean viewBobbing = mc.gameSettings.viewBobbing;
			mc.gameSettings.viewBobbing = false;
			mc.entityRenderer.setupCameraTransform(par1, var13);
			final EventRender3D event = new EventRender3D(par1);
			TeamBattleClient.getEventManager().call(event);
			mc.gameSettings.viewBobbing = viewBobbing;
			final boolean renderFirstPersonHand = Reflector.callBoolean(
					Reflector.ForgeHooksClient_renderFirstPersonHand,
					new Object[] { mc.renderGlobal, Float.valueOf(par1),
							Integer.valueOf(var13) });

			if (!renderFirstPersonHand && cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				renderHand(par1, var13);
			}

			if (!mc.gameSettings.anaglyph) {
				mc.mcProfiler.endSection();
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
		mc.mcProfiler.endSection();
	}

	/**
	 * Update and return fogColorBuffer with the RGBA values passed as arguments
	 */
	private FloatBuffer setFogColorBuffer(float par1, float par2, float par3,
			float par4) {
		fogColorBuffer.clear();
		fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
		fogColorBuffer.flip();
		return fogColorBuffer;
	}

	/**
	 * sets up projection, view effects, camera position/rotation
	 */
	public void setupCameraTransform(float par1, int par2) {
		farPlaneDistance = mc.gameSettings.renderDistanceChunks * 16;

		if (Config.isFogFancy()) {
			farPlaneDistance *= 0.95F;
		}

		if (Config.isFogFast()) {
			farPlaneDistance *= 0.83F;
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		final float var3 = 0.07F;

		if (mc.gameSettings.anaglyph) {
			GL11.glTranslatef(-(par2 * 2 - 1) * var3, 0.0F, 0.0F);
		}

		float clipDistance = farPlaneDistance * 2.0F;

		if (clipDistance < 128.0F) {
			clipDistance = 128.0F;
		}

		if (mc.theWorld.provider.dimensionId == 1) {
			clipDistance = 256.0F;
		}

		if (cameraZoom != 1.0D) {
			GL11.glTranslatef((float) cameraYaw, (float) -cameraPitch, 0.0F);
			GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
		}

		Project.gluPerspective(getFOVModifier(par1, true),
				(float) mc.displayWidth / (float) mc.displayHeight, 0.05F,
				clipDistance);
		float var4;

		if (mc.playerController.enableEverythingIsScrewedUpMode()) {
			var4 = 0.6666667F;
			GL11.glScalef(1.0F, var4, 1.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		if (mc.gameSettings.anaglyph) {
			GL11.glTranslatef((par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		hurtCameraEffect(par1);

		if (mc.gameSettings.viewBobbing) {
			setupViewBobbing(par1);
		}

		var4 = mc.thePlayer.prevTimeInPortal
				+ (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal)
				* par1;

		if (var4 > 0.0F) {
			final byte var7 = 20;

			// TeamBattleClient
			// if (mc.thePlayer.isPotionActive(Potion.confusion)) {
			// var7 = 7;
			// }

			float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
			var6 *= var6;
			GL11.glRotatef((rendererUpdateCount + par1) * var7, 0.0F, 1.0F,
					1.0F);
			GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
			GL11.glRotatef(-(rendererUpdateCount + par1) * var7, 0.0F, 1.0F,
					1.0F);
		}

		orientCamera(par1);

		if (debugViewDirection > 0) {
			final int var71 = debugViewDirection - 1;

			if (var71 == 1) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var71 == 2) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var71 == 3) {
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (var71 == 4) {
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (var71 == 5) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	/**
	 * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts
	 * at 0 and goes to 80% of far plane distance and is used for sky rendering.
	 */
	private void setupFog(int par1, float par2) {
		final EntityLivingBase var3 = mc.renderViewEntity;
		boolean var4 = false;
		fogStandard = false;

		if (var3 instanceof EntityPlayer) {
			var4 = ((EntityPlayer) var3).capabilities.isCreativeMode;
		}

		if (par1 == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR,
					setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, 8.0F);

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}

			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
		} else {
			GL11.glFog(
					GL11.GL_FOG_COLOR,
					setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue,
							1.0F));
			GL11.glNormal3f(0.0F, -1.0F, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final Block var5 = ActiveRenderInfo.getBlockAtEntityViewpoint(
					mc.theWorld, var3, par2);
			final Object event = Reflector.newInstance(
					Reflector.EntityViewRenderEvent_FogDensity_Constructor,
					new Object[] { this, var3, var5, Float.valueOf(par2),
							Float.valueOf(0.1F) });

			if (Reflector.postForgeBusEvent(event)) {
				final float var10 = Reflector.getFieldValueFloat(event,
						Reflector.EntityViewRenderEvent_FogDensity_density,
						0.0F);
				GL11.glFogf(GL11.GL_FOG_DENSITY, var10);
			} else {
				float var6;

				// TeamBattleClient
				// if (var3.isPotionActive(Potion.blindness)) {
				// var6 = 5.0F;
				// final int var101 = var3.getActivePotionEffect(
				// Potion.blindness).getDuration();
				//
				// if (var101 < 20) {
				// var6 = 5.0F + (farPlaneDistance - 5.0F)
				// * (1.0F - var101 / 20.0F);
				// }
				//
				// GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
				//
				// if (par1 < 0) {
				// GL11.glFogf(GL11.GL_FOG_START, 0.0F);
				// GL11.glFogf(GL11.GL_FOG_END, var6 * 0.8F);
				// } else {
				// GL11.glFogf(GL11.GL_FOG_START, var6 * 0.25F);
				// GL11.glFogf(GL11.GL_FOG_END, var6);
				// }
				//
				// if (Config.isFogFancy()) {
				// GL11.glFogi(34138, 34139);
				// }
				// } else if (cloudFog) {
				if (cloudFog) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				} else if (var5.getMaterial() == Material.water) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

					if (var3.isPotionActive(Potion.waterBreathing)) {
						GL11.glFogf(GL11.GL_FOG_DENSITY, 0.05F);
					} else {
						GL11.glFogf(
								GL11.GL_FOG_DENSITY,
								0.1F - EnchantmentHelper.getRespiration(var3) * 0.03F);
					}

					if (Config.isClearWater()) {
						GL11.glFogf(GL11.GL_FOG_DENSITY, 0.02F);
					}
				} else if (var5.getMaterial() == Material.lava) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
				} else {
					var6 = farPlaneDistance;
					fogStandard = true;

					if (Config.isDepthFog()
							&& mc.theWorld.provider.getWorldHasVoidParticles()
							&& !var4) {
						double var102 = ((var3.getBrightnessForRender(par2) & 15728640) >> 20)
								/ 16.0D
								+ (var3.lastTickPosY
										+ (var3.posY - var3.lastTickPosY)
										* par2 + 4.0D) / 32.0D;

						if (var102 < 1.0D) {
							if (var102 < 0.0D) {
								var102 = 0.0D;
							}

							var102 *= var102;
							float var9 = 100.0F * (float) var102;

							if (var9 < 5.0F) {
								var9 = 5.0F;
							}

							if (var6 > var9) {
								var6 = var9;
							}
						}
					}

					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

					if (par1 < 0) {
						GL11.glFogf(GL11.GL_FOG_START, 0.0F);
						GL11.glFogf(GL11.GL_FOG_END, var6);
					} else {
						GL11.glFogf(GL11.GL_FOG_START,
								var6 * Config.getFogStart());
						GL11.glFogf(GL11.GL_FOG_END, var6);
					}

					if (GLContext.getCapabilities().GL_NV_fog_distance) {
						if (Config.isFogFancy()) {
							GL11.glFogi(34138, 34139);
						}

						if (Config.isFogFast()) {
							GL11.glFogi(34138, 34140);
						}
					}

					if (mc.theWorld.provider.doesXZShowFog((int) var3.posX,
							(int) var3.posZ)) {
						var6 = farPlaneDistance;
						GL11.glFogf(GL11.GL_FOG_START, var6 * 0.05F);
						GL11.glFogf(GL11.GL_FOG_END, var6);
					}
				}
			}

			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
		}
	}

	/**
	 * Setup orthogonal projection for rendering GUI screen overlays
	 */
	public void setupOverlayRendering() {
		final ScaledResolution var1 = new ScaledResolution(mc, mc.displayWidth,
				mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, var1.getScaledWidth_double(),
				var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	/**
	 * Setups all the GL settings for view bobbing. Args: partialTickTime
	 */
	private void setupViewBobbing(float par1) {
		if (mc.renderViewEntity instanceof EntityPlayer) {
			final EntityPlayer var2 = (EntityPlayer) mc.renderViewEntity;
			final float var3 = var2.distanceWalkedModified
					- var2.prevDistanceWalkedModified;
			final float var4 = -(var2.distanceWalkedModified + var3 * par1);
			final float var5 = var2.prevCameraYaw
					+ (var2.cameraYaw - var2.prevCameraYaw) * par1;
			final float var6 = var2.prevCameraPitch
					+ (var2.cameraPitch - var2.prevCameraPitch) * par1;
			GL11.glTranslatef(MathHelper.sin(var4 * (float) Math.PI) * var5
					* 0.5F,
					-Math.abs(MathHelper.cos(var4 * (float) Math.PI) * var5),
					0.0F);
			GL11.glRotatef(
					MathHelper.sin(var4 * (float) Math.PI) * var5 * 3.0F, 0.0F,
					0.0F, 1.0F);
			GL11.glRotatef(
					Math.abs(MathHelper.cos(var4 * (float) Math.PI - 0.2F)
							* var5) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
		}
	}

	private void showLagometer(long tickTimeNano, long chunkTimeNano) {
		if (mc.gameSettings.ofLagometer || showExtendedDebugInfo) {
			if (prevFrameTimeNano == -1L) {
				prevFrameTimeNano = System.nanoTime();
			}

			final long timeNowNano = System.nanoTime();
			final int currFrameIndex = numRecordedFrameTimes
					& frameTimes.length - 1;
			tickTimes[currFrameIndex] = tickTimeNano;
			chunkTimes[currFrameIndex] = chunkTimeNano;
			serverTimes[currFrameIndex] = serverWaitTimeCurrent;
			frameTimes[currFrameIndex] = timeNowNano - prevFrameTimeNano;
			++numRecordedFrameTimes;
			prevFrameTimeNano = timeNowNano;
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, mc.displayWidth, mc.displayHeight, 0.0D,
					1000.0D, 3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glLineWidth(1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawing(1);

			for (int frameNum = 0; frameNum < frameTimes.length; ++frameNum) {
				final int lum = (frameNum - numRecordedFrameTimes & frameTimes.length - 1)
						* 255 / frameTimes.length;
				final long heightFrame = frameTimes[frameNum] / 200000L;
				float baseHeight = mc.displayHeight;
				tessellator.setColorOpaque_I(-16777216 + lum * 256);
				tessellator.addVertex(frameNum + 0.5F, baseHeight - heightFrame
						+ 0.5F, 0.0D);
				tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F, 0.0D);
				baseHeight -= heightFrame;
				final long heightTick = tickTimes[frameNum] / 200000L;
				tessellator.setColorOpaque_I(-16777216 + lum * 65536 + lum
						* 256 + lum * 1);
				tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F, 0.0D);
				tessellator.addVertex(frameNum + 0.5F, baseHeight + heightTick
						+ 0.5F, 0.0D);
				baseHeight += heightTick;
				final long heightChunk = chunkTimes[frameNum] / 200000L;
				tessellator.setColorOpaque_I(-16777216 + lum * 65536);
				tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F, 0.0D);
				tessellator.addVertex(frameNum + 0.5F, baseHeight + heightChunk
						+ 0.5F, 0.0D);
				baseHeight += heightChunk;
				final long srvTime = serverTimes[frameNum];

				if (srvTime > 0L) {
					final long heightSrv = srvTime * 1000000L / 200000L;
					tessellator.setColorOpaque_I(-16777216 + lum * 1);
					tessellator.addVertex(frameNum + 0.5F, baseHeight + 0.5F,
							0.0D);
					tessellator.addVertex(frameNum + 0.5F, baseHeight
							+ heightSrv + 0.5F, 0.0D);
				}
			}

			tessellator.draw();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}

	/**
	 * Will update any inputs that effect the camera angle (mouse) and then
	 * render the world and GUI
	 */
	public void updateCameraAndRender(float par1) {
		mc.mcProfiler.startSection("lightTex");

		if (!initialized) {
			TextureUtils.registerResourceListener();
			final ItemRendererOF world = new ItemRendererOF(mc);
			itemRenderer = world;
			RenderManager.instance.itemRenderer = world;
			initialized = true;
		}

		Config.checkDisplayMode();
		final WorldClient world1 = mc.theWorld;

		if (mc.currentScreen instanceof GuiMainMenu) {
			updateMainMenu((GuiMainMenu) mc.currentScreen);
		}

		if (updatedWorld != world1) {
			RandomMobs.worldChanged(updatedWorld, world1);
			Config.updateThreadPriorities();
			lastServerTime = 0L;
			lastServerTicks = 0;
			updatedWorld = world1;
		}

		RenderBlocks.fancyGrass = Config.isGrassFancy()
				|| Config.isBetterGrassFancy();
		Blocks.leaves.func_150122_b(Config.isTreesFancy());

		if (lightmapUpdateNeeded) {
			updateLightmap(par1);
		}

		mc.mcProfiler.endSection();
		final boolean var21 = Display.isActive();

		if (!var21 && mc.gameSettings.pauseOnLostFocus
				&& (!mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
			if (Minecraft.getSystemTime() - prevFrameTime > 500L) {
				mc.displayInGameMenu();
			}
		} else {
			prevFrameTime = Minecraft.getSystemTime();
		}

		mc.mcProfiler.startSection("mouse");

		if (mc.inGameHasFocus && var21) {
			mc.mouseHelper.mouseXYChange();
			final float var132 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			final float var141 = var132 * var132 * var132 * 8.0F;
			float var15 = mc.mouseHelper.deltaX * var141;
			float var16 = mc.mouseHelper.deltaY * var141;
			byte var17 = 1;

			if (mc.gameSettings.invertMouse) {
				var17 = -1;
			}

			if (mc.gameSettings.smoothCamera) {
				smoothCamYaw += var15;
				smoothCamPitch += var16;
				final float var18 = par1 - smoothCamPartialTicks;
				smoothCamPartialTicks = par1;
				var15 = smoothCamFilterX * var18;
				var16 = smoothCamFilterY * var18;
				mc.thePlayer.setAngles(var15, var16 * var17);
			} else {
				mc.thePlayer.setAngles(var15, var16 * var17);
			}
		}

		mc.mcProfiler.endSection();

		if (!mc.skipRenderWorld) {
			anaglyphEnable = mc.gameSettings.anaglyph;
			final ScaledResolution var133 = new ScaledResolution(mc,
					mc.displayWidth, mc.displayHeight);
			final int var142 = var133.getScaledWidth();
			final int var151 = var133.getScaledHeight();
			final int var161 = Mouse.getX() * var142 / mc.displayWidth;
			final int var171 = var151 - Mouse.getY() * var151
					/ mc.displayHeight - 1;
			final int var181 = mc.gameSettings.limitFramerate;

			if (mc.theWorld != null) {
				mc.mcProfiler.startSection("level");

				if (mc.isFramerateLimitBelowMax()) {
					renderWorld(par1, renderEndNanoTime + 1000000000 / var181);
				} else {
					renderWorld(par1, 0L);
				}

				if (OpenGlHelper.shadersSupported) {
					if (theShaderGroup != null) {
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glPushMatrix();
						GL11.glLoadIdentity();
						theShaderGroup.loadShaderGroup(par1);
						GL11.glPopMatrix();
					}

					mc.getFramebuffer().bindFramebuffer(true);
				}

				renderEndNanoTime = System.nanoTime();
				mc.mcProfiler.endStartSection("gui");

				if (!mc.gameSettings.hideGUI || mc.currentScreen != null) {
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					mc.ingameGUI.renderGameOverlay(par1,
							mc.currentScreen != null, var161, var171);
				}

				mc.mcProfiler.endSection();
			} else {
				GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				setupOverlayRendering();
				renderEndNanoTime = System.nanoTime();
			}

			if (mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

				try {
					boolean var12 = false;

					if (Reflector.EventBus_post.exists()) {
						var12 = Reflector.postForgeBusEvent(
								Reflector.DrawScreenEvent_Pre_Constructor,
								new Object[] { mc.currentScreen,
										Integer.valueOf(var161),
										Integer.valueOf(var171),
										Float.valueOf(par1) });
					}

					if (!var12) {
						mc.currentScreen.drawScreen(var161, var171, par1);
					}

					Reflector.postForgeBusEvent(
							Reflector.DrawScreenEvent_Post_Constructor,
							new Object[] { mc.currentScreen,
									Integer.valueOf(var161),
									Integer.valueOf(var171),
									Float.valueOf(par1) });
				} catch (final Throwable var131) {
					final CrashReport var10 = CrashReport.makeCrashReport(
							var131, "Rendering screen");
					final CrashReportCategory var11 = var10
							.makeCategory("Screen render details");
					var11.addCrashSectionCallable("Screen name",
							new Callable() {
								@Override
								public String call() {
									return mc.currentScreen.getClass()
											.getCanonicalName();
								}
							});
					var11.addCrashSectionCallable("Mouse location",
							new Callable() {
								@Override
								public String call() {
									return String
											.format("Scaled: (%d, %d). Absolute: (%d, %d)",
													new Object[] {
															Integer.valueOf(var161),
															Integer.valueOf(var171),
															Integer.valueOf(Mouse
																	.getX()),
															Integer.valueOf(Mouse
																	.getY()) });
								}
							});
					var11.addCrashSectionCallable("Screen size",
							new Callable() {
								@Override
								public String call() {
									return String
											.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
													new Object[] {
															Integer.valueOf(var133
																	.getScaledWidth()),
															Integer.valueOf(var133
																	.getScaledHeight()),
															Integer.valueOf(mc.displayWidth),
															Integer.valueOf(mc.displayHeight),
															Integer.valueOf(var133
																	.getScaleFactor()) });
								}
							});
					throw new ReportedException(var10);
				}
			}
		}

		waitForServerThread();

		if (mc.gameSettings.showDebugInfo != lastShowDebugInfo) {
			showExtendedDebugInfo = mc.gameSettings.showDebugProfilerChart;
			lastShowDebugInfo = mc.gameSettings.showDebugInfo;
		}

		if (mc.gameSettings.showDebugInfo) {
			showLagometer(mc.mcProfiler.timeTickNano,
					mc.mcProfiler.timeUpdateChunksNano);
		}

		if (mc.gameSettings.ofProfiler) {
			mc.gameSettings.showDebugProfilerChart = true;
		}
	}

	/**
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float par1) {
		final WorldClient var2 = mc.theWorld;
		final EntityLivingBase var3 = mc.renderViewEntity;
		float var4 = 0.25F + 0.75F * mc.gameSettings.renderDistanceChunks / 16.0F;
		var4 = 1.0F - (float) Math.pow(var4, 0.25D);
		Vec3 var5 = var2.getSkyColor(mc.renderViewEntity, par1);
		var5 = CustomColorizer.getWorldSkyColor(var5, var2,
				mc.renderViewEntity, par1);
		final float var6 = (float) var5.xCoord;
		final float var7 = (float) var5.yCoord;
		final float var8 = (float) var5.zCoord;
		Vec3 var9 = var2.getFogColor(par1);
		var9 = CustomColorizer.getWorldFogColor(var9, var2, par1);
		fogColorRed = (float) var9.xCoord;
		fogColorGreen = (float) var9.yCoord;
		fogColorBlue = (float) var9.zCoord;
		float var11;

		if (mc.gameSettings.renderDistanceChunks >= 4) {
			final Vec3 var19 = MathHelper.sin(var2
					.getCelestialAngleRadians(par1)) > 0.0F ? Vec3
					.createVectorHelper(-1.0D, 0.0D, 0.0D) : Vec3
					.createVectorHelper(1.0D, 0.0D, 0.0D);
			var11 = (float) var3.getLook(par1).dotProduct(var19);

			if (var11 < 0.0F) {
				var11 = 0.0F;
			}

			if (var11 > 0.0F) {
				final float[] var20 = var2.provider.calcSunriseSunsetColors(
						var2.getCelestialAngle(par1), par1);

				if (var20 != null) {
					var11 *= var20[3];
					fogColorRed = fogColorRed * (1.0F - var11) + var20[0]
							* var11;
					fogColorGreen = fogColorGreen * (1.0F - var11) + var20[1]
							* var11;
					fogColorBlue = fogColorBlue * (1.0F - var11) + var20[2]
							* var11;
				}
			}
		}

		fogColorRed += (var6 - fogColorRed) * var4;
		fogColorGreen += (var7 - fogColorGreen) * var4;
		fogColorBlue += (var8 - fogColorBlue) * var4;
		final float var191 = var2.getRainStrength(par1);
		float var201;

		if (var191 > 0.0F) {
			var11 = 1.0F - var191 * 0.5F;
			var201 = 1.0F - var191 * 0.4F;
			fogColorRed *= var11;
			fogColorGreen *= var11;
			fogColorBlue *= var201;
		}

		var11 = var2.getWeightedThunderStrength(par1);

		if (var11 > 0.0F) {
			var201 = 1.0F - var11 * 0.5F;
			fogColorRed *= var201;
			fogColorGreen *= var201;
			fogColorBlue *= var201;
		}

		final Block var21 = ActiveRenderInfo.getBlockAtEntityViewpoint(
				mc.theWorld, var3, par1);
		float var22;
		Vec3 fogYFactor;

		if (cloudFog) {
			fogYFactor = var2.getCloudColour(par1);
			fogColorRed = (float) fogYFactor.xCoord;
			fogColorGreen = (float) fogYFactor.yCoord;
			fogColorBlue = (float) fogYFactor.zCoord;
		} else if (var21.getMaterial() == Material.water) {
			var22 = EnchantmentHelper.getRespiration(var3) * 0.2F;
			fogColorRed = 0.02F + var22;
			fogColorGreen = 0.02F + var22;
			fogColorBlue = 0.2F + var22;
			fogYFactor = CustomColorizer.getUnderwaterColor(mc.theWorld,
					mc.renderViewEntity.posX, mc.renderViewEntity.posY + 1.0D,
					mc.renderViewEntity.posZ);

			if (fogYFactor != null) {
				fogColorRed = (float) fogYFactor.xCoord;
				fogColorGreen = (float) fogYFactor.yCoord;
				fogColorBlue = (float) fogYFactor.zCoord;
			}
		} else if (var21.getMaterial() == Material.lava) {
			fogColorRed = 0.6F;
			fogColorGreen = 0.1F;
			fogColorBlue = 0.0F;
		}

		var22 = fogColor2 + (fogColor1 - fogColor2) * par1;
		fogColorRed *= var22;
		fogColorGreen *= var22;
		fogColorBlue *= var22;
		double fogYFactor1 = var2.provider.getVoidFogYFactor();

		if (!Config.isDepthFog()) {
			fogYFactor1 = 1.0D;
		}

		double var14 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY)
				* par1)
				* fogYFactor1;

		// TeamBattleClient
		// if (var3.isPotionActive(Potion.blindness)) {
		// final int var23 = var3.getActivePotionEffect(Potion.blindness)
		// .getDuration();
		//
		// if (var23 < 20) {
		// var14 *= 1.0F - var23 / 20.0F;
		// } else {
		// var14 = 0.0D;
		// }
		// }

		if (var14 < 1.0D) {
			if (var14 < 0.0D) {
				var14 = 0.0D;
			}

			var14 *= var14;
			fogColorRed = (float) (fogColorRed * var14);
			fogColorGreen = (float) (fogColorGreen * var14);
			fogColorBlue = (float) (fogColorBlue * var14);
		}

		float var231;

		if (bossColorModifier > 0.0F) {
			var231 = bossColorModifierPrev
					+ (bossColorModifier - bossColorModifierPrev) * par1;
			fogColorRed = fogColorRed * (1.0F - var231) + fogColorRed * 0.7F
					* var231;
			fogColorGreen = fogColorGreen * (1.0F - var231) + fogColorGreen
					* 0.6F * var231;
			fogColorBlue = fogColorBlue * (1.0F - var231) + fogColorBlue * 0.6F
					* var231;
		}

		float var17;

		if (var3.isPotionActive(Potion.nightVision)) {
			var231 = getNightVisionBrightness(mc.thePlayer, par1);
			var17 = 1.0F / fogColorRed;

			if (var17 > 1.0F / fogColorGreen) {
				var17 = 1.0F / fogColorGreen;
			}

			if (var17 > 1.0F / fogColorBlue) {
				var17 = 1.0F / fogColorBlue;
			}

			fogColorRed = fogColorRed * (1.0F - var231) + fogColorRed * var17
					* var231;
			fogColorGreen = fogColorGreen * (1.0F - var231) + fogColorGreen
					* var17 * var231;
			fogColorBlue = fogColorBlue * (1.0F - var231) + fogColorBlue
					* var17 * var231;
		}

		if (mc.gameSettings.anaglyph) {
			var231 = (fogColorRed * 30.0F + fogColorGreen * 59.0F + fogColorBlue * 11.0F) / 100.0F;
			var17 = (fogColorRed * 30.0F + fogColorGreen * 70.0F) / 100.0F;
			final float event = (fogColorRed * 30.0F + fogColorBlue * 70.0F) / 100.0F;
			fogColorRed = var231;
			fogColorGreen = var17;
			fogColorBlue = event;
		}

		if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
			final Object event1 = Reflector.newInstance(
					Reflector.EntityViewRenderEvent_FogColors_Constructor,
					new Object[] { this, var3, var21, Float.valueOf(par1),
							Float.valueOf(fogColorRed),
							Float.valueOf(fogColorGreen),
							Float.valueOf(fogColorBlue) });
			Reflector.postForgeBusEvent(event1);
			fogColorRed = Reflector.getFieldValueFloat(event1,
					Reflector.EntityViewRenderEvent_FogColors_red, fogColorRed);
			fogColorGreen = Reflector.getFieldValueFloat(event1,
					Reflector.EntityViewRenderEvent_FogColors_green,
					fogColorGreen);
			fogColorBlue = Reflector.getFieldValueFloat(event1,
					Reflector.EntityViewRenderEvent_FogColors_blue,
					fogColorBlue);
		}

		GL11.glClearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
	}

	/**
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		if (mc.renderViewEntity instanceof EntityPlayerSP) {
			final EntityPlayerSP var1 = (EntityPlayerSP) mc.renderViewEntity;
			fovMultiplierTemp = var1.getFOVMultiplier();
		} else {
			fovMultiplierTemp = mc.thePlayer.getFOVMultiplier();
		}

		fovModifierHandPrev = fovModifierHand;
		fovModifierHand += (fovMultiplierTemp - fovModifierHand) * 0.5F;

		if (fovModifierHand > 1.5F) {
			fovModifierHand = 1.5F;
		}

		if (fovModifierHand < 0.1F) {
			fovModifierHand = 0.1F;
		}
	}

	private void updateLightmap(float par1) {
		final WorldClient var2 = mc.theWorld;

		if (var2 != null) {
			if (CustomColorizer.updateLightmap(var2, torchFlickerX,
					lightmapColors,
					mc.thePlayer.isPotionActive(Potion.nightVision))) {
				lightmapTexture.updateDynamicTexture();
				lightmapUpdateNeeded = false;
				return;
			}

			for (int var3 = 0; var3 < 256; ++var3) {
				final float var4 = var2.getSunBrightness(1.0F) * 0.95F + 0.05F;
				float var5 = var2.provider.lightBrightnessTable[var3 / 16]
						* var4;
				final float var6 = var2.provider.lightBrightnessTable[var3 % 16]
						* (torchFlickerX * 0.1F + 1.5F);

				if (var2.lastLightningBolt > 0) {
					var5 = var2.provider.lightBrightnessTable[var3 / 16];
				}

				final float var7 = var5
						* (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
				final float var8 = var5
						* (var2.getSunBrightness(1.0F) * 0.65F + 0.35F);
				final float var11 = var6 * ((var6 * 0.6F + 0.4F) * 0.6F + 0.4F);
				final float var12 = var6 * (var6 * var6 * 0.6F + 0.4F);
				float var13 = var7 + var6;
				float var14 = var8 + var11;
				float var15 = var5 + var12;
				var13 = var13 * 0.96F + 0.03F;
				var14 = var14 * 0.96F + 0.03F;
				var15 = var15 * 0.96F + 0.03F;
				float var16;

				if (bossColorModifier > 0.0F) {
					var16 = bossColorModifierPrev
							+ (bossColorModifier - bossColorModifierPrev)
							* par1;
					var13 = var13 * (1.0F - var16) + var13 * 0.7F * var16;
					var14 = var14 * (1.0F - var16) + var14 * 0.6F * var16;
					var15 = var15 * (1.0F - var16) + var15 * 0.6F * var16;
				}

				if (var2.provider.dimensionId == 1) {
					var13 = 0.22F + var6 * 0.75F;
					var14 = 0.28F + var11 * 0.75F;
					var15 = 0.25F + var12 * 0.75F;
				}

				float var17;

				if (mc.thePlayer.isPotionActive(Potion.nightVision)) {
					var16 = getNightVisionBrightness(mc.thePlayer, par1);
					var17 = 1.0F / var13;

					if (var17 > 1.0F / var14) {
						var17 = 1.0F / var14;
					}

					if (var17 > 1.0F / var15) {
						var17 = 1.0F / var15;
					}

					var13 = var13 * (1.0F - var16) + var13 * var17 * var16;
					var14 = var14 * (1.0F - var16) + var14 * var17 * var16;
					var15 = var15 * (1.0F - var16) + var15 * var17 * var16;
				}

				if (var13 > 1.0F) {
					var13 = 1.0F;
				}

				if (var14 > 1.0F) {
					var14 = 1.0F;
				}

				if (var15 > 1.0F) {
					var15 = 1.0F;
				}

				var16 = mc.gameSettings.gammaSetting;
				var17 = 1.0F - var13;
				float var18 = 1.0F - var14;
				float var19 = 1.0F - var15;
				var17 = 1.0F - var17 * var17 * var17 * var17;
				var18 = 1.0F - var18 * var18 * var18 * var18;
				var19 = 1.0F - var19 * var19 * var19 * var19;
				var13 = var13 * (1.0F - var16) + var17 * var16;
				var14 = var14 * (1.0F - var16) + var18 * var16;
				var15 = var15 * (1.0F - var16) + var19 * var16;
				var13 = var13 * 0.96F + 0.03F;
				var14 = var14 * 0.96F + 0.03F;
				var15 = var15 * 0.96F + 0.03F;

				if (var13 > 1.0F) {
					var13 = 1.0F;
				}

				if (var14 > 1.0F) {
					var14 = 1.0F;
				}

				if (var15 > 1.0F) {
					var15 = 1.0F;
				}

				if (var13 < 0.0F) {
					var13 = 0.0F;
				}

				if (var14 < 0.0F) {
					var14 = 0.0F;
				}

				if (var15 < 0.0F) {
					var15 = 0.0F;
				}

				final short var20 = 255;
				final int var21 = (int) (var13 * 255.0F);
				final int var22 = (int) (var14 * 255.0F);
				final int var23 = (int) (var15 * 255.0F);
				lightmapColors[var3] = var20 << 24 | var21 << 16 | var22 << 8
						| var23;
			}

			lightmapTexture.updateDynamicTexture();
			lightmapUpdateNeeded = false;
		}
	}

	private void updateMainMenu(GuiMainMenu mainGui) {
		try {
			String e = null;
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			final int day = calendar.get(5);
			final int month = calendar.get(2) + 1;

			if (day == 8 && month == 4) {
				e = "Happy birthday, OptiFine!";
			}

			if (day == 14 && month == 8) {
				e = "Happy birthday, sp614x!";
			}

			if (e == null)
				return;

			final Field[] fs = GuiMainMenu.class.getDeclaredFields();

			for (final Field element : fs) {
				if (element.getType() == String.class) {
					element.setAccessible(true);
					element.set(mainGui, e);
					break;
				}
			}
		} catch (final Throwable var8) {
			;
		}
	}

	/**
	 * Updates the entity renderer
	 */
	public void updateRenderer() {
		if (OpenGlHelper.shadersSupported
				&& ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		}

		updateFovModifierHand();
		updateTorchFlicker();
		fogColor2 = fogColor1;
		thirdPersonDistanceTemp = thirdPersonDistance;
		prevDebugCamYaw = debugCamYaw;
		prevDebugCamPitch = debugCamPitch;
		prevDebugCamFOV = debugCamFOV;
		prevCamRoll = camRoll;
		float var1;
		float var2;

		if (mc.gameSettings.smoothCamera) {
			var1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			var2 = var1 * var1 * var1 * 8.0F;
			smoothCamFilterX = mouseFilterXAxis.smooth(smoothCamYaw,
					0.05F * var2);
			smoothCamFilterY = mouseFilterYAxis.smooth(smoothCamPitch,
					0.05F * var2);
			smoothCamPartialTicks = 0.0F;
			smoothCamYaw = 0.0F;
			smoothCamPitch = 0.0F;
		}

		if (mc.renderViewEntity == null) {
			mc.renderViewEntity = mc.thePlayer;
		}

		var1 = mc.theWorld.getLightBrightness(
				MathHelper.floor_double(mc.renderViewEntity.posX),
				MathHelper.floor_double(mc.renderViewEntity.posY),
				MathHelper.floor_double(mc.renderViewEntity.posZ));
		var2 = mc.gameSettings.renderDistanceChunks / 16.0F;
		final float var3 = var1 * (1.0F - var2) + var2;
		fogColor1 += (var3 - fogColor1) * 0.1F;
		++rendererUpdateCount;
		itemRenderer.updateEquippedItem();
		addRainParticles();
		bossColorModifierPrev = bossColorModifier;

		if (BossStatus.hasColorModifier) {
			bossColorModifier += 0.05F;

			if (bossColorModifier > 1.0F) {
				bossColorModifier = 1.0F;
			}

			BossStatus.hasColorModifier = false;
		} else if (bossColorModifier > 0.0F) {
			bossColorModifier -= 0.0125F;
		}
	}

	public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_) {
		if (OpenGlHelper.shadersSupported && theShaderGroup != null) {
			theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
		}
	}

	/**
	 * Recompute a random value that is applied to block color in
	 * updateLightmap()
	 */
	private void updateTorchFlicker() {
		torchFlickerDX = (float) (torchFlickerDX + (Math.random() - Math
				.random()) * Math.random() * Math.random());
		torchFlickerDY = (float) (torchFlickerDY + (Math.random() - Math
				.random()) * Math.random() * Math.random());
		torchFlickerDX = (float) (torchFlickerDX * 0.9D);
		torchFlickerDY = (float) (torchFlickerDY * 0.9D);
		torchFlickerX += (torchFlickerDX - torchFlickerX) * 1.0F;
		torchFlickerY += (torchFlickerDY - torchFlickerY) * 1.0F;
		lightmapUpdateNeeded = true;
	}

	private void waitForServerThread() {
		serverWaitTimeCurrent = 0;

		if (!Config.isSmoothWorld()) {
			lastServerTime = 0L;
			lastServerTicks = 0;
		} else if (mc.getIntegratedServer() != null) {
			final IntegratedServer srv = mc.getIntegratedServer();
			final boolean paused = mc.func_147113_T();

			if (!paused && !(mc.currentScreen instanceof GuiDownloadTerrain)) {
				if (serverWaitTime > 0) {
					Config.sleep(serverWaitTime);
					serverWaitTimeCurrent = serverWaitTime;
				}

				final long timeNow = System.nanoTime() / 1000000L;

				if (lastServerTime != 0L && lastServerTicks != 0) {
					long timeDiff = timeNow - lastServerTime;

					if (timeDiff < 0L) {
						lastServerTime = timeNow;
						timeDiff = 0L;
					}

					if (timeDiff >= 50L) {
						lastServerTime = timeNow;
						final int ticks = srv.getTickCounter();
						int tickDiff = ticks - lastServerTicks;

						if (tickDiff < 0) {
							lastServerTicks = ticks;
							tickDiff = 0;
						}

						if (tickDiff < 1 && serverWaitTime < 100) {
							serverWaitTime += 2;
						}

						if (tickDiff > 1 && serverWaitTime > 0) {
							--serverWaitTime;
						}

						lastServerTicks = ticks;
					}
				} else {
					lastServerTime = timeNow;
					lastServerTicks = srv.getTickCounter();
				}
			} else {
				if (mc.currentScreen instanceof GuiDownloadTerrain) {
					Config.sleep(20L);
				}

				lastServerTime = 0L;
				lastServerTicks = 0;
			}
		}
	}
}
