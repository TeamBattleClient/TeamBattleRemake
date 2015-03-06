package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPacketSent;
import down.TeamBattle.Modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.StringUtils;

public final class DashName extends ModuleBase {
	public DashName() {
		super("DashName");
		setEnabled(true);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacketSent) {
			final EventPacketSent sent = (EventPacketSent) event;
			if (sent.getPacket() instanceof C01PacketChatMessage) {
				final C01PacketChatMessage chat = (C01PacketChatMessage) sent
						.getPacket();
				String changed = chat.func_149439_c();
				for (final Object o : Minecraft.getMinecraft().getNetHandler().playerInfoList) {
					final GuiPlayerInfo playerInfo = (GuiPlayerInfo) o;
					final String name = StringUtils
							.stripControlCodes(playerInfo.name);
					if (TeamBattleClient.getFriendManager().isFriend(name)) {
						final String protect = TeamBattleClient.getFriendManager()
								.getContents().get(name);
						changed = changed.replaceAll("(?i)-" + protect, name);
					}
				}
				sent.setPacket(new C01PacketChatMessage(changed));
			}
		}
	}
}
