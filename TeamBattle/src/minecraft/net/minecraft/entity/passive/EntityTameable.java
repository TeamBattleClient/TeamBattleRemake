package net.minecraft.entity.passive;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements
		IEntityOwnable {
	protected EntityAISit aiSit = new EntityAISit(this);

	public EntityTameable(World p_i1604_1_) {
		super(p_i1604_1_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		dataWatcher.addObject(17, "");
	}

	public boolean func_142018_a(EntityLivingBase p_142018_1_,
			EntityLivingBase p_142018_2_) {
		return true;
	}

	@Override
	public String func_152113_b() {
		return dataWatcher.getWatchableObjectString(17);
	}

	public boolean func_152114_e(EntityLivingBase p_152114_1_) {
		return p_152114_1_ == getOwner();
	}

	public void func_152115_b(String p_152115_1_) {
		dataWatcher.updateObject(17, p_152115_1_);
	}

	public EntityAISit func_70907_r() {
		return aiSit;
	}

	@Override
	public EntityLivingBase getOwner() {
		try {
			final UUID var1 = UUID.fromString(func_152113_b());
			return var1 == null ? null : worldObj.func_152378_a(var1);
		} catch (final IllegalArgumentException var2) {
			return null;
		}
	}

	@Override
	public Team getTeam() {
		if (isTamed()) {
			final EntityLivingBase var1 = getOwner();

			if (var1 != null)
				return var1.getTeam();
		}

		return super.getTeam();
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 7) {
			playTameEffect(true);
		} else if (p_70103_1_ == 6) {
			playTameEffect(false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	public boolean isOnSameTeam(EntityLivingBase p_142014_1_) {
		if (isTamed()) {
			final EntityLivingBase var2 = getOwner();

			if (p_142014_1_ == var2)
				return true;

			if (var2 != null)
				return var2.isOnSameTeam(p_142014_1_);
		}

		return super.isOnSameTeam(p_142014_1_);
	}

	public boolean isSitting() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public boolean isTamed() {
		return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	/**
	 * Play the taming effect, will either be hearts or smoke depending on
	 * status
	 */
	protected void playTameEffect(boolean p_70908_1_) {
		String var2 = "heart";

		if (!p_70908_1_) {
			var2 = "smoke";
		}

		for (int var3 = 0; var3 < 7; ++var3) {
			final double var4 = rand.nextGaussian() * 0.02D;
			final double var6 = rand.nextGaussian() * 0.02D;
			final double var8 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(var2, posX + rand.nextFloat() * width * 2.0F
					- width, posY + 0.5D + rand.nextFloat() * height, posZ
					+ rand.nextFloat() * width * 2.0F - width, var4, var6, var8);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		String var2 = "";

		if (p_70037_1_.func_150297_b("OwnerUUID", 8)) {
			var2 = p_70037_1_.getString("OwnerUUID");
		} else {
			final String var3 = p_70037_1_.getString("Owner");
			var2 = PreYggdrasilConverter.func_152719_a(var3);
		}

		if (var2.length() > 0) {
			func_152115_b(var2);
			setTamed(true);
		}

		aiSit.setSitting(p_70037_1_.getBoolean("Sitting"));
		setSitting(p_70037_1_.getBoolean("Sitting"));
	}

	public void setSitting(boolean p_70904_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70904_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}

	public void setTamed(boolean p_70903_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70903_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 4)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -5)));
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (func_152113_b() == null) {
			p_70014_1_.setString("OwnerUUID", "");
		} else {
			p_70014_1_.setString("OwnerUUID", func_152113_b());
		}

		p_70014_1_.setBoolean("Sitting", isSitting());
	}
}
