package down.TeamBattle.ConfigSaving.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.ConfigSaving.CustomFile;
import down.TeamBattle.Modules.Modules.Macros;
import down.TeamBattle.Modules.Modules.addons.Macro;

public final class MacrosFile extends CustomFile {
	public MacrosFile() {
		super("macros");
	}

	@Override
	public void loadFile() {
		try {
			final Macros macros = (Macros) TeamBattleClient.getModManager()
					.getModByName("macros");
			if (macros == null)
				return;
			final BufferedReader reader = new BufferedReader(new FileReader(
					getFile()));
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] arguments = line.split(":");
				if (arguments.length == 2) {
					final String command = arguments[0];
					final int key = Keyboard.getKeyIndex(arguments[1]);
					macros.getMacros().add(new Macro(command, key));
				}
			}
			reader.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveFile() {
		try {
			final Macros macros = (Macros) TeamBattleClient.getModManager()
					.getModByName("macros");
			if (macros == null)
				return;
			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					getFile()));
			for (final Macro macro : macros.getMacros()) {
				final String command = macro.getCommand();
				final String key = Keyboard.getKeyName(macro.getKey());
				writer.write(command + ":" + key);
				writer.newLine();
			}
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
