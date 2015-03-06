package net.minecraft.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic {
	public class WeightedRandomMinecart extends WeightedRandom.Item {
		public final NBTTagCompound field_98222_b;
		public final String minecartName;

		public WeightedRandomMinecart(NBTTagCompound p_i1945_2_) {
			super(p_i1945_2_.getInteger("Weight"));
			final NBTTagCompound var3 = p_i1945_2_.getCompoundTag("Properties");
			String var4 = p_i1945_2_.getString("Type");

			if (var4.equals("Minecart")) {
				if (var3 != null) {
					switch (var3.getInteger("Type")) {
					case 0:
						var4 = "MinecartRideable";
						break;

					case 1:
						var4 = "MinecartChest";
						break;

					case 2:
						var4 = "MinecartFurnace";
					}
				} else {
					var4 = "MinecartRideable";
				}
			}

			field_98222_b = var3;
			minecartName = var4;
		}

		public WeightedRandomMinecart(NBTTagCompound p_i1946_2_,
				String p_i1946_3_) {
			super(1);

			if (p_i1946_3_.equals("Minecart")) {
				if (p_i1946_2_ != null) {
					switch (p_i1946_2_.getInteger("Type")) {
					case 0:
						p_i1946_3_ = "MinecartRideable";
						break;

					case 1:
						p_i1946_3_ = "MinecartChest";
						break;

					case 2:
						p_i1946_3_ = "MinecartFurnace";
					}
				} else {
					p_i1946_3_ = "MinecartRideable";
				}
			}

			field_98222_b = p_i1946_2_;
			minecartName = p_i1946_3_;
		}

		public NBTTagCompound func_98220_a() {
			final NBTTagCompound var1 = new NBTTagCompound();
			var1.setTag("Properties", field_98222_b);
			var1.setString("Type", minecartName);
			var1.setInteger("Weight", itemWeight);
			return var1;
		}
	}

	/** The distance from which a player activates the spawner. */
	private int activatingRangeFromPlayer = 16;

	public double field_98284_d;
	public double field_98287_c;
	private Entity field_98291_j;
	private int maxNearbyEntities = 6;
	private int maxSpawnDelay = 800;
	/** List of minecart to spawn. */
	private List minecartToSpawn;

	private int minSpawnDelay = 200;
	private String mobID = "Pig";
	private MobSpawnerBaseLogic.WeightedRandomMinecart randomMinecart;

	/** A counter for spawn tries. */
	private int spawnCount = 4;

	/** The delay to spawn. */
	public int spawnDelay = 20;

	/** The range coefficient for spawning entities around. */
	private int spawnRange = 4;

	/**
	 * Returns true if there's a player close enough to this mob spawner to
	 * activate it.
	 */
	public boolean canRun() {
		return getSpawnerWorld().getClosestPlayer(getSpawnerX() + 0.5D,
				getSpawnerY() + 0.5D, getSpawnerZ() + 0.5D,
				activatingRangeFromPlayer) != null;
	}

	public Entity func_98265_a(Entity p_98265_1_) {
		if (getRandomMinecart() != null) {
			NBTTagCompound var2 = new NBTTagCompound();
			p_98265_1_.writeToNBTOptional(var2);
			final Iterator var3 = getRandomMinecart().field_98222_b
					.func_150296_c().iterator();

			while (var3.hasNext()) {
				final String var4 = (String) var3.next();
				final NBTBase var5 = getRandomMinecart().field_98222_b
						.getTag(var4);
				var2.setTag(var4, var5.copy());
			}

			p_98265_1_.readFromNBT(var2);

			if (p_98265_1_.worldObj != null) {
				p_98265_1_.worldObj.spawnEntityInWorld(p_98265_1_);
			}

			NBTTagCompound var11;

			for (Entity var10 = p_98265_1_; var2.func_150297_b("Riding", 10); var2 = var11) {
				var11 = var2.getCompoundTag("Riding");
				final Entity var12 = EntityList.createEntityByName(
						var11.getString("id"), p_98265_1_.worldObj);

				if (var12 != null) {
					final NBTTagCompound var6 = new NBTTagCompound();
					var12.writeToNBTOptional(var6);
					final Iterator var7 = var11.func_150296_c().iterator();

					while (var7.hasNext()) {
						final String var8 = (String) var7.next();
						final NBTBase var9 = var11.getTag(var8);
						var6.setTag(var8, var9.copy());
					}

					var12.readFromNBT(var6);
					var12.setLocationAndAngles(var10.posX, var10.posY,
							var10.posZ, var10.rotationYaw, var10.rotationPitch);

					if (p_98265_1_.worldObj != null) {
						p_98265_1_.worldObj.spawnEntityInWorld(var12);
					}

					var10.mountEntity(var12);
				}

				var10 = var12;
			}
		} else if (p_98265_1_ instanceof EntityLivingBase
				&& p_98265_1_.worldObj != null) {
			((EntityLiving) p_98265_1_)
					.onSpawnWithEgg((IEntityLivingData) null);
			getSpawnerWorld().spawnEntityInWorld(p_98265_1_);
		}

		return p_98265_1_;
	}

	public abstract void func_98267_a(int p_98267_1_);

	public Entity func_98281_h() {
		if (field_98291_j == null) {
			Entity var1 = EntityList.createEntityByName(getEntityNameToSpawn(),
					(World) null);
			var1 = func_98265_a(var1);
			field_98291_j = var1;
		}

		return field_98291_j;
	}

	/**
	 * Gets the entity name that should be spawned.
	 */
	public String getEntityNameToSpawn() {
		if (getRandomMinecart() == null) {
			if (mobID.equals("Minecart")) {
				mobID = "MinecartRideable";
			}

			return mobID;
		} else
			return getRandomMinecart().minecartName;
	}

	public MobSpawnerBaseLogic.WeightedRandomMinecart getRandomMinecart() {
		return randomMinecart;
	}

	public abstract World getSpawnerWorld();

	public abstract int getSpawnerX();

	public abstract int getSpawnerY();

	public abstract int getSpawnerZ();

	public void readFromNBT(NBTTagCompound p_98270_1_) {
		mobID = p_98270_1_.getString("EntityId");
		spawnDelay = p_98270_1_.getShort("Delay");

		if (p_98270_1_.func_150297_b("SpawnPotentials", 9)) {
			minecartToSpawn = new ArrayList();
			final NBTTagList var2 = p_98270_1_
					.getTagList("SpawnPotentials", 10);

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				minecartToSpawn
						.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(
								var2.getCompoundTagAt(var3)));
			}
		} else {
			minecartToSpawn = null;
		}

		if (p_98270_1_.func_150297_b("SpawnData", 10)) {
			setRandomMinecart(new MobSpawnerBaseLogic.WeightedRandomMinecart(
					p_98270_1_.getCompoundTag("SpawnData"), mobID));
		} else {
			setRandomMinecart((MobSpawnerBaseLogic.WeightedRandomMinecart) null);
		}

		if (p_98270_1_.func_150297_b("MinSpawnDelay", 99)) {
			minSpawnDelay = p_98270_1_.getShort("MinSpawnDelay");
			maxSpawnDelay = p_98270_1_.getShort("MaxSpawnDelay");
			spawnCount = p_98270_1_.getShort("SpawnCount");
		}

		if (p_98270_1_.func_150297_b("MaxNearbyEntities", 99)) {
			maxNearbyEntities = p_98270_1_.getShort("MaxNearbyEntities");
			activatingRangeFromPlayer = p_98270_1_
					.getShort("RequiredPlayerRange");
		}

		if (p_98270_1_.func_150297_b("SpawnRange", 99)) {
			spawnRange = p_98270_1_.getShort("SpawnRange");
		}

		if (getSpawnerWorld() != null && getSpawnerWorld().isClient) {
			field_98291_j = null;
		}
	}

	private void resetTimer() {
		if (maxSpawnDelay <= minSpawnDelay) {
			spawnDelay = minSpawnDelay;
		} else {
			final int var10003 = maxSpawnDelay - minSpawnDelay;
			spawnDelay = minSpawnDelay
					+ getSpawnerWorld().rand.nextInt(var10003);
		}

		if (minecartToSpawn != null && minecartToSpawn.size() > 0) {
			setRandomMinecart((MobSpawnerBaseLogic.WeightedRandomMinecart) WeightedRandom
					.getRandomItem(getSpawnerWorld().rand, minecartToSpawn));
		}

		func_98267_a(1);
	}

	/**
	 * Sets the delay to minDelay if parameter given is 1, else return false.
	 */
	public boolean setDelayToMin(int p_98268_1_) {
		if (p_98268_1_ == 1 && getSpawnerWorld().isClient) {
			spawnDelay = minSpawnDelay;
			return true;
		} else
			return false;
	}

	public void setMobID(String p_98272_1_) {
		mobID = p_98272_1_;
	}

	public void setRandomMinecart(
			MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
		randomMinecart = p_98277_1_;
	}

	public void updateSpawner() {
		if (canRun()) {
			double var5;

			if (getSpawnerWorld().isClient) {
				final double var1 = getSpawnerX()
						+ getSpawnerWorld().rand.nextFloat();
				final double var3 = getSpawnerY()
						+ getSpawnerWorld().rand.nextFloat();
				var5 = getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
				getSpawnerWorld().spawnParticle("smoke", var1, var3, var5,
						0.0D, 0.0D, 0.0D);
				getSpawnerWorld().spawnParticle("flame", var1, var3, var5,
						0.0D, 0.0D, 0.0D);

				if (spawnDelay > 0) {
					--spawnDelay;
				}

				field_98284_d = field_98287_c;
				field_98287_c = (field_98287_c + 1000.0F / (spawnDelay + 200.0F)) % 360.0D;
			} else {
				if (spawnDelay == -1) {
					resetTimer();
				}

				if (spawnDelay > 0) {
					--spawnDelay;
					return;
				}

				boolean var12 = false;

				for (int var2 = 0; var2 < spawnCount; ++var2) {
					final Entity var13 = EntityList.createEntityByName(
							getEntityNameToSpawn(), getSpawnerWorld());

					if (var13 == null)
						return;

					final int var4 = getSpawnerWorld().getEntitiesWithinAABB(
							var13.getClass(),
							AxisAlignedBB.getBoundingBox(getSpawnerX(),
									getSpawnerY(), getSpawnerZ(),
									getSpawnerX() + 1, getSpawnerY() + 1,
									getSpawnerZ() + 1).expand(spawnRange * 2,
									4.0D, spawnRange * 2)).size();

					if (var4 >= maxNearbyEntities) {
						resetTimer();
						return;
					}

					var5 = getSpawnerX()
							+ (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand
									.nextDouble()) * spawnRange;
					final double var7 = getSpawnerY()
							+ getSpawnerWorld().rand.nextInt(3) - 1;
					final double var9 = getSpawnerZ()
							+ (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand
									.nextDouble()) * spawnRange;
					final EntityLiving var11 = var13 instanceof EntityLiving ? (EntityLiving) var13
							: null;
					var13.setLocationAndAngles(var5, var7, var9,
							getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

					if (var11 == null || var11.getCanSpawnHere()) {
						func_98265_a(var13);
						getSpawnerWorld().playAuxSFX(2004, getSpawnerX(),
								getSpawnerY(), getSpawnerZ(), 0);

						if (var11 != null) {
							var11.spawnExplosionParticle();
						}

						var12 = true;
					}
				}

				if (var12) {
					resetTimer();
				}
			}
		}
	}

	public void writeToNBT(NBTTagCompound p_98280_1_) {
		p_98280_1_.setString("EntityId", getEntityNameToSpawn());
		p_98280_1_.setShort("Delay", (short) spawnDelay);
		p_98280_1_.setShort("MinSpawnDelay", (short) minSpawnDelay);
		p_98280_1_.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
		p_98280_1_.setShort("SpawnCount", (short) spawnCount);
		p_98280_1_.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
		p_98280_1_.setShort("RequiredPlayerRange",
				(short) activatingRangeFromPlayer);
		p_98280_1_.setShort("SpawnRange", (short) spawnRange);

		if (getRandomMinecart() != null) {
			p_98280_1_.setTag("SpawnData",
					getRandomMinecart().field_98222_b.copy());
		}

		if (getRandomMinecart() != null || minecartToSpawn != null
				&& minecartToSpawn.size() > 0) {
			final NBTTagList var2 = new NBTTagList();

			if (minecartToSpawn != null && minecartToSpawn.size() > 0) {
				final Iterator var3 = minecartToSpawn.iterator();

				while (var3.hasNext()) {
					final MobSpawnerBaseLogic.WeightedRandomMinecart var4 = (MobSpawnerBaseLogic.WeightedRandomMinecart) var3
							.next();
					var2.appendTag(var4.func_98220_a());
				}
			} else {
				var2.appendTag(getRandomMinecart().func_98220_a());
			}

			p_98280_1_.setTag("SpawnPotentials", var2);
		}
	}
}
