package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWorkbench extends Block {
	private IIcon field_150034_b;
	private IIcon field_150035_a;

	protected BlockWorkbench() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_150035_a
				: p_149691_1_ == 0 ? Blocks.planks
						.getBlockTextureFromSide(p_149691_1_)
						: p_149691_1_ != 2 && p_149691_1_ != 4 ? blockIcon
								: field_150034_b;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_,
			int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_,
			int p_149727_6_, float p_149727_7_, float p_149727_8_,
			float p_149727_9_) {
		if (p_149727_1_.isClient)
			return true;
		else {
			p_149727_5_.displayGUIWorkbench(p_149727_2_, p_149727_3_,
					p_149727_4_);
			return true;
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150035_a = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150034_b = p_149651_1_.registerIcon(getTextureName() + "_front");
	}
}
