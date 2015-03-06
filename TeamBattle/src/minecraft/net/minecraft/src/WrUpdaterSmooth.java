package net.minecraft.src;

import java.util.List;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class WrUpdaterSmooth implements IWrUpdater {
	private WorldRendererSmooth currentUpdateRenderer = null;
	private int renderersFound = 0;
	private int renderersUpdated = 0;
	private long updateStartTimeNs = 0L;
	private long updateTimeNs = 10000000L;

	@Override
	public void clearAllUpdates() {
		finishCurrentUpdate();
	}

	@Override
	public void finishCurrentUpdate() {
		if (currentUpdateRenderer != null) {
			currentUpdateRenderer.updateRenderer();
			currentUpdateRenderer = null;
		}
	}

	@Override
	public void initialize() {
	}

	@Override
	public WorldRenderer makeWorldRenderer(World worldObj, List tileEntities,
			int x, int y, int z, int glRenderListBase) {
		return new WorldRendererSmooth(worldObj, tileEntities, x, y, z,
				glRenderListBase);
	}

	@Override
	public void pauseBackgroundUpdates() {
	}

	@Override
	public void postRender() {
	}

	@Override
	public void preRender(RenderGlobal rg, EntityLivingBase player) {
	}

	@Override
	public void resumeBackgroundUpdates() {
	}

	@Override
	public void terminate() {
	}

	private boolean updateRenderer(WorldRendererSmooth wr) {
		final long finishTime = updateStartTimeNs + updateTimeNs;
		wr.needsUpdate = false;
		final boolean ready = wr.updateRenderer(finishTime);

		if (!ready) {
			currentUpdateRenderer = wr;
			return false;
		} else {
			wr.finishUpdate();
			currentUpdateRenderer = null;
			return true;
		}
	}

	@Override
	public boolean updateRenderers(RenderGlobal rg,
			EntityLivingBase entityliving, boolean flag) {
		updateStartTimeNs = System.nanoTime();
		final long finishTimeNs = updateStartTimeNs + updateTimeNs;
		int maxNum = Config.getUpdatesPerFrame();

		if (Config.isDynamicUpdates() && !rg.isMoving(entityliving)) {
			maxNum *= 3;
		}

		renderersUpdated = 0;

		do {
			renderersFound = 0;
			updateRenderersImpl(rg, entityliving, flag);
		} while (renderersFound > 0 && System.nanoTime() - finishTimeNs < 0L);

		if (renderersFound > 0) {
			maxNum = Math.min(maxNum, renderersFound);
			final long diff = 400000L;

			if (renderersUpdated > maxNum) {
				updateTimeNs -= 2L * diff;
			}

			if (renderersUpdated < maxNum) {
				updateTimeNs += diff;
			}
		} else {
			updateTimeNs = 0L;
			updateTimeNs -= 200000L;
		}

		if (updateTimeNs < 0L) {
			updateTimeNs = 0L;
		}

		return renderersUpdated > 0;
	}

	private void updateRenderersImpl(RenderGlobal rg,
			EntityLivingBase entityliving, boolean flag) {
		renderersFound = 0;
		boolean currentUpdateFinished = true;

		if (currentUpdateRenderer != null) {
			++renderersFound;
			currentUpdateFinished = updateRenderer(currentUpdateRenderer);

			if (currentUpdateFinished) {
				++renderersUpdated;
			}
		}

		if (rg.worldRenderersToUpdate.size() > 0) {
			final byte NOT_IN_FRUSTRUM_MUL = 4;
			WorldRendererSmooth wrBest = null;
			float distSqBest = Float.MAX_VALUE;
			int indexBest = -1;
			int dstIndex;

			for (dstIndex = 0; dstIndex < rg.worldRenderersToUpdate.size(); ++dstIndex) {
				final WorldRendererSmooth i = (WorldRendererSmooth) rg.worldRenderersToUpdate
						.get(dstIndex);

				if (i != null) {
					++renderersFound;

					if (!i.needsUpdate) {
						rg.worldRenderersToUpdate.set(dstIndex, (Object) null);
					} else {
						float wr = i.distanceToEntitySquared(entityliving);

						if (wr <= 256.0F && rg.isActingNow()) {
							i.updateRenderer();
							i.needsUpdate = false;
							rg.worldRenderersToUpdate.set(dstIndex,
									(Object) null);
							++renderersUpdated;
						} else {
							if (!i.isInFrustum) {
								wr *= NOT_IN_FRUSTRUM_MUL;
							}

							if (wrBest == null) {
								wrBest = i;
								distSqBest = wr;
								indexBest = dstIndex;
							} else if (wr < distSqBest) {
								wrBest = i;
								distSqBest = wr;
								indexBest = dstIndex;
							}
						}
					}
				}
			}

			if (currentUpdateRenderer == null || currentUpdateFinished) {
				int var15;

				if (wrBest != null) {
					rg.worldRenderersToUpdate.set(indexBest, (Object) null);

					if (!updateRenderer(wrBest))
						return;

					++renderersUpdated;

					if (System.nanoTime() > updateStartTimeNs + updateTimeNs)
						return;

					final float var14 = distSqBest / 5.0F;

					for (var15 = 0; var15 < rg.worldRenderersToUpdate.size(); ++var15) {
						final WorldRendererSmooth var16 = (WorldRendererSmooth) rg.worldRenderersToUpdate
								.get(var15);

						if (var16 != null) {
							float distSq = var16
									.distanceToEntitySquared(entityliving);

							if (!var16.isInFrustum) {
								distSq *= NOT_IN_FRUSTRUM_MUL;
							}

							final float diffDistSq = Math.abs(distSq
									- distSqBest);

							if (diffDistSq < var14) {
								rg.worldRenderersToUpdate.set(var15,
										(Object) null);

								if (!updateRenderer(var16))
									return;

								++renderersUpdated;

								if (System.nanoTime() > updateStartTimeNs
										+ updateTimeNs) {
									break;
								}
							}
						}
					}
				}

				if (renderersFound == 0) {
					rg.worldRenderersToUpdate.clear();
				}

				if (rg.worldRenderersToUpdate.size() > 100
						&& renderersFound < rg.worldRenderersToUpdate.size() * 4 / 5) {
					dstIndex = 0;

					for (var15 = 0; var15 < rg.worldRenderersToUpdate.size(); ++var15) {
						final Object var17 = rg.worldRenderersToUpdate
								.get(var15);

						if (var17 != null) {
							if (var15 != dstIndex) {
								rg.worldRenderersToUpdate.set(dstIndex, var17);
							}

							++dstIndex;
						}
					}

					for (var15 = rg.worldRenderersToUpdate.size() - 1; var15 >= dstIndex; --var15) {
						rg.worldRenderersToUpdate.remove(var15);
					}
				}
			}
		}
	}
}
