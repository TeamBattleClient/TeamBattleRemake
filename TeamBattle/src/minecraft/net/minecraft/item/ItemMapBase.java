package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {

	public Packet func_150911_c(ItemStack p_150911_1_, World p_150911_2_,
			EntityPlayer p_150911_3_) {
		return null;
	}

	/**
	 * false for all Items except sub-classes of ItemMapBase
	 */
	@Override
	public boolean isMap() {
		return true;
	}
}
