package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenVillage extends MapGenStructure {
	public static class Start extends StructureStart {
		private boolean hasMoreThanTwoComponents;

		public Start() {
		}

		public Start(World p_i2092_1_, Random p_i2092_2_, int p_i2092_3_,
				int p_i2092_4_, int p_i2092_5_) {
			super(p_i2092_3_, p_i2092_4_);
			final List var6 = StructureVillagePieces
					.getStructureVillageWeightedPieceList(p_i2092_2_,
							p_i2092_5_);
			final StructureVillagePieces.Start var7 = new StructureVillagePieces.Start(
					p_i2092_1_.getWorldChunkManager(), 0, p_i2092_2_,
					(p_i2092_3_ << 4) + 2, (p_i2092_4_ << 4) + 2, var6,
					p_i2092_5_);
			components.add(var7);
			var7.buildComponent(var7, components, p_i2092_2_);
			final List var8 = var7.field_74930_j;
			final List var9 = var7.field_74932_i;
			int var10;

			while (!var8.isEmpty() || !var9.isEmpty()) {
				StructureComponent var11;

				if (var8.isEmpty()) {
					var10 = p_i2092_2_.nextInt(var9.size());
					var11 = (StructureComponent) var9.remove(var10);
					var11.buildComponent(var7, components, p_i2092_2_);
				} else {
					var10 = p_i2092_2_.nextInt(var8.size());
					var11 = (StructureComponent) var8.remove(var10);
					var11.buildComponent(var7, components, p_i2092_2_);
				}
			}

			updateBoundingBox();
			var10 = 0;
			final Iterator var13 = components.iterator();

			while (var13.hasNext()) {
				final StructureComponent var12 = (StructureComponent) var13
						.next();

				if (!(var12 instanceof StructureVillagePieces.Road)) {
					++var10;
				}
			}

			hasMoreThanTwoComponents = var10 > 2;
		}

		@Override
		public void func_143017_b(NBTTagCompound p_143017_1_) {
			super.func_143017_b(p_143017_1_);
			hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
		}

		@Override
		public void func_143022_a(NBTTagCompound p_143022_1_) {
			super.func_143022_a(p_143022_1_);
			p_143022_1_.setBoolean("Valid", hasMoreThanTwoComponents);
		}

		@Override
		public boolean isSizeableStructure() {
			return hasMoreThanTwoComponents;
		}
	}

	/** A list of all the biomes villages can spawn in. */
	public static final List villageSpawnBiomes = Arrays
			.asList(new BiomeGenBase[] { BiomeGenBase.plains,
					BiomeGenBase.desert, BiomeGenBase.field_150588_X });
	private int field_82665_g;
	private final int field_82666_h;

	/** World terrain type, 0 for normal, 1 for flat map */
	private int terrainType;

	public MapGenVillage() {
		field_82665_g = 32;
		field_82666_h = 8;
	}

	public MapGenVillage(Map p_i2093_1_) {
		this();
		final Iterator var2 = p_i2093_1_.entrySet().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();

			if (((String) var3.getKey()).equals("size")) {
				terrainType = MathHelper.parseIntWithDefaultAndMax(
						(String) var3.getValue(), terrainType, 0);
			} else if (((String) var3.getKey()).equals("distance")) {
				field_82665_g = MathHelper.parseIntWithDefaultAndMax(
						(String) var3.getValue(), field_82665_g,
						field_82666_h + 1);
			}
		}
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		final int var3 = p_75047_1_;
		final int var4 = p_75047_2_;

		if (p_75047_1_ < 0) {
			p_75047_1_ -= field_82665_g - 1;
		}

		if (p_75047_2_ < 0) {
			p_75047_2_ -= field_82665_g - 1;
		}

		int var5 = p_75047_1_ / field_82665_g;
		int var6 = p_75047_2_ / field_82665_g;
		final Random var7 = worldObj.setRandomSeed(var5, var6, 10387312);
		var5 *= field_82665_g;
		var6 *= field_82665_g;
		var5 += var7.nextInt(field_82665_g - field_82666_h);
		var6 += var7.nextInt(field_82665_g - field_82666_h);

		if (var3 == var5 && var4 == var6) {
			final boolean var8 = worldObj.getWorldChunkManager()
					.areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 0,
							villageSpawnBiomes);

			if (var8)
				return true;
		}

		return false;
	}

	@Override
	public String func_143025_a() {
		return "Village";
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenVillage.Start(worldObj, rand, p_75049_1_, p_75049_2_,
				terrainType);
	}
}
