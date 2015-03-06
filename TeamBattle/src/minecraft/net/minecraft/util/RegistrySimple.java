package net.minecraft.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

public class RegistrySimple implements IRegistry {
	private static final Logger logger = LogManager.getLogger();

	/** Objects registered on this registry. */
	protected final Map registryObjects = createUnderlyingMap();

	/**
	 * Does this registry contain an entry for the given key?
	 */
	public boolean containsKey(Object p_148741_1_) {
		return registryObjects.containsKey(p_148741_1_);
	}

	/**
	 * Creates the Map we will use to map keys to their registered values.
	 */
	protected Map createUnderlyingMap() {
		return Maps.newHashMap();
	}

	/**
	 * Gets all the keys recognized by this registry.
	 */
	public Set getKeys() {
		return Collections.unmodifiableSet(registryObjects.keySet());
	}

	@Override
	public Object getObject(Object p_82594_1_) {
		return registryObjects.get(p_82594_1_);
	}

	/**
	 * Register an object on this registry.
	 */
	@Override
	public void putObject(Object p_82595_1_, Object p_82595_2_) {
		if (registryObjects.containsKey(p_82595_1_)) {
			logger.debug("Adding duplicate key \'" + p_82595_1_
					+ "\' to registry");
		}

		registryObjects.put(p_82595_1_, p_82595_2_);
	}
}
