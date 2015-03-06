package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenJungle extends BiomeGenBase {
	private final boolean field_150614_aC;

	public BiomeGenJungle(int p_i45379_1_, boolean p_i45379_2_) {
		super(p_i45379_1_);
		field_150614_aC = p_i45379_2_;

		if (p_i45379_2_) {
			theBiomeDecorator.treesPerChunk = 2;
		} else {
			theBiomeDecorator.treesPerChunk = 50;
		}

		theBiomeDecorator.grassPerChunk = 25;
		theBiomeDecorator.flowersPerChunk = 4;

		if (!p_i45379_2_) {
			spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
					EntityOcelot.class, 2, 1, 1));
		}

		spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
				EntityChicken.class, 10, 4, 4));
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
		final int var5 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
		int var6 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
		int var7 = p_76728_2_
				.nextInt(p_76728_1_.getHeightValue(var5, var6) * 2);
		new WorldGenMelon().generate(p_76728_1_, p_76728_2_, var5, var7, var6);
		final WorldGenVines var10 = new WorldGenVines();

		for (var6 = 0; var6 < 50; ++var6) {
			var7 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
			final short var8 = 128;
			final int var9 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
			var10.generate(p_76728_1_, p_76728_2_, var7, var8, var9);
		}
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return p_150567_1_.nextInt(10) == 0 ? worldGeneratorBigTree
				: p_150567_1_.nextInt(2) == 0 ? new WorldGenShrub(3, 0)
						: !field_150614_aC && p_150567_1_.nextInt(3) == 0 ? new WorldGenMegaJungle(
								false, 10, 20, 3, 3) : new WorldGenTrees(false,
								4 + p_150567_1_.nextInt(7), 3, 3, true);
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_) {
		return p_76730_1_.nextInt(4) == 0 ? new WorldGenTallGrass(
				Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass,
				1);
	}
}
