package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.management.LowerStringMap;

import com.google.common.collect.Multimap;

public abstract class BaseAttributeMap {
	protected final Map attributes = new HashMap();
	protected final Map attributesByName = new LowerStringMap();

	public void addAttributeInstance(ModifiableAttributeInstance p_111149_1_) {
	}

	public void applyAttributeModifiers(Multimap p_111147_1_) {
		final Iterator var2 = p_111147_1_.entries().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();
			final IAttributeInstance var4 = getAttributeInstanceByName((String) var3
					.getKey());

			if (var4 != null) {
				var4.removeModifier((AttributeModifier) var3.getValue());
				var4.applyModifier((AttributeModifier) var3.getValue());
			}
		}
	}

	public Collection getAllAttributes() {
		return attributesByName.values();
	}

	public IAttributeInstance getAttributeInstance(IAttribute p_111151_1_) {
		return (IAttributeInstance) attributes.get(p_111151_1_);
	}

	public IAttributeInstance getAttributeInstanceByName(String p_111152_1_) {
		return (IAttributeInstance) attributesByName.get(p_111152_1_);
	}

	/**
	 * Registers an attribute with this AttributeMap, returns a modifiable
	 * AttributeInstance associated with this map
	 */
	public abstract IAttributeInstance registerAttribute(IAttribute p_111150_1_);

	public void removeAttributeModifiers(Multimap p_111148_1_) {
		final Iterator var2 = p_111148_1_.entries().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();
			final IAttributeInstance var4 = getAttributeInstanceByName((String) var3
					.getKey());

			if (var4 != null) {
				var4.removeModifier((AttributeModifier) var3.getValue());
			}
		}
	}
}
