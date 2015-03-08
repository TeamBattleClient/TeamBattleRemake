package net.minecraft.client.settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

public class KeyBinding implements Comparable {
	private static final IntHashMap hash = new IntHashMap();

	private static final List keybindArray = new ArrayList();

	private static final Set keybindSet = new HashSet();

	public static Set func_151467_c() {
		return keybindSet;
	}

	public static void onTick(int p_74507_0_) {
		if (p_74507_0_ != 0) {
			final KeyBinding var1 = (KeyBinding) hash.lookup(p_74507_0_);

			if (var1 != null) {
				++var1.presses;
			}
		}
	}

	public static void resetKeyBindingArrayAndHash() {
		hash.clearMap();
		final Iterator var0 = keybindArray.iterator();

		while (var0.hasNext()) {
			final KeyBinding var1 = (KeyBinding) var0.next();
			hash.addKey(var1.keyCode, var1);
		}
	}

	public static void setKeyBindState(int p_74510_0_, boolean p_74510_1_) {
		if (p_74510_0_ != 0) {
			final KeyBinding var2 = (KeyBinding) hash.lookup(p_74510_0_);

			if (var2 != null) {
				var2.pressed = p_74510_1_;
			}
		}
	}

	public static void unPressAllKeys() {
		final Iterator var0 = keybindArray.iterator();

		while (var0.hasNext()) {
			final KeyBinding var1 = (KeyBinding) var0.next();
			var1.unpressKey();
		}
	}

	private final String keyCategory;

	private int keyCode;

	private final int keyCodeDefault;

	private final String keyDescription;

	/** because _303 wanted me to call it that(Caironater) */
	public boolean pressed;

	private int presses;

	public KeyBinding(String p_i45001_1_, int p_i45001_2_, String p_i45001_3_) {
		keyDescription = p_i45001_1_;
		keyCode = p_i45001_2_;
		keyCodeDefault = p_i45001_2_;
		keyCategory = p_i45001_3_;
		keybindArray.add(this);
		hash.addKey(p_i45001_2_, this);
		keybindSet.add(p_i45001_3_);
	}

	public int compareTo(KeyBinding p_compareTo_1_) {
		int var2 = I18n.format(keyCategory, new Object[0]).compareTo(
				I18n.format(p_compareTo_1_.keyCategory, new Object[0]));

		if (var2 == 0) {
			var2 = I18n.format(keyDescription, new Object[0]).compareTo(
					I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
		}

		return var2;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((KeyBinding) p_compareTo_1_);
	}

	public boolean getIsKeyPressed() {
		return pressed;
	}

	public String getKeyCategory() {
		return keyCategory;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public int getKeyCodeDefault() {
		return keyCodeDefault;
	}

	public String getKeyDescription() {
		return keyDescription;
	}

	public boolean isPressed() {
		if (presses == 0)
			return false;
		else {
			--presses;
			return true;
		}
	}

	public void setKeyCode(int p_151462_1_) {
		keyCode = p_151462_1_;
	}

	private void unpressKey() {
		presses = 0;
		pressed = false;
	}
}
