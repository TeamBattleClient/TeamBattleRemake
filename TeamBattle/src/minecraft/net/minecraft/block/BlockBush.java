package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBush extends Block {

	protected BlockBush() {
		this(Material.plants);
	}

	protected BlockBush(Material p_i45395_1_) {
		super(p_i45395_1_);
		setTickRandomly(true);
		final float var2 = 0.2F;
		setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2,
				var2 * 3.0F, 0.5F + var2);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_,
			int p_149718_3_, int p_149718_4_) {
		return func_149854_a(p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1,
				p_149718_4_));
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_,
			int p_149742_3_, int p_149742_4_) {
		return super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_,
				p_149742_4_)
				&& func_149854_a(p_149742_1_.getBlock(p_149742_2_,
						p_149742_3_ - 1, p_149742_4_));
	}

	protected boolean func_149854_a(Block p_149854_1_) {
		return p_149854_1_ == Blocks.grass || p_149854_1_ == Blocks.dirt
				|| p_149854_1_ == Blocks.farmland;
	}

	protected void func_149855_e(World p_149855_1_, int p_149855_2_,
			int p_149855_3_, int p_149855_4_) {
		if (!canBlockStay(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_)) {
			dropBlockAsItem(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_,
					p_149855_1_.getBlockMetadata(p_149855_2_, p_149855_3_,
							p_149855_4_), 0);
			p_149855_1_.setBlock(p_149855_2_, p_149855_3_, p_149855_4_,
					getBlockById(0), 0, 2);
		}
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_,
				p_149695_4_, p_149695_5_);
		func_149855_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		func_149855_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
	}
}
