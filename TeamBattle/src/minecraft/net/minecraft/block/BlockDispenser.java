package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer {
	public static final IRegistry dispenseBehaviorRegistry = new RegistryDefaulted(
			new BehaviorDefaultDispenseItem());

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 7);
	}

	public static IPosition func_149939_a(IBlockSource p_149939_0_) {
		final EnumFacing var1 = func_149937_b(p_149939_0_.getBlockMetadata());
		final double var2 = p_149939_0_.getX() + 0.7D * var1.getFrontOffsetX();
		final double var4 = p_149939_0_.getY() + 0.7D * var1.getFrontOffsetY();
		final double var6 = p_149939_0_.getZ() + 0.7D * var1.getFrontOffsetZ();
		return new PositionImpl(var2, var4, var6);
	}

	protected Random field_149942_b = new Random();
	protected IIcon field_149944_M;

	protected IIcon field_149945_N;

	protected IIcon field_149946_O;

	protected BlockDispenser() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		final TileEntityDispenser var7 = (TileEntityDispenser) p_149749_1_
				.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

		if (var7 != null) {
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
				final ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null) {
					final float var10 = field_149942_b.nextFloat() * 0.8F + 0.1F;
					final float var11 = field_149942_b.nextFloat() * 0.8F + 0.1F;
					final float var12 = field_149942_b.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0) {
						int var13 = field_149942_b.nextInt(21) + 10;

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
						var14.motionX = (float) field_149942_b.nextGaussian()
								* var15;
						var14.motionY = (float) field_149942_b.nextGaussian()
								* var15 + 0.2F;
						var14.motionZ = (float) field_149942_b.nextGaussian()
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
		return new TileEntityDispenser();
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return 4;
	}

	private void func_149938_m(World p_149938_1_, int p_149938_2_,
			int p_149938_3_, int p_149938_4_) {
		if (!p_149938_1_.isClient) {
			final Block var5 = p_149938_1_.getBlock(p_149938_2_, p_149938_3_,
					p_149938_4_ - 1);
			final Block var6 = p_149938_1_.getBlock(p_149938_2_, p_149938_3_,
					p_149938_4_ + 1);
			final Block var7 = p_149938_1_.getBlock(p_149938_2_ - 1,
					p_149938_3_, p_149938_4_);
			final Block var8 = p_149938_1_.getBlock(p_149938_2_ + 1,
					p_149938_3_, p_149938_4_);
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

			p_149938_1_.setBlockMetadataWithNotify(p_149938_2_, p_149938_3_,
					p_149938_4_, var9, 2);
		}
	}

	protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_) {
		return (IBehaviorDispenseItem) dispenseBehaviorRegistry
				.getObject(p_149940_1_.getItem());
	}

	protected void func_149941_e(World p_149941_1_, int p_149941_2_,
			int p_149941_3_, int p_149941_4_) {
		final BlockSourceImpl var5 = new BlockSourceImpl(p_149941_1_,
				p_149941_2_, p_149941_3_, p_149941_4_);
		final TileEntityDispenser var6 = (TileEntityDispenser) var5
				.getBlockTileEntity();

		if (var6 != null) {
			final int var7 = var6.func_146017_i();

			if (var7 < 0) {
				p_149941_1_.playAuxSFX(1001, p_149941_2_, p_149941_3_,
						p_149941_4_, 0);
			} else {
				final ItemStack var8 = var6.getStackInSlot(var7);
				final IBehaviorDispenseItem var9 = func_149940_a(var8);

				if (var9 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
					final ItemStack var10 = var9.dispense(var5, var8);
					var6.setInventorySlotContents(var7,
							var10.stackSize == 0 ? null : var10);
				}
			}
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
		final int var3 = p_149691_2_ & 7;
		return p_149691_1_ == var3 ? var3 != 1 && var3 != 0 ? field_149945_N
				: field_149946_O : var3 != 1 && var3 != 0 ? p_149691_1_ != 1
				&& p_149691_1_ != 0 ? blockIcon : field_149944_M
				: field_149944_M;
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
			final TileEntityDispenser var10 = (TileEntityDispenser) p_149727_1_
					.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

			if (var10 != null) {
				p_149727_5_.func_146102_a(var10);
			}

			return true;
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		func_149938_m(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
			int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
			ItemStack p_149689_6_) {
		final int var7 = BlockPistonBase.func_150071_a(p_149689_1_,
				p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
				p_149689_4_, var7, 2);

		if (p_149689_6_.hasDisplayName()) {
			((TileEntityDispenser) p_149689_1_.getTileEntity(p_149689_2_,
					p_149689_3_, p_149689_4_)).func_146018_a(p_149689_6_
					.getDisplayName());
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		final boolean var6 = p_149695_1_.isBlockIndirectlyGettingPowered(
				p_149695_2_, p_149695_3_, p_149695_4_)
				|| p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_,
						p_149695_3_ + 1, p_149695_4_);
		final int var7 = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_,
				p_149695_4_);
		final boolean var8 = (var7 & 8) != 0;

		if (var6 && !var8) {
			p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_,
					p_149695_4_, this, func_149738_a(p_149695_1_));
			p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_,
					p_149695_4_, var7 | 8, 4);
		} else if (!var6 && var8) {
			p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_,
					p_149695_4_, var7 & -9, 4);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("furnace_side");
		field_149944_M = p_149651_1_.registerIcon("furnace_top");
		field_149945_N = p_149651_1_.registerIcon(getTextureName()
				+ "_front_horizontal");
		field_149946_O = p_149651_1_.registerIcon(getTextureName()
				+ "_front_vertical");
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isClient) {
			func_149941_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
		}
	}
}
