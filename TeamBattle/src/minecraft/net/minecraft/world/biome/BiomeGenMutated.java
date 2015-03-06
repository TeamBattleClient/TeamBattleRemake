package net.minecraft.world.biome;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenMutated extends BiomeGenBase {
	protected BiomeGenBase field_150611_aD;

	public BiomeGenMutated(int p_i45381_1_, BiomeGenBase p_i45381_2_) {
		super(p_i45381_1_);
		field_150611_aD = p_i45381_2_;
		func_150557_a(p_i45381_2_.color, true);
		biomeName = p_i45381_2_.biomeName + " M";
		topBlock = p_i45381_2_.topBlock;
		fillerBlock = p_i45381_2_.fillerBlock;
		field_76754_C = p_i45381_2_.field_76754_C;
		minHeight = p_i45381_2_.minHeight;
		maxHeight = p_i45381_2_.maxHeight;
		temperature = p_i45381_2_.temperature;
		rainfall = p_i45381_2_.rainfall;
		waterColorMultiplier = p_i45381_2_.waterColorMultiplier;
		enableSnow = p_i45381_2_.enableSnow;
		enableRain = p_i45381_2_.enableRain;
		spawnableCreatureList = new ArrayList(p_i45381_2_.spawnableCreatureList);
		spawnableMonsterList = new ArrayList(p_i45381_2_.spawnableMonsterList);
		spawnableCaveCreatureList = new ArrayList(
				p_i45381_2_.spawnableCaveCreatureList);
		spawnableWaterCreatureList = new ArrayList(
				p_i45381_2_.spawnableWaterCreatureList);
		temperature = p_i45381_2_.temperature;
		rainfall = p_i45381_2_.rainfall;
		minHeight = p_i45381_2_.minHeight + 0.1F;
		maxHeight = p_i45381_2_.maxHeight + 0.2F;
	}

	@Override
	public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_,
			int p_76728_4_) {
		field_150611_aD.theBiomeDecorator.func_150512_a(p_76728_1_, p_76728_2_,
				this, p_76728_3_, p_76728_4_);
	}

	@Override
	public BiomeGenBase.TempCategory func_150561_m() {
		return field_150611_aD.func_150561_m();
	}

	@Override
	public Class func_150562_l() {
		return field_150611_aD.func_150562_l();
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
		return field_150611_aD.func_150567_a(p_150567_1_);
	}

	@Override
	public boolean func_150569_a(BiomeGenBase p_150569_1_) {
		return field_150611_aD.func_150569_a(p_150569_1_);
	}

	@Override
	public void func_150573_a(World p_150573_1_, Random p_150573_2_,
			Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_,
			int p_150573_6_, double p_150573_7_) {
		field_150611_aD.func_150573_a(p_150573_1_, p_150573_2_, p_150573_3_,
				p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
	}

	/**
	 * Provides the basic foliage color based on the biome temperature and
	 * rainfall
	 */
	@Override
	public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_,
			int p_150571_3_) {
		return field_150611_aD.getBiomeFoliageColor(p_150571_1_, p_150571_2_,
				p_150571_2_);
	}

	/**
	 * Provides the basic grass color based on the biome temperature and
	 * rainfall
	 */
	@Override
	public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_,
			int p_150558_3_) {
		return field_150611_aD.getBiomeGrassColor(p_150558_1_, p_150558_2_,
				p_150558_2_);
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	@Override
	public float getSpawningChance() {
		return field_150611_aD.getSpawningChance();
	}
}
