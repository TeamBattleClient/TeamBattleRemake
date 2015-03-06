package net.minecraft.client.renderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFireworkSparkFX;
import net.minecraft.client.particle.EntityFishWakeFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntityFootStepFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntityHugeExplodeFX;
import net.minecraft.client.particle.EntityLargeExplodeFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntitySnowShovelFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntitySuspendFX;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.util.RenderDistanceSorter;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.profiler.Profiler;
import net.minecraft.src.CompactArrayList;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.CustomSky;
import net.minecraft.src.EntitySorterFast;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.src.WrDisplayListAllocator;
import net.minecraft.src.WrUpdates;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

public class RenderGlobal implements IWorldAccess {
	private static AxisAlignedBB AABB_INFINITE = AxisAlignedBB.getBoundingBox(
			Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
			Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
			Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	private static final ResourceLocation locationCloudsPng = new ResourceLocation(
			"textures/environment/clouds.png");
	private static final ResourceLocation locationEndSkyPng = new ResourceLocation(
			"textures/environment/end_sky.png");
	private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation(
			"textures/environment/moon_phases.png");
	private static final ResourceLocation locationSunPng = new ResourceLocation(
			"textures/environment/sun.png");
	private static final Logger logger = LogManager.getLogger();

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	public static void drawOutlinedBoundingBox(AxisAlignedBB p_147590_0_,
			int p_147590_1_) {
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawing(3);

		if (p_147590_1_ != -1) {
			var2.setColorOpaque_I(p_147590_1_);
		}

		var2.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
		var2.draw();
		var2.startDrawing(3);

		if (p_147590_1_ != -1) {
			var2.setColorOpaque_I(p_147590_1_);
		}

		var2.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
		var2.draw();
		var2.startDrawing(1);

		if (p_147590_1_ != -1) {
			var2.setColorOpaque_I(p_147590_1_);
		}

		var2.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
		var2.addVertex(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
		var2.draw();
	}

	/** All render lists (fixed length 4) */
	private final RenderList[] allRenderLists = new RenderList[] {
			new RenderList(), new RenderList(), new RenderList(),
			new RenderList() };
	private double cloudPlayerX = 0.0D;
	private double cloudPlayerY = 0.0D;
	private double cloudPlayerZ = 0.0D;
	/**
	 * counts the cloud render updates. Used with mod to stagger some updates
	 */
	private int cloudTickCounter;
	private int cloudTickCounterGlList = -99999;
	/** Count entities hidden */
	private int countEntitiesHidden;

	/** Count entities rendered */
	private int countEntitiesRendered;

	/** Count entities total */
	private int countEntitiesTotal;
	private int countSortedWorldRenderers = 0;

	/**
	 * Stores blocks currently being broken. Key is entity ID of the thing doing
	 * the breaking. Value is a DestroyBlockProgress
	 */
	public final Map damagedBlocks = new HashMap();

	private IIcon[] destroyBlockIcons;

	public WrDisplayListAllocator displayListAllocator = new WrDisplayListAllocator();

	private final int displayListEntities;

	private boolean displayListEntitiesDirty;

	private int effectivePreloadedChunks = 0;

	/**
	 * The offset used to determine if a renderer is one of the sixteenth that
	 * are being updated this frame
	 */
	int frustumCheckOffset;

	private final IntBuffer glListBuffer = BufferUtils.createIntBuffer(65536);

	private int glListClouds = -1;

	/** OpenGL occlusion query base */
	private IntBuffer glOcclusionQueryBase;

	/** OpenGL render lists base */
	public int glRenderListBase;

	/** List of OpenGL lists for the current render pass */
	private final List glRenderLists = new ArrayList();

	/** OpenGL sky list */
	private final int glSkyList;
	/** OpenGL sky list 2 */
	private final int glSkyList2;
	private boolean isFancyGlListClouds = false;
	private long lastActionTime = System.currentTimeMillis();
	private long lastMovedTime = System.currentTimeMillis();
	private final Map mapSoundPositions = Maps.newHashMap();

	/** Maximum block X */
	private int maxBlockX;

	/** Maximum block Y */
	private int maxBlockY;

	/** Maximum block Z */
	private int maxBlockZ;

	/** A reference to the Minecraft object. */
	public Minecraft mc;

	/** Minimum block X */
	private int minBlockX;

	/** Minimum block Y */
	private int minBlockY;

	/** Minimum block Z */
	private int minBlockZ;

	/** Is occlusion testing enabled */
	private final boolean occlusionEnabled;

	/** Occlusion query result */
	IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);

	int prevChunkSortX = -999;

	int prevChunkSortY = -999;

	int prevChunkSortZ = -999;

	double prevRenderSortX = -9999.0D;

	double prevRenderSortY = -9999.0D;

	double prevRenderSortZ = -9999.0D;

	double prevReposX;
	double prevReposY;
	double prevReposZ;
	/**
	 * Previous x position when the renderers were sorted. (Once the distance
	 * moves more than 4 units they will be resorted)
	 */
	double prevSortX = -9999.0D;
	/**
	 * Previous y position when the renderers were sorted. (Once the distance
	 * moves more than 4 units they will be resorted)
	 */
	double prevSortY = -9999.0D;
	/**
	 * Previous Z position when the renderers were sorted. (Once the distance
	 * moves more than 4 units they will be resorted)
	 */
	double prevSortZ = -9999.0D;
	public RenderBlocks renderBlocksRg;

	private int renderChunksDeep;

	private int renderChunksTall;
	private int renderChunksWide;
	private int renderDistanceChunks = -1;
	public Entity renderedEntity;
	/** The RenderEngine instance used by RenderGlobal */
	public final TextureManager renderEngine;
	/** Render entities startup counter (init value=2) */
	private int renderEntitiesStartupCounter = 2;
	/** How many renderers are being clipped by the frustrum this frame */
	private int renderersBeingClipped;
	/** How many renderers are being occluded this frame */
	private int renderersBeingOccluded;
	/** How many renderers are actually being rendered this frame */
	private int renderersBeingRendered;
	/** How many renderers are loaded this frame that try to be rendered */
	private int renderersLoaded;
	/**
	 * How many renderers are skipping rendering due to not having a render pass
	 * this frame
	 */
	private int renderersSkippingRenderPass;
	public EntityLivingBase renderViewEntity;
	private WorldRenderer[] sortedWorldRenderers;
	/** The star GL Call list */
	private final int starGLCallList;
	public WorldClient theWorld;
	public List tileEntities = new ArrayList();
	private int vertexResortCounter = 30;
	private WorldRenderer[] worldRenderers;
	/** World renderers check index */
	private int worldRenderersCheckIndex;

	public CompactArrayList worldRenderersToUpdate = new CompactArrayList(100,
			0.8F);

	public RenderGlobal(Minecraft par1Minecraft) {
		glListClouds = GLAllocation.generateDisplayLists(1);
		mc = par1Minecraft;
		renderEngine = par1Minecraft.getTextureManager();
		final byte maxChunkDim = 65;
		final byte maxChunkHeight = 16;
		final int countWorldRenderers = maxChunkDim * maxChunkDim
				* maxChunkHeight;
		final int countStandardRenderLists = countWorldRenderers * 3;
		glRenderListBase = GLAllocation
				.generateDisplayLists(countStandardRenderLists);
		displayListEntitiesDirty = false;
		displayListEntities = GLAllocation.generateDisplayLists(1);
		occlusionEnabled = OpenGlCapsChecker.checkARBOcclusion();

		if (occlusionEnabled) {
			occlusionResult.clear();
			glOcclusionQueryBase = GLAllocation
					.createDirectIntBuffer(maxChunkDim * maxChunkDim
							* maxChunkHeight);
			glOcclusionQueryBase.clear();
			glOcclusionQueryBase.position(0);
			glOcclusionQueryBase.limit(maxChunkDim * maxChunkDim
					* maxChunkHeight);
			ARBOcclusionQuery.glGenQueriesARB(glOcclusionQueryBase);
		}

		starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(starGLCallList, GL11.GL_COMPILE);
		renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		final Tessellator var4 = Tessellator.instance;
		glSkyList = starGLCallList + 1;
		GL11.glNewList(glSkyList, GL11.GL_COMPILE);
		final byte var6 = 64;
		final int var7 = 256 / var6 + 2;
		float var5 = 16.0F;
		int var8;
		int var9;

		for (var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
			for (var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
				var4.startDrawingQuads();
				var4.addVertex(var8 + 0, var5, var9 + 0);
				var4.addVertex(var8 + var6, var5, var9 + 0);
				var4.addVertex(var8 + var6, var5, var9 + var6);
				var4.addVertex(var8 + 0, var5, var9 + var6);
				var4.draw();
			}
		}

		GL11.glEndList();
		glSkyList2 = starGLCallList + 2;
		GL11.glNewList(glSkyList2, GL11.GL_COMPILE);
		var5 = -16.0F;
		var4.startDrawingQuads();

		for (var8 = -var6 * var7; var8 <= var6 * var7; var8 += var6) {
			for (var9 = -var6 * var7; var9 <= var6 * var7; var9 += var6) {
				var4.addVertex(var8 + var6, var5, var9 + 0);
				var4.addVertex(var8 + 0, var5, var9 + 0);
				var4.addVertex(var8 + 0, var5, var9 + var6);
				var4.addVertex(var8 + var6, var5, var9 + var6);
			}
		}

		var4.draw();
		GL11.glEndList();
	}

	public void addToSortedWorldRenderers(WorldRenderer wr) {
		if (!wr.inSortedWorldRenderers) {
			int pos = countSortedWorldRenderers;
			wr.sortDistanceToEntitySquared = wr
					.distanceToEntitySquared(renderViewEntity);
			final float distSq = wr.sortDistanceToEntitySquared;
			int countGreater;

			if (countSortedWorldRenderers > 0) {
				countGreater = 0;
				int high = countSortedWorldRenderers - 1;
				int mid = (countGreater + high) / 2;

				while (countGreater <= high) {
					mid = (countGreater + high) / 2;
					final WorldRenderer wrMid = sortedWorldRenderers[mid];

					if (distSq < wrMid.sortDistanceToEntitySquared) {
						high = mid - 1;
					} else {
						countGreater = mid + 1;
					}
				}

				if (countGreater > mid) {
					pos = mid + 1;
				} else {
					pos = mid;
				}
			}

			countGreater = countSortedWorldRenderers - pos;

			if (countGreater > 0) {
				System.arraycopy(sortedWorldRenderers, pos,
						sortedWorldRenderers, pos + 1, countGreater);
			}

			sortedWorldRenderers[pos] = wr;
			wr.inSortedWorldRenderers = true;
			++countSortedWorldRenderers;
		}
	}

	@Override
	public void broadcastSound(int par1, int par2, int par3, int par4, int par5) {
		switch (par1) {
		case 1013:
		case 1018:
			if (mc.renderViewEntity != null) {
				final double var7 = par2 - mc.renderViewEntity.posX;
				final double var9 = par3 - mc.renderViewEntity.posY;
				final double var11 = par4 - mc.renderViewEntity.posZ;
				final double var13 = Math.sqrt(var7 * var7 + var9 * var9
						+ var11 * var11);
				double var15 = mc.renderViewEntity.posX;
				double var17 = mc.renderViewEntity.posY;
				double var19 = mc.renderViewEntity.posZ;

				if (var13 > 0.0D) {
					var15 += var7 / var13 * 2.0D;
					var17 += var9 / var13 * 2.0D;
					var19 += var11 / var13 * 2.0D;
				}

				if (par1 == 1013) {
					theWorld.playSound(var15, var17, var19, "mob.wither.spawn",
							1.0F, 1.0F, false);
				} else if (par1 == 1018) {
					theWorld.playSound(var15, var17, var19,
							"mob.enderdragon.end", 5.0F, 1.0F, false);
				}
			}

		default:
		}
	}

	private void checkOcclusionQueryResult(int startIndex, int endIndex,
			double px, double py, double pz) {
		for (int k = startIndex; k < endIndex; ++k) {
			final WorldRenderer wr = sortedWorldRenderers[k];

			if (wr.isWaitingOnOcclusionQuery) {
				occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(wr.glOcclusionQuery,
						ARBOcclusionQuery.GL_QUERY_RESULT_AVAILABLE_ARB,
						occlusionResult);

				if (occlusionResult.get(0) != 0) {
					wr.isWaitingOnOcclusionQuery = false;
					occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(wr.glOcclusionQuery,
							ARBOcclusionQuery.GL_QUERY_RESULT_ARB,
							occlusionResult);

					if (!wr.isUpdating && !wr.needsBoxUpdate) {
						final boolean wasVisible = wr.isVisible;
						wr.isVisible = occlusionResult.get(0) > 0;

						if (wasVisible && wr.isVisible) {
							wr.isVisibleFromPosition = true;
							wr.visibleFromX = px;
							wr.visibleFromY = py;
							wr.visibleFromZ = pz;
						}
					} else {
						wr.isVisible = true;
					}
				}
			}
		}
	}

	/**
	 * Checks all renderers that previously weren't in the frustum and 1/16th of
	 * those that previously were in the frustum for frustum clipping Args:
	 * frustum, partialTickTime
	 */
	public void clipRenderersByFrustum(ICamera par1ICamera, float par2) {
		for (int var3 = 0; var3 < countSortedWorldRenderers; ++var3) {
			final WorldRenderer wr = sortedWorldRenderers[var3];

			if (!wr.skipAllRenderPasses()) {
				wr.updateInFrustum(par1ICamera);
			}
		}
	}

	/**
	 * Deletes all display lists
	 */
	public void deleteAllDisplayLists() {
		GLAllocation.deleteDisplayLists(glRenderListBase);
		displayListAllocator.deleteDisplayLists();
	}

	/**
	 * Starts (or continues) destroying a block with given ID at the given
	 * coordinates for the given partially destroyed value
	 */
	@Override
	public void destroyBlockPartially(int p_147587_1_, int p_147587_2_,
			int p_147587_3_, int p_147587_4_, int p_147587_5_) {
		if (p_147587_5_ >= 0 && p_147587_5_ < 10) {
			DestroyBlockProgress var6 = (DestroyBlockProgress) damagedBlocks
					.get(Integer.valueOf(p_147587_1_));

			if (var6 == null || var6.getPartialBlockX() != p_147587_2_
					|| var6.getPartialBlockY() != p_147587_3_
					|| var6.getPartialBlockZ() != p_147587_4_) {
				var6 = new DestroyBlockProgress(p_147587_1_, p_147587_2_,
						p_147587_3_, p_147587_4_);
				damagedBlocks.put(Integer.valueOf(p_147587_1_), var6);
			}

			var6.setPartialBlockDamage(p_147587_5_);
			var6.setCloudUpdateTick(cloudTickCounter);
		} else {
			damagedBlocks.remove(Integer.valueOf(p_147587_1_));
		}
	}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public EntityFX doSpawnParticle(String par1Str, double par2, double par4,
			double par6, double par8, double par10, double par12) {
		if (mc != null && mc.renderViewEntity != null
				&& mc.effectRenderer != null) {
			int var14 = mc.gameSettings.particleSetting;

			if (var14 == 1 && theWorld.rand.nextInt(3) == 0) {
				var14 = 2;
			}

			final double var15 = mc.renderViewEntity.posX - par2;
			final double var17 = mc.renderViewEntity.posY - par4;
			final double var19 = mc.renderViewEntity.posZ - par6;
			Object var21 = null;

			if (par1Str.equals("hugeexplosion")) {
				if (Config.isAnimatedExplosion()) {
					mc.effectRenderer
							.addEffect((EntityFX) (var21 = new EntityHugeExplodeFX(
									theWorld, par2, par4, par6, par8, par10,
									par12)));
				}
			} else if (par1Str.equals("largeexplode")) {
				if (Config.isAnimatedExplosion()) {
					mc.effectRenderer
							.addEffect((EntityFX) (var21 = new EntityLargeExplodeFX(
									renderEngine, theWorld, par2, par4, par6,
									par8, par10, par12)));
				}
			} else if (par1Str.equals("fireworksSpark")) {
				mc.effectRenderer
						.addEffect((EntityFX) (var21 = new EntityFireworkSparkFX(
								theWorld, par2, par4, par6, par8, par10, par12,
								mc.effectRenderer)));
			}

			if (var21 != null)
				return (EntityFX) var21;
			else {
				double var22 = 16.0D;
				if (par1Str.equals("crit")) {
					var22 = 196.0D;
				}

				if (var15 * var15 + var17 * var17 + var19 * var19 > var22
						* var22)
					return null;
				else if (var14 > 1)
					return null;
				else {
					if (par1Str.equals("bubble")) {
						var21 = new EntityBubbleFX(theWorld, par2, par4, par6,
								par8, par10, par12);
						CustomColorizer.updateWaterFX((EntityFX) var21,
								theWorld);
					} else if (par1Str.equals("suspended")) {
						if (Config.isWaterParticles()) {
							var21 = new EntitySuspendFX(theWorld, par2, par4,
									par6, par8, par10, par12);
						}
					} else if (par1Str.equals("depthsuspend")) {
						if (Config.isVoidParticles()) {
							var21 = new EntityAuraFX(theWorld, par2, par4,
									par6, par8, par10, par12);
						}
					} else if (par1Str.equals("townaura")) {
						var21 = new EntityAuraFX(theWorld, par2, par4, par6,
								par8, par10, par12);
						CustomColorizer.updateMyceliumFX((EntityFX) var21);
					} else if (par1Str.equals("crit")) {
						var21 = new EntityCritFX(theWorld, par2, par4, par6,
								par8, par10, par12);
					} else if (par1Str.equals("magicCrit")) {
						var21 = new EntityCritFX(theWorld, par2, par4, par6,
								par8, par10, par12);
						((EntityFX) var21).setRBGColorF(
								((EntityFX) var21).getRedColorF() * 0.3F,
								((EntityFX) var21).getGreenColorF() * 0.8F,
								((EntityFX) var21).getBlueColorF());
						((EntityFX) var21).nextTextureIndexX();
					} else if (par1Str.equals("smoke")) {
						if (Config.isAnimatedSmoke()) {
							var21 = new EntitySmokeFX(theWorld, par2, par4,
									par6, par8, par10, par12);
						}
					} else if (par1Str.equals("mobSpell")) {
						if (Config.isPotionParticles()) {
							var21 = new EntitySpellParticleFX(theWorld, par2,
									par4, par6, 0.0D, 0.0D, 0.0D);
							((EntityFX) var21).setRBGColorF((float) par8,
									(float) par10, (float) par12);
						}
					} else if (par1Str.equals("mobSpellAmbient")) {
						if (Config.isPotionParticles()) {
							var21 = new EntitySpellParticleFX(theWorld, par2,
									par4, par6, 0.0D, 0.0D, 0.0D);
							((EntityFX) var21).setAlphaF(0.15F);
							((EntityFX) var21).setRBGColorF((float) par8,
									(float) par10, (float) par12);
						}
					} else if (par1Str.equals("spell")) {
						if (Config.isPotionParticles()) {
							var21 = new EntitySpellParticleFX(theWorld, par2,
									par4, par6, par8, par10, par12);
						}
					} else if (par1Str.equals("instantSpell")) {
						if (Config.isPotionParticles()) {
							var21 = new EntitySpellParticleFX(theWorld, par2,
									par4, par6, par8, par10, par12);
							((EntitySpellParticleFX) var21)
									.setBaseSpellTextureIndex(144);
						}
					} else if (par1Str.equals("witchMagic")) {
						if (Config.isPotionParticles()) {
							var21 = new EntitySpellParticleFX(theWorld, par2,
									par4, par6, par8, par10, par12);
							((EntitySpellParticleFX) var21)
									.setBaseSpellTextureIndex(144);
							final float var26 = theWorld.rand.nextFloat() * 0.5F + 0.35F;
							((EntityFX) var21).setRBGColorF(1.0F * var26,
									0.0F * var26, 1.0F * var26);
						}
					} else if (par1Str.equals("note")) {
						var21 = new EntityNoteFX(theWorld, par2, par4, par6,
								par8, par10, par12);
					} else if (par1Str.equals("portal")) {
						if (Config.isPortalParticles()) {
							var21 = new EntityPortalFX(theWorld, par2, par4,
									par6, par8, par10, par12);
							CustomColorizer.updatePortalFX((EntityFX) var21);
						}
					} else if (par1Str.equals("enchantmenttable")) {
						var21 = new EntityEnchantmentTableParticleFX(theWorld,
								par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("explode")) {
						if (Config.isAnimatedExplosion()) {
							var21 = new EntityExplodeFX(theWorld, par2, par4,
									par6, par8, par10, par12);
						}
					} else if (par1Str.equals("flame")) {
						if (Config.isAnimatedFlame()) {
							var21 = new EntityFlameFX(theWorld, par2, par4,
									par6, par8, par10, par12);
						}
					} else if (par1Str.equals("lava")) {
						var21 = new EntityLavaFX(theWorld, par2, par4, par6);
					} else if (par1Str.equals("footstep")) {
						var21 = new EntityFootStepFX(renderEngine, theWorld,
								par2, par4, par6);
					} else if (par1Str.equals("splash")) {
						var21 = new EntitySplashFX(theWorld, par2, par4, par6,
								par8, par10, par12);
						CustomColorizer.updateWaterFX((EntityFX) var21,
								theWorld);
					} else if (par1Str.equals("wake")) {
						var21 = new EntityFishWakeFX(theWorld, par2, par4,
								par6, par8, par10, par12);
					} else if (par1Str.equals("largesmoke")) {
						if (Config.isAnimatedSmoke()) {
							var21 = new EntitySmokeFX(theWorld, par2, par4,
									par6, par8, par10, par12, 2.5F);
						}
					} else if (par1Str.equals("cloud")) {
						var21 = new EntityCloudFX(theWorld, par2, par4, par6,
								par8, par10, par12);
					} else if (par1Str.equals("reddust")) {
						if (Config.isAnimatedRedstone()) {
							var21 = new EntityReddustFX(theWorld, par2, par4,
									par6, (float) par8, (float) par10,
									(float) par12);
							CustomColorizer.updateReddustFX((EntityFX) var21,
									theWorld, var15, var17, var19);
						}
					} else if (par1Str.equals("snowballpoof")) {
						var21 = new EntityBreakingFX(theWorld, par2, par4,
								par6, Items.snowball);
					} else if (par1Str.equals("dripWater")) {
						if (Config.isDrippingWaterLava()) {
							var21 = new EntityDropParticleFX(theWorld, par2,
									par4, par6, Material.water);
						}
					} else if (par1Str.equals("dripLava")) {
						if (Config.isDrippingWaterLava()) {
							var21 = new EntityDropParticleFX(theWorld, par2,
									par4, par6, Material.lava);
						}
					} else if (par1Str.equals("snowshovel")) {
						var21 = new EntitySnowShovelFX(theWorld, par2, par4,
								par6, par8, par10, par12);
					} else if (par1Str.equals("slime")) {
						var21 = new EntityBreakingFX(theWorld, par2, par4,
								par6, Items.slime_ball);
					} else if (par1Str.equals("heart")) {
						var21 = new EntityHeartFX(theWorld, par2, par4, par6,
								par8, par10, par12);
					} else if (par1Str.equals("angryVillager")) {
						var21 = new EntityHeartFX(theWorld, par2, par4 + 0.5D,
								par6, par8, par10, par12);
						((EntityFX) var21).setParticleTextureIndex(81);
						((EntityFX) var21).setRBGColorF(1.0F, 1.0F, 1.0F);
					} else if (par1Str.equals("happyVillager")) {
						var21 = new EntityAuraFX(theWorld, par2, par4, par6,
								par8, par10, par12);
						((EntityFX) var21).setParticleTextureIndex(82);
						((EntityFX) var21).setRBGColorF(1.0F, 1.0F, 1.0F);
					} else {
						String[] var27;
						int var261;

						if (par1Str.startsWith("iconcrack_")) {
							var27 = par1Str.split("_", 3);
							final int var28 = Integer.parseInt(var27[1]);

							if (var27.length > 2) {
								var261 = Integer.parseInt(var27[2]);
								var21 = new EntityBreakingFX(theWorld, par2,
										par4, par6, par8, par10, par12,
										Item.getItemById(var28), var261);
							} else {
								var21 = new EntityBreakingFX(theWorld, par2,
										par4, par6, par8, par10, par12,
										Item.getItemById(var28), 0);
							}
						} else {
							Block var281;

							if (par1Str.startsWith("blockcrack_")) {
								var27 = par1Str.split("_", 3);
								var281 = Block.getBlockById(Integer
										.parseInt(var27[1]));
								var261 = Integer.parseInt(var27[2]);
								var21 = new EntityDiggingFX(theWorld, par2,
										par4, par6, par8, par10, par12, var281,
										var261).applyRenderColor(var261);
							} else if (par1Str.startsWith("blockdust_")) {
								var27 = par1Str.split("_", 3);
								var281 = Block.getBlockById(Integer
										.parseInt(var27[1]));
								var261 = Integer.parseInt(var27[2]);
								var21 = new EntityBlockDustFX(theWorld, par2,
										par4, par6, par8, par10, par12, var281,
										var261).applyRenderColor(var261);
							}
						}
					}

					if (var21 != null) {
						mc.effectRenderer.addEffect((EntityFX) var21);
					}

					return (EntityFX) var21;
				}
			}
		} else
			return null;
	}

	public void drawBlockDamageTexture(Tessellator par1Tessellator,
			EntityLivingBase par2EntityPlayer, float par3) {
		final double var4 = par2EntityPlayer.lastTickPosX
				+ (par2EntityPlayer.posX - par2EntityPlayer.lastTickPosX)
				* par3;
		final double var6 = par2EntityPlayer.lastTickPosY
				+ (par2EntityPlayer.posY - par2EntityPlayer.lastTickPosY)
				* par3;
		final double var8 = par2EntityPlayer.lastTickPosZ
				+ (par2EntityPlayer.posZ - par2EntityPlayer.lastTickPosZ)
				* par3;

		if (!damagedBlocks.isEmpty()) {
			OpenGlHelper.glBlendFunc(774, 768, 1, 0);
			renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			GL11.glPolygonOffset(-3.0F, -3.0F);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			par1Tessellator.startDrawingQuads();
			par1Tessellator.setTranslation(-var4, -var6, -var8);
			par1Tessellator.disableColor();
			final Iterator var10 = damagedBlocks.values().iterator();

			while (var10.hasNext()) {
				final DestroyBlockProgress var11 = (DestroyBlockProgress) var10
						.next();
				final double var12 = var11.getPartialBlockX() - var4;
				final double var14 = var11.getPartialBlockY() - var6;
				final double var16 = var11.getPartialBlockZ() - var8;

				if (var12 * var12 + var14 * var14 + var16 * var16 > 1024.0D) {
					var10.remove();
				} else {
					final Block var18 = theWorld.getBlock(
							var11.getPartialBlockX(), var11.getPartialBlockY(),
							var11.getPartialBlockZ());

					if (var18.getMaterial() != Material.air) {
						renderBlocksRg
								.renderBlockUsingTexture(var18, var11
										.getPartialBlockX(), var11
										.getPartialBlockY(), var11
										.getPartialBlockZ(),
										destroyBlockIcons[var11
												.getPartialBlockDamage()]);
					}
				}
			}

			par1Tessellator.draw();
			par1Tessellator.setTranslation(0.0D, 0.0D, 0.0D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glPolygonOffset(0.0F, 0.0F);
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
		}
	}

	public void drawBlockDamageTexture(Tessellator par1Tessellator,
			EntityPlayer par2EntityPlayer, float par3) {
		this.drawBlockDamageTexture(par1Tessellator, par2EntityPlayer, par3);
	}

	/**
	 * Draws the selection box for the player. Args: entityPlayer, rayTraceHit,
	 * i, itemStack, partialTickTime
	 */
	public void drawSelectionBox(EntityPlayer par1EntityPlayer,
			MovingObjectPosition par2MovingObjectPosition, int par3, float par4) {
		if (par3 == 0
				&& par2MovingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			final float var5 = 0.002F;
			final Block var6 = theWorld.getBlock(
					par2MovingObjectPosition.blockX,
					par2MovingObjectPosition.blockY,
					par2MovingObjectPosition.blockZ);

			if (var6.getMaterial() != Material.air) {
				var6.setBlockBoundsBasedOnState(theWorld,
						par2MovingObjectPosition.blockX,
						par2MovingObjectPosition.blockY,
						par2MovingObjectPosition.blockZ);
				final double var7 = par1EntityPlayer.lastTickPosX
						+ (par1EntityPlayer.posX - par1EntityPlayer.lastTickPosX)
						* par4;
				final double var9 = par1EntityPlayer.lastTickPosY
						+ (par1EntityPlayer.posY - par1EntityPlayer.lastTickPosY)
						* par4;
				final double var11 = par1EntityPlayer.lastTickPosZ
						+ (par1EntityPlayer.posZ - par1EntityPlayer.lastTickPosZ)
						* par4;
				drawOutlinedBoundingBox(
						var6.getSelectedBoundingBoxFromPool(theWorld,
								par2MovingObjectPosition.blockX,
								par2MovingObjectPosition.blockY,
								par2MovingObjectPosition.blockZ)
								.expand(var5, var5, var5)
								.getOffsetBoundingBox(-var7, -var9, -var11), -1);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	/**
	 * Gets the entities info for use on the Debug screen
	 */
	public String getDebugInfoEntities() {
		return "E: "
				+ countEntitiesRendered
				+ "/"
				+ countEntitiesTotal
				+ ". B: "
				+ countEntitiesHidden
				+ ", I: "
				+ (countEntitiesTotal - countEntitiesHidden - countEntitiesRendered)
				+ ", " + Config.getVersion();
	}

	/**
	 * Gets the render info for use on the Debug screen
	 */
	public String getDebugInfoRenders() {
		return "C: " + renderersBeingRendered + "/" + renderersLoaded + ". F: "
				+ renderersBeingClipped + ", O: " + renderersBeingOccluded
				+ ", E: " + renderersSkippingRenderPass;
	}

	public AxisAlignedBB getTileEntityBoundingBox(TileEntity te) {
		if (!te.hasWorldObj())
			return AABB_INFINITE;
		else {
			final Block blockType = te.getBlockType();

			if (blockType == Blocks.enchanting_table)
				return AxisAlignedBB.getBoundingBox(te.field_145851_c,
						te.field_145848_d, te.field_145849_e,
						te.field_145851_c + 1, te.field_145848_d + 1,
						te.field_145849_e + 1);
			else if (blockType != Blocks.chest
					&& blockType != Blocks.trapped_chest) {
				AxisAlignedBB blockAabb;

				if (Reflector.ForgeTileEntity_getRenderBoundingBox.exists()) {
					blockAabb = (AxisAlignedBB) Reflector.call(te,
							Reflector.ForgeTileEntity_getRenderBoundingBox,
							new Object[0]);

					if (blockAabb != null)
						return blockAabb;
				}

				if (blockType != null && blockType != Blocks.beacon) {
					blockAabb = blockType.getCollisionBoundingBoxFromPool(
							te.getWorldObj(), te.field_145851_c,
							te.field_145848_d, te.field_145849_e);

					if (blockAabb != null)
						return blockAabb;
				}

				return AABB_INFINITE;
			} else
				return AxisAlignedBB.getBoundingBox(te.field_145851_c - 1,
						te.field_145848_d, te.field_145849_e - 1,
						te.field_145851_c + 2, te.field_145848_d + 2,
						te.field_145849_e + 2);
		}
	}

	/**
	 * Checks if the given position is to be rendered with cloud fog
	 */
	public boolean hasCloudFog(double par1, double par3, double par5, float par7) {
		return false;
	}

	public boolean isActing() {
		final boolean acting = isActingNow();

		if (acting) {
			lastActionTime = System.currentTimeMillis();
			return true;
		} else
			return System.currentTimeMillis() - lastActionTime < 500L;
	}

	public boolean isActingNow() {
		return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
	}

	public boolean isMoving(EntityLivingBase entityliving) {
		final boolean moving = isMovingNow(entityliving);

		if (moving) {
			lastMovedTime = System.currentTimeMillis();
			return true;
		} else
			return System.currentTimeMillis() - lastMovedTime < 2000L;
	}

	private boolean isMovingNow(EntityLivingBase entityliving) {
		final double maxDiff = 0.001D;
		return entityliving.isSneaking() ? true
				: entityliving.prevSwingProgress > maxDiff ? true
						: mc.mouseHelper.deltaX != 0 ? true
								: mc.mouseHelper.deltaY != 0 ? true
										: Math.abs(entityliving.posX
												- entityliving.prevPosX) > maxDiff ? true
												: Math.abs(entityliving.posY
														- entityliving.prevPosY) > maxDiff ? true
														: Math.abs(entityliving.posZ
																- entityliving.prevPosZ) > maxDiff;
	}

	/**
	 * Loads all the renderers and sets up the basic settings usage
	 */
	public void loadRenderers() {
		if (theWorld != null) {
			Blocks.leaves.func_150122_b(Config.isTreesFancy());
			Blocks.leaves2.func_150122_b(Config.isTreesFancy());
			renderDistanceChunks = mc.gameSettings.renderDistanceChunks;
			WrUpdates.clearAllUpdates();
			int numChunks;

			if (worldRenderers != null) {
				for (numChunks = 0; numChunks < worldRenderers.length; ++numChunks) {
					worldRenderers[numChunks].stopRendering();
				}
			}

			numChunks = mc.gameSettings.renderDistanceChunks;
			final byte numChunksFar = 16;

			if (Config.isLoadChunksFar() && numChunks < numChunksFar) {
				numChunks = numChunksFar;
			}

			final int maxPreloadedChunks = Config.limit(numChunksFar
					- numChunks, 0, 8);
			effectivePreloadedChunks = Config.limit(
					Config.getPreloadedChunks(), 0, maxPreloadedChunks);
			numChunks += effectivePreloadedChunks;
			final byte limit = 32;

			if (numChunks > limit) {
				numChunks = limit;
			}

			prevReposX = -9999.0D;
			prevReposY = -9999.0D;
			prevReposZ = -9999.0D;
			final int var1 = numChunks * 2 + 1;
			renderChunksWide = var1;
			renderChunksTall = 16;
			renderChunksDeep = var1;
			worldRenderers = new WorldRenderer[renderChunksWide
					* renderChunksTall * renderChunksDeep];
			sortedWorldRenderers = new WorldRenderer[renderChunksWide
					* renderChunksTall * renderChunksDeep];
			countSortedWorldRenderers = 0;
			displayListAllocator.resetAllocatedLists();
			int var2 = 0;
			int var3 = 0;
			minBlockX = 0;
			minBlockY = 0;
			minBlockZ = 0;
			maxBlockX = renderChunksWide;
			maxBlockY = renderChunksTall;
			maxBlockZ = renderChunksDeep;
			int var10;

			for (var10 = 0; var10 < worldRenderersToUpdate.size(); ++var10) {
				final WorldRenderer esf = (WorldRenderer) worldRenderersToUpdate
						.get(var10);

				if (esf != null) {
					esf.needsUpdate = false;
				}
			}

			worldRenderersToUpdate.clear();
			tileEntities.clear();
			onStaticEntitiesChanged();

			for (var10 = 0; var10 < renderChunksWide; ++var10) {
				for (int var14 = 0; var14 < renderChunksTall; ++var14) {
					for (int cz = 0; cz < renderChunksDeep; ++cz) {
						final int wri = (cz * renderChunksTall + var14)
								* renderChunksWide + var10;
						worldRenderers[wri] = WrUpdates.makeWorldRenderer(
								theWorld, tileEntities, var10 * 16, var14 * 16,
								cz * 16, glRenderListBase + var2);

						if (occlusionEnabled) {
							worldRenderers[wri].glOcclusionQuery = glOcclusionQueryBase
									.get(var3);
						}

						worldRenderers[wri].isWaitingOnOcclusionQuery = false;
						worldRenderers[wri].isVisible = true;
						worldRenderers[wri].isInFrustum = false;
						worldRenderers[wri].chunkIndex = var3++;

						if (theWorld.blockExists(var10 << 4, 0, cz << 4)) {
							worldRenderers[wri].markDirty();
							worldRenderersToUpdate.add(worldRenderers[wri]);
						}

						var2 += 3;
					}
				}
			}

			if (theWorld != null) {
				Object var13 = mc.renderViewEntity;

				if (var13 == null) {
					var13 = mc.thePlayer;
				}

				if (var13 != null) {
					markRenderersForNewPosition(
							MathHelper
									.floor_double(((EntityLivingBase) var13).posX),
							MathHelper
									.floor_double(((EntityLivingBase) var13).posY),
							MathHelper
									.floor_double(((EntityLivingBase) var13).posZ));
					final EntitySorterFast var15 = new EntitySorterFast(
							(Entity) var13);
					var15.prepareToSort(sortedWorldRenderers,
							countSortedWorldRenderers);
					Arrays.sort(sortedWorldRenderers, 0,
							countSortedWorldRenderers, var15);
				}
			}

			renderEntitiesStartupCounter = 2;
		}
	}

	/**
	 * On the client, re-renders this block. On the server, does nothing. Used
	 * for lighting updates.
	 */
	@Override
	public void markBlockForRenderUpdate(int p_147588_1_, int p_147588_2_,
			int p_147588_3_) {
		markBlocksForUpdate(p_147588_1_ - 1, p_147588_2_ - 1, p_147588_3_ - 1,
				p_147588_1_ + 1, p_147588_2_ + 1, p_147588_3_ + 1);
	}

	/**
	 * On the client, re-renders the block. On the server, sends the block to
	 * the client (which will re-render it), including the tile entity
	 * description packet if applicable. Args: x, y, z
	 */
	@Override
	public void markBlockForUpdate(int p_147586_1_, int p_147586_2_,
			int p_147586_3_) {
		markBlocksForUpdate(p_147586_1_ - 1, p_147586_2_ - 1, p_147586_3_ - 1,
				p_147586_1_ + 1, p_147586_2_ + 1, p_147586_3_ + 1);
	}

	/**
	 * On the client, re-renders all blocks in this range, inclusive. On the
	 * server, does nothing. Args: min x, min y, min z, max x, max y, max z
	 */
	@Override
	public void markBlockRangeForRenderUpdate(int p_147585_1_, int p_147585_2_,
			int p_147585_3_, int p_147585_4_, int p_147585_5_, int p_147585_6_) {
		markBlocksForUpdate(p_147585_1_ - 1, p_147585_2_ - 1, p_147585_3_ - 1,
				p_147585_4_ + 1, p_147585_5_ + 1, p_147585_6_ + 1);
	}

	/**
	 * Marks the blocks in the given range for update
	 */
	public void markBlocksForUpdate(int par1, int par2, int par3, int par4,
			int par5, int par6) {
		final int var7 = MathHelper.bucketInt(par1, 16);
		final int var8 = MathHelper.bucketInt(par2, 16);
		final int var9 = MathHelper.bucketInt(par3, 16);
		final int var10 = MathHelper.bucketInt(par4, 16);
		final int var11 = MathHelper.bucketInt(par5, 16);
		final int var12 = MathHelper.bucketInt(par6, 16);

		for (int var13 = var7; var13 <= var10; ++var13) {
			int var14 = var13 % renderChunksWide;

			if (var14 < 0) {
				var14 += renderChunksWide;
			}

			for (int var15 = var8; var15 <= var11; ++var15) {
				int var16 = var15 % renderChunksTall;

				if (var16 < 0) {
					var16 += renderChunksTall;
				}

				for (int var17 = var9; var17 <= var12; ++var17) {
					int var18 = var17 % renderChunksDeep;

					if (var18 < 0) {
						var18 += renderChunksDeep;
					}

					final int var19 = (var18 * renderChunksTall + var16)
							* renderChunksWide + var14;
					final WorldRenderer var20 = worldRenderers[var19];

					if (var20 != null && !var20.needsUpdate) {
						worldRenderersToUpdate.add(var20);
						var20.markDirty();
					}
				}
			}
		}
	}

	/**
	 * Goes through all the renderers setting new positions on them and those
	 * that have their position changed are adding to be updated
	 */
	private void markRenderersForNewPosition(int x, int y, int z) {
		x -= 8;
		y -= 8;
		z -= 8;
		minBlockX = Integer.MAX_VALUE;
		minBlockY = Integer.MAX_VALUE;
		minBlockZ = Integer.MAX_VALUE;
		maxBlockX = Integer.MIN_VALUE;
		maxBlockY = Integer.MIN_VALUE;
		maxBlockZ = Integer.MIN_VALUE;
		final int blocksWide = renderChunksWide * 16;
		final int blocksWide2 = blocksWide / 2;

		for (int ix = 0; ix < renderChunksWide; ++ix) {
			int blockX = ix * 16;
			int blockXAbs = blockX + blocksWide2 - x;

			if (blockXAbs < 0) {
				blockXAbs -= blocksWide - 1;
			}

			blockXAbs /= blocksWide;
			blockX -= blockXAbs * blocksWide;

			if (blockX < minBlockX) {
				minBlockX = blockX;
			}

			if (blockX > maxBlockX) {
				maxBlockX = blockX;
			}

			for (int iz = 0; iz < renderChunksDeep; ++iz) {
				int blockZ = iz * 16;
				int blockZAbs = blockZ + blocksWide2 - z;

				if (blockZAbs < 0) {
					blockZAbs -= blocksWide - 1;
				}

				blockZAbs /= blocksWide;
				blockZ -= blockZAbs * blocksWide;

				if (blockZ < minBlockZ) {
					minBlockZ = blockZ;
				}

				if (blockZ > maxBlockZ) {
					maxBlockZ = blockZ;
				}

				for (int iy = 0; iy < renderChunksTall; ++iy) {
					final int blockY = iy * 16;

					if (blockY < minBlockY) {
						minBlockY = blockY;
					}

					if (blockY > maxBlockY) {
						maxBlockY = blockY;
					}

					final WorldRenderer worldrenderer = worldRenderers[(iz
							* renderChunksTall + iy)
							* renderChunksWide + ix];
					final boolean wasNeedingUpdate = worldrenderer.needsUpdate;
					worldrenderer.setPosition(blockX, blockY, blockZ);

					if (!wasNeedingUpdate && worldrenderer.needsUpdate) {
						worldRenderersToUpdate.add(worldrenderer);
					}
				}
			}
		}
	}

	/**
	 * Called on all IWorldAccesses when an entity is created or loaded. On
	 * client worlds, starts downloading any necessary textures. On server
	 * worlds, adds the entity to the entity tracker.
	 */
	@Override
	public void onEntityCreate(Entity par1Entity) {
		RandomMobs.entityLoaded(par1Entity);
	}

	/**
	 * Called on all IWorldAccesses when an entity is unloaded or destroyed. On
	 * client worlds, releases any downloaded textures. On server worlds,
	 * removes the entity from the entity tracker.
	 */
	@Override
	public void onEntityDestroy(Entity par1Entity) {
	}

	@Override
	public void onStaticEntitiesChanged() {
		displayListEntitiesDirty = true;
	}

	/**
	 * Plays a pre-canned sound effect along with potentially auxiliary
	 * data-driven one-shot behaviour (particles, etc).
	 */
	@Override
	public void playAuxSFX(EntityPlayer par1EntityPlayer, int par2, int par3,
			int par4, int par5, int par6) {
		final Random var7 = theWorld.rand;
		Block var8 = null;
		double var9;
		double var11;
		double var13;
		String var15;
		int var16;
		double var22;
		double var26;
		double var28;
		double var30;
		int var40;
		double var41;
		double var21;

		switch (par2) {
		case 1000:
			theWorld.playSound(par3, par4, par5, "random.click", 1.0F, 1.0F,
					false);
			break;

		case 1001:
			theWorld.playSound(par3, par4, par5, "random.click", 1.0F, 1.2F,
					false);
			break;

		case 1002:
			theWorld.playSound(par3, par4, par5, "random.bow", 1.0F, 1.2F,
					false);
			break;

		case 1003:
			if (Math.random() < 0.5D) {
				theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
						"random.door_open", 1.0F,
						theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			} else {
				theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
						"random.door_close", 1.0F,
						theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			}

			break;

		case 1004:
			theWorld.playSound(par3 + 0.5F, par4 + 0.5F, par5 + 0.5F,
					"random.fizz", 0.5F,
					2.6F + (var7.nextFloat() - var7.nextFloat()) * 0.8F, false);
			break;

		case 1005:
			if (Item.getItemById(par6) instanceof ItemRecord) {
				theWorld.playRecord(
						"records."
								+ ((ItemRecord) Item.getItemById(par6)).field_150929_a,
						par3, par4, par5);
			} else {
				theWorld.playRecord((String) null, par3, par4, par5);
			}

			break;

		case 1007:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.ghast.charge", 10.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1008:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.ghast.fireball", 10.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1009:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.ghast.fireball", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1010:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.zombie.wood", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1011:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.zombie.metal", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1012:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.zombie.woodbreak", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1014:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.wither.shoot", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1015:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.bat.takeoff", 0.05F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1016:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.zombie.infect", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1017:
			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"mob.zombie.unfect", 2.0F,
					(var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
			break;

		case 1020:
			theWorld.playSound(par3 + 0.5F, par4 + 0.5F, par5 + 0.5F,
					"random.anvil_break", 1.0F,
					theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			break;

		case 1021:
			theWorld.playSound(par3 + 0.5F, par4 + 0.5F, par5 + 0.5F,
					"random.anvil_use", 1.0F,
					theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			break;

		case 1022:
			theWorld.playSound(par3 + 0.5F, par4 + 0.5F, par5 + 0.5F,
					"random.anvil_land", 0.3F,
					theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			break;

		case 2000:
			final int var34 = par6 % 3 - 1;
			final int var10 = par6 / 3 % 3 - 1;
			var11 = par3 + var34 * 0.6D + 0.5D;
			var13 = par4 + 0.5D;
			final double var35 = par5 + var10 * 0.6D + 0.5D;

			for (int var43 = 0; var43 < 10; ++var43) {
				final double var44 = var7.nextDouble() * 0.2D + 0.01D;
				final double var45 = var11 + var34 * 0.01D
						+ (var7.nextDouble() - 0.5D) * var10 * 0.5D;
				var22 = var13 + (var7.nextDouble() - 0.5D) * 0.5D;
				var41 = var35 + var10 * 0.01D + (var7.nextDouble() - 0.5D)
						* var34 * 0.5D;
				var26 = var34 * var44 + var7.nextGaussian() * 0.01D;
				var28 = -0.03D + var7.nextGaussian() * 0.01D;
				var30 = var10 * var44 + var7.nextGaussian() * 0.01D;
				spawnParticle("smoke", var45, var22, var41, var26, var28, var30);
			}

			return;

		case 2001:
			var8 = Block.getBlockById(par6 & 4095);

			if (var8.getMaterial() != Material.air) {
				mc.getSoundHandler().playSound(
						new PositionedSoundRecord(new ResourceLocation(
								var8.stepSound.func_150495_a()),
								(var8.stepSound.func_150497_c() + 1.0F) / 2.0F,
								var8.stepSound.func_150494_d() * 0.8F,
								par3 + 0.5F, par4 + 0.5F, par5 + 0.5F));
			}

			mc.effectRenderer.func_147215_a(par3, par4, par5, var8,
					par6 >> 12 & 255);
			break;

		case 2002:
			var9 = par3;
			var11 = par4;
			var13 = par5;
			var15 = "iconcrack_" + Item.getIdFromItem(Items.potionitem) + "_"
					+ par6;

			for (var16 = 0; var16 < 8; ++var16) {
				spawnParticle(var15, var9, var11, var13,
						var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D,
						var7.nextGaussian() * 0.15D);
			}

			var16 = Items.potionitem.getColorFromDamage(par6);
			final float var17 = (var16 >> 16 & 255) / 255.0F;
			final float var18 = (var16 >> 8 & 255) / 255.0F;
			final float var19 = (var16 >> 0 & 255) / 255.0F;
			String var20 = "spell";

			if (Items.potionitem.isEffectInstant(par6)) {
				var20 = "instantSpell";
			}

			for (var40 = 0; var40 < 100; ++var40) {
				var22 = var7.nextDouble() * 4.0D;
				var41 = var7.nextDouble() * Math.PI * 2.0D;
				var26 = Math.cos(var41) * var22;
				var28 = 0.01D + var7.nextDouble() * 0.5D;
				var30 = Math.sin(var41) * var22;
				final EntityFX var46 = doSpawnParticle(var20, var9 + var26
						* 0.1D, var11 + 0.3D, var13 + var30 * 0.1D, var26,
						var28, var30);

				if (var46 != null) {
					final float var33 = 0.75F + var7.nextFloat() * 0.25F;
					var46.setRBGColorF(var17 * var33, var18 * var33, var19
							* var33);
					var46.multiplyVelocity((float) var22);
				}
			}

			theWorld.playSound(par3 + 0.5D, par4 + 0.5D, par5 + 0.5D,
					"game.potion.smash", 1.0F,
					theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
			break;

		case 2003:
			var9 = par3 + 0.5D;
			var11 = par4;
			var13 = par5 + 0.5D;
			var15 = "iconcrack_" + Item.getIdFromItem(Items.ender_eye);

			for (var16 = 0; var16 < 8; ++var16) {
				spawnParticle(var15, var9, var11, var13,
						var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D,
						var7.nextGaussian() * 0.15D);
			}

			for (var21 = 0.0D; var21 < Math.PI * 2D; var21 += 0.15707963267948966D) {
				spawnParticle("portal", var9 + Math.cos(var21) * 5.0D,
						var11 - 0.4D, var13 + Math.sin(var21) * 5.0D,
						Math.cos(var21) * -5.0D, 0.0D, Math.sin(var21) * -5.0D);
				spawnParticle("portal", var9 + Math.cos(var21) * 5.0D,
						var11 - 0.4D, var13 + Math.sin(var21) * 5.0D,
						Math.cos(var21) * -7.0D, 0.0D, Math.sin(var21) * -7.0D);
			}

			return;

		case 2004:
			for (var40 = 0; var40 < 20; ++var40) {
				var22 = par3 + 0.5D + (theWorld.rand.nextFloat() - 0.5D) * 2.0D;
				var41 = par4 + 0.5D + (theWorld.rand.nextFloat() - 0.5D) * 2.0D;
				var26 = par5 + 0.5D + (theWorld.rand.nextFloat() - 0.5D) * 2.0D;
				theWorld.spawnParticle("smoke", var22, var41, var26, 0.0D,
						0.0D, 0.0D);
				theWorld.spawnParticle("flame", var22, var41, var26, 0.0D,
						0.0D, 0.0D);
			}

			return;

		case 2005:
			ItemDye.func_150918_a(theWorld, par3, par4, par5, par6);
			break;

		case 2006:
			var8 = theWorld.getBlock(par3, par4, par5);

			if (var8.getMaterial() != Material.air) {
				var21 = Math.min(0.2F + par6 / 15.0F, 10.0F);

				if (var21 > 2.5D) {
					var21 = 2.5D;
				}

				final int var23 = (int) (150.0D * var21);

				for (int var24 = 0; var24 < var23; ++var24) {
					final float var25 = MathHelper.randomFloatClamp(var7, 0.0F,
							(float) Math.PI * 2F);
					var26 = MathHelper.randomFloatClamp(var7, 0.75F, 1.0F);
					var28 = 0.20000000298023224D + var21 / 100.0D;
					var30 = MathHelper.cos(var25) * 0.2F * var26 * var26
							* (var21 + 0.2D);
					final double var32 = MathHelper.sin(var25) * 0.2F * var26
							* var26 * (var21 + 0.2D);
					theWorld.spawnParticle(
							"blockdust_"
									+ Block.getIdFromBlock(var8)
									+ "_"
									+ theWorld.getBlockMetadata(par3, par4,
											par5), par3 + 0.5F, par4 + 1.0F,
							par5 + 0.5F, var30, var28, var32);
				}
			}
		}
	}

	/**
	 * Plays the specified record. Arg: recordName, x, y, z
	 */
	@Override
	public void playRecord(String par1Str, int par2, int par3, int par4) {
		final ChunkCoordinates var5 = new ChunkCoordinates(par2, par3, par4);
		final ISound var6 = (ISound) mapSoundPositions.get(var5);

		if (var6 != null) {
			mc.getSoundHandler().func_147683_b(var6);
			mapSoundPositions.remove(var5);
		}

		if (par1Str != null) {
			final ItemRecord var7 = ItemRecord.func_150926_b(par1Str);

			if (var7 != null) {
				mc.ingameGUI.setRecordPlayingMessage(var7.func_150927_i());
			}

			final PositionedSoundRecord var8 = PositionedSoundRecord
					.func_147675_a(new ResourceLocation(par1Str), par2, par3,
							par4);
			mapSoundPositions.put(var5, var8);
			mc.getSoundHandler().playSound(var8);
		}
	}

	/**
	 * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
	 */
	@Override
	public void playSound(String par1Str, double par2, double par4,
			double par6, float par8, float par9) {
	}

	/**
	 * Plays sound to all near players except the player reference given
	 */
	@Override
	public void playSoundToNearExcept(EntityPlayer par1EntityPlayer,
			String par2Str, double par3, double par5, double par7, float par9,
			float par10) {
	}

	public void rebuildDisplayListEntities() {
		theWorld.theProfiler.startSection("staticentityrebuild");
		GL11.glPushMatrix();
		GL11.glNewList(displayListEntities, GL11.GL_COMPILE);
		final List var1 = theWorld.getLoadedEntityList();
		displayListEntitiesDirty = false;

		for (int var2 = 0; var2 < var1.size(); ++var2) {
			final Entity var3 = (Entity) var1.get(var2);

			if (RenderManager.instance.getEntityRenderObject(var3)
					.func_147905_a()) {
				displayListEntitiesDirty = displayListEntitiesDirty
						|| !RenderManager.instance.func_147936_a(var3, 0.0F,
								true);
			}
		}

		GL11.glEndList();
		GL11.glPopMatrix();
		theWorld.theProfiler.endSection();
	}

	public void registerDestroyBlockIcons(IIconRegister par1IconRegister) {
		destroyBlockIcons = new IIcon[10];

		for (int var2 = 0; var2 < destroyBlockIcons.length; ++var2) {
			destroyBlockIcons[var2] = par1IconRegister
					.registerIcon("destroy_stage_" + var2);
		}
	}

	/**
	 * Render all render lists
	 */
	public void renderAllRenderLists(int par1, double par2) {
		mc.entityRenderer.enableLightmap(par2);

		for (final RenderList allRenderList : allRenderLists) {
			allRenderList.callLists();
		}

		mc.entityRenderer.disableLightmap(par2);
	}

	public int renderAllSortedRenderers(int renderPass, double partialTicks) {
		return renderSortedRenderers(0, countSortedWorldRenderers, renderPass,
				partialTicks);
	}

	public void renderClouds(float par1) {
		if (!Config.isCloudsOff()) {
			if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
				final WorldProvider partialTicks = mc.theWorld.provider;
				final Object var2 = Reflector.call(partialTicks,
						Reflector.ForgeWorldProvider_getCloudRenderer,
						new Object[0]);

				if (var2 != null) {
					Reflector.callVoid(var2, Reflector.IRenderHandler_render,
							new Object[] { Float.valueOf(par1), theWorld, mc });
					return;
				}
			}

			if (mc.theWorld.provider.isSurfaceWorld()) {
				if (Config.isCloudsFancy()) {
					renderCloudsFancy(par1);
				} else {
					final float partialTicks1 = par1;
					par1 = 0.0F;
					GL11.glDisable(GL11.GL_CULL_FACE);
					final float preRenderTicks = (float) (mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY)
							* par1);
					final byte var3 = 32;
					final int var4 = 256 / var3;
					final Tessellator var5 = Tessellator.instance;
					renderEngine.bindTexture(locationCloudsPng);
					GL11.glEnable(GL11.GL_BLEND);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					double dc;
					double exactPlayerZ1;

					if (isFancyGlListClouds
							|| cloudTickCounter >= cloudTickCounterGlList + 20) {
						GL11.glNewList(glListClouds, GL11.GL_COMPILE);
						final Vec3 entityliving = theWorld.getCloudColour(par1);
						float exactPlayerX = (float) entityliving.xCoord;
						float var8 = (float) entityliving.yCoord;
						float exactPlayerY = (float) entityliving.zCoord;
						float var10;

						if (mc.gameSettings.anaglyph) {
							var10 = (exactPlayerX * 30.0F + var8 * 59.0F + exactPlayerY * 11.0F) / 100.0F;
							final float exactPlayerZ = (exactPlayerX * 30.0F + var8 * 70.0F) / 100.0F;
							final float var12 = (exactPlayerX * 30.0F + exactPlayerY * 70.0F) / 100.0F;
							exactPlayerX = var10;
							var8 = exactPlayerZ;
							exactPlayerY = var12;
						}

						var10 = 4.8828125E-4F;
						exactPlayerZ1 = cloudTickCounter + par1;
						dc = mc.renderViewEntity.prevPosX
								+ (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX)
								* par1 + exactPlayerZ1 * 0.029999999329447746D;
						double cdx = mc.renderViewEntity.prevPosZ
								+ (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ)
								* par1;
						final int cdz = MathHelper.floor_double(dc / 2048.0D);
						final int var18 = MathHelper
								.floor_double(cdx / 2048.0D);
						dc -= cdz * 2048;
						cdx -= var18 * 2048;
						float var19 = theWorld.provider.getCloudHeight()
								- preRenderTicks + 0.33F;
						var19 += mc.gameSettings.ofCloudsHeight * 128.0F;
						final float var20 = (float) (dc * var10);
						final float var21 = (float) (cdx * var10);
						var5.startDrawingQuads();
						var5.setColorRGBA_F(exactPlayerX, var8, exactPlayerY,
								0.8F);

						for (int var22 = -var3 * var4; var22 < var3 * var4; var22 += var3) {
							for (int var23 = -var3 * var4; var23 < var3 * var4; var23 += var3) {
								var5.addVertexWithUV(var22 + 0, var19, var23
										+ var3, (var22 + 0) * var10 + var20,
										(var23 + var3) * var10 + var21);
								var5.addVertexWithUV(var22 + var3, var19, var23
										+ var3, (var22 + var3) * var10 + var20,
										(var23 + var3) * var10 + var21);
								var5.addVertexWithUV(var22 + var3, var19,
										var23 + 0, (var22 + var3) * var10
												+ var20, (var23 + 0) * var10
												+ var21);
								var5.addVertexWithUV(var22 + 0, var19,
										var23 + 0, (var22 + 0) * var10 + var20,
										(var23 + 0) * var10 + var21);
							}
						}

						var5.draw();
						GL11.glEndList();
						isFancyGlListClouds = false;
						cloudTickCounterGlList = cloudTickCounter;
						cloudPlayerX = mc.renderViewEntity.prevPosX;
						cloudPlayerY = mc.renderViewEntity.prevPosY;
						cloudPlayerZ = mc.renderViewEntity.prevPosZ;
					}

					final EntityLivingBase entityliving1 = mc.renderViewEntity;
					final double exactPlayerX1 = entityliving1.prevPosX
							+ (entityliving1.posX - entityliving1.prevPosX)
							* partialTicks1;
					final double exactPlayerY1 = entityliving1.prevPosY
							+ (entityliving1.posY - entityliving1.prevPosY)
							* partialTicks1;
					exactPlayerZ1 = entityliving1.prevPosZ
							+ (entityliving1.posZ - entityliving1.prevPosZ)
							* partialTicks1;
					dc = cloudTickCounter - cloudTickCounterGlList
							+ partialTicks1;
					final float cdx1 = (float) (exactPlayerX1 - cloudPlayerX + dc * 0.03D);
					final float cdy = (float) (exactPlayerY1 - cloudPlayerY);
					final float cdz1 = (float) (exactPlayerZ1 - cloudPlayerZ);
					GL11.glTranslatef(-cdx1, -cdy, -cdz1);
					GL11.glCallList(glListClouds);
					GL11.glTranslatef(cdx1, cdy, cdz1);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_CULL_FACE);
				}
			}
		}
	}

	/**
	 * Renders the 3d fancy clouds
	 */
	public void renderCloudsFancy(float par1) {
		final float partialTicks = par1;
		par1 = 0.0F;
		GL11.glDisable(GL11.GL_CULL_FACE);
		final float var2 = (float) (mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY)
				* par1);
		final Tessellator var3 = Tessellator.instance;
		final float var4 = 12.0F;
		final float var5 = 4.0F;
		final double var6 = cloudTickCounter + par1;
		double var8 = (mc.renderViewEntity.prevPosX
				+ (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX)
				* par1 + var6 * 0.029999999329447746D)
				/ var4;
		double var10 = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ)
				* par1)
				/ var4 + 0.33000001311302185D;
		float var12 = theWorld.provider.getCloudHeight() - var2 + 0.33F;
		var12 += mc.gameSettings.ofCloudsHeight * 128.0F;
		final int var13 = MathHelper.floor_double(var8 / 2048.0D);
		final int var14 = MathHelper.floor_double(var10 / 2048.0D);
		var8 -= var13 * 2048;
		var10 -= var14 * 2048;
		renderEngine.bindTexture(locationCloudsPng);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		float cdz;

		if (!isFancyGlListClouds
				|| cloudTickCounter >= cloudTickCounterGlList + 20) {
			GL11.glNewList(glListClouds, GL11.GL_COMPILE);
			final Vec3 entityliving = theWorld.getCloudColour(par1);
			float exactPlayerX = (float) entityliving.xCoord;
			float var17 = (float) entityliving.yCoord;
			float exactPlayerY = (float) entityliving.zCoord;
			float var19;
			float exactPlayerZ;
			float var21;

			if (mc.gameSettings.anaglyph) {
				var19 = (exactPlayerX * 30.0F + var17 * 59.0F + exactPlayerY * 11.0F) / 100.0F;
				exactPlayerZ = (exactPlayerX * 30.0F + var17 * 70.0F) / 100.0F;
				var21 = (exactPlayerX * 30.0F + exactPlayerY * 70.0F) / 100.0F;
				exactPlayerX = var19;
				var17 = exactPlayerZ;
				exactPlayerY = var21;
			}

			var19 = (float) (var8 * 0.0D);
			exactPlayerZ = (float) (var10 * 0.0D);
			var21 = 0.00390625F;
			var19 = MathHelper.floor_double(var8) * var21;
			exactPlayerZ = MathHelper.floor_double(var10) * var21;
			final float dc = (float) (var8 - MathHelper.floor_double(var8));
			final float var23 = (float) (var10 - MathHelper.floor_double(var10));
			final byte cdx = 8;
			final byte cdy = 4;
			cdz = 9.765625E-4F;
			GL11.glScalef(var4, 1.0F, var4);

			for (int var27 = 0; var27 < 2; ++var27) {
				if (var27 == 0) {
					GL11.glColorMask(false, false, false, false);
				} else if (mc.gameSettings.anaglyph) {
					if (EntityRenderer.anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				for (int var28 = -cdy + 1; var28 <= cdy; ++var28) {
					for (int var29 = -cdy + 1; var29 <= cdy; ++var29) {
						var3.startDrawingQuads();
						final float var30 = var28 * cdx;
						final float var31 = var29 * cdx;
						final float var32 = var30 - dc;
						final float var33 = var31 - var23;

						if (var12 > -var5 - 1.0F) {
							var3.setColorRGBA_F(exactPlayerX * 0.7F,
									var17 * 0.7F, exactPlayerY * 0.7F, 0.8F);
							var3.setNormal(0.0F, -1.0F, 0.0F);
							var3.addVertexWithUV(var32 + 0.0F, var12 + 0.0F,
									var33 + cdx,
									(var30 + 0.0F) * var21 + var19,
									(var31 + cdx) * var21 + exactPlayerZ);
							var3.addVertexWithUV(var32 + cdx, var12 + 0.0F,
									var33 + cdx, (var30 + cdx) * var21 + var19,
									(var31 + cdx) * var21 + exactPlayerZ);
							var3.addVertexWithUV(var32 + cdx, var12 + 0.0F,
									var33 + 0.0F,
									(var30 + cdx) * var21 + var19,
									(var31 + 0.0F) * var21 + exactPlayerZ);
							var3.addVertexWithUV(var32 + 0.0F, var12 + 0.0F,
									var33 + 0.0F, (var30 + 0.0F) * var21
											+ var19, (var31 + 0.0F) * var21
											+ exactPlayerZ);
						}

						if (var12 <= var5 + 1.0F) {
							var3.setColorRGBA_F(exactPlayerX, var17,
									exactPlayerY, 0.8F);
							var3.setNormal(0.0F, 1.0F, 0.0F);
							var3.addVertexWithUV(var32 + 0.0F, var12 + var5
									- cdz, var33 + cdx, (var30 + 0.0F) * var21
									+ var19, (var31 + cdx) * var21
									+ exactPlayerZ);
							var3.addVertexWithUV(var32 + cdx, var12 + var5
									- cdz, var33 + cdx, (var30 + cdx) * var21
									+ var19, (var31 + cdx) * var21
									+ exactPlayerZ);
							var3.addVertexWithUV(var32 + cdx, var12 + var5
									- cdz, var33 + 0.0F, (var30 + cdx) * var21
									+ var19, (var31 + 0.0F) * var21
									+ exactPlayerZ);
							var3.addVertexWithUV(var32 + 0.0F, var12 + var5
									- cdz, var33 + 0.0F, (var30 + 0.0F) * var21
									+ var19, (var31 + 0.0F) * var21
									+ exactPlayerZ);
						}

						var3.setColorRGBA_F(exactPlayerX * 0.9F, var17 * 0.9F,
								exactPlayerY * 0.9F, 0.8F);
						int var34;

						if (var28 > -1) {
							var3.setNormal(-1.0F, 0.0F, 0.0F);

							for (var34 = 0; var34 < cdx; ++var34) {
								var3.addVertexWithUV(var32 + var34 + 0.0F,
										var12 + 0.0F, var33 + cdx, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + cdx)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + var34 + 0.0F,
										var12 + var5, var33 + cdx, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + cdx)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + var34 + 0.0F,
										var12 + var5, var33 + 0.0F, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + 0.0F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + var34 + 0.0F,
										var12 + 0.0F, var33 + 0.0F, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + 0.0F)
												* var21 + exactPlayerZ);
							}
						}

						if (var28 <= 1) {
							var3.setNormal(1.0F, 0.0F, 0.0F);

							for (var34 = 0; var34 < cdx; ++var34) {
								var3.addVertexWithUV(
										var32 + var34 + 1.0F - cdz,
										var12 + 0.0F, var33 + cdx, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + cdx)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(
										var32 + var34 + 1.0F - cdz, var12
												+ var5, var33 + cdx, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + cdx)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(
										var32 + var34 + 1.0F - cdz, var12
												+ var5, var33 + 0.0F, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + 0.0F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(
										var32 + var34 + 1.0F - cdz,
										var12 + 0.0F, var33 + 0.0F, (var30
												+ var34 + 0.5F)
												* var21 + var19, (var31 + 0.0F)
												* var21 + exactPlayerZ);
							}
						}

						var3.setColorRGBA_F(exactPlayerX * 0.8F, var17 * 0.8F,
								exactPlayerY * 0.8F, 0.8F);

						if (var29 > -1) {
							var3.setNormal(0.0F, 0.0F, -1.0F);

							for (var34 = 0; var34 < cdx; ++var34) {
								var3.addVertexWithUV(var32 + 0.0F,
										var12 + var5, var33 + var34 + 0.0F,
										(var30 + 0.0F) * var21 + var19, (var31
												+ var34 + 0.5F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + cdx, var12 + var5,
										var33 + var34 + 0.0F, (var30 + cdx)
												* var21 + var19,
										(var31 + var34 + 0.5F) * var21
												+ exactPlayerZ);
								var3.addVertexWithUV(var32 + cdx, var12 + 0.0F,
										var33 + var34 + 0.0F, (var30 + cdx)
												* var21 + var19,
										(var31 + var34 + 0.5F) * var21
												+ exactPlayerZ);
								var3.addVertexWithUV(var32 + 0.0F,
										var12 + 0.0F, var33 + var34 + 0.0F,
										(var30 + 0.0F) * var21 + var19, (var31
												+ var34 + 0.5F)
												* var21 + exactPlayerZ);
							}
						}

						if (var29 <= 1) {
							var3.setNormal(0.0F, 0.0F, 1.0F);

							for (var34 = 0; var34 < cdx; ++var34) {
								var3.addVertexWithUV(var32 + 0.0F,
										var12 + var5, var33 + var34 + 1.0F
												- cdz, (var30 + 0.0F) * var21
												+ var19, (var31 + var34 + 0.5F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + cdx, var12 + var5,
										var33 + var34 + 1.0F - cdz,
										(var30 + cdx) * var21 + var19, (var31
												+ var34 + 0.5F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + cdx, var12 + 0.0F,
										var33 + var34 + 1.0F - cdz,
										(var30 + cdx) * var21 + var19, (var31
												+ var34 + 0.5F)
												* var21 + exactPlayerZ);
								var3.addVertexWithUV(var32 + 0.0F,
										var12 + 0.0F, var33 + var34 + 1.0F
												- cdz, (var30 + 0.0F) * var21
												+ var19, (var31 + var34 + 0.5F)
												* var21 + exactPlayerZ);
							}
						}

						var3.draw();
					}
				}
			}

			GL11.glEndList();
			isFancyGlListClouds = true;
			cloudTickCounterGlList = cloudTickCounter;
			cloudPlayerX = mc.renderViewEntity.prevPosX;
			cloudPlayerY = mc.renderViewEntity.prevPosY;
			cloudPlayerZ = mc.renderViewEntity.prevPosZ;
		}

		final EntityLivingBase var36 = mc.renderViewEntity;
		final double var37 = var36.prevPosX + (var36.posX - var36.prevPosX)
				* partialTicks;
		final double var38 = var36.prevPosY + (var36.posY - var36.prevPosY)
				* partialTicks;
		final double var39 = var36.prevPosZ + (var36.posZ - var36.prevPosZ)
				* partialTicks;
		final double var40 = cloudTickCounter - cloudTickCounterGlList
				+ partialTicks;
		final float var41 = (float) (var37 - cloudPlayerX + var40 * 0.03D);
		final float var42 = (float) (var38 - cloudPlayerY);
		cdz = (float) (var39 - cloudPlayerZ);
		GL11.glTranslatef(-var41, -var42, -cdz);
		GL11.glCallList(glListClouds);
		GL11.glTranslatef(var41, var42, cdz);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public void renderEntities(EntityLivingBase p_147589_1_,
			ICamera p_147589_2_, float p_147589_3_) {
		int pass = 0;

		if (Reflector.MinecraftForgeClient_getRenderPass.exists()) {
			pass = Reflector
					.callInt(Reflector.MinecraftForgeClient_getRenderPass,
							new Object[0]);
		}

		final boolean hasEntityShouldRenderInPass = Reflector.ForgeEntity_shouldRenderInPass
				.exists();
		final boolean hasTileEntityShouldRenderInPass = Reflector.ForgeTileEntity_shouldRenderInPass
				.exists();

		if (renderEntitiesStartupCounter > 0) {
			if (pass > 0)
				return;

			--renderEntitiesStartupCounter;
		} else {
			final double var4 = p_147589_1_.prevPosX
					+ (p_147589_1_.posX - p_147589_1_.prevPosX) * p_147589_3_;
			final double var6 = p_147589_1_.prevPosY
					+ (p_147589_1_.posY - p_147589_1_.prevPosY) * p_147589_3_;
			final double var8 = p_147589_1_.prevPosZ
					+ (p_147589_1_.posZ - p_147589_1_.prevPosZ) * p_147589_3_;
			theWorld.theProfiler.startSection("prepare");
			TileEntityRendererDispatcher.instance.func_147542_a(theWorld,
					mc.getTextureManager(), mc.fontRenderer,
					mc.renderViewEntity, p_147589_3_);
			RenderManager.instance.func_147938_a(theWorld,
					mc.getTextureManager(), mc.fontRenderer,
					mc.renderViewEntity, mc.pointedEntity, mc.gameSettings,
					p_147589_3_);

			if (pass == 0) {
				countEntitiesTotal = 0;
				countEntitiesRendered = 0;
				countEntitiesHidden = 0;
				final EntityLivingBase var17 = mc.renderViewEntity;
				final double var18 = var17.lastTickPosX
						+ (var17.posX - var17.lastTickPosX) * p_147589_3_;
				final double oldFancyGraphics = var17.lastTickPosY
						+ (var17.posY - var17.lastTickPosY) * p_147589_3_;
				final double aabb = var17.lastTickPosZ
						+ (var17.posZ - var17.lastTickPosZ) * p_147589_3_;
				TileEntityRendererDispatcher.staticPlayerX = var18;
				TileEntityRendererDispatcher.staticPlayerY = oldFancyGraphics;
				TileEntityRendererDispatcher.staticPlayerZ = aabb;
				theWorld.theProfiler.endStartSection("staticentities");

				if (displayListEntitiesDirty) {
					RenderManager.renderPosX = 0.0D;
					RenderManager.renderPosY = 0.0D;
					RenderManager.renderPosZ = 0.0D;
					rebuildDisplayListEntities();
				}

				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPushMatrix();
				GL11.glTranslated(-var18, -oldFancyGraphics, -aabb);
				GL11.glCallList(displayListEntities);
				GL11.glPopMatrix();
				RenderManager.renderPosX = var18;
				RenderManager.renderPosY = oldFancyGraphics;
				RenderManager.renderPosZ = aabb;
			}

			mc.entityRenderer.enableLightmap(p_147589_3_);
			theWorld.theProfiler.endStartSection("global");
			final List var24 = theWorld.getLoadedEntityList();

			if (pass == 0) {
				countEntitiesTotal = var24.size();
			}

			if (Config.isFogOff() && mc.entityRenderer.fogStandard) {
				GL11.glDisable(GL11.GL_FOG);
			}

			Entity var19;
			int var25;

			for (var25 = 0; var25 < theWorld.weatherEffects.size(); ++var25) {
				var19 = (Entity) theWorld.weatherEffects.get(var25);

				if (!hasEntityShouldRenderInPass
						|| Reflector.callBoolean(var19,
								Reflector.ForgeEntity_shouldRenderInPass,
								new Object[] { Integer.valueOf(pass) })) {
					++countEntitiesRendered;

					if (var19.isInRangeToRender3d(var4, var6, var8)) {
						RenderManager.instance
								.func_147937_a(var19, p_147589_3_);
					}
				}
			}

			theWorld.theProfiler.endStartSection("entities");
			final boolean var26 = mc.gameSettings.fancyGraphics;
			mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();

			for (var25 = 0; var25 < var24.size(); ++var25) {
				var19 = (Entity) var24.get(var25);

				if (!hasEntityShouldRenderInPass
						|| Reflector.callBoolean(var19,
								Reflector.ForgeEntity_shouldRenderInPass,
								new Object[] { Integer.valueOf(pass) })) {
					boolean te = var19.isInRangeToRender3d(var4, var6, var8)
							&& (var19.ignoreFrustumCheck
									|| p_147589_2_
											.isBoundingBoxInFrustum(var19.boundingBox) || var19.riddenByEntity == mc.thePlayer);

					if (!te && var19 instanceof EntityLiving) {
						final EntityLiving var28 = (EntityLiving) var19;

						if (var28.getLeashed()
								&& var28.getLeashedToEntity() != null) {
							final Entity teClass = var28.getLeashedToEntity();
							te = p_147589_2_
									.isBoundingBoxInFrustum(teClass.boundingBox);
						}
					}

					if (te
							&& (var19 != mc.renderViewEntity
									|| mc.gameSettings.thirdPersonView != 0 || mc.renderViewEntity
										.isPlayerSleeping())
							&& theWorld.blockExists(
									MathHelper.floor_double(var19.posX), 0,
									MathHelper.floor_double(var19.posZ))) {
						++countEntitiesRendered;

						if (var19.getClass() == EntityItemFrame.class) {
							var19.renderDistanceWeight = 0.06D;
						}

						renderedEntity = var19;
						RenderManager.instance
								.func_147937_a(var19, p_147589_3_);
						renderedEntity = null;
					}
				}
			}

			mc.gameSettings.fancyGraphics = var26;
			theWorld.theProfiler.endStartSection("blockentities");
			RenderHelper.enableStandardItemLighting();

			for (var25 = 0; var25 < tileEntities.size(); ++var25) {
				final TileEntity var27 = (TileEntity) tileEntities.get(var25);

				if (!hasTileEntityShouldRenderInPass
						|| Reflector.callBoolean(var27,
								Reflector.ForgeTileEntity_shouldRenderInPass,
								new Object[] { Integer.valueOf(pass) })) {
					final AxisAlignedBB var29 = getTileEntityBoundingBox(var27);

					if (var29 == AABB_INFINITE
							|| p_147589_2_.isBoundingBoxInFrustum(var29)) {
						final Class var30 = var27.getClass();

						if (var30 == TileEntitySign.class && !Config.zoomMode) {
							final EntityClientPlayerMP block = mc.thePlayer;
							final double distSq = var27.getDistanceFrom(
									block.posX, block.posY, block.posZ);

							if (distSq > 256.0D) {
								final FontRenderer fr = TileEntityRendererDispatcher.instance
										.func_147548_a();
								fr.enabled = false;
								TileEntityRendererDispatcher.instance
										.func_147544_a(var27, p_147589_3_);
								fr.enabled = true;
								continue;
							}
						}

						if (var30 == TileEntityChest.class) {
							final Block var31 = theWorld.getBlock(
									var27.field_145851_c, var27.field_145848_d,
									var27.field_145849_e);

							if (!(var31 instanceof BlockChest)) {
								continue;
							}
						}

						TileEntityRendererDispatcher.instance.func_147544_a(
								var27, p_147589_3_);
					}
				}
			}

			mc.entityRenderer.disableLightmap(p_147589_3_);
			theWorld.theProfiler.endSection();
		}
	}

	/**
	 * Renders the sky with the partial tick time. Args: partialTickTime
	 */
	public void renderSky(float par1) {
		if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
			final WorldProvider var2 = mc.theWorld.provider;
			final Object var3 = Reflector.call(var2,
					Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);

			if (var3 != null) {
				Reflector.callVoid(var3, Reflector.IRenderHandler_render,
						new Object[] { Float.valueOf(par1), theWorld, mc });
				return;
			}
		}

		if (mc.theWorld.provider.dimensionId == 1) {
			if (!Config.isSkyEnabled())
				return;

			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			RenderHelper.disableStandardItemLighting();
			GL11.glDepthMask(false);
			renderEngine.bindTexture(locationEndSkyPng);
			final Tessellator var201 = Tessellator.instance;

			for (int var22 = 0; var22 < 6; ++var22) {
				GL11.glPushMatrix();

				if (var22 == 1) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 2) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 3) {
					GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 4) {
					GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
				}

				if (var22 == 5) {
					GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				}

				var201.startDrawingQuads();
				var201.setColorOpaque_I(2631720);
				var201.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
				var201.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
				var201.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
				var201.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
				var201.draw();
				GL11.glPopMatrix();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		} else if (mc.theWorld.provider.isSurfaceWorld()) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Vec3 var21 = theWorld.getSkyColor(mc.renderViewEntity, par1);
			var21 = CustomColorizer.getSkyColor(var21, mc.theWorld,
					mc.renderViewEntity.posX, mc.renderViewEntity.posY + 1.0D,
					mc.renderViewEntity.posZ);
			float var231 = (float) var21.xCoord;
			float var4 = (float) var21.yCoord;
			float var5 = (float) var21.zCoord;
			float var8;

			if (mc.gameSettings.anaglyph) {
				final float var23 = (var231 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
				final float var24 = (var231 * 30.0F + var4 * 70.0F) / 100.0F;
				var8 = (var231 * 30.0F + var5 * 70.0F) / 100.0F;
				var231 = var23;
				var4 = var24;
				var5 = var8;
			}

			GL11.glColor3f(var231, var4, var5);
			final Tessellator var241 = Tessellator.instance;
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glColor3f(var231, var4, var5);

			if (Config.isSkyEnabled()) {
				GL11.glCallList(glSkyList);
			}

			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			RenderHelper.disableStandardItemLighting();
			final float[] var251 = theWorld.provider.calcSunriseSunsetColors(
					theWorld.getCelestialAngle(par1), par1);
			float var9;
			float var10;
			float var11;
			float var12;
			float var20;
			int var30;
			float var16;
			float var17;

			if (var251 != null && Config.isSunMoonEnabled()) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glPushMatrix();
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(
						MathHelper.sin(theWorld.getCelestialAngleRadians(par1)) < 0.0F ? 180.0F
								: 0.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
				var8 = var251[0];
				var9 = var251[1];
				var10 = var251[2];

				if (mc.gameSettings.anaglyph) {
					var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
					var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
					var20 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
					var8 = var11;
					var9 = var12;
					var10 = var20;
				}

				var241.startDrawing(6);
				var241.setColorRGBA_F(var8, var9, var10, var251[3]);
				var241.addVertex(0.0D, 100.0D, 0.0D);
				final byte var25 = 16;
				var241.setColorRGBA_F(var251[0], var251[1], var251[2], 0.0F);

				for (var30 = 0; var30 <= var25; ++var30) {
					var20 = var30 * (float) Math.PI * 2.0F / var25;
					var16 = MathHelper.sin(var20);
					var17 = MathHelper.cos(var20);
					var241.addVertex(var16 * 120.0F, var17 * 120.0F, -var17
							* 40.0F * var251[3]);
				}

				var241.draw();
				GL11.glPopMatrix();
				GL11.glShadeModel(GL11.GL_FLAT);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			GL11.glPushMatrix();
			var8 = 1.0F - theWorld.getRainStrength(par1);
			var9 = 0.0F;
			var10 = 0.0F;
			var11 = 0.0F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
			GL11.glTranslatef(var9, var10, var11);
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			CustomSky.renderSky(theWorld, renderEngine,
					theWorld.getCelestialAngle(par1), var8);
			GL11.glRotatef(theWorld.getCelestialAngle(par1) * 360.0F, 1.0F,
					0.0F, 0.0F);

			if (Config.isSunMoonEnabled()) {
				var12 = 30.0F;
				renderEngine.bindTexture(locationSunPng);
				var241.startDrawingQuads();
				var241.addVertexWithUV(-var12, 100.0D, -var12, 0.0D, 0.0D);
				var241.addVertexWithUV(var12, 100.0D, -var12, 1.0D, 0.0D);
				var241.addVertexWithUV(var12, 100.0D, var12, 1.0D, 1.0D);
				var241.addVertexWithUV(-var12, 100.0D, var12, 0.0D, 1.0D);
				var241.draw();
				var12 = 20.0F;
				renderEngine.bindTexture(locationMoonPhasesPng);
				final int var26 = theWorld.getMoonPhase();
				final int var27 = var26 % 4;
				var30 = var26 / 4 % 2;
				var16 = (var27 + 0) / 4.0F;
				var17 = (var30 + 0) / 2.0F;
				final float var18 = (var27 + 1) / 4.0F;
				final float var19 = (var30 + 1) / 2.0F;
				var241.startDrawingQuads();
				var241.addVertexWithUV(-var12, -100.0D, var12, var18, var19);
				var241.addVertexWithUV(var12, -100.0D, var12, var16, var19);
				var241.addVertexWithUV(var12, -100.0D, -var12, var16, var17);
				var241.addVertexWithUV(-var12, -100.0D, -var12, var18, var17);
				var241.draw();
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var20 = theWorld.getStarBrightness(par1) * var8;

			if (var20 > 0.0F && Config.isStarsEnabled()
					&& !CustomSky.hasSkyLayers(theWorld)) {
				GL11.glColor4f(var20, var20, var20, var20);
				GL11.glCallList(starGLCallList);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			final double var28 = mc.thePlayer.getPosition(par1).yCoord
					- theWorld.getHorizon();

			if (var28 < 0.0D) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0F, 12.0F, 0.0F);
				GL11.glCallList(glSkyList2);
				GL11.glPopMatrix();
				var10 = 1.0F;
				var11 = -((float) (var28 + 65.0D));
				var12 = -var10;
				var241.startDrawingQuads();
				var241.setColorRGBA_I(0, 255);
				var241.addVertex(-var10, var11, var10);
				var241.addVertex(var10, var11, var10);
				var241.addVertex(var10, var12, var10);
				var241.addVertex(-var10, var12, var10);
				var241.addVertex(-var10, var12, -var10);
				var241.addVertex(var10, var12, -var10);
				var241.addVertex(var10, var11, -var10);
				var241.addVertex(-var10, var11, -var10);
				var241.addVertex(var10, var12, -var10);
				var241.addVertex(var10, var12, var10);
				var241.addVertex(var10, var11, var10);
				var241.addVertex(var10, var11, -var10);
				var241.addVertex(-var10, var11, -var10);
				var241.addVertex(-var10, var11, var10);
				var241.addVertex(-var10, var12, var10);
				var241.addVertex(-var10, var12, -var10);
				var241.addVertex(-var10, var12, -var10);
				var241.addVertex(-var10, var12, var10);
				var241.addVertex(var10, var12, var10);
				var241.addVertex(var10, var12, -var10);
				var241.draw();
			}

			if (theWorld.provider.isSkyColored()) {
				GL11.glColor3f(var231 * 0.2F + 0.04F, var4 * 0.2F + 0.04F,
						var5 * 0.6F + 0.1F);
			} else {
				GL11.glColor3f(var231, var4, var5);
			}

			if (mc.gameSettings.renderDistanceChunks <= 4) {
				GL11.glColor3f(mc.entityRenderer.fogColorRed,
						mc.entityRenderer.fogColorGreen,
						mc.entityRenderer.fogColorBlue);
			}

			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -((float) (var28 - 16.0D)), 0.0F);

			if (Config.isSkyEnabled()) {
				GL11.glCallList(glSkyList2);
			}

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
		}
	}

	/**
	 * Renders the sorted renders for the specified render pass. Args:
	 * startRenderer, numRenderers, renderPass, partialTickTime
	 */
	private int renderSortedRenderers(int par1, int par2, int par3, double par4) {
		if (Config.isFastRender())
			return renderSortedRenderersFast(par1, par2, par3, par4);
		else {
			glRenderLists.clear();
			int var6 = 0;
			int var7 = par1;
			int var8 = par2;
			byte var9 = 1;

			if (par3 == 1) {
				var7 = countSortedWorldRenderers - 1 - par1;
				var8 = countSortedWorldRenderers - 1 - par2;
				var9 = -1;
			}

			for (int var22 = var7; var22 != var8; var22 += var9) {
				if (par3 == 0) {
					++renderersLoaded;

					if (sortedWorldRenderers[var22].skipRenderPass[par3]) {
						++renderersSkippingRenderPass;
					} else if (!sortedWorldRenderers[var22].isInFrustum) {
						++renderersBeingClipped;
					} else if (occlusionEnabled
							&& !sortedWorldRenderers[var22].isVisible) {
						++renderersBeingOccluded;
					} else {
						++renderersBeingRendered;
					}
				}

				if (!sortedWorldRenderers[var22].skipRenderPass[par3]
						&& sortedWorldRenderers[var22].isInFrustum
						&& (!occlusionEnabled || sortedWorldRenderers[var22].isVisible)) {
					final int var23 = sortedWorldRenderers[var22]
							.getGLCallListForPass(par3);

					if (var23 >= 0) {
						glRenderLists.add(sortedWorldRenderers[var22]);
						++var6;
					}
				}
			}

			final EntityLivingBase var221 = mc.renderViewEntity;
			final double var231 = var221.lastTickPosX
					+ (var221.posX - var221.lastTickPosX) * par4;
			final double var13 = var221.lastTickPosY
					+ (var221.posY - var221.lastTickPosY) * par4;
			final double var15 = var221.lastTickPosZ
					+ (var221.posZ - var221.lastTickPosZ) * par4;
			int var17 = 0;
			int var18;

			for (var18 = 0; var18 < allRenderLists.length; ++var18) {
				allRenderLists[var18].resetList();
			}

			int var20;
			int var21;

			for (var18 = 0; var18 < glRenderLists.size(); ++var18) {
				final WorldRenderer var24 = (WorldRenderer) glRenderLists
						.get(var18);
				var20 = -1;

				for (var21 = 0; var21 < var17; ++var21) {
					if (allRenderLists[var21].rendersChunk(var24.posXMinus,
							var24.posYMinus, var24.posZMinus)) {
						var20 = var21;
					}
				}

				if (var20 < 0) {
					var20 = var17++;
					allRenderLists[var20].setupRenderList(var24.posXMinus,
							var24.posYMinus, var24.posZMinus, var231, var13,
							var15);
				}

				allRenderLists[var20].addGLRenderList(var24
						.getGLCallListForPass(par3));
			}

			if (Config.isFogOff() && mc.entityRenderer.fogStandard) {
				GL11.glDisable(GL11.GL_FOG);
			}

			var18 = MathHelper.floor_double(var231);
			final int var241 = MathHelper.floor_double(var15);
			var20 = var18 - (var18 & 1023);
			var21 = var241 - (var241 & 1023);
			Arrays.sort(allRenderLists, new RenderDistanceSorter(var20, var21));
			renderAllRenderLists(par3, par4);
			return var6;
		}
	}

	private int renderSortedRenderersFast(int startIndex, int endIndex,
			int renderPass, double partialTicks) {
		glListBuffer.clear();
		int l = 0;
		final boolean debug = mc.gameSettings.showDebugInfo;
		int loopStart = startIndex;
		int loopEnd = endIndex;
		byte loopInc = 1;

		if (renderPass == 1) {
			loopStart = countSortedWorldRenderers - 1 - startIndex;
			loopEnd = countSortedWorldRenderers - 1 - endIndex;
			loopInc = -1;
		}

		for (int entityliving = loopStart; entityliving != loopEnd; entityliving += loopInc) {
			final WorldRenderer partialX = sortedWorldRenderers[entityliving];

			if (debug && renderPass == 0) {
				++renderersLoaded;

				if (partialX.skipRenderPass[renderPass]) {
					++renderersSkippingRenderPass;
				} else if (!partialX.isInFrustum) {
					++renderersBeingClipped;
				} else if (occlusionEnabled && !partialX.isVisible) {
					++renderersBeingOccluded;
				} else {
					++renderersBeingRendered;
				}
			}

			if (partialX.isInFrustum && !partialX.skipRenderPass[renderPass]
					&& (!occlusionEnabled || partialX.isVisible)) {
				final int glCallList = partialX
						.getGLCallListForPass(renderPass);

				if (glCallList >= 0) {
					glListBuffer.put(glCallList);
					++l;
				}
			}
		}

		if (l == 0)
			return 0;
		else {
			if (Config.isFogOff() && mc.entityRenderer.fogStandard) {
				GL11.glDisable(GL11.GL_FOG);
			}

			glListBuffer.flip();
			final EntityLivingBase var18 = mc.renderViewEntity;
			final double var19 = var18.lastTickPosX
					+ (var18.posX - var18.lastTickPosX) * partialTicks
					- WorldRenderer.globalChunkOffsetX;
			final double partialY = var18.lastTickPosY
					+ (var18.posY - var18.lastTickPosY) * partialTicks;
			final double partialZ = var18.lastTickPosZ
					+ (var18.posZ - var18.lastTickPosZ) * partialTicks
					- WorldRenderer.globalChunkOffsetZ;
			mc.entityRenderer.enableLightmap(partialTicks);
			GL11.glTranslatef((float) -var19, (float) -partialY,
					(float) -partialZ);
			GL11.glCallLists(glListBuffer);
			GL11.glTranslatef((float) var19, (float) partialY, (float) partialZ);
			mc.entityRenderer.disableLightmap(partialTicks);
			return l;
		}
	}

	private void renderStars() {
		final Random var1 = new Random(10842L);
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();

		for (int var3 = 0; var3 < 1500; ++var3) {
			double var4 = var1.nextFloat() * 2.0F - 1.0F;
			double var6 = var1.nextFloat() * 2.0F - 1.0F;
			double var8 = var1.nextFloat() * 2.0F - 1.0F;
			final double var10 = 0.15F + var1.nextFloat() * 0.1F;
			double var12 = var4 * var4 + var6 * var6 + var8 * var8;

			if (var12 < 1.0D && var12 > 0.01D) {
				var12 = 1.0D / Math.sqrt(var12);
				var4 *= var12;
				var6 *= var12;
				var8 *= var12;
				final double var14 = var4 * 100.0D;
				final double var16 = var6 * 100.0D;
				final double var18 = var8 * 100.0D;
				final double var20 = Math.atan2(var4, var8);
				final double var22 = Math.sin(var20);
				final double var24 = Math.cos(var20);
				final double var26 = Math.atan2(
						Math.sqrt(var4 * var4 + var8 * var8), var6);
				final double var28 = Math.sin(var26);
				final double var30 = Math.cos(var26);
				final double var32 = var1.nextDouble() * Math.PI * 2.0D;
				final double var34 = Math.sin(var32);
				final double var36 = Math.cos(var32);

				for (int var38 = 0; var38 < 4; ++var38) {
					final double var39 = 0.0D;
					final double var41 = ((var38 & 2) - 1) * var10;
					final double var43 = ((var38 + 1 & 2) - 1) * var10;
					final double var47 = var41 * var36 - var43 * var34;
					final double var49 = var43 * var36 + var41 * var34;
					final double var53 = var47 * var28 + var39 * var30;
					final double var55 = var39 * var28 - var47 * var30;
					final double var57 = var55 * var22 - var49 * var24;
					final double var61 = var49 * var22 + var55 * var24;
					var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
				}
			}
		}

		var2.draw();
	}

	public void setAllRenderersVisible() {
		if (worldRenderers != null) {
			for (int i = 0; i < worldRenderers.length; ++i) {
				worldRenderers[i].isVisible = true;
			}
		}
	}

	/**
	 * set null to clear
	 */
	public void setWorldAndLoadRenderers(WorldClient par1WorldClient) {
		if (theWorld != null) {
			theWorld.removeWorldAccess(this);
		}

		prevSortX = -9999.0D;
		prevSortY = -9999.0D;
		prevSortZ = -9999.0D;
		prevRenderSortX = -9999.0D;
		prevRenderSortY = -9999.0D;
		prevRenderSortZ = -9999.0D;
		prevChunkSortX = -9999;
		prevChunkSortY = -9999;
		prevChunkSortZ = -9999;
		RenderManager.instance.set(par1WorldClient);
		theWorld = par1WorldClient;
		renderBlocksRg = new RenderBlocks(par1WorldClient);

		if (par1WorldClient != null) {
			par1WorldClient.addWorldAccess(this);
			loadRenderers();
		}
	}

	/**
	 * Sorts all renderers based on the passed in entity. Args: entityLiving,
	 * renderPass, partialTickTime
	 */
	public int sortAndRender(EntityLivingBase player, int renderPass,
			double partialTicks) {
		renderViewEntity = player;
		final Profiler profiler = theWorld.theProfiler;
		profiler.startSection("sortchunks");
		int num;

		if (worldRenderersToUpdate.size() < 10) {
			final byte shouldSort = 10;

			for (num = 0; num < shouldSort; ++num) {
				worldRenderersCheckIndex = (worldRenderersCheckIndex + 1)
						% worldRenderers.length;
				final WorldRenderer ocReq = worldRenderers[worldRenderersCheckIndex];

				if (ocReq.needsUpdate
						&& !worldRenderersToUpdate.contains(ocReq)) {
					worldRenderersToUpdate.add(ocReq);
				}
			}
		}

		if (mc.gameSettings.renderDistanceChunks != renderDistanceChunks
				&& !Config.isLoadChunksFar()) {
			loadRenderers();
		}

		if (renderPass == 0) {
			renderersLoaded = 0;
			renderersBeingClipped = 0;
			renderersBeingOccluded = 0;
			renderersBeingRendered = 0;
			renderersSkippingRenderPass = 0;
		}

		boolean var33 = prevChunkSortX != player.chunkCoordX
				|| prevChunkSortY != player.chunkCoordY
				|| prevChunkSortZ != player.chunkCoordZ;
		double partialX;
		double partialY;
		double partialZ;
		double var34;

		if (!var33) {
			var34 = player.posX - prevSortX;
			partialX = player.posY - prevSortY;
			partialY = player.posZ - prevSortZ;
			partialZ = var34 * var34 + partialX * partialX + partialY
					* partialY;
			var33 = partialZ > 16.0D;
		}

		int endIndex;
		int stepNum;

		if (var33) {
			prevChunkSortX = player.chunkCoordX;
			prevChunkSortY = player.chunkCoordY;
			prevChunkSortZ = player.chunkCoordZ;
			prevSortX = player.posX;
			prevSortY = player.posY;
			prevSortZ = player.posZ;
			num = effectivePreloadedChunks * 16;
			final double var36 = player.posX - prevReposX;
			final double dReposY = player.posY - prevReposY;
			final double dReposZ = player.posZ - prevReposZ;
			final double countResort = var36 * var36 + dReposY * dReposY
					+ dReposZ * dReposZ;

			if (countResort > num * num + 16.0D) {
				prevReposX = player.posX;
				prevReposY = player.posY;
				prevReposZ = player.posZ;
				markRenderersForNewPosition(
						MathHelper.floor_double(player.posX),
						MathHelper.floor_double(player.posY),
						MathHelper.floor_double(player.posZ));
			}

			final EntitySorterFast lastIndex = new EntitySorterFast(player);
			lastIndex.prepareToSort(sortedWorldRenderers,
					countSortedWorldRenderers);
			Arrays.sort(sortedWorldRenderers, 0, countSortedWorldRenderers,
					lastIndex);

			if (Config.isFastRender()) {
				endIndex = (int) player.posX;
				stepNum = (int) player.posZ;
				final short step = 2000;

				if (Math.abs(endIndex - WorldRenderer.globalChunkOffsetX) > step
						|| Math.abs(stepNum - WorldRenderer.globalChunkOffsetZ) > step) {
					WorldRenderer.globalChunkOffsetX = endIndex;
					WorldRenderer.globalChunkOffsetZ = stepNum;
					loadRenderers();
				}
			}
		}

		if (Config.isTranslucentBlocksFancy()) {
			var34 = player.posX - prevRenderSortX;
			partialX = player.posY - prevRenderSortY;
			partialY = player.posZ - prevRenderSortZ;
			final int var39 = Math.min(27, countSortedWorldRenderers);
			WorldRenderer firstIndex;

			if (var34 * var34 + partialX * partialX + partialY * partialY > 1.0D) {
				prevRenderSortX = player.posX;
				prevRenderSortY = player.posY;
				prevRenderSortZ = player.posZ;

				for (int var38 = 0; var38 < var39; ++var38) {
					firstIndex = sortedWorldRenderers[var38];

					if (firstIndex.vertexState != null
							&& firstIndex.sortDistanceToEntitySquared < 1152.0F) {
						firstIndex.needVertexStateResort = true;

						if (vertexResortCounter > var38) {
							vertexResortCounter = var38;
						}
					}
				}
			}

			if (vertexResortCounter < var39) {
				while (vertexResortCounter < var39) {
					firstIndex = sortedWorldRenderers[vertexResortCounter];
					++vertexResortCounter;

					if (firstIndex.needVertexStateResort) {
						firstIndex.updateRendererSort(player);
						break;
					}
				}
			}
		}

		RenderHelper.disableStandardItemLighting();
		WrUpdates.preRender(this, player);

		if (mc.gameSettings.ofSmoothFps && renderPass == 0) {
			GL11.glFinish();
		}

		final byte var35 = 0;
		if (occlusionEnabled && mc.gameSettings.advancedOpengl
				&& !mc.gameSettings.anaglyph && renderPass == 0) {
			partialX = player.lastTickPosX
					+ (player.posX - player.lastTickPosX) * partialTicks;
			partialY = player.lastTickPosY
					+ (player.posY - player.lastTickPosY) * partialTicks;
			partialZ = player.lastTickPosZ
					+ (player.posZ - player.lastTickPosZ) * partialTicks;
			final byte var40 = 0;
			final int var41 = Math.min(20, countSortedWorldRenderers);
			checkOcclusionQueryResult(var40, var41, player.posX, player.posY,
					player.posZ);

			for (endIndex = var40; endIndex < var41; ++endIndex) {
				sortedWorldRenderers[endIndex].isVisible = true;
			}

			profiler.endStartSection("render");
			num = var35
					+ renderSortedRenderers(var40, var41, renderPass,
							partialTicks);
			endIndex = var41;
			stepNum = 0;
			final byte var42 = 40;
			int startIndex;

			for (final int switchStep = renderChunksWide; endIndex < countSortedWorldRenderers; num += renderSortedRenderers(
					startIndex, endIndex, renderPass, partialTicks)) {
				profiler.endStartSection("occ");
				startIndex = endIndex;

				if (stepNum < switchStep) {
					++stepNum;
				} else {
					--stepNum;
				}

				endIndex += stepNum * var42;

				if (endIndex <= startIndex) {
					endIndex = startIndex + 10;
				}

				if (endIndex > countSortedWorldRenderers) {
					endIndex = countSortedWorldRenderers;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				profiler.startSection("check");
				checkOcclusionQueryResult(startIndex, endIndex, player.posX,
						player.posY, player.posZ);
				profiler.endSection();
				GL11.glPushMatrix();
				float sumTX = 0.0F;
				float sumTY = 0.0F;
				float sumTZ = 0.0F;

				for (int k = startIndex; k < endIndex; ++k) {
					final WorldRenderer wr = sortedWorldRenderers[k];

					if (wr.skipAllRenderPasses()) {
						wr.isInFrustum = false;
					} else if (!wr.isUpdating && !wr.needsBoxUpdate) {
						if (wr.isInFrustum) {
							if (Config.isOcclusionFancy()
									&& !wr.isInFrustrumFully) {
								wr.isVisible = true;
							} else if (wr.isInFrustum
									&& !wr.isWaitingOnOcclusionQuery) {
								float bbX;
								float bbY;
								float bbZ;
								float tX;

								if (wr.isVisibleFromPosition) {
									bbX = Math
											.abs((float) (wr.visibleFromX - player.posX));
									bbY = Math
											.abs((float) (wr.visibleFromY - player.posY));
									bbZ = Math
											.abs((float) (wr.visibleFromZ - player.posZ));
									tX = bbX + bbY + bbZ;

									if (tX < 10.0D + k / 1000.0D) {
										wr.isVisible = true;
										continue;
									}

									wr.isVisibleFromPosition = false;
								}

								bbX = (float) (wr.posXMinus - partialX);
								bbY = (float) (wr.posYMinus - partialY);
								bbZ = (float) (wr.posZMinus - partialZ);
								tX = bbX - sumTX;
								final float tY = bbY - sumTY;
								final float tZ = bbZ - sumTZ;

								if (tX != 0.0F || tY != 0.0F || tZ != 0.0F) {
									GL11.glTranslatef(tX, tY, tZ);
									sumTX += tX;
									sumTY += tY;
									sumTZ += tZ;
								}

								profiler.startSection("bb");
								ARBOcclusionQuery
										.glBeginQueryARB(
												ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB,
												wr.glOcclusionQuery);
								wr.callOcclusionQueryList();
								ARBOcclusionQuery
										.glEndQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB);
								profiler.endSection();
								wr.isWaitingOnOcclusionQuery = true;
							}
						}
					} else {
						wr.isVisible = true;
					}
				}

				GL11.glPopMatrix();

				if (mc.gameSettings.anaglyph) {
					if (EntityRenderer.anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
				profiler.endStartSection("render");
			}
		} else {
			profiler.endStartSection("render");
			num = var35
					+ renderSortedRenderers(0, countSortedWorldRenderers,
							renderPass, partialTicks);
		}

		profiler.endSection();
		WrUpdates.postRender();
		return num;
	}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	@Override
	public void spawnParticle(String par1Str, final double par2,
			final double par4, final double par6, double par8, double par10,
			double par12) {
		try {
			doSpawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
		} catch (final Throwable var17) {
			final CrashReport var15 = CrashReport.makeCrashReport(var17,
					"Exception while adding particle");
			final CrashReportCategory var16 = var15
					.makeCategory("Particle being added");
			var16.addCrashSection("Name", par1Str);
			var16.addCrashSectionCallable("Position", new Callable() {
				@Override
				public String call() {
					return CrashReportCategory.func_85074_a(par2, par4, par6);
				}
			});
			throw new ReportedException(var15);
		}
	}

	public void updateCapes() {
	}

	public void updateClouds() {
		++cloudTickCounter;

		if (cloudTickCounter % 20 == 0) {
			final Iterator var1 = damagedBlocks.values().iterator();

			while (var1.hasNext()) {
				final DestroyBlockProgress var2 = (DestroyBlockProgress) var1
						.next();
				final int var3 = var2.getCreationCloudUpdateTick();

				if (cloudTickCounter - var3 > 400) {
					var1.remove();
				}
			}
		}
	}

	/**
	 * Updates some of the renderers sorted by distance from the player
	 */
	public boolean updateRenderers(EntityLivingBase entityliving, boolean flag) {
		renderViewEntity = entityliving;

		if (WrUpdates.hasWrUpdater())
			return WrUpdates.updateRenderers(this, entityliving, flag);
		else if (worldRenderersToUpdate.size() <= 0)
			return false;
		else {
			int num = 0;
			int maxNum = Config.getUpdatesPerFrame();

			if (Config.isDynamicUpdates() && !isMoving(entityliving)) {
				maxNum *= 3;
			}

			final byte NOT_IN_FRUSTRUM_MUL = 4;
			int numValid = 0;
			WorldRenderer wrBest = null;
			float distSqBest = Float.MAX_VALUE;
			int indexBest = -1;

			for (int maxDiffDistSq = 0; maxDiffDistSq < worldRenderersToUpdate
					.size(); ++maxDiffDistSq) {
				final WorldRenderer i = (WorldRenderer) worldRenderersToUpdate
						.get(maxDiffDistSq);

				if (i != null) {
					++numValid;

					if (!i.needsUpdate) {
						worldRenderersToUpdate
								.set(maxDiffDistSq, (Object) null);
					} else {
						float wr = i.distanceToEntitySquared(entityliving);

						if (wr <= 256.0F && isActingNow()) {
							i.updateRenderer(entityliving);
							i.needsUpdate = false;
							worldRenderersToUpdate.set(maxDiffDistSq,
									(Object) null);
							++num;
						} else {
							if (wr > 256.0F && num >= maxNum) {
								break;
							}

							if (!i.isInFrustum) {
								wr *= NOT_IN_FRUSTRUM_MUL;
							}

							if (wrBest == null) {
								wrBest = i;
								distSqBest = wr;
								indexBest = maxDiffDistSq;
							} else if (wr < distSqBest) {
								wrBest = i;
								distSqBest = wr;
								indexBest = maxDiffDistSq;
							}
						}
					}
				}
			}

			if (wrBest != null) {
				wrBest.updateRenderer(entityliving);
				wrBest.needsUpdate = false;
				worldRenderersToUpdate.set(indexBest, (Object) null);
				++num;
				final float var15 = distSqBest / 5.0F;

				for (int var16 = 0; var16 < worldRenderersToUpdate.size()
						&& num < maxNum; ++var16) {
					final WorldRenderer var17 = (WorldRenderer) worldRenderersToUpdate
							.get(var16);

					if (var17 != null) {
						float distSq = var17
								.distanceToEntitySquared(entityliving);

						if (!var17.isInFrustum) {
							distSq *= NOT_IN_FRUSTRUM_MUL;
						}

						final float diffDistSq = Math.abs(distSq - distSqBest);

						if (diffDistSq < var15) {
							var17.updateRenderer(entityliving);
							var17.needsUpdate = false;
							worldRenderersToUpdate.set(var16, (Object) null);
							++num;
						}
					}
				}
			}

			if (numValid == 0) {
				worldRenderersToUpdate.clear();
			}

			worldRenderersToUpdate.compact();
			return true;
		}
	}
}
