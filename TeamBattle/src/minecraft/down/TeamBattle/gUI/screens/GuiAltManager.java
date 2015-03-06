package down.TeamBattle.gUI.screens;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.MinecraftLoginUtils.AltLoginThread;
import down.TeamBattle.Utils.Alt;
import down.TeamBattle.Utils.RenderHelper;

public class GuiAltManager extends GuiScreen {
	private GuiButton login, remove, rename;
	private AltLoginThread loginThread;
	private int offset;
	public Alt selectedAlt = null;
	private String status = "\2477Waiting...";

	public GuiAltManager() {
		TeamBattleClient.getAltManager().getContents().clear();
		TeamBattleClient.getFileManager().getFileByName("alts").loadFile();
	}

	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			if (loginThread == null) {
				mc.displayGuiScreen(null);
				break;
			}
			if (!loginThread.getStatus().equals("\247eLogging in...")
					&& !loginThread.getStatus().equals(
							"\247cDo not hit back! \247eLogging in...")) {
				mc.displayGuiScreen(null);
			} else {
				loginThread
						.setStatus("\247cDo not hit back! \247eLogging in...");
			}
			break;
		case 1:
			final String user = selectedAlt.getUsername();
			final String pass = selectedAlt.getPassword();
			loginThread = new AltLoginThread(user, pass);
			loginThread.start();
			break;
		case 2:
			if (loginThread != null) {
				loginThread = null;
			}
			TeamBattleClient.getAltManager().getContents().remove(selectedAlt);
			status = "\247aRemoved.";
			selectedAlt = null;
			TeamBattleClient.getFileManager().getFileByName("alts").saveFile();
			break;
		case 3:
			mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		case 4:
			mc.displayGuiScreen(new GuiAltLogin(this));
			break;
		case 5:
			final Alt randomAlt = TeamBattleClient
					.getAltManager()
					.getContents()
					.get(new Random().nextInt(TeamBattleClient.getAltManager()
							.getContents().size()));
			final String user1 = randomAlt.getUsername();
			final String pass1 = randomAlt.getPassword();
			loginThread = new AltLoginThread(user1, pass1);
			loginThread.start();
			break;
		case 6:
			mc.displayGuiScreen(new GuiRenameAlt(this));
			break;
		case 7:
			final Alt lastAlt = TeamBattleClient.getAltManager().getLastAlt();
			if (lastAlt == null) {
				if (loginThread == null) {
					status = "\247cThere is no last used alt!";
				} else {
					loginThread.setStatus("\247cThere is no last used alt!");
				}
			} else {
				final String user2 = lastAlt.getUsername();
				final String pass2 = lastAlt.getPassword();
				loginThread = new AltLoginThread(user2, pass2);
				loginThread.start();
			}
			break;
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if (Mouse.hasWheel()) {
			final int wheel = Mouse.getDWheel();
			if (wheel < 0) {
				offset += 26;
				if (offset < 0) {
					offset = 0;
				}
			} else if (wheel > 0) {
				offset -= 26;
				if (offset < 0) {
					offset = 0;
				}
			}
		}

