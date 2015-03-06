package net.minecraft.village;

public class VillageDoorInfo {
	private int doorOpeningRestrictionCounter;
	public final int insideDirectionX;
	public final int insideDirectionZ;
	public boolean isDetachedFromVillageFlag;
	public int lastActivityTimestamp;
	public final int posX;
	public final int posY;
	public final int posZ;

	public VillageDoorInfo(int p_i1673_1_, int p_i1673_2_, int p_i1673_3_,
			int p_i1673_4_, int p_i1673_5_, int p_i1673_6_) {
		posX = p_i1673_1_;
		posY = p_i1673_2_;
		posZ = p_i1673_3_;
		insideDirectionX = p_i1673_4_;
		insideDirectionZ = p_i1673_5_;
		lastActivityTimestamp = p_i1673_6_;
	}

	/**
	 * Returns the squared distance between this door and the given coordinate.
	 */
	public int getDistanceSquared(int p_75474_1_, int p_75474_2_, int p_75474_3_) {
		final int var4 = p_75474_1_ - posX;
		final int var5 = p_75474_2_ - posY;
		final int var6 = p_75474_3_ - posZ;
		return var4 * var4 + var5 * var5 + var6 * var6;
	}

	public int getDoorOpeningRestrictionCounter() {
		return doorOpeningRestrictionCounter;
	}

	/**
	 * Get the square of the distance from a location 2 blocks away from the
	 * door considered 'inside' and the given arguments
	 */
	public int getInsideDistanceSquare(int p_75469_1_, int p_75469_2_,
			int p_75469_3_) {
		final int var4 = p_75469_1_ - posX - insideDirectionX;
		final int var5 = p_75469_2_ - posY;
		final int var6 = p_75469_3_ - posZ - insideDirectionZ;
		return var4 * var4 + var5 * var5 + var6 * var6;
	}

	public int getInsidePosX() {
		return posX + insideDirectionX;
	}

	public int getInsidePosY() {
		return posY;
	}

	public int getInsidePosZ() {
		return posZ + insideDirectionZ;
	}

	public void incrementDoorOpeningRestrictionCounter() {
		++doorOpeningRestrictionCounter;
	}

	public boolean isInside(int p_75467_1_, int p_75467_2_) {
		final int var3 = p_75467_1_ - posX;
		final int var4 = p_75467_2_ - posZ;
		return var3 * insideDirectionX + var4 * insideDirectionZ >= 0;
	}

	public void resetDoorOpeningRestrictionCounter() {
		doorOpeningRestrictionCounter = 0;
	}
}
