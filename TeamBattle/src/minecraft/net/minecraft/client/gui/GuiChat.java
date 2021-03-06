package net.minecraft.client.gui;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import tv.twitch.chat.ChatUserInfo;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import down.TeamBattle.TeamBattleClient;

public class GuiChat extends GuiScreen implements GuiYesNoCallback {
	private static final Set field_152175_f = Sets.newHashSet(new String[] {
			"http", "https" });
	private static final Logger logger = LogManager.getLogger();
	private String field_146409_v = "";
	private String field_146410_g = "";
	private URI field_146411_u;
	private final List field_146412_t = new ArrayList();
	private int field_146413_s;
	private boolean field_146414_r;
	protected GuiTextField field_146415_a;
	private int field_146416_h = -1;
	private boolean field_146417_i;

	public GuiChat() {
	}

	public GuiChat(String p_i1024_1_) {
		field_146409_v = p_i1024_1_;
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_2_ == 0) {
			if (p_73878_1_) {
				func_146407_a(field_146411_u);
			}

			field_146411_u = null;
			mc.displayGuiScreen(this);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (mc.ingameGUI.persistantChatGUI.drag) {
			mc.ingameGUI.persistantChatGUI.drag(p_73863_1_, p_73863_2_);
		}
		drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
		field_146415_a.drawTextBox();
		final IChatComponent var4 = mc.ingameGUI.getChatGUI().func_146236_a(
				Mouse.getX(), Mouse.getY());

		if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null) {
			final HoverEvent var5 = var4.getChatStyle().getChatHoverEvent();

			if (var5.getAction() == HoverEvent.Action.SHOW_ITEM) {
				ItemStack var6 = null;

				try {
					final NBTBase var7 = JsonToNBT.func_150315_a(var5
							.getValue().getUnformattedText());

					if (var7 != null && var7 instanceof NBTTagCompound) {
						var6 = ItemStack
								.loadItemStackFromNBT((NBTTagCompound) var7);
					}
				} catch (final NBTException var11) {
					;
				}

				if (var6 != null) {
					func_146285_a(var6, p_73863_1_, p_73863_2_);
				} else {
					func_146279_a(EnumChatFormatting.RED + "Invalid Item!",
							p_73863_1_, p_73863_2_);
				}
			} else if (var5.getAction() == HoverEvent.Action.SHOW_TEXT) {
				func_146283_a(
						Splitter.on("\n").splitToList(
								var5.getValue().getFormattedText()),
						p_73863_1_, p_73863_2_);
			} else if (var5.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
				final StatBase var12 = StatList.func_151177_a(var5.getValue()
						.getUnformattedText());

				if (var12 != null) {
					final IChatComponent var13 = var12.func_150951_e();
					final ChatComponentTranslation var8 = new ChatComponentTranslation(
							"stats.tooltip.type."
									+ (var12.isAchievement() ? "achievement"
											: "statistic"), new Object[0]);
					var8.getChatStyle().setItalic(Boolean.valueOf(true));
					final String var9 = var12 instanceof Achievement ? ((Achievement) var12)
							.getDescription() : null;
					final ArrayList var10 = Lists
							.newArrayList(new String[] {
									var13.getFormattedText(),
									var8.getFormattedText() });

					if (var9 != null) {
						var10.addAll(fontRendererObj
								.listFormattedStringToWidth(var9, 150));
					}

					func_146283_a(var10, p_73863_1_, p_73863_2_);
				} else {
					func_146279_a(EnumChatFormatting.RED
							+ "Invalid statistic/achievement!", p_73863_1_,
							p_73863_2_);
				}
			}

			GL11.glDisable(GL11.GL_LIGHTING);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public void func_146402_a(int p_146402_1_) {
		int var2 = field_146416_h + p_146402_1_;
		final int var3 = mc.ingameGUI.getChatGUI().func_146238_c().size();

		if (var2 < 0) {
			var2 = 0;
		}

		if (var2 > var3) {
			var2 = var3;
		}

		if (var2 != field_146416_h) {
			if (var2 == var3) {
				field_146416_h = var3;
				field_146415_a.setText(field_146410_g);
			} else {
				if (field_146416_h == var3) {
					field_146410_g = field_146415_a.getText();
				}

				field_146415_a.setText((String) mc.ingameGUI.getChatGUI()
						.func_146238_c().get(var2));
				field_146416_h = var2;
			}
		}
	}

	public void func_146403_a(String p_146403_1_) {
		mc.ingameGUI.getChatGUI().func_146239_a(p_146403_1_);
		mc.thePlayer.sendChatMessage(p_146403_1_);
	}

	public void func_146404_p_() {
		String var3;

		if (field_146417_i) {
			field_146415_a.func_146175_b(field_146415_a.func_146197_a(-1,
					field_146415_a.func_146198_h(), false)
					- field_146415_a.func_146198_h());

			if (field_146413_s >= field_146412_t.size()) {
				field_146413_s = 0;
			}
		} else {
			final int var1 = field_146415_a.func_146197_a(-1,
					field_146415_a.func_146198_h(), false);
			field_146412_t.clear();
			field_146413_s = 0;
			final String var2 = field_146415_a.getText().substring(var1)
					.toLowerCase();
			var3 = field_146415_a.getText().substring(0,
					field_146415_a.func_146198_h());
			func_146405_a(var3, var2);

			if (field_146412_t.isEmpty())
				return;

			field_146417_i = true;
			field_146415_a.func_146175_b(var1 - field_146415_a.func_146198_h());
		}

		if (field_146412_t.size() > 1) {
			final StringBuilder var4 = new StringBuilder();

			for (final Iterator var5 = field_146412_t.iterator(); var5
					.hasNext(); var4.append(var3)) {
				var3 = (String) var5.next();

				if (var4.length() > 0) {
					var4.append(", ");
				}
			}

			mc.ingameGUI.getChatGUI().func_146234_a(
					new ChatComponentText(var4.toString()), 1);
		}

		field_146415_a.func_146191_b((String) field_146412_t
				.get(field_146413_s++));
	}

	private void func_146405_a(String p_146405_1_, String p_146405_2_) {
		if (p_146405_1_.length() >= 1) {
			mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(
					p_146405_1_));
			field_146414_r = true;
		}
	}

