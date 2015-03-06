package down.TeamBattle.ConfigSaving.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.ConfigSaving.CustomFile;

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
				TeamBattleClient.getFriendManager()
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
			for (final String name : TeamBattleClient.getFriendManager().getContents()
					.keySet()) {
				writer.write(name + ":"
						+ TeamBattleClient.getFriendManager().getContents().get(name));
				writer.newLine();
			}
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
