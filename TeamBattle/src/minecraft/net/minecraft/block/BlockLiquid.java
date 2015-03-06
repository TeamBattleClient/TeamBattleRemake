package net.minecraft.block;

import java.util.Random;

import me.client.Client;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import event.events.EventVelocity;
import event.events.EventVelocity.Type;

public abstract class BlockLiquid extends Block {
	public static float func_149801_b(int p_149801_0_) {
		if (p_149801_0_ >= 8) {
			p_149801_0_ = 0;
		}

		return (p_149801_0_ + 1) / 9.0F;
	}

	public static double func_149802_a(IBlockAccess p_149802_0_,
			int p_149802_1_, int p_149802_2_, int p_149802_3_,
			Material p_149802_4_) {
		Vec3 var5 = null;

		if (p_149802_4_ == Material.water) {
			var5 = Blocks.flowing_water.func_149800_f(p_149802_0_, p_149802_1_,
					p_149802_2_, p_149802_3_);
		}

		if (p_149802_4_ == Material.lava) {
			var5 = Blocks.flowing_lava.func_149800_f(p_149802_0_, p_149802_1_,
					p_149802_2_, p_149802_3_);
		}

		return var5.xCoord == 0.0D && var5.zCoord == 0.0D ? -1000.0D : Math
				.atan2(var5.zCoord, var5.xCoord) - Math.PI / 2D;
	}

	public static IIcon func_149803_e(String p_149803_0_) {
		return p_149803_0_ == "water_still" ? Blocks.flowing_water.field_149806_a[0]
				: p_149803_0_ == "water_flow" ? Blocks.flowing_water.field_149806_a[1]
						: p_149803_0_ == "lava_still" ? Blocks.flowing_lava.field_149806_a[0]
								: p_149803_0_ == "lava_flow" ? Blocks.flowing_lava.field_149806_a[1]
										: null;
	}

	private IIcon[] field_149806_a;

	protected BlockLiquid(Material p_i45413_1_) {
		super(p_i45413_1_);
		final float var2 = 0.0F;
		final float var3 = 0.0F;
		setBlockBounds(0.0F + var3, 0.0F + var2, 0.0F + var3, 1.0F + var3,
				1.0F + var2, 1.0F + var3);
		setTickRandomly(true);
	}

