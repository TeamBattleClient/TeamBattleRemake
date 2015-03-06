package net.minecraft.src;

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
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

public class WorldRendererSmooth extends WorldRenderer {
	public int[] activeListIndex = new int[] { 0, 0 };
	public int activeSet = 0;
	public int[][][] glWorkLists = new int[2][2][16];
	public boolean[] tempSkipRenderPass = new boolean[2];
	public TesselatorVertexState tempVertexState;
	private final WrUpdateState updateState = new WrUpdateState();

	public WorldRendererSmooth(World par1World, List par2List, int par3,
			int par4, int par5, int par6) {
		super(par1World, par2List, par3, par4, par5, par6);
	}

	private void checkGlWorkLists() {
		if (glWorkLists[0][0][0] <= 0) {
			final int glWorkBase = renderGlobal.displayListAllocator
					.allocateDisplayLists(64);

			for (int set = 0; set < 2; ++set) {
				final int setBase = glWorkBase + set * 2 * 16;

				for (int pass = 0; pass < 2; ++pass) {
					final int passBase = setBase + pass * 16;

					for (int t = 0; t < 16; ++t) {
						glWorkLists[set][pass][t] = passBase + t;
					}
				}
			}
		}
	}

	public void finishUpdate() {
		int pass;
		int i;
		int list;

		for (pass = 0; pass < 2; ++pass) {
			if (!skipRenderPass[pass]) {
				GL11.glNewList(glRenderList + pass, GL11.GL_COMPILE);

				for (i = 0; i <= activeListIndex[pass]; ++i) {
					list = glWorkLists[activeSet][pass][i];
					GL11.glCallList(list);
				}

				GL11.glEndList();
			}
		}

		if (activeSet == 0) {
			activeSet = 1;
		} else {
			activeSet = 0;
		}

		for (pass = 0; pass < 2; ++pass) {
			if (!skipRenderPass[pass]) {
				for (i = 0; i <= activeListIndex[pass]; ++i) {
					list = glWorkLists[activeSet][pass][i];
					GL11.glNewList(list, GL11.GL_COMPILE);
					GL11.glEndList();
				}
			}
		}

		for (pass = 0; pass < 2; ++pass) {
			activeListIndex[pass] = 0;
		}
	}

