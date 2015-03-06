package net.minecraft.item;

import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemArmor extends Item {
	public static enum ArmorMaterial {
		CHAIN("CHAIN", 1, 15, new int[] { 2, 5, 4, 1 }, 12), CLOTH("CLOTH", 0,
				5, new int[] { 1, 3, 2, 1 }, 15), DIAMOND("DIAMOND", 4, 33,
				new int[] { 3, 8, 6, 3 }, 10), GOLD("GOLD", 3, 7, new int[] {
				2, 5, 3, 1 }, 25), IRON("IRON", 2, 15,
				new int[] { 2, 6, 5, 2 }, 9);
		private int[] damageReductionAmountArray;
		private int enchantability;
		private int maxDamageFactor;

		private ArmorMaterial(String p_i1827_1_, int p_i1827_2_,
				int p_i1827_3_, int[] p_i1827_4_, int p_i1827_5_) {
			maxDamageFactor = p_i1827_3_;
			damageReductionAmountArray = p_i1827_4_;
			enchantability = p_i1827_5_;
		}

		public Item func_151685_b() {
			return this == CLOTH ? Items.leather
					: this == CHAIN ? Items.iron_ingot
							: this == GOLD ? Items.gold_ingot
									: this == IRON ? Items.iron_ingot
											: this == DIAMOND ? Items.diamond
													: null;
		}

		public int getDamageReductionAmount(int p_78044_1_) {
			return damageReductionAmountArray[p_78044_1_];
		}

		public int getDurability(int p_78046_1_) {
			return ItemArmor.maxDamageArray[p_78046_1_] * maxDamageFactor;
		}

		public int getEnchantability() {
			return enchantability;
		}
	}

	private static final String[] CLOTH_OVERLAY_NAMES = new String[] {
			"leather_helmet_overlay", "leather_chestplate_overlay",
			"leather_leggings_overlay", "leather_boots_overlay" };

	private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem() {

		@Override
		protected ItemStack dispenseStack(IBlockSource p_82487_1_,
				ItemStack p_82487_2_) {
			final EnumFacing var3 = BlockDispenser.func_149937_b(p_82487_1_
					.getBlockMetadata());
			final int var4 = p_82487_1_.getXInt() + var3.getFrontOffsetX();
			final int var5 = p_82487_1_.getYInt() + var3.getFrontOffsetY();
			final int var6 = p_82487_1_.getZInt() + var3.getFrontOffsetZ();
			final AxisAlignedBB var7 = AxisAlignedBB.getBoundingBox(var4, var5,
					var6, var4 + 1, var5 + 1, var6 + 1);
			final List var8 = p_82487_1_.getWorld().selectEntitiesWithinAABB(
					EntityLivingBase.class, var7,
					new IEntitySelector.ArmoredMob(p_82487_2_));

			if (var8.size() > 0) {
				final EntityLivingBase var9 = (EntityLivingBase) var8.get(0);
				final int var10 = var9 instanceof EntityPlayer ? 1 : 0;
				final int var11 = EntityLiving.getArmorPosition(p_82487_2_);
				final ItemStack var12 = p_82487_2_.copy();
				var12.stackSize = 1;
				var9.setCurrentItemOrArmor(var11 - var10, var12);

				if (var9 instanceof EntityLiving) {
					((EntityLiving) var9).setEquipmentDropChance(var11, 2.0F);
				}

				--p_82487_2_.stackSize;
				return p_82487_2_;
			} else
				return super.dispenseStack(p_82487_1_, p_82487_2_);
		}
	};
	public static final String[] EMPTY_SLOT_NAMES = new String[] {
			"empty_armor_slot_helmet", "empty_armor_slot_chestplate",
			"empty_armor_slot_leggings", "empty_armor_slot_boots" };

	/** Holds the 'base' maxDamage that each armorType have. */
	private static final int[] maxDamageArray = new int[] { 11, 16, 15, 13 };

	public static IIcon func_94602_b(int p_94602_0_) {
		switch (p_94602_0_) {
		case 0:
			return Items.diamond_helmet.emptySlotIcon;

		case 1:
			return Items.diamond_chestplate.emptySlotIcon;

		case 2:
			return Items.diamond_leggings.emptySlotIcon;

		case 3:
			return Items.diamond_boots.emptySlotIcon;

		default:
			return null;
		}
	}

	/**
	 * Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots
	 */
	public final int armorType;

	/** Holds the amount of damage that the armor reduces at full durability. */
	public final int damageReduceAmount;
	private IIcon emptySlotIcon;
	/** The EnumArmorMaterial used for this ItemArmor */
	private final ItemArmor.ArmorMaterial material;

	private IIcon overlayIcon;

	/**
	 * Used on RenderPlayer to select the correspondent armor to be rendered on
	 * the player: 0 is cloth, 1 is chain, 2 is iron, 3 is diamond and 4 is
	 * gold.
	 */
	public final int renderIndex;

	public ItemArmor(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_,
			int p_i45325_3_) {
		material = p_i45325_1_;
		armorType = p_i45325_3_;
		renderIndex = p_i45325_2_;
		damageReduceAmount = p_i45325_1_.getDamageReductionAmount(p_i45325_3_);
		setMaxDamage(p_i45325_1_.getDurability(p_i45325_3_));
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabCombat);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this,
				dispenserBehavior);
	}

	public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_) {
		if (material != ItemArmor.ArmorMaterial.CLOTH)
			throw new UnsupportedOperationException("Can\'t dye non-leather!");
		else {
			NBTTagCompound var3 = p_82813_1_.getTagCompound();

			if (var3 == null) {
				var3 = new NBTTagCompound();
				p_82813_1_.setTagCompound(var3);
			}

			final NBTTagCompound var4 = var3.getCompoundTag("display");

			if (!var3.func_150297_b("display", 10)) {
				var3.setTag("display", var4);
			}

			var4.setInteger("color", p_82813_2_);
		}
	}

	/**
	 * Return the armor material for this armor item.
	 */
	public ItemArmor.ArmorMaterial getArmorMaterial() {
		return material;
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	public int getColor(ItemStack p_82814_1_) {
		if (material != ItemArmor.ArmorMaterial.CLOTH)
			return -1;
		else {
			final NBTTagCompound var2 = p_82814_1_.getTagCompound();

			if (var2 == null)
				return 10511680;
			else {
				final NBTTagCompound var3 = var2.getCompoundTag("display");
				return var3 == null ? 10511680
						: var3.func_150297_b("color", 3) ? var3
								.getInteger("color") : 10511680;
			}
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		if (p_82790_2_ > 0)
			return 16777215;
		else {
			int var3 = getColor(p_82790_1_);

			if (var3 < 0) {
				var3 = 16777215;
			}

			return var3;
		}
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render
	 * pass
	 */
	@Override
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
		return p_77618_2_ == 1 ? overlayIcon : super
				.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return material.func_151685_b() == p_82789_2_.getItem() ? true : super
				.getIsRepairable(p_82789_1_, p_82789_2_);
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return material.getEnchantability();
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	public boolean hasColor(ItemStack p_82816_1_) {
		return material != ItemArmor.ArmorMaterial.CLOTH ? false : !p_82816_1_
				.hasTagCompound() ? false : !p_82816_1_.getTagCompound()
				.func_150297_b("display", 10) ? false : p_82816_1_
				.getTagCompound().getCompoundTag("display")
				.func_150297_b("color", 3);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		final int var4 = EntityLiving.getArmorPosition(p_77659_1_) - 1;
		final ItemStack var5 = p_77659_3_.getCurrentArmor(var4);

		if (var5 == null) {
			p_77659_3_.setCurrentItemOrArmor(var4, p_77659_1_.copy());
			p_77659_1_.stackSize = 0;
		}

		return p_77659_1_;
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		super.registerIcons(p_94581_1_);

		if (material == ItemArmor.ArmorMaterial.CLOTH) {
			overlayIcon = p_94581_1_
					.registerIcon(CLOTH_OVERLAY_NAMES[armorType]);
		}

		emptySlotIcon = p_94581_1_.registerIcon(EMPTY_SLOT_NAMES[armorType]);
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	public void removeColor(ItemStack p_82815_1_) {
		if (material == ItemArmor.ArmorMaterial.CLOTH) {
			final NBTTagCompound var2 = p_82815_1_.getTagCompound();

			if (var2 != null) {
				final NBTTagCompound var3 = var2.getCompoundTag("display");

				if (var3.hasKey("color")) {
					var3.removeTag("color");
				}
			}
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return material == ItemArmor.ArmorMaterial.CLOTH;
	}
}
