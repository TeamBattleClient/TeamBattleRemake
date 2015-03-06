package down.TeamBattle.CommandSystem.commands;

import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.Utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class ItemSize extends Command {
	public ItemSize() {
		super("itemsize", "<size>", "is");
	}

	@Override
	public void run(String message) {
		if (Minecraft.getMinecraft().playerController.isNotCreative()) {
			Logger.logChat("You must be in creative mode to edit an item's stack size!");
			return;
		}
		final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory
				.getCurrentItem();
		if (itemStack == null) {
			Logger.logChat("You must be holding an item to edit it's size!");
			return;
		}
		int size = Integer.parseInt(message.split(" ")[1]);
		if (size < 0) {
			size = 0;
		}
		itemStack.stackSize = size;
		Minecraft
				.getMinecraft()
				.getNetHandler()
				.addToSendQueue(
						new C10PacketCreativeInventoryAction(
								Minecraft.getMinecraft().thePlayer.inventory.currentItem,
								itemStack));
		Logger.logChat("Your current item now has an amount of: " + size);
	}
}