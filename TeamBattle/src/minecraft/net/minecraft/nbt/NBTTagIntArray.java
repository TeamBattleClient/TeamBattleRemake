package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {
	/** The array of saved integers */
	private int[] intArray;

	NBTTagIntArray() {
	}

	public NBTTagIntArray(int[] p_i45132_1_) {
		intArray = p_i45132_1_;
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		final int[] var1 = new int[intArray.length];
		System.arraycopy(intArray, 0, var1, 0, intArray.length);
		return new NBTTagIntArray(var1);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) ? Arrays.equals(intArray,
				((NBTTagIntArray) p_equals_1_).intArray) : false;
	}

	public int[] func_150302_c() {
		return intArray;
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_,
			NBTSizeTracker p_152446_3_) throws IOException {
		final int var4 = p_152446_1_.readInt();
		p_152446_3_.func_152450_a(32 * var4);
		intArray = new int[var4];

		for (int var5 = 0; var5 < var4; ++var5) {
			intArray[var5] = p_152446_1_.readInt();
		}
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return (byte) 11;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(intArray);
	}

	@Override
	public String toString() {
		String var1 = "[";
		final int[] var2 = intArray;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final int var5 = var2[var4];
			var1 = var1 + var5 + ",";
		}

		return var1 + "]";
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeInt(intArray.length);

		for (final int element : intArray) {
			p_74734_1_.writeInt(element);
		}
	}
}
