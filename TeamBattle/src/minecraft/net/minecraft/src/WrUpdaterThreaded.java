package net.minecraft.src;

import java.util.List;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

public class WrUpdaterThreaded implements IWrUpdater {
	public static boolean isBackgroundChunkLoading() {
		return true;
	}

	private boolean firstUpdate = true;
	private float timePerUpdateMs = 10.0F;
	private long updateStartTimeNs = 0L;
	private int updateTargetNum = 0;

	private WrUpdateThread updateThread = null;

	@Override
	public void clearAllUpdates() {
		if (updateThread != null) {
			updateThread.clearAllUpdates();
		}

		firstUpdate = true;
	}

	public WrUpdateThread createUpdateThread(Drawable displayDrawable) {
		if (updateThread != null)
			throw new IllegalStateException("UpdateThread is already existing");
		else {
			try {
				final Pbuffer e = new Pbuffer(1, 1, new PixelFormat(),
						displayDrawable);
				updateThread = new WrUpdateThread(e);
				updateThread.setPriority(1);
				updateThread.start();
				updateThread.pause();
				return updateThread;
			} catch (final Exception var3) {
				throw new RuntimeException(var3);
			}
		}
	}

	private void delayedInit() {
		if (updateThread == null) {
			createUpdateThread(Display.getDrawable());
		}
	}

	@Override
	public void finishCurrentUpdate() {
		if (updateThread != null) {
			updateThread.unpauseToEndOfUpdate();
		}
	}

	@Override
	public void initialize() {
	}

	public boolean isUpdateThread() {
		return Thread.currentThread() == updateThread;
	}

	@Override
	public WorldRenderer makeWorldRenderer(World worldObj, List tileEntities,
			int x, int y, int z, int glRenderListBase) {
		return new WorldRendererThreaded(worldObj, tileEntities, x, y, z,
				glRenderListBase);
	}

	@Override
	public void pauseBackgroundUpdates() {
		if (updateThread != null) {
			updateThread.pause();
		}
	}

	@Override
	public void postRender() {
		if (updateThread != null) {
			if (updateTargetNum > 0) {
				System.nanoTime();
				final float targetRunTime = timePerUpdateMs
						* (1.0F + (updateTargetNum - 1) / 2.0F);

				if (targetRunTime > 0.0F) {
					final int sleepTimeMsInt = (int) targetRunTime;
					Config.sleep(sleepTimeMsInt);
				}

				updateThread.pause();
			}

			final float deltaTime1 = 0.2F;

			if (updateTargetNum > 0) {
				final int updateCount = updateThread.resetUpdateCount();

				if (updateCount < updateTargetNum) {
					timePerUpdateMs += deltaTime1;
				}

				if (updateCount > updateTargetNum) {
					timePerUpdateMs -= deltaTime1;
				}

				if (updateCount == updateTargetNum) {
					timePerUpdateMs -= deltaTime1;
				}
			} else {
				timePerUpdateMs -= deltaTime1 / 5.0F;
			}

			if (timePerUpdateMs < 0.0F) {
				timePerUpdateMs = 0.0F;
			}

			updateStartTimeNs = System.nanoTime();
		}
	}

	@Override
	public void preRender(RenderGlobal rg, EntityLivingBase player) {
		updateTargetNum = 0;

		if (updateThread != null) {
			if (updateStartTimeNs == 0L) {
				updateStartTimeNs = System.nanoTime();
			}

			if (updateThread.hasWorkToDo()) {
				updateTargetNum = Config.getUpdatesPerFrame();

				if (Config.isDynamicUpdates() && !rg.isMoving(player)) {
					updateTargetNum *= 3;
				}

				updateTargetNum = Math.min(updateTargetNum,
						updateThread.getPendingUpdatesCount());

				if (updateTargetNum > 0) {
					updateThread.unpause();
				}
			}
		}
	}

	@Override
	public void resumeBackgroundUpdates() {
		if (updateThread != null) {
			updateThread.unpause();
		}
	}

	@Override
	public void terminate() {
		if (updateThread != null) {
			updateThread.terminate();
			updateThread.unpauseToEndOfUpdate();
		}
	}

	private void updateRenderer(WorldRenderer wr, EntityLivingBase entityLiving) {
		final WrUpdateThread ut = updateThread;

		if (ut != null) {
			ut.addRendererToUpdate(wr, false);
			wr.needsUpdate = false;
		} else {
			wr.updateRenderer(entityLiving);
			wr.needsUpdate = false;
			wr.isUpdating = false;
		}
	}

