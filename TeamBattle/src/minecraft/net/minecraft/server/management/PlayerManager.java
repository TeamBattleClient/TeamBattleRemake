package net.minecraft.server.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.src.CompactArrayList;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.src.WorldServerOF;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager {
	public class PlayerInstance {
		public boolean chunkLoaded;
		private final ChunkCoordIntPair chunkLocation;
		private final short[] field_151254_d;
		private int flagsYAreasToUpdate;
		private int numberOfTilesToUpdate;
		private final List playersWatchingChunk;

		private long previousWorldTime;

		public PlayerInstance(int par2, int par3) {
			this(par2, par3, false);
		}

		public PlayerInstance(int par2, int par3, boolean lazy) {
			playersWatchingChunk = new ArrayList();
			field_151254_d = new short[64];
			chunkLoaded = false;
			chunkLocation = new ChunkCoordIntPair(par2, par3);
			final boolean useLazy = lazy && Config.isLazyChunkLoading();

			if (useLazy
					&& !getWorldServer().theChunkProviderServer.chunkExists(
							par2, par3)) {
				chunkCoordsNotLoaded.add(chunkLocation);
				chunkLoaded = false;
			} else {
				getWorldServer().theChunkProviderServer.loadChunk(par2, par3);
				chunkLoaded = true;
			}
		}

		public void addPlayer(EntityPlayerMP par1EntityPlayerMP) {
			if (playersWatchingChunk.contains(par1EntityPlayerMP)) {
				PlayerManager.field_152627_a.debug(
						"Failed to add player. {} already is in chunk {}, {}",
						new Object[] { par1EntityPlayerMP,
								Integer.valueOf(chunkLocation.chunkXPos),
								Integer.valueOf(chunkLocation.chunkZPos) });
			} else {
				if (playersWatchingChunk.isEmpty()) {
					previousWorldTime = theWorldServer.getTotalWorldTime();
				}

				playersWatchingChunk.add(par1EntityPlayerMP);
				par1EntityPlayerMP.loadedChunks.add(chunkLocation);
			}
		}

		public void func_151251_a(Packet p_151251_1_) {
			for (int var2 = 0; var2 < playersWatchingChunk.size(); ++var2) {
				final EntityPlayerMP var3 = (EntityPlayerMP) playersWatchingChunk
						.get(var2);

				if (!var3.loadedChunks.contains(chunkLocation)) {
					var3.playerNetServerHandler.sendPacket(p_151251_1_);
				}
			}
		}

		private void func_151252_a(TileEntity p_151252_1_) {
			if (p_151252_1_ != null) {
				final Packet var2 = p_151252_1_.getDescriptionPacket();

				if (var2 != null) {
					func_151251_a(var2);
				}
			}
		}

		public void func_151253_a(int p_151253_1_, int p_151253_2_,
				int p_151253_3_) {
			if (numberOfTilesToUpdate == 0) {
				chunkWatcherWithPlayers.add(this);
			}

			flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);

			if (numberOfTilesToUpdate < 64) {
				final short var4 = (short) (p_151253_1_ << 12
						| p_151253_3_ << 8 | p_151253_2_);

				for (int var5 = 0; var5 < numberOfTilesToUpdate; ++var5) {
					if (field_151254_d[var5] == var4)
						return;
				}

				field_151254_d[numberOfTilesToUpdate++] = var4;
			}
		}

		private void increaseInhabitedTime(Chunk par1Chunk) {
			par1Chunk.inhabitedTime += theWorldServer.getTotalWorldTime()
					- previousWorldTime;
			previousWorldTime = theWorldServer.getTotalWorldTime();
		}

		public void processChunk() {
			increaseInhabitedTime(theWorldServer.getChunkFromChunkCoords(
					chunkLocation.chunkXPos, chunkLocation.chunkZPos));
		}

		public void removePlayer(EntityPlayerMP par1EntityPlayerMP) {
			this.removePlayer(par1EntityPlayerMP, true);
		}

		public void removePlayer(EntityPlayerMP par1EntityPlayerMP,
				boolean sendData) {
			if (playersWatchingChunk.contains(par1EntityPlayerMP)) {
				final Chunk var2 = theWorldServer.getChunkFromChunkCoords(
						chunkLocation.chunkXPos, chunkLocation.chunkZPos);

				if (sendData && var2.func_150802_k()) {
					par1EntityPlayerMP.playerNetServerHandler
							.sendPacket(new S21PacketChunkData(var2, true, 0));
				}

				playersWatchingChunk.remove(par1EntityPlayerMP);
				par1EntityPlayerMP.loadedChunks.remove(chunkLocation);

				if (Reflector.EventBus.exists()) {
					Reflector.postForgeBusEvent(
							Reflector.ChunkWatchEvent_UnWatch_Constructor,
							new Object[] { chunkLocation, par1EntityPlayerMP });
				}

				if (playersWatchingChunk.isEmpty()) {
					final long var3 = chunkLocation.chunkXPos + 2147483647L
							| chunkLocation.chunkZPos + 2147483647L << 32;
					increaseInhabitedTime(var2);
					playerInstances.remove(var3);
					playerInstanceList.remove(this);

					if (numberOfTilesToUpdate > 0) {
						chunkWatcherWithPlayers.remove(this);
					}

					if (chunkLoaded) {
						getWorldServer().theChunkProviderServer
								.unloadChunksIfNotNearSpawn(
										chunkLocation.chunkXPos,
										chunkLocation.chunkZPos);
					}
				}
			}
		}

		public void sendChunkUpdate() {
			if (numberOfTilesToUpdate != 0) {
				int var1;
				int var2;
				int var3;

				if (numberOfTilesToUpdate == 1) {
					var1 = chunkLocation.chunkXPos * 16
							+ (field_151254_d[0] >> 12 & 15);
					var2 = field_151254_d[0] & 255;
					var3 = chunkLocation.chunkZPos * 16
							+ (field_151254_d[0] >> 8 & 15);
					func_151251_a(new S23PacketBlockChange(var1, var2, var3,
							theWorldServer));

					if (theWorldServer.getBlock(var1, var2, var3)
							.hasTileEntity()) {
						func_151252_a(theWorldServer.getTileEntity(var1, var2,
								var3));
					}
				} else {
					int var4;

					if (numberOfTilesToUpdate == 64) {
						var1 = chunkLocation.chunkXPos * 16;
						var2 = chunkLocation.chunkZPos * 16;
						func_151251_a(new S21PacketChunkData(
								theWorldServer.getChunkFromChunkCoords(
										chunkLocation.chunkXPos,
										chunkLocation.chunkZPos), false,
								flagsYAreasToUpdate));

						for (var3 = 0; var3 < 16; ++var3) {
							if ((flagsYAreasToUpdate & 1 << var3) != 0) {
								var4 = var3 << 4;
								final List var5 = theWorldServer.func_147486_a(
										var1, var4, var2, var1 + 16, var4 + 16,
										var2 + 16);

								for (int var6 = 0; var6 < var5.size(); ++var6) {
									func_151252_a((TileEntity) var5.get(var6));
								}
							}
						}
					} else {
						func_151251_a(new S22PacketMultiBlockChange(
								numberOfTilesToUpdate, field_151254_d,
								theWorldServer.getChunkFromChunkCoords(
										chunkLocation.chunkXPos,
										chunkLocation.chunkZPos)));

						for (var1 = 0; var1 < numberOfTilesToUpdate; ++var1) {
							var2 = chunkLocation.chunkXPos * 16
									+ (field_151254_d[var1] >> 12 & 15);
							var3 = field_151254_d[var1] & 255;
							var4 = chunkLocation.chunkZPos * 16
									+ (field_151254_d[var1] >> 8 & 15);

							if (theWorldServer.getBlock(var2, var3, var4)
									.hasTileEntity()) {
								func_151252_a(theWorldServer.getTileEntity(
										var2, var3, var4));
							}
						}
					}
				}

				numberOfTilesToUpdate = 0;
				flagsYAreasToUpdate = 0;
			}
		}

		public void sendThisChunkToAllPlayers() {
			for (int i = 0; i < playersWatchingChunk.size(); ++i) {
				final EntityPlayerMP player = (EntityPlayerMP) playersWatchingChunk
						.get(i);
				final Chunk chunk = getWorldServer().getChunkFromChunkCoords(
						chunkLocation.chunkXPos, chunkLocation.chunkZPos);
				final ArrayList list = new ArrayList(1);
				list.add(chunk);
				player.playerNetServerHandler
						.sendPacket(new S26PacketMapChunkBulk(list));
			}
		}
	}

	private static final Logger field_152627_a = LogManager.getLogger();

	/**
	 * Get the furthest viewable block given player's view distance
	 */
	public static int getFurthestViewableBlock(int par0) {
		return par0 * 16 - 16;
	}

	public CompactArrayList chunkCoordsNotLoaded = new CompactArrayList(100,
			0.8F);

	/**
	 * contains a PlayerInstance for every chunk they can see. the
	 * "player instance" cotains a list of all players who can also that chunk
	 */
	private final List chunkWatcherWithPlayers = new ArrayList();

	/** This field is using when chunk should be processed (every 8000 ticks) */
	private final List playerInstanceList = new ArrayList();
	/**
	 * A map of chunk position (two ints concatenated into a long) to
	 * PlayerInstance
	 */
	private final LongHashMap playerInstances = new LongHashMap();

	/** players in the current instance */
	private final List players = new ArrayList();

	/**
	 * Number of chunks the server sends to the client. Valid 3<=x<=15. In
	 * server.properties.
	 */
	private int playerViewRadius;

	/** time what is using to check if InhabitedTime should be calculated */
	private long previousTotalWorldTime;

	private final WorldServer theWorldServer;

	/** x, z direction vectors: east, south, west, north */
	private final int[][] xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 },
			{ -1, 0 }, { 0, -1 } };

	public PlayerManager(WorldServer par1Minecraft) {
		theWorldServer = par1Minecraft;
		func_152622_a(par1Minecraft.func_73046_m().getConfigurationManager()
				.getViewDistance());
	}

	/**
	 * Adds an EntityPlayerMP to the PlayerManager.
	 */
	public void addPlayer(EntityPlayerMP par1EntityPlayerMP) {
		final int var2 = (int) par1EntityPlayerMP.posX >> 4;
		final int var3 = (int) par1EntityPlayerMP.posZ >> 4;
		par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
		par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
		final ArrayList spawnList = new ArrayList(1);

		for (int var4 = var2 - playerViewRadius; var4 <= var2
				+ playerViewRadius; ++var4) {
			for (int var5 = var3 - playerViewRadius; var5 <= var3
					+ playerViewRadius; ++var5) {
				this.getOrCreateChunkWatcher(var4, var5, true).addPlayer(
						par1EntityPlayerMP);

				if (var4 >= var2 - 1 && var4 <= var2 + 1 && var5 >= var3 - 1
						&& var5 <= var3 + 1) {
					final Chunk spawnChunk = getWorldServer().theChunkProviderServer
							.loadChunk(var4, var5);
					spawnList.add(spawnChunk);
				}
			}
		}

		par1EntityPlayerMP.playerNetServerHandler
				.sendPacket(new S26PacketMapChunkBulk(spawnList));
		players.add(par1EntityPlayerMP);
		filterChunkLoadQueue(par1EntityPlayerMP);
	}

	/**
	 * Removes all chunks from the given player's chunk load queue that are not
	 * in viewing range of the player.
	 */
	public void filterChunkLoadQueue(EntityPlayerMP par1EntityPlayerMP) {
		final ArrayList var2 = new ArrayList(par1EntityPlayerMP.loadedChunks);
		int var3 = 0;
		final int var4 = playerViewRadius;
		final int var5 = (int) par1EntityPlayerMP.posX >> 4;
		final int var6 = (int) par1EntityPlayerMP.posZ >> 4;
		int var7 = 0;
		int var8 = 0;
		ChunkCoordIntPair var9 = this.getOrCreateChunkWatcher(var5, var6, true).chunkLocation;
		par1EntityPlayerMP.loadedChunks.clear();

		if (var2.contains(var9)) {
			par1EntityPlayerMP.loadedChunks.add(var9);
		}

		int var10;

		for (var10 = 1; var10 <= var4 * 2; ++var10) {
			for (int var11 = 0; var11 < 2; ++var11) {
				final int[] var12 = xzDirectionsConst[var3++ % 4];

				for (int var13 = 0; var13 < var10; ++var13) {
					var7 += var12[0];
					var8 += var12[1];
					var9 = this.getOrCreateChunkWatcher(var5 + var7, var6
							+ var8, true).chunkLocation;

					if (var2.contains(var9)) {
						par1EntityPlayerMP.loadedChunks.add(var9);
					}
				}
			}
		}

		var3 %= 4;

		for (var10 = 0; var10 < var4 * 2; ++var10) {
			var7 += xzDirectionsConst[var3][0];
			var8 += xzDirectionsConst[var3][1];
			var9 = this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true).chunkLocation;

			if (var2.contains(var9)) {
				par1EntityPlayerMP.loadedChunks.add(var9);
			}
		}
	}

	public void func_151250_a(int p_151250_1_, int p_151250_2_, int p_151250_3_) {
		final int var4 = p_151250_1_ >> 4;
		final int var5 = p_151250_3_ >> 4;
		final PlayerManager.PlayerInstance var6 = this.getOrCreateChunkWatcher(
				var4, var5, false);

		if (var6 != null) {
			var6.func_151253_a(p_151250_1_ & 15, p_151250_2_, p_151250_3_ & 15);
		}
	}

	public boolean func_152621_a(int p_152621_1_, int p_152621_2_) {
		final long var3 = p_152621_1_ + 2147483647L
				| p_152621_2_ + 2147483647L << 32;
		return playerInstances.getValueByKey(var3) != null;
	}

	public void func_152622_a(int p_152622_1_) {
		p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 32);

		if (p_152622_1_ != playerViewRadius) {
			final int var2 = p_152622_1_ - playerViewRadius;
			final Iterator var3 = players.iterator();

			while (var3.hasNext()) {
				final EntityPlayerMP var4 = (EntityPlayerMP) var3.next();
				final int var5 = (int) var4.posX >> 4;
				final int var6 = (int) var4.posZ >> 4;
				int var7;
				int var8;

				if (var2 > 0) {
					for (var7 = var5 - p_152622_1_; var7 <= var5 + p_152622_1_; ++var7) {
						for (var8 = var6 - p_152622_1_; var8 <= var6
								+ p_152622_1_; ++var8) {
							final PlayerManager.PlayerInstance var9 = this
									.getOrCreateChunkWatcher(var7, var8, true,
											true);

							if (!var9.playersWatchingChunk.contains(var4)) {
								var9.addPlayer(var4);
							}
						}
					}
				} else {
					for (var7 = var5 - playerViewRadius; var7 <= var5
							+ playerViewRadius; ++var7) {
						for (var8 = var6 - playerViewRadius; var8 <= var6
								+ playerViewRadius; ++var8) {
							if (!overlaps(var7, var8, var5, var6, p_152622_1_)) {
								this.getOrCreateChunkWatcher(var7, var8, true)
										.removePlayer(var4);
							}
						}
					}
				}
			}

			playerViewRadius = p_152622_1_;
		}
	}

	public PlayerManager.PlayerInstance getOrCreateChunkWatcher(int par1,
			int par2, boolean par3) {
		return this.getOrCreateChunkWatcher(par1, par2, par3, false);
	}

	public PlayerManager.PlayerInstance getOrCreateChunkWatcher(int par1,
			int par2, boolean par3, boolean lazy) {
		final long var4 = par1 + 2147483647L | par2 + 2147483647L << 32;
		PlayerManager.PlayerInstance var6 = (PlayerManager.PlayerInstance) playerInstances
				.getValueByKey(var4);

		if (var6 == null && par3) {
			var6 = new PlayerManager.PlayerInstance(par1, par2, lazy);
			playerInstances.add(var4, var6);
			playerInstanceList.add(var6);
		}

		return var6;
	}

	public WorldServer getWorldServer() {
		return theWorldServer;
	}

	public boolean isPlayerWatchingChunk(EntityPlayerMP par1EntityPlayerMP,
			int par2, int par3) {
		final PlayerManager.PlayerInstance var4 = this.getOrCreateChunkWatcher(
				par2, par3, false);
		return var4 != null
				&& var4.playersWatchingChunk.contains(par1EntityPlayerMP)
				&& !par1EntityPlayerMP.loadedChunks
						.contains(var4.chunkLocation);
	}

	/**
	 * Determine if two rectangles centered at the given points overlap for the
	 * provided radius. Arguments: x1, z1, x2, z2, radius.
	 */
	private boolean overlaps(int par1, int par2, int par3, int par4, int par5) {
		final int var6 = par1 - par3;
		final int var7 = par2 - par4;
		return var6 >= -par5 && var6 <= par5 ? var7 >= -par5 && var7 <= par5
				: false;
	}

	/**
	 * Removes an EntityPlayerMP from the PlayerManager.
	 */
	public void removePlayer(EntityPlayerMP par1EntityPlayerMP) {
		final int var2 = (int) par1EntityPlayerMP.managedPosX >> 4;
		final int var3 = (int) par1EntityPlayerMP.managedPosZ >> 4;

		for (int var4 = var2 - playerViewRadius; var4 <= var2
				+ playerViewRadius; ++var4) {
			for (int var5 = var3 - playerViewRadius; var5 <= var3
					+ playerViewRadius; ++var5) {
				final PlayerManager.PlayerInstance var6 = this
						.getOrCreateChunkWatcher(var4, var5, false);

				if (var6 != null) {
					var6.removePlayer(par1EntityPlayerMP, false);
				}
			}
		}

		players.remove(par1EntityPlayerMP);
	}

	/**
	 * update chunks around a player being moved by server logic (e.g. cart,
	 * boat)
	 */
	public void updateMountedMovingPlayer(EntityPlayerMP par1EntityPlayerMP) {
		final int var2 = (int) par1EntityPlayerMP.posX >> 4;
		final int var3 = (int) par1EntityPlayerMP.posZ >> 4;
		final double var4 = par1EntityPlayerMP.managedPosX
				- par1EntityPlayerMP.posX;
		final double var6 = par1EntityPlayerMP.managedPosZ
				- par1EntityPlayerMP.posZ;
		final double var8 = var4 * var4 + var6 * var6;

		if (var8 >= 64.0D) {
			final int var10 = (int) par1EntityPlayerMP.managedPosX >> 4;
			final int var11 = (int) par1EntityPlayerMP.managedPosZ >> 4;
			final int var12 = playerViewRadius;
			final int var13 = var2 - var10;
			final int var14 = var3 - var11;

			if (var13 != 0 || var14 != 0) {
				WorldServerOF worldServerOf = null;

				if (theWorldServer instanceof WorldServerOF) {
					worldServerOf = (WorldServerOF) theWorldServer;
				}

				for (int var15 = var2 - var12; var15 <= var2 + var12; ++var15) {
					for (int var16 = var3 - var12; var16 <= var3 + var12; ++var16) {
						if (!overlaps(var15, var16, var10, var11, var12)) {
							this.getOrCreateChunkWatcher(var15, var16, true,
									true).addPlayer(par1EntityPlayerMP);

							if (worldServerOf != null) {
								worldServerOf.addChunkToTickOnce(var15, var16);
							}
						}

						if (!overlaps(var15 - var13, var16 - var14, var2, var3,
								var12)) {
							final PlayerManager.PlayerInstance var17 = this
									.getOrCreateChunkWatcher(var15 - var13,
											var16 - var14, false);

							if (var17 != null) {
								var17.removePlayer(par1EntityPlayerMP);
							}
						}
					}
				}

				filterChunkLoadQueue(par1EntityPlayerMP);
				par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
				par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
			}
		}
	}

	/**
	 * updates all the player instances that need to be updated
	 */
	public void updatePlayerInstances() {
		final long var1 = theWorldServer.getTotalWorldTime();
		int var3;
		PlayerManager.PlayerInstance var4;

		if (var1 - previousTotalWorldTime > 8000L) {
			previousTotalWorldTime = var1;

			for (var3 = 0; var3 < playerInstanceList.size(); ++var3) {
				var4 = (PlayerManager.PlayerInstance) playerInstanceList
						.get(var3);
				var4.sendChunkUpdate();
				var4.processChunk();
			}
		} else {
			for (var3 = 0; var3 < chunkWatcherWithPlayers.size(); ++var3) {
				var4 = (PlayerManager.PlayerInstance) chunkWatcherWithPlayers
						.get(var3);
				var4.sendChunkUpdate();
			}
		}

		chunkWatcherWithPlayers.clear();

		if (players.isEmpty()) {
			final WorldProvider ip = theWorldServer.provider;

			if (!ip.canRespawnHere()) {
				theWorldServer.theChunkProviderServer.unloadAllChunks();
			}
		}

		if (chunkCoordsNotLoaded.size() > 0) {
			for (int var22 = 0; var22 < players.size(); ++var22) {
				final EntityPlayerMP player = (EntityPlayerMP) players
						.get(var22);
				final int px = player.chunkCoordX;
				final int pz = player.chunkCoordZ;
				final int maxRadius = playerViewRadius + 1;
				final int maxRadius2 = maxRadius / 2;
				final int maxDistSq = maxRadius * maxRadius + maxRadius2
						* maxRadius2;
				int bestDistSq = maxDistSq;
				int bestIndex = -1;
				PlayerManager.PlayerInstance bestCw = null;
				ChunkCoordIntPair bestCoords = null;

				for (int chunk = 0; chunk < chunkCoordsNotLoaded.size(); ++chunk) {
					final ChunkCoordIntPair coords = (ChunkCoordIntPair) chunkCoordsNotLoaded
							.get(chunk);

					if (coords != null) {
						final PlayerManager.PlayerInstance cw = this
								.getOrCreateChunkWatcher(coords.chunkXPos,
										coords.chunkZPos, false);

						if (cw != null && !cw.chunkLoaded) {
							final int dx = px - coords.chunkXPos;
							final int dz = pz - coords.chunkZPos;
							final int distSq = dx * dx + dz * dz;

							if (distSq < bestDistSq) {
								bestDistSq = distSq;
								bestIndex = chunk;
								bestCw = cw;
								bestCoords = coords;
							}
						} else {
							chunkCoordsNotLoaded.set(chunk, (Object) null);
						}
					}
				}

				if (bestIndex >= 0) {
					chunkCoordsNotLoaded.set(bestIndex, (Object) null);
				}

				if (bestCw != null) {
					bestCw.chunkLoaded = true;
					getWorldServer().theChunkProviderServer.loadChunk(
							bestCoords.chunkXPos, bestCoords.chunkZPos);
					bestCw.sendThisChunkToAllPlayers();
					break;
				}
			}

			chunkCoordsNotLoaded.compact();
		}
	}
}
