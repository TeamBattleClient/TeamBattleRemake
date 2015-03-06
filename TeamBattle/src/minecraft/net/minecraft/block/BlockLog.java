package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
	public static int func_150165_c(int p_150165_0_) {
		return p_150165_0_ & 3;
	}

	protected IIcon[] field_150166_b;

	protected IIcon[] field_150167_a;

	public BlockLog() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		final byte var7 = 4;
		final int var8 = var7 + 1;

		if (p_149749_1_.checkChunksExist(p_149749_2_ - var8,
				p_149749_3_ - var8, p_149749_4_ - var8, p_149749_2_ + var8,
				p_149749_3_ + var8, p_149749_4_ + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						if (p_149749_1_.getBlock(p_149749_2_ + var9,
								p_149749_3_ + var10, p_149749_4_ + var11)
								.getMaterial() == Material.leaves) {
							final int var12 = p_149749_1_.getBlockMetadata(
									p_149749_2_ + var9, p_149749_3_ + var10,
									p_149749_4_ + var11);

							if ((var12 & 8) == 0) {
								p_149749_1_.setBlockMetadataWithNotify(
										p_149749_2_ + var9,
										p_149749_3_ + var10, p_149749_4_
												+ var11, var12 | 8, 4);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected IIcon func_150161_d(int p_150161_1_) {
		return field_150166_b[p_150161_1_ % field_150166_b.length];
	}

	@Override
	protected IIcon func_150163_b(int p_150163_1_) {
		return field_150167_a[p_150163_1_ % field_150167_a.length];
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Item.getItemFromBlock(this);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}
}
