package net.minecraft.client.gui;

import java.util.List;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private final OldServerPinger field_146797_f = new OldServerPinger();
	private final GuiScreen field_146798_g;
	private LanServerDetector.LanServerList field_146799_A;
	private LanServerDetector.ThreadLanServerFind field_146800_B;
	private boolean field_146801_C;
	private ServerSelectionList field_146803_h;
	private ServerList field_146804_i;
	private boolean field_146805_w;
	private boolean field_146806_v;
	private boolean field_146807_u;
	private GuiButton field_146808_t;
	private GuiButton field_146809_s;
	private GuiButton field_146810_r;
	private ServerData field_146811_z;
	private String field_146812_y;
	private boolean field_146813_x;

	public GuiMultiplayer(GuiScreen p_i1040_1_) {
		field_146798_g = p_i1040_1_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			final GuiListExtended.IGuiListEntry var2 = field_146803_h
					.func_148193_k() < 0 ? null : field_146803_h
					.func_148180_b(field_146803_h.func_148193_k());

			if (p_146284_1_.id == 2 && var2 instanceof ServerListEntryNormal) {
				final String var9 = ((ServerListEntryNormal) var2)
						.func_148296_a().serverName;

				if (var9 != null) {
					field_146807_u = true;
					final String var4 = I18n.format(
							"selectServer.deleteQuestion", new Object[0]);
					final String var5 = "\'"
							+ var9
							+ "\' "
							+ I18n.format("selectServer.deleteWarning",
									new Object[0]);
					final String var6 = I18n.format(
							"selectServer.deleteButton", new Object[0]);
					final String var7 = I18n
							.format("gui.cancel", new Object[0]);
					final GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6,
							var7, field_146803_h.func_148193_k());
					mc.displayGuiScreen(var8);
				}
			} else if (p_146284_1_.id == 1) {
				func_146796_h();
			} else if (p_146284_1_.id == 4) {
				field_146813_x = true;
				mc.displayGuiScreen(new GuiScreenServerList(this,
						field_146811_z = new ServerData(I18n.format(
								"selectServer.defaultName", new Object[0]), "")));
			} else if (p_146284_1_.id == 3) {
				field_146806_v = true;
				mc.displayGuiScreen(new GuiScreenAddServer(this,
						field_146811_z = new ServerData(I18n.format(
								"selectServer.defaultName", new Object[0]), "")));
			} else if (p_146284_1_.id == 7
					&& var2 instanceof ServerListEntryNormal) {
				field_146805_w = true;
				final ServerData var3 = ((ServerListEntryNormal) var2)
						.func_148296_a();
				field_146811_z = new ServerData(var3.serverName, var3.serverIP);
				field_146811_z.func_152583_a(var3);
				mc.displayGuiScreen(new GuiScreenAddServer(this, field_146811_z));
			} else if (p_146284_1_.id == 0) {
				mc.displayGuiScreen(field_146798_g);
			} else if (p_146284_1_.id == 8) {
				func_146792_q();
			}
		}
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		final GuiListExtended.IGuiListEntry var3 = field_146803_h
				.func_148193_k() < 0 ? null : field_146803_h
				.func_148180_b(field_146803_h.func_148193_k());

		if (field_146807_u) {
			field_146807_u = false;

			if (p_73878_1_ && var3 instanceof ServerListEntryNormal) {
				field_146804_i.removeServerData(field_146803_h.func_148193_k());
				field_146804_i.saveServerList();
				field_146803_h.func_148192_c(-1);
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		} else if (field_146813_x) {
			field_146813_x = false;

			if (p_73878_1_) {
				func_146791_a(field_146811_z);
			} else {
				mc.displayGuiScreen(this);
			}
		} else if (field_146806_v) {
			field_146806_v = false;

			if (p_73878_1_) {
				field_146804_i.addServerData(field_146811_z);
				field_146804_i.saveServerList();
				field_146803_h.func_148192_c(-1);
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		} else if (field_146805_w) {
			field_146805_w = false;

			if (p_73878_1_ && var3 instanceof ServerListEntryNormal) {
				final ServerData var4 = ((ServerListEntryNormal) var3)
						.func_148296_a();
				var4.serverName = field_146811_z.serverName;
				var4.serverIP = field_146811_z.serverIP;
				var4.func_152583_a(field_146811_z);
				field_146804_i.saveServerList();
				field_146803_h.func_148195_a(field_146804_i);
			}

			mc.displayGuiScreen(this);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_146812_y = null;
		drawDefaultBackground();
		field_146803_h.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj,
				I18n.format("multiplayer.title", new Object[0]), width / 2, 20,
				16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

		if (field_146812_y != null) {
			func_146283_a(
					Lists.newArrayList(Splitter.on("\n").split(field_146812_y)),
					p_73863_1_, p_73863_2_);
		}
	}

	public OldServerPinger func_146789_i() {
		return field_146797_f;
	}

	public void func_146790_a(int p_146790_1_) {
		field_146803_h.func_148192_c(p_146790_1_);
		final GuiListExtended.IGuiListEntry var2 = p_146790_1_ < 0 ? null
				: field_146803_h.func_148180_b(p_146790_1_);
		field_146809_s.enabled = false;
		field_146810_r.enabled = false;
		field_146808_t.enabled = false;

		if (var2 != null && !(var2 instanceof ServerListEntryLanScan)) {
			field_146809_s.enabled = true;

			if (var2 instanceof ServerListEntryNormal) {
				field_146810_r.enabled = true;
				field_146808_t.enabled = true;
			}
		}
	}

	private void func_146791_a(ServerData p_146791_1_) {
		mc.displayGuiScreen(new GuiConnecting(this, mc, p_146791_1_));
	}

	private void func_146792_q() {
		mc.displayGuiScreen(new GuiMultiplayer(field_146798_g));
	}

	public void func_146793_a(String p_146793_1_) {
		field_146812_y = p_146793_1_;
	}

	public void func_146794_g() {
		buttons.add(field_146810_r = new GuiButton(7, width / 2 - 154,
				height - 28, 70, 20, I18n.format("selectServer.edit",
						new Object[0])));
		buttons.add(field_146808_t = new GuiButton(2, width / 2 - 74,
				height - 28, 70, 20, I18n.format("selectServer.delete",
						new Object[0])));
		buttons.add(field_146809_s = new GuiButton(1, width / 2 - 154,
				height - 52, 100, 20, I18n.format("selectServer.select",
						new Object[0])));
		buttons.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n
				.format("selectServer.direct", new Object[0])));
		buttons.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20,
				I18n.format("selectServer.add", new Object[0])));
		buttons.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n
				.format("selectServer.refresh", new Object[0])));
		buttons.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20,
				I18n.format("gui.cancel", new Object[0])));
		func_146790_a(field_146803_h.func_148193_k());
	}

	public ServerList func_146795_p() {
		return field_146804_i;
	}

	public void func_146796_h() {
		final GuiListExtended.IGuiListEntry var1 = field_146803_h
				.func_148193_k() < 0 ? null : field_146803_h
				.func_148180_b(field_146803_h.func_148193_k());

		if (var1 instanceof ServerListEntryNormal) {
			func_146791_a(((ServerListEntryNormal) var1).func_148296_a());
		} else if (var1 instanceof ServerListEntryLanDetected) {
			final LanServerDetector.LanServer var2 = ((ServerListEntryLanDetected) var1)
					.func_148289_a();
			func_146791_a(new ServerData(var2.getServerMotd(),
					var2.getServerIpPort(), true));
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttons.clear();

		if (!field_146801_C) {
			field_146801_C = true;
			field_146804_i = new ServerList(mc);
			field_146804_i.loadServerList();
			field_146799_A = new LanServerDetector.LanServerList();

			try {
				field_146800_B = new LanServerDetector.ThreadLanServerFind(
						field_146799_A);
				field_146800_B.start();
			} catch (final Exception var2) {
				logger.warn("Unable to start LAN server detection: "
						+ var2.getMessage());
			}

			field_146803_h = new ServerSelectionList(this, mc, width, height,
					32, height - 64, 36);
			field_146803_h.func_148195_a(field_146804_i);
		} else {
			field_146803_h.func_148122_a(width, height, 32, height - 64);
		}

		func_146794_g();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		final int var3 = field_146803_h.func_148193_k();
		final GuiListExtended.IGuiListEntry var4 = var3 < 0 ? null
				: field_146803_h.func_148180_b(var3);

		if (p_73869_2_ == 63) {
			func_146792_q();
		} else {
			if (var3 >= 0) {
				if (p_73869_2_ == 200) {
					if (isShiftKeyDown()) {
						if (var3 > 0 && var4 instanceof ServerListEntryNormal) {
							field_146804_i.swapServers(var3, var3 - 1);
							func_146790_a(field_146803_h.func_148193_k() - 1);
							field_146803_h.func_148145_f(-field_146803_h
									.func_148146_j());
							field_146803_h.func_148195_a(field_146804_i);
						}
					} else if (var3 > 0) {
						func_146790_a(field_146803_h.func_148193_k() - 1);
						field_146803_h.func_148145_f(-field_146803_h
								.func_148146_j());

						if (field_146803_h.func_148180_b(field_146803_h
								.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (field_146803_h.func_148193_k() > 0) {
								func_146790_a(field_146803_h.getSize() - 1);
								field_146803_h.func_148145_f(-field_146803_h
										.func_148146_j());
							} else {
								func_146790_a(-1);
							}
						}
					} else {
						func_146790_a(-1);
					}
				} else if (p_73869_2_ == 208) {
					if (isShiftKeyDown()) {
						if (var3 < field_146804_i.countServers() - 1) {
							field_146804_i.swapServers(var3, var3 + 1);
							func_146790_a(var3 + 1);
							field_146803_h.func_148145_f(field_146803_h
									.func_148146_j());
							field_146803_h.func_148195_a(field_146804_i);
						}
					} else if (var3 < field_146803_h.getSize()) {
						func_146790_a(field_146803_h.func_148193_k() + 1);
						field_146803_h.func_148145_f(field_146803_h
								.func_148146_j());

						if (field_146803_h.func_148180_b(field_146803_h
								.func_148193_k()) instanceof ServerListEntryLanScan) {
							if (field_146803_h.func_148193_k() < field_146803_h
									.getSize() - 1) {
								func_146790_a(field_146803_h.getSize() + 1);
								field_146803_h.func_148145_f(field_146803_h
										.func_148146_j());
							} else {
								func_146790_a(-1);
							}
						}
					} else {
						func_146790_a(-1);
					}
				} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
					super.keyTyped(p_73869_1_, p_73869_2_);
				} else {
					actionPerformed((GuiButton) buttons.get(2));
				}
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146803_h.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		field_146803_h.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		if (field_146800_B != null) {
			field_146800_B.interrupt();
			field_146800_B = null;
		}

		field_146797_f.func_147226_b();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		if (field_146799_A.getWasUpdated()) {
			final List var1 = field_146799_A.getLanServers();
			field_146799_A.setWasNotUpdated();
			field_146803_h.func_148194_a(var1);
		}

		field_146797_f.func_147223_a();
	}
}
