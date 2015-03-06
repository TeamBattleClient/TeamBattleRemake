package down.TeamBattle.Modules.Modules;

import java.util.ArrayList;
import java.util.List;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import down.TeamBattle.Utils.TimeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class AutoDrop extends ModuleBase {
	private final List<Item> dropitems = new ArrayList();
	private final TimeHelper time = new TimeHelper();

	public AutoDrop() {
		super("AutoDrop", 0, 0xFFC24C48);
		setVisible(true);

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("drop", "<item>", new String[] {}) {
					private boolean isInteger(String integer) {
						try {
							Integer.parseInt(integer);
						} catch (final NumberFormatException exception) {
							return false;
						}
						return true;
					}

					@Override
					public void run(String message) {
						final String input = message.substring((message
								.split(" ")[0] + " ").length());
						Item item = null;
						if (isInteger(input)) {
							item = Item.getItemById(Integer.parseInt(input));
						} else {
							for (final Object o : Item.itemRegistry) {
								final Item itemm = (Item) o;
								final String name = StatCollector
										.translateToLocal(itemm
												.getUnlocalizedName() + ".name");
								if (name.toLowerCase().startsWith(input)) {
									item = itemm;
									break;
								}
							}
						}
						if (item == null) {
							Logger.logChat("No such item/block.");
							return;
						}
						if (!dropitems.contains(item)) {
							dropitems.add(item);
						} else {
							dropitems.remove(dropitems.indexOf(item));
						}

						Logger.logChat(StatCollector.translateToLocal(item
								.getUnlocalizedName() + ".name")
								+ " will "
								+ (dropitems.contains(item) ? "now"
										: "no longer") + " be dropped.");
					}
				});
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			for (int x = 9; x < 45; x++) {
				final ItemStack itemStack = mc.thePlayer.inventoryContainer
						.getSlot(x).getStack();
				if (itemStack != null
						&& dropitems.contains(itemStack.getItem())
						&& time.hasReached(200)) {
					mc.playerController.windowClick(0, x, 0, 0, mc.thePlayer);
					mc.playerController
							.windowClick(0, -999, 0, 0, mc.thePlayer);
					time.setLastMS(time.getCurrentMS());
				}
			}
		}
	}
}