package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenNetherBridge extends MapGenStructure {
	public static class Start extends StructureStart {

		public Start() {
		}

		public Start(World p_i2040_1_, Random p_i2040_2_, int p_i2040_3_,
				int p_i2040_4_) {
			super(p_i2040_3_, p_i2040_4_);
			final StructureNetherBridgePieces.Start var5 = new StructureNetherBridgePieces.Start(
					p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
			components.add(var5);
			var5.buildComponent(var5, components, p_i2040_2_);
			final ArrayList var6 = var5.field_74967_d;

			while (!var6.isEmpty()) {
				final int var7 = p_i2040_2_.nextInt(var6.size());
				final StructureComponent var8 = (StructureComponent) var6
						.remove(var7);
				var8.buildComponent(var5, components, p_i2040_2_);
			}

			updateBoundingBox();
			setRandomHeight(p_i2040_1_, p_i2040_2_, 48, 70);
		}
	}

	private final List spawnList = new ArrayList();

	public MapGenNetherBridge() {
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2,
				3));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5,
				4, 4));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10,
				4, 4));
		spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3,
				4, 4));
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		final int var3 = p_75047_1_ >> 4;
		final int var4 = p_75047_2_ >> 4;
		rand.setSeed(var3 ^ var4 << 4 ^ worldObj.getSeed());
		rand.nextInt();
		return rand.nextInt(3) != 0 ? false : p_75047_1_ != (var3 << 4) + 4
				+ rand.nextInt(8) ? false : p_75047_2_ == (var4 << 4) + 4
				+ rand.nextInt(8);
	}

	@Override
	public String func_143025_a() {
		return "Fortress";
	}

	public List getSpawnList() {
		return spawnList;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenNetherBridge.Start(worldObj, rand, p_75049_1_,
				p_75049_2_);
	}
}
