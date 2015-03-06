package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockDirectional implements IGrowable {
	public static int func_149987_c(int p_149987_0_) {
		return (p_149987_0_ & 12) >> 2;
	}

	private IIcon[] field_149989_a;

	public BlockCocoa() {
		super(Material.plants);
		setTickRandomly(true);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_,
			int p_149718_3_, int p_149718_4_) {
		final int var5 = func_149895_l(p_149718_1_.getBlockMetadata(
				p_149718_2_, p_149718_3_, p_149718_4_));
		p_149718_2_ += Direction.offsetX[var5];
		p_149718_4_ += Direction.offsetZ[var5];
		final Block var6 = p_149718_1_.getBlock(p_149718_2_, p_149718_3_,
				p_149718_4_);
		return var6 == Blocks.log
				&& BlockLog.func_150165_c(p_149718_1_.getBlockMetadata(
						p_149718_2_, p_149718_3_, p_149718_4_)) == 3;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified
	 * items
	 */
	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_,
			int p_149690_3_, int p_149690_4_, int p_149690_5_,
			float p_149690_6_, int p_149690_7_) {
		final int var8 = func_149987_c(p_149690_5_);
		byte var9 = 1;

		if (var8 >= 2) {
			var9 = 3;
		}

		for (int var10 = 0; var10 < var9; ++var10) {
			dropBlockAsItem_do(p_149690_1_, p_149690_2_, p_149690_3_,
					p_149690_4_, new ItemStack(Items.dye, 1, 3));
		}
	}

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_,
			int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
		final int var6 = p_149851_1_.getBlockMetadata(p_149851_2_, p_149851_3_,
				p_149851_4_);
		final int var7 = func_149987_c(var6);
		return var7 < 2;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_,
			int p_149852_3_, int p_149852_4_, int p_149852_5_) {
		return true;
	}

	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_,
			int p_149853_3_, int p_149853_4_, int p_149853_5_) {
		final int var6 = p_149853_1_.getBlockMetadata(p_149853_3_, p_149853_4_,
				p_149853_5_);
		final int var7 = BlockDirectional.func_149895_l(var6);
		int var8 = func_149987_c(var6);
		++var8;
		p_149853_1_.setBlockMetadataWithNotify(p_149853_3_, p_149853_4_,
				p_149853_5_, var8 << 2 | var7, 2);
	}

	public IIcon func_149988_b(int p_149988_1_) {
		if (p_149988_1_ < 0 || p_149988_1_ >= field_149989_a.length) {
			p_149988_1_ = field_149989_a.length - 1;
		}

		return field_149989_a[p_149988_1_];
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_,
				p_149668_4_);
		return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_,
				p_149668_3_, p_149668_4_);
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	@Override
	public int getDamageValue(World p_149643_1_, int p_149643_2_,
			int p_149643_3_, int p_149643_4_) {
		return 3;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return field_149989_a[2];
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Items.dye;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 28;
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
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_,
			int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_,
			int p_149660_9_) {
		if (p_149660_5_ == 1 || p_149660_5_ == 0) {
			p_149660_5_ = 2;
		}

		return Direction.rotateOpposite[Direction.facingToDirection[p_149660_5_]];
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		final int var7 = ((MathHelper
				.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) + 0) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
				p_149689_4_, var7, 2);
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		if (!canBlockStay(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_,
							p_149695_4_), 0);
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_,
					getBlockById(0), 0, 2);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149989_a = new IIcon[3];

		for (int var2 = 0; var2 < field_149989_a.length; ++var2) {
			field_149989_a[var2] = p_149651_1_.registerIcon(getTextureName()
					+ "_stage_" + var2);
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		final int var5 = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_,
				p_149719_4_);
		final int var6 = func_149895_l(var5);
		final int var7 = func_149987_c(var5);
		final int var8 = 4 + var7 * 2;
		final int var9 = 5 + var7 * 2;
		final float var10 = var8 / 2.0F;

		switch (var6) {
		case 0:
			setBlockBounds((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F,
					(15.0F - var8) / 16.0F, (8.0F + var10) / 16.0F, 0.75F,
					0.9375F);
			break;

		case 1:
			setBlockBounds(0.0625F, (12.0F - var9) / 16.0F,
					(8.0F - var10) / 16.0F, (1.0F + var8) / 16.0F, 0.75F,
					(8.0F + var10) / 16.0F);
			break;

		case 2:
			setBlockBounds((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F,
					0.0625F, (8.0F + var10) / 16.0F, 0.75F,
					(1.0F + var8) / 16.0F);
			break;

		case 3:
			setBlockBounds((15.0F - var8) / 16.0F, (12.0F - var9) / 16.0F,
					(8.0F - var10) / 16.0F, 0.9375F, 0.75F,
					(8.0F + var10) / 16.0F);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!canBlockStay(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_)) {
			dropBlockAsItem(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
					p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_,
							p_149674_4_), 0);
			p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_,
					getBlockById(0), 0, 2);
		} else if (p_149674_1_.rand.nextInt(5) == 0) {
			final int var6 = p_149674_1_.getBlockMetadata(p_149674_2_,
					p_149674_3_, p_149674_4_);
			int var7 = func_149987_c(var6);

			if (var7 < 2) {
				++var7;
				p_149674_1_.setBlockMetadataWithNotify(p_149674_2_,
						p_149674_3_, p_149674_4_, var7 << 2
								| func_149895_l(var6), 2);
			}
		}
	}
}
