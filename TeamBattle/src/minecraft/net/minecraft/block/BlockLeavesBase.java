package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block {
	protected boolean field_150121_P;

	protected BlockLeavesBase(Material p_i45433_1_, boolean p_i45433_2_) {
		super(p_i45433_1_);
		field_150121_P = p_i45433_2_;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		final Block var6 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_,
				p_149646_4_);
		return !field_150121_P && var6 == this ? false : super
				.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_,
						p_149646_4_, p_149646_5_);
	}
}
