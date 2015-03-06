package Managers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StringUtils;

public class AdminManager extends ListManager<String> {
	public void addAdmin(String admin) {
		if (contents.contains(admin))
			return;
		contents.add(admin);
	}

	public List<String> getAdmins() {
		return this.contents;
	}

	public boolean isAdmin(String admin) {
		return contents.contains(StringUtils.stripControlCodes(admin));
	}

	public void removeAdmin(String admin) {
		if (contents.contains(admin))
			return;
		contents.remove(contents.indexOf(admin));
	}

	@Override
	public void setup() {
		contents = new ArrayList<String>();
	}

}
