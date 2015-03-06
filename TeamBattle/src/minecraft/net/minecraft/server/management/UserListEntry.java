package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry {
	private final Object field_152642_a;

	public UserListEntry(Object p_i1146_1_) {
		field_152642_a = p_i1146_1_;
	}

	protected UserListEntry(Object p_i1147_1_, JsonObject p_i1147_2_) {
		field_152642_a = p_i1147_1_;
	}

	Object func_152640_f() {
		return field_152642_a;
	}

	protected void func_152641_a(JsonObject p_152641_1_) {
	}

	boolean hasBanExpired() {
		return false;
	}
}
