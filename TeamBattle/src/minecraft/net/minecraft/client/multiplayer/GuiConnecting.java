package net.minecraft.client.multiplayer;

import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting extends GuiScreen {
	private static final AtomicInteger field_146372_a = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private NetworkManager field_146371_g;
	private boolean field_146373_h;
	private final GuiScreen field_146374_i;

	public GuiConnecting(GuiScreen p_i1181_1_, Minecraft p_i1181_2_,
			ServerData p_i1181_3_) {
		mc = p_i1181_2_;
		field_146374_i = p_i1181_1_;
		final ServerAddress var4 = ServerAddress
				.func_78860_a(p_i1181_3_.serverIP);
		p_i1181_2_.loadWorld((WorldClient) null);
		p_i1181_2_.setServerData(p_i1181_3_);
		func_146367_a(var4.getIP(), var4.getPort());
	}

	public GuiConnecting(GuiScreen p_i1182_1_, Minecraft p_i1182_2_,
			String p_i1182_3_, int p_i1182_4_) {
		mc = p_i1182_2_;
		field_146374_i = p_i1182_1_;
		p_i1182_2_.loadWorld((WorldClient) null);
		func_146367_a(p_i1182_3_, p_i1182_4_);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			field_146373_h = true;

			if (field_146371_g != null) {
				field_146371_g.closeChannel(new ChatComponentText("Aborted"));
			}

			mc.displayGuiScreen(field_146374_i);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();

		if (field_146371_g == null) {
			drawCenteredString(fontRendererObj,
					I18n.format("connect.connecting", new Object[0]),
					width / 2, height / 2 - 50, 16777215);
		} else {
			drawCenteredString(fontRendererObj,
					I18n.format("connect.authorizing", new Object[0]),
					width / 2, height / 2 - 50, 16777215);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private void func_146367_a(final String p_146367_1_, final int p_146367_2_) {
		logger.info("Connecting to " + p_146367_1_ + ", " + p_146367_2_);
		new Thread("Server Connector #" + field_146372_a.incrementAndGet()) {

			@Override
			public void run() {
				InetAddress var1 = null;

				try {
					if (field_146373_h)
						return;

					var1 = InetAddress.getByName(p_146367_1_);
					field_146371_g = NetworkManager.provideLanClient(var1,
							p_146367_2_);
					field_146371_g.setNetHandler(new NetHandlerLoginClient(
							field_146371_g, GuiConnecting.this.mc,
							field_146374_i));
					field_146371_g.scheduleOutboundPacket(
							new C00Handshake(5, p_146367_1_, p_146367_2_,
									EnumConnectionState.LOGIN),
							new GenericFutureListener[0]);
					field_146371_g.scheduleOutboundPacket(
							new C00PacketLoginStart(GuiConnecting.this.mc
									.getSession().func_148256_e()),
							new GenericFutureListener[0]);
				} catch (final UnknownHostException var5) {
					if (field_146373_h)
						return;

					GuiConnecting.logger.error("Couldn\'t connect to server",
							var5);
					GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(
							field_146374_i, "connect.failed",
							new ChatComponentTranslation(
									"disconnect.genericReason",
									new Object[] { "Unknown host" })));
				} catch (final Exception var6) {
					if (field_146373_h)
						return;

					GuiConnecting.logger.error("Couldn\'t connect to server",
							var6);
					String var3 = var6.toString();

					if (var1 != null) {
						final String var4 = var1.toString() + ":" + p_146367_2_;
						var3 = var3.replaceAll(var4, "");
					}

					GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(
							field_146374_i, "connect.failed",
							new ChatComponentTranslation(
									"disconnect.genericReason",
									new Object[] { var3 })));
				}
			}
		}.start();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 100, height / 2 + 50, I18n
				.format("gui.cancel", new Object[0])));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		if (field_146371_g != null) {
			if (field_146371_g.isChannelOpen()) {
				field_146371_g.processReceivedPackets();
			} else if (field_146371_g.getExitMessage() != null) {
				field_146371_g.getNetHandler().onDisconnect(
						field_146371_g.getExitMessage());
			}
		}
	}
}
