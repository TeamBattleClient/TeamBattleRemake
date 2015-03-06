package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public class NullStream implements IStream {
	private final Throwable field_152938_a;

	public NullStream(Throwable p_i1006_1_) {
		field_152938_a = p_i1006_1_;
	}

	@Override
	public boolean func_152908_z() {
		return false;
	}

	@Override
	public void func_152909_x() {
	}

	@Override
	public void func_152910_a(boolean p_152910_1_) {
	}

	@Override
	public void func_152911_a(Metadata p_152911_1_, long p_152911_2_) {
	}

	@Override
	public ErrorCode func_152912_E() {
		return null;
	}

	@Override
	public boolean func_152913_F() {
		return false;
	}

	@Override
	public void func_152914_u() {
	}

	@Override
	public void func_152915_s() {
	}

	@Override
	public void func_152916_q() {
	}

	@Override
	public void func_152917_b(String p_152917_1_) {
	}

	@Override
	public IStream.AuthFailureReason func_152918_H() {
		return IStream.AuthFailureReason.ERROR;
	}

	@Override
	public boolean func_152919_o() {
		return false;
	}

	@Override
	public int func_152920_A() {
		return 0;
	}

	@Override
	public String func_152921_C() {
		return null;
	}

	@Override
	public void func_152922_k() {
	}

	@Override
	public void func_152923_i() {
	}

	@Override
	public boolean func_152924_m() {
		return false;
	}

	@Override
	public IngestServer[] func_152925_v() {
		return new IngestServer[0];
	}

	@Override
	public ChatUserInfo func_152926_a(String p_152926_1_) {
		return null;
	}

	@Override
	public boolean func_152927_B() {
		return false;
	}

	@Override
	public boolean func_152928_D() {
		return false;
	}

	@Override
	public boolean func_152929_G() {
		return false;
	}

	@Override
	public void func_152930_t() {
	}

	@Override
	public void func_152931_p() {
	}

	@Override
	public IngestServerTester func_152932_y() {
		return null;
	}

	@Override
	public void func_152933_r() {
	}

	@Override
	public boolean func_152934_n() {
		return false;
	}

	@Override
	public void func_152935_j() {
	}

	@Override
	public boolean func_152936_l() {
		return false;
	}

	public Throwable func_152937_a() {
		return field_152938_a;
	}
}
