package net.minecraft.world.gen.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase {
	private MapGenStructureData field_143029_e;

	/**
	 * Used to store a list of all structures that have been recursively
	 * generated. Used so that during recursive generation, the structure
	 * generator can avoid generating structures that intersect ones that have
	 * already been placed.
	 */
	protected Map structureMap = new HashMap();

	protected abstract boolean canSpawnStructureAtCoords(int p_75047_1_,
			int p_75047_2_);

	public boolean func_142038_b(int p_142038_1_, int p_142038_2_,
			int p_142038_3_) {
		func_143027_a(worldObj);
		final Iterator var4 = structureMap.values().iterator();
		StructureStart var5;

		do {
			if (!var4.hasNext())
				return false;

			var5 = (StructureStart) var4.next();
		} while (!var5.isSizeableStructure());

		return var5.getBoundingBox().intersectsWith(p_142038_1_, p_142038_3_,
				p_142038_1_, p_142038_3_);
	}

	public abstract String func_143025_a();

	private void func_143026_a(int p_143026_1_, int p_143026_2_,
			StructureStart p_143026_3_) {
		field_143029_e.func_143043_a(
				p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_),
				p_143026_1_, p_143026_2_);
		field_143029_e.markDirty();
	}

	private void func_143027_a(World p_143027_1_) {
		if (field_143029_e == null) {
			field_143029_e = (MapGenStructureData) p_143027_1_.loadItemData(
					MapGenStructureData.class, func_143025_a());

			if (field_143029_e == null) {
				field_143029_e = new MapGenStructureData(func_143025_a());
				p_143027_1_.setItemData(func_143025_a(), field_143029_e);
			} else {
				final NBTTagCompound var2 = field_143029_e.func_143041_a();
				final Iterator var3 = var2.func_150296_c().iterator();

				while (var3.hasNext()) {
					final String var4 = (String) var3.next();
					final NBTBase var5 = var2.getTag(var4);

					if (var5.getId() == 10) {
						final NBTTagCompound var6 = (NBTTagCompound) var5;

						if (var6.hasKey("ChunkX") && var6.hasKey("ChunkZ")) {
							final int var7 = var6.getInteger("ChunkX");
							final int var8 = var6.getInteger("ChunkZ");
							final StructureStart var9 = MapGenStructureIO
									.func_143035_a(var6, p_143027_1_);

							if (var9 != null) {
								structureMap.put(Long.valueOf(ChunkCoordIntPair
										.chunkXZ2Int(var7, var8)), var9);
							}
						}
					}
				}
			}
		}
	}

	protected StructureStart func_143028_c(int p_143028_1_, int p_143028_2_,
			int p_143028_3_) {
		final Iterator var4 = structureMap.values().iterator();

		while (var4.hasNext()) {
			final StructureStart var5 = (StructureStart) var4.next();

			if (var5.isSizeableStructure()
					&& var5.getBoundingBox().intersectsWith(p_143028_1_,
							p_143028_3_, p_143028_1_, p_143028_3_)) {
				final Iterator var6 = var5.getComponents().iterator();

				while (var6.hasNext()) {
					final StructureComponent var7 = (StructureComponent) var6
							.next();

					if (var7.getBoundingBox().isVecInside(p_143028_1_,
							p_143028_2_, p_143028_3_))
						return var5;
				}
			}
		}

		return null;
	}

	@Override
	protected final void func_151538_a(World p_151538_1_,
			final int p_151538_2_, final int p_151538_3_, int p_151538_4_,
			int p_151538_5_, Block[] p_151538_6_) {
		func_143027_a(p_151538_1_);

		if (!structureMap.containsKey(Long.valueOf(ChunkCoordIntPair
				.chunkXZ2Int(p_151538_2_, p_151538_3_)))) {
			rand.nextInt();

			try {
				if (canSpawnStructureAtCoords(p_151538_2_, p_151538_3_)) {
					final StructureStart var7 = getStructureStart(p_151538_2_,
							p_151538_3_);
					structureMap.put(Long.valueOf(ChunkCoordIntPair
							.chunkXZ2Int(p_151538_2_, p_151538_3_)), var7);
					func_143026_a(p_151538_2_, p_151538_3_, var7);
				}
			} catch (final Throwable var10) {
				final CrashReport var8 = CrashReport.makeCrashReport(var10,
						"Exception preparing structure feature");
				final CrashReportCategory var9 = var8
						.makeCategory("Feature being prepared");
				var9.addCrashSectionCallable("Is feature chunk",
						new Callable() {

							@Override
							public String call() {
								return MapGenStructure.this
										.canSpawnStructureAtCoords(p_151538_2_,
												p_151538_3_) ? "True" : "False";
							}
						});
				var9.addCrashSection(
						"Chunk location",
						String.format("%d,%d",
								new Object[] { Integer.valueOf(p_151538_2_),
										Integer.valueOf(p_151538_3_) }));
				var9.addCrashSectionCallable("Chunk pos hash", new Callable() {

					@Override
					public String call() {
						return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(
								p_151538_2_, p_151538_3_));
					}
				});
				var9.addCrashSectionCallable("Structure type", new Callable() {

					@Override
					public String call() {
						return MapGenStructure.this.getClass()
								.getCanonicalName();
					}
				});
				throw new ReportedException(var8);
			}
		}
	}

	public ChunkPosition func_151545_a(World p_151545_1_, int p_151545_2_,
			int p_151545_3_, int p_151545_4_) {
		worldObj = p_151545_1_;
		func_143027_a(p_151545_1_);
		rand.setSeed(p_151545_1_.getSeed());
		final long var5 = rand.nextLong();
		final long var7 = rand.nextLong();
		final long var9 = (p_151545_2_ >> 4) * var5;
		final long var11 = (p_151545_4_ >> 4) * var7;
		rand.setSeed(var9 ^ var11 ^ p_151545_1_.getSeed());
		func_151538_a(p_151545_1_, p_151545_2_ >> 4, p_151545_4_ >> 4, 0, 0,
				(Block[]) null);
		double var13 = Double.MAX_VALUE;
		ChunkPosition var15 = null;
		final Iterator var16 = structureMap.values().iterator();
		ChunkPosition var19;
		int var20;
		int var21;
		int var22;
		double var23;

		while (var16.hasNext()) {
			final StructureStart var17 = (StructureStart) var16.next();

			if (var17.isSizeableStructure()) {
				final StructureComponent var18 = (StructureComponent) var17
						.getComponents().get(0);
				var19 = var18.func_151553_a();
				var20 = var19.field_151329_a - p_151545_2_;
				var21 = var19.field_151327_b - p_151545_3_;
				var22 = var19.field_151328_c - p_151545_4_;
				var23 = var20 * var20 + var21 * var21 + var22 * var22;

				if (var23 < var13) {
					var13 = var23;
					var15 = var19;
				}
			}
		}

		if (var15 != null)
			return var15;
		else {
			final List var25 = getCoordList();

			if (var25 != null) {
				ChunkPosition var26 = null;
				final Iterator var27 = var25.iterator();

				while (var27.hasNext()) {
					var19 = (ChunkPosition) var27.next();
					var20 = var19.field_151329_a - p_151545_2_;
					var21 = var19.field_151327_b - p_151545_3_;
					var22 = var19.field_151328_c - p_151545_4_;
					var23 = var20 * var20 + var21 * var21 + var22 * var22;

					if (var23 < var13) {
						var13 = var23;
						var26 = var19;
					}
				}

				return var26;
			} else
				return null;
		}
	}

	/**
	 * Generates structures in specified chunk next to existing structures. Does
	 * *not* generate StructureStarts.
	 */
	public boolean generateStructuresInChunk(World p_75051_1_,
			Random p_75051_2_, int p_75051_3_, int p_75051_4_) {
		func_143027_a(p_75051_1_);
		final int var5 = (p_75051_3_ << 4) + 8;
		final int var6 = (p_75051_4_ << 4) + 8;
		boolean var7 = false;
		final Iterator var8 = structureMap.values().iterator();

		while (var8.hasNext()) {
			final StructureStart var9 = (StructureStart) var8.next();

			if (var9.isSizeableStructure()
					&& var9.getBoundingBox().intersectsWith(var5, var6,
							var5 + 15, var6 + 15)) {
				var9.generateStructure(p_75051_1_, p_75051_2_,
						new StructureBoundingBox(var5, var6, var5 + 15,
								var6 + 15));
				var7 = true;
				func_143026_a(var9.func_143019_e(), var9.func_143018_f(), var9);
			}
		}

		return var7;
	}

	/**
	 * Returns a list of other locations at which the structure generation has
	 * been run, or null if not relevant to this structure generator.
	 */
	protected List getCoordList() {
		return null;
	}

	protected abstract StructureStart getStructureStart(int p_75049_1_,
			int p_75049_2_);

	/**
	 * Returns true if the structure generator has generated a structure located
	 * at the given position tuple.
	 */
	public boolean hasStructureAt(int p_75048_1_, int p_75048_2_, int p_75048_3_) {
		func_143027_a(worldObj);
		return func_143028_c(p_75048_1_, p_75048_2_, p_75048_3_) != null;
	}
}
