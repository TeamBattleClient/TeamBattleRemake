package me.client.file;

import java.io.File;

import me.client.Client;

public abstract class CustomFile {
	private final File file;
	private final String name;

	public CustomFile(String name) {
		this.name = name;
		file = new File(Client.getDirectory(), name + ".txt");
		if (!file.exists()) {
			saveFile();
		}
	}

	public final File getFile() {
		return file;
	}

	public final String getName() {
		return name;
	}

	public abstract void loadFile();

	public abstract void saveFile();
}
