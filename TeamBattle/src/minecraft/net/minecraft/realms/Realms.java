package net.minecraft.realms;

import java.net.Proxy;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

import com.mojang.authlib.GameProfile;

public class Realms {

	public static int adventureId() {
		return WorldSettings.GameType.ADVENTURE.getID();
	}

	public static int creativeId() {
		return WorldSettings.GameType.CREATIVE.getID();
	}

	public static long currentTimeMillis() {
		return Minecraft.getSystemTime();
	}

	public static String getGameDirectoryPath() {
		return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
	}

	public static String getName() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static Proxy getProxy() {
		return Minecraft.getMinecraft().getProxy();
	}

	public static String getSessionId() {
		return Minecraft.getMinecraft().getSession().getSessionID();
	}

	public static boolean isTouchScreen() {
		return Minecraft.getMinecraft().gameSettings.touchscreen;
	}

	public static String sessionId() {
		final Session var0 = Minecraft.getMinecraft().getSession();
		return var0 == null ? null : var0.getSessionID();
	}

	public static void setScreen(RealmsScreen p_setScreen_0_) {
		Minecraft.getMinecraft().displayGuiScreen(p_setScreen_0_.getProxy());
	}

	public static int survivalId() {
		return WorldSettings.GameType.SURVIVAL.getID();
	}

	public static String userName() {
		final Session var0 = Minecraft.getMinecraft().getSession();
		return var0 == null ? null : var0.getUsername();
	}

	public static String uuidToName(String p_uuidToName_0_) {
		return Minecraft
				.getMinecraft()
				.func_152347_ac()
				.fillProfileProperties(
						new GameProfile(
								UUID.fromString(p_uuidToName_0_
										.replaceAll(
												"(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
												"$1-$2-$3-$4-$5")),
								(String) null), false).getName();
	}
}
