package net.minecraft.world.biome;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;

public class BiomeGenEnd extends BiomeGenBase {

	public BiomeGenEnd(int p_i1990_1_) {
		super(p_i1990_1_);
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(
				EntityEnderman.class, 10, 4, 4));
		topBlock = Blocks.dirt;
		fillerBlock = Blocks.dirt;
		theBiomeDecorator = new BiomeEndDecorator();
	}

	/**
	 * takes temperature, returns color
	 */
	@Override
	public int getSkyColorByTemp(float p_76731_1_) {
		return 0;
	}
}
