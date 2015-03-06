package net.minecraft.util;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class RegistryNamespaced extends RegistrySimple implements
		IObjectIntIterable {
	/**
	 * Ensures that the given name is indicated by a colon-delimited namespace,
	 * prepending "minecraft:" if it is not already.
	 */
	private static String ensureNamespaced(String p_148755_0_) {
		return p_148755_0_.indexOf(58) == -1 ? "minecraft:" + p_148755_0_
				: p_148755_0_;
	}

	protected final Map field_148758_b;

	/** The backing store that maps Integers to objects. */
	protected final ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();

	public RegistryNamespaced() {
		field_148758_b = ((BiMap) registryObjects).inverse();
	}

	/**
	 * Adds a new object to this registry, keyed by both the given integer ID
	 * and the given string.
	 */
	public void addObject(int p_148756_1_, String p_148756_2_,
			Object p_148756_3_) {
		underlyingIntegerMap.func_148746_a(p_148756_3_, p_148756_1_);
		putObject(ensureNamespaced(p_148756_2_), p_148756_3_);
	}

	/**
	 * Gets a value indicating whether this registry contains an object that can
	 * be identified by the given integer value
	 */
	public boolean containsID(int p_148753_1_) {
		return underlyingIntegerMap.func_148744_b(p_148753_1_);
	}

	/**
	 * Does this registry contain an entry for the given key?
	 */
	@Override
	public boolean containsKey(Object p_148741_1_) {
		return this.containsKey((String) p_148741_1_);
	}

	/**
	 * Does this registry contain an entry for the given key?
	 */
	public boolean containsKey(String p_148741_1_) {
		return super.containsKey(ensureNamespaced(p_148741_1_));
	}

	/**
	 * Creates the Map we will use to map keys to their registered values.
	 */
	@Override
	protected Map createUnderlyingMap() {
		return HashBiMap.create();
	}

	/**
	 * Gets the integer ID we use to identify the given object.
	 */
	public int getIDForObject(Object p_148757_1_) {
		return underlyingIntegerMap.func_148747_b(p_148757_1_);
	}

	/**
	 * Gets the name we use to identify the given object.
	 */
	public String getNameForObject(Object p_148750_1_) {
		return (String) field_148758_b.get(p_148750_1_);
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return this.getObject((String) p_82594_1_);
	}

	public Object getObject(String p_82594_1_) {
		return super.getObject(ensureNamespaced(p_82594_1_));
	}

	/**
	 * Gets the object identified by the given ID.
	 */
	public Object getObjectForID(int p_148754_1_) {
		return underlyingIntegerMap.func_148745_a(p_148754_1_);
	}

	@Override
	public Iterator iterator() {
		return underlyingIntegerMap.iterator();
	}
}
