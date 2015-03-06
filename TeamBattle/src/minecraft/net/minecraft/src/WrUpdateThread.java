package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import org.lwjgl.opengl.Pbuffer;

public class WrUpdateThread extends Thread {
	static class NamelessClass7794369 {
	}

	private class ThreadUpdateControl implements IWrUpdateControl {
		private boolean paused;
		private IWrUpdateControl updateControl;

		private ThreadUpdateControl() {
			updateControl = null;
			paused = false;
		}

		ThreadUpdateControl(WrUpdateThread.NamelessClass7794369 x1) {
			this();
		}

		@Override
		public void pause() {
			if (!paused) {
				paused = true;
				updateControl.pause();
				Tessellator.instance = mainTessellator;
			}
		}

		@Override
		public void resume() {
			if (paused) {
				paused = false;
				Tessellator.instance = threadTessellator;
				updateControl.resume();
			}
		}

		public void setUpdateControl(IWrUpdateControl updateControl) {
			this.updateControl = updateControl;
		}
	}

	private class ThreadUpdateListener implements IWrUpdateListener {
		private final WrUpdateThread.ThreadUpdateControl tuc;

		private ThreadUpdateListener() {
			tuc = WrUpdateThread.this.new ThreadUpdateControl(
					(WrUpdateThread.NamelessClass7794369) null);
		}

		ThreadUpdateListener(WrUpdateThread.NamelessClass7794369 x1) {
			this();
		}

		@Override
		public void updating(IWrUpdateControl uc) {
			tuc.setUpdateControl(uc);
			checkCanWork(tuc);
		}
	}

	private boolean canWork;
	private boolean canWorkToEndOfUpdate;
	private WorldRendererThreaded currentRenderer;
	private final Object lock = new Object();
	private final Tessellator mainTessellator;
	private Pbuffer pbuffer = null;
	private boolean terminated;
	private final Tessellator threadTessellator;
	private int updateCount = 0;

	private final List updatedList = new LinkedList();

	private final List updateList = new LinkedList();

	private boolean working;

	public WrUpdateThread(Pbuffer pbuffer) {
		super("WrUpdateThread");
		mainTessellator = Tessellator.instance;
		threadTessellator = new Tessellator(2097152);
		working = false;
		currentRenderer = null;
		canWork = false;
		canWorkToEndOfUpdate = false;
		terminated = false;
		this.pbuffer = pbuffer;
	}

	public void addRendererToUpdate(WorldRenderer wr, boolean first) {
		synchronized (lock) {
			if (wr.isUpdating)
				throw new IllegalArgumentException("Renderer already updating");
			else {
				if (first) {
					updateList.add(0, wr);
				} else {
					updateList.add(wr);
				}

				wr.isUpdating = true;
				lock.notifyAll();
			}
		}
	}

	private void checkCanWork(IWrUpdateControl uc) {
		Thread.yield();
		synchronized (lock) {
			while (!canWork
					&& (!canWorkToEndOfUpdate || currentRenderer == null)) {
				if (uc != null) {
					uc.pause();
				}

				working = false;
				lock.notifyAll();

				try {
					lock.wait();
				} catch (final InterruptedException var5) {
					;
				}
			}

			working = true;

			if (uc != null) {
				uc.resume();
			}

			lock.notifyAll();
		}
	}

	public void clearAllUpdates() {
		synchronized (lock) {
			unpauseToEndOfUpdate();

			for (int i = 0; i < updateList.size(); ++i) {
				final WorldRenderer wr = (WorldRenderer) updateList.get(i);
				wr.needsUpdate = true;
				wr.isUpdating = false;
			}

			updateList.clear();
			lock.notifyAll();
		}
	}

	private void finishUpdatedRenderers() {
		synchronized (lock) {
			for (int i = 0; i < updatedList.size(); ++i) {
				final WorldRendererThreaded wr = (WorldRendererThreaded) updatedList
						.get(i);
				wr.finishUpdate();
				wr.isUpdating = false;
			}

			updatedList.clear();
		}
	}

	public int getPendingUpdatesCount() {
		synchronized (lock) {
			int count = updateList.size();

			if (currentRenderer != null) {
				++count;
			}

			return count;
		}
	}

	private WorldRendererThreaded getRendererToUpdate() {
		synchronized (lock) {
			while (updateList.size() <= 0) {
				try {
					lock.wait(2000L);

					if (terminated) {
						final Object var10000 = null;
						return (WorldRendererThreaded) var10000;
					}
				} catch (final InterruptedException var4) {
					;
				}
			}

			final WorldRendererThreaded wrt = (WorldRendererThreaded) updateList
					.remove(0);
			lock.notifyAll();
			return wrt;
		}
	}

	public int getUpdateCapacity() {
		synchronized (lock) {
			return updateList.size() > 10 ? 0 : 10 - updateList.size();
		}
	}

	public boolean hasWorkToDo() {
		synchronized (lock) {
			return updateList.size() > 0 ? true
					: currentRenderer != null ? true : working;
		}
	}

	public void pause() {
		synchronized (lock) {
			canWork = false;
			canWorkToEndOfUpdate = false;
			lock.notifyAll();

			while (working) {
				try {
					lock.wait();
				} catch (final InterruptedException var4) {
					;
				}
			}

			finishUpdatedRenderers();
		}
	}

	private void rendererUpdated(WorldRenderer wr) {
		synchronized (lock) {
			updatedList.add(wr);
			++updateCount;
			currentRenderer = null;
			working = false;
			lock.notifyAll();
		}
	}

	public int resetUpdateCount() {
		synchronized (lock) {
			final int count = updateCount;
			updateCount = 0;
			return count;
		}
	}

	@Override
	public void run() {
		try {
			pbuffer.makeCurrent();
		} catch (final Exception var8) {
			var8.printStackTrace();
		}

		final WrUpdateThread.ThreadUpdateListener updateListener = new WrUpdateThread.ThreadUpdateListener(
				(WrUpdateThread.NamelessClass7794369) null);

		while (!Thread.interrupted() && !terminated) {
			try {
				final WorldRendererThreaded e = getRendererToUpdate();

				if (e == null)
					return;

				checkCanWork((IWrUpdateControl) null);

				try {
					currentRenderer = e;
					Tessellator.instance = threadTessellator;
					e.updateRenderer(updateListener);
				} finally {
					Tessellator.instance = mainTessellator;
				}

				rendererUpdated(e);
			} catch (final Exception var9) {
				var9.printStackTrace();

				if (currentRenderer != null) {
					currentRenderer.isUpdating = false;
					currentRenderer.needsUpdate = true;
				}

				currentRenderer = null;
				working = false;
			}
		}
	}

	public void terminate() {
		terminated = true;
	}

	public void unpause() {
		synchronized (lock) {
			if (working) {
				Config.warn("UpdateThread still working in unpause()!!!");
			}

			canWork = true;
			canWorkToEndOfUpdate = false;
			lock.notifyAll();
		}
	}

	public void unpauseToEndOfUpdate() {
		synchronized (lock) {
			if (working) {
				Config.warn("UpdateThread still working in unpause()!!!");
			}

			if (currentRenderer != null) {
				while (currentRenderer != null) {
					canWork = false;
					canWorkToEndOfUpdate = true;
					lock.notifyAll();

					try {
						lock.wait();
					} catch (final InterruptedException var4) {
						;
					}
				}

				pause();
			}
		}
	}
}
