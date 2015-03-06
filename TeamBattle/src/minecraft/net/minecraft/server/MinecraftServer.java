package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public abstract class MinecraftServer implements ICommandSender, Runnable,
		IPlayerUsage {
	public static final File field_152367_a = new File("usercache.json");

	private static final Logger logger = LogManager.getLogger();

	/** Instance of Minecraft Server. */
	private static MinecraftServer mcServer;

	/**
	 * Gets mcServer.
	 */
	public static MinecraftServer getServer() {
		return mcServer;
	}

	/**
	 * returns the difference, measured in milliseconds, between the current
	 * system time and midnight, January 1, 1970 UTC.
	 */
	public static long getSystemTimeMillis() {
		return System.currentTimeMillis();
	}

	/** Determines if flight is allowed or not. */
	private boolean allowFlight;
	private final ISaveFormat anvilConverterForAnvilFile;
	/** Maximum build height. */
	private int buildLimit;
	/** True if the server has animals turned on. */
	private boolean canSpawnAnimals;
	private boolean canSpawnNPCs;
	private final ICommandManager commandManager;

	/**
	 * The task the server is currently working on(and will output on
	 * outputPercentRemaining).
	 */
	public String currentTask;

	private boolean enableBonusChest;

	private int field_143008_E = 0;

	private final String field_147141_M = "";
	private long field_147142_T = 0L;

	private final MinecraftSessionService field_147143_S;

	private final NetworkSystem field_147144_o;

	private final Random field_147146_q = new Random();

	private final ServerStatusResponse field_147147_p = new ServerStatusResponse();
	private final YggdrasilAuthenticationService field_152364_T;

	private final GameProfileRepository field_152365_W;

	private final PlayerProfileCache field_152366_X;

	private String folderName;

	private boolean isDemo;
	private boolean isGamemodeForced;
	/** The server MOTD string. */
	private String motd;

	/** True if the server is in online mode. */
	private boolean onlineMode;
	/** The percentage of the current task finished so far. */
	public int percentDone;

	/** Indicates whether PvP is active on the server or not. */
	private boolean pvpEnabled;
	/** The ServerConfigurationManager instance. */
	private ServerConfigurationManager serverConfigManager;
	private boolean serverIsRunning;
	private KeyPair serverKeyPair;
	/** Username of the server owner (for integrated servers) */
	private String serverOwner;

	protected final Proxy serverProxy;
	/**
	 * Indicates whether the server is running or not. Set to false to initiate
	 * a shutdown.
	 */
	private boolean serverRunning = true;
	private boolean startProfiling;

	public final Profiler theProfiler = new Profiler();
	/**
	 * Collection of objects to update every tick. Type:
	 * List<IUpdatePlayerListBox>
	 */
	private final List tickables = new ArrayList();
	/** Incremented every tick. */
	private int tickCounter;
	public final long[] tickTimeArray = new long[100];
	/** Stats are [dimension][tick%100] system.nanoTime is stored. */
	public long[][] timeOfLastDimensionTick;
	/**
	 * Set when warned for "Can't keep up", which triggers again after 15
	 * seconds.
	 */
	private long timeOfLastWarning;
	/** The PlayerUsageSnooper instance. */
	private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper(
			"server", this, getSystemTimeMillis());
	private String userMessage;
	/**
	 * If true, there is no need to save chunks or stop the server, because that
	 * is already being done.
	 */
	private boolean worldIsBeingDeleted;

	private String worldName;

	/** The server world instances. */
	public WorldServer[] worldServers;

	public MinecraftServer(File p_i45281_1_, Proxy p_i45281_2_) {
		field_152366_X = new PlayerProfileCache(this, field_152367_a);
		mcServer = this;
		serverProxy = p_i45281_2_;
		field_147144_o = new NetworkSystem(this);
		commandManager = new ServerCommandManager();
		anvilConverterForAnvilFile = new AnvilSaveConverter(p_i45281_1_);
		field_152364_T = new YggdrasilAuthenticationService(p_i45281_2_, UUID
				.randomUUID().toString());
		field_147143_S = field_152364_T.createMinecraftSessionService();
		field_152365_W = field_152364_T.createProfileRepository();
	}

	/**
	 * Notifies this sender of some sort of information. This is for messages
	 * intended to display to the user. Used for typical output (like
	 * "you asked for whether or not this game rule is set, so here's your answer"
	 * ), warnings (like "I fetched this block for you by ID, but I'd like you
	 * to know that every time you do this, I die a little
	 * inside"), and errors (like "it's not called iron_pixacke, silly").
	 */
	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		logger.info(p_145747_1_.getUnformattedText());
	}

	/**
	 * Adds the server info, including from theWorldServer, to the crash report.
	 */
	public CrashReport addServerInfoToCrashReport(CrashReport p_71230_1_) {
		p_71230_1_.getCategory().addCrashSectionCallable("Profiler Position",
				new Callable() {

					@Override
					public String call() {
						return theProfiler.profilingEnabled ? theProfiler
								.getNameOfLastSection() : "N/A (disabled)";
					}
				});

		if (worldServers != null && worldServers.length > 0
				&& worldServers[0] != null) {
			p_71230_1_.getCategory().addCrashSectionCallable("Vec3 Pool Size",
					new Callable() {

						@Override
						public String call() {
							final byte var1 = 0;
							final int var2 = 56 * var1;
							final int var3 = var2 / 1024 / 1024;
							final byte var4 = 0;
							final int var5 = 56 * var4;
							final int var6 = var5 / 1024 / 1024;
							return var1 + " (" + var2 + " bytes; " + var3
									+ " MB) allocated, " + var4 + " (" + var5
									+ " bytes; " + var6 + " MB) used";
						}
					});
		}

		if (serverConfigManager != null) {
			p_71230_1_.getCategory().addCrashSectionCallable("Player Count",
					new Callable() {

						@Override
						public String call() {
							return serverConfigManager.getCurrentPlayerCount()
									+ " / "
									+ serverConfigManager.getMaxPlayers()
									+ "; "
									+ serverConfigManager.playerEntityList;
						}
					});
		}

		return p_71230_1_;
	}

	@Override
	public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_) {
		p_70000_1_.func_152768_a("whitelist_enabled", Boolean.valueOf(false));
		p_70000_1_.func_152768_a("whitelist_count", Integer.valueOf(0));
		p_70000_1_.func_152768_a("players_current",
				Integer.valueOf(getCurrentPlayerCount()));
		p_70000_1_.func_152768_a("players_max",
				Integer.valueOf(getMaxPlayers()));
		p_70000_1_.func_152768_a("players_seen", Integer
				.valueOf(serverConfigManager.getAvailablePlayerDat().length));
		p_70000_1_.func_152768_a("uses_auth", Boolean.valueOf(onlineMode));
		p_70000_1_.func_152768_a("gui_state", getGuiEnabled() ? "enabled"
				: "disabled");
		p_70000_1_.func_152768_a("run_time", Long
				.valueOf((getSystemTimeMillis() - p_70000_1_
						.getMinecraftStartTimeMillis()) / 60L * 1000L));
		p_70000_1_.func_152768_a("avg_tick_ms", Integer
				.valueOf((int) (MathHelper.average(tickTimeArray) * 1.0E-6D)));
		int var2 = 0;

		for (final WorldServer var4 : worldServers) {
			if (var4 != null) {
				final WorldInfo var5 = var4.getWorldInfo();
				p_70000_1_.func_152768_a("world[" + var2 + "][dimension]",
						Integer.valueOf(var4.provider.dimensionId));
				p_70000_1_.func_152768_a("world[" + var2 + "][mode]",
						var5.getGameType());
				p_70000_1_.func_152768_a("world[" + var2 + "][difficulty]",
						var4.difficultySetting);
				p_70000_1_.func_152768_a("world[" + var2 + "][hardcore]",
						Boolean.valueOf(var5.isHardcoreModeEnabled()));
				p_70000_1_.func_152768_a("world[" + var2 + "][generator_name]",
						var5.getTerrainType().getWorldTypeName());
				p_70000_1_.func_152768_a("world[" + var2
						+ "][generator_version]", Integer.valueOf(var5
						.getTerrainType().getGeneratorVersion()));
				p_70000_1_.func_152768_a("world[" + var2 + "][height]",
						Integer.valueOf(buildLimit));
				p_70000_1_.func_152768_a("world[" + var2 + "][chunks_loaded]",
						Integer.valueOf(var4.getChunkProvider()
								.getLoadedChunkCount()));
				++var2;
			}
		}

		p_70000_1_.func_152768_a("worlds", Integer.valueOf(var2));
	}

	@Override
	public void addServerTypeToSnooper(PlayerUsageSnooper p_70001_1_) {
		p_70001_1_.func_152767_b("singleplayer",
				Boolean.valueOf(isSinglePlayer()));
		p_70001_1_.func_152767_b("server_brand", getServerModName());
		p_70001_1_.func_152767_b("gui_supported",
				GraphicsEnvironment.isHeadless() ? "headless" : "supported");
		p_70001_1_.func_152767_b("dedicated",
				Boolean.valueOf(isDedicatedServer()));
	}

	protected boolean allowSpawnMonsters() {
		return true;
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return true;
	}

	public void canCreateBonusChest(boolean p_71194_1_) {
		enableBonusChest = p_71194_1_;
	}

	public abstract boolean canStructuresSpawn();

	/**
	 * Set current task to null and set its percentage to 0.
	 */
	protected void clearCurrentTask() {
		currentTask = null;
		percentDone = 0;
	}

	protected void convertMapIfNeeded(String p_71237_1_) {
		if (getActiveAnvilConverter().isOldMapFormat(p_71237_1_)) {
			logger.info("Converting map!");
			setUserMessage("menu.convertingLevel");
			getActiveAnvilConverter().convertMapFormat(p_71237_1_,
					new IProgressUpdate() {
						private long field_96245_b = System.currentTimeMillis();

						@Override
						public void displayProgressMessage(String p_73720_1_) {
						}

						@Override
						public void func_146586_a() {
						}

						@Override
						public void resetProgresAndWorkingMessage(
								String p_73719_1_) {
						}

						@Override
						public void resetProgressAndMessage(String p_73721_1_) {
						}

						@Override
						public void setLoadingProgress(int p_73718_1_) {
							if (System.currentTimeMillis() - field_96245_b >= 1000L) {
								field_96245_b = System.currentTimeMillis();
								MinecraftServer.logger.info("Converting... "
										+ p_73718_1_ + "%");
							}
						}
					});
		}
	}

	/**
	 * WARNING : directly calls
	 * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer
	 * [0].getSaveHandler().getWorldDirectoryName());
	 */
	public void deleteWorldAndStopServer() {
		worldIsBeingDeleted = true;
		getActiveAnvilConverter().flushCache();

		for (final WorldServer var2 : worldServers) {
			if (var2 != null) {
				var2.flush();
			}
		}

		getActiveAnvilConverter().deleteWorldDirectory(
				worldServers[0].getSaveHandler().getWorldDirectoryName());
		initiateShutdown();
	}

	public void enableProfiling() {
		startProfiling = true;
	}

	/**
	 * Called on exit from the main run() loop.
	 */
	protected void finalTick(CrashReport p_71228_1_) {
	}

	public abstract int func_110455_j();

	public void func_143006_e(int p_143006_1_) {
		field_143008_E = p_143006_1_;
	}

	public int func_143007_ar() {
		return field_143008_E;
	}

	@Override
	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	public MinecraftSessionService func_147130_as() {
		return field_147143_S;
	}

	public void func_147132_au() {
		field_147142_T = 0L;
	}

	public String func_147133_T() {
		return field_147141_M;
	}

	public ServerStatusResponse func_147134_at() {
		return field_147147_p;
	}

	public abstract EnumDifficulty func_147135_j();

	public boolean func_147136_ar() {
		return true;
	}

	public NetworkSystem func_147137_ag() {
		return field_147144_o;
	}

	private void func_147138_a(ServerStatusResponse p_147138_1_) {
		final File var2 = getFile("server-icon.png");

		if (var2.isFile()) {
			final ByteBuf var3 = Unpooled.buffer();

			try {
				final BufferedImage var4 = ImageIO.read(var2);
				Validate.validState(var4.getWidth() == 64,
						"Must be 64 pixels wide", new Object[0]);
				Validate.validState(var4.getHeight() == 64,
						"Must be 64 pixels high", new Object[0]);
				ImageIO.write(var4, "PNG", new ByteBufOutputStream(var3));
				final ByteBuf var5 = Base64.encode(var3);
				p_147138_1_.func_151320_a("data:image/png;base64,"
						+ var5.toString(Charsets.UTF_8));
			} catch (final Exception var9) {
				logger.error("Couldn\'t load server icon", var9);
			} finally {
				var3.release();
			}
		}
	}

	public void func_147139_a(EnumDifficulty p_147139_1_) {
		for (final WorldServer worldServer : worldServers) {
			final WorldServer var3 = worldServer;

			if (var3 != null) {
				if (var3.getWorldInfo().isHardcoreModeEnabled()) {
					var3.difficultySetting = EnumDifficulty.HARD;
					var3.setAllowedSpawnTypes(true, true);
				} else if (isSinglePlayer()) {
					var3.difficultySetting = p_147139_1_;
					var3.setAllowedSpawnTypes(
							var3.difficultySetting != EnumDifficulty.PEACEFUL,
							true);
				} else {
					var3.difficultySetting = p_147139_1_;
					var3.setAllowedSpawnTypes(allowSpawnMonsters(),
							canSpawnAnimals);
				}
			}
		}
	}

	public GameProfile[] func_152357_F() {
		return serverConfigManager.func_152600_g();
	}

	public PlayerProfileCache func_152358_ax() {
		return field_152366_X;
	}

	public GameProfileRepository func_152359_aw() {
		return field_152365_W;
	}

	public void func_152361_a(ServerConfigurationManager p_152361_1_) {
		serverConfigManager = p_152361_1_;
	}

	public abstract boolean func_152363_m();

	public ISaveFormat getActiveAnvilConverter() {
		return anvilConverterForAnvilFile;
	}

	public boolean getAllowNether() {
		return true;
	}

	/**
	 * Returns an array of the usernames of all the connected players.
	 */
	public String[] getAllUsernames() {
		return serverConfigManager.getAllUsernames();
	}

	public int getBuildLimit() {
		return buildLimit;
	}

	public boolean getCanSpawnAnimals() {
		return canSpawnAnimals;
	}

	public boolean getCanSpawnNPCs() {
		return canSpawnNPCs;
	}

	public ICommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return "Server";
	}

	public ServerConfigurationManager getConfigurationManager() {
		return serverConfigManager;
	}

	/**
	 * Returns the number of players currently on the server.
	 */
	public int getCurrentPlayerCount() {
		return serverConfigManager.getCurrentPlayerCount();
	}

	protected File getDataDirectory() {
		return new File(".");
	}

	@Override
	public World getEntityWorld() {
		return worldServers[0];
	}

	/**
	 * Returns a File object from the specified string.
	 */
	public File getFile(String p_71209_1_) {
		return new File(getDataDirectory(), p_71209_1_);
	}

	public String getFolderName() {
		return folderName;
	}

	public boolean getForceGamemode() {
		return isGamemodeForced;
	}

	public abstract WorldSettings.GameType getGameType();

	public boolean getGuiEnabled() {
		return false;
	}

	/**
	 * Gets KeyPair instanced in MinecraftServer.
	 */
	public KeyPair getKeyPair() {
		return serverKeyPair;
	}

	/**
	 * Returns the maximum number of players allowed on the server.
	 */
	public int getMaxPlayers() {
		return serverConfigManager.getMaxPlayers();
	}

	/**
	 * Returns the server's Minecraft version as string.
	 */
	public String getMinecraftVersion() {
		return "1.7.10";
	}

	public String getMOTD() {
		return motd;
	}

	/**
	 * Return the position for this command sender.
	 */
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(0, 0, 0);
	}

	public PlayerUsageSnooper getPlayerUsageSnooper() {
		return usageSnooper;
	}

	/**
	 * If par2Str begins with /, then it searches for commands, otherwise it
	 * returns players.
	 */
	public List getPossibleCompletions(ICommandSender p_71248_1_,
			String p_71248_2_) {
		final ArrayList var3 = new ArrayList();

		if (p_71248_2_.startsWith("/")) {
			p_71248_2_ = p_71248_2_.substring(1);
			final boolean var10 = !p_71248_2_.contains(" ");
			final List var11 = commandManager.getPossibleCommands(p_71248_1_,
					p_71248_2_);

			if (var11 != null) {
				final Iterator var12 = var11.iterator();

				while (var12.hasNext()) {
					final String var13 = (String) var12.next();

					if (var10) {
						var3.add("/" + var13);
					} else {
						var3.add(var13);
					}
				}
			}

			return var3;
		} else {
			final String[] var4 = p_71248_2_.split(" ", -1);
			final String var5 = var4[var4.length - 1];
			final String[] var6 = serverConfigManager.getAllUsernames();
			final int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				final String var9 = var6[var8];

				if (CommandBase.doesStringStartWith(var5, var9)) {
					var3.add(var9);
				}
			}

			return var3;
		}
	}

	public String getServerModName() {
		return "vanilla";
	}

	/**
	 * Returns the username of the server owner (for integrated servers)
	 */
	public String getServerOwner() {
		return serverOwner;
	}

	public Proxy getServerProxy() {
		return serverProxy;
	}

	/**
	 * Return the spawn protection area's size.
	 */
	public int getSpawnProtectionSize() {
		return 16;
	}

	public int getTickCounter() {
		return tickCounter;
	}

	public synchronized String getUserMessage() {
		return userMessage;
	}

	public String getWorldName() {
		return worldName;
	}

	protected void initialWorldChunkLoad() {
		int var5 = 0;
		setUserMessage("menu.generatingTerrain");
		final byte var6 = 0;
		logger.info("Preparing start region for level " + var6);
		final WorldServer var7 = worldServers[var6];
		final ChunkCoordinates var8 = var7.getSpawnPoint();
		long var9 = getSystemTimeMillis();

		for (int var11 = -192; var11 <= 192 && isServerRunning(); var11 += 16) {
			for (int var12 = -192; var12 <= 192 && isServerRunning(); var12 += 16) {
				final long var13 = getSystemTimeMillis();

				if (var13 - var9 > 1000L) {
					outputPercentRemaining("Preparing spawn area",
							var5 * 100 / 625);
					var9 = var13;
				}

				++var5;
				var7.theChunkProviderServer.loadChunk(var8.posX + var11 >> 4,
						var8.posZ + var12 >> 4);
			}
		}

		clearCurrentTask();
	}

	/**
	 * Sets the serverRunning variable to false, in order to get the server to
	 * shut down.
	 */
	public void initiateShutdown() {
		serverRunning = false;
	}

	/**
	 * Returns true if a player does not have permission to edit the block at
	 * the given coordinates.
	 */
	public boolean isBlockProtected(World p_96290_1_, int p_96290_2_,
			int p_96290_3_, int p_96290_4_, EntityPlayer p_96290_5_) {
		return false;
	}

	/**
	 * Return whether command blocks are enabled.
	 */
	public abstract boolean isCommandBlockEnabled();

	public abstract boolean isDedicatedServer();

	/**
	 * Gets whether this is a demo or not.
	 */
	public boolean isDemo() {
		return isDemo;
	}

	public boolean isFlightAllowed() {
		return allowFlight;
	}

	/**
	 * Defaults to false.
	 */
	public abstract boolean isHardcore();

	public boolean isPVPEnabled() {
		return pvpEnabled;
	}

	public boolean isServerInOnlineMode() {
		return onlineMode;
	}

	public boolean isServerRunning() {
		return serverRunning;
	}

	public boolean isSinglePlayer() {
		return serverOwner != null;
	}

	/**
	 * Returns whether snooping is enabled or not.
	 */
	@Override
	public boolean isSnooperEnabled() {
		return true;
	}

	protected void loadAllWorlds(String p_71247_1_, String p_71247_2_,
			long p_71247_3_, WorldType p_71247_5_, String p_71247_6_) {
		convertMapIfNeeded(p_71247_1_);
		setUserMessage("menu.loadingLevel");
		worldServers = new WorldServer[3];
		timeOfLastDimensionTick = new long[worldServers.length][100];
		final ISaveHandler var7 = anvilConverterForAnvilFile.getSaveLoader(
				p_71247_1_, true);
		final WorldInfo var9 = var7.loadWorldInfo();
		WorldSettings var8;

		if (var9 == null) {
			var8 = new WorldSettings(p_71247_3_, getGameType(),
					canStructuresSpawn(), isHardcore(), p_71247_5_);
			var8.func_82750_a(p_71247_6_);
		} else {
			var8 = new WorldSettings(var9);
		}

		if (enableBonusChest) {
			var8.enableBonusChest();
		}

		for (int var10 = 0; var10 < worldServers.length; ++var10) {
			byte var11 = 0;

			if (var10 == 1) {
				var11 = -1;
			}

			if (var10 == 2) {
				var11 = 1;
			}

			if (var10 == 0) {
				if (isDemo()) {
					worldServers[var10] = new DemoWorldServer(this, var7,
							p_71247_2_, var11, theProfiler);
				} else {
					worldServers[var10] = new WorldServer(this, var7,
							p_71247_2_, var11, var8, theProfiler);
				}
			} else {
				worldServers[var10] = new WorldServerMulti(this, var7,
						p_71247_2_, var11, var8, worldServers[0], theProfiler);
			}

			worldServers[var10].addWorldAccess(new WorldManager(this,
					worldServers[var10]));

			if (!isSinglePlayer()) {
				worldServers[var10].getWorldInfo().setGameType(getGameType());
			}

			serverConfigManager.setPlayerManager(worldServers);
		}

		func_147139_a(func_147135_j());
		initialWorldChunkLoad();
	}

	/**
	 * Logs the message with a level of WARN.
	 */
	public void logWarning(String p_71236_1_) {
		logger.warn(p_71236_1_);
	}

	/**
	 * Used to display a percent remaining given text and the percentage.
	 */
	protected void outputPercentRemaining(String p_71216_1_, int p_71216_2_) {
		currentTask = p_71216_1_;
		percentDone = p_71216_2_;
		logger.info(p_71216_1_ + ": " + p_71216_2_ + "%");
	}

	@Override
	public void run() {
		try {
			if (startServer()) {
				long var1 = getSystemTimeMillis();
				long var50 = 0L;
				field_147147_p.func_151315_a(new ChatComponentText(motd));
				field_147147_p
						.func_151321_a(new ServerStatusResponse.MinecraftProtocolVersionIdentifier(
								"1.7.10", 5));
				func_147138_a(field_147147_p);

				while (serverRunning) {
					final long var5 = getSystemTimeMillis();
					long var7 = var5 - var1;

					if (var7 > 2000L && var1 - timeOfLastWarning >= 15000L) {
						logger.warn(
								"Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)",
								new Object[] { Long.valueOf(var7),
										Long.valueOf(var7 / 50L) });
						var7 = 2000L;
						timeOfLastWarning = var1;
					}

					if (var7 < 0L) {
						logger.warn("Time ran backwards! Did the system time change?");
						var7 = 0L;
					}

					var50 += var7;
					var1 = var5;

					if (worldServers[0].areAllPlayersAsleep()) {
						tick();
						var50 = 0L;
					} else {
						while (var50 > 50L) {
							var50 -= 50L;
							tick();
						}
					}

					Thread.sleep(Math.max(1L, 50L - var50));
					serverIsRunning = true;
				}
			} else {
				finalTick((CrashReport) null);
			}
		} catch (final Throwable var48) {
			logger.error("Encountered an unexpected exception", var48);
			CrashReport var2 = null;

			if (var48 instanceof ReportedException) {
				var2 = addServerInfoToCrashReport(((ReportedException) var48)
						.getCrashReport());
			} else {
				var2 = addServerInfoToCrashReport(new CrashReport(
						"Exception in server tick loop", var48));
			}

			final File var3 = new File(new File(getDataDirectory(),
					"crash-reports"),
					"crash-"
							+ new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")
									.format(new Date()) + "-server.txt");

			if (var2.saveToFile(var3)) {
				logger.error("This crash report has been saved to: "
						+ var3.getAbsolutePath());
			} else {
				logger.error("We were unable to save this crash report to disk.");
			}

			finalTick(var2);
		} finally {
			try {
				stopServer();
			} catch (final Throwable var46) {
				logger.error("Exception stopping the server", var46);
			} finally {
				systemExitNow();
			}
		}
	}

	/**
	 * par1 indicates if a log message should be output.
	 */
	protected void saveAllWorlds(boolean p_71267_1_) {
		if (!worldIsBeingDeleted) {
			final WorldServer[] var2 = worldServers;
			final int var3 = var2.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				final WorldServer var5 = var2[var4];

				if (var5 != null) {
					if (!p_71267_1_) {
						logger.info("Saving chunks for level \'"
								+ var5.getWorldInfo().getWorldName() + "\'/"
								+ var5.provider.getDimensionName());
					}

					try {
						var5.saveAllChunks(true, (IProgressUpdate) null);
					} catch (final MinecraftException var7) {
						logger.warn(var7.getMessage());
					}
				}
			}
		}
	}

	public boolean serverIsInRunLoop() {
		return serverIsRunning;
	}

	public void setAllowFlight(boolean p_71245_1_) {
		allowFlight = p_71245_1_;
	}

	public void setAllowPvp(boolean p_71188_1_) {
		pvpEnabled = p_71188_1_;
	}

	public void setBuildLimit(int p_71191_1_) {
		buildLimit = p_71191_1_;
	}

	public void setCanSpawnAnimals(boolean p_71251_1_) {
		canSpawnAnimals = p_71251_1_;
	}

	public void setCanSpawnNPCs(boolean p_71257_1_) {
		canSpawnNPCs = p_71257_1_;
	}

	/**
	 * Sets whether this is a demo or not.
	 */
	public void setDemo(boolean p_71204_1_) {
		isDemo = p_71204_1_;
	}

	public void setFolderName(String p_71261_1_) {
		folderName = p_71261_1_;
	}

	/**
	 * Sets the game type for all worlds.
	 */
	public void setGameType(WorldSettings.GameType p_71235_1_) {
		for (int var2 = 0; var2 < worldServers.length; ++var2) {
			getServer().worldServers[var2].getWorldInfo().setGameType(
					p_71235_1_);
		}
	}

	public void setKeyPair(KeyPair p_71253_1_) {
		serverKeyPair = p_71253_1_;
	}

	public void setMOTD(String p_71205_1_) {
		motd = p_71205_1_;
	}

	public void setOnlineMode(boolean p_71229_1_) {
		onlineMode = p_71229_1_;
	}

	/**
	 * Sets the username of the owner of this server (in the case of an
	 * integrated server)
	 */
	public void setServerOwner(String p_71224_1_) {
		serverOwner = p_71224_1_;
	}

	/**
	 * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
	 */
	protected synchronized void setUserMessage(String p_71192_1_) {
		userMessage = p_71192_1_;
	}

	public void setWorldName(String p_71246_1_) {
		worldName = p_71246_1_;
	}

	/**
	 * On dedicated does nothing. On integrated, sets commandsAllowedForAll,
	 * gameType and allows external connections.
	 */
	public abstract String shareToLAN(WorldSettings.GameType p_71206_1_,
			boolean p_71206_2_);

	/**
	 * Initialises the server and starts it.
	 */
	protected abstract boolean startServer() throws IOException;

	public void startServerThread() {
		new Thread("Server thread") {

			@Override
			public void run() {
				MinecraftServer.this.run();
			}
		}.start();
	}

	/**
	 * Saves all necessary data as preparation for stopping the server.
	 */
	public void stopServer() {
		if (!worldIsBeingDeleted) {
			logger.info("Stopping server");

			if (func_147137_ag() != null) {
				func_147137_ag().terminateEndpoints();
			}

			if (serverConfigManager != null) {
				logger.info("Saving players");
				serverConfigManager.saveAllPlayerData();
				serverConfigManager.removeAllPlayers();
			}

			if (worldServers != null) {
				logger.info("Saving worlds");
				saveAllWorlds(false);

				for (final WorldServer var2 : worldServers) {
					var2.flush();
				}
			}

			if (usageSnooper.isSnooperRunning()) {
				usageSnooper.stopSnooper();
			}
		}
	}

	/**
	 * Directly calls System.exit(0), instantly killing the program.
	 */
	protected void systemExitNow() {
	}

	/**
	 * Main function called by run() every loop.
	 */
	public void tick() {
		final long var1 = System.nanoTime();
		++tickCounter;

		if (startProfiling) {
			startProfiling = false;
			theProfiler.profilingEnabled = true;
			theProfiler.clearProfiling();
		}

		theProfiler.startSection("root");
		updateTimeLightAndEntities();

		if (var1 - field_147142_T >= 5000000000L) {
			field_147142_T = var1;
			field_147147_p
					.func_151319_a(new ServerStatusResponse.PlayerCountData(
							getMaxPlayers(), getCurrentPlayerCount()));
			final GameProfile[] var3 = new GameProfile[Math.min(
					getCurrentPlayerCount(), 12)];
			final int var4 = MathHelper.getRandomIntegerInRange(field_147146_q,
					0, getCurrentPlayerCount() - var3.length);

			for (int var5 = 0; var5 < var3.length; ++var5) {
				var3[var5] = ((EntityPlayerMP) serverConfigManager.playerEntityList
						.get(var4 + var5)).getGameProfile();
			}

			Collections.shuffle(Arrays.asList(var3));
			field_147147_p.func_151318_b().func_151330_a(var3);
		}

		if (tickCounter % 900 == 0) {
			theProfiler.startSection("save");
			serverConfigManager.saveAllPlayerData();
			saveAllWorlds(true);
			theProfiler.endSection();
		}

		theProfiler.startSection("tallying");
		tickTimeArray[tickCounter % 100] = System.nanoTime() - var1;
		theProfiler.endSection();
		theProfiler.startSection("snooper");

		if (!usageSnooper.isSnooperRunning() && tickCounter > 100) {
			usageSnooper.startSnooper();
		}

		if (tickCounter % 6000 == 0) {
			usageSnooper.addMemoryStatsToSnooper();
		}

		theProfiler.endSection();
		theProfiler.endSection();
	}

	public void updateTimeLightAndEntities() {
		theProfiler.startSection("levels");
		int var1;

		for (var1 = 0; var1 < worldServers.length; ++var1) {
			final long var2 = System.nanoTime();

			if (var1 == 0 || getAllowNether()) {
				final WorldServer var4 = worldServers[var1];
				theProfiler.startSection(var4.getWorldInfo().getWorldName());
				theProfiler.startSection("pools");
				theProfiler.endSection();

				if (tickCounter % 20 == 0) {
					theProfiler.startSection("timeSync");
					serverConfigManager.func_148537_a(
							new S03PacketTimeUpdate(var4.getTotalWorldTime(),
									var4.getWorldTime(), var4.getGameRules()
											.getGameRuleBooleanValue(
													"doDaylightCycle")),
							var4.provider.dimensionId);
					theProfiler.endSection();
				}

				theProfiler.startSection("tick");
				CrashReport var6;

				try {
					var4.tick();
				} catch (final Throwable var8) {
					var6 = CrashReport.makeCrashReport(var8,
							"Exception ticking world");
					var4.addWorldInfoToCrashReport(var6);
					throw new ReportedException(var6);
				}

				try {
					var4.updateEntities();
				} catch (final Throwable var7) {
					var6 = CrashReport.makeCrashReport(var7,
							"Exception ticking world entities");
					var4.addWorldInfoToCrashReport(var6);
					throw new ReportedException(var6);
				}

				theProfiler.endSection();
				theProfiler.startSection("tracker");
				var4.getEntityTracker().updateTrackedEntities();
				theProfiler.endSection();
				theProfiler.endSection();
			}

			timeOfLastDimensionTick[var1][tickCounter % 100] = System
					.nanoTime() - var2;
		}

		theProfiler.endStartSection("connection");
		func_147137_ag().networkTick();
		theProfiler.endStartSection("players");
		serverConfigManager.sendPlayerInfoToAllPlayers();
		theProfiler.endStartSection("tickables");

		for (var1 = 0; var1 < tickables.size(); ++var1) {
			((IUpdatePlayerListBox) tickables.get(var1)).update();
		}

		theProfiler.endSection();
	}

	/**
	 * Gets the worldServer by the given dimension.
	 */
	public WorldServer worldServerForDimension(int p_71218_1_) {
		return p_71218_1_ == -1 ? worldServers[1]
				: p_71218_1_ == 1 ? worldServers[2] : worldServers[0];
	}
}
