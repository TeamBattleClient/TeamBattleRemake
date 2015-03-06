package net.minecraft.client.gui.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiContainerCreative extends InventoryEffectRenderer {
	static class ContainerCreative extends Container {
		public List field_148330_a = new ArrayList();

		public ContainerCreative(EntityPlayer p_i1086_1_) {
			final InventoryPlayer var2 = p_i1086_1_.inventory;
			int var3;

			for (var3 = 0; var3 < 5; ++var3) {
				for (int var4 = 0; var4 < 9; ++var4) {
					addSlotToContainer(new Slot(
							GuiContainerCreative.field_147060_v, var3 * 9
									+ var4, 9 + var4 * 18, 18 + var3 * 18));
				}
			}

			for (var3 = 0; var3 < 9; ++var3) {
				addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
			}

			func_148329_a(0.0F);
		}

		@Override
		public boolean canDragIntoSlot(Slot p_94531_1_) {
			return p_94531_1_.inventory instanceof InventoryPlayer
					|| p_94531_1_.yDisplayPosition > 90
					&& p_94531_1_.xDisplayPosition <= 162;
		}

		@Override
		public boolean canInteractWith(EntityPlayer p_75145_1_) {
			return true;
		}

		public boolean func_148328_e() {
			return field_148330_a.size() > 45;
		}

		public void func_148329_a(float p_148329_1_) {
			final int var2 = field_148330_a.size() / 9 - 5 + 1;
			int var3 = (int) (p_148329_1_ * var2 + 0.5D);

			if (var3 < 0) {
				var3 = 0;
			}

			for (int var4 = 0; var4 < 5; ++var4) {
				for (int var5 = 0; var5 < 9; ++var5) {
					final int var6 = var5 + (var4 + var3) * 9;

					if (var6 >= 0 && var6 < field_148330_a.size()) {
						GuiContainerCreative.field_147060_v
								.setInventorySlotContents(var5 + var4 * 9,
										(ItemStack) field_148330_a.get(var6));
					} else {
						GuiContainerCreative.field_147060_v
								.setInventorySlotContents(var5 + var4 * 9,
										(ItemStack) null);
					}
				}
			}
		}

		@Override
		public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
			return p_94530_2_.yDisplayPosition > 90;
		}

		@Override
		protected void retrySlotClick(int p_75133_1_, int p_75133_2_,
				boolean p_75133_3_, EntityPlayer p_75133_4_) {
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer p_82846_1_,
				int p_82846_2_) {
			if (p_82846_2_ >= inventorySlots.size() - 9
					&& p_82846_2_ < inventorySlots.size()) {
				final Slot var3 = (Slot) inventorySlots.get(p_82846_2_);

				if (var3 != null && var3.getHasStack()) {
					var3.putStack((ItemStack) null);
				}
			}

			return null;
		}
	}

	class CreativeSlot extends Slot {
		private final Slot field_148332_b;

		public CreativeSlot(Slot p_i1087_2_, int p_i1087_3_) {
			super(p_i1087_2_.inventory, p_i1087_3_, 0, 0);
			field_148332_b = p_i1087_2_;
		}

		@Override
		public ItemStack decrStackSize(int p_75209_1_) {
			return field_148332_b.decrStackSize(p_75209_1_);
		}

		@Override
		public IIcon getBackgroundIconIndex() {
			return field_148332_b.getBackgroundIconIndex();
		}

		@Override
		public boolean getHasStack() {
			return field_148332_b.getHasStack();
		}

		@Override
		public int getSlotStackLimit() {
			return field_148332_b.getSlotStackLimit();
		}

		@Override
		public ItemStack getStack() {
			return field_148332_b.getStack();
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return field_148332_b.isItemValid(p_75214_1_);
		}

		@Override
		public boolean isSlotInInventory(IInventory p_75217_1_, int p_75217_2_) {
			return field_148332_b.isSlotInInventory(p_75217_1_, p_75217_2_);
		}

		@Override
		public void onPickupFromSlot(EntityPlayer p_82870_1_,
				ItemStack p_82870_2_) {
			field_148332_b.onPickupFromSlot(p_82870_1_, p_82870_2_);
		}

		@Override
		public void onSlotChanged() {
			field_148332_b.onSlotChanged();
		}

		@Override
		public void putStack(ItemStack p_75215_1_) {
			field_148332_b.putStack(p_75215_1_);
		}
	}

	private static int field_147058_w = CreativeTabs.tabBlock.getTabIndex();
	private static InventoryBasic field_147060_v = new InventoryBasic("tmp",
			true, 45);
	private static final ResourceLocation field_147061_u = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");
	private boolean field_147057_D;
	private CreativeCrafting field_147059_E;
	private GuiTextField field_147062_A;
	private List field_147063_B;
	private Slot field_147064_C;
	private boolean field_147065_z;

	private boolean field_147066_y;

	private float field_147067_x;

	public GuiContainerCreative(EntityPlayer p_i1088_1_) {
		super(new GuiContainerCreative.ContainerCreative(p_i1088_1_));
		p_i1088_1_.openContainer = field_147002_h;
		field_146291_p = true;
		field_147000_g = 136;
		field_146999_f = 195;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer
					.func_146107_m()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.func_146107_m()));
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		final boolean var4 = Mouse.isButtonDown(0);
		final int var5 = field_147003_i;
		final int var6 = field_147009_r;
		final int var7 = var5 + 175;
		final int var8 = var6 + 18;
		final int var9 = var7 + 14;
		final int var10 = var8 + 112;

		if (!field_147065_z && var4 && p_73863_1_ >= var7 && p_73863_2_ >= var8
				&& p_73863_1_ < var9 && p_73863_2_ < var10) {
			field_147066_y = func_147055_p();
		}

		if (!var4) {
			field_147066_y = false;
		}

		field_147065_z = var4;

		if (field_147066_y) {
			field_147067_x = (p_73863_2_ - var8 - 7.5F)
					/ (var10 - var8 - 15.0F);

			if (field_147067_x < 0.0F) {
				field_147067_x = 0.0F;
			}

			if (field_147067_x > 1.0F) {
				field_147067_x = 1.0F;
			}

			((GuiContainerCreative.ContainerCreative) field_147002_h)
					.func_148329_a(field_147067_x);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		final CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
		final int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			final CreativeTabs var14 = var11[var13];

			if (func_147052_b(var14, p_73863_1_, p_73863_2_)) {
				break;
			}
		}

		if (field_147064_C != null
				&& field_147058_w == CreativeTabs.tabInventory.getTabIndex()
				&& func_146978_c(field_147064_C.xDisplayPosition,
						field_147064_C.yDisplayPosition, 16, 16, p_73863_1_,
						p_73863_2_)) {
			func_146279_a(I18n.format("inventory.binSlot", new Object[0]),
					p_73863_1_, p_73863_2_);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	protected void func_146285_a(ItemStack p_146285_1_, int p_146285_2_,
			int p_146285_3_) {
		if (field_147058_w == CreativeTabs.tabAllSearch.getTabIndex()) {
			final List var4 = p_146285_1_.getTooltip(mc.thePlayer,
					mc.gameSettings.advancedItemTooltips);
			CreativeTabs var5 = p_146285_1_.getItem().getCreativeTab();

			if (var5 == null && p_146285_1_.getItem() == Items.enchanted_book) {
				final Map var6 = EnchantmentHelper.getEnchantments(p_146285_1_);

				if (var6.size() == 1) {
					final Enchantment var7 = Enchantment.enchantmentsList[((Integer) var6
							.keySet().iterator().next()).intValue()];
					final CreativeTabs[] var8 = CreativeTabs.creativeTabArray;
					final int var9 = var8.length;

					for (int var10 = 0; var10 < var9; ++var10) {
						final CreativeTabs var11 = var8[var10];

						if (var11.func_111226_a(var7.type)) {
							var5 = var11;
							break;
						}
					}
				}
			}

			if (var5 != null) {
				var4.add(
						1,
						""
								+ EnumChatFormatting.BOLD
								+ EnumChatFormatting.BLUE
								+ I18n.format(var5.getTranslatedTabLabel(),
										new Object[0]));
			}

			for (int var12 = 0; var12 < var4.size(); ++var12) {
				if (var12 == 0) {
					var4.set(var12, p_146285_1_.getRarity().rarityColor
							+ (String) var4.get(var12));
				} else {
					var4.set(var12,
							EnumChatFormatting.GRAY + (String) var4.get(var12));
				}
			}

			func_146283_a(var4, p_146285_2_, p_146285_3_);
		} else {
			super.func_146285_a(p_146285_1_, p_146285_2_, p_146285_3_);
		}
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		final CreativeTabs var4 = CreativeTabs.creativeTabArray[field_147058_w];
		final CreativeTabs[] var5 = CreativeTabs.creativeTabArray;
		int var6 = var5.length;
		int var7;

		for (var7 = 0; var7 < var6; ++var7) {
			final CreativeTabs var8 = var5[var7];
			mc.getTextureManager().bindTexture(field_147061_u);

			if (var8.getTabIndex() != field_147058_w) {
				func_147051_a(var8);
			}
		}

		mc.getTextureManager().bindTexture(
				new ResourceLocation(
						"textures/gui/container/creative_inventory/tab_"
								+ var4.getBackgroundImageName()));
		drawTexturedModalRect(field_147003_i, field_147009_r, 0, 0,
				field_146999_f, field_147000_g);
		field_147062_A.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final int var9 = field_147003_i + 175;
		var6 = field_147009_r + 18;
		var7 = var6 + 112;
		mc.getTextureManager().bindTexture(field_147061_u);

		if (var4.shouldHidePlayerInventory()) {
			drawTexturedModalRect(var9, var6
					+ (int) ((var7 - var6 - 17) * field_147067_x),
					232 + (func_147055_p() ? 0 : 12), 0, 12, 15);
		}

		func_147051_a(var4);

		if (var4 == CreativeTabs.tabInventory) {
			GuiInventory.func_147046_a(field_147003_i + 43,
					field_147009_r + 45, 20, field_147003_i + 43 - p_146976_2_,
					field_147009_r + 45 - 30 - p_146976_3_, mc.thePlayer);
		}
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		final CreativeTabs var3 = CreativeTabs.creativeTabArray[field_147058_w];

		if (var3.drawInForegroundOfTab()) {
			GL11.glDisable(GL11.GL_BLEND);
			fontRendererObj.drawString(
					I18n.format(var3.getTranslatedTabLabel(), new Object[0]),
					8, 6, 4210752);
		}
	}

	@Override
	protected void func_146984_a(Slot p_146984_1_, int p_146984_2_,
			int p_146984_3_, int p_146984_4_) {
		field_147057_D = true;
		final boolean var5 = p_146984_4_ == 1;
		p_146984_4_ = p_146984_2_ == -999 && p_146984_4_ == 0 ? 4 : p_146984_4_;
		ItemStack var7;
		InventoryPlayer var11;

		if (p_146984_1_ == null
				&& field_147058_w != CreativeTabs.tabInventory.getTabIndex()
				&& p_146984_4_ != 5) {
			var11 = mc.thePlayer.inventory;

			if (var11.getItemStack() != null) {
				if (p_146984_3_ == 0) {
					mc.thePlayer.dropPlayerItemWithRandomChoice(
							var11.getItemStack(), true);
					mc.playerController
							.sendPacketDropItem(var11.getItemStack());
					var11.setItemStack((ItemStack) null);
				}

				if (p_146984_3_ == 1) {
					var7 = var11.getItemStack().splitStack(1);
					mc.thePlayer.dropPlayerItemWithRandomChoice(var7, true);
					mc.playerController.sendPacketDropItem(var7);

					if (var11.getItemStack().stackSize == 0) {
						var11.setItemStack((ItemStack) null);
					}
				}
			}
		} else {
			int var10;

			if (p_146984_1_ == field_147064_C && var5) {
				for (var10 = 0; var10 < mc.thePlayer.inventoryContainer
						.getInventory().size(); ++var10) {
					mc.playerController.sendSlotPacket((ItemStack) null, var10);
				}
			} else {
				ItemStack var6;

				if (field_147058_w == CreativeTabs.tabInventory.getTabIndex()) {
					if (p_146984_1_ == field_147064_C) {
						mc.thePlayer.inventory.setItemStack((ItemStack) null);
					} else if (p_146984_4_ == 4 && p_146984_1_ != null
							&& p_146984_1_.getHasStack()) {
						var6 = p_146984_1_.decrStackSize(p_146984_3_ == 0 ? 1
								: p_146984_1_.getStack().getMaxStackSize());
						mc.thePlayer.dropPlayerItemWithRandomChoice(var6, true);
						mc.playerController.sendPacketDropItem(var6);
					} else if (p_146984_4_ == 4
							&& mc.thePlayer.inventory.getItemStack() != null) {
						mc.thePlayer.dropPlayerItemWithRandomChoice(
								mc.thePlayer.inventory.getItemStack(), true);
						mc.playerController
								.sendPacketDropItem(mc.thePlayer.inventory
										.getItemStack());
						mc.thePlayer.inventory.setItemStack((ItemStack) null);
					} else {
						mc.thePlayer.inventoryContainer
								.slotClick(
										p_146984_1_ == null ? p_146984_2_
												: ((GuiContainerCreative.CreativeSlot) p_146984_1_).field_148332_b.slotNumber,
										p_146984_3_, p_146984_4_, mc.thePlayer);
						mc.thePlayer.inventoryContainer.detectAndSendChanges();
					}
				} else if (p_146984_4_ != 5
						&& p_146984_1_.inventory == field_147060_v) {
					var11 = mc.thePlayer.inventory;
					var7 = var11.getItemStack();
					final ItemStack var8 = p_146984_1_.getStack();
					ItemStack var9;

					if (p_146984_4_ == 2) {
						if (var8 != null && p_146984_3_ >= 0 && p_146984_3_ < 9) {
							var9 = var8.copy();
							var9.stackSize = var9.getMaxStackSize();
							mc.thePlayer.inventory.setInventorySlotContents(
									p_146984_3_, var9);
							mc.thePlayer.inventoryContainer
									.detectAndSendChanges();
						}

						return;
					}

					if (p_146984_4_ == 3) {
						if (var11.getItemStack() == null
								&& p_146984_1_.getHasStack()) {
							var9 = p_146984_1_.getStack().copy();
							var9.stackSize = var9.getMaxStackSize();
							var11.setItemStack(var9);
						}

						return;
					}

					if (p_146984_4_ == 4) {
						if (var8 != null) {
							var9 = var8.copy();
							var9.stackSize = p_146984_3_ == 0 ? 1 : var9
									.getMaxStackSize();
							mc.thePlayer.dropPlayerItemWithRandomChoice(var9,
									true);
							mc.playerController.sendPacketDropItem(var9);
						}

						return;
					}

					if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
						if (p_146984_3_ == 0) {
							if (var5) {
								var7.stackSize = var7.getMaxStackSize();
							} else if (var7.stackSize < var7.getMaxStackSize()) {
								++var7.stackSize;
							}
						} else if (var7.stackSize <= 1) {
							var11.setItemStack((ItemStack) null);
						} else {
							--var7.stackSize;
						}
					} else if (var8 != null && var7 == null) {
						var11.setItemStack(ItemStack.copyItemStack(var8));
						var7 = var11.getItemStack();

						if (var5) {
							var7.stackSize = var7.getMaxStackSize();
						}
					} else {
						var11.setItemStack((ItemStack) null);
					}
				} else {
					field_147002_h.slotClick(p_146984_1_ == null ? p_146984_2_
							: p_146984_1_.slotNumber, p_146984_3_, p_146984_4_,
							mc.thePlayer);

					if (Container.func_94532_c(p_146984_3_) == 2) {
						for (var10 = 0; var10 < 9; ++var10) {
							mc.playerController
									.sendSlotPacket(
											field_147002_h.getSlot(45 + var10)
													.getStack(), 36 + var10);
						}
					} else if (p_146984_1_ != null) {
						var6 = field_147002_h.getSlot(p_146984_1_.slotNumber)
								.getStack();
						mc.playerController.sendSlotPacket(var6,
								p_146984_1_.slotNumber
										- field_147002_h.inventorySlots.size()
										+ 9 + 36);
					}
				}
			}
		}
	}

	protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_,
			int p_147049_3_) {
		final int var4 = p_147049_1_.getTabColumn();
		int var5 = 28 * var4;
		final byte var6 = 0;

		if (var4 == 5) {
			var5 = field_146999_f - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (p_147049_1_.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + field_147000_g;
		}

		return p_147049_2_ >= var5 && p_147049_2_ <= var5 + 28
				&& p_147049_3_ >= var7 && p_147049_3_ <= var7 + 32;
	}

	private void func_147050_b(CreativeTabs p_147050_1_) {
		final int var2 = field_147058_w;
		field_147058_w = p_147050_1_.getTabIndex();
		final GuiContainerCreative.ContainerCreative var3 = (GuiContainerCreative.ContainerCreative) field_147002_h;
		field_147008_s.clear();
		var3.field_148330_a.clear();
		p_147050_1_.displayAllReleventItems(var3.field_148330_a);

		if (p_147050_1_ == CreativeTabs.tabInventory) {
			final Container var4 = mc.thePlayer.inventoryContainer;

			if (field_147063_B == null) {
				field_147063_B = var3.inventorySlots;
			}

			var3.inventorySlots = new ArrayList();

			for (int var5 = 0; var5 < var4.inventorySlots.size(); ++var5) {
				final GuiContainerCreative.CreativeSlot var6 = new GuiContainerCreative.CreativeSlot(
						(Slot) var4.inventorySlots.get(var5), var5);
				var3.inventorySlots.add(var6);
				int var7;
				int var8;
				int var9;

				if (var5 >= 5 && var5 < 9) {
					var7 = var5 - 5;
					var8 = var7 / 2;
					var9 = var7 % 2;
					var6.xDisplayPosition = 9 + var8 * 54;
					var6.yDisplayPosition = 6 + var9 * 27;
				} else if (var5 >= 0 && var5 < 5) {
					var6.yDisplayPosition = -2000;
					var6.xDisplayPosition = -2000;
				} else if (var5 < var4.inventorySlots.size()) {
					var7 = var5 - 9;
					var8 = var7 % 9;
					var9 = var7 / 9;
					var6.xDisplayPosition = 9 + var8 * 18;

					if (var5 >= 36) {
						var6.yDisplayPosition = 112;
					} else {
						var6.yDisplayPosition = 54 + var9 * 18;
					}
				}
			}

			field_147064_C = new Slot(field_147060_v, 0, 173, 112);
			var3.inventorySlots.add(field_147064_C);
		} else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
			var3.inventorySlots = field_147063_B;
			field_147063_B = null;
		}

		if (field_147062_A != null) {
			if (p_147050_1_ == CreativeTabs.tabAllSearch) {
				field_147062_A.func_146189_e(true);
				field_147062_A.func_146205_d(false);
				field_147062_A.setFocused(true);
				field_147062_A.setText("");
				func_147053_i();
			} else {
				field_147062_A.func_146189_e(false);
				field_147062_A.func_146205_d(true);
				field_147062_A.setFocused(false);
			}
		}

		field_147067_x = 0.0F;
		var3.func_148329_a(0.0F);
	}

	protected void func_147051_a(CreativeTabs p_147051_1_) {
		final boolean var2 = p_147051_1_.getTabIndex() == field_147058_w;
		final boolean var3 = p_147051_1_.isTabInFirstRow();
		final int var4 = p_147051_1_.getTabColumn();
		final int var5 = var4 * 28;
		int var6 = 0;
		int var7 = field_147003_i + 28 * var4;
		int var8 = field_147009_r;
		final byte var9 = 32;

		if (var2) {
			var6 += 32;
		}

		if (var4 == 5) {
			var7 = field_147003_i + field_146999_f - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var6 += 64;
			var8 += field_147000_g - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
		zLevel = 100.0F;
		itemRender.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		final ItemStack var10 = p_147051_1_.getIconItemStack();
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), var10, var7, var8);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj,
				mc.getTextureManager(), var10, var7, var8);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	protected boolean func_147052_b(CreativeTabs p_147052_1_, int p_147052_2_,
			int p_147052_3_) {
		final int var4 = p_147052_1_.getTabColumn();
		int var5 = 28 * var4;
		final byte var6 = 0;

		if (var4 == 5) {
			var5 = field_146999_f - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (p_147052_1_.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + field_147000_g;
		}

		if (func_146978_c(var5 + 3, var7 + 3, 23, 27, p_147052_2_, p_147052_3_)) {
			func_146279_a(I18n.format(p_147052_1_.getTranslatedTabLabel(),
					new Object[0]), p_147052_2_, p_147052_3_);
			return true;
		} else
			return false;
	}

	private void func_147053_i() {
		final GuiContainerCreative.ContainerCreative var1 = (GuiContainerCreative.ContainerCreative) field_147002_h;
		var1.field_148330_a.clear();
		Iterator var2 = Item.itemRegistry.iterator();

		while (var2.hasNext()) {
			final Item var3 = (Item) var2.next();

			if (var3 != null && var3.getCreativeTab() != null) {
				var3.getSubItems(var3, (CreativeTabs) null, var1.field_148330_a);
			}
		}

		final Enchantment[] var8 = Enchantment.enchantmentsList;
		final int var9 = var8.length;

		for (int var4 = 0; var4 < var9; ++var4) {
			final Enchantment var5 = var8[var4];

			if (var5 != null && var5.type != null) {
				Items.enchanted_book.func_92113_a(var5, var1.field_148330_a);
			}
		}

		var2 = var1.field_148330_a.iterator();
		final String var10 = field_147062_A.getText().toLowerCase();

		while (var2.hasNext()) {
			final ItemStack var11 = (ItemStack) var2.next();
			boolean var12 = false;
			final Iterator var6 = var11.getTooltip(mc.thePlayer,
					mc.gameSettings.advancedItemTooltips).iterator();

			while (true) {
				if (var6.hasNext()) {
					final String var7 = (String) var6.next();

					if (!var7.toLowerCase().contains(var10)) {
						continue;
					}

					var12 = true;
				}

				if (!var12) {
					var2.remove();
				}

				break;
			}
		}

		field_147067_x = 0.0F;
		var1.func_148329_a(0.0F);
	}

	private boolean func_147055_p() {
		return field_147058_w != CreativeTabs.tabInventory.getTabIndex()
				&& CreativeTabs.creativeTabArray[field_147058_w]
						.shouldHidePlayerInventory()
				&& ((GuiContainerCreative.ContainerCreative) field_147002_h)
						.func_148328_e();
	}

	public int func_147056_g() {
		return field_147058_w;
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0 && func_147055_p()) {
			final int var2 = ((GuiContainerCreative.ContainerCreative) field_147002_h).field_148330_a
					.size() / 9 - 5 + 1;

			if (var1 > 0) {
				var1 = 1;
			}

			if (var1 < 0) {
				var1 = -1;
			}

			field_147067_x = (float) (field_147067_x - (double) var1
					/ (double) var2);

			if (field_147067_x < 0.0F) {
				field_147067_x = 0.0F;
			}

			if (field_147067_x > 1.0F) {
				field_147067_x = 1.0F;
			}

			((GuiContainerCreative.ContainerCreative) field_147002_h)
					.func_148329_a(field_147067_x);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		if (mc.playerController.isInCreativeMode()) {
			super.initGui();
			buttons.clear();
			Keyboard.enableRepeatEvents(true);
			field_147062_A = new GuiTextField(fontRendererObj,
					field_147003_i + 82, field_147009_r + 6, 89,
					fontRendererObj.FONT_HEIGHT);
			field_147062_A.func_146203_f(15);
			field_147062_A.func_146185_a(false);
			field_147062_A.func_146189_e(false);
			field_147062_A.func_146193_g(16777215);
			final int var1 = field_147058_w;
			field_147058_w = -1;
			func_147050_b(CreativeTabs.creativeTabArray[var1]);
			field_147059_E = new CreativeCrafting(mc);
			mc.thePlayer.inventoryContainer
					.addCraftingToCrafters(field_147059_E);
		} else {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_147058_w != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindChat)) {
				func_147050_b(CreativeTabs.tabAllSearch);
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		} else {
			if (field_147057_D) {
				field_147057_D = false;
				field_147062_A.setText("");
			}

			if (!func_146983_a(p_73869_2_)) {
				if (field_147062_A.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
					func_147053_i();
				} else {
					super.keyTyped(p_73869_1_, p_73869_2_);
				}
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0) {
			final int var4 = p_73864_1_ - field_147003_i;
			final int var5 = p_73864_2_ - field_147009_r;
			final CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			final int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				final CreativeTabs var9 = var6[var8];

				if (func_147049_a(var9, var4, var5))
					return;
			}
		}

		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		if (p_146286_3_ == 0) {
			final int var4 = p_146286_1_ - field_147003_i;
			final int var5 = p_146286_2_ - field_147009_r;
			final CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			final int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				final CreativeTabs var9 = var6[var8];

				if (func_147049_a(var9, var4, var5)) {
					func_147050_b(var9);
					return;
				}
			}
		}

		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (mc.thePlayer != null && mc.thePlayer.inventory != null) {
			mc.thePlayer.inventoryContainer
					.removeCraftingFromCrafters(field_147059_E);
		}

		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		if (!mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
	}
}
