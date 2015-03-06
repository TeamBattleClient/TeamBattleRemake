package net.minecraft.block;

import net.minecraft.block.material.Material;

public abstract class BlockDirectional extends Block {

	public static int func_149895_l(int p_149895_0_) {
		return p_149895_0_ & 3;
	}

	protected BlockDirectional(Material p_i45401_1_) {
		super(p_i45401_1_);
	}
}
