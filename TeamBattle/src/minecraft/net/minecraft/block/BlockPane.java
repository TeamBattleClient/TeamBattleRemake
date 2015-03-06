package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {
	private final boolean field_150099_b;
	private final String field_150100_a;
	private final String field_150101_M;
	private IIcon field_150102_N;

	protected BlockPane(String p_i45432_1_, String p_i45432_2_,
			Material p_i45432_3_, boolean p_i45432_4_) {
		super(p_i45432_3_);
		field_150100_a = p_i45432_2_;
		field_150099_b = p_i45432_4_;
		field_150101_M = p_i45432_1_;
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) {
		final boolean var8 = func_150098_a(p_149743_1_.getBlock(p_149743_2_,
				p_149743_3_, p_149743_4_ - 1));
		final boolean var9 = func_150098_a(p_149743_1_.getBlock(p_149743_2_,
				p_149743_3_, p_149743_4_ + 1));
		final boolean var10 = func_150098_a(p_149743_1_.getBlock(
				p_149743_2_ - 1, p_149743_3_, p_149743_4_));
		final boolean var11 = func_150098_a(p_149743_1_.getBlock(
				p_149743_2_ + 1, p_149743_3_, p_149743_4_));

		if ((!var10 || !var11) && (var10 || var11 || var8 || var9)) {
			if (var10 && !var11) {
				setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
						p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
						p_149743_7_);
			} else if (!var10 && var11) {
				setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
						p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
						p_149743_7_);
			}
		} else {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		if ((!var8 || !var9) && (var10 || var11 || var8 || var9)) {
			if (var8 && !var9) {
				setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
						p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
						p_149743_7_);
			} else if (!var8 && var9) {
				setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
				super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
						p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
						p_149743_7_);
			}
		} else {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Returns an item stack containing a single instance of the current block
	 * type. 'i' is the block's subtype/damage and is ignored for blocks which
	 * do not support subtypes. Blocks which cannot be harvested should return
	 * null.
	 */
	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return new ItemStack(Item.getItemFromBlock(this), 1, p_149644_1_);
	}

	public IIcon func_150097_e() {
		return field_150102_N;
	}

	public final boolean func_150098_a(Block p_150098_1_) {
		return p_150098_1_.func_149730_j() || p_150098_1_ == this
				|| p_150098_1_ == Blocks.glass
				|| p_150098_1_ == Blocks.stained_glass
				|| p_150098_1_ == Blocks.stained_glass_pane
				|| p_150098_1_ instanceof BlockPane;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return !field_150099_b ? null : super.getItemDropped(p_149650_1_,
				p_149650_2_, p_149650_3_);
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return blockMaterial == Material.glass ? 41 : 18;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_150101_M);
		field_150102_N = p_149651_1_.registerIcon(field_150100_a);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		float var5 = 0.4375F;
		float var6 = 0.5625F;
		float var7 = 0.4375F;
		float var8 = 0.5625F;
		final boolean var9 = func_150098_a(p_149719_1_.getBlock(p_149719_2_,
				p_149719_3_, p_149719_4_ - 1));
		final boolean var10 = func_150098_a(p_149719_1_.getBlock(p_149719_2_,
				p_149719_3_, p_149719_4_ + 1));
		final boolean var11 = func_150098_a(p_149719_1_.getBlock(
				p_149719_2_ - 1, p_149719_3_, p_149719_4_));
		final boolean var12 = func_150098_a(p_149719_1_.getBlock(
				p_149719_2_ + 1, p_149719_3_, p_149719_4_));

		if ((!var11 || !var12) && (var11 || var12 || var9 || var10)) {
			if (var11 && !var12) {
				var5 = 0.0F;
			} else if (!var11 && var12) {
				var6 = 1.0F;
			}
		} else {
			var5 = 0.0F;
			var6 = 1.0F;
		}

		if ((!var9 || !var10) && (var11 || var12 || var9 || var10)) {
			if (var9 && !var10) {
				var7 = 0.0F;
			} else if (!var9 && var10) {
				var8 = 1.0F;
			}
		} else {
			var7 = 0.0F;
			var8 = 1.0F;
		}

		setBlockBounds(var5, 0.0F, var7, var6, 1.0F, var8);
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_) == this ? false
				: super.shouldSideBeRendered(p_149646_1_, p_149646_2_,
						p_149646_3_, p_149646_4_, p_149646_5_);
	}
}
