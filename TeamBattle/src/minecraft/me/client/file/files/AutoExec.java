package me.client.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import me.client.Client;
import me.client.file.CustomFile;
import event.events.EventChatSent;

public final class AutoExec extends CustomFile {
	public AutoExec() {
		super("autoexec");
	}

	@Override
	public void loadFile() {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(
					getFile()));
			String line;
			while ((line = reader.readLine()) != null) {
				final EventChatSent sent = new EventChatSent("." + line);
				Client.getEventManager().call(sent);
				sent.checkForCommands();
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
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
