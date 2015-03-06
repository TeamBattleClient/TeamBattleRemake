package net.minecraft.realms;

import java.lang.reflect.Constructor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsBridge extends RealmsScreen {
	private static final Logger LOGGER = LogManager.getLogger();
	private GuiScreen previousScreen;

	@Override
	public void init() {
		Minecraft.getMinecraft().displayGuiScreen(previousScreen);
	}

	public void switchToRealms(GuiScreen p_switchToRealms_1_) {
		previousScreen = p_switchToRealms_1_;

		try {
			final Class var2 = Class
					.forName("com.mojang.realmsclient.RealmsMainScreen");
			final Constructor var3 = var2
					.getDeclaredConstructor(new Class[] { RealmsScreen.class });
			var3.setAccessible(true);
			final Object var4 = var3.newInstance(new Object[] { this });
			Minecraft.getMinecraft().displayGuiScreen(
					((RealmsScreen) var4).getProxy());
		} catch (final Exception var5) {
			LOGGER.error("Realms module missing", var5);
		}
	}
}
