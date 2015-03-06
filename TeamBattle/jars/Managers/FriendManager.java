package Managers;

import java.util.HashMap;
import java.util.regex.Matcher;

import me.client.Client;
import net.minecraft.util.StringUtils;

public final class FriendManager extends MapManager<String, String> {

	public void addFriend(String name, String alias) {
		contents.put(name, alias);
	}

	public boolean isFriend(String name) {
		return contents.containsKey(StringUtils.stripControlCodes(name));
	}

	public void removeFriend(String name) {
		contents.remove(name);
	}

	public String replaceNames(String message, boolean color) {
		if (!message.startsWith("\247r\2473[LM]\247f")) {
			for (final String name : contents.keySet()) {
				message = message.replaceAll("(?i)" + name, Matcher
						.quoteReplacement(color ? (Client.getModManager()
								.getModByName("latechat").isEnabled() ? "\247g"
								: "\2473") + contents.get(name) + "\247f"
								: contents.get(name)));
			}
		}
		return message;
	}

	@Override
	public void setup() {
		contents = new HashMap<String, String>();
	}
}
