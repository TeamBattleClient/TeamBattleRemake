package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock {
	private final BlockSlab field_150947_d;
	private final boolean field_150948_b;
	private final BlockSlab field_150949_c;

	public ItemSlab(Block p_i45355_1_, BlockSlab p_i45355_2_,
			BlockSlab p_i45355_3_, boolean p_i45355_4_) {
		super(p_i45355_1_);
		field_150949_c = p_i45355_2_;
		field_150947_d = p_i45355_3_;
		field_150948_b = p_i45355_4_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public boolean func_150936_a(World p_150936_1_, int p_150936_2_,
			int p_150936_3_, int p_150936_4_, int p_150936_5_,
			EntityPlayer p_150936_6_, ItemStack p_150936_7_) {
		final int var8 = p_150936_2_;
		final int var9 = p_150936_3_;
		final int var10 = p_150936_4_;
		final Block var11 = p_150936_1_.getBlock(p_150936_2_, p_150936_3_,
				p_150936_4_);
		final int var12 = p_150936_1_.getBlockMetadata(p_150936_2_,
				p_150936_3_, p_150936_4_);
		int var13 = var12 & 7;
		final boolean var14 = (var12 & 8) != 0;

		if ((p_150936_5_ == 1 && !var14 || p_150936_5_ == 0 && var14)
				&& var11 == field_150949_c
				&& var13 == p_150936_7_.getItemDamage())
			return true;
		else {
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

			final Block var15 = p_150936_1_.getBlock(p_150936_2_, p_150936_3_,
					p_150936_4_);
			final int var16 = p_150936_1_.getBlockMetadata(p_150936_2_,
					p_150936_3_, p_150936_4_);
			var13 = var16 & 7;
			return var15 == field_150949_c
					&& var13 == p_150936_7_.getItemDamage() ? true : super
					.func_150936_a(p_150936_1_, var8, var9, var10, p_150936_5_,
							p_150936_6_, p_150936_7_);
		}
	}

	private boolean func_150946_a(ItemStack p_150946_1_,
			EntityPlayer p_150946_2_, World p_150946_3_, int p_150946_4_,
			int p_150946_5_, int p_150946_6_, int p_150946_7_) {
		if (p_150946_7_ == 0) {
			--p_150946_5_;
		}

		if (p_150946_7_ == 1) {
			++p_150946_5_;
		}

		if (p_150946_7_ == 2) {
			--p_150946_6_;
		}

		if (p_150946_7_ == 3) {
			++p_150946_6_;
		}

		if (p_150946_7_ == 4) {
			--p_150946_4_;
		}

		if (p_150946_7_ == 5) {
			++p_150946_4_;
		}

		final Block var8 = p_150946_3_.getBlock(p_150946_4_, p_150946_5_,
				p_150946_6_);
		final int var9 = p_150946_3_.getBlockMetadata(p_150946_4_, p_150946_5_,
				p_150946_6_);
		final int var10 = var9 & 7;

		if (var8 == field_150949_c && var10 == p_150946_1_.getItemDamage()) {
			if (p_150946_3_.checkNoEntityCollision(field_150947_d
					.getCollisionBoundingBoxFromPool(p_150946_3_, p_150946_4_,
							p_150946_5_, p_150946_6_))
					&& p_150946_3_.setBlock(p_150946_4_, p_150946_5_,
							p_150946_6_, field_150947_d, var10, 3)) {
				p_150946_3_
						.playSoundEffect(
								p_150946_4_ + 0.5F,
								p_150946_5_ + 0.5F,
								p_150946_6_ + 0.5F,
								field_150947_d.stepSound.func_150496_b(),
								(field_150947_d.stepSound.func_150497_c() + 1.0F) / 2.0F,
								field_150947_d.stepSound.func_150494_d() * 0.8F);
				--p_150946_1_.stackSize;
			}

			return true;
		} else
			return false;
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return Block.getBlockFromItem(this).getIcon(2, p_77617_1_);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return field_150949_c.func_150002_b(p_77667_1_.getItemDamage());
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
		if (field_150948_b)
			return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_,
					p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_,
					p_77648_9_, p_77648_10_);
		else if (p_77648_1_.stackSize == 0)
			return false;
		else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_,
				p_77648_7_, p_77648_1_))
			return false;
		else {
			final Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_,
					p_77648_6_);
			final int var12 = p_77648_3_.getBlockMetadata(p_77648_4_,
					p_77648_5_, p_77648_6_);
			final int var13 = var12 & 7;
			final boolean var14 = (var12 & 8) != 0;

			if ((p_77648_7_ == 1 && !var14 || p_77648_7_ == 0 && var14)
					&& var11 == field_150949_c
					&& var13 == p_77648_1_.getItemDamage()) {
				if (p_77648_3_.checkNoEntityCollision(field_150947_d
						.getCollisionBoundingBoxFromPool(p_77648_3_,
								p_77648_4_, p_77648_5_, p_77648_6_))
						&& p_77648_3_.setBlock(p_77648_4_, p_77648_5_,
								p_77648_6_, field_150947_d, var13, 3)) {
					p_77648_3_
							.playSoundEffect(
									p_77648_4_ + 0.5F,
									p_77648_5_ + 0.5F,
									p_77648_6_ + 0.5F,
									field_150947_d.stepSound.func_150496_b(),
									(field_150947_d.stepSound.func_150497_c() + 1.0F) / 2.0F,
									field_150947_d.stepSound.func_150494_d() * 0.8F);
					--p_77648_1_.stackSize;
				}

				return true;
			} else
				return func_150946_a(p_77648_1_, p_77648_2_, p_77648_3_,
						p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_) ? true
						: super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_,
								p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_,
								p_77648_8_, p_77648_9_, p_77648_10_);
		}
	}
}
