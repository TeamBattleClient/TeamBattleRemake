package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class StructureStrongholdPieces {
	public static class ChestCorridor extends
			StructureStrongholdPieces.Stronghold {
		private static final WeightedRandomChestContent[] strongholdChestContents = new WeightedRandomChestContent[] {
				new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10),
				new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
				new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
				new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
				new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
				new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
				new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
				new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5),
				new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5),
				new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1,
						5),
				new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5),
				new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5),
				new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5),
				new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
				new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1),
				new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1,
						1),
				new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1,
						1, 1),
				new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1,
						1, 1) };

		public static StructureStrongholdPieces.ChestCorridor findValidPlacement(
				List p_75000_0_, Random p_75000_1_, int p_75000_2_,
				int p_75000_3_, int p_75000_4_, int p_75000_5_, int p_75000_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75000_2_, p_75000_3_,
							p_75000_4_, -1, -1, 0, 5, 5, 7, p_75000_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75000_0_, var7) == null ? new StructureStrongholdPieces.ChestCorridor(
					p_75000_6_, p_75000_1_, var7, p_75000_5_) : null;
		}

		private boolean hasMadeChest;

		public ChestCorridor() {
		}

		public ChestCorridor(int p_i2071_1_, Random p_i2071_2_,
				StructureBoundingBox p_i2071_3_, int p_i2071_4_) {
			super(p_i2071_1_);
			coordBaseMode = p_i2071_4_;
			field_143013_d = getRandomDoor(p_i2071_2_);
			boundingBox = p_i2071_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 4,
						6, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 1, 0);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_,
						StructureStrongholdPieces.Stronghold.Door.OPENING, 1,
						1, 6);
				func_151549_a(p_74875_1_, p_74875_3_, 3, 1, 2, 3, 1, 4,
						Blocks.stonebrick, Blocks.stonebrick, false);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 5, 3, 1, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 5, 3, 1, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 5, 3, 2, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 5, 3, 2, 4,
						p_74875_3_);
				int var4;

				for (var4 = 2; var4 <= 4; ++var4) {
					func_151550_a(p_74875_1_, Blocks.stone_slab, 5, 2, 1, var4,
							p_74875_3_);
				}

				if (!hasMadeChest) {
					var4 = getYWithOffset(2);
					final int var5 = getXWithOffset(3, 3);
					final int var6 = getZWithOffset(3, 3);

					if (p_74875_3_.isVecInside(var5, var4, var6)) {
						hasMadeChest = true;
						generateStructureChestContents(
								p_74875_1_,
								p_74875_3_,
								p_74875_2_,
								3,
								2,
								3,
								WeightedRandomChestContent
										.func_92080_a(
												strongholdChestContents,
												new WeightedRandomChestContent[] { Items.enchanted_book
														.func_92114_b(p_74875_2_) }),
								2 + p_74875_2_.nextInt(2));
					}
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 1, 1);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			hasMadeChest = p_143011_1_.getBoolean("Chest");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Chest", hasMadeChest);
		}
	}

	public static class Corridor extends StructureStrongholdPieces.Stronghold {
		public static StructureBoundingBox func_74992_a(List p_74992_0_,
				Random p_74992_1_, int p_74992_2_, int p_74992_3_,
				int p_74992_4_, int p_74992_5_) {
			StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_74992_2_, p_74992_3_,
							p_74992_4_, -1, -1, 0, 5, 5, 4, p_74992_5_);
			final StructureComponent var8 = StructureComponent
					.findIntersecting(p_74992_0_, var7);

			if (var8 == null)
				return null;
			else {
				if (var8.getBoundingBox().minY == var7.minY) {
					for (int var9 = 3; var9 >= 1; --var9) {
						var7 = StructureBoundingBox
								.getComponentToAddBoundingBox(p_74992_2_,
										p_74992_3_, p_74992_4_, -1, -1, 0, 5,
										5, var9 - 1, p_74992_5_);

						if (!var8.getBoundingBox().intersectsWith(var7))
							return StructureBoundingBox
									.getComponentToAddBoundingBox(p_74992_2_,
											p_74992_3_, p_74992_4_, -1, -1, 0,
											5, 5, var9, p_74992_5_);
					}
				}

				return null;
			}
		}

		private int field_74993_a;

		public Corridor() {
		}

		public Corridor(int p_i2072_1_, Random p_i2072_2_,
				StructureBoundingBox p_i2072_3_, int p_i2072_4_) {
			super(p_i2072_1_);
			coordBaseMode = p_i2072_4_;
			boundingBox = p_i2072_3_;
			field_74993_a = p_i2072_4_ != 2 && p_i2072_4_ != 0 ? p_i2072_3_
					.getXSize() : p_i2072_3_.getZSize();
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				for (int var4 = 0; var4 < field_74993_a; ++var4) {
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 0, 0, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 0, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2, 0, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 0, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 4, 0, var4,
							p_74875_3_);

					for (int var5 = 1; var5 <= 3; ++var5) {
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 0,
								var5, var4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.air, 0, 1, var5, var4,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.air, 0, 2, var5, var4,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.air, 0, 3, var5, var4,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 4,
								var5, var4, p_74875_3_);
					}

					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 0, 4, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 4, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2, 4, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 4, var4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 4, 4, var4,
							p_74875_3_);
				}

				return true;
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			field_74993_a = p_143011_1_.getInteger("Steps");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setInteger("Steps", field_74993_a);
		}
	}

	public static class Crossing extends StructureStrongholdPieces.Stronghold {
		public static StructureStrongholdPieces.Crossing findValidPlacement(
				List p_74994_0_, Random p_74994_1_, int p_74994_2_,
				int p_74994_3_, int p_74994_4_, int p_74994_5_, int p_74994_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_74994_2_, p_74994_3_,
							p_74994_4_, -4, -3, 0, 10, 9, 11, p_74994_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_74994_0_, var7) == null ? new StructureStrongholdPieces.Crossing(
					p_74994_6_, p_74994_1_, var7, p_74994_5_) : null;
		}

		private boolean field_74995_d;
		private boolean field_74996_b;
		private boolean field_74997_c;

		private boolean field_74999_h;

		public Crossing() {
		}

		public Crossing(int p_i2073_1_, Random p_i2073_2_,
				StructureBoundingBox p_i2073_3_, int p_i2073_4_) {
			super(p_i2073_1_);
			coordBaseMode = p_i2073_4_;
			field_143013_d = getRandomDoor(p_i2073_2_);
			boundingBox = p_i2073_3_;
			field_74996_b = p_i2073_2_.nextBoolean();
			field_74997_c = p_i2073_2_.nextBoolean();
			field_74995_d = p_i2073_2_.nextBoolean();
			field_74999_h = p_i2073_2_.nextInt(3) > 0;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 9, 8,
						10, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						4, 3, 0);

				if (field_74996_b) {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 3, 1, 0, 5, 3,
							Blocks.air, Blocks.air, false);
				}

				if (field_74995_d) {
					func_151549_a(p_74875_1_, p_74875_3_, 9, 3, 1, 9, 5, 3,
							Blocks.air, Blocks.air, false);
				}

				if (field_74997_c) {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 5, 7, 0, 7, 9,
							Blocks.air, Blocks.air, false);
				}

				if (field_74999_h) {
					func_151549_a(p_74875_1_, p_74875_3_, 9, 5, 7, 9, 7, 9,
							Blocks.air, Blocks.air, false);
				}

				func_151549_a(p_74875_1_, p_74875_3_, 5, 1, 10, 7, 3, 10,
						Blocks.air, Blocks.air, false);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 2, 1, 8, 2,
						6, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 5, 4, 4,
						9, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 8, 1, 5, 8, 4,
						9, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 4, 7, 3, 4,
						9, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 5, 3, 3,
						6, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				func_151549_a(p_74875_1_, p_74875_3_, 1, 3, 4, 3, 3, 4,
						Blocks.stone_slab, Blocks.stone_slab, false);
				func_151549_a(p_74875_1_, p_74875_3_, 1, 4, 6, 3, 4, 6,
						Blocks.stone_slab, Blocks.stone_slab, false);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 1, 7, 7, 1,
						8, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 1, 9, 7, 1, 9,
						Blocks.stone_slab, Blocks.stone_slab, false);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 2, 7, 7, 2, 7,
						Blocks.stone_slab, Blocks.stone_slab, false);
				func_151549_a(p_74875_1_, p_74875_3_, 4, 5, 7, 4, 5, 9,
						Blocks.stone_slab, Blocks.stone_slab, false);
				func_151549_a(p_74875_1_, p_74875_3_, 8, 5, 7, 8, 5, 9,
						Blocks.stone_slab, Blocks.stone_slab, false);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 5, 7, 7, 5, 9,
						Blocks.double_stone_slab, Blocks.double_stone_slab,
						false);
				func_151550_a(p_74875_1_, Blocks.torch, 0, 6, 5, 6, p_74875_3_);
				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			int var4 = 3;
			int var5 = 5;

			if (coordBaseMode == 1 || coordBaseMode == 2) {
				var4 = 8 - var4;
				var5 = 8 - var5;
			}

			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 5, 1);

			if (field_74996_b) {
				getNextComponentX(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, var4, 1);
			}

			if (field_74997_c) {
				getNextComponentX(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, var5, 7);
			}

			if (field_74995_d) {
				getNextComponentZ(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, var4, 1);
			}

			if (field_74999_h) {
				getNextComponentZ(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, var5, 7);
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			field_74996_b = p_143011_1_.getBoolean("leftLow");
			field_74997_c = p_143011_1_.getBoolean("leftHigh");
			field_74995_d = p_143011_1_.getBoolean("rightLow");
			field_74999_h = p_143011_1_.getBoolean("rightHigh");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("leftLow", field_74996_b);
			p_143012_1_.setBoolean("leftHigh", field_74997_c);
			p_143012_1_.setBoolean("rightLow", field_74995_d);
			p_143012_1_.setBoolean("rightHigh", field_74999_h);
		}
	}

	public static class LeftTurn extends StructureStrongholdPieces.Stronghold {

		public static StructureStrongholdPieces.LeftTurn findValidPlacement(
				List p_75010_0_, Random p_75010_1_, int p_75010_2_,
				int p_75010_3_, int p_75010_4_, int p_75010_5_, int p_75010_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75010_2_, p_75010_3_,
							p_75010_4_, -1, -1, 0, 5, 5, 5, p_75010_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75010_0_, var7) == null ? new StructureStrongholdPieces.LeftTurn(
					p_75010_6_, p_75010_1_, var7, p_75010_5_) : null;
		}

		public LeftTurn() {
		}

		public LeftTurn(int p_i2074_1_, Random p_i2074_2_,
				StructureBoundingBox p_i2074_3_, int p_i2074_4_) {
			super(p_i2074_1_);
			coordBaseMode = p_i2074_4_;
			field_143013_d = getRandomDoor(p_i2074_2_);
			boundingBox = p_i2074_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 4,
						4, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 1, 0);

				if (coordBaseMode != 2 && coordBaseMode != 3) {
					func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 1, 4, 3, 3,
							Blocks.air, Blocks.air, false);
				} else {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 3, 3,
							Blocks.air, Blocks.air, false);
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			if (coordBaseMode != 2 && coordBaseMode != 3) {
				getNextComponentZ(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 1);
			} else {
				getNextComponentX(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 1);
			}
		}
	}

	public static class Library extends StructureStrongholdPieces.Stronghold {
		private static final WeightedRandomChestContent[] strongholdLibraryChestContents = new WeightedRandomChestContent[] {
				new WeightedRandomChestContent(Items.book, 0, 1, 3, 20),
				new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20),
				new WeightedRandomChestContent(Items.map, 0, 1, 1, 1),
				new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1) };

		public static StructureStrongholdPieces.Library findValidPlacement(
				List p_75006_0_, Random p_75006_1_, int p_75006_2_,
				int p_75006_3_, int p_75006_4_, int p_75006_5_, int p_75006_6_) {
			StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75006_2_, p_75006_3_,
							p_75006_4_, -4, -1, 0, 14, 11, 15, p_75006_5_);

			if (!canStrongholdGoDeeper(var7)
					|| StructureComponent.findIntersecting(p_75006_0_, var7) != null) {
				var7 = StructureBoundingBox.getComponentToAddBoundingBox(
						p_75006_2_, p_75006_3_, p_75006_4_, -4, -1, 0, 14, 6,
						15, p_75006_5_);

				if (!canStrongholdGoDeeper(var7)
						|| StructureComponent
								.findIntersecting(p_75006_0_, var7) != null)
					return null;
			}

			return new StructureStrongholdPieces.Library(p_75006_6_,
					p_75006_1_, var7, p_75006_5_);
		}

		private boolean isLargeRoom;

		public Library() {
		}

		public Library(int p_i2075_1_, Random p_i2075_2_,
				StructureBoundingBox p_i2075_3_, int p_i2075_4_) {
			super(p_i2075_1_);
			coordBaseMode = p_i2075_4_;
			field_143013_d = getRandomDoor(p_i2075_2_);
			boundingBox = p_i2075_3_;
			isLargeRoom = p_i2075_3_.getYSize() > 6;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				byte var4 = 11;

				if (!isLargeRoom) {
					var4 = 6;
				}

				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 13,
						var4 - 1, 14, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						4, 1, 0);
				func_151551_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.07F, 2, 1,
						1, 11, 4, 13, Blocks.web, Blocks.web, false);
				int var7;

				for (var7 = 1; var7 <= 13; ++var7) {
					if ((var7 - 1) % 4 == 0) {
						func_151549_a(p_74875_1_, p_74875_3_, 1, 1, var7, 1, 4,
								var7, Blocks.planks, Blocks.planks, false);
						func_151549_a(p_74875_1_, p_74875_3_, 12, 1, var7, 12,
								4, var7, Blocks.planks, Blocks.planks, false);
						func_151550_a(p_74875_1_, Blocks.torch, 0, 2, 3, var7,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.torch, 0, 11, 3, var7,
								p_74875_3_);

						if (isLargeRoom) {
							func_151549_a(p_74875_1_, p_74875_3_, 1, 6, var7,
									1, 9, var7, Blocks.planks, Blocks.planks,
									false);
							func_151549_a(p_74875_1_, p_74875_3_, 12, 6, var7,
									12, 9, var7, Blocks.planks, Blocks.planks,
									false);
						}
					} else {
						func_151549_a(p_74875_1_, p_74875_3_, 1, 1, var7, 1, 4,
								var7, Blocks.bookshelf, Blocks.bookshelf, false);
						func_151549_a(p_74875_1_, p_74875_3_, 12, 1, var7, 12,
								4, var7, Blocks.bookshelf, Blocks.bookshelf,
								false);

						if (isLargeRoom) {
							func_151549_a(p_74875_1_, p_74875_3_, 1, 6, var7,
									1, 9, var7, Blocks.bookshelf,
									Blocks.bookshelf, false);
							func_151549_a(p_74875_1_, p_74875_3_, 12, 6, var7,
									12, 9, var7, Blocks.bookshelf,
									Blocks.bookshelf, false);
						}
					}
				}

				for (var7 = 3; var7 < 12; var7 += 2) {
					func_151549_a(p_74875_1_, p_74875_3_, 3, 1, var7, 4, 3,
							var7, Blocks.bookshelf, Blocks.bookshelf, false);
					func_151549_a(p_74875_1_, p_74875_3_, 6, 1, var7, 7, 3,
							var7, Blocks.bookshelf, Blocks.bookshelf, false);
					func_151549_a(p_74875_1_, p_74875_3_, 9, 1, var7, 10, 3,
							var7, Blocks.bookshelf, Blocks.bookshelf, false);
				}

				if (isLargeRoom) {
					func_151549_a(p_74875_1_, p_74875_3_, 1, 5, 1, 3, 5, 13,
							Blocks.planks, Blocks.planks, false);
					func_151549_a(p_74875_1_, p_74875_3_, 10, 5, 1, 12, 5, 13,
							Blocks.planks, Blocks.planks, false);
					func_151549_a(p_74875_1_, p_74875_3_, 4, 5, 1, 9, 5, 2,
							Blocks.planks, Blocks.planks, false);
					func_151549_a(p_74875_1_, p_74875_3_, 4, 5, 12, 9, 5, 13,
							Blocks.planks, Blocks.planks, false);
					func_151550_a(p_74875_1_, Blocks.planks, 0, 9, 5, 11,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.planks, 0, 8, 5, 11,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.planks, 0, 9, 5, 10,
							p_74875_3_);
					func_151549_a(p_74875_1_, p_74875_3_, 3, 6, 2, 3, 6, 12,
							Blocks.fence, Blocks.fence, false);
					func_151549_a(p_74875_1_, p_74875_3_, 10, 6, 2, 10, 6, 10,
							Blocks.fence, Blocks.fence, false);
					func_151549_a(p_74875_1_, p_74875_3_, 4, 6, 2, 9, 6, 2,
							Blocks.fence, Blocks.fence, false);
					func_151549_a(p_74875_1_, p_74875_3_, 4, 6, 12, 8, 6, 12,
							Blocks.fence, Blocks.fence, false);
					func_151550_a(p_74875_1_, Blocks.fence, 0, 9, 6, 11,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, 8, 6, 11,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, 9, 6, 10,
							p_74875_3_);
					var7 = func_151555_a(Blocks.ladder, 3);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 1, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 2, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 3, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 4, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 5, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 6, 13,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder, var7, 10, 7, 13,
							p_74875_3_);
					final byte var8 = 7;
					final byte var9 = 7;
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 1, 9,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8, 9, var9,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 1, 8,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8, 8, var9,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 1, 7,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8, 7, var9,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 2, 7,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 + 1, 7,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 1, 7,
							var9 - 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8 - 1, 7,
							var9 + 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8, 7,
							var9 - 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.fence, 0, var8, 7,
							var9 + 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8 - 2, 8,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8 + 1, 8,
							var9, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8 - 1, 8,
							var9 - 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8 - 1, 8,
							var9 + 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8, 8,
							var9 - 1, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, var8, 8,
							var9 + 1, p_74875_3_);
				}

				generateStructureChestContents(
						p_74875_1_,
						p_74875_3_,
						p_74875_2_,
						3,
						3,
						5,
						WeightedRandomChestContent
								.func_92080_a(
										strongholdLibraryChestContents,
										new WeightedRandomChestContent[] { Items.enchanted_book
												.func_92112_a(p_74875_2_, 1, 5,
														2) }),
						1 + p_74875_2_.nextInt(4));

				if (isLargeRoom) {
					func_151550_a(p_74875_1_, Blocks.air, 0, 12, 9, 1,
							p_74875_3_);
					generateStructureChestContents(
							p_74875_1_,
							p_74875_3_,
							p_74875_2_,
							12,
							8,
							1,
							WeightedRandomChestContent
									.func_92080_a(
											strongholdLibraryChestContents,
											new WeightedRandomChestContent[] { Items.enchanted_book
													.func_92112_a(p_74875_2_,
															1, 5, 2) }),
							1 + p_74875_2_.nextInt(4));
				}

				return true;
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			isLargeRoom = p_143011_1_.getBoolean("Tall");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Tall", isLargeRoom);
		}
	}

	static class PieceWeight {
		public int instancesLimit;
		public int instancesSpawned;
		public Class pieceClass;
		public final int pieceWeight;

		public PieceWeight(Class p_i2076_1_, int p_i2076_2_, int p_i2076_3_) {
			pieceClass = p_i2076_1_;
			pieceWeight = p_i2076_2_;
			instancesLimit = p_i2076_3_;
		}

		public boolean canSpawnMoreStructures() {
			return instancesLimit == 0 || instancesSpawned < instancesLimit;
		}

		public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
			return instancesLimit == 0 || instancesSpawned < instancesLimit;
		}
	}

	public static class PortalRoom extends StructureStrongholdPieces.Stronghold {
		public static StructureStrongholdPieces.PortalRoom findValidPlacement(
				List p_75004_0_, Random p_75004_1_, int p_75004_2_,
				int p_75004_3_, int p_75004_4_, int p_75004_5_, int p_75004_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75004_2_, p_75004_3_,
							p_75004_4_, -4, -1, 0, 11, 8, 16, p_75004_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75004_0_, var7) == null ? new StructureStrongholdPieces.PortalRoom(
					p_75004_6_, p_75004_1_, var7, p_75004_5_) : null;
		}

		private boolean hasSpawner;

		public PortalRoom() {
		}

		public PortalRoom(int p_i2077_1_, Random p_i2077_2_,
				StructureBoundingBox p_i2077_3_, int p_i2077_4_) {
			super(p_i2077_1_);
			coordBaseMode = p_i2077_4_;
			boundingBox = p_i2077_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 10, 7,
					15, false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			placeDoor(p_74875_1_, p_74875_2_, p_74875_3_,
					StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
			final byte var4 = 6;
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, var4, 1, 1,
					var4, 14, false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, var4, 1, 9,
					var4, 14, false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, var4, 1, 8,
					var4, 2, false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, var4, 14, 8,
					var4, 14, false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 2, 1, 4,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 8, 1, 1, 9, 1, 4,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			func_151549_a(p_74875_1_, p_74875_3_, 1, 1, 1, 1, 1, 3,
					Blocks.flowing_lava, Blocks.flowing_lava, false);
			func_151549_a(p_74875_1_, p_74875_3_, 9, 1, 1, 9, 1, 3,
					Blocks.flowing_lava, Blocks.flowing_lava, false);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 3, 1, 8, 7, 1, 12,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 9, 6, 1, 11,
					Blocks.flowing_lava, Blocks.flowing_lava, false);
			int var5;

			for (var5 = 3; var5 < 14; var5 += 2) {
				func_151549_a(p_74875_1_, p_74875_3_, 0, 3, var5, 0, 4, var5,
						Blocks.iron_bars, Blocks.iron_bars, false);
				func_151549_a(p_74875_1_, p_74875_3_, 10, 3, var5, 10, 4, var5,
						Blocks.iron_bars, Blocks.iron_bars, false);
			}

			for (var5 = 2; var5 < 9; var5 += 2) {
				func_151549_a(p_74875_1_, p_74875_3_, var5, 3, 15, var5, 4, 15,
						Blocks.iron_bars, Blocks.iron_bars, false);
			}

			var5 = func_151555_a(Blocks.stone_brick_stairs, 3);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 5, 6, 1, 7,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 2, 6, 6, 2, 7,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);
			fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 3, 7, 6, 3, 7,
					false, p_74875_2_,
					StructureStrongholdPieces.strongholdStones);

			for (int var6 = 4; var6 <= 6; ++var6) {
				func_151550_a(p_74875_1_, Blocks.stone_brick_stairs, var5,
						var6, 1, 4, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_brick_stairs, var5,
						var6, 2, 5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_brick_stairs, var5,
						var6, 3, 6, p_74875_3_);
			}

			byte var14 = 2;
			byte var7 = 0;
			byte var8 = 3;
			byte var9 = 1;

			switch (coordBaseMode) {
			case 0:
				var14 = 0;
				var7 = 2;
				break;

			case 1:
				var14 = 1;
				var7 = 3;
				var8 = 0;
				var9 = 2;

			case 2:
			default:
				break;

			case 3:
				var14 = 3;
				var7 = 1;
				var8 = 0;
				var9 = 2;
			}

			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var14
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 4, 3, 8,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var14
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 5, 3, 8,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var14
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 6, 3, 8,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var7
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 4, 3, 12,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var7
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 5, 3, 12,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var7
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 6, 3, 12,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var8
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 3, 3, 9,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var8
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 3, 3, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var8
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 3, 3, 11,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var9
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 7, 3, 9,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var9
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 7, 3, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.end_portal_frame, var9
					+ (p_74875_2_.nextFloat() > 0.9F ? 4 : 0), 7, 3, 11,
					p_74875_3_);

			if (!hasSpawner) {
				final int var13 = getYWithOffset(3);
				final int var10 = getXWithOffset(5, 6);
				final int var11 = getZWithOffset(5, 6);

				if (p_74875_3_.isVecInside(var10, var13, var11)) {
					hasSpawner = true;
					p_74875_1_.setBlock(var10, var13, var11,
							Blocks.mob_spawner, 0, 2);
					final TileEntityMobSpawner var12 = (TileEntityMobSpawner) p_74875_1_
							.getTileEntity(var10, var13, var11);

					if (var12 != null) {
						var12.func_145881_a().setMobID("Silverfish");
					}
				}
			}

			return true;
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			if (p_74861_1_ != null) {
				((StructureStrongholdPieces.Stairs2) p_74861_1_).strongholdPortalRoom = this;
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			hasSpawner = p_143011_1_.getBoolean("Mob");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Mob", hasSpawner);
		}
	}

	public static class Prison extends StructureStrongholdPieces.Stronghold {

		public static StructureStrongholdPieces.Prison findValidPlacement(
				List p_75016_0_, Random p_75016_1_, int p_75016_2_,
				int p_75016_3_, int p_75016_4_, int p_75016_5_, int p_75016_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75016_2_, p_75016_3_,
							p_75016_4_, -1, -1, 0, 9, 5, 11, p_75016_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75016_0_, var7) == null ? new StructureStrongholdPieces.Prison(
					p_75016_6_, p_75016_1_, var7, p_75016_5_) : null;
		}

		public Prison() {
		}

		public Prison(int p_i2078_1_, Random p_i2078_2_,
				StructureBoundingBox p_i2078_3_, int p_i2078_4_) {
			super(p_i2078_1_);
			coordBaseMode = p_i2078_4_;
			field_143013_d = getRandomDoor(p_i2078_2_);
			boundingBox = p_i2078_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 8, 4,
						10, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 1, 0);
				func_151549_a(p_74875_1_, p_74875_3_, 1, 1, 10, 3, 3, 10,
						Blocks.air, Blocks.air, false);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 1, 4, 3,
						1, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 3, 4, 3,
						3, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 7, 4, 3,
						7, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 9, 4, 3,
						9, false, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 4, 4, 3, 6,
						Blocks.iron_bars, Blocks.iron_bars, false);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 1, 5, 7, 3, 5,
						Blocks.iron_bars, Blocks.iron_bars, false);
				func_151550_a(p_74875_1_, Blocks.iron_bars, 0, 4, 3, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.iron_bars, 0, 4, 3, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.iron_door,
						func_151555_a(Blocks.iron_door, 3), 4, 1, 2, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.iron_door,
						func_151555_a(Blocks.iron_door, 3) + 8, 4, 2, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.iron_door,
						func_151555_a(Blocks.iron_door, 3), 4, 1, 8, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.iron_door,
						func_151555_a(Blocks.iron_door, 3) + 8, 4, 2, 8,
						p_74875_3_);
				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 1, 1);
		}
	}

	public static class RightTurn extends StructureStrongholdPieces.LeftTurn {

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 4,
						4, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 1, 0);

				if (coordBaseMode != 2 && coordBaseMode != 3) {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 3, 3,
							Blocks.air, Blocks.air, false);
				} else {
					func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 1, 4, 3, 3,
							Blocks.air, Blocks.air, false);
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			if (coordBaseMode != 2 && coordBaseMode != 3) {
				getNextComponentX(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 1);
			} else {
				getNextComponentZ(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 1);
			}
		}
	}

	public static class RoomCrossing extends
			StructureStrongholdPieces.Stronghold {
		private static final WeightedRandomChestContent[] strongholdRoomCrossingChestContents = new WeightedRandomChestContent[] {
				new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
				new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
				new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
				new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10),
				new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
				new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
				new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1) };

		public static StructureStrongholdPieces.RoomCrossing findValidPlacement(
				List p_75012_0_, Random p_75012_1_, int p_75012_2_,
				int p_75012_3_, int p_75012_4_, int p_75012_5_, int p_75012_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75012_2_, p_75012_3_,
							p_75012_4_, -4, -1, 0, 11, 7, 11, p_75012_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75012_0_, var7) == null ? new StructureStrongholdPieces.RoomCrossing(
					p_75012_6_, p_75012_1_, var7, p_75012_5_) : null;
		}

		protected int roomType;

		public RoomCrossing() {
		}

		public RoomCrossing(int p_i2079_1_, Random p_i2079_2_,
				StructureBoundingBox p_i2079_3_, int p_i2079_4_) {
			super(p_i2079_1_);
			coordBaseMode = p_i2079_4_;
			field_143013_d = getRandomDoor(p_i2079_2_);
			boundingBox = p_i2079_3_;
			roomType = p_i2079_2_.nextInt(5);
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 10,
						6, 10, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						4, 1, 0);
				func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 10, 6, 3, 10,
						Blocks.air, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, 0, 1, 4, 0, 3, 6,
						Blocks.air, Blocks.air, false);
				func_151549_a(p_74875_1_, p_74875_3_, 10, 1, 4, 10, 3, 6,
						Blocks.air, Blocks.air, false);
				int var4;

				switch (roomType) {
				case 0:
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 2, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 3, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, 4, 3, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, 6, 3, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, 5, 3, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.torch, 0, 5, 3, 6,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 4, 1, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 4, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 4, 1, 6,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 6, 1, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 6, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 6, 1, 6,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 5, 1, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 5, 1, 6,
							p_74875_3_);
					break;

				case 1:
					for (var4 = 0; var4 < 5; ++var4) {
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 1,
								3 + var4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 7, 1,
								3 + var4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0,
								3 + var4, 1, 3, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0,
								3 + var4, 1, 7, p_74875_3_);
					}

					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 2, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 5, 3, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.flowing_water, 0, 5, 4, 5,
							p_74875_3_);
					break;

				case 2:
					for (var4 = 1; var4 <= 9; ++var4) {
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 1, 3,
								var4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 9, 3,
								var4, p_74875_3_);
					}

					for (var4 = 1; var4 <= 9; ++var4) {
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, var4,
								3, 1, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, var4,
								3, 9, p_74875_3_);
					}

					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 5, 1, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 5, 1, 6,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 5, 3, 4,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 5, 3, 6,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 4, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 6, 1, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 4, 3, 5,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 6, 3, 5,
							p_74875_3_);

					for (var4 = 1; var4 <= 3; ++var4) {
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 4,
								var4, 4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 6,
								var4, 4, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 4,
								var4, 6, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.cobblestone, 0, 6,
								var4, 6, p_74875_3_);
					}

					func_151550_a(p_74875_1_, Blocks.torch, 0, 5, 3, 5,
							p_74875_3_);

					for (var4 = 2; var4 <= 8; ++var4) {
						func_151550_a(p_74875_1_, Blocks.planks, 0, 2, 3, var4,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.planks, 0, 3, 3, var4,
								p_74875_3_);

						if (var4 <= 3 || var4 >= 7) {
							func_151550_a(p_74875_1_, Blocks.planks, 0, 4, 3,
									var4, p_74875_3_);
							func_151550_a(p_74875_1_, Blocks.planks, 0, 5, 3,
									var4, p_74875_3_);
							func_151550_a(p_74875_1_, Blocks.planks, 0, 6, 3,
									var4, p_74875_3_);
						}

						func_151550_a(p_74875_1_, Blocks.planks, 0, 7, 3, var4,
								p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.planks, 0, 8, 3, var4,
								p_74875_3_);
					}

					func_151550_a(p_74875_1_, Blocks.ladder,
							func_151555_a(Blocks.ladder, 4), 9, 1, 3,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder,
							func_151555_a(Blocks.ladder, 4), 9, 2, 3,
							p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.ladder,
							func_151555_a(Blocks.ladder, 4), 9, 3, 3,
							p_74875_3_);
					generateStructureChestContents(
							p_74875_1_,
							p_74875_3_,
							p_74875_2_,
							3,
							4,
							8,
							WeightedRandomChestContent
									.func_92080_a(
											strongholdRoomCrossingChestContents,
											new WeightedRandomChestContent[] { Items.enchanted_book
													.func_92114_b(p_74875_2_) }),
							1 + p_74875_2_.nextInt(4));
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 4, 1);
			getNextComponentX((StructureStrongholdPieces.Stairs2) p_74861_1_,
					p_74861_2_, p_74861_3_, 1, 4);
			getNextComponentZ((StructureStrongholdPieces.Stairs2) p_74861_1_,
					p_74861_2_, p_74861_3_, 1, 4);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			roomType = p_143011_1_.getInteger("Type");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setInteger("Type", roomType);
		}
	}

	public static class Stairs extends StructureStrongholdPieces.Stronghold {
		public static StructureStrongholdPieces.Stairs getStrongholdStairsComponent(
				List p_75022_0_, Random p_75022_1_, int p_75022_2_,
				int p_75022_3_, int p_75022_4_, int p_75022_5_, int p_75022_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75022_2_, p_75022_3_,
							p_75022_4_, -1, -7, 0, 5, 11, 5, p_75022_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75022_0_, var7) == null ? new StructureStrongholdPieces.Stairs(
					p_75022_6_, p_75022_1_, var7, p_75022_5_) : null;
		}

		private boolean field_75024_a;

		public Stairs() {
		}

		public Stairs(int p_i2081_1_, Random p_i2081_2_, int p_i2081_3_,
				int p_i2081_4_) {
			super(p_i2081_1_);
			field_75024_a = true;
			coordBaseMode = p_i2081_2_.nextInt(4);
			field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;

			switch (coordBaseMode) {
			case 0:
			case 2:
				boundingBox = new StructureBoundingBox(p_i2081_3_, 64,
						p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
				break;

			default:
				boundingBox = new StructureBoundingBox(p_i2081_3_, 64,
						p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
			}
		}

		public Stairs(int p_i2082_1_, Random p_i2082_2_,
				StructureBoundingBox p_i2082_3_, int p_i2082_4_) {
			super(p_i2082_1_);
			field_75024_a = false;
			coordBaseMode = p_i2082_4_;
			field_143013_d = getRandomDoor(p_i2082_2_);
			boundingBox = p_i2082_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4,
						10, 4, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 7, 0);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_,
						StructureStrongholdPieces.Stronghold.Door.OPENING, 1,
						1, 4);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2, 6, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 5, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 1, 6, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 5, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 4, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 1, 5, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2, 4, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 3, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 3, 4, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 3, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3, 2, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 3, 3, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2, 2, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 1, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 1, 2, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1, 1, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_slab, 0, 1, 1, 3,
						p_74875_3_);
				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			if (field_75024_a) {
				StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
			}

			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 1, 1);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			field_75024_a = p_143011_1_.getBoolean("Source");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Source", field_75024_a);
		}
	}

	public static class Stairs2 extends StructureStrongholdPieces.Stairs {
		public List field_75026_c = new ArrayList();
		public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
		public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;

		public Stairs2() {
		}

		public Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_,
				int p_i2083_4_) {
			super(0, p_i2083_2_, p_i2083_3_, p_i2083_4_);
		}

		@Override
		public ChunkPosition func_151553_a() {
			return strongholdPortalRoom != null ? strongholdPortalRoom
					.func_151553_a() : super.func_151553_a();
		}
	}

	public static class StairsStraight extends
			StructureStrongholdPieces.Stronghold {

		public static StructureStrongholdPieces.StairsStraight findValidPlacement(
				List p_75028_0_, Random p_75028_1_, int p_75028_2_,
				int p_75028_3_, int p_75028_4_, int p_75028_5_, int p_75028_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75028_2_, p_75028_3_,
							p_75028_4_, -1, -7, 0, 5, 11, 8, p_75028_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75028_0_, var7) == null ? new StructureStrongholdPieces.StairsStraight(
					p_75028_6_, p_75028_1_, var7, p_75028_5_) : null;
		}

		public StairsStraight() {
		}

		public StairsStraight(int p_i2085_1_, Random p_i2085_2_,
				StructureBoundingBox p_i2085_3_, int p_i2085_4_) {
			super(p_i2085_1_);
			coordBaseMode = p_i2085_4_;
			field_143013_d = getRandomDoor(p_i2085_2_);
			boundingBox = p_i2085_3_;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4,
						10, 7, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 7, 0);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_,
						StructureStrongholdPieces.Stronghold.Door.OPENING, 1,
						1, 7);
				final int var4 = func_151555_a(Blocks.stone_stairs, 2);

				for (int var5 = 0; var5 < 6; ++var5) {
					func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 1,
							6 - var5, 1 + var5, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 2,
							6 - var5, 1 + var5, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 3,
							6 - var5, 1 + var5, p_74875_3_);

					if (var5 < 5) {
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 1,
								5 - var5, 1 + var5, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 2,
								5 - var5, 1 + var5, p_74875_3_);
						func_151550_a(p_74875_1_, Blocks.stonebrick, 0, 3,
								5 - var5, 1 + var5, p_74875_3_);
					}
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 1, 1);
		}
	}

	static class Stones extends StructureComponent.BlockSelector {

		private Stones() {
		}

		Stones(Object p_i2080_1_) {
			this();
		}

		@Override
		public void selectBlocks(Random p_75062_1_, int p_75062_2_,
				int p_75062_3_, int p_75062_4_, boolean p_75062_5_) {
			if (p_75062_5_) {
				field_151562_a = Blocks.stonebrick;
				final float var6 = p_75062_1_.nextFloat();

				if (var6 < 0.2F) {
					selectedBlockMetaData = 2;
				} else if (var6 < 0.5F) {
					selectedBlockMetaData = 1;
				} else if (var6 < 0.55F) {
					field_151562_a = Blocks.monster_egg;
					selectedBlockMetaData = 2;
				} else {
					selectedBlockMetaData = 0;
				}
			} else {
				field_151562_a = Blocks.air;
				selectedBlockMetaData = 0;
			}
		}
	}

	public static class Straight extends StructureStrongholdPieces.Stronghold {
		public static StructureStrongholdPieces.Straight findValidPlacement(
				List p_75018_0_, Random p_75018_1_, int p_75018_2_,
				int p_75018_3_, int p_75018_4_, int p_75018_5_, int p_75018_6_) {
			final StructureBoundingBox var7 = StructureBoundingBox
					.getComponentToAddBoundingBox(p_75018_2_, p_75018_3_,
							p_75018_4_, -1, -1, 0, 5, 5, 7, p_75018_5_);
			return canStrongholdGoDeeper(var7)
					&& StructureComponent.findIntersecting(p_75018_0_, var7) == null ? new StructureStrongholdPieces.Straight(
					p_75018_6_, p_75018_1_, var7, p_75018_5_) : null;
		}

		private boolean expandsX;

		private boolean expandsZ;

		public Straight() {
		}

		public Straight(int p_i2084_1_, Random p_i2084_2_,
				StructureBoundingBox p_i2084_3_, int p_i2084_4_) {
			super(p_i2084_1_);
			coordBaseMode = p_i2084_4_;
			field_143013_d = getRandomDoor(p_i2084_2_);
			boundingBox = p_i2084_3_;
			expandsX = p_i2084_2_.nextInt(2) == 0;
			expandsZ = p_i2084_2_.nextInt(2) == 0;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 4,
						6, true, p_74875_2_,
						StructureStrongholdPieces.strongholdStones);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_, field_143013_d,
						1, 1, 0);
				placeDoor(p_74875_1_, p_74875_2_, p_74875_3_,
						StructureStrongholdPieces.Stronghold.Door.OPENING, 1,
						1, 6);
				func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 1, 2,
						1, Blocks.torch, 0);
				func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 3, 2,
						1, Blocks.torch, 0);
				func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 1, 2,
						5, Blocks.torch, 0);
				func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 3, 2,
						5, Blocks.torch, 0);

				if (expandsX) {
					func_151549_a(p_74875_1_, p_74875_3_, 0, 1, 2, 0, 3, 4,
							Blocks.air, Blocks.air, false);
				}

				if (expandsZ) {
					func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 2, 4, 3, 4,
							Blocks.air, Blocks.air, false);
				}

				return true;
			}
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_,
				List p_74861_2_, Random p_74861_3_) {
			getNextComponentNormal(
					(StructureStrongholdPieces.Stairs2) p_74861_1_, p_74861_2_,
					p_74861_3_, 1, 1);

			if (expandsX) {
				getNextComponentX(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 2);
			}

			if (expandsZ) {
				getNextComponentZ(
						(StructureStrongholdPieces.Stairs2) p_74861_1_,
						p_74861_2_, p_74861_3_, 1, 2);
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			expandsX = p_143011_1_.getBoolean("Left");
			expandsZ = p_143011_1_.getBoolean("Right");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Left", expandsX);
			p_143012_1_.setBoolean("Right", expandsZ);
		}
	}

	abstract static class Stronghold extends StructureComponent {
		public static enum Door {
			GRATES("GRATES", 2), IRON_DOOR("IRON_DOOR", 3), OPENING("OPENING",
					0), WOOD_DOOR("WOOD_DOOR", 1);

			private Door(String p_i2086_1_, int p_i2086_2_) {
			}
		}

		protected static boolean canStrongholdGoDeeper(
				StructureBoundingBox p_74991_0_) {
			return p_74991_0_ != null && p_74991_0_.minY > 10;
		}

		protected StructureStrongholdPieces.Stronghold.Door field_143013_d;

		public Stronghold() {
			field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
		}

		protected Stronghold(int p_i2087_1_) {
			super(p_i2087_1_);
			field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			field_143013_d = StructureStrongholdPieces.Stronghold.Door
					.valueOf(p_143011_1_.getString("EntryDoor"));
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setString("EntryDoor", field_143013_d.name());
		}

		protected StructureComponent getNextComponentNormal(
				StructureStrongholdPieces.Stairs2 p_74986_1_, List p_74986_2_,
				Random p_74986_3_, int p_74986_4_, int p_74986_5_) {
			switch (coordBaseMode) {
			case 0:
				return StructureStrongholdPieces
						.getNextValidComponent(p_74986_1_, p_74986_2_,
								p_74986_3_, boundingBox.minX + p_74986_4_,
								boundingBox.minY + p_74986_5_,
								boundingBox.maxZ + 1, coordBaseMode,
								getComponentType());

			case 1:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74986_1_, p_74986_2_, p_74986_3_,
						boundingBox.minX - 1, boundingBox.minY + p_74986_5_,
						boundingBox.minZ + p_74986_4_, coordBaseMode,
						getComponentType());

			case 2:
				return StructureStrongholdPieces
						.getNextValidComponent(p_74986_1_, p_74986_2_,
								p_74986_3_, boundingBox.minX + p_74986_4_,
								boundingBox.minY + p_74986_5_,
								boundingBox.minZ - 1, coordBaseMode,
								getComponentType());

			case 3:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74986_1_, p_74986_2_, p_74986_3_,
						boundingBox.maxX + 1, boundingBox.minY + p_74986_5_,
						boundingBox.minZ + p_74986_4_, coordBaseMode,
						getComponentType());

			default:
				return null;
			}
		}

		protected StructureComponent getNextComponentX(
				StructureStrongholdPieces.Stairs2 p_74989_1_, List p_74989_2_,
				Random p_74989_3_, int p_74989_4_, int p_74989_5_) {
			switch (coordBaseMode) {
			case 0:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74989_1_, p_74989_2_, p_74989_3_,
						boundingBox.minX - 1, boundingBox.minY + p_74989_4_,
						boundingBox.minZ + p_74989_5_, 1, getComponentType());

			case 1:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74989_1_, p_74989_2_, p_74989_3_, boundingBox.minX
								+ p_74989_5_, boundingBox.minY + p_74989_4_,
						boundingBox.minZ - 1, 2, getComponentType());

			case 2:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74989_1_, p_74989_2_, p_74989_3_,
						boundingBox.minX - 1, boundingBox.minY + p_74989_4_,
						boundingBox.minZ + p_74989_5_, 1, getComponentType());

			case 3:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74989_1_, p_74989_2_, p_74989_3_, boundingBox.minX
								+ p_74989_5_, boundingBox.minY + p_74989_4_,
						boundingBox.minZ - 1, 2, getComponentType());

			default:
				return null;
			}
		}

		protected StructureComponent getNextComponentZ(
				StructureStrongholdPieces.Stairs2 p_74987_1_, List p_74987_2_,
				Random p_74987_3_, int p_74987_4_, int p_74987_5_) {
			switch (coordBaseMode) {
			case 0:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74987_1_, p_74987_2_, p_74987_3_,
						boundingBox.maxX + 1, boundingBox.minY + p_74987_4_,
						boundingBox.minZ + p_74987_5_, 3, getComponentType());

			case 1:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74987_1_, p_74987_2_, p_74987_3_, boundingBox.minX
								+ p_74987_5_, boundingBox.minY + p_74987_4_,
						boundingBox.maxZ + 1, 0, getComponentType());

			case 2:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74987_1_, p_74987_2_, p_74987_3_,
						boundingBox.maxX + 1, boundingBox.minY + p_74987_4_,
						boundingBox.minZ + p_74987_5_, 3, getComponentType());

			case 3:
				return StructureStrongholdPieces.getNextValidComponent(
						p_74987_1_, p_74987_2_, p_74987_3_, boundingBox.minX
								+ p_74987_5_, boundingBox.minY + p_74987_4_,
						boundingBox.maxZ + 1, 0, getComponentType());

			default:
				return null;
			}
		}

		protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(
				Random p_74988_1_) {
			final int var2 = p_74988_1_.nextInt(5);

			switch (var2) {
			case 0:
			case 1:
			default:
				return StructureStrongholdPieces.Stronghold.Door.OPENING;

			case 2:
				return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;

			case 3:
				return StructureStrongholdPieces.Stronghold.Door.GRATES;

			case 4:
				return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
			}
		}

		protected void placeDoor(World p_74990_1_, Random p_74990_2_,
				StructureBoundingBox p_74990_3_,
				StructureStrongholdPieces.Stronghold.Door p_74990_4_,
				int p_74990_5_, int p_74990_6_, int p_74990_7_) {
			switch (StructureStrongholdPieces.SwitchDoor.doorEnum[p_74990_4_
					.ordinal()]) {
			case 1:
			default:
				func_151549_a(p_74990_1_, p_74990_3_, p_74990_5_, p_74990_6_,
						p_74990_7_, p_74990_5_ + 3 - 1, p_74990_6_ + 3 - 1,
						p_74990_7_, Blocks.air, Blocks.air, false);
				break;

			case 2:
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 1,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.wooden_door, 0,
						p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.wooden_door, 8,
						p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				break;

			case 3:
				func_151550_a(p_74990_1_, Blocks.air, 0, p_74990_5_ + 1,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.air, 0, p_74990_5_ + 1,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_ + 1,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_ + 2,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_ + 2,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_bars, 0, p_74990_5_ + 2,
						p_74990_6_, p_74990_7_, p_74990_3_);
				break;

			case 4:
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 1,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_ + 2, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stonebrick, 0, p_74990_5_ + 2,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_door, 0, p_74990_5_ + 1,
						p_74990_6_, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.iron_door, 8, p_74990_5_ + 1,
						p_74990_6_ + 1, p_74990_7_, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stone_button,
						func_151555_a(Blocks.stone_button, 4), p_74990_5_ + 2,
						p_74990_6_ + 1, p_74990_7_ + 1, p_74990_3_);
				func_151550_a(p_74990_1_, Blocks.stone_button,
						func_151555_a(Blocks.stone_button, 3), p_74990_5_ + 2,
						p_74990_6_ + 1, p_74990_7_ - 1, p_74990_3_);
			}
		}
	}

	static final class SwitchDoor {
		static final int[] doorEnum = new int[StructureStrongholdPieces.Stronghold.Door
				.values().length];

		static {
			try {
				doorEnum[StructureStrongholdPieces.Stronghold.Door.OPENING
						.ordinal()] = 1;
			} catch (final NoSuchFieldError var4) {
				;
			}

			try {
				doorEnum[StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR
						.ordinal()] = 2;
			} catch (final NoSuchFieldError var3) {
				;
			}

			try {
				doorEnum[StructureStrongholdPieces.Stronghold.Door.GRATES
						.ordinal()] = 3;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				doorEnum[StructureStrongholdPieces.Stronghold.Door.IRON_DOOR
						.ordinal()] = 4;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private static final StructureStrongholdPieces.PieceWeight[] pieceWeightArray = new StructureStrongholdPieces.PieceWeight[] {
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.Straight.class, 40, 0),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.Prison.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.LeftTurn.class, 20, 0),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.RightTurn.class, 20, 0),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.RoomCrossing.class, 10, 6),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.StairsStraight.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.Stairs.class, 5, 5),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.Crossing.class, 5, 4),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.ChestCorridor.class, 5, 4),
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.Library.class, 10, 2) {

				@Override
				public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
					return super.canSpawnMoreStructuresOfType(p_75189_1_)
							&& p_75189_1_ > 4;
				}
			},
			new StructureStrongholdPieces.PieceWeight(
					StructureStrongholdPieces.PortalRoom.class, 20, 1) {

				@Override
				public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
					return super.canSpawnMoreStructuresOfType(p_75189_1_)
							&& p_75189_1_ > 5;
				}
			} };

	private static Class strongComponentType;

	private static final StructureStrongholdPieces.Stones strongholdStones = new StructureStrongholdPieces.Stones(
			null);

	private static List structurePieceList;

	static int totalWeight;

	private static boolean canAddStructurePieces() {
		boolean var0 = false;
		totalWeight = 0;
		StructureStrongholdPieces.PieceWeight var2;

		for (final Iterator var1 = structurePieceList.iterator(); var1
				.hasNext(); totalWeight += var2.pieceWeight) {
			var2 = (StructureStrongholdPieces.PieceWeight) var1.next();

			if (var2.instancesLimit > 0
					&& var2.instancesSpawned < var2.instancesLimit) {
				var0 = true;
			}
		}

		return var0;
	}

	public static void func_143046_a() {
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.ChestCorridor.class, "SHCC");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.Corridor.class, "SHFC");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.Crossing.class, "SH5C");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.LeftTurn.class, "SHLT");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.Library.class, "SHLi");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.PortalRoom.class, "SHPR");
		MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Prison.class,
				"SHPH");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.RightTurn.class, "SHRT");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.RoomCrossing.class, "SHRC");
		MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Stairs.class,
				"SHSD");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.Stairs2.class, "SHStart");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.Straight.class, "SHS");
		MapGenStructureIO.func_143031_a(
				StructureStrongholdPieces.StairsStraight.class, "SHSSD");
	}

	private static StructureStrongholdPieces.Stronghold getNextComponent(
			StructureStrongholdPieces.Stairs2 p_75201_0_, List p_75201_1_,
			Random p_75201_2_, int p_75201_3_, int p_75201_4_, int p_75201_5_,
			int p_75201_6_, int p_75201_7_) {
		if (!canAddStructurePieces())
			return null;
		else {
			if (strongComponentType != null) {
				final StructureStrongholdPieces.Stronghold var8 = getStrongholdComponentFromWeightedPiece(
						strongComponentType, p_75201_1_, p_75201_2_,
						p_75201_3_, p_75201_4_, p_75201_5_, p_75201_6_,
						p_75201_7_);
				strongComponentType = null;

				if (var8 != null)
					return var8;
			}

			int var13 = 0;

			while (var13 < 5) {
				++var13;
				int var9 = p_75201_2_.nextInt(totalWeight);
				final Iterator var10 = structurePieceList.iterator();

				while (var10.hasNext()) {
					final StructureStrongholdPieces.PieceWeight var11 = (StructureStrongholdPieces.PieceWeight) var10
							.next();
					var9 -= var11.pieceWeight;

					if (var9 < 0) {
						if (!var11.canSpawnMoreStructuresOfType(p_75201_7_)
								|| var11 == p_75201_0_.strongholdPieceWeight) {
							break;
						}

						final StructureStrongholdPieces.Stronghold var12 = getStrongholdComponentFromWeightedPiece(
								var11.pieceClass, p_75201_1_, p_75201_2_,
								p_75201_3_, p_75201_4_, p_75201_5_, p_75201_6_,
								p_75201_7_);

						if (var12 != null) {
							++var11.instancesSpawned;
							p_75201_0_.strongholdPieceWeight = var11;

							if (!var11.canSpawnMoreStructures()) {
								structurePieceList.remove(var11);
							}

							return var12;
						}
					}
				}
			}

			final StructureBoundingBox var14 = StructureStrongholdPieces.Corridor
					.func_74992_a(p_75201_1_, p_75201_2_, p_75201_3_,
							p_75201_4_, p_75201_5_, p_75201_6_);

			if (var14 != null && var14.minY > 1)
				return new StructureStrongholdPieces.Corridor(p_75201_7_,
						p_75201_2_, var14, p_75201_6_);
			else
				return null;
		}
	}

	private static StructureComponent getNextValidComponent(
			StructureStrongholdPieces.Stairs2 p_75196_0_, List p_75196_1_,
			Random p_75196_2_, int p_75196_3_, int p_75196_4_, int p_75196_5_,
			int p_75196_6_, int p_75196_7_) {
		if (p_75196_7_ > 50)
			return null;
		else if (Math.abs(p_75196_3_ - p_75196_0_.getBoundingBox().minX) <= 112
				&& Math.abs(p_75196_5_ - p_75196_0_.getBoundingBox().minZ) <= 112) {
			final StructureStrongholdPieces.Stronghold var8 = getNextComponent(
					p_75196_0_, p_75196_1_, p_75196_2_, p_75196_3_, p_75196_4_,
					p_75196_5_, p_75196_6_, p_75196_7_ + 1);

			if (var8 != null) {
				p_75196_1_.add(var8);
				p_75196_0_.field_75026_c.add(var8);
			}

			return var8;
		} else
			return null;
	}

	/**
	 * translates the PieceWeight class to the Component class
	 */
	private static StructureStrongholdPieces.Stronghold getStrongholdComponentFromWeightedPiece(
			Class p_75200_0_, List p_75200_1_, Random p_75200_2_,
			int p_75200_3_, int p_75200_4_, int p_75200_5_, int p_75200_6_,
			int p_75200_7_) {
		Object var8 = null;

		if (p_75200_0_ == StructureStrongholdPieces.Straight.class) {
			var8 = StructureStrongholdPieces.Straight.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.Prison.class) {
			var8 = StructureStrongholdPieces.Prison.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.LeftTurn.class) {
			var8 = StructureStrongholdPieces.LeftTurn.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.RightTurn.class) {
			var8 = LeftTurn.findValidPlacement(p_75200_1_, p_75200_2_,
					p_75200_3_, p_75200_4_, p_75200_5_, p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.RoomCrossing.class) {
			var8 = StructureStrongholdPieces.RoomCrossing.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.StairsStraight.class) {
			var8 = StructureStrongholdPieces.StairsStraight.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.Stairs.class) {
			var8 = StructureStrongholdPieces.Stairs
					.getStrongholdStairsComponent(p_75200_1_, p_75200_2_,
							p_75200_3_, p_75200_4_, p_75200_5_, p_75200_6_,
							p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.Crossing.class) {
			var8 = StructureStrongholdPieces.Crossing.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.ChestCorridor.class) {
			var8 = StructureStrongholdPieces.ChestCorridor.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.Library.class) {
			var8 = StructureStrongholdPieces.Library.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		} else if (p_75200_0_ == StructureStrongholdPieces.PortalRoom.class) {
			var8 = StructureStrongholdPieces.PortalRoom.findValidPlacement(
					p_75200_1_, p_75200_2_, p_75200_3_, p_75200_4_, p_75200_5_,
					p_75200_6_, p_75200_7_);
		}

		return (StructureStrongholdPieces.Stronghold) var8;
	}

	/**
	 * sets up Arrays with the Structure pieces and their weights
	 */
	public static void prepareStructurePieces() {
		structurePieceList = new ArrayList();
		final StructureStrongholdPieces.PieceWeight[] var0 = pieceWeightArray;
		final int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			final StructureStrongholdPieces.PieceWeight var3 = var0[var2];
			var3.instancesSpawned = 0;
			structurePieceList.add(var3);
		}

		strongComponentType = null;
	}
}
