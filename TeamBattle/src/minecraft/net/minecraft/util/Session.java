package net.minecraft.util;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;

public class Session {
	public static enum Type {
		LEGACY("LEGACY", 0, "legacy"), MOJANG("MOJANG", 1, "mojang");
		private static final Map field_152425_c = Maps.newHashMap();

		static {
			final Session.Type[] var0 = values();
			final int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				final Session.Type var3 = var0[var2];
				field_152425_c.put(var3.field_152426_d, var3);
			}
		}

		public static Session.Type func_152421_a(String p_152421_0_) {
			return (Session.Type) field_152425_c.get(p_152421_0_.toLowerCase());
		}

		private final String field_152426_d;

		private Type(String p_i1096_1_, int p_i1096_2_, String p_i1096_3_) {
			field_152426_d = p_i1096_3_;
		}
	}

	private final Session.Type field_152429_d;
	private final String playerID;
	private final String token;

	private final String username;

	public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_,
			String p_i1098_4_) {
		username = p_i1098_1_;
		playerID = p_i1098_2_;
		token = p_i1098_3_;
		field_152429_d = Session.Type.func_152421_a(p_i1098_4_);
	}

	public GameProfile func_148256_e() {
		try {
			final UUID var1 = UUIDTypeAdapter.fromString(getPlayerID());
			return new GameProfile(var1, getUsername());
		} catch (final IllegalArgumentException var2) {
			return new GameProfile((UUID) null, getUsername());
		}
	}

	public Session.Type func_152428_f() {
		return field_152429_d;
	}

	public String getPlayerID() {
		return playerID;
	}

	public String getSessionID() {
		return "token:" + token + ":" + playerID;
	}

	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}
}
