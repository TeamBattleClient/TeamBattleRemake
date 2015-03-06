package net.minecraft.src;

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;

public class WorldRendererThreaded extends WorldRenderer {
	private final int glRenderListBoundingBox;
	private int glRenderListWork;
	public boolean[] tempSkipRenderPass = new boolean[2];
	public TesselatorVertexState tempVertexState;

	public WorldRendererThreaded(World par1World, List par2List, int par3,
			int par4, int par5, int par6) {
		super(par1World, par2List, par3, par4, par5, par6);
		final RenderGlobal renderGlobal = Minecraft.getMinecraft().renderGlobal;
		glRenderListWork = renderGlobal.displayListAllocator
				.allocateDisplayLists(2);
		glRenderListBoundingBox = glRenderList + 2;
	}

	/**
	 * Renders the occlusion query GL List
	 */
	@Override
	public void callOcclusionQueryList() {
		GL11.glCallList(glRenderListBoundingBox);
	}

	public void finishUpdate() {
		final int temp = glRenderList;
		glRenderList = glRenderListWork;
		glRenderListWork = temp;
		int lightCache;

		for (lightCache = 0; lightCache < 2; ++lightCache) {
			if (!skipRenderPass[lightCache]) {
				GL11.glNewList(glRenderListWork + lightCache, GL11.GL_COMPILE);
				GL11.glEndList();
			}
		}

		for (lightCache = 0; lightCache < 2; ++lightCache) {
			skipRenderPass[lightCache] = tempSkipRenderPass[lightCache];
		}

		skipAllRenderPasses = skipRenderPass[0] && skipRenderPass[1];

		if (needsBoxUpdate && !skipAllRenderPasses()) {
			GL11.glNewList(glRenderListBoundingBox, GL11.GL_COMPILE);
			Render.renderAABB(AxisAlignedBB.getBoundingBox(posXClip, posYClip,
					posZClip, posXClip + 16, posYClip + 16, posZClip + 16));
			GL11.glEndList();
			needsBoxUpdate = false;
		}

		vertexState = tempVertexState;
		isVisible = true;
		isVisibleFromPosition = false;

		if (Reflector.LightCache.exists()) {
			final Object var3 = Reflector
					.getFieldValue(Reflector.LightCache_cache);
			Reflector.callVoid(var3, Reflector.LightCache_clear, new Object[0]);
			Reflector.callVoid(Reflector.BlockCoord_resetPool, new Object[0]);
		}

		updateFinished();
	}

	protected void postRenderBlocksThreaded(int renderpass,
			EntityLivingBase entityLiving) {
		if (Config.isTranslucentBlocksFancy() && renderpass == 1
				&& !tempSkipRenderPass[renderpass]) {
			tempVertexState = tessellator.getVertexState(
					(float) entityLiving.posX, (float) entityLiving.posY,
					(float) entityLiving.posZ);
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

	protected void preRenderBlocksThreaded(int renderpass) {
		GL11.glNewList(glRenderListWork + renderpass, GL11.GL_COMPILE);
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
	 * Sets a new position for the renderer and setting it up so it can be
	 * reloaded with the new data for that position
	 */
	@Override
	public void setPosition(int px, int py, int pz) {
		if (isUpdating) {
			WrUpdates.finishCurrentUpdate();
		}

		super.setPosition(px, py, pz);
	}

	public void updateRenderer() {
		if (worldObj != null) {
			this.updateRenderer((IWrUpdateListener) null);
			finishUpdate();
		}
	}

	public void updateRenderer(IWrUpdateListener updateListener) {
		if (worldObj != null) {
			needsUpdate = false;
			final int xMin = posX;
			final int yMin = posY;
			final int zMin = posZ;
			final int xMax = posX + 16;
			final int yMax = posY + 16;
			final int zMax = posZ + 16;

			for (int hashset = 0; hashset < tempSkipRenderPass.length; ++hashset) {
				tempSkipRenderPass[hashset] = true;
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
			final byte one = 1;
			final ChunkCache chunkcache = new ChunkCache(worldObj, xMin - one,
					yMin - one, zMin - one, xMax + one, yMax + one, zMax + one,
					one);

			if (!chunkcache.extendedLevelsInChunkCache()) {
				++chunksUpdated;
				final RenderBlocks hashset1 = new RenderBlocks(chunkcache);
				Reflector.callVoid(
						Reflector.ForgeHooksClient_setWorldRendererRB,
						new Object[] { hashset1 });
				bytesDrawn = 0;
				tempVertexState = null;
				tessellator = Tessellator.instance;
				final boolean hasForge = Reflector.ForgeHooksClient.exists();
				final WrUpdateControl uc = new WrUpdateControl();

				for (int renderPass = 0; renderPass < 2; ++renderPass) {
					uc.setRenderPass(renderPass);
					boolean renderNextPass = false;
					boolean hasRenderedBlocks = false;
					boolean hasGlList = false;

					for (int y = yMin; y < yMax; ++y) {
						if (hasRenderedBlocks && updateListener != null) {
							updateListener.updating(uc);
							tessellator = Tessellator.instance;
						}

						for (int z = zMin; z < zMax; ++z) {
							for (int x = xMin; x < xMax; ++x) {
								final Block block = chunkcache
										.getBlock(x, y, z);

								if (block.getMaterial() != Material.air) {
									if (!hasGlList) {
										hasGlList = true;
										preRenderBlocksThreaded(renderPass);
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
										hasTileEntity = block.hasTileEntity();
									}

									if (renderPass == 0 && hasTileEntity) {
										final TileEntity blockPass = chunkcache
												.getTileEntity(x, y, z);

										if (TileEntityRendererDispatcher.instance
												.hasSpecialRenderer(blockPass)) {
											tileEntityRenderers.add(blockPass);
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
										hasRenderedBlocks |= hashset1
												.renderBlockByRenderType(block,
														x, y, z);

										if (block.getRenderType() == 0
												&& x == viewEntityPosX
												&& y == viewEntityPosY
												&& z == viewEntityPosZ) {
											hashset1.setRenderFromInside(true);
											hashset1.setRenderAllFaces(true);
											hashset1.renderBlockByRenderType(
													block, x, y, z);
											hashset1.setRenderFromInside(false);
											hashset1.setRenderAllFaces(false);
										}
									}
								}
							}
						}
					}

					if (hasRenderedBlocks) {
						tempSkipRenderPass[renderPass] = false;
					}

					if (hasGlList) {
						if (updateListener != null) {
							updateListener.updating(uc);
						}

						tessellator = Tessellator.instance;
						postRenderBlocksThreaded(renderPass,
								renderGlobal.renderViewEntity);
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
		}
	}
}
