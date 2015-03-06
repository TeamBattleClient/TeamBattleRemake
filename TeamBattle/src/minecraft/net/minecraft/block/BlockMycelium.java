package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium extends Block {
	private IIcon field_150199_b;
	private IIcon field_150200_a;

	protected BlockMycelium() {
		super(Material.grass);
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_,
			int p_149673_3_, int p_149673_4_, int p_149673_5_) {
		if (p_149673_5_ == 1)
			return field_150200_a;
		else if (p_149673_5_ == 0)
			return Blocks.dirt.getBlockTextureFromSide(p_149673_5_);
		else {
			final Material var6 = p_149673_1_.getBlock(p_149673_2_,
					p_149673_3_ + 1, p_149673_4_).getMaterial();
			return var6 != Material.field_151597_y
					&& var6 != Material.craftedSnow ? blockIcon
					: field_150199_b;
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_150200_a
				: p_149691_1_ == 0 ? Blocks.dirt
						.getBlockTextureFromSide(p_149691_1_) : blockIcon;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Blocks.dirt.getItemDropped(0, p_149650_2_, p_149650_3_);
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		super.randomDisplayTick(p_149734_1_, p_149734_2_, p_149734_3_,
				p_149734_4_, p_149734_5_);

		if (p_149734_5_.nextInt(10) == 0) {
			p_149734_1_.spawnParticle("townaura",
					p_149734_2_ + p_149734_5_.nextFloat(), p_149734_3_ + 1.1F,
					p_149734_4_ + p_149734_5_.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150200_a = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150199_b = p_149651_1_.registerIcon("grass_side_snowed");
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isClient) {
			if (p_149674_1_.getBlockLightValue(p_149674_2_, p_149674_3_ + 1,
					p_149674_4_) < 4
					&& p_149674_1_.getBlock(p_149674_2_, p_149674_3_ + 1,
							p_149674_4_).getLightOpacity() > 2) {
				p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_,
						Blocks.dirt);
			} else if (p_149674_1_.getBlockLightValue(p_149674_2_,
					p_149674_3_ + 1, p_149674_4_) >= 9) {
				for (int var6 = 0; var6 < 4; ++var6) {
					final int var7 = p_149674_2_ + p_149674_5_.nextInt(3) - 1;
					final int var8 = p_149674_3_ + p_149674_5_.nextInt(5) - 3;
					final int var9 = p_149674_4_ + p_149674_5_.nextInt(3) - 1;
					final Block var10 = p_149674_1_.getBlock(var7, var8 + 1,
							var9);

					if (p_149674_1_.getBlock(var7, var8, var9) == Blocks.dirt
							&& p_149674_1_.getBlockMetadata(var7, var8, var9) == 0
							&& p_149674_1_.getBlockLightValue(var7, var8 + 1,
									var9) >= 4 && var10.getLightOpacity() <= 2) {
						p_149674_1_.setBlock(var7, var8, var9, this);
					}
				}
			}
		}
	}
}
