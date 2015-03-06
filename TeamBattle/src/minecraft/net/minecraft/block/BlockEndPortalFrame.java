package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEndPortalFrame extends Block {
	public static boolean func_150020_b(int p_150020_0_) {
		return (p_150020_0_ & 4) != 0;
	}

	private IIcon field_150022_b;

	private IIcon field_150023_a;

	public BlockEndPortalFrame() {
		super(Material.rock);
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		final int var8 = p_149743_1_.getBlockMetadata(p_149743_2_, p_149743_3_,
				p_149743_4_);

		if (func_150020_b(var8)) {
			setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		setBlockBoundsForItemRender();
	}

	public IIcon func_150021_e() {
		return field_150022_b;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_,
			int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		final int var6 = p_149736_1_.getBlockMetadata(p_149736_2_, p_149736_3_,
				p_149736_4_);
		return func_150020_b(var6) ? 15 : 0;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_150023_a
				: p_149691_1_ == 0 ? Blocks.end_stone
						.getBlockTextureFromSide(p_149691_1_) : blockIcon;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return null;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 26;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		final int var7 = ((MathHelper
				.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) + 2) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
				p_149689_4_, var7, 2);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150023_a = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150022_b = p_149651_1_.registerIcon(getTextureName() + "_eye");
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
	}
}
