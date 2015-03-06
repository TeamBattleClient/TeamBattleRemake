package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
	private final EntityLiving field_151500_b;
	private final World field_151501_c;
	int field_151502_a;

	public EntityAIEatGrass(EntityLiving p_i45314_1_) {
		field_151500_b = p_i45314_1_;
		field_151501_c = p_i45314_1_.worldObj;
		setMutexBits(7);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return field_151502_a > 0;
	}

	public int func_151499_f() {
		return field_151502_a;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		field_151502_a = 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (field_151500_b.getRNG().nextInt(
				field_151500_b.isChild() ? 50 : 1000) != 0)
			return false;
		else {
			final int var1 = MathHelper.floor_double(field_151500_b.posX);
			final int var2 = MathHelper.floor_double(field_151500_b.posY);
			final int var3 = MathHelper.floor_double(field_151500_b.posZ);
			return field_151501_c.getBlock(var1, var2, var3) == Blocks.tallgrass
					&& field_151501_c.getBlockMetadata(var1, var2, var3) == 1 ? true
					: field_151501_c.getBlock(var1, var2 - 1, var3) == Blocks.grass;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_151502_a = 40;
		field_151501_c.setEntityState(field_151500_b, (byte) 10);
		field_151500_b.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		field_151502_a = Math.max(0, field_151502_a - 1);

		if (field_151502_a == 4) {
			final int var1 = MathHelper.floor_double(field_151500_b.posX);
			final int var2 = MathHelper.floor_double(field_151500_b.posY);
			final int var3 = MathHelper.floor_double(field_151500_b.posZ);

			if (field_151501_c.getBlock(var1, var2, var3) == Blocks.tallgrass) {
				if (field_151501_c.getGameRules().getGameRuleBooleanValue(
						"mobGriefing")) {
					field_151501_c.func_147480_a(var1, var2, var3, false);
				}

				field_151500_b.eatGrassBonus();
			} else if (field_151501_c.getBlock(var1, var2 - 1, var3) == Blocks.grass) {
				if (field_151501_c.getGameRules().getGameRuleBooleanValue(
						"mobGriefing")) {
					field_151501_c.playAuxSFX(2001, var1, var2 - 1, var3,
							Block.getIdFromBlock(Blocks.grass));
					field_151501_c.setBlock(var1, var2 - 1, var3, Blocks.dirt,
							0, 2);
				}

				field_151500_b.eatGrassBonus();
			}
		}
	}
}
