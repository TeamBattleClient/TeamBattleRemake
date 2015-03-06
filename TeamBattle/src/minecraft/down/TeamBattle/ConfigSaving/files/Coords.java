package down.TeamBattle.ConfigSaving.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.ConfigSaving.CustomFile;
import down.TeamBattle.Modules.Modules.ClaimFinder;

public final class Coords extends CustomFile {
	public Coords() {
		super("coords");
	}

	@Override
	public void loadFile() {
		// no need to load the file as this file will not be used for anything
		// besides saving coordinates
	}

	@Override
	public void saveFile() {
		try {
			final ClaimFinder claimfinder = (ClaimFinder) TeamBattleClient
					.getModManager().getModByName("claimfinder");
			if (claimfinder == null)
				return;
			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					getFile()));
			for (final String line : claimfinder.getMap()) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
