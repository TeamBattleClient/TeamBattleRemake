package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class StructureMineshaftPieces {
	public static class Corridor extends StructureComponent {
		public static StructureBoundingBox findValidPlacement(List p_74954_0_,
				Random p_74954_1_, int p_74954_2_, int p_74954_3_,
				int p_74954_4_, int p_74954_5_) {
			final StructureBoundingBox var6 = new StructureBoundingBox(
					p_74954_2_, p_74954_3_, p_74954_4_, p_74954_2_,
					p_74954_3_ + 2, p_74954_4_);
			int var7;

			for (var7 = p_74954_1_.nextInt(3) + 2; var7 > 0; --var7) {
				final int var8 = var7 * 5;

				switch (p_74954_5_) {
				case 0:
					var6.maxX = p_74954_2_ + 2;
					var6.maxZ = p_74954_4_ + var8 - 1;
					break;

				case 1:
					var6.minX = p_74954_2_ - (var8 - 1);
					var6.maxZ = p_74954_4_ + 2;
					break;

				case 2:
					var6.maxX = p_74954_2_ + 2;
					var6.minZ = p_74954_4_ - (var8 - 1);
					break;

				case 3:
					var6.maxX = p_74954_2_ + var8 - 1;
					var6.maxZ = p_74954_4_ + 2;
				}

				if (StructureComponent.findIntersecting(p_74954_0_, var6) == null) {
					break;
				}
			}

			return var7 > 0 ? var6 : null;
		}

		private boolean hasRails;
		private boolean hasSpiders;
		private int sectionCount;

		private boolean spawnerPlaced;

		public Corridor() {
		}

		public Corridor(int p_i2035_1_, Random p_i2035_2_,
				StructureBoundingBox p_i2035_3_, int p_i2035_4_) {
			super(p_i2035_1_);
			coordBaseMode = p_i2035_4_;
			boundingBox = p_i2035_3_;
			hasRails = p_i2035_2_.nextInt(3) == 0;
			hasSpiders = !hasRails && p_i2035_2_.nextInt(23) == 0;

			if (coordBaseMode != 2 && coordBaseMode != 0) {
				sectionCount = p_i2035_3_.getXSize() / 5;
			} else {
				sectionCount = p_i2035_3_.getZSize() / 5;
			}
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				final int var8 = sectionCount * 5 - 1;
				func_151549_a(p_74875_1_, p_74875_3_, 0, 0, 0, 2, 1, var8,
						Blocks.air, Blocks.air, false);
				func_151551_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.8F, 0, 2,
						0, 2, 2, var8, Blocks.air, Blocks.air, false);

				if (hasSpiders) {
					func_151551_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.6F, 0,
							0, 0, 2, 1, var8, Blocks.web, Blocks.air, false);
				}

				int var9;
				int var10;

				for (var9 = 0; var9 < sectionCount; ++var9) {
					var10 = 2 + var9 * 5;
					func_151549_a(p_74875_1_, p_74875_3_, 0, 0, var10, 0, 1,
							var10, Blocks.fence, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, 2, 0, var10, 2, 1,
							var10, Blocks.fence, Blocks.air, false);

					if (p_74875_2_.nextInt(4) == 0) {
						func_151549_a(p_74875_1_, p_74875_3_, 0, 2, var10, 0,
								2, var10, Blocks.planks, Blocks.air, false);
						func_151549_a(p_74875_1_, p_74875_3_, 2, 2, var10, 2,
								2, var10, Blocks.planks, Blocks.air, false);
					} else {
						func_151549_a(p_74875_1_, p_74875_3_, 0, 2, var10, 2,
								2, var10, Blocks.planks, Blocks.air, false);
					}

					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0,
							2, var10 - 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2,
							2, var10 - 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0,
							2, var10 + 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2,
							2, var10 + 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0,
							2, var10 - 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2,
							2, var10 - 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0,
							2, var10 + 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2,
							2, var10 + 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1,
							2, var10 - 1, Blocks.torch, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1,
							2, var10 + 1, Blocks.torch, 0);

					if (p_74875_2_.nextInt(100) == 0) {
						generateStructureChestContents(
								p_74875_1_,
								p_74875_3_,
								p_74875_2_,
								2,
								0,
								var10 - 1,
								WeightedRandomChestContent
										.func_92080_a(
												StructureMineshaftPieces.mineshaftChestContents,
												new WeightedRandomChestContent[] { Items.enchanted_book
														.func_92114_b(p_74875_2_) }),
								3 + p_74875_2_.nextInt(4));
					}

					if (p_74875_2_.nextInt(100) == 0) {
						generateStructureChestContents(
								p_74875_1_,
								p_74875_3_,
								p_74875_2_,
								0,
								0,
								var10 + 1,
								WeightedRandomChestContent
										.func_92080_a(
												StructureMineshaftPieces.mineshaftChestContents,
												new WeightedRandomChestContent[] { Items.enchanted_book
														.func_92114_b(p_74875_2_) }),
								3 + p_74875_2_.nextInt(4));
					}

					if (hasSpiders && !spawnerPlaced) {
						final int var11 = getYWithOffset(0);
						int var12 = var10 - 1 + p_74875_2_.nextInt(3);
						final int var13 = getXWithOffset(1, var12);
						var12 = getZWithOffset(1, var12);

						if (p_74875_3_.isVecInside(var13, var11, var12)) {
							spawnerPlaced = true;
							p_74875_1_.setBlock(var13, var11, var12,
									Blocks.mob_spawner, 0, 2);
							final TileEntityMobSpawner var14 = (TileEntityMobSpawner) p_74875_1_
									.getTileEntity(var13, var11, var12);

							if (var14 != null) {
								var14.func_145881_a().setMobID("CaveSpider");
							}
						}
					}
				}

				for (var9 = 0; var9 <= 2; ++var9) {
					for (var10 = 0; var10 <= var8; ++var10) {
						final byte var16 = -1;
						final Block var17 = func_151548_a(p_74875_1_, var9,
								var16, var10, p_74875_3_);

						if (var17.getMaterial() == Material.air) {
							final byte var18 = -1;
							func_151550_a(p_74875_1_, Blocks.planks, 0, var9,
									var18, var10, p_74875_3_);
						}
					}
				}

				if (hasRails) {
					for (var9 = 0; var9 <= var8; ++var9) {
						final Block var15 = func_151548_a(p_74875_1_, 1, -1,
								var9, p_74875_3_);

						if (var15.getMaterial() != Material.air
								&& var15.func_149730_j()) {
							func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_,
									0.7F, 1, 0, var9, Blocks.rail,
									func_151555_a(Blocks.rail, 0));
						}
					}
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			final int var4 = getComponentType();
			final int var5 = p_74861_3_.nextInt(4);

			switch (coordBaseMode) {
			case 0:
				if (var5 <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX,
							boundingBox.minY - 1 + p_74861_3_.nextInt(3),
							boundingBox.maxZ + 1, coordBaseMode, var4);
				} else if (var5 == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3),
							boundingBox.maxZ - 3, 1, var4);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3),
							boundingBox.maxZ - 3, 3, var4);
				}

				break;

			case 1:
				if (var5 <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3), boundingBox.minZ,
							coordBaseMode, var4);
				} else if (var5 == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX,
							boundingBox.minY - 1 + p_74861_3_.nextInt(3),
							boundingBox.minZ - 1, 2, var4);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX,
							boundingBox.minY - 1 + p_74861_3_.nextInt(3),
							boundingBox.maxZ + 1, 0, var4);
				}

				break;

			case 2:
				if (var5 <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX,
							boundingBox.minY - 1 + p_74861_3_.nextInt(3),
							boundingBox.minZ - 1, coordBaseMode, var4);
				} else if (var5 == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3), boundingBox.minZ,
							1, var4);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3), boundingBox.minZ,
							3, var4);
				}

				break;

			case 3:
				if (var5 <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3), boundingBox.minZ,
							coordBaseMode, var4);
				} else if (var5 == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX - 3, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3),
							boundingBox.minZ - 1, 2, var4);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX - 3, boundingBox.minY - 1
									+ p_74861_3_.nextInt(3),
							boundingBox.maxZ + 1, 0, var4);
				}
			}

			if (var4 < 8) {
				int var6;
				int var7;

				if (coordBaseMode != 2 && coordBaseMode != 0) {
					for (var6 = boundingBox.minX + 3; var6 + 3 <= boundingBox.maxX; var6 += 5) {
						var7 = p_74861_3_.nextInt(5);

						if (var7 == 0) {
							StructureMineshaftPieces.getNextMineShaftComponent(
									p_74861_1_, p_74861_2_, p_74861_3_, var6,
									boundingBox.minY, boundingBox.minZ - 1, 2,
									var4 + 1);
						} else if (var7 == 1) {
							StructureMineshaftPieces.getNextMineShaftComponent(
									p_74861_1_, p_74861_2_, p_74861_3_, var6,
									boundingBox.minY, boundingBox.maxZ + 1, 0,
									var4 + 1);
						}
					}
				} else {
					for (var6 = boundingBox.minZ + 3; var6 + 3 <= boundingBox.maxZ; var6 += 5) {
						var7 = p_74861_3_.nextInt(5);

						if (var7 == 0) {
							StructureMineshaftPieces.getNextMineShaftComponent(
									p_74861_1_, p_74861_2_, p_74861_3_,
									boundingBox.minX - 1, boundingBox.minY,
									var6, 1, var4 + 1);
						} else if (var7 == 1) {
							StructureMineshaftPieces.getNextMineShaftComponent(
									p_74861_1_, p_74861_2_, p_74861_3_,
									boundingBox.maxX + 1, boundingBox.minY,
									var6, 3, var4 + 1);
						}
					}
				}
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			hasRails = p_143011_1_.getBoolean("hr");
			hasSpiders = p_143011_1_.getBoolean("sc");
			spawnerPlaced = p_143011_1_.getBoolean("hps");
			sectionCount = p_143011_1_.getInteger("Num");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setBoolean("hr", hasRails);
			p_143012_1_.setBoolean("sc", hasSpiders);
			p_143012_1_.setBoolean("hps", spawnerPlaced);
			p_143012_1_.setInteger("Num", sectionCount);
		}

		@Override
		protected boolean generateStructureChestContents(World p_74879_1_,
				StructureBoundingBox p_74879_2_, Random p_74879_3_,
				int p_74879_4_, int p_74879_5_, int p_74879_6_,
				WeightedRandomChestContent[] p_74879_7_, int p_74879_8_) {
			final int var9 = getXWithOffset(p_74879_4_, p_74879_6_);
			final int var10 = getYWithOffset(p_74879_5_);
			final int var11 = getZWithOffset(p_74879_4_, p_74879_6_);

			if (p_74879_2_.isVecInside(var9, var10, var11)
					&& p_74879_1_.getBlock(var9, var10, var11).getMaterial() == Material.air) {
				final int var12 = p_74879_3_.nextBoolean() ? 1 : 0;
				p_74879_1_.setBlock(var9, var10, var11, Blocks.rail,
						func_151555_a(Blocks.rail, var12), 2);
				final EntityMinecartChest var13 = new EntityMinecartChest(
						p_74879_1_, var9 + 0.5F, var10 + 0.5F, var11 + 0.5F);
				WeightedRandomChestContent.generateChestContents(p_74879_3_,
						p_74879_7_, var13, p_74879_8_);
				p_74879_1_.spawnEntityInWorld(var13);
				return true;
			} else
				return false;
		}
	}

	public static class Cross extends StructureComponent {
		public static StructureBoundingBox findValidPlacement(List p_74951_0_,
				Random p_74951_1_, int p_74951_2_, int p_74951_3_,
				int p_74951_4_, int p_74951_5_) {
			final StructureBoundingBox var6 = new StructureBoundingBox(
					p_74951_2_, p_74951_3_, p_74951_4_, p_74951_2_,
					p_74951_3_ + 2, p_74951_4_);

			if (p_74951_1_.nextInt(4) == 0) {
				var6.maxY += 4;
			}

			switch (p_74951_5_) {
			case 0:
				var6.minX = p_74951_2_ - 1;
				var6.maxX = p_74951_2_ + 3;
				var6.maxZ = p_74951_4_ + 4;
				break;

			case 1:
				var6.minX = p_74951_2_ - 4;
				var6.minZ = p_74951_4_ - 1;
				var6.maxZ = p_74951_4_ + 3;
				break;

			case 2:
				var6.minX = p_74951_2_ - 1;
				var6.maxX = p_74951_2_ + 3;
				var6.minZ = p_74951_4_ - 4;
				break;

			case 3:
				var6.maxX = p_74951_2_ + 4;
				var6.minZ = p_74951_4_ - 1;
				var6.maxZ = p_74951_4_ + 3;
			}

			return StructureComponent.findIntersecting(p_74951_0_, var6) != null ? null
					: var6;
		}

		private int corridorDirection;

		private boolean isMultipleFloors;

		public Cross() {
		}

		public Cross(int p_i2036_1_, Random p_i2036_2_,
				StructureBoundingBox p_i2036_3_, int p_i2036_4_) {
			super(p_i2036_1_);
			corridorDirection = p_i2036_4_;
			boundingBox = p_i2036_3_;
			isMultipleFloors = p_i2036_3_.getYSize() > 3;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				if (isMultipleFloors) {
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
							boundingBox.minY, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.minY + 3 - 1,
							boundingBox.maxZ, Blocks.air, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX,
							boundingBox.minY, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.minY + 3 - 1,
							boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
							boundingBox.maxY - 2, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.maxY,
							boundingBox.maxZ, Blocks.air, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX,
							boundingBox.maxY - 2, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.maxY,
							boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
							boundingBox.minY + 3, boundingBox.minZ + 1,
							boundingBox.maxX - 1, boundingBox.minY + 3,
							boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
				} else {
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
							boundingBox.minY, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.maxY,
							boundingBox.maxZ, Blocks.air, Blocks.air, false);
					func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX,
							boundingBox.minY, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.maxY,
							boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
				}

				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.minZ + 1,
						boundingBox.minX + 1, boundingBox.maxY,
						boundingBox.minZ + 1, Blocks.planks, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.maxZ - 1,
						boundingBox.minX + 1, boundingBox.maxY,
						boundingBox.maxZ - 1, Blocks.planks, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.maxX - 1,
						boundingBox.minY, boundingBox.minZ + 1,
						boundingBox.maxX - 1, boundingBox.maxY,
						boundingBox.minZ + 1, Blocks.planks, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.maxX - 1,
						boundingBox.minY, boundingBox.maxZ - 1,
						boundingBox.maxX - 1, boundingBox.maxY,
						boundingBox.maxZ - 1, Blocks.planks, Blocks.air, false);

				for (int var4 = boundingBox.minX; var4 <= boundingBox.maxX; ++var4) {
					for (int var5 = boundingBox.minZ; var5 <= boundingBox.maxZ; ++var5) {
						if (func_151548_a(p_74875_1_, var4,
								boundingBox.minY - 1, var5, p_74875_3_)
								.getMaterial() == Material.air) {
							func_151550_a(p_74875_1_, Blocks.planks, 0, var4,
									boundingBox.minY - 1, var5, p_74875_3_);
						}
					}
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			final int var4 = getComponentType();

			switch (corridorDirection) {
			case 0:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.maxZ + 1, 0, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX - 1,
						boundingBox.minY, boundingBox.minZ + 1, 1, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.maxX + 1,
						boundingBox.minY, boundingBox.minZ + 1, 3, var4);
				break;

			case 1:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.minZ - 1, 2, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.maxZ + 1, 0, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX - 1,
						boundingBox.minY, boundingBox.minZ + 1, 1, var4);
				break;

			case 2:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.minZ - 1, 2, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX - 1,
						boundingBox.minY, boundingBox.minZ + 1, 1, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.maxX + 1,
						boundingBox.minY, boundingBox.minZ + 1, 3, var4);
				break;

			case 3:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.minZ - 1, 2, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX + 1,
						boundingBox.minY, boundingBox.maxZ + 1, 0, var4);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.maxX + 1,
						boundingBox.minY, boundingBox.minZ + 1, 3, var4);
			}

			if (isMultipleFloors) {
				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX + 1, boundingBox.minY + 3 + 1,
							boundingBox.minZ - 1, 2, var4);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY + 3 + 1,
							boundingBox.minZ + 1, 1, var4);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY + 3 + 1,
							boundingBox.minZ + 1, 3, var4);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(
							p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX + 1, boundingBox.minY + 3 + 1,
							boundingBox.maxZ + 1, 0, var4);
				}
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			isMultipleFloors = p_143011_1_.getBoolean("tf");
			corridorDirection = p_143011_1_.getInteger("D");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setBoolean("tf", isMultipleFloors);
			p_143012_1_.setInteger("D", corridorDirection);
		}
	}

	public static class Room extends StructureComponent {
		private final List roomsLinkedToTheRoom = new LinkedList();

		public Room() {
		}

		public Room(int p_i2037_1_, Random p_i2037_2_, int p_i2037_3_,
				int p_i2037_4_) {
			super(p_i2037_1_);
			boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_,
					p_i2037_3_ + 7 + p_i2037_2_.nextInt(6),
					54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7
							+ p_i2037_2_.nextInt(6));
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX,
						boundingBox.minY, boundingBox.minZ, boundingBox.maxX,
						boundingBox.minY, boundingBox.maxZ, Blocks.dirt,
						Blocks.air, true);
				func_151549_a(p_74875_1_, p_74875_3_, boundingBox.minX,
						boundingBox.minY + 1, boundingBox.minZ,
						boundingBox.maxX,
						Math.min(boundingBox.minY + 3, boundingBox.maxY),
						boundingBox.maxZ, Blocks.air, Blocks.air, false);
				final Iterator var4 = roomsLinkedToTheRoom.iterator();

				while (var4.hasNext()) {
					final StructureBoundingBox var5 = (StructureBoundingBox) var4
							.next();
					func_151549_a(p_74875_1_, p_74875_3_, var5.minX,
							var5.maxY - 2, var5.minZ, var5.maxX, var5.maxY,
							var5.maxZ, Blocks.air, Blocks.air, false);
				}

				func_151547_a(p_74875_1_, p_74875_3_, boundingBox.minX,
						boundingBox.minY + 4, boundingBox.minZ,
						boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ,
						Blocks.air, false);
				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			final int var4 = getComponentType();
			int var6 = boundingBox.getYSize() - 3 - 1;

			if (var6 <= 0) {
				var6 = 1;
			}

			int var5;
			StructureComponent var7;
			StructureBoundingBox var8;

			for (var5 = 0; var5 < boundingBox.getXSize(); var5 += 4) {
				var5 += p_74861_3_.nextInt(boundingBox.getXSize());

				if (var5 + 3 > boundingBox.getXSize()) {
					break;
				}

				var7 = StructureMineshaftPieces.getNextMineShaftComponent(
						p_74861_1_, p_74861_2_, p_74861_3_, boundingBox.minX
								+ var5,
						boundingBox.minY + p_74861_3_.nextInt(var6) + 1,
						boundingBox.minZ - 1, 2, var4);

				if (var7 != null) {
					var8 = var7.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(
							var8.minX, var8.minY, boundingBox.minZ, var8.maxX,
							var8.maxY, boundingBox.minZ + 1));
				}
			}

			for (var5 = 0; var5 < boundingBox.getXSize(); var5 += 4) {
				var5 += p_74861_3_.nextInt(boundingBox.getXSize());

				if (var5 + 3 > boundingBox.getXSize()) {
					break;
				}

				var7 = StructureMineshaftPieces.getNextMineShaftComponent(
						p_74861_1_, p_74861_2_, p_74861_3_, boundingBox.minX
								+ var5,
						boundingBox.minY + p_74861_3_.nextInt(var6) + 1,
						boundingBox.maxZ + 1, 0, var4);

				if (var7 != null) {
					var8 = var7.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(
							var8.minX, var8.minY, boundingBox.maxZ - 1,
							var8.maxX, var8.maxY, boundingBox.maxZ));
				}
			}

			for (var5 = 0; var5 < boundingBox.getZSize(); var5 += 4) {
				var5 += p_74861_3_.nextInt(boundingBox.getZSize());

				if (var5 + 3 > boundingBox.getZSize()) {
					break;
				}

				var7 = StructureMineshaftPieces.getNextMineShaftComponent(
						p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX - 1,
						boundingBox.minY + p_74861_3_.nextInt(var6) + 1,
						boundingBox.minZ + var5, 1, var4);

				if (var7 != null) {
					var8 = var7.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(
							boundingBox.minX, var8.minY, var8.minZ,
							boundingBox.minX + 1, var8.maxY, var8.maxZ));
				}
			}

			for (var5 = 0; var5 < boundingBox.getZSize(); var5 += 4) {
				var5 += p_74861_3_.nextInt(boundingBox.getZSize());

				if (var5 + 3 > boundingBox.getZSize()) {
					break;
				}

				var7 = StructureMineshaftPieces.getNextMineShaftComponent(
						p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.maxX + 1,
						boundingBox.minY + p_74861_3_.nextInt(var6) + 1,
						boundingBox.minZ + var5, 3, var4);

				if (var7 != null) {
					var8 = var7.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(
							boundingBox.maxX - 1, var8.minY, var8.minZ,
							boundingBox.maxX, var8.maxY, var8.maxZ));
				}
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			final NBTTagList var2 = p_143011_1_.getTagList("Entrances", 11);

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				roomsLinkedToTheRoom.add(new StructureBoundingBox(var2
						.func_150306_c(var3)));
			}
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			final NBTTagList var2 = new NBTTagList();
			final Iterator var3 = roomsLinkedToTheRoom.iterator();

			while (var3.hasNext()) {
				final StructureBoundingBox var4 = (StructureBoundingBox) var3
						.next();
				var2.appendTag(var4.func_151535_h());
			}

			p_143012_1_.setTag("Entrances", var2);
		}
	}

	public static class Stairs extends StructureComponent {

		public static StructureBoundingBox findValidPlacement(List p_74950_0_,
				Random p_74950_1_, int p_74950_2_, int p_74950_3_,
				int p_74950_4_, int p_74950_5_) {
			final StructureBoundingBox var6 = new StructureBoundingBox(
					p_74950_2_, p_74950_3_ - 5, p_74950_4_, p_74950_2_,
					p_74950_3_ + 2, p_74950_4_);

			switch (p_74950_5_) {
			case 0:
				var6.maxX = p_74950_2_ + 2;
				var6.maxZ = p_74950_4_ + 8;
				break;

			case 1:
				var6.minX = p_74950_2_ - 8;
				var6.maxZ = p_74950_4_ + 2;
				break;

			case 2:
				var6.maxX = p_74950_2_ + 2;
				var6.minZ = p_74950_4_ - 8;
				break;

			case 3:
				var6.maxX = p_74950_2_ + 8;
				var6.maxZ = p_74950_4_ + 2;
			}

			return StructureComponent.findIntersecting(p_74950_0_, var6) != null ? null
					: var6;
		}

		public Stairs() {
		}

		public Stairs(int p_i2038_1_, Random p_i2038_2_,
				StructureBoundingBox p_i2038_3_, int p_i2038_4_) {
			super(p_i2038_1_);
			coordBaseMode = p_i2038_4_;
			boundingBox = p_i2038_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				func_151549_a(p_74875_1_, p_74875_3_, 0, 5, 0, 2, 7, 1,
						Blocks.air, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, 0, 0, 7, 2, 2, 8,
						Blocks.air, Blocks.air, false);

				for (int var4 = 0; var4 < 5; ++var4) {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 5 - var4
							- (var4 < 4 ? 1 : 0), 2 + var4, 2, 7 - var4,
							2 + var4, Blocks.air, Blocks.air, false);
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			final int var4 = getComponentType();

			switch (coordBaseMode) {
			case 0:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX,
						boundingBox.minY, boundingBox.maxZ + 1, 0, var4);
				break;

			case 1:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX - 1,
						boundingBox.minY, boundingBox.minZ, 1, var4);
				break;

			case 2:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.minX,
						boundingBox.minY, boundingBox.minZ - 1, 2, var4);
				break;

			case 3:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_,
						p_74861_2_, p_74861_3_, boundingBox.maxX + 1,
						boundingBox.minY, boundingBox.minZ, 3, var4);
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
		}
	}

	/** List of contents that can generate in Mineshafts. */
	private static final WeightedRandomChestContent[] mineshaftChestContents = new WeightedRandomChestContent[] {
			new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
			new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
			new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
			new WeightedRandomChestContent(Items.dye, 4, 4, 9, 5),
			new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3),
			new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10),
			new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
			new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail),
					0, 4, 8, 1),
			new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10),
			new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10),
			new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1) };

	public static void func_143048_a() {
		MapGenStructureIO.func_143031_a(
				StructureMineshaftPieces.Corridor.class, "MSCorridor");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Cross.class,
				"MSCrossing");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Room.class,
				"MSRoom");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Stairs.class,
				"MSStairs");
	}

	private static StructureComponent getNextMineShaftComponent(
			StructureComponent p_78817_0_, List p_78817_1_, Random p_78817_2_,
			int p_78817_3_, int p_78817_4_, int p_78817_5_, int p_78817_6_,
			int p_78817_7_) {
		if (p_78817_7_ > 8)
			return null;
		else if (Math.abs(p_78817_3_ - p_78817_0_.getBoundingBox().minX) <= 80
				&& Math.abs(p_78817_5_ - p_78817_0_.getBoundingBox().minZ) <= 80) {
			final StructureComponent var8 = getRandomComponent(p_78817_1_,
					p_78817_2_, p_78817_3_, p_78817_4_, p_78817_5_, p_78817_6_,
					p_78817_7_ + 1);

			if (var8 != null) {
				p_78817_1_.add(var8);
				var8.buildComponent(p_78817_0_, p_78817_1_, p_78817_2_);
			}

			return var8;
		} else
			return null;
	}

	private static StructureComponent getRandomComponent(List p_78815_0_,
			Random p_78815_1_, int p_78815_2_, int p_78815_3_, int p_78815_4_,
			int p_78815_5_, int p_78815_6_) {
		final int var7 = p_78815_1_.nextInt(100);
		StructureBoundingBox var8;

		if (var7 >= 80) {
			var8 = StructureMineshaftPieces.Cross.findValidPlacement(
					p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_,
					p_78815_5_);

			if (var8 != null)
				return new StructureMineshaftPieces.Cross(p_78815_6_,
						p_78815_1_, var8, p_78815_5_);
		} else if (var7 >= 70) {
			var8 = StructureMineshaftPieces.Stairs.findValidPlacement(
					p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_,
					p_78815_5_);

			if (var8 != null)
				return new StructureMineshaftPieces.Stairs(p_78815_6_,
						p_78815_1_, var8, p_78815_5_);
		} else {
			var8 = StructureMineshaftPieces.Corridor.findValidPlacement(
					p_78815_0_, p_78815_1_, p_78815_2_, p_78815_3_, p_78815_4_,
					p_78815_5_);

			if (var8 != null)
				return new StructureMineshaftPieces.Corridor(p_78815_6_,
						p_78815_1_, var8, p_78815_5_);
		}

		return null;
	}
}
