package down.TeamBattle.Modules.Modules;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventPreSendMotionUpdates;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public final class InstantUse extends ModuleBase {
	private final Value<Boolean> bow = new Value<Boolean>("instantuse_bow",
			false);
	private final Value<Boolean> food = new Value<Boolean>("instantuse_food",
			true);
	private final Value<Boolean> milk = new Value<Boolean>("instantuse_milk",
			true);
	private final Value<Boolean> potions = new Value<Boolean>(
			"instantuse_potions", true);

	public InstantUse() {
		super("InstantUse", 0xFF2CBCA4);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("instantuse", "<bow/food/potions/milk>",
						"insuse", "iu") {
					@Override
					public void run(String message) {
						final String option = message.split(" ")[1];
						if (option.equalsIgnoreCase("bow")) {
							bow.setValue(!bow.getValue());
							Logger.logChat("InstantUse will "
									+ (bow.getValue() ? "now" : "no longer")
									+ " instantly use bows.");
						} else if (option.equalsIgnoreCase("food")) {
							food.setValue(!food.getValue());
							Logger.logChat("InstantUse will "
									+ (food.getValue() ? "now" : "no longer")
									+ " instantly eat food.");
						} else if (option.equalsIgnoreCase("potions")) {
							potions.setValue(!potions.getValue());
							Logger.logChat("InstantUse will "
									+ (potions.getValue() ? "now" : "no longer")
									+ " instantly drink potions.");
						} else if (option.equalsIgnoreCase("milk")) {
							milk.setValue(!milk.getValue());
							Logger.logChat("InstantUse will "
									+ (milk.getValue() ? "now" : "no longer")
									+ " instantly drink milk.");
						} else {
							Logger.logChat("Invalid option! Valid options: bow, food, potions, milk");
						}
					}
				});
	}

	private boolean isUsable(ItemStack stack) {
		if (stack == null)
			return false;
		if (mc.thePlayer.isUsingItem()) {
			if (stack.getItem() instanceof ItemBow)
				return bow.getValue();
			else if (stack.getItem() instanceof ItemFood)
				return food.getValue();
			else if (stack.getItem() instanceof ItemPotion)
				return potions.getValue();
			else if (stack.getItem() instanceof ItemBucketMilk)
				return milk.getValue();
		}

		return false;
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
			if (isUsable(mc.thePlayer.getCurrentEquippedItem())) {
				mc.getNetHandler().addToSendQueue(
						new C08PacketPlayerBlockPlacement(-1, -1, -1, -1,
								mc.thePlayer.inventory.getCurrentItem(), 0.0F,
								0.0F, 0.0F));
				mc.getNetHandler().addToSendQueue(
						new C09PacketHeldItemChange(
								mc.thePlayer.inventory.currentItem));
				for (int x = 0; x < 32; x++) {
					mc.getNetHandler().addToSendQueue(
							new C03PacketPlayer(mc.thePlayer.onGround));
				}
				mc.getNetHandler().addToSendQueue(
						new C07PacketPlayerDigging(5, 0, 0, 0, 0));
				mc.thePlayer.stopUsingItem();
			}
		}
	}
}
