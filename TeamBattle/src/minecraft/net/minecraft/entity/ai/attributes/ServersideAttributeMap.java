package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.management.LowerStringMap;

import com.google.common.collect.Sets;

public class ServersideAttributeMap extends BaseAttributeMap {
	private final Set attributeInstanceSet = Sets.newHashSet();
	protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();

	@Override
	public void addAttributeInstance(ModifiableAttributeInstance p_111149_1_) {
		if (p_111149_1_.getAttribute().getShouldWatch()) {
			attributeInstanceSet.add(p_111149_1_);
		}
	}

	@Override
	public ModifiableAttributeInstance getAttributeInstance(
			IAttribute p_111151_1_) {
		return (ModifiableAttributeInstance) super
				.getAttributeInstance(p_111151_1_);
	}

	@Override
	public ModifiableAttributeInstance getAttributeInstanceByName(
			String p_111152_1_) {
		IAttributeInstance var2 = super.getAttributeInstanceByName(p_111152_1_);

		if (var2 == null) {
			var2 = (IAttributeInstance) descriptionToAttributeInstanceMap
					.get(p_111152_1_);
		}

		return (ModifiableAttributeInstance) var2;
	}

	public Set getAttributeInstanceSet() {
		return attributeInstanceSet;
	}

	public Collection getWatchedAttributes() {
		final HashSet var1 = Sets.newHashSet();
		final Iterator var2 = getAllAttributes().iterator();

		while (var2.hasNext()) {
			final IAttributeInstance var3 = (IAttributeInstance) var2.next();

			if (var3.getAttribute().getShouldWatch()) {
				var1.add(var3);
			}
		}

		return var1;
	}

	/**
	 * Registers an attribute with this AttributeMap, returns a modifiable
	 * AttributeInstance associated with this map
	 */
	@Override
	public IAttributeInstance registerAttribute(IAttribute p_111150_1_) {
		if (attributesByName.containsKey(p_111150_1_
				.getAttributeUnlocalizedName()))
			throw new IllegalArgumentException(
					"Attribute is already registered!");
		else {
			final ModifiableAttributeInstance var2 = new ModifiableAttributeInstance(
					this, p_111150_1_);
			attributesByName.put(p_111150_1_.getAttributeUnlocalizedName(),
					var2);

			if (p_111150_1_ instanceof RangedAttribute
					&& ((RangedAttribute) p_111150_1_).getDescription() != null) {
				descriptionToAttributeInstanceMap.put(
						((RangedAttribute) p_111150_1_).getDescription(), var2);
			}

			attributes.put(p_111150_1_, var2);
			return var2;
		}
	}
}
