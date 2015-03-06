package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {
	private static final int[][] field_150150_a = new int[][] { { 2, 6 },
			{ 3, 7 }, { 2, 3 }, { 6, 7 }, { 0, 4 }, { 1, 5 }, { 0, 1 },
			{ 4, 5 } };

	public static boolean func_150148_a(Block p_150148_0_) {
		return p_150148_0_ instanceof BlockStairs;
	}

	private final Block field_150149_b;
	private final int field_150151_M;
	private boolean field_150152_N;

	private int field_150153_O;

	protected BlockStairs(Block p_i45428_1_, int p_i45428_2_) {
		super(p_i45428_1_.blockMaterial);
		field_150149_b = p_i45428_1_;
		field_150151_M = p_i45428_2_;
		setHardness(p_i45428_1_.blockHardness);
		setResistance(p_i45428_1_.blockResistance / 3.0F);
		setStepSound(p_i45428_1_.stepSound);
		setLightOpacity(255);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_,
			int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_,
			List p_149743_6_, Entity p_149743_7_) {
		func_150147_e(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		final boolean var8 = func_150145_f(p_149743_1_, p_149743_2_,
				p_149743_3_, p_149743_4_);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_,
				p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);

		if (var8
				&& func_150144_g(p_149743_1_, p_149743_2_, p_149743_3_,
						p_149743_4_)) {
			super.addCollisionBoxesToList(p_149743_1_, p_149743_2_,
					p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
					p_149743_7_);
		}

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		field_150149_b.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_,
				p_149749_4_, p_149749_5_, p_149749_6_);
	}

	/**
	 * Returns whether this block is collideable based on the arguments passed
	 * in \n@param par1 block metaData \n@param par2 whether the player
	 * right-clicked while holding a boat
	 */
	@Override
	public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
		return field_150149_b.canCollideCheck(p_149678_1_, p_149678_2_);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_,
			int p_149742_3_, int p_149742_4_) {
		return field_150149_b.canPlaceBlockAt(p_149742_1_, p_149742_2_,
				p_149742_3_, p_149742_4_);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World p_149731_1_,
			int p_149731_2_, int p_149731_3_, int p_149731_4_,
			Vec3 p_149731_5_, Vec3 p_149731_6_) {
		final MovingObjectPosition[] var7 = new MovingObjectPosition[8];
		final int var8 = p_149731_1_.getBlockMetadata(p_149731_2_, p_149731_3_,
				p_149731_4_);
		final int var9 = var8 & 3;
		final boolean var10 = (var8 & 4) == 4;
		final int[] var11 = field_150150_a[var9 + (var10 ? 4 : 0)];
		field_150152_N = true;
		int var14;
		int var15;
		int var16;

		for (int var12 = 0; var12 < 8; ++var12) {
			field_150153_O = var12;
			final int[] var13 = var11;
			var14 = var11.length;

			for (var15 = 0; var15 < var14; ++var15) {
				var16 = var13[var15];

				if (var16 == var12) {
					;
				}
			}

			var7[var12] = super.collisionRayTrace(p_149731_1_, p_149731_2_,
					p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
		}

		final int[] var21 = var11;
		final int var23 = var11.length;

		for (var14 = 0; var14 < var23; ++var14) {
			var15 = var21[var14];
			var7[var15] = null;
		}

		MovingObjectPosition var22 = null;
		double var24 = 0.0D;
		final MovingObjectPosition[] var25 = var7;
		var16 = var7.length;

		for (int var17 = 0; var17 < var16; ++var17) {
			final MovingObjectPosition var18 = var25[var17];

			if (var18 != null) {
				final double var19 = var18.hitVec.squareDistanceTo(p_149731_6_);

				if (var19 > var24) {
					var22 = var18;
					var24 = var19;
				}
			}
		}

		return var22;
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return field_150149_b.func_149738_a(p_149738_1_);
	}

	public boolean func_150144_g(IBlockAccess p_150144_1_, int p_150144_2_,
			int p_150144_3_, int p_150144_4_) {
		final int var5 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_,
				p_150144_4_);
		final int var6 = var5 & 3;
		float var7 = 0.5F;
		float var8 = 1.0F;

		if ((var5 & 4) != 0) {
			var7 = 0.0F;
			var8 = 0.5F;
		}

		float var9 = 0.0F;
		float var10 = 0.5F;
		float var11 = 0.5F;
		float var12 = 1.0F;
		boolean var13 = false;
		Block var14;
		int var15;
		int var16;

		if (var6 == 0) {
			var14 = p_150144_1_.getBlock(p_150144_2_ - 1, p_150144_3_,
					p_150144_4_);
			var15 = p_150144_1_.getBlockMetadata(p_150144_2_ - 1, p_150144_3_,
					p_150144_4_);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 3
						&& !func_150146_f(p_150144_1_, p_150144_2_,
								p_150144_3_, p_150144_4_ - 1, var5)) {
					var11 = 0.0F;
					var12 = 0.5F;
					var13 = true;
				} else if (var16 == 2
						&& !func_150146_f(p_150144_1_, p_150144_2_,
								p_150144_3_, p_150144_4_ + 1, var5)) {
					var11 = 0.5F;
					var12 = 1.0F;
					var13 = true;
				}
			}
		} else if (var6 == 1) {
			var14 = p_150144_1_.getBlock(p_150144_2_ + 1, p_150144_3_,
					p_150144_4_);
			var15 = p_150144_1_.getBlockMetadata(p_150144_2_ + 1, p_150144_3_,
					p_150144_4_);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var9 = 0.5F;
				var10 = 1.0F;
				var16 = var15 & 3;

				if (var16 == 3
						&& !func_150146_f(p_150144_1_, p_150144_2_,
								p_150144_3_, p_150144_4_ - 1, var5)) {
					var11 = 0.0F;
					var12 = 0.5F;
					var13 = true;
				} else if (var16 == 2
						&& !func_150146_f(p_150144_1_, p_150144_2_,
								p_150144_3_, p_150144_4_ + 1, var5)) {
					var11 = 0.5F;
					var12 = 1.0F;
					var13 = true;
				}
			}
		} else if (var6 == 2) {
			var14 = p_150144_1_.getBlock(p_150144_2_, p_150144_3_,
					p_150144_4_ - 1);
			var15 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_,
					p_150144_4_ - 1);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var11 = 0.0F;
				var12 = 0.5F;
				var16 = var15 & 3;

				if (var16 == 1
						&& !func_150146_f(p_150144_1_, p_150144_2_ - 1,
								p_150144_3_, p_150144_4_, var5)) {
					var13 = true;
				} else if (var16 == 0
						&& !func_150146_f(p_150144_1_, p_150144_2_ + 1,
								p_150144_3_, p_150144_4_, var5)) {
					var9 = 0.5F;
					var10 = 1.0F;
					var13 = true;
				}
			}
		} else if (var6 == 3) {
			var14 = p_150144_1_.getBlock(p_150144_2_, p_150144_3_,
					p_150144_4_ + 1);
			var15 = p_150144_1_.getBlockMetadata(p_150144_2_, p_150144_3_,
					p_150144_4_ + 1);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 1
						&& !func_150146_f(p_150144_1_, p_150144_2_ - 1,
								p_150144_3_, p_150144_4_, var5)) {
					var13 = true;
				} else if (var16 == 0
						&& !func_150146_f(p_150144_1_, p_150144_2_ + 1,
								p_150144_3_, p_150144_4_, var5)) {
					var9 = 0.5F;
					var10 = 1.0F;
					var13 = true;
				}
			}
		}

		if (var13) {
			setBlockBounds(var9, var7, var11, var10, var8, var12);
		}

		return var13;
	}

	public boolean func_150145_f(IBlockAccess p_150145_1_, int p_150145_2_,
			int p_150145_3_, int p_150145_4_) {
		final int var5 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_,
				p_150145_4_);
		final int var6 = var5 & 3;
		float var7 = 0.5F;
		float var8 = 1.0F;

		if ((var5 & 4) != 0) {
			var7 = 0.0F;
			var8 = 0.5F;
		}

		float var9 = 0.0F;
		float var10 = 1.0F;
		float var11 = 0.0F;
		float var12 = 0.5F;
		boolean var13 = true;
		Block var14;
		int var15;
		int var16;

		if (var6 == 0) {
			var9 = 0.5F;
			var12 = 1.0F;
			var14 = p_150145_1_.getBlock(p_150145_2_ + 1, p_150145_3_,
					p_150145_4_);
			var15 = p_150145_1_.getBlockMetadata(p_150145_2_ + 1, p_150145_3_,
					p_150145_4_);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 3
						&& !func_150146_f(p_150145_1_, p_150145_2_,
								p_150145_3_, p_150145_4_ + 1, var5)) {
					var12 = 0.5F;
					var13 = false;
				} else if (var16 == 2
						&& !func_150146_f(p_150145_1_, p_150145_2_,
								p_150145_3_, p_150145_4_ - 1, var5)) {
					var11 = 0.5F;
					var13 = false;
				}
			}
		} else if (var6 == 1) {
			var10 = 0.5F;
			var12 = 1.0F;
			var14 = p_150145_1_.getBlock(p_150145_2_ - 1, p_150145_3_,
					p_150145_4_);
			var15 = p_150145_1_.getBlockMetadata(p_150145_2_ - 1, p_150145_3_,
					p_150145_4_);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 3
						&& !func_150146_f(p_150145_1_, p_150145_2_,
								p_150145_3_, p_150145_4_ + 1, var5)) {
					var12 = 0.5F;
					var13 = false;
				} else if (var16 == 2
						&& !func_150146_f(p_150145_1_, p_150145_2_,
								p_150145_3_, p_150145_4_ - 1, var5)) {
					var11 = 0.5F;
					var13 = false;
				}
			}
		} else if (var6 == 2) {
			var11 = 0.5F;
			var12 = 1.0F;
			var14 = p_150145_1_.getBlock(p_150145_2_, p_150145_3_,
					p_150145_4_ + 1);
			var15 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_,
					p_150145_4_ + 1);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 1
						&& !func_150146_f(p_150145_1_, p_150145_2_ + 1,
								p_150145_3_, p_150145_4_, var5)) {
					var10 = 0.5F;
					var13 = false;
				} else if (var16 == 0
						&& !func_150146_f(p_150145_1_, p_150145_2_ - 1,
								p_150145_3_, p_150145_4_, var5)) {
					var9 = 0.5F;
					var13 = false;
				}
			}
		} else if (var6 == 3) {
			var14 = p_150145_1_.getBlock(p_150145_2_, p_150145_3_,
					p_150145_4_ - 1);
			var15 = p_150145_1_.getBlockMetadata(p_150145_2_, p_150145_3_,
					p_150145_4_ - 1);

			if (func_150148_a(var14) && (var5 & 4) == (var15 & 4)) {
				var16 = var15 & 3;

				if (var16 == 1
						&& !func_150146_f(p_150145_1_, p_150145_2_ + 1,
								p_150145_3_, p_150145_4_, var5)) {
					var10 = 0.5F;
					var13 = false;
				} else if (var16 == 0
						&& !func_150146_f(p_150145_1_, p_150145_2_ - 1,
								p_150145_3_, p_150145_4_, var5)) {
					var9 = 0.5F;
					var13 = false;
				}
			}
		}

		setBlockBounds(var9, var7, var11, var10, var8, var12);
		return var13;
	}

	private boolean func_150146_f(IBlockAccess p_150146_1_, int p_150146_2_,
			int p_150146_3_, int p_150146_4_, int p_150146_5_) {
		final Block var6 = p_150146_1_.getBlock(p_150146_2_, p_150146_3_,
				p_150146_4_);
		return func_150148_a(var6)
				&& p_150146_1_.getBlockMetadata(p_150146_2_, p_150146_3_,
						p_150146_4_) == p_150146_5_;
	}

	public void func_150147_e(IBlockAccess p_150147_1_, int p_150147_2_,
			int p_150147_3_, int p_150147_4_) {
		final int var5 = p_150147_1_.getBlockMetadata(p_150147_2_, p_150147_3_,
				p_150147_4_);

		if ((var5 & 4) != 0) {
			setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}
	}

	@Override
	public int getBlockBrightness(IBlockAccess p_149677_1_, int p_149677_2_,
			int p_149677_3_, int p_149677_4_) {
		return field_150149_b.getBlockBrightness(p_149677_1_, p_149677_2_,
				p_149677_3_, p_149677_4_);
	}

	/**
	 * Returns how much this block can resist explosions from the passed in
	 * entity.
	 */
	@Override
	public float getExplosionResistance(Entity p_149638_1_) {
		return field_150149_b.getExplosionResistance(p_149638_1_);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return field_150149_b.getIcon(p_149691_1_, field_150151_M);
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return field_150149_b.getMapColor(field_150151_M);
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return field_150149_b.getRenderBlockPass();
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 10;
	}

	/**
	 * Returns the bounding box of the wired rectangular prism to render.
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_,
			int p_149633_2_, int p_149633_3_, int p_149633_4_) {
		return field_150149_b.getSelectedBoundingBoxFromPool(p_149633_1_,
				p_149633_2_, p_149633_3_, p_149633_4_);
	}

	@Override
	public boolean isCollidable() {
		return field_150149_b.isCollidable();
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
		return field_150149_b.onBlockActivated(p_149727_1_, p_149727_2_,
				p_149727_3_, p_149727_4_, p_149727_5_, 0, 0.0F, 0.0F, 0.0F);
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		onNeighborBlockChange(p_149726_1_, p_149726_2_, p_149726_3_,
				p_149726_4_, Blocks.air);
		field_150149_b.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_,
				p_149726_4_);
	}

	/**
	 * Called when a player hits the block. Args: world, x, y, z, player
	 */
	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_,
			int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {
		field_150149_b.onBlockClicked(p_149699_1_, p_149699_2_, p_149699_3_,
				p_149699_4_, p_149699_5_);
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	@Override
	public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_,
			int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {
		field_150149_b.onBlockDestroyedByExplosion(p_149723_1_, p_149723_2_,
				p_149723_3_, p_149723_4_, p_149723_5_);
	}

	@Override
	public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_,
			int p_149664_3_, int p_149664_4_, int p_149664_5_) {
		field_150149_b.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_,
				p_149664_3_, p_149664_4_, p_149664_5_);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_,
			int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_,
			int p_149660_9_) {
		return p_149660_5_ != 0 && (p_149660_5_ == 1 || p_149660_7_ <= 0.5D) ? p_149660_9_
				: p_149660_9_ | 4;
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
		final int var8 = p_149689_1_.getBlockMetadata(p_149689_2_, p_149689_3_,
				p_149689_4_) & 4;

		if (var7 == 0) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 2 | var8, 2);
		}

		if (var7 == 1) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 1 | var8, 2);
		}

		if (var7 == 2) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 3 | var8, 2);
		}

		if (var7 == 3) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_,
					p_149689_4_, 0 | var8, 2);
		}
	}

	@Override
	public void onEntityWalking(World p_149724_1_, int p_149724_2_,
			int p_149724_3_, int p_149724_4_, Entity p_149724_5_) {
		field_150149_b.onEntityWalking(p_149724_1_, p_149724_2_, p_149724_3_,
				p_149724_4_, p_149724_5_);
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		field_150149_b.randomDisplayTick(p_149734_1_, p_149734_2_, p_149734_3_,
				p_149734_4_, p_149734_5_);
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		if (field_150152_N) {
			setBlockBounds(0.5F * (field_150153_O % 2),
					0.5F * (field_150153_O / 2 % 2),
					0.5F * (field_150153_O / 4 % 2),
					0.5F + 0.5F * (field_150153_O % 2),
					0.5F + 0.5F * (field_150153_O / 2 % 2),
					0.5F + 0.5F * (field_150153_O / 4 % 2));
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		field_150149_b.updateTick(p_149674_1_, p_149674_2_, p_149674_3_,
				p_149674_4_, p_149674_5_);
	}

	@Override
	public void velocityToAddToEntity(World p_149640_1_, int p_149640_2_,
			int p_149640_3_, int p_149640_4_, Entity p_149640_5_,
			Vec3 p_149640_6_) {
		field_150149_b.velocityToAddToEntity(p_149640_1_, p_149640_2_,
				p_149640_3_, p_149640_4_, p_149640_5_, p_149640_6_);
	}
}
