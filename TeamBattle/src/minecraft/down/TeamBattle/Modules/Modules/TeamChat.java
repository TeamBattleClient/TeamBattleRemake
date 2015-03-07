package down.TeamBattle.Modules.Modules;

import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Modules.Modules.addons.CustomTTFfont;
import net.minecraft.client.gui.GuiNewChat;

public class TeamChat extends ModuleBase {
	public TeamChat() {
		super("TeamChat");
		setEnabled(true);
	}

	@Override
	public void onDisabled() {
		mc.ingameGUI.persistantChatGUI = new GuiNewChat(mc);
	}

	@Override
	public void onEnabled() {
		mc.ingameGUI.persistantChatGUI = new CustomTTFfont(mc);
	}

	@Override
	public void onEvent(Event event) {

	}
}
