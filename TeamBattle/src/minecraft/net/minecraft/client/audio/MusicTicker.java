package net.minecraft.client.audio;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker implements IUpdatePlayerListBox {
	public static enum MusicType {
		CREATIVE("CREATIVE", 2, new ResourceLocation(
				"minecraft:music.game.creative"), 1200, 3600), CREDITS(
				"CREDITS", 3, new ResourceLocation(
						"minecraft:music.game.end.credits"), Integer.MAX_VALUE,
				Integer.MAX_VALUE), END("END", 6, new ResourceLocation(
				"minecraft:music.game.end"), 6000, 24000), END_BOSS("END_BOSS",
				5, new ResourceLocation("minecraft:music.game.end.dragon"), 0,
				0), GAME("GAME", 1,
				new ResourceLocation("minecraft:music.game"), 12000, 24000), MENU(
				"MENU", 0, new ResourceLocation("minecraft:music.menu"), 20,
				600), NETHER("NETHER", 4, new ResourceLocation(
				"minecraft:music.game.nether"), 1200, 3600);
		private final int field_148643_j;
		private final ResourceLocation field_148645_h;
		private final int field_148646_i;

		private MusicType(String p_i45111_1_, int p_i45111_2_,
				ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_) {
			field_148645_h = p_i45111_3_;
			field_148646_i = p_i45111_4_;
			field_148643_j = p_i45111_5_;
		}

		public int func_148633_c() {
			return field_148643_j;
		}

		public int func_148634_b() {
			return field_148646_i;
		}

		public ResourceLocation func_148635_a() {
			return field_148645_h;
		}
	}

	private int field_147676_d = 100;
	private final Minecraft field_147677_b;
	private ISound field_147678_c;

	private final Random field_147679_a = new Random();

	public MusicTicker(Minecraft p_i45112_1_) {
		field_147677_b = p_i45112_1_;
	}

	/**
	 * Updates the JList with a new model.
	 */
	@Override
	public void update() {
		final MusicTicker.MusicType var1 = field_147677_b.func_147109_W();

		if (field_147678_c != null) {
			if (!var1.func_148635_a().equals(field_147678_c.func_147650_b())) {
				field_147677_b.getSoundHandler().func_147683_b(field_147678_c);
				field_147676_d = MathHelper.getRandomIntegerInRange(
						field_147679_a, 0, var1.func_148634_b() / 2);
			}

			if (!field_147677_b.getSoundHandler().func_147692_c(field_147678_c)) {
				field_147678_c = null;
				field_147676_d = Math.min(
						MathHelper.getRandomIntegerInRange(field_147679_a,
								var1.func_148634_b(), var1.func_148633_c()),
						field_147676_d);
			}
		}

		if (field_147678_c == null && field_147676_d-- <= 0) {
			field_147678_c = PositionedSoundRecord.func_147673_a(var1
					.func_148635_a());
			field_147677_b.getSoundHandler().playSound(field_147678_c);
			field_147676_d = Integer.MAX_VALUE;
		}
	}
}
