package net.minecraft.nbt;

public class NBTSizeTracker {
	public static final NBTSizeTracker field_152451_a = new NBTSizeTracker(0L) {

		@Override
		public void func_152450_a(long p_152450_1_) {
		}
	};
	private final long field_152452_b;
	private long field_152453_c;

	public NBTSizeTracker(long p_i1203_1_) {
		field_152452_b = p_i1203_1_;
	}

	public void func_152450_a(long p_152450_1_) {
		field_152453_c += p_152450_1_ / 8L;

		if (field_152453_c > field_152452_b)
			throw new RuntimeException(
					"Tried to read NBT tag that was too big; tried to allocate: "
							+ field_152453_c + "bytes where max allowed: "
							+ field_152452_b);
	}
}
