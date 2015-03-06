package me.client.Threads;

import java.net.Proxy;

import me.client.Client;
import me.client.alts.Alt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class AltLoginThread extends Thread {
	private final Minecraft mc = Minecraft.getMinecraft();
	private final String password;
	private String status;
	private final String username;

	public AltLoginThread(final String username, final String password) {
		super("Alt Login Thread");
		this.username = username;
		this.password = password;
		status = "\2477Waiting...";
	}

	private final Session createSession(String username, String password) {
		final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(
				Proxy.NO_PROXY, "");
		final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
				.createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername(username);
		auth.setPassword(password);
		try {
			auth.logIn();
			return new Session(auth.getSelectedProfile().getName(), auth
					.getSelectedProfile().getId().toString(),
					auth.getAuthenticatedToken(), "mojang");
		} catch (final AuthenticationException e) {
			return null;
		}
	}

	public String getStatus() {
		return status;
	}

	@Override
	public void run() {
		if (password.equals("")) {
			mc.session = new Session(username, "", "", "mojang");
			status = "\247aLogged in. (" + username + " - offline name)";
			return;
		}
		status = "\247eLogging in...";
		final Session auth = createSession(username, password);
		if (auth == null) {
			status = "\247cLogin failed!";
		} else {
			Client.getAltManager().setLastAlt(new Alt(username, password));
			Client.getFileManager().getFileByName("lastalt").saveFile();
			status = "\247aLogged in. (" + auth.getUsername() + ")";
			mc.session = auth;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}
}