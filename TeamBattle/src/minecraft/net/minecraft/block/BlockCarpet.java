package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet extends Block {

	protected BlockCarpet() {
		super(Material.carpet);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabDecorations);
		func_150089_b(0);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_,
			int p_149718_3_, int p_149718_4_) {
		return !p_149718_1_.isAirBlock(p_149718_2_, p_149718_3_ - 1,
				p_149718_4_);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_,
			int p_149742_3_, int p_149742_4_) {
		return super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_,
				p_149742_4_)
				&& canBlockStay(p_149742_1_, p_149742_2_, p_149742_3_,
						p_149742_4_);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	protected void func_150089_b(int p_150089_1_) {
		final byte var2 = 0;
		final float var3 = 1 * (1 + var2) / 16.0F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
	}

	private boolean func_150090_e(World p_150090_1_, int p_150090_2_,
			int p_150090_3_, int p_150090_4_) {
		if (!canBlockStay(p_150090_1_, p_150090_2_, p_150090_3_, p_150090_4_)) {
			dropBlockAsItem(p_150090_1_, p_150090_2_, p_150090_3_, p_150090_4_,
					p_150090_1_.getBlockMetadata(p_150090_2_, p_150090_3_,
							p_150090_4_), 0);
			p_150090_1_.setBlockToAir(p_150090_2_, p_150090_3_, p_150090_4_);
			return false;
		} else
			return true;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		final byte var5 = 0;
		final float var6 = 0.0625F;
		return AxisAlignedBB.getBoundingBox(p_149668_2_ + field_149759_B,
				p_149668_3_ + field_149760_C, p_149668_4_ + field_149754_D,
				p_149668_2_ + field_149755_E, p_149668_3_ + var5 * var6,
				p_149668_4_ + field_149757_G);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.wool.getIcon(p_149691_1_, p_149691_2_);
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
			List p_149666_3_) {
		for (int var4 = 0; var4 < 16; ++var4) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, var4));
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		func_150090_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		func_150089_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_,
				p_149719_4_));
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		func_150089_b(0);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return p_149646_5_ == 1 ? true : super
				.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_,
						p_149646_4_, p_149646_5_);
	}
}
