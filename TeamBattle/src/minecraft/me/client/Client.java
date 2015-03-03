package me.client;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import me.client.helpers.MinecraftHelper;
import me.client.modules.ModuleManager;
import me.client.utils.Logger;

public class Client implements MinecraftHelper{

	public String Client_Name = "ClientName";
	public String Client_Version = "0.1";
	public int buildNumber = 1;
	public boolean isInDev = true;
	
	/**
	 * Clients instance so that we can access this file in different classes
	 */
	public static Client instance = new Client();
	
	
	private Logger logger;
	private ModuleManager moduleManager;
	
	/**
	 * Starts the client and initiates everything we need!
	 */
	public void startClient() {
		try {
			this.getLogger().log("Starting up the client!", Logger.LogType.NORMAL);
			this.getModuleManager();
			
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
	
	public ModuleManager getModuleManager() {
		if(this.moduleManager == null)
			this.moduleManager = new ModuleManager();
		
		return this.moduleManager;
	}
	
	
	
}