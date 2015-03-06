package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;

public class TileEntityDaylightDetector extends TileEntity {

	@Override
	public void updateEntity() {
		if (worldObj != null && !worldObj.isClient
				&& worldObj.getTotalWorldTime() % 20L == 0L) {
			blockType = getBlockType();

			if (blockType instanceof BlockDaylightDetector) {
				((BlockDaylightDetector) blockType).func_149957_e(worldObj,
						field_145851_c, field_145848_d, field_145849_e);
			}
		}
	}
}
