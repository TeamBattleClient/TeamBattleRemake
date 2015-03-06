package net.minecraft.server.management;

import java.io.File;
import java.net.SocketAddress;

import com.google.gson.JsonObject;

public class BanList extends UserList {

	public BanList(File p_i1490_1_) {
		super(p_i1490_1_);
	}

	@Override
	protected UserListEntry func_152682_a(JsonObject p_152682_1_) {
		return new IPBanEntry(p_152682_1_);
	}

	private String func_152707_c(SocketAddress p_152707_1_) {
		String var2 = p_152707_1_.toString();

		if (var2.contains("/")) {
			var2 = var2.substring(var2.indexOf(47) + 1);
		}

		if (var2.contains(":")) {
			var2 = var2.substring(0, var2.indexOf(58));
		}

		return var2;
	}

	public boolean func_152708_a(SocketAddress p_152708_1_) {
		final String var2 = func_152707_c(p_152708_1_);
		return func_152692_d(var2);
	}

	public IPBanEntry func_152709_b(SocketAddress p_152709_1_) {
		final String var2 = func_152707_c(p_152709_1_);
		return (IPBanEntry) func_152683_b(var2);
	}
}
