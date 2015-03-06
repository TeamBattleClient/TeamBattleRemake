package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEnderChest extends BlockContainer {

	protected BlockEnderChest() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnderChest();
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.obsidian);
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 22;
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
		final InventoryEnderChest var10 = p_149727_5_.getInventoryEnderChest();
		final TileEntityEnderChest var11 = (TileEntityEnderChest) p_149727_1_
				.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

		if (var10 != null && var11 != null) {
			if (p_149727_1_.getBlock(p_149727_2_, p_149727_3_ + 1, p_149727_4_)
					.isNormalCube())
				return true;
			else if (p_149727_1_.isClient)
				return true;
			else {
				var10.func_146031_a(var11);
				p_149727_5_.displayGUIChest(var10);
				return true;
			}
		} else
			return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		byte var7 = 0;
		final int var8 = MathHelper
				.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (var8 == 0) {
			var7 = 2;
		}

		if (var8 == 1) {
			var7 = 5;
		}

		if (var8 == 2) {
			var7 = 3;
		}

		if (var8 == 3) {
			var7 = 4;
		}

		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
				p_149689_4_, var7, 2);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 8;
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		for (int var6 = 0; var6 < 3; ++var6) {
			p_149734_5_.nextFloat();
			final double var9 = p_149734_3_ + p_149734_5_.nextFloat();
			p_149734_5_.nextFloat();
			double var13 = 0.0D;
			double var15 = 0.0D;
			double var17 = 0.0D;
			final int var19 = p_149734_5_.nextInt(2) * 2 - 1;
			final int var20 = p_149734_5_.nextInt(2) * 2 - 1;
			var13 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			var15 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			var17 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			final double var11 = p_149734_4_ + 0.5D + 0.25D * var20;
			var17 = p_149734_5_.nextFloat() * 1.0F * var20;
			final double var7 = p_149734_2_ + 0.5D + 0.25D * var19;
			var13 = p_149734_5_.nextFloat() * 1.0F * var19;
			p_149734_1_.spawnParticle("portal", var7, var9, var11, var13,
					var15, var17);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("obsidian");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
