package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public abstract class StructureStart {
	protected StructureBoundingBox boundingBox;
	/** List of all StructureComponents that are part of this structure */
	protected LinkedList components = new LinkedList();
	private int field_143023_d;
	private int field_143024_c;

	public StructureStart() {
	}

	public StructureStart(int p_i43002_1_, int p_i43002_2_) {
		field_143024_c = p_i43002_1_;
		field_143023_d = p_i43002_2_;
	}

	public void func_143017_b(NBTTagCompound p_143017_1_) {
	}

	public int func_143018_f() {
		return field_143023_d;
	}

	public int func_143019_e() {
		return field_143024_c;
	}

	public void func_143020_a(World p_143020_1_, NBTTagCompound p_143020_2_) {
		field_143024_c = p_143020_2_.getInteger("ChunkX");
		field_143023_d = p_143020_2_.getInteger("ChunkZ");

		if (p_143020_2_.hasKey("BB")) {
			boundingBox = new StructureBoundingBox(
					p_143020_2_.getIntArray("BB"));
		}

		final NBTTagList var3 = p_143020_2_.getTagList("Children", 10);

		for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
			components.add(MapGenStructureIO.func_143032_b(
					var3.getCompoundTagAt(var4), p_143020_1_));
		}

		func_143017_b(p_143020_2_);
	}

	public NBTTagCompound func_143021_a(int p_143021_1_, int p_143021_2_) {
		final NBTTagCompound var3 = new NBTTagCompound();
		var3.setString("id", MapGenStructureIO.func_143033_a(this));
		var3.setInteger("ChunkX", p_143021_1_);
		var3.setInteger("ChunkZ", p_143021_2_);
		var3.setTag("BB", boundingBox.func_151535_h());
		final NBTTagList var4 = new NBTTagList();
		final Iterator var5 = components.iterator();

		while (var5.hasNext()) {
			final StructureComponent var6 = (StructureComponent) var5.next();
			var4.appendTag(var6.func_143010_b());
		}

		var3.setTag("Children", var4);
		func_143022_a(var3);
		return var3;
	}

	public void func_143022_a(NBTTagCompound p_143022_1_) {
	}

	/**
	 * Keeps iterating Structure Pieces and spawning them until the checks tell
	 * it to stop
	 */
	public void generateStructure(World p_75068_1_, Random p_75068_2_,
			StructureBoundingBox p_75068_3_) {
		final Iterator var4 = components.iterator();

		while (var4.hasNext()) {
			final StructureComponent var5 = (StructureComponent) var4.next();

			if (var5.getBoundingBox().intersectsWith(p_75068_3_)
					&& !var5.addComponentParts(p_75068_1_, p_75068_2_,
							p_75068_3_)) {
				var4.remove();
			}
		}
	}

	public StructureBoundingBox getBoundingBox() {
		return boundingBox;
	}

	public LinkedList getComponents() {
		return components;
	}

	/**
	 * currently only defined for Villages, returns true if Village has more
	 * than 2 non-road components
	 */
	public boolean isSizeableStructure() {
		return true;
	}

	/**
	 * offsets the structure Bounding Boxes up to a certain height, typically 63
	 * - 10
	 */
	protected void markAvailableHeight(World p_75067_1_, Random p_75067_2_,
			int p_75067_3_) {
		final int var4 = 63 - p_75067_3_;
		int var5 = boundingBox.getYSize() + 1;

		if (var5 < var4) {
			var5 += p_75067_2_.nextInt(var4 - var5);
		}

		final int var6 = var5 - boundingBox.maxY;
		boundingBox.offset(0, var6, 0);
		final Iterator var7 = components.iterator();

		while (var7.hasNext()) {
			final StructureComponent var8 = (StructureComponent) var7.next();
			var8.getBoundingBox().offset(0, var6, 0);
		}
	}

	protected void setRandomHeight(World p_75070_1_, Random p_75070_2_,
			int p_75070_3_, int p_75070_4_) {
		final int var5 = p_75070_4_ - p_75070_3_ + 1 - boundingBox.getYSize();
		int var10;

		if (var5 > 1) {
			var10 = p_75070_3_ + p_75070_2_.nextInt(var5);
		} else {
			var10 = p_75070_3_;
		}

		final int var7 = var10 - boundingBox.minY;
		boundingBox.offset(0, var7, 0);
		final Iterator var8 = components.iterator();

		while (var8.hasNext()) {
			final StructureComponent var9 = (StructureComponent) var8.next();
			var9.getBoundingBox().offset(0, var7, 0);
		}
	}

	/**
	 * Calculates total bounding box based on components' bounding boxes and
	 * saves it to boundingBox
	 */
	protected void updateBoundingBox() {
		boundingBox = StructureBoundingBox.getNewBoundingBox();
		final Iterator var1 = components.iterator();

		while (var1.hasNext()) {
			final StructureComponent var2 = (StructureComponent) var1.next();
			boundingBox.expandTo(var2.getBoundingBox());
		}
	}
}
