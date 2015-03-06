package me.client.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import me.client.Client;
import me.client.file.CustomFile;
import me.client.modules.Module;

import org.lwjgl.input.Keyboard;

public final class ModConfig extends CustomFile {
	public ModConfig() {
		super("modconfig");
	}

	@Override
	public void loadFile() {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(
					getFile()));
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] arguments = line.split(":");
				if (arguments.length == 4) {
					final Module mod = Client.getModManager().getModByName(
							arguments[0]);
					if (mod != null) {
						mod.setEnabled(Boolean.parseBoolean(arguments[1]));
						mod.setKeybind(Keyboard.getKeyIndex(arguments[2]
								.toUpperCase()));
						mod.setVisible(Boolean.parseBoolean(arguments[3]));
					}
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
			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					getFile()));
			for (final Module mod : Client.getModManager().getContents()) {
				writer.write(mod.getName().toLowerCase() + ":"
						+ mod.isEnabled() + ":"
						+ Keyboard.getKeyName(mod.getKeybind()) + ":"
						+ mod.isVisible());
				writer.newLine();
			}
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
