package down.TeamBattle.Modules.Modules;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.CommandSystem.Command;
import down.TeamBattle.EventSystem.Event;
import down.TeamBattle.EventSystem.events.EventHUDDraw;
import down.TeamBattle.ModuleValues.Value;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class HUD extends ModuleBase {
	private final Value<Boolean> armor = new Value<Boolean>("hud_armor_status",
			true);
	private final Value<Boolean> arraylist = new Value<Boolean>(
			"hud_arraylist", true);
	private final Value<Boolean> coords = new Value<Boolean>("hud_coords", true);
	private final Value<Boolean> fps = new Value<Boolean>("hud_fps", true);
	private final Value<Boolean> ign = new Value<Boolean>("hud_in-game_name",
			false);
	private final Value<Boolean> item = new Value<Boolean>("hud_item_status",
			false);
	private final RenderItem itemRender = new RenderItem();
	private final Value<Boolean> potions = new Value<Boolean>(
			"hud_potion_effects", true);
	private final Value<Boolean> time = new Value<Boolean>("hud_time", false);
	private final Value<Boolean> watermark = new Value<Boolean>(
			"hud_watermark", true);

	public HUD() {
		super("HUD");
		setEnabled(true);

		TeamBattleClient.getCommandManager()
				.getContents()
				.add(new Command(
						"hud",
						"<watermark/arraylist/coords/fps/ign/time/potions/item/armor>",
						new String[] {}) {
					@Override
					public void run(String message) {
						if (message.split(" ")[1].equalsIgnoreCase("watermark")) {
							watermark.setValue(!watermark.getValue());
							Logger.logChat("HUD will "
									+ (watermark.getValue() ? "now"
											: "no longer")
									+ " show the watermark.");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("arraylist")) {
							arraylist.setValue(!arraylist.getValue());
							Logger.logChat("HUD will "
									+ (arraylist.getValue() ? "now"
											: "no longer")
									+ " show the arraylist.");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("coords")) {
							coords.setValue(!coords.getValue());
							Logger.logChat("HUD will "
									+ (coords.getValue() ? "now" : "no longer")
									+ " show your coordinates. (XYZ)");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("fps")) {
							fps.setValue(!fps.getValue());
							Logger.logChat("HUD will "
									+ (fps.getValue() ? "now" : "no longer")
									+ " show your FPS.");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("ign")) {
							ign.setValue(!ign.getValue());
							Logger.logChat("HUD will "
									+ (ign.getValue() ? "now" : "no longer")
									+ " show your in-game name. (IGN)");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("time")) {
							time.setValue(!time.getValue());
							Logger.logChat("HUD will "
									+ (time.getValue() ? "now" : "no longer")
									+ " show system time.");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("item")) {
							item.setValue(!item.getValue());
							Logger.logChat("HUD will "
									+ (item.getValue() ? "now" : "no longer")
									+ " show your item status.");
						} else if (message.split(" ")[1]
								.equalsIgnoreCase("armor")) {
							armor.setValue(!armor.getValue());
							Logger.logChat("HUD will "
									+ (armor.getValue() ? "now" : "no longer")
									+ " show your armor status.");
						} else {
							Logger.logChat("Invalid option! Valid options: watermark, arraylist, coords, fps, ign, time, potions, item, armor");
						}
						TeamBattleClient.getFileManager().getFileByName("hudconfig")
								.saveFile();
					}
				});
	}

	private void drawArmorStatus(ScaledResolution scaledRes) {
		if (mc.playerController.isNotCreative()) {
			int x = 15;
			GL11.glPushMatrix();
			RenderHelper.enableGUIStandardItemLighting();
			for (int index = 3; index >= 0; index--) {
				final ItemStack stack = mc.thePlayer.inventory.armorInventory[index];
				if (stack != null) {
					itemRender
							.renderItemAndEffectIntoGUI(
									mc.fontRenderer,
									mc.getTextureManager(),
									stack,
									scaledRes.getScaledWidth() / 2 + x,
									scaledRes.getScaledHeight()
											- (mc.thePlayer
													.isInsideOfMaterial(Material.water) ? 65
													: 55));
					itemRender
							.renderItemOverlayIntoGUI(
									mc.fontRenderer,
									mc.getTextureManager(),
									stack,
									scaledRes.getScaledWidth() / 2 + x,
									scaledRes.getScaledHeight()
											- (mc.thePlayer
													.isInsideOfMaterial(Material.water) ? 65
													: 55));
					x += 18;
				}
			}
			RenderHelper.disableStandardItemLighting();
			GL11.glPopMatrix();
		}
	}

	private void drawArraylist(ScaledResolution scaledRes) {
		int y = 2;
		List<ModuleBase> mods = TeamBattleClient.getModManager().getContents();
		Collections.sort(mods, new Comparator<ModuleBase>() {
			@Override
			public int compare(ModuleBase mod1, ModuleBase mod2) {
				return mod1.getTag().compareTo(mod2.getTag());
			}
		});
		for (final ModuleBase mod : mods) {
			if (!mod.isVisible() || !mod.isEnabled()) {
				continue;
			}
			mc.fontRenderer.drawStringWithShadow(
					mod.getTag(),
					scaledRes.getScaledWidth()
							- mc.fontRenderer.getStringWidth(mod.getTag()) - 2,
					y, mod.getColor());
			y += mc.fontRenderer.FONT_HEIGHT;
		}
	}

	private void drawInformation(ScaledResolution scaledRes) {
		final List<String> info = new ArrayList<String>();
		if (ign.getValue()) {
			info.add("IGN: \2477" + mc.session.getUsername());
		}

		if (fps.getValue()) {
			info.add("FPS: \2477"
					+ mc.debug.split(",")[0].replaceAll(" fps", ""));
		}

		if (coords.getValue()) {
			final int x = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posX);
			final int y = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posY);
			final int z = MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
			info.add("XYZ: \2477" + x + ", " + y + ", " + z);
		}

		int y = scaledRes.getScaledHeight() - 10;
		for (final String text : info) {
			mc.fontRenderer.drawStringWithShadow(text, 2, y, 0xFFFFFFFF);
			y -= mc.fontRenderer.FONT_HEIGHT;
		}
	}

	private void drawItemStatus(ScaledResolution scaledRes) {
		final ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		if (stack != null) {
			if (stack.isItemDamaged() || stack.hasTagCompound()) {
				int x = 20;
				int y = watermark.getValue() || time.getValue() ? ((LagDetector) TeamBattleClient
						.getModManager().getModByName("lagdetector")).getTime()
						.hasReached(1000) ? 24 : 12 : ((LagDetector) TeamBattleClient
						.getModManager().getModByName("lagdetector")).getTime()
						.hasReached(1000) ? 12 : 2;
				GL11.glPushMatrix();
				RenderHelper.enableStandardItemLighting();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				itemRender.renderItemAndEffectIntoGUI(mc.fontRenderer,
						mc.getTextureManager(), stack, 2, y);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
				RenderHelper.disableStandardItemLighting();
				if (scaledRes.getScaleFactor() == 2) {
					final double scale = 0.5D;
					GL11.glScaled(scale, scale, scale);
					x *= 2;
					y *= 2;
				}

				// display name
				String name = stack.getDisplayName();
				if (stack.hasDisplayName()) {
					name = "\247o" + name; // italics
				}
				mc.fontRenderer.drawStringWithShadow(name, x, y, 0xFFFFFFFF);
				y += mc.fontRenderer.FONT_HEIGHT;

				// amount of item
				if (stack.stackSize > 1) {
					mc.fontRenderer.drawStringWithShadow("Amount: "
							+ stack.stackSize, x, y, 0xFFFFFFFF);
					y += mc.fontRenderer.FONT_HEIGHT;
				}

				// damage of the item
				if (stack.isItemDamaged() && stack.getMaxDamage() != 0) {
					// color thingy taken from renderitem
					final int damagecolor = (int) Math.round(255.0D
							- stack.getItemDamageForDisplay() * 255.0D
							/ stack.getMaxDamage());
					final int fixedcolor = 255 - damagecolor << 16
							| damagecolor << 8;
					// INTENSE MATH PROBLEMS
					final int maxDamage = stack.getMaxDamage();
					final int itemDamage = stack.getItemDamageForDisplay();
					final int durability = 100 * (maxDamage - itemDamage)
							/ maxDamage;
					mc.fontRenderer.drawStringWithShadow(
							"\247fDurability: \247r" + durability, x, y,
							fixedcolor);
					y += mc.fontRenderer.FONT_HEIGHT;
				}

				// item's enchantments
				if (stack.hasTagCompound()) {
					final NBTTagList list = stack.getEnchantmentTagList();

					if (list != null) {
						if (list.tagCount() >= 10) {
							mc.fontRenderer
									.drawStringWithShadow(
											"God Item (10+ Enchants)", x, y,
											0xFFFF0000);
						} else {
							for (int index = 0; index < list.tagCount(); ++index) {
								final short var7 = list.getCompoundTagAt(index)
										.getShort("id");
								final short var8 = list.getCompoundTagAt(index)
										.getShort("lvl");

								if (Enchantment.enchantmentsList[var7] != null) {
									mc.fontRenderer.drawStringWithShadow(
											Enchantment.enchantmentsList[var7]
													.getTranslatedName(var8),
											x, y, 0xFFAAAAAA);
									y += mc.fontRenderer.FONT_HEIGHT;
								}
							}
						}
					}
				}
				GL11.glPopMatrix();
			}
		}
	}

	private void drawPotionEffects(ScaledResolution scaledRes) {
		int y = 10;
		for (final PotionEffect effect : (Collection<PotionEffect>) mc.thePlayer
				.getActivePotionEffects()) {
			final Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName());

			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			}

			name = name + "\247f: \2477" + Potion.getDurationString(effect);
			mc.fontRenderer.drawStringWithShadow(
					name,
					scaledRes.getScaledWidth()
							- mc.fontRenderer.getStringWidth(name) - 2,
					scaledRes.getScaledHeight() - y, potion.getLiquidColor());
			y += mc.fontRenderer.FONT_HEIGHT;
		}
	}

	private void drawTime(ScaledResolution scaledRes) {
		// this format goes in HOUR:MINUTES A/PM
		// it's also not retarded like cicada's where it shows "03:00 AM"
		// no it goes "3:00 AM"
		// because that's how it works
		final Date date = new Date();
		final SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		final String time = formatter.format(date);
		mc.fontRenderer.drawStringWithShadow(
				time,
				watermark.getValue() ? 2 + mc.fontRenderer
						.getStringWidth("TeamBattle") : 2, 2, 0xDD808080);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventHUDDraw) {
			final ScaledResolution scaledRes = new ScaledResolution(mc,
					mc.displayWidth, mc.displayHeight);
			if (watermark.getValue()) {
				GL11.glEnable(GL11.GL_BLEND);
				mc.fontRenderer.drawStringWithShadow("TeamBattle", 2, 2,
						0x8055FFFF);
				
				GL11.glDisable(GL11.GL_BLEND);
			}

			if (arraylist.getValue()) {
				GL11.glEnable(GL11.GL_BLEND);
				drawArraylist(scaledRes);
				GL11.glDisable(GL11.GL_BLEND);
			}

			drawInformation(scaledRes);

			if (time.getValue()) {
				drawTime(scaledRes);
			}

			if (potions.getValue()) {
				drawPotionEffects(scaledRes);
			}

			if (armor.getValue()) {
				drawArmorStatus(scaledRes);
			}

			if (item.getValue()) {
				drawItemStatus(scaledRes);
			}
		}
	}

}
