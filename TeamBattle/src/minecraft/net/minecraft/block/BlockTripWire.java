package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block {

	public static boolean func_150139_a(IBlockAccess p_150139_0_,
			int p_150139_1_, int p_150139_2_, int p_150139_3_, int p_150139_4_,
			int p_150139_5_) {
		final int var6 = p_150139_1_ + Direction.offsetX[p_150139_5_];
		final int var8 = p_150139_3_ + Direction.offsetZ[p_150139_5_];
		final Block var9 = p_150139_0_.getBlock(var6, p_150139_2_, var8);
		final boolean var10 = (p_150139_4_ & 2) == 2;
		int var11;

		if (var9 == Blocks.tripwire_hook) {
			var11 = p_150139_0_.getBlockMetadata(var6, p_150139_2_, var8);
			final int var13 = var11 & 3;
			return var13 == Direction.rotateOpposite[p_150139_5_];
		} else if (var9 == Blocks.tripwire) {
			var11 = p_150139_0_.getBlockMetadata(var6, p_150139_2_, var8);
			final boolean var12 = (var11 & 2) == 2;
			return var10 == var12;
		} else
			return false;
	}

	public BlockTripWire() {
		super(Material.circuits);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
		setTickRandomly(true);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		func_150138_a(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_6_ | 1);
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return 10;
	}

	private void func_150138_a(World p_150138_1_, int p_150138_2_,
			int p_150138_3_, int p_150138_4_, int p_150138_5_) {
		int var6 = 0;

		while (var6 < 2) {
			int var7 = 1;

			while (true) {
				if (var7 < 42) {
					final int var8 = p_150138_2_ + Direction.offsetX[var6]
							* var7;
					final int var9 = p_150138_4_ + Direction.offsetZ[var6]
							* var7;
					final Block var10 = p_150138_1_.getBlock(var8, p_150138_3_,
							var9);

					if (var10 == Blocks.tripwire_hook) {
						final int var11 = p_150138_1_.getBlockMetadata(var8,
								p_150138_3_, var9) & 3;

						if (var11 == Direction.rotateOpposite[var6]) {
							Blocks.tripwire_hook.func_150136_a(p_150138_1_,
									var8, p_150138_3_, var9, false, p_150138_1_
											.getBlockMetadata(var8,
													p_150138_3_, var9), true,
									var7, p_150138_5_);
						}
					} else if (var10 == Blocks.tripwire) {
						++var7;
						continue;
					}
				}

				++var6;
				break;
			}
		}
	}

	private void func_150140_e(World p_150140_1_, int p_150140_2_,
			int p_150140_3_, int p_150140_4_) {
		int var5 = p_150140_1_.getBlockMetadata(p_150140_2_, p_150140_3_,
				p_150140_4_);
		final boolean var6 = (var5 & 1) == 1;
		boolean var7 = false;
		final List var8 = p_150140_1_.getEntitiesWithinAABBExcludingEntity(
				(Entity) null, AxisAlignedBB.getBoundingBox(p_150140_2_
						+ field_149759_B, p_150140_3_ + field_149760_C,
						p_150140_4_ + field_149754_D, p_150140_2_
								+ field_149755_E, p_150140_3_ + field_149756_F,
						p_150140_4_ + field_149757_G));

		if (!var8.isEmpty()) {
			final Iterator var9 = var8.iterator();

			while (var9.hasNext()) {
				final Entity var10 = (Entity) var9.next();

				if (!var10.doesEntityNotTriggerPressurePlate()) {
					var7 = true;
					break;
				}
			}
		}

		if (var7 && !var6) {
			var5 |= 1;
		}

		if (!var7 && var6) {
			var5 &= -2;
		}

		if (var7 != var6) {
			p_150140_1_.setBlockMetadataWithNotify(p_150140_2_, p_150140_3_,
					p_150140_4_, var5, 3);
			func_150138_a(p_150140_1_, p_150140_2_, p_150140_3_, p_150140_4_,
					var5);
		}

		if (var7) {
			p_150140_1_.scheduleBlockUpdate(p_150140_2_, p_150140_3_,
					p_150140_4_, this, func_149738_a(p_150140_1_));
		}
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
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Items.string;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Items.string;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 30;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		final int var5 = World.doesBlockHaveSolidTopSurface(p_149726_1_,
				p_149726_2_, p_149726_3_ - 1, p_149726_4_) ? 0 : 2;
		p_149726_1_.setBlockMetadataWithNotify(p_149726_2_, p_149726_3_,
				p_149726_4_, var5, 3);
		func_150138_a(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, var5);
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_,
			int p_149681_3_, int p_149681_4_, int p_149681_5_,
			EntityPlayer p_149681_6_) {
		if (!p_149681_1_.isClient) {
			if (p_149681_6_.getCurrentEquippedItem() != null
					&& p_149681_6_.getCurrentEquippedItem().getItem() == Items.shears) {
				p_149681_1_.setBlockMetadataWithNotify(p_149681_2_,
						p_149681_3_, p_149681_4_, p_149681_5_ | 8, 4);
			}
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_,
			int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		if (!p_149670_1_.isClient) {
			if ((p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_,
					p_149670_4_) & 1) != 1) {
				func_150140_e(p_149670_1_, p_149670_2_, p_149670_3_,
						p_149670_4_);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		final int var6 = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_,
				p_149695_4_);
		final boolean var7 = (var6 & 2) == 2;
		final boolean var8 = !World.doesBlockHaveSolidTopSurface(p_149695_1_,
				p_149695_2_, p_149695_3_ - 1, p_149695_4_);

		if (var7 != var8) {
			dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					var6, 0);
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
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
		final boolean var6 = (var5 & 4) == 4;
		final boolean var7 = (var5 & 2) == 2;

		if (!var7) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
		} else if (!var6) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isClient) {
			if ((p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_,
					p_149674_4_) & 1) == 1) {
				func_150140_e(p_149674_1_, p_149674_2_, p_149674_3_,
						p_149674_4_);
			}
		}
	}
}
