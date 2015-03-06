package net.minecraft.entity.effect;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect {
	/**
	 * Determines the time before the EntityLightningBolt is destroyed. It is a
	 * random integer decremented over time.
	 */
	private int boltLivingTime;

	/**
	 * A random long that is used to change the vertex of the lightning rendered
	 * in RenderLightningBolt
	 */
	public long boltVertex;

	/**
	 * Declares which state the lightning bolt is in. Whether it's in the air,
	 * hit the ground, etc.
	 */
	private int lightningState;

	public EntityLightningBolt(World p_i1703_1_, double p_i1703_2_,
			double p_i1703_4_, double p_i1703_6_) {
		super(p_i1703_1_);
		setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
		lightningState = 2;
		boltVertex = rand.nextLong();
		boltLivingTime = rand.nextInt(3) + 1;

		if (!p_i1703_1_.isClient
				&& p_i1703_1_.getGameRules().getGameRuleBooleanValue(
						"doFireTick")
				&& (p_i1703_1_.difficultySetting == EnumDifficulty.NORMAL || p_i1703_1_.difficultySetting == EnumDifficulty.HARD)
				&& p_i1703_1_.doChunksNearChunkExist(
						MathHelper.floor_double(p_i1703_2_),
						MathHelper.floor_double(p_i1703_4_),
						MathHelper.floor_double(p_i1703_6_), 10)) {
			int var8 = MathHelper.floor_double(p_i1703_2_);
			int var9 = MathHelper.floor_double(p_i1703_4_);
			int var10 = MathHelper.floor_double(p_i1703_6_);

			if (p_i1703_1_.getBlock(var8, var9, var10).getMaterial() == Material.air
					&& Blocks.fire.canPlaceBlockAt(p_i1703_1_, var8, var9,
							var10)) {
				p_i1703_1_.setBlock(var8, var9, var10, Blocks.fire);
			}

			for (var8 = 0; var8 < 4; ++var8) {
				var9 = MathHelper.floor_double(p_i1703_2_) + rand.nextInt(3)
						- 1;
				var10 = MathHelper.floor_double(p_i1703_4_) + rand.nextInt(3)
						- 1;
				final int var11 = MathHelper.floor_double(p_i1703_6_)
						+ rand.nextInt(3) - 1;

				if (p_i1703_1_.getBlock(var9, var10, var11).getMaterial() == Material.air
						&& Blocks.fire.canPlaceBlockAt(p_i1703_1_, var9, var10,
								var11)) {
					p_i1703_1_.setBlock(var9, var10, var11, Blocks.fire);
				}
			}
		}
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (lightningState == 2) {
			worldObj.playSoundEffect(posX, posY, posZ,
					"ambient.weather.thunder", 10000.0F,
					0.8F + rand.nextFloat() * 0.2F);
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2.0F,
					0.5F + rand.nextFloat() * 0.2F);
		}

		--lightningState;

		if (lightningState < 0) {
			if (boltLivingTime == 0) {
				setDead();
			} else if (lightningState < -rand.nextInt(10)) {
				--boltLivingTime;
				lightningState = 1;
				boltVertex = rand.nextLong();

				if (!worldObj.isClient
						&& worldObj.getGameRules().getGameRuleBooleanValue(
								"doFireTick")
						&& worldObj.doChunksNearChunkExist(
								MathHelper.floor_double(posX),
								MathHelper.floor_double(posY),
								MathHelper.floor_double(posZ), 10)) {
					final int var1 = MathHelper.floor_double(posX);
					final int var2 = MathHelper.floor_double(posY);
					final int var3 = MathHelper.floor_double(posZ);

					if (worldObj.getBlock(var1, var2, var3).getMaterial() == Material.air
							&& Blocks.fire.canPlaceBlockAt(worldObj, var1,
									var2, var3)) {
						worldObj.setBlock(var1, var2, var3, Blocks.fire);
					}
				}
			}
		}

		if (lightningState >= 0) {
			if (worldObj.isClient) {
				worldObj.lastLightningBolt = 2;
			} else {
				final double var6 = 3.0D;
				final List var7 = worldObj
						.getEntitiesWithinAABBExcludingEntity(
								this,
								AxisAlignedBB.getBoundingBox(posX - var6, posY
										- var6, posZ - var6, posX + var6, posY
										+ 6.0D + var6, posZ + var6));

				for (int var4 = 0; var4 < var7.size(); ++var4) {
					final Entity var5 = (Entity) var7.get(var4);
					var5.onStruckByLightning(this);
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}
