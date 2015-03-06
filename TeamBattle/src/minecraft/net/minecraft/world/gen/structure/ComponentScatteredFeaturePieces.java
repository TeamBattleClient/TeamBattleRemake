package net.minecraft.world.gen.structure;

import java.util.Random;

import net.minecraft.block.BlockLever;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class ComponentScatteredFeaturePieces {

	public static class DesertPyramid extends
			ComponentScatteredFeaturePieces.Feature {
		private static final WeightedRandomChestContent[] itemsToGenerateInTemple = new WeightedRandomChestContent[] {
				new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
				new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
				new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
				new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
				new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
				new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
				new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
				new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1,
						1),
				new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1,
						1, 1),
				new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1,
						1, 1) };
		private final boolean[] field_74940_h = new boolean[4];

		public DesertPyramid() {
		}

		public DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_) {
			super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			func_151549_a(p_74875_1_, p_74875_3_, 0, -4, 0,
					scatteredFeatureSizeX - 1, 0, scatteredFeatureSizeZ - 1,
					Blocks.sandstone, Blocks.sandstone, false);
			int var4;

			for (var4 = 1; var4 <= 9; ++var4) {
				func_151549_a(p_74875_1_, p_74875_3_, var4, var4, var4,
						scatteredFeatureSizeX - 1 - var4, var4,
						scatteredFeatureSizeZ - 1 - var4, Blocks.sandstone,
						Blocks.sandstone, false);
				func_151549_a(p_74875_1_, p_74875_3_, var4 + 1, var4, var4 + 1,
						scatteredFeatureSizeX - 2 - var4, var4,
						scatteredFeatureSizeZ - 2 - var4, Blocks.air,
						Blocks.air, false);
			}

			int var5;

			for (var4 = 0; var4 < scatteredFeatureSizeX; ++var4) {
				for (var5 = 0; var5 < scatteredFeatureSizeZ; ++var5) {
					final byte var6 = -5;
					func_151554_b(p_74875_1_, Blocks.sandstone, 0, var4, var6,
							var5, p_74875_3_);
				}
			}

			var4 = func_151555_a(Blocks.sandstone_stairs, 3);
			var5 = func_151555_a(Blocks.sandstone_stairs, 2);
			final int var13 = func_151555_a(Blocks.sandstone_stairs, 0);
			final int var7 = func_151555_a(Blocks.sandstone_stairs, 1);
			final byte var8 = 1;
			final byte var9 = 11;
			func_151549_a(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 9, 4,
					Blocks.sandstone, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 1, 10, 1, 3, 10, 3,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4, 2, 10, 0,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var5, 2, 10, 4,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var13, 0, 10, 2,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var7, 4, 10, 2,
					p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 5, 0,
					0, scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone,
					Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 4,
					10, 1, scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone,
					Blocks.sandstone, false);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4,
					scatteredFeatureSizeX - 3, 10, 0, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var5,
					scatteredFeatureSizeX - 3, 10, 4, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var13,
					scatteredFeatureSizeX - 5, 10, 2, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var7,
					scatteredFeatureSizeX - 1, 10, 2, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 8, 0, 0, 12, 4, 4,
					Blocks.sandstone, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 9, 1, 0, 11, 3, 4,
					Blocks.air, Blocks.air, false);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 9, 1, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 9, 2, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 9, 3, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 10, 3, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 11, 3, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 11, 2, 1, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 11, 1, 1, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 1, 8, 3, 3,
					Blocks.sandstone, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 4, 1, 2, 8, 2, 2, Blocks.air,
					Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 12, 1, 1, 16, 3, 3,
					Blocks.sandstone, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 12, 1, 2, 16, 2, 2,
					Blocks.air, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, 5, 4, 5,
					scatteredFeatureSizeX - 6, 4, scatteredFeatureSizeZ - 6,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, 9, 4, 9, 11, 4, 11,
					Blocks.air, Blocks.air, false);
			func_151556_a(p_74875_1_, p_74875_3_, 8, 1, 8, 8, 3, 8,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151556_a(p_74875_1_, p_74875_3_, 12, 1, 8, 12, 3, 8,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151556_a(p_74875_1_, p_74875_3_, 8, 1, 12, 8, 3, 12,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151556_a(p_74875_1_, p_74875_3_, 12, 1, 12, 12, 3, 12,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151549_a(p_74875_1_, p_74875_3_, 1, 1, 5, 4, 4, 11,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 5, 1,
					5, scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone,
					Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, 6, 7, 9, 6, 7, 11,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 7, 7,
					9, scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone,
					Blocks.sandstone, false);
			func_151556_a(p_74875_1_, p_74875_3_, 5, 5, 9, 5, 7, 11,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151556_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 6, 5,
					9, scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone, 2,
					Blocks.sandstone, 2, false);
			func_151550_a(p_74875_1_, Blocks.air, 0, 5, 5, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 5, 6, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 6, 6, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, scatteredFeatureSizeX - 6,
					5, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, scatteredFeatureSizeX - 6,
					6, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, scatteredFeatureSizeX - 7,
					6, 10, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 2, 4, 4, 2, 6, 4, Blocks.air,
					Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 3, 4,
					4, scatteredFeatureSizeX - 3, 6, 4, Blocks.air, Blocks.air,
					false);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4, 2, 4, 5,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4, 2, 3, 4,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4,
					scatteredFeatureSizeX - 3, 4, 5, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var4,
					scatteredFeatureSizeX - 3, 3, 4, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 1, 1, 3, 2, 2, 3,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 3, 1,
					3, scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone,
					Blocks.sandstone, false);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, 0, 1, 1, 2,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, 0,
					scatteredFeatureSizeX - 2, 1, 2, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.stone_slab, 1, 1, 2, 2, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.stone_slab, 1,
					scatteredFeatureSizeX - 2, 2, 2, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var7, 2, 1, 2,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone_stairs, var13,
					scatteredFeatureSizeX - 3, 1, 2, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 4, 3, 5, 4, 3, 18,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 5, 3,
					5, scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone,
					Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, 3, 1, 5, 4, 2, 16,
					Blocks.air, Blocks.air, false);
			func_151549_a(p_74875_1_, p_74875_3_, scatteredFeatureSizeX - 6, 1,
					5, scatteredFeatureSizeX - 5, 2, 16, Blocks.air,
					Blocks.air, false);
			int var10;

			for (var10 = 5; var10 <= 17; var10 += 2) {
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, 4, 1, var10,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1, 4, 2, var10,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2,
						scatteredFeatureSizeX - 5, 1, var10, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1,
						scatteredFeatureSizeX - 5, 2, var10, p_74875_3_);
			}

			func_151550_a(p_74875_1_, Blocks.wool, var8, 10, 0, 7, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 10, 0, 8, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 9, 0, 9, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 11, 0, 9, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 8, 0, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 12, 0, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 7, 0, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 13, 0, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 9, 0, 11, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 11, 0, 11, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 10, 0, 12, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 10, 0, 13, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var9, 10, 0, 10, p_74875_3_);

			for (var10 = 0; var10 <= scatteredFeatureSizeX - 1; var10 += scatteredFeatureSizeX - 1) {
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 2, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 2, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 2, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 3, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 3, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 3, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 4, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1, var10, 4, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 4, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 5, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 5, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 5, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 6, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1, var10, 6, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 6, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 7, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 7, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 7, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 8, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 8, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 8, 3,
						p_74875_3_);
			}

			for (var10 = 2; var10 <= scatteredFeatureSizeX - 3; var10 += scatteredFeatureSizeX - 3 - 2) {
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 - 1, 2, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 2, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 + 1, 2, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 - 1, 3, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 3, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 + 1, 3, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 - 1, 4, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1, var10, 4, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 + 1, 4, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 - 1, 5, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 5, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 + 1, 5, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 - 1, 6, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 1, var10, 6, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 + 1, 6, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 - 1, 7, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10, 7, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.wool, var8, var10 + 1, 7, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 - 1, 8, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10, 8, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sandstone, 2, var10 + 1, 8, 0,
						p_74875_3_);
			}

			func_151556_a(p_74875_1_, p_74875_3_, 8, 4, 0, 12, 6, 0,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151550_a(p_74875_1_, Blocks.air, 0, 8, 6, 0, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 12, 6, 0, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 9, 5, 0, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 1, 10, 5, 0, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.wool, var8, 11, 5, 0, p_74875_3_);
			func_151556_a(p_74875_1_, p_74875_3_, 8, -14, 8, 12, -11, 12,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151556_a(p_74875_1_, p_74875_3_, 8, -10, 8, 12, -10, 12,
					Blocks.sandstone, 1, Blocks.sandstone, 1, false);
			func_151556_a(p_74875_1_, p_74875_3_, 8, -9, 8, 12, -9, 12,
					Blocks.sandstone, 2, Blocks.sandstone, 2, false);
			func_151549_a(p_74875_1_, p_74875_3_, 8, -8, 8, 12, -1, 12,
					Blocks.sandstone, Blocks.sandstone, false);
			func_151549_a(p_74875_1_, p_74875_3_, 9, -11, 9, 11, -1, 11,
					Blocks.air, Blocks.air, false);
			func_151550_a(p_74875_1_, Blocks.stone_pressure_plate, 0, 10, -11,
					10, p_74875_3_);
			func_151549_a(p_74875_1_, p_74875_3_, 9, -13, 9, 11, -13, 11,
					Blocks.tnt, Blocks.air, false);
			func_151550_a(p_74875_1_, Blocks.air, 0, 8, -11, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 8, -10, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 1, 7, -10, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 7, -11, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 12, -11, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 12, -10, 10, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 1, 13, -10, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 13, -11, 10,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 10, -11, 8, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 10, -10, 8, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 1, 10, -10, 7,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 10, -11, 7,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 10, -11, 12, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.air, 0, 10, -10, 12, p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 1, 10, -10, 13,
					p_74875_3_);
			func_151550_a(p_74875_1_, Blocks.sandstone, 2, 10, -11, 13,
					p_74875_3_);

			for (var10 = 0; var10 < 4; ++var10) {
				if (!field_74940_h[var10]) {
					final int var11 = Direction.offsetX[var10] * 2;
					final int var12 = Direction.offsetZ[var10] * 2;
					field_74940_h[var10] = generateStructureChestContents(
							p_74875_1_,
							p_74875_3_,
							p_74875_2_,
							10 + var11,
							-11,
							10 + var12,
							WeightedRandomChestContent
									.func_92080_a(
											itemsToGenerateInTemple,
											new WeightedRandomChestContent[] { Items.enchanted_book
													.func_92114_b(p_74875_2_) }),
							2 + p_74875_2_.nextInt(5));
				}
			}

			return true;
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			field_74940_h[0] = p_143011_1_.getBoolean("hasPlacedChest0");
			field_74940_h[1] = p_143011_1_.getBoolean("hasPlacedChest1");
			field_74940_h[2] = p_143011_1_.getBoolean("hasPlacedChest2");
			field_74940_h[3] = p_143011_1_.getBoolean("hasPlacedChest3");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("hasPlacedChest0", field_74940_h[0]);
			p_143012_1_.setBoolean("hasPlacedChest1", field_74940_h[1]);
			p_143012_1_.setBoolean("hasPlacedChest2", field_74940_h[2]);
			p_143012_1_.setBoolean("hasPlacedChest3", field_74940_h[3]);
		}
	}

	abstract static class Feature extends StructureComponent {
		protected int field_74936_d = -1;
		protected int scatteredFeatureSizeX;
		protected int scatteredFeatureSizeY;
		protected int scatteredFeatureSizeZ;

		public Feature() {
		}

		protected Feature(Random p_i2065_1_, int p_i2065_2_, int p_i2065_3_,
				int p_i2065_4_, int p_i2065_5_, int p_i2065_6_, int p_i2065_7_) {
			super(0);
			scatteredFeatureSizeX = p_i2065_5_;
			scatteredFeatureSizeY = p_i2065_6_;
			scatteredFeatureSizeZ = p_i2065_7_;
			coordBaseMode = p_i2065_1_.nextInt(4);

			switch (coordBaseMode) {
			case 0:
			case 2:
				boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_,
						p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_
								+ p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
				break;

			default:
				boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_,
						p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_
								+ p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			scatteredFeatureSizeX = p_143011_1_.getInteger("Width");
			scatteredFeatureSizeY = p_143011_1_.getInteger("Height");
			scatteredFeatureSizeZ = p_143011_1_.getInteger("Depth");
			field_74936_d = p_143011_1_.getInteger("HPos");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setInteger("Width", scatteredFeatureSizeX);
			p_143012_1_.setInteger("Height", scatteredFeatureSizeY);
			p_143012_1_.setInteger("Depth", scatteredFeatureSizeZ);
			p_143012_1_.setInteger("HPos", field_74936_d);
		}

		protected boolean func_74935_a(World p_74935_1_,
				StructureBoundingBox p_74935_2_, int p_74935_3_) {
			if (field_74936_d >= 0)
				return true;
			else {
				int var4 = 0;
				int var5 = 0;

				for (int var6 = boundingBox.minZ; var6 <= boundingBox.maxZ; ++var6) {
					for (int var7 = boundingBox.minX; var7 <= boundingBox.maxX; ++var7) {
						if (p_74935_2_.isVecInside(var7, 64, var6)) {
							var4 += Math
									.max(p_74935_1_.getTopSolidOrLiquidBlock(
											var7, var6), p_74935_1_.provider
											.getAverageGroundLevel());
							++var5;
						}
					}
				}

				if (var5 == 0)
					return false;
				else {
					field_74936_d = var4 / var5;
					boundingBox.offset(0, field_74936_d - boundingBox.minY
							+ p_74935_3_, 0);
					return true;
				}
			}
		}
	}

	public static class JunglePyramid extends
			ComponentScatteredFeaturePieces.Feature {
		static class Stones extends StructureComponent.BlockSelector {

			private Stones() {
			}

			Stones(Object p_i2063_1_) {
				this();
			}

			@Override
			public void selectBlocks(Random p_75062_1_, int p_75062_2_,
					int p_75062_3_, int p_75062_4_, boolean p_75062_5_) {
				if (p_75062_1_.nextFloat() < 0.4F) {
					field_151562_a = Blocks.cobblestone;
				} else {
					field_151562_a = Blocks.mossy_cobblestone;
				}
			}
		}

		private static final WeightedRandomChestContent[] junglePyramidsChestContents = new WeightedRandomChestContent[] {
				new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
				new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
				new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
				new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
				new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
				new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
				new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
				new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1,
						1),
				new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1,
						1, 1),
				new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1,
						1, 1) };
		private static final WeightedRandomChestContent[] junglePyramidsDispenserContents = new WeightedRandomChestContent[] { new WeightedRandomChestContent(
				Items.arrow, 0, 2, 7, 30) };
		private static ComponentScatteredFeaturePieces.JunglePyramid.Stones junglePyramidsRandomScatteredStones = new ComponentScatteredFeaturePieces.JunglePyramid.Stones(
				null);
		private boolean field_74945_j;
		private boolean field_74946_k;
		private boolean field_74947_h;

		private boolean field_74948_i;

		public JunglePyramid() {
		}

		public JunglePyramid(Random p_i2064_1_, int p_i2064_2_, int p_i2064_3_) {
			super(p_i2064_1_, p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (!func_74935_a(p_74875_1_, p_74875_3_, 0))
				return false;
			else {
				final int var4 = func_151555_a(Blocks.stone_stairs, 3);
				final int var5 = func_151555_a(Blocks.stone_stairs, 2);
				final int var6 = func_151555_a(Blocks.stone_stairs, 0);
				final int var7 = func_151555_a(Blocks.stone_stairs, 1);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, -4, 0,
						scatteredFeatureSizeX - 1, 0,
						scatteredFeatureSizeZ - 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 2, 9, 2,
						2, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 12, 9,
						2, 12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 3, 2, 2,
						11, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 1, 3, 9, 2,
						11, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 1, 10,
						6, 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 13, 10,
						6, 13, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 2, 1, 6,
						12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 10, 3, 2, 10,
						6, 12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 3, 2, 9, 3,
						12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 6, 2, 9, 6,
						12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 3, 7, 3, 8, 7,
						11, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 8, 4, 7, 8,
						10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithAir(p_74875_1_, p_74875_3_, 3, 1, 3, 8, 2, 11);
				fillWithAir(p_74875_1_, p_74875_3_, 4, 3, 6, 7, 3, 9);
				fillWithAir(p_74875_1_, p_74875_3_, 2, 4, 2, 9, 5, 12);
				fillWithAir(p_74875_1_, p_74875_3_, 4, 6, 5, 7, 6, 9);
				fillWithAir(p_74875_1_, p_74875_3_, 5, 7, 6, 6, 7, 8);
				fillWithAir(p_74875_1_, p_74875_3_, 5, 1, 2, 6, 2, 2);
				fillWithAir(p_74875_1_, p_74875_3_, 5, 2, 12, 6, 2, 12);
				fillWithAir(p_74875_1_, p_74875_3_, 5, 5, 1, 6, 5, 1);
				fillWithAir(p_74875_1_, p_74875_3_, 5, 5, 13, 6, 5, 13);
				func_151550_a(p_74875_1_, Blocks.air, 0, 1, 5, 5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 10, 5, 5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 1, 5, 9, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 10, 5, 9, p_74875_3_);
				int var8;

				for (var8 = 0; var8 <= 14; var8 += 14) {
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 4,
							var8, 2, 5, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 4,
							var8, 4, 5, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 4,
							var8, 7, 5, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 4,
							var8, 9, 5, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
				}

				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 6, 0, 6, 6,
						0, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);

				for (var8 = 0; var8 <= 11; var8 += 11) {
					for (int var9 = 2; var9 <= 12; var9 += 2) {
						fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, var8,
								4, var9, var8, 5, var9, false, p_74875_2_,
								junglePyramidsRandomScatteredStones);
					}

					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, var8, 6,
							5, var8, 6, 5, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, var8, 6,
							9, var8, 6, 9, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
				}

				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 7, 2, 2, 9,
						2, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 7, 2, 9, 9,
						2, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 7, 12, 2,
						9, 12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 7, 12, 9,
						9, 12, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 9, 4, 4, 9,
						4, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 9, 4, 7, 9,
						4, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 9, 10, 4,
						9, 10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 9, 10, 7,
						9, 10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 9, 7, 6, 9,
						7, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 5, 9, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 6, 9, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var5, 5, 9, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var5, 6, 9, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 4, 0, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 5, 0, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 6, 0, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 7, 0, 0,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 4, 1, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 4, 2, 9,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 4, 3, 10,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 7, 1, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 7, 2, 9,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var4, 7, 3, 10,
						p_74875_3_);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 9, 4, 1,
						9, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 1, 9, 7, 1,
						9, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 10, 7,
						2, 10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 4, 5, 6, 4,
						5, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var6, 4, 4, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stone_stairs, var7, 7, 4, 5,
						p_74875_3_);

				for (var8 = 0; var8 < 4; ++var8) {
					func_151550_a(p_74875_1_, Blocks.stone_stairs, var5, 5,
							0 - var8, 6 + var8, p_74875_3_);
					func_151550_a(p_74875_1_, Blocks.stone_stairs, var5, 6,
							0 - var8, 6 + var8, p_74875_3_);
					fillWithAir(p_74875_1_, p_74875_3_, 5, 0 - var8, 7 + var8,
							6, 0 - var8, 9 + var8);
				}

				fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 12, 10, -1, 13);
				fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 1, 3, -1, 13);
				fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 1, 9, -1, 5);

				for (var8 = 1; var8 <= 13; var8 += 2) {
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, -3,
							var8, 1, -2, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
				}

				for (var8 = 2; var8 <= 12; var8 += 2) {
					fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, -1,
							var8, 3, -1, var8, false, p_74875_2_,
							junglePyramidsRandomScatteredStones);
				}

				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, -2, 1, 5,
						-2, 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, -2, 1, 9,
						-2, 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 6, -3, 1, 6,
						-3, 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 6, -1, 1, 6,
						-1, 1, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				func_151550_a(p_74875_1_, Blocks.tripwire_hook,
						func_151555_a(Blocks.tripwire_hook, 3) | 4, 1, -3, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire_hook,
						func_151555_a(Blocks.tripwire_hook, 1) | 4, 4, -3, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire, 4, 2, -3, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire, 4, 3, -3, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 7,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 4,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 5, -3, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 4, -3, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 3, -3,
						1, p_74875_3_);

				if (!field_74945_j) {
					field_74945_j = generateStructureDispenserContents(
							p_74875_1_, p_74875_3_, p_74875_2_, 3, -2, 1, 2,
							junglePyramidsDispenserContents, 2);
				}

				func_151550_a(p_74875_1_, Blocks.vine, 15, 3, -2, 2, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire_hook,
						func_151555_a(Blocks.tripwire_hook, 2) | 4, 7, -3, 1,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire_hook,
						func_151555_a(Blocks.tripwire_hook, 0) | 4, 7, -3, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire, 4, 7, -3, 2,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire, 4, 7, -3, 3,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.tripwire, 4, 7, -3, 4,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 8, -3, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 9, -3, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 9, -3, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 9, -3,
						4, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 9, -2, 4,
						p_74875_3_);

				if (!field_74946_k) {
					field_74946_k = generateStructureDispenserContents(
							p_74875_1_, p_74875_3_, p_74875_2_, 9, -2, 3, 4,
							junglePyramidsDispenserContents, 2);
				}

				func_151550_a(p_74875_1_, Blocks.vine, 15, 8, -1, 3, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.vine, 15, 8, -2, 3, p_74875_3_);

				if (!field_74947_h) {
					field_74947_h = generateStructureChestContents(
							p_74875_1_,
							p_74875_3_,
							p_74875_2_,
							8,
							-3,
							3,
							WeightedRandomChestContent
									.func_92080_a(
											junglePyramidsChestContents,
											new WeightedRandomChestContent[] { Items.enchanted_book
													.func_92114_b(p_74875_2_) }),
							2 + p_74875_2_.nextInt(5));
				}

				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 9, -3,
						2, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 8, -3,
						1, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 4, -3,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 5, -2,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 5, -1,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 6, -3,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 7, -2,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 7, -1,
						5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 8, -3,
						5, p_74875_3_);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, -1, 1, 9,
						-1, 5, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithAir(p_74875_1_, p_74875_3_, 8, -3, 8, 10, -1, 10);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 3, 8, -2, 11,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 3, 9, -2, 11,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.stonebrick, 3, 10, -2, 11,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.lever,
						BlockLever
								.func_149819_b(func_151555_a(Blocks.lever, 2)),
						8, -2, 12, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.lever,
						BlockLever
								.func_149819_b(func_151555_a(Blocks.lever, 2)),
						9, -2, 12, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.lever,
						BlockLever
								.func_149819_b(func_151555_a(Blocks.lever, 2)),
						10, -2, 12, p_74875_3_);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 8, -3, 8, 8,
						-3, 10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 10, -3, 8, 10,
						-3, 10, false, p_74875_2_,
						junglePyramidsRandomScatteredStones);
				func_151550_a(p_74875_1_, Blocks.mossy_cobblestone, 0, 10, -2,
						9, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 8, -2, 9,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 8, -2, 10,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.redstone_wire, 0, 10, -1, 9,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sticky_piston, 1, 9, -2, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sticky_piston,
						func_151555_a(Blocks.sticky_piston, 4), 10, -2, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.sticky_piston,
						func_151555_a(Blocks.sticky_piston, 4), 10, -1, 8,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.unpowered_repeater,
						func_151555_a(Blocks.unpowered_repeater, 2), 10, -2,
						10, p_74875_3_);

				if (!field_74948_i) {
					field_74948_i = generateStructureChestContents(
							p_74875_1_,
							p_74875_3_,
							p_74875_2_,
							9,
							-3,
							10,
							WeightedRandomChestContent
									.func_92080_a(
											junglePyramidsChestContents,
											new WeightedRandomChestContent[] { Items.enchanted_book
													.func_92114_b(p_74875_2_) }),
							2 + p_74875_2_.nextInt(5));
				}

				return true;
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			field_74947_h = p_143011_1_.getBoolean("placedMainChest");
			field_74948_i = p_143011_1_.getBoolean("placedHiddenChest");
			field_74945_j = p_143011_1_.getBoolean("placedTrap1");
			field_74946_k = p_143011_1_.getBoolean("placedTrap2");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("placedMainChest", field_74947_h);
			p_143012_1_.setBoolean("placedHiddenChest", field_74948_i);
			p_143012_1_.setBoolean("placedTrap1", field_74945_j);
			p_143012_1_.setBoolean("placedTrap2", field_74946_k);
		}
	}

	public static class SwampHut extends
			ComponentScatteredFeaturePieces.Feature {
		private boolean hasWitch;

		public SwampHut() {
		}

		public SwampHut(Random p_i2066_1_, int p_i2066_2_, int p_i2066_3_) {
			super(p_i2066_1_, p_i2066_2_, 64, p_i2066_3_, 7, 5, 9);
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_,
				StructureBoundingBox p_74875_3_) {
			if (!func_74935_a(p_74875_1_, p_74875_3_, 0))
				return false;
			else {
				func_151556_a(p_74875_1_, p_74875_3_, 1, 1, 1, 5, 1, 7,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 1, 4, 2, 5, 4, 7,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 2, 1, 0, 4, 1, 0,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 2, 2, 2, 3, 3, 2,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 1, 2, 3, 1, 3, 6,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 5, 2, 3, 5, 3, 6,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151556_a(p_74875_1_, p_74875_3_, 2, 2, 7, 4, 3, 7,
						Blocks.planks, 1, Blocks.planks, 1, false);
				func_151549_a(p_74875_1_, p_74875_3_, 1, 0, 2, 1, 3, 2,
						Blocks.log, Blocks.log, false);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 0, 2, 5, 3, 2,
						Blocks.log, Blocks.log, false);
				func_151549_a(p_74875_1_, p_74875_3_, 1, 0, 7, 1, 3, 7,
						Blocks.log, Blocks.log, false);
				func_151549_a(p_74875_1_, p_74875_3_, 5, 0, 7, 5, 3, 7,
						Blocks.log, Blocks.log, false);
				func_151550_a(p_74875_1_, Blocks.fence, 0, 2, 3, 2, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.fence, 0, 3, 3, 7, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 1, 3, 4, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 5, 3, 4, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.air, 0, 5, 3, 5, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.flower_pot, 7, 1, 3, 5,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.crafting_table, 0, 3, 2, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.cauldron, 0, 4, 2, 6,
						p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.fence, 0, 1, 2, 1, p_74875_3_);
				func_151550_a(p_74875_1_, Blocks.fence, 0, 5, 2, 1, p_74875_3_);
				final int var4 = func_151555_a(Blocks.oak_stairs, 3);
				final int var5 = func_151555_a(Blocks.oak_stairs, 1);
				final int var6 = func_151555_a(Blocks.oak_stairs, 0);
				final int var7 = func_151555_a(Blocks.oak_stairs, 2);
				func_151556_a(p_74875_1_, p_74875_3_, 0, 4, 1, 6, 4, 1,
						Blocks.spruce_stairs, var4, Blocks.spruce_stairs, var4,
						false);
				func_151556_a(p_74875_1_, p_74875_3_, 0, 4, 2, 0, 4, 7,
						Blocks.spruce_stairs, var6, Blocks.spruce_stairs, var6,
						false);
				func_151556_a(p_74875_1_, p_74875_3_, 6, 4, 2, 6, 4, 7,
						Blocks.spruce_stairs, var5, Blocks.spruce_stairs, var5,
						false);
				func_151556_a(p_74875_1_, p_74875_3_, 0, 4, 8, 6, 4, 8,
						Blocks.spruce_stairs, var7, Blocks.spruce_stairs, var7,
						false);
				int var8;
				int var9;

				for (var8 = 2; var8 <= 7; var8 += 5) {
					for (var9 = 1; var9 <= 5; var9 += 4) {
						func_151554_b(p_74875_1_, Blocks.log, 0, var9, -1,
								var8, p_74875_3_);
					}
				}

				if (!hasWitch) {
					var8 = getXWithOffset(2, 5);
					var9 = getYWithOffset(2);
					final int var10 = getZWithOffset(2, 5);

					if (p_74875_3_.isVecInside(var8, var9, var10)) {
						hasWitch = true;
						final EntityWitch var11 = new EntityWitch(p_74875_1_);
						var11.setLocationAndAngles(var8 + 0.5D, var9,
								var10 + 0.5D, 0.0F, 0.0F);
						var11.onSpawnWithEgg((IEntityLivingData) null);
						p_74875_1_.spawnEntityInWorld(var11);
					}
				}

				return true;
			}
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			super.func_143011_b(p_143011_1_);
			hasWitch = p_143011_1_.getBoolean("Witch");
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			super.func_143012_a(p_143012_1_);
			p_143012_1_.setBoolean("Witch", hasWitch);
		}
	}

	public static void func_143045_a() {
		MapGenStructureIO.func_143031_a(
				ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
		MapGenStructureIO.func_143031_a(
				ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
		MapGenStructureIO.func_143031_a(
				ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
	}
}
