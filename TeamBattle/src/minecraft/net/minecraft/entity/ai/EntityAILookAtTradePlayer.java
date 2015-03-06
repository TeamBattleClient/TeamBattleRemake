package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
	private final EntityVillager theMerchant;

	public EntityAILookAtTradePlayer(EntityVillager p_i1633_1_) {
		super(p_i1633_1_, EntityPlayer.class, 8.0F);
		theMerchant = p_i1633_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (theMerchant.isTrading()) {
			closestEntity = theMerchant.getCustomer();
			return true;
		} else
			return false;
	}
}
