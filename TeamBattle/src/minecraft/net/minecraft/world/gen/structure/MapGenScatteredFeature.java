package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenScatteredFeature extends MapGenStructure {
	public static class Start extends StructureStart {

		public Start() {
		}

		public Start(World p_i2060_1_, Random p_i2060_2_, int p_i2060_3_,
				int p_i2060_4_) {
			super(p_i2060_3_, p_i2060_4_);
			final BiomeGenBase var5 = p_i2060_1_.getBiomeGenForCoords(
					p_i2060_3_ * 16 + 8, p_i2060_4_ * 16 + 8);

			if (var5 != BiomeGenBase.jungle && var5 != BiomeGenBase.jungleHills) {
				if (var5 == BiomeGenBase.swampland) {
					final ComponentScatteredFeaturePieces.SwampHut var7 = new ComponentScatteredFeaturePieces.SwampHut(
							p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
					components.add(var7);
				} else {
					final ComponentScatteredFeaturePieces.DesertPyramid var8 = new ComponentScatteredFeaturePieces.DesertPyramid(
							p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
					components.add(var8);
				}
			} else {
				final ComponentScatteredFeaturePieces.JunglePyramid var6 = new ComponentScatteredFeaturePieces.JunglePyramid(
						p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
				components.add(var6);
			}

			updateBoundingBox();
		}
	}

	private static List biomelist = Arrays.asList(new BiomeGenBase[] {
			BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle,
			BiomeGenBase.jungleHills, BiomeGenBase.swampland });

	/** the maximum distance between scattered features */
	private int maxDistanceBetweenScatteredFeatures;

	/** the minimum distance between scattered features */
	private final int minDistanceBetweenScatteredFeatures;

	/** contains possible spawns for scattered features */
	private final List scatteredFeatureSpawnList;

	public MapGenScatteredFeature() {
		scatteredFeatureSpawnList = new ArrayList();
		maxDistanceBetweenScatteredFeatures = 32;
		minDistanceBetweenScatteredFeatures = 8;
		scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(
				EntityWitch.class, 1, 1, 1));
	}

	public MapGenScatteredFeature(Map p_i2061_1_) {
		this();
		final Iterator var2 = p_i2061_1_.entrySet().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();

			if (((String) var3.getKey()).equals("distance")) {
				maxDistanceBetweenScatteredFeatures = MathHelper
						.parseIntWithDefaultAndMax((String) var3.getValue(),
								maxDistanceBetweenScatteredFeatures,
								minDistanceBetweenScatteredFeatures + 1);
			}
		}
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		final int var3 = p_75047_1_;
		final int var4 = p_75047_2_;

		if (p_75047_1_ < 0) {
			p_75047_1_ -= maxDistanceBetweenScatteredFeatures - 1;
		}

		if (p_75047_2_ < 0) {
			p_75047_2_ -= maxDistanceBetweenScatteredFeatures - 1;
		}

		int var5 = p_75047_1_ / maxDistanceBetweenScatteredFeatures;
		int var6 = p_75047_2_ / maxDistanceBetweenScatteredFeatures;
		final Random var7 = worldObj.setRandomSeed(var5, var6, 14357617);
		var5 *= maxDistanceBetweenScatteredFeatures;
		var6 *= maxDistanceBetweenScatteredFeatures;
		var5 += var7.nextInt(maxDistanceBetweenScatteredFeatures
				- minDistanceBetweenScatteredFeatures);
		var6 += var7.nextInt(maxDistanceBetweenScatteredFeatures
				- minDistanceBetweenScatteredFeatures);

		if (var3 == var5 && var4 == var6) {
			final BiomeGenBase var8 = worldObj.getWorldChunkManager()
					.getBiomeGenAt(var3 * 16 + 8, var4 * 16 + 8);
			final Iterator var9 = biomelist.iterator();

			while (var9.hasNext()) {
				final BiomeGenBase var10 = (BiomeGenBase) var9.next();

				if (var8 == var10)
					return true;
			}
		}

		return false;
	}

	@Override
	public String func_143025_a() {
		return "Temple";
	}

	public boolean func_143030_a(int p_143030_1_, int p_143030_2_,
			int p_143030_3_) {
		final StructureStart var4 = func_143028_c(p_143030_1_, p_143030_2_,
				p_143030_3_);

		if (var4 != null && var4 instanceof MapGenScatteredFeature.Start
				&& !var4.components.isEmpty()) {
			final StructureComponent var5 = (StructureComponent) var4.components
					.getFirst();
			return var5 instanceof ComponentScatteredFeaturePieces.SwampHut;
		} else
			return false;
	}

	/**
	 * returns possible spawns for scattered features
	 */
	public List getScatteredFeatureSpawnList() {
		return scatteredFeatureSpawnList;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenScatteredFeature.Start(worldObj, rand, p_75049_1_,
				p_75049_2_);
	}
}
