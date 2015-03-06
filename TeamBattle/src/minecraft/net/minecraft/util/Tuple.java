package net.minecraft.util;

public class Tuple {
	/** First Object in the Tuple */
	private final Object first;

	/** Second Object in the Tuple */
	private final Object second;

	public Tuple(Object p_i1555_1_, Object p_i1555_2_) {
		first = p_i1555_1_;
		second = p_i1555_2_;
	}

	/**
	 * Get the first Object in the Tuple
	 */
	public Object getFirst() {
		return first;
	}

	/**
	 * Get the second Object in the Tuple
	 */
	public Object getSecond() {
		return second;
	}
}
