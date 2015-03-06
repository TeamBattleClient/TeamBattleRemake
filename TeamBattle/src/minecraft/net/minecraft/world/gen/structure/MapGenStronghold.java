package net.minecraft.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenStronghold extends MapGenStructure {
	public static class Start extends StructureStart {

		public Start() {
		}

		public Start(World p_i2067_1_, Random p_i2067_2_, int p_i2067_3_,
				int p_i2067_4_) {
			super(p_i2067_3_, p_i2067_4_);
			StructureStrongholdPieces.prepareStructurePieces();
			final StructureStrongholdPieces.Stairs2 var5 = new StructureStrongholdPieces.Stairs2(
					0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
			components.add(var5);
			var5.buildComponent(var5, components, p_i2067_2_);
			final List var6 = var5.field_75026_c;

			while (!var6.isEmpty()) {
				final int var7 = p_i2067_2_.nextInt(var6.size());
				final StructureComponent var8 = (StructureComponent) var6
						.remove(var7);
				var8.buildComponent(var5, components, p_i2067_2_);
			}

			updateBoundingBox();
			markAvailableHeight(p_i2067_1_, p_i2067_2_, 10);
		}
	}

	private final List field_151546_e;
	private double field_82671_h;
	private int field_82672_i;
	/**
	 * is spawned false and set true once the defined BiomeGenBases were
	 * compared with the present ones
	 */
	private boolean ranBiomeCheck;

	private ChunkCoordIntPair[] structureCoords;

	public MapGenStronghold() {
		structureCoords = new ChunkCoordIntPair[3];
		field_82671_h = 32.0D;
		field_82672_i = 3;
		field_151546_e = new ArrayList();
		final BiomeGenBase[] var1 = BiomeGenBase.getBiomeGenArray();
		final int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final BiomeGenBase var4 = var1[var3];

			if (var4 != null && var4.minHeight > 0.0F) {
				field_151546_e.add(var4);
			}
		}
	}

	public MapGenStronghold(Map p_i2068_1_) {
		this();
		final Iterator var2 = p_i2068_1_.entrySet().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();

			if (((String) var3.getKey()).equals("distance")) {
				field_82671_h = MathHelper.parseDoubleWithDefaultAndMax(
						(String) var3.getValue(), field_82671_h, 1.0D);
			} else if (((String) var3.getKey()).equals("count")) {
				structureCoords = new ChunkCoordIntPair[MathHelper
						.parseIntWithDefaultAndMax((String) var3.getValue(),
								structureCoords.length, 1)];
			} else if (((String) var3.getKey()).equals("spread")) {
				field_82672_i = MathHelper.parseIntWithDefaultAndMax(
						(String) var3.getValue(), field_82672_i, 1);
			}
		}
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		if (!ranBiomeCheck) {
			final Random var3 = new Random();
			var3.setSeed(worldObj.getSeed());
			double var4 = var3.nextDouble() * Math.PI * 2.0D;
			int var6 = 1;

			for (int var7 = 0; var7 < structureCoords.length; ++var7) {
				final double var8 = (1.25D * var6 + var3.nextDouble())
						* field_82671_h * var6;
				int var10 = (int) Math.round(Math.cos(var4) * var8);
				int var11 = (int) Math.round(Math.sin(var4) * var8);
				final ChunkPosition var12 = worldObj.getWorldChunkManager()
						.func_150795_a((var10 << 4) + 8, (var11 << 4) + 8, 112,
								field_151546_e, var3);

				if (var12 != null) {
					var10 = var12.field_151329_a >> 4;
					var11 = var12.field_151328_c >> 4;
				}

				structureCoords[var7] = new ChunkCoordIntPair(var10, var11);
				var4 += Math.PI * 2D * var6 / field_82672_i;

				if (var7 == field_82672_i) {
					var6 += 2 + var3.nextInt(5);
					field_82672_i += 1 + var3.nextInt(2);
				}
			}

			ranBiomeCheck = true;
		}

		final ChunkCoordIntPair[] var13 = structureCoords;
		final int var14 = var13.length;

		for (int var5 = 0; var5 < var14; ++var5) {
			final ChunkCoordIntPair var15 = var13[var5];

			if (p_75047_1_ == var15.chunkXPos && p_75047_2_ == var15.chunkZPos)
				return true;
		}

		return false;
	}

	@Override
	public String func_143025_a() {
		return "Stronghold";
	}

	/**
	 * Returns a list of other locations at which the structure generation has
	 * been run, or null if not relevant to this structure generator.
	 */
	@Override
	protected List getCoordList() {
		final ArrayList var1 = new ArrayList();
		final ChunkCoordIntPair[] var2 = structureCoords;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final ChunkCoordIntPair var5 = var2[var4];

			if (var5 != null) {
				var1.add(var5.func_151349_a(64));
			}
		}

		return var1;
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		MapGenStronghold.Start var3;

		for (var3 = new MapGenStronghold.Start(worldObj, rand, p_75049_1_,
				p_75049_2_); var3.getComponents().isEmpty()
				|| ((StructureStrongholdPieces.Stairs2) var3.getComponents()
						.get(0)).strongholdPortalRoom == null; var3 = new MapGenStronghold.Start(
				worldObj, rand, p_75049_1_, p_75049_2_)) {
			;
		}

		return var3;
	}
}
