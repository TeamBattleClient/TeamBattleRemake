package net.minecraft.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.storage.ISaveHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

public class WorldServer extends World {
	static class ServerBlockEventList extends ArrayList {

		private ServerBlockEventList() {
		}

		ServerBlockEventList(Object p_i1521_1_) {
			this();
		}
	}

	private static final WeightedRandomChestContent[] bonusChestContent = new WeightedRandomChestContent[] {
			new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10),
			new WeightedRandomChestContent(
					Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log),
					0, 1, 3, 10),
			new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5),
			new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2),
					0, 1, 3, 10) };
	private static final Logger logger = LogManager.getLogger();
	/** is false if there are no players */
	private boolean allPlayersSleeping;
	private final SpawnerAnimals animalSpawner = new SpawnerAnimals();

	/** An IntHashMap of entity IDs (integers) to their Entity objects. */
	private IntHashMap entityIdMap;
	private int field_147489_T;

	private final WorldServer.ServerBlockEventList[] field_147490_S = new WorldServer.ServerBlockEventList[] {
			new WorldServer.ServerBlockEventList(null),
			new WorldServer.ServerBlockEventList(null) };

	/** Whether or not level saving is enabled */
	public boolean levelSaving;
	private final MinecraftServer mcServer;

	private Set pendingTickListEntriesHashSet;
	private final List pendingTickListEntriesThisTick = new ArrayList();
	/** All work to do in future ticks. */
	private TreeSet pendingTickListEntriesTreeSet;
	public ChunkProviderServer theChunkProviderServer;
	private final EntityTracker theEntityTracker;
	private final PlayerManager thePlayerManager;

	private int updateEntityTick;

	/**
	 * the teleporter to use when the entity is being transferred into the
	 * dimension
	 */
	private final Teleporter worldTeleporter;

	public WorldServer(MinecraftServer p_i45284_1_, ISaveHandler p_i45284_2_,
			String p_i45284_3_, int p_i45284_4_, WorldSettings p_i45284_5_,
			Profiler p_i45284_6_) {
		super(p_i45284_2_, p_i45284_3_, p_i45284_5_, WorldProvider
				.getProviderForDimension(p_i45284_4_), p_i45284_6_);
		mcServer = p_i45284_1_;
		theEntityTracker = new EntityTracker(this);
		thePlayerManager = new PlayerManager(this);

		if (entityIdMap == null) {
			entityIdMap = new IntHashMap();
		}

		if (pendingTickListEntriesHashSet == null) {
			pendingTickListEntriesHashSet = new HashSet();
		}

		if (pendingTickListEntriesTreeSet == null) {
			pendingTickListEntriesTreeSet = new TreeSet();
		}

		worldTeleporter = new Teleporter(this);
		worldScoreboard = new ServerScoreboard(p_i45284_1_);
		ScoreboardSaveData var7 = (ScoreboardSaveData) mapStorage.loadData(
				ScoreboardSaveData.class, "scoreboard");

		if (var7 == null) {
			var7 = new ScoreboardSaveData();
			mapStorage.setData("scoreboard", var7);
		}

		var7.func_96499_a(worldScoreboard);
		((ServerScoreboard) worldScoreboard).func_96547_a(var7);
	}

	/**
	 * adds a lightning bolt to the list of lightning bolts in this world.
	 */
	@Override
	public boolean addWeatherEffect(Entity p_72942_1_) {
		if (super.addWeatherEffect(p_72942_1_)) {
			mcServer.getConfigurationManager().func_148541_a(p_72942_1_.posX,
					p_72942_1_.posY, p_72942_1_.posZ, 512.0D,
					provider.dimensionId,
					new S2CPacketSpawnGlobalEntity(p_72942_1_));
			return true;
		} else
			return false;
	}

	public boolean areAllPlayersAsleep() {
		if (allPlayersSleeping && !isClient) {
			final Iterator var1 = playerEntities.iterator();
			EntityPlayer var2;

			do {
				if (!var1.hasNext())
					return true;

				var2 = (EntityPlayer) var1.next();
			} while (var2.isPlayerFullyAsleep());

			return false;
		} else
			return false;
	}

	/**
	 * Called when checking if a certain block can be mined or not. The 'spawn
	 * safe zone' check is located here.
	 */
	@Override
	public boolean canMineBlock(EntityPlayer p_72962_1_, int p_72962_2_,
			int p_72962_3_, int p_72962_4_) {
		return !mcServer.isBlockProtected(this, p_72962_2_, p_72962_3_,
				p_72962_4_, p_72962_1_);
	}

	/**
	 * Creates the bonus chest in the world.
	 */
	protected void createBonusChest() {
		final WorldGeneratorBonusChest var1 = new WorldGeneratorBonusChest(
				bonusChestContent, 10);

		for (int var2 = 0; var2 < 10; ++var2) {
			final int var3 = worldInfo.getSpawnX() + rand.nextInt(6)
					- rand.nextInt(6);
			final int var4 = worldInfo.getSpawnZ() + rand.nextInt(6)
					- rand.nextInt(6);
			final int var5 = getTopSolidOrLiquidBlock(var3, var4) + 1;

			if (var1.generate(this, rand, var3, var5, var4)) {
				break;
			}
		}
	}

	/**
	 * Creates the chunk provider for this world. Called in the constructor.
	 * Retrieves provider from worldProvider?
	 */
	@Override
	protected IChunkProvider createChunkProvider() {
		final IChunkLoader var1 = saveHandler.getChunkLoader(provider);
		theChunkProviderServer = new ChunkProviderServer(this, var1,
				provider.createChunkGenerator());
		return theChunkProviderServer;
	}

	/**
	 * creates a spawn position at random within 256 blocks of 0,0
	 */
	protected void createSpawnPosition(WorldSettings p_73052_1_) {
		if (!provider.canRespawnHere()) {
			worldInfo.setSpawnPosition(0, provider.getAverageGroundLevel(), 0);
		} else {
			findingSpawnPoint = true;
			final WorldChunkManager var2 = provider.worldChunkMgr;
			final List var3 = var2.getBiomesToSpawnIn();
			final Random var4 = new Random(getSeed());
			final ChunkPosition var5 = var2
					.func_150795_a(0, 0, 256, var3, var4);
			int var6 = 0;
			final int var7 = provider.getAverageGroundLevel();
			int var8 = 0;

			if (var5 != null) {
				var6 = var5.field_151329_a;
				var8 = var5.field_151328_c;
			} else {
				logger.warn("Unable to find spawn biome");
			}

			int var9 = 0;

			while (!provider.canCoordinateBeSpawn(var6, var8)) {
				var6 += var4.nextInt(64) - var4.nextInt(64);
				var8 += var4.nextInt(64) - var4.nextInt(64);
				++var9;

				if (var9 == 1000) {
					break;
				}
			}

			worldInfo.setSpawnPosition(var6, var7, var8);
			findingSpawnPoint = false;

			if (p_73052_1_.isBonusChestEnabled()) {
				createBonusChest();
			}
		}
	}

	/**
	 * Syncs all changes to disk and wait for completion.
	 */
	public void flush() {
		saveHandler.flush();
	}

	@Override
	public void func_147446_b(int p_147446_1_, int p_147446_2_,
			int p_147446_3_, Block p_147446_4_, int p_147446_5_, int p_147446_6_) {
		final NextTickListEntry var7 = new NextTickListEntry(p_147446_1_,
				p_147446_2_, p_147446_3_, p_147446_4_);
		var7.setPriority(p_147446_6_);

		if (p_147446_4_.getMaterial() != Material.air) {
			var7.setScheduledTime(p_147446_5_ + worldInfo.getWorldTotalTime());
		}

		if (!pendingTickListEntriesHashSet.contains(var7)) {
			pendingTickListEntriesHashSet.add(var7);
			pendingTickListEntriesTreeSet.add(var7);
		}
	}

	@Override
	public void func_147452_c(int p_147452_1_, int p_147452_2_,
			int p_147452_3_, Block p_147452_4_, int p_147452_5_, int p_147452_6_) {
		final BlockEventData var7 = new BlockEventData(p_147452_1_,
				p_147452_2_, p_147452_3_, p_147452_4_, p_147452_5_, p_147452_6_);
		final Iterator var8 = field_147490_S[field_147489_T].iterator();
		BlockEventData var9;

		do {
			if (!var8.hasNext()) {
				field_147490_S[field_147489_T].add(var7);
				return;
			}

			var9 = (BlockEventData) var8.next();
		} while (!var9.equals(var7));
	}

	@Override
	public void func_147454_a(int p_147454_1_, int p_147454_2_,
			int p_147454_3_, Block p_147454_4_, int p_147454_5_, int p_147454_6_) {
		final NextTickListEntry var7 = new NextTickListEntry(p_147454_1_,
				p_147454_2_, p_147454_3_, p_147454_4_);
		byte var8 = 0;

		if (scheduledUpdatesAreImmediate
				&& p_147454_4_.getMaterial() != Material.air) {
			if (p_147454_4_.func_149698_L()) {
				var8 = 8;

				if (checkChunksExist(var7.xCoord - var8, var7.yCoord - var8,
						var7.zCoord - var8, var7.xCoord + var8, var7.yCoord
								+ var8, var7.zCoord + var8)) {
					final Block var9 = getBlock(var7.xCoord, var7.yCoord,
							var7.zCoord);

					if (var9.getMaterial() != Material.air
							&& var9 == var7.func_151351_a()) {
						var9.updateTick(this, var7.xCoord, var7.yCoord,
								var7.zCoord, rand);
					}
				}

				return;
			}

			p_147454_5_ = 1;
		}

		if (checkChunksExist(p_147454_1_ - var8, p_147454_2_ - var8,
				p_147454_3_ - var8, p_147454_1_ + var8, p_147454_2_ + var8,
				p_147454_3_ + var8)) {
			if (p_147454_4_.getMaterial() != Material.air) {
				var7.setScheduledTime(p_147454_5_
						+ worldInfo.getWorldTotalTime());
				var7.setPriority(p_147454_6_);
			}

			if (!pendingTickListEntriesHashSet.contains(var7)) {
				pendingTickListEntriesHashSet.add(var7);
				pendingTickListEntriesTreeSet.add(var7);
			}
		}
	}

	@Override
	protected void func_147456_g() {
		super.func_147456_g();
		final Iterator var3 = activeChunkSet.iterator();

		while (var3.hasNext()) {
			final ChunkCoordIntPair var4 = (ChunkCoordIntPair) var3.next();
			final int var5 = var4.chunkXPos * 16;
			final int var6 = var4.chunkZPos * 16;
			theProfiler.startSection("getChunk");
			final Chunk var7 = getChunkFromChunkCoords(var4.chunkXPos,
					var4.chunkZPos);
			func_147467_a(var5, var6, var7);
			theProfiler.endStartSection("tickChunk");
			var7.func_150804_b(false);
			theProfiler.endStartSection("thunder");
			int var8;
			int var9;
			int var10;
			int var11;

			if (rand.nextInt(100000) == 0 && isRaining() && isThundering()) {
				updateLCG = updateLCG * 3 + 1013904223;
				var8 = updateLCG >> 2;
				var9 = var5 + (var8 & 15);
				var10 = var6 + (var8 >> 8 & 15);
				var11 = getPrecipitationHeight(var9, var10);

				if (canLightningStrikeAt(var9, var11, var10)) {
					addWeatherEffect(new EntityLightningBolt(this, var9, var11,
							var10));
				}
			}

			theProfiler.endStartSection("iceandsnow");

			if (rand.nextInt(16) == 0) {
				updateLCG = updateLCG * 3 + 1013904223;
				var8 = updateLCG >> 2;
				var9 = var8 & 15;
				var10 = var8 >> 8 & 15;
				var11 = getPrecipitationHeight(var9 + var5, var10 + var6);

				if (isBlockFreezableNaturally(var9 + var5, var11 - 1, var10
						+ var6)) {
					this.setBlock(var9 + var5, var11 - 1, var10 + var6,
							Blocks.ice);
				}

				if (isRaining()
						&& func_147478_e(var9 + var5, var11, var10 + var6, true)) {
					this.setBlock(var9 + var5, var11, var10 + var6,
							Blocks.snow_layer);
				}

				if (isRaining()) {
					final BiomeGenBase var12 = getBiomeGenForCoords(
							var9 + var5, var10 + var6);

					if (var12.canSpawnLightningBolt()) {
						getBlock(var9 + var5, var11 - 1, var10 + var6)
								.fillWithRain(this, var9 + var5, var11 - 1,
										var10 + var6);
					}
				}
			}

			theProfiler.endStartSection("tickBlocks");
			final ExtendedBlockStorage[] var18 = var7.getBlockStorageArray();
			var9 = var18.length;

			for (var10 = 0; var10 < var9; ++var10) {
				final ExtendedBlockStorage var19 = var18[var10];

				if (var19 != null && var19.getNeedsRandomTick()) {
					for (int var20 = 0; var20 < 3; ++var20) {
						updateLCG = updateLCG * 3 + 1013904223;
						final int var13 = updateLCG >> 2;
						final int var14 = var13 & 15;
						final int var15 = var13 >> 8 & 15;
						final int var16 = var13 >> 16 & 15;
						final Block var17 = var19.func_150819_a(var14, var16,
								var15);

						if (var17.getTickRandomly()) {
							var17.updateTick(this, var14 + var5,
									var16 + var19.getYLocation(), var15 + var6,
									rand);
						}
					}
				}
			}

			theProfiler.endSection();
		}
	}

	@Override
	public boolean func_147477_a(int p_147477_1_, int p_147477_2_,
			int p_147477_3_, Block p_147477_4_) {
		final NextTickListEntry var5 = new NextTickListEntry(p_147477_1_,
				p_147477_2_, p_147477_3_, p_147477_4_);
		return pendingTickListEntriesThisTick.contains(var5);
	}

	private boolean func_147485_a(BlockEventData p_147485_1_) {
		final Block var2 = getBlock(p_147485_1_.func_151340_a(),
				p_147485_1_.func_151342_b(), p_147485_1_.func_151341_c());
		return var2 == p_147485_1_.getBlock() ? var2.onBlockEventReceived(this,
				p_147485_1_.func_151340_a(), p_147485_1_.func_151342_b(),
				p_147485_1_.func_151341_c(), p_147485_1_.getEventID(),
				p_147485_1_.getEventParameter()) : false;
	}

	public List func_147486_a(int p_147486_1_, int p_147486_2_,
			int p_147486_3_, int p_147486_4_, int p_147486_5_, int p_147486_6_) {
		final ArrayList var7 = new ArrayList();

		for (int var8 = 0; var8 < field_147482_g.size(); ++var8) {
			final TileEntity var9 = (TileEntity) field_147482_g.get(var8);

			if (var9.field_145851_c >= p_147486_1_
					&& var9.field_145848_d >= p_147486_2_
					&& var9.field_145849_e >= p_147486_3_
					&& var9.field_145851_c < p_147486_4_
					&& var9.field_145848_d < p_147486_5_
					&& var9.field_145849_e < p_147486_6_) {
				var7.add(var9);
			}
		}

		return var7;
	}

	public void func_147487_a(String p_147487_1_, double p_147487_2_,
			double p_147487_4_, double p_147487_6_, int p_147487_8_,
			double p_147487_9_, double p_147487_11_, double p_147487_13_,
			double p_147487_15_) {
		final S2APacketParticles var17 = new S2APacketParticles(p_147487_1_,
				(float) p_147487_2_, (float) p_147487_4_, (float) p_147487_6_,
				(float) p_147487_9_, (float) p_147487_11_,
				(float) p_147487_13_, (float) p_147487_15_, p_147487_8_);

		for (int var18 = 0; var18 < playerEntities.size(); ++var18) {
			final EntityPlayerMP var19 = (EntityPlayerMP) playerEntities
					.get(var18);
			final ChunkCoordinates var20 = var19.getPlayerCoordinates();
			final double var21 = p_147487_2_ - var20.posX;
			final double var23 = p_147487_4_ - var20.posY;
			final double var25 = p_147487_6_ - var20.posZ;
			final double var27 = var21 * var21 + var23 * var23 + var25 * var25;

			if (var27 <= 256.0D) {
				var19.playerNetServerHandler.sendPacket(var17);
			}
		}
	}

	private void func_147488_Z() {
		while (!field_147490_S[field_147489_T].isEmpty()) {
			final int var1 = field_147489_T;
			field_147489_T ^= 1;
			final Iterator var2 = field_147490_S[var1].iterator();

			while (var2.hasNext()) {
				final BlockEventData var3 = (BlockEventData) var2.next();

				if (func_147485_a(var3)) {
					mcServer.getConfigurationManager().func_148541_a(
							var3.func_151340_a(),
							var3.func_151342_b(),
							var3.func_151341_c(),
							64.0D,
							provider.dimensionId,
							new S24PacketBlockAction(var3.func_151340_a(), var3
									.func_151342_b(), var3.func_151341_c(),
									var3.getBlock(), var3.getEventID(), var3
											.getEventParameter()));
				}
			}

			field_147490_S[var1].clear();
		}
	}

	@Override
	protected int func_152379_p() {
		return mcServer.getConfigurationManager().getViewDistance();
	}

	public MinecraftServer func_73046_m() {
		return mcServer;
	}

	public Teleporter getDefaultTeleporter() {
		return worldTeleporter;
	}

	/**
	 * Returns the Entity with the given ID, or null if it doesn't exist in this
	 * World.
	 */
	@Override
	public Entity getEntityByID(int p_73045_1_) {
		return (Entity) entityIdMap.lookup(p_73045_1_);
	}

	/**
	 * Gets the EntityTracker
	 */
	public EntityTracker getEntityTracker() {
		return theEntityTracker;
	}

	/**
	 * Gets the hard-coded portal location to use when entering this dimension.
	 */
	public ChunkCoordinates getEntrancePortalLocation() {
		return provider.getEntrancePortalLocation();
	}

	@Override
	public List getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
		ArrayList var3 = null;
		final ChunkCoordIntPair var4 = p_72920_1_.getChunkCoordIntPair();
		final int var5 = (var4.chunkXPos << 4) - 2;
		final int var6 = var5 + 16 + 2;
		final int var7 = (var4.chunkZPos << 4) - 2;
		final int var8 = var7 + 16 + 2;

		for (int var9 = 0; var9 < 2; ++var9) {
			Iterator var10;

			if (var9 == 0) {
				var10 = pendingTickListEntriesTreeSet.iterator();
			} else {
				var10 = pendingTickListEntriesThisTick.iterator();

				if (!pendingTickListEntriesThisTick.isEmpty()) {
					logger.debug("toBeTicked = "
							+ pendingTickListEntriesThisTick.size());
				}
			}

			while (var10.hasNext()) {
				final NextTickListEntry var11 = (NextTickListEntry) var10
						.next();

				if (var11.xCoord >= var5 && var11.xCoord < var6
						&& var11.zCoord >= var7 && var11.zCoord < var8) {
					if (p_72920_2_) {
						pendingTickListEntriesHashSet.remove(var11);
						var10.remove();
					}

					if (var3 == null) {
						var3 = new ArrayList();
					}

					var3.add(var11);
				}
			}
		}

		return var3;
	}

	public PlayerManager getPlayerManager() {
		return thePlayerManager;
	}

	@Override
	protected void initialize(WorldSettings p_72963_1_) {
		if (entityIdMap == null) {
			entityIdMap = new IntHashMap();
		}

		if (pendingTickListEntriesHashSet == null) {
			pendingTickListEntriesHashSet = new HashSet();
		}

		if (pendingTickListEntriesTreeSet == null) {
			pendingTickListEntriesTreeSet = new TreeSet();
		}

		createSpawnPosition(p_72963_1_);
		super.initialize(p_72963_1_);
	}

	/**
	 * returns a new explosion. Does initiation (at time of writing Explosion is
	 * not finished)
	 */
	@Override
	public Explosion newExplosion(Entity p_72885_1_, double p_72885_2_,
			double p_72885_4_, double p_72885_6_, float p_72885_8_,
			boolean p_72885_9_, boolean p_72885_10_) {
		final Explosion var11 = new Explosion(this, p_72885_1_, p_72885_2_,
				p_72885_4_, p_72885_6_, p_72885_8_);
		var11.isFlaming = p_72885_9_;
		var11.isSmoking = p_72885_10_;
		var11.doExplosionA();
		var11.doExplosionB(false);

		if (!p_72885_10_) {
			var11.affectedBlockPositions.clear();
		}

		final Iterator var12 = playerEntities.iterator();

		while (var12.hasNext()) {
			final EntityPlayer var13 = (EntityPlayer) var12.next();

			if (var13.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D) {
				((EntityPlayerMP) var13).playerNetServerHandler
						.sendPacket(new S27PacketExplosion(p_72885_2_,
								p_72885_4_, p_72885_6_, p_72885_8_,
								var11.affectedBlockPositions, (Vec3) var11
										.func_77277_b().get(var13)));
			}
		}

		return var11;
	}

	@Override
	protected void onEntityAdded(Entity p_72923_1_) {
		super.onEntityAdded(p_72923_1_);
		entityIdMap.addKey(p_72923_1_.getEntityId(), p_72923_1_);
		final Entity[] var2 = p_72923_1_.getParts();

		if (var2 != null) {
			for (final Entity element : var2) {
				entityIdMap.addKey(element.getEntityId(), element);
			}
		}
	}

	@Override
	protected void onEntityRemoved(Entity p_72847_1_) {
		super.onEntityRemoved(p_72847_1_);
		entityIdMap.removeObject(p_72847_1_.getEntityId());
		final Entity[] var2 = p_72847_1_.getParts();

		if (var2 != null) {
			for (final Entity element : var2) {
				entityIdMap.removeObject(element.getEntityId());
			}
		}
	}

	private void resetRainAndThunder() {
		worldInfo.setRainTime(0);
		worldInfo.setRaining(false);
		worldInfo.setThunderTime(0);
		worldInfo.setThundering(false);
	}

	/**
	 * Resets the updateEntityTick field to 0
	 */
	public void resetUpdateEntityTick() {
		updateEntityTick = 0;
	}

	/**
	 * Saves all chunks to disk while updating progress bar.
	 */
	public void saveAllChunks(boolean p_73044_1_, IProgressUpdate p_73044_2_)
			throws MinecraftException {
		if (chunkProvider.canSave()) {
			if (p_73044_2_ != null) {
				p_73044_2_.displayProgressMessage("Saving level");
			}

			saveLevel();

			if (p_73044_2_ != null) {
				p_73044_2_.resetProgresAndWorkingMessage("Saving chunks");
			}

			chunkProvider.saveChunks(p_73044_1_, p_73044_2_);
			final ArrayList var3 = Lists.newArrayList(theChunkProviderServer
					.func_152380_a());
			final Iterator var4 = var3.iterator();

			while (var4.hasNext()) {
				final Chunk var5 = (Chunk) var4.next();

				if (var5 != null
						&& !thePlayerManager.func_152621_a(var5.xPosition,
								var5.zPosition)) {
					theChunkProviderServer.unloadChunksIfNotNearSpawn(
							var5.xPosition, var5.zPosition);
				}
			}
		}
	}

	/**
	 * saves chunk data - currently only called during execution of the Save All
	 * command
	 */
	public void saveChunkData() {
		if (chunkProvider.canSave()) {
			chunkProvider.saveExtraData();
		}
	}

	/**
	 * Saves the chunks to disk.
	 */
	protected void saveLevel() throws MinecraftException {
		checkSessionLock();
		saveHandler.saveWorldInfoWithPlayer(worldInfo, mcServer
				.getConfigurationManager().getHostPlayerData());
		mapStorage.saveAllData();
	}

	/**
	 * Schedules a tick to a block with a delay (Most commonly the tick rate)
	 */
	@Override
	public void scheduleBlockUpdate(int p_147464_1_, int p_147464_2_,
			int p_147464_3_, Block p_147464_4_, int p_147464_5_) {
		func_147454_a(p_147464_1_, p_147464_2_, p_147464_3_, p_147464_4_,
				p_147464_5_, 0);
	}

	/**
	 * sends a Packet 38 (Entity Status) to all tracked players of that entity
	 */
	@Override
	public void setEntityState(Entity p_72960_1_, byte p_72960_2_) {
		getEntityTracker().func_151248_b(p_72960_1_,
				new S19PacketEntityStatus(p_72960_1_, p_72960_2_));
	}

	/**
	 * Sets a new spawn location by finding an uncovered block at a random (x,z)
	 * location in the chunk.
	 */
	@Override
	public void setSpawnLocation() {
		if (worldInfo.getSpawnY() <= 0) {
			worldInfo.setSpawnY(64);
		}

		int var1 = worldInfo.getSpawnX();
		int var2 = worldInfo.getSpawnZ();
		int var3 = 0;

		while (getTopBlock(var1, var2).getMaterial() == Material.air) {
			var1 += rand.nextInt(8) - rand.nextInt(8);
			var2 += rand.nextInt(8) - rand.nextInt(8);
			++var3;

			if (var3 == 10000) {
				break;
			}
		}

		worldInfo.setSpawnX(var1);
		worldInfo.setSpawnZ(var2);
	}

	/**
	 * only spawns creatures allowed by the chunkProvider
	 */
	public BiomeGenBase.SpawnListEntry spawnRandomCreature(
			EnumCreatureType p_73057_1_, int p_73057_2_, int p_73057_3_,
			int p_73057_4_) {
		final List var5 = getChunkProvider().getPossibleCreatures(p_73057_1_,
				p_73057_2_, p_73057_3_, p_73057_4_);
		return var5 != null && !var5.isEmpty() ? (BiomeGenBase.SpawnListEntry) WeightedRandom
				.getRandomItem(rand, var5) : null;
	}

	/**
	 * Runs a single tick for the world
	 */
	@Override
	public void tick() {
		super.tick();

		if (getWorldInfo().isHardcoreModeEnabled()
				&& difficultySetting != EnumDifficulty.HARD) {
			difficultySetting = EnumDifficulty.HARD;
		}

		provider.worldChunkMgr.cleanupCache();

		if (areAllPlayersAsleep()) {
			if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
				final long var1 = worldInfo.getWorldTime() + 24000L;
				worldInfo.setWorldTime(var1 - var1 % 24000L);
			}

			wakeAllPlayers();
		}

		theProfiler.startSection("mobSpawner");

		if (getGameRules().getGameRuleBooleanValue("doMobSpawning")) {
			animalSpawner.findChunksForSpawning(this, spawnHostileMobs,
					spawnPeacefulMobs,
					worldInfo.getWorldTotalTime() % 400L == 0L);
		}

		theProfiler.endStartSection("chunkSource");
		chunkProvider.unloadQueuedChunks();
		final int var3 = calculateSkylightSubtracted(1.0F);

		if (var3 != skylightSubtracted) {
			skylightSubtracted = var3;
		}

		worldInfo.incrementTotalWorldTime(worldInfo.getWorldTotalTime() + 1L);

		if (getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
			worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
		}

		theProfiler.endStartSection("tickPending");
		tickUpdates(false);
		theProfiler.endStartSection("tickBlocks");
		func_147456_g();
		theProfiler.endStartSection("chunkMap");
		thePlayerManager.updatePlayerInstances();
		theProfiler.endStartSection("village");
		villageCollectionObj.tick();
		villageSiegeObj.tick();
		theProfiler.endStartSection("portalForcer");
		worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
		theProfiler.endSection();
		func_147488_Z();
	}

	/**
	 * Runs through the list of updates to run and ticks them
	 */
	@Override
	public boolean tickUpdates(boolean p_72955_1_) {
		int var2 = pendingTickListEntriesTreeSet.size();

		if (var2 != pendingTickListEntriesHashSet.size())
			throw new IllegalStateException("TickNextTick list out of synch");
		else {
			if (var2 > 1000) {
				var2 = 1000;
			}

			theProfiler.startSection("cleaning");
			NextTickListEntry var4;

			for (int var3 = 0; var3 < var2; ++var3) {
				var4 = (NextTickListEntry) pendingTickListEntriesTreeSet
						.first();

				if (!p_72955_1_
						&& var4.scheduledTime > worldInfo.getWorldTotalTime()) {
					break;
				}

				pendingTickListEntriesTreeSet.remove(var4);
				pendingTickListEntriesHashSet.remove(var4);
				pendingTickListEntriesThisTick.add(var4);
			}

			theProfiler.endSection();
			theProfiler.startSection("ticking");
			final Iterator var14 = pendingTickListEntriesThisTick.iterator();

			while (var14.hasNext()) {
				var4 = (NextTickListEntry) var14.next();
				var14.remove();
				final byte var5 = 0;

				if (checkChunksExist(var4.xCoord - var5, var4.yCoord - var5,
						var4.zCoord - var5, var4.xCoord + var5, var4.yCoord
								+ var5, var4.zCoord + var5)) {
					final Block var6 = getBlock(var4.xCoord, var4.yCoord,
							var4.zCoord);

					if (var6.getMaterial() != Material.air
							&& Block.isEqualTo(var6, var4.func_151351_a())) {
						try {
							var6.updateTick(this, var4.xCoord, var4.yCoord,
									var4.zCoord, rand);
						} catch (final Throwable var13) {
							final CrashReport var8 = CrashReport
									.makeCrashReport(var13,
											"Exception while ticking a block");
							final CrashReportCategory var9 = var8
									.makeCategory("Block being ticked");
							int var10;

							try {
								var10 = getBlockMetadata(var4.xCoord,
										var4.yCoord, var4.zCoord);
							} catch (final Throwable var12) {
								var10 = -1;
							}

							CrashReportCategory.func_147153_a(var9,
									var4.xCoord, var4.yCoord, var4.zCoord,
									var6, var10);
							throw new ReportedException(var8);
						}
					}
				} else {
					scheduleBlockUpdate(var4.xCoord, var4.yCoord, var4.zCoord,
							var4.func_151351_a(), 0);
				}
			}

			theProfiler.endSection();
			pendingTickListEntriesThisTick.clear();
			return !pendingTickListEntriesTreeSet.isEmpty();
		}
	}

	/**
	 * Updates the flag that indicates whether or not all players in the world
	 * are sleeping.
	 */
	@Override
	public void updateAllPlayersSleepingFlag() {
		allPlayersSleeping = !playerEntities.isEmpty();
		final Iterator var1 = playerEntities.iterator();

		while (var1.hasNext()) {
			final EntityPlayer var2 = (EntityPlayer) var1.next();

			if (!var2.isPlayerSleeping()) {
				allPlayersSleeping = false;
				break;
			}
		}
	}

	/**
	 * Updates (and cleans up) entities and tile entities
	 */
	@Override
	public void updateEntities() {
		if (playerEntities.isEmpty()) {
			if (updateEntityTick++ >= 1200)
				return;
		} else {
			resetUpdateEntityTick();
		}

		super.updateEntities();
	}

	/**
	 * Will update the entity in the world if the chunk the entity is in is
	 * currently loaded or its forced to update. Args: entity, forceUpdate
	 */
	@Override
	public void updateEntityWithOptionalForce(Entity p_72866_1_,
			boolean p_72866_2_) {
		if (!mcServer.getCanSpawnAnimals()
				&& (p_72866_1_ instanceof EntityAnimal || p_72866_1_ instanceof EntityWaterMob)) {
			p_72866_1_.setDead();
		}

		if (!mcServer.getCanSpawnNPCs() && p_72866_1_ instanceof INpc) {
			p_72866_1_.setDead();
		}

		super.updateEntityWithOptionalForce(p_72866_1_, p_72866_2_);
	}

	/**
	 * Updates all weather states.
	 */
	@Override
	protected void updateWeather() {
		final boolean var1 = isRaining();
		super.updateWeather();

		if (prevRainingStrength != rainingStrength) {
			mcServer.getConfigurationManager().func_148537_a(
					new S2BPacketChangeGameState(7, rainingStrength),
					provider.dimensionId);
		}

		if (prevThunderingStrength != thunderingStrength) {
			mcServer.getConfigurationManager().func_148537_a(
					new S2BPacketChangeGameState(8, thunderingStrength),
					provider.dimensionId);
		}

		if (var1 != isRaining()) {
			if (var1) {
				mcServer.getConfigurationManager().func_148540_a(
						new S2BPacketChangeGameState(2, 0.0F));
			} else {
				mcServer.getConfigurationManager().func_148540_a(
						new S2BPacketChangeGameState(1, 0.0F));
			}

			mcServer.getConfigurationManager().func_148540_a(
					new S2BPacketChangeGameState(7, rainingStrength));
			mcServer.getConfigurationManager().func_148540_a(
					new S2BPacketChangeGameState(8, thunderingStrength));
		}
	}

	protected void wakeAllPlayers() {
		allPlayersSleeping = false;
		final Iterator var1 = playerEntities.iterator();

		while (var1.hasNext()) {
			final EntityPlayer var2 = (EntityPlayer) var1.next();

			if (var2.isPlayerSleeping()) {
				var2.wakeUpPlayer(false, false, true);
			}
		}

		resetRainAndThunder();
	}
}
