package net.minecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlock extends Item {
	private IIcon field_150938_b;
	protected final Block field_150939_a;

	public ItemBlock(Block p_i45328_1_) {
		field_150939_a = p_i45328_1_;
	}

	public boolean func_150936_a(World p_150936_1_, int p_150936_2_,
			int p_150936_3_, int p_150936_4_, int p_150936_5_,
			EntityPlayer p_150936_6_, ItemStack p_150936_7_) {
		final Block var8 = p_150936_1_.getBlock(p_150936_2_, p_150936_3_,
				p_150936_4_);

		if (var8 == Blocks.snow_layer) {
			p_150936_5_ = 1;
		} else if (var8 != Blocks.vine && var8 != Blocks.tallgrass
				&& var8 != Blocks.deadbush) {
			if (p_150936_5_ == 0) {
				--p_150936_3_;
			}

			if (p_150936_5_ == 1) {
				++p_150936_3_;
			}

			if (p_150936_5_ == 2) {
				--p_150936_4_;
			}

			if (p_150936_5_ == 3) {
				++p_150936_4_;
			}

			if (p_150936_5_ == 4) {
				--p_150936_2_;
			}

			if (p_150936_5_ == 5) {
				++p_150936_2_;
			}
		}

		return p_150936_1_.canPlaceEntityOnSide(field_150939_a, p_150936_2_,
				p_150936_3_, p_150936_4_, false, p_150936_5_, (Entity) null,
				p_150936_7_);
	}

	/**
	 * gets the CreativeTab this item is displayed on
	 */
	@Override
	public CreativeTabs getCreativeTab() {
		return field_150939_a.getCreativeTabToDisplayOn();
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150938_b != null ? field_150938_b : field_150939_a
				.getBlockTextureFromSide(1);
	}

	/**
	 * Returns 0 for /terrain.png, 1 for /gui/items.png
	 */
	@Override
	public int getSpriteNumber() {
		return field_150939_a.getItemIconName() != null ? 1 : 0;
	}

	/**
	 * This returns the sub items
	 */
	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
			List p_150895_3_) {
		field_150939_a.getSubBlocks(p_150895_1_, p_150895_2_, p_150895_3_);
	}

	/**
	 * Returns the unlocalized name of this item.
	 */
	@Override
	public String getUnlocalizedName() {
		return field_150939_a.getUnlocalizedName();
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return field_150939_a.getUnlocalizedName();
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_,
			World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_,
			int p_77648_7_, float p_77648_8_, float p_77648_9_,
			float p_77648_10_) {
		final Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_,
				p_77648_6_);

		if (var11 == Blocks.snow_layer
				&& (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_,
						p_77648_6_) & 7) < 1) {
			p_77648_7_ = 1;
		} else if (var11 != Blocks.vine && var11 != Blocks.tallgrass
				&& var11 != Blocks.deadbush) {
			if (p_77648_7_ == 0) {
				--p_77648_5_;
			}

			if (p_77648_7_ == 1) {
				++p_77648_5_;
			}

			if (p_77648_7_ == 2) {
				--p_77648_6_;
			}

			if (p_77648_7_ == 3) {
				++p_77648_6_;
			}

			if (p_77648_7_ == 4) {
				--p_77648_4_;
			}

			if (p_77648_7_ == 5) {
				++p_77648_4_;
			}
		}

		if (p_77648_1_.stackSize == 0)
			return false;
		else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_,
				p_77648_7_, p_77648_1_))
			return false;
		else if (p_77648_5_ == 255 && field_150939_a.getMaterial().isSolid())
			return false;
		else if (p_77648_3_.canPlaceEntityOnSide(field_150939_a, p_77648_4_,
				p_77648_5_, p_77648_6_, false, p_77648_7_, p_77648_2_,
				p_77648_1_)) {
			final int var12 = getMetadata(p_77648_1_.getItemDamage());
			final int var13 = field_150939_a.onBlockPlaced(p_77648_3_,
					p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_,
					p_77648_9_, p_77648_10_, var12);

			if (p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_,
					field_150939_a, var13, 3)) {
				if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == field_150939_a) {
					field_150939_a.onBlockPlacedBy(p_77648_3_, p_77648_4_,
							p_77648_5_, p_77648_6_, p_77648_2_, p_77648_1_);
					field_150939_a.onPostBlockPlaced(p_77648_3_, p_77648_4_,
							p_77648_5_, p_77648_6_, var13);
				}

				p_77648_3_
						.playSoundEffect(
								p_77648_4_ + 0.5F,
								p_77648_5_ + 0.5F,
								p_77648_6_ + 0.5F,
								field_150939_a.stepSound.func_150496_b(),
								(field_150939_a.stepSound.func_150497_c() + 1.0F) / 2.0F,
								field_150939_a.stepSound.func_150494_d() * 0.8F);
				--p_77648_1_.stackSize;
			}

			return true;
		} else
			return false;
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		final String var2 = field_150939_a.getItemIconName();

		if (var2 != null) {
			field_150938_b = p_94581_1_.registerIcon(var2);
		}
	}

	/**
	 * Sets the unlocalized name of this item to the string passed as the
	 * parameter, prefixed by "item."
	 */
	@Override
	public ItemBlock setUnlocalizedName(String p_77655_1_) {
		super.setUnlocalizedName(p_77655_1_);
		return this;
	}
}
