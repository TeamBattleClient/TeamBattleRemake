package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIBase {
	private int field_151487_f;
	private int field_151488_g;
	private int field_151489_d;
	private int field_151490_e;
	private final double field_151491_b;
	private int field_151492_c;
	private final EntityOcelot field_151493_a;
	private int field_151494_h;

	public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_) {
		field_151493_a = p_i45315_1_;
		field_151491_b = p_i45315_2_;
		setMutexBits(5);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return field_151492_c <= field_151490_e
				&& field_151489_d <= 60
				&& func_151486_a(field_151493_a.worldObj, field_151487_f,
						field_151488_g, field_151494_h);
	}

	private boolean func_151485_f() {
		final int var1 = (int) field_151493_a.posY;
		double var2 = 2.147483647E9D;

		for (int var4 = (int) field_151493_a.posX - 8; var4 < field_151493_a.posX + 8.0D; ++var4) {
			for (int var5 = (int) field_151493_a.posZ - 8; var5 < field_151493_a.posZ + 8.0D; ++var5) {
				if (func_151486_a(field_151493_a.worldObj, var4, var1, var5)
						&& field_151493_a.worldObj.isAirBlock(var4, var1 + 1,
								var5)) {
					final double var6 = field_151493_a.getDistanceSq(var4,
							var1, var5);

					if (var6 < var2) {
						field_151487_f = var4;
						field_151488_g = var1;
						field_151494_h = var5;
						var2 = var6;
					}
				}
			}
		}

		return var2 < 2.147483647E9D;
	}

	private boolean func_151486_a(World p_151486_1_, int p_151486_2_,
			int p_151486_3_, int p_151486_4_) {
		final Block var5 = p_151486_1_.getBlock(p_151486_2_, p_151486_3_,
				p_151486_4_);
		final int var6 = p_151486_1_.getBlockMetadata(p_151486_2_, p_151486_3_,
				p_151486_4_);

		if (var5 == Blocks.chest) {
			final TileEntityChest var7 = (TileEntityChest) p_151486_1_
					.getTileEntity(p_151486_2_, p_151486_3_, p_151486_4_);

			if (var7.field_145987_o < 1)
				return true;
		} else {
			if (var5 == Blocks.lit_furnace)
				return true;

			if (var5 == Blocks.bed && !BlockBed.func_149975_b(var6))
				return true;
		}

		return false;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		field_151493_a.setSitting(false);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return field_151493_a.isTamed()
				&& !field_151493_a.isSitting()
				&& field_151493_a.getRNG().nextDouble() <= 0.006500000134110451D
				&& func_151485_f();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_151493_a.getNavigator().tryMoveToXYZ(field_151487_f + 0.5D,
				field_151488_g + 1, field_151494_h + 0.5D, field_151491_b);
		field_151492_c = 0;
		field_151489_d = 0;
		field_151490_e = field_151493_a.getRNG().nextInt(
				field_151493_a.getRNG().nextInt(1200) + 1200) + 1200;
		field_151493_a.func_70907_r().setSitting(false);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		++field_151492_c;
		field_151493_a.func_70907_r().setSitting(false);

		if (field_151493_a.getDistanceSq(field_151487_f, field_151488_g + 1,
				field_151494_h) > 1.0D) {
			field_151493_a.setSitting(false);
			field_151493_a.getNavigator().tryMoveToXYZ(field_151487_f + 0.5D,
					field_151488_g + 1, field_151494_h + 0.5D, field_151491_b);
			++field_151489_d;
		} else if (!field_151493_a.isSitting()) {
			field_151493_a.setSitting(true);
		} else {
			--field_151489_d;
		}
	}
}
