package net.minecraft.client.audio;

import java.util.List;

import com.google.common.collect.Lists;

public class SoundList {
	public static class SoundEntry {
		public static enum Type {
			FILE("FILE", 0, "file"), SOUND_EVENT("SOUND_EVENT", 1, "event");
			public static SoundList.SoundEntry.Type func_148580_a(
					String p_148580_0_) {
				final SoundList.SoundEntry.Type[] var1 = values();
				final int var2 = var1.length;

				for (int var3 = 0; var3 < var2; ++var3) {
					final SoundList.SoundEntry.Type var4 = var1[var3];

					if (var4.field_148583_c.equals(p_148580_0_))
						return var4;
				}

				return null;
			}

			private final String field_148583_c;

			private Type(String p_i45109_1_, int p_i45109_2_, String p_i45109_3_) {
				field_148583_c = p_i45109_3_;
			}
		}

		private boolean field_148564_f;
		private int field_148565_d = 1;
		private SoundList.SoundEntry.Type field_148566_e;
		private float field_148567_b = 1.0F;
		private float field_148568_c = 1.0F;

		private String field_148569_a;

		public SoundEntry() {
			field_148566_e = SoundList.SoundEntry.Type.FILE;
			field_148564_f = false;
		}

		public boolean func_148552_f() {
			return field_148564_f;
		}

		public void func_148553_a(float p_148553_1_) {
			field_148567_b = p_148553_1_;
		}

		public void func_148554_a(int p_148554_1_) {
			field_148565_d = p_148554_1_;
		}

		public int func_148555_d() {
			return field_148565_d;
		}

		public String func_148556_a() {
			return field_148569_a;
		}

		public void func_148557_a(boolean p_148557_1_) {
			field_148564_f = p_148557_1_;
		}

		public float func_148558_b() {
			return field_148567_b;
		}

		public void func_148559_b(float p_148559_1_) {
			field_148568_c = p_148559_1_;
		}

		public float func_148560_c() {
			return field_148568_c;
		}

		public void func_148561_a(String p_148561_1_) {
			field_148569_a = p_148561_1_;
		}

		public void func_148562_a(SoundList.SoundEntry.Type p_148562_1_) {
			field_148566_e = p_148562_1_;
		}

		public SoundList.SoundEntry.Type func_148563_e() {
			return field_148566_e;
		}
	}

	private boolean field_148575_b;
	private SoundCategory field_148576_c;

	private final List field_148577_a = Lists.newArrayList();

	public List func_148570_a() {
		return field_148577_a;
	}

	public void func_148571_a(SoundCategory p_148571_1_) {
		field_148576_c = p_148571_1_;
	}

	public void func_148572_a(boolean p_148572_1_) {
		field_148575_b = p_148572_1_;
	}

	public SoundCategory func_148573_c() {
		return field_148576_c;
	}

	public boolean func_148574_b() {
		return field_148575_b;
	}
}
