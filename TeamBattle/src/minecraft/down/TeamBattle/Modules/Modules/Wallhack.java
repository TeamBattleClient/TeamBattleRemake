package down.TeamBattle.Modules.Modules;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventBlockOpacity;
import down.TeamBattle.EventSystem.events.EventBlockRender;
import down.TeamBattle.EventSystem.events.EventRenderAsNormalBlock;
import down.TeamBattle.EventSystem.events.EventRenderBlockPass;
import down.TeamBattle.EventSystem.events.EventWorldLoad;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class Wallhack extends ModuleBase {
	private final List<Block> blocks = new ArrayList<Block>();
	private final Value<Integer> opacity = new Value<Integer>(
			"wallhack_opacity", 128);

	public Wallhack() {
		super("Wallhack", Keyboard.KEY_X, 0xFF8A8A8A);
		blocks.add(Blocks.flowing_lava);
		blocks.add(Blocks.lava);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command("wallhackopacity", "<opacity>",
						"whackopacity", "who") {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("-d")) {
							opacity.setValue(opacity.getDefaultValue());
						} else {
							opacity.setValue(Integer.parseInt(message
									.split(" ")[1]));
						}

						if (opacity.getValue() > 255) {
							opacity.setValue(255);
						} else if (opacity.getValue() < 25) {
							opacity.setValue(25);
						}
						mc.renderGlobal.loadRenderers();
						Logger.logChat("Wallhack Opacity set to: "
								+ opacity.getValue());
					}
				});

		TeamBattleClient.getCommandManager().getContents()
				.add(new Command("wallhack", "<block name/id>", "whack", "wh") {
					public boolean isInteger(String string) {
						try {
							Integer.parseInt(string);
						} catch (final Exception e) {
							return false;
						}
						return true;
					}

					@Override
					public void run(String message) {
						final String[] arguments = message.split(" ");
						if (arguments.length == 1) {
							blocks.clear();
							Logger.logChat("Wallhack cleared.");
							return;
						}
						final String input = message
								.substring((arguments[0] + " ").length());
						Block block = null;
						if (isInteger(input)) {
							block = Block.getBlockById(Integer.parseInt(input));
						} else {
							for (final Object o : Block.blockRegistry) {
								final Block blockk = (Block) o;
								final String name = blockk.getLocalizedName()
										.replaceAll("tile.", "")
										.replaceAll(".name", "");
								if (name.toLowerCase().startsWith(
										input.toLowerCase())) {
									block = blockk;
									break;
								}
							}
						}
						if (block == null) {
							Logger.logChat("No such block.");
							return;
						}
						if (blocks.contains(block)) {
							blocks.remove(blocks.indexOf(block));
						} else {
							blocks.add(block);
						}
						mc.renderGlobal.loadRenderers();
						Logger.logChat((blocks.contains(block) ? "Now"
								: "No longer")
								+ " showing \""
								+ block.getLocalizedName()
										.replaceAll("tile.", "")
										.replaceAll(".name", "")
								+ "\" on Wallhack.");
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

		if (TeamBattleClient.getModManager().getModByName("fullbright") != null
				&& TeamBattleClient.getModManager().getModByName("fullbright")
						.isEnabled()) {
			TeamBattleClient.getModManager().getModByName("fullbright").setEnabled(true);
		}
	}

	@Override
	public void onEnabled() {
		super.onEnabled();
		editTable(mc.theWorld, 1.0F);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventBlockOpacity) {
			final EventBlockOpacity blockOpacity = (EventBlockOpacity) event;
			blockOpacity.setBlockOpacity(opacity.getValue());
		} else if (event instanceof EventRenderBlockPass) {
			final EventRenderBlockPass renderBlockPass = (EventRenderBlockPass) event;
			renderBlockPass.setRenderBlockPass(blocks.contains(renderBlockPass
					.getBlock()) ? 0 : 1);
		} else if (event instanceof EventBlockRender) {
			final EventBlockRender blockRender = (EventBlockRender) event;
			blockRender.setRenderAllFaces(blocks.contains(blockRender
					.getBlock()));
		} else if (event instanceof EventRenderAsNormalBlock) {
			final EventRenderAsNormalBlock renderAsNormalBlock = (EventRenderAsNormalBlock) event;
			renderAsNormalBlock.setRenderAsNormalBlock(false);
		} else if (event instanceof EventWorldLoad) {
			final EventWorldLoad worldLoad = (EventWorldLoad) event;
			editTable(worldLoad.getWorld(), 1.0F);
		}
	}

	@Override
	public void toggle() {
		super.toggle();
		mc.renderGlobal.loadRenderers();
	}
}