package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
	/** The byte array stored in the tag. */
	private byte[] byteArray;

	NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] p_i45128_1_) {
		byteArray = p_i45128_1_;
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		final byte[] var1 = new byte[byteArray.length];
		System.arraycopy(byteArray, 0, var1, 0, byteArray.length);
		return new NBTTagByteArray(var1);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) ? Arrays.equals(byteArray,
				((NBTTagByteArray) p_equals_1_).byteArray) : false;
	}

	public byte[] func_150292_c() {
		return byteArray;
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_,
			NBTSizeTracker p_152446_3_) throws IOException {
		final int var4 = p_152446_1_.readInt();
		p_152446_3_.func_152450_a(8 * var4);
		byteArray = new byte[var4];
		p_152446_1_.readFully(byteArray);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return (byte) 7;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(byteArray);
	}

	@Override
	public String toString() {
		return "[" + byteArray.length + " bytes]";
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeInt(byteArray.length);
		p_74734_1_.write(byteArray);
	}
}
