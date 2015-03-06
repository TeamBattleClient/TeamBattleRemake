package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
	private final EntityLiving entity;
	private boolean isJumping;

	public EntityJumpHelper(EntityLiving p_i1612_1_) {
		entity = p_i1612_1_;
	}

	/**
	 * Called to actually make the entity jump if isJumping is true.
	 */
	public void doJump() {
		entity.setJumping(isJumping);
		isJumping = false;
	}

	public void setJumping() {
		isJumping = true;
	}
}
