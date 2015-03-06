package net.minecraft.client.gui.achievement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiStats extends GuiScreen implements IProgressMeter {
	abstract class Stats extends GuiSlot {
		protected int field_148215_p;
		protected Comparator field_148216_n;
		protected int field_148217_o = -1;
		protected int field_148218_l = -1;
		protected List field_148219_m;

		protected Stats() {
			super(GuiStats.this.mc, GuiStats.this.width, GuiStats.this.height,
					32, GuiStats.this.height - 64, 20);
			func_148130_a(false);
			func_148133_a(true, 20);
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
		}

		@Override
		protected void func_148129_a(int p_148129_1_, int p_148129_2_,
				Tessellator p_148129_3_) {
			if (!Mouse.isButtonDown(0)) {
				field_148218_l = -1;
			}

			if (field_148218_l == 0) {
				func_146527_c(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 0);
			} else {
				func_146527_c(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 0, 18);
			}

			if (field_148218_l == 1) {
				func_146527_c(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 0);
			} else {
				func_146527_c(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 0, 18);
			}

			if (field_148218_l == 2) {
				func_146527_c(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 0);
			} else {
				func_146527_c(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 0, 18);
			}

			if (field_148217_o != -1) {
				short var4 = 79;
				byte var5 = 18;

				if (field_148217_o == 1) {
					var4 = 129;
				} else if (field_148217_o == 2) {
					var4 = 179;
				}

				if (field_148215_p == 1) {
					var5 = 36;
				}

				func_146527_c(p_148129_1_ + var4, p_148129_2_ + 1, var5, 0);
			}
		}

		@Override
		protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
			field_148218_l = -1;

			if (p_148132_1_ >= 79 && p_148132_1_ < 115) {
				field_148218_l = 0;
			} else if (p_148132_1_ >= 129 && p_148132_1_ < 165) {
				field_148218_l = 1;
			} else if (p_148132_1_ >= 179 && p_148132_1_ < 215) {
				field_148218_l = 2;
			}

			if (field_148218_l >= 0) {
				func_148212_h(field_148218_l);
				GuiStats.this.mc.getSoundHandler()
						.playSound(
								PositionedSoundRecord
										.func_147674_a(new ResourceLocation(
												"gui.button.press"), 1.0F));
			}
		}

		@Override
		protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
			if (p_148142_2_ >= field_148153_b && p_148142_2_ <= field_148154_c) {
				final int var3 = func_148124_c(p_148142_1_, p_148142_2_);
				final int var4 = field_148155_a / 2 - 92 - 16;

				if (var3 >= 0) {
					if (p_148142_1_ < var4 + 40 || p_148142_1_ > var4 + 40 + 20)
						return;

					final StatCrafting var5 = func_148211_c(var3);
					func_148213_a(var5, p_148142_1_, p_148142_2_);
				} else {
					String var9 = "";

					if (p_148142_1_ >= var4 + 115 - 18
							&& p_148142_1_ <= var4 + 115) {
						var9 = func_148210_b(0);
					} else if (p_148142_1_ >= var4 + 165 - 18
							&& p_148142_1_ <= var4 + 165) {
						var9 = func_148210_b(1);
					} else {
						if (p_148142_1_ < var4 + 215 - 18
								|| p_148142_1_ > var4 + 215)
							return;

						var9 = func_148210_b(2);
					}

					var9 = ("" + I18n.format(var9, new Object[0])).trim();

					if (var9.length() > 0) {
						final int var6 = p_148142_1_ + 12;
						final int var7 = p_148142_2_ - 12;
						final int var8 = GuiStats.this.fontRendererObj
								.getStringWidth(var9);
						drawGradientRect(var6 - 3, var7 - 3, var6 + var8 + 3,
								var7 + 8 + 3, -1073741824, -1073741824);
						GuiStats.this.fontRendererObj.drawStringWithShadow(
								var9, var6, var7, -1);
					}
				}
			}
		}

		protected void func_148209_a(StatBase p_148209_1_, int p_148209_2_,
				int p_148209_3_, boolean p_148209_4_) {
			String var5;

			if (p_148209_1_ != null) {
				var5 = p_148209_1_.func_75968_a(field_146546_t
						.writeStat(p_148209_1_));
				drawString(GuiStats.this.fontRendererObj, var5, p_148209_2_
						- GuiStats.this.fontRendererObj.getStringWidth(var5),
						p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
			} else {
				var5 = "-";
				drawString(GuiStats.this.fontRendererObj, var5, p_148209_2_
						- GuiStats.this.fontRendererObj.getStringWidth(var5),
						p_148209_3_ + 5, p_148209_4_ ? 16777215 : 9474192);
			}
		}

		protected abstract String func_148210_b(int p_148210_1_);

		protected final StatCrafting func_148211_c(int p_148211_1_) {
			return (StatCrafting) field_148219_m.get(p_148211_1_);
		}

		protected void func_148212_h(int p_148212_1_) {
			if (p_148212_1_ != field_148217_o) {
				field_148217_o = p_148212_1_;
				field_148215_p = -1;
			} else if (field_148215_p == -1) {
				field_148215_p = 1;
			} else {
				field_148217_o = -1;
				field_148215_p = 0;
			}

			Collections.sort(field_148219_m, field_148216_n);
		}

		protected void func_148213_a(StatCrafting p_148213_1_, int p_148213_2_,
				int p_148213_3_) {
			if (p_148213_1_ != null) {
				final Item var4 = p_148213_1_.func_150959_a();
				final String var5 = ("" + I18n.format(var4.getUnlocalizedName()
						+ ".name", new Object[0])).trim();

				if (var5.length() > 0) {
					final int var6 = p_148213_2_ + 12;
					final int var7 = p_148213_3_ - 12;
					final int var8 = GuiStats.this.fontRendererObj
							.getStringWidth(var5);
					drawGradientRect(var6 - 3, var7 - 3, var6 + var8 + 3,
							var7 + 8 + 3, -1073741824, -1073741824);
					GuiStats.this.fontRendererObj.drawStringWithShadow(var5,
							var6, var7, -1);
				}
			}
		}

		@Override
		protected final int getSize() {
			return field_148219_m.size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
	}

	class StatsBlock extends GuiStats.Stats {

		public StatsBlock() {
			field_148219_m = new ArrayList();
			final Iterator var2 = StatList.objectMineStats.iterator();

			while (var2.hasNext()) {
				final StatCrafting var3 = (StatCrafting) var2.next();
				boolean var4 = false;
				final int var5 = Item.getIdFromItem(var3.func_150959_a());

				if (field_146546_t.writeStat(var3) > 0) {
					var4 = true;
				} else if (StatList.objectUseStats[var5] != null
						&& field_146546_t
								.writeStat(StatList.objectUseStats[var5]) > 0) {
					var4 = true;
				} else if (StatList.objectCraftStats[var5] != null
						&& field_146546_t
								.writeStat(StatList.objectCraftStats[var5]) > 0) {
					var4 = true;
				}

				if (var4) {
					field_148219_m.add(var3);
				}
			}

			field_148216_n = new Comparator() {

				@Override
				public int compare(Object p_compare_1_, Object p_compare_2_) {
					return this.compare((StatCrafting) p_compare_1_,
							(StatCrafting) p_compare_2_);
				}

				public int compare(StatCrafting p_compare_1_,
						StatCrafting p_compare_2_) {
					final int var3 = Item.getIdFromItem(p_compare_1_
							.func_150959_a());
					final int var4 = Item.getIdFromItem(p_compare_2_
							.func_150959_a());
					StatBase var5 = null;
					StatBase var6 = null;

					if (StatsBlock.this.field_148217_o == 2) {
						var5 = StatList.mineBlockStatArray[var3];
						var6 = StatList.mineBlockStatArray[var4];
					} else if (StatsBlock.this.field_148217_o == 0) {
						var5 = StatList.objectCraftStats[var3];
						var6 = StatList.objectCraftStats[var4];
					} else if (StatsBlock.this.field_148217_o == 1) {
						var5 = StatList.objectUseStats[var3];
						var6 = StatList.objectUseStats[var4];
					}

					if (var5 != null || var6 != null) {
						if (var5 == null)
							return 1;

						if (var6 == null)
							return -1;

						final int var7 = field_146546_t.writeStat(var5);
						final int var8 = field_146546_t.writeStat(var6);

						if (var7 != var8)
							return (var7 - var8)
									* StatsBlock.this.field_148215_p;
					}

					return var3 - var4;
				}
			};
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			final StatCrafting var8 = func_148211_c(p_148126_1_);
			final Item var9 = var8.func_150959_a();
			func_146521_a(p_148126_2_ + 40, p_148126_3_, var9);
			final int var10 = Item.getIdFromItem(var9);
			func_148209_a(StatList.objectCraftStats[var10], p_148126_2_ + 115,
					p_148126_3_, p_148126_1_ % 2 == 0);
			func_148209_a(StatList.objectUseStats[var10], p_148126_2_ + 165,
					p_148126_3_, p_148126_1_ % 2 == 0);
			func_148209_a(var8, p_148126_2_ + 215, p_148126_3_,
					p_148126_1_ % 2 == 0);
		}

		@Override
		protected void func_148129_a(int p_148129_1_, int p_148129_2_,
				Tessellator p_148129_3_) {
			super.func_148129_a(p_148129_1_, p_148129_2_, p_148129_3_);

			if (field_148218_l == 0) {
				func_146527_c(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1,
						18, 18);
			} else {
				func_146527_c(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 18, 18);
			}

			if (field_148218_l == 1) {
				func_146527_c(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1,
						36, 18);
			} else {
				func_146527_c(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 36, 18);
			}

			if (field_148218_l == 2) {
				func_146527_c(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1,
						54, 18);
			} else {
				func_146527_c(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 54, 18);
			}
		}

		@Override
		protected String func_148210_b(int p_148210_1_) {
			return p_148210_1_ == 0 ? "stat.crafted"
					: p_148210_1_ == 1 ? "stat.used" : "stat.mined";
		}
	}

	class StatsGeneral extends GuiSlot {

		public StatsGeneral() {
			super(GuiStats.this.mc, GuiStats.this.width, GuiStats.this.height,
					32, GuiStats.this.height - 64, 10);
			func_148130_a(false);
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			final StatBase var8 = (StatBase) StatList.generalStats
					.get(p_148126_1_);
			drawString(GuiStats.this.fontRendererObj, var8.func_150951_e()
					.getUnformattedText(), p_148126_2_ + 2, p_148126_3_ + 1,
					p_148126_1_ % 2 == 0 ? 16777215 : 9474192);
			final String var9 = var8.func_75968_a(field_146546_t
					.writeStat(var8));
			drawString(GuiStats.this.fontRendererObj, var9, p_148126_2_ + 2
					+ 213 - GuiStats.this.fontRendererObj.getStringWidth(var9),
					p_148126_3_ + 1, p_148126_1_ % 2 == 0 ? 16777215 : 9474192);
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
		}

		@Override
		protected int func_148138_e() {
			return getSize() * 10;
		}

		@Override
		protected int getSize() {
			return StatList.generalStats.size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
	}

	class StatsItem extends GuiStats.Stats {

		public StatsItem() {
			field_148219_m = new ArrayList();
			final Iterator var2 = StatList.itemStats.iterator();

			while (var2.hasNext()) {
				final StatCrafting var3 = (StatCrafting) var2.next();
				boolean var4 = false;
				final int var5 = Item.getIdFromItem(var3.func_150959_a());

				if (field_146546_t.writeStat(var3) > 0) {
					var4 = true;
				} else if (StatList.objectBreakStats[var5] != null
						&& field_146546_t
								.writeStat(StatList.objectBreakStats[var5]) > 0) {
					var4 = true;
				} else if (StatList.objectCraftStats[var5] != null
						&& field_146546_t
								.writeStat(StatList.objectCraftStats[var5]) > 0) {
					var4 = true;
				}

				if (var4) {
					field_148219_m.add(var3);
				}
			}

			field_148216_n = new Comparator() {

				@Override
				public int compare(Object p_compare_1_, Object p_compare_2_) {
					return this.compare((StatCrafting) p_compare_1_,
							(StatCrafting) p_compare_2_);
				}

				public int compare(StatCrafting p_compare_1_,
						StatCrafting p_compare_2_) {
					final int var3 = Item.getIdFromItem(p_compare_1_
							.func_150959_a());
					final int var4 = Item.getIdFromItem(p_compare_2_
							.func_150959_a());
					StatBase var5 = null;
					StatBase var6 = null;

					if (StatsItem.this.field_148217_o == 0) {
						var5 = StatList.objectBreakStats[var3];
						var6 = StatList.objectBreakStats[var4];
					} else if (StatsItem.this.field_148217_o == 1) {
						var5 = StatList.objectCraftStats[var3];
						var6 = StatList.objectCraftStats[var4];
					} else if (StatsItem.this.field_148217_o == 2) {
						var5 = StatList.objectUseStats[var3];
						var6 = StatList.objectUseStats[var4];
					}

					if (var5 != null || var6 != null) {
						if (var5 == null)
							return 1;

						if (var6 == null)
							return -1;

						final int var7 = field_146546_t.writeStat(var5);
						final int var8 = field_146546_t.writeStat(var6);

						if (var7 != var8)
							return (var7 - var8)
									* StatsItem.this.field_148215_p;
					}

					return var3 - var4;
				}
			};
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			final StatCrafting var8 = func_148211_c(p_148126_1_);
			final Item var9 = var8.func_150959_a();
			func_146521_a(p_148126_2_ + 40, p_148126_3_, var9);
			final int var10 = Item.getIdFromItem(var9);
			func_148209_a(StatList.objectBreakStats[var10], p_148126_2_ + 115,
					p_148126_3_, p_148126_1_ % 2 == 0);
			func_148209_a(StatList.objectCraftStats[var10], p_148126_2_ + 165,
					p_148126_3_, p_148126_1_ % 2 == 0);
			func_148209_a(var8, p_148126_2_ + 215, p_148126_3_,
					p_148126_1_ % 2 == 0);
		}

		@Override
		protected void func_148129_a(int p_148129_1_, int p_148129_2_,
				Tessellator p_148129_3_) {
			super.func_148129_a(p_148129_1_, p_148129_2_, p_148129_3_);

			if (field_148218_l == 0) {
				func_146527_c(p_148129_1_ + 115 - 18 + 1, p_148129_2_ + 1 + 1,
						72, 18);
			} else {
				func_146527_c(p_148129_1_ + 115 - 18, p_148129_2_ + 1, 72, 18);
			}

			if (field_148218_l == 1) {
				func_146527_c(p_148129_1_ + 165 - 18 + 1, p_148129_2_ + 1 + 1,
						18, 18);
			} else {
				func_146527_c(p_148129_1_ + 165 - 18, p_148129_2_ + 1, 18, 18);
			}

			if (field_148218_l == 2) {
				func_146527_c(p_148129_1_ + 215 - 18 + 1, p_148129_2_ + 1 + 1,
						36, 18);
			} else {
				func_146527_c(p_148129_1_ + 215 - 18, p_148129_2_ + 1, 36, 18);
			}
		}

		@Override
		protected String func_148210_b(int p_148210_1_) {
			return p_148210_1_ == 1 ? "stat.crafted"
					: p_148210_1_ == 2 ? "stat.used" : "stat.depleted";
		}
	}

	class StatsMobsList extends GuiSlot {
		private final List field_148222_l = new ArrayList();

		public StatsMobsList() {
			super(GuiStats.this.mc, GuiStats.this.width, GuiStats.this.height,
					32, GuiStats.this.height - 64,
					GuiStats.this.fontRendererObj.FONT_HEIGHT * 4);
			func_148130_a(false);
			final Iterator var2 = EntityList.entityEggs.values().iterator();

			while (var2.hasNext()) {
				final EntityList.EntityEggInfo var3 = (EntityList.EntityEggInfo) var2
						.next();

				if (field_146546_t.writeStat(var3.field_151512_d) > 0
						|| field_146546_t.writeStat(var3.field_151513_e) > 0) {
					field_148222_l.add(var3);
				}
			}
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			final EntityList.EntityEggInfo var8 = (EntityList.EntityEggInfo) field_148222_l
					.get(p_148126_1_);
			final String var9 = I18n.format(
					"entity." + EntityList.getStringFromID(var8.spawnedID)
							+ ".name", new Object[0]);
			final int var10 = field_146546_t.writeStat(var8.field_151512_d);
			final int var11 = field_146546_t.writeStat(var8.field_151513_e);
			String var12 = I18n.format("stat.entityKills", new Object[] {
					Integer.valueOf(var10), var9 });
			String var13 = I18n.format("stat.entityKilledBy", new Object[] {
					var9, Integer.valueOf(var11) });

			if (var10 == 0) {
				var12 = I18n.format("stat.entityKills.none",
						new Object[] { var9 });
			}

			if (var11 == 0) {
				var13 = I18n.format("stat.entityKilledBy.none",
						new Object[] { var9 });
			}

			drawString(GuiStats.this.fontRendererObj, var9,
					p_148126_2_ + 2 - 10, p_148126_3_ + 1, 16777215);
			drawString(
					GuiStats.this.fontRendererObj,
					var12,
					p_148126_2_ + 2,
					p_148126_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT,
					var10 == 0 ? 6316128 : 9474192);
			drawString(GuiStats.this.fontRendererObj, var13, p_148126_2_ + 2,
					p_148126_3_ + 1 + GuiStats.this.fontRendererObj.FONT_HEIGHT
							* 2, var11 == 0 ? 6316128 : 9474192);
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
		}

		@Override
		protected int func_148138_e() {
			return getSize() * GuiStats.this.fontRendererObj.FONT_HEIGHT * 4;
		}

		@Override
		protected int getSize() {
			return field_148222_l.size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
	}

	private static RenderItem field_146544_g = new RenderItem();
	protected String field_146542_f = "Select world";
	private boolean field_146543_v = true;
	private GuiSlot field_146545_u;
	private final StatFileWriter field_146546_t;

	private GuiStats.StatsMobsList field_146547_s;

	private GuiStats.StatsBlock field_146548_r;

	protected GuiScreen field_146549_a;

	private GuiStats.StatsGeneral field_146550_h;

	private GuiStats.StatsItem field_146551_i;

	public GuiStats(GuiScreen p_i1071_1_, StatFileWriter p_i1071_2_) {
		field_146549_a = p_i1071_1_;
		field_146546_t = p_i1071_2_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				mc.displayGuiScreen(field_146549_a);
			} else if (p_146284_1_.id == 1) {
				field_146545_u = field_146550_h;
			} else if (p_146284_1_.id == 3) {
				field_146545_u = field_146551_i;
			} else if (p_146284_1_.id == 2) {
				field_146545_u = field_146548_r;
			} else if (p_146284_1_.id == 4) {
				field_146545_u = field_146547_s;
			} else {
				field_146545_u.func_148147_a(p_146284_1_);
			}
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return !field_146543_v;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (field_146543_v) {
			drawDefaultBackground();
			drawCenteredString(fontRendererObj,
					I18n.format("multiplayer.downloadingStats", new Object[0]),
					width / 2, height / 2, 16777215);
			drawCenteredString(
					fontRendererObj,
					field_146510_b_[(int) (Minecraft.getSystemTime() / 150L % field_146510_b_.length)],
					width / 2, height / 2 + fontRendererObj.FONT_HEIGHT * 2,
					16777215);
		} else {
			field_146545_u.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
			drawCenteredString(fontRendererObj, field_146542_f, width / 2, 20,
					16777215);
			super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		}
	}

	@Override
	public void func_146509_g() {
		if (field_146543_v) {
			field_146550_h = new GuiStats.StatsGeneral();
			field_146550_h.func_148134_d(1, 1);
			field_146551_i = new GuiStats.StatsItem();
			field_146551_i.func_148134_d(1, 1);
			field_146548_r = new GuiStats.StatsBlock();
			field_146548_r.func_148134_d(1, 1);
			field_146547_s = new GuiStats.StatsMobsList();
			field_146547_s.func_148134_d(1, 1);
			field_146545_u = field_146550_h;
			func_146541_h();
			field_146543_v = false;
		}
	}

	private void func_146521_a(int p_146521_1_, int p_146521_2_,
			Item p_146521_3_) {
		func_146531_b(p_146521_1_ + 1, p_146521_2_ + 1);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		field_146544_g.renderItemIntoGUI(fontRendererObj,
				mc.getTextureManager(), new ItemStack(p_146521_3_, 1, 0),
				p_146521_1_ + 2, p_146521_2_ + 2);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	private void func_146527_c(int p_146527_1_, int p_146527_2_,
			int p_146527_3_, int p_146527_4_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(statIcons);
		final Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(p_146527_1_ + 0, p_146527_2_ + 18, zLevel,
				(p_146527_3_ + 0) * 0.0078125F, (p_146527_4_ + 18) * 0.0078125F);
		var9.addVertexWithUV(p_146527_1_ + 18, p_146527_2_ + 18, zLevel,
				(p_146527_3_ + 18) * 0.0078125F,
				(p_146527_4_ + 18) * 0.0078125F);
		var9.addVertexWithUV(p_146527_1_ + 18, p_146527_2_ + 0, zLevel,
				(p_146527_3_ + 18) * 0.0078125F, (p_146527_4_ + 0) * 0.0078125F);
		var9.addVertexWithUV(p_146527_1_ + 0, p_146527_2_ + 0, zLevel,
				(p_146527_3_ + 0) * 0.0078125F, (p_146527_4_ + 0) * 0.0078125F);
		var9.draw();
	}

	private void func_146531_b(int p_146531_1_, int p_146531_2_) {
		func_146527_c(p_146531_1_, p_146531_2_, 0, 0);
	}

	public void func_146541_h() {
		buttons.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, I18n
				.format("gui.done", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 - 160, height - 52, 80, 20, I18n
				.format("stat.generalButton", new Object[0])));
		GuiButton var1;
		GuiButton var2;
		GuiButton var3;
		buttons.add(var1 = new GuiButton(2, width / 2 - 80, height - 52, 80,
				20, I18n.format("stat.blocksButton", new Object[0])));
		buttons.add(var2 = new GuiButton(3, width / 2, height - 52, 80, 20,
				I18n.format("stat.itemsButton", new Object[0])));
		buttons.add(var3 = new GuiButton(4, width / 2 + 80, height - 52, 80,
				20, I18n.format("stat.mobsButton", new Object[0])));

		if (field_146548_r.getSize() == 0) {
			var1.enabled = false;
		}

		if (field_146551_i.getSize() == 0) {
			var2.enabled = false;
		}

		if (field_146547_s.getSize() == 0) {
			var3.enabled = false;
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		field_146542_f = I18n.format("gui.stats", new Object[0]);
		mc.getNetHandler().addToSendQueue(
				new C16PacketClientStatus(
						C16PacketClientStatus.EnumState.REQUEST_STATS));
	}
}
