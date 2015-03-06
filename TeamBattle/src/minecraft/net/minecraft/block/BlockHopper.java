package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHopper extends BlockContainer {
	public static IIcon func_149916_e(String p_149916_0_) {
		return p_149916_0_.equals("hopper_outside") ? Blocks.hopper.field_149921_b
				: p_149916_0_.equals("hopper_inside") ? Blocks.hopper.field_149924_N
						: null;
	}

	public static boolean func_149917_c(int p_149917_0_) {
		return (p_149917_0_ & 8) != 8;
	}

	public static int func_149918_b(int p_149918_0_) {
		return p_149918_0_ & 7;
	}

	public static TileEntityHopper func_149920_e(IBlockAccess p_149920_0_,
			int p_149920_1_, int p_149920_2_, int p_149920_3_) {
		return (TileEntityHopper) p_149920_0_.getTileEntity(p_149920_1_,
				p_149920_2_, p_149920_3_);
	}

	private IIcon field_149921_b;

	private final Random field_149922_a = new Random();

	private IIcon field_149923_M;

	private IIcon field_149924_N;

	public BlockHopper() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabRedstone);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		final float var8 = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, var8, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var8);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(1.0F - var8, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 1.0F - var8, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		final TileEntityHopper var7 = (TileEntityHopper) p_149749_1_
				.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

		if (var7 != null) {
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
				final ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null) {
					final float var10 = field_149922_a.nextFloat() * 0.8F + 0.1F;
					final float var11 = field_149922_a.nextFloat() * 0.8F + 0.1F;
					final float var12 = field_149922_a.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0) {
						int var13 = field_149922_a.nextInt(21) + 10;

						if (var13 > var9.stackSize) {
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						final EntityItem var14 = new EntityItem(p_149749_1_,
								p_149749_2_ + var10, p_149749_3_ + var11,
								p_149749_4_ + var12, new ItemStack(
										var9.getItem(), var13,
										var9.getItemDamage()));

						if (var9.hasTagCompound()) {
							var14.getEntityItem().setTagCompound(
									(NBTTagCompound) var9.getTagCompound()
											.copy());
						}

						final float var15 = 0.05F;
						var14.motionX = (float) field_149922_a.nextGaussian()
								* var15;
						var14.motionY = (float) field_149922_a.nextGaussian()
								* var15 + 0.2F;
						var14.motionZ = (float) field_149922_a.nextGaussian()
								* var15;
						p_149749_1_.spawnEntityInWorld(var14);
					}
				}
			}

			p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_,
					p_149749_5_);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_5_, p_149749_6_);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityHopper();
	}

	private void func_149919_e(World p_149919_1_, int p_149919_2_,
			int p_149919_3_, int p_149919_4_) {
		final int var5 = p_149919_1_.getBlockMetadata(p_149919_2_, p_149919_3_,
				p_149919_4_);
		final int var6 = func_149918_b(var5);
		final boolean var7 = !p_149919_1_.isBlockIndirectlyGettingPowered(
				p_149919_2_, p_149919_3_, p_149919_4_);
		final boolean var8 = func_149917_c(var5);

		if (var7 != var8) {
			p_149919_1_.setBlockMetadataWithNotify(p_149919_2_, p_149919_3_,
					p_149919_4_, var6 | (var7 ? 0 : 8), 4);
		}
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_,
			int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		return Container.calcRedstoneFromInventory(func_149920_e(p_149736_1_,
				p_149736_2_, p_149736_3_, p_149736_4_));
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149923_M : field_149921_b;
	}

	/**
	 * Gets the icon name of the ItemBlock corresponding to this block. Used by
	 * hoppers.
	 */
	@Override
	public String getItemIconName() {
		return "hopper";
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 38;
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
			final TileEntityHopper var10 = func_149920_e(p_149727_1_,
					p_149727_2_, p_149727_3_, p_149727_4_);

			if (var10 != null) {
				p_149727_5_.func_146093_a(var10);
			}

			return true;
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		func_149919_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_,
			int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_,
			int p_149660_9_) {
		int var10 = Facing.oppositeSide[p_149660_5_];

		if (var10 == 1) {
			var10 = 0;
		}

		return var10;
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
			final TileEntityHopper var7 = func_149920_e(p_149689_1_,
					p_149689_2_, p_149689_3_, p_149689_4_);
			var7.func_145886_a(p_149689_6_.getDisplayName());
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		func_149919_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149921_b = p_149651_1_.registerIcon("hopper_outside");
		field_149923_M = p_149651_1_.registerIcon("hopper_top");
		field_149924_N = p_149651_1_.registerIcon("hopper_inside");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}
}
