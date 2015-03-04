package me.client;

import java.awt.Font;

import me.client.font.CFontRenderer;
import me.client.helpers.MinecraftHelper;
import me.client.modules.ModuleManager;
import me.client.utils.Logger;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Client implements MinecraftHelper{

	public String Client_Name = "ClientName";
	public String Client_Version = "0.1";
	public int buildNumber = 1;
	public boolean isInDev = true;
	
	/**
	 * Clients instance so that we can access this file in different classes
	 */
	public static Client instance = new Client();
	
	/**
	 * Fonts
	 */
	public CFontRenderer tahoma = new CFontRenderer(new Font("Tahoma", Font.TRUETYPE_FONT, 18), true, 8);
	
	
	private Logger logger;
	private ModuleManager moduleManager;
	
	/**
	 * Starts the client and initiates everything we need!
	 */
	public void startClient() {
		try {
			this.getLogger().log("Starting up the client!", Logger.LogType.NORMAL);
			this.moduleManager = new ModuleManager();
			
			this.getLogger().log("Setting resolution to: 1280x720", Logger.LogType.NORMAL);
			mc.resize(1280, 720);
			Display.setDisplayMode(new DisplayMode(1280, 720));
		}catch(Exception e) {
			
		}
	}
	
	/**
	 * Getters / Setters
	 */
	public Logger getLogger() {
		if(this.logger == null)
			this.logger = new Logger();
		
		return logger;
	}

	@Override
	public boolean isCancelled() {
		
		return false;
	}

	@Override
	public void setCancelled(boolean cancel) {}
	
	
	
}
