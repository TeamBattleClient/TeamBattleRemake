package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockDirectional {

	/**
	 * Returns if the fence gate is open according to its metadata.
	 */
	public static boolean isFenceGateOpen(int p_149896_0_) {
		return (p_149896_0_ & 4) != 0;
	}

	public BlockFenceGate() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_,
			int p_149742_3_, int p_149742_4_) {
		return !p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_)
				.getMaterial().isSolid() ? false : super.canPlaceBlockAt(
				p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_,
			int p_149655_3_, int p_149655_4_) {
		return isFenceGateOpen(p_149655_1_.getBlockMetadata(p_149655_2_,
				p_149655_3_, p_149655_4_));
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		final int var5 = p_149668_1_.getBlockMetadata(p_149668_2_, p_149668_3_,
				p_149668_4_);
		return isFenceGateOpen(var5) ? null
				: var5 != 2 && var5 != 0 ? AxisAlignedBB.getBoundingBox(
						p_149668_2_ + 0.375F, p_149668_3_, p_149668_4_,
						p_149668_2_ + 0.625F, p_149668_3_ + 1.5F,
						p_149668_4_ + 1) : AxisAlignedBB.getBoundingBox(
						p_149668_2_, p_149668_3_, p_149668_4_ + 0.375F,
						p_149668_2_ + 1, p_149668_3_ + 1.5F,
						p_149668_4_ + 0.625F);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.planks.getBlockTextureFromSide(p_149691_1_);
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 21;
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
		int var10 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_,
				p_149727_4_);

		if (isFenceGateOpen(var10)) {
			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_,
					p_149727_4_, var10 & -5, 2);
		} else {
			final int var11 = (MathHelper
					.floor_double(p_149727_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
			final int var12 = func_149895_l(var10);

			if (var12 == (var11 + 2) % 4) {
				var10 = var11;
			}

			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_,
					p_149727_4_, var10 | 4, 2);
		}

		p_149727_1_.playAuxSFXAtEntity(p_149727_5_, 1003, p_149727_2_,
				p_149727_3_, p_149727_4_, 0);
		return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		final int var7 = (MathHelper
				.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
				p_149689_4_, var7, 2);
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		if (!p_149695_1_.isClient) {
			final int var6 = p_149695_1_.getBlockMetadata(p_149695_2_,
					p_149695_3_, p_149695_4_);
			final boolean var7 = p_149695_1_.isBlockIndirectlyGettingPowered(
					p_149695_2_, p_149695_3_, p_149695_4_);

			if (var7 || p_149695_5_.canProvidePower()) {
				if (var7 && !isFenceGateOpen(var6)) {
					p_149695_1_.setBlockMetadataWithNotify(p_149695_2_,
							p_149695_3_, p_149695_4_, var6 | 4, 2);
					p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003,
							p_149695_2_, p_149695_3_, p_149695_4_, 0);
				} else if (!var7 && isFenceGateOpen(var6)) {
					p_149695_1_.setBlockMetadataWithNotify(p_149695_2_,
							p_149695_3_, p_149695_4_, var6 & -5, 2);
					p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003,
							p_149695_2_, p_149695_3_, p_149695_4_, 0);
				}
			}
		}
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
		final int var5 = func_149895_l(p_149719_1_.getBlockMetadata(
				p_149719_2_, p_149719_3_, p_149719_4_));

		if (var5 != 2 && var5 != 0) {
			setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}
}
