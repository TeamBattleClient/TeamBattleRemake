package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {
	/**
	 * Contains three sets of two values that provide complimentary indices for
	 * a given 'major' index - 1 and 2 for 0, 0 and 2 for 1, and 0 and 1 for 2.
	 */
	static final byte[] otherCoordPairs = new byte[] { (byte) 2, (byte) 0,
			(byte) 0, (byte) 1, (byte) 2, (byte) 1 };

	int[] basePos = new int[] { 0, 0, 0 };

	double branchDensity = 1.0D;
	double branchSlope = 0.381D;
	int height;
	double heightAttenuation = 0.618D;
	int heightLimit;
	/**
	 * Sets the limit of the random value used to initialize the height limit.
	 */
	int heightLimitLimit = 12;
	double leafDensity = 1.0D;
	/**
	 * Sets the distance limit for how far away the generator will populate
	 * leaves from the base leaf node.
	 */
	int leafDistanceLimit = 4;
	/** Contains a list of a points at which to generate groups of leaves. */
	int[][] leafNodes;

	/** random seed for GenBigTree */
	Random rand = new Random();

	double scaleWidth = 1.0D;

	/**
	 * Currently always 1, can be set to 2 in the class constructor to generate
	 * a double-sized tree trunk for big trees.
	 */
	int trunkSize = 1;

	/** Reference to the World object. */
	World worldObj;

	public WorldGenBigTree(boolean p_i2008_1_) {
		super(p_i2008_1_);
	}

	/**
	 * Checks a line of blocks in the world from the first coordinate to triplet
	 * to the second, returning the distance (in blocks) before a non-air,
	 * non-leaf block is encountered and/or the end is encountered.
	 */
	int checkBlockLine(int[] p_76496_1_, int[] p_76496_2_) {
		final int[] var3 = new int[] { 0, 0, 0 };
		byte var4 = 0;
		byte var5;

		for (var5 = 0; var4 < 3; ++var4) {
			var3[var4] = p_76496_2_[var4] - p_76496_1_[var4];

			if (Math.abs(var3[var4]) > Math.abs(var3[var5])) {
				var5 = var4;
			}
		}

		if (var3[var5] == 0)
			return -1;
		else {
			final byte var6 = otherCoordPairs[var5];
			final byte var7 = otherCoordPairs[var5 + 3];
			byte var8;

			if (var3[var5] > 0) {
				var8 = 1;
			} else {
				var8 = -1;
			}

			final double var9 = (double) var3[var6] / (double) var3[var5];
			final double var11 = (double) var3[var7] / (double) var3[var5];
			final int[] var13 = new int[] { 0, 0, 0 };
			int var14 = 0;
			int var15;

			for (var15 = var3[var5] + var8; var14 != var15; var14 += var8) {
				var13[var5] = p_76496_1_[var5] + var14;
				var13[var6] = MathHelper.floor_double(p_76496_1_[var6] + var14
						* var9);
				var13[var7] = MathHelper.floor_double(p_76496_1_[var7] + var14
						* var11);
				final Block var16 = worldObj.getBlock(var13[0], var13[1],
						var13[2]);

				if (!func_150523_a(var16)) {
					break;
				}
			}

			return var14 == var15 ? -1 : Math.abs(var14);
		}
	}

	void func_150529_a(int p_150529_1_, int p_150529_2_, int p_150529_3_,
			float p_150529_4_, byte p_150529_5_, Block p_150529_6_) {
		final int var7 = (int) (p_150529_4_ + 0.618D);
		final byte var8 = otherCoordPairs[p_150529_5_];
		final byte var9 = otherCoordPairs[p_150529_5_ + 3];
		final int[] var10 = new int[] { p_150529_1_, p_150529_2_, p_150529_3_ };
		final int[] var11 = new int[] { 0, 0, 0 };
		int var12 = -var7;
		int var13 = -var7;

		for (var11[p_150529_5_] = var10[p_150529_5_]; var12 <= var7; ++var12) {
			var11[var8] = var10[var8] + var12;
			var13 = -var7;

			while (var13 <= var7) {
				final double var15 = Math.pow(Math.abs(var12) + 0.5D, 2.0D)
						+ Math.pow(Math.abs(var13) + 0.5D, 2.0D);

				if (var15 > p_150529_4_ * p_150529_4_) {
					++var13;
				} else {
					var11[var9] = var10[var9] + var13;
					final Block var14 = worldObj.getBlock(var11[0], var11[1],
							var11[2]);

					if (var14.getMaterial() != Material.air
							&& var14.getMaterial() != Material.leaves) {
						++var13;
					} else {
						func_150516_a(worldObj, var11[0], var11[1], var11[2],
								p_150529_6_, 0);
						++var13;
					}
				}
			}
		}
	}

	void func_150530_a(int[] p_150530_1_, int[] p_150530_2_, Block p_150530_3_) {
		final int[] var4 = new int[] { 0, 0, 0 };
		byte var5 = 0;
		byte var6;

		for (var6 = 0; var5 < 3; ++var5) {
			var4[var5] = p_150530_2_[var5] - p_150530_1_[var5];

			if (Math.abs(var4[var5]) > Math.abs(var4[var6])) {
				var6 = var5;
			}
		}

		if (var4[var6] != 0) {
			final byte var7 = otherCoordPairs[var6];
			final byte var8 = otherCoordPairs[var6 + 3];
			byte var9;

			if (var4[var6] > 0) {
				var9 = 1;
			} else {
				var9 = -1;
			}

			final double var10 = (double) var4[var7] / (double) var4[var6];
			final double var12 = (double) var4[var8] / (double) var4[var6];
			final int[] var14 = new int[] { 0, 0, 0 };
			int var15 = 0;

			for (final int var16 = var4[var6] + var9; var15 != var16; var15 += var9) {
				var14[var6] = MathHelper.floor_double(p_150530_1_[var6] + var15
						+ 0.5D);
				var14[var7] = MathHelper.floor_double(p_150530_1_[var7] + var15
						* var10 + 0.5D);
				var14[var8] = MathHelper.floor_double(p_150530_1_[var8] + var15
						* var12 + 0.5D);
				byte var17 = 0;
				final int var18 = Math.abs(var14[0] - p_150530_1_[0]);
				final int var19 = Math.abs(var14[2] - p_150530_1_[2]);
				final int var20 = Math.max(var18, var19);

				if (var20 > 0) {
					if (var18 == var20) {
						var17 = 4;
					} else if (var19 == var20) {
						var17 = 8;
					}
				}

				func_150516_a(worldObj, var14[0], var14[1], var14[2],
						p_150530_3_, var17);
			}
		}
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_,
			int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		worldObj = p_76484_1_;
		final long var6 = p_76484_2_.nextLong();
		rand.setSeed(var6);
		basePos[0] = p_76484_3_;
		basePos[1] = p_76484_4_;
		basePos[2] = p_76484_5_;

		if (heightLimit == 0) {
			heightLimit = 5 + rand.nextInt(heightLimitLimit);
		}

		if (!validTreeLocation())
			return false;
		else {
			generateLeafNodeList();
			generateLeaves();
			generateTrunk();
			generateLeafNodeBases();
			return true;
		}
	}

	/**
	 * Generates the leaves surrounding an individual entry in the leafNodes
	 * list.
	 */
	void generateLeafNode(int p_76491_1_, int p_76491_2_, int p_76491_3_) {
		int var4 = p_76491_2_;

		for (final int var5 = p_76491_2_ + leafDistanceLimit; var4 < var5; ++var4) {
			final float var6 = leafSize(var4 - p_76491_2_);
			func_150529_a(p_76491_1_, var4, p_76491_3_, var6, (byte) 1,
					Blocks.leaves);
		}
	}

	/**
	 * Generates additional wood blocks to fill out the bases of different leaf
	 * nodes that would otherwise degrade.
	 */
	void generateLeafNodeBases() {
		int var1 = 0;
		final int var2 = leafNodes.length;

		for (final int[] var3 = new int[] { basePos[0], basePos[1], basePos[2] }; var1 < var2; ++var1) {
			final int[] var4 = leafNodes[var1];
			final int[] var5 = new int[] { var4[0], var4[1], var4[2] };
			var3[1] = var4[3];
			final int var6 = var3[1] - basePos[1];

			if (leafNodeNeedsBase(var6)) {
				func_150530_a(var3, var5, Blocks.log);
			}
		}
	}

	/**
	 * Generates a list of leaf nodes for the tree, to be populated by
	 * generateLeaves.
	 */
	void generateLeafNodeList() {
		height = (int) (heightLimit * heightAttenuation);

		if (height >= heightLimit) {
			height = heightLimit - 1;
		}

		int var1 = (int) (1.382D + Math.pow(leafDensity * heightLimit / 13.0D,
				2.0D));

		if (var1 < 1) {
			var1 = 1;
		}

		final int[][] var2 = new int[var1 * heightLimit][4];
		int var3 = basePos[1] + heightLimit - leafDistanceLimit;
		int var4 = 1;
		final int var5 = basePos[1] + height;
		int var6 = var3 - basePos[1];
		var2[0][0] = basePos[0];
		var2[0][1] = var3;
		var2[0][2] = basePos[2];
		var2[0][3] = var5;
		--var3;

		while (var6 >= 0) {
			int var7 = 0;
			final float var8 = layerSize(var6);

			if (var8 < 0.0F) {
				--var3;
				--var6;
			} else {
				for (final double var9 = 0.5D; var7 < var1; ++var7) {
					final double var11 = scaleWidth * var8
							* (rand.nextFloat() + 0.328D);
					final double var13 = rand.nextFloat() * 2.0D * Math.PI;
					final int var15 = MathHelper.floor_double(var11
							* Math.sin(var13) + basePos[0] + var9);
					final int var16 = MathHelper.floor_double(var11
							* Math.cos(var13) + basePos[2] + var9);
					final int[] var17 = new int[] { var15, var3, var16 };
					final int[] var18 = new int[] { var15,
							var3 + leafDistanceLimit, var16 };

					if (checkBlockLine(var17, var18) == -1) {
						final int[] var19 = new int[] { basePos[0], basePos[1],
								basePos[2] };
						final double var20 = Math.sqrt(Math.pow(
								Math.abs(basePos[0] - var17[0]), 2.0D)
								+ Math.pow(Math.abs(basePos[2] - var17[2]),
										2.0D));
						final double var22 = var20 * branchSlope;

						if (var17[1] - var22 > var5) {
							var19[1] = var5;
						} else {
							var19[1] = (int) (var17[1] - var22);
						}

						if (checkBlockLine(var19, var17) == -1) {
							var2[var4][0] = var15;
							var2[var4][1] = var3;
							var2[var4][2] = var16;
							var2[var4][3] = var19[1];
							++var4;
						}
					}
				}

				--var3;
				--var6;
			}
		}

		leafNodes = new int[var4][4];
		System.arraycopy(var2, 0, leafNodes, 0, var4);
	}

	/**
	 * Generates the leaf portion of the tree as specified by the leafNodes
	 * list.
	 */
	void generateLeaves() {
		int var1 = 0;

		for (final int var2 = leafNodes.length; var1 < var2; ++var1) {
			final int var3 = leafNodes[var1][0];
			final int var4 = leafNodes[var1][1];
			final int var5 = leafNodes[var1][2];
			generateLeafNode(var3, var4, var5);
		}
	}

	/**
	 * Places the trunk for the big tree that is being generated. Able to
	 * generate double-sized trunks by changing a field that is always 1 to 2.
	 */
	void generateTrunk() {
		final int var1 = basePos[0];
		final int var2 = basePos[1];
		final int var3 = basePos[1] + height;
		final int var4 = basePos[2];
		final int[] var5 = new int[] { var1, var2, var4 };
		final int[] var6 = new int[] { var1, var3, var4 };
		func_150530_a(var5, var6, Blocks.log);

		if (trunkSize == 2) {
			++var5[0];
			++var6[0];
			func_150530_a(var5, var6, Blocks.log);
			++var5[2];
			++var6[2];
			func_150530_a(var5, var6, Blocks.log);
			var5[0] += -1;
			var6[0] += -1;
			func_150530_a(var5, var6, Blocks.log);
		}
	}

	/**
	 * Gets the rough size of a layer of the tree.
	 */
	float layerSize(int p_76490_1_) {
		if (p_76490_1_ < heightLimit * 0.3D)
			return -1.618F;
		else {
			final float var2 = heightLimit / 2.0F;
			final float var3 = heightLimit / 2.0F - p_76490_1_;
			float var4;

			if (var3 == 0.0F) {
				var4 = var2;
			} else if (Math.abs(var3) >= var2) {
				var4 = 0.0F;
			} else {
				var4 = (float) Math.sqrt(Math.pow(Math.abs(var2), 2.0D)
						- Math.pow(Math.abs(var3), 2.0D));
			}

			var4 *= 0.5F;
			return var4;
		}
	}

	/**
	 * Indicates whether or not a leaf node requires additional wood to be added
	 * to preserve integrity.
	 */
	boolean leafNodeNeedsBase(int p_76493_1_) {
		return p_76493_1_ >= heightLimit * 0.2D;
	}

	float leafSize(int p_76495_1_) {
		return p_76495_1_ >= 0 && p_76495_1_ < leafDistanceLimit ? p_76495_1_ != 0
				&& p_76495_1_ != leafDistanceLimit - 1 ? 3.0F : 2.0F
				: -1.0F;
	}

	/**
	 * Rescales the generator settings, only used in WorldGenBigTree
	 */
	@Override
	public void setScale(double p_76487_1_, double p_76487_3_, double p_76487_5_) {
		heightLimitLimit = (int) (p_76487_1_ * 12.0D);

		if (p_76487_1_ > 0.5D) {
			leafDistanceLimit = 5;
		}

		scaleWidth = p_76487_3_;
		leafDensity = p_76487_5_;
	}

	/**
	 * Returns a boolean indicating whether or not the current location for the
	 * tree, spanning basePos to to the height limit, is valid.
	 */
	boolean validTreeLocation() {
		final int[] var1 = new int[] { basePos[0], basePos[1], basePos[2] };
		final int[] var2 = new int[] { basePos[0],
				basePos[1] + heightLimit - 1, basePos[2] };
		final Block var3 = worldObj.getBlock(basePos[0], basePos[1] - 1,
				basePos[2]);

		if (var3 != Blocks.dirt && var3 != Blocks.grass
				&& var3 != Blocks.farmland)
			return false;
		else {
			final int var4 = checkBlockLine(var1, var2);

			if (var4 == -1)
				return true;
			else if (var4 < 6)
				return false;
			else {
				heightLimit = var4;
				return true;
			}
		}
	}
}
