package down.TeamBattle.Managers.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.reflections.Reflections;

import down.TeamBattle.Managers.ListManager;
import down.TeamBattle.Modules.ModuleBase;
import down.TeamBattle.Utils.Logger;

public final class ModManager extends ListManager<ModuleBase> {
	public final ModuleBase getModByName(String name) {
		if (contents == null)
			return null;
		for (final ModuleBase mod : contents) {
			if (mod.getName().equalsIgnoreCase(name))
				return mod;
		}
		return null;
	}

	@Override
	public void setup() {
		Logger.logConsole("Preparing to load mods...");
		contents = new ArrayList<ModuleBase>();
		final Reflections reflect = new Reflections(ModuleBase.class);
		final Set<Class<? extends ModuleBase>> mods = reflect.getSubTypesOf(ModuleBase.class);
		for (final Class clazz : mods) {
			try {
				final ModuleBase mod = (ModuleBase) clazz.newInstance();
				getContents().add(mod);
				Logger.logConsole("Loaded mod \"" + mod.getName() + "\"");
			} catch (final InstantiationException e) {
				e.printStackTrace();
				Logger.logConsole("Failed to load mod \""
						+ clazz.getSimpleName() + "\" (InstantiationException)");
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
				Logger.logConsole("Failed to load mod \""
						+ clazz.getSimpleName() + "\" (IllegalAccessException)");
			}
		}

		Collections.sort(contents, new Comparator<ModuleBase>() {
			@Override
			public int compare(ModuleBase mod1, ModuleBase mod2) {
				return mod1.getName().compareTo(mod2.getName());
			}
		});
		Logger.logConsole("Successfully loaded " + getContents().size()
				+ " mods.");
	}
}
