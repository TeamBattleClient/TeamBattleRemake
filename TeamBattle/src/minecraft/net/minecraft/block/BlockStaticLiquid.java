package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockStaticLiquid extends BlockLiquid {

	protected BlockStaticLiquid(Material p_i45429_1_) {
		super(p_i45429_1_);
		setTickRandomly(false);

		if (p_i45429_1_ == Material.lava) {
			setTickRandomly(true);
		}
	}

	private boolean isFlammable(World p_149817_1_, int p_149817_2_,
			int p_149817_3_, int p_149817_4_) {
		return p_149817_1_.getBlock(p_149817_2_, p_149817_3_, p_149817_4_)
				.getMaterial().getCanBurn();
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_,
				p_149695_4_, p_149695_5_);

		if (p_149695_1_.getBlock(p_149695_2_, p_149695_3_, p_149695_4_) == this) {
			setNotStationary(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	private void setNotStationary(World p_149818_1_, int p_149818_2_,
			int p_149818_3_, int p_149818_4_) {
		final int var5 = p_149818_1_.getBlockMetadata(p_149818_2_, p_149818_3_,
				p_149818_4_);
		p_149818_1_.setBlock(p_149818_2_, p_149818_3_, p_149818_4_,
				Block.getBlockById(Block.getIdFromBlock(this) - 1), var5, 2);
		p_149818_1_.scheduleBlockUpdate(p_149818_2_, p_149818_3_, p_149818_4_,
				Block.getBlockById(Block.getIdFromBlock(this) - 1),
				func_149738_a(p_149818_1_));
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (blockMaterial == Material.lava) {
			final int var6 = p_149674_5_.nextInt(3);
			int var7;

			for (var7 = 0; var7 < var6; ++var7) {
				p_149674_2_ += p_149674_5_.nextInt(3) - 1;
				++p_149674_3_;
				p_149674_4_ += p_149674_5_.nextInt(3) - 1;
				final Block var8 = p_149674_1_.getBlock(p_149674_2_,
						p_149674_3_, p_149674_4_);

				if (var8.blockMaterial == Material.air) {
					if (isFlammable(p_149674_1_, p_149674_2_ - 1, p_149674_3_,
							p_149674_4_)
							|| isFlammable(p_149674_1_, p_149674_2_ + 1,
									p_149674_3_, p_149674_4_)
							|| isFlammable(p_149674_1_, p_149674_2_,
									p_149674_3_, p_149674_4_ - 1)
							|| isFlammable(p_149674_1_, p_149674_2_,
									p_149674_3_, p_149674_4_ + 1)
							|| isFlammable(p_149674_1_, p_149674_2_,
									p_149674_3_ - 1, p_149674_4_)
							|| isFlammable(p_149674_1_, p_149674_2_,
									p_149674_3_ + 1, p_149674_4_)) {
						p_149674_1_.setBlock(p_149674_2_, p_149674_3_,
								p_149674_4_, Blocks.fire);
						return;
					}
				} else if (var8.blockMaterial.blocksMovement())
					return;
			}

			if (var6 == 0) {
				var7 = p_149674_2_;
				final int var10 = p_149674_4_;

				for (int var9 = 0; var9 < 3; ++var9) {
					p_149674_2_ = var7 + p_149674_5_.nextInt(3) - 1;
					p_149674_4_ = var10 + p_149674_5_.nextInt(3) - 1;

					if (p_149674_1_.isAirBlock(p_149674_2_, p_149674_3_ + 1,
							p_149674_4_)
							&& isFlammable(p_149674_1_, p_149674_2_,
									p_149674_3_, p_149674_4_)) {
						p_149674_1_.setBlock(p_149674_2_, p_149674_3_ + 1,
								p_149674_4_, Blocks.fire);
					}
				}
			}
		}
	}
}
