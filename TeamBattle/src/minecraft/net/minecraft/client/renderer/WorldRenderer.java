package net.minecraft.client.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;

public class WorldRenderer {
	public static volatile int chunksUpdated;

	public static int globalChunkOffsetX = 0;
	public static int globalChunkOffsetZ = 0;
	/** Bytes sent to the GPU */
	protected int bytesDrawn;
	/** Chunk index */
	public int chunkIndex;
	/** OpenGL occlusion query */
	public int glOcclusionQuery;
	protected int glRenderList;
	public boolean inSortedWorldRenderers;

	/** Is the chunk lit */
	public boolean isChunkLit;

	public boolean isInFrustrumFully;

	public boolean isInFrustum;

	protected boolean isInitialized;

	public volatile boolean isUpdating;

	/** Is this renderer visible according to the occlusion query */
	public boolean isVisible;
	public boolean isVisibleFromPosition;

	/** Is this renderer waiting on the result of the occlusion query */
	public boolean isWaitingOnOcclusionQuery;

	protected boolean needsBoxUpdate;

	/** Boolean for whether this renderer needs to be updated or not */
	public volatile boolean needsUpdate;

	public boolean needVertexStateResort;

	public int posX;

	/** Pos X clipped */
	public int posXClip;

	/** Pos X minus */
	public int posXMinus;

	/** Pos X plus */
	public int posXPlus;

	public int posY;

	/** Pos Y clipped */
	public int posYClip;

	/** Pos Y minus */
	public int posYMinus;
	/** Pos Y plus */
	public int posYPlus;
	public int posZ;
	/** Pos Z clipped */
	public int posZClip;

	/** Pos Z minus */
	public int posZMinus;

	/** Pos Z plus */
	public int posZPlus;
	/** Axis aligned bounding box */
	public AxisAlignedBB rendererBoundingBox;
	public RenderGlobal renderGlobal;
	protected boolean skipAllRenderPasses;
	/** Should this renderer skip this render pass */
	public boolean[] skipRenderPass;
	public float sortDistanceToEntitySquared;
	protected Tessellator tessellator;
	protected List tileEntities;
	public List tileEntityRenderers;
	protected TesselatorVertexState vertexState;
	public double visibleFromX;
	public double visibleFromY;
	public double visibleFromZ;
	/** Reference to the World object. */
	public World worldObj;

	public WorldRenderer(World par1World, List par2List, int par3, int par4,
			int par5, int par6) {
		tessellator = Tessellator.instance;
		skipRenderPass = new boolean[] { true, true };
		tileEntityRenderers = new ArrayList();
		isVisibleFromPosition = false;
		isInFrustrumFully = false;
		needsBoxUpdate = false;
		isUpdating = false;
		skipAllRenderPasses = true;
		renderGlobal = Minecraft.getMinecraft().renderGlobal;
		glRenderList = -1;
		isInFrustum = false;
		isVisible = true;
		isInitialized = false;
		worldObj = par1World;
		vertexState = null;
		tileEntities = par2List;
		glRenderList = par6;
		posX = -999;
		setPosition(par3, par4, par5);
		needsUpdate = false;
	}

	/**
	 * Renders the occlusion query GL List
	 */
	public void callOcclusionQueryList() {
		GL11.glCallList(glRenderList + 2);
	}

