package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class BlockHay extends BlockRotatedPillar {

	public BlockHay() {
		super(Material.grass);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	protected IIcon func_150163_b(int p_150163_1_) {
		return blockIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150164_N = p_149651_1_.registerIcon(getTextureName() + "_top");
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
	}
}
