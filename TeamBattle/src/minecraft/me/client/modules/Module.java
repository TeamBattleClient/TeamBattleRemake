package me.client.modules;

import me.client.Client;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import event.Listener;

public abstract class Module implements Listener {
	protected static Minecraft mc = Minecraft.getMinecraft();
	protected boolean enabled, visible;
	protected int keybind, color;
	protected final String name;
	protected String tag;
	
	public Module(String name) {
		this(name, 0, -1);
		visible = false;
	}

	public Module(String name, int color) {
		this(name, 0, color);
	}

	public Module(String name, int keybind, int color) {
		this.name = name;
		tag = name;
		this.keybind = keybind;
		this.color = color;
		enabled = false;
		visible = true;
	}

	public Module(String name, String keybind) {
		this(name, Keyboard.getKeyIndex(keybind), -1);
		visible = false;
	}

	public int getColor() {
		return color;
	}

	public int getKeybind() {
		return keybind;
	}

	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isVisible() {
		return visible;
	}

	public void onDisabled() {
		Client.getEventManager().removeListener(this);
	}

	public void onEnabled() {
		Client.getEventManager().addListener(this);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
//deleted filemanager
		if (this.enabled) {
			onEnabled();
		} else {
			onDisabled();
		}
	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
		//deleted filemanager
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void toggle() {
		enabled = !enabled;
		//deleted filemanager 

		if (enabled) {
			onEnabled();
		} else {
			onDisabled();
		}
	}
}
