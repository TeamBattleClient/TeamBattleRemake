package net.minecraft.world.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
	/** Instance of ThreadedFileIOBase */
	public static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
	private volatile boolean isThreadWaiting;
	private volatile long savedIOCounter;
	private final List threadedIOQueue = Collections
			.synchronizedList(new ArrayList());
	private volatile long writeQueuedCounter;

	private ThreadedFileIOBase() {
		final Thread var1 = new Thread(this, "File IO Thread");
		var1.setPriority(1);
		var1.start();
	}

	/**
	 * Process the items that are in the queue
	 */
	private void processQueue() {
		for (int var1 = 0; var1 < threadedIOQueue.size(); ++var1) {
			final IThreadedFileIO var2 = (IThreadedFileIO) threadedIOQueue
					.get(var1);
			final boolean var3 = var2.writeNextIO();

			if (!var3) {
				threadedIOQueue.remove(var1--);
				++savedIOCounter;
			}

			try {
				Thread.sleep(isThreadWaiting ? 0L : 10L);
			} catch (final InterruptedException var6) {
				var6.printStackTrace();
			}
		}

		if (threadedIOQueue.isEmpty()) {
			try {
				Thread.sleep(25L);
			} catch (final InterruptedException var5) {
				var5.printStackTrace();
			}
		}
	}

	/**
	 * threaded io
	 */
	public void queueIO(IThreadedFileIO p_75735_1_) {
		if (!threadedIOQueue.contains(p_75735_1_)) {
			++writeQueuedCounter;
			threadedIOQueue.add(p_75735_1_);
		}
	}

	@Override
	public void run() {
		while (true) {
			processQueue();
		}
	}

	public void waitForFinish() throws InterruptedException {
		isThreadWaiting = true;

		while (writeQueuedCounter != savedIOCounter) {
			Thread.sleep(10L);
		}

		isThreadWaiting = false;
	}
}
