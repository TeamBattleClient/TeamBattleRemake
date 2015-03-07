package down.TeamBattle.Modules;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.Listener;

public abstract class ModuleBase implements Listener {
	protected static Minecraft mc = Minecraft.getMinecraft();
	protected boolean enabled, visible;
	protected int keybind, color;
	protected final String name;
	protected String tag;
	
	public ModuleBase(String name) {
		this(name, 0, -1);
		visible = false;
	}

	public ModuleBase(String name, int color) {
		this(name, 0, color);
	}
	public double X()
	{
		return mc.thePlayer.posX;
	}
	public double Y()
	{
		return mc.thePlayer.posY;
	}
		public double Z()
		{
			return mc.thePlayer.posZ;
		}
		public double YY()
		{
			return mc.thePlayer.boundingBox.minY;
		}

	public ModuleBase(String name, int keybind, int color) {
		this.name = name;
		tag = name;
		this.keybind = keybind;
		this.color = color;
		enabled = false;
		visible = true;
	}

	public ModuleBase(String name, String keybind) {
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
		TeamBattleClient.getEventManager().removeListener(this);
	}

	public void onEnabled() {
		TeamBattleClient.getEventManager().addListener(this);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (TeamBattleClient.getFileManager().getFileByName("modconfig") != null) {
			TeamBattleClient.getFileManager().getFileByName("modconfig").saveFile();
		}

		if (this.enabled) {
			onEnabled();
		} else {
			onDisabled();
		}
	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
		if (TeamBattleClient.getFileManager().getFileByName("modconfig") != null) {
			TeamBattleClient.getFileManager().getFileByName("modconfig").saveFile();
		}
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void toggle() {
		enabled = !enabled;
		if (TeamBattleClient.getFileManager().getFileByName("modconfig") != null) {
			TeamBattleClient.getFileManager().getFileByName("modconfig").saveFile();
		}

		if (enabled) {
			onEnabled();
		} else {
			onDisabled();
		}
	}
}
