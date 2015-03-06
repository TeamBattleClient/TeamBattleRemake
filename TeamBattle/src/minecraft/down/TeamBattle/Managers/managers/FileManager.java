package down.TeamBattle.Managers.managers;

import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;

import down.TeamBattle.ConfigSaving.CustomFile;
import down.TeamBattle.Managers.ListManager;
import down.TeamBattle.Utils.Logger;

public final class FileManager extends ListManager<CustomFile> {
	public final CustomFile getFileByName(String name) {
		if (contents == null)
			return null;
		for (final CustomFile file : contents) {
			if (file.getName().equalsIgnoreCase(name))
				return file;
		}
		return null;
	}

	@Override
	public void setup() {
		Logger.logConsole("Preparing to load files...");
		contents = new ArrayList<CustomFile>();
		final Reflections reflect = new Reflections(CustomFile.class);
		final Set<Class<? extends CustomFile>> files = reflect
				.getSubTypesOf(CustomFile.class);
		for (final Class clazz : files) {
			try {
				final CustomFile file = (CustomFile) clazz.newInstance();
				getContents().add(file);
				file.loadFile();
				Logger.logConsole("Loaded file \"" + file.getName() + "\"");
			} catch (final InstantiationException e) {
				e.printStackTrace();
				Logger.logConsole("Failed to load file \""
						+ clazz.getSimpleName() + "\" (InstantiationException)");
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
				Logger.logConsole("Failed to load file \""
						+ clazz.getSimpleName() + "\" (IllegalAccessException)");
			}
		}
		Logger.logConsole("Successfully loaded " + getContents().size()
				+ " files.");
	}
}
