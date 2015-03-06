package net.minecraft.server.integrated;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Reflector;
import net.minecraft.src.WorldServerMultiOF;
import net.minecraft.src.WorldServerOF;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer {
	private static final Logger logger = LogManager.getLogger();

	private boolean isGamePaused;
	private boolean isPublic;
	private ThreadLanServerPing lanServerPing;
	/** The Minecraft instance. */
	private final Minecraft mc;
	private final WorldSettings theWorldSettings;

	public IntegratedServer(Minecraft par1Minecraft, String par2Str,
			String par3Str, WorldSettings par4WorldSettings) {
		super(new File(par1Minecraft.mcDataDir, "saves"), par1Minecraft
				.getProxy());
		setServerOwner(par1Minecraft.getSession().getUsername());
		setFolderName(par2Str);
		setWorldName(par3Str);
		setDemo(par1Minecraft.isDemo());
		canCreateBonusChest(par4WorldSettings.isBonusChestEnabled());
		setBuildLimit(256);
		func_152361_a(new IntegratedPlayerList(this));
		mc = par1Minecraft;
		theWorldSettings = par4WorldSettings;
		Reflector.callVoid(Reflector.ModLoader_registerServer,
				new Object[] { this });
	}

	/**
	 * Adds the server info, including from theWorldServer, to the crash report.
	 */
	@Override
	public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport) {
		par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
		par1CrashReport.getCategory().addCrashSectionCallable("Type",
				new Callable() {

					@Override
					public String call() {
						return "Integrated Server (map_client.txt)";
					}
				});
		par1CrashReport.getCategory().addCrashSectionCallable("Is Modded",
				new Callable() {

					@Override
					public String call() {
						String var1 = ClientBrandRetriever.getClientModName();

						if (!var1.equals("vanilla"))
							return "Definitely; Client brand changed to \'"
									+ var1 + "\'";
						else {
							var1 = IntegratedServer.this.getServerModName();
							return !var1.equals("vanilla") ? "Definitely; Server brand changed to \'"
									+ var1 + "\'"
									: Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated"
											: "Probably not. Jar signature remains and both client + server brands are untouched.";
						}
					}
				});
		return par1CrashReport;
	}

	@Override
	public void addServerStatsToSnooper(
			PlayerUsageSnooper par1PlayerUsageSnooper) {
		super.addServerStatsToSnooper(par1PlayerUsageSnooper);
		par1PlayerUsageSnooper.func_152768_a("snooper_partner", mc
				.getPlayerUsageSnooper().getUniqueID());
	}

	@Override
	public boolean canStructuresSpawn() {
		return false;
	}

	/**
	 * Called on exit from the main run() loop.
	 */
	@Override
	protected void finalTick(CrashReport par1CrashReport) {
		mc.crashed(par1CrashReport);
	}

	@Override
	public int func_110455_j() {
		return 4;
	}

	@Override
	public EnumDifficulty func_147135_j() {
		return mc.gameSettings.difficulty;
	}

	@Override
	public boolean func_152363_m() {
		return false;
	}

	@Override
	protected File getDataDirectory() {
		return mc.mcDataDir;
	}

	@Override
	public WorldSettings.GameType getGameType() {
		return theWorldSettings.getGameType();
	}

	/**
	 * Returns true if this integrated server is open to LAN
	 */
	public boolean getPublic() {
		return isPublic;
	}

	/**
	 * Sets the serverRunning variable to false, in order to get the server to
	 * shut down.
	 */
	@Override
	public void initiateShutdown() {
		super.initiateShutdown();

		if (lanServerPing != null) {
			lanServerPing.interrupt();
			lanServerPing = null;
		}
	}

	/**
	 * Return whether command blocks are enabled.
	 */
	@Override
	public boolean isCommandBlockEnabled() {
		return true;
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	/**
	 * Defaults to false.
	 */
	@Override
	public boolean isHardcore() {
		return theWorldSettings.getHardcoreEnabled();
	}

	/**
	 * Returns whether snooping is enabled or not.
	 */
	@Override
	public boolean isSnooperEnabled() {
		return Minecraft.getMinecraft().isSnooperEnabled();
	}

	@Override
	protected void loadAllWorlds(String par1Str, String par2Str, long par3,
			WorldType par5WorldType, String par6Str) {
		convertMapIfNeeded(par1Str);
		final ISaveHandler var7 = getActiveAnvilConverter().getSaveLoader(
				par1Str, true);

		if (Reflector.DimensionManager.exists()) {
			final Object var8 = isDemo() ? new DemoWorldServer(this, var7,
					par2Str, 0, theProfiler) : new WorldServerOF(this, var7,
					par2Str, 0, theWorldSettings, theProfiler);
			final Integer[] var9 = (Integer[]) Reflector.call(
					Reflector.DimensionManager_getStaticDimensionIDs,
					new Object[0]);
			final Integer[] arr$ = var9;
			final int len$ = var9.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				final int dim = arr$[i$].intValue();
				final Object world = dim == 0 ? var8 : new WorldServerMultiOF(
						this, var7, par2Str, dim, theWorldSettings,
						(WorldServer) var8, theProfiler);
				((WorldServer) world).addWorldAccess(new WorldManager(this,
						(WorldServer) world));

				if (!isSinglePlayer()) {
					((WorldServer) world).getWorldInfo().setGameType(
							getGameType());
				}

				if (Reflector.EventBus.exists()) {
					Reflector.postForgeBusEvent(
							Reflector.WorldEvent_Load_Constructor,
							new Object[] { world });
				}
			}

			getConfigurationManager().setPlayerManager(
					new WorldServer[] { (WorldServer) var8 });
		} else {
			worldServers = new WorldServer[3];
			timeOfLastDimensionTick = new long[worldServers.length][100];

			for (int var15 = 0; var15 < worldServers.length; ++var15) {
				byte var16 = 0;

				if (var15 == 1) {
					var16 = -1;
				}

				if (var15 == 2) {
					var16 = 1;
				}

				if (var15 == 0) {
					if (isDemo()) {
						worldServers[var15] = new DemoWorldServer(this, var7,
								par2Str, var16, theProfiler);
					} else {
						worldServers[var15] = new WorldServerOF(this, var7,
								par2Str, var16, theWorldSettings, theProfiler);
					}
				} else {
					worldServers[var15] = new WorldServerMultiOF(this, var7,
							par2Str, var16, theWorldSettings, worldServers[0],
							theProfiler);
				}

				worldServers[var15].addWorldAccess(new WorldManager(this,
						worldServers[var15]));
				getConfigurationManager().setPlayerManager(worldServers);
			}
		}

		func_147139_a(func_147135_j());
		initialWorldChunkLoad();
	}

	/**
	 * Sets the game type for all worlds.
	 */
	@Override
	public void setGameType(WorldSettings.GameType par1EnumGameType) {
		getConfigurationManager().func_152604_a(par1EnumGameType);
	}

	/**
	 * On dedicated does nothing. On integrated, sets commandsAllowedForAll,
	 * gameType and allows external connections.
	 */
	@Override
	public String shareToLAN(WorldSettings.GameType par1EnumGameType,
			boolean par2) {
		try {
			int var6 = -1;

			try {
				var6 = HttpUtil.func_76181_a();
			} catch (final IOException var5) {
				;
			}

			if (var6 <= 0) {
				var6 = 25564;
			}

			func_147137_ag().addLanEndpoint((InetAddress) null, var6);
			logger.info("Started on " + var6);
			isPublic = true;
			lanServerPing = new ThreadLanServerPing(getMOTD(), var6 + "");
			lanServerPing.start();
			getConfigurationManager().func_152604_a(par1EnumGameType);
			getConfigurationManager().setCommandsAllowedForAll(par2);
			return var6 + "";
		} catch (final IOException var61) {
			return null;
		}
	}

	/**
	 * Initialises the server and starts it.
	 */
	@Override
	protected boolean startServer() throws IOException {
		logger.info("Starting integrated minecraft server version 1.7.10");
		setOnlineMode(true);
		setCanSpawnAnimals(true);
		setCanSpawnNPCs(true);
		setAllowPvp(true);
		setAllowFlight(true);
		logger.info("Generating keypair");
		setKeyPair(CryptManager.createNewKeyPair());
		Object inst;

		if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
			inst = Reflector.call(Reflector.FMLCommonHandler_instance,
					new Object[0]);

			if (!Reflector.callBoolean(inst,
					Reflector.FMLCommonHandler_handleServerAboutToStart,
					new Object[] { this }))
				return false;
		}

		loadAllWorlds(getFolderName(), getWorldName(),
				theWorldSettings.getSeed(), theWorldSettings.getTerrainType(),
				theWorldSettings.func_82749_j());
		setMOTD(getServerOwner() + " - "
				+ worldServers[0].getWorldInfo().getWorldName());

		if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
			inst = Reflector.call(Reflector.FMLCommonHandler_instance,
					new Object[0]);

			if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE)
				return Reflector.callBoolean(inst,
						Reflector.FMLCommonHandler_handleServerStarting,
						new Object[] { this });

			Reflector.callVoid(inst,
					Reflector.FMLCommonHandler_handleServerStarting,
					new Object[] { this });
		}

		return true;
	}

	/**
	 * Saves all necessary data as preparation for stopping the server.
	 */
	@Override
	public void stopServer() {
		super.stopServer();

		if (lanServerPing != null) {
			lanServerPing.interrupt();
			lanServerPing = null;
		}
	}

	/**
	 * Main function called by run() every loop.
	 */
	@Override
	public void tick() {
		final boolean var1 = isGamePaused;
		isGamePaused = Minecraft.getMinecraft().getNetHandler() != null
				&& Minecraft.getMinecraft().func_147113_T();

		if (!var1 && isGamePaused) {
			logger.info("Saving and pausing game...");
			getConfigurationManager().saveAllPlayerData();
			saveAllWorlds(false);
		}

		if (!isGamePaused) {
			super.tick();

			if (mc.gameSettings.renderDistanceChunks != getConfigurationManager()
					.getViewDistance()) {
				logger.info(
						"Changing view distance to {}, from {}",
						new Object[] {
								Integer.valueOf(mc.gameSettings.renderDistanceChunks),
								Integer.valueOf(getConfigurationManager()
										.getViewDistance()) });
				getConfigurationManager().func_152611_a(
						mc.gameSettings.renderDistanceChunks);
			}
		}
	}
}
