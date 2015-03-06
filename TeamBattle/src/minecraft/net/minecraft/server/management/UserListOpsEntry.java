package net.minecraft.server.management;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOpsEntry extends UserListEntry {
	private static GameProfile func_152643_b(JsonObject p_152643_0_) {
		if (p_152643_0_.has("uuid") && p_152643_0_.has("name")) {
			final String var1 = p_152643_0_.get("uuid").getAsString();
			UUID var2;

			try {
				var2 = UUID.fromString(var1);
			} catch (final Throwable var4) {
				return null;
			}

			return new GameProfile(var2, p_152643_0_.get("name").getAsString());
		} else
			return null;
	}

	private final int field_152645_a;

	public UserListOpsEntry(GameProfile p_i1149_1_, int p_i1149_2_) {
		super(p_i1149_1_);
		field_152645_a = p_i1149_2_;
	}

	public UserListOpsEntry(JsonObject p_i1150_1_) {
		super(func_152643_b(p_i1150_1_), p_i1150_1_);
		field_152645_a = p_i1150_1_.has("level") ? p_i1150_1_.get("level")
				.getAsInt() : 0;
	}

	@Override
	protected void func_152641_a(JsonObject p_152641_1_) {
		if (func_152640_f() != null) {
			p_152641_1_.addProperty("uuid", ((GameProfile) func_152640_f())
					.getId() == null ? "" : ((GameProfile) func_152640_f())
					.getId().toString());
			p_152641_1_.addProperty("name",
					((GameProfile) func_152640_f()).getName());
			super.func_152641_a(p_152641_1_);
			p_152641_1_.addProperty("level", Integer.valueOf(field_152645_a));
		}
	}

	public int func_152644_a() {
		return field_152645_a;
	}
}
