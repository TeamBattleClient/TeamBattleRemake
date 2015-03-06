package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
	/** The string value for the tag (cannot be empty). */
	private String data;

	public NBTTagString() {
		data = "";
	}

	public NBTTagString(String p_i1389_1_) {
		data = p_i1389_1_;

		if (p_i1389_1_ == null)
			throw new IllegalArgumentException("Empty string not allowed");
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		return new NBTTagString(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!super.equals(p_equals_1_))
			return false;
		else {
			final NBTTagString var2 = (NBTTagString) p_equals_1_;
			return data == null && var2.data == null || data != null
					&& data.equals(var2.data);
		}
	}

	@Override
	public String func_150285_a_() {
		return data;
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_,
			NBTSizeTracker p_152446_3_) throws IOException {
		data = p_152446_1_.readUTF();
		p_152446_3_.func_152450_a(16 * data.length());
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return (byte) 8;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ data.hashCode();
	}

	@Override
	public String toString() {
		return "\"" + data + "\"";
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeUTF(data);
	}
}
