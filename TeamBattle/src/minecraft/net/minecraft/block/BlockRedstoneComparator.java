package net.minecraft.block;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements
		ITileEntityProvider {

	public BlockRedstoneComparator(boolean p_i45399_1_) {
		super(p_i45399_1_);
		isBlockContainer = true;
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_5_, p_149749_6_);
		p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
		func_149911_e(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityComparator();
	}

	@Override
	protected void func_149897_b(World p_149897_1_, int p_149897_2_,
			int p_149897_3_, int p_149897_4_, Block p_149897_5_) {
		if (!p_149897_1_.func_147477_a(p_149897_2_, p_149897_3_, p_149897_4_,
				this)) {
			final int var6 = p_149897_1_.getBlockMetadata(p_149897_2_,
					p_149897_3_, p_149897_4_);
			final int var7 = func_149970_j(p_149897_1_, p_149897_2_,
					p_149897_3_, p_149897_4_, var6);
			final int var8 = func_149971_e(p_149897_1_, p_149897_2_,
					p_149897_3_, p_149897_4_).func_145996_a();

			if (var7 != var8
					|| func_149905_c(var6) != func_149900_a(p_149897_1_,
							p_149897_2_, p_149897_3_, p_149897_4_, var6)) {
				if (func_149912_i(p_149897_1_, p_149897_2_, p_149897_3_,
						p_149897_4_, var6)) {
					p_149897_1_.func_147454_a(p_149897_2_, p_149897_3_,
							p_149897_4_, this, func_149901_b(0), -1);
				} else {
					p_149897_1_.func_147454_a(p_149897_2_, p_149897_3_,
							p_149897_4_, this, func_149901_b(0), 0);
				}
			}
		}
	}

	@Override
	protected BlockRedstoneDiode func_149898_i() {
		return Blocks.unpowered_comparator;
	}

	@Override
	protected boolean func_149900_a(World p_149900_1_, int p_149900_2_,
			int p_149900_3_, int p_149900_4_, int p_149900_5_) {
		final int var6 = func_149903_h(p_149900_1_, p_149900_2_, p_149900_3_,
				p_149900_4_, p_149900_5_);

		if (var6 >= 15)
			return true;
		else if (var6 == 0)
			return false;
		else {
			final int var7 = func_149902_h(p_149900_1_, p_149900_2_,
					p_149900_3_, p_149900_4_, p_149900_5_);
			return var7 == 0 ? true : var6 >= var7;
		}
	}

	@Override
	protected int func_149901_b(int p_149901_1_) {
		return 2;
	}

	@Override
	protected int func_149903_h(World p_149903_1_, int p_149903_2_,
			int p_149903_3_, int p_149903_4_, int p_149903_5_) {
		int var6 = super.func_149903_h(p_149903_1_, p_149903_2_, p_149903_3_,
				p_149903_4_, p_149903_5_);
		final int var7 = func_149895_l(p_149903_5_);
		int var8 = p_149903_2_ + Direction.offsetX[var7];
		int var9 = p_149903_4_ + Direction.offsetZ[var7];
		Block var10 = p_149903_1_.getBlock(var8, p_149903_3_, var9);

		if (var10.hasComparatorInputOverride()) {
			var6 = var10.getComparatorInputOverride(p_149903_1_, var8,
					p_149903_3_, var9, Direction.rotateOpposite[var7]);
		} else if (var6 < 15 && var10.isNormalCube()) {
			var8 += Direction.offsetX[var7];
			var9 += Direction.offsetZ[var7];
			var10 = p_149903_1_.getBlock(var8, p_149903_3_, var9);

			if (var10.hasComparatorInputOverride()) {
				var6 = var10.getComparatorInputOverride(p_149903_1_, var8,
						p_149903_3_, var9, Direction.rotateOpposite[var7]);
			}
		}

		return var6;
	}

	@Override
	protected int func_149904_f(IBlockAccess p_149904_1_, int p_149904_2_,
			int p_149904_3_, int p_149904_4_, int p_149904_5_) {
		return func_149971_e(p_149904_1_, p_149904_2_, p_149904_3_, p_149904_4_)
				.func_145996_a();
	}

	@Override
	protected boolean func_149905_c(int p_149905_1_) {
		return field_149914_a || (p_149905_1_ & 8) != 0;
	}

	@Override
	protected BlockRedstoneDiode func_149906_e() {
		return Blocks.powered_comparator;
	}

	public boolean func_149969_d(int p_149969_1_) {
		return (p_149969_1_ & 4) == 4;
	}

	private int func_149970_j(World p_149970_1_, int p_149970_2_,
			int p_149970_3_, int p_149970_4_, int p_149970_5_) {
		return !func_149969_d(p_149970_5_) ? func_149903_h(p_149970_1_,
				p_149970_2_, p_149970_3_, p_149970_4_, p_149970_5_) : Math.max(
				func_149903_h(p_149970_1_, p_149970_2_, p_149970_3_,
						p_149970_4_, p_149970_5_)
						- func_149902_h(p_149970_1_, p_149970_2_, p_149970_3_,
								p_149970_4_, p_149970_5_), 0);
	}

	public TileEntityComparator func_149971_e(IBlockAccess p_149971_1_,
			int p_149971_2_, int p_149971_3_, int p_149971_4_) {
		return (TileEntityComparator) p_149971_1_.getTileEntity(p_149971_2_,
				p_149971_3_, p_149971_4_);
	}

	private void func_149972_c(World p_149972_1_, int p_149972_2_,
			int p_149972_3_, int p_149972_4_, Random p_149972_5_) {
		final int var6 = p_149972_1_.getBlockMetadata(p_149972_2_, p_149972_3_,
				p_149972_4_);
		final int var7 = func_149970_j(p_149972_1_, p_149972_2_, p_149972_3_,
				p_149972_4_, var6);
		final int var8 = func_149971_e(p_149972_1_, p_149972_2_, p_149972_3_,
				p_149972_4_).func_145996_a();
		func_149971_e(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_)
				.func_145995_a(var7);

		if (var8 != var7 || !func_149969_d(var6)) {
			final boolean var9 = func_149900_a(p_149972_1_, p_149972_2_,
					p_149972_3_, p_149972_4_, var6);
			final boolean var10 = field_149914_a || (var6 & 8) != 0;

			if (var10 && !var9) {
				p_149972_1_.setBlockMetadataWithNotify(p_149972_2_,
						p_149972_3_, p_149972_4_, var6 & -9, 2);
			} else if (!var10 && var9) {
				p_149972_1_.setBlockMetadataWithNotify(p_149972_2_,
						p_149972_3_, p_149972_4_, var6 | 8, 2);
			}

			func_149911_e(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_);
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		final boolean var3 = field_149914_a || (p_149691_2_ & 8) != 0;
		return p_149691_1_ == 0 ? var3 ? Blocks.redstone_torch
				.getBlockTextureFromSide(p_149691_1_)
				: Blocks.unlit_redstone_torch
						.getBlockTextureFromSide(p_149691_1_)
				: p_149691_1_ == 1 ? var3 ? Blocks.powered_comparator.blockIcon
						: blockIcon : Blocks.double_stone_slab
						.getBlockTextureFromSide(1);
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Items.comparator;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Items.comparator;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 37;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_,
			int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_,
			int p_149727_6_, float p_149727_7_, float p_149727_8_,
			float p_149727_9_) {
		final int var10 = p_149727_1_.getBlockMetadata(p_149727_2_,
				p_149727_3_, p_149727_4_);
		final boolean var11 = field_149914_a | (var10 & 8) != 0;
		final boolean var12 = !func_149969_d(var10);
		int var13 = var12 ? 4 : 0;
		var13 |= var11 ? 8 : 0;
		p_149727_1_.playSoundEffect(p_149727_2_ + 0.5D, p_149727_3_ + 0.5D,
				p_149727_4_ + 0.5D, "random.click", 0.3F, var12 ? 0.55F : 0.5F);
		p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_,
				p_149727_4_, var13 | var10 & 3, 2);
		func_149972_c(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_,
				p_149727_1_.rand);
		return true;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		p_149726_1_.setTileEntity(p_149726_2_, p_149726_3_, p_149726_4_,
				createNewTileEntity(p_149726_1_, 0));
	}

	@Override
	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_,
			int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_) {
		super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_,
				p_149696_4_, p_149696_5_, p_149696_6_);
		final TileEntity var7 = p_149696_1_.getTileEntity(p_149696_2_,
				p_149696_3_, p_149696_4_);
		return var7 != null ? var7.receiveClientEvent(p_149696_5_, p_149696_6_)
				: false;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (field_149914_a) {
			final int var6 = p_149674_1_.getBlockMetadata(p_149674_2_,
					p_149674_3_, p_149674_4_);
			p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_,
					func_149898_i(), var6 | 8, 4);
		}

		func_149972_c(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
				p_149674_5_);
	}
}
