package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer {
	private static boolean field_149934_M;

	public static void func_149931_a(boolean p_149931_0_, World p_149931_1_,
			int p_149931_2_, int p_149931_3_, int p_149931_4_) {
		final int var5 = p_149931_1_.getBlockMetadata(p_149931_2_, p_149931_3_,
				p_149931_4_);
		final TileEntity var6 = p_149931_1_.getTileEntity(p_149931_2_,
				p_149931_3_, p_149931_4_);
		field_149934_M = true;

		if (p_149931_0_) {
			p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_,
					Blocks.lit_furnace);
		} else {
			p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_,
					Blocks.furnace);
		}

		field_149934_M = false;
		p_149931_1_.setBlockMetadataWithNotify(p_149931_2_, p_149931_3_,
				p_149931_4_, var5, 2);

		if (var6 != null) {
			var6.validate();
			p_149931_1_.setTileEntity(p_149931_2_, p_149931_3_, p_149931_4_,
					var6);
		}
	}

	private final boolean field_149932_b;
	private final Random field_149933_a = new Random();
	private IIcon field_149935_N;

	private IIcon field_149936_O;

	protected BlockFurnace(boolean p_i45407_1_) {
		super(Material.rock);
		field_149932_b = p_i45407_1_;
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		if (!field_149934_M) {
			final TileEntityFurnace var7 = (TileEntityFurnace) p_149749_1_
					.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

			if (var7 != null) {
				for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
					final ItemStack var9 = var7.getStackInSlot(var8);

					if (var9 != null) {
						final float var10 = field_149933_a.nextFloat() * 0.8F + 0.1F;
						final float var11 = field_149933_a.nextFloat() * 0.8F + 0.1F;
						final float var12 = field_149933_a.nextFloat() * 0.8F + 0.1F;

						while (var9.stackSize > 0) {
							int var13 = field_149933_a.nextInt(21) + 10;

							if (var13 > var9.stackSize) {
								var13 = var9.stackSize;
							}

							var9.stackSize -= var13;
							final EntityItem var14 = new EntityItem(
									p_149749_1_, p_149749_2_ + var10,
									p_149749_3_ + var11, p_149749_4_ + var12,
									new ItemStack(var9.getItem(), var13,
											var9.getItemDamage()));

							if (var9.hasTagCompound()) {
								var14.getEntityItem().setTagCompound(
										(NBTTagCompound) var9.getTagCompound()
												.copy());
							}

							final float var15 = 0.05F;
							var14.motionX = (float) field_149933_a
									.nextGaussian() * var15;
							var14.motionY = (float) field_149933_a
									.nextGaussian() * var15 + 0.2F;
							var14.motionZ = (float) field_149933_a
									.nextGaussian() * var15;
							p_149749_1_.spawnEntityInWorld(var14);
						}
					}
				}

				p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_,
						p_149749_4_, p_149749_5_);
			}
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
		return new TileEntityFurnace();
	}

	private void func_149930_e(World p_149930_1_, int p_149930_2_,
			int p_149930_3_, int p_149930_4_) {
		if (!p_149930_1_.isClient) {
			final Block var5 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_,
					p_149930_4_ - 1);
			final Block var6 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_,
					p_149930_4_ + 1);
			final Block var7 = p_149930_1_.getBlock(p_149930_2_ - 1,
					p_149930_3_, p_149930_4_);
			final Block var8 = p_149930_1_.getBlock(p_149930_2_ + 1,
					p_149930_3_, p_149930_4_);
			byte var9 = 3;

			if (var5.func_149730_j() && !var6.func_149730_j()) {
				var9 = 3;
			}

			if (var6.func_149730_j() && !var5.func_149730_j()) {
				var9 = 2;
			}

			if (var7.func_149730_j() && !var8.func_149730_j()) {
				var9 = 5;
			}

			if (var8.func_149730_j() && !var7.func_149730_j()) {
				var9 = 4;
			}

			p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_,
					p_149930_4_, var9, 2);
		}
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_,
			int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		return Container.calcRedstoneFromInventory((IInventory) p_149736_1_
				.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149935_N
				: p_149691_1_ == 0 ? field_149935_N
						: p_149691_1_ != p_149691_2_ ? blockIcon
								: field_149936_O;
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Item.getItemFromBlock(Blocks.furnace);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.furnace);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
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
			final TileEntityFurnace var10 = (TileEntityFurnace) p_149727_1_
					.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

			if (var10 != null) {
				p_149727_5_.func_146101_a(var10);
			}

			return true;
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		func_149930_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		final int var7 = MathHelper
				.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (var7 == 0) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 2, 2);
		}

		if (var7 == 1) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 5, 2);
		}

		if (var7 == 2) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 3, 2);
		}

		if (var7 == 3) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 4, 2);
		}

		if (p_149689_6_.hasDisplayName()) {
			((TileEntityFurnace) p_149689_1_.getTileEntity(p_149689_2_,
					p_149689_3_, p_149689_4_)).func_145951_a(p_149689_6_
					.getDisplayName());
		}
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		if (field_149932_b) {
			final int var6 = p_149734_1_.getBlockMetadata(p_149734_2_,
					p_149734_3_, p_149734_4_);
			final float var7 = p_149734_2_ + 0.5F;
			final float var8 = p_149734_3_ + 0.0F + p_149734_5_.nextFloat()
					* 6.0F / 16.0F;
			final float var9 = p_149734_4_ + 0.5F;
			final float var10 = 0.52F;
			final float var11 = p_149734_5_.nextFloat() * 0.6F - 0.3F;

			if (var6 == 4) {
				p_149734_1_.spawnParticle("smoke", var7 - var10, var8, var9
						+ var11, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", var7 - var10, var8, var9
						+ var11, 0.0D, 0.0D, 0.0D);
			} else if (var6 == 5) {
				p_149734_1_.spawnParticle("smoke", var7 + var10, var8, var9
						+ var11, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", var7 + var10, var8, var9
						+ var11, 0.0D, 0.0D, 0.0D);
			} else if (var6 == 2) {
				p_149734_1_.spawnParticle("smoke", var7 + var11, var8, var9
						- var10, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", var7 + var11, var8, var9
						- var10, 0.0D, 0.0D, 0.0D);
			} else if (var6 == 3) {
				p_149734_1_.spawnParticle("smoke", var7 + var11, var8, var9
						+ var10, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", var7 + var11, var8, var9
						+ var10, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("furnace_side");
		field_149936_O = p_149651_1_
				.registerIcon(field_149932_b ? "furnace_front_on"
						: "furnace_front_off");
		field_149935_N = p_149651_1_.registerIcon("furnace_top");
	}
}
