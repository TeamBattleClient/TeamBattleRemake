package net.minecraft.client.multiplayer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFireworkStarterFX;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveHandlerMP;

public class WorldClient extends World {
	/** The ChunkProviderClient instance */
	private ChunkProviderClient clientChunkProvider;

	/**
	 * The hash set of entities handled by this client. Uses the entity's ID as
	 * the hash set's key.
	 */
	private final IntHashMap entityHashSet = new IntHashMap();

	/** Contains all entities for this client, both spawned and non-spawned. */
	private final Set entityList = new HashSet();

	/**
	 * Contains all entities for this client that were not spawned due to a
	 * non-present chunk. The game will attempt to spawn up to 10 pending
	 * entities with each subsequent tick until the spawn queue is empty.
	 */
	private final Set entitySpawnQueue = new HashSet();

	private final Minecraft mc = Minecraft.getMinecraft();
	private final Set previousActiveChunkSet = new HashSet();
	/** The packets that need to be sent to the server. */
	private final NetHandlerPlayClient sendQueue;

	public WorldClient(NetHandlerPlayClient p_i45063_1_,
			WorldSettings p_i45063_2_, int p_i45063_3_,
			EnumDifficulty p_i45063_4_, Profiler p_i45063_5_) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider
				.getProviderForDimension(p_i45063_3_), p_i45063_2_, p_i45063_5_);
		sendQueue = p_i45063_1_;
		difficultySetting = p_i45063_4_;
		this.setSpawnLocation(8, 64, 8);
		mapStorage = p_i45063_1_.mapStorageOrigin;
	}

	/**
	 * Add an ID to Entity mapping to entityHashSet
	 */
	public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_) {
		final Entity var3 = getEntityByID(p_73027_1_);

		if (var3 != null) {
			removeEntity(var3);
		}

		entityList.add(p_73027_2_);
		p_73027_2_.setEntityId(p_73027_1_);

		if (!spawnEntityInWorld(p_73027_2_)) {
			entitySpawnQueue.add(p_73027_2_);
		}

		entityHashSet.addKey(p_73027_1_, p_73027_2_);

		if (RenderManager.instance.getEntityRenderObject(p_73027_2_)
				.func_147905_a()) {
			mc.renderGlobal.onStaticEntitiesChanged();
		}
	}

	/**
	 * Adds some basic stats of the world to the given crash report.
	 */
	@Override
	public CrashReportCategory addWorldInfoToCrashReport(CrashReport p_72914_1_) {
		final CrashReportCategory var2 = super
				.addWorldInfoToCrashReport(p_72914_1_);
		var2.addCrashSectionCallable("Forced entities", new Callable() {

			@Override
			public String call() {
				return entityList.size() + " total; " + entityList.toString();
			}
		});
		var2.addCrashSectionCallable("Retry entities", new Callable() {

			@Override
			public String call() {
				return entitySpawnQueue.size() + " total; "
						+ entitySpawnQueue.toString();
			}
		});
		var2.addCrashSectionCallable("Server brand", new Callable() {

			@Override
			public String call() {
				return mc.thePlayer.func_142021_k();
			}
		});
		var2.addCrashSectionCallable("Server type", new Callable() {

			@Override
			public String call() {
				return mc.getIntegratedServer() == null ? "Non-integrated multiplayer server"
						: "Integrated singleplayer server";
			}
		});
		return var2;
	}

	/**
	 * Creates the chunk provider for this world. Called in the constructor.
	 * Retrieves provider from worldProvider?
	 */
	@Override
	protected IChunkProvider createChunkProvider() {
		clientChunkProvider = new ChunkProviderClient(this);
		return clientChunkProvider;
	}

	public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_) {
		if (p_73025_3_) {
			clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
		} else {
			clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
		}

		if (!p_73025_3_) {
			markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16,
					p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
		}
	}

	public void doVoidFogParticles(int p_73029_1_, int p_73029_2_,
			int p_73029_3_) {
		final byte var4 = 16;
		final Random var5 = new Random();

		for (int var6 = 0; var6 < 1000; ++var6) {
			final int var7 = p_73029_1_ + rand.nextInt(var4)
					- rand.nextInt(var4);
			final int var8 = p_73029_2_ + rand.nextInt(var4)
					- rand.nextInt(var4);
			final int var9 = p_73029_3_ + rand.nextInt(var4)
					- rand.nextInt(var4);
			final Block var10 = getBlock(var7, var8, var9);

			if (var10.getMaterial() == Material.air) {
				if (rand.nextInt(8) > var8
						&& provider.getWorldHasVoidParticles()) {
					spawnParticle("depthsuspend", var7 + rand.nextFloat(), var8
							+ rand.nextFloat(), var9 + rand.nextFloat(), 0.0D,
							0.0D, 0.0D);
				}
			} else {
				var10.randomDisplayTick(this, var7, var8, var9, var5);
			}
		}
	}

	@Override
	protected void func_147456_g() {
		super.func_147456_g();
		previousActiveChunkSet.retainAll(activeChunkSet);

		if (previousActiveChunkSet.size() == activeChunkSet.size()) {
			previousActiveChunkSet.clear();
		}

		int var1 = 0;
		final Iterator var2 = activeChunkSet.iterator();

		while (var2.hasNext()) {
			final ChunkCoordIntPair var3 = (ChunkCoordIntPair) var2.next();

			if (!previousActiveChunkSet.contains(var3)) {
				final int var4 = var3.chunkXPos * 16;
				final int var5 = var3.chunkZPos * 16;
				theProfiler.startSection("getChunk");
				final Chunk var6 = getChunkFromChunkCoords(var3.chunkXPos,
						var3.chunkZPos);
				func_147467_a(var4, var5, var6);
				theProfiler.endSection();
				previousActiveChunkSet.add(var3);
				++var1;

				if (var1 >= 10)
					return;
			}
		}
	}

	public boolean func_147492_c(int p_147492_1_, int p_147492_2_,
			int p_147492_3_, Block p_147492_4_, int p_147492_5_) {
		invalidateBlockReceiveRegion(p_147492_1_, p_147492_2_, p_147492_3_,
				p_147492_1_, p_147492_2_, p_147492_3_);
		return super.setBlock(p_147492_1_, p_147492_2_, p_147492_3_,
				p_147492_4_, p_147492_5_, 3);
	}

	@Override
	protected int func_152379_p() {
		return mc.gameSettings.renderDistanceChunks;
	}

	/**
	 * Returns the Entity with the given ID, or null if it doesn't exist in this
	 * World.
	 */
	@Override
	public Entity getEntityByID(int p_73045_1_) {
		return p_73045_1_ == mc.thePlayer.getEntityId() ? mc.thePlayer
				: (Entity) entityHashSet.lookup(p_73045_1_);
	}

	/**
	 * Invalidates an AABB region of blocks from the receive queue, in the event
	 * that the block has been modified client-side in the intervening 80
	 * receive ticks.
	 */
	public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_,
			int p_73031_3_, int p_73031_4_, int p_73031_5_, int p_73031_6_) {
	}

	@Override
	public void makeFireworks(double p_92088_1_, double p_92088_3_,
			double p_92088_5_, double p_92088_7_, double p_92088_9_,
			double p_92088_11_, NBTTagCompound p_92088_13_) {
		mc.effectRenderer.addEffect(new EntityFireworkStarterFX(this,
				p_92088_1_, p_92088_3_, p_92088_5_, p_92088_7_, p_92088_9_,
				p_92088_11_, mc.effectRenderer, p_92088_13_));
	}

	@Override
	protected void onEntityAdded(Entity p_72923_1_) {
		super.onEntityAdded(p_72923_1_);

		if (entitySpawnQueue.contains(p_72923_1_)) {
			entitySpawnQueue.remove(p_72923_1_);
		}
	}

	@Override
	protected void onEntityRemoved(Entity p_72847_1_) {
		super.onEntityRemoved(p_72847_1_);
		boolean var2 = false;

		if (entityList.contains(p_72847_1_)) {
			if (p_72847_1_.isEntityAlive()) {
				entitySpawnQueue.add(p_72847_1_);
				var2 = true;
			} else {
				entityList.remove(p_72847_1_);
			}
		}

		if (RenderManager.instance.getEntityRenderObject(p_72847_1_)
				.func_147905_a() && !var2) {
			mc.renderGlobal.onStaticEntitiesChanged();
		}
	}

	/**
	 * par8 is loudness, all pars passed to
	 * minecraftInstance.sndManager.playSound
	 */
	@Override
	public void playSound(double p_72980_1_, double p_72980_3_,
			double p_72980_5_, String p_72980_7_, float p_72980_8_,
			float p_72980_9_, boolean p_72980_10_) {
		final double var11 = mc.renderViewEntity.getDistanceSq(p_72980_1_,
				p_72980_3_, p_72980_5_);
		final PositionedSoundRecord var13 = new PositionedSoundRecord(
				new ResourceLocation(p_72980_7_), p_72980_8_, p_72980_9_,
				(float) p_72980_1_, (float) p_72980_3_, (float) p_72980_5_);

		if (p_72980_10_ && var11 > 100.0D) {
			final double var14 = Math.sqrt(var11) / 40.0D;
			mc.getSoundHandler().playDelayedSound(var13, (int) (var14 * 20.0D));
		} else {
			mc.getSoundHandler().playSound(var13);
		}
	}

	/**
	 * also releases skins.
	 */
	public void removeAllEntities() {
		loadedEntityList.removeAll(unloadedEntityList);
		int var1;
		Entity var2;
		int var3;
		int var4;

		for (var1 = 0; var1 < unloadedEntityList.size(); ++var1) {
			var2 = (Entity) unloadedEntityList.get(var1);
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;

			if (var2.addedToChunk && chunkExists(var3, var4)) {
				getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		for (var1 = 0; var1 < unloadedEntityList.size(); ++var1) {
			onEntityRemoved((Entity) unloadedEntityList.get(var1));
		}

		unloadedEntityList.clear();

		for (var1 = 0; var1 < loadedEntityList.size(); ++var1) {
			var2 = (Entity) loadedEntityList.get(var1);

			if (var2.ridingEntity != null) {
				if (!var2.ridingEntity.isDead
						&& var2.ridingEntity.riddenByEntity == var2) {
					continue;
				}

				var2.ridingEntity.riddenByEntity = null;
				var2.ridingEntity = null;
			}

			if (var2.isDead) {
				var3 = var2.chunkCoordX;
				var4 = var2.chunkCoordZ;

				if (var2.addedToChunk && chunkExists(var3, var4)) {
					getChunkFromChunkCoords(var3, var4).removeEntity(var2);
				}

				loadedEntityList.remove(var1--);
				onEntityRemoved(var2);
			}
		}
	}

	/**
	 * Schedule the entity for removal during the next tick. Marks the entity
	 * dead in anticipation.
	 */
	@Override
	public void removeEntity(Entity p_72900_1_) {
		super.removeEntity(p_72900_1_);
		entityList.remove(p_72900_1_);
	}

	public Entity removeEntityFromWorld(int p_73028_1_) {
		final Entity var2 = (Entity) entityHashSet.removeObject(p_73028_1_);

		if (var2 != null) {
			entityList.remove(var2);
			removeEntity(var2);
		}

		return var2;
	}

	/**
	 * If on MP, sends a quitting packet.
	 */
	@Override
	public void sendQuittingDisconnectingPacket() {
		sendQueue.getNetworkManager().closeChannel(
				new ChatComponentText("Quitting"));
	}

	public void setWorldScoreboard(Scoreboard p_96443_1_) {
		worldScoreboard = p_96443_1_;
	}

	/**
	 * Sets the world time.
	 */
	@Override
	public void setWorldTime(long p_72877_1_) {
		if (p_72877_1_ < 0L) {
			p_72877_1_ = -p_72877_1_;
			getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
		} else {
			getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
		}

		super.setWorldTime(p_72877_1_);
	}

	/**
	 * Called to place all entities as part of a world
	 */
	@Override
	public boolean spawnEntityInWorld(Entity p_72838_1_) {
		final boolean var2 = super.spawnEntityInWorld(p_72838_1_);
		entityList.add(p_72838_1_);

		if (!var2) {
			entitySpawnQueue.add(p_72838_1_);
		} else if (p_72838_1_ instanceof EntityMinecart) {
			mc.getSoundHandler().playSound(
					new MovingSoundMinecart((EntityMinecart) p_72838_1_));
		}

		return var2;
	}

	/**
	 * Runs a single tick for the world
	 */
	@Override
	public void tick() {
		super.tick();
		func_82738_a(getTotalWorldTime() + 1L);

		if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
			setWorldTime(getWorldTime() + 1L);
		}

		theProfiler.startSection("reEntryProcessing");

		for (int var1 = 0; var1 < 10 && !entitySpawnQueue.isEmpty(); ++var1) {
			final Entity var2 = (Entity) entitySpawnQueue.iterator().next();
			entitySpawnQueue.remove(var2);

			if (!loadedEntityList.contains(var2)) {
				spawnEntityInWorld(var2);
			}
		}

		theProfiler.endStartSection("connection");
		sendQueue.onNetworkTick();
		theProfiler.endStartSection("chunkCache");
		clientChunkProvider.unloadQueuedChunks();
		theProfiler.endStartSection("blocks");
		func_147456_g();
		theProfiler.endSection();
	}

	/**
	 * Updates all weather states.
	 */
	@Override
	protected void updateWeather() {
		if (!provider.hasNoSky) {
			;
		}
	}
}
