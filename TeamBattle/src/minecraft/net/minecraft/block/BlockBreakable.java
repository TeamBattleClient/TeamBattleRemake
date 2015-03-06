package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable extends Block {
	private final String field_149995_b;
	private final boolean field_149996_a;

	protected BlockBreakable(String p_i45411_1_, Material p_i45411_2_,
			boolean p_i45411_3_) {
		super(p_i45411_2_);
		field_149996_a = p_i45411_3_;
		field_149995_b = p_i45411_1_;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_149995_b);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		final Block var6 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_,
				p_149646_4_);

		if (this == Blocks.glass || this == Blocks.stained_glass) {
			if (p_149646_1_.getBlockMetadata(p_149646_2_, p_149646_3_,
					p_149646_4_) != p_149646_1_.getBlockMetadata(p_149646_2_
					- Facing.offsetsXForSide[p_149646_5_], p_149646_3_
					- Facing.offsetsYForSide[p_149646_5_], p_149646_4_
					- Facing.offsetsZForSide[p_149646_5_]))
				return true;

			if (var6 == this)
				return false;
		}

		return !field_149996_a && var6 == this ? false : super
				.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_,
						p_149646_4_, p_149646_5_);
	}
}
