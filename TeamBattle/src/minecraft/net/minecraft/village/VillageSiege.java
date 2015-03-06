package net.minecraft.village;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;

public class VillageSiege {
	private int field_75532_g;
	private int field_75533_d;
	private int field_75534_e;
	private boolean field_75535_b;
	private int field_75536_c = -1;

	private int field_75538_h;
	private int field_75539_i;
	/** Instance of Village. */
	private Village theVillage;
	private final World worldObj;

	public VillageSiege(World p_i1676_1_) {
		worldObj = p_i1676_1_;
	}

	private Vec3 func_75527_a(int p_75527_1_, int p_75527_2_, int p_75527_3_) {
		for (int var4 = 0; var4 < 10; ++var4) {
			final int var5 = p_75527_1_ + worldObj.rand.nextInt(16) - 8;
			final int var6 = p_75527_2_ + worldObj.rand.nextInt(6) - 3;
			final int var7 = p_75527_3_ + worldObj.rand.nextInt(16) - 8;

			if (theVillage.isInRange(var5, var6, var7)
					&& SpawnerAnimals.canCreatureTypeSpawnAtLocation(
							EnumCreatureType.monster, worldObj, var5, var6,
							var7)) {
				Vec3.createVectorHelper(var5, var6, var7);
			}
		}

		return null;
	}

	private boolean func_75529_b() {
		final List var1 = worldObj.playerEntities;
		final Iterator var2 = var1.iterator();

		while (var2.hasNext()) {
			final EntityPlayer var3 = (EntityPlayer) var2.next();
			theVillage = worldObj.villageCollectionObj.findNearestVillage(
					(int) var3.posX, (int) var3.posY, (int) var3.posZ, 1);

			if (theVillage != null && theVillage.getNumVillageDoors() >= 10
					&& theVillage.getTicksSinceLastDoorAdding() >= 20
					&& theVillage.getNumVillagers() >= 20) {
				final ChunkCoordinates var4 = theVillage.getCenter();
				final float var5 = theVillage.getVillageRadius();
				boolean var6 = false;
				int var7 = 0;

				while (true) {
					if (var7 < 10) {
						field_75532_g = var4.posX
								+ (int) (MathHelper.cos(worldObj.rand
										.nextFloat() * (float) Math.PI * 2.0F)
										* var5 * 0.9D);
						field_75538_h = var4.posY;
						field_75539_i = var4.posZ
								+ (int) (MathHelper.sin(worldObj.rand
										.nextFloat() * (float) Math.PI * 2.0F)
										* var5 * 0.9D);
						var6 = false;
						final Iterator var8 = worldObj.villageCollectionObj
								.getVillageList().iterator();

						while (var8.hasNext()) {
							final Village var9 = (Village) var8.next();

							if (var9 != theVillage
									&& var9.isInRange(field_75532_g,
											field_75538_h, field_75539_i)) {
								var6 = true;
								break;
							}
						}

						if (var6) {
							++var7;
							continue;
						}
					}

					if (var6)
						return false;

					final Vec3 var10 = func_75527_a(field_75532_g,
							field_75538_h, field_75539_i);

					if (var10 != null) {
						field_75534_e = 0;
						field_75533_d = 20;
						return true;
					}

					break;
				}
			}
		}

		return false;
	}

	private boolean spawnZombie() {
		final Vec3 var1 = func_75527_a(field_75532_g, field_75538_h,
				field_75539_i);

		if (var1 == null)
			return false;
		else {
			EntityZombie var2;

			try {
				var2 = new EntityZombie(worldObj);
				var2.onSpawnWithEgg((IEntityLivingData) null);
				var2.setVillager(false);
			} catch (final Exception var4) {
				var4.printStackTrace();
				return false;
			}

			var2.setLocationAndAngles(var1.xCoord, var1.yCoord, var1.zCoord,
					worldObj.rand.nextFloat() * 360.0F, 0.0F);
			worldObj.spawnEntityInWorld(var2);
			final ChunkCoordinates var3 = theVillage.getCenter();
			var2.setHomeArea(var3.posX, var3.posY, var3.posZ,
					theVillage.getVillageRadius());
			return true;
		}
	}

	/**
	 * Runs a single tick for the village siege
	 */
	public void tick() {
		final boolean var1 = false;

		if (var1) {
			if (field_75536_c == 2) {
				field_75533_d = 100;
				return;
			}
		} else {
			if (worldObj.isDaytime()) {
				field_75536_c = 0;
				return;
			}

			if (field_75536_c == 2)
				return;

			if (field_75536_c == 0) {
				final float var2 = worldObj.getCelestialAngle(0.0F);

				if (var2 < 0.5D || var2 > 0.501D)
					return;

				field_75536_c = worldObj.rand.nextInt(10) == 0 ? 1 : 2;
				field_75535_b = false;

				if (field_75536_c == 2)
					return;
			}
		}

		if (!field_75535_b) {
			if (!func_75529_b())
				return;

			field_75535_b = true;
		}

		if (field_75534_e > 0) {
			--field_75534_e;
		} else {
			field_75534_e = 2;

			if (field_75533_d > 0) {
				spawnZombie();
				--field_75533_d;
			} else {
				field_75536_c = 2;
			}
		}
	}
}