	/**
	 * Returns whether this block is collideable based on the arguments passed
	 * in \n@param par1 block metaData \n@param par2 whether the player
	 * right-clicked while holding a boat
	 */
	@Override
	public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
		return p_149678_2_ && p_149678_1_ == 0;
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied
	 * against the blocks color. Note only called when first determining what to
	 * render.
	 */
	@Override
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_,
			int p_149720_3_, int p_149720_4_) {
		if (blockMaterial != Material.water)
			return 16777215;
		else {
			int var5 = 0;
			int var6 = 0;
			int var7 = 0;

			for (int var8 = -1; var8 <= 1; ++var8) {
				for (int var9 = -1; var9 <= 1; ++var9) {
					final int var10 = p_149720_1_.getBiomeGenForCoords(
							p_149720_2_ + var9, p_149720_4_ + var8).waterColorMultiplier;
					var5 += (var10 & 16711680) >> 16;
					var6 += (var10 & 65280) >> 8;
					var7 += var10 & 255;
				}
			}

			return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9
					& 255;
		}
	}

	@Override
	public int func_149738_a(World p_149738_1_) {
		return blockMaterial == Material.water ? 5
				: blockMaterial == Material.lava ? p_149738_1_.provider.hasNoSky ? 10
						: 30
						: 0;
	}

	protected int func_149798_e(IBlockAccess p_149798_1_, int p_149798_2_,
			int p_149798_3_, int p_149798_4_) {
		if (p_149798_1_.getBlock(p_149798_2_, p_149798_3_, p_149798_4_)
				.getMaterial() != blockMaterial)
			return -1;
		else {
			int var5 = p_149798_1_.getBlockMetadata(p_149798_2_, p_149798_3_,
					p_149798_4_);

			if (var5 >= 8) {
				var5 = 0;
			}

			return var5;
		}
	}

	protected void func_149799_m(World p_149799_1_, int p_149799_2_,
			int p_149799_3_, int p_149799_4_) {
		p_149799_1_.playSoundEffect(p_149799_2_ + 0.5F, p_149799_3_ + 0.5F,
				p_149799_4_ + 0.5F, "random.fizz", 0.5F,
				2.6F + (p_149799_1_.rand.nextFloat() - p_149799_1_.rand
						.nextFloat()) * 0.8F);

		for (int var5 = 0; var5 < 8; ++var5) {
			p_149799_1_.spawnParticle("largesmoke",
					p_149799_2_ + Math.random(), p_149799_3_ + 1.2D,
					p_149799_4_ + Math.random(), 0.0D, 0.0D, 0.0D);
		}
	}

	private Vec3 func_149800_f(IBlockAccess p_149800_1_, int p_149800_2_,
			int p_149800_3_, int p_149800_4_) {
		Vec3 var5 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
		final int var6 = func_149798_e(p_149800_1_, p_149800_2_, p_149800_3_,
				p_149800_4_);

		for (int var7 = 0; var7 < 4; ++var7) {
			int var8 = p_149800_2_;
			int var10 = p_149800_4_;

			if (var7 == 0) {
				var8 = p_149800_2_ - 1;
			}

			if (var7 == 1) {
				var10 = p_149800_4_ - 1;
			}

			if (var7 == 2) {
				++var8;
			}

			if (var7 == 3) {
				++var10;
			}

			int var11 = func_149798_e(p_149800_1_, var8, p_149800_3_, var10);
			int var12;

			if (var11 < 0) {
				if (!p_149800_1_.getBlock(var8, p_149800_3_, var10)
						.getMaterial().blocksMovement()) {
					var11 = func_149798_e(p_149800_1_, var8, p_149800_3_ - 1,
							var10);

					if (var11 >= 0) {
						var12 = var11 - (var6 - 8);
						var5 = var5.addVector((var8 - p_149800_2_) * var12,
								(p_149800_3_ - p_149800_3_) * var12,
								(var10 - p_149800_4_) * var12);
					}
				}
			} else if (var11 >= 0) {
				var12 = var11 - var6;
				var5 = var5.addVector((var8 - p_149800_2_) * var12,
						(p_149800_3_ - p_149800_3_) * var12,
						(var10 - p_149800_4_) * var12);
			}
		}

		if (p_149800_1_.getBlockMetadata(p_149800_2_, p_149800_3_, p_149800_4_) >= 8) {
			boolean var13 = false;

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_,
							p_149800_4_ - 1, 2)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_,
							p_149800_4_ + 1, 3)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_ - 1, p_149800_3_,
							p_149800_4_, 4)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_ + 1, p_149800_3_,
							p_149800_4_, 5)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_ + 1,
							p_149800_4_ - 1, 2)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_, p_149800_3_ + 1,
							p_149800_4_ + 1, 3)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_ - 1,
							p_149800_3_ + 1, p_149800_4_, 4)) {
				var13 = true;
			}

			if (var13
					|| isBlockSolid(p_149800_1_, p_149800_2_ + 1,
							p_149800_3_ + 1, p_149800_4_, 5)) {
				var13 = true;
			}

			if (var13) {
				var5 = var5.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}

		var5 = var5.normalize();
		return var5;
	}

	protected int func_149804_e(World p_149804_1_, int p_149804_2_,
			int p_149804_3_, int p_149804_4_) {
		return p_149804_1_.getBlock(p_149804_2_, p_149804_3_, p_149804_4_)
				.getMaterial() == blockMaterial ? p_149804_1_.getBlockMetadata(
				p_149804_2_, p_149804_3_, p_149804_4_) : -1;
	}

	private void func_149805_n(World p_149805_1_, int p_149805_2_,
			int p_149805_3_, int p_149805_4_) {
		if (p_149805_1_.getBlock(p_149805_2_, p_149805_3_, p_149805_4_) == this) {
			if (blockMaterial == Material.lava) {
				boolean var5 = false;

				if (var5
						|| p_149805_1_.getBlock(p_149805_2_, p_149805_3_,
								p_149805_4_ - 1).getMaterial() == Material.water) {
					var5 = true;
				}

				if (var5
						|| p_149805_1_.getBlock(p_149805_2_, p_149805_3_,
								p_149805_4_ + 1).getMaterial() == Material.water) {
					var5 = true;
				}

				if (var5
						|| p_149805_1_.getBlock(p_149805_2_ - 1, p_149805_3_,
								p_149805_4_).getMaterial() == Material.water) {
					var5 = true;
				}

				if (var5
						|| p_149805_1_.getBlock(p_149805_2_ + 1, p_149805_3_,
								p_149805_4_).getMaterial() == Material.water) {
					var5 = true;
				}

				if (var5
						|| p_149805_1_.getBlock(p_149805_2_, p_149805_3_ + 1,
								p_149805_4_).getMaterial() == Material.water) {
					var5 = true;
				}

				if (var5) {
					final int var6 = p_149805_1_.getBlockMetadata(p_149805_2_,
							p_149805_3_, p_149805_4_);

					if (var6 == 0) {
						p_149805_1_.setBlock(p_149805_2_, p_149805_3_,
								p_149805_4_, Blocks.obsidian);
					} else if (var6 <= 4) {
						p_149805_1_.setBlock(p_149805_2_, p_149805_3_,
								p_149805_4_, Blocks.cobblestone);
					}

					func_149799_m(p_149805_1_, p_149805_2_, p_149805_3_,
							p_149805_4_);
				}
			}
		}
	}

	@Override
	public int getBlockBrightness(IBlockAccess p_149677_1_, int p_149677_2_,
			int p_149677_3_, int p_149677_4_) {
		final int var5 = p_149677_1_.getLightBrightnessForSkyBlocks(
				p_149677_2_, p_149677_3_, p_149677_4_, 0);
		final int var6 = p_149677_1_.getLightBrightnessForSkyBlocks(
				p_149677_2_, p_149677_3_ + 1, p_149677_4_, 0);
		final int var7 = var5 & 255;
		final int var8 = var6 & 255;
		final int var9 = var5 >> 16 & 255;
		final int var10 = var6 >> 16 & 255;
		return (var7 > var8 ? var7 : var8)
				| (var9 > var10 ? var9 : var10) << 16;
	}

	@Override
	public int getBlockColor() {
		return 16777215;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_,
			int p_149655_3_, int p_149655_4_) {
		return blockMaterial != Material.lava;
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
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ != 0 && p_149691_1_ != 1 ? field_149806_a[1]
				: field_149806_a[0];
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return null;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return blockMaterial == Material.water ? 1 : 0;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 4;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_,
			int p_149747_3_, int p_149747_4_, int p_149747_5_) {
		final Material var6 = p_149747_1_.getBlock(p_149747_2_, p_149747_3_,
				p_149747_4_).getMaterial();
		return var6 == blockMaterial ? false : p_149747_5_ == 1 ? true
				: var6 == Material.ice ? false : super.isBlockSolid(
						p_149747_1_, p_149747_2_, p_149747_3_, p_149747_4_,
						p_149747_5_);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		func_149805_n(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_,
			int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		func_149805_n(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		int var6;

		if (blockMaterial == Material.water) {
			if (p_149734_5_.nextInt(10) == 0) {
				var6 = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_,
						p_149734_4_);

				if (var6 <= 0 || var6 >= 8) {
					p_149734_1_.spawnParticle("suspended", p_149734_2_
							+ p_149734_5_.nextFloat(), p_149734_3_
							+ p_149734_5_.nextFloat(), p_149734_4_
							+ p_149734_5_.nextFloat(), 0.0D, 0.0D, 0.0D);
				}
			}

			for (var6 = 0; var6 < 0; ++var6) {
				final int var7 = p_149734_5_.nextInt(4);
				int var8 = p_149734_2_;
				int var9 = p_149734_4_;

				if (var7 == 0) {
					var8 = p_149734_2_ - 1;
				}

				if (var7 == 1) {
					++var8;
				}

				if (var7 == 2) {
					var9 = p_149734_4_ - 1;
				}

				if (var7 == 3) {
					++var9;
				}

				if (p_149734_1_.getBlock(var8, p_149734_3_, var9).getMaterial() == Material.air
						&& (p_149734_1_.getBlock(var8, p_149734_3_ - 1, var9)
								.getMaterial().blocksMovement() || p_149734_1_
								.getBlock(var8, p_149734_3_ - 1, var9)
								.getMaterial().isLiquid())) {
					final float var10 = 0.0625F;
					double var11 = p_149734_2_ + p_149734_5_.nextFloat();
					final double var13 = p_149734_3_ + p_149734_5_.nextFloat();
					double var15 = p_149734_4_ + p_149734_5_.nextFloat();

					if (var7 == 0) {
						var11 = p_149734_2_ - var10;
					}

					if (var7 == 1) {
						var11 = p_149734_2_ + 1 + var10;
					}

					if (var7 == 2) {
						var15 = p_149734_4_ - var10;
					}

					if (var7 == 3) {
						var15 = p_149734_4_ + 1 + var10;
					}

					double var17 = 0.0D;
					double var19 = 0.0D;

					if (var7 == 0) {
						var17 = -var10;
					}

					if (var7 == 1) {
						var17 = var10;
					}

					if (var7 == 2) {
						var19 = -var10;
					}

					if (var7 == 3) {
						var19 = var10;
					}

					p_149734_1_.spawnParticle("splash", var11, var13, var15,
							var17, 0.0D, var19);
				}
			}
		}

		if (blockMaterial == Material.water && p_149734_5_.nextInt(64) == 0) {
			var6 = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_,
					p_149734_4_);

			if (var6 > 0 && var6 < 8) {
				p_149734_1_.playSound(p_149734_2_ + 0.5F, p_149734_3_ + 0.5F,
						p_149734_4_ + 0.5F, "liquid.water",
						p_149734_5_.nextFloat() * 0.25F + 0.75F,
						p_149734_5_.nextFloat() * 1.0F + 0.5F, false);
			}
		}

		double var21;
		double var22;
		double var23;

		if (blockMaterial == Material.lava
				&& p_149734_1_.getBlock(p_149734_2_, p_149734_3_ + 1,
						p_149734_4_).getMaterial() == Material.air
				&& !p_149734_1_.getBlock(p_149734_2_, p_149734_3_ + 1,
						p_149734_4_).isOpaqueCube()) {
			if (p_149734_5_.nextInt(100) == 0) {
				var21 = p_149734_2_ + p_149734_5_.nextFloat();
				var22 = p_149734_3_ + field_149756_F;
				var23 = p_149734_4_ + p_149734_5_.nextFloat();
				p_149734_1_.spawnParticle("lava", var21, var22, var23, 0.0D,
						0.0D, 0.0D);
				p_149734_1_.playSound(var21, var22, var23, "liquid.lavapop",
						0.2F + p_149734_5_.nextFloat() * 0.2F,
						0.9F + p_149734_5_.nextFloat() * 0.15F, false);
			}

			if (p_149734_5_.nextInt(200) == 0) {
				p_149734_1_.playSound(p_149734_2_, p_149734_3_, p_149734_4_,
						"liquid.lava", 0.2F + p_149734_5_.nextFloat() * 0.2F,
						0.9F + p_149734_5_.nextFloat() * 0.15F, false);
			}
		}

		if (p_149734_5_.nextInt(10) == 0
				&& World.doesBlockHaveSolidTopSurface(p_149734_1_, p_149734_2_,
						p_149734_3_ - 1, p_149734_4_)
				&& !p_149734_1_
						.getBlock(p_149734_2_, p_149734_3_ - 2, p_149734_4_)
						.getMaterial().blocksMovement()) {
			var21 = p_149734_2_ + p_149734_5_.nextFloat();
			var22 = p_149734_3_ - 1.05D;
			var23 = p_149734_4_ + p_149734_5_.nextFloat();

			if (blockMaterial == Material.water) {
				p_149734_1_.spawnParticle("dripWater", var21, var22, var23,
						0.0D, 0.0D, 0.0D);
			} else {
				p_149734_1_.spawnParticle("dripLava", var21, var22, var23,
						0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		if (blockMaterial == Material.lava) {
			field_149806_a = new IIcon[] {
					p_149651_1_.registerIcon("lava_still"),
					p_149651_1_.registerIcon("lava_flow") };
		} else {
			field_149806_a = new IIcon[] {
					p_149651_1_.registerIcon("water_still"),
					p_149651_1_.registerIcon("water_flow") };
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		final Material var6 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_,
				p_149646_4_).getMaterial();
		return var6 == blockMaterial ? false : p_149646_5_ == 1 ? true : super
				.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_,
						p_149646_4_, p_149646_5_);
	}

	@Override
	public void velocityToAddToEntity(World p_149640_1_, int p_149640_2_,
			int p_149640_3_, int p_149640_4_, Entity p_149640_5_,
			Vec3 p_149640_6_) {
		final EventVelocity event = new EventVelocity(Type.WATER, p_149640_5_);
		Client.getEventManager().call(event);
		if (event.isCancelled())
			return;
		final Vec3 var7 = func_149800_f(p_149640_1_, p_149640_2_, p_149640_3_,
				p_149640_4_);
		p_149640_6_.xCoord += var7.xCoord;
		p_149640_6_.yCoord += var7.yCoord;
		p_149640_6_.zCoord += var7.zCoord;
	}
}
