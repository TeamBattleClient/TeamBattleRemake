package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public final class AutoEat extends ModuleBase {
	private final int[] food = { 320, 350, 366, 364, 393, 319, 349, 363, 365,
			392, 297, 360, 260, 282, 322, 367 };
	private final Value<Float> foodlvl = new Value<Float>("autoeat_food_level",
			17.0F);

	public AutoEat() {
		super("AutoEat", 0xFFC0B987);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("autoeatfoodlevel", "<food level>", "aefood",
						"aef") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							foodlvl.setValue(foodlvl.getDefaultValue());
						} else {
							foodlvl.setValue(Float.parseFloat(message
									.split(" ")[1]));
						}

						if (foodlvl.getValue() > 19.0F) {
							foodlvl.setValue(19.0F);
						} else if (foodlvl.getValue() < 1.0F) {
							foodlvl.setValue(1.0F);
						}
						Logger.logChat("AutoEat Food Level set to: "
								+ foodlvl.getValue());
					}
				});
	}

	private boolean canEat() {
		return mc.thePlayer.onGround
				&& mc.thePlayer.getFoodStats().getFoodLevel() <= foodlvl
						.getValue();
	}

	private int findItem(int id) {
		for (int index = 36; index < 45; index++) {
			final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(
					index).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == id)
				return index;
		}
		return -1;
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		if (mc.theWorld != null) {
			Logger.logChat("\247cWARNING:\247f This mod is patched by newer NoCheat versions.");
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreSendMotionUpdates) {
			if (canEat()) {
				int selectedSlot = -1;
				final int original = mc.thePlayer.inventory.currentItem;
				for (final int id : food) {
					if (findItem(id) != -1) {
						selectedSlot = findItem(id) - 36;
						break;
					}
				}

				if (selectedSlot != -1) {
					mc.getNetHandler().addToSendQueue(
							new C09PacketHeldItemChange(selectedSlot));
					mc.playerController.updateController();
					mc.getNetHandler().addToSendQueue(
							new C08PacketPlayerBlockPlacement(-1, -1, -1, -1,
									mc.thePlayer.inventory.getCurrentItem(),
									0.0F, 0.0F, 0.0F));
					mc.getNetHandler().addToSendQueue(
							new C09PacketHeldItemChange(selectedSlot));
					for (int x = 0; x < 32; x++) {
						mc.getNetHandler().addToSendQueue(
								new C03PacketPlayer(mc.thePlayer.onGround));
					}
					mc.getNetHandler().addToSendQueue(
							new C07PacketPlayerDigging(5, 0, 0, 0, 0));
					mc.thePlayer.stopUsingItem();
					mc.getNetHandler().addToSendQueue(
							new C09PacketHeldItemChange(original));
				}
			}
		}
	}
}
