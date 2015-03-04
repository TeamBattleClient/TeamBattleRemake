package me.client.modules;

import me.client.Client;
import me.client.helpers.MinecraftHelper;
import me.client.utils.Logger;

import com.darkmagician6.eventapi.EventManager;

public class Module implements MinecraftHelper{

	private String modName;
	private String modListName;
	private String modDesc;
	private int modKey;
	private ModuleCategory modCategory;
	private boolean enabled;
	
	public Module(String modName, String listName, ModuleCategory category) {
		this.modName = modName;
		this.modListName = listName;
		this.modCategory = category;
		Client.instance.getLogger().log("Setting up mod: " + this.modName, Logger.LogType.NORMAL);
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onToggle() {}
	
	public void setEnabled(boolean state) {
		this.enabled = state;
		
		if(this.isEnabled()) {
			this.onEnable();
		}else {
			this.onDisable();
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
	
	public boolean isEnabled() {
		return enabled;
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

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub
		
	}
}
