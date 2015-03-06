package net.minecraft.util;

public class RegistryNamespacedDefaultedByKey extends RegistryNamespaced {
	private final String field_148760_d;
	private Object field_148761_e;

	public RegistryNamespacedDefaultedByKey(String p_i45127_1_) {
		field_148760_d = p_i45127_1_;
	}

	/**
	 * Adds a new object to this registry, keyed by both the given integer ID
	 * and the given string.
	 */
	@Override
	public void addObject(int p_148756_1_, String p_148756_2_,
			Object p_148756_3_) {
		if (field_148760_d.equals(p_148756_2_)) {
			field_148761_e = p_148756_3_;
		}

		super.addObject(p_148756_1_, p_148756_2_, p_148756_3_);
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return this.getObject((String) p_82594_1_);
	}

	@Override
	public Object getObject(String p_82594_1_) {
		final Object var2 = super.getObject(p_82594_1_);
		return var2 == null ? field_148761_e : var2;
	}

	/**
	 * Gets the object identified by the given ID.
	 */
	@Override
	public Object getObjectForID(int p_148754_1_) {
		final Object var2 = super.getObjectForID(p_148754_1_);
		return var2 == null ? field_148761_e : var2;
	}
}