	public void func_146406_a(String[] p_146406_1_) {
		if (field_146414_r) {
			field_146417_i = false;
			field_146412_t.clear();
			final String[] var2 = p_146406_1_;
			final int var3 = p_146406_1_.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				final String var5 = var2[var4];

				if (var5.length() > 0) {
					field_146412_t.add(var5);
				}
			}

			final String var6 = field_146415_a.getText().substring(
					field_146415_a.func_146197_a(-1,
							field_146415_a.func_146198_h(), false));
			final String var7 = StringUtils.getCommonPrefix(p_146406_1_);

			if (var7.length() > 0 && !var6.equalsIgnoreCase(var7)) {
				field_146415_a.func_146175_b(field_146415_a.func_146197_a(-1,
						field_146415_a.func_146198_h(), false)
						- field_146415_a.func_146198_h());
				field_146415_a.func_146191_b(var7);
			} else if (field_146412_t.size() > 0) {
				field_146417_i = true;
				func_146404_p_();
			}
		}
	}

	private void func_146407_a(URI p_146407_1_) {
		try {
			final Class var2 = Class.forName("java.awt.Desktop");
			final Object var3 = var2.getMethod("getDesktop", new Class[0])
					.invoke((Object) null, new Object[0]);
			var2.getMethod("browse", new Class[] { URI.class }).invoke(var3,
					new Object[] { p_146407_1_ });
		} catch (final Throwable var4) {
			logger.error("Couldn\'t open link", var4);
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0) {
			if (var1 > 1) {
				var1 = 1;
			}

			if (var1 < -1) {
				var1 = -1;
			}

			if (!isShiftKeyDown()) {
				var1 *= 7;
			}

			mc.ingameGUI.getChatGUI().func_146229_b(var1);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		field_146416_h = mc.ingameGUI.getChatGUI().func_146238_c().size();
		field_146415_a = new GuiTextField(fontRendererObj, 4, height - 12,
				width - 4, 12);
		field_146415_a.func_146203_f(100);
		field_146415_a.func_146185_a(false);
		field_146415_a.setFocused(true);
		field_146415_a.setText(field_146409_v);
		field_146415_a.func_146205_d(false);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146414_r = false;

		if (p_73869_2_ == 15) {
			func_146404_p_();
		} else {
			field_146417_i = false;
		}

		if (p_73869_2_ == 1) {
			mc.displayGuiScreen((GuiScreen) null);
		} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			if (p_73869_2_ == 200) {
				func_146402_a(-1);
			} else if (p_73869_2_ == 208) {
				func_146402_a(1);
			} else if (p_73869_2_ == 201) {
				mc.ingameGUI.getChatGUI().func_146229_b(
						mc.ingameGUI.getChatGUI().func_146232_i() - 1);
			} else if (p_73869_2_ == 209) {
				mc.ingameGUI.getChatGUI().func_146229_b(
						-mc.ingameGUI.getChatGUI().func_146232_i() + 1);
			} else {
				field_146415_a.textboxKeyTyped(p_73869_1_, p_73869_2_);
			}
		} else {
			final String var3 = field_146415_a.getText().trim();

			if (var3.length() > 0) {
				func_146403_a(var3);
			}

			mc.displayGuiScreen((GuiScreen) null);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		mc.ingameGUI.persistantChatGUI.mouseClicked(p_73864_1_, p_73864_2_,
				p_73864_3_);
		final boolean Teamchat = TeamBattleClient.getModManager().getModByName(
				"TeamChat") != null
				&& TeamBattleClient.getModManager().getModByName("TeamChat").isEnabled();
		if (p_73864_3_ == 0 && mc.gameSettings.chatLinks && !Teamchat) {
			final IChatComponent var4 = mc.ingameGUI.getChatGUI()
					.func_146236_a(Mouse.getX(), Mouse.getY());

			if (var4 != null) {
				final ClickEvent var5 = var4.getChatStyle().getChatClickEvent();

				if (var5 != null) {
					if (isShiftKeyDown()) {
						field_146415_a.func_146191_b(var4
								.getUnformattedTextForChat());
					} else {
						URI var6;

						if (var5.getAction() == ClickEvent.Action.OPEN_URL) {
							try {
								var6 = new URI(var5.getValue());

								if (!field_152175_f.contains(var6.getScheme()
										.toLowerCase()))
									throw new URISyntaxException(
											var5.getValue(),
											"Unsupported protocol: "
													+ var6.getScheme()
															.toLowerCase());

								if (mc.gameSettings.chatLinksPrompt) {
									field_146411_u = var6;
									mc.displayGuiScreen(new GuiConfirmOpenLink(
											this, var5.getValue(), 0, false));
								} else {
									func_146407_a(var6);
								}
							} catch (final URISyntaxException var7) {
								logger.error("Can\'t open url for " + var5,
										var7);
							}
						} else if (var5.getAction() == ClickEvent.Action.OPEN_FILE) {
							var6 = new File(var5.getValue()).toURI();
							func_146407_a(var6);
						} else if (var5.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
							field_146415_a.setText(var5.getValue());
						} else if (var5.getAction() == ClickEvent.Action.RUN_COMMAND) {
							func_146403_a(var5.getValue());
						} else if (var5.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
							final ChatUserInfo var8 = mc.func_152346_Z()
									.func_152926_a(var5.getValue());

							if (var8 != null) {
								mc.displayGuiScreen(new GuiTwitchUserMode(mc
										.func_152346_Z(), var8));
							} else {
								logger.error("Tried to handle twitch user but couldn\'t find them!");
							}
						} else {
							logger.error("Don\'t know how to handle " + var5);
						}
					}

					return;
				}
			}
		}

		field_146415_a.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		mc.ingameGUI.persistantChatGUI.mouseMovedOrUp(p_146286_1_, p_146286_2_,
				p_146286_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		mc.ingameGUI.getChatGUI().resetScroll();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		field_146415_a.updateCursorCounter();
	}
}
