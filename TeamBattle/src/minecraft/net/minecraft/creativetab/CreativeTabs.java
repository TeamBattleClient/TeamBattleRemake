package net.minecraft.creativetab;

import java.util.Iterator;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
	public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
	public static final CreativeTabs tabAllSearch = new CreativeTabs(5,
			"search") {

		@Override
		public Item getTabIconItem() {
			return Items.compass;
		}
	}.setBackgroundImageName("item_search.png");
	public static final CreativeTabs tabBlock = new CreativeTabs(0,
			"buildingBlocks") {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.brick_block);
		}
	};
	public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing") {

		@Override
		public Item getTabIconItem() {
			return Items.potionitem;
		}
	};
	public static final CreativeTabs tabCombat = new CreativeTabs(8, "combat") {

		@Override
		public Item getTabIconItem() {
			return Items.golden_sword;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.armor,
			EnumEnchantmentType.armor_feet, EnumEnchantmentType.armor_head,
			EnumEnchantmentType.armor_legs, EnumEnchantmentType.armor_torso,
			EnumEnchantmentType.bow, EnumEnchantmentType.weapon });
	public static final CreativeTabs tabDecorations = new CreativeTabs(1,
			"decorations") {

		@Override
		public int func_151243_f() {
			return 5;
		}

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.double_plant);
		}
	};
	public static final CreativeTabs tabFood = new CreativeTabs(6, "food") {

		@Override
		public Item getTabIconItem() {
			return Items.apple;
		}
	};
	public static final CreativeTabs tabInventory = new CreativeTabs(11,
			"inventory") {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.chest);
		}
	}.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
	public static final CreativeTabs tabMaterials = new CreativeTabs(10,
			"materials") {

		@Override
		public Item getTabIconItem() {
			return Items.stick;
		}
	};
	public static final CreativeTabs tabMisc = new CreativeTabs(4, "misc") {

		@Override
		public Item getTabIconItem() {
			return Items.lava_bucket;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.all });
	public static final CreativeTabs tabRedstone = new CreativeTabs(2,
			"redstone") {

		@Override
		public Item getTabIconItem() {
			return Items.redstone;
		}
	};
	public static final CreativeTabs tabTools = new CreativeTabs(7, "tools") {

		@Override
		public Item getTabIconItem() {
			return Items.iron_axe;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.digger,
			EnumEnchantmentType.fishing_rod, EnumEnchantmentType.breakable });
	public static final CreativeTabs tabTransport = new CreativeTabs(3,
			"transportation") {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.golden_rail);
		}
	};
	/** Texture to use. */
	private String backgroundImageName = "items.png";
	/** Whether to draw the title in the foreground of the creative GUI */
	private boolean drawTitle = true;

	private EnumEnchantmentType[] field_111230_s;
	private ItemStack field_151245_t;

	private boolean hasScrollbar = true;
	private final int tabIndex;
	private final String tabLabel;

	public CreativeTabs(int p_i1853_1_, String p_i1853_2_) {
		tabIndex = p_i1853_1_;
		tabLabel = p_i1853_2_;
		creativeTabArray[p_i1853_1_] = this;
	}

	/**
	 * Adds the enchantment books from the supplied EnumEnchantmentType to the
	 * given list.
	 */
	public void addEnchantmentBooksToList(List p_92116_1_,
			EnumEnchantmentType... p_92116_2_) {
		final Enchantment[] var3 = Enchantment.enchantmentsList;
		final int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final Enchantment var6 = var3[var5];

			if (var6 != null && var6.type != null) {
				boolean var7 = false;

				for (int var8 = 0; var8 < p_92116_2_.length && !var7; ++var8) {
					if (var6.type == p_92116_2_[var8]) {
						var7 = true;
					}
				}

				if (var7) {
					p_92116_1_.add(Items.enchanted_book
							.getEnchantedItemStack(new EnchantmentData(var6,
									var6.getMaxLevel())));
				}
			}
		}
	}

	/**
	 * only shows items which have tabToDisplayOn == this
	 */
	public void displayAllReleventItems(List p_78018_1_) {
		final Iterator var2 = Item.itemRegistry.iterator();

		while (var2.hasNext()) {
			final Item var3 = (Item) var2.next();

			if (var3 != null && var3.getCreativeTab() == this) {
				var3.getSubItems(var3, this, p_78018_1_);
			}
		}

		if (func_111225_m() != null) {
			addEnchantmentBooksToList(p_78018_1_, func_111225_m());
		}
	}

	public boolean drawInForegroundOfTab() {
		return drawTitle;
	}

	public EnumEnchantmentType[] func_111225_m() {
		return field_111230_s;
	}

	public boolean func_111226_a(EnumEnchantmentType p_111226_1_) {
		if (field_111230_s == null)
			return false;
		else {
			final EnumEnchantmentType[] var2 = field_111230_s;
			final int var3 = var2.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				final EnumEnchantmentType var5 = var2[var4];

				if (var5 == p_111226_1_)
					return true;
			}

			return false;
		}
	}

	public CreativeTabs func_111229_a(EnumEnchantmentType... p_111229_1_) {
		field_111230_s = p_111229_1_;
		return this;
	}

	public int func_151243_f() {
		return 0;
	}

	public String getBackgroundImageName() {
		return backgroundImageName;
	}

	public ItemStack getIconItemStack() {
		if (field_151245_t == null) {
			field_151245_t = new ItemStack(getTabIconItem(), 1, func_151243_f());
		}

		return field_151245_t;
	}

	/**
	 * returns index % 6
	 */
	public int getTabColumn() {
		return tabIndex % 6;
	}

	public abstract Item getTabIconItem();

	public int getTabIndex() {
		return tabIndex;
	}

	public String getTabLabel() {
		return tabLabel;
	}

	/**
	 * Gets the translated Label.
	 */
	public String getTranslatedTabLabel() {
		return "itemGroup." + getTabLabel();
	}

	/**
	 * returns tabIndex < 6
	 */
	public boolean isTabInFirstRow() {
		return tabIndex < 6;
	}

	public CreativeTabs setBackgroundImageName(String p_78025_1_) {
		backgroundImageName = p_78025_1_;
		return this;
	}

	public CreativeTabs setNoScrollbar() {
		hasScrollbar = false;
		return this;
	}

	public CreativeTabs setNoTitle() {
		drawTitle = false;
		return this;
	}

	public boolean shouldHidePlayerInventory() {
		return hasScrollbar;
	}
}