	@Override
	public boolean updateRenderers(RenderGlobal rg,
			EntityLivingBase entityliving, boolean flag) {
		delayedInit();

		if (rg.worldRenderersToUpdate.size() <= 0)
			return true;
		else {
			int num = 0;
			final byte NOT_IN_FRUSTRUM_MUL = 4;
			int numValid = 0;
			WorldRenderer wrBest = null;
			float distSqBest = Float.MAX_VALUE;
			int indexBest = -1;
			int maxUpdateNum;
			float dstIndex;

			for (maxUpdateNum = 0; maxUpdateNum < rg.worldRenderersToUpdate
					.size(); ++maxUpdateNum) {
				final WorldRenderer turboMode = (WorldRenderer) rg.worldRenderersToUpdate
						.get(maxUpdateNum);

				if (turboMode != null) {
					++numValid;

					if (!turboMode.isUpdating) {
						if (!turboMode.needsUpdate) {
							rg.worldRenderersToUpdate.set(maxUpdateNum,
									(Object) null);
						} else {
							dstIndex = turboMode
									.distanceToEntitySquared(entityliving);

							if (dstIndex < 512.0F) {
								if (dstIndex < 256.0F && rg.isActingNow()
										&& turboMode.isInFrustum || firstUpdate) {
									if (updateThread != null) {
										updateThread.unpauseToEndOfUpdate();
									}

									turboMode.updateRenderer(entityliving);
									turboMode.needsUpdate = false;
									rg.worldRenderersToUpdate.set(maxUpdateNum,
											(Object) null);
									++num;
									continue;
								}

								if (updateThread != null) {
									updateThread.addRendererToUpdate(turboMode,
											true);
									turboMode.needsUpdate = false;
									rg.worldRenderersToUpdate.set(maxUpdateNum,
											(Object) null);
									++num;
									continue;
								}
							}

							if (!turboMode.isInFrustum) {
								dstIndex *= NOT_IN_FRUSTRUM_MUL;
							}

							if (wrBest == null) {
								wrBest = turboMode;
								distSqBest = dstIndex;
								indexBest = maxUpdateNum;
							} else if (dstIndex < distSqBest) {
								wrBest = turboMode;
								distSqBest = dstIndex;
								indexBest = maxUpdateNum;
							}
						}
					}
				}
			}

			maxUpdateNum = Config.getUpdatesPerFrame();
			if (Config.isDynamicUpdates() && !rg.isMoving(entityliving)) {
				maxUpdateNum *= 3;
			}

			if (updateThread != null) {
				maxUpdateNum = updateThread.getUpdateCapacity();

				if (maxUpdateNum <= 0)
					return true;
			}

			int i;

			if (wrBest != null) {
				updateRenderer(wrBest, entityliving);
				rg.worldRenderersToUpdate.set(indexBest, (Object) null);
				++num;
				dstIndex = distSqBest / 5.0F;

				for (i = 0; i < rg.worldRenderersToUpdate.size()
						&& num < maxUpdateNum; ++i) {
					final WorldRenderer wr = (WorldRenderer) rg.worldRenderersToUpdate
							.get(i);

					if (wr != null && !wr.isUpdating) {
						float distSq = wr.distanceToEntitySquared(entityliving);

						if (!wr.isInFrustum) {
							distSq *= NOT_IN_FRUSTRUM_MUL;
						}

						final float diffDistSq = Math.abs(distSq - distSqBest);

						if (diffDistSq < dstIndex) {
							updateRenderer(wr, entityliving);
							rg.worldRenderersToUpdate.set(i, (Object) null);
							++num;
						}
					}
				}
			}

			if (numValid == 0) {
				rg.worldRenderersToUpdate.clear();
			}

			if (rg.worldRenderersToUpdate.size() > 100
					&& numValid < rg.worldRenderersToUpdate.size() * 4 / 5) {
				int var18 = 0;

				for (i = 0; i < rg.worldRenderersToUpdate.size(); ++i) {
					final Object var19 = rg.worldRenderersToUpdate.get(i);

					if (var19 != null) {
						if (i != var18) {
							rg.worldRenderersToUpdate.set(var18, var19);
						}

						++var18;
					}
				}

				for (i = rg.worldRenderersToUpdate.size() - 1; i >= var18; --i) {
					rg.worldRenderersToUpdate.remove(i);
				}
			}

			firstUpdate = false;
			return true;
		}
	}
}
