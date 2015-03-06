package net.minecraft.world.gen;

import net.minecraft.block.Block;

public class FlatLayerInfo {
	private final Block field_151537_a;

	/** Amount of layers for this set of layers. */
	private int layerCount;

	/** Block metadata used on this set of laeyrs. */
	private int layerFillBlockMeta;
	private int layerMinimumY;

	public FlatLayerInfo(int p_i45467_1_, Block p_i45467_2_) {
		layerCount = 1;
		layerCount = p_i45467_1_;
		field_151537_a = p_i45467_2_;
	}

	public FlatLayerInfo(int p_i45468_1_, Block p_i45468_2_, int p_i45468_3_) {
		this(p_i45468_1_, p_i45468_2_);
		layerFillBlockMeta = p_i45468_3_;
	}

	public Block func_151536_b() {
		return field_151537_a;
	}

	/**
	 * Return the block metadata used on this set of layers.
	 */
	public int getFillBlockMeta() {
		return layerFillBlockMeta;
	}

	/**
	 * Return the amount of layers for this set of layers.
	 */
	public int getLayerCount() {
		return layerCount;
	}

	/**
	 * Return the minimum Y coordinate for this layer, set during generation.
	 */
	public int getMinY() {
		return layerMinimumY;
	}

	/**
	 * Set the minimum Y coordinate for this layer.
	 */
	public void setMinY(int p_82660_1_) {
		layerMinimumY = p_82660_1_;
	}

	@Override
	public String toString() {
		String var1 = Integer.toString(Block.getIdFromBlock(field_151537_a));

		if (layerCount > 1) {
			var1 = layerCount + "x" + var1;
		}

		if (layerFillBlockMeta > 0) {
			var1 = var1 + ":" + layerFillBlockMeta;
		}

		return var1;
	}
}
