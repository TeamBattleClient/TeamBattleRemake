package down.TeamBattle.Modules.Modules;

import net.minecraft.client.multiplayer.WorldClient;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventWorldLoad;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class Fullbright extends ModuleBase {
	private final Value<Float> brightness = new Value<Float>(
			"fullbright_brightness", 0.5F);

	public Fullbright() {
		super("Fullbright", Keyboard.getKeyIndex("C"), 0xFFCD6261);
		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("brightness", "<float>", "b") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							brightness.setValue(brightness.getDefaultValue());
						} else {
							brightness.setValue(Float.parseFloat(message
									.split(" ")[1]));
						}
						if (brightness.getValue() < 0.1F) {
							brightness.setValue(0.1F);
						} else if (brightness.getValue() > 1.0F) {
							brightness.setValue(1.0F);
						}
						Logger.logChat("Brightness set to: "
								+ brightness.getValue());
					}
				});
	}

	public void editTable(WorldClient world, float value) {
		if (world == null)
			return;
		final float[] light = world.provider.lightBrightnessTable;
		for (int index = 0; index < light.length; index++) {
			if (light[index] > value) {
				continue;
			}
			light[index] = value;
		}
	}

	@Override
	public void onDisabled() {
		super.onDisabled();

		if (TeamBattleClient.getModManager().getModByName("wallhack") != null
				&& TeamBattleClient.getModManager().getModByName("wallhack").isEnabled())
			return;
		// took this from the generateLightBrightnessTable() method in world
		// provider
		// works perfectly fine despite what ownage the autist says
		if (mc.theWorld != null) {
			for (int var2 = 0; var2 <= 15; ++var2) {
				final float var3 = 1.0F - var2 / 15.0F;
				mc.theWorld.provider.lightBrightnessTable[var2] = (1.0F - var3)
						/ (var3 * 3.0F + 1.0F);
			}
		}
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		float brightness = this.brightness.getValue();
		if (TeamBattleClient.getModManager().getModByName("wallhack") != null
				&& TeamBattleClient.getModManager().getModByName("wallhack").isEnabled()) {
			brightness = 1.0F;
		}
		editTable(mc.theWorld, brightness);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventWorldLoad) {
			final EventWorldLoad worldLoad = (EventWorldLoad) event;
			float brightness = this.brightness.getValue();
			if (TeamBattleClient.getModManager().getModByName("wallhack") != null
					&& TeamBattleClient.getModManager().getModByName("wallhack")
							.isEnabled()) {
				brightness = 1.0F;
			}
			editTable(worldLoad.getWorld(), brightness);
		}
	}
}