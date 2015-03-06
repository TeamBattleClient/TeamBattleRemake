package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public interface IStream {
	public static enum AuthFailureReason {
		ERROR("ERROR", 0), INVALID_TOKEN("INVALID_TOKEN", 1);

		private AuthFailureReason(String p_i1014_1_, int p_i1014_2_) {
		}
	}

	boolean func_152908_z();

	void func_152909_x();

	void func_152910_a(boolean p_152910_1_);

	void func_152911_a(Metadata p_152911_1_, long p_152911_2_);

	ErrorCode func_152912_E();

	boolean func_152913_F();

	void func_152914_u();

	void func_152915_s();

	void func_152916_q();

	void func_152917_b(String p_152917_1_);

	IStream.AuthFailureReason func_152918_H();

	boolean func_152919_o();

	int func_152920_A();

	String func_152921_C();

	void func_152922_k();

	void func_152923_i();

	boolean func_152924_m();

	IngestServer[] func_152925_v();

	ChatUserInfo func_152926_a(String p_152926_1_);

	boolean func_152927_B();

	boolean func_152928_D();

	boolean func_152929_G();

	void func_152930_t();

	void func_152931_p();

	IngestServerTester func_152932_y();

	void func_152933_r();

	boolean func_152934_n();

	void func_152935_j();

	boolean func_152936_l();
}
