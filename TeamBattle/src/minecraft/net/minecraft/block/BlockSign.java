package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSign extends BlockContainer {
	private final boolean field_149967_b;
	private final Class field_149968_a;

	protected BlockSign(Class p_i45426_1_, boolean p_i45426_2_) {
		super(Material.wood);
		field_149967_b = p_i45426_2_;
		field_149968_a = p_i45426_1_;
		final float var3 = 0.25F;
		final float var4 = 1.0F;
		setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4,
				0.5F + var3);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		try {
			return (TileEntity) field_149968_a.newInstance();
		} catch (final Exception var4) {
			throw new RuntimeException(var4);
		}
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_,
			int p_149655_3_, int p_149655_4_) {
		return true;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.planks.getBlockTextureFromSide(p_149691_1_);
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Items.sign;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Items.sign;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return -1;
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_,
			int p_149633_2_, int p_149633_3_, int p_149633_4_) {
		setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_,
				p_149633_4_);
		return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_,
				p_149633_3_, p_149633_4_);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		boolean var6 = false;

		if (field_149967_b) {
			if (!p_149695_1_
					.getBlock(p_149695_2_, p_149695_3_ - 1, p_149695_4_)
					.getMaterial().isSolid()) {
				var6 = true;
			}
		} else {
			final int var7 = p_149695_1_.getBlockMetadata(p_149695_2_,
					p_149695_3_, p_149695_4_);
			var6 = true;

			if (var7 == 2
					&& p_149695_1_
							.getBlock(p_149695_2_, p_149695_3_, p_149695_4_ + 1)
							.getMaterial().isSolid()) {
				var6 = false;
			}

			if (var7 == 3
					&& p_149695_1_
							.getBlock(p_149695_2_, p_149695_3_, p_149695_4_ - 1)
							.getMaterial().isSolid()) {
				var6 = false;
			}

			if (var7 == 4
					&& p_149695_1_
							.getBlock(p_149695_2_ + 1, p_149695_3_, p_149695_4_)
							.getMaterial().isSolid()) {
				var6 = false;
			}

			if (var7 == 5
					&& p_149695_1_
							.getBlock(p_149695_2_ - 1, p_149695_3_, p_149695_4_)
							.getMaterial().isSolid()) {
				var6 = false;
			}
		}

		if (var6) {
			dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_,
							p_149695_4_), 0);
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
		}

		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_,
				p_149695_4_, p_149695_5_);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		if (!field_149967_b) {
			final int var5 = p_149719_1_.getBlockMetadata(p_149719_2_,
					p_149719_3_, p_149719_4_);
			final float var6 = 0.28125F;
			final float var7 = 0.78125F;
			final float var8 = 0.0F;
			final float var9 = 1.0F;
			final float var10 = 0.125F;
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

			if (var5 == 2) {
				setBlockBounds(var8, var6, 1.0F - var10, var9, var7, 1.0F);
			}

			if (var5 == 3) {
				setBlockBounds(var8, var6, 0.0F, var9, var7, var10);
			}

			if (var5 == 4) {
				setBlockBounds(1.0F - var10, var6, var8, 1.0F, var7, var9);
			}

			if (var5 == 5) {
				setBlockBounds(0.0F, var6, var8, var10, var7, var9);
			}
		}
	}
}
