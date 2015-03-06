package down.TeamBattle.gUI.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import down.TeamBattle.TeamBattleClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public final class GuiChangelog extends GuiScreen {
	private final List<String> log = new ArrayList<String>();

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		mc.displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		super.drawScreen(x, y, z);
		drawCenteredString(fontRendererObj, "TeamBattle changelog", width / 2, 20,
				0xFFFFFFFF);
		drawCenteredString(fontRendererObj,
				"Build "
						+ (TeamBattleClient.isNewerVersionAvailable() ? "\247c"
								: "\247a") + "#" + TeamBattleClient.getBuild(),
				width / 2, 29, 0xFFFFFFFF);
		int logY = 50;

		for (final String text : log) {
			drawString(fontRendererObj, text, width / 6, logY, 0xFFFFFFFF);
			logY += fontRendererObj.FONT_HEIGHT;
		}

		if (log.isEmpty()) {
			drawString(fontRendererObj, "\247eLoading...", width / 6, logY,
					0xFFFFFFFF);
		}
	}

	@Override
	public void initGui() {
		log.add("/247First version of TEAMBATTLE!!!");
		buttons.clear();
		buttons.add(new GuiButton(0, width / 2 - 100, height - 40, "Back"));
	}
}
