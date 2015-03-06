package net.minecraft.client.renderer;

public class DestroyBlockProgress {
	/**
	 * keeps track of how many ticks this PartiallyDestroyedBlock already exists
	 */
	private int createdAtCloudUpdateTick;
	/**
	 * damage ranges from 1 to 10. -1 causes the client to delete the partial
	 * block renderer.
	 */
	private int partialBlockProgress;
	private final int partialBlockX;

	private final int partialBlockY;

	private final int partialBlockZ;

	public DestroyBlockProgress(int p_i1511_1_, int p_i1511_2_, int p_i1511_3_,
			int p_i1511_4_) {
		partialBlockX = p_i1511_2_;
		partialBlockY = p_i1511_3_;
		partialBlockZ = p_i1511_4_;
	}

	/**
	 * retrieves the 'date' at which the PartiallyDestroyedBlock was created
	 */
	public int getCreationCloudUpdateTick() {
		return createdAtCloudUpdateTick;
	}

	public int getPartialBlockDamage() {
		return partialBlockProgress;
	}

	public int getPartialBlockX() {
		return partialBlockX;
	}

	public int getPartialBlockY() {
		return partialBlockY;
	}

	public int getPartialBlockZ() {
		return partialBlockZ;
	}

	/**
	 * saves the current Cloud update tick into the PartiallyDestroyedBlock
	 */
	public void setCloudUpdateTick(int p_82744_1_) {
		createdAtCloudUpdateTick = p_82744_1_;
	}

	/**
	 * inserts damage value into this partially destroyed Block. -1 causes
	 * client renderer to delete it, otherwise ranges from 1 to 10
	 */
	public void setPartialBlockDamage(int p_73107_1_) {
		if (p_73107_1_ > 10) {
			p_73107_1_ = 10;
		}

		partialBlockProgress = p_73107_1_;
	}
}
