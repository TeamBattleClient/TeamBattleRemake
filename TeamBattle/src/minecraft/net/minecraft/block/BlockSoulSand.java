package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockSoulSand extends Block {

	public BlockSoulSand() {
		super(Material.sand);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_,
			int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		final float var5 = 0.125F;
		return AxisAlignedBB.getBoundingBox(p_149668_2_, p_149668_3_,
				p_149668_4_, p_149668_2_ + 1, p_149668_3_ + 1 - var5,
				p_149668_4_ + 1);
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_,
			int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		p_149670_5_.motionX *= 0.4D;
		p_149670_5_.motionZ *= 0.4D;
	}
}
