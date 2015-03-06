package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.ISaveHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServerOF extends WorldServer {
	private static final Logger logger = LogManager.getLogger();
	private boolean allChunksTicked = false;
	private int lastViewDistance = 0;
	private final Set limitedChunkSet = new HashSet();
	private NextTickHashSet pendingTickListEntriesHashSet;
	private List pendingTickListEntriesThisTick = new ArrayList();
	private TreeSet pendingTickListEntriesTreeSet;
	public Set setChunkCoordsToTickOnce = new HashSet();

	public WorldServerOF(MinecraftServer par1MinecraftServer,
			ISaveHandler par2iSaveHandler, String par3Str, int par4,
			WorldSettings par5WorldSettings, Profiler par6Profiler) {
		super(par1MinecraftServer, par2iSaveHandler, par3Str, par4,
				par5WorldSettings, par6Profiler);
		fixSetNextTicks();
	}

	public void addChunkToTickOnce(int cx, int cz) {
		final int viewDistance = func_152379_p();

		if (viewDistance > 10) {
			setChunkCoordsToTickOnce.add(new ChunkCoordIntPair(cx, cz));
		}
	}

	private boolean canSkipEntityUpdate(Entity entity) {
		if (!(entity instanceof EntityLivingBase))
			return false;
		else {
			final EntityLivingBase entityLiving = (EntityLivingBase) entity;

			if (entityLiving.isChild())
				return false;
			else if (entityLiving.hurtTime > 0)
				return false;
			else if (entity.ticksExisted < 20)
				return false;
			else if (playerEntities.size() != 1)
				return false;
			else {
				final Entity player = (Entity) playerEntities.get(0);
				final double dx = Math.abs(entity.posX - player.posX) - 16.0D;
				final double dz = Math.abs(entity.posZ - player.posZ) - 16.0D;
				final double distSq = dx * dx + dz * dz;
				return !entity.isInRangeToRenderDist(distSq);
			}
		}
	}

	private int findField(Field[] fields, Class cls, int startPos) {
		if (startPos < 0)
			return -1;
		else {
			for (int i = startPos; i < fields.length; ++i) {
				final Field field = fields[i];

				if (field.getType() == cls)
					return i;
			}

			return -1;
		}
	}

	private void fixSetNextTicks() {
		try {
			final Field[] e = WorldServer.class.getDeclaredFields();
			final int posSet = findField(e, Set.class, 0);
			final int posTreeSet = findField(e, TreeSet.class, posSet);
			final int posList = findField(e, List.class, posTreeSet);

			if (posSet >= 0 && posTreeSet >= 0 && posList >= 0) {
				final Field fieldSet = e[posSet];
				final Field fieldTreeSet = e[posTreeSet];
				final Field fieldList = e[posList];
				fieldSet.setAccessible(true);
				fieldTreeSet.setAccessible(true);
				fieldList.setAccessible(true);
				pendingTickListEntriesTreeSet = (TreeSet) fieldTreeSet
						.get(this);
				pendingTickListEntriesThisTick = (List) fieldList.get(this);
				final Set oldSet = (Set) fieldSet.get(this);

				if (oldSet instanceof NextTickHashSet)
					return;

				pendingTickListEntriesHashSet = new NextTickHashSet(oldSet);
				fieldSet.set(this, pendingTickListEntriesHashSet);
				Config.dbg("WorldServer.nextTickSet updated");
				return;
			}

			Config.warn("Error updating WorldServer.nextTickSet");
		} catch (final Exception var9) {
			Config.warn("Error setting WorldServer.nextTickSet: "
					+ var9.getMessage());
		}
	}

	private void fixWorldTime() {
		if (worldInfo.getGameType().getID() == 1) {
			final long time = getWorldTime();
			final long timeOfDay = time % 24000L;

			if (Config.isTimeDayOnly()) {
				if (timeOfDay <= 1000L) {
					setWorldTime(time - timeOfDay + 1001L);
				}

				if (timeOfDay >= 11000L) {
					setWorldTime(time - timeOfDay + 24001L);
				}
			}

			if (Config.isTimeNightOnly()) {
				if (timeOfDay <= 14000L) {
					setWorldTime(time - timeOfDay + 14001L);
				}

				if (timeOfDay >= 22000L) {
					setWorldTime(time - timeOfDay + 24000L + 14001L);
				}
			}
		}
	}

	private void fixWorldWeather() {
		if (worldInfo.isRaining() || worldInfo.isThundering()) {
			worldInfo.setRainTime(0);
			worldInfo.setRaining(false);
			setRainStrength(0.0F);
			worldInfo.setThunderTime(0);
			worldInfo.setThundering(false);
			setThunderStrength(0.0F);
			func_73046_m().getConfigurationManager().func_148540_a(
					new S2BPacketChangeGameState(2, 0.0F));
			func_73046_m().getConfigurationManager().func_148540_a(
					new S2BPacketChangeGameState(7, 0.0F));
			func_73046_m().getConfigurationManager().func_148540_a(
					new S2BPacketChangeGameState(8, 0.0F));
		}
	}

	@Override
	protected void func_147456_g() {
		final Set oldSet = activeChunkSet;

		if (limitedChunkSet.size() > 0) {
			activeChunkSet = limitedChunkSet;
		}

		super.func_147456_g();
		activeChunkSet = oldSet;
	}

	@Override
	public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2) {
		if (pendingTickListEntriesHashSet != null
				&& pendingTickListEntriesTreeSet != null
				&& pendingTickListEntriesThisTick != null) {
			ArrayList var3 = null;
			final ChunkCoordIntPair var4 = par1Chunk.getChunkCoordIntPair();
			final int var5 = (var4.chunkXPos << 4) - 2;
			final int var6 = var5 + 16 + 2;
			final int var7 = (var4.chunkZPos << 4) - 2;
			final int var8 = var7 + 16 + 2;

			for (int var9 = 0; var9 < 2; ++var9) {
				Iterator var10;

				if (var9 == 0) {
					final TreeSet var11 = new TreeSet();

					for (int dx = -1; dx <= 1; ++dx) {
						for (int dz = -1; dz <= 1; ++dz) {
							final HashSet set = pendingTickListEntriesHashSet
									.getNextTickEntriesSet(var4.chunkXPos + dx,
											var4.chunkZPos + dz);
							var11.addAll(set);
						}
					}

					var10 = var11.iterator();
				} else {
					var10 = pendingTickListEntriesThisTick.iterator();

					if (!pendingTickListEntriesThisTick.isEmpty()) {
						logger.debug("toBeTicked = "
								+ pendingTickListEntriesThisTick.size());
					}
				}

				while (var10.hasNext()) {
					final NextTickListEntry var15 = (NextTickListEntry) var10
							.next();

					if (var15.xCoord >= var5 && var15.xCoord < var6
							&& var15.zCoord >= var7 && var15.zCoord < var8) {
						if (par2) {
							pendingTickListEntriesHashSet.remove(var15);
							pendingTickListEntriesTreeSet.remove(var15);
							var10.remove();
						}

						if (var3 == null) {
							var3 = new ArrayList();
						}

						var3.add(var15);
					}
				}
			}

			return var3;
		} else
			return super.getPendingBlockUpdates(par1Chunk, par2);
	}

	@Override
	protected void initialize(WorldSettings par1WorldSettings) {
		super.initialize(par1WorldSettings);
		fixSetNextTicks();
	}

	@Override
	protected void setActivePlayerChunksAndCheckLight() {
		super.setActivePlayerChunksAndCheckLight();
		limitedChunkSet.clear();
		final int viewDistance = func_152379_p();

		if (viewDistance > 10) {
			if (viewDistance != lastViewDistance) {
				lastViewDistance = viewDistance;
				allChunksTicked = false;
			} else if (!allChunksTicked) {
				allChunksTicked = true;
			} else {
				for (int i = 0; i < playerEntities.size(); ++i) {
					final EntityPlayer player = (EntityPlayer) playerEntities
							.get(i);
					final int pcx = MathHelper
							.floor_double(player.posX / 16.0D);
					final int pcz = MathHelper
							.floor_double(player.posZ / 16.0D);
					final byte dist = 10;

					for (int cx = -dist; cx <= dist; ++cx) {
						for (int cz = -dist; cz <= dist; ++cz) {
							limitedChunkSet.add(new ChunkCoordIntPair(cx + pcx,
									cz + pcz));
						}
					}
				}

				if (setChunkCoordsToTickOnce.size() > 0) {
					limitedChunkSet.addAll(setChunkCoordsToTickOnce);
					setChunkCoordsToTickOnce.clear();
				}
			}
		}
	}

	/**
	 * Runs a single tick for the world
	 */
	@Override
	public void tick() {
		super.tick();

		if (!Config.isTimeDefault()) {
			fixWorldTime();
		}

		if (Config.waterOpacityChanged) {
			Config.waterOpacityChanged = false;
			updateWaterOpacity();
		}
	}

	/**
	 * Will update the entity in the world if the chunk the entity is in is
	 * currently loaded. Args: entity
	 */
	@Override
	public void updateEntity(Entity par1Entity) {
		if (canSkipEntityUpdate(par1Entity)
				&& par1Entity instanceof EntityLivingBase) {
			final EntityLivingBase elb = (EntityLivingBase) par1Entity;
			int entityAge = EntityUtils.getEntityAge(elb);
			++entityAge;

			if (elb instanceof EntityMob) {
				final float el = elb.getBrightness(1.0F);

				if (el > 0.5F) {
					entityAge += 2;
				}
			}

			EntityUtils.setEntityAge(elb, entityAge);

			if (elb instanceof EntityLiving) {
				final EntityLiving var5 = (EntityLiving) elb;
				EntityUtils.despawnEntity(var5);
			}
		} else {
			super.updateEntity(par1Entity);

			if (Config.isSmoothWorld()) {
				Thread.currentThread();
				Thread.yield();
			}
		}
	}

	public void updateWaterOpacity() {
		byte opacity = 3;

		if (Config.isClearWater()) {
			opacity = 1;
		}

		BlockUtils.setLightOpacity(Blocks.water, opacity);
		BlockUtils.setLightOpacity(Blocks.flowing_water, opacity);
		final IChunkProvider cp = chunkProvider;

		if (cp != null) {
			for (int x = -512; x < 512; ++x) {
				for (int z = -512; z < 512; ++z) {
					if (cp.chunkExists(x, z)) {
						final Chunk c = cp.provideChunk(x, z);

						if (c != null && !(c instanceof EmptyChunk)) {
							final ExtendedBlockStorage[] ebss = c
									.getBlockStorageArray();

							for (final ExtendedBlockStorage ebs : ebss) {
								if (ebs != null) {
									final NibbleArray na = ebs
											.getSkylightArray();

									if (na != null) {
										final byte[] data = na.data;

										for (int d = 0; d < data.length; ++d) {
											data[d] = 0;
										}
									}
								}
							}

							c.generateSkylightMap();
						}
					}
				}
			}
		}
	}

	/**
	 * Updates all weather states.
	 */
	@Override
	protected void updateWeather() {
		if (!Config.isWeatherEnabled()) {
			fixWorldWeather();
		}

		super.updateWeather();
	}
}
