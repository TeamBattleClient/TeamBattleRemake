package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase {
	private double field_111176_d;
	private double field_111177_e;
	private final double field_111178_b;
	private double field_111179_c;
	private final EntityHorse horseHost;

	public EntityAIRunAroundLikeCrazy(EntityHorse p_i1653_1_, double p_i1653_2_) {
		horseHost = p_i1653_1_;
		field_111178_b = p_i1653_2_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !horseHost.getNavigator().noPath()
				&& horseHost.riddenByEntity != null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!horseHost.isTame() && horseHost.riddenByEntity != null) {
			final Vec3 var1 = RandomPositionGenerator.findRandomTarget(
					horseHost, 5, 4);

			if (var1 == null)
				return false;
			else {
				field_111179_c = var1.xCoord;
				field_111176_d = var1.yCoord;
				field_111177_e = var1.zCoord;
				return true;
			}
		} else
			return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		horseHost.getNavigator().tryMoveToXYZ(field_111179_c, field_111176_d,
				field_111177_e, field_111178_b);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (horseHost.getRNG().nextInt(50) == 0) {
			if (horseHost.riddenByEntity instanceof EntityPlayer) {
				final int var1 = horseHost.getTemper();
				final int var2 = horseHost.getMaxTemper();

				if (var2 > 0 && horseHost.getRNG().nextInt(var2) < var1) {
					horseHost
							.setTamedBy((EntityPlayer) horseHost.riddenByEntity);
					horseHost.worldObj.setEntityState(horseHost, (byte) 7);
					return;
				}

				horseHost.increaseTemper(5);
			}

			horseHost.riddenByEntity.mountEntity((Entity) null);
			horseHost.riddenByEntity = null;
			horseHost.makeHorseRearWithSound();
			horseHost.worldObj.setEntityState(horseHost, (byte) 6);
		}
	}
}
