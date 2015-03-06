package net.minecraft.entity.ai;

public abstract class EntityAIBase {
	/**
	 * A bitmask telling which other tasks may not run concurrently. The test is
	 * a simple bitwise AND - if it yields zero, the two tasks may run
	 * concurrently, if not - they must run exclusively from each other.
	 */
	private int mutexBits;

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return shouldExecute();
	}

	/**
	 * Get a bitmask telling which other tasks may not run concurrently. The
	 * test is a simple bitwise AND - if it yields zero, the two tasks may run
	 * concurrently, if not - they must run exclusively from each other.
	 */
	public int getMutexBits() {
		return mutexBits;
	}

	/**
	 * Determine if this AI Task is interruptible by a higher (= lower value)
	 * priority task.
	 */
	public boolean isInterruptible() {
		return true;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
	}

	/**
	 * Sets a bitmask telling which other tasks may not run concurrently. The
	 * test is a simple bitwise AND - if it yields zero, the two tasks may run
	 * concurrently, if not - they must run exclusively from each other.
	 */
	public void setMutexBits(int p_75248_1_) {
		mutexBits = p_75248_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public abstract boolean shouldExecute();

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
	}
}