	/**
	 * Returns the distance of this chunk renderer to the entity without
	 * performing the final normalizing square root, for performance reasons.
	 */
	public float distanceToEntitySquared(Entity par1Entity) {
		final float var2 = (float) (par1Entity.posX - posXPlus);
		final float var3 = (float) (par1Entity.posY - posYPlus);
		final float var4 = (float) (par1Entity.posZ - posZPlus);
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	/**
	 * Takes in the pass the call list is being requested for. Args: renderPass
	 */
	public int getGLCallListForPass(int par1) {
		return glRenderList + par1;
	}

	/**
	 * Marks the current renderer data as dirty and needing to be updated.
	 */
	public void markDirty() {
		needsUpdate = true;
	}

	protected void postRenderBlocks(int renderpass,
			EntityLivingBase entityLiving) {
		if (Config.isTranslucentBlocksFancy() && renderpass == 1
				&& !skipRenderPass[renderpass]) {
			vertexState = tessellator.getVertexState((float) entityLiving.posX,
					(float) entityLiving.posY, (float) entityLiving.posZ);
		}

		bytesDrawn += tessellator.draw();
		Reflector.callVoid(Reflector.ForgeHooksClient_onPostRenderWorld,
				new Object[] { this, Integer.valueOf(renderpass) });
		tessellator.setRenderingChunk(false);

		if (!Config.isFastRender()) {
			GL11.glPopMatrix();
		}

		GL11.glEndList();
		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
	}

	protected void preRenderBlocks(int renderpass) {
		GL11.glNewList(glRenderList + renderpass, GL11.GL_COMPILE);
		tessellator.setRenderingChunk(true);

		if (Config.isFastRender()) {
			Reflector.callVoid(Reflector.ForgeHooksClient_onPreRenderWorld,
					new Object[] { this, Integer.valueOf(renderpass) });
			tessellator.startDrawingQuads();
			tessellator.setTranslation(-globalChunkOffsetX, 0.0D,
					-globalChunkOffsetZ);
		} else {
			GL11.glPushMatrix();
			setupGLTranslation();
			final float var2 = 1.000001F;
			GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
			GL11.glScalef(var2, var2, var2);
			GL11.glTranslatef(8.0F, 8.0F, 8.0F);
			Reflector.callVoid(Reflector.ForgeHooksClient_onPreRenderWorld,
					new Object[] { this, Integer.valueOf(renderpass) });
			tessellator.startDrawingQuads();
			tessellator.setTranslation(-posX, -posY, -posZ);
		}
	}

	/**
	 * When called this renderer won't draw anymore until its gets initialized
	 * again
	 */
	public void setDontDraw() {
		for (int var1 = 0; var1 < 2; ++var1) {
			skipRenderPass[var1] = true;
		}

		skipAllRenderPasses = true;
		isInFrustum = false;
		isInitialized = false;
		vertexState = null;
	}

	/**
	 * Sets a new position for the renderer and setting it up so it can be
	 * reloaded with the new data for that position
	 */
	public void setPosition(int par1, int par2, int par3) {
		if (par1 != posX || par2 != posY || par3 != posZ) {
			setDontDraw();
			posX = par1;
			posY = par2;
			posZ = par3;
			posXPlus = par1 + 8;
			posYPlus = par2 + 8;
			posZPlus = par3 + 8;
			posXClip = par1 & 1023;
			posYClip = par2;
			posZClip = par3 & 1023;
			posXMinus = par1 - posXClip;
			posYMinus = par2 - posYClip;
			posZMinus = par3 - posZClip;
			rendererBoundingBox = AxisAlignedBB.getBoundingBox(par1, par2,
					par3, par1 + 16, par2 + 16, par3 + 16);
			needsBoxUpdate = true;
			markDirty();
			isVisibleFromPosition = false;
		}
	}

	protected void setupGLTranslation() {
		GL11.glTranslatef(posXClip, posYClip, posZClip);
	}

	/**
	 * Checks if all render passes are to be skipped. Returns false if the
	 * renderer is not initialized
	 */
	public boolean skipAllRenderPasses() {
		return skipAllRenderPasses;
	}

	public void stopRendering() {
		setDontDraw();
		worldObj = null;
	}

	protected void updateFinished() {
		if (!skipAllRenderPasses && !inSortedWorldRenderers) {
			Minecraft.getMinecraft().renderGlobal
					.addToSortedWorldRenderers(this);
		}
	}

	public void updateInFrustum(ICamera par1ICamera) {
		isInFrustum = par1ICamera.isBoundingBoxInFrustum(rendererBoundingBox);

		if (isInFrustum && Config.isOcclusionFancy()) {
			isInFrustrumFully = par1ICamera
					.isBoundingBoxInFrustumFully(rendererBoundingBox);
		} else {
			isInFrustrumFully = false;
		}
	}

	/**
	 * Will update this chunk renderer
	 */
	public void updateRenderer(EntityLivingBase p_147892_1_) {
		if (worldObj != null) {
			if (needsBoxUpdate) {
				GL11.glNewList(glRenderList + 2, GL11.GL_COMPILE);
				Render.renderAABB(AxisAlignedBB.getBoundingBox(posXClip,
						posYClip, posZClip, posXClip + 16, posYClip + 16,
						posZClip + 16));
				GL11.glEndList();
				needsBoxUpdate = false;
			}

			isVisible = true;
			isVisibleFromPosition = false;

			if (needsUpdate) {
				needsUpdate = false;
				final int xMin = posX;
				final int yMin = posY;
				final int zMain = posZ;
				final int xMax = posX + 16;
				final int yMax = posY + 16;
				final int zMax = posZ + 16;

				for (int var26 = 0; var26 < 2; ++var26) {
					skipRenderPass[var26] = true;
				}

				skipAllRenderPasses = true;

				if (Reflector.LightCache.exists()) {
					final Object var29 = Reflector
							.getFieldValue(Reflector.LightCache_cache);
					Reflector.callVoid(var29, Reflector.LightCache_clear,
							new Object[0]);
					Reflector.callVoid(Reflector.BlockCoord_resetPool,
							new Object[0]);
				}

				Chunk.isLit = false;
				final HashSet var30 = new HashSet();
				var30.addAll(tileEntityRenderers);
				tileEntityRenderers.clear();
				final Minecraft var9 = Minecraft.getMinecraft();
				final EntityLivingBase var10 = var9.renderViewEntity;
				final int viewEntityPosX = MathHelper.floor_double(var10.posX);
				final int viewEntityPosY = MathHelper.floor_double(var10.posY);
				final int viewEntityPosZ = MathHelper.floor_double(var10.posZ);
				final byte var14 = 1;
				final ChunkCache chunkcache = new ChunkCache(worldObj, xMin
						- var14, yMin - var14, zMain - var14, xMax + var14,
						yMax + var14, zMax + var14, var14);

				if (!chunkcache.extendedLevelsInChunkCache()) {
					++chunksUpdated;
					final RenderBlocks var27 = new RenderBlocks(chunkcache);
					Reflector.callVoid(
							Reflector.ForgeHooksClient_setWorldRendererRB,
							new Object[] { var27 });
					bytesDrawn = 0;
					vertexState = null;
					tessellator = Tessellator.instance;
					final boolean hasForge = Reflector.ForgeHooksClient
							.exists();

					for (int renderPass = 0; renderPass < 2; ++renderPass) {
						boolean renderNextPass = false;
						boolean hasRenderedBlocks = false;
						boolean hasGlList = false;

						for (int y = yMin; y < yMax; ++y) {
							for (int z = zMain; z < zMax; ++z) {
								for (int x = xMin; x < xMax; ++x) {
									final Block block = chunkcache.getBlock(x,
											y, z);

									if (block.getMaterial() != Material.air) {
										if (!hasGlList) {
											hasGlList = true;
											preRenderBlocks(renderPass);
										}

										boolean hasTileEntity = false;

										if (hasForge) {
											hasTileEntity = Reflector
													.callBoolean(
															block,
															Reflector.ForgeBlock_hasTileEntity,
															new Object[] { Integer
																	.valueOf(chunkcache
																			.getBlockMetadata(
																					x,
																					y,
																					z)) });
										} else {
											hasTileEntity = block
													.hasTileEntity();
										}

										if (renderPass == 0 && hasTileEntity) {
											final TileEntity blockPass = chunkcache
													.getTileEntity(x, y, z);

											if (TileEntityRendererDispatcher.instance
													.hasSpecialRenderer(blockPass)) {
												tileEntityRenderers
														.add(blockPass);
											}
										}

										final int var32 = block
												.getRenderBlockPass();

										if (var32 > renderPass) {
											renderNextPass = true;
										}

										boolean canRender = var32 == renderPass;

										if (Reflector.ForgeBlock_canRenderInPass
												.exists()) {
											canRender = Reflector
													.callBoolean(
															block,
															Reflector.ForgeBlock_canRenderInPass,
															new Object[] { Integer
																	.valueOf(renderPass) });
										}

										if (canRender) {
											hasRenderedBlocks |= var27
													.renderBlockByRenderType(
															block, x, y, z);

											if (block.getRenderType() == 0
													&& x == viewEntityPosX
													&& y == viewEntityPosY
													&& z == viewEntityPosZ) {
												var27.setRenderFromInside(true);
												var27.setRenderAllFaces(true);
												var27.renderBlockByRenderType(
														block, x, y, z);
												var27.setRenderFromInside(false);
												var27.setRenderAllFaces(false);
											}
										}
									}
								}
							}
						}

						if (hasRenderedBlocks) {
							skipRenderPass[renderPass] = false;
						}

						if (hasGlList) {
							postRenderBlocks(renderPass, p_147892_1_);
						} else {
							hasRenderedBlocks = false;
						}

						if (!renderNextPass) {
							break;
						}
					}

					Reflector.callVoid(
							Reflector.ForgeHooksClient_setWorldRendererRB,
							new Object[] { null });
				}

				final HashSet var31 = new HashSet();
				var31.addAll(tileEntityRenderers);
				var31.removeAll(var30);
				tileEntities.addAll(var31);
				var30.removeAll(tileEntityRenderers);
				tileEntities.removeAll(var30);
				isChunkLit = Chunk.isLit;
				isInitialized = true;
				skipAllRenderPasses = skipRenderPass[0] && skipRenderPass[1];
				updateFinished();
			}
		}
	}

	public void updateRendererSort(EntityLivingBase p_147889_1_) {
		if (vertexState != null && !skipRenderPass[1]) {
			tessellator = Tessellator.instance;
			preRenderBlocks(1);
			tessellator.setVertexState(vertexState);
			postRenderBlocks(1, p_147889_1_);
		}
	}
}
