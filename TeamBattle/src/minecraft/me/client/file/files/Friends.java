package me.client.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import me.client.Client;
import me.client.file.CustomFile;

public final class Friends extends CustomFile {
	public Friends() {
		super("friends");
	}

	@Override
	public void loadFile() {
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(
					getFile()));
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] arguments = line.split(":");
				Client.getFriendManager()
						.addFriend(arguments[0], arguments[1]);
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
			for (final String name : Client.getFriendManager().getContents()
					.keySet()) {
				writer.write(name + ":"
						+ Client.getFriendManager().getContents().get(name));
				writer.newLine();
			}
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
