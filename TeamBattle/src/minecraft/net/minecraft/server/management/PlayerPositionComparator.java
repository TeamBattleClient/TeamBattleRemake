package net.minecraft.server.management;

import java.util.Comparator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

public class PlayerPositionComparator implements Comparator {
	private final ChunkCoordinates theChunkCoordinates;

	public PlayerPositionComparator(ChunkCoordinates p_i1499_1_) {
		theChunkCoordinates = p_i1499_1_;
	}

	public int compare(EntityPlayerMP p_compare_1_, EntityPlayerMP p_compare_2_) {
		final double var3 = p_compare_1_.getDistanceSq(
				theChunkCoordinates.posX, theChunkCoordinates.posY,
				theChunkCoordinates.posZ);
		final double var5 = p_compare_2_.getDistanceSq(
				theChunkCoordinates.posX, theChunkCoordinates.posY,
				theChunkCoordinates.posZ);
		return var3 < var5 ? -1 : var3 > var5 ? 1 : 0;
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((EntityPlayerMP) p_compare_1_,
				(EntityPlayerMP) p_compare_2_);
	}
}
