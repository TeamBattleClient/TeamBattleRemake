package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import down.TeamBattle.Utils.TimeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class AutoArmor extends ModuleBase {
	// diamond, iron, gold, chainmail, leather

	private final int[] boots = { 313, 309, 317, 305, 301 };
	private final int[] chestplate = { 311, 307, 315, 303, 299 };
	private final Value<Long> delay = new Value<Long>("autoarmor_delay", 250L);
	private final int[] helmet = { 310, 306, 314, 302, 298 };
	private final int[] leggings = { 312, 308, 316, 304, 300 };
	private final TimeHelper time = new TimeHelper();

	public AutoArmor() {
		super("AutoArmor", 0xFF98B290);

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("autoarmordelay", "<delay>", "aarmordelay") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							delay.setValue(delay.getDefaultValue());
						} else {
							delay.setValue(Long.parseLong(message.split(" ")[1]));
						}

						if (delay.getValue() < 1L) {
							delay.setValue(1L);
						}
						Logger.logChat("Auto Armor delay set to: "
								+ delay.getValue());

					}
				});
	}

	private int findItem(int id) {
		for (int index = 9; index < 45; index++) {
			final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(
					index).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == id)
				return index;
		}
		return -1;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			int item = -1;
			if (time.hasReached(delay.getValue())) {
				if (mc.thePlayer.inventory.armorInventory[0] == null) {
					for (final int id : boots) {
						if (findItem(id) != -1) {
							item = findItem(id);
							break;
						}
					}
				}

				if (mc.thePlayer.inventory.armorInventory[1] == null) {
					for (final int id : leggings) {
						if (findItem(id) != -1) {
							item = findItem(id);
							break;
						}
					}
				}

				if (mc.thePlayer.inventory.armorInventory[2] == null) {
					for (final int id : chestplate) {
						if (findItem(id) != -1) {
							item = findItem(id);
							break;
						}
					}
				}

				if (mc.thePlayer.inventory.armorInventory[3] == null) {
					for (final int id : helmet) {
						if (findItem(id) != -1) {
							item = findItem(id);
							break;
						}
					}
				}

				if (item != -1) {
					mc.playerController
							.windowClick(0, item, 0, 1, mc.thePlayer);
					time.reset();
				}
			}
		}
	}
}
