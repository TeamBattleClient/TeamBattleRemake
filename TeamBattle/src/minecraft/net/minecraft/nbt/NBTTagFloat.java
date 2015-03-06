package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.util.MathHelper;

public class NBTTagFloat extends NBTBase.NBTPrimitive {
	/** The float value for the tag. */
	private float data;

	NBTTagFloat() {
	}

	public NBTTagFloat(float p_i45131_1_) {
		data = p_i45131_1_;
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		return new NBTTagFloat(data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			final NBTTagFloat var2 = (NBTTagFloat) p_equals_1_;
			return data == var2.data;
		} else
			return false;
	}

	@Override
	public double func_150286_g() {
		return data;
	}

	@Override
	public int func_150287_d() {
		return MathHelper.floor_float(data);
	}

	@Override
	public float func_150288_h() {
		return data;
	}

	@Override
	public short func_150289_e() {
		return (short) (MathHelper.floor_float(data) & 65535);
	}

	@Override
	public byte func_150290_f() {
		return (byte) (MathHelper.floor_float(data) & 255);
	}

	@Override
	public long func_150291_c() {
		return (long) data;
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_,
			NBTSizeTracker p_152446_3_) throws IOException {
		p_152446_3_.func_152450_a(32L);
		data = p_152446_1_.readFloat();
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return (byte) 5;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Float.floatToIntBits(data);
	}

	@Override
	public String toString() {
		return "" + data + "f";
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		p_74734_1_.writeFloat(data);
	}
}
