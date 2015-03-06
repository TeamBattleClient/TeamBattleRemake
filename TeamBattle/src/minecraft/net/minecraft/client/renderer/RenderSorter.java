package net.minecraft.client.renderer;

import java.util.Comparator;

import net.minecraft.entity.EntityLivingBase;

public class RenderSorter implements Comparator {
	/** The entity (usually the player) that the camera is inside. */
	private final EntityLivingBase baseEntity;

	public RenderSorter(EntityLivingBase p_i1241_1_) {
		baseEntity = p_i1241_1_;
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((WorldRenderer) p_compare_1_,
				(WorldRenderer) p_compare_2_);
	}

	public int compare(WorldRenderer p_compare_1_, WorldRenderer p_compare_2_) {
		if (p_compare_1_.isInFrustum && !p_compare_2_.isInFrustum)
			return 1;
		else if (p_compare_2_.isInFrustum && !p_compare_1_.isInFrustum)
			return -1;
		else {
			final double var3 = p_compare_1_
					.distanceToEntitySquared(baseEntity);
			final double var5 = p_compare_2_
					.distanceToEntitySquared(baseEntity);
			return var3 < var5 ? 1 : var3 > var5 ? -1
					: p_compare_1_.chunkIndex < p_compare_2_.chunkIndex ? 1
							: -1;
		}
	}
}
