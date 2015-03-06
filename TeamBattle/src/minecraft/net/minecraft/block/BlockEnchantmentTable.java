package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEnchantmentTable extends BlockContainer {
	private IIcon field_149949_b;
	private IIcon field_149950_a;

	protected BlockEnchantmentTable() {
		super(Material.rock);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		setLightOpacity(0);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnchantmentTable();
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 0 ? field_149949_b
				: p_149691_1_ == 1 ? field_149950_a : blockIcon;
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
		if (p_149727_1_.isClient)
			return true;
		else {
			final TileEntityEnchantmentTable var10 = (TileEntityEnchantmentTable) p_149727_1_
					.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);
			p_149727_5_.displayGUIEnchantment(p_149727_2_, p_149727_3_,
					p_149727_4_, var10.func_145921_b() ? var10.func_145919_a()
							: null);
			return true;
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_,
				p_149689_4_, p_149689_5_, p_149689_6_);

		if (p_149689_6_.hasDisplayName()) {
			((TileEntityEnchantmentTable) p_149689_1_.getTileEntity(
					p_149689_2_, p_149689_3_, p_149689_4_))
					.func_145920_a(p_149689_6_.getDisplayName());
		}
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

		for (int var6 = p_149734_2_ - 2; var6 <= p_149734_2_ + 2; ++var6) {
			for (int var7 = p_149734_4_ - 2; var7 <= p_149734_4_ + 2; ++var7) {
				if (var6 > p_149734_2_ - 2 && var6 < p_149734_2_ + 2
						&& var7 == p_149734_4_ - 1) {
					var7 = p_149734_4_ + 2;
				}

				if (p_149734_5_.nextInt(16) == 0) {
					for (int var8 = p_149734_3_; var8 <= p_149734_3_ + 1; ++var8) {
						if (p_149734_1_.getBlock(var6, var8, var7) == Blocks.bookshelf) {
							if (!p_149734_1_.isAirBlock((var6 - p_149734_2_)
									/ 2 + p_149734_2_, var8,
									(var7 - p_149734_4_) / 2 + p_149734_4_)) {
								break;
							}

							p_149734_1_.spawnParticle(
									"enchantmenttable",
									p_149734_2_ + 0.5D,
									p_149734_3_ + 2.0D,
									p_149734_4_ + 0.5D,
									var6 - p_149734_2_
											+ p_149734_5_.nextFloat() - 0.5D,
									var8 - p_149734_3_
											- p_149734_5_.nextFloat() - 1.0F,
									var7 - p_149734_4_
											+ p_149734_5_.nextFloat() - 0.5D);
						}
					}
				}
			}
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_" + "side");
		field_149950_a = p_149651_1_.registerIcon(getTextureName() + "_"
				+ "top");
		field_149949_b = p_149651_1_.registerIcon(getTextureName() + "_"
				+ "bottom");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
