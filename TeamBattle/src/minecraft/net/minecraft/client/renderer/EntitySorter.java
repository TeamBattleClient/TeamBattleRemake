package net.minecraft.client.renderer;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class EntitySorter implements Comparator {
	/** Entity position X */
	private final double entityPosX;

	/** Entity position Y */
	private final double entityPosY;

	/** Entity position Z */
	private final double entityPosZ;

	public EntitySorter(Entity p_i1242_1_) {
		entityPosX = -p_i1242_1_.posX;
		entityPosY = -p_i1242_1_.posY;
		entityPosZ = -p_i1242_1_.posZ;
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((WorldRenderer) p_compare_1_,
				(WorldRenderer) p_compare_2_);
	}

	public int compare(WorldRenderer p_compare_1_, WorldRenderer p_compare_2_) {
		final double var3 = p_compare_1_.posXPlus + entityPosX;
		final double var5 = p_compare_1_.posYPlus + entityPosY;
		final double var7 = p_compare_1_.posZPlus + entityPosZ;
		final double var9 = p_compare_2_.posXPlus + entityPosX;
		final double var11 = p_compare_2_.posYPlus + entityPosY;
		final double var13 = p_compare_2_.posZPlus + entityPosZ;
		return (int) ((var3 * var3 + var5 * var5 + var7 * var7 - (var9 * var9
				+ var11 * var11 + var13 * var13)) * 1024.0D);
	}
}
