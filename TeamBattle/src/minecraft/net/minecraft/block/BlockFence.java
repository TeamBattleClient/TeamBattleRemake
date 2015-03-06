package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block {
	public static boolean func_149825_a(Block p_149825_0_) {
		return p_149825_0_ == Blocks.fence
				|| p_149825_0_ == Blocks.nether_brick_fence;
	}

	private final String field_149827_a;

	public BlockFence(String p_i45406_1_, Material p_i45406_2_) {
		super(p_i45406_2_);
		field_149827_a = p_i45406_1_;
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) {
		final boolean var8 = func_149826_e(p_149743_1_, p_149743_2_,
				p_149743_3_, p_149743_4_ - 1);
		final boolean var9 = func_149826_e(p_149743_1_, p_149743_2_,
				p_149743_3_, p_149743_4_ + 1);
		final boolean var10 = func_149826_e(p_149743_1_, p_149743_2_ - 1,
				p_149743_3_, p_149743_4_);
		final boolean var11 = func_149826_e(p_149743_1_, p_149743_2_ + 1,
				p_149743_3_, p_149743_4_);
		float var12 = 0.375F;
		float var13 = 0.625F;
		float var14 = 0.375F;
		float var15 = 0.625F;

		if (var8) {
			var14 = 0.0F;
		}

		if (var9) {
			var15 = 1.0F;
		}

		if (var8 || var9) {
			setBlockBounds(var12, 0.0F, var14, var13, 1.5F, var15);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		var14 = 0.375F;
		var15 = 0.625F;

		if (var10) {
			var12 = 0.0F;
		}

		if (var11) {
			var13 = 1.0F;
		}

		if (var10 || var11 || !var8 && !var9) {
			setBlockBounds(var12, 0.0F, var14, var13, 1.5F, var15);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		if (var8) {
			var14 = 0.0F;
		}

		if (var9) {
			var15 = 1.0F;
		}

		setBlockBounds(var12, 0.0F, var14, var13, 1.0F, var15);
	}

	public boolean func_149826_e(IBlockAccess p_149826_1_, int p_149826_2_,
			int p_149826_3_, int p_149826_4_) {
		final Block var5 = p_149826_1_.getBlock(p_149826_2_, p_149826_3_,
				p_149826_4_);
		return var5 != this && var5 != Blocks.fence_gate ? var5.blockMaterial
				.isOpaque() && var5.renderAsNormalBlock() ? var5.blockMaterial != Material.field_151572_C
				: false
				: true;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_,
			int p_149655_3_, int p_149655_4_) {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 11;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_,
			int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_,
			int p_149727_6_, float p_149727_7_, float p_149727_8_,
			float p_149727_9_) {
		return p_149727_1_.isClient ? true : ItemLead
				.func_150909_a(p_149727_5_, p_149727_1_, p_149727_2_,
						p_149727_3_, p_149727_4_);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_149827_a);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		final boolean var5 = func_149826_e(p_149719_1_, p_149719_2_,
				p_149719_3_, p_149719_4_ - 1);
		final boolean var6 = func_149826_e(p_149719_1_, p_149719_2_,
				p_149719_3_, p_149719_4_ + 1);
		final boolean var7 = func_149826_e(p_149719_1_, p_149719_2_ - 1,
				p_149719_3_, p_149719_4_);
		final boolean var8 = func_149826_e(p_149719_1_, p_149719_2_ + 1,
				p_149719_3_, p_149719_4_);
		float var9 = 0.375F;
		float var10 = 0.625F;
		float var11 = 0.375F;
		float var12 = 0.625F;

		if (var5) {
			var11 = 0.0F;
		}

		if (var6) {
			var12 = 1.0F;
		}

		if (var7) {
			var9 = 0.0F;
		}

		if (var8) {
			var10 = 1.0F;
		}

		setBlockBounds(var9, 0.0F, var11, var10, 1.0F, var12);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}
}
