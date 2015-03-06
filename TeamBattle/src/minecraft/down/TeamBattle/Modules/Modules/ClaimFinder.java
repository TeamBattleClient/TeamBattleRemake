package down.TeamBattle.Modules.Modules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPacketReceive;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import down.TeamBattle.Utils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;

public final class ClaimFinder extends ModuleBase {
	private final Value<Long> delay = new Value<Long>("claimfinder_delay", 1L);
	private int line;
	private List<String> map = new CopyOnWriteArrayList<String>();
	private final Value<Integer> radius = new Value<Integer>(
			"claimfinder_radius", 10000);
	private final TimeHelper time = new TimeHelper();
	private int x;
	private int z;

	public ClaimFinder() {
		super("ClaimFinder", 0xFF92DAC2);
		x = -radius.getValue();
		z = -radius.getValue();

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("claimfinderdelay", "<delay>", "cfdelay",
						"cfd") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							delay.setValue(delay.getDefaultValue());
						} else {
							delay.setValue(Long.parseLong(message.split(" ")[1]));
						}

						if (delay.getValue() > 10000) {
							delay.setValue(10000L);
						} else if (delay.getValue() < 1) {
							delay.setValue(1L);
						}
						Logger.logChat("ClaimFinder Delay set to: "
								+ delay.getValue());
					}
				});

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("claimfinderradius", "<radius>", "cfradius",
						"cfr") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							radius.setValue(radius.getDefaultValue());
						} else {
							radius.setValue(Integer.parseInt(message.split(" ")[1]));
						}
						if (radius.getValue() > 50000) {
							radius.setValue(50000);
						} else if (radius.getValue() < 500) {
							radius.setValue(500);
						}
						x = -radius.getValue();
						z = -radius.getValue();
						Logger.logChat("ClaimFinder Radius set to: "
								+ radius.getValue());
					}
				});
	}

	public final List<String> getMap() {
		return map;
	}

	@Override
	public void onDisabled() {
		if (mc.theWorld != null && mc.thePlayer.ridingEntity != null) {
			mc.thePlayer.sendChatMessage("/f map off");
		}
		setTag("ClaimFinder");
		super.onDisabled();
	}

	@Override
	public void onEnabled() {
		if (mc.theWorld == null) {
			toggle();
			return;
		} else {
			mc.thePlayer.sendChatMessage("/f map on");
		}
		super.onEnabled();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			final EventPreSendMotionUpdates pre = (EventPreSendMotionUpdates) event;
			if (mc.thePlayer.ridingEntity == null) {
				Logger.logChat("Get in a vehicle!");
				toggle();
				return;
			}
			pre.setCancelled(true);
		} else if (event instanceof EventPacketReceive) {
			final EventPacketReceive receive = (EventPacketReceive) event;
			if (receive.getPacket() instanceof S02PacketChat) {
				final S02PacketChat chat = (S02PacketChat) receive.getPacket();
				final String message = StringUtils.stripControlCodes(chat
						.func_148915_c().getFormattedText());
				final Pattern pattern = Pattern
						.compile(".+\\((-?[0-9]+),(-?[0-9]+)\\).([^ ]+).+");
				final Matcher matcher = pattern.matcher(message);
				if (matcher.matches()) {
					new Thread() {
						@Override
						public void run() {
							if (time.hasReached(delay.getValue())) {
								if (z <= radius.getValue()) {
									z += 128;
								} else if (x <= radius.getValue()) {
									z = -radius.getValue();
									x += 624;
								} else {
									Logger.logChat("ClaimFinder finished!");
									if (TeamBattleClient.getFileManager().getFileByName(
											"coords") != null) {
										TeamBattleClient.getFileManager()
												.getFileByName("coords")
												.saveFile();
									}
									map = new ArrayList();
									x = -radius.getValue();
									z = -radius.getValue();
									setEnabled(false);
									return;
								}
								setTag("ClaimFinder (" + x + ", " + z + ")");
								mc.getNetHandler()
										.addToSendQueue(
												new C03PacketPlayer.C04PacketPlayerPosition(
														x, 0.0D, 1.0D, z, true));
								time.reset();
							}
						}
					}.start();
					line = 1;
				}
				if (line > 0) {
					map.add(message);
					receive.setCancelled(true);
					line += 1;
					if (line == 11) {
						line = 0;
					}
				}
			}
		}
	}

}