package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPostSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import down.TeamBattle.Utils.TimeHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public final class Repot extends ModuleBase {
	private final Value<Long> delay = new Value<Long>("repot_delay", 500L);
	private final TimeHelper time = new TimeHelper();

	public Repot() {
		super("Repot", 0xFFDCD1DB);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("repotdelay", "<milliseconds>", "rpotdelay",
						"rpd") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							delay.setValue(delay.getDefaultValue());
						} else {
							delay.setValue(Long.parseLong(message.split(" ")[1]));
						}
						if (delay.getValue() > 1000L) {
							delay.setValue(1000L);
						} else if (delay.getValue() < 0) {
							delay.setValue(0L);
						}
						Logger.logChat("Repot Delay set to: "
								+ delay.getValue());
					}
				});
	}

	private boolean isHotbarFull() {
		for (int index = 36; index < 45; index++) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(
					index).getStack();
			if (stack == null)
				return false;
		}
		return true;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPostSendMotionUpdates) {
			if (mc.currentScreen instanceof GuiChest)
				return;
			if (time.hasReached(delay.getValue())) {
				if (!isHotbarFull()) {
					for (int index = 9; index < 36; index++) {
						final ItemStack stack = mc.thePlayer.inventoryContainer
								.getSlot(index).getStack();
						if (stack == null) {
							continue;
						}

						if (stack.getItem() instanceof ItemPotion) {
							mc.playerController.windowClick(0, index, 0, 1,
									mc.thePlayer);
							break;
						}
					}
				}
				time.reset();
			}
		}
	}
}
