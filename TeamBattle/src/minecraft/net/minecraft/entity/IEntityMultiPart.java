package net.minecraft.entity;

import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart {
	boolean attackEntityFromPart(EntityDragonPart p_70965_1_,
			DamageSource p_70965_2_, float p_70965_3_);

	World func_82194_d();
}
