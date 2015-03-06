package net.minecraft.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {
	private static final Logger logger = LogManager.getLogger();
	public int blocksDistanceThreshold;
	private Entity field_85178_v;

	/** set to true on first sendLocationToClients */
	private boolean isDataInitialized;
	public int lastHeadMotion;
	public int lastPitch;
	public int lastScaledXPosition;
	public int lastScaledYPosition;
	public int lastScaledZPosition;
	public int lastYaw;
	public double motionX;
	public double motionY;
	public double motionZ;
	public Entity myEntity;
	public boolean playerEntitiesUpdated;
	private double posX;
	private double posY;

	private double posZ;
	private boolean ridingEntity;

	private final boolean sendVelocityUpdates;
	public int ticks;
	/**
	 * every 400 ticks a full teleport packet is sent, rather than just a
	 * "move me +x" command, so that position remains fully synced.
	 */
	private int ticksSinceLastForcedTeleport;
	/**
	 * Holds references to all the players that are currently receiving position
	 * updates for this entity.
	 */
	public Set trackingPlayers = new HashSet();

	/** check for sync when ticks % updateFrequency==0 */
	public int updateFrequency;

	public EntityTrackerEntry(Entity p_i1525_1_, int p_i1525_2_,
			int p_i1525_3_, boolean p_i1525_4_) {
		myEntity = p_i1525_1_;
		blocksDistanceThreshold = p_i1525_2_;
		updateFrequency = p_i1525_3_;
		sendVelocityUpdates = p_i1525_4_;
		lastScaledXPosition = MathHelper.floor_double(p_i1525_1_.posX * 32.0D);
		lastScaledYPosition = MathHelper.floor_double(p_i1525_1_.posY * 32.0D);
		lastScaledZPosition = MathHelper.floor_double(p_i1525_1_.posZ * 32.0D);
		lastYaw = MathHelper
				.floor_float(p_i1525_1_.rotationYaw * 256.0F / 360.0F);
		lastPitch = MathHelper
				.floor_float(p_i1525_1_.rotationPitch * 256.0F / 360.0F);
		lastHeadMotion = MathHelper
				.floor_float(p_i1525_1_.getRotationYawHead() * 256.0F / 360.0F);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return p_equals_1_ instanceof EntityTrackerEntry ? ((EntityTrackerEntry) p_equals_1_).myEntity
				.getEntityId() == myEntity.getEntityId() : false;
	}

	private void func_111190_b() {
		final DataWatcher var1 = myEntity.getDataWatcher();

		if (var1.hasChanges()) {
			func_151261_b(new S1CPacketEntityMetadata(myEntity.getEntityId(),
					var1, false));
		}

		if (myEntity instanceof EntityLivingBase) {
			final ServersideAttributeMap var2 = (ServersideAttributeMap) ((EntityLivingBase) myEntity)
					.getAttributeMap();
			final Set var3 = var2.getAttributeInstanceSet();

			if (!var3.isEmpty()) {
				func_151261_b(new S20PacketEntityProperties(
						myEntity.getEntityId(), var3));
			}

			var3.clear();
		}
	}

	public void func_151259_a(Packet p_151259_1_) {
		final Iterator var2 = trackingPlayers.iterator();

		while (var2.hasNext()) {
			final EntityPlayerMP var3 = (EntityPlayerMP) var2.next();
			var3.playerNetServerHandler.sendPacket(p_151259_1_);
		}
	}

	private Packet func_151260_c() {
		if (myEntity.isDead) {
			logger.warn("Fetching addPacket for removed entity");
		}

		if (myEntity instanceof EntityItem)
			return new S0EPacketSpawnObject(myEntity, 2, 1);
		else if (myEntity instanceof EntityPlayerMP)
			return new S0CPacketSpawnPlayer((EntityPlayer) myEntity);
		else if (myEntity instanceof EntityMinecart) {
			final EntityMinecart var9 = (EntityMinecart) myEntity;
			return new S0EPacketSpawnObject(myEntity, 10,
					var9.getMinecartType());
		} else if (myEntity instanceof EntityBoat)
			return new S0EPacketSpawnObject(myEntity, 1);
		else if (!(myEntity instanceof IAnimals)
				&& !(myEntity instanceof EntityDragon)) {
			if (myEntity instanceof EntityFishHook) {
				final EntityPlayer var8 = ((EntityFishHook) myEntity).field_146042_b;
				return new S0EPacketSpawnObject(myEntity, 90,
						var8 != null ? var8.getEntityId()
								: myEntity.getEntityId());
			} else if (myEntity instanceof EntityArrow) {
				final Entity var7 = ((EntityArrow) myEntity).shootingEntity;
				return new S0EPacketSpawnObject(myEntity, 60,
						var7 != null ? var7.getEntityId()
								: myEntity.getEntityId());
			} else if (myEntity instanceof EntitySnowball)
				return new S0EPacketSpawnObject(myEntity, 61);
			else if (myEntity instanceof EntityPotion)
				return new S0EPacketSpawnObject(myEntity, 73,
						((EntityPotion) myEntity).getPotionDamage());
			else if (myEntity instanceof EntityExpBottle)
				return new S0EPacketSpawnObject(myEntity, 75);
			else if (myEntity instanceof EntityEnderPearl)
				return new S0EPacketSpawnObject(myEntity, 65);
			else if (myEntity instanceof EntityEnderEye)
				return new S0EPacketSpawnObject(myEntity, 72);
			else if (myEntity instanceof EntityFireworkRocket)
				return new S0EPacketSpawnObject(myEntity, 76);
			else {
				S0EPacketSpawnObject var2;

				if (myEntity instanceof EntityFireball) {
					final EntityFireball var6 = (EntityFireball) myEntity;
					var2 = null;
					byte var3 = 63;

					if (myEntity instanceof EntitySmallFireball) {
						var3 = 64;
					} else if (myEntity instanceof EntityWitherSkull) {
						var3 = 66;
					}

					if (var6.shootingEntity != null) {
						var2 = new S0EPacketSpawnObject(myEntity, var3,
								((EntityFireball) myEntity).shootingEntity
										.getEntityId());
					} else {
						var2 = new S0EPacketSpawnObject(myEntity, var3, 0);
					}

					var2.func_149003_d((int) (var6.accelerationX * 8000.0D));
					var2.func_149000_e((int) (var6.accelerationY * 8000.0D));
					var2.func_149007_f((int) (var6.accelerationZ * 8000.0D));
					return var2;
				} else if (myEntity instanceof EntityEgg)
					return new S0EPacketSpawnObject(myEntity, 62);
				else if (myEntity instanceof EntityTNTPrimed)
					return new S0EPacketSpawnObject(myEntity, 50);
				else if (myEntity instanceof EntityEnderCrystal)
					return new S0EPacketSpawnObject(myEntity, 51);
				else if (myEntity instanceof EntityFallingBlock) {
					final EntityFallingBlock var5 = (EntityFallingBlock) myEntity;
					return new S0EPacketSpawnObject(myEntity, 70,
							Block.getIdFromBlock(var5.func_145805_f())
									| var5.field_145814_a << 16);
				} else if (myEntity instanceof EntityPainting)
					return new S10PacketSpawnPainting((EntityPainting) myEntity);
				else if (myEntity instanceof EntityItemFrame) {
					final EntityItemFrame var4 = (EntityItemFrame) myEntity;
					var2 = new S0EPacketSpawnObject(myEntity, 71,
							var4.hangingDirection);
					var2.func_148996_a(MathHelper
							.floor_float(var4.field_146063_b * 32));
					var2.func_148995_b(MathHelper
							.floor_float(var4.field_146064_c * 32));
					var2.func_149005_c(MathHelper
							.floor_float(var4.field_146062_d * 32));
					return var2;
				} else if (myEntity instanceof EntityLeashKnot) {
					final EntityLeashKnot var1 = (EntityLeashKnot) myEntity;
					var2 = new S0EPacketSpawnObject(myEntity, 77);
					var2.func_148996_a(MathHelper
							.floor_float(var1.field_146063_b * 32));
					var2.func_148995_b(MathHelper
							.floor_float(var1.field_146064_c * 32));
					var2.func_149005_c(MathHelper
							.floor_float(var1.field_146062_d * 32));
					return var2;
				} else if (myEntity instanceof EntityXPOrb)
					return new S11PacketSpawnExperienceOrb(
							(EntityXPOrb) myEntity);
				else
					throw new IllegalArgumentException(
							"Don\'t know how to add " + myEntity.getClass()
									+ "!");
			}
		} else {
			lastHeadMotion = MathHelper.floor_float(myEntity
					.getRotationYawHead() * 256.0F / 360.0F);
			return new S0FPacketSpawnMob((EntityLivingBase) myEntity);
		}
	}

	public void func_151261_b(Packet p_151261_1_) {
		func_151259_a(p_151261_1_);

		if (myEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP) myEntity).playerNetServerHandler
					.sendPacket(p_151261_1_);
		}
	}

	@Override
	public int hashCode() {
		return myEntity.getEntityId();
	}

	public void informAllAssociatedPlayersOfItemDestruction() {
		final Iterator var1 = trackingPlayers.iterator();

		while (var1.hasNext()) {
			final EntityPlayerMP var2 = (EntityPlayerMP) var1.next();
			var2.func_152339_d(myEntity);
		}
	}

	private boolean isPlayerWatchingThisChunk(EntityPlayerMP p_73121_1_) {
		return p_73121_1_
				.getServerForPlayer()
				.getPlayerManager()
				.isPlayerWatchingChunk(p_73121_1_, myEntity.chunkCoordX,
						myEntity.chunkCoordZ);
	}

	public void removeFromWatchingList(EntityPlayerMP p_73118_1_) {
		if (trackingPlayers.contains(p_73118_1_)) {
			p_73118_1_.func_152339_d(myEntity);
			trackingPlayers.remove(p_73118_1_);
		}
	}

	public void removePlayerFromTracker(EntityPlayerMP p_73123_1_) {
		if (trackingPlayers.contains(p_73123_1_)) {
			trackingPlayers.remove(p_73123_1_);
			p_73123_1_.func_152339_d(myEntity);
		}
	}

	public void sendEventsToPlayers(List p_73125_1_) {
		for (int var2 = 0; var2 < p_73125_1_.size(); ++var2) {
			tryStartWachingThis((EntityPlayerMP) p_73125_1_.get(var2));
		}
	}

	/**
	 * also sends velocity, rotation, and riding info.
	 */
	public void sendLocationToAllClients(List p_73122_1_) {
		playerEntitiesUpdated = false;

		if (!isDataInitialized
				|| myEntity.getDistanceSq(posX, posY, posZ) > 16.0D) {
			posX = myEntity.posX;
			posY = myEntity.posY;
			posZ = myEntity.posZ;
			isDataInitialized = true;
			playerEntitiesUpdated = true;
			sendEventsToPlayers(p_73122_1_);
		}

		if (field_85178_v != myEntity.ridingEntity
				|| myEntity.ridingEntity != null && ticks % 60 == 0) {
			field_85178_v = myEntity.ridingEntity;
			func_151259_a(new S1BPacketEntityAttach(0, myEntity,
					myEntity.ridingEntity));
		}

		if (myEntity instanceof EntityItemFrame && ticks % 10 == 0) {
			final EntityItemFrame var23 = (EntityItemFrame) myEntity;
			final ItemStack var24 = var23.getDisplayedItem();

			if (var24 != null && var24.getItem() instanceof ItemMap) {
				final MapData var26 = Items.filled_map.getMapData(var24,
						myEntity.worldObj);
				final Iterator var27 = p_73122_1_.iterator();

				while (var27.hasNext()) {
					final EntityPlayer var28 = (EntityPlayer) var27.next();
					final EntityPlayerMP var29 = (EntityPlayerMP) var28;
					var26.updateVisiblePlayers(var29, var24);
					final Packet var30 = Items.filled_map.func_150911_c(var24,
							myEntity.worldObj, var29);

					if (var30 != null) {
						var29.playerNetServerHandler.sendPacket(var30);
					}
				}
			}

			func_111190_b();
		} else if (ticks % updateFrequency == 0 || myEntity.isAirBorne
				|| myEntity.getDataWatcher().hasChanges()) {
			int var2;
			int var3;

			if (myEntity.ridingEntity == null) {
				++ticksSinceLastForcedTeleport;
				var2 = myEntity.myEntitySize
						.multiplyBy32AndRound(myEntity.posX);
				var3 = MathHelper.floor_double(myEntity.posY * 32.0D);
				final int var4 = myEntity.myEntitySize
						.multiplyBy32AndRound(myEntity.posZ);
				final int var5 = MathHelper
						.floor_float(myEntity.rotationYaw * 256.0F / 360.0F);
				final int var6 = MathHelper
						.floor_float(myEntity.rotationPitch * 256.0F / 360.0F);
				final int var7 = var2 - lastScaledXPosition;
				final int var8 = var3 - lastScaledYPosition;
				final int var9 = var4 - lastScaledZPosition;
				Object var10 = null;
				final boolean var11 = Math.abs(var7) >= 4
						|| Math.abs(var8) >= 4 || Math.abs(var9) >= 4
						|| ticks % 60 == 0;
				final boolean var12 = Math.abs(var5 - lastYaw) >= 4
						|| Math.abs(var6 - lastPitch) >= 4;

				if (ticks > 0 || myEntity instanceof EntityArrow) {
					if (var7 >= -128 && var7 < 128 && var8 >= -128
							&& var8 < 128 && var9 >= -128 && var9 < 128
							&& ticksSinceLastForcedTeleport <= 400
							&& !ridingEntity) {
						if (var11 && var12) {
							var10 = new S14PacketEntity.S17PacketEntityLookMove(
									myEntity.getEntityId(), (byte) var7,
									(byte) var8, (byte) var9, (byte) var5,
									(byte) var6);
						} else if (var11) {
							var10 = new S14PacketEntity.S15PacketEntityRelMove(
									myEntity.getEntityId(), (byte) var7,
									(byte) var8, (byte) var9);
						} else if (var12) {
							var10 = new S14PacketEntity.S16PacketEntityLook(
									myEntity.getEntityId(), (byte) var5,
									(byte) var6);
						}
					} else {
						ticksSinceLastForcedTeleport = 0;
						var10 = new S18PacketEntityTeleport(
								myEntity.getEntityId(), var2, var3, var4,
								(byte) var5, (byte) var6);
					}
				}

				if (sendVelocityUpdates) {
					final double var13 = myEntity.motionX - motionX;
					final double var15 = myEntity.motionY - motionY;
					final double var17 = myEntity.motionZ - motionZ;
					final double var19 = 0.02D;
					final double var21 = var13 * var13 + var15 * var15 + var17
							* var17;

					if (var21 > var19 * var19 || var21 > 0.0D
							&& myEntity.motionX == 0.0D
							&& myEntity.motionY == 0.0D
							&& myEntity.motionZ == 0.0D) {
						motionX = myEntity.motionX;
						motionY = myEntity.motionY;
						motionZ = myEntity.motionZ;
						func_151259_a(new S12PacketEntityVelocity(
								myEntity.getEntityId(), motionX, motionY,
								motionZ));
					}
				}

				if (var10 != null) {
					func_151259_a((Packet) var10);
				}

				func_111190_b();

				if (var11) {
					lastScaledXPosition = var2;
					lastScaledYPosition = var3;
					lastScaledZPosition = var4;
				}

				if (var12) {
					lastYaw = var5;
					lastPitch = var6;
				}

				ridingEntity = false;
			} else {
				var2 = MathHelper
						.floor_float(myEntity.rotationYaw * 256.0F / 360.0F);
				var3 = MathHelper
						.floor_float(myEntity.rotationPitch * 256.0F / 360.0F);
				final boolean var25 = Math.abs(var2 - lastYaw) >= 4
						|| Math.abs(var3 - lastPitch) >= 4;

				if (var25) {
					func_151259_a(new S14PacketEntity.S16PacketEntityLook(
							myEntity.getEntityId(), (byte) var2, (byte) var3));
					lastYaw = var2;
					lastPitch = var3;
				}

				lastScaledXPosition = myEntity.myEntitySize
						.multiplyBy32AndRound(myEntity.posX);
				lastScaledYPosition = MathHelper
						.floor_double(myEntity.posY * 32.0D);
				lastScaledZPosition = myEntity.myEntitySize
						.multiplyBy32AndRound(myEntity.posZ);
				func_111190_b();
				ridingEntity = true;
			}

			var2 = MathHelper
					.floor_float(myEntity.getRotationYawHead() * 256.0F / 360.0F);

			if (Math.abs(var2 - lastHeadMotion) >= 4) {
				func_151259_a(new S19PacketEntityHeadLook(myEntity, (byte) var2));
				lastHeadMotion = var2;
			}

			myEntity.isAirBorne = false;
		}

		++ticks;

		if (myEntity.velocityChanged) {
			func_151261_b(new S12PacketEntityVelocity(myEntity));
			myEntity.velocityChanged = false;
		}
	}

	/**
	 * if the player is more than the distance threshold (typically 64) then the
	 * player is removed instead
	 */
	public void tryStartWachingThis(EntityPlayerMP p_73117_1_) {
		if (p_73117_1_ != myEntity) {
			final double var2 = p_73117_1_.posX - lastScaledXPosition / 32;
			final double var4 = p_73117_1_.posZ - lastScaledZPosition / 32;

			if (var2 >= -blocksDistanceThreshold
					&& var2 <= blocksDistanceThreshold
					&& var4 >= -blocksDistanceThreshold
					&& var4 <= blocksDistanceThreshold) {
				if (!trackingPlayers.contains(p_73117_1_)
						&& (isPlayerWatchingThisChunk(p_73117_1_) || myEntity.forceSpawn)) {
					trackingPlayers.add(p_73117_1_);
					final Packet var6 = func_151260_c();
					p_73117_1_.playerNetServerHandler.sendPacket(var6);

					if (!myEntity.getDataWatcher().getIsBlank()) {
						p_73117_1_.playerNetServerHandler
								.sendPacket(new S1CPacketEntityMetadata(
										myEntity.getEntityId(), myEntity
												.getDataWatcher(), true));
					}

					if (myEntity instanceof EntityLivingBase) {
						final ServersideAttributeMap var7 = (ServersideAttributeMap) ((EntityLivingBase) myEntity)
								.getAttributeMap();
						final Collection var8 = var7.getWatchedAttributes();

						if (!var8.isEmpty()) {
							p_73117_1_.playerNetServerHandler
									.sendPacket(new S20PacketEntityProperties(
											myEntity.getEntityId(), var8));
						}
					}

					motionX = myEntity.motionX;
					motionY = myEntity.motionY;
					motionZ = myEntity.motionZ;

					if (sendVelocityUpdates
							&& !(var6 instanceof S0FPacketSpawnMob)) {
						p_73117_1_.playerNetServerHandler
								.sendPacket(new S12PacketEntityVelocity(
										myEntity.getEntityId(),
										myEntity.motionX, myEntity.motionY,
										myEntity.motionZ));
					}

					if (myEntity.ridingEntity != null) {
						p_73117_1_.playerNetServerHandler
								.sendPacket(new S1BPacketEntityAttach(0,
										myEntity, myEntity.ridingEntity));
					}

					if (myEntity instanceof EntityLiving
							&& ((EntityLiving) myEntity).getLeashedToEntity() != null) {
						p_73117_1_.playerNetServerHandler
								.sendPacket(new S1BPacketEntityAttach(1,
										myEntity, ((EntityLiving) myEntity)
												.getLeashedToEntity()));
					}

					if (myEntity instanceof EntityLivingBase) {
						for (int var10 = 0; var10 < 5; ++var10) {
							final ItemStack var13 = ((EntityLivingBase) myEntity)
									.getEquipmentInSlot(var10);

							if (var13 != null) {
								p_73117_1_.playerNetServerHandler
										.sendPacket(new S04PacketEntityEquipment(
												myEntity.getEntityId(), var10,
												var13));
							}
						}
					}

					if (myEntity instanceof EntityPlayer) {
						final EntityPlayer var11 = (EntityPlayer) myEntity;

						if (var11.isPlayerSleeping()) {
							p_73117_1_.playerNetServerHandler
									.sendPacket(new S0APacketUseBed(
											var11,
											MathHelper
													.floor_double(myEntity.posX),
											MathHelper
													.floor_double(myEntity.posY),
											MathHelper
													.floor_double(myEntity.posZ)));
						}
					}

					if (myEntity instanceof EntityLivingBase) {
						final EntityLivingBase var12 = (EntityLivingBase) myEntity;
						final Iterator var14 = var12.getActivePotionEffects()
								.iterator();

						while (var14.hasNext()) {
							final PotionEffect var9 = (PotionEffect) var14
									.next();
							p_73117_1_.playerNetServerHandler
									.sendPacket(new S1DPacketEntityEffect(
											myEntity.getEntityId(), var9));
						}
					}
				}
			} else if (trackingPlayers.contains(p_73117_1_)) {
				trackingPlayers.remove(p_73117_1_);
				p_73117_1_.func_152339_d(myEntity);
			}
		}
	}
}
