package me.client;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import me.client.Managers.AdminManager;
import me.client.Managers.AltManager;
import me.client.Managers.CommandManager;
import me.client.Managers.EventManager;
import me.client.Managers.FriendManager;
import me.client.Managers.ModuleManager;
import me.client.Managers.ValueManager;
import me.client.Threads.UpdateCheckThread;
import me.client.command.Command;
import me.client.modules.Module;
import me.client.utils.Logger;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.Display;


public final class Client {
	private static final me.client.Managers.FileManager fileManager = new me.client.Managers.FileManager();
	private static final AdminManager adminManager = new AdminManager();
	private static final AltManager altManager = new AltManager();
	private static final int build = 1;
	private static final CommandManager commandManager = new CommandManager();
	private static final File directory = new File(
			Minecraft.getMinecraft().mcDataDir, "TeamBattleRemake");
	private static final EventManager eventManager = new EventManager();
	private static final FriendManager friendManager = new FriendManager();
	private static final ModuleManager modManager = new ModuleManager();
	private static boolean newerVersionAvailable;
	private static final ValueManager valueManager = new ValueManager();

	

	public static AdminManager getAdminManager() {
		return adminManager;
	}

	public static AltManager getAltManager() {
		return altManager;
	}

	public static int getBuild() {
		return build;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}  

	public static File getDirectory() {
		return directory;
	} 

	public static EventManager getEventManager() {
		return eventManager;
	}

	public static me.client.Managers.FileManager getFileManager() {
		return fileManager;
	}

	public static FriendManager getFriendManager() {
		return friendManager;
	}

	public static ModuleManager getModManager() {
		return modManager;
	}

	public static ValueManager getValueManager() {
		return valueManager;
	}

	public static boolean isNewerVersionAvailable() {
		return newerVersionAvailable;
	}

	public static void onStartup() {
		Display.setTitle("TeamBattleRemake");
		Logger.logConsole("Started loading BaLen");
		Logger.logConsole("Build # " + build);
		new UpdateCheckThread().start();
		if (!directory.isDirectory()) {
			directory.mkdirs();
		}
		eventManager.setup();
		valueManager.setup();
		friendManager.setup();
		adminManager.setup();
	    commandManager.setup();
		modManager.setup();
	    altManager.setup();
		fileManager.setup();
	
		Collections.sort(commandManager.getContents(),
				new Comparator<Command>() {
					@Override
					public int compare(Command cmd1, Command cmd2) {
						return cmd1.getCommand().compareTo(cmd2.getCommand());
					}
				});

		// then sort mods so they are alphabetical
		Collections.sort(modManager.getContents(), new Comparator<Module>() {
			@Override
			public int compare(Module mod1, Module mod2) {
				return mod1.getName().compareTo(mod2.getName());
			}
		});
		
	}

	//public static void setAccount(Account account) {
	//	LateMod.account = account;
	//}

	//public static void setNewerVersionAvailable(boolean newerVersionAvailable) {
	//	LateMod.newerVersionAvailable = newerVersionAvailable;
	//}
}

/*
 * Nice memes
 * 
 * if ((event instanceof EventPostUpdate)) { if (isInLiquid()) {
 * Wrapper.getPlayer().motionY = 0.12D; return; } if
 * (isOnLiquid(Wrapper.getPlayer())) { Wrapper.getPlayer().noClip = true;
 * 
 * 
 * long currentMS = System.nanoTime() / 1000000L;
 * Minecraft.getMinecraft().thePlayer.onGround = false; if ((currentMS -
 * this.lastMS >= 250L) || (this.lastMS == -1L)) {
 * Wrapper.getPlayer().boundingBox.expand(0.0D, 2.0D, 0.0D); this.lastMS =
 * currentMS; } } else { Wrapper.getPlayer().noClip = false; } }
 */