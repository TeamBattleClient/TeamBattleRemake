package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.profiler.Profiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks {
	class EntityAITaskEntry {
		public EntityAIBase action;
		public int priority;

		public EntityAITaskEntry(int p_i1627_2_, EntityAIBase p_i1627_3_) {
			priority = p_i1627_2_;
			action = p_i1627_3_;
		}
	}

	private static final Logger logger = LogManager.getLogger();

	/** A list of EntityAITaskEntrys that are currently being executed. */
	private final List executingTaskEntries = new ArrayList();

	/** A list of EntityAITaskEntrys in EntityAITasks. */
	private final List taskEntries = new ArrayList();
	/** Instance of Profiler. */
	private final Profiler theProfiler;
	private int tickCount;

	private final int tickRate = 3;

	public EntityAITasks(Profiler p_i1628_1_) {
		theProfiler = p_i1628_1_;
	}

	public void addTask(int p_75776_1_, EntityAIBase p_75776_2_) {
		taskEntries.add(new EntityAITasks.EntityAITaskEntry(p_75776_1_,
				p_75776_2_));
	}

	/**
	 * Returns whether two EntityAITaskEntries can be executed concurrently
	 */
	private boolean areTasksCompatible(
			EntityAITasks.EntityAITaskEntry p_75777_1_,
			EntityAITasks.EntityAITaskEntry p_75777_2_) {
		return (p_75777_1_.action.getMutexBits() & p_75777_2_.action
				.getMutexBits()) == 0;
	}

	/**
	 * Determine if a specific AI Task should continue being executed.
	 */
	private boolean canContinue(EntityAITasks.EntityAITaskEntry p_75773_1_) {
		theProfiler.startSection("canContinue");
		final boolean var2 = p_75773_1_.action.continueExecuting();
		theProfiler.endSection();
		return var2;
	}

	/**
	 * Determine if a specific AI Task can be executed, which means that all
	 * running higher (= lower int value) priority tasks are compatible with it
	 * or all lower priority tasks can be interrupted.
	 */
	private boolean canUse(EntityAITasks.EntityAITaskEntry p_75775_1_) {
		theProfiler.startSection("canUse");
		final Iterator var2 = taskEntries.iterator();

		while (var2.hasNext()) {
			final EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry) var2
					.next();

			if (var3 != p_75775_1_) {
				if (p_75775_1_.priority >= var3.priority) {
					if (executingTaskEntries.contains(var3)
							&& !areTasksCompatible(p_75775_1_, var3)) {
						theProfiler.endSection();
						return false;
					}
				} else if (executingTaskEntries.contains(var3)
						&& !var3.action.isInterruptible()) {
					theProfiler.endSection();
					return false;
				}
			}
		}

		theProfiler.endSection();
		return true;
	}

	public void onUpdateTasks() {
		final ArrayList var1 = new ArrayList();
		Iterator var2;
		EntityAITasks.EntityAITaskEntry var3;

		if (tickCount++ % tickRate == 0) {
			var2 = taskEntries.iterator();

			while (var2.hasNext()) {
				var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
				final boolean var4 = executingTaskEntries.contains(var3);

				if (var4) {
					if (canUse(var3) && canContinue(var3)) {
						continue;
					}

					var3.action.resetTask();
					executingTaskEntries.remove(var3);
				}

				if (canUse(var3) && var3.action.shouldExecute()) {
					var1.add(var3);
					executingTaskEntries.add(var3);
				}
			}
		} else {
			var2 = executingTaskEntries.iterator();

			while (var2.hasNext()) {
				var3 = (EntityAITasks.EntityAITaskEntry) var2.next();

				if (!var3.action.continueExecuting()) {
					var3.action.resetTask();
					var2.remove();
				}
			}
		}

		theProfiler.startSection("goalStart");
		var2 = var1.iterator();

		while (var2.hasNext()) {
			var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
			theProfiler.startSection(var3.action.getClass().getSimpleName());
			var3.action.startExecuting();
			theProfiler.endSection();
		}

		theProfiler.endSection();
		theProfiler.startSection("goalTick");
		var2 = executingTaskEntries.iterator();

		while (var2.hasNext()) {
			var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
			var3.action.updateTask();
		}

		theProfiler.endSection();
	}

	/**
	 * removes the indicated task from the entity's AI tasks.
	 */
	public void removeTask(EntityAIBase p_85156_1_) {
		final Iterator var2 = taskEntries.iterator();

		while (var2.hasNext()) {
			final EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry) var2
					.next();
			final EntityAIBase var4 = var3.action;

			if (var4 == p_85156_1_) {
				if (executingTaskEntries.contains(var3)) {
					var4.resetTask();
					executingTaskEntries.remove(var3);
				}

				var2.remove();
			}
		}
	}
}
