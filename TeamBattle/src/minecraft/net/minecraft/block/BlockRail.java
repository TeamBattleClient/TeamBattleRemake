package net.minecraft.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase {
	private IIcon field_150056_b;

	protected BlockRail() {
		super(false);
	}

	@Override
	protected void func_150048_a(World p_150048_1_, int p_150048_2_,
			int p_150048_3_, int p_150048_4_, int p_150048_5_, int p_150048_6_,
			Block p_150048_7_) {
		if (p_150048_7_.canProvidePower()
				&& new BlockRailBase.Rail(p_150048_1_, p_150048_2_,
						p_150048_3_, p_150048_4_).func_150650_a() == 3) {
			func_150052_a(p_150048_1_, p_150048_2_, p_150048_3_, p_150048_4_,
					false);
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_2_ >= 6 ? field_150056_b : blockIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		super.registerBlockIcons(p_149651_1_);
		field_150056_b = p_149651_1_.registerIcon(getTextureName() + "_turned");
	}
}