	protected void postRenderBlocksSmooth(int renderpass,
			EntityLivingBase entityLiving, boolean updateFinished) {
		if (Config.isTranslucentBlocksFancy() && renderpass == 1
				&& !tempSkipRenderPass[renderpass]) {
			final TesselatorVertexState tsv = tessellator.getVertexState(
					(float) entityLiving.posX, (float) entityLiving.posY,
					(float) entityLiving.posZ);

			if (tempVertexState == null) {
				tempVertexState = tsv;
			} else {
				tempVertexState.addTessellatorVertexState(tsv);
			}
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

	protected void preRenderBlocksSmooth(int renderpass) {
		GL11.glNewList(
				glWorkLists[activeSet][renderpass][activeListIndex[renderpass]],
				GL11.GL_COMPILE);
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
			this.updateRenderer(0L);
			finishUpdate();
		}
	}

	public boolean updateRenderer(long finishTime) {
		if (worldObj == null)
			return true;
		else {
			needsUpdate = false;

			if (!isUpdating) {
				if (needsBoxUpdate) {
					GL11.glNewList(glRenderList + 2, GL11.GL_COMPILE);
					Render.renderAABB(AxisAlignedBB.getBoundingBox(posXClip,
							posYClip, posZClip, posXClip + 16, posYClip + 16,
							posZClip + 16));
					GL11.glEndList();
					needsBoxUpdate = false;
				}

				if (Reflector.LightCache.exists()) {
					final Object xMin = Reflector
							.getFieldValue(Reflector.LightCache_cache);
					Reflector.callVoid(xMin, Reflector.LightCache_clear,
							new Object[0]);
					Reflector.callVoid(Reflector.BlockCoord_resetPool,
							new Object[0]);
				}

				Chunk.isLit = false;
			}

			final int var27 = posX;
			final int yMin = posY;
			final int zMin = posZ;
			final int xMax = posX + 16;
			final int yMax = posY + 16;
			final int zMax = posZ + 16;
			ChunkCache chunkcache = null;
			RenderBlocks renderblocks = null;
			HashSet setOldEntityRenders = null;
			int viewEntityPosX = 0;
			int viewEntityPosY = 0;
			int viewEntityPosZ = 0;

			if (!isUpdating) {
				for (int setNewEntityRenderers = 0; setNewEntityRenderers < 2; ++setNewEntityRenderers) {
					tempSkipRenderPass[setNewEntityRenderers] = true;
				}

				tempVertexState = null;
				final Minecraft var28 = Minecraft.getMinecraft();
				final EntityLivingBase renderPass = var28.renderViewEntity;
				viewEntityPosX = MathHelper.floor_double(renderPass.posX);
				viewEntityPosY = MathHelper.floor_double(renderPass.posY);
				viewEntityPosZ = MathHelper.floor_double(renderPass.posZ);
				final byte renderNextPass = 1;
				chunkcache = new ChunkCache(worldObj, var27 - renderNextPass,
						yMin - renderNextPass, zMin - renderNextPass, xMax
								+ renderNextPass, yMax + renderNextPass, zMax
								+ renderNextPass, renderNextPass);
				renderblocks = new RenderBlocks(chunkcache);
				Reflector.callVoid(
						Reflector.ForgeHooksClient_setWorldRendererRB,
						new Object[] { renderblocks });
				setOldEntityRenders = new HashSet();
				setOldEntityRenders.addAll(tileEntityRenderers);
				tileEntityRenderers.clear();
			}

			if (isUpdating || !chunkcache.extendedLevelsInChunkCache()) {
				bytesDrawn = 0;
				tessellator = Tessellator.instance;
				final boolean var29 = Reflector.ForgeHooksClient.exists();
				checkGlWorkLists();

				for (int var31 = 0; var31 < 2; ++var31) {
					boolean var32 = false;
					boolean hasRenderedBlocks = false;
					boolean hasGlList = false;

					for (int y = yMin; y < yMax; ++y) {
						if (isUpdating) {
							isUpdating = false;
							chunkcache = updateState.chunkcache;
							renderblocks = updateState.renderblocks;
							Reflector
									.callVoid(
											Reflector.ForgeHooksClient_setWorldRendererRB,
											new Object[] { renderblocks });
							setOldEntityRenders = updateState.setOldEntityRenders;
							viewEntityPosX = updateState.viewEntityPosX;
							viewEntityPosY = updateState.viewEntityPosY;
							viewEntityPosZ = updateState.viewEntityPosZ;
							var31 = updateState.renderPass;
							y = updateState.y;
							var32 = updateState.flag;
							hasRenderedBlocks = updateState.hasRenderedBlocks;
							hasGlList = updateState.hasGlList;

							if (hasGlList) {
								preRenderBlocksSmooth(var31);
							}
						} else if (hasGlList && finishTime != 0L
								&& System.nanoTime() - finishTime > 0L
								&& activeListIndex[var31] < 15) {
							if (hasRenderedBlocks) {
								tempSkipRenderPass[var31] = false;
							}

							postRenderBlocksSmooth(var31,
									renderGlobal.renderViewEntity, false);
							++activeListIndex[var31];
							updateState.chunkcache = chunkcache;
							updateState.renderblocks = renderblocks;
							updateState.setOldEntityRenders = setOldEntityRenders;
							updateState.viewEntityPosX = viewEntityPosX;
							updateState.viewEntityPosY = viewEntityPosY;
							updateState.viewEntityPosZ = viewEntityPosZ;
							updateState.renderPass = var31;
							updateState.y = y;
							updateState.flag = var32;
							updateState.hasRenderedBlocks = hasRenderedBlocks;
							updateState.hasGlList = hasGlList;
							isUpdating = true;
							return false;
						}

						for (int z = zMin; z < zMax; ++z) {
							for (int x = var27; x < xMax; ++x) {
								final Block block = chunkcache
										.getBlock(x, y, z);

								if (block.getMaterial() != Material.air) {
									if (!hasGlList) {
										hasGlList = true;
										preRenderBlocksSmooth(var31);
									}

									boolean hasTileEntity = false;

									if (var29) {
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

									if (var31 == 0 && hasTileEntity) {
										final TileEntity blockPass = chunkcache
												.getTileEntity(x, y, z);

										if (TileEntityRendererDispatcher.instance
												.hasSpecialRenderer(blockPass)) {
											tileEntityRenderers.add(blockPass);
										}
									}

									final int var33 = block
											.getRenderBlockPass();

									if (var33 > var31) {
										var32 = true;
									}

									boolean canRender = var33 == var31;

									if (Reflector.ForgeBlock_canRenderInPass
											.exists()) {
										canRender = Reflector
												.callBoolean(
														block,
														Reflector.ForgeBlock_canRenderInPass,
														new Object[] { Integer
																.valueOf(var31) });
									}

									if (canRender) {
										hasRenderedBlocks |= renderblocks
												.renderBlockByRenderType(block,
														x, y, z);

										if (block.getRenderType() == 0
												&& x == viewEntityPosX
												&& y == viewEntityPosY
												&& z == viewEntityPosZ) {
											renderblocks
													.setRenderFromInside(true);
											renderblocks
													.setRenderAllFaces(true);
											renderblocks
													.renderBlockByRenderType(
															block, x, y, z);
											renderblocks
													.setRenderFromInside(false);
											renderblocks
													.setRenderAllFaces(false);
										}
									}
								}
							}
						}
					}

					if (hasRenderedBlocks) {
						tempSkipRenderPass[var31] = false;
					}

					if (hasGlList) {
						postRenderBlocksSmooth(var31,
								renderGlobal.renderViewEntity, true);
					} else {
						hasRenderedBlocks = false;
					}

					if (!var32) {
						break;
					}
				}

				Reflector.callVoid(
						Reflector.ForgeHooksClient_setWorldRendererRB,
						new Object[] { null });
			}

			final HashSet var30 = new HashSet();
			var30.addAll(tileEntityRenderers);
			var30.removeAll(setOldEntityRenders);
			tileEntities.addAll(var30);
			setOldEntityRenders.removeAll(tileEntityRenderers);
			tileEntities.removeAll(setOldEntityRenders);
			isChunkLit = Chunk.isLit;
			isInitialized = true;
			++chunksUpdated;
			isVisible = true;
			isVisibleFromPosition = false;
			skipRenderPass[0] = tempSkipRenderPass[0];
			skipRenderPass[1] = tempSkipRenderPass[1];
			skipAllRenderPasses = skipRenderPass[0] && skipRenderPass[1];
			vertexState = tempVertexState;
			isUpdating = false;
			updateFinished();
			return true;
		}
	}
}
