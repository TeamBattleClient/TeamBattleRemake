package net.minecraft.client.audio;

import java.util.Map;

import net.minecraft.util.RegistrySimple;

import com.google.common.collect.Maps;

public class SoundRegistry extends RegistrySimple {
	private Map field_148764_a;

	/**
	 * Creates the Map we will use to map keys to their registered values.
	 */
	@Override
	protected Map createUnderlyingMap() {
		field_148764_a = Maps.newHashMap();
		return field_148764_a;
	}

	public void func_148762_a(SoundEventAccessorComposite p_148762_1_) {
		putObject(p_148762_1_.func_148729_c(), p_148762_1_);
	}

	public void func_148763_c() {
		field_148764_a.clear();
	}
}
