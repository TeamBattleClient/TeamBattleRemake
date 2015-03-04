package me.client.modules;

import me.client.Client;
import me.client.helpers.MinecraftHelper;
import me.client.utils.Logger;

import com.darkmagician6.eventapi.EventManager;

public class Module implements MinecraftHelper{

	private String modName;
	private String modListName;
	private String modDesc;
	private int modKey = 0;
	private ModuleCategory modCategory;
	private boolean enabled;
	
	public Module(String modName, String listName, ModuleCategory category) {
		this.modName = modName;
		this.modListName = listName;
		this.modCategory = category;
		if(modDesc == null)
			modDesc = "No description available.";
		Client.instance.getLogger().log("Setting up mod: " + this.modName, Logger.LogType.NORMAL);
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onToggle() {}
	
	public void setEnabled(boolean state) {
		this.enabled = state;
		
		if(state) {
			this.onEnable();
			EventManager.register(this);
		}else {
			this.onDisable();
			EventManager.unregister(this);
		}
	}
	
	public void toggle() {
		this.setEnabled(!enabled);
		this.onToggle();
	}
	
	/**
	 * Getters
	 */
	public String getName() {
		return modName;
	}
	
	public String getListName() {
		return this.modListName;
	}
	
	public String getDesc() {
		return this.modDesc;
	}
	
	public int getKeybind() {
		return this.modKey;
	}
	
	public ModuleCategory getCategory() {
		return this.modCategory;
	}
	
	/**
	 * Setters
	 */
	public void setListName(String newName) {
		this.modListName = newName;
	}
	
	public void setDescription(String newDesc) {
		this.modDesc = newDesc;
	}
	
	public void setKeybind(int newKey) {
		this.modKey = newKey;
	}
}
