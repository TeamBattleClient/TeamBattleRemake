package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
	/** Field that holds big mushroom generator */
	protected WorldGenerator bigMushroomGen;

	/** Amount of big mushrooms per chunk */
	protected int bigMushroomsPerChunk;

	/**
	 * The number of cactus plants to generate per chunk. Cacti only work on
	 * sand.
	 */
	protected int cactiPerChunk;

	/** Field that holds WorldGenCactus */
	protected WorldGenerator cactusGen;

	/** The X-coordinate of the chunk currently being decorated */
	protected int chunk_X;

	/** The Z-coordinate of the chunk currently being decorated */
	protected int chunk_Z;

	/** The clay generator. */
	protected WorldGenerator clayGen = new WorldGenClay(4);

	/**
	 * The number of clay patches to generate per chunk. Only generates when
	 * part of it is underwater.
	 */
	protected int clayPerChunk;
	protected WorldGenerator coalGen;
	/** The world the BiomeDecorator is currently decorating */
	protected World currentWorld;
	/**
	 * The number of dead bushes to generate per chunk. Used in deserts and
	 * swamps.
	 */
	protected int deadBushPerChunk;

	/** Field that holds diamond WorldGenMinable */
	protected WorldGenerator diamondGen;

	/** The dirt generator. */
	protected WorldGenerator dirtGen;

	protected WorldGenFlowers field_150514_p;

	/**
	 * The number of yellow flower patches to generate per chunk. The game
	 * generates much less than this number, since it attempts to generate them
	 * at a random altitude.
	 */
	protected int flowersPerChunk;
	/** True if decorator should generate surface lava & water */
	public boolean generateLakes;

	/** Field that holds gold WorldGenMinable */
	protected WorldGenerator goldGen;

	/** The amount of tall grass to generate per chunk. */
	protected int grassPerChunk;

	/** The gravel generator. */
	protected WorldGenerator gravelAsSandGen;

	protected WorldGenerator gravelGen;

	protected WorldGenerator ironGen;

	/** Field that holds Lapis WorldGenMinable */
	protected WorldGenerator lapisGen;

	/** Field that holds mushroomBrown WorldGenFlowers */
	protected WorldGenerator mushroomBrownGen;

	/** Field that holds mushroomRed WorldGenFlowers */
	protected WorldGenerator mushroomRedGen;

	/**
	 * The number of extra mushroom patches per chunk. It generates 1/4 this
	 * number in brown mushroom patches, and 1/8 this number in red mushroom
	 * patches. These mushrooms go beyond the default base number of mushrooms.
	 */
	protected int mushroomsPerChunk;

	/** The Biome Decorator's random number generator. */
	protected Random randomGenerator;

	/** Field that holds redstone WorldGenMinable */
	protected WorldGenerator redstoneGen;

	/** Field that holds WorldGenReed */
	protected WorldGenerator reedGen;

	/**
	 * The number of reeds to generate per chunk. Reeds won't generate if the
	 * randomly selected placement is unsuitable.
	 */
	protected int reedsPerChunk;

	/** The sand generator. */
	protected WorldGenerator sandGen;

	/**
	 * The number of sand patches to generate per chunk. Sand patches only
	 * generate when part of it is underwater.
	 */
	protected int sandPerChunk;

	/**
	 * The number of sand patches to generate per chunk. Sand patches only
	 * generate when part of it is underwater. There appear to be two separate
	 * fields for this.
	 */
	protected int sandPerChunk2;

	/**
	 * The number of trees to attempt to generate per chunk. Up to 10 in
	 * forests, none in deserts.
	 */
	protected int treesPerChunk;

	/** The water lily generation! */
	protected WorldGenerator waterlilyGen;

	/** Amount of waterlilys per chunk. */
	protected int waterlilyPerChunk;

	public BiomeDecorator() {
		sandGen = new WorldGenSand(Blocks.sand, 7);
		gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
		dirtGen = new WorldGenMinable(Blocks.dirt, 32);
		gravelGen = new WorldGenMinable(Blocks.gravel, 32);
		coalGen = new WorldGenMinable(Blocks.coal_ore, 16);
		ironGen = new WorldGenMinable(Blocks.iron_ore, 8);
		goldGen = new WorldGenMinable(Blocks.gold_ore, 8);
		redstoneGen = new WorldGenMinable(Blocks.redstone_ore, 7);
		diamondGen = new WorldGenMinable(Blocks.diamond_ore, 7);
		lapisGen = new WorldGenMinable(Blocks.lapis_ore, 6);
		field_150514_p = new WorldGenFlowers(Blocks.yellow_flower);
		mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
		mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
		bigMushroomGen = new WorldGenBigMushroom();
		reedGen = new WorldGenReed();
		cactusGen = new WorldGenCactus();
		waterlilyGen = new WorldGenWaterlily();
		flowersPerChunk = 2;
		grassPerChunk = 1;
		sandPerChunk = 1;
		sandPerChunk2 = 3;
		clayPerChunk = 1;
		generateLakes = true;
	}

	public void func_150512_a(World p_150512_1_, Random p_150512_2_,
			BiomeGenBase p_150512_3_, int p_150512_4_, int p_150512_5_) {
		if (currentWorld != null)
			throw new RuntimeException("Already decorating!!");
		else {
			currentWorld = p_150512_1_;
			randomGenerator = p_150512_2_;
			chunk_X = p_150512_4_;
			chunk_Z = p_150512_5_;
			func_150513_a(p_150512_3_);
			currentWorld = null;
			randomGenerator = null;
		}
	}

	protected void func_150513_a(BiomeGenBase p_150513_1_) {
		generateOres();
		int var2;
		int var3;
		int var4;

		for (var2 = 0; var2 < sandPerChunk2; ++var2) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			sandGen.generate(currentWorld, randomGenerator, var3,
					currentWorld.getTopSolidOrLiquidBlock(var3, var4), var4);
		}

		for (var2 = 0; var2 < clayPerChunk; ++var2) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			clayGen.generate(currentWorld, randomGenerator, var3,
					currentWorld.getTopSolidOrLiquidBlock(var3, var4), var4);
		}

		for (var2 = 0; var2 < sandPerChunk; ++var2) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			gravelAsSandGen.generate(currentWorld, randomGenerator, var3,
					currentWorld.getTopSolidOrLiquidBlock(var3, var4), var4);
		}

		var2 = treesPerChunk;

		if (randomGenerator.nextInt(10) == 0) {
			++var2;
		}

		int var5;
		int var6;

		for (var3 = 0; var3 < var2; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = currentWorld.getHeightValue(var4, var5);
			final WorldGenAbstractTree var7 = p_150513_1_
					.func_150567_a(randomGenerator);
			var7.setScale(1.0D, 1.0D, 1.0D);

			if (var7.generate(currentWorld, randomGenerator, var4, var6, var5)) {
				var7.func_150524_b(currentWorld, randomGenerator, var4, var6,
						var5);
			}
		}

		for (var3 = 0; var3 < bigMushroomsPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			bigMushroomGen.generate(currentWorld, randomGenerator, var4,
					currentWorld.getHeightValue(var4, var5), var5);
		}

		for (var3 = 0; var3 < flowersPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) + 32);
			final String var9 = p_150513_1_.func_150572_a(randomGenerator,
					var4, var6, var5);
			final BlockFlower var8 = BlockFlower.func_149857_e(var9);

			if (var8.getMaterial() != Material.air) {
				field_150514_p.func_150550_a(var8,
						BlockFlower.func_149856_f(var9));
				field_150514_p.generate(currentWorld, randomGenerator, var4,
						var6, var5);
			}
		}

		for (var3 = 0; var3 < grassPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) * 2);
			final WorldGenerator var10 = p_150513_1_
					.getRandomWorldGenForGrass(randomGenerator);
			var10.generate(currentWorld, randomGenerator, var4, var6, var5);
		}

		for (var3 = 0; var3 < deadBushPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) * 2);
			new WorldGenDeadBush(Blocks.deadbush).generate(currentWorld,
					randomGenerator, var4, var6, var5);
		}

		for (var3 = 0; var3 < waterlilyPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;

			for (var6 = randomGenerator.nextInt(currentWorld.getHeightValue(
					var4, var5) * 2); var6 > 0
					&& currentWorld.isAirBlock(var4, var6 - 1, var5); --var6) {
				;
			}

			waterlilyGen.generate(currentWorld, randomGenerator, var4, var6,
					var5);
		}

		for (var3 = 0; var3 < mushroomsPerChunk; ++var3) {
			if (randomGenerator.nextInt(4) == 0) {
				var4 = chunk_X + randomGenerator.nextInt(16) + 8;
				var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
				var6 = currentWorld.getHeightValue(var4, var5);
				mushroomBrownGen.generate(currentWorld, randomGenerator, var4,
						var6, var5);
			}

			if (randomGenerator.nextInt(8) == 0) {
				var4 = chunk_X + randomGenerator.nextInt(16) + 8;
				var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
				var6 = randomGenerator.nextInt(currentWorld.getHeightValue(
						var4, var5) * 2);
				mushroomRedGen.generate(currentWorld, randomGenerator, var4,
						var6, var5);
			}
		}

		if (randomGenerator.nextInt(4) == 0) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var5 = randomGenerator.nextInt(currentWorld.getHeightValue(var3,
					var4) * 2);
			mushroomBrownGen.generate(currentWorld, randomGenerator, var3,
					var5, var4);
		}

		if (randomGenerator.nextInt(8) == 0) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var5 = randomGenerator.nextInt(currentWorld.getHeightValue(var3,
					var4) * 2);
			mushroomRedGen.generate(currentWorld, randomGenerator, var3, var5,
					var4);
		}

		for (var3 = 0; var3 < reedsPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) * 2);
			reedGen.generate(currentWorld, randomGenerator, var4, var6, var5);
		}

		for (var3 = 0; var3 < 10; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) * 2);
			reedGen.generate(currentWorld, randomGenerator, var4, var6, var5);
		}

		if (randomGenerator.nextInt(32) == 0) {
			var3 = chunk_X + randomGenerator.nextInt(16) + 8;
			var4 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var5 = randomGenerator.nextInt(currentWorld.getHeightValue(var3,
					var4) * 2);
			new WorldGenPumpkin().generate(currentWorld, randomGenerator, var3,
					var5, var4);
		}

		for (var3 = 0; var3 < cactiPerChunk; ++var3) {
			var4 = chunk_X + randomGenerator.nextInt(16) + 8;
			var5 = chunk_Z + randomGenerator.nextInt(16) + 8;
			var6 = randomGenerator.nextInt(currentWorld.getHeightValue(var4,
					var5) * 2);
			cactusGen.generate(currentWorld, randomGenerator, var4, var6, var5);
		}

		if (generateLakes) {
			for (var3 = 0; var3 < 50; ++var3) {
				var4 = chunk_X + randomGenerator.nextInt(16) + 8;
				var5 = randomGenerator
						.nextInt(randomGenerator.nextInt(248) + 8);
				var6 = chunk_Z + randomGenerator.nextInt(16) + 8;
				new WorldGenLiquids(Blocks.flowing_water).generate(
						currentWorld, randomGenerator, var4, var5, var6);
			}

			for (var3 = 0; var3 < 20; ++var3) {
				var4 = chunk_X + randomGenerator.nextInt(16) + 8;
				var5 = randomGenerator.nextInt(randomGenerator
						.nextInt(randomGenerator.nextInt(240) + 8) + 8);
				var6 = chunk_Z + randomGenerator.nextInt(16) + 8;
				new WorldGenLiquids(Blocks.flowing_lava).generate(currentWorld,
						randomGenerator, var4, var5, var6);
			}
		}
	}

	/**
	 * Generates ores in the current chunk
	 */
	protected void generateOres() {
		genStandardOre1(20, dirtGen, 0, 256);
		genStandardOre1(10, gravelGen, 0, 256);
		genStandardOre1(20, coalGen, 0, 128);
		genStandardOre1(20, ironGen, 0, 64);
		genStandardOre1(2, goldGen, 0, 32);
		genStandardOre1(8, redstoneGen, 0, 16);
		genStandardOre1(1, diamondGen, 0, 16);
		genStandardOre2(1, lapisGen, 16, 16);
	}

	/**
	 * Standard ore generation helper. Generates most ores.
	 */
	protected void genStandardOre1(int p_76795_1_, WorldGenerator p_76795_2_,
			int p_76795_3_, int p_76795_4_) {
		for (int var5 = 0; var5 < p_76795_1_; ++var5) {
			final int var6 = chunk_X + randomGenerator.nextInt(16);
			final int var7 = randomGenerator.nextInt(p_76795_4_ - p_76795_3_)
					+ p_76795_3_;
			final int var8 = chunk_Z + randomGenerator.nextInt(16);
			p_76795_2_
					.generate(currentWorld, randomGenerator, var6, var7, var8);
		}
	}

	/**
	 * Standard ore generation helper. Generates Lapis Lazuli.
	 */
	protected void genStandardOre2(int p_76793_1_, WorldGenerator p_76793_2_,
			int p_76793_3_, int p_76793_4_) {
		for (int var5 = 0; var5 < p_76793_1_; ++var5) {
			final int var6 = chunk_X + randomGenerator.nextInt(16);
			final int var7 = randomGenerator.nextInt(p_76793_4_)
					+ randomGenerator.nextInt(p_76793_4_) + p_76793_3_
					- p_76793_4_;
			final int var8 = chunk_Z + randomGenerator.nextInt(16);
			p_76793_2_
					.generate(currentWorld, randomGenerator, var6, var7, var8);
		}
	}
}
