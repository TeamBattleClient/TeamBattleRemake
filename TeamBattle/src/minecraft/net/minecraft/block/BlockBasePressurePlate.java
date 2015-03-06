package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block {
	private final String field_150067_a;

	protected BlockBasePressurePlate(String p_i45387_1_, Material p_i45387_2_) {
		super(p_i45387_2_);
		field_150067_a = p_i45387_1_;
		setCreativeTab(CreativeTabs.tabRedstone);
		setTickRandomly(true);
		func_150063_b(func_150066_d(15));
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		if (func_150060_c(p_149749_6_) > 0) {
			func_150064_a_(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_5_, p_149749_6_);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_,
			int p_149742_3_, int p_149742_4_) {
		return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_,
				p_149742_3_ - 1, p_149742_4_)
				|| BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_,
						p_149742_3_ - 1, p_149742_4_));
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this
	 * change based on its state.
	 */
	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return 20;
	}

	protected abstract int func_150060_c(int p_150060_1_);

	protected AxisAlignedBB func_150061_a(int p_150061_1_, int p_150061_2_,
			int p_150061_3_) {
		final float var4 = 0.125F;
		return AxisAlignedBB.getBoundingBox(p_150061_1_ + var4, p_150061_2_,
				p_150061_3_ + var4, p_150061_1_ + 1 - var4,
				p_150061_2_ + 0.25D, p_150061_3_ + 1 - var4);
	}

	protected void func_150062_a(World p_150062_1_, int p_150062_2_,
			int p_150062_3_, int p_150062_4_, int p_150062_5_) {
		final int var6 = func_150065_e(p_150062_1_, p_150062_2_, p_150062_3_,
				p_150062_4_);
		final boolean var7 = p_150062_5_ > 0;
		final boolean var8 = var6 > 0;

		if (p_150062_5_ != var6) {
			p_150062_1_.setBlockMetadataWithNotify(p_150062_2_, p_150062_3_,
					p_150062_4_, func_150066_d(var6), 2);
			func_150064_a_(p_150062_1_, p_150062_2_, p_150062_3_, p_150062_4_);
			p_150062_1_.markBlockRangeForRenderUpdate(p_150062_2_, p_150062_3_,
					p_150062_4_, p_150062_2_, p_150062_3_, p_150062_4_);
		}

		if (!var8 && var7) {
			p_150062_1_.playSoundEffect(p_150062_2_ + 0.5D, p_150062_3_ + 0.1D,
					p_150062_4_ + 0.5D, "random.click", 0.3F, 0.5F);
		} else if (var8 && !var7) {
			p_150062_1_.playSoundEffect(p_150062_2_ + 0.5D, p_150062_3_ + 0.1D,
					p_150062_4_ + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if (var8) {
			p_150062_1_.scheduleBlockUpdate(p_150062_2_, p_150062_3_,
					p_150062_4_, this, func_149738_a(p_150062_1_));
		}
	}

	protected void func_150063_b(int p_150063_1_) {
		final boolean var2 = func_150060_c(p_150063_1_) > 0;
		final float var3 = 0.0625F;

		if (var2) {
			setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.03125F, 1.0F - var3);
		} else {
			setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.0625F, 1.0F - var3);
		}
	}

	protected void func_150064_a_(World p_150064_1_, int p_150064_2_,
			int p_150064_3_, int p_150064_4_) {
		p_150064_1_.notifyBlocksOfNeighborChange(p_150064_2_, p_150064_3_,
				p_150064_4_, this);
		p_150064_1_.notifyBlocksOfNeighborChange(p_150064_2_, p_150064_3_ - 1,
				p_150064_4_, this);
	}

	protected abstract int func_150065_e(World p_150065_1_, int p_150065_2_,
			int p_150065_3_, int p_150065_4_);

	protected abstract int func_150066_d(int p_150066_1_);

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_,
			int p_149655_3_, int p_149655_4_) {
		return true;
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

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_,
			int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
		return p_149748_5_ == 1 ? func_150060_c(p_149748_1_.getBlockMetadata(
				p_149748_2_, p_149748_3_, p_149748_4_)) : 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_,
			int p_149709_3_, int p_149709_4_, int p_149709_5_) {
		return func_150060_c(p_149709_1_.getBlockMetadata(p_149709_2_,
				p_149709_3_, p_149709_4_));
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_,
			int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		if (!p_149670_1_.isClient) {
			final int var6 = func_150060_c(p_149670_1_.getBlockMetadata(
					p_149670_2_, p_149670_3_, p_149670_4_));

			if (var6 == 0) {
				func_150062_a(p_149670_1_, p_149670_2_, p_149670_3_,
						p_149670_4_, var6);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		boolean var6 = false;

		if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_,
				p_149695_3_ - 1, p_149695_4_)
				&& !BlockFence.func_149825_a(p_149695_1_.getBlock(p_149695_2_,
						p_149695_3_ - 1, p_149695_4_))) {
			var6 = true;
		}

		if (var6) {
			dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_,
							p_149695_4_), 0);
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_150067_a);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		func_150063_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_,
				p_149719_4_));
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		final float var1 = 0.5F;
		final float var2 = 0.125F;
		final float var3 = 0.5F;
		setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1,
				0.5F + var2, 0.5F + var3);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isClient) {
			final int var6 = func_150060_c(p_149674_1_.getBlockMetadata(
					p_149674_2_, p_149674_3_, p_149674_4_));

			if (var6 > 0) {
				func_150062_a(p_149674_1_, p_149674_2_, p_149674_3_,
						p_149674_4_, var6);
			}
		}
	}
}