		drawDefaultBackground();
		drawString(fontRendererObj, mc.session.getUsername(), 10, 10,
				0xFF888888);
		drawCenteredString(fontRendererObj, "Account Manager - "
				+ TeamBattleClient.getAltManager().getContents().size() + " alts",
				width / 2, 10, 0xFFFFFFFF);
		drawCenteredString(fontRendererObj, loginThread == null ? status
				: loginThread.getStatus(), width / 2, 20, 0xFFFFFFFF);
		RenderHelper.drawBorderedRect(50, 33, width - 50, height - 50, 1,
				0xFF000000, 0x80000000);
		GL11.glPushMatrix();
		prepareScissorBox(0, 33, width, height - 50);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int y = 38;
		for (final Alt alt : TeamBattleClient.getAltManager().getContents()) {
			if (!isAltInArea(y)) {
				continue;
			}
			String name, pass;
			if (alt.getMask().equals("")) {
				name = alt.getUsername();
			} else {
				name = alt.getMask();
			}

			if (alt.getPassword().equals("")) {
				pass = "\247cCracked";
			} else {
				pass = alt.getPassword().replaceAll(".", "*");
			}

			if (alt == selectedAlt) {
				if (isMouseOverAlt(par1, par2, y - offset)
						&& Mouse.isButtonDown(0)) {
					RenderHelper.drawBorderedRect(52, y - offset - 4,
							width - 52, y - offset + 20, 1, 0xFF000000,
							0x80454545);
				} else if (isMouseOverAlt(par1, par2, y - offset)) {
					RenderHelper.drawBorderedRect(52, y - offset - 4,
							width - 52, y - offset + 20, 1, 0xFF000000,
							0x80525252);
				} else {
					RenderHelper.drawBorderedRect(52, y - offset - 4,
							width - 52, y - offset + 20, 1, 0xFF000000,
							0x80313131);
				}
			} else if (isMouseOverAlt(par1, par2, y - offset)
					&& Mouse.isButtonDown(0)) {
				RenderHelper.drawBorderedRect(52, y - offset - 4, width - 52, y
						- offset + 20, 1, 0xFF000000, 0x80151515);
			} else if (isMouseOverAlt(par1, par2, y - offset)) {
				RenderHelper.drawBorderedRect(52, y - offset - 4, width - 52, y
						- offset + 20, 1, 0xFF000000, 0x80232323);
			}

			drawCenteredString(fontRendererObj, name, width / 2, y - offset,
					0xFFFFFFFF);
			drawCenteredString(fontRendererObj, pass, width / 2, y - offset
					+ 10, 5592405);
			y += 26;
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		if (selectedAlt == null) {
			login.enabled = false;
			remove.enabled = false;
			rename.enabled = false;
		} else {
			login.enabled = true;
			remove.enabled = true;
			rename.enabled = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			offset -= 26;
			if (offset < 0) {
				offset = 0;
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			offset += 26;
			if (offset < 0) {
				offset = 0;
			}
		}
	}

	@Override
	public void initGui() {
		this.buttons.add(new GuiButton(0, this.width / 2 + 4 + 76,
				this.height - 24, 75, 20, "Cancel"));
		this.buttons.add(login = new GuiButton(1, this.width / 2 - 154,
				this.height - 48, 100, 20, "Login"));
		this.buttons.add(remove = new GuiButton(2, this.width / 2 - 74,
				this.height - 24, 70, 20, "Remove"));
		this.buttons.add(new GuiButton(3, this.width / 2 + 4 + 50,
				this.height - 48, 100, 20, "Add"));
		this.buttons.add(new GuiButton(4, this.width / 2 - 50,
				this.height - 48, 100, 20, "Direct Login"));
		this.buttons.add(new GuiButton(5, this.width / 2 - 154,
				this.height - 24, 70, 20, "Random"));
		this.buttons.add(rename = new GuiButton(6, this.width / 2 + 4,
				this.height - 24, 70, 20, "Rename"));
		this.buttons.add(new GuiButton(7, this.width / 2 - 206,
				this.height - 24, 50, 20, "Last Alt"));
		login.enabled = false;
		remove.enabled = false;
		rename.enabled = false;
	}

	private boolean isAltInArea(int y) {
		return y - offset <= height - 50;
	}

	private boolean isMouseOverAlt(int x, int y, int y1) {
		return x >= 52 && y >= y1 - 4 && x <= width - 52 && y <= y1 + 20
				&& x >= 0 && y >= 33 && x <= width && y <= height - 50;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (offset < 0) {
			offset = 0;
		}
		int y = 38 - offset;
		for (final Alt alt : TeamBattleClient.getAltManager().getContents()) {
			if (isMouseOverAlt(par1, par2, y)) {
				if (alt == selectedAlt) {
					actionPerformed((GuiButton) buttons.get(1));
					return;
				}
				selectedAlt = alt;
			}
			y += 26;
		}
		super.mouseClicked(par1, par2, par3);
	}

	public void prepareScissorBox(float x, float y, float x2, float y2) {
		final int factor = mc.getScaledResolution().getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((mc.getScaledResolution()
				.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}
}